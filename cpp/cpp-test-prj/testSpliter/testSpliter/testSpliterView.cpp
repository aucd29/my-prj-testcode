
// testSpliterView.cpp : implementation of the CtestSpliterView class
//

#include "stdafx.h"
#include "testSpliter.h"

#include "testSpliterDoc.h"
#include "testSpliterView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CtestSpliterView

IMPLEMENT_DYNCREATE(CtestSpliterView, CFormView)

BEGIN_MESSAGE_MAP(CtestSpliterView, CFormView)
END_MESSAGE_MAP()

// CtestSpliterView construction/destruction

CtestSpliterView::CtestSpliterView()
	: CFormView(CtestSpliterView::IDD)
{
	// TODO: add construction code here

}

CtestSpliterView::~CtestSpliterView()
{
}

void CtestSpliterView::DoDataExchange(CDataExchange* pDX)
{
	CFormView::DoDataExchange(pDX);
}

BOOL CtestSpliterView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs

	return CFormView::PreCreateWindow(cs);
}

void CtestSpliterView::OnInitialUpdate()
{
	CFormView::OnInitialUpdate();
	GetParentFrame()->RecalcLayout();
	ResizeParentToFit();

}

void CtestSpliterView::OnRButtonUp(UINT nFlags, CPoint point)
{
	ClientToScreen(&point);
	OnContextMenu(this, point);
}

void CtestSpliterView::OnContextMenu(CWnd* pWnd, CPoint point)
{
	theApp.GetContextMenuManager()->ShowPopupMenu(IDR_POPUP_EDIT, point.x, point.y, this, TRUE);
}


// CtestSpliterView diagnostics

#ifdef _DEBUG
void CtestSpliterView::AssertValid() const
{
	CFormView::AssertValid();
}

void CtestSpliterView::Dump(CDumpContext& dc) const
{
	CFormView::Dump(dc);
}

CtestSpliterDoc* CtestSpliterView::GetDocument() const // non-debug version is inline
{
	ASSERT(m_pDocument->IsKindOf(RUNTIME_CLASS(CtestSpliterDoc)));
	return (CtestSpliterDoc*)m_pDocument;
}
#endif //_DEBUG


// CtestSpliterView message handlers
