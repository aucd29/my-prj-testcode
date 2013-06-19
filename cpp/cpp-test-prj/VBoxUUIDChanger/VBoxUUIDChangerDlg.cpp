// VBoxUUIDChangerDlg.cpp : ���� ����
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


// ���� ���α׷� ������ ���Ǵ� CAboutDlg ��ȭ �����Դϴ�.

class CAboutDlg : public CDialog
{
public:
	CAboutDlg();

// ��ȭ ���� �������Դϴ�.
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV �����Դϴ�.

// �����Դϴ�.
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


// CVBoxUUIDChangerDlg ��ȭ ����




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


// CVBoxUUIDChangerDlg �޽��� ó����

BOOL CVBoxUUIDChangerDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// �ý��� �޴��� "����..." �޴� �׸��� �߰��մϴ�.

	// IDM_ABOUTBOX�� �ý��� ��� ������ �־�� �մϴ�.
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

	// �� ��ȭ ������ �������� �����մϴ�. ���� ���α׷��� �� â�� ��ȭ ���ڰ� �ƴ� ��쿡��
	//  �����ӿ�ũ�� �� �۾��� �ڵ����� �����մϴ�.
	SetIcon(m_hIcon, TRUE);			// ū �������� �����մϴ�.
	SetIcon(m_hIcon, FALSE);		// ���� �������� �����մϴ�.

	// TODO: ���⿡ �߰� �ʱ�ȭ �۾��� �߰��մϴ�.

	return TRUE;  // ��Ŀ���� ��Ʈ�ѿ� �������� ������ TRUE�� ��ȯ�մϴ�.
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

// ��ȭ ���ڿ� �ּ�ȭ ���߸� �߰��� ��� �������� �׸�����
//  �Ʒ� �ڵ尡 �ʿ��մϴ�. ����/�� ���� ����ϴ� MFC ���� ���α׷��� ��쿡��
//  �����ӿ�ũ���� �� �۾��� �ڵ����� �����մϴ�.

void CVBoxUUIDChangerDlg::OnPaint()
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
			AfxMessageBox(L"Vitual box �� ���� ��ġ��3");

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
			AfxMessageBox(L"Vitual box �� ���� ��ġ��3");

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

		AfxMessageBox(L"�����!!\n");
	} 
}

