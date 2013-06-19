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

//
// Userdata
//
require $_SELF[CFG]."/admin_config.php";

$sPage       = 'sendmail';
$sTblColor   = (!$_GET[sTblColor])   ? '#7DA916'		: $_GET[sTblColor];
$sAttachFile = (!$_GET[sAttachFile]) ? 0				: 1;  // ÷�������� �ִ���üŷ
$sToName     = (!$_GET[sToName])     ? '������'			: $_GET[sToName];
$sToMail     = (!$_GET[sToMail])     ? $HOME_EMAIL		: $_GET[sToMail];
$sSubject    = $_GET[sSubject]       ? $_GET[sSubject]	: '';
?>
<html>
<head>
<title><?=$HOME_NAME?>������</title>
<meta name='author' content='interkorea'>
<meta name='keywords' content=''>
<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>
<link rel='stylesheet' href='/include/css.css' type='text/css'>
<script language='javascript' src='/include/main.js'></script>
</head>
<body>

	<form method=post action='/sendMail/<?=$sPage?>_process.php?psMode=WRITE' enctype='multipart/form-data' style='border:0;margin:0' onsubmit='return SoftCheck(this);' name='frmmail'>
        
        <input type='hidden' name='sURL' value='<?php echo $sURL;?>' />

		<table width='95%' cellpadding='3' cellspacing='1' border='0' align='center'>
		<tr>
			<td bgcolor='<?=$sTblColor?>' colspan='2'><font color='#ffffff'>������ ���</font></td>
		</tr>
		<tr bgcolor='#f7f7f7'>
			<td width='20%' align='center'>�̸�</td>
			<td ><input type='text' name='sFromName' value='<?=$sFromName?>' class='input' hname='�����»�� �̸�' required='required' /></td>
		</tr>
		<tr bgcolor='#f7f7f7'>
			<td align='center'>�̸���</td>
			<td><input type='text' name='sFromMail' value='<?=$sFromMail?>' class='input' hname='�����»�� �̸���' required='required' option='email' style='width:95%' /></td>
		</tr>
		</table>

		<br>

		<table width='95%' cellpadding='3' cellspacing='1' border='0' align='center'>
		<tr>
			<td bgcolor='<?=$sTblColor?>' colspan='2'><font color='#ffffff'>�޴� ���</font></td>
		</tr>
		<tr bgcolor='#f7f7f7'>
			<td width='20%' align='center'>�̸�</td>
			<td><input type='text' readonly name='sToName' class='input' hname='�޴»�� �̸�' required='required' value='<?=$sToName?>' /></td>
		</tr>
		<tr bgcolor='#f7f7f7'>
			<td align='center'>�̸���</td>
			<td><input type='text' readonly name='sToMail' class='input' hname='�޴»�� �̸�����' required='required' value='<?=$sToMail?>' option='email' style='width:95%'  /></td>
		</tr>
		<tr bgcolor='#f7f7f7'>
			<td align='center'>����</td>
			<td><input type='text' name='sTitle' class='input' style='width:95%' hname='����' required='required' /></td>
		</tr>
		<tr bgcolor='#f7f7f7'>
			<td align='center'>����</td>
			<td><textarea name='sContent' rows='8' style='width:95%' hname='����' required='required' class='input' /></textarea>
			</td>
		</tr>
		</table>
		<?php
		// ÷������ --------------------------------------------------------------------
		if($sAttachFile)
		{
			echo "
            <table cellspacing='0' cellpadding='3' border='0'  width='95%' align='center'>
			<tr bgcolor='#f7f7f7'>
				<td align='center' width='20%'>÷������</td>
				<td><input type='file' name='upload' maxlength='255' class='input' style='width:100%' /></td>
			</tr>
			</table>";
		}
		?>

		<table align='center'>
		<tr>
			<td colspan='2' align='center'>
				<input type='submit' value=' ���� ������ ' class='inputbtn'>
				<input type='button' name='close' value=' �� �� ' onclick='window.close();'  class='inputbtn'>
			</td>
		</tr>
		</table>

	</form>

</body>
</html>