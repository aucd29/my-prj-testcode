// VBoxUUIDChanger.h : PROJECT_NAME ���� ���α׷��� ���� �� ��� �����Դϴ�.
//

#pragma once

#ifndef __AFXWIN_H__
	#error "PCH�� ���� �� ������ �����ϱ� ���� 'stdafx.h'�� �����մϴ�."
#endif

#include "resource.h"		// �� ��ȣ�Դϴ�.


// CVBoxUUIDChangerApp:
// �� Ŭ������ ������ ���ؼ��� VBoxUUIDChanger.cpp�� �����Ͻʽÿ�.
//

class CVBoxUUIDChangerApp : public CWinApp
{
public:
	CVBoxUUIDChangerApp();

// �������Դϴ�.
	public:
	virtual BOOL InitInstance();

// �����Դϴ�.

	DECLARE_MESSAGE_MAP()
};

extern CVBoxUUIDChangerApp theApp;