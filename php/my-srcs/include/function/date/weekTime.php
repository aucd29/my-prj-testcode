<?php
// +----------------------------------------------------------------------+
// | 2004-06-01 주 단위 unixtime 반환
// +----------------------------------------------------------------------+
// | Authors: Kurome <aucd29@kornet.net>								  |
// +----------------------------------------------------------------------+


function weekTime()
{
	$nBase	= date(w);
	$nTime	= time();
	$nLTime = 6-$nBase;
	$rtn[first] = $nBase ?strtotime("-$nBase day",$nTime) :$nTime;
	$rtn[last]  = $nLTime?strtotime("+$nLTime day",$nTime):$nTime;
	return $rtn;
}
?>