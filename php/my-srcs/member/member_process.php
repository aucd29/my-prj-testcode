<?php
/***************************************************************************
 *
 *		Date			: 2004-11-03
 *		Copyright		: Kurome
 *		E-mail			: aucd29@daum.net
 *		Type			:
 *
 *
 *
 ***************************************************************************/

//
// Default Setting
//
$sTable = 'member';
require '../global.php';

//
// Function
//
require $_SELF[FUN].'/common/common.php';
require $_SELF[FUN].'/sql/query_engin.php';
require $_SELF[INC].'/dbconn.php3';

//
// Userdata
//
require "../config/member_config.php";

//
// Class
//
$sUploadRoot = $_SELF[UPD].'/member';
require $_SELF[CLS].'/class.Upload3.php';
foreach($_POST as $ix => $v) $_POST[$ix] = strip_tags($v, "<font><div><span><table><tr><td><b><meta><p><tbody><br><img>");

switch( $psMode )
{
	case ( 'WRITE' ):

		$rs = sql_fetch("SELECT COUNT(*) FROM member WHERE nJumin='$_POST[nJumin]' AND nJumin1='$_POST[nJumin1]'",MYSQL_NUM);
		if($rs[0]) msg('이미 등록된 주민등록번호입니다.', null);

		$rs = sql_fetch("SELECT COUNT(*) FROM member WHERE sID='$_POST[sID]'",MYSQL_NUM);
		if($rs[0]) msg('이미 등록된 아이디 입니다.', null);
		$_POST[sUpload] = $objUpload->Upload($_FILES[sUpload]);

		if($_POST[sPasswd]!=$_POST[sRepasswd]) msg('비밀번호가 일치하지 않습니다.', null); // close, none

		$_POST[sUserLevel] = $join;
		$_POST[wdate]  = time();
		$_POST[nBirthDay] = $_POST[nBirthY].$_POST[nBirthM].$_POST[nBirthD];

		unset($_POST[sRepasswd]);
		unset($_POST[nBirthY]);
		unset($_POST[nBirthM]);
		unset($_POST[nBirthD]);
		unset($_POST[nRequiredIDCheck]);

		query_engin($sTable, $_POST);
		msg('회원가입이 완료 되었습니다.', '/');

	break;

	// 로그인 -------------------------------------------------------------------------------------

	case( 'LOGIN' ):

		if(!$_POST[sID] || !$_POST[sPasswd]) msg('아이디나 패스워드가 없습니다.', null);
		$rs = sql_fetch("SELECT * FROM member WHERE sID='$_POST[sID]' AND sPasswd='$_POST[sPasswd]'");
		if($rs[no])
		{
			if(!$rs[sUserLevel]) msg('현재 회원님은 인증전입니다. 관리자가 인증후에 사용가능합니다.', null);
			SetCookie("member_id",		 $rs[sID]			,0,"/");
			SetCookie("member_name",	 $rs[sName]			,0,"/");
			SetCookie("member_level",	 $rs[sUserLevel]	,0,"/");
			if($rs[sUpload]) SetCookie('member_image', $rs[sUpload],0,"/");

			//
			// Administrator
			//
			if($rs[sUserLevel] == 'admin')
			{
				$sAdminCode = md5(date(Ymd));
				$adminDir	= $_SELF[CFG]."/login";

				if(!is_dir($adminDir))
				{
					@chmod($_SELF[CFG], 0777);
					@mkdir($adminDir, 0777);
				}
				SetCookie("member_admin",$sAdminCode,0,"/");

				$fp = fopen($adminDir."/$_POST[sID]-$admin_code.cgi", "w");
				fwrite($fp, "$_POST[sID]|$_SERVER[REMOTE_ADDR]");
				fclose($fp);
			}

			if($_POST[sURL])
			{
				if($_POST[sURL] == 'close')
				{
					msg('로그인되었습니다.', 'none');
					closed(1);
				}
				else msg('로그인 되었습니다.', $_POST[sURL]);
			}
			else go("/");
		}
		else
		{
			$rs = sql_query("SELECT COUNT(*) FROM member WHERE sID='$_POST[sID]'",MYSQL_NUM);
			if($rs[0])	msg('아이디가 존재하지 않습니다.\\n 회원가입 해주세요', null);
			else		msg('패스워드가 일치하지않습니다.',null);
		}

	break;

	// 로그아웃 --------------------------------------------------------------------------------

	case( 'LOGOUT' ):

		//
		// 로그인햇을때의 파일 삭제 (관리자꺼)
		//
		if(is_file($_SELF[CFG]."/login/$_COOKIE[member_id]-$_COOKIE[member_admin].cgi"))
			@unlink($_SELF[CFG]."/login/$_COOKIE[member_id]-$_COOKIE[member_admin].cgi");

		SetCookie("member_id",		 ''	,0,"/");
		SetCookie("member_name",	 ''	,0,"/");
		SetCookie("member_level",	 ''	,0,"/");

		//
		// 관리자 쿠키 삭제
		//
		if($_COOKIE[member_admin])
		{
			SetCookie('member_admin',  '',0,"/");
			if(eregi('superadmin',$_SERVER[HTTP_REFERER]))
				msg('로그아웃햇습니다.','close');
			else
				msg('로그아웃햇습니다.', '/');
		}
		else msg('로그아웃햇습니다.','/');

	break;

	// 회원수정 ----------------------------------------------------------------------------------

	case ( 'MODIFY' ):

		if(!$_POST[no]) msg('error');

		# 새 비밀번호 적용
		if($_POST[set_pw])
		{
			if($_POST[sPasswd] != $_POST[sRepasswd])
				msg('변경할 비밀번호가 일치하지 않습니다.',null);
		}
		else
		{
			unset($_POST[sPasswd]);
		}
		$_POST[sUpload] = $objUpload->Upload($_FILES[sUpload],$_POST[sOldFile]);
		$_POST[nBirthDay] = $_POST[nBirthY].$_POST[nBirthM].$_POST[nBirthD];

		$sWhere = "no = $_POST[no]";
		unset($_POST[set_pw]);
		unset($_POST[sRepasswd]);
		unset($_POST[sOldFile]);
		unset($_POST[no]);
		unset($_POST[nBirthY]);
		unset($_POST[nBirthM]);
		unset($_POST[nBirthD]);
		unset($_POST[nRequiredIDCheck]);

		query_engin('member', $_POST,1,$sWhere);
		if(eregi('superadmin', $_SERVER[HTTP_REFERER]))
			go("/superadmin/member/member_list.php?nPage=$_REQUEST[nPage]&nTotal=$_REQUEST[nTotal]");
		else
			msg('수정되었습니다. 로그아웃됩니다.', $_SERVER[PHP_SELF].'?psMode=LOGOUT');

	break;

	// 회원탈퇴 --------------------------------------------------------------------------------

	case( 'MEMBEROUT' ):

	break;

	// REMOVE ----------------------------------------------------------------------------------

	case ( 'REMOVE' ):

		if(!$_GET[no]) msg($sPage.' remove error');
		$sWhere = "no = $_POST[no]";

		if($_GET[sUpload])
		{
			$sRemoveFile = $uploadRoot."/$_GET[upload]";
			if(is_file($sRemoveFile)) unlink($sRemoveFile);
		}
		sql_query("DELETE FROM member WHERE no=$_GET[no]");
		go('/superadmin/member/'.$sPage."_list.php?nPage=$nPage");

	break;

	default:
		go( '/' );
	break;
}
?>