/*****************************************************************************
* Copyright 2003 - 2007 Broadcom Corporation.  All rights reserved.
*
* This program is the proprietary software of Broadcom Corporation and/or
* its licensors, and may only be used, duplicated, modified or distributed
* pursuant to the terms and conditions of a separate, written license
* agreement executed between you and Broadcom (an "Authorized License").
* Except as set forth in an Authorized License, Broadcom grants no license
* (express or implied), right to use, or waiver of any kind with respect to
* the Software, and Broadcom expressly reserves all rights in and to the
* Software and all intellectual property rights therein.  IF YOU HAVE NO
* AUTHORIZED LICENSE, THEN YOU HAVE NO RIGHT TO USE THIS SOFTWARE IN ANY
* WAY, AND SHOULD IMMEDIATELY NOTIFY BROADCOM AND DISCONTINUE ALL USE OF
* THE SOFTWARE.
*
* Except as expressly set forth in the Authorized License,
* 1. This program, including its structure, sequence and organization,
*    constitutes the valuable trade secrets of Broadcom, and you shall use
*    all reasonable efforts to protect the confidentiality thereof, and to
*    use this information only in connection with your use of Broadcom
*    integrated circuit products.
* 2. TO THE MAXIMUM EXTENT PERMITTED BY LAW, THE SOFTWARE IS PROVIDED "AS IS"
*    AND WITH ALL FAULTS AND BROADCOM MAKES NO PROMISES, REPRESENTATIONS OR
*    WARRANTIES, EITHER EXPRESS, IMPLIED, STATUTORY, OR OTHERWISE, WITH
*    RESPECT TO THE SOFTWARE.  BROADCOM SPECIFICALLY DISCLAIMS ANY AND ALL
*    IMPLIED WARRANTIES OF TITLE, MERCHANTABILITY, NONINFRINGEMENT, FITNESS
*    FOR A PARTICULAR PURPOSE, LACK OF VIRUSES, ACCURACY OR COMPLETENESS,
*    QUIET ENJOYMENT, QUIET POSSESSION OR CORRESPONDENCE TO DESCRIPTION. YOU
*    ASSUME THE ENTIRE RISK ARISING OUT OF USE OR PERFORMANCE OF THE SOFTWARE.
* 3. TO THE MAXIMUM EXTENT PERMITTED BY LAW, IN NO EVENT SHALL BROADCOM OR ITS
*    LICENSORS BE LIABLE FOR (i) CONSEQUENTIAL, INCIDENTAL, SPECIAL, INDIRECT,
*    OR EXEMPLARY DAMAGES WHATSOEVER ARISING OUT OF OR IN ANY WAY RELATING TO
*    YOUR USE OF OR INABILITY TO USE THE SOFTWARE EVEN IF BROADCOM HAS BEEN
*    ADVISED OF THE POSSIBILITY OF SUCH DAMAGES; OR (ii) ANY AMOUNT IN EXCESS
*    OF THE AMOUNT ACTUALLY PAID FOR THE SOFTWARE ITSELF OR U.S. $1, WHICHEVER
*    IS GREATER. THESE LIMITATIONS SHALL APPLY NOTWITHSTANDING ANY FAILURE OF
*    ESSENTIAL PURPOSE OF ANY LIMITED REMEDY.
*****************************************************************************/



/*
*
*****************************************************************************
*
*  webconfig.h
*
*  PURPOSE:
*
*     Header file for wireless config daemon.
*
*  NOTES:
*
*****************************************************************************/


#ifndef __WEBCONFIG_H__
#define __WEBCONFIG_H__

#include <stdarg.h>

#include "common_util.h"

enum
{
	WC_PAGE_STATUS = 0,
	WC_PAGE_ENET_CONFIG,
	WC_PAGE_NETWORKS_CONFIG,
	WC_PAGE_WIRELESS_CONFIG,
	WC_PAGE_ADHOC_CONFIG,
	WC_PAGE_PHONE_CONFIG,
	WC_PAGE_SIP_CONFIG,
	WC_PAGE_SKYPE_CONFIG,
	WC_PAGE_AUDIO_CONFIG,
	WC_PAGE_STUN_CONFIG,
	WC_PAGE_VIDEO_CONFIG,
	WC_PAGE_UPGRADE,
	WC_PAGE_UPGRADE_AND_REBOOT,
	WC_PAGE_UPLOAD,
	WC_PAGE_WEBCAM_SNAP,
	WC_PAGE_WEBCAM_STREAM,
	WC_PAGE_VERBOSE_DEBUG_STATS,
	WC_PAGE_SUMMARY_DEBUG_STATS,
	WC_PAGE_CPU_USAGE,
	WC_PAGE_REBOOT,
	WC_PAGE_PHONE_PW_INIT,
	WC_PAGE_PHONE_LOGIN,
	WC_PAGE_PHONE_LOGOUT,
	WC_PAGE_PHONE_PASSWD_MODIFY,
	WC_PAGE_PHONE_W200_AUDIO_CONTROL,
	WC_PAGE_PHONE_W110_AUDIO_CONTROL,
	WC_PAGE_PHONE_CALL_LOG,
	WC_PAGE_PHONE_SIP_LOG,
	WC_PAGE_TOP,
	WC_PAGE_SERVER_CONFIG,
	WC_PAGE_MSG_CONFIG,
	WC_PAGE_DEVICE_CONFIG,
	WC_PAGE_WIFI_CONFIG,
};

enum
{
	WC_ACTION_SHOW = 0,
	WC_ACTION_SUBMIT,
	WC_ACTION_RESET_TO_DEFAULTS,
	WC_ACTION_CONFIGURE,
	WC_ACTION_REMOVE,
	WC_ACTION_MOVE_UP,
	WC_ACTION_MOVE_DOWN,
	WC_ACTION_REMOVE_ADHOC,
	WC_ACTION_RESET_CFG,
	WC_ACTION_REBOOT,
	WC_ACTION_PHONE_PW_INIT,
	WC_ACTION_PHONE_LOGIN,
	WC_ACTION_PHONE_LOGOUT,
	WC_ACTION_PHONE_PW_MODIFY,
};

#define WC_MIN_NETWORK_NUM	1
#define WC_MAX_NETWORK_NUM	9

#define POOL_MALLOC_SIZE	4096
#define POOL_OVERHEAD_SIZE	4
#define POOL_BUFFER_SIZE	(POOL_MALLOC_SIZE - POOL_OVERHEAD_SIZE)
typedef struct pool_list_t
{
	struct pool_list_t *next;
	int free;
	char *freep;
	char buffer[1];
} pool_list_t;

typedef struct wc_control
{
	int log_level;
	int page;
	int action;
	int network;
	int content_len;
	char *section;
	struct pool_list_t * pool_list;
} wc_control;

typedef enum
{
	TYPE_NUM,
	TYPE_STRING,
	TYPE_SSID_NAME,
	TYPE_INTERNAL_NETWORK,
	TYPE_INTERNAL_SECTION,
	TYPE_ENUM
} type_t;

typedef enum
{
	LOC_CFG_FILE,
	LOC_LOCAL_VAR
} loc_t;

typedef union value_t
{
	int num;
	char *str;
	int *num_ptr;
} value_t;

typedef struct wc_enum_map_t
{
	int         enumValue;
	const char *enumStr;

} wc_enum_map_t;

typedef struct map_t
{
	char * section;			/* section in config file */
	char * key;				/* key in config file */
	char * search_key;		/* input or substitution key */
	int search_key_len;
	loc_t location;         /* indicates where the value is derived from */
	type_t type;
	value_t default_value;
	value_t value;
	char * input_value;
	void (*print_fn)(struct map_t *map_entry, const char *subvalue);
	int (*parse_fn)(char *str, value_t *value, value_t *old_value, loc_t location);
	int (*validate_fn)(value_t *value, loc_t location);
	int (*equal_fn)(const value_t *value1, const value_t *value2, loc_t location);
	int matching;
	int valid;
	int changed;
	const wc_enum_map_t *enum_map;
} map_t;


/* ---- Public Variables ------------------------------------------------- */

extern wc_control wc;

#define WC_WIRELESS_CFG_FILE	"/nvdata/etc/wlconfigd.rc"
#define WC_HEADER_HTML	        "../html/header.html"
#define WC_FOOTER_HTML	        "../html/footer.html"

#define WC_STATUS_HTML			"../html/status.html"
#define WC_ENET_CONFIG_HTML	    "../html/enetcfg.html"
#define WC_NETWORKS_CONFIG_HTML	"../html/networkscfg.html"
#define WC_WIRELESS_CONFIG_HTML	"../html/wlancfg.html"
#define WC_ADHOC_CONFIG_HTML	"../html/adhoccfg.html"
#define WC_PHONE_CONFIG_HTML	"../html/phonecfg.html"
#define WC_SERVER_CONFIG_HTML	"../html/servercfg.html"
#define WC_MSG_CONFIG_HTML	    "../html/msgcfg.html"
#define WC_DEVICE_CONFIG_HTML	"../html/devicecfg.html"
#define WC_SIP_CONFIG_HTML   	"../html/sipcfg.html"
#define WC_SKYPE_CONFIG_HTML    "../html/skypecfg.html"
#define WC_AUDIO_CONFIG_HTML 	"../html/audiocfg.html"
#define WC_STUN_CONFIG_HTML 	"../html/stuncfg.html"
#define WC_VIDEO_CONFIG_HTML   	"../html/videocfg.html"
#define WC_UPGRADE_HTML			"../html/upgrade.html"
#define WC_UPLOAD_HTML	   		"../html/upload.html"
#define WC_REBOOT_HTML			"../html/reboot.html"
#define WC_TOP_HTML             "../html/top.html"
#define WC_TOP_USER_HTML        "../html/top_user.html"
#define WC_TOP_SERVICE_HTML		"../html/top_service.html"
#define WC_LOGIN_HTML           "../html/login.html"
#define WC_PW_MODIFY_HTML       "../html/pwchange.html"
#define WC_W200_AUDIO_CONFIG_HTML       "../syshtml/w200audiocfg.html"
#define WC_W110_AUDIO_CONFIG_HTML       "../syshtml/w110audiocfg.html"
#define WC_SYSTEM_CONTROL_MAIN_HTML       "../syshtml/syscontmain.html"



/* ---- Public Functions ------------------------------------------------- */


#define CM_CFG_MAX_TEXT_LEN 256
#endif /* __WEBCONFIG_H__ */

