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
	if(!$_COOKIE[member_id]) msg('로그인후 이용하세요',null);
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
		다음 설명을 먼저 읽으신 후 회원가입 양식을 작성해 주시기 바랍니다. <br><br>
		1. 이름 및 주민등록번호는 실명확인 후 회원가입이 되므로 반드시 실제의 데이터를 사용해야 합니다. <br>
		2. 이용자님이 한번 정하신 아이디는 변경하실 수 없습니다. 주의 깊게 선택해주시기 바랍니다. <br>
		<font color='deeppink'>ㅁ 빨간색으로 표시된 부분은 필수 입력 사항입니다. </font>
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
	<td bgColor='f7f7f7' width='120'><font color='deeppink'>ㅁ</font> 아이디 (ID)</td>
	<td bgColor='#ffffff'>
	<?php
	if($_GET[mode] == 'modify') echo $rs[sID];
	else
	{
		echo "
		<input type='text' name='sID' size='10' maxlength='8' class='input' minbyte='5' maxbyte='8' hname='아이디' maxlength='8' required='required' option='engonly' />
		<input type='button' name='findID' value='아이디 검사' class='inputbtn' onClick='CheckID(sID)'/>
		<font color='deeppink'>영문 또는 영/숫자혼합하여 5-8자 </font>
		<input type='hidden' name='nRequiredIDCheck' errmsg='아이디 검사를 하지 않으셨습니다.' required='required' />	
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
		<td bgColor='f7f7f7'><font color='deeppink'>ㅁ</font> 변경 비밀번호</td>
		<td bgColor='#ffffff'>
			<input type='password' name='sPasswd' size='8' maxlength='8' class='input' minbyte='5' maxbyte='8' hname='비밀번호' match='sRepasswd'>
			<font color='deeppink'>영문 또는 영/숫자혼합하여 5-8자 </font>
		</td>
	</tr>
	<tr>
		<td bgColor='f7f7f7'><font color='deeppink'>ㅁ</font> 비밀번호 확인</td>
		<td bgColor='#ffffff'><input type='password' name='sRepasswd' size='8' maxlength='8' minbyte='5' maxbyte='8' class='input'> 비밀번호를 다시 입력해 주세요
		</td>
	</tr>
	<tr>
		<td bgColor='f7f7f7'><font color='deeppink'>ㅁ</font>최종 확인</td>
		<td bgColor='#ffffff'>
			비밀번호를 변경하시겠습니까?
			<input type='radio' name='set_pw' value='1'>예
			<input type='radio' name='set_pw' value='0' CHECKED>아니요
		</td>
	</tr>";
}
else
{
	echo "
	<tr>
		<td bgColor='f7f7f7'><font color='deeppink'>ㅁ</font> 비밀번호</td>
		<td bgColor='#ffffff'>
			<input type='password' name='sPasswd' size='8' maxlength='8' class='input' minbyte='5' maxbyte='8' hname='비밀번호' required='required'  match='sRepasswd'>
			<font color='deeppink'>영문 또는 영/숫자혼합하여 5-8자 </font>
		</td>
	</tr>
	<tr>
		<td bgColor='f7f7f7'><font color='deeppink'>ㅁ</font> 비밀번호 확인</td>
		<td bgColor='#ffffff'><input type='password' name='sRepasswd' size='8' maxlength='8' class='input'> 비밀번호를 다시 입력해 주세요 </td>
	</tr>";
}
?>
</table>

<br>

<table width="100%" cellspacing="1" cellpadding="4" border="0" bgcolor='cccccc'>
<tr>
	<td width="120" bgColor='f7f7f7'><font color='deeppink'>ㅁ</font> 한글 이름</td>
	<td bgColor='#ffffff'>
	<?php
	if($_GET[mode] != 'modify')
	{
	?>
		<input type="text" name="sName" size="10" maxlength="12" class='input' hname='한글이름' required='required'> 이름은 <font color="deeppink">실명으로 공백없이</font> 입력하세요<br>업체회원들게서는 <font color="deeppink"> 대표자 성함</font>으로 입력하시기 바랍니다.
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
	<td bgColor='f7f7f7'><font color='deeppink'>ㅁ</font> 주민등록번호</td>
	<td bgColor='#ffffff'>
		<input maxlength=6 name='nJumin' onKeyup='NextJumin(this)' size=6 class='input' hname='주민번호' required='required'> -
		<input maxlength=7 name='nJumin1' size=7 class='input' onBlur='inputbirth();Jumin(nJumin,nJumin1)' hname='주민번호' required='required' >
		잘못된 주민등록번호는 등록되지 않습니다.
	</td>
</tr>
<?php
}
ereg("([0-9]{4})+([0-9]{2})+([0-9]{2})",$rs[nBirthDay],$aDate);
?>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>ㅁ</font> 생년월일</td>
	<td bgColor='#ffffff'>
		<input maxlength='4' name='nBirthY' size=4 class='input' hname='생년월일' required='required' value='<?php echo $aDate[1];?>'> 년&nbsp;&nbsp;
		<select name="nBirthM" size=1 hname='생년월일' required='required'>
		<?php
		for($i=1; $i<13; ++$i)
		{
			if($i < 10) $i = '0'.$i;
			$slt = $i==$aDate[2]?'SELECTED':'';
			echo"<option value='$i' $slt>$i";
		}
		?>
		</select>월&nbsp;&nbsp;
		<select name="nBirthD" size=1 hname='생년월일' required='required'>
		<?php
		for($i=1; $i<32; ++$i)
		{
			if($i < 10) $i = '0'.$i;
			$slt = $i==$aDate[2]?'SELECTED':'';
			echo"<option value='$i' $slt>$i";
		}
		?>
		</select>일
		<input type="radio" name="nLunar" value="0" checked>양력
		<input type="radio" name="nLunar" value="1">음력

		<script language='javascript'>
		var i = '<?php $rs[nLunar]?$rs[nLunar]:0;?>';
		document.frm_write.elements('nLunar['+i+']').checked = true;
		</script>
	</td>
</tr>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>ㅁ</font> 성 별</td>
	<td bgColor='#ffffff'>
	<?php
	if($_GET[mode] == 'modify')
	{
		echo $rs[nSex]?'여자':'남자';
	}
	else
	{
	?>
		<input type="radio" name="nSex" value="0">남자
		<input type="radio" name="nSex" value="1">여자
	<?php
	}
	?>
	</td>
</tr>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>ㅁ</font> 주소</td>
	<td bgColor='#ffffff'>
		<input type="text" name="nZip" size="3" maxlength="3" class='input'  hname='우편번호' required='required'  value='<?=$rs[nZip]?>' READONLY /> - 
		<input type="text" name="nZip1" value='<?=$rs[nZip1]?>' size="3" maxlength="3" class='input'  hname='우편번호' required='required' READONLY /> &nbsp;		
		<input type="button" value="우편번호 및 주소 자동입력" onClick="ZipCode('frm_write','nZip','nZip1','sHAddr','sTel')" class='inputbtn'><br>
		<input type="text" name="sHAddr" size="50" class='input' value='<?=$rs[sHAddr]?>' READONLY />
		<input type='text' name='sHAddr2' class='input' style='width:150' value='<?php echo $rs[sHAddr2];?>' />
	</td>
</tr>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>ㅁ</font> 전화번호</td>
	<td bgColor='#ffffff'><input type='text' name='sTel' class='input' style='width:110' value='<?php echo $rs[sTel];?>' option='homephone' /></td>
</tr>
<?php
if(eregi('sPcs',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'>ㅁ 휴대폰 (호출기)</td>
	<td bgColor='#ffffff'><input type='text' name='sPcs' class='input' style='width:110' value='<?php echo $rs[sPcs];?>' option='handphone' /></td>
</tr>
<?php
}

if(eregi('sURL',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'>ㅁ 홈페이지주소</td>
	<td bgColor='#ffffff'><input type='text' name='sURL' class='input' style='width:230' value='<?php echo $rs[sURL];?>' option='url' /> http://로 시작되는 홈페이지 주소</td>
</tr>
<?php
}

if(eregi('sEmail',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>ㅁ</font> 전자우편주소</td>
	<td bgColor='#ffffff'><input type='text' name='sEmail' class='input' style='width:180' value='<?php echo $rs[sEmail];?>' option='email' required='required' /> 각종 정보를 메일로 보내드립니다.
	</td>
</tr>
<?php
}

if(eregi('sJob',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>ㅁ</font> 직 업</td>
	<td bgColor='#ffffff'><?php
	select('sJob', $aJob, 'one', $rs[sJob], null, null, null); // 이름,배열,배열종류,이전값,꼭선택하기,첫줄비우기,js
	?></td>
</tr>
<?php
}
if(eregi('sCompany',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'><font color='deeppink'>ㅁ</font> 직장명(학교명)</td>
	<td bgColor='#ffffff'><input type='text' name='sCompany' class='input' style='width:150' value='<?php echo $rs[sCompany];?>' /></td>
</tr>
<?php
}
if(eregi('sTeam',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'>ㅁ 부서명 (학과명)</td>
	<td bgColor='#ffffff'><input type='text' name='sTeam' class='input' style='width:150' value='<?php echo $rs[sTeam];?>' /></td>
</tr>
<?php
}

if(eregi('sCAddr',$pMemberAllow))
{
?>
<tr>
	<td bgColor='f7f7f7'>ㅁ 직장주소</td>
	<td bgColor='#ffffff'>
		<input type='text' name='nCZip' class='input' size="3" maxlength='3' value='<?php echo $rs[nCZip];?>' READONLY /> - 
		<input type='text' name='nCZip1' class='input' size="3" maxlength='3' value='<?php echo $rs[nCZip1];?>' READONLY />
		<input type="button" value="우편번호 및 주소 자동입력" onClick="ZipCode('frm_write','nCZip','nCZip1','sCAddr','sCTel')" class='inputbtn'>
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
	<td bgColor='f7f7f7'>ㅁ 직장전화번호</td>
	<td bgColor='#ffffff'><input type='text' name='sCTel' class='input' style='width:110' option='phone' value='<?php echo $rs[sCTel];?>' /></td>
</tr>
<?php
}
?>
<tr>
	<td bgColor='f7f7f7'>ㅁ 엠블럼</td>
	<td bgColor='#ffffff'><input type='file' name='sUpload' maxlength='255' class='input' onChange="ImageSize(this);" style='width:60%' /> 80,60 사이즈</td>
</tr>
<tr>
	<td bgColor='f7f7f7'>ㅁ 미리보기</td>
	<td bgColor='#ffffff'><?php prvUpload('member', 1, $rs[sUpload]); ?></td>
</tr>
</table>
<br>

<table width=96% align=center>
<tr>
	<td align="center" bgColor="#FFFFFF">
		<?php
		$sMsg = $_GET[mode]?'수정하기':'가입신청';
		?>
		<input type="submit" value=" <?php echo $sMsg;?> "  class='inputbtn'>
		<input type="reset" value=" 다시 입력 " class='inputbtn'>
	</td>
</tr>
</table>
</form>

<?php if(is_file('../html/'.$footer)) include "../html/".$footer; ?>