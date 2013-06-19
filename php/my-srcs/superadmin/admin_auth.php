<?php
/*
include $_SELF[DOC]."/include/function/logAutoDel.php";

if(!$_COOKIE[member_sid] && $_COOKIE[member_level] != "admin")
{
	$url = $_SERVER[PHP_SELF];
	echo ("<meta http-equiv='Refresh' content='0; URL=/superadmin/login/login.html?url=$url'>");
	exit;
}

$year		= date("Y");
$month		= date("m");
$day		= date("j");

$new		= $year-$month-$month+$day;
$newday		= $year-$month-$day;
$admin_code	= "$new$month$newday";

$filename   = $_SELF[DOC]."/config/login/$member_id-$admin_code.cgi";
if ( !file_exists( $filename ) )
{
	echo "
	<script>
	window.alert('\\n\\n관리자만 입장이 가능합니다.\\n\\n만일 관리자면 로그인 해주십시요.\\n\\n');
	self.close();
	</script>";
	exit;
}
*/
?>