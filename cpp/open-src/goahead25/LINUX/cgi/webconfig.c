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
/******************************************************************************
*
*  webconfig.c
*
*  PURPOSE:
*
*     This utility is a background service for configuring and maintaining
*     wireless connections.
*
*  NOTES:
*
*****************************************************************************/

/* ---- Include Files ---------------------------------------------------- */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <signal.h>
#include <unistd.h>
#include <time.h>
#include <fcntl.h>
#include <ctype.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <sys/sysinfo.h>
#include <sys/ioctl.h>
#include <sys/time.h>

#include <arpa/inet.h>
#include <linux/route.h>

#include <time.h>

//  Logging stuff.:q!

#define ONE_LOG_MODULE_ID     webconfig
//#define ONE_LOG_DEFAULT_LEVEL (ONE_LOG_ERROR)
#define ONE_LOG_DEFAULT_HEADER "webconfig"

#include "webconfig.h"
#include "external.h"
/* ---- Public Variables ------------------------------------------------- */
wc_control wc;
//ONE_LOG_MODULE_VARS_INIT;
/* ---- Private Constants and Types -------------------------------------- */
#define PRINT_FILE_BUFFER_LEN 2048

#define SUBST_MARKER    		'$'
#define VALID_MARKER    		'#'
#define SELECT_MARKER   		'&'
#define SEARCH_SEPARATOR    "/"

#define HIDDEN_PASSWORD 	"**********"
#define WC_MIME_BOUNDARY 	"my_boundary"

#if 1
#define FILENAME_SKB_CONF					"/nvdata/skb.conf"
#define FILENAME_USER_SUBSCRIBER_CONF		"/nvdata/user_subscriber.conf"
#define FILENAME_CONFIF_CFG					"/nvdata/config.cfg"
#define FILENAME_SIP_CONF					"/nvdata/sip.conf"
#define FILENAME_NETWORK_CONF				"/nvdata/network.conf"
#define FILENAME_QOS_CONF					"/nvdata/qos.conf"
#define FILENAME_AUDIO_CONTROL_CFG			"/nvdata/voip/WaveDeviceControlInfo.ini"
#define FILENAME_SYSINFO_CONF				"/etc/sysinfo.conf"
#define FILENAME_W300_CONF                  "/nvdata/skb_w300.conf"
#else
#define FILENAME_SKB_CONF					"/home/hlee/FSP_1.0/trunk/FMCSoftwarePlatform/src/Library/PlatformThirdPartyLibrary/WebServer/GoAHead2.5/www/cgi-bin/../config/kct.conf"
#define FILENAME_USER_SUBSCRIBER_CONF		"/home/hlee/FSP_1.0/trunk/FMCSoftwarePlatform/src/Library/PlatformThirdPartyLibrary/WebServer/GoAHead2.5/www/cgi-bin/../config/user_subscriber.conf"
#define FILENAME_CONFIF_CFG					"/home/hlee/FSP_1.0/trunk/FMCSoftwarePlatform/src/Library/PlatformThirdPartyLibrary/WebServer/GoAHead2.5/www/cgi-bin/../config/config.cfg"
#define FILENAME_SIP_CONF					"/home/hlee/FSP_1.0/trunk/FMCSoftwarePlatform/src/Library/PlatformThirdPartyLibrary/WebServer/GoAHead2.5/www/cgi-bin/../config/sip.conf"
#define FILENAME_NETWORK_CONF				"/home/hlee/FSP_1.0/trunk/FMCSoftwarePlatform/src/Library/PlatformThirdPartyLibrary/WebServer/GoAHead2.5/www/cgi-bin/../config/network.conf"
#define FILENAME_QOS_CONF					"/home/hlee/FSP_1.0/trunk/FMCSoftwarePlatform/src/Library/PlatformThirdPartyLibrary/WebServer/GoAHead2.5/www/cgi-bin/../config/qos.conf"
#define FILENAME_AUDIO_CONTROL_CFG			"/nvdata/voip/WaveDeviceControlInfo.ini"
#define FILENAME_SYSINFO_CONF				"/etc/sysinfo.conf"
#define FILENAME_W300_CONF                   "/home/hlee/FSP_1.0/trunk/FMCSoftwarePlatform/src/Library/PlatformThirdPartyLibrary/WebServer/GoAHead2.5/www/cgi-bin/../config/skb_w300.conf"
#endif

typedef enum
{
	PROF_NAME_LIST,
	PROF_GET_PROFILE,
	PROF_GET_ADHOC,
	PROF_UPDATE_PROFILE,
	PROF_UPDATE_ADHOC,
	PROF_DELETE_PROFILE,
	PROF_DELETE_ADHOC,
	PROF_SWAP_PROFILES,
} prof_action_t;

typedef enum
{
#if CFG_GLOBAL_SIP_SUPPORT
	PHONE_CFG_GET_SIP,
	PHONE_CFG_UPDATE_SIP,
	PHONE_CFG_RESET_SIP,
#endif
	PHONE_CFG_GET_AUDIO,
	PHONE_CFG_UPDATE_AUDIO,
	PHONE_CFG_RESET_AUDIO,

	PHONE_CFG_GET_PHONE,
	PHONE_CFG_UPDATE_PHONE,
	PHONE_CFG_RESET_PHONE,

	PHONE_CFG_GET_STUN,
	PHONE_CFG_UPDATE_STUN,
	PHONE_CFG_RESET_STUN,

	PHONE_W200_AUDIO_CFG_GET,
	PHONE_W200_AUDIO_CFG_SET,

	PHONE_W110_AUDIO_CFG_GET,
	PHONE_W110_AUDIO_CFG_SET,
	
	PHONE_CFG_SET_PHONE,
	PHONE_CFG_SET_SERVER,
	PHONE_CFG_SET_MESSAGE,
	
    PHONE_CFG_GET_DEVICE,
    PHONE_CFG_UPDATE_DEVICE,
    PHONE_CFG_RESET_DEVICE,

	PHONE_CFG_SET_WIFI,
} phone_cfg_action_t;

typedef enum
{
	DEST_VOIP,
	DEST_BIN,
	DEST_SBIN,
	DEST_CGI,
	DEST_WEBROOT_VIDEO,
	DEST_MOD,
	DEST_SD_VIDEO,
	DEST_LAST    /*  Last entry in the list.  */
} eFileDest;

typedef enum
{
	BYPASS = 0,
	RFC2833,
	INFO
} eDTMFMethod;

typedef enum
{
	TOS_ENABLE = 0,
	TOS_DISABLE
} eTosControl;

typedef enum
{
	VAD_ENABLE = 0,
	VAD_DISABLE
} eVadControl;

typedef enum
{
	ENC_DISABLE = 0,
	ENC_ENABLE
} eConfileFileEncControl;

typedef enum
{
	G711a = 0,
	G711u,
	G729,
    G711wa,
    G711wu,
    G722,
	CODEC_MAX,
} eCodecPriority;

typedef enum
{
	TAPS_DISABLE = 0,
	TAPS_ENABLE ,
} eTapsEnable;

typedef enum
{
	AREA_CODE_NONE = 0,
    AREA_CODE_02,
    AREA_CODE_031,
    AREA_CODE_032,
    AREA_CODE_033,
    AREA_CODE_041,
    AREA_CODE_042,
    AREA_CODE_043,
    AREA_CODE_051,
    AREA_CODE_052,
    AREA_CODE_053,
    AREA_CODE_054,
    AREA_CODE_055,
    AREA_CODE_061,
    AREA_CODE_062,
    AREA_CODE_063,
    AREA_CODE_070,
    AREA_CODE_MAX,
} eAreaCode;

typedef struct
{
	eTapsEnable         taps_active;
	char			    taps_address[CM_CFG_MAX_TEXT_LEN+1];
	int				    taps_port;
	int				    taps_retry_count;
	int				    taps_retry_time;
	eTapsEnable         taps_ssl_active;
	eAreaCode			area_code;
	eTapsEnable         telnet_active;
	int				    telnet_port;
	eTapsEnable         ntp_active;
	char				ntp_address1[CM_CFG_MAX_TEXT_LEN+1];
	char				ntp_address2[CM_CFG_MAX_TEXT_LEN+1];
} device_info;

typedef struct
{
	char  proxy_domain_name[CM_CFG_MAX_TEXT_LEN+1];
	char  sbc_outbound_proxy_address[CM_CFG_MAX_TEXT_LEN+1];
	char  sms_address[CM_CFG_MAX_TEXT_LEN+1];
	char  ssw_outbound_proxy_address[CM_CFG_MAX_TEXT_LEN+1];
	char  display_name[CM_CFG_MAX_TEXT_LEN+1];
	char  phone_number[CM_CFG_MAX_TEXT_LEN+1];
	char  auth_ID[CM_CFG_MAX_TEXT_LEN+1];
	char  password[CM_CFG_MAX_TEXT_LEN+1];
	eDTMFMethod  		dtmf_method;
	char  					expire_time[CM_CFG_MAX_TEXT_LEN+1];
	eTosControl 		tos_control;
	eVadControl 		vad_control;
	eCodecPriority  audio_codec[CODEC_MAX];
	char  					ps_sever_address[CM_CFG_MAX_TEXT_LEN+1];
	eConfileFileEncControl 	confile_file_enc;
	char  									p_device_uid[CM_CFG_MAX_TEXT_LEN+1];
} phone_info;

typedef struct
{
	char  input_login_pw[CM_CFG_MAX_TEXT_LEN+1];
	char  current_login_pw[CM_CFG_MAX_TEXT_LEN+1];
	char  new_login_pw[CM_CFG_MAX_TEXT_LEN+1];
    char  login_id[CM_CFG_MAX_TEXT_LEN+1];
} login_pw;

typedef struct
{
	char  wideband_voice_gain_tab[5][64];
	char  narrowband_voice_gain_tab[5][64];
	char  ringer_gain_tab[5][64];
	char  mic_gain[5];
	
	char  wideband_reg_r11[5];
	char  wideband_reg_r12[5];
	char  wideband_reg_r15[5];
	char  wideband_reg_r18[5];
	char  wideband_reg_r19[5];
	char  wideband_reg_r20[5];
	char  wideband_reg_r21[5];
	char  wideband_reg_r22[5];
	char  wideband_reg_r45[5];

	char  narrowband_reg_r11[5];
	char  narrowband_reg_r12[5];
	char  narrowband_reg_r15[5];
	char  narrowband_reg_r18[5];
	char  narrowband_reg_r19[5];
	char  narrowband_reg_r20[5];
	char  narrowband_reg_r21[5];
	char  narrowband_reg_r22[5];
	char  narrowband_reg_r45[5];
} at_audio_control;

static int fileDest = DEST_VOIP;
static char *fileDestStr[] = {
	"/voip",
	"/bin",
	"/sbin",
	"/webroot/cgi-bin",
	"/webroot/video",
	"/lib/modules/BROADCOM",
	"/mfs/sd/video",
	"/"
};

static cm_cfg_audio 	audio_cfg;
static cm_cfg_phone 	phone_cfg;
static int 						name_mod = 0;
static int 						phone_cfg_loaded = 0;
static int 						phone_cfg_changed = 0;

static char *						g_sValue;
static phone_info 			stPhoneInfo;
static login_pw 				stLoginPw;
static at_audio_control stAudioControl;
static device_info 			stDeviceInfo;

/* ---- Private Function Prototypes -------------------------------------- */

/* String functions */
static int 		streq (const char *from, const char *to);
static char * strnew (const char *from, int len);

/* Parsing functions */
static char * get_value_from_query (const char *query, char *name);
static int 		get_values_from_input (map_t *map);
static int 		get_real_char ( void );
static int 		get_pair_from_input (char **key, char **value);
static void 	wc_get_page_and_action ( void );

static void wc_show_table_start
(
 unsigned int border,
 unsigned int cellSpacing,
 unsigned int cellPadding
 );
 
static void wc_show_table_end( void );
static void wc_show_table_row_start( void );
static void wc_show_table_row_end( void );
static void wc_show_table_data_cell( const char *attributes, const char *displayStr );
static void wc_show_status ( int debug_info );
static void wc_show_summary_status ( void );
static void wc_show_cpu_usage( void );

static void wc_print( char *msg );
static void wc_regular_print( char *msg );
static void wc_show_top( void );
static void wc_show_header ( void );
static void wc_show_footer ( void );
/* Printing functions */
static void print_file (const char *filename);
static void print_call_log (const char *filename);
static void subst_and_print_file (const char *filename, map_t *map);
static void print_str_value (map_t *map_entry, const char *subvalue);
static void print_ip_address (map_t *map_entry, const char *subvalue);
static void print_password_value (map_t *map_entry, const char *subvalue);
static void print_checked_value (map_t *map_entry, const char *subvalue);
static void print_num_value (map_t *map_entry, const char *subvalue);
static void set_search_keys (map_t *map);
static int equal_str_value (const value_t *value1, const value_t *value2, loc_t location);
static int equal_name_value (const value_t *value1, const value_t *value2, loc_t location);
static int equal_num_value (const value_t *value1, const value_t *value2, loc_t location);

static int parse_dtmf_method_select_value (char *str, value_t *value, value_t *old_value, loc_t location);
static int parse_tos_control_select_value (char *str, value_t *value, value_t *old_value, loc_t location);
static int parse_vad_control_select_value (char *str, value_t *value, value_t *old_value, loc_t location);
static int parse_codec_perioity_control_select_value (char *str, value_t *value, value_t *old_value, loc_t location);
static int parse_config_file_enc_control_select_value (char *str, value_t *value, value_t *old_value, loc_t location);
static int parse_taps_value(char *str, value_t *value, value_t *old_value, loc_t location);
static void parse_taps_area_code(char *str, value_t *value, value_t *old_value, loc_t location);
static void parse_taps_int_value(char *str, value_t *value, value_t *old_value, loc_t location);

static void print_dtmf_method_select_value (map_t *map_entry, const char *subvalue);
static void print_tos_select_value (map_t *map_entry, const char *subvalue);
static void print_vad_select_value (map_t *map_entry, const char *subvalue);
static void print_codec_perioity_select_value (map_t *map_entry, const char *subvalue);
static void print_config_file_enc_select_value (map_t *map_entry, const char *subvalue);

static void print_config_area_code(map_t *map_entry, const char *subvalue);
static void print_config_file_bool_active_device (map_t *map_entry, const char *subvalue);
static void print_int_value(map_t *map_entry, const char *subvalue);

static int phone_setting_api(phone_cfg_action_t action);
static int device_setting_api(phone_cfg_action_t action);

static int profile_api (prof_action_t action, int prof_num, int prof_num2);
static int phone_cfg_api (phone_cfg_action_t action);
/* Map functions */
static void initialize_map (map_t *map);
static void get_internal_values (map_t *map);

static void wc_submit_phone_config ( void );
static void wc_submit_device_config(void);

static map_t device_info_map[] =
{
	{
section: "device_setting",
key: "taps_active",
type: TYPE_NUM,
location: LOC_LOCAL_VAR,
default_value: { num: TAPS_ENABLE },
value: {num_ptr: (int *) &stDeviceInfo.taps_active},
print_fn: print_config_file_bool_active_device,
parse_fn: parse_taps_value,
validate_fn: NULL,
equal_fn: equal_num_value,
valid: 1,
	},
	{
section: "device_setting",
key: "taps_address",
type: TYPE_STRING,
location: LOC_LOCAL_VAR,
default_value: { str: NULL },
value: {str: stDeviceInfo.taps_address},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: NULL,
valid: 1,
	},
	{
section: "device_setting",
key: "taps_port",
type: TYPE_NUM,
location: LOC_LOCAL_VAR,
default_value: { num: 19700 },
value: {num_ptr: (int *) &stDeviceInfo.taps_port},
print_fn: print_int_value,
parse_fn: parse_taps_int_value,
validate_fn: NULL,
equal_fn: NULL,
valid: 1,
	},
	{
section: "device_setting",
key: "taps_retry_count",
type: TYPE_NUM,
location: LOC_LOCAL_VAR,
default_value: { num: 3 },
value: {num_ptr: (int *) &stDeviceInfo.taps_retry_count},
print_fn: print_int_value,
parse_fn: parse_taps_int_value,
validate_fn: NULL,
equal_fn: NULL,
valid: 1,
	},
	{
section: "device_setting",
key: "taps_retry_time",
type: TYPE_NUM,
location: LOC_LOCAL_VAR,
default_value: { num: 5 },
value: {num_ptr: (int *) &stDeviceInfo.taps_retry_time},
print_fn: print_int_value,
parse_fn: parse_taps_int_value,
validate_fn: NULL,
equal_fn: NULL,
valid: 1,
	},
	{
section: "device_setting",
key: "taps_ssl_active",
type: TYPE_NUM,
location: LOC_LOCAL_VAR,
default_value: { num: TAPS_ENABLE },
value: {num_ptr: (int *) &stDeviceInfo.taps_ssl_active},
print_fn: print_config_file_bool_active_device,
parse_fn: parse_taps_value,
validate_fn: NULL,
equal_fn: NULL,
valid: 1,
	},
	{
section: "device_setting",
key: "AreaCode",
type: TYPE_NUM,
location: LOC_LOCAL_VAR,
default_value: { num: AREA_CODE_070 },
value: {num_ptr: (int *) &stDeviceInfo.area_code},
print_fn: print_config_area_code,
parse_fn: parse_taps_area_code,
validate_fn: NULL,
equal_fn: NULL,
valid: 1,
	},
	{
section: "device_setting",
key: "telnet_active",
type: TYPE_NUM,
location: LOC_LOCAL_VAR,
default_value: { num: TAPS_ENABLE },
value: {num_ptr: (int *) &stDeviceInfo.telnet_active},
print_fn: print_config_file_bool_active_device,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: NULL,
valid: 1,
	},	
	{
section: "device_setting",
key: "telnet_port",
type: TYPE_NUM,
location: LOC_LOCAL_VAR,
default_value: { num: 6000 },
value: {num_ptr: (int *) &stDeviceInfo.telnet_port},
print_fn: print_int_value,
parse_fn: parse_taps_int_value,
validate_fn: NULL,
equal_fn: NULL,
valid: 1,
	},
	{
section: "device_setting",
key: "ntp_active",
type: TYPE_NUM,
location: LOC_LOCAL_VAR,
default_value: { num: TAPS_ENABLE },
value: {num_ptr: (int *) &stDeviceInfo.ntp_active},
print_fn: print_config_file_bool_active_device,
parse_fn: parse_taps_value,
validate_fn: NULL,
equal_fn: NULL,
valid: 1,
	},
	{
section: "device_setting",
key: "ntp_address1",
type: TYPE_STRING,
location: LOC_LOCAL_VAR,
default_value: { str: NULL },
value: {str: stDeviceInfo.ntp_address1},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
valid: 1,
	},
	{
section: "device_setting",
key: "ntp_address2",
type: TYPE_STRING,
location: LOC_LOCAL_VAR,
default_value: { str: NULL },
value: {str: stDeviceInfo.ntp_address2},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: NULL,
valid: 1,
	},
	{
section: NULL,
		 }
};

static map_t top_map[] =
{
	/* ip */
	{
section: "top",
key: "ip",
type: TYPE_STRING,
default_value: { num: BYPASS },
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: NULL,
	},

	/* Terminating entry */
	{
section: NULL,
		 }
};

static map_t at_audio_control_map[] =
{
	{
section: "at_audio_control",
key: "wide_ear_gain_tab1",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.wideband_voice_gain_tab[0]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "wide_ear_gain_tab2",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.wideband_voice_gain_tab[1]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "wide_ear_gain_tab3",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.wideband_voice_gain_tab[2]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "wide_ear_gain_tab4",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.wideband_voice_gain_tab[3]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "wide_ear_gain_tab5",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.wideband_voice_gain_tab[4]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "narrow_ear_gain_tab1",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.narrowband_voice_gain_tab[0]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "narrow_ear_gain_tab2",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.narrowband_voice_gain_tab[1]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "narrow_ear_gain_tab3",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.narrowband_voice_gain_tab[2]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "narrow_ear_gain_tab4",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.narrowband_voice_gain_tab[3]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "narrow_ear_gain_tab5",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.narrowband_voice_gain_tab[4]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "ringer_gain_tab1",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.ringer_gain_tab[0]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },

		 {
section: "at_audio_control",
key: "ringer_gain_tab2",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.ringer_gain_tab[1]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },

		 {
section: "at_audio_control",
key: "ringer_gain_tab3",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.ringer_gain_tab[2]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },

		 {
section: "at_audio_control",
key: "ringer_gain_tab4",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.ringer_gain_tab[3]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },

		 {
section: "at_audio_control",
key: "ringer_gain_tab5",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.ringer_gain_tab[4]},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },

		 {
section: "at_audio_control",
key: "mic_gain",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.mic_gain},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },
		 
	{
section: "at_audio_control",
key: "wideband_reg_r11",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.wideband_reg_r11},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "wideband_reg_r12",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.wideband_reg_r12},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},	

	{
section: "at_audio_control",
key: "wideband_reg_r15",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.wideband_reg_r15},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "wideband_reg_r18",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.wideband_reg_r18},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "wideband_reg_r19",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.wideband_reg_r19},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "wideband_reg_r20",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.wideband_reg_r20},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "wideband_reg_r21",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.wideband_reg_r21},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "wideband_reg_r22",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.wideband_reg_r22},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "wideband_reg_r45",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.wideband_reg_r45},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "narrowband_reg_r11",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.narrowband_reg_r11},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "narrowband_reg_r12",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.narrowband_reg_r12},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},	

	{
section: "at_audio_control",
key: "narrowband_reg_r15",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.narrowband_reg_r15},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "narrowband_reg_r18",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.narrowband_reg_r18},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "narrowband_reg_r19",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.narrowband_reg_r19},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "narrowband_reg_r20",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.narrowband_reg_r20},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "narrowband_reg_r21",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.narrowband_reg_r21},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "narrowband_reg_r22",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.narrowband_reg_r22},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "at_audio_control",
key: "narrowband_reg_r45",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stAudioControl.narrowband_reg_r45},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

		 /* Terminating entry */
		 {
section: NULL,
		 }
};

static map_t login_pw_map[] =
{
	{
section: "login_pw",
key: "input_pw",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stLoginPw.input_login_pw},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "login_pw",
key: "current_pw",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stLoginPw.current_login_pw},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },

		 {
section: "login_pw",
key: "new_pw",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stLoginPw.new_login_pw},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },
		 {
section: "login_id",
key: "input_id",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stLoginPw.login_id},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },
		 /* Terminating entry */
		 {
section: NULL,
		 }
};

static map_t phone_setting_map[] =
{
	{
section: "phone_setting",
key: "proxy_domain_name",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stPhoneInfo.proxy_domain_name},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
	},

	{
section: "phone_setting",
key: "sbc_outbound_proxy_address",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stPhoneInfo.sbc_outbound_proxy_address},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "sms_address",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stPhoneInfo.sms_address},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "ssw_outbound_proxy_address",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stPhoneInfo.ssw_outbound_proxy_address},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },
		 ///
		 {
section: "phone_setting",
key: "display_name",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stPhoneInfo.display_name},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "phone_number",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stPhoneInfo.phone_number},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "auth_ID",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stPhoneInfo.auth_ID},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "password",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stPhoneInfo.password},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "dtmf_method",
location: LOC_LOCAL_VAR,
type: TYPE_NUM,
default_value: { num: BYPASS },
value: { num_ptr: (int *) &stPhoneInfo.dtmf_method },
print_fn: print_dtmf_method_select_value,
parse_fn: parse_dtmf_method_select_value,
validate_fn: NULL,
equal_fn: equal_num_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "expire_time",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stPhoneInfo.expire_time},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "tos_control",
location: LOC_LOCAL_VAR,
type: TYPE_NUM,
default_value: { num: TOS_ENABLE },
value: { num_ptr: (int *) &stPhoneInfo.tos_control },
print_fn: print_tos_select_value,
parse_fn: parse_tos_control_select_value,
validate_fn: NULL,
equal_fn: equal_num_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "vad_control",
location: LOC_LOCAL_VAR,
type: TYPE_NUM,
default_value: { num: VAD_ENABLE },
value: { num_ptr: (int *) &stPhoneInfo.vad_control },
print_fn: print_vad_select_value,
parse_fn: parse_vad_control_select_value,
validate_fn: NULL,
equal_fn: equal_num_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "audio_codec1",
location: LOC_LOCAL_VAR,
type: TYPE_NUM,
default_value: { num: G711a },
value: { num_ptr: (int *) &stPhoneInfo.audio_codec[0] },
print_fn: print_codec_perioity_select_value,
parse_fn: parse_codec_perioity_control_select_value,
validate_fn: NULL,
equal_fn: equal_num_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "audio_codec2",
location: LOC_LOCAL_VAR,
type: TYPE_NUM,
default_value: { num: G711u },
value: { num_ptr: (int *) &stPhoneInfo.audio_codec[1] },
print_fn: print_codec_perioity_select_value,
parse_fn: parse_codec_perioity_control_select_value,
validate_fn: NULL,
equal_fn: equal_num_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "audio_codec3",
location: LOC_LOCAL_VAR,
type: TYPE_NUM,
default_value: { num: G729 },
value: { num_ptr: (int *) &stPhoneInfo.audio_codec[2] },
print_fn: print_codec_perioity_select_value,
parse_fn: parse_codec_perioity_control_select_value,
validate_fn: NULL,
equal_fn: equal_num_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "ps_sever_address",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stPhoneInfo.ps_sever_address},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "confile_file_enc",
location: LOC_LOCAL_VAR,
type: TYPE_NUM,
default_value: { num: ENC_ENABLE},
value: { num_ptr: (int *) &stPhoneInfo.confile_file_enc },
print_fn: print_config_file_enc_select_value,
parse_fn: parse_config_file_enc_control_select_value,
validate_fn: NULL,
equal_fn: equal_num_value,
valid: 1,
		 },

		 {
section: "phone_setting",
key: "p_device_uid",
location: LOC_LOCAL_VAR,
type: TYPE_STRING,
default_value: { str: NULL },
value: {str: stPhoneInfo.p_device_uid},
print_fn: print_str_value,
parse_fn: NULL,
validate_fn: NULL,
equal_fn: equal_str_value,
valid: 1,
		 },


		 /* Terminating entry */
		 {
section: NULL,
		 }
};

//------------------------------------------------------------
// rtp statistics names table
const char *rtpStatNames[] =
{
	"igrRtpPkt",
	"igrRtpByte",
	"egrRtpPkt",
	"egrRtpByte",
	"egrRtpCumLostPkt",
	"egrRtpDiscardPkt",
	"igrRtpNoProfDiscPkt",
	"igrRtcpPkt",
	"egrRtcpPkt",
	"egrRtpJitter",
	"igrRtpLatency",
	"egrRtpSesLostPkt",
	"igrRtpBitRate",
	"igrRtpPeakBitRate",
	"egrRtpBitRate",
	"egrRtpPeakBitRate"
};

const char *hostUsageStatNames[] =
{
	"Host min (%)",
	"Host max (%)",
	"Host avg (%)",
	"Host curr (%)",
	"Usage history[1] (%)",
	"Usage history[2] (%)",
	"Usage history[3] (%)",
	"Usage history[4] (%)",
	"Usage history[5] (%)",
	"Usage history[6] (%)",
	"Usage history[7] (%)",
	"Usage history[8] (%)",
	"Usage history[9] (%)",
	"Usage history[10] (%)",
};

//------------------------------------------------------------
// pve statistics names table
const char *pveStatNames[] =
{
	"peakHoldingTime",
	"packetCount",
	"addTailCount",
	"reorderCount",
	"overrunCount",
	"duplicateCount",
	"outOfRangeCount",
	"tooLateCount",
	"cantDecodeCount",
	"ajcUnderrunCount",
	"ajcDeleteCount",
	"ajcRepeatCount",
	"ajcResyncCount",
	"ajcPhaseJitterCount",
	"ajcHdrPayloadDiff",
	"ajcPayloadOverflowE",
	"inactiveFrameCount",
	"activeFrameCount",
	"sidFrameCount",
	"toneRelayCount"
};
//------------------------------------------------------------
// pve state names table
const char *pveStateNames[] =
{
	"timer",
	"expectedTimestamp",
	"lastArrivalTime",
	"lastArrTimeStamp",
	"lastReleaseTimestamp",
	"actualHoldTime",
	"currentHoldTime",
	"minHoldTimeTracker",
	"maxHoldTimeTracker",
	"releaseBump",
	"underrunDuration",
	"actionPendingTimer",
	"currentDec",
	"timerLastArrival",
	"currentEnc",
	"currentEncPktTime",
	"currentEncPktSize"
};

#define STATS_IS_AUDIO_FIELD( strName ) ( *strName == 'a' )
#define STATS_IS_VIDEO_FIELD( strName ) ( *strName == 'v' )
#define STATS_DISPLAY_NAME( strName )   ( &strName[2] )

const char *gMediaStatusNames[] =
{
	"v/Encoder frame-rate (fps)",
	"v/Decoder frame-rate (fps)",
	"v/Encoder bit-rate (bps)",
	"v/Decoder bit-rate (bps)",
	"a/Encoder bit-rate (bps)",
	"a/Decoder bit-rate (bps)",
	"a/End-to-end delay (ms)",
	"v/End-to-end delay (ms)",
	"a/Jitter delay (ms)",
	"v/Jitter delay (ms)",
	"a/Voice sync (ms)",
	"v/Video sync (ms)",
	"a/Active level ",
	"a/Auto gain ",
	"a/AEC enable",
	"a/ECAN ERL (dB)",
	"a/ECAN ERLE (dB)",
	//"Voice packet loss (%)",
	//"Video packet loss (%)",
};

#define MAX_AUDIO_NB_CODEC    26
const char *audioNBCodecName[] =
{
	"g711 ulaw",
	"g711 alaw",
	"g726-16",
	"g726-24",
	"g726-32",
	"g726-40",
	"g729a",
	"g729",
	"g729e",
	"g7231-53",
	"g7231-63",
	"g728",
	"bv16",
	"Linear-128",
	"ilbc-20",
	"ilbc-30",
	"gsmfr-13",
	"gsmefr-122",
	"gsmamr-475",
	"gsmamr-515",
	"gsmamr-590",
	"gsmamr-670",
	"gsmamr-740",
	"gsmamr-795",
	"gsmamr-102",
	"gsmamr-122"
};

#define AUDIO_WB_OFFSET 36
const char *audioWBCodecName[] =
{
	"g711-wb ulaw",
	"g711-wb alaw",
	"bv32",
	"Linear-256",
	"g722-1",
	"g722-2",
	"g722-3",
	"g7221-24",
	"g7221-32",
	"amr-wb-660",
	"amr-wb-885",
	"amr-wb-1265",
	"amr-wb-1425",
	"amr-wb-1585",
	"amr-wb-1825",
	"amr-wb-1985",
	"amr-wb-2305",
	"amr-wb-2385",
	"none"
};


/*
* set_search_keys - set search keys for configuration parameters
*/
static void set_search_keys (map_t *map)
{
	if (!map)
		return;

	/* Loop through entire map list */
	while (map->section && map->key)
	{
		map->search_key = strnew (map->section,
			strlen(map->section) + strlen(SEARCH_SEPARATOR) + strlen(map->key));
		strcat (map->search_key, SEARCH_SEPARATOR);
		strcat (map->search_key, map->key);
		map->search_key_len = strlen (map->search_key);
		map++;
	}
}

/*
* print_file - print file to STDOUT
*/
static void print_file (const char *filename)
{
	FILE *file;
	char buffer[PRINT_FILE_BUFFER_LEN];
	int len;

	if (!filename)
		return;

	file = fopen (filename, "r");
	if (!file)
	{
		wc_print("fopen Error");
		return;
	}

	len = PRINT_FILE_BUFFER_LEN;
	while (len == PRINT_FILE_BUFFER_LEN)
	{
		len = fread (buffer, sizeof(char), PRINT_FILE_BUFFER_LEN, file);
		if (len > 0)
		{
			fwrite (buffer, sizeof(char), len, stdout);
		}
	}

	fclose(file);
}

static void print_call_log (const char *filename)
{
	FILE *fp;
	char buffer[256];//, orig[256], *token, *line;//, *sub_line;
	//char sValue[256];

	if (!filename)
		return;

	fp = fopen (filename, "r");
	if (!fp)
	{
		wc_print("fopen Error");
		return;
	}

	while (fgets(buffer, 256, fp)) {
		if(strchr(buffer, '\n')) 
		{
			//*(strchr(buffer, '\n')) = '\0';
			//sprintf(sValue, "%s\n", buffer);
			//fprintf(stdout, sValue);
			wc_regular_print(buffer);
		}
	}

	fclose(fp);
}

/*
* subst_and_print_file - print file to STDOUT with substituted values in map
*/

static void subst_and_print_file (const char *filename, map_t *map)
{
	FILE *file;
	int buffer[PRINT_FILE_BUFFER_LEN];
	char value[PRINT_FILE_BUFFER_LEN];
	int matched_index;
	int ch;
	int marker;
	int matching;
	map_t *map_entry;

	if (!filename)
		return;

	file = fopen (filename, "r");
	if (!file)
	{
		return;
	}

	marker = 0;
	matching = 0;
	matched_index = 0;
	while ((ch = getc (file)) != EOF)
	{
		if (!matching)
		{
			if ((ch != SUBST_MARKER) && (ch != VALID_MARKER) && (ch != SELECT_MARKER))
				putc (ch, stdout);
			else
			{
				/* save marker */
				marker = ch;
				matched_index = 0;

				/* set matching flags in map */
				matching = 1;
				for (map_entry = map; map_entry->section; map_entry++)
				{
					map_entry->matching = 1;
				}
			}
		}
		else
		{
			buffer[matched_index++] = ch;

			/* try to match substitution strings */
			matching = 0;
			for (map_entry = map; map_entry->section; map_entry++)
			{
				if (map_entry->matching)
				{
					if ((int)(map_entry->search_key[matched_index - 1]) == ch)
					{
						if (matched_index == map_entry->search_key_len)
						{
							/* Get trailing marker */
							ch = getc (file);
							if (ch == EOF)
							{
								matching = 0;
								break;
							}

							buffer[matched_index++] = ch;

							/* Match trailing marker */
							if (ch == marker)
							{
								/* match found */
								switch (marker)
								{
								case SUBST_MARKER:
									if (map_entry->valid)
										if (map_entry->print_fn)
											map_entry->print_fn(map_entry, NULL);
										else
											printf("%s", map_entry->value.str);
									else
										printf ("%s", map_entry->input_value);
									marker = 0;
									matched_index = 0;
									break;
								case VALID_MARKER:
									printf (map_entry->valid?"valid":"invalid");
									marker = 0;
									matched_index = 0;
									break;
								case SELECT_MARKER:
									/* get value for select marker */
									{
										int value_index;

										value_index = 0;
										value[0] = '\0';
										while ((ch = getc (file)) != EOF)
										{
											buffer[matched_index++] = ch;
											if (ch == SELECT_MARKER)
												break;
											value[value_index++] = (char)ch;
										}

										if (ch != EOF)
										{
											value[value_index] = '\0';
											/* value marker found, now print it */
											if (map_entry->print_fn)
											{
												map_entry->print_fn(map_entry, value);
												marker = 0;
												matched_index = 0;
											}
										}
									}
									break;
								default:
									break;
								}
								matching = 0;
								break;
							}
							else
							{
								/* This entry no longer matches */
								map_entry->matching = 0;
							}
						}
					}
					else
					{
						map_entry->matching = 0;
					}
				}
				matching = matching || map_entry->matching;
			}

			if (!matching)
			{
				int j;
				/* none matched, dump buffer */
				if (marker)
					putc (marker, stdout);
				for (j = 0; j < matched_index; j++)
				{
					putc (buffer[j], stdout);
				}
			}

		}
	}

	/* Dump characters left in matching buffer */
	if (matching)
	{
		int j;
		if (marker)
			putc (marker, stdout);
		for (j = 0; j < matched_index; j++)
		{
			putc (buffer[j], stdout);
		}
	}

	fclose(file);
}

static void print_str_value (map_t *map_entry, const char *subvalue)
{
	(void)subvalue;
	if (map_entry && map_entry->value.str)
	{
		printf ("%s", map_entry->value.str);
	}
}

static void print_int_value(map_t *map_entry, const char *subvalue)
{
    (void)subvalue;
    if (map_entry && map_entry->value.num_ptr)
    {
        printf ("%d", *(map_entry->value.num_ptr));        
    }
}

static int equal_str_value (const value_t *value1, const value_t *value2, loc_t location)
{
	(void)location;
	if (!value1 || !value2)
		return 0;

	if (!value1->str || !value2->str)
		return 0;

	return streq(value1->str, value2->str);
}

static int equal_name_value (const value_t *value1, const value_t *value2, loc_t location)
{
	int compare_result;

	(void)location;
	if (!value1 || !value2)
		return 0;

	if (!value1->str || !value2->str)
		return 0;

	compare_result = streq(value1->str, value2->str);
	if (!compare_result)
	{
		name_mod = 1;
	}
	return compare_result;
}

static int equal_num_value (const value_t *value1, const value_t *value2, loc_t location)
{
	if (!value1 || !value2)
		return 0;

	if (location == LOC_LOCAL_VAR)
		return (*(value1->num_ptr) == *(value2->num_ptr));

	return (value1->num == value2->num);
}

static void wc_show_phone_config ( void )
{
	initialize_map (phone_setting_map);

	/* Generate substitute markers */
	set_search_keys (phone_setting_map);

	if (phone_setting_api(PHONE_CFG_GET_PHONE) < 0)
		return;

	/* Substitute page with current values */
	subst_and_print_file (WC_PHONE_CONFIG_HTML, phone_setting_map);
}

static void wc_show_server_config ( void )
{
	initialize_map (phone_setting_map);

	/* Generate substitute markers */
	set_search_keys (phone_setting_map);

	if (phone_setting_api(PHONE_CFG_GET_PHONE) < 0)
		return;

	/* Substitute page with current values */
	subst_and_print_file (WC_SERVER_CONFIG_HTML, phone_setting_map);
}

static void wc_show_msg_config ( void )
{
	initialize_map (phone_setting_map);

	/* Generate substitute markers */
	set_search_keys (phone_setting_map);

	if (phone_setting_api(PHONE_CFG_GET_PHONE) < 0)
		return;

	/* Substitute page with current values */
	subst_and_print_file (WC_MSG_CONFIG_HTML, phone_setting_map);
}

static void wc_show_device_config ( void )
{
	initialize_map (device_info_map);

	/* Generate substitute markers */
	set_search_keys (device_info_map);

	if (device_setting_api(PHONE_CFG_GET_DEVICE) < 0)
		return;

	/* Substitute page with current values */
	subst_and_print_file (WC_DEVICE_CONFIG_HTML, device_info_map);
}

static void wc_show_wifi_config ( void )
{
	initialize_map (phone_setting_map);

	/* Generate substitute markers */
	set_search_keys (phone_setting_map);

	if (phone_setting_api(PHONE_CFG_GET_PHONE) < 0)
		return;

	/* Substitute page with current values */
	subst_and_print_file (WC_PHONE_CONFIG_HTML, phone_setting_map);
}

static void wc_show_w200_audio_config ( void )
{
	initialize_map (at_audio_control_map);

	/* Generate substitute markers */
	set_search_keys (at_audio_control_map);

//	if (w200_audio_setting_api(PHONE_W200_AUDIO_CFG_GET) < 0)
	{
		wc_print("Config File Not Found!!");
//		return;
	}

	/* Substitute page with current values */
	subst_and_print_file (WC_W200_AUDIO_CONFIG_HTML, at_audio_control_map);
}

static void wc_show_w110_audio_config ( void )
{
	initialize_map (at_audio_control_map);

	/* Generate substitute markers */
	set_search_keys (at_audio_control_map);

//	if (w110_audio_setting_api(PHONE_W110_AUDIO_CFG_GET) < 0)
	{
		wc_print("Config File Not Found!!");
//		return;
	}

	/* Substitute page with current values */
	subst_and_print_file (WC_W110_AUDIO_CONFIG_HTML, at_audio_control_map);
}

static void wc_show_login_pw_config ( void )
{
	initialize_map (login_pw_map);

	/* Generate substitute markers */
	set_search_keys (login_pw_map);
	memset(stLoginPw.input_login_pw, 0x00, strlen(stLoginPw.input_login_pw));

	/* Substitute page with current values */
	subst_and_print_file (WC_LOGIN_HTML, login_pw_map);
}

static void wc_show_status ( int debug_info )
{
	char *p;
	char buf[160] = {0, };
	char strBuf[256] = {0, };
	char buf2[20] = {0, };
	struct in_addr addr;

	wc_print("[Phone Status]");
	memset(buf, 0x00, sizeof(buf));
	config_find_by_name(FILENAME_SIP_CONF, "RegSts", 0, buf);
	if(atoi(buf) == 1)
	{
		wc_regular_print("상태: 서비스 등록"); wc_regular_print("");

		memset(buf, 0x00, sizeof(buf));
		config_find_by_name(FILENAME_SIP_CONF, "id", 0, buf);	
		sprintf(strBuf, "전화번호: %s", buf); 
		wc_regular_print(strBuf); wc_regular_print("");
	}
	else if(atoi(buf) == 2)
	{
		wc_regular_print("상태: 서비스 미등록"); wc_regular_print("");
	}

    memset(buf, 0x00, sizeof(buf));
    config_find_by_name(FILENAME_SYSINFO_CONF, "sysDescr_version", 0, buf);   //sw version
    sprintf(strBuf, "Firmware Version : %s", buf);
    wc_regular_print(strBuf);wc_regular_print("");

    memset(buf, 0x00, sizeof(buf));
    config_find_by_name(FILENAME_SYSINFO_CONF, "sysDescr_date", 0, buf);   //sw release date
    sprintf(strBuf, "Firmware Release Data : %s", buf);
    wc_regular_print(strBuf);wc_regular_print("");

    memset(buf, 0x00, sizeof(buf));
    config_find_by_name(FILENAME_SYSINFO_CONF, "hardware_version", 0, buf);   //HW Version
    sprintf(strBuf, "HW Version: %s", buf);
    wc_regular_print(strBuf);wc_regular_print("");
    
    /* Print active image status.. */
	wc_print("[Network Status]");

	p = (getipaddr(&addr.s_addr) > 0) ? inet_ntoa( addr ) : "";
	sprintf( buf, "IP address: %s", p);
	wc_regular_print( buf );wc_regular_print("");

	getethaddr(buf2);
	sprintf( buf, "MAC address: %02X:%02X:%02X:%02X:%02X:%02X",
		buf2[0]&0xff, buf2[1]&0xff, buf2[2]&0xff,
		buf2[3]&0xff, buf2[4]&0xff, buf2[5]&0xff);
	wc_regular_print( buf );wc_regular_print("");

	memset(buf, 0x00, sizeof(buf));
	config_find_by_name(FILENAME_NETWORK_CONF, "netmask", 0, buf);
	sprintf(strBuf, "Netmask : %s", buf);
	wc_regular_print(strBuf);wc_regular_print("");

    memset(buf, 0x00, sizeof(buf));
    config_find_by_name(FILENAME_NETWORK_CONF, "gateway",0, buf);
    sprintf(strBuf, "Gateway : %s", buf);
    wc_regular_print(strBuf);wc_regular_print("");

    memset(buf, 0x00, sizeof(buf));
    config_find_by_name(FILENAME_NETWORK_CONF, "dns1",0, buf);
    sprintf(strBuf, "DNS 1 : %s", buf);
    wc_regular_print(strBuf);wc_regular_print("");

    memset(buf, 0x00, sizeof(buf));
    config_find_by_name(FILENAME_NETWORK_CONF, "dns2",0, buf);
    sprintf(strBuf, "DNS 2 : %s", buf);
    wc_regular_print(strBuf);wc_regular_print("");

	memset(buf, 0x00, sizeof(buf));
	config_find_by_name(FILENAME_NETWORK_CONF, "ssid", 0, buf);
	sprintf(strBuf, "SSID : %s", buf);
	wc_regular_print(strBuf);wc_regular_print("");

	memset(buf, 0x00, sizeof(buf));
	config_find_by_name(FILENAME_NETWORK_CONF, "ap_mac_addr", 0, buf);
	sprintf(strBuf, "AP MAC address : %s", buf);
	wc_regular_print(strBuf);wc_regular_print("");

    memset(buf, 0x00, sizeof(buf));
    config_find_by_name(FILENAME_NETWORK_CONF, "rssi", 0, buf);
    sprintf(strBuf, "WLAN Strength : %s", buf);
    wc_regular_print(strBuf);wc_regular_print("");

    memset(buf, 0x00, sizeof(buf));
    config_find_by_name(FILENAME_NETWORK_CONF, "received_packet", 0, buf);
    sprintf(strBuf, "수신 패킷 : %s", buf);
    wc_regular_print(strBuf);wc_regular_print("");

    memset(buf, 0x00, sizeof(buf));
    config_find_by_name(FILENAME_NETWORK_CONF, "transmitted_packet", 0, buf);
    sprintf(strBuf, "발신 패킷 : %s", buf);
    wc_regular_print(strBuf);wc_regular_print("");


	if(!debug_info)
	{
		return;
	}

}
/*
* streq - check if two strings are equal
*/
static int streq (const char *from, const char *to)
{
	if (!from || !to)
		return 0;

	return (strcasecmp (from, to) == 0);
}

/*
* strnew - allocate and assign string
*/
static char * strnew (const char *from, int len)
{
	char *value;

	struct pool_list_t *pool;
	struct pool_list_t **poolp;

	/* adjust for NULL terminator */
	len++;

	if (len > POOL_BUFFER_SIZE)
	{
		printf("String length %d too large\n", len);
		return NULL;
	}

	poolp = &wc.pool_list;
	pool = *poolp;

	/* find pool with enough room */
	while (pool && (pool->free < len))
	{
		poolp = &(pool->next);
		pool = *poolp;
	}

	/* malloc new pool */
	if (!pool)
	{
		pool = malloc (POOL_MALLOC_SIZE);
		memset (pool, 0, POOL_MALLOC_SIZE);
		if (!pool)
		{
			printf("Failed to allocate %d bytes of memory\n", POOL_MALLOC_SIZE);
			return NULL;
		}
		pool->next = NULL;
		pool->free = POOL_MALLOC_SIZE - ((uint32_t)(pool->buffer) - (uint32_t)pool);
		pool->freep = pool->buffer;

		/* attach pool to list */
		*poolp = pool;
	}

	/* found pool, now allocate space for string */
	value = pool->freep;
	pool->free -= len;
	pool->freep += len;

	/* initialize new string */
	if (from)
	{
		memcpy (value, from, len - 1);
		value[len] = '\0';
	}
	else
	{
		value[0] = '\0';
	}

	return value;
}

/*
* get_value_from_query - get parameter from query string
*/
static char * get_value_from_query (const char *query, char *name)
{
	const char *value;
	char *end;
	char *newvalue;
	int namelen;
	int valuelen;


	if (!query || !name)
		return NULL;

	namelen = strlen(name);

	/* search for "name=" in query */
	value = query;
	while (*value)
	{
		value = strstr(value, name);
		if (!value)
			return NULL;

		value += namelen;
		if (*value == '=')
		{
			break;
		}
		else
		{
			value++;
		}
	}

	/* "name=" found, now extract value */
	value++;
	end = strchr(value, '&');
	if (!end)
	{
		valuelen = strlen(value);
	}
	else
	{
		valuelen = end - value;
	}

	newvalue = strnew(value, valuelen);

	return newvalue;
}

static int parse_dtmf_method_select_value (char *str, value_t *value, value_t *old_value, loc_t location)
{
	int *value_ptr;

	(void)old_value;
	if (location == LOC_LOCAL_VAR)
		value_ptr = value->num_ptr;
	else
		value_ptr = &(value->num);

	if (!strcmp(str, "bypass"))
	{
		*value_ptr = BYPASS;
	}
	else if (!strcmp(str, "rfc2833"))
	{
		*value_ptr = RFC2833;
	}
	else if (!strcmp(str, "info"))
	{
		*value_ptr = INFO;
	}

	return 1;
}

static int parse_tos_control_select_value (char *str, value_t *value, value_t *old_value, loc_t location)
{
	int *value_ptr;

	(void)old_value;
	if (location == LOC_LOCAL_VAR)
		value_ptr = value->num_ptr;
	else
		value_ptr = &(value->num);

	if (!strcmp(str, "Enable"))
	{
		*value_ptr = TOS_ENABLE;
	}
	else if (!strcmp(str, "Disable"))
	{
		*value_ptr = TOS_DISABLE;
	}

	return 1;
}

static int parse_vad_control_select_value (char *str, value_t *value, value_t *old_value, loc_t location)
{
	int *value_ptr;

	(void)old_value;
	if (location == LOC_LOCAL_VAR)
		value_ptr = value->num_ptr;
	else
		value_ptr = &(value->num);

	if (!strcmp(str, "Enable"))
	{
		*value_ptr = VAD_ENABLE;
	}
	else if (!strcmp(str, "Disable"))
	{
		*value_ptr = VAD_DISABLE;
	}

	return 1;
}

static int parse_codec_perioity_control_select_value (char *str, value_t *value, value_t *old_value, loc_t location)
{
	int *value_ptr;

	(void)old_value;
	if (location == LOC_LOCAL_VAR)
		value_ptr = value->num_ptr;
	else
		value_ptr = &(value->num);

	if (!strncmp(str, "g711a", 5))
	{
		*value_ptr = G711a;
	}
	else if (!strncmp(str, "g711u", 5))
	{
		*value_ptr = G711u;
	}
	else if (!strncmp(str, "g729", 4))
	{
		*value_ptr = G729;
	}

	return 1;
}

static int parse_config_file_enc_control_select_value (char *str, value_t *value, value_t *old_value, loc_t location)
{
	int *value_ptr;

	(void)old_value;
	if (location == LOC_LOCAL_VAR)
		value_ptr = value->num_ptr;
	else
		value_ptr = &(value->num);

    if (!strcmp(str, "Enable"))
    {
        *value_ptr = ENC_ENABLE;
    }
    else if (!strcmp(str, "Disable"))
    {
        *value_ptr = ENC_DISABLE;
    }

	return 1;
}

static int parse_taps_value(char *str, value_t *value, value_t *old_value, loc_t location)
{
    int *value_ptr;

    (void)old_value;
    if (location == LOC_LOCAL_VAR)
        value_ptr = value->num_ptr;
    else
        value_ptr = &(value->num);

    if (!strcmp(str, "enable"))
    {
        *value_ptr = TAPS_ENABLE;
    }
    else
    {
        *value_ptr = TAPS_DISABLE;
    }
}

static void print_dtmf_method_select_value (map_t *map_entry, const char *subvalue)
{
	char *tmp;

	if (!map_entry)
		return;
        switch( (eDTMFMethod) *(map_entry->value.num_ptr) )
	{
	case BYPASS:
		tmp = "bypass";
		break;
	case RFC2833:
		tmp = "rfc2833";
		break;
	case INFO:
		tmp = "info";
		break;
	default:
		tmp = "bypass";
	}
	if ( (map_entry->input_value && streq (map_entry->input_value, subvalue)) ||
		(streq (tmp, subvalue)) )
	{
		printf ("selected=\"selected\"");
	}
}

static void print_tos_select_value (map_t *map_entry, const char *subvalue)
{
	char *tmp;

	if (!map_entry)
		return;

	/* map the enum type of the cng mode setting to a select string.  */
	switch( (eTosControl) *(map_entry->value.num_ptr) )
	{
	case TOS_ENABLE:
		tmp = "Enable";
		break;
	case TOS_DISABLE:
		tmp = "Disable";
		break;
	default:
		tmp = "Enable";
	}
	if ( (map_entry->input_value && streq (map_entry->input_value, subvalue)) ||
		(streq (tmp, subvalue)) )
	{
		printf ("selected=\"selected\"");
	}
}

static void print_vad_select_value (map_t *map_entry, const char *subvalue)
{
	char *tmp;

	if (!map_entry)
		return;

	/* map the enum type of the cng mode setting to a select string.  */
	switch( (eVadControl) *(map_entry->value.num_ptr) )
	{
	case VAD_ENABLE:
		tmp = "Enable";
		break;
	case VAD_DISABLE:
		tmp = "Disable";
		break;
	default:
		tmp = "Enable";
	}
	if ( (map_entry->input_value && streq (map_entry->input_value, subvalue)) ||
		(streq (tmp, subvalue)) )
	{
		printf ("selected=\"selected\"");
	}
}

static void print_codec_perioity_select_value (map_t *map_entry, const char *subvalue)
{
	char *tmp;

	if (!map_entry)
		return;

	/* map the enum type of the cng mode setting to a select string.  */
	switch( (eCodecPriority) *(map_entry->value.num_ptr) )
	{
	case G711wa:
		tmp = "g711.1a";
		break;
	case G711wu:
		tmp = "g711.1u";
		break;
	case G722:
		tmp = "g722";
		break;
	case G711a:
		tmp = "g711a";
		break;
	case G711u:
		tmp = "g711u";
		break;
	case G729:
		tmp = "g729";
		break;	
	default:
		tmp = "g711a";
	}
	if ( (map_entry->input_value && streq (map_entry->input_value, subvalue)) ||
		(streq (tmp, subvalue)) )
	{
		printf ("selected=\"selected\"");
	}
}

static void print_config_file_enc_select_value (map_t *map_entry, const char *subvalue)
{
	char *tmp;

	if (!map_entry)
		return;

	/* map the enum type of the cng mode setting to a select string.  */
	switch( (eTosControl) *(map_entry->value.num_ptr) )
	{
	case ENC_ENABLE:
		tmp = "Enable";
		break;
	case ENC_DISABLE:
		tmp = "Disable";
		break;
	default:
		tmp = "Enable";
	}
	if ( (map_entry->input_value && streq (map_entry->input_value, subvalue)) ||
		(streq (tmp, subvalue)) )
	{
		printf ("selected=\"selected\"");
	}
}

static void print_config_file_bool_active_device (map_t *map_entry, const char *subvalue)
{
    char *tmp;

    if (!map_entry)
        return;
    
    /* map the enum type of the taps mode setting to a select string.  */
    switch( (eTapsEnable) *(map_entry->value.num_ptr) )
    {
    case TAPS_ENABLE:
        tmp = "enable";
        break;
    case TAPS_DISABLE:
        tmp = "disable";
        break;
    default:
        tmp = "enable";
    }
   
    if ( (map_entry->input_value && streq (map_entry->input_value, subvalue)) ||
        (streq (tmp, subvalue)) )
    {
        printf ("selected=\"selected\"");
    }

}


static void parse_taps_int_value(char *str, value_t *value, value_t *old_value, loc_t location)
{
    int *value_ptr;

    (void)old_value;
    if (location == LOC_LOCAL_VAR)
        value_ptr = value->num_ptr;
    else
        value_ptr = &(value->num);

    if( str != NULL )
        *value_ptr = atoi(str);
    else
        *value_ptr = 0;
}


static void parse_taps_area_code(char *str, value_t *value, value_t *old_value, loc_t location)
{
    int *value_ptr;

    (void)old_value;
    if (location == LOC_LOCAL_VAR)
        value_ptr = value->num_ptr;
    else
        value_ptr = &(value->num);

    if (!strcmp(str, "none"))
    {
        *value_ptr = AREA_CODE_NONE;
    }
    else if (!strcmp(str, "02"))
    {
        *value_ptr = AREA_CODE_02;
    }
    else if (!strcmp(str, "031"))
    {
        *value_ptr = AREA_CODE_031;
    }
    else if (!strcmp(str, "032"))
    {
        *value_ptr = AREA_CODE_032;
    }
    else if (!strcmp(str, "033"))
    {
        *value_ptr = AREA_CODE_033;
    }
    else if (!strcmp(str, "041"))
    {
        *value_ptr = AREA_CODE_041;
    }
    else if (!strcmp(str, "042"))
    {
        *value_ptr = AREA_CODE_042;
    }
    else if (!strcmp(str, "043"))
    {
        *value_ptr = AREA_CODE_043;
    }
    else if (!strcmp(str, "051"))
    {
        *value_ptr = AREA_CODE_051;
    }
    else if (!strcmp(str, "052"))
    {
        *value_ptr = AREA_CODE_052;
    }
    else if (!strcmp(str, "061"))
    {
        *value_ptr = AREA_CODE_061;
    }
    else if (!strcmp(str, "062"))
    {
        *value_ptr = AREA_CODE_062;
    }
    else if (!strcmp(str, "063"))
    {
        *value_ptr = AREA_CODE_063;
    }
    else
    {
        *value_ptr = AREA_CODE_070;
    }
}

static void print_config_area_code(map_t *map_entry, const char *subvalue)
{
    char *tmp;

    if (!map_entry)
        return;

    /* map the enum type of the taps mode setting to a select string.  */
    switch( (eTapsEnable) *(map_entry->value.num_ptr) )
    {
			case AREA_CODE_NONE:
				tmp = "none";
				break;
			case AREA_CODE_02:
				tmp = "02";
				break;
			case AREA_CODE_031:
				tmp = "031";
				break;
			case AREA_CODE_032:
				tmp = "032";
				break;
			case AREA_CODE_033:
				tmp = "033";
				break;
			case AREA_CODE_041:
				tmp = "041";
				break;
			case AREA_CODE_042:
				tmp = "042";
				break;
			case AREA_CODE_043:
				tmp = "043";
				break;
			case AREA_CODE_051:
				tmp = "051";
				break;
			case AREA_CODE_052:
				tmp = "052";
				break;
			case AREA_CODE_053:
				tmp = "053";
				break;
			case AREA_CODE_054:
				tmp = "054";
				break;
			case AREA_CODE_055:
				tmp = "055";
				break;
			case AREA_CODE_061:
				tmp = "061";
				break;
			case AREA_CODE_062:
				tmp = "062";
				break;
			case AREA_CODE_063:
				tmp = "063";
				break;
			case AREA_CODE_070:
				tmp = "070";
				break;
            default:
				tmp = "070";
				break;
    }

    if ( (map_entry->input_value && streq (map_entry->input_value, subvalue)) ||
        (streq (tmp, subvalue)) )
    {
        printf ("selected=\"selected\"");
    }
}

static int GetCodecPriorityStrToNum(char *codec)
{
	if(!strncmp(codec, "g711a", 5))
	{
		return G711a;
	}
	else if(!strncmp(codec, "g711u", 5))
	{
		return G711u;
	}
	else if(!strncmp(codec, "g729", 4))
	{
		return G729;
	}	

	return 0;
}

static int device_setting_api(phone_cfg_action_t action)
{
    stConfigArg input_argv[10];
    int  ret                = 1;
    char sTapsActive[20]    = {0,};
    char sTapsPort[20]      = {0,};
    char sTapsRetryCount[20] = {0,};
    char sTapsRetryTime[20] = {0,};
    char sTapsSSLActive[20] = {0,};
    char sTapsNTPActive[20] = {0,};
    char sTapsAreaCode[20]  = {0,};

    if ( action == PHONE_CFG_GET_DEVICE )
    {
        input_argv[0].name = "taps_active";
        input_argv[0].data = sTapsActive; //stDeviceInfo.taps_active;
        input_argv[1].name = "taps_address";
        input_argv[1].data = stDeviceInfo.taps_address;
        input_argv[2].name = "taps_port";
        input_argv[2].data = sTapsPort; //stDeviceInfo.taps_port;
        input_argv[3].name = "taps_retry_count";
        input_argv[3].data = sTapsRetryCount; //stDeviceInfo.taps_retry_count;
        input_argv[4].name = "taps_retry_time";
        input_argv[4].data = sTapsRetryTime; //stDeviceInfo.taps_retry_time;
        input_argv[5].name = "taps_ssl_active";
        input_argv[5].data = sTapsSSLActive; //stDeviceInfo.taps_ssl_active;
        input_argv[6].name = "AreaCode";
        input_argv[6].data = sTapsAreaCode;
        input_argv[7].name = "ntp_active";
        input_argv[7].data = sTapsNTPActive; //stDeviceInfo.ntp_active;
        input_argv[8].name = "ntp_address1";
        input_argv[8].data = stDeviceInfo.ntp_address1;
        input_argv[9].name = "ntp_address2";
        input_argv[9].data = stDeviceInfo.ntp_address2;

        if(config_find_multi_by_name(FILENAME_SIP_CONF, 10, input_argv) > 0)
        {
            if ( atoi(sTapsActive) == 1 )
                stDeviceInfo.taps_active = TAPS_ENABLE;
            else
                stDeviceInfo.taps_active = TAPS_DISABLE;

	        stDeviceInfo.taps_port = atoi(sTapsPort);
	        stDeviceInfo.taps_retry_count = atoi(sTapsRetryCount);
	        stDeviceInfo.taps_retry_time = atoi(sTapsRetryTime);

            if ( atoi(sTapsSSLActive) == 1 )
                stDeviceInfo.taps_ssl_active = TAPS_ENABLE;
            else
                stDeviceInfo.taps_ssl_active = TAPS_DISABLE;

            if ( atoi(sTapsNTPActive) == 1 )
                stDeviceInfo.ntp_active = TAPS_ENABLE;
            else
                stDeviceInfo.ntp_active = TAPS_DISABLE; 

            stDeviceInfo.area_code = atoi(sTapsAreaCode);

		return 1;
        }
        else
            return -1;

    }
    else if(action == PHONE_CFG_UPDATE_DEVICE)
    {    
        /// 저장된 정보 취득
        device_info stDeviceInfoBuf;
        int         reset_flag      = 0;
        int         ntp_flag        = 0;
        int         area_code_flag  = 0;

        input_argv[0].name = "taps_active";
        input_argv[0].data = sTapsActive; //stDeviceInfo.taps_active;
        input_argv[1].name = "taps_address";
        input_argv[1].data = stDeviceInfoBuf.taps_address;
        input_argv[2].name = "taps_port";
        input_argv[2].data = sTapsPort; //stDeviceInfo.taps_port;
        input_argv[3].name = "taps_retry_count";
        input_argv[3].data = sTapsRetryCount; //stDeviceInfo.taps_retry_count;
        input_argv[4].name = "taps_retry_time";
        input_argv[4].data = sTapsRetryTime; //stDeviceInfo.taps_retry_time;
        input_argv[5].name = "taps_ssl_active";
        input_argv[5].data = sTapsSSLActive; //stDeviceInfo.taps_ssl_active;
        input_argv[6].name = "AreaCode";
        input_argv[6].data = sTapsAreaCode;
        input_argv[7].name = "ntp_active";
        input_argv[7].data = sTapsNTPActive; //stDeviceInfo.ntp_active;
        input_argv[8].name = "ntp_address1";
        input_argv[8].data = stDeviceInfoBuf.ntp_address1;
        input_argv[9].name = "ntp_address2";
        input_argv[9].data = stDeviceInfoBuf.ntp_address2;

        if(config_find_multi_by_name(FILENAME_SIP_CONF, 10, input_argv) > 0)
        {
            if ( atoi(sTapsActive) == 1 )
                stDeviceInfoBuf.taps_active     = TAPS_ENABLE;
            else
                stDeviceInfoBuf.taps_active     = TAPS_DISABLE;

	        stDeviceInfoBuf.taps_port           = atoi(sTapsPort);
	        stDeviceInfoBuf.taps_retry_count    = atoi(sTapsRetryCount);
	        stDeviceInfoBuf.taps_retry_time     = atoi(sTapsRetryTime);

            if ( atoi(sTapsSSLActive) == 1 )
                stDeviceInfoBuf.taps_ssl_active = TAPS_ENABLE;
            else
                stDeviceInfoBuf.taps_ssl_active = TAPS_DISABLE;

            if ( atoi(sTapsNTPActive) == 1 )
                stDeviceInfoBuf.ntp_active = TAPS_ENABLE;
            else
                stDeviceInfoBuf.ntp_active = TAPS_DISABLE; 

            stDeviceInfoBuf.area_code = atoi(sTapsAreaCode);
            char aa[100] = {0,};
            char bb[100] = {0,};

            // 신/구 데이터 비교후, 변경된 값이면 저장함.
            if(stDeviceInfo.taps_active != stDeviceInfoBuf.taps_active)
            {
                reset_flag = 1;

                if ( stDeviceInfo.taps_active == TAPS_ENABLE )
                    ret = config_replace_by_name(FILENAME_SIP_CONF, "taps_active", 0, "1", '=', 1);
                else
                    ret = config_replace_by_name(FILENAME_SIP_CONF, "taps_active", 0, "0", '=', 1);

            }


            if (strcmp(stDeviceInfo.taps_address, stDeviceInfoBuf.taps_address))
            {
                reset_flag = 1;
                ret = config_replace_by_name(FILENAME_SIP_CONF, "taps_address", 0, stDeviceInfo.taps_address, '=', 1);
            }

            if(stDeviceInfo.taps_port != stDeviceInfoBuf.taps_port)
            {
                reset_flag = 1;
                char strPort[4] = {0,};
                sprintf(strPort,"%d",stDeviceInfo.taps_port);

                ret = config_replace_by_name(FILENAME_SIP_CONF, "taps_port", 0, strPort, '=', 1);
            }

            if(stDeviceInfo.taps_retry_count != stDeviceInfoBuf.taps_retry_count)
            {
                reset_flag = 1;

                char strRetryCount[5] = {0,};
                sprintf(strRetryCount,"%d",stDeviceInfo.taps_retry_count);

                ret = config_replace_by_name(FILENAME_SIP_CONF, "taps_retry_count", 0, strRetryCount, '=', 1);
            }

            if(stDeviceInfo.taps_retry_time != stDeviceInfoBuf.taps_retry_time)
            {
                reset_flag = 1;

                char strRetryTime[8] = {0,};
                sprintf(strRetryTime,"%d",stDeviceInfo.taps_retry_time);

                ret = config_replace_by_name(FILENAME_SIP_CONF, "taps_retry_time", 0, strRetryTime, '=', 1);
            }

            if(stDeviceInfo.taps_ssl_active != stDeviceInfoBuf.taps_ssl_active)
            {
                reset_flag = 1;

                if ( stDeviceInfo.taps_ssl_active == TAPS_ENABLE )
                    ret = config_replace_by_name(FILENAME_SIP_CONF, "taps_ssl_active", 0, "1", '=', 1);
                else
                    ret = config_replace_by_name(FILENAME_SIP_CONF, "taps_ssl_active", 0, "0", '=', 1);                
            }

            if(stDeviceInfo.area_code != stDeviceInfoBuf.area_code)
            {
                area_code_flag = 1;

                char strAreaCode[4] = {0,};
                sprintf(strAreaCode,"%d",stDeviceInfo.area_code);

                ret = config_replace_by_name(FILENAME_SIP_CONF, "AreaCode", 0, strAreaCode, '=', 1);
            }
 
            if(stDeviceInfo.ntp_active != stDeviceInfoBuf.ntp_active)
            {
                ntp_flag = 1;
                if ( stDeviceInfo.ntp_active == TAPS_ENABLE )
                    ret = config_replace_by_name(FILENAME_SIP_CONF, "ntp_active", 0, "1", '=', 1);
                else
                    ret = config_replace_by_name(FILENAME_SIP_CONF, "ntp_active", 0, "0", '=', 1);
            }

            if (strcmp(stDeviceInfo.ntp_address1, stDeviceInfoBuf.ntp_address1))
            {
                ntp_flag = 1;
                ret = config_replace_by_name(FILENAME_SIP_CONF, "ntp_address1", 0, stDeviceInfo.ntp_address1, '=', 1);
            }

            if (strcmp(stDeviceInfo.ntp_address2, stDeviceInfoBuf.ntp_address2))
            {
                ntp_flag = 1;
                ret = config_replace_by_name(FILENAME_SIP_CONF, "ntp_address2", 0, stDeviceInfo.ntp_address2, '=', 1);
            }


            if ( reset_flag == 1)
            {                
                usleep(200);
                Send_IPC_to_SIP(FSP_EM_IPC_CMD_WEB_SIP_TAPS_CONTROL, 0, 0);
            }

            if ( ntp_flag == 1 )
            {
                usleep(200);
                Send_IPC_to_SIP(FSP_EM_IPC_CMD_WEB_SIP_AREA_CODE, 0, 0);
            }

            if ( area_code_flag == 1 )
            {
                usleep(200);
                Send_IPC_to_SIP(FSP_EM_IPC_CMD_WEB_SIP_NTP_SET, 0, 0);
            }

            return ret;
        }
        else
            return -1;

    }
}

/*
* phone_cfg_api - call the profile API to perform a given action.
*/
static int phone_setting_api(phone_cfg_action_t action)
{
	stConfigArg input_argv[15];
	char sDtmfMethod[20] = {0, };
	char sTosControl[20] = {0, };
	char sVadControl[20] = {0, };
	char sCodecPriority1[20] = {0, };
	char sCodecPriority2[20] = {0, };
	char sCodecPriority3[20] = {0, };
	char sConfigFileEnc[20] = {0, };
	int ret = 1;
	char sCodecName[3][20] = {"g711a", "g711u", "g729" };

	if(action == PHONE_CFG_GET_PHONE)
	{
		input_argv[0].name = "service_domain";
		input_argv[0].data 	= stPhoneInfo.proxy_domain_name;
		input_argv[1].name = "sms_domain";
		input_argv[1].data 	= stPhoneInfo.sms_address;
		input_argv[2].name = "outbound_proxy2nd";
		input_argv[2].data 	= stPhoneInfo.sbc_outbound_proxy_address;
		input_argv[3].name = "outbound_proxy";
		input_argv[3].data 	= stPhoneInfo.ssw_outbound_proxy_address;
		//input_argv[4].name = "display_name";
		//input_argv[4].data 	= stPhoneInfo.display_name;
		input_argv[4].name = "dn";
		input_argv[4].data 	= stPhoneInfo.phone_number;
		input_argv[5].name = "id";
		input_argv[5].data 	= stPhoneInfo.auth_ID;
		input_argv[6].name = "password";
		input_argv[6].data 	= stPhoneInfo.password;
		input_argv[7].name = "dtmf_method";
		input_argv[7].data 	= sDtmfMethod;
		input_argv[8].name = "expire_time";
		input_argv[8].data 	= stPhoneInfo.expire_time;
		input_argv[9].name = "tos_control";
		input_argv[9].data = sTosControl;
		input_argv[10].name = "vad_control";
		input_argv[10].data = sVadControl;
		input_argv[11].name = "P-Device-UID";
		input_argv[11].data = stPhoneInfo.p_device_uid;
		input_argv[12].name = "CodecPriority1";
		input_argv[12].data = sCodecPriority1;
		input_argv[13].name = "CodecPriority2";
		input_argv[13].data = sCodecPriority2;
		input_argv[14].name = "CodecPriority3";
		input_argv[14].data = sCodecPriority3;

		if(config_find_multi_by_name(FILENAME_SIP_CONF, 15, input_argv) > 0)
		{
			//strcpy(stPhoneInfo.audio_codec, CM_CFG_VALUE_DEFAULT_AUDIO_CODEC_NARROWBAND);
			config_find_by_name(FILENAME_SKB_CONF, "ps_ip", 0, stPhoneInfo.ps_sever_address);
			config_find_by_name(FILENAME_SIP_CONF, "display_name", 0, stPhoneInfo.display_name);

			if(!strncmp(sDtmfMethod, "bypass", 6))
				stPhoneInfo.dtmf_method = BYPASS;
			else if(!strncmp(sDtmfMethod, "rfc2833", 7))
				stPhoneInfo.dtmf_method = RFC2833;
			else
				stPhoneInfo.dtmf_method = INFO;

			if(!strncmp(sTosControl, "enable", 6))
				stPhoneInfo.tos_control = TOS_ENABLE;
			else
				stPhoneInfo.tos_control = TOS_DISABLE;

			if(!strncmp(sVadControl, "enable", 6))
				stPhoneInfo.vad_control = VAD_ENABLE;
			else
				stPhoneInfo.vad_control = VAD_DISABLE;

			stPhoneInfo.audio_codec[0] = GetCodecPriorityStrToNum(sCodecPriority1);
			stPhoneInfo.audio_codec[1] = GetCodecPriorityStrToNum(sCodecPriority2);
			stPhoneInfo.audio_codec[2] = GetCodecPriorityStrToNum(sCodecPriority3);

			config_find_by_name(FILENAME_SKB_CONF, "downType", 0, sConfigFileEnc);

			stPhoneInfo.confile_file_enc = atoi(sConfigFileEnc);
		}
		else
			ret = -1;
	}
	else if(action == PHONE_CFG_UPDATE_PHONE)
	{
		phone_info stPhoneInfoBuf;
		int trap_send_flag = 0;
		int sip_reset_flag = 0;
		int codec_perioity_modify = 0;

		input_argv[0].name = "service_domain";
		input_argv[0].data 	= stPhoneInfoBuf.proxy_domain_name;
		input_argv[1].name = "sms_domain";
		input_argv[1].data 	= stPhoneInfoBuf.sms_address;
		input_argv[2].name = "outbound_proxy2nd";
		input_argv[2].data 	= stPhoneInfoBuf.sbc_outbound_proxy_address;
		input_argv[3].name = "outbound_proxy";
		input_argv[3].data 	= stPhoneInfoBuf.ssw_outbound_proxy_address;
		//input_argv[4].name = "display_name";
		//input_argv[4].data 	= stPhoneInfoBuf.display_name;
		input_argv[4].name = "dn";
		input_argv[4].data 	= stPhoneInfoBuf.phone_number;
		input_argv[5].name = "id";
		input_argv[5].data 	= stPhoneInfoBuf.auth_ID;
		input_argv[6].name = "password";
		input_argv[6].data 	= stPhoneInfoBuf.password;
		input_argv[7].name = "dtmf_method";
		input_argv[7].data 	= sDtmfMethod;
		input_argv[8].name = "expire_time";
		input_argv[8].data 	= stPhoneInfoBuf.expire_time;
		input_argv[9].name = "tos_control";
		input_argv[9].data = sTosControl;
		input_argv[10].name = "vad_control";
		input_argv[10].data = sVadControl;
		input_argv[11].name = "P-Device-UID";
		input_argv[11].data = stPhoneInfoBuf.p_device_uid;
		input_argv[12].name = "CodecPriority1";
		input_argv[12].data = sCodecPriority1;
		input_argv[13].name = "CodecPriority2";
		input_argv[13].data = sCodecPriority2;
		input_argv[14].name = "CodecPriority3";
		input_argv[14].data = sCodecPriority3;


		if(config_find_multi_by_name(FILENAME_SIP_CONF, 15, input_argv) > 0)
		{
			ret = config_find_by_name(FILENAME_SKB_CONF, "ps_ip", 0, stPhoneInfoBuf.ps_sever_address);
			config_find_by_name(FILENAME_SIP_CONF, "display_name", 0, stPhoneInfoBuf.display_name);

			if(!strncmp(sDtmfMethod, "bypass", 6))
				stPhoneInfoBuf.dtmf_method = BYPASS;
			else if(!strncmp(sDtmfMethod, "rfc2833", 7))
				stPhoneInfoBuf.dtmf_method = RFC2833;
			else
				stPhoneInfoBuf.dtmf_method = INFO;

			if(!strncmp(sTosControl, "enable", 6))
				stPhoneInfoBuf.tos_control = TOS_ENABLE;
			else
				stPhoneInfoBuf.tos_control = TOS_DISABLE;

			if(!strncmp(sVadControl, "enable", 6))
				stPhoneInfoBuf.vad_control = VAD_ENABLE;
			else
				stPhoneInfoBuf.vad_control = VAD_DISABLE;

			stPhoneInfoBuf.audio_codec[0] = GetCodecPriorityStrToNum(sCodecPriority1);
			stPhoneInfoBuf.audio_codec[1] = GetCodecPriorityStrToNum(sCodecPriority2);
			stPhoneInfoBuf.audio_codec[2] = GetCodecPriorityStrToNum(sCodecPriority3);
			
			ret = config_find_by_name(FILENAME_SKB_CONF, "downType", 0, sConfigFileEnc);

			stPhoneInfoBuf.confile_file_enc = atoi(sConfigFileEnc);

			/* Compair to file data from current data */
			if(strcmp(stPhoneInfo.proxy_domain_name, stPhoneInfoBuf.proxy_domain_name))
			{
				//wc_print ("proxy_domain_name");
				sip_reset_flag = 1;
				ret = config_replace_by_name(FILENAME_SIP_CONF, "service_domain", 0, stPhoneInfo.proxy_domain_name, '=', 1);
			}
			if(strcmp(stPhoneInfo.sms_address, stPhoneInfoBuf.sms_address))
			{
				//wc_print ("sms_address");
				sip_reset_flag = 1;
				ret = config_replace_by_name(FILENAME_SIP_CONF, "sms_domain", 0, stPhoneInfo.sms_address, '=', 1);
			}
			if(strcmp(stPhoneInfo.sbc_outbound_proxy_address, stPhoneInfoBuf.sbc_outbound_proxy_address))
			{
				//wc_print ("sbc_outbound_proxy_address");
				sip_reset_flag = 1;
				trap_send_flag = 1;
				ret = config_replace_by_name(FILENAME_SIP_CONF, "outbound_proxy2nd", 0, stPhoneInfo.sbc_outbound_proxy_address, '=', 1);
			}
			if(strcmp(stPhoneInfo.ssw_outbound_proxy_address, stPhoneInfoBuf.ssw_outbound_proxy_address))
			{
				//wc_print ("ssw_outbound_proxy_address");
				sip_reset_flag = 1;
				trap_send_flag = 1;
				ret = config_replace_by_name(FILENAME_SIP_CONF, "outbound_proxy", 0, stPhoneInfo.ssw_outbound_proxy_address, '=', 1);
			}

			if(strcmp(stPhoneInfo.display_name, stPhoneInfoBuf.display_name))
			{
				//wc_print ("display_name");
				sip_reset_flag = 1;
				trap_send_flag = 1;
				ret = config_replace_by_name(FILENAME_SIP_CONF, "display_name", 0, stPhoneInfo.display_name, '=', 1);
			}

			if(strcmp(stPhoneInfo.phone_number, stPhoneInfoBuf.phone_number))
			{
				//wc_print ("phone_number");
				sip_reset_flag = 1;
				trap_send_flag = 1;
				ret = config_replace_by_name(FILENAME_SIP_CONF, "dn", 0, stPhoneInfo.phone_number, '=', 1);
			}
			if(strcmp(stPhoneInfo.auth_ID, stPhoneInfoBuf.auth_ID))
			{
				//wc_print ("auth_ID");
				sip_reset_flag = 1;
				ret = config_replace_by_name(FILENAME_SIP_CONF, "id", 0, stPhoneInfo.auth_ID, '=', 1);
			}
			if(strcmp(stPhoneInfo.password, stPhoneInfoBuf.password))
			{
				//wc_print ("password");
				sip_reset_flag = 1;
				ret = config_replace_by_name(FILENAME_SIP_CONF, "password", 0, stPhoneInfo.password, '=', 1);
			}
			if(strcmp(stPhoneInfo.expire_time, stPhoneInfoBuf.expire_time))
			{
				//wc_print ("expire_time");
				sip_reset_flag = 1;
				ret = config_replace_by_name(FILENAME_SIP_CONF, "expire_time", 0, stPhoneInfo.expire_time, '=', 1);
			}
			if(strcmp(stPhoneInfo.p_device_uid, stPhoneInfoBuf.p_device_uid))
			{
				//wc_print ("p_device_uid");
				sip_reset_flag = 1;
				ret = config_replace_by_name(FILENAME_SIP_CONF, "P-Device-UID", 0, stPhoneInfo.p_device_uid, '=', 1);
			}
			if(strcmp(stPhoneInfo.ps_sever_address, stPhoneInfoBuf.ps_sever_address))
			{
				//wc_print ("ps_sever_address");
				ret = config_replace_by_name(FILENAME_SKB_CONF, "ps_ip", 0, stPhoneInfo.ps_sever_address, '=', 1);
			}

			if(stPhoneInfo.confile_file_enc != stPhoneInfoBuf.confile_file_enc)
			{
				char sValue[10];
				sprintf(sValue,"%d", stPhoneInfo.confile_file_enc);
				//wc_print ("confile_file_enc");
				ret = config_replace_by_name(FILENAME_SKB_CONF, "downType", 0, sValue, '=', 1);
			}
			if(stPhoneInfo.dtmf_method != stPhoneInfoBuf.dtmf_method)
			{
				//sip_reset_flag = 1;
				if(stPhoneInfo.dtmf_method == BYPASS)
					ret = config_replace_by_name(FILENAME_SIP_CONF, "dtmf_method", 0, "bypass", '=', 1);
				else if(stPhoneInfo.dtmf_method == RFC2833)
					ret = config_replace_by_name(FILENAME_SIP_CONF, "dtmf_method", 0, "rfc2833", '=', 1);
				else
					ret = config_replace_by_name(FILENAME_SIP_CONF, "dtmf_method", 0, "info", '=', 1);

				usleep(200);
				Send_IPC_to_SIP(FSP_EM_IPC_CMD_WEB_SIP_MODIFY_DTMF_METHOD, 0, 0);
			}
			if(stPhoneInfo.tos_control != stPhoneInfoBuf.tos_control)
			{
				//wc_print ("tos_control");
				sip_reset_flag = 1;
				if(stPhoneInfo.tos_control == TOS_ENABLE)
					ret = config_replace_by_name(FILENAME_SIP_CONF, "tos_control", 0, "enable", '=', 1);
				else
					ret = config_replace_by_name(FILENAME_SIP_CONF, "tos_control", 0, "disable", '=', 1);
			}

			if(stPhoneInfo.vad_control != stPhoneInfoBuf.vad_control)
			{
				//wc_print ("vad_control");
				if(stPhoneInfo.vad_control == VAD_ENABLE)
					ret = config_replace_by_name(FILENAME_SIP_CONF, "vad_control", 0, "enable", '=', 1);
				else
					ret = config_replace_by_name(FILENAME_SIP_CONF, "vad_control", 0, "disable", '=', 1);

				usleep(200);
				Send_IPC_to_SIP(FSP_EM_IPC_CMD_WEB_SIP_CODEC_VAD_MODIFY, 0, 0);
			}

			if(stPhoneInfo.audio_codec[0] != stPhoneInfoBuf.audio_codec[0])
			{
				codec_perioity_modify = 1;
				ret = config_replace_by_name(FILENAME_SIP_CONF, "CodecPriority1", 0, sCodecName[stPhoneInfo.audio_codec[0]], '=', 1);
			}
			if(stPhoneInfo.audio_codec[1] != stPhoneInfoBuf.audio_codec[1])
			{
				codec_perioity_modify = 1;
				ret = config_replace_by_name(FILENAME_SIP_CONF, "CodecPriority2", 0, sCodecName[stPhoneInfo.audio_codec[1]], '=', 1);
			}
			if(stPhoneInfo.audio_codec[2] != stPhoneInfoBuf.audio_codec[2])
			{
				codec_perioity_modify = 1;
				ret = config_replace_by_name(FILENAME_SIP_CONF, "CodecPriority3", 0, sCodecName[stPhoneInfo.audio_codec[2]], '=', 1);
			}
		}
		else
			ret = -1;

		if(sip_reset_flag)
		{
			usleep(200);
			Send_IPC_to_SIP(FSP_EM_IPC_CMD_CWMP_SIP_RESET, 0, 0);
		}

		if(trap_send_flag)
		{
			usleep(200);
			Send_IPC_to_CWMP(FSP_EM_IPC_CMD_WEB_CWMP_POWERON_TRAP_REQUEST, 0, 0);
		}

		if(codec_perioity_modify)
		{
			usleep(200);
			Send_IPC_to_SIP(FSP_EM_IPC_CMD_WEB_SIP_CODEC_PERIOITY_MODIFY, 0, 0);
		}
	}

	return ret;
}
#define CPU_STATS_MAX_BUF_LEN   160
static void wc_show_cpu_usage ( void )
{

	char    linebuf[CPU_STATS_MAX_BUF_LEN];
	FILE   *fp;
	char   *token = NULL;
	int     statsIndex;

	/* Print Status header. */
	wc_print( "CPU Usage:" );

	wc_show_table_start( 2, 0, 5 );
	// Display dynamic settings.

	// Host Usage stats.
	wc_show_table_row_start();
	wc_show_table_data_cell( "align=center COLSPAN=2", "<b>Host Usage Stats</b>" );
	wc_show_table_row_end();
	fp = fopen( "/proc/sys/hostUsage/stats/summary", "rt" );
	if ( fp != NULL )
	{
		if ( fgets( linebuf, CPU_STATS_MAX_BUF_LEN, fp ) != NULL )
		{
			token = strtok( linebuf, " \t\n\r" );
			statsIndex = 0;

			while ( token != NULL )
			{
				char floatStr[16];
				int cpu_usage;

				sscanf( token, "%d", &cpu_usage );
				sprintf( floatStr, "%.1f", (cpu_usage) / 10.0 );
				wc_show_table_row_start();

				wc_show_table_data_cell( "align=right",  hostUsageStatNames[statsIndex] );
				wc_show_table_data_cell( NULL, floatStr );

				wc_show_table_row_end();

				statsIndex++;
				token = strtok( NULL, " \t\n\r" );
				if ( statsIndex >= (int)(sizeof( hostUsageStatNames ) / sizeof( hostUsageStatNames[0] )) )
				{
					break;  // In case there are too many entries
				}
			}
		}
		fclose( fp );
	}
	wc_show_table_end();
}

static int phone_cfg_api (phone_cfg_action_t action)
{
	int rc = 1;
	char msg_buf[110];

	if (!phone_cfg_loaded)
	{
		/* use Phone cfg interface to pre-load configuration. */
		//if (cm_cfg_load( "Web Configuration", 1 ) < 0)
		{
		//	sprintf(msg_buf, "Phone Cfg file locked by: %s", cm_cfg_lock_id());
		//	wc_print( msg_buf );
		//	return -1;
		}
		phone_cfg_loaded = 1;
	}

	/* perform the given action.  */
	switch (action)
	{
#if CFG_GLOBAL_SIP_SUPPORT
	  case PHONE_CFG_GET_SIP:
	//	  cm_cfg_get_sip_defaults( &sip_cfg );
	//	  cm_cfg_get_cur_sip( &sip_cfg );
		  break;
#endif
	  case PHONE_CFG_GET_AUDIO:
		 // cm_cfg_get_audio_defaults( &audio_cfg );
		 // cm_cfg_get_audio( &audio_cfg );
		  break;
	  case PHONE_CFG_GET_PHONE:
//	  cm_cfg_get_phone_defaults( &phone_cfg );
//		  cm_cfg_get_phone( &phone_cfg );
		  break;
#if CFG_GLOBAL_STUN_CLIENT
	  case PHONE_CFG_GET_STUN:
//		  cm_cfg_get_stun_defaults( &stun_cfg );
//		  cm_cfg_get_stun( &stun_cfg );
		  break;
#endif

#if CFG_GLOBAL_SIP_SUPPORT
	  case PHONE_CFG_UPDATE_SIP:
//		  rc = cm_cfg_update_sip( &sip_cfg, -1, 0, NULL );
		  phone_cfg_changed = 1;
		  break;
#endif
	  case PHONE_CFG_UPDATE_AUDIO:
	//	  rc = cm_cfg_update_audio( &audio_cfg );
		  phone_cfg_changed = 1;
		  break;
	  case PHONE_CFG_UPDATE_PHONE:
//		  rc = cm_cfg_update_phone( &phone_cfg );
		  phone_cfg_changed = 1;
		  break;

#if CFG_GLOBAL_SIP_SUPPORT
	  case PHONE_CFG_RESET_SIP:
		  cm_cfg_get_sip_defaults( &sip_cfg );
		  break;
#endif
	  case PHONE_CFG_RESET_AUDIO:
	//	  cm_cfg_get_audio_defaults( &audio_cfg );
		  break;
	  case PHONE_CFG_RESET_PHONE:
	//	  cm_cfg_get_phone_defaults( &phone_cfg );
		  break;
#if CFG_GLOBAL_STUN_CLIENT
	  case PHONE_CFG_RESET_STUN:
		  cm_cfg_get_stun_defaults( &stun_cfg );
		  break;
#endif
	  default:
		  return -1;
	}

	return rc;
}

/*
* get_real_char - get real character from input stream
*/
#define ishex(ch) ((((ch) >= '0') && ((ch) <= '9')) || (((ch) >= 'a' && ((ch) <= 'f'))) || (((ch) >= 'A' && ((ch) <= 'F'))))
#define hextonum(ch) (  (((ch) >= '0') && ((ch) <= '9')) ? (ch) - '0' : \
	( (((ch) >= 'a') && ((ch) <= 'f')) ? (ch) - 'a' + 10 : \
	((((ch) >= 'A') && ((ch) <= 'F')) ? (ch) - 'A' + 10 : 0 )))
static int get_real_char ( void )
{
	int ch1, ch2;

	ch1 = getc (stdin);
	switch (ch1)
	{
	case '+':
		return ' ';

	case '%':
		ch1 = getc (stdin);
		if (ch1 == EOF)
			return EOF;
		ch2 = getc (stdin);
		if (ch2 == EOF)
			return EOF;

		if (!ishex(ch1) || !ishex(ch2))
			return EOF;

		return (hextonum(ch1) * 16 + hextonum(ch2));

	default:
		return ch1;
	}

}

/*
* initialize_map - Initialize default values in map array
*/
static void initialize_map (map_t *map)
{
	if (!map)
		return;

	/* Loop through entire map list */
	while (map->section && map->key)
	{
		map->input_value = "";
		map->valid = (map->location == LOC_LOCAL_VAR) ? 1 : 0;
		map->changed = 0;
		map++;
	}
}


/*
* get_internal_values - Fill in map with internal values
*/
static void get_internal_values (map_t *map)
{
	if (!map)
		return;

	/* Loop through entire map list */
	while (map->section && map->key)
	{
		switch (map->type)
		{
		case TYPE_INTERNAL_NETWORK:
			*map->value.num_ptr = wc.network;
			map->valid = 1;
			map->changed = 0;
			break;
		case TYPE_INTERNAL_SECTION:
			map->value.str = wc.section;
			map->valid = 1;
			map->changed = 0;
			break;
		default:
			break;
		}
		map++;
	}
}

/*
* wc_get_page_and_action - get requested page and action
*/
static void wc_get_page_and_action ( void )
{
	char *method;
	char *query;
	char *page;
	char *action;
	char *network;
	char *len;

	/* Default to show status page */
	wc.page = WC_PAGE_STATUS;
	wc.action = WC_ACTION_SHOW;

	/* Get necessary strings */
	len = getenv ("CONTENT_LENGTH");
	method = getenv ("REQUEST_METHOD");   //” GET”,” HEAD”,” POST”,” PUT”
	query = getenv ("QUERY_STRING");         // ? 이후의 값    cgi?page=b 이면, page=b
	page = get_value_from_query (query, "page");
	action = get_value_from_query (query, "action");
	network = get_value_from_query (query, "network");
	g_sValue = get_value_from_query (query, "value");
	wc.section = get_value_from_query (query, "section");

	if (streq (page, "networkscfg"))
	{
		wc.page = WC_PAGE_NETWORKS_CONFIG;
	}
	else if (streq (page, "wlancfg"))
	{
		wc.page = WC_PAGE_WIRELESS_CONFIG;
	}
	else if (streq (page, "adhoccfg"))
	{
		wc.page = WC_PAGE_ADHOC_CONFIG;
	}
	else if (streq (page, "phonecfg"))
	{
		wc.page = WC_PAGE_PHONE_CONFIG;
	}
        else if (streq (page, "servercfg"))
        {
                wc.page = WC_PAGE_SERVER_CONFIG;
        }
        else if (streq (page, "msgcfg"))
        {
                wc.page = WC_PAGE_MSG_CONFIG;
        }
        else if (streq (page, "devicecfg"))
        {
                wc.page = WC_PAGE_DEVICE_CONFIG;
        }
        else if (streq (page, "wificfg"))
        {
                wc.page =  WC_PAGE_WIFI_CONFIG;
        }
	else if (streq (page, "call_log"))
	{
		wc.page = WC_PAGE_PHONE_CALL_LOG;
	}
	else if (streq (page, "sip_log"))
	{
		wc.page = WC_PAGE_PHONE_SIP_LOG;
	}
#if CFG_GLOBAL_WIRED_ENET_SUPPORT
	else if (streq (page, "enetcfg"))
	{
		wc.page = WC_PAGE_ENET_CONFIG;
	}
#endif
#if CFG_GLOBAL_SIP_SUPPORT
	else if (streq (page, "sipcfg"))
	{
		wc.page = WC_PAGE_SIP_CONFIG;
	}
#endif
	else if (streq (page, "audiocfg"))
	{
		wc.page = WC_PAGE_AUDIO_CONFIG;
	}
#if CFG_GLOBAL_STUN_CLIENT
	else if (streq (page, "stuncfg"))
	{
		wc.page = WC_PAGE_STUN_CONFIG;
	}
#endif
	else if (streq (page, "upgrade_and_reboot"))
	{
		wc.page = WC_PAGE_UPGRADE_AND_REBOOT;
	}
	else if (streq (page, "upgrade"))
	{
		wc.page = WC_PAGE_UPGRADE;
	}
	else if (streq (page, "upload"))
	{
		wc.page = WC_PAGE_UPLOAD;
	}
	else if (streq (page, "debugstats_verbose"))
	{
		wc.page = WC_PAGE_VERBOSE_DEBUG_STATS;
	}
	else if (streq (page, "debugstats_summary"))
	{
		wc.page = WC_PAGE_SUMMARY_DEBUG_STATS;
	}
	else if (streq (page, "cpu_usage"))
	{
		wc.page = WC_PAGE_CPU_USAGE;
	}
	else if (streq (page, "reboot"))
	{
		wc.page = WC_PAGE_REBOOT;
	}
	else if (streq (page, "top"))
	{
		wc.page = WC_PAGE_TOP;
	}
	else if (streq (page, "phone_passwd_init"))
	{
		wc.page = WC_PAGE_PHONE_PW_INIT;
	}
	else if (streq (page, "login"))
	{
		wc.page = WC_PAGE_PHONE_LOGIN;
	}
	else if (streq (page, "logout"))
	{
		wc.page = WC_PAGE_PHONE_LOGOUT;
	}
	else if (streq (page, "webpage_passwd_modify"))
	{
		wc.page = WC_PAGE_PHONE_PASSWD_MODIFY;
	}
	else if (streq (page, "w200_audio_control"))
	{
		wc.page = WC_PAGE_PHONE_W200_AUDIO_CONTROL;
	}
	else if (streq (page, "w110_audio_control"))
	{
		wc.page = WC_PAGE_PHONE_W110_AUDIO_CONTROL;
	}

	if ((wc.page == WC_PAGE_WIRELESS_CONFIG) ||
		(wc.page == WC_PAGE_ADHOC_CONFIG) ||
		(wc.page == WC_PAGE_PHONE_CONFIG) ||
		(wc.page == WC_PAGE_SERVER_CONFIG) ||
		(wc.page == WC_PAGE_MSG_CONFIG) ||
		(wc.page == WC_PAGE_DEVICE_CONFIG) ||
		(wc.page == WC_PAGE_WIFI_CONFIG) ||
#if CFG_GLOBAL_SIP_SUPPORT
		(wc.page == WC_PAGE_SIP_CONFIG) ||
#endif
		(wc.page == WC_PAGE_AUDIO_CONFIG) ||
		(wc.page == WC_PAGE_STUN_CONFIG) ||
#if CFG_GLOBAL_WIRED_ENET_SUPPORT
		(wc.page == WC_PAGE_ENET_CONFIG) ||
#endif
		//[2010-10-19] cgHyun
		(wc.page == WC_PAGE_UPGRADE) ||
		(wc.page == WC_PAGE_UPGRADE_AND_REBOOT) ||
		(wc.page == WC_PAGE_PHONE_LOGIN) ||
		(wc.page == WC_PAGE_PHONE_W200_AUDIO_CONTROL) ||
		(wc.page == WC_PAGE_PHONE_W110_AUDIO_CONTROL) ||
		(wc.page == WC_PAGE_UPLOAD))
	{
		if (streq (method, "POST") && streq (action, "submit"))
		{
			wc.action = WC_ACTION_SUBMIT;
			wc.content_len = atol(len);
		}
		else if (streq (action, "reset"))
		{
			wc.action = WC_ACTION_RESET_TO_DEFAULTS;
		}
	}
	else if (wc.page == WC_PAGE_NETWORKS_CONFIG)
	{
		if (streq (action, "configure"))
			wc.action = WC_ACTION_CONFIGURE;
		else if (streq (action, "remove"))
			wc.action = WC_ACTION_REMOVE;
		else if (streq (action, "moveup"))
			wc.action = WC_ACTION_MOVE_UP;
		else if (streq (action, "movedown"))
			wc.action = WC_ACTION_MOVE_DOWN;
		else if (streq (action, "remove_adhoc"))
			wc.action = WC_ACTION_REMOVE_ADHOC;
		else if (streq (action, "reload_cfg"))
			wc.action = WC_ACTION_RESET_CFG;
		else
			wc.action = WC_ACTION_SHOW;
	}
	else if (wc.page == WC_PAGE_REBOOT)
	{
		if (streq (action, "reboot"))
			wc.action = WC_ACTION_REBOOT;
	}
	else if (wc.page == WC_PAGE_PHONE_PW_INIT)
	{
		if (streq (action, "pw_init"))
			wc.action = WC_ACTION_PHONE_PW_INIT;
	}
	else if (wc.page == WC_PAGE_PHONE_LOGIN)
	{
		if (streq (action, "login"))
			wc.action = WC_ACTION_PHONE_LOGIN;
	}
	else if (wc.page == WC_PAGE_PHONE_LOGOUT)
	{
		if (streq (action, "logout"))
			wc.action = WC_ACTION_PHONE_LOGOUT;
	}
	else if (wc.page == WC_PAGE_PHONE_PASSWD_MODIFY)
	{
		if (streq (action, "pwmodify"))
			wc.action = WC_ACTION_PHONE_PW_MODIFY;
	}

	if (network && strlen (network))
	{
		wc.network = atol(network);
		if ((wc.network < WC_MIN_NETWORK_NUM) ||
			(wc.network > WC_MAX_NETWORK_NUM))
		{
			wc.page = WC_PAGE_STATUS;
			wc.action = WC_ACTION_SHOW;
		}
	}
	else
	{
		wc.network = WC_MIN_NETWORK_NUM;
	}
}

static void wc_show_table_start
(
 unsigned int border,
 unsigned int cellSpacing,
 unsigned int cellPadding
 )
{
	printf( "<table border=%d cellspacing=%d cellpadding=%d>\n",
		border,
		cellSpacing,
		cellPadding );
}

static void wc_show_table_end( void )
{
	printf( "</table>\n" );
}

static void wc_show_table_row_start( void )
{
	printf( "<tr>" );
}

static void wc_show_table_row_end( void )
{
	printf( "</tr>\n" );
}

static void wc_show_table_data_cell( const char *attributes, const char *displayStr )
{
	if ( attributes == NULL )
	{
		attributes = "";
	}

	printf( "<td %s>%s</td>\n", attributes, displayStr );
}

static void wc_print ( char *msg )
{
	printf ("<h2>%s</h2>\n", msg);
}

static void wc_regular_print ( char *msg )
{
	printf ("%s<BR>\n", msg);
}

static void wc_show_header ( void )
{
	printf ("Content-type: text/html\n\n");

	/* Print header file */
	print_file (WC_HEADER_HTML);
}

static void wc_show_footer ( void )
{
	print_file (WC_FOOTER_HTML);
}

static void wc_submit_login(void)
{
	int valid;
	//   int rc = 0;

	initialize_map (login_pw_map);

	/* Generate substitute markers */
	set_search_keys (login_pw_map);

	/* Process form input */
	valid = get_values_from_input (login_pw_map);

	if(valid)
	{
		char webMasterPwBuf[20] = {0, };
		char webUserPwBuf[20] = {0, };
		char webServicePwBuf[20] = {0, };

		//Audio setting For Keiti
		if(!strncmp(stLoginPw.input_login_pw, "audioman", 8))	
		{
			print_file(WC_SYSTEM_CONTROL_MAIN_HTML);
			return;
		}
//		 char buff[1024];
//  		getcwd(buff, 1024);                   // 작업 디렉토리 구하기
   		//wc_print(buff);

//             int nLen = strlen(buff);
//		strcpy(buff+nLen, FILENAME_SKB_CONF);
		if(access(FILENAME_SKB_CONF, F_OK) >= 0)
		{
			
            config_find_by_name(FILENAME_SKB_CONF, "webmasterpw",0, webMasterPwBuf);
			config_find_by_name(FILENAME_SKB_CONF, "webuserpw",0, webUserPwBuf);
			config_find_by_name(FILENAME_SKB_CONF, "webservicepw",0, webServicePwBuf);
            
            if ( !strncmp(stLoginPw.login_id, "admin", strlen(stLoginPw.login_id)))
            {
			    if(!strncmp(stLoginPw.input_login_pw, webMasterPwBuf, strlen(webMasterPwBuf)))	
				    print_file(WC_TOP_HTML);
                else
                    wc_print("Invalid password");
            }
            else if ( !strncmp(stLoginPw.login_id, "user", strlen(stLoginPw.login_id)))
            {
			    if(!strncmp(stLoginPw.input_login_pw, webUserPwBuf, strlen(webUserPwBuf)))	
				    print_file(WC_TOP_USER_HTML);
                else
                    wc_print("Invalid password");
            }
            else if ( !strncmp(stLoginPw.login_id, "service", strlen(stLoginPw.login_id)))
            {
			    if(!strncmp(stLoginPw.input_login_pw, webServicePwBuf, strlen(webServicePwBuf)))	
				    print_file(WC_TOP_SERVICE_HTML);
                else
                    wc_print("Invalid password");
            }	
				wc_print("Invalid Access!!!, ID/Password를 확인 하세요");	
		}
		else
		{
			wc_print("Invalid Access.....");	
			//print_file(WC_TOP_USER_HTML);
		}
	}
	else
	{
		/* Invalid entries found */
		wc_print("Invalid Entries");
	}
}

static void wc_show_top ( void )
{
	struct in_addr addr;
	char *p;

	initialize_map (top_map);

	p = (getipaddr(&addr.s_addr) > 0) ? inet_ntoa( addr ) : "0.0.0.0";

	// Put the active string into upgrade_map[0] & vice-versa
	top_map[0].value.str = p;
	top_map[0].valid = 1;
	top_map[0].changed = 0;

	/* Generate substitute markers */
	set_search_keys (top_map);

	/* Substitute page with current values */
	subst_and_print_file (WC_TOP_HTML, top_map);
}

/*
* get_pair_from_input - get key=value pair from posted form input
*/
static int get_pair_from_input (char **key, char **value)
{
	char buffer[CM_CFG_MAX_TEXT_LEN];
	int index;
	int ch;

	index = 0;
	*key = NULL;
	*value = NULL;

	while ((ch = get_real_char()) != EOF)
	{
		//putc(ch, stdout);
		switch (ch)
		{
		case '=':
			if (!(*key))
			{
				*key = strnew (buffer, index);
				index = 0;
			}
			else
			{
				/* Accept '=' as possible character in value */
				buffer[index++] = (char)ch;
			}
			break;

		case '&':
			if (!(*key))
			{
				/* Accept '&' as possible character in key */
				buffer[index++] = (char)ch;
			}
			else
			{
				*value = strnew (buffer, index);
				return 1;
			}
			break;

		default:
			buffer[index++] = (char)ch;
			break;
		}

		/* Length check */
		if (index >= CM_CFG_MAX_TEXT_LEN)
		{
			//ONE_LOG(ONE_LOG_ERROR, ("get_pair_from_input String length %d too large\n", index));
			return 0;
		}
	}

	/* Handle terminating case */
	if (*key)
	{
		*value = strnew (buffer, index);
		return 1;
	}

	return 0;
}

/*
* get_values_from_input - get values from posted form input
*/
static int get_values_from_input (map_t *map)
{
	map_t *map_entry;
	char *key;
	char *value;
	int valid;
	value_t parsed_value;
	int num_value;

	valid = 1;

	/* Initialize all input values in map */
	for (map_entry = map; map_entry->section && map_entry->key; map_entry++)
	{
		map_entry->input_value = "";
	}

	/* Get key=value pair from input stream */
	while (get_pair_from_input(&key, &value))
	{
		/* Search through map for value */
		map_entry = map;
		while (map_entry->section && map_entry->key)
		{
			if (streq (map_entry->search_key, key))
			{
				/* Match found, validate and store value */
				map_entry->input_value = value;
				break;
			}
			map_entry++;
		}
#if 0
		if (!map_entry->section)
		{
			printf ("Unmatched input %s=%s<p>", key, value);
		}
#endif

	}

	/* Process input values */
	map_entry = map;
	while (map_entry->section && map_entry->key)
	{
		parsed_value.num_ptr = &num_value;
		if (map_entry->parse_fn)
		{
			if ( map_entry->type == TYPE_ENUM )
			{
				map_entry->parse_fn(map_entry->input_value, &parsed_value, (value_t *)map_entry, map_entry->location);
			}
			else
			{
				map_entry->parse_fn(map_entry->input_value, &parsed_value, &map_entry->value, map_entry->location);
			}
		}
		else
		{
			parsed_value.str = map_entry->input_value;
		}

		if (!map_entry->validate_fn || map_entry->validate_fn(&parsed_value, map_entry->location))
		{
			/* Store changed validated value */
			if (!map_entry->equal_fn ||
				!map_entry->equal_fn(&parsed_value, &map_entry->value, map_entry->location))
			{
				map_entry->changed = 1;
				if (map_entry->location == LOC_LOCAL_VAR)
				{
					if (( map_entry->type == TYPE_NUM  )
						||  ( map_entry->type == TYPE_ENUM ))
					{
						*map_entry->value.num_ptr = *parsed_value.num_ptr;
					}
					else
					{
						strcpy(map_entry->value.str, parsed_value.str);
					}
				}
				else
				{
					memcpy (&map_entry->value, &parsed_value, sizeof(value_t));
				}
			}
		}
		else
		{
			map_entry->valid = 0;
			valid = 0;
		}
#if 0
		if (map_entry->valid)
		{
			printf ("Valid %s/%s = ", map_entry->section, map_entry->key);
			map_entry->print_fn(map_entry, value);
			printf ("<p>");
		}
		else
		{
			printf ("Invalid %s/%s = %s<p>", map_entry->section, map_entry->key, map_entry->input_value);
		}
#endif
		map_entry++;
	}

	return valid;
}

static void wc_submit_device_config()
{
    int valid;

    initialize_map (device_info_map);

    /* Generate substitute markers */
    set_search_keys (device_info_map);

    /* Process form input */
    valid = get_values_from_input (device_info_map);

    if (valid)
    {
        if(device_setting_api(PHONE_CFG_UPDATE_DEVICE) > 0)
        {
            /* Saved successfully */
            wc_print("Settings Saved Successfully....");
        }
        else
        {
            /* Save failed */
            wc_print("Save Failed");
        }
    }
    else
    {
        /* Invalid entries found */
        wc_print("Invalid Entries");
    }
    subst_and_print_file (WC_DEVICE_CONFIG_HTML, device_info_map);
}

static void wc_submit_phone_config ( void )
{
    int valid;

    initialize_map (phone_setting_map);

    /* Generate substitute markers */
    set_search_keys (phone_setting_map);

    /* Load phone config data.  */
    //      if (phone_cfg_api( PHONE_CFG_GET_PHONE ) < 0)
    //        return;

    /* Process form input */

    valid = get_values_from_input (phone_setting_map);

    if (valid)
    {
        if(phone_setting_api(PHONE_CFG_UPDATE_PHONE) > 0)
        {
            /* Saved successfully */
            wc_print("Settings Saved Successfully");
        }
        else
        {
            /* Save failed */
            wc_print("Save Failed");
        }
    }
    else
    {
        /* Invalid entries found */
        wc_print("Invalid Entries");
    }

    subst_and_print_file (WC_PHONE_CONFIG_HTML, phone_setting_map);
}

static void wc_show_upgrade ( void )
{
    char tempstr0[OTA_MAXDATE + OTA_MAXNAME + 1];
    char tempstr1[OTA_MAXDATE + OTA_MAXNAME + 1];
    OTA_CTRLBLOCK block;
    int active;
    int valid0 = 0;
    int valid1 = 0;
    initialize_map (upgrade_map);

    active = ota_getbank();

    if (ota_IsCtrlHdrValid(0)) { valid0 = 1; }
    if (ota_IsCtrlHdrValid(1)) { valid1 = 1; }

    ota_GetCtrlBlock(&block);

    // Get current configuration from flash for both banks
    GetFlashNameAndDate(0, valid0, &block.hdr[0], tempstr0, sizeof(tempstr0), 1);
    GetFlashNameAndDate(1, valid1, &block.hdr[1], tempstr1, sizeof(tempstr1), 1);

    // Put the active string into upgrade_map[0] & vice-versa
    upgrade_map[0].value.str = (active == 0) ? tempstr0 : tempstr1;
    upgrade_map[0].valid = 1;
    upgrade_map[0].changed = 0;

    // Inactive side
    upgrade_map[1].value.str = (active == 1) ? tempstr0 : tempstr1;
    upgrade_map[1].valid = 1;
    upgrade_map[1].changed = 0;

    // Browse button
    upgrade_map[2].valid = 1;
    upgrade_map[2].changed = 0;

    /* Generate substitute markers */
    set_search_keys (upgrade_map);

    /* Substitute page with current values */
    subst_and_print_file (WC_UPGRADE_HTML, upgrade_map);

}

int main( int argc, char **argv )
{
	int reload_net_config = 0;
	int reload_audio_sip_config = 0;
	int reboot = 0;

	(void)argc;
	(void)argv;

	//bcm_log_config( ONE_LOG_DEFAULT_HEADER, &(ONE_LOG_LEVEL(ONE_LOG_MODULE_ID)) );
	//wc_init();

	/* Get page and action */
	wc_get_page_and_action ();
	wc_show_header();

	/* Act on selected page */  
	switch (wc.page)
	{
	case WC_PAGE_STATUS:
	default:
		wc_show_status( 0 );
		break;

	case WC_PAGE_TOP:
		wc_show_top();
		break;

	case WC_PAGE_NETWORKS_CONFIG:
		switch (wc.action)
		{
		case WC_ACTION_CONFIGURE:
			/* Configure selected network */
		//	wc_show_wireless_config();
			break;

		case WC_ACTION_REMOVE:
			/* Remove selected network */
//			profile_api( PROF_DELETE_PROFILE, wc.network-1, 0 );

			/* Show networks page */
//			wc_show_networks_config();
			break;

		case WC_ACTION_MOVE_UP:
		case WC_ACTION_MOVE_DOWN:
			/* Re-order networks */
//			wc_swap_networks();

			/* Show networks page */
//			wc_show_networks_config();
			break;

		case WC_ACTION_REMOVE_ADHOC:
			/* Remove adhoc network */
//			wc_remove_adhoc_network();

			/* Show networks page */
//			wc_show_networks_config();
			break;

		case WC_ACTION_RESET_CFG:
			reload_net_config = 1;
			wc_print( "The Phone is now reseting its network connection.  Wait until it rejoins a network before trying to reconnect your web browser.");
			break;

		case WC_ACTION_SHOW:
		default:
//			wc_show_networks_config();
			break;
		}
		break;
#if 0
	case WC_PAGE_WIRELESS_CONFIG:
		if (wc.action == WC_ACTION_SHOW)
			wc_show_wireless_config();
		else
			wc_submit_wireless_config();
		break;

	case WC_PAGE_ADHOC_CONFIG:
		if (wc.action == WC_ACTION_SHOW)
			wc_show_adhoc_config();
		else
			wc_submit_adhoc_config();
		break;
#endif
#if CFG_GLOBAL_WIRED_ENET_SUPPORT
	case WC_PAGE_ENET_CONFIG:
		if (wc.action == WC_ACTION_SHOW)
			wc_show_enet_config();
		else
			wc_submit_enet_config();
		break;
#endif
	case WC_PAGE_PHONE_CONFIG:
		if (wc.action == WC_ACTION_SHOW)
			wc_show_phone_config();
		else
			wc_submit_phone_config();
		break;
	case WC_PAGE_SERVER_CONFIG:
		if (wc.action == WC_ACTION_SHOW)
			wc_show_server_config();
		//else
		//	wc_submit_phone_config();
		break;
	case WC_PAGE_MSG_CONFIG:
		if (wc.action == WC_ACTION_SHOW)
			wc_show_msg_config();
		else
			wc_submit_phone_config();
		break;
	case WC_PAGE_DEVICE_CONFIG:
		if (wc.action == WC_ACTION_SHOW)
			wc_show_device_config();
		else     
			wc_submit_device_config();
		break;
	case WC_PAGE_WIFI_CONFIG:
		if (wc.action == WC_ACTION_SHOW)
			wc_show_wifi_config();
		//else
		//	wc_submit_phone_config();
		break;
	case WC_PAGE_PHONE_CALL_LOG:
		print_call_log("/ramdisk/snmp/config/calllog.info");
		break;
	case WC_PAGE_PHONE_SIP_LOG:
		print_call_log("/tmp/__system_log__.log");
		break;	

#if CFG_GLOBAL_SIP_SUPPORT
	case WC_PAGE_SIP_CONFIG:
		if (wc.action == WC_ACTION_SHOW)
			wc_show_sip_config(0);
		else if (wc.action == WC_ACTION_RESET_TO_DEFAULTS)
			wc_show_sip_config(1);
		else
			reload_audio_sip_config = wc_submit_sip_config();
		break;
#endif

	case WC_PAGE_AUDIO_CONFIG:
//		if (wc.action == WC_ACTION_SHOW)
//			wc_show_audio_config(0);
//		else if (wc.action == WC_ACTION_RESET_TO_DEFAULTS)
//			wc_show_audio_config(1);
//		else
//			reload_audio_sip_config = wc_submit_audio_config();
	break;

#if CFG_GLOBAL_STUN_CLIENT
	case WC_PAGE_STUN_CONFIG:
		if (wc.action == WC_ACTION_SHOW)
			wc_show_stun_config(0);
		else if (wc.action == WC_ACTION_RESET_TO_DEFAULTS)
			wc_show_stun_config(1);
		else
			reload_audio_sip_config = wc_submit_stun_config();
		break;
#endif

	case WC_PAGE_UPGRADE:
		if (wc.action == WC_ACTION_SHOW)
			wc_show_upgrade();
		else
			wc_submit_upgrade(0);
		break;

	case WC_PAGE_UPGRADE_AND_REBOOT:
		if (wc.action == WC_ACTION_SHOW)
			wc_show_upgrade();
		else
			reboot = wc_submit_upgrade(1);
		break;


	case WC_PAGE_UPLOAD:
	//	if (wc.action == WC_ACTION_SHOW)
			//wc_show_upload();
	//	else
		{
			//wc_submit_upload();
		}
		break;


	case WC_PAGE_VERBOSE_DEBUG_STATS:
		wc_show_status( 1 );
		break;

	case WC_PAGE_SUMMARY_DEBUG_STATS:
		//wc_show_summary_status();
		break;

	case WC_PAGE_CPU_USAGE:
		wc_show_cpu_usage();
		break;

	case WC_PAGE_REBOOT:
		if (wc.action == WC_ACTION_REBOOT)
		{
			// This is the easiest way to trigger
			// a reboot from user mode. Should
			// a target ever be built without
			// busybox, a different way of
			// restarting will be required.
			printf("<h2>Rebooting Phone...</h2>\n");
			reboot = 1;
		}
		else
		{
			FILE *fp = fopen (WC_REBOOT_HTML, "r");
			if (!fp)
			{
				printf("Bad open: %s\n", strerror(errno));
			}
			else
			{
				int ch;
				while ((ch = getc(fp)) != EOF)
				{
					putc (ch, stdout);
				}
			}
		}
		break;

	case WC_PAGE_PHONE_PW_INIT:	
		if (wc.action == WC_ACTION_PHONE_PW_INIT)
		{
		//	Send_IPC_to_SIP(FSP_EM_IPC_CMD_WEB_SIP_PHONE_PASSWORD_INIT, 0, 0);
			printf("<h2>????? ????? 0000???? ???? ????????.</h2>\n");
		}
		break;

	case WC_PAGE_PHONE_LOGIN:
		if (wc.action == WC_ACTION_SUBMIT)
		{
			wc_submit_login();
		}
		else
		{
			wc_show_login_pw_config();
		}
		break;

	case WC_PAGE_PHONE_LOGOUT:
		if (wc.action == WC_ACTION_PHONE_LOGOUT)
		{
			wc_show_login_pw_config();
		}
		break;

	case WC_PAGE_PHONE_PASSWD_MODIFY:
		if (wc.action == WC_ACTION_PHONE_PW_MODIFY)
		{
			config_replace_by_name(FILENAME_SKB_CONF, "webuserpw", 0, g_sValue, '=', 1);
		}
		else
		{
			print_file(WC_PW_MODIFY_HTML);
		}
		break;	

	case WC_PAGE_PHONE_W200_AUDIO_CONTROL:
		if (wc.action == WC_ACTION_SUBMIT)
		{
//			wc_submit_w200_audio_control();
		}
		else
		{
//			wc_show_w200_audio_config();
		}

		break;

	case WC_PAGE_PHONE_W110_AUDIO_CONTROL:
		if (wc.action == WC_ACTION_SUBMIT)
		{
//			wc_submit_w110_audio_control();
		}
		else
		{
//			wc_show_w110_audio_config();
		}

		break;	

	}

	wc_show_footer();

	//wc_cleanup( reload_net_config, reload_audio_sip_config, reboot );

	return 0;
}
