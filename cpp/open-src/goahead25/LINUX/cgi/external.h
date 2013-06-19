#define UINT32 unsigned int

extern int getipaddr( UINT32 *ipaddr);
int getethaddr( char ethaddr[6]);

#define CM_CFG_MAX_PROFILES 9
#define CM_CFG_MAX_SIP_ID_LEN 10
#define CM_CFG_MAX_TEXT_LEN 256

typedef enum
{
   SRTP_OFF,
   SRTP_OPTIONAL,
   SRTP_MANDATORY
} cm_cfg_srtp;

typedef enum
{
   CNG_MODE_NOISEOFF = 0,   /* Generate silence */
   CNG_MODE_WHITENOISE,     /* Generate White noise */
   CNG_MODE_HOTHNOISE,       /* Generate Hoth noise */
   CNG_MODE_MATCHSPECTRUM    /* match spectrum noise */
} cm_cfg_cng_mode;

typedef enum
{
   PLC_MODE_DEFAULT = 0,    /* Use G.711 Appendix I */
   PLC_MODE_OFF,
   PLC_MODE_FRAMEREPEAT,    /* Use Frame Repeat */
   PLC_MODE_G711_APXI,      /* Use G.711 APXI */
   PLC_MODE_BVC             /* Use BVC */
} cm_cfg_plc_mode;

typedef enum
{
   VAD_LEVEL_TRANSPARENT = 1,
   VAD_LEVEL_CONSERVATIVE = 2,
   VAD_LEVEL_AGGRESSIVE = 3
} cm_cfg_vad_level;


typedef struct
{
   int speaker_volume;  /* Analog hardware setting */
   int mic_gain;        /* Analog hardware setting */
   int speaker_volume_dig; /* Digital hardware setting - may or may not be supported by hardware */
   int mic_gain_dig;       /* Digital hardware setting - may or may not be supported by hardware */

} CM_CFG_AUDIO_BLOCK_SETTINGS;

typedef enum
{
   CM_CFG_AUDIO_MODE_HANDSET = 0,
   CM_CFG_AUDIO_MODE_HEADSET = 1,
   CM_CFG_AUDIO_MODE_SPEAKERPHONE = 2,
   CM_CFG_AUDIO_MODE_AUX_SPEAKERPHONE = 3,
   CM_CFG_AUDIO_MODE_NUM_MODES = 4
} CM_CFG_AUDIO_MODE;

typedef enum
{
   CM_CFG_AUDIO_BLOCK_EAR = 0,
   CM_CFG_AUDIO_BLOCK_AUX = 1,
   CM_CFG_AUDIO_BLOCK_NUM_BLOCKS = 2,

} CM_CFG_AUDIO_BLOCKS;

typedef struct
{
   CM_CFG_AUDIO_BLOCK_SETTINGS audioBlock[CM_CFG_AUDIO_BLOCK_NUM_BLOCKS];
   int sidetone;
   int SWgain;
   int SWvolume;

} CM_CFG_AUDIO_MODE_SETTINGS;

typedef struct
{
   int ring_delay;
   char ring_file[CM_CFG_MAX_TEXT_LEN+1];
   int ringer_volume;
   int ringer_speaker_volume;
   int ringer_speaker_volume_dig;
   int polyringer_speaker_volume;
   int polyringer_speaker_volume_dig;
   int vibrating_alert;

   CM_CFG_AUDIO_MODE_SETTINGS audioMode[CM_CFG_AUDIO_MODE_NUM_MODES];

   //  Former RTP section:
   //
   cm_cfg_srtp secure_rtp;
   int         min_rtp_port;
   int         max_rtp_port;
   int         rfc2833;
   int         SidRxSupported;
   int         SidTxNever;
   int         ptime;
   char        audioCodec[CM_CFG_MAX_TEXT_LEN+1];
   cm_cfg_vad_level vad_level;
   cm_cfg_cng_mode  cng_mode;
   int         noiseSuppress;
   cm_cfg_plc_mode  plc_mode;
   int         isExternalSpeakersEnabled;
} cm_cfg_audio;

typedef struct
{
   char bgimage[CM_CFG_MAX_TEXT_LEN+1];
   int backlight;
   int brightness;
   int powersave;
   int lcdoff;
   int autoans;
   int wlpmenable;
   int wlapsdtimeout;
   int call_waiting;
   int block_out_id;
   int refuse_if_no_id;

#if CFG_GLOBAL_VIDEO_SUPPORT
   int pkt_through_userspace;
#endif
} cm_cfg_phone;
