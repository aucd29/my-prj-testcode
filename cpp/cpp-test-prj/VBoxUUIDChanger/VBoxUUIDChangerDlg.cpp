// VBoxUUIDChangerDlg.cpp : 구현 파일
//

#include "stdafx.h"
#include "VBoxUUIDChanger.h"
#include "VBoxUUIDChangerDlg.h"
#include "CDRegKey.h"
#include <xstring>
#include <atlbase.h>

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// 응용 프로그램 정보에 사용되는 CAboutDlg 대화 상자입니다.

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// 대화 상자 데이터입니다.
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 지원입니다.

// 구현입니다.
protected:
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CDialog(CAboutDlg::IDD)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
END_MESSAGE_MAP()


// CVBoxUUIDChangerDlg 대화 상자




CVBoxUUIDChangerDlg::CVBoxUUIDChangerDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CVBoxUUIDChangerDlg::IDD, pParent)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CVBoxUUIDChangerDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CVBoxUUIDChangerDlg, CDialog)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	//}}AFX_MSG_MAP
	ON_BN_CLICKED(IDBTN_CHANGE, &CVBoxUUIDChangerDlg::OnBnClickedChange)
END_MESSAGE_MAP()


// CVBoxUUIDChangerDlg 메시지 처리기

BOOL CVBoxUUIDChangerDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// 시스템 메뉴에 "정보..." 메뉴 항목을 추가합니다.

	// IDM_ABOUTBOX는 시스템 명령 범위에 있어야 합니다.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		CString strAboutMenu;
		strAboutMenu.LoadString(IDS_ABOUTBOX);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// 이 대화 상자의 아이콘을 설정합니다. 응용 프로그램의 주 창이 대화 상자가 아닐 경우에는
	//  프레임워크가 이 작업을 자동으로 수행합니다.
	SetIcon(m_hIcon, TRUE);			// 큰 아이콘을 설정합니다.
	SetIcon(m_hIcon, FALSE);		// 작은 아이콘을 설정합니다.

	// TODO: 여기에 추가 초기화 작업을 추가합니다.

	return TRUE;  // 포커스를 컨트롤에 설정하지 않으면 TRUE를 반환합니다.
}

void CVBoxUUIDChangerDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

// 대화 상자에 최소화 단추를 추가할 경우 아이콘을 그리려면
//  아래 코드가 필요합니다. 문서/뷰 모델을 사용하는 MFC 응용 프로그램의 경우에는
//  프레임워크에서 이 작업을 자동으로 수행합니다.

void CVBoxUUIDChangerDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // 그리기를 위한 디바이스 컨텍스트

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// 클라이언트 사각형에서 아이콘을 가운데에 맞춥니다.
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// 아이콘을 그립니다.
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// 사용자가 최소화된 창을 끄는 동안에 커서가 표시되도록 시스템에서
//  이 함수를 호출합니다.
HCURSOR CVBoxUUIDChangerDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}


void CVBoxUUIDChangerDlg::OnBnClickedChange()
{
	CFileDialog m_ldFile(TRUE);

	// Set the current directory
	//m_ldFile.m_ofn.lpstrInitialDir = "C:\\Temp\\";

	// Show the File Open dialog and capture the result
	if (m_ldFile.DoModal() == IDOK)
	{
		CString szVdiPath;
		szVdiPath  = m_ldFile.GetPathName();
		TRACE(L"vdi path : %s\n", szVdiPath);

		bool bRes;
		CRegKey objReg;
		bRes = objReg.Open(HKEY_LOCAL_MACHINE, L"SOFTWARE\\Oracle\\VirtualBox", KEY_ALL_ACCESS);
		if (bRes != ERROR_SUCCESS )
		{
			objReg.Close();	
			AfxMessageBox(L"Vitual box 를 먼저 설치하3");

			return ;
		}

		TCHAR szBuffer[512];
		ULONG cchBuffer = 512;
		objReg.QueryStringValue(_T("InstallDir"), szBuffer, &cchBuffer);
		
		objReg.Close();
		AfxMessageBox(szBuffer);

		/*CDRegKey objReg;
		bRes = objReg.Open(HKEY_LOCAL_MACHINE, L"SOFTWARE\\Oracle\\VirtualBox");
		if (bRes == false)
		{
			objReg.Close();	
			AfxMessageBox(L"Vitual box 를 먼저 설치하3");

			return ;
		}*/
		
		CString szVBoxPath;
		//szVBoxPath  = objReg.ReadString(L"InstallDir").c_str();
		szVBoxPath  = szBuffer;
		szVBoxPath += L"VBoxManage.exe";
		TRACE(L"vbox path : %s\n", szVBoxPath);

		STARTUPINFO StartupInfo = {0};
		PROCESS_INFORMATION ProcessInfo;
		CString szOption;		
		szOption  = L" internalcommands setvdiuuid ";
		szOption += szVdiPath;

		TRACE(L"CMD : %s %s\n", szVBoxPath, szOption);

		//szVBoxPath += szOption;

		if(!::CreateProcess((LPWSTR)(LPCTSTR)szVBoxPath, (LPWSTR)(LPCTSTR)szOption, NULL, NULL, FALSE, CREATE_SUSPENDED, NULL, NULL, &StartupInfo, &ProcessInfo))
		//if(!::CreateProcess((LPWSTR)(LPCTSTR)szVBoxPath, NULL, NULL, NULL, FALSE, CREATE_SUSPENDED, NULL, NULL, &StartupInfo, &ProcessInfo))
		{
			AfxMessageBox(L"ERROR!!\n");
			return ;
		}

		AfxMessageBox(L"변경됨!!\n");
	} 
}

