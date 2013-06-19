
// backupEmoticonDlg.h : header file
//

#pragma once
#include "afxwin.h"


// CbackupEmoticonDlg dialog
class CbackupEmoticonDlg : public CDialogEx
{
// Construction
public:
	CbackupEmoticonDlg(CWnd* pParent = NULL);	// standard constructor

// Dialog Data
	enum { IDD = IDD_BACKUPEMOTICON_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support


// Implementation
protected:
	HICON m_hIcon;

	// Generated message map functions
	virtual BOOL OnInitDialog();
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedBackup();
	afx_msg void OnBnClickedRestore();
private:
	CString getEmoticonPath(void);
public:
	CButton _backup;
	CButton _restore;
};
