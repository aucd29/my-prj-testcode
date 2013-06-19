#ifndef __RSLOGGER_H__
#define __RSLOGGER_H__

#include <stdio.h>
#include <string.h>
#include <stdarg.h>
#include <time.h>
#include <assert.h>

#if defined(_WIN32_WCE)
	#pragma warning(disable: 4005)
	#include <winbase.h>

	#include <windows.h>
	#include <xstring>

#elif defined(_WIN32_WINNT)
	#include <windows.h>

#elif defined(ANDROID)
	#define NO_WCHAR_SUPPORT 1

#else
	#include <wchar.h>
	#include <string>

#endif

//----------------------------------------------------------------------------//
//
//
//----------------------------------------------------------------------------//
#define DEBUG


#define LOG_CONSOLE
//#define LOG_FILE

#if defined(LOG_FILE)
#if defined(_WIN32_WCE)
	#define LOG_PATH "/Storage Card/Miniple/log.txt"
#else
	#define LOG_PATH "/data/data/android.MinipleJava/log.txt"
#endif
#endif


//#define LEVEL0 // 자주호출 // 왠만하면 안찍는게
//#define LEVEL1 // 콜 플로우
#define LEVEL2 // 값 출력
#define LEVEL3 // 완전 중요
#define LEVEL4 // 에러의 경우
#define LEVELPACKET // 패킷덤프
#define LEVELALWAYS // 항상

#define LOG_LENGTH 2048


//----------------------------------------------------------------------------//
//
//
//----------------------------------------------------------------------------//
//#if !defined(ANDROID)
//#define ASSERT(e) assert(e)
//
//#else
//#define ASSERT(e) \
//{ \
//	if (e) \
//	{ \
//		(void)0; \
//	} \
//	else \
//	{ \
//		char a[LOG_LENGTH]; \
//		sprintf(a, "!! assert !! %s (%d line) %s\n", __FILE__, __LINE__, __func__); \
//		PRINT(a); \
//		__assert2(__FILE__, __LINE__, __func__, #e); \
//	} \
//}
//
//#endif


//----------------------------------------------------------------------------//
//
//
//----------------------------------------------------------------------------//
static void PRINT(const char* aBuf)
{
#ifdef LOG_FILE
	FILE* file;
	file = fopen(LOG_PATH, "a");
	if (file == NULL)
		return;
	fprintf(file, "%s", aBuf);
	fclose(file);
#endif

#ifdef LOG_CONSOLE
	#if defined(_WIN32_WINNT)
		OutputDebugStringA(aBuf);
	//#elif defiend(_WIN32_WCE)
		//NKDbgPrintfW(L"%s", aBuf);
		//OutputDebugString(aBuf);
	#else
		printf("%s", aBuf);
	#endif
#endif
}

static void PRINT(const wchar_t* aBuf)
{
#if !defined(NO_WCHAR_SUPPORT)
#ifdef LOG_FILE
	FILE* file;
	file = fopen(LOG_PATH, "a");
	if (file == NULL)
		return;
	fwprintf(file, L"%s", aBuf);
	fclose(file);
#endif

#ifdef LOG_CONSOLE
	#if defined(WIN32)
		OutputDebugStringW(aBuf);
	#else
		wprintf(L"%s", aBuf);
	#endif
#endif
#endif // defined(NO_WCHAR_SUPPORT)
}

inline void GetTime(char* szBuffer)
{
#if defined(_WIN32_WCE)
	SYSTEMTIME st;
	GetSystemTime(&st);
	sprintf(szBuffer, "[%04d%02d%02d%02d%02d%02d]", st.wYear, st.wMonth, st.wDay, st.wHour, st.wMinute, st.wSecond);
#else
	struct tm *tm_ptr;
	time_t the_time;

	(void)time(&the_time);
	tm_ptr = localtime((time_t*)(&the_time));

	sprintf(szBuffer,
		"[%04d%02d%02d%02d%02d%02d]",
		tm_ptr->tm_year+1900,
		tm_ptr->tm_mon+1,
		tm_ptr->tm_mday,
		tm_ptr->tm_hour,
		tm_ptr->tm_min,
		tm_ptr->tm_sec);
#endif
}

inline void GetTime(wchar_t* szBuffer)
{
#if !defined(NO_WCHAR_SUPPORT)
#if defined(_WIN32_WCE)
	SYSTEMTIME st;
	GetSystemTime(&st);
	swprintf(szBuffer, L"[%04d%02d%02d%02d%02d%02d]", st.wYear, st.wMonth, st.wDay, st.wHour, st.wMinute, st.wSecond);
#else
	struct tm *tm_ptr;
	time_t the_time;

	(void)time(&the_time);
	tm_ptr = localtime((time_t*)(&the_time));

	swprintf(szBuffer, 512,
		L"[%04d%02d%02d%02d%02d%02d]",
		tm_ptr->tm_year+1900,
		tm_ptr->tm_mon+1,
		tm_ptr->tm_mday,
		tm_ptr->tm_hour,
		tm_ptr->tm_min,
		tm_ptr->tm_sec);
#endif
#endif // defined(NO_WCHAR_SUPPORT)
}

static void LOGALWAYS(const char* aFormat, ...)
{
#ifdef LEVELALWAYS
	char buf[LOG_LENGTH];

	GetTime(buf);
	PRINT(buf);

	va_list arg;
	va_start(arg, aFormat);
	vsprintf(buf, aFormat, arg);
	PRINT("[ALWAYS]");
	if (strlen(buf) > LOG_LENGTH)
	{
		PRINT("!!! overflow \n");
	}
	else
	{
		PRINT(buf);
	}
	va_end(arg);
#endif
}

static void LOGALWAYS(const wchar_t* aFormat, ...)
{
#if !defined(NO_WCHAR_SUPPORT)
#ifdef LEVELALWAYS
	wchar_t buf[LOG_LENGTH] = {0};

	GetTime(buf);
	PRINT(buf);

	va_list arg;
	va_start(arg, aFormat);
	#if defined(_WIN32_WCE)
		vswprintf(buf, aFormat, arg);
	#else
		vswprintf(buf, 512, aFormat, arg);
	#endif
	PRINT(L"[ALWAYS]");
	PRINT(buf);

	PRINT(L"\n");
	va_end(arg);
#endif
#endif // defined(NO_WCHAR_SUPPORT)
}

//----------------------------------------------------------------------------//
//
//
//----------------------------------------------------------------------------//
#ifdef DEBUG

static void LOG0(const char* aFormat, ...)
{
#ifdef LEVEL0
	char buf[LOG_LENGTH];

	GetTime(buf);
	PRINT(buf);

	va_list arg;
	va_start(arg, aFormat);
	vsprintf(buf, aFormat, arg);
	PRINT("[0]");
	if (strlen(buf) > LOG_LENGTH)
		{
		PRINT("!!! overflow \n");
		}
	else
		{
		PRINT(buf);
		}
	va_end(arg);
#endif
}

static void LOG0(const wchar_t* aFormat, ...)
{
#if !defined(NO_WCHAR_SUPPORT)
#ifdef LEVEL0
	wchar_t buf[LOG_LENGTH];

	GetTime(buf);
	PRINT(buf);

	va_list arg;
	va_start(arg, aFormat);
	#if defined(_WIN32_WCE)
		vswprintf(buf, aFormat, arg);
	#else
		vswprintf(buf, LOG_LENGTH, aFormat, arg);
	#endif
	PRINT(L"[0]");
	PRINT(buf);
	PRINT(L"\n");
	va_end(arg);
#endif
#endif // defined(NO_WCHAR_SUPPORT)
}

static void LOG1(const char* aFormat, ...)
{
#ifdef LEVEL1
	char buf[LOG_LENGTH];

	GetTime(buf);
	PRINT(buf);

	va_list arg;
	va_start(arg, aFormat);
	vsprintf(buf, aFormat, arg);
	PRINT("[1]");
	PRINT(buf);
	va_end(arg);
#endif
}

static void LOG1(const wchar_t* aFormat, ...)
{
#if !defined(NO_WCHAR_SUPPORT)
#ifdef LEVEL1
	wchar_t buf[LOG_LENGTH];

	GetTime(buf);
	PRINT(buf);

	va_list arg;
	va_start(arg, aFormat);
	#if defined(_WIN32_WCE)
		vswprintf(buf, aFormat, arg);
	#else
		vswprintf(buf, LOG_LENGTH, aFormat, arg);
	#endif
	PRINT(L"[1]");
	PRINT(buf);

	PRINT(L"\n");
	va_end(arg);
#endif // defined(NO_WCHAR_SUPPORT)
#endif
}

static void LOG2(const char* aFormat, ...)
{
#ifdef LEVEL2
	char buf[LOG_LENGTH];

	GetTime(buf);
	PRINT(buf);

	va_list arg;
	va_start(arg, aFormat);
	vsprintf(buf, aFormat, arg);
	PRINT("[2]");
	if (strlen(buf) > LOG_LENGTH)
		PRINT("!!! overflow \n");
	else
		PRINT(buf);
	va_end(arg);
#endif
}

static void LOG2(const wchar_t* aFormat, ...)
{
#if !defined(NO_WCHAR_SUPPORT)
#ifdef LEVEL2
	wchar_t buf[LOG_LENGTH];

	GetTime(buf);
	PRINT(buf);

	va_list arg;
	va_start(arg, aFormat);
	#if defined(_WIN32_WCE)
		vswprintf(buf, aFormat, arg);
	#else
		vswprintf(buf, LOG_LENGTH, aFormat, arg);
	#endif
	PRINT(L"[2]");
	PRINT(buf);
	PRINT(L"\n");
	va_end(arg);
#endif
#endif // defined(NO_WCHAR_SUPPORT)
}

static void LOG3(const char* aFormat, ...)
{
#ifdef LEVEL3
	char buf[LOG_LENGTH];

	GetTime(buf);
	PRINT(buf);

	va_list arg;
	va_start(arg, aFormat);
	vsprintf(buf, aFormat, arg);
	PRINT("[3]");
	if (strlen(buf) > LOG_LENGTH)
		PRINT("!!! overflow \n");
	else
		PRINT(buf);
	va_end(arg);
#endif
}

static void LOG3(const wchar_t* aFormat, ...)
{
#if !defined(NO_WCHAR_SUPPORT)
#ifdef LEVEL3
	wchar_t buf[LOG_LENGTH];

	GetTime(buf);
	PRINT(buf);

	va_list arg;
	va_start(arg, aFormat);
	#if defined(_WIN32_WCE)
		vswprintf(buf, aFormat, arg);
	#else
		vswprintf(buf, LOG_LENGTH, aFormat, arg);
	#endif
	PRINT(L"[3]");
	PRINT(buf);
	PRINT(L"\n");
	va_end(arg);
#endif
#endif // defined(NO_WCHAR_SUPPORT)
}

static void LOG4(const char* aFormat, ...)
{
#ifdef LEVEL4
	char buf[LOG_LENGTH];

	GetTime(buf);
	PRINT(buf);

	va_list arg;
	va_start(arg, aFormat);
	vsprintf(buf, aFormat, arg);
	PRINT("[4]");
	if (strlen(buf) > LOG_LENGTH)
		PRINT("!!! overflow \n");
	else
		PRINT(buf);
	va_end(arg);
#endif
}

static void LOG4(const wchar_t* aFormat, ...)
{
#if !defined(NO_WCHAR_SUPPORT)
#ifdef LEVEL4
	wchar_t buf[LOG_LENGTH];

	GetTime(buf);
	PRINT(buf);

	va_list arg;
	va_start(arg, aFormat);
	#if defined(_WIN32_WCE)
		vswprintf(buf, aFormat, arg);
	#else
		vswprintf(buf, LOG_LENGTH, aFormat, arg);
	#endif
	PRINT(L"[4]");
	PRINT(buf);
	PRINT(L"\n");
	va_end(arg);
#endif
#endif // defined(NO_WCHAR_SUPPORT)
}

static void LOGPACKET(const char* aPacket, int aLength)
{
#ifdef LEVELPACKET
	PRINT("===== PACKET DUMP ===== \n");
	char hex[256];
	char txt[32];
	char tmp[8];

	for (int i = 0; i < aLength; i++)
	{
		if (i % 16 == 0)
		{
			strcpy(hex, "\t[");
			strcpy(txt, "\t[");
		}

		char p = aPacket[i];

		memset(tmp, 0, 8);
		sprintf(tmp, "%02x ", p);
		strcat(hex, tmp);
		if ((0x20 < p) && (p < 0x80))
		{
			memset(tmp, 0, 8);
			sprintf(tmp, "%c", p);
			strcat(txt, tmp);
		}
		else
		{
			memset(tmp, 0, 8);
			sprintf(tmp, ".");
			strcat(txt, tmp);
		}

		if ((i % 16) == 15)
		{
			PRINT(hex);
			PRINT("]");
			PRINT(txt);
			PRINT("]\n");
			memset(hex, 0, 256);
			memset(txt, 0, 32);
		}
	}

	PRINT(hex);
	PRINT("]");
	PRINT(txt);
	PRINT("]\n\n");
#endif
}

#else // DEBUG

	static void LOG0(const char* aFormat, ...) {}
	static void LOG1(const char* aFormat, ...) {}
	static void LOG2(const char* aFormat, ...) {}
	static void LOG3(const char* aFormat, ...) {}
	static void LOG4(const char* aFormat, ...) {}
	static void LOGPACKET(const char* aPacket, int aLength) {}

	static void LOG0(const wchar_t* aFormat, ...) {}
	static void LOG1(const wchar_t* aFormat, ...) {}
	static void LOG2(const wchar_t* aFormat, ...) {}
	static void LOG3(const wchar_t* aFormat, ...) {}
	static void LOG4(const wchar_t* aFormat, ...) {}

#endif // DEBUG

#endif //__RSLOGGER_H__

// -----------------------------------------------------------
// NOTE : August 19, 2009 9:54:50
// -----------------------------------------------------------
// * SUPPORT WIDE CHAR
//
