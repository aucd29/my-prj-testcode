<?php
// +----------------------------------------------------------------------+
// | 2004-05-27 남은 날을 계산해보자    |
// +----------------------------------------------------------------------+
// | Authors: Kurome <aucd29@kornet.net>                                  |
// +----------------------------------------------------------------------+

$countDateToday = time();
function countDate($date)
{
	global $countDateToday;
	
	$gap = $date - $countDateToday;
	$rtn = strftime("%d", $$gap);
	
	return $rtn;
}
?>