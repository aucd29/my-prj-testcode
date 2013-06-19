<?php
/***************************************************************************
 *
 *		Date			: 2004-07-27
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			: Fucntion
 *
 *		내용 출력 란
 *
 ***************************************************************************/

function TextOut(&$text,$type=null)
{
	if(!$type)
	{
		$text = eregi_replace("http://([a-z0-9\_\+\.\/\~\@\?\=\&amp;\%\-]+)", " <A HREF=\"http://\\1\" TARGET=_blank>http://\\1</a> ", strip_tags($text));

		echo nl2br($text);
	}
	else if($type=='HTML')
	{
		echo nl2br(strip_tags($text,"<a><img><b><i><u><table><tr><td><font>"));
	}
}
?>