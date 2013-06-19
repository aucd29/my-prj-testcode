
// ISOConvertorDlg.cpp : ���� ����
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


// CISOConvertorDlg ��ȭ ����




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


// CISOConvertorDlg �޽��� ó����

BOOL CISOConvertorDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// �� ��ȭ ������ �������� �����մϴ�. ���� ���α׷��� �� â�� ��ȭ ���ڰ� �ƴ� ��쿡��
	//  �����ӿ�ũ�� �� �۾��� �ڵ����� �����մϴ�.
	SetIcon(m_hIcon, TRUE);			// ū �������� �����մϴ�.
	SetIcon(m_hIcon, FALSE);		// ���� �������� �����մϴ�.

	// TODO: ���⿡ �߰� �ʱ�ȭ �۾��� �߰��մϴ�.

	return TRUE;  // ��Ŀ���� ��Ʈ�ѿ� �������� ������ TRUE�� ��ȯ�մϴ�.
}

// ��ȭ ���ڿ� �ּ�ȭ ���߸� �߰��� ��� �������� �׸�����
//  �Ʒ� �ڵ尡 �ʿ��մϴ�. ����/�� ���� ����ϴ� MFC ���� ���α׷��� ��쿡��
//  �����ӿ�ũ���� �� �۾��� �ڵ����� �����մϴ�.

void CISOConvertorDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // �׸��⸦ ���� ����̽� ���ؽ�Ʈ

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// Ŭ���̾�Ʈ �簢������ �������� ����� ����ϴ�.
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// �������� �׸��ϴ�.
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// ����ڰ� �ּ�ȭ�� â�� ���� ���ȿ� Ŀ���� ǥ�õǵ��� �ý��ۿ���
//  �� �Լ��� ȣ���մϴ�.
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
	BrInfo.lpszTitle = L"�ٿ�ε� ��μ���"; 
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
		AfxMessageBox(L"��θ� ���� �����ϼ���");

		return ;
	}

	TRACE("THREAD TEST\n");

	WaitForSingleObject(_hThread, INFINITE);

	//1. �ٿ�ε� ���� zip������ Ǯ�� Oscdimg.exe �� C:\Windows\System32 ������ ���� �� �ٿ��ֱ� �մϴ�. 
	//2. ���� - ���� - cmd ������ Ȯ�� (������ ���� �����ؾ� �մϴ�.)
	//3. �Ʒ��� ���� �Է��մϴ�.
	//Oscdimg.exe -u2 -bC:\<exe���Ϸ� ���� Ǯ�� ����> \expandedSetup\boot\etfsboot.com -h C:\<exe���Ϸ� ����Ǯ�� ����> \expandedSetup C:\<exe���Ϸ� ���� Ǯ�� ����> \Win7.iso 
	//����: Oscdimg.exe -u2 -bC:\Users\James\Downloads\expandedSetup\boot\etfsboot.com -h C:\ Users\James\Downloads \expandedSetup C:\ Users\James\Downloads \Win7.iso
	//4. �̷��� �ؼ� ������� ������ Win7.iso�Դϴ�. ���� �����ϼ̴� ���� ���� ����̴ϴ�..
	//5. ������ DVD ���� ���� �̿��ؼ� ������� ISO ������ �����ϴ�. 
	//6. ������� DVD�� ������ �����ϰ� �缳ġ �غ��ñ� �ٶ��ϴ� ^^

	_hThread = AfxBeginThread(RunThread, this);
}

UINT CISOConvertorDlg::RunThread(void* lParam)
{
	CISOConvertorDlg* pDlg = (CISOConvertorDlg*)lParam;

	pDlg->DisableAllButton(false);

	CString szComamnd, szPath = pDlg->GetPath();

	if (_waccess(szPath + "\\expandedSetup\\boot\\etfsboot.com", 0) == -1)
	{		
		AfxMessageBox(L"expandedSetup\\boot\\etfsboot.com ������ ã�� �� �����ϴ�.\n��θ� �ٽ� �������ּ���.");
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
		AfxMessageBox(L"�̹� ������ �����մϴ�.");
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
	szResult = L"������ �����Ǿ����ϴ�.\n(";
	szResult += szPath;
	szResult += "\\Windows7.iso)";
	AfxMessageBox(szResult);

	return 0;
}