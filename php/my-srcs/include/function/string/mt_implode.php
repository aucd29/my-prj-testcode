<?php
// +----------------------------------------------------------------------+
// | 2004-06-03 | �����ִ°͸� implode ��Ű�� type���� index�� implode�Ѵ�.|
// +----------------------------------------------------------------------+
// | Authors: Kurome <aucd29@kornet.net>                                  |
// +----------------------------------------------------------------------+
function mt_implode($arg,&$array,$type=null)
{
	if($type) {
		foreach($array as $index => $value) if($index) $rtn .= $index.$arg;
	}
	else {
		foreach($array as $value) if($value) $rtn .= $value.$arg;
	}

	$argLen = strlen($arg);
	return substr($rtn,0,-$argLen);
}
?>