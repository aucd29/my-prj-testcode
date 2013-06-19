
// backupEmoticonDlg.cpp : implementation file
//

#include "stdafx.h"
#include "backupEmoticon.h"
#include "backupEmoticonDlg.h"
#include "afxdialogex.h"

#include "SystemInfo.h"
#include "CFileFindEx.h"
#include "ZipManager.h"

//#include <afxdlgs.h>
#include <vector>

#define NATEON_PATH _T("\\SK Communications\\NATEON")

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CbackupEmoticonDlg dialog




CbackupEmoticonDlg::CbackupEmoticonDlg(CWnd* pParent /*=NULL*/)
	: CDialogEx(CbackupEmoticonDlg::IDD, pParent)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CbackupEmoticonDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
	DDX_Control(pDX, IDB_BACKUP, _backup);
	DDX_Control(pDX, IDB_RESTORE, _restore);
}

BEGIN_MESSAGE_MAP(CbackupEmoticonDlg, CDialogEx)
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_BN_CLICKED(IDB_BACKUP, &CbackupEmoticonDlg::OnBnClickedBackup)
	ON_BN_CLICKED(IDB_RESTORE, &CbackupEmoticonDlg::OnBnClickedRestore)
END_MESSAGE_MAP()


// CbackupEmoticonDlg message handlers

BOOL CbackupEmoticonDlg::OnInitDialog()
{
	CDialogEx::OnInitDialog();

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	// TODO: Add extra initialization here

	return TRUE;  // return TRUE  unless you set the focus to a control
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CbackupEmoticonDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialogEx::OnPaint();
	}
}

// The system calls this function to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CbackupEmoticonDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}



void CbackupEmoticonDlg::OnBnClickedBackup()
{
	_backup.EnableWindow(FALSE);

	CString path = getEmoticonPath();
	if (path.GetLength() == 0)
	{
		_backup.EnableWindow(TRUE);
		return ;
	}

	COleDateTime currentTime = COleDateTime::GetCurrentTime();
	CString currentDate = currentTime.Format(L"emoticon_%Y%m%d%H%M%S.zip"); // "yyyy/mm/dd" 이런 형식으로 문자열로 출력
	TRACE(L"FILENAME : %s\n", currentDate);

	ZipManager::compress(currentDate, path);

	AfxMessageBox(L"OK", MB_OK);
	_backup.EnableWindow(TRUE);
}


void CbackupEmoticonDlg::OnBnClickedRestore()
{
	_restore.EnableWindow(FALSE);
	CString path = getEmoticonPath();
	if (path.GetLength() == 0)
	{
		_restore.EnableWindow(TRUE);
		return ;
	}

	TCHAR szFilters[]= _T("Backup File (*.zip)|*.zip||");
	
	TCHAR szCurDir[MAX_PATH];
	GetCurrentDirectory(MAX_PATH, szCurDir);   //현재 디렉터리 저장
	CFileDialog fileDlg(TRUE, NULL, NULL, OFN_FILEMUSTEXIST | OFN_HIDEREADONLY | OFN_PATHMUSTEXIST, szFilters);
	fileDlg.m_ofn.lpstrInitialDir = szCurDir;

	if (fileDlg.DoModal() == IDOK)
	{
		CString pathName = fileDlg.GetPathName();
		// Implement opening and reading file in here.
		//Change the window's title to the opened file's title.
		
		TRACE(L"GET FILE NAME : %s\n", pathName);
		ZipManager::decompress(pathName, path);

		AfxMessageBox(L"OK", MB_OK);
	}
	else
	{
		TRACE(L"CANCEL\n");
	}

	_restore.EnableWindow(TRUE);
}


CString CbackupEmoticonDlg::getEmoticonPath(void)
{
	CString fullpath = SystemInfo::getLocalAppDataPath();
	fullpath += NATEON_PATH;
	TRACE(L"FULL PATH : " + fullpath);

	CFileFindEx dir;
	std::vector<CString> dirs;
    if (!dir.FindFile(fullpath))
    {
		return _T("");
	}

	dir.AddAllowList(_T(".sm"));
	dir.GetAllowList(dirs, true);
	TRACE(_T(", COUNT (in) : %d\n"), dirs.size());

	CString emoticonPath;
	for (int i=0; i<dirs.size(); ++i)
	{
		if (dirs[i].Find(L"emoticons_u") > 0)
		{
			int last = dirs[i].ReverseFind('\\');
			TRACE(L"DIR1 : %s\n", dirs[i]);
			emoticonPath = dirs[i].Left(last);
			TRACE(L"DIR2 : %s\n", emoticonPath);
			break;
		}
	}

	return emoticonPath;
}
