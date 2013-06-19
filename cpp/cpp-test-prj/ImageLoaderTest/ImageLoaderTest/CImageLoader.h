#pragma once

#include "CParserXML.h"
#include <afxtempl.h>

class CImageData
{
public:
	CImageData()
		: m_pImg(NULL)
		, m_pBmp(NULL)
		, m_szName(L"")
		, m_nLength(0)
	{

	}

	CString m_szName;
	CBitmap** m_pBmp;
	int m_nLength;

public:
	int GetCount(void)
	{
		return m_nLength;
	}

	void GetImageSize(BITMAP& bm)
	{
		m_pBmp[0]->GetObject(sizeof(BITMAP), &bm);
	}

	void SetMemDC(CDC* pDC, CDC* pMDC)
	{
		int i;
		for (i=0; i<m_nLength; ++i)		
		{
			pMDC->CreateCompatibleDC(pDC);
			pMDC->SelectObject(m_pBmp[i]);
			pMDC++;
		}
	}

	// GDI PLUS를 셋팅 했을 때에만 적용된다.
#ifdef _GDIPLUS_H
	Image** m_pImg;
#endif;
};

class CImageLoader :
	public CParserXML
{
public:
	CImageLoader(void);
	~CImageLoader(void);

protected:
	CString m_szDir;
	bool m_bGdiPlus, m_bPreDelete;
	CList<CImageData, CImageData&> m_list;

protected:
	void ReadData(void);
	void LoadImages(CImageData* pImg, int nIndex, CString szPath);
	void DeleteImages(void);

#ifdef _GDIPLUS_H
	void DeleteImagesGdiplus(void);
	void LoadImagesGdiplus(CImageData* pImg, int nIndex, CString szPath);
#endif;

public:	
	int GetCount(void);
	int GetTargetCount(CString szName);	
	void LoadXML(CString szDir, CString szFile, bool bGdiPlus = false);
	void FindChildNode(MSXML::IXMLDOMNode* pChild);
	void ErrorLoad(void);
	void PreviousDelete(void);
	CPoint GetImageInfo(CBitmap* pBmp);
	CBitmap* FindBitmap(CString szName);
	CImageData* FindImageData(CString szName);

#ifdef _GDIPLUS_H
	Image** FindImages(CString szName);
	void FindChildNodeGdiPlus(MSXML::IXMLDOMNode* pChild);
#endif;
};
