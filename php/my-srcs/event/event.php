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
if(!$_GET[no]) msg('�̻���� �����Ͽ���', null);

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
	// �̹����϶�
	case ( 1 ):
		switch($nClicked)
		{
			// �ݱ�
			case ( 1 ):
				echo "<img src='/UploadFiles/popup/$upload' border='0' onclick='self.close()' style='cursor:hand' />";
			break;

			// ����â�����̵�
			case ( 2 ):
				echo "<img src='/UploadFiles/popup/$upload' border='0' onclick=\"self.location.href='$nClickedTxt'\"  style='cursor:hand' />";
			break;

			// �θ�â�����̵�
			case ( 3 ):
				echo "<a href='$nClickedTxt' target='main'><img src='/UploadFiles/popup/$upload' border='0' onClick='self.close()' style='cursor:hand' /></a>";
			break;

			default:
				echo'�ش�����̾����ϴ�.';
			break;
		}
	break;

	// html�϶�
	case( 2 ):
		$html = stripslashes( $html );
		echo "$html";
	break;

	default:
		echo'�ش�����̾����ϴ�.';
	break;
}
?>
<form method='post' action='popup_process.php?psMode=COOKIE' style='border:0;margin:0'>
<table cellspacing='0' cellpadding='0' border='0'  width='100%'>
<tr>
	<td bgcolor='#ececec' align='center'>
	�Ϸ絿�� ��â�� ���� ����.
	<input type='checkbox' name='nMark' value='<?=$nMark?>' onClick='submit();'>
	</td>
</tr>
</table>
</form>