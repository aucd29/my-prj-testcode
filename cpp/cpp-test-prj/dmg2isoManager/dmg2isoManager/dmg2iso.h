/*
 *	DMG2ISO
 *	dmg2iso.h
 *
 *	Copyright (c) 2004 vu1tur <v@vu1tur.eu.org>
 *	This program is free software; you can redistribute it and/or
 *      modify it under the terms of the GNU General Public License 
 *	as published by the Free Software Foundation.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 * 	You should have received a copy of the GNU General Public License
 *	along with this program; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

#include "zlib.h"

const char plist_begin[]	="<plist version=\"1.0\">";
const char plist_end[]		="</plist>";
const char partlist_begin[]	="<array>";
const char partlist_end[]	="</array>";
const char part_begin[]		="<data>";
const char part_end[]		="</data>";

static unsigned __int32 convert_endian(unsigned __int32 n) {
	return (n >> 24) | ((n & 0x00ff0000) >> 8) |
     	  ((n & 0x0000ff00) << 8) | (n << 24);
}
