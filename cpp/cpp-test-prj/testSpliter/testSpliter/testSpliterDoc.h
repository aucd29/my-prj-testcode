
// testSpliterDoc.h : interface of the CtestSpliterDoc class
//


#pragma once


class CtestSpliterDoc : public CDocument
{
protected: // create from serialization only
	CtestSpliterDoc();
	DECLARE_DYNCREATE(CtestSpliterDoc)

// Attributes
public:

// Operations
public:

// Overrides
public:
	virtual BOOL OnNewDocument();
	virtual void Serialize(CArchive& ar);

// Implementation
public:
	virtual ~CtestSpliterDoc();
#ifdef _DEBUG
	virtual void AssertValid() const;
	virtual void Dump(CDumpContext& dc) const;
#endif

protected:

// Generated message map functions
protected:
	DECLARE_MESSAGE_MAP()
};


