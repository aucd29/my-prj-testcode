
// testSpliterView.h : interface of the CtestSpliterView class
//


#pragma once


class CtestSpliterView : public CFormView
{
protected: // create from serialization only
	CtestSpliterView();
	DECLARE_DYNCREATE(CtestSpliterView)

public:
	enum{ IDD = IDD_TESTSPLITER_FORM };

// Attributes
public:
	CtestSpliterDoc* GetDocument() const;

// Operations
public:

// Overrides
public:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	virtual void OnInitialUpdate(); // called first time after construct

// Implementation
public:
	virtual ~CtestSpliterView();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	afx_msg void OnFilePrintPreview();
	afx_msg void OnRButtonUp(UINT nFlags, CPoint point);
	afx_msg void OnContextMenu(CWnd* pWnd, CPoint point);
	DECLARE_MESSAGE_MAP()
};

#ifndef _DEBUG  // debug version in testSpliterView.cpp
inline CtestSpliterDoc* CtestSpliterView::GetDocument() const
   { return reinterpret_cast<CtestSpliterDoc*>(m_pDocument); }
#endif

