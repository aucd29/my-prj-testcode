
// ISOConvertorDlg.cpp : 구현 파일
//

#include "stdafx.h"
#include "ISOConvertor.h"
#include "ISOConvertorDlg.h"

#include <afxdlgs.h>
#include  <stdlib.h>
#include  <io.h>

//#include "ProgressDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CISOConvertorDlg 대화 상자




CISOConvertorDlg::CISOConvertorDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CISOConvertorDlg::IDD, pParent)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
	_hThread = NULL;
}

void CISOConvertorDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CISOConvertorDlg, CDialog)
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	//}}AFX_MSG_MAP
	ON_BN_CLICKED(IDC_BUTTON1, &CISOConvertorDlg::OnBnClickedCommand1)
	ON_BN_CLICKED(IDC_BUTTON2, &CISOConvertorDlg::OnBnClickedCommand3)
END_MESSAGE_MAP()


// CISOConvertorDlg 메시지 처리기

BOOL CISOConvertorDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// 이 대화 상자의 아이콘을 설정합니다. 응용 프로그램의 주 창이 대화 상자가 아닐 경우에는
	//  프레임워크가 이 작업을 자동으로 수행합니다.
	SetIcon(m_hIcon, TRUE);			// 큰 아이콘을 설정합니다.
	SetIcon(m_hIcon, FALSE);		// 작은 아이콘을 설정합니다.

	// TODO: 여기에 추가 초기화 작업을 추가합니다.

	return TRUE;  // 포커스를 컨트롤에 설정하지 않으면 TRUE를 반환합니다.
}

// 대화 상자에 최소화 단추를 추가할 경우 아이콘을 그리려면
//  아래 코드가 필요합니다. 문서/뷰 모델을 사용하는 MFC 응용 프로그램의 경우에는
//  프레임워크에서 이 작업을 자동으로 수행합니다.

void CISOConvertorDlg::OnPaint()
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
HCURSOR CISOConvertorDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

void CISOConvertorDlg::OnBnClickedCommand1()
{
	ITEMIDLIST *pidlBrowse; 
	wchar_t pszPathname[256] = {0}; 

	BROWSEINFO BrInfo; 
	BrInfo.hwndOwner = GetSafeHwnd(); 
	BrInfo.pidlRoot = NULL; 
	memset( &BrInfo, 0, sizeof( BrInfo ) ); 
	BrInfo.pszDisplayName = pszPathname; 
	BrInfo.lpszTitle = L"다운로드 경로설정"; 
	BrInfo.ulFlags = BIF_RETURNONLYFSDIRS; 
	pidlBrowse = ::SHBrowseForFolder( &BrInfo ); 

	if (pidlBrowse == NULL)
	{
		return ;
	}

	SHGetPathFromIDList( pidlBrowse, pszPathname ); 

	TRACE(L"FULL PATH %s\n", pszPathname);

	_szPath = pszPathname;

	wchar_t szShortPath[256] = {0};
	GetShortPathName(pszPathname, szShortPath, MAX_PATH);
	SetDlgItemText(IDSTC_PATH_CONTENT, szShortPath);

	TRACE(L"SHORT PATH %s\n", szShortPath);

	UpdateWindow();
}

void CISOConvertorDlg::DisableAllButton(bool bState)
{
	GetDlgItem(IDC_BUTTON1)->EnableWindow(bState);
	GetDlgItem(IDC_BUTTON3)->EnableWindow(bState);
	GetDlgItem(IDCANCEL)->EnableWindow(bState);
}

void CISOConvertorDlg::OnBnClickedCommand3()
{
	if (!_szPath.GetLength())
	{
		AfxMessageBox(L"경로를 먼저 설정하세요");

		return ;
	}

	TRACE("THREAD TEST\n");

	WaitForSingleObject(_hThread, INFINITE);

	//1. 다운로드 받은 zip파일을 풀고 Oscdimg.exe 을 C:\Windows\System32 폴더에 복사 후 붙여넣기 합니다. 
	//2. 시작 - 실행 - cmd 누르고 확인 (관리자 모드로 실행해야 합니다.)
	//3. 아래와 같이 입력합니다.
	//Oscdimg.exe -u2 -bC:\<exe파일로 압축 풀린 폴더> \expandedSetup\boot\etfsboot.com -h C:\<exe파일로 압축풀린 폴더> \expandedSetup C:\<exe파일로 압축 풀린 폴더> \Win7.iso 
	//예시: Oscdimg.exe -u2 -bC:\Users\James\Downloads\expandedSetup\boot\etfsboot.com -h C:\ Users\James\Downloads \expandedSetup C:\ Users\James\Downloads \Win7.iso
	//4. 이렇게 해서 만들어진 파일이 Win7.iso입니다. 위에 지정하셨던 폴더 내에 생길겁니다..
	//5. 갖고계신 DVD 버닝 툴을 이용해서 만들어진 ISO 파일을 굽습니다. 
	//6. 만들어진 DVD를 가지고 깨끗하게 재설치 해보시기 바랍니다 ^^

	_hThread = AfxBeginThread(RunThread, this);
}

UINT CISOConvertorDlg::RunThread(void* lParam)
{
	CISOConvertorDlg* pDlg = (CISOConvertorDlg*)lParam;

	pDlg->DisableAllButton(false);

	CString szComamnd, szPath = pDlg->GetPath();

	if (_waccess(szPath + "\\expandedSetup\\boot\\etfsboot.com", 0) == -1)
	{		
		AfxMessageBox(L"expandedSetup\\boot\\etfsboot.com 파일을 찾을 수 없습니다.\n경로를 다시 지정해주세요.");
		pDlg->DisableAllButton(true);
		return 0;
	}

	szComamnd  = L"oscdimg.exe -u2 -b";
	szComamnd += szPath;
	szComamnd += L"\\expandedSetup\\boot\\etfsboot.com -h ";
	szComamnd += szPath;
	szComamnd += L"\\expandedSetup ";
	szComamnd += szPath;
	szComamnd += L"\\Windows7.iso";

	if (_waccess(szPath + L"\\Windows7.iso", 0) != -1)
	{
		AfxMessageBox(L"이미 파일이 존재합니다.");
		pDlg->DisableAllButton(true);

		return 0;
	}

	TRACE(L"CMD: %s\n", szComamnd);

	char szSystemCmd[2048] = {0};
	WideCharToMultiByte(CP_ACP, 0, szComamnd, -1, szSystemCmd, szComamnd.GetLength(), NULL, NULL);

	TRACE("CMD: %s\n", szSystemCmd);

	system(szSystemCmd);	

	pDlg->DisableAllButton(true);

	CString szResult;
	szResult = L"파일이 생성되었습니다.\n(";
	szResult += szPath;
	szResult += "\\Windows7.iso)";
	AfxMessageBox(szResult);

	return 0;
}