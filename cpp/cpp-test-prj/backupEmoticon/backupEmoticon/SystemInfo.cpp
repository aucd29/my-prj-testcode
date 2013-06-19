#include "StdAfx.h"
#include "SystemInfo.h"
#include <assert.h>
#include "Userenv.h"

 
// MSDN    : SHGetSpecialFolderLocation http://msdn.microsoft.com/en-us/library/bb762203%28VS.85%29.aspx
// CSIDL   : http://msdn.microsoft.com/en-us/library/bb762494%28VS.85%29.aspx
// EXAMPLE : http://deneb.egloos.com/2283673

#define INFO_BUFFER_SIZE 512 

SystemInfo::SystemInfo(void)
{
}


SystemInfo::~SystemInfo(void)
{
}

void WorkWithSpecialFolder()
{

}

CString SystemInfo::getLocalAppDataPath() 
{
	// Allocate a pointer to an Item ID list
	LPITEMIDLIST pidl;

	// Get a pointer to an item ID list that
	// represents the path of a special folder
	HRESULT hr = SHGetSpecialFolderLocation(NULL, CSIDL_LOCAL_APPDATA, &pidl);

	// Convert the item ID list's binary
	// representation into a file system path
	TCHAR szPath[_MAX_PATH];
	BOOL f = SHGetPathFromIDList(pidl, szPath);

	// Allocate a pointer to an IMalloc interface
	LPMALLOC pMalloc;

	// Get the address of our task allocator's IMalloc interface
	hr = SHGetMalloc(&pMalloc);

	// Free the item ID list allocated by SHGetSpecialFolderLocation
	pMalloc->Free(pidl);

	// Free our task allocator
	pMalloc->Release();

	// Work with the special folder's path (contained in szPath)

	/*if(!::GetUserDirectory(username, &bufCharCount))
	{
		assert(0);
		return _T("");
	}*/

	/*if(!::GetUserName(username, &bufCharCount))
	{
		assert(0);
		return _T("");
	}*/

	/*if(!::GetUserProfileDirectory(NULL, username, &bufCharCount))
	{
		assert(0);
		return _T("");
	}*/

	//TRACE(_T("USERNAME : ") + username);
    
	return szPath;
}