// dmg2isoManager.cpp : Defines the entry point for the application.
//

#include "stdafx.h"
#include "dmg2isoManager.h"
#include <xstring>
#include <Commdlg.h>
#include "windows.h"
#include "dmg2iso.h"
#include "base64.h"
#include "commctrl.h"

#pragma comment(lib, "Comdlg32.lib")

#define MAX_LOADSTRING 100

// Global Variables:
TCHAR szTitle[MAX_LOADSTRING];					// The title bar text
TCHAR szWindowClass[MAX_LOADSTRING];			// the main window class name
HINSTANCE g_hInst;
HWND hDlgMain;

// Forward declarations of functions included in this code module:
BOOL CALLBACK	WndProc(HWND, UINT, WPARAM, LPARAM);
INT_PTR CALLBACK	About(HWND, UINT, WPARAM, LPARAM);
DWORD WINAPI RunThread(void* lParam);
static HANDLE hThread;


// global
std::wstring gszFullPath, gszOutputPath;
#define PLIST_LOOKUP_SIZE 0x200000
#define BT_ZLIB 0x80000005
#define BT_COPY 0x00000001
#define BT_ZERO 0x00000002
#define BT_END 0xffffffff

z_stream z;

int APIENTRY _tWinMain(HINSTANCE hInstance,
                     HINSTANCE hPrevInstance,
                     LPTSTR    lpCmdLine,
                     int       nCmdShow)
{
	UNREFERENCED_PARAMETER(hPrevInstance);
	UNREFERENCED_PARAMETER(lpCmdLine);

	g_hInst = hInstance;

	// Initialize global strings
	LoadString(hInstance, IDS_APP_TITLE, szTitle, MAX_LOADSTRING);
	LoadString(hInstance, IDC_DMG2ISOMANAGER, szWindowClass, MAX_LOADSTRING);
	
	DialogBox(g_hInst, MAKEINTRESOURCE(IDD_DIALOG1), HWND_DESKTOP, WndProc);


	return 0;
}


int findstr(
			const char* inp,
			const char* str,
			const unsigned long ilen,
			const unsigned long slen,
			const unsigned int num
			)
{
	if (num > 0) {
		unsigned int tnum = num-1;
		int found = 0;
		char *tinp, *tstr;
		for (tinp = (char*)inp; tinp < inp+ilen-slen+1; tinp++) {
			for (tstr = (char*)str; tstr < str+slen; tstr++) {
				if (*tstr != tinp[tstr-str]) break;
				if ((*tstr == tinp[tstr-str])&&(tstr-str == slen-1)) found = 1;
			}
			if ((found == 1)&&(!tnum--)) return tinp-inp;
			found = 0;
		}
		return -1;
	}
	else {
		unsigned int tnum = 0;
		int found = 0;
		char *tinp, *tstr;
		for (tinp = (char*)inp; tinp < inp+ilen-slen+1; tinp++) {
			for (tstr = (char*)str; tstr < str+slen; tstr++) {
				if (*tstr != tinp[tstr-str]) break;
				if ((*tstr == tinp[tstr-str])&&(tstr-str == slen-1)) tnum++;
			}
		}
		return tnum;
	}
}

bool is_base64(const char c) {
	if ( (c >= 'A' && c <= 'Z') || 
		(c >= 'a' && c <= 'z') || 
		(c >= '0' && c <= '9') ||
		c == '+' || 
		c == '/' || 
		c == '=') return true;
	return false;
}

void cleanup_base64(char *inp, const unsigned int size) {
	char *tinp1,*tinp2;
	tinp1 = inp;
	tinp2 = inp;
	for (unsigned int i = 0; i < size; i++) {
		if (is_base64(*tinp2)) {
			*tinp1++ = *tinp2++;
		}
		else {
			*tinp1 = *tinp2++;
		}
	}
	*(tinp1) = 0;
}

unsigned char decode_base64_char(const char c) {
	if (c >= 'A' && c <= 'Z') return c - 'A';
	if (c >= 'a' && c <= 'z') return c - 'a' + 26;
	if (c >= '0' && c <= '9') return c - '0' + 52;
	if (c == '+') return 62;
	if (c == '=') return 0;
	return 63;   
}

bool decode_base64(const char *inp, unsigned int isize, 
				   char *out, unsigned int *osize) {
					   char *tinp = (char*)inp; 
					   char *tout;
					   *osize = isize / 4 * 3;
					   if (inp != out) {
						   tout = (char*) malloc(*osize);
						   out = tout;
					   }
					   else {
						   tout = tinp;
					   }
					   for(unsigned int i = 0; i < isize >> 2; i++) {
						   *tout++ = (decode_base64_char(*tinp++) << 2) | (decode_base64_char(*tinp) >> 4);
						   *tout++ = (decode_base64_char(*tinp++) << 4) | (decode_base64_char(*tinp) >> 2);
						   *tout++ = (decode_base64_char(*tinp++) << 6) | decode_base64_char(*tinp++);
					   }
					   if (*(tinp-1) == '=') (*osize)--;
					   if (*(tinp-2) == '=') (*osize)--;

					   return true;
}


DWORD WINAPI RunThread(void* lParam)
{
	FILE *FIN = _wfopen(gszFullPath.c_str(), L"rb");

	if (FIN == NULL)
	{
		return 0;
	}

	FILE *FOUT = _wfopen(gszOutputPath.c_str(), L"wb");
	
	if (FIN == NULL)
	{
		return 0;
	}

	SendMessage(GetDlgItem(hDlgMain, IDPS_LOOP), PBM_SETMARQUEE,TRUE, 50);


	fseek(FIN,-PLIST_LOOKUP_SIZE, 2);

	unsigned long ss = ftell(FIN);

	printf("reading property list...");
	
	char* ar = (char*)malloc(PLIST_LOOKUP_SIZE);

	if (ar == NULL)
	{
		printf("ERROR: not enough memory\n");
		
		return 0;
	}

	fread((char*)ar, PLIST_LOOKUP_SIZE, 1, FIN);
	if (findstr(ar,plist_begin,PLIST_LOOKUP_SIZE,strlen(plist_begin),0) == 0)
	{
		printf("ERROR: Property list is corrupted.\n");
		free(ar);

		return 0;
	}

	unsigned int ploffs_begin = findstr(ar,partlist_begin,PLIST_LOOKUP_SIZE,strlen(partlist_begin),1);
	unsigned int pl_size = findstr(ar,partlist_end,PLIST_LOOKUP_SIZE,strlen(partlist_end),1)+8-ploffs_begin;
	char* plist = (char*)malloc(pl_size);

	memcpy(plist,ar+ploffs_begin,pl_size);
	unsigned char partnum = findstr(plist,part_begin,pl_size,strlen(part_begin),0);
	printf("found %d partitions\n",partnum);

	char** parts = new char*[partnum];	
	unsigned int* partlen = new unsigned int[partnum];

	for (int i = 0; i < partnum; i++) {
		unsigned int data_begin = findstr(plist,part_begin,pl_size,strlen(part_begin),i+1)+strlen(part_begin);
		unsigned int data_size = findstr(plist,part_end,pl_size,strlen(part_end),i+1)-data_begin;
		parts[i] = (char*)malloc(data_size);
		memcpy(parts[i],plist+data_begin,data_size);
		cleanup_base64(parts[i],data_size);
		
		decode_base64(parts[i],strlen(parts[i]),parts[i], &partlen[i]);
		
	}

	delete [] partlen;

	z.zalloc = (alloc_func)0;
	z.zfree = (free_func)0;
	z.opaque = (voidpf)0;
	printf("decompressing:\n");
	Bytef* tmp = (Bytef*)malloc(0x40000);
	Bytef* otmp = (Bytef*)malloc(0x40000);
	unsigned __int32 last_offs = 0;
	for (int i = 0; i < partnum; i++) {
		printf("partition %d...",i);
		unsigned int offset = 0xcc;
		
		unsigned __int32 out_offs,out_size,in_offs,in_size,last_in_offs;
		unsigned __int32 block_type = 0;
		while (block_type != 0xffffffff) {
			memcpy(&block_type,parts[i]+offset,4);
			memcpy(&out_offs,parts[i]+offset+12,4);
			memcpy(&out_size,parts[i]+offset+20,4);
			memcpy(&in_offs,parts[i]+offset+28,4);
			memcpy(&in_size,parts[i]+offset+36,4);
			block_type = convert_endian(block_type);
			out_offs = convert_endian(out_offs)*0x200;
			out_size = convert_endian(out_size)*0x200;
			in_offs = convert_endian(in_offs);
			in_size = convert_endian(in_size);
			if (block_type == BT_ZLIB) {
				if (inflateInit(&z) != Z_OK) {
					printf("ERROR: Can't initialize inflate stream\n");
					return 0;
				}
				fseek(FIN,last_offs+in_offs,0);
				fread(tmp,in_size,1,FIN);
				z.next_in = (Bytef*)tmp;
				z.next_out = (Bytef*)otmp;
				while (1) {
					z.avail_in = z.avail_out = 32768;
					int err = inflate(&z, Z_NO_FLUSH);
					if (err != Z_OK && err != Z_STREAM_END) {
						printf("ERROR: Inflation failed\n");
						return 0;
					}
					if (err == Z_STREAM_END) break;
				}
				if (inflateEnd(&z) != Z_OK) { 
					printf("ERROR:\n");
					return 0;
				}
				fwrite(otmp,out_size,1,FOUT);
				last_in_offs = in_offs+in_size;
			}
			else if (block_type == BT_COPY) {
				fseek(FIN,last_offs+in_offs,0);
				fread(tmp,in_size,1,FIN);
				fwrite(tmp,out_size,1,FOUT);//copy
			}
			else if (block_type == BT_ZERO) {
				char* zeroblock = (char*)malloc(out_size);
				memset(zeroblock,0,out_size);
				fwrite(zeroblock,out_size,1,FOUT);
				free(zeroblock);
			}
			else if (block_type == BT_END) {
				last_offs+=last_in_offs;
			}
			offset+=0x28;
			//if ((offset-0xcc)/0x28 % 64 == 0)
			//printf(".");
		}
		printf("ok\n");
	}
	printf("\nconversion successful\n");
	free(tmp);
	free(otmp);

	for (int i = 0; i < partnum; i++) free(parts[i]);

	delete [] parts;

	free(plist);
	fclose(FIN);
	fclose(FOUT);

	SendMessage(GetDlgItem(hDlgMain, IDPS_LOOP), PBM_SETMARQUEE, FALSE, 0);

	return 0;
}

//
//  FUNCTION: WndProc(HWND, UINT, WPARAM, LPARAM)
//
//  PURPOSE:  Processes messages for the main window.
//
//  WM_COMMAND	- process the application menu
//  WM_PAINT	- Paint the main window
//  WM_DESTROY	- post a quit message and return
//
//
BOOL CALLBACK WndProc(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
	int wmId, wmEvent;

	switch (message)
	{
	case WM_INITDIALOG:
		hDlgMain = hDlg;
		return TRUE;

	case WM_COMMAND:
		wmId    = LOWORD(wParam);
		wmEvent = HIWORD(wParam);
		// Parse the menu selections:
		switch (wmId)
		{
		case IDBTN_OPEN:
			{
				OPENFILENAME OFN;
				wchar_t szFilePath[MAX_PATH] = {0};
				wchar_t szFileName[MAX_PATH] = {0};

				OFN.lStructSize		= sizeof(OPENFILENAME);			// 구조체 사이즈 정의
				OFN.hwndOwner		= hDlg;                            // 오너 핸들 넘기고
				OFN.lpstrFilter		= L"dmg(*.dmg)\0";				// 파일확장자 제한시키고
				OFN.lpstrFile		= szFilePath;                       // 1. 처음 나타낼 파일명, 2. 사용자가 선택한 파일
				OFN.nMaxFile		= MAX_PATH;                         // lpstrFile 길이
				OFN.lpstrTitle		= L"파일을 선택해 주십시요";       // 다이얼로그 타이틀
				OFN.lpstrFileTitle	= szFileName;					// 파일 이름을 리턴받기위한 것
				OFN.nMaxFileTitle	= MAX_PATH;
				OFN.lpstrDefExt		= L"dmg";                         // 기본 확장자
				OFN.lpstrInitialDir = NULL;

				if (GetOpenFileName(&OFN) != 0)
				{
					// dmg data
					gszFullPath = szFilePath;

					// output iso data
					gszOutputPath  = gszFullPath.substr(0, gszFullPath.size() - 3);
					gszOutputPath += L"iso";

					wchar_t szShortPath[512] = {0};
					GetShortPathName(gszFullPath.c_str(), szShortPath, MAX_PATH);
					SetDlgItemText(hDlg, IDSTC_PATH, szShortPath);

					DWORD ID1;
					hThread = CreateThread(NULL, 0, RunThread, NULL, 0, &ID1);

					UpdateWindow(hDlg);

					return 0;
				}

				return 0;
			}
			break;

		case IDM_ABOUT:
			DialogBox(g_hInst, MAKEINTRESOURCE(IDD_ABOUTBOX), hDlg, About);
			break;

		case IDCANCEL:
			EndDialog(hDlg,0);
			return TRUE;
		}
		break;
	}
	return 0;
}

// Message handler for about box.
INT_PTR CALLBACK About(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
	UNREFERENCED_PARAMETER(lParam);
	switch (message)
	{
	case WM_INITDIALOG:
		return (INT_PTR)TRUE;

	case WM_COMMAND:
		if (LOWORD(wParam) == IDOK || LOWORD(wParam) == IDCANCEL)
		{
			EndDialog(hDlg, LOWORD(wParam));
			return (INT_PTR)TRUE;
		}
		break;
	}
	return (INT_PTR)FALSE;
}
