<?
include_once $_SERVER[DOCUMENT_ROOT]."/config/member_db.php";
$dbconn = mysql_connect($sHost,$sUser,$sPasswd) or die("DB�� ������ �� �����ϴ�. �����ڿ��� ���� �Ͻʽÿ�");
mysql_select_db($sDB,$dbconn);
?>
