// CDRegKey.cpp

//
// includes
//
#include "CDRegKey.h"
#include "RSLOGGER.H"


#define MAX_KEY_LENGTH 255
#define MAX_VALUE_NAME 16383

//
// CDRegKey
//

CDRegKey::CDRegKey()
	: _hKey(NULL)
	, _nResult(0)
{

}


//
// ~CDRegKey
//

CDRegKey::~CDRegKey()
{
	if (_hKey)
		Close();
}


//
// Open
//

bool CDRegKey::Open(HKEY hRoot, const wchar_t* szPath)
{
	LOG1("CDRegKey::Open\n");

	//
	// Method description
	// -------
	// Open registry key
	//

	_nResult = RegOpenKeyEx(hRoot, szPath, 0, KEY_ALL_ACCESS, &_hKey);

	if(ERROR_SUCCESS != _nResult)
	{
		LOG2("ERROR::CDRegKey::Open <Fail RegOpenKeyEx>\n");

		TCHAR szBuf[80]; 
		LPVOID lpMsgBuf;
		DWORD dw = GetLastError(); 

		FormatMessage(
			FORMAT_MESSAGE_ALLOCATE_BUFFER | 
			FORMAT_MESSAGE_FROM_SYSTEM,
			NULL,
			dw,
			MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
			(LPTSTR) &lpMsgBuf,
			0, NULL );

		wsprintf(szBuf, 
			L"%s failed with error %d: %s", 
			L"Open", dw, lpMsgBuf); 

		MessageBox(NULL, szBuf, L"Error", MB_OK); 

		LocalFree(lpMsgBuf);
		//ExitProcess(dw); 


		return false;
	}

	return true;
}


//
// Create
//

bool CDRegKey::Create(HKEY hRoot, const wchar_t* szPath)
{
	LOG1("CDRegKey::Create\n");

	//
	// Method description
	// -------
	// create key
	//

	DWORD dwDisposition = REG_CREATED_NEW_KEY;

	_nResult = RegCreateKeyEx(hRoot, szPath, 0, NULL, REG_OPTION_NON_VOLATILE, KEY_ALL_ACCESS, 0, &_hKey, &dwDisposition);

	if(ERROR_SUCCESS != _nResult)
	{
		LOG2("ERROR::CDRegKey::Create <Fail RegCreateKeyEx>\n");

		return false;
	}

	return true;
}


//
// ReadInt
//

DWORD CDRegKey::ReadInt(const wchar_t* szKey)
{
	LOG1("CDRegKey::ReadInt\n");

	//
	// Method description
	// -------
	// read data on registry
	//

	if (_hKey == NULL)
	{
		LOG2("ERROR::CDRegKey::ReadInt <_hKey is NULL>\n");

		return false;
	}

	DWORD dwType = REG_DWORD, dwRead = 0, dwBytes = 4;
	_nResult = RegQueryValueEx(_hKey, szKey, NULL, &dwType, (LPBYTE)&dwRead, &dwBytes);

	if (_nResult != ERROR_SUCCESS)
	{
		LOG2("ERROR::CDRegKey::ReadInt <Fail RegQueryValueEx(%s)>\n", szKey);

		return false;
	}

	return dwRead;
}


//
// ReadInt
//

std::wstring CDRegKey::ReadString(const wchar_t* szKey)
{
	LOG1("CDRegKey::ReadString\n");

	//
	// Method description
	// -------
	// read data on registry
	//

	if (_hKey == NULL)
	{
		LOG2("ERROR::CDRegKey::ReadString <_hKey is NULL>\n");

		return L"";
	}

	DWORD dwType = REG_SZ, dwBytes = 0;

	//
	// Getting data size
	//
	_nResult = RegQueryValueEx(_hKey, szKey, 0, NULL, NULL, &dwBytes);

	if (_nResult != ERROR_SUCCESS)
	{
		LOG2(L"ERROR::CDRegKey::ReadString <Fail RegQueryValueEx>\n");

		return L"";
	}

	if (dwBytes == 0)
	{
		LOG2("ERROR::CDRegKey::ReadString <data size is zero>\n");

		Close();

		return L"";
	}

	wchar_t* szBuffer = new wchar_t[dwBytes + 1];

	if (szBuffer == NULL)
	{
		LOG2("ERROR::CDRegKey::ReadString <Out of memory>\n");

		Close();

		return L"";
	}

	memset(szBuffer, 0, (sizeof(wchar_t) * dwBytes) + 1);
	_nResult = RegQueryValueEx(_hKey, szKey, NULL, &dwType, (LPBYTE)szBuffer, &dwBytes);

	std::wstring szResult = szBuffer;
	delete[] szBuffer;

	if (_nResult != ERROR_SUCCESS)
	{
		LOG2("ERROR::CDRegKey::ReadString <Fail write key(%s)>\n", szKey);

		Close();

		return L"";
	}

	return szResult;
}


//
// CompareInt
//

bool CDRegKey::CompareInt(const wchar_t* szKey, DWORD dwValue)
{
	LOG1("CDRegKey::CompareInt\n");

	//
	// Method description
	// -------
	// Compare value
	//

	if (ReadInt(szKey) == dwValue)
	{
		return true;
	}

	return false;
}


//
// CompareString
//

bool CDRegKey::CompareString(const wchar_t* szKey, const wchar_t* szValue)
{
	LOG1("CDRegKey::CompareString\n");

	//
	// Method description
	// -------
	// Compare value
	//

	if (wcscmp(ReadString(szKey).c_str(), szValue) == 0)
	{
		return true;
	}

	return false;
}


//
// WriteInt
//

bool CDRegKey::WriteInt(const wchar_t* szKey, DWORD dwValue)
{
	LOG1("CDRegKey::WriteInt\n");

	//
	// Method description
	// -------
	// write data at key on registry
	//

	if (_hKey == NULL)
	{
		LOG2("ERROR::CDRegKey::WriteInt <_hKey is NULL>\n");

		return false;
	}

	DWORD dwType = REG_DWORD;
	_nResult = RegSetValueEx(_hKey, szKey, 0, dwType,(const BYTE*) &dwValue, sizeof(DWORD));

	if (_nResult != ERROR_SUCCESS)
	{
		LOG2("ERROR::CDRegKey::WriteInt <Fail write key(%s)>\n", szKey);

		Close();

		return false;
	}

	return true;
}


//
// WriteString
//

bool CDRegKey::WriteString(const wchar_t* szKey, const wchar_t* szValue)
{
	LOG1("CDRegKey::WriteString\n");

	//
	// Method description
	// -------
	// write data at key on registry
	//

	if (_hKey == NULL)
	{
		LOG2("ERROR::CDRegKey::WriteString <_hKey is NULL>\n");

		return false;
	}

	if (wcslen(szValue) == 0)
	{
		LOG2("ERROR::CDRegKey::WriteString <No insert data>\n");

		return false;
	}

	DWORD dwType = REG_SZ;
	_nResult = RegSetValueEx(_hKey, szKey, 0, dwType,(const BYTE*)szValue, wcslen(szValue) * sizeof(wchar_t));

	if (_nResult != ERROR_SUCCESS)
	{
		LOG2("ERROR::CDRegKey::WriteString <Fail write key(%s)>\n", szKey);

		Close();

		return false;
	}

	return true;
}


//
// Delete
//

bool CDRegKey::DeleteKey(HKEY hKey, const wchar_t* szPath)
{
	LOG1("CDRegKey::DeleteKey\n");

	//
	// Method description
	// -------
	// ERROR_SUCCESS indicates success. A nonzero error code defined in Winerror.h indicates failure.
	// To get a generic description of the error, call FormatMessage with the FORMAT_MESSAGE_FROM_SYSTEM
	// flag set. The message resource is optional; therefore, if you call FormatMessage it could fail.
	//

	_nResult = RegDeleteKey(hKey, szPath);

	if (_nResult != ERROR_SUCCESS)
	{
		LOGALWAYS(L"ERROR::CDRegKey::Delete <Fail delete key(%s)>\n", szPath);

		return false;
	}

	return true;
}


//
// Delete
//

bool CDRegKey::DeleteValue(const wchar_t* szPath)
{
	LOG1("CDRegKey::DeleteValue\n");

	//
	// Method description
	// -------
	// ERROR_SUCCESS indicates success. A nonzero error code defined in Winerror.h indicates failure.
	// To get a generic description of the error, call FormatMessage with the FORMAT_MESSAGE_FROM_SYSTEM
	// flag set. The message resource is optional; therefore, if you call FormatMessage it could fail.
	//

	_nResult = RegDeleteValue(_hKey, szPath);

	if (_nResult != ERROR_SUCCESS)
	{
		LOGALWAYS(L"ERROR::CDRegKey::Delete <Fail delete key(%s)>\n", szPath);

		return false;
	}

	return true;
}

//
// Close
//

void CDRegKey::Close()
{
	LOG1("CDRegKey::Close\n");

	//
	// Method description
	// -------
	// Close registry key
	//

	RegCloseKey(_hKey);
	_hKey = NULL;
}


//
// GetHKey
//

HKEY CDRegKey::GetHKey()
{
	LOG1("CDRegKey::GetHKey\n");

	//
	// Method description
	// -------
	// return the registry key handle
	//

	return _hKey;
}


//
// GetErrorMessage
//

std::wstring CDRegKey::GetErrorMessage()
{
	LOG1("CDRegKey::GetErrorMessage\n");

	//
	// Method description
	// -------
	// Extract error message
	//

	LPVOID lpMsgBuf;
	DWORD dw = GetLastError();

	FormatMessage(
		FORMAT_MESSAGE_ALLOCATE_BUFFER |
		FORMAT_MESSAGE_FROM_SYSTEM,
		NULL,
		dw,
		MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
		(LPTSTR) &lpMsgBuf,
		0, NULL );

	std::wstring szErrorMessage = (const wchar_t*)lpMsgBuf;

	LocalFree(lpMsgBuf);

	return szErrorMessage;
}



//
// DATE : December 4, 2009 11:42:39
// CODER : kurome(cdchoi@minigate.net)
// URL : http://www.minigate.net
//
// Copyright Minigate Ltd. All rights reserved.
//
// -----------------------------------------------------------
// NOTE : December 4, 2009 11:42:40
// -----------------------------------------------------------
// *
//
