
#include <net/if.h>
#include <sys/ioctl.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <string.h>
#include <byteswap.h>
#include <ctype.h>
#include<stdio.h> 
#include "external.h"
// ---- Public Variables -------------------------------------


// ---- Private Constants and Types --------------------------

#define dtoh32(val) (swap_ioctl? bswap_32(val): (uint32)val)
#define htod32(val) (swap_ioctl? bswap_32(val): (uint32)val)
#define NB_ENABLE 0   
#define NB_DISABLE 1 

const int LOCK_EX = 2;
const int LOCK_UN = 8;

// ---- Private Variables ------------------------------------

char ifname[] = "eth0";
int skfd = -1;
int swap_ioctl = 0;

int getipaddr( UINT32 *ipaddr)
{
   struct ifreq ifr[4];
   struct ifconf ifc;
   unsigned int i;
   struct sockaddr_in *sa;
   int rc = -1;

   do
   {
      if (skfd < 0)
      {
         skfd = socket( AF_INET, SOCK_DGRAM, 0);
      }
      ifc.ifc_len = sizeof(ifr);
      ifc.ifc_req = &ifr[0];
      if (ioctl( skfd, SIOCGIFCONF, &ifc) < 0)
         break;
      for (i = 0; i < ifc.ifc_len/sizeof(struct ifreq); i++)
      {
         if (strcmp( ifr[i].ifr_name, ifname) == 0)
         {
            // Found our interface
            sa = (struct sockaddr_in *)&ifr[i].ifr_addr;
            *ipaddr = ntohl(sa->sin_addr.s_addr);
            rc = 0;
            break;
         }
      }
      if (rc < 0)
         break;
      if (ioctl( skfd, SIOCGIFFLAGS, &ifr[i]) < 0)
         break;
      rc =  (ifr[i].ifr_flags & IFF_UP) ? 1 : 0;
   } while (0);
   return rc;
}


//------------------------------------------------------------
// getethaddr -- return binary value of ethernet address
//
// returns: *ethaddr - 6-byte receive ethernet address
//      -1 = error
//      0 = interface currently "down"
//      1 = interface "up"
//

int getethaddr( char ethaddr[6])
{
   struct ifreq ifr;
   int rc = -1;

   do
   {
      if (skfd < 0)
      {
         skfd = socket( AF_INET, SOCK_DGRAM, 0);
      }
      strcpy( ifr.ifr_name, ifname);
      if (ioctl( skfd, SIOCGIFHWADDR, &ifr) < 0)
         break;
      memcpy( ethaddr, &ifr.ifr_hwaddr.sa_data, IFHWADDRLEN);
      if (ioctl( skfd, SIOCGIFFLAGS, &ifr) < 0)
         break;
      rc =  (ifr.ifr_flags & IFF_UP) ? 1 : 0;
   } while (0);
   return rc;
}


