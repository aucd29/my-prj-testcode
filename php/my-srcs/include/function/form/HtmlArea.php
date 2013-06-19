<?php
/***************************************************************************
 *
 *		Date			: 2004-07-28
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			:
 *
 *		HTMLAREA
 *
 ***************************************************************************/

function HtmlArea($sHTMLAREA,$nHeight,$sContent)
{
	global $_SELF;

	$_SELF[INC]=$_SELF[INC]?$_SELF[INC]:$_SERVER[DOCUMENT_ROOT].'/include';
	include $_SELF[INC]."/htmlarea/GetFile.html";
}
?>