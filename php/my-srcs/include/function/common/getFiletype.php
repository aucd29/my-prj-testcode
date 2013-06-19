<?php
/***************************************************************************
 *
 *		Date			: 2004-06-16
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			: Function
 *		
 *		File Type Viewer
 *
 ***************************************************************************/

function getFiletype($file)
{
	$aPathinfo = pathinfo($file);
	$img = $aPathinfo[extension];

	if(		eregi("txt",$img))				$ftype = "txt.gif";
	elseif( eregi("exe|msi|bat", $img))		$ftype = "exe.gif";
	elseif( eregi("pdf", $img))				$ftype = "pdf.gif";
	elseif( eregi("asf|asx", $img))			$ftype = "asf.gif";
	elseif( eregi("wmv", $img))				$ftype = "wmv.gif";
	elseif( eregi("php|php3", $img))		$ftype = "php3.gif";
	elseif( eregi("css", $img))				$ftype = "css.gif";
	elseif( eregi("asp", $img))				$ftype = "asp.gif";
	elseif( eregi("wav", $img))				$ftype = "wav.gif";
	elseif( eregi("rar", $img))				$ftype = "rar.gif";
	elseif( eregi("mpeg|mpg", $img))		$ftype = "mpeg.gif";
	elseif( eregi("ra|rm", $img))			$ftype = "ra.gif";
	elseif( eregi("mov", $img))				$ftype = "mov.gif";
	elseif( eregi("avi", $img))				$ftype = "avi.gif";
	elseif( eregi("cgi", $img))				$ftype = "cgi.gif";
	elseif( eregi("psd", $img))				$ftype = "psd.gif";
	elseif( eregi("eml", $img))				$ftype = "eml.gif";
	elseif( eregi("ai", $img))				$ftype = "ai.gif";
	elseif( eregi("js", $img))				$ftype = "js.gif";
	elseif( eregi("mid", $img))				$ftype = "mid.gif";
	elseif( eregi("bmp", $img))				$ftype = "bmp.gif";
	elseif( eregi("hlp", $img))				$ftype = "hlp.gif";
	elseif( eregi("gif", $img))				$ftype = "gif.gif";
	elseif( eregi("ppt", $img))				$ftype = "ppt.gif";
	elseif( eregi("doc", $img))				$ftype = "doc.gif";
	elseif( eregi("dll|ini|sys", $img))		$ftype = "dll.gif";
	elseif( eregi("zip|gz|tar|ace",$img))	$ftype = "zip.gif";
	elseif( eregi("jpeg|jpg",$img))			$ftype = "jpg.gif";
	elseif( eregi("swf|fla|swi",$img ))		$ftype = "swf.gif";
	elseif( eregi("pcx",$img ))				$ftype = "pcx.gif";
	elseif( eregi("tif",$img ))				$ftype = "tif.gif";
	elseif( eregi("hwp",$img))			$ftype = "hwp.gif";
	elseif( eregi("xls",$img))			$ftype = "xls.gif";
	elseif( eregi("html|htm|shtml",$img))	$ftype = "html.gif";
	elseif( eregi("reg",$img))				$ftype = "reg.gif";
	else									$ftype = "unknown.gif";
	return $ftype;
}
?>