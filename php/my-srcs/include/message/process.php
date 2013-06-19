<?php
/***************************************************************************
 *
 *		Date			: 2004-11-10
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
require '../../global.php';

//
// Function
//
require $_SELF[FUN].'/common/common.php';

//
// Class
//
require $_SELF[DOC].'/include/dbconn.php';
require "./include/class.Message.php";

if(!$_COOKIE[member_id]) msg('�α��� �� �̿��ϼ���');
$sTable = $sPage = 'message';

switch($_GET[psMode])
{
	case ( 'WRITE' ):

		$objMsg->setData($_POST);
		$objMsg->msgInsert();
		go("receive.php?type=sFrom&nPos=3");

	break;

	// REMOVE ----------------------------------------------------------------------------------

	case ( 'REMOVE' ):
		
		if(!$_GET[no]) $objCommon->msg($sPage.' remove error');
		$sWhere = "no = $_GET[no]";

		if($objDB->delete($sTable, $sWhere))
			go("receive.php?type=$type&nPage=$_REQUEST[nPage]&nPos=$_REQUEST[nPos]");

	break;

	// ---------------------------------------------------------------------------------------

	case( 'MULTIDEL' ):
		
		if(is_array($_POST[chkBox]))
		{
			foreach($_POST[chkBox] as $value)
			{
				$sWhere = "no = $value";
				$objDB->delete($sTable, $sWhere);
			}
			go($_SERVER[HTTP_REFERER]);
		}
		else msg('���õ� ���� �����ϴ�.', null);

	break;

	// ---------------------------------------------------------------------------------------

	case( 'ADDFRIEND' ):

		if(is_array($_POST[chkBox]))
			if($objMsg->addFriend($_POST[chkBox])) msg($rtn.'���� ��ϵǾ����ϴ�.', null);

		go('./wish.php?nPos=4');

	break;

	// ---------------------------------------------------------------------------------------

	case( 'DELFRIEND' ):

		if(is_array($_POST[chkBox]))
		{
			foreach($_POST[chkBox] as $index => $value) $objDB->delete('message_wish', "sMemberID='$value'");
		}
		msg('ģ���� �����߽��ϴ�.', $sURL);

	break;

	// ------------------------------------------------------------------------------------------

	default:
		go( '/' );
	break;
}
?>