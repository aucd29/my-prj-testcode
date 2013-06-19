/**
 * @Author burke
 */
#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>

#ifdef ANDROID
#include <android/log.h>

#define  LOG_TAG    "DAEMON-KILLER"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#else
#define LOGD printf
#endif

#define DELAY 2

char* g_packageName = NULL;
char* g_pid_nginx = NULL;
char* g_pid_checkalive = NULL;
char* g_pid_sqlited = NULL;

static void killProcess(const char* pid)
{
	char buff[128];

	if (pid != NULL)
	{
		sprintf(buff, "kill -15 %s", pid);
		LOGE("== STOP NGINX : %s\n", buff);
		system(buff);
	}
}

static void* checkingPackageName(void* arg)
{
	LOGD("== RUN THREAD\n");

	while(1)
	{
		sleep(DELAY);

		FILE* fp = fopen(g_packageName, "r");
		if (fp == NULL)
		{
			killProcess(g_pid_nginx);
			killProcess(g_pid_checkalive);
			killProcess(g_pid_sqlited);

			break;
		}
		else
		{
			fclose(fp);
		}
	}
}

int main(int argc, char** argv)
{
	LOGD("== START DAEMON KILLER\n");

	if (argv[1] == NULL)
	{
		LOGE("== PLEASE INPUT PACKAGE NAME\n");
		return 0;
	}

	g_packageName		= argv[1];
	g_pid_nginx			= argv[2];
	g_pid_checkalive	= argv[3];
	g_pid_sqlited		= argv[4];

	LOGD("== PACKAGE-NAME    = %s\n", argv[1]);
	LOGD("== NGINX       PID = %s\n", argv[2]);
	LOGD("== CHECK-ALIVE PID = %s\n", argv[3]);
	LOGD("== SQLITED     PID = %s\n", argv[4]);

	if (g_packageName == NULL)
	{
		LOGE("== ERROR!! PACKAGE NAME\n");
		return 1;
	}

	pthread_t tid;
	int pid = pthread_create(&tid, NULL, checkingPackageName, NULL);
	pthread_join(tid, NULL);

	LOGD("== EXIT DAEMON KILLER FOR %s", argv[1]);

	return 0;
}
