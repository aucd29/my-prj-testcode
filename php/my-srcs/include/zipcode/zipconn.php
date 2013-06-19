<?
$dbconn = mysql_connect("localhost","clefunion","clefunion") or die("데이터베이스 연결에 실패하였습니다.");

$status = mysql_select_db("clefunion");
if (!$status) {
   error("DB_ERROR");
   exit;
}
?>