<?
$dbconn = mysql_connect("localhost","clefunion","clefunion") or die("�����ͺ��̽� ���ῡ �����Ͽ����ϴ�.");

$status = mysql_select_db("clefunion");
if (!$status) {
   error("DB_ERROR");
   exit;
}
?>