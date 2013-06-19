#include "StdAfx.h"
#include "ZipManager.h"
#include "CFileFindEx.h"

#include "zip.h"
#include "unzip.h"


ZipManager::ZipManager(void)
{
}


ZipManager::~ZipManager(void)
{
}

void ZipManager::compress(CString filename, CString fullpath)
{
	TRACE(L"ZipManager::compress : " + fullpath + L"\n");
	
	fullpath += L"\\*";

	CFileFindEx dir;
	std::vector<CString> dirs;
    if (!dir.FindFile(fullpath))
    {
		return ;
	}

	TRACE(L"ZipManager::compress 2\n");
	HZIP hz = CreateZip(filename, 0);

	dir.GetList(dirs, true);

	TRACE(L"ZipManager::compress (%d)\n", dirs.size());
	
	CString name;
	int last;
	for (int i=0; i<dirs.size(); ++i)
	{
		TRACE(L"full : " + dirs[i]);		
		last = dirs[i].ReverseFind('\\') + 1;
		name = dirs[i].Right(dirs[i].GetLength() - last);
		TRACE(L", name : " + name + L"\n");

		ZipAdd(hz, name,  dirs[i]);
	}

	CloseZip(hz);
}

void ZipManager::decompress(CString filename, CString fullpath)
{
	TRACE(L"FULLPATH : %s 1\n", fullpath);

	if (filename.GetLength() == 0)
	{
		return ;
	}

	TRACE(L"FULLPATH : %s 2\n", fullpath);
	
	HZIP hz = OpenZip(filename, 0);
	SetUnzipBaseDir(hz, fullpath);
	ZIPENTRY ze; 
	
	GetZipItem(hz,-1, &ze);
	int numitems = ze.index;
	CString filepath;
	TRACE(L"COUNT (%d)\n", numitems);
	for (int i=0; i<numitems; ++i)
	{
		TRACE(L"EXTRACT : %s\n", ze.name);
		GetZipItem(hz,i,&ze);
		UnzipItem(hz,i, ze.name);
	}

	CloseZip(hz);
}