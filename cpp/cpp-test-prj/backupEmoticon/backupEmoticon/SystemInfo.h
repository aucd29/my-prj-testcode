#pragma once

#include "stdafx.h"

class SystemInfo
{
public:
	SystemInfo(void);
	~SystemInfo(void);

public:
	static CString getLocalAppDataPath();

};

