/**
 * @file CFileFind.h
 * @author cheol-dong choi 
 * @version 1.0
 * @date 
 * @section LICENSE
 *
 * @section DESCRIPTION
 *
 *
 * @section CHANGE_LOG
 * 
 */


#ifndef __CD_CFileFindWinCE_H__
#define __CD_CFileFindWinCE_H__

#include <Windows.h>
#include <vector>
#include <xstring>


/**
 * @class CFileFindWinCE 
 * @brief 파일 리스트를 얻는 클래스
 */
class CFileFindEx
{
public:

	/*
	 * Constructor
	 */
	CFileFindEx();


	/*
	 * Destructor
	 */
	virtual ~CFileFindEx();

public:

	/*
	 * Getter methods
	 */
	bool IsDirectory();

	bool GetFindNextFile();

	bool FindFile(CString szPathName);

	CString GetFileName();

	CString GetFilePath();

	void GetAllList(std::vector<CString>& arrValue, bool bFullPath);

	void GetList(std::vector<CString>& arrValue, bool bFullPath);

	void GetDenyList(std::vector<CString>& arrValue, bool bFullPath);

	void GetAllowList(std::vector<CString>& arrValue, bool bFullPath);

	void Close();

	void SetAllListSort(bool bSort)
	{
		_bAllListSort = bSort;
	}

	void AddAllowList(CString szName)
	{
		_arrAllowList.push_back(szName);
	}

	void AddAllowList(std::vector<CString>& arrList)
	{
		_arrAllowList = arrList;
	}

	void AddDenyList(CString szName)
	{
		_arrDenyList.push_back(szName);
	}

	void AddDenyList(std::vector<CString>& arrList)
	{
		_arrDenyList = arrList;
	}

private:

	/*
	 * Attributes
	 */
	LPWIN32_FIND_DATA   _pfiledata;

	LPWIN32_FIND_DATA   _pNextdata;

	HANDLE              _hFileHandle;

	CString        _csRoot;

	bool _bAllListSort;

	std::vector<CString> _arrAllowList;

	std::vector<CString> _arrDenyList;
};

#endif
