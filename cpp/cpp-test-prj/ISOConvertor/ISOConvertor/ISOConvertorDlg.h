
// ISOConvertorDlg.h : 헤더 파일
//

#pragma once


// CISOConvertorDlg 대화 상자
class CISOConvertorDlg : public CDialog
{
// 생성입니다.
public:
	CISOConvertorDlg(CWnd* pParent = NULL);	// 표준 생성자입니다.

// 대화 상자 데이터입니다.
	enum { IDD = IDD_ISOCONVERTOR_DIALOG };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV 지원입니다.


// 구현입니다.
protected:
	HICON m_hIcon;

	// 생성된 메시지 맵 함수
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
