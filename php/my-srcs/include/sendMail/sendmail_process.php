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

		// ������ �������� Ȯ��
		$sAttachFile = ($_FILES[upload][name])?$_FILES[upload]:null;

		// ���Ϻ����� Ȯ�� ������ ���Ϸ� ������ ��Ų��.
		if($objMail->mail($sFrom, $sFromName, $sTo, $sTitle, $sContent, $sAttachFile))
		{
			$objFctl->insert("./data", $_POST);
			$sURL = ($_POST[sURL])?$_POST[sURL]:'close';
			msg('������ ���������� �����Ͽ����ϴ�.',$sURL);
		}
		else msg('���Ϻ����Ⱑ�����߽��ϴ�.',null);

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