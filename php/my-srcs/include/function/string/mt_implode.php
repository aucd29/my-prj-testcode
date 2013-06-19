<?php
// +----------------------------------------------------------------------+
// | 2004-06-03 | 값이있는것만 implode 시키고 type으로 index도 implode한다.|
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