<?php
// +----------------------------------------------------------------------+
// | 2004-05-27 ���� ���� ����غ���    |
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