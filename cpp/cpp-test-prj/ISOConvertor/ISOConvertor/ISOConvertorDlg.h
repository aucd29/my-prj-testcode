
// ISOConvertorDlg.h : ��� ����
//

#pragma once


// CISOConvertorDlg ��ȭ ����
class CISOConvertorDlg : public CDialog
{
// �����Դϴ�.
public:
	CISOConvertorDlg(CWnd* pParent = NULL);	// ǥ�� �������Դϴ�.

// ��ȭ ���� �������Դϴ�.
	enum { IDD = IDD_ISOCONVERTOR_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV �����Դϴ�.


// �����Դϴ�.
protected:
	HICON m_hIcon;

	// ������ �޽��� �� �Լ�
	virtual BOOL OnInitDialog();
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()

private:
	CString _szPath;
	HANDLE _hThread;

public:
	static UINT RunThread(void* lParam);
	CString GetPath() { return _szPath; }
	void DisableAllButton(bool bState);

public:
	afx_msg void OnBnClickedCommand1();
	afx_msg void OnBnClickedCommand3();
};
