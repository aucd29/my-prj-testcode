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
$sTable = 'member';
require '../global.php';

//
// Function
//
require $_SELF[FUN].'/common/common.php';
require $_SELF[FUN].'/sql/query_engin.php';
require $_SELF[FUN].'/form/select.php';
require $_SELF[FUN].'/form/radio.php';
require $_SELF[FUN].'/prvUpload.php';

//
// Config File
//
require $_SELF[CFG].'/member_data.php';
require $_SELF[CFG].'/admin_config.php';
require $_SELF[CFG].'/member_config.php';
require $_SELF[CFG].'/member_include.php';

//
// Header
//
if(is_file('../html/'.$header))	include_once "../html/$header";

if($_GET[mode]=='modify')
{
	include $_SELF[DOC].'/include/dbconn.php3';
	if(!$_COOKIE[member_id]) msg('�α����� �̿��ϼ���',null);
	$rs = sql_fetch("SELECT * FROM $sTable WHERE sID='$_COOKIE[member_id]'");
	$psMode = 'MODIFY';
}
else
{
	$psMode = 'WRITE';
	echo "
	<table width='100%' align='center' cellpadding=10 cellspacing=1>
	<tr>
		<td>
		���� ������ ���� ������ �� ȸ������ ����� �ۼ��� �ֽñ� �ٶ��ϴ�. <br><br>
		1. �̸� �� �ֹε�Ϲ�ȣ�� �Ǹ�Ȯ�� �� ȸ�������� �ǹǷ� �ݵ�� ������ �����͸� ����ؾ� �մϴ�. <br>
		2. �̿��ڴ��� �ѹ� ���Ͻ� ���̵�� �����Ͻ� �� �����ϴ�. ���� ��� �������ֽñ� �ٶ��ϴ�. <br>
		<font color='deeppink'>�� ���������� ǥ�õ� �κ��� �ʼ� �Է� �����Դϴ�. </font>
		</td>
	</tr>
	</table>";
}
?>
<script language='javascript' src='./include/member.js'></script>
<script language='javascript' src='/include/main.js'></script>
<link rel='stylesheet' href='/include/css.css' type='text/css'>
<form method='post' action='member_process.php?psMode=<?=$psMode?>' name='frm_write' enctype='multipart/form-data' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>
<?php
if($_GET[mode] == 'modify')
{
	echo "<input type='hidden' name='no' value='$rs[no]' /><input type='hidden' name='sOldFile' value='$rs[sUpload]' />";
}
?>
<table width="100%" cellspacing="1" cellpadding="4" border="0" bgcolor='cccccc'>
<tr>
	<td bgColor='f7f7f7' width='120'><font color='deeppink'>��</font> ���̵� (ID)</td>
	<td bgColor='#ffffff'>
	<?php
	if($_GET[mode] == 'modify') echo $rs[sID];
	else
	{
		echo "
		<input type='text' name='sID' size='10' maxlength='8' class='input' minbyte='5' maxbyte='8' hname='���̵�' maxlength='8' required='required' option='engonly' />
		<input type='button' name='findID' value='���̵� �˻�' class='inputbtn' onClick='CheckID(sID)'/>
		<font color='deeppink'>���� �Ǵ� ��/����ȥ���Ͽ� 5-8�� </font>
		<input type='hidden' name='nRequiredIDCheck' errmsg='���̵� �˻縦 ���� �����̽��ϴ�.' required='required' />	
		";
	}
	?>
	</td>
</tr>
<?php
if($_GET[mode] == 'modify')
{
	echo "
	<tr>
		<td bgColor='f7f7f7'><font color='deeppink'>��</font> ���� ��й�ȣ</td>
		<td bgColor='#ffffff'>
			<input type='password' name='sPasswd' size='8' maxlength='8' class='input' minbyte='5' maxbyte='8' hname='��й�ȣ' match='sRepasswd'>
			<font color='deeppink'>���� �Ǵ� ��/����ȥ���Ͽ� 5-8�� </font>
		</td>
	</tr>
	<tr>
		<td bgColor='f7f7f7'><font color='deeppink'>��</font> ��й�ȣ Ȯ��</td>
		<td bgColor='#ffffff'><input type='password' name='sRepasswd' size='8' maxlength='8' minbyte='5' maxbyte='8' class='input'> ��й�ȣ�� �ٽ� �Է��� �ּ���
		</td>
	</tr>
	<tr>
		<td bgColor='f7f7f7'><font color='deeppink'>��</font>���� Ȯ��</td>
		<td bgColor='#ffffff'>
			��й�ȣ�� �����Ͻðڽ��ϱ�?
			<input type='radio' name='set_pw' value='1'>��
			<input type='radio' name='set_pw' value='0' CHECKED>�ƴϿ�
		</td>
	</tr>";
}
else
{
	echo "
	<tr>
		<td bgColor='f7f7f7'><font color='deeppink'>��</font> ��й�ȣ</td>
		<td bgColor='#ffffff'>
			<input type='password' name='sPasswd' size='8' maxlength='8' class='input' minbyte='5' maxbyte='8' hname='��й�ȣ' required='required'  match='sRepasswd'>
			<font color='deeppink'>���� �Ǵ� ��/����ȥ���Ͽ� 5-8�� </font>
		</td>
	</tr>
	<tr>
		<td bgColor='f7f7f7'><font color='deeppink'>��</font> ��й�ȣ Ȯ��</td>
		<td bgColor='#ffffff'><input type='password' name='sRepasswd' size='8' maxlength='8' class='input'> ��й�ȣ�� �ٽ� �Է��� �ּ��� </td>
	</tr>";
}
?>
</table>

<br>

<table width="100%" cellspacing="1" cellpadding="4" border="0" bgcolor='cccccc'>
<tr>
	<td width="120" bgColor='f7f7f7'><font color='deeppink'>��</font> �ѱ� �̸�</td>
	<td bgColor='#ffffff'>
	<?php
	if($_GET[mode] != 'modify')
	{
	?>
		<input type="text" name="sName" size="10" maxlength="12" class='input' hname='�ѱ��̸�' required='required'> �̸��� <font color="deeppink">�Ǹ����� �������</font> �Է��ϼ���<br>��üȸ����Լ��� <font color="deeppink"> ��ǥ�� ����</font>���� �Է��Ͻñ� �ٶ��ϴ�.
	<?php
	}
	else 
	{ 
		echo $rs[sName]; 
	}	
	?>
	</td>
</tr>

<?php
if($_GET[mode] != 'modify')
{
?>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>��</font> �ֹε�Ϲ�ȣ</td>
	<td bgColor='#ffffff'>
		<input maxlength=6 name='nJumin' onKeyup='NextJumin(this)' size=6 class='input' hname='�ֹι�ȣ' required='required'> -
		<input maxlength=7 name='nJumin1' size=7 class='input' onBlur='inputbirth();Jumin(nJumin,nJumin1)' hname='�ֹι�ȣ' required='required' >
		�߸��� �ֹε�Ϲ�ȣ�� ��ϵ��� �ʽ��ϴ�.
	</td>
</tr>
<?php
}
ereg("([0-9]{4})+([0-9]{2})+([0-9]{2})",$rs[nBirthDay],$aDate);
?>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>��</font> �������</td>
	<td bgColor='#ffffff'>
		<input maxlength='4' name='nBirthY' size=4 class='input' hname='�������' required='required' value='<?php echo $aDate[1];?>'> ��&nbsp;&nbsp;
		<select name="nBirthM" size=1 hname='�������' required='required'>
		<?php
		for($i=1; $i<13; ++$i)
		{
			if($i < 10) $i = '0'.$i;
			$slt = $i==$aDate[2]?'SELECTED':'';
			echo"<option value='$i' $slt>$i";
		}
		?>
		</select>��&nbsp;&nbsp;
		<select name="nBirthD" size=1 hname='�������' required='required'>
		<?php
		for($i=1; $i<32; ++$i)
		{
			if($i < 10) $i = '0'.$i;
			$slt = $i==$aDate[2]?'SELECTED':'';
			echo"<option value='$i' $slt>$i";
		}
		?>
		</select>��
		<input type="radio" name="nLunar" value="0" checked>���
		<input type="radio" name="nLunar" value="1">����

		<script language='javascript'>
		var i = '<?php $rs[nLunar]?$rs[nLunar]:0;?>';
		document.frm_write.elements('nLunar['+i+']').checked = true;
		</script>
	</td>
</tr>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>��</font> �� ��</td>
	<td bgColor='#ffffff'>
	<?php
	if($_GET[mode] == 'modify')
	{
		echo $rs[nSex]?'����':'����';
	}
	else
	{
	?>
		<input type="radio" name="nSex" value="0">����
		<input type="radio" name="nSex" value="1">����
	<?php
	}
	?>
	</td>
</tr>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>��</font> �ּ�</td>
	<td bgColor='#ffffff'>
		<input type="text" name="nZip" size="3" maxlength="3" class='input'  hname='�����ȣ' required='required'  value='<?=$rs[nZip]?>' READONLY /> - 
		<input type="text" name="nZip1" value='<?=$rs[nZip1]?>' size="3" maxlength="3" class='input'  hname='�����ȣ' required='required' READONLY /> &nbsp;		
		<input type="button" value="�����ȣ �� �ּ� �ڵ��Է�" onClick="ZipCode('frm_write','nZip','nZip1','sHAddr','sTel')" class='inputbtn'><br>
		<input type="text" name="sHAddr" size="50" class='input' value='<?=$rs[sHAddr]?>' READONLY />
		<input type='text' name='sHAddr2' class='input' style='width:150' value='<?php echo $rs[sHAddr2];?>' />
	</td>
</tr>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>��</font> ��ȭ��ȣ</td>
	<td bgColor='#ffffff'><input type='text' name='sTel' class='input' style='width:110' value='<?php echo $rs[sTel];?>' option='homephone' /></td>
</tr>
<?php
if(eregi('sPcs',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'>�� �޴��� (ȣ���)</td>
	<td bgColor='#ffffff'><input type='text' name='sPcs' class='input' style='width:110' value='<?php echo $rs[sPcs];?>' option='handphone' /></td>
</tr>
<?php
}

if(eregi('sURL',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'>�� Ȩ�������ּ�</td>
	<td bgColor='#ffffff'><input type='text' name='sURL' class='input' style='width:230' value='<?php echo $rs[sURL];?>' option='url' /> http://�� ���۵Ǵ� Ȩ������ �ּ�</td>
</tr>
<?php
}

if(eregi('sEmail',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>��</font> ���ڿ����ּ�</td>
	<td bgColor='#ffffff'><input type='text' name='sEmail' class='input' style='width:180' value='<?php echo $rs[sEmail];?>' option='email' required='required' /> ���� ������ ���Ϸ� �����帳�ϴ�.
	</td>
</tr>
<?php
}

if(eregi('sJob',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>��</font> �� ��</td>
	<td bgColor='#ffffff'><?php
	select('sJob', $aJob, 'one', $rs[sJob], null, null, null); // �̸�,�迭,�迭����,������,�������ϱ�,ù�ٺ���,js
	?></td>
</tr>
<?php
}
if(eregi('sCompany',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>��</font> �����(�б���)</td>
	<td bgColor='#ffffff'><input type='text' name='sCompany' class='input' style='width:150' value='<?php echo $rs[sCompany];?>' /></td>
</tr>
<?php
}
if(eregi('sTeam',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'>�� �μ��� (�а���)</td>
	<td bgColor='#ffffff'><input type='text' name='sTeam' class='input' style='width:150' value='<?php echo $rs[sTeam];?>' /></td>
</tr>
<?php
}

if(eregi('sCAddr',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'>�� �����ּ�</td>
	<td bgColor='#ffffff'>
		<input type='text' name='nCZip' class='input' size="3" maxlength='3' value='<?php echo $rs[nCZip];?>' READONLY /> - 
		<input type='text' name='nCZip1' class='input' size="3" maxlength='3' value='<?php echo $rs[nCZip1];?>' READONLY />
		<input type="button" value="�����ȣ �� �ּ� �ڵ��Է�" onClick="ZipCode('frm_write','nCZip','nCZip1','sCAddr','sCTel')" class='inputbtn'>
		<br />
		<input type="text" name="sCAddr" size="50" maxlength="120" class='input' value='<?=$rs[sCAddr]?>' READONLY />
		<input type='text' name='sCAddr2' class='input' style='width:150' value='<?php echo $rs[sCAddr2];?>' />
   </td>
</tr>
<?php
}

if(eregi('sCTel',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'>�� ������ȭ��ȣ</td>
	<td bgColor='#ffffff'><input type='text' name='sCTel' class='input' style='width:110' option='phone' value='<?php echo $rs[sCTel];?>' /></td>
</tr>
<?php
}
?>
<tr>
	<td bgColor='f7f7f7'>�� ����</td>
	<td bgColor='#ffffff'><input type='file' name='sUpload' maxlength='255' class='input' onChange="ImageSize(this);" style='width:60%' /> 80,60 ������</td>
</tr>
<tr>
	<td bgColor='f7f7f7'>�� �̸�����</td>
	<td bgColor='#ffffff'><?php prvUpload('member', 1, $rs[sUpload]); ?></td>
</tr>
</table>
<br>

<table width=96% align=center>
<tr>
	<td align="center" bgColor="#FFFFFF">
		<?php
		$sMsg = $_GET[mode]?'�����ϱ�':'���Խ�û';
		?>
		<input type="submit" value=" <?php echo $sMsg;?> "  class='inputbtn'>
		<input type="reset" value=" �ٽ� �Է� " class='inputbtn'>
	</td>
</tr>
</table>
</form>

<?php if(is_file('../html/'.$footer)) include "../html/".$footer; ?>