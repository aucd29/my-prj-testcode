<?php
include '../../global.php';

// ------------------------------------------------------------------------
// ��������
// ------------------------------------------------------------------------
$sPage = 'message';

// ------------------------------------------------------------------------
// function
// ------------------------------------------------------------------------
include $_SELF[FUN].'/common/common.php';
if(!$_COOKIE[member_id]) msg('�α��� �� �̿��ϼ���');

if($_GET[mode] == 'reply')
{
	require $_SELF[DOC].'/include/dbconn.php3';
	include $_SELF[FUN].'/sql/query_engin.php';

	if(!$_GET[no]) msg($sPage.'modify no error');

	$sWhere = "no = $_GET[no]";
	$sTable = $sPage;

	$rs = sql_fetch("SELECT * FROM message WHERE no=$_GET[no]");
}
else
{
	$rs[sTo] = $sTo ? $sTo : '';
}
?>
<html>
<head>
<title>����</title>
<meta name='author' content='interkorea'>
<meta name='keywords' content=''>
<meta http-equiv='Cache-Control' content='no-cache'>
<meta http-equiv='Pragma' content='no-cache'>
<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>
<link rel='stylesheet' href='../css.css' type='text/css'>
<script language='javascript' src='../main.js'></script>
</head>
<body>

<?php include "./menu.php"; ?>

<form method='post' action='process.php?psMode=WRITE&nPage=<?php echo $nPage;?>&nTotal=<?php echo $nTotal;?>' name='frm_write' enctype='multipart/form-data' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>

	<table cellspacing='0' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6' align='center'>
<?php
if($mode == 'modify')
{
	echo "
	<input type='hidden' name='no' value='$_GET[no]' />";
	if($rs[sUpload]) echo "<input type='hidden' name='upOneBon' value='$rs[sUpload]' />";
}
?>
	<tr>
		<td class='tableTR' width='20%'>�޴���</td>
		<td>
			<input type='text' name='sTo' class='input' value='<?php echo $rs[sFrom];?>' hname='�̸�' required='required' style='width:60%' READONLY />

			<input type='button' name='btn' value=' ģ������ ' onClick="winOpens('./find_id.php', 'find_id', '500', '400')" class='inputbtn' />
		</td>
	</tr>
	<tr>
		<td class='tableTR' width='20%'>����</td>
		<td><input type='text' name='sTitle' class='input' style='width:80%' value='<?php if($_GET[mode]=='reply') echo 'RE:'.$rs[sTitle];?>' /></td>
	</tr>
	<tr>
		<td class='tableTR'>����</td>
		<td><textarea name='sContent' style='width:100%;height:200px' class='textarea'><?php echo $rs[sContent];?></textarea></td>
	</tr>
	</table>
	<div align='center' width='95%' style='font-size:9pt;padding-top:5;color:#FF6600;padding-top:5'>

	<input type='submit' value='  �� ��  ' class='inputbtn' />
	<input type='button' value='  �� ��  ' class='inputbtn' onClick='history.back();' />

			<br /><br>
	�� �������� �����ÿ��� ',' �޸��� �̿����ּ���</div>

</form>
</body>
</html>