<?php
/***************************************************************************
 *
 *		Date			: 2004-11-08
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
$sPage = 'sendmail';
require '../../global.php';

//
// Function
//
require $_SELF[FUN].'/common/common.php';

//
// Class
//
require $_SELF[CLS].'/class.Filecontrol.php';

switch($_GET[psMode])
{
	case ( 'WRITE' ):

		include $_SELF[CLS].'/class.sendMail.php';
		$sFrom		= $sFromMail;
		$sTo		= $sToMail;
		$sContent	= $sContent;

		// 파일의 존재유무 확인
		$sAttachFile = ($_FILES[upload][name])?$_FILES[upload]:null;

		// 메일보내기 확인 메일을 파일로 저장을 시킨다.
		if($objMail->mail($sFrom, $sFromName, $sTo, $sTitle, $sContent, $sAttachFile))
		{
			$objFctl->insert("./data", $_POST);
			$sURL = ($_POST[sURL])?$_POST[sURL]:'close';
			msg('메일을 성공적으로 전송하였습니다.',$sURL);
		}
		else msg('메일보내기가실패했습니다.',null);

	break;

	// REMOVE ----------------------------------------------------------------------------------

	case ( 'REMOVE' ):
	
		if(!$_GET[no]) msg($sPage.' remove error');
		if($objFctl->delete($_GET[no], $_GET[sTable])) go("./{$sPage}_list.php");
		else msg('delete error');

	default:
		$objCommon->go( '/' );
	break;
}
?>