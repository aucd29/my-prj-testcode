<?php
//
// Default
//
include "../global.php";
include "../superadmin/admin_auth.php";

$sPage = 'popup';

//
// Class
//
include $_SELF[CLS].'/class.Common.php';

//
// Error
//
if(!$_GET[no]) $objCommon->msg($sPage.'modify no error');

//
// Include Data
//
include "./data/$_GET[no].php";
$html = stripslashes($html);
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
</head>
<body>
<table border='0' width='95%'  align='center' cellspacing='0' cellpadding='0'><tr><td height='5' bgcolor='black'></td></tr></table>
<table cellspacing='0' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6' align='center'>
<tr>
	<td class='tableTR' width='20%'>����</td>
	<td><?php echo $sTitle;?></td>
</tr>
<tr>
	<td class='tabletr'>����</td>
	<td><?php echo (!$nType || $nType == 1)?"�̹���":"HTML";?></td>
</tr>
<tr>
	<td class='tableTR'>HTML</td>
	<td><textarea name='html' style='width:100%;height:100px' onKeyup="var m=50;var s=this.scrollHeight;if(s>=m)this.style.pixelHeight=s+4" class='textarea'><?=$html?></textarea></td>
</tr>
<tr>
	<td class='tableTR'>���ʼ��ý�</td>
	<td>
		<table cellspacing='0' cellpadding='0' border='0'>
		<tr>
			<td>
			<?php
			if($nClicked == 1 || !$nClicked)
				$sClickChk = '�ݱ�';
			else if($nClicked == 2)
				$sClickChk = '���ʿ��� ����';
			else
				$sClickChk = 'Ȩ���������� ����';
			echo $sClickChk;
			?>
			</td>
		</tr>
		<tr>
			<td><?=$nClickedTxt?>&nbsp;</td>
		</tr>
		</table>
	</td>
</tr>
<tr>
	<td class='tableTR'>�׸�����</td>
	<td><? $objCommon->prvUpload( $sPage, $nType , $upload ); ?>&nbsp;</td>
</tr>
<tr>
	<td class='tabletr'>âũ��</td>
	<td>
	���� <input type='text' name='nWidth' class='input' style='width:60' value='<?=$nWidth?>' msg='â ����ũ�⸦ �Է����ּ���' required />
	���� <input type='text' name='nHeight' class='input' style='width:60' value='<?=$nHeight?>' msg='â ����ũ�⸦ �Է����ּ���' required />
	</td>
</tr>
<tr>
	<td class='tabletr'>â�̵�</td>
	<td>
	���� <input type='text' name='nHorWinPos' class='input' style='width:60' value='<?=$nHorWinPos?>' />
	���� <input type='text' name='nVerWinPos' class='input' style='width:60' value='<?=$nVerWinPos?>' />
	</td>
</tr>
<tr>
	<td class='tabletr'>��ũ��</td>
	<td><?php $nScrollChk = ( $nScroll ) ? 'checked' : '';?>
	<input type='checkbox' name='nScroll' value='1' class='inputradio' <?=$nScrollChk?> /> ��ũ�� ���� </td>
</tr>
<tr>
	<td class='tabletr'>�̺�Ʈ �Ⱓ</td>
	<td>���� : <?php echo $nStart; ?>���� : <?php echo $nEnd; ?></td>
</tr>

<tr>
	<td colspan='2' align='center'>
	<input type='submit' name='submit' value='  �� ��  ' class='inputbtn' />
	<input type='button' name='back' value='  �� ��  ' class='inputbtn' onClick='history.back();' />
	</td>
</tr>
</table>
</form>
</body>
</html>