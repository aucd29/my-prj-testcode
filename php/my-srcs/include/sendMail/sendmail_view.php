<?php
/***************************************************************************
 *
 *		Date			: 2004-11-08
 *		Copyright		: Kurome
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
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
require $_SELF[SPR]."/admin_auth.php";

if(!$_GET[no]) msg('�߸��� �����Դϴ�.', null);
$sTableColor ='#007DBD';
include "./data/$no.php";
?>
<html>
<head>
<title>���� �б�</title>
<meta name='author' content='interkorea'>
<meta name='keywords' content=''>
<meta http-equiv='Cache-Control' content='no-cache'>
<meta http-equiv='Pragma' content='no-cache'>
<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>
<link rel='stylesheet' href='/include/css.css' type='text/css'>
<script language='javascript' src='/include/main.js'></script>
</head>
<body>
	<table width='95%' cellpadding='3' cellspacing='1' border='0' align='center'>
	<tr>
		<td  bgcolor='<?=$sTableColor?>' colspan='2'><font color='#ffffff'>������ ���</font></td>
	</tr>
	<tr bgcolor='#f7f7f7'>
		<td align='center'>�̸�</td>
		<td><?=$sFromName?></td>
	</tr>
	<tr bgcolor='#f7f7f7'>
		<td align='center'>�̸���</td>
		<td><?=$sFromMail?></td>
	</tr>
	</table>
	<br>

	<table width='95%' cellpadding='3' cellspacing='1' border='0' align='center'>
	<tr>
		<td bgcolor='<?=$sTableColor?>' colspan='2'><font color='#ffffff'>�޴� ���</font></td>
	</tr>
	<tr bgcolor='#f7f7f7'>
		<td width='78' align='center'>�̸�</td>
		<td><?=$sToName?></td>
	</tr>
	<tr bgcolor='#f7f7f7'>
		<td align='center'>�̸���</td>
		<td><?=$sToMail?></td>
	</tr>
	<tr bgcolor='#f7f7f7'>
		<td align='center'>����</td>
		<td><?=$sTitle?></td>
	</tr>
	<tr bgcolor='#f7f7f7'>
		<td align='center'>����</td>
		<td><textarea name='sContent' rows='8' style='width:95%' class='input' /><?=$sContent?></textarea>
		</td>
	</tr>
	<?php
	// ÷������ --------------------------------------------------------------------
	if($sAttachFile)
	{
		echo "
		<tr bgcolor='#f7f7f7'>
			<td align='center'>÷������</td>
			<td>Y</td>
		</tr>";
	}
	?>
	<tr>
		<td colspan='2' align='center'>
			<input type='button' name='back' value=' �� �� ' class='inputbtn' onClick='history.back()' />
		</td>
	</tr>
	</table>

</body>
</html>