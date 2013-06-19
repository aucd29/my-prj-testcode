
// DATE		: February 22, 2007
// CODER	: Kurome (ccd@apsat.co.kr)
// URL		: http://www.apsat.co.kr
//
//
// Copyright Asia pasific satellite industries Co., Ltd.
//

// Require
// --------------------------------------------------------
// CParserXML
// http://www.sarangnamu.net/basic/basic_view.php?no=2825
//

// --------------------------------------------------------
// NOTE	: February 22, 2007
// --------------------------------------------------------
// * �̹����� �ս��� �ε��� �� �ְ� ����.
//   ������ RESOURCE �κп� �ְ� �����ϰ� �׷��°� �ʹ�
//   �����ϴϱ�...
//
// * GDI, GDI+ ��� ����
//
// --------------------------------------------------------
// FIX : February 27, 2007
// --------------------------------------------------------
// * LENGTH �� �ȳ����� ����
// * GDI+ �� �޸� ���� ���� ����
// 
// --------------------------------------------------------
// FIX : March 5, 2007
// --------------------------------------------------------
// * Memory leak ����
// * �߸��� ����� �޸� �Ҵ� ����
//
// --------------------------------------------------------
// FIX : March 30, 2007
// --------------------------------------------------------
// * FindImageData �󿡼� ���� element�� ã�� ��� ����
//   �޽����� �߻��ϰ� ����
//
// -----------------------------------------------------------
// NOTE : June 11, 2007 17:41:3
// -----------------------------------------------------------
// * CImageData Ŭ�������� ������ ���� �ɹ� �Լ��� �߰��ߴ�.
//	  - GetCount() : ���� Ŭ������ �����ϰ� �ִ� ��Ʈ�� ��
//	  - GetImageSize() : ù��° �̹����� ũ��
//    - SetMemDC(CDC*,CDC*) : �޸� ��Ʈ���� ���� �����ϱ�
// 
// * ���� ������ ĸ��ȭ�� ���� �����Ѵ�.
//

#include "StdAfx.h"
#include "CImageLoader.h"

CImageLoader::CImageLoader(void)
: m_bGdiPlus(false)
, m_bPreDelete(false)				// �̹��� ������ �̸� �����ؾ��� ���
{
}

CImageLoader::~CImageLoader(void)
{
	if (m_bGdiPlus)
	{
#ifdef _GDIPLUS_H
		DeleteImagesGdiplus();
#endif;
	}
	else
	{
		if (!m_bPreDelete)
		{
			DeleteImages();
		}
	}
}

void CImageLoader::LoadXML(CString szDir, CString szFile, bool bGdiPlus)
{
	// XML ������ �ε��Ѵ�.
	m_szDir	   = szDir;
	m_bGdiPlus = bGdiPlus;
	Load(szDir + L"/" + szFile);
	ReadData();
}

void CImageLoader::ReadData(void)
{
	BSTR nodeName;

	m_hr = m_pDocument->get_documentElement(&m_pElement);
	if (FAILED(m_hr) || m_pElement == NULL)
	{
		AfxMessageBox(L"Empty document!");
	}

	// ROOT ��忡 �̸��� �˾ƿ���
	m_pElement->get_nodeName(&nodeName);
	TRACE(L"------------------------------------------\n");
	TRACE(L"ROOT NODE : %s\n", CString(nodeName));
	TRACE(L"------------------------------------------\n");

	// CHILD �� ���ؼ� �غ���
	MSXML::IXMLDOMNode* pChild = NULL;
	m_hr = m_pElement->get_firstChild(&pChild);

	if (!m_bGdiPlus)
	{
		FindChildNode(pChild);
	}
	else 
	{
#ifdef _GDIPLUS_H
		FindChildNodeGdiPlus(pChild);
#endif;
	}

	SysFreeString(nodeName);
}

void CImageLoader::LoadImages(CImageData* pImg, int nIndex, CString szPath)
{
	if (szPath.GetLength() == 0)
	{
		// ���ϸ��� ������NULL�� �Է��ص���.		
		return;
	}

	// ������ �����ϸ� �ش� ������ �о� ������.
	// �̰����� ù splash window �� �����Ͽ� �ε� ������
	// �����ָ� ���� �� ���⵵ �ϴ�! �߰������� �����ؾ��� �κ��̰���
	//
	pImg->m_pBmp[nIndex] = new CBitmap;
	if (!pImg->m_pBmp[nIndex]->Attach((HBITMAP)LoadImage(NULL,m_szDir + L"/" + szPath,IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE)))
	{
		CString szError;

		szError.Format(L"Error : File Not Found (%s) <ImageLoader>", m_szDir + L"/" + szPath);
		AfxMessageBox(szError);
		exit(1);
	}
}

CBitmap* CImageLoader::FindBitmap(CString szName)
{
	ErrorLoad();

	// ���ϴ� �� ã�� �����ϱ�
	POSITION pos = m_list.GetHeadPosition();
	CImageData* pImg;

	while (pos)
	{
		pImg = &m_list.GetNext(pos);

		if (pImg->m_szName == szName)
		{
			return pImg->m_pBmp[0];
		}
	}

	CString szError;
	szError.Format(L"Element Not Found (%s) <ImageLoader>", szName);
	AfxMessageBox(szError);

	return NULL;
}

void CImageLoader::DeleteImages(void)
{
	TRACE(L"DELETE BITMAP\n");
	POSITION pos = m_list.GetHeadPosition();
	CImageData *pImg;
	int i = 0;

	// ���� �Ҵ��� �̹����� ã�Ƽ� �����
	while (pos)
	{
		pImg = &m_list.GetNext(pos);

		for (i=0; i<pImg->m_nLength; ++i)
		{
			delete pImg->m_pBmp[i];
		}

		delete[] pImg->m_pBmp;
	}

	// ��ũ�帮��Ʈ ������.
	m_list.RemoveAll();	
}

void CImageLoader::FindChildNode(MSXML::IXMLDOMNode* pChild)
{
	BSTR nodeType, nodeChildName;

	while (true)
	{
		pChild->get_nodeTypeString(&nodeType);

		if (!wcscmp(nodeType, L"element"))
		{
			TRACE(L"ELEMENT ");
			pChild->get_nodeName(&nodeChildName);

			// �̹����� �̸��� �����ϰ�
			CImageData img;
			img.m_szName = CString(nodeChildName);

			// �ش� ��忡 �ڽ� ��带 �˻��ؼ� ���� üũ�ϰ�
			MSXML::IXMLDOMNodeList* nodeChild2;
			MSXML::IXMLDOMNode* listItem = NULL;
			m_hr = pChild->get_childNodes(&nodeChild2);

			if (SUCCEEDED(m_hr) && nodeChild2 != NULL)
			{
				long nLength = 0;
				nodeChild2->get_length(&nLength);
				TRACE(L"NAME %s, LENGTH :%ld\n", img.m_szName, nLength);

				// �ش� ��忡 ��Ʈ�� ���� üũ�� �ڿ�
				// �޸𸮸� �Ҵ��Ѵ�.
				if (nLength == 1)
				{
					img.m_pBmp = new CBitmap*;
				}
				else 
				{
					img.m_pBmp = new CBitmap* [nLength];
				}

				img.m_nLength = nLength;

				CString szNodeText;
				BSTR nodeText;

				// ��忡 ���� �̹��� ��θ� �����ͼ� �ҷ�������.
				int i;
				TRACE(L"------------------------------------------\n");
				for (i=0; i<nLength; ++i)
				{
					listItem = NULL;
					m_hr = nodeChild2->get_item(i, &listItem);
					listItem->get_text(&nodeText);
					szNodeText = CString(nodeText);
					TRACE(L"NODE TEXT : %d : %s\n", i+1, szNodeText);

					// �̹����� �ε� ����.
					LoadImages(&img, i, szNodeText);
				}
				TRACE(L"------------------------------------------\n");

				m_list.AddTail(img);
				SysFreeString(nodeText);
			}
		}
		/*else if (!wcscmp(nodeType, L"text"))
		{
		TRACE(L"text\n");
		}
		else if (!wcscmp(nodeType, L"comment"))
		{
		TRACE(L"comment\n");
		}
		else
		{
		TRACE(L"others\n");
		}*/

		m_hr = pChild->get_nextSibling(&pChild);

		if (FAILED(m_hr) || pChild == NULL)
		{
			break;
		}
	}

	SysFreeString(nodeType);
	SysFreeString(nodeChildName);
}

int CImageLoader::GetCount(void)
{
	return static_cast<int>(m_list.GetCount());
}

int CImageLoader::GetTargetCount(CString szName)
{
	POSITION pos = m_list.GetHeadPosition();
	CImageData* pImg;

	while (pos)
	{
		pImg = &m_list.GetNext(pos);

		if (pImg->m_szName == szName)
		{
			return pImg->m_nLength;
		}
	}

	return 0;
}

CPoint CImageLoader::GetImageInfo(CBitmap* pBmp)
{
	BITMAP bm;
	pBmp->GetObject(sizeof(BITMAP), &bm); 

	return CPoint(bm.bmWidth, bm.bmHeight);
}

CImageData* CImageLoader::FindImageData(CString szName)
{
	ErrorLoad();

	POSITION pos = m_list.GetHeadPosition();
	CImageData* pImg;

	while (pos)
	{
		pImg = &m_list.GetNext(pos);

		if (pImg->m_szName == szName)
		{
			return pImg;
		}
	}

	CString szError;
	szError.Format(L"Element Not Found (%s) <ImageLoader>", szName);
	AfxMessageBox(szError);

	return NULL;
}

#ifdef _GDIPLUS_H
Image** CImageLoader::FindImages(CString szName)
{
	ErrorLoad();
	// ���ϴ� �� ã�� �����ϱ�
	POSITION pos = m_list.GetHeadPosition();
	CImageData* pImg;

	while (pos)
	{
		pImg = &m_list.GetNext(pos);

		if (pImg->m_szName == szName)
		{
			return pImg->m_pImg;
		}
	}

	CString szError;
	szError.Format(L"Element Not Found (%s) <ImageLoader>", szName);
	AfxMessageBox(szError);

	return NULL;
}

void CImageLoader::LoadImagesGdiplus(CImageData* pImg, int nIndex, CString szPath)
{
	if (szPath.GetLength() == 0)
	{
		// ���ϸ��� ������NULL�� �Է��ص���.		
		return;
	}

	pImg->m_pImg[nIndex] = NULL;
	pImg->m_pImg[nIndex] = Image::FromFile(m_szDir + L"/" + szPath);
	if (pImg->m_pImg[nIndex] == NULL)
	{
		CString szError;

		szError.Format(L"Error : File Not Found (%s) <ImageLoader plus>", m_szDir + L"/" + szPath);
		AfxMessageBox(szError);
		exit(1);
	}
}

void CImageLoader::FindChildNodeGdiPlus(MSXML::IXMLDOMNode* pChild)
{
	BSTR nodeType, nodeChildName;

	while (true)
	{
		pChild->get_nodeTypeString(&nodeType);

		if (!wcscmp(nodeType, L"element"))
		{
			TRACE(L"ELEMENT ");
			pChild->get_nodeName(&nodeChildName);

			// �̹����� �̸��� �����ϰ�
			CImageData img;
			img.m_szName = CString(nodeChildName);

			// �ش� ��忡 �ڽ� ��带 �˻��ؼ� ���� üũ�ϰ�
			MSXML::IXMLDOMNodeList* nodeChild2;
			MSXML::IXMLDOMNode* listItem = NULL;
			m_hr = pChild->get_childNodes(&nodeChild2);

			if (SUCCEEDED(m_hr) && nodeChild2 != NULL)
			{
				long nLength = 0;
				nodeChild2->get_length(&nLength);
				TRACE(L"NAME %s, LENGTH :%ld\n", img.m_szName, nLength);

				// �ش� ��忡 ��Ʈ�� ���� üũ�� �ڿ�
				// �޸𸮸� �Ҵ��Ѵ�.
				img.m_pImg	  = new Image* [nLength];
				img.m_nLength = nLength;

				CString szNodeText;
				BSTR nodeText;

				// ��忡 ���� �̹��� ��θ� �����ͼ� �ҷ�������.
				int i;
				TRACE(L"------------------------------------------\n");
				for (i=0; i<nLength; ++i)
				{
					listItem  = NULL;
					m_hr	  = nodeChild2->get_item(i, &listItem);
					listItem->get_text(&nodeText);
					szNodeText = CString(nodeText);
					TRACE(L"NODE TEXT(%d) : %s\n", i+1, szNodeText);

					// �̹����� �ε� ����.
					LoadImagesGdiplus(&img, i, szNodeText);
				}
				TRACE(L"------------------------------------------\n");

				m_list.AddTail(img);
				SysFreeString(nodeText);
			}
		}

		m_hr = pChild->get_nextSibling(&pChild);
		if (FAILED(m_hr) || pChild == NULL)
		{
			break;
		}
	}

	SysFreeString(nodeType);
	SysFreeString(nodeChildName);
}

void CImageLoader::DeleteImagesGdiplus(void)
{
	POSITION pos = m_list.GetHeadPosition();
	CImageData* pImg;

	// ���� �Ҵ��� �̹����� ã�Ƽ� �����
	while (pos)
	{
		pImg = &m_list.GetNext(pos);

		TRACE(L"DELETE : %s %d\n", pImg->m_szName, pImg->m_nLength);
		
		if (pImg->m_nLength == 1)
		{
			delete pImg->m_pImg;
		}
		else
		{
			delete[] pImg->m_pImg;
		}
	}

	// ��ũ�帮��Ʈ ������.
	m_list.RemoveAll();
}

#endif;

void CImageLoader::ErrorLoad(void)
{
	if (!m_szDir.GetLength())
	{
		AfxMessageBox(L"ERROR : NOT LOADING XML <ImageLoader> path:'" + m_szDir + "'");
		exit(1);
	}
}

void CImageLoader::PreviousDelete(void)
{
	m_bPreDelete = true;
	DeleteImages();
}
