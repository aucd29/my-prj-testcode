<?php
//
// Create Query�� �������� ���� phpMyAdmin ���� �����ϴ°� ������.
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