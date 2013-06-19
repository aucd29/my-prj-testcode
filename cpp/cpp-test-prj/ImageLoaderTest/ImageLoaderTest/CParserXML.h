#ifndef __CD_CPARSERXML_H__
#define __CD_CPARSERXML_H__

//
// includes
//
#import <msxml.tlb>


class CParserXML
{
public:

	//
	// Construction
	//
	CParserXML();

	//
	// Destructor
	//
	virtual ~CParserXML();

public:

	//
	// Getter methods
	//
	int Load(LPCTSTR szFile);	

	CString GetContent(LPCTSTR szElementName);

	CString GetAttribute(LPCTSTR szElementName, LPCTSTR szAttributeName);

protected:

	//
	// Attributes
	//
	HRESULT _hr;

	IXMLDOMDocumentPtr _pDocument;

	IXMLDOMNodePtr _pNode;
};

#endif

//
// DATE : December 18, 2009 2:5:7
// CODER : Kurome(aucd29@gmail.com)
// URL : http://www.sarangnamu.net, http://blog.naver.com/kurome
// 
// Copyright kurome All rights reserved.
// 
// -----------------------------------------------------------
// NOTE : December 17, 2009 17:40:44
// -----------------------------------------------------------
// * 
//
