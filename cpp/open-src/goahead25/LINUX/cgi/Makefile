#
# Makefile for the GoAhead web server reference source base
#  for the Linux OS
#
# Copyright (c) GoAhead Software, Inc. 1995-2010
# Copyright (c) PeerSec Networks, Inc. 2010
#
#

all:	compile 
#
# This is set by the debug target below
#
#ifdef WEBS_DEBUG
DFLAGS  = -g -Wall -Winline -Wdisabled-optimization -DDEBUG
#else
#DFLAGS  = -Os
#endif

debug:
	@$(MAKE) compile "WEBS_DEBUG = 1"

gold:
	@$(MAKE) compile

default: gold

#CROSS_COMPILE=/opt/CnS/arm/gcc-3.4.3-glibc-2.3.3/arm-linux-gnu/bin/arm-linux-gnu-
CC              = $(CROSS_COMPILE)gcc
CXX             = $(CROSS_COMPILE)g++
#CXX             = $(CROSS_COMPILE)gcc
#LD              = $(CROSS_COMPILE)ld
LD              = $(CROSS_COMPILE)gcc
#LD              = $(CROSS_COMPILE)g++
LIB_ARCHIVE     = $(CROSS_COMPILE)ar
NM              = $(CROSS_COMPILE)nm
STRIP           = $(CROSS_COMPILE)strip
OBJCOPY         = $(CROSS_COMPILE)objcopy
RANLIB          = $(CROSS_COMPILE)ranlib
OBJDUMP         = $(CROSS_COMPILE)objdump

# Dependencies
DEPEND_FILES	= webconfig.o enetcfg.o
CFLAGS	= -DWEBS -DOS="LINUX" -DLINUX $(UMSW) $(DASW) $(SSLSW) $(IFMODSW)
OTHERS	= -DB_STATS -DB_FILL 
IFLAGS 	= -I.. 
LDFLAGS	= $(SSLLIB) $(IFMODLIB)

compile: webconfig

webconfig: Makefile 
	$(CC) -o webconfig $(DFLAGS) \
		$(CFLAGS) $(IFLAGS) \
		webconfig.c external.c common_util.c $(LDFLAGS) 

clean:
	rm -f webconfig $(DEPEND_FILES)

#
#	Transition rules (add -o to put object in right directory)
#
.c.o:
	$(CC) -c -o $@ $(DFLAGS) $(CFLAGS) $(IFLAGS) $<
