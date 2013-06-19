<?php
/***************************************************************************
 *
 *		Date			: 2004-10-01
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			:
 *
 *		�˾� �ڵ�ȭ ���α׷�
 *
 ***************************************************************************/

//
// Default
//
include '../global.php';
include '../superadmin/admin_auth.php'; 
$sPage = 'popup';
	
//
// Function
//
require $_SELF[FUN].'/common/common.php';
require $_SELF[FUN].'/form/sltDate.php';
require $_SELF[FUN].'/prvUpload.php';

if($_GET[mode] == 'modify')
{
	if(!$_GET[no]) msg($sPage.'modify no error');
	include './data/'.$_GET[no].'.php';
	$process = 'MODIFY';
}
else
{
	$process = 'WRITE';
}
?>
<html>
<head>
<title><?=$sPage?>����</title>
<meta name='author' content='interkorea'>
<meta name='keywords' content=''>
<meta http-equiv='Cache-Control' content='no-cache'>
<meta http-equiv='Pragma' content='no-cache'>
<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>
<link rel='stylesheet' href='../include/css.css' type='text/css'>
<script language='javascript' src='../include/main.js'></script>
<script language='javascript' src='./popup.js'></script>
</head>
<body>
<form method=post action='<?=$sPage?>_process.php?psMode=<?=$process?>' name='frm_write' enctype='multipart/form-data' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>
<table border='0' width='95%'  align='center' cellspacing='0' cellpadding='0'><tr><td height='5' bgcolor='black'></td></tr></table>
<table cellspacing='0' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6' align='center'>
<?php
if($_GET[mode] == 'modify')
{
	echo "
	<input type='hidden' name='no' value='$no' />
	<input type='hidden' name='wdate' value='$wdate' />
	<input type='hidden' name='nMark' value='$nMark' />";
	if($upload) echo "<input type='hidden' name='upOneBon' value='$upload' />";
}
?>
<tr>
	<td class='tableTR' width='20%'>����</td>
	<td><input type='text' name='sTitle' class='input' value='<?=stripslashes($sTitle)?>' msg='������ �Է����ּ���' required style='width:60%' /></td>
</tr>
<tr>
	<td class='tabletr'>����</td>
	<td>
		<input id='nType[0]' type='radio' name='nType' value='1' class='inputradio' /> �̹���
		<input id='nType[1]' type='radio' name='nType' value='2' class='inputradio' /> HTML
	</td>
</tr>
<tr>
	<td class='tableTR'>HTML</td>
	<td><textarea id='' name='html' style='width:100%;height:100px' class='textarea'><?=stripslashes($html)?></textarea></td>
</tr>
<script language='javascript'>
var chk = <?php echo $nType?$nType:1; ?>;
if(chk == 1)
{
	document.getElementById('nType[0]').checked = true;
	//document.getElementById('html').disabled = true;
}
else
{
	document.getElementById('nType[1]').checked = true;
	//document.getElementById('html').disabled = false;
}
</script>
<tr>
	<td class='tableTR'>���ʼ��ý�</td>
	<td>
		<table cellspacing='0' cellpadding='0' border='0'>
		<tr>
			<td>
			<input id='nClicked[0]' type='radio' name='nClicked' value='1' class='inputradio' onclick='Clicked(this)' <?=$sClickChk?>/> �ݱ�
			<input id='nClicked[1]' type='radio' name='nClicked' value='2' class='inputradio' onclick='Clicked(this)' <?=$sClickChk1?>/> �˾�â���� ����
			<input id='nClicked[2]' type='radio' name='nClicked' value='3' class='inputradio' onclick='Clicked(this)' <?=$sClickChk2?>/> Ȩ���������� ����

			<font color='deeppink'>�÷����� ��� �۵����� �ʽ��ϴ�.</font>
			</td>
		</tr>
		<tr>
			<td><input id='nClickedTxt' type='text' name='nClickedTxt' value='<?php echo $nClickedTxt;?>' class='input' style='width:60%' <?php echo $disabled;?>/> ��ũ�� �����ּ���</td>
		</tr>
		</table>

		<script language='javascript'>
		var nClicked = <?php echo $nClicked?$nClicked:1; ?>;
		switch(nClicked)
		{
		case (1):
			document.getElementById('nClicked[0]').checked = true;
			document.getElementById('nClickedTxt').disabled = true;
			break;
		case (2):
			document.getElementById('nClicked[1]').checked = true;
			document.getElementById('nClickedTxt').disabled = false;
			break;
		case (3):
			document.getElementById('nClicked[2]').checked = true;
			document.getElementById('nClickedTxt').disabled = false;
			break;
		}
		</script>
	</td>
</tr>
<tr>
	<td class='tableTR'>�̹���</td>
	<td><input type='file' name='upload' maxlength='255' class='input' onChange='prevPopup(this)' style='width:60%' /></td>
</tr>
<tr>
	<td class='tableTR'>�̸�����</td>
	<td><? prvUpload($sPage,1,$upload); ?>&nbsp;</td>
</tr>
<tr>
	<td class='tabletr'>âũ��</td>
	<td>
	���� <input type='text' name='nWidth' class='input' style='width:60' value='<?=$nWidth?>' msg='â ����ũ�⸦ �Է����ּ���' required />
	���� <input type='text' name='nHeight' class='input' style='width:60' value='<?=$nHeight?>' msg='â ����ũ�⸦ �Է����ּ���' required />

	<font color='deeppink'>�÷����� ��� âũ�⸦ ��Ȯ�� �Է����ֽñ� �ٶ��ϴ�.</font>
	</td>
</tr>
<tr>
	<td class='tabletr'>â�̵�</td>
	<td>
	���� <input type='text' name='nHorWinPos' class='input' style='width:60' value='<?=$nHorWinPos?>' />
	���� <input type='text' name='nVerWinPos' class='input' style='width:60' value='<?=$nVerWinPos?>' />
	<?php $nMvApplyChk = $nMvApply?'checked':''; ?>
	<input type='checkbox' name='nMvApply' value='1' class='inputradio' <?=$nMvApplyChk?> /> �����ϱ�
	</td>
</tr>
<tr>
	<td class='tabletr'>��ũ��</td>
	<td>
	<?php $nScrollChk = $nScroll?'checked':'';?>
	<input type='checkbox' name='nScroll' value='1' class='inputradio' <?=$nScrollChk?> /> ��ũ�� ���� </td>
</tr>
<tr>
	<td class='tabletr'>�̺�Ʈ�Ⱓ</td>
	<td>
	<?php
	$nYear = date(Y);
	if($_GET[mode] == 'modify')
	{
	 	$nBfStart = $nStartY.$nStartM.$nStartD;
		$nBfEnd	  = $nEndY.$nEndM.$nEndD;
	}
	else
	{
		// ������ �ƴϸ� �⺻���� 1�ް� �˾�â�� �����Ų��.
		$nBfEnd = date(Ymd,strtotime("+1 Month"));
	}
?>
	<table cellspacing='0' cellpadding='0' border='0'>
	<tr>
		<td width='50'>������</td>
		<td width='300'><?php sltDate('nStart', $nStart);?></td>
		<td width='30'></td>
	</tr>
	<tr>
		<td>������</td>
		<td><?php sltDate('nEnd', $nEnd); ?></td>
		<td>
			<input type='checkbox' name='nApply' value='1' class='inputradio' <?php echo $nApply?'checked':''; ?> /> �����ϱ�
		</td>
	</tr>
	</table>

	</td>
</tr>
<tr>
	<td colspan='2' align='center'>
		<input type='submit' value='  �� ��  ' class='inputbtn' />
		<input type='button' value='  �� ��  ' class='inputbtn' onClick='history.back();' />
	</td>
</tr>
</table>
</form>
</body>
</html>