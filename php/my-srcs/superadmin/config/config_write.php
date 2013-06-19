<?php
/***************************************************************************
 *
 *		Date			: 2004-11-02
 *		Copyright		: Kurome
 *		E-mail			: aucd29@daum.net
 *		Type			:
 *
 *
 *
 ***************************************************************************/

//
// Default Setting
//
require '../../global.php';
include "../admin_auth.php";

//
// Function
//
require $_SELF[FUN].'/common/common.php';
require $_SELF[FUN].'/file/insert.php';
require $_SELF[FUN].'/form/select.php';

//
// ȸ������ ����������
//
include "../../config/member_data.php";

if(is_file('../../config/admin_config.php'))
{
	include "../../config/admin_config.php";
}
else
{
	if(!is_dir('../../config'))
		msg('config ���丮 �����䱸',NULL);	// null-> close �޼����Ĵݱ� �ٸ��� �̵�

	$arrBaseWrite = array(
		'HOME_NAME'  => "�����ڸ���",
		'HOME_EMAIL' => "kurome@damota.com",
		'HOME_URL'   => "http://localhost/",
		'HOME_TITLE' => ":: �����ڸ��� ::",
		'wdate'		 => date(Ymd)
	);
	insert('../../config', &$arrBaseWrite, 'admin_config');
	$objCommon->go($PHP_SELF);
}
?>
<html>
<head>
<title>�����ڼ���</title>
<meta name='author' content='interkorea'>
<meta name='keywords' content=''>
<meta http-equiv='Cache-Control' content='no-cache'>
<meta http-equiv='Pragma' content='no-cache'>
<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>
<link rel='stylesheet' href='/include/css.css' type='text/css'>
<script language='javascript' src='/include/main.js'></script>
</head>
<body>
	<form method='post' action='config_process.php?psMode=WRITE' name='frm_write1' enctype='multipart/form-data' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>

		<table border='0' width='95%'  align='center' cellspacing='0' cellpadding='0'><tr><td height='5' bgcolor='black'></td></tr></table>
		<table cellspacing='0' align='center' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6'>
		<tr>
			<td class='tableTR' width='20%'>Ȩ��������</td>
			<td><input type='text' name='HOME_NAME' class='input' style='width:60%' value='<?=$HOME_NAME?>' hname='Ȩ��������' required='required' /></td>
		</tr>
		<tr>
			<td class='tableTR'>������ �����ּ�</td>
			<td><input type='text' name='HOME_EMAIL' class='input' style='width:60%' value='<?=$HOME_EMAIL?>' hname='�����ڸ����ּ�' option='email' required='required' /></td>
		</tr>
		<tr>
			<td class='tableTR'>�ּ�</td>
			<td><input type='text' name='HOME_URL' class='input' style='width:60%' value='<?=$HOME_URL?>' hname='Ȩ�������ּ�' required='required' option='url' /></td>
		</tr>
		<tr>
			<td class='tableTR'>Ȩ������ Ÿ��Ʋ</td>
			<td><input type='text' name='HOME_TITLE' class='input' style='width:60%' value='<?=$HOME_TITLE?>' hname='Ȩ������Ÿ��Ʋ' required='required' /></td>
		</tr>
		<tr>
			<td class='tableTR'>�Խ��� ��й�ȣ</td>
			<td><input type='text' name='HOME_BOARD' class='input' style='width:60%' value='<?=$HOME_BOARD?>' hname='�Խ��� ��й�ȣ' required='required' /></td>
		</tr>
		<tr>
			<td colspan='2' align='center'>
				<input type='submit'  value='  �� ��  ' class='inputbtn' />
			</td>
		</tr>
		</table>
	</form>

<br />

	<?php
	if(is_file("../../config/member_db.php")) include "../../config/member_db.php";

	// �ѹ� �������Ŀ��� ���������� �׷��� ��ȯ�� ����.
	if(is_file("../../config/root.php"))
	{
	?>

	<form method='post' action='config_process.php?psMode=DBSET' name='frm_write2' enctype='multipart/form-data' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>

		<table border='0' width='95%'  align='center' cellspacing='0' cellpadding='0'><tr><td height='5' bgcolor='black'></td></tr></table>
		<table cellspacing='0' align='center' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6'>
		<tr>
			<td class='tableTR' width='20%'>����</td>
			<td>
				<select name='sServer' class='input'>
					<option value='webmota'>webmota
					<option value='damota'>damota
					<option value='morning'>morning
				</select>

				<script language='javascript'>
				<!--
					document.frm_write2.sServer.value = "<?php echo $sServer?$sServer:'webmota';?>";
				//-->
				</script>
			</td>
		</tr>
		<tr>
			<td class='tableTR' width='20%'>ȣ��Ʈ</td>
			<td><input type='text' name='sHost' class='input' style='width:60%' value='<?=$sHost?>' /></td>
		</tr>
		<tr>
			<td class='tableTR'>����</td>
			<td><input type='text' name='sUser' class='input' style='width:60%' value='<?=$sUser?>' /></td>
		</tr>
		<tr>
			<td class='tableTR'>���</td>
			<td><input type='text' name='sPasswd' class='input' style='width:60%' value='<?=$sPasswd?>' /></td>
		</tr>
		<tr>
			<td class='tableTR'>�����ͺ��̽�</td>
			<td><input type='text' name='sDB' class='input' style='width:60%' value='<?=$sDB?>' /></td>
		</tr>
		<tr>
			<td colspan='2' align='center'>
				<input type='submit'  value='  �� ��  ' class='inputbtn' />
			</td>
		</tr>
		</table>
	</form>

<br />

	<?
	}

	if(is_file("../../config/member_config.php")) include "../../config/member_config.php";
	?>
	<form method='post' action='config_process.php?psMode=MEMCONFIG' name='frm_write3' enctype='multipart/form-data' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>

		<table border='0' width='95%'  align='center' cellspacing='0' cellpadding='0'><tr><td height='5' bgcolor='black'></td></tr></table>
		<table cellspacing='0' align='center' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6'>
		<tr>
			<td colspan='2' class='tableTR'>ȸ������ ���</td>
		</tr>
		<tr>
			<td>ȸ����������</td>
			<td>
				<?
				if(!$join)	$joinChk1 = 'checked';
				else		$joinChk2 = 'checked';
				?>
				<input type='radio' name='join' value='0' class='inputradio' <?=$joinChk1?> />�³��İ���
				<input type='radio' name='join' value='1' class='inputradio' <?=$joinChk2?> />�ٷΰ���
			</td>
		</tr>
		<tr>
			<td colspan='2' align='center'>
				<input type='submit'  value='  �� ��  ' class='inputbtn' />
				<input type='button' name='open' value='  ȸ������������  ' class='inputbtn' onClick="winOpens('../../member/member_write.php', 'ȸ������', '680', '700')" />


				<?php
				include $_SELF[INC].'/dbconn.php3';
				$sql = "SELECT * FROM member";
				$res = @mysql_query($sql);
				if(!$res)
				{
					echo "
					<input type='button' name='btn' value=' ȸ�� ��� �Է� ' onClick=location.href='./member_sql.php' class='inputbtn' />";
				}
				?>


			</td>
		</tr>
		</table>
	</form>

<br />

	<?
	if(is_file("../../config/member_include.php"))	include "../../config/member_include.php";
	?>
	<form method='post' action='config_process.php?psMode=MEMINCLUDE' name='frm_write4' enctype='multipart/form-data' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>

		<table border='0' width='95%'  align='center' cellspacing='0' cellpadding='0'><tr><td height='5' bgcolor='black'></td></tr></table>
		<table cellspacing='0' align='center' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6'>
		<tr>
			<td width='20%' class='tableTR' title='�� ���״¸����� ������ 1���������ߵȴ� ���� ���ϸ��� ����̾���������..'>ȸ������ ���</td>
			<td>
			<?
			$sRoot = '../../html';

			if(is_dir($sRoot))
			{
				$objDir	= dir($sRoot);
				while (false !== ($data	= $objDir->read()))
				{
					if($data != '.' && $data != '..')
					{
						if(is_file("$sRoot/$data"))
						{
							if(eregi('header',$value))	$aHeaderData[] = $value;
							else						$aFooterData[] = $value;
						}
					}
				}
				$objDir->close();
			}
			if($aHeaderData) select('header', $aHeaderData, 'one', $header);
			?>&nbsp;
			</td>
			<td width='20%' class='tableTR'>ȸ������ ǲ��</td>
			<td><? if($aFooterData) select('footer', $aFooterData, 'one', $footer); ?>&nbsp;</td>
			<td width='15'><input type='submit' value='  �� ��  ' class='inputbtn' /></td>
		</tr>
		</table>
	</form>

	<br />

	<?
	if(is_file("../../config/member_article.php")) include "../../config/member_article.php";
	?>
	<form method='post' action='config_process.php?psMode=ARTICLE' name='frm_write5' enctype='multipart/form-data' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>

		<table border='0' width='95%'  align='center' cellspacing='0' cellpadding='0'><tr><td height='5' bgcolor='black'></td></tr></table>
		<table cellspacing='0' align='center' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6'>
		<tr>
			<td class='tableTR'>ȸ�����</td>
		</tr>
		<tr>
			<td><textarea name='article' style='width:100%;height:200px' onKeyup='var m=200;var s=this.scrollHeight;if(s>=m)this.style.pixelHeight=s+4'  class='textarea'><?=$article?></textarea></td>
		</tr>
		<tr>
			<td align='center'><input type='submit' value='  �� ��  ' class='inputbtn' /></td>
		</tr>
		</table>
	</form>

</body>
</html>