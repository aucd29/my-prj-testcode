<?php
/***************************************************************************
 *
 *		Date			: 2004-11-02
 *		Copyright		: Kurome
 *		E-mail			: aucd29@daum.net
 *		Type			:
 *
 *
 *
 ***************************************************************************/

//
// Default
//
include '../../global.php';
include "../admin_auth.php";

//
// Function
//
require $_SELF[FUN].'/common/common.php';
require $_SELF[FUN].'/file/insert.php';

//
// Define
//
$sPage    = 'config';

switch( $psMode )
{
	case ( 'WRITE' ):

		insert($_SELF[CFG], &$_POST, 'admin_config');
		go("./{$sPage}_write.php");

	break;

	// ---------------------------------------------------------------- 회원관련 설정파일

	case( 'MEMCONFIG' ):

		insert($_SELF[CFG], &$_POST, 'member_config');
		go("./{$sPage}_write.php");

	break;

	// ------------------------------------------------------------- 회원페이지 헤더 풋터

	case( 'MEMINCLUDE' ):

		insert($_SELF[CFG], &$_POST, 'member_include');
		go("./{$sPage}_write.php");

	break;

	// -------------------------------------------------------------- 회원약관

	case( 'ARTICLE' ):

		insert($_SELF[CFG], &$_POST, 'member_article');
		go("./{$sPage}_write.php");

	break;

	// -------------------------------------------------------------- 로그인폼 스킨저장파일

	case( 'LOGINFORM' ):

		insert($_SELF[CFG], &$_POST, 'member_loginform');
		go("./{$sPage}_write.php");

	break;

	// 데이터 베이스 설정 저장 ----------------------------------------------------------------

	case( 'DBSET' ):

		insert($_SELF[CFG], &$_POST, 'member_db');

		// 한번만 셋팅하자
		if(is_file("../../config/root.php"))
		{
			include "../../config/root.php";
			$passwd = $$sServer;
			$dbconn = mysql_connect('localhost','root',$passwd) or die(mysql_error());
			mysql_query("CREATE DATABASE `$_POST[sDB]`");
			mysql_select_db("mysql",$dbconn);
			
			// db
			$sql  = "INSERT INTO db(Host,Db,User,Select_priv,Insert_priv,Update_priv,Delete_priv,Create_priv,";
			$sql .= "Drop_priv,Grant_priv,References_priv,Index_priv,Alter_priv)";
			$sql .= " VALUES ('$_POST[sHost]','$_POST[sDB]','$_POST[sUser]','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y')";
			mysql_query($sql) or die(mysql_error());

			// user
			$sql  = "INSERT INTO `user`(Host,User,Password,Select_priv,Insert_priv,Update_priv,Delete_priv,";
			$sql .= "Create_priv,Drop_priv,Reload_priv,Shutdown_priv,Process_priv,File_priv,Grant_priv,";
			$sql .= "References_priv,Index_priv,Alter_priv) VALUES ('$_POST[sHost]','$_POST[sUser]',";
			$sql .= "PASSWORD('$_POST[sPasswd]'),'N','N','N','N','N','N','N','N','N','N','N','N','N','N')";
			mysql_query($sql) or die(mysql_error());
			mysql_query("FLUSH PRIVILEGES")or die(mysql_error());

			unlink("../../config/root.php");
		}
		go("./{$sPage}_write.php");

	break;

	case( 'MEMTYPE' ):

		insert($_SELF[CFG], &$_POST, 'member_type');
		go("./{$sPage}_write.php");

	break;

	default:
		go( '/');
	break;
}
?>