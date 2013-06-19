<?php
//
// Create Query를 생성하자 쓰기 phpMyAdmin 에서 선택하는게 귀찮다.
//
function CreateQuery($name,&$p,$t='MyISAM')
{
	$rtn = "CREATE TABLE `$name`(
	`no` int(11) NOT NULL auto_increment,";

	foreach($p as $ix => $v) $rtn .= "
	`$ix` varchar() NOT NULL default '',";

	$rtn .= "
	PRIMARY KEY (`no`)
) TYPE=$t;";

	return '<pre>'.$rtn.'</pre>';
}
?>