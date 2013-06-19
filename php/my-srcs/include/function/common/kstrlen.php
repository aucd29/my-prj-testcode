<?php
// +----------------------------------------------------------------------+
// | 2004-05-31 string length( kor & eng )
// +----------------------------------------------------------------------+
// | Authors: Kurome <aucd29@kornet.net>								  |
// +----------------------------------------------------------------------+

function kstrlen($strMessage)
{
	// $strMessage 의 바이트 수를 구한다.
	$nLen = strlen($strMessage);
	
	$nKstrlen = 0;
	for($i=0; $i<$nLen; ++$i)
	{
		// korean 0.5 increment else 1 increment
		if(ord($strMessage[$i])>126)	$nKstrlen += 0.5;
		else							++$nKstrlen;
	}
	return $nKstrlen;
}
?>