
// testSpliterDoc.cpp : implementation of the CtestSpliterDoc class
//

#include "stdafx.h"
#include "testSpliter.h"

#include "testSpliterDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CtestSpliterDoc

IMPLEMENT_DYNCREATE(CtestSpliterDoc, CDocument)

BEGIN_MESSAGE_MAP(CtestSpliterDoc, CDocument)
END_MESSAGE_MAP()


// CtestSpliterDoc construction/destruction

CtestSpliterDoc::CtestSpliterDoc()
{
	// TODO: add one-time construction code here

}

CtestSpliterDoc::~CtestSpliterDoc()
{
}

BOOL CtestSpliterDoc::OnNewDocument()
{
	if (!CDocument::OnNewDocument())
		return FALSE;

	// TODO: add reinitialization code here
	// (SDI documents will reuse this document)

	return TRUE;
}




// CtestSpliterDoc serialization

void CtestSpliterDoc::Serialize(CArchive& ar)
{
	if (ar.IsStoring())
	{
		// TODO: add storing code here
	}
	else
	{
		// TODO: add loading code here
	}
}


// CtestSpliterDoc diagnostics

#ifdef _DEBUG
void CtestSpliterDoc::AssertValid() const
{
	CDocument::AssertValid();
}

void CtestSpliterDoc::Dump(CDumpContext& dc) const
{
	CDocument::Dump(dc);
}
#endif //_DEBUG


// CtestSpliterDoc commands
