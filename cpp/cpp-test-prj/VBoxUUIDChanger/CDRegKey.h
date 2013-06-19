#ifndef __CD_CDREGKEY_H__
#define __CD_CDREGKEY_H__

//
// includes
//
#include "stdafx.h"
#include <xstring>


class CDRegKey
{
public:

	//
	// Construction
	//
	CDRegKey();

	//
	// Destructor
	//
	virtual ~CDRegKey();

public:

	//
	// Getter methods
	//
	bool Open(HKEY hRoot, const wchar_t* szPath);

	bool Create(HKEY hRoot, const wchar_t* szPath);

	DWORD ReadInt(const wchar_t* szKey);

	std::wstring ReadString(const wchar_t* szKey);

	bool CompareInt(const wchar_t* szKey, DWORD dwValue);

	bool CompareString(const wchar_t* szKey, const wchar_t* szValue);

	bool WriteInt(const wchar_t* szKey, DWORD dwValue);

	bool WriteString(const wchar_t* szKey, const wchar_t* szValue);

	bool DeleteKey(HKEY hKey, const wchar_t* szPath);

	bool DeleteValue(const wchar_t* szPath);

	void Close();

	HKEY GetHKey();

	std::wstring GetErrorMessage();

protected:

	//
	// Attributes
	//
	HKEY _hKey;

	UINT _nResult;
};

#endif

//
// DATE : December 4, 2009 11:42:36
// CODER : kurome(cdchoi@minigate.net)
// URL : http://www.minigate.net
//
// Copyright Minigate Ltd. All rights reserved.
//
// -----------------------------------------------------------
// NOTE : December 4, 2009 11:42:36
// -----------------------------------------------------------
// *
//
// -----------------------------------------------------------
// SAMPLE CODE
// -----------------------------------------------------------
//
// CDRegKey objKey;
// objKey.Create(HKEY_LOCAL_MACHINE, L"System\\GDI\\FontAlias\\");
// objKey.Close();
//
// CDRegKey objKey;
// wchar_t szRegPath[512] = {0};
//
// wcscpy(szRegPath, L"Software\\Microsoft\\Today\\Items\\");
// wcscat(szRegPath, szPath);
// LOG1(L"regKey: %s\n", szRegPath);
// bResult = objKey.Open(HKEY_LOCAL_MACHINE, szRegPath);
//
// if (bResult == false)
// {
//     objKey.Close();
//
//     return bResult;
// }
//
// if (objKey.ReadInt(L"Enabled") == 0)
// {
//     LOG1("The key value is 0, The key is passed\n");
//
//     objKey.Close();
//
//     return false;
// }
//
// LOG2(L"Changing the key value (%s)\n", szPath);
// objKey.WriteInt(L"Enabled", 0);
// objKey.Close();
//
// CDRegKey objKey;
// objKey.Open(HKEY_LOCAL_MACHINE, L"System\\GDI\\FontAlias\\");
// objKey.DeleteValue(L"±¼¸²Ã¼");
// objKey.DeleteValue(L"±¼¸²");
// objKey.DeleteValue(L"Tahoma");
// objKey.DeleteValue(L"Nina");
// objKey.DeleteValue(L"GulimChe");
// objKey.DeleteValue(L"Gulim");
// objKey.Close();
//
//
//