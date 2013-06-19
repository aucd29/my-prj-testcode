<?php
include '../global.php';
if($psMode!='COOKIE') include "../superadmin/admin_auth.php";

// class
include $_SELF[CLS].'/class.Common.php';
include $_SELF[CLS]."/class.Filecontrol.php";

$sPage = 'popup';

$uploadRoot = $_SELF[UPD].'/'.$sPage;
if($_FILES[upload][name] || $upOneBon || $psMode == 'DOWNLOAD')
{
	$upType     = 'img';                        // img, file, user
	$upfileName = 'time';                       // name, time
	include $_SELF[CLS].'/class.Upload2.php';
}

switch( $psMode )
{
	case ( 'WRITE' ):

		if($_FILES[upload][name])
		{
			$upload = $objUpload->uploads($_FILES[upload]);
			$_POST[upload] = $upload;
		}

		// 쿠키생성을 위한 절대값
		$_POST[nMark] = time();

		$objFctl->insert( './data', $_POST );
		$objCommon->go("./{$sPage}_list.php");

	break;

	# MODIFY ----------------------------------------------------------------------------------

	case ( 'MODIFY' ):

		if(!$_POST[no]) $objCommon->msg($sPage.' modify error');
		if($_FILES[upload][name] || $_POST[upOneBon])
		{
			$_POST[upload] = $objUpload->uploads($_FILES[upload], $_POST[upOneBon]);

			if($_POST[upOneBon] != $_POST[upload])
			{
				@unlink($uploadRoot."/$_POST[upOneBon]");
				if($thumb) @unlink($uploadRoot.'/thumb/'.$_POST[upOneBon]);
			}
		}

		$objFctl->modify('./data', $_POST, $_POST[no]);
		$objCommon->go("./{$sPage}_list.php");

	break;

	# REMOVE ----------------------------------------------------------------------------------

	case ( 'REMOVE' ):

		if(!$_GET[no]) $objCommon->msg($sPage.' remove error');
		if($_GET[upload])
		{
			$sRemoveFile = $uploadRoot."/$_GET[upload]";
			@unlink($sRemoveFile);
		}

		if($objFctl->delete($_GET[no]));
		$objCommon->go("./{$sPage}_list.php");

	break;

	case( 'COOKIE' ):

		if(!$nMark) $objCommon->msg('popup nMark error');
		// 넘겨오면 쿠키생성 하고 닫는다.
		//SetCookie($nMark, 'Y',time()+86400,"/");

		//
		// 2005-01-15 창마다 쿠키가 생성되는 것은 별로인 것 같아서 수정한다.
		//
		$popup_mark = $_COOKIE[popup_mark]?$_COOKIE[popup_mark].'|'.$nMark:$nMark;
		SetCookie('popup_mark',$popup_mark,0,"/");
		echo "<script>self.close()</script>";

	break;

	default:
		$objCommon->go( '/' );
	break;
}
?>