/**
 * @file CFileFindEx.cpp
 * @author cheol-dong choi
 * @version 1.0
 * @date 2010-12-06 (14:00:24)
 * @section LICENSE
 *
 * @section DESCRIPTION
 *
 *
 * @section CHANGE_LOG
 */

#include "stdafx.h"
#include "CFileFindEx.h"
#include <assert.h>
#include <algorithm>

#define DIR_SEPERATOR		    '\\'
#define DELETE_POINTER(ptr)		if( ptr != NULL )	\
								{					\
								delete ptr;		\
								ptr = NULL;		\
								}


/*
 * Constructor
 */
CFileFindEx::CFileFindEx()
	: _hFileHandle(NULL) // initialize to NULL
	, _pfiledata(NULL)
	, _pNextdata(NULL)
{
	//TRACE(L"CFileFindEx::CFileFindEx");
}


/*
 * Destructor
 */
CFileFindEx::~CFileFindEx()
{
	//TRACE(L"CFileFindEx::~CFileFindEx");

	Close();
}


/**
 * 열려있는 파일 핸들을 닫는다.
 */
void CFileFindEx::Close()
{
	//TRACE(L"CFileFindEx::Close");

	DELETE_POINTER(_pfiledata);
	DELETE_POINTER(_pNextdata);

	if(_hFileHandle!= NULL && _hFileHandle != INVALID_HANDLE_VALUE)
	{
		::FindClose(_hFileHandle);

		_hFileHandle = NULL;
	}
}

bool CFileFindEx::GetFindNextFile()
{
	if (_hFileHandle == NULL)
	{
		return false;
	}

	if (_pfiledata == NULL)
	{
		_pfiledata = new WIN32_FIND_DATA;

		memset(_pfiledata, 0, sizeof(WIN32_FIND_DATA));
	}

	LPWIN32_FIND_DATA pTemp;

	pTemp      = _pfiledata;
	_pfiledata = _pNextdata;
	_pNextdata = pTemp;

	return ::FindNextFile(_hFileHandle, _pNextdata) ? true : false;
}

/**
 * 해당 위치의 데이터가 디렉토리인지 아닌지 판단 한다.
 *
 * @return true or false
 */
bool CFileFindEx::IsDirectory()
{
	//TRACE(L"CFileFindEx::IsDirectory");

	if (_pfiledata != NULL)
	{
		if (_pfiledata -> dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY)
			return true;
		else
			return false;
	}

	return false;
}

/**
 * FindFile wide char 버전
 *
 * @see FindFile
 * @param szPathName 
 * @return 
 */
bool CFileFindEx::FindFile(CString szPathName)
{
	//TRACE(L"CFileFindEx::FindFileW");

	Close();

	if(szPathName.GetLength() == 0)
	{
		ASSERT(0);
	}
	else
	{
		_csRoot = szPathName;
		int nPos = _csRoot.ReverseFind('\\');

		if( nPos == 0 )
			_csRoot = L'\\';
		else
			_csRoot = _csRoot.Left(nPos);
	}

	_pNextdata   = new WIN32_FIND_DATA;
	_hFileHandle = FindFirstFile(szPathName, _pNextdata);

	if (_hFileHandle == INVALID_HANDLE_VALUE)
	{
		Close();

		return false;
	}

	return true;
}


/**
 * GetFileName wide char 버전
 *
 * @see GetFileName
 * @return 
 */
CString CFileFindEx::GetFileName()
{
	//TRACE(L"CFileFindEx::GetFileNameW");

	if (_pfiledata != NULL)
	{
		return _pfiledata->cFileName;
	}

	return L"";
}


/**
 * GetFilePath wide char 버전
 *
 * @see GetFilePath
 * @return 
 */
CString CFileFindEx::GetFilePath()
{
	//TRACE(L"CFileFindEx::GetFilePathW");

	CString csResult = _csRoot;
	CString szLast;
	 
	szLast = csResult.Mid(csResult.GetLength() - 2, 2);

	if (szLast == L"/*" || szLast == L"\\*")
	{
		//csResult.resize(csResult.size() - 1);
		//csResult.Delete(csResult.GetLength() - 1);
		csResult = csResult.Left(csResult.GetLength() - 1);
	}

	csResult += L"\\";
	csResult += GetFileName();

	return csResult;
}


/**
 * GetAllList wide char 버전
 *
 * @see GetAllList
 * @param arrValue 
 * @param bFullPath 
 */
void CFileFindEx::GetAllList(std::vector<CString>& arrValue, bool bFullPath)
{
	//TRACE("CFileFindEx::GetAllListW\n");

	bool bRes = true;

	while (bRes)
	{
		bRes = GetFindNextFile();

		if ((_pfiledata->cFileName[0] != '.'))
		{
			if (IsDirectory())
			{
				CFileFindEx objWCE;
				CString szSubPath = GetFilePath();

				szSubPath += _T("\\*");

				if (!objWCE.FindFile(szSubPath))
				{
					continue;
				}

				objWCE.GetAllList(arrValue, bFullPath);
			}
			else
			{
				if (bFullPath)
					arrValue.push_back(GetFilePath());
				else
					arrValue.push_back(GetFileName());
			}
		}
	}

	//
	// true : sorting
	//
	if (_bAllListSort)
	{
		std::sort(arrValue.begin(), arrValue.end());
	}
}

void CFileFindEx::GetList(std::vector<CString>& arrValue, bool bFullPath)
{
	bool bRes = true;

	while (bRes)
	{
		bRes = GetFindNextFile();

		if ((_pfiledata->cFileName[0] != '.'))
		{
			if (!IsDirectory())
			{
				if (bFullPath)
					arrValue.push_back(GetFilePath());
				else
					arrValue.push_back(GetFileName());
			}
		}
	}

	//
	// true : sorting
	//
	if (_bAllListSort)
	{
		std::sort(arrValue.begin(), arrValue.end());
	}
}


/**
 * GetDenyList wide char 버전
 *
 * @see GetDenyList
 * @param arrValue 
 * @param bFullPath 
 */
void CFileFindEx::GetDenyList(std::vector<CString>& arrValue, bool bFullPath)
{
	//TRACE("CFileFindEx::GetDenyListW\n");

	bool bRes = true;

	while (bRes)
	{
		bRes = GetFindNextFile();

		if ((_pfiledata->cFileName[0] != '.'))
		{
			if (IsDirectory())
			{
				CFileFindEx objWCE;
				CString szSubPath = GetFilePath();

				szSubPath += _T("\\*");

				if (!objWCE.FindFile(szSubPath))
				{
					continue;
				}

				objWCE.AddDenyList(_arrDenyList);
				objWCE.GetDenyList(arrValue, bFullPath);
			}
			else
			{
				CString szFileName, szFileExtension;
				std::vector<CString>::iterator it;

				szFileName = bFullPath ? GetFilePath() : GetFileName();
				int nPos = szFileName.ReverseFind('.');

				if (nPos != -1)
				{
					szFileExtension = szFileName.Mid(nPos, szFileName.GetLength() - nPos);
					it = std::find(_arrDenyList.begin(), _arrDenyList.end(), szFileExtension);
				}
				else
				{
					it = std::find(_arrDenyList.begin(), _arrDenyList.end(), szFileName);
				}

				if (it == _arrDenyList.end())
				{
					arrValue.push_back(szFileName);
				}
			}
		}
	}

	//
	// true : sorting
	//

	if (_bAllListSort)
	{
		std::sort(arrValue.begin(), arrValue.end());
	}
}


/**
 * GetAllowList wide char 버전
 *
 * @see GetAllowList
 * @param arrValue 
 * @param bFullPath 
 */
void CFileFindEx::GetAllowList(std::vector<CString>& arrValue, bool bFullPath)
{
	//TRACE("CFileFindEx::GetAllowListW\n");

	bool bRes = true;

	while (bRes)
	{
		bRes = GetFindNextFile();

		if ((_pfiledata->cFileName[0] != '.'))
		{
			if (IsDirectory())
			{
				CFileFindEx objWCE;
				CString szSubPath = GetFilePath();

				szSubPath += _T("\\*");

				if (!objWCE.FindFile(szSubPath))
				{
					continue;
				}

				objWCE.AddAllowList(_arrAllowList);
				objWCE.GetAllowList(arrValue, bFullPath);
			}
			else
			{
				CString szFileName, szFileExtension;
				std::vector<CString>::iterator it;

				szFileName = bFullPath ? GetFilePath() : GetFileName();
				int nPos = szFileName.ReverseFind('.');

				if (nPos != -1)
				{
					szFileExtension = szFileName.Mid(nPos, szFileName.GetLength() - nPos);
					it = std::find(_arrAllowList.begin(), _arrAllowList.end(), szFileExtension);
				}
				else
				{
					it = std::find(_arrAllowList.begin(), _arrAllowList.end(), szFileName);
				}

				if (it != _arrAllowList.end())
				{
					arrValue.push_back(szFileName);
				}
			}
		}
	}

	//
	// true : sorting
	//

	if (_bAllListSort)
	{
		std::sort(arrValue.begin(), arrValue.end());
	}
}

