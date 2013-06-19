#pragma once

class ZipManager
{
public:
	ZipManager(void);
	~ZipManager(void);

public:
	static void compress(CString filename, CString fullpath);

	static void decompress(CString filename, CString fullpath);
};

