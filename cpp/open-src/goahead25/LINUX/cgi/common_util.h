#ifndef COMMON_UTIL_H
#define COMMON_UTIL_H

#define WAN_IFNAME			"eth0"

enum FSPEmIpcCmd
{
	//FSP_EM_IPC_CMD_from_to_command <- ��~R~R~U��~R~R~]��~R~R~E��~R~R~H��.
	FSP_EM_IPC_CMD_CWMP_SIP_RESET           	= 0x0001,
	FSP_EM_IPC_CMD_CWMP_SIP_MODIFY_LINE     	= 0x0002,
	FSP_EM_IPC_CMD_CWMP_SIP_MODIFY_SIP      	= 0x0004,
	FSP_EM_IPC_CMD_CWMP_SIP_MODIFY_SIP_AUTH 	= 0x0008,
	FSP_EM_IPC_CMD_CWMP_SIP_MODIFY_LIST     	= 0x0010,
	FSP_EM_IPC_CMD_CWMP_SIP_LOG_ON	    		= 0x0020, 
	FSP_EM_IPC_CMD_CWMP_SIP_LOG_OFF	    		= 0x0030,
	FSP_EM_IPC_CMD_CWMP_SIP_LOG_TYPE_CHANGE	    = 0x0031,
	FSP_EM_IPC_CMD_CWMP_SIP_LOG_ADDR_CHANGE	    = 0x0032,
	FSP_EM_IPC_CMD_CWMP_SIP_REBOOT          	= 0x0080,

	FSP_EM_IPC_CMD_SIP_CWMP_OFFHOOK 			= FSP_EM_IPC_CMD_CWMP_SIP_REBOOT + 1,
	FSP_EM_IPC_CMD_SIP_CWMP_CALLEND,
	FSP_EM_IPC_CMD_SIP_CWMP_REGISTERED,
	FSP_EM_IPC_CMD_SIP_CWMP_UNREGISTERED,
	FSP_EM_IPC_CMD_SIP_CWMP_CALLSTATS,

	FSP_EM_IPC_CMD_HANDY_CWMP_RESET,
	FSP_EM_IPC_CMD_HANDY_CWMP_FRESET,

	FSP_EM_IPC_CMD_CWMP_HANDY_MSG1, 				// �ֽŹ����� ������Դϴ�.
	FSP_EM_IPC_CMD_CWMP_HANDY_MSG2, 				// S/W ������Ʈ�� �����մϴ�.             
	FSP_EM_IPC_CMD_CWMP_HANDY_MSG3, 				// S/W �ٿ�ε� ���Դϴ�. 0x008a
	FSP_EM_IPC_CMD_CWMP_HANDY_MSG4, 				// S/W �ٿ�ε� ���Դϴ�.(���α׷��� ǥ��)
	FSP_EM_IPC_CMD_CWMP_HANDY_MSG5, 				// S/W ��ġ���Դϴ�.                      
	FSP_EM_IPC_CMD_CWMP_HANDY_MSG6, 				// S/W ������Ʈ�� �����Ͽ����ϴ�.         
	FSP_EM_IPC_CMD_CWMP_HANDY_MSG7, 				// �ý����� ������մϴ�.                 

	FSP_EM_IPC_CMD_CWMP_HANDY_ERR_MSG1,				// S/W ������Ʈ ���� (���ϼ��� �������)
	FSP_EM_IPC_CMD_CWMP_HANDY_ERR_MSG2,				// S/W ������Ʈ ���� (���ϼ��� �α��ν���)90
	FSP_EM_IPC_CMD_CWMP_HANDY_ERR_MSG3,				// S/W ������Ʈ ���� (���ϼ��� ����)
	FSP_EM_IPC_CMD_CWMP_HANDY_ERR_MSG4,				// S/W ������Ʈ ���� (���Ϲ��Ἲ ����)
	FSP_EM_IPC_CMD_CWMP_HANDY_ERR_MSG5,				// S/W ������Ʈ ���� (��ġ����)
	FSP_EM_IPC_CMD_CWMP_HANDY_FIRM_READY, 			// Handy Firmware DOWN & CRC OK Param[Physical path&filename]      

	FSP_EM_IPC_CMD_CURL_WRONG_URL,

	FSP_EM_IPC_CMD_CWMP_HANDY_ERR_MSG11,			// S/W ������Ʈ ���� (���׷��̵弭�� ��������)
	FSP_EM_IPC_CMD_CWMP_HANDY_ERR_MSG12,			// S/W ������Ʈ ���� (���׷��̵弭�� �������)
	FSP_EM_IPC_CMD_CWMP_HANDY_ERR_MSG13,			// S/W ������Ʈ ���� (���׷��̵� �������� ����)
	FSP_EM_IPC_CMD_CWMP_HANDY_ERR_MSG14,			// S/W ������Ʈ ���� (���׷��̵� ���� ����)99

	FSP_EM_IPC_CMD_CURL_UPGRADEINFO_NOK =0x0110,  	//272
	FSP_EM_IPC_CMD_CURL_DNSERVERINFO_NOK, 			//273
	FSP_EM_IPC_CMD_CURL_SODE_CONNECT_FAIL,			//374
	FSP_EM_IPC_CMD_CURL_SODE_NON_200,				//275
	FSP_EM_IPC_CMD_CURL_SODE_NON_200PRARAM_ERR,		//276
	FSP_EM_IPC_CMD_CURL_DOWNLOAD_START,				//277
	FSP_EM_IPC_CMD_CURL_DOWNLOAD_CONNECT_ERR,		//278
	FSP_EM_IPC_CMD_CURL_DOWNLOAD_LOGIN_ERR,			//279
	FSP_EM_IPC_CMD_CURL_DOWNLOAD_SERVER_ERR,		//280
	FSP_EM_IPC_CMD_CURL_DOWNLOAD_FAIL,				//281
	FSP_EM_IPC_CMD_CURL_DOWNLOAD_CRC_ERR,			//282
	FSP_EM_IPC_CMD_CURL_DOWNLOAD_OK,				//283
	FSP_EM_IPC_CMD_CURL_INSTALL_ERR,				//284
	FSP_EM_IPC_CMD_CURL_INSTALL_OK,					//285
	FSP_EM_IPC_CMD_CURL_TRANS_COMPLETE,				//286
	FSP_EM_IPC_CMD_CURL_AUTOTRANS_COMPLETE,			//287
	FSP_EM_IPC_CMD_CURL_TMER_RESET,					//288
	FSP_EM_IPC_CMD_CURL_MANUAL_UPGRADE,				//289
	FSP_EM_IPC_CMD_CURL_BOOT_START,					//290
	FSP_EM_IPC_CMD_CURL_START_PKGID,				//291

	FSP_EM_IPC_CMD_ANY_CWMP_LINKUP,					//292
	FSP_EM_IPC_CMD_ANY_CWMP_IPALLOC,				//293
	FSP_EM_IPC_CMD_SIP_CWMP_ONHOOK,					//294
	FSP_EM_IPC_CMD_CURL_ACS_MODIFY,					//295
	FSP_EM_IPC_CMD_HANDY_UPGRADE_OK,				//296
	FSP_EM_IPC_CMD_HANDY_UPGRADE_FAIL,				//297
	FSP_EM_IPC_CMD_CURL_HANDY_UPGRADE,				//298
	FSP_EM_IPC_CMD_SIP_QUERY_REGISTER,				//299
	FSP_EM_IPC_CMD_CWMP_RESPONSE_REGISTER_WAIT,		//300
	FSP_EM_IPC_CMD_CWMP_RESPONSE_REGISTER_GO,		//301
	FSP_EM_IPC_CMD_CWMP_SIP_PROBE_OK,				//302
	FSP_EM_IPC_CMD_HANDY_UPGRADE_CANCEL,			//303

    FSP_EM_IPC_CMD_FACTORY_RESET,		//304 �����ʱ�ȭ �ڵ�/���̽� ��� �ʱ�ȭ
    FSP_EM_IPC_CMD_CONFIGINIT_CMD1,		//305 Config Init
    FSP_EM_IPC_CMD_CONFIGINIT_CMD2,		//306 AP Init
    FSP_EM_IPC_CMD_CONFIGINIT_CMD3,		//307 VoIP Init

	//HCG
	FSP_EM_IPC_CMD_CWMP_SIP_NETWORK_COMMIT = 0x0190,	//400
	FSP_EM_IPC_CMD_CWMP_SIP_DOWNLOAD_REQUEST,			//401
	FSP_EM_IPC_CMD_SIP_CWMP_CURL_DOWNLOAD_START,		//402
	FSP_EM_IPC_CMD_SIP_CWMP_CWMP_EXIT,					//403
	FSP_EM_IPC_CMD_CWMP_SIP_FUSING_PROGRESS,			//404
	
	//TRAP Message
	FSP_EM_IPC_CMD_SIP_CWMP_MODIFY_IPADDRESS = 500,
	FSP_EM_IPC_CMD_SIP_CWMP_PROXY_ADDRESS_MODIFY,
	FSP_EM_IPC_CMD_SIP_CWMP_MODIFY_PORXY_INDEX,	// 1:SSW	2:SBC
	FSP_EM_IPC_CMD_SIP_CWMP_MODIFY_REGSTS,		// 1:Reg	2:Unreg
	FSP_EM_IPC_CMD_SIP_CWMP_MODIFY_DISPLAYNAME,
	FSP_EM_IPC_CMD_SIP_CWMP_MODIFY_PHONENUMBER,
	FSP_EM_IPC_CMD_SIP_CWMP_QOS_TRAP_REQUEST,
	FSP_EM_IPC_CMD_WEB_CWMP_POWERON_TRAP_REQUEST,

	FSP_EM_IPC_CMD_WEB_SIP_MODIFY_DTMF_METHOD = 550,	
	FSP_EM_IPC_CMD_WEB_SIP_PHONE_PASSWORD_INIT,
	FSP_EM_IPC_CMD_WEB_SIP_CODEC_PERIOITY_MODIFY,
	FSP_EM_IPC_CMD_WEB_SIP_CODEC_VAD_MODIFY,

    // for SKB
    // TAPS Conrol
    FSP_EM_IPC_CMD_WEB_SIP_TAPS_CONTROL      = 600,
    FSP_EM_IPC_CMD_WEB_SIP_AREA_CODE,
    FSP_EM_IPC_CMD_WEB_SIP_NTP_SET,
};

#pragma pack(1)
typedef struct sConfigArg{
	int found;
	char *name;
	char *sub_name;
	char *data;
}stConfigArg;
#pragma pack()

extern int config_find_by_name(char *filename, char *name, char *sub_name, char *data);
extern int config_find_multi_by_name(char *filename, int argc, struct sConfigArg *argv);
extern int save_file_to_flash(char *filename); 
extern int config_replace_by_name(char *filename, char *name, char *sub_name, char *data, char delim, int save);
extern int config_replace_multi_by_name(char *filename, int argc, struct sConfigArg *argv, char delim, int save);
extern int ifx_GetCfgData(char *pFileName, char *pTag, char *pData, char *pRetValue);
extern int move_file(char *filename, char *dest_filename);
extern int copy_file(char *filename, char *dest_filename);
extern int Send_IPC_to_SIP(int cmd, char *body, int size);
extern int Send_IPC_to_CWMP(int cmd, char *body, int size);

#endif
