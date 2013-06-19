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
	// ���ϴ� XML ��ҿ� �����͸� �����´�
	// Ʈ�� �����̹Ƿ� Ʈ���� ������ '//' �� �� �� �ִ�
	//
	// ��
	// <normal>
	//		<center>test</center>
	// </normal>
	// �� ��������� GetContent ������
	//
	// "//normal//center" �� �ش� ��ġ�� �����͸�
	//
	// ������ �� �ִ�.
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

