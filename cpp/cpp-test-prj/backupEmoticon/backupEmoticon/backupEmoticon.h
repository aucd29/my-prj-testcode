
// backupEmoticon.h : main header file for the PROJECT_NAME application
//

#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"		// main symbols


// CbackupEmoticonApp:
// See backupEmoticon.cpp for the implementation of this class
//

class CbackupEmoticonApp : public CWinApp
{
public:
	CbackupEmoticonApp();

// Overrides
public:
	virtual BOOL InitInstance();

// Implementation

	DECLARE_MESSAGE_MAP()
};

extern CbackupEmoticonApp theApp;