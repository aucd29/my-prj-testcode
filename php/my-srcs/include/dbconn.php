<?
include_once $_SERVER[DOCUMENT_ROOT]."/config/member_db.php";
include_once $_SERVER[DOCUMENT_ROOT]."/include/class/class.Mysql.php";

# 호스트 & 사용자 & 비밀번호 & 데이터베이스

$objDB = & new Mysql($sHost, $sUser, $sPasswd);
$objDB->connect();
$objDB->selectDB($sDB);

# DB 접속 종료
function disconnect()
{
    $GLOBALS[objDB]->close();
}

register_shutdown_function('disconnect');
?>