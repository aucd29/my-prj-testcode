// CParserXML.cpp

//
// includes
//
#include "stdafx.h"
#include "CParserXML.h"

#pragma warning(disable:4996)


//
// CParserXML
//

CParserXML::CParserXML()
	: _pDocument(NULL)
	, _pNode(NULL)
{

}

//
// ~CParserXML
//

CParserXML::~CParserXML()
{
	if (_pDocument)
		CoInitialize(NULL);
}


//
// Load
//

int CParserXML::Load(LPCTSTR szFile)
{
	//
	// Method description
	// -------
	// XML LOAD
	//
	_hr = CoInitialize(NULL);

	try
	{
		if (FAILED(_hr)) throw _T("CoInitialize Fail");

		_hr = _pDocument.CreateInstance(__uuidof(DOMDocument));

		if (FAILED(_hr)) throw _T("CreateInstance Fail");

		_pDocument->put_async(VARIANT_FALSE);

		VARIANT_BOOL varStatus = VARIANT_FALSE;
		_pDocument->load(CComVariant(szFile), &varStatus);

		if (varStatus != VARIANT_TRUE) throw _T("Failed to load");
		
		TRACE(_T("Path : '%s'\n"), szFile);
	}	
	catch (const TCHAR* e)
	{
		AfxMessageBox(e);

		return false;
	}

	return false;
}


//
// GetAttributeData
//

CString CParserXML::GetContent(LPCTSTR szElementName)
{
	//
	// Method description
	// -------
	// 원하는 XML 요소에 데이터를 가져온다
	// 트리 구조이므로 트리에 구분은 '//' 로 할 수 있다
	//
	// 예
	// <normal>
	//		<center>test</center>
	// </normal>
	// 의 구조라면은 GetContent 에서는
	//
	// "//normal//center" 로 해당 위치에 데이터를
	//
	// 가져올 수 있다.
	//

	CString szNodeValue;
	HRESULT hr;

	hr = _pDocument->selectSingleNode(CComBSTR(szElementName), &_pNode);

	if (_pNode != NULL)
	{
		BSTR bszNodeValue = NULL;
		
		_pNode->get_text(&bszNodeValue);		
		szNodeValue.Format(_T("%s"), bszNodeValue);
		SysFreeString(bszNodeValue);
	}

	if (_pNode != NULL)
		_pNode.Release();

	return szNodeValue;
}




//
// DATE : December 18, 2009 2:5:1
// CODER : Kurome(aucd29@gmail.com)
// URL : http://www.sarangnamu.net, http://blog.naver.com/kurome
// 
// Copyright kurome All rights reserved.
// 
//
// MSDN Manual
// -----------------------------------------------------------
// http://msdn.microsoft.com/en-us/library/ms765465(VS.85).aspx
//
//
// XML Reference
// --------------------------------------------------------
// Link : http://www.w3.org
//

