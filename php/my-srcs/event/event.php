<?php
//
// Default
//
include "../global.php";

//
// Function
//
include $_SELF[FUN].'/common/common.php';

//
// Parameter check
//
if(!$_GET[no]) msg('이상허니 접근하요잉', null);

//
// Data include
//
include "./data/$no.php";
?>
<html>
<head>
<title><?php echo $sTitle; ?></title>
<meta name='author' content='interkorea'>
<meta name='keywords' content=''>
<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>
<link rel='stylesheet' href='/include/css.css' type='text/css'>
</head>
<body style='margin:0;border:0'>
<?php
switch($nType)
{
	// 이미지일때
	case ( 1 ):
		switch($nClicked)
		{
			// 닫기
			case ( 1 ):
				echo "<img src='/UploadFiles/popup/$upload' border='0' onclick='self.close()' style='cursor:hand' />";
			break;

			// 현재창에서이동
			case ( 2 ):
				echo "<img src='/UploadFiles/popup/$upload' border='0' onclick=\"self.location.href='$nClickedTxt'\"  style='cursor:hand' />";
			break;

			// 부모창에서이동
			case ( 3 ):
				echo "<a href='$nClickedTxt' target='main'><img src='/UploadFiles/popup/$upload' border='0' onClick='self.close()' style='cursor:hand' /></a>";
			break;

			default:
				echo'해당사항이없습니다.';
			break;
		}
	break;

	// html일때
	case( 2 ):
		$html = stripslashes( $html );
		echo "$html";
	break;

	default:
		echo'해당사항이없습니다.';
	break;
}
?>
<form method='post' action='popup_process.php?psMode=COOKIE' style='border:0;margin:0'>
<table cellspacing='0' cellpadding='0' border='0'  width='100%'>
<tr>
	<td bgcolor='#ececec' align='center'>
	하루동안 이창을 열지 않음.
	<input type='checkbox' name='nMark' value='<?=$nMark?>' onClick='submit();'>
	</td>
</tr>
</table>
</form>