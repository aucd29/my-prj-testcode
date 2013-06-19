<?
include_once $_SERVER[DOCUMENT_ROOT]."/config/member_db.php";
$dbconn = mysql_connect($sHost,$sUser,$sPasswd) or die("DB에 연결할 수 없습니다. 관리자에게 문의 하십시요");
mysql_select_db($sDB,$dbconn);
?>
