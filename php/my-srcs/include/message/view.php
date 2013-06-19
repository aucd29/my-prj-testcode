<?php
$sPage = 'message';

include "../../global.php";
include $_SELF[INC].'/dbconn.php';
include $_SELF[CLS].'/class.Common.php';

if(!$_COOKIE[member_id]) $objCommon->msg('로그인 후 이용하세요');
if(!$_GET[no]) $objCommon->msg($sPage.'_view no error');

$sWhere = "no = $_GET[no]";
$sField = '*';
$sTable = $sPage;

if(is_file("./data/$_COOKIE[member_id]"))
{
	@unlink("./data/$_COOKIE[member_id]");
}

$rs = $objDB->slt($sTable, $sField, $sWhere);
?>
<html>
<head>
<title>메신저</title>
<meta name='author' content='interkorea'>
<meta name='keywords' content=''>
<meta http-equiv='Cache-Control' content='no-cache'>
<meta http-equiv='Pragma' content='no-cache'>
<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>
<link rel='stylesheet' href='/include/css.css' type='text/css'>
<script language='javascript' src='/include/main.js'></script>
</head>
<body>

	<?php include "./menu.php"; ?>

	<table cellspacing='0' align='center' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6'>
	<tr>
		<td class='tableTR' width='20%'>보낸이</td>
		<td><?php echo $rs[sFrom];?></td>
	</tr>
	<tr>
		<td class='tableTR'>제목</td>
		<td><?php echo $rs[sTitle];?></td>
	</tr>
	<tr>
		<td class='tableTR'>내용</td>
		<td valign='top' height='230' style='word-break:break-all;'>
			<?php echo nl2br(stripslashes($rs[sContent]));?>
		</td>
	</tr>
	</table>

	<div align='center'>
	<br />
		
		<?php 
		if(!$_GET[type])
		{
			echo "
			<input type='button' name='btn' value=' 답장 하기 ' onClick=\"location.href='write.php?nPos=1&no=$_GET[no]&mode=reply'\" class='inputbtn' />";
		}
		?>

		<input type='button' name='back' value=' 뒤 로 ' class='inputbtn' onClick='history.back()' />

		<input type='button' name='btn' value=' 삭 제 ' onClick="self.location.href='./MessageProcess.php?psMode=REMOVE&no=<?php echo $_GET[no];?>&nPos=<?php echo $nPos;?>&type=<?php echo $type;?>&nPage=<?php echo $nPage;?>'" class='inputbtn' />
	</div>

</body>
</html>
