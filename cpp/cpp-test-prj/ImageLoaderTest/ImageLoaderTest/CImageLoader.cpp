
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
// * 이미지를 손쉽게 로딩할 수 있게 하자.
//   기존에 RESOURCE 부분에 넣고 수정하고 그러는거 너무
//   불편하니깐...
//
// * GDI, GDI+ 모두 지원
//
// --------------------------------------------------------
// FIX : February 27, 2007
// --------------------------------------------------------
// * LENGTH 값 안넣은거 수정
// * GDI+ 에 메모리 해제 관련 수정
// 
// --------------------------------------------------------
// FIX : March 5, 2007
// --------------------------------------------------------
// * Memory leak 수정
// * 잘못된 방식의 메모리 할당 수정
//
// --------------------------------------------------------
// FIX : March 30, 2007
// --------------------------------------------------------
// * FindImageData 상에서 없는 element를 찾을 경우 오류
//   메시지를 발생하게 변경
//
// -----------------------------------------------------------
// NOTE : June 11, 2007 17:41:3
// -----------------------------------------------------------
// * CImageData 클래스에서 다음과 같은 맴버 함수를 추가했다.
//	  - GetCount() : 현재 클래스가 관리하고 있는 비트맵 수
//	  - GetImageSize() : 첫번째 이미지의 크기
//    - SetMemDC(CDC*,CDC*) : 메모리 비트맵을 쉽게 생성하기
// 
// * 추후 데이터 캡슐화를 위해 진행한다.
//

#include "StdAfx.h"
#include "CImageLoader.h"

CImageLoader::CImageLoader(void)
: m_bGdiPlus(false)
, m_bPreDelete(false)				// 이미지 정보를 미리 삭제해야할 경우
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
	// XML 파일일 로드한다.
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

	// ROOT 노드에 이름을 알아오자
	m_pElement->get_nodeName(&nodeName);
	TRACE(L"------------------------------------------\n");
	TRACE(L"ROOT NODE : %s\n", CString(nodeName));
	TRACE(L"------------------------------------------\n");

	// CHILD 를 위해서 준비중
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
		// 파일명이 없으면NULL을 입력해두자.		
		return;
	}

	// 파일이 존재하면 해당 파일을 읽어 들이자.
	// 이곳에서 첫 splash window 와 연동하여 로딩 파일을
	// 보여주면 좋을 것 같기도 하다! 추가적으로 구현해야할 부분이겠지
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

	// 원하는 것 찾고 리턴하기
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

	// 동적 할당한 이미지를 찾아서 지우고
	while (pos)
	{
		pImg = &m_list.GetNext(pos);

		for (i=0; i<pImg->m_nLength; ++i)
		{
			delete pImg->m_pBmp[i];
		}

		delete[] pImg->m_pBmp;
	}

	// 링크드리스트 지우자.
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

			// 이미지의 이름을 설정하고
			CImageData img;
			img.m_szName = CString(nodeChildName);

			// 해당 노드에 자식 노드를 검사해서 갯수 체크하고
			MSXML::IXMLDOMNodeList* nodeChild2;
			MSXML::IXMLDOMNode* listItem = NULL;
			m_hr = pChild->get_childNodes(&nodeChild2);

			if (SUCCEEDED(m_hr) && nodeChild2 != NULL)
			{
				long nLength = 0;
				nodeChild2->get_length(&nLength);
				TRACE(L"NAME %s, LENGTH :%ld\n", img.m_szName, nLength);

				// 해당 노드에 비트맵 수를 체크한 뒤에
				// 메모리를 할당한다.
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

				// 노드에 값인 이미지 경로를 가져와서 불러들이자.
				int i;
				TRACE(L"------------------------------------------\n");
				for (i=0; i<nLength; ++i)
				{
					listItem = NULL;
					m_hr = nodeChild2->get_item(i, &listItem);
					listItem->get_text(&nodeText);
					szNodeText = CString(nodeText);
					TRACE(L"NODE TEXT : %d : %s\n", i+1, szNodeText);

					// 이미지를 로딩 하자.
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
	// 원하는 것 찾고 리턴하기
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
		// 파일명이 없으면NULL을 입력해두자.		
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

			// 이미지의 이름을 설정하고
			CImageData img;
			img.m_szName = CString(nodeChildName);

			// 해당 노드에 자식 노드를 검사해서 갯수 체크하고
			MSXML::IXMLDOMNodeList* nodeChild2;
			MSXML::IXMLDOMNode* listItem = NULL;
			m_hr = pChild->get_childNodes(&nodeChild2);

			if (SUCCEEDED(m_hr) && nodeChild2 != NULL)
			{
				long nLength = 0;
				nodeChild2->get_length(&nLength);
				TRACE(L"NAME %s, LENGTH :%ld\n", img.m_szName, nLength);

				// 해당 노드에 비트맵 수를 체크한 뒤에
				// 메모리를 할당한다.
				img.m_pImg	  = new Image* [nLength];
				img.m_nLength = nLength;

				CString szNodeText;
				BSTR nodeText;

				// 노드에 값인 이미지 경로를 가져와서 불러들이자.
				int i;
				TRACE(L"------------------------------------------\n");
				for (i=0; i<nLength; ++i)
				{
					listItem  = NULL;
					m_hr	  = nodeChild2->get_item(i, &listItem);
					listItem->get_text(&nodeText);
					szNodeText = CString(nodeText);
					TRACE(L"NODE TEXT(%d) : %s\n", i+1, szNodeText);

					// 이미지를 로딩 하자.
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

	// 동적 할당한 이미지를 찾아서 지우고
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

	// 링크드리스트 지우자.
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
