<?
include_once $_SERVER[DOCUMENT_ROOT]."/config/member_db.php";
include_once $_SERVER[DOCUMENT_ROOT]."/include/class/class.Mysql.php";

# ȣ��Ʈ & ����� & ��й�ȣ & �����ͺ��̽�

$objDB = & new Mysql($sHost, $sUser, $sPasswd);
$objDB->connect();
$objDB->selectDB($sDB);

# DB ���� ����
function disconnect()
{
    $GLOBALS[objDB]->close();
}

register_shutdown_function('disconnect');
?>