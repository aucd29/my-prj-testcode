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

if(!$_GET[no]) msg('잘못된 접근입니다.', null);
$sTableColor ='#007DBD';
include "./data/$no.php";
?>
<html>
<head>
<title>메일 읽기</title>
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
		<td  bgcolor='<?=$sTableColor?>' colspan='2'><font color='#ffffff'>보내는 사람</font></td>
	</tr>
	<tr bgcolor='#f7f7f7'>
		<td align='center'>이름</td>
		<td><?=$sFromName?></td>
	</tr>
	<tr bgcolor='#f7f7f7'>
		<td align='center'>이메일</td>
		<td><?=$sFromMail?></td>
	</tr>
	</table>
	<br>

	<table width='95%' cellpadding='3' cellspacing='1' border='0' align='center'>
	<tr>
		<td bgcolor='<?=$sTableColor?>' colspan='2'><font color='#ffffff'>받는 사람</font></td>
	</tr>
	<tr bgcolor='#f7f7f7'>
		<td width='78' align='center'>이름</td>
		<td><?=$sToName?></td>
	</tr>
	<tr bgcolor='#f7f7f7'>
		<td align='center'>이메일</td>
		<td><?=$sToMail?></td>
	</tr>
	<tr bgcolor='#f7f7f7'>
		<td align='center'>제목</td>
		<td><?=$sTitle?></td>
	</tr>
	<tr bgcolor='#f7f7f7'>
		<td align='center'>내용</td>
		<td><textarea name='sContent' rows='8' style='width:95%' class='input' /><?=$sContent?></textarea>
		</td>
	</tr>
	<?php
	// 첨부파일 --------------------------------------------------------------------
	if($sAttachFile)
	{
		echo "
		<tr bgcolor='#f7f7f7'>
			<td align='center'>첨부파일</td>
			<td>Y</td>
		</tr>";
	}
	?>
	<tr>
		<td colspan='2' align='center'>
			<input type='button' name='back' value=' 뒤 로 ' class='inputbtn' onClick='history.back()' />
		</td>
	</tr>
	</table>

</body>
</html>