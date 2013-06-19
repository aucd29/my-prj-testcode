#include <stdio.h>
#include <string.h>
#include <sys/file.h>
#include <stdlib.h>
#include <ctype.h>
#include <unistd.h>
#include "common_util.h"

#define MAX_IPC_PARAMETER  		508
#define MAX_IPC_BODY_LEN 		500
#define PERMS 					0666 /* octal value*/
#define BUFFER_LEN  			50

static int tmp_filename_count=0;
static int fp_write = 0;

//Function Prototype
int write_ipc_parameter(int cmd, char *body, int size, char **write_buf);
int Send_IPC_to_SIP(int cmd, char *body, int size);
int Send_IPC_to_CWMP(int cmd, char *body, int size);

int config_find_by_name(char *filename, char *name, char *sub_name, char *data)
{
	FILE *fp;
	char buffer[256], *token, *line, *sub_line = NULL;
	int i;
	int found=0;
	fp = fopen(filename, "r+t");

	if (fp==NULL) return -1;

	if (flock(fileno(fp), LOCK_EX) < 0){
		fclose(fp);
		return -1;
	}
	
	while (fgets(buffer, 256, fp)) {
		if (strchr(buffer, '\n')) *(strchr(buffer, '\n')) = '\0';
		if (strchr(buffer, '#')) *(strchr(buffer, '#')) = '\0';
		if (strchr(buffer, ';')) *(strchr(buffer, ';')) = '\0';

		token = buffer + strspn(buffer, " \t");
		if (*token == '\0') continue;

		line = token + strcspn(token, " \t=");
		if (*line == '\0') continue;

		*line = '\0';
		line++;

		/* eat leading whitespace */
		line = line + strspn(line, " \t=\"");
		/* eat trailing whitespace */
		for (i = strlen(line); i > 0 && (isspace(line[i - 1]) || line[i-1]=='\"'); i--);
		line[i] = '\0';

		if (sub_name && *sub_name){
			sub_line = line + strcspn(line, " \t");
			if (*sub_line ){
				*sub_line = '\0';

				for (i = strlen(line); i > 0 && (isspace(line[i - 1]) || line[i-1]=='\"'); i--);
				line[i] = '\0';

				sub_line++;
				sub_line = sub_line + strspn(sub_line, " \t");
			}
		}

		if (sub_name && *sub_name){
			if (strcmp(token, name)==0 && strcmp(line, sub_name)==0){
				strcpy(data, sub_line);
				found=1;
				break;
			}
		}
		else if (strcmp(token, name)==0){
			strcpy(data, line);
			found=1;
			break;
		}
	}

	flock(fileno(fp), LOCK_UN);

	fclose(fp);
	
	usleep(100);
	
	return found;
}

int config_find_multi_by_name(char *filename, int argc, struct sConfigArg *argv)
{
	FILE *fp;
	char buffer[256], orig[256], *token, *line, *sub_line;
	int i, j;

	for (j=0;j<argc;j++){
		argv[j].found=0;
	}

	for (j=0;j<argc;j++){
		argv[j].sub_name=NULL;
	}

	fp = fopen(filename, "r+t");
	if (fp==NULL){
		return -1;
	}

	if (flock(fileno(fp), LOCK_EX) < 0){
		fclose(fp);
		return -1;
	}

	orig[0]=0;
	while (fgets(buffer, 256, fp)) {
		if (*buffer){
			strcpy(orig, buffer);
		}
		else{
			continue;
		}

		if (strchr(buffer, '\n')) *(strchr(buffer, '\n')) = '\0';
		if (strchr(buffer, '#')) *(strchr(buffer, '#')) = '\0';
		if (strchr(buffer, ';')) *(strchr(buffer, ';')) = '\0';

		token = buffer + strspn(buffer, " \t");
		if (*token == '\0'){
			continue;
		}

		line = token + strcspn(token, " \t=");
		if (*line == '\0'){
			continue;
		}

		*line = '\0';
		line++;

		/* eat leading whitespace */
		line = line + strspn(line, " \t=\"");

		/* eat trailing whitespace */
		for (i = strlen(line); i > 0 && (isspace(line[i - 1])||line[i-1]=='\"'); i--);
		line[i] = '\0';

		sub_line = line + strcspn(line, " \t");
		if (*sub_line ){
			*sub_line = '\0';

			for (i = strlen(line); i > 0 && (isspace(line[i - 1]) || line[i-1]=='\"'); i--);
			line[i] = '\0';

			sub_line++;
			sub_line = sub_line + strspn(sub_line, " \t");
		}
		else{
		}


		for (j=0;j<argc;j++){
			if (argv[j].sub_name && *argv[j].sub_name){
				if (strcmp(token, argv[j].name)==0 && strcmp(line, argv[j].sub_name)==0){
					strcpy(argv[j].data, sub_line);
					argv[j].found=1;
				}
			}
			else if (strcmp(token, argv[j].name)==0){
				strcpy(argv[j].data, line);
				argv[j].found=1;
			}
		}
	}

	if (fp) flock(fileno(fp), LOCK_UN);
	if (fp) fclose(fp);

	usleep(100);
	
	return 1;
}

int save_file_to_flash(char *filename) 
{
    char command[256] = {0,};
	char dest_path[256] = {0,};
	int i;
	char sDest_temp2[256] = {0,};
	char dest_filename[256] = {0,};

#if 0
	strcpy(dest_filename, "/nvdata/");
	strcpy(sDest_temp2, filename + strlen("/ramdisk/"));
#else
    strcpy(dest_filename, filename);
    strcpy(sDest_temp2, filename + strlen("/root/"));
#endif
    strcat(dest_filename, sDest_temp2);
	strcpy(dest_path, dest_filename);

	for (i=strlen(dest_path)-1;i>=0;i--){
		if (dest_path[i]=='/'){
			dest_path[i]=0;
			break;
		}
	}

	if (i>0 && dest_path[0]!=0){
		sprintf(command, "mkdir -p %s", dest_path);
		//system(command);
	}

	sprintf(command, "/bin/cp -f %s %s", filename, dest_filename);

	//system(command);

    char test[256] = {0,};
    sprintf(test, "echo %s", command);
    system(test);

	return 1;
}

int config_replace_by_name(char *filename, char *name, char *sub_name, char *data, char delim, int save)
{
	FILE *fp;
	FILE *fp_w;
	char buffer[256], orig[256], *token, *line, *sub_line;
	int i;
	char temp_filename[256];
	int found=0;

	fp = fopen(filename, "r+t");
	if (fp==NULL){
        //return -1;
		sprintf(temp_filename, "/tmp/config1_%04d.tmp", tmp_filename_count++);
		fp_w = fopen(temp_filename, "w+t");
		if (fp_w==NULL){
			fclose(fp);		
		}

		goto exit;
		//return -1;
	}

	if (flock(fileno(fp), LOCK_EX) < 0){

		fclose(fp);
		return -1;
	}

	sprintf(temp_filename, "/tmp/config_%04d.tmp", tmp_filename_count++);

	fp_w = fopen(temp_filename, "w+t");
	if (fp_w==NULL){
		fclose(fp);		
	}

	if (flock(fileno(fp_w), LOCK_EX) < 0){
		flock(fileno(fp), LOCK_UN);
		fclose(fp);
		fclose(fp_w);
		return -1;
	}

	orig[0]=0;
	while (fgets(buffer, 256, fp)) {
		if (*buffer){
			strcpy(orig, buffer);
		}
		else{
			continue;
		}

		if (strchr(buffer, '\n')) *(strchr(buffer, '\n')) = '\0';
		if (strchr(buffer, '#')) *(strchr(buffer, '#')) = '\0';
		if (strchr(buffer, ';')) *(strchr(buffer, ';')) = '\0';

		token = buffer + strspn(buffer, " \t");
		if (*token == '\0'){
			fprintf(fp_w, orig);
			continue;
		}


		line = token + strcspn(token, " \t=");
		if (*line == '\0'){
			fprintf(fp_w, orig);
			continue;
		}

		*line = '\0';
		line++;

		/* eat leading whitespace */
		line = line + strspn(line, " \t=\"");

		/* eat trailing whitespace */
		for (i = strlen(line); i > 0 && (isspace(line[i - 1]) || line[i-1]=='\"'); i--);
		line[i] = '\0';

		if (sub_name && *sub_name){
			sub_line = line + strcspn(line, " \t");
			if (*sub_line ){
				*sub_line = '\0';

				for (i = strlen(line); i > 0 && (isspace(line[i - 1]) || line[i-1]=='\"'); i--);
				line[i] = '\0';

				sub_line++;
				sub_line = sub_line + strspn(sub_line, " \t");
			}
		}

		if (sub_name && *sub_name){
			if (strcmp(token, name)==0 && strcmp(line, sub_name)==0){
				//strcpy(data, sub_line);
				sprintf(orig, "%s%c%s %s\n", token, delim, line, data);
				found=1;
			}
		}
		else if (strcmp(token, name)==0){
			//strcpy(data, line);
			sprintf(orig, "%s%c%s\n", token, delim, data);
			found=1;
		}

		fprintf(fp_w, orig);
	}

exit:	
	if (found==0){
		if (sub_name && *sub_name){
			fprintf(fp_w, "%s%c%s %s\n", name, delim, sub_name, data);
		}
		else{
			fprintf(fp_w, "%s%c%s\n", name, delim, data);
		}
	}
	if (fp) flock(fileno(fp), LOCK_UN);
	if (fp_w) flock(fileno(fp_w), LOCK_UN);

	if (fp) fclose(fp);
	if (fp_w) fclose(fp_w);

	if (remove(filename) == 0)
	{
		char cmd[128];
		sprintf(cmd, "cp -f %s %s",  temp_filename, filename);
		system(cmd);
		remove(temp_filename);
	}
	else{
		char cmd[128];
		sprintf(cmd, "cp -f %s %s",  temp_filename, filename);
		system(cmd);
		remove(temp_filename);
	}

	//if(save) save_file_to_flash(filename);

	return 1;
}

int config_replace_multi_by_name(char *filename, int argc, struct sConfigArg *argv, char delim, int save)
{
	FILE *fp;
	FILE *fp_w;
	char buffer[256], orig[256], *token, *line, *sub_line;
	int i, j;
	char temp_filename[256];

	for (j=0;j<argc;j++){
		argv[j].found=0;
	}

	for (j=0;j<argc;j++){
		argv[j].sub_name=NULL;
	}

	fp = fopen(filename, "r+t");
	if (fp==NULL){
		sprintf(temp_filename, "/tmp/config_%04d.tmp", tmp_filename_count++);

		fp_w = fopen(temp_filename, "w+t");
		if (fp_w==NULL){
			fclose(fp);		
		}

		goto exit;
	}

	if (flock(fileno(fp), LOCK_EX) < 0){
		fclose(fp);
		return -1;
	}

	sprintf(temp_filename, "/tmp/config_%04d.tmp", tmp_filename_count++);

	fp_w = fopen(temp_filename, "w+t");
	if (fp_w==NULL){
		fclose(fp);		
	}

	if (flock(fileno(fp_w), LOCK_EX) < 0){
		flock(fileno(fp), LOCK_UN);
		fclose(fp);
		fclose(fp_w);
		return -1;
	}

	orig[0]=0;
	while (fgets(buffer, 256, fp)) {
		if (*buffer){
			strcpy(orig, buffer);
		}
		else{
			continue;
		}

		if (strchr(buffer, '\n')) *(strchr(buffer, '\n')) = '\0';
		if (strchr(buffer, '#')) *(strchr(buffer, '#')) = '\0';
		if (strchr(buffer, ';')) *(strchr(buffer, ';')) = '\0';

		token = buffer + strspn(buffer, " \t");
		if (*token == '\0'){
			fprintf(fp_w, orig);
			continue;
		}

		line = token + strcspn(token, " \t=");
		if (*line == '\0'){
			fprintf(fp_w, orig);
			continue;
		}

		*line = '\0';
		line++;

		/* eat leading whitespace */
		line = line + strspn(line, " \t=\"");

		/* eat trailing whitespace */
		for (i = strlen(line); i > 0 && (isspace(line[i - 1])||line[i-1]=='\"'); i--);
		line[i] = '\0';

		sub_line = line + strcspn(line, " \t");
		if (*sub_line ){
			*sub_line = '\0';

			for (i = strlen(line); i > 0 && (isspace(line[i - 1]) || line[i-1]=='\"'); i--);
			line[i] = '\0';

			sub_line++;
			sub_line = sub_line + strspn(sub_line, " \t");
		}

		for (j=0;j<argc;j++){

			if (argv[j].sub_name && *argv[j].sub_name){
				if (strcmp(token, argv[j].name)==0 && strcmp(line, argv[j].sub_name)==0){
					sprintf(orig, "%s%c%s %s\n", token, delim, line, argv[j].data);
					argv[j].found=1;
				}
			}
			else if (strcmp(token, argv[j].name)==0){
				sprintf(orig, "%s%c%s\n", token, delim, argv[j].data);
				argv[j].found=1;
			}
		}

		fprintf(fp_w, orig);
	}
exit:
	for (j=0;j<argc;j++){
		if (argv[j].found==0){
			if (argv[j].sub_name && *argv[j].sub_name){
				fprintf(fp_w, "%s%c%s %s\n", argv[j].name, delim, argv[j].sub_name, argv[j].data);
			}
			else{
				fprintf(fp_w, "%s%c%s\n", argv[j].name, delim, argv[j].data);
			}
		}
	}
	if (fp) flock(fileno(fp), LOCK_UN);
	if (fp_w) flock(fileno(fp_w), LOCK_UN);

	if (fp) fclose(fp);
	if (fp_w) fclose(fp_w);

	if (remove(filename) == 0)
	{
		char cmd[128];
		sprintf(cmd, "cp -f %s %s",  temp_filename, filename);
		system(cmd);
		remove(temp_filename);
	}
	else{
		char cmd[128];
		sprintf(cmd, "cp -f %s %s",  temp_filename, filename);
		system(cmd);
		remove(temp_filename);
	}

	if(save) save_file_to_flash(filename);
	
	return 1;
}

int move_file(char *filename, char *dest_filename)
{
	FILE *fp;
	FILE *fp_w;
	char buffer[256];
	int len;

	fp=fp_w=NULL;

	fp = fopen(filename, "r+t");
	if (fp==NULL){
		return -1;
	}

	if (flock(fileno(fp), LOCK_EX) < 0){
		fclose(fp);
		return -1;
	}

	fp_w = fopen(dest_filename, "w+t");
	if (fp_w==NULL){
		fclose(fp);		
	}

	if (flock(fileno(fp_w), LOCK_EX) < 0){
		goto exit;
	}

	while(1){
		len=fread(buffer, 1, 256, fp);
		if (len<=0) break;
		fwrite(buffer, 1, len, fp_w);
	}
	
exit:	
	if (fp) flock(fileno(fp), LOCK_UN);
	if (fp_w) flock(fileno(fp_w), LOCK_UN);

	if (fp) fclose(fp);
	if (fp_w) fclose(fp_w);

	unlink(filename);
	return 1;
}

int copy_file(char *filename, char *dest_filename)
{
	FILE *fp;
	FILE *fp_w;
	char buffer[256];
	int len;

	fp=fp_w=NULL;

	fp = fopen(filename, "r+t");
	if (fp==NULL){
		return -1;
	}

	if (flock(fileno(fp), LOCK_EX) < 0){
		fclose(fp);
		return -1;
	}

	fp_w = fopen(dest_filename, "w+t");
	if (fp_w==NULL){
		fclose(fp);		
	}

	if (flock(fileno(fp_w), LOCK_EX) < 0){
		goto exit;
	}

	while(1){
		len=fread(buffer, 1, 256, fp);
		if (len<=0) break;
		fwrite(buffer, 1, len, fp_w);
	}
	
exit:	
	if (fp) flock(fileno(fp), LOCK_UN);
	if (fp_w) flock(fileno(fp_w), LOCK_UN);

	if (fp) fclose(fp);
	if (fp_w) fclose(fp_w);

	usleep(100);

	return 1;
}

int ifx_GetCfgData(char *pFileName, char *pTag, char *pData, char *pRetValue)
{
        FILE    *fd;
 //       char  *pString = NULL;
#if 0
        char  *buffer = NULL;
#else
		//char  buffer[1024*RC_SIZE_K];
		char  buffer[1024];
#endif
        char  arrBeginTag[64];
        char  arrEndTag[64];
        char  arrDataTag[64];
        char *tmp;
        
        char  sValue[1024];
        int             nReturn = 0;
        int             nLine = 0;
        int             nIndex = 1;
#if 0
				*pRetValue=0;
#endif				
        if ( pTag && *pTag )
        {
            if ((fd = fopen(pFileName, "r")) != NULL)
            {
#if 0
                buffer = (char *)malloc(1024*RC_SIZE_K*sizeof(char));
#endif
                if (buffer != NULL)
                {
                        //memset(buffer, 0x00, 1024*RC_SIZE_K);
                }
                else
                {
                        nReturn = 0;
                        goto exit;
                }

				sprintf(arrBeginTag, "#<< %s", pTag);
				sprintf(arrEndTag, "#>> %s", pTag);
				sprintf(arrDataTag, "%s=", pData);
				
				nReturn = 0;
				while(fgets(buffer, 1024, fd)!=0){
					//printf("%s", buffer);
					if (strncmp(buffer, arrBeginTag, strlen(arrBeginTag))==0){
						while(fgets(buffer, 1024, fd)!=0){
							//printf("-%s", buffer);
							
							if (strncmp(buffer, arrDataTag, strlen(arrDataTag))==0){
								int len,i;
								
								tmp=buffer+strlen(arrDataTag);
								
								while((*tmp==' ' || *tmp=='\n' || *tmp=='\r' || *tmp=='\t' || *tmp=='\"' || *tmp=='\'') && *tmp!=0) tmp++;
								len=strlen(tmp);
								for (i=len-1;i>=0;i--){
									if (tmp[i]==' ' || tmp[i]=='\n' || tmp[i]=='\r' || tmp[i]=='\t' || tmp[i]=='\"' || tmp[i]=='\'') tmp[i]=0;
									else break;
								}	
								
								if (pRetValue)
								{
									if(strlen(tmp)==0)	
									{
										nReturn=0;
									}
									else
									{
										strcpy(pRetValue, tmp);
										nReturn = 1;
									}
									
								}
							}
								
							if (strncmp(buffer, arrEndTag, strlen(arrEndTag))==0){	
								break;
							}
						}
						break;
					}
				}
				
				if (nReturn!=0){
					//printf("ifx_GetCfgData: pFileName=%s [%s%s]\n", pFileName, arrDataTag, pRetValue);
				}
				else{
					//printf("ifx_GetCfgData: pFileName=%s [%s] not found\n", pFileName, arrDataTag);
				}
            }
            else
            {
            		//printf("ifx_GetCfgData fileopen error[%s]\n", pFileName);
                    nReturn = 0;
            }
        }
        else
        {
                memset(sValue, 0x00, 1024);
                nLine = atoi(pData);
                if ((fd = popen(pFileName, "r")) == NULL)
                {
                        nReturn = 0;
                        goto exit;
                }
                //pclose(fd);
                //return 0;
                
                while ( fgets(sValue, 1024, fd) )
                {
                	
                        if (nIndex == nLine)
                        {
                                sValue[strlen(sValue)-1] = 0; // remove "\n" and keep string only
                                if (pRetValue){
                                	strcpy(pRetValue, sValue);
                                }
                                nReturn = 1;
                                //pclose(fd);
                                goto exit;
                        }
                        nIndex++;
                }
                
                //pclose(fd);
                nReturn = 0;
        }

exit:
		if(pTag)
        {
			 if (fd)
             {
             	fclose(fd);
             }
        }
        else
        {
             if (fd)
             {
             	pclose(fd);
             }
        }
        
		if (pRetValue && *pRetValue){
        	//printf("pFileName=%s pTag=%s pData=%s pRetValue=%s nReturn=%d\n", pFileName, pTag, pData, pRetValue, nReturn);
        }
        else{
        	//printf("pFileName=%s pTag=%s pData=%s pRetValue= nReturn=%d\n", pFileName, pTag, pData, nReturn);
        }
        
        return nReturn;
}

//////////////////////////////////////////////////////////////////////////////////////////
// IPC Command
//////////////////////////////////////////////////////////////////////////////////////////
int write_ipc_parameter(int cmd, char *body, int size, char **write_buf)
{
	char cmd_b[5];
	char len_b[5];
	char *tbuff = 0;
	
	memset(cmd_b,0,5);
	memset(len_b,0,5);
	if(size > MAX_IPC_BODY_LEN){
		return 0;
	}
	tbuff = (char *) malloc((size + 8)*sizeof(char));
	if (tbuff==0){
		return 0;
	}
	memset(tbuff , 0, (size + 8 ) * sizeof(char));

	memcpy(tbuff,&cmd, sizeof(cmd));
	memcpy(tbuff+sizeof(cmd), &size, sizeof(size));

	if(body != 0 || size !=0)
	{
		memcpy(tbuff + 8, body, size);
	}

	*write_buf = tbuff;
	
	return (size * sizeof(char) + 8 *sizeof(char) );
}

int Send_IPC_to_SIP(int cmd, char *body, int size)
{
	char *t_write_buf = NULL;
	int packet_size =0;
	
	if (fp_write == 0){
		if((fp_write = open("/mnt/ramdisk/fifo_sip", O_WRONLY)) < 0)
		{
			return 0;
		}
	}
	packet_size = write_ipc_parameter(cmd, body, size, &t_write_buf);

	if (t_write_buf == 0 || packet_size==0){
		return 0;
	}
	else
	{
		write(fp_write, t_write_buf, packet_size);
		memset(t_write_buf, 0 , packet_size);
		free(t_write_buf);
		t_write_buf = 0;
		packet_size =0;
	}
	
	close(fp_write);
	fp_write = 0;
	
	return 1;
}

int Send_IPC_to_CWMP(int cmd, char *body, int size)
{
	char *t_write_buf = NULL;
	int packet_size =0;
	
	if (fp_write == 0){
		if((fp_write = open("/mnt/ramdisk/fifo_cwmp", O_WRONLY)) < 0)
		{
			return 0;
		}
	}
	packet_size = write_ipc_parameter(cmd, body, size, &t_write_buf);

	if (t_write_buf == 0 || packet_size==0){
		return 0;
	}
	else
	{
		write(fp_write, t_write_buf, packet_size);
		memset(t_write_buf, 0 , packet_size);
		free(t_write_buf);
		t_write_buf = 0;
		packet_size =0;
	}
	
	close(fp_write);
	fp_write = 0;
	
	return 1;
}


