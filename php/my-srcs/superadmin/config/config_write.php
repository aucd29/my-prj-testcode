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
// 회원관련 데이터파일
//
include "../../config/member_data.php";

if(is_file('../../config/admin_config.php'))
{
	include "../../config/admin_config.php";
}
else
{
	if(!is_dir('../../config'))
		msg('config 디렉토리 생성요구',NULL);	// null-> close 메세지후닫기 다른건 이동

	$arrBaseWrite = array(
		'HOME_NAME'  => "인터코리아",
		'HOME_EMAIL' => "kurome@damota.com",
		'HOME_URL'   => "http://localhost/",
		'HOME_TITLE' => ":: 인터코리아 ::",
		'wdate'		 => date(Ymd)
	);
	insert('../../config', &$arrBaseWrite, 'admin_config');
	$objCommon->go($PHP_SELF);
}
?>
<html>
<head>
<title>관리자설정</title>
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
			<td class='tableTR' width='20%'>홈페이지명</td>
			<td><input type='text' name='HOME_NAME' class='input' style='width:60%' value='<?=$HOME_NAME?>' hname='홈페이지명' required='required' /></td>
		</tr>
		<tr>
			<td class='tableTR'>관리자 메일주소</td>
			<td><input type='text' name='HOME_EMAIL' class='input' style='width:60%' value='<?=$HOME_EMAIL?>' hname='관리자메일주소' option='email' required='required' /></td>
		</tr>
		<tr>
			<td class='tableTR'>주소</td>
			<td><input type='text' name='HOME_URL' class='input' style='width:60%' value='<?=$HOME_URL?>' hname='홈페이지주소' required='required' option='url' /></td>
		</tr>
		<tr>
			<td class='tableTR'>홈페이지 타이틀</td>
			<td><input type='text' name='HOME_TITLE' class='input' style='width:60%' value='<?=$HOME_TITLE?>' hname='홈페이지타이틀' required='required' /></td>
		</tr>
		<tr>
			<td class='tableTR'>게시판 비밀번호</td>
			<td><input type='text' name='HOME_BOARD' class='input' style='width:60%' value='<?=$HOME_BOARD?>' hname='게시판 비밀번호' required='required' /></td>
		</tr>
		<tr>
			<td colspan='2' align='center'>
				<input type='submit'  value='  저 장  ' class='inputbtn' />
			</td>
		</tr>
		</table>
	</form>

<br />

	<?php
	if(is_file("../../config/member_db.php")) include "../../config/member_db.php";

	// 한번 세팅한후에는 지워버리자 그래야 후환이 없다.
	if(is_file("../../config/root.php"))
	{
	?>

	<form method='post' action='config_process.php?psMode=DBSET' name='frm_write2' enctype='multipart/form-data' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>

		<table border='0' width='95%'  align='center' cellspacing='0' cellpadding='0'><tr><td height='5' bgcolor='black'></td></tr></table>
		<table cellspacing='0' align='center' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6'>
		<tr>
			<td class='tableTR' width='20%'>서버</td>
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
			<td class='tableTR' width='20%'>호스트</td>
			<td><input type='text' name='sHost' class='input' style='width:60%' value='<?=$sHost?>' /></td>
		</tr>
		<tr>
			<td class='tableTR'>유저</td>
			<td><input type='text' name='sUser' class='input' style='width:60%' value='<?=$sUser?>' /></td>
		</tr>
		<tr>
			<td class='tableTR'>비번</td>
			<td><input type='text' name='sPasswd' class='input' style='width:60%' value='<?=$sPasswd?>' /></td>
		</tr>
		<tr>
			<td class='tableTR'>데이터베이스</td>
			<td><input type='text' name='sDB' class='input' style='width:60%' value='<?=$sDB?>' /></td>
		</tr>
		<tr>
			<td colspan='2' align='center'>
				<input type='submit'  value='  저 장  ' class='inputbtn' />
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
			<td colspan='2' class='tableTR'>회원가입 요소</td>
		</tr>
		<tr>
			<td>회원가입종류</td>
			<td>
				<?
				if(!$join)	$joinChk1 = 'checked';
				else		$joinChk2 = 'checked';
				?>
				<input type='radio' name='join' value='0' class='inputradio' <?=$joinChk1?> />승낙후가입
				<input type='radio' name='join' value='1' class='inputradio' <?=$joinChk2?> />바로가입
			</td>
		</tr>
		<tr>
			<td colspan='2' align='center'>
				<input type='submit'  value='  전 송  ' class='inputbtn' />
				<input type='button' name='open' value='  회원가입폼보기  ' class='inputbtn' onClick="winOpens('../../member/member_write.php', '회원가입', '680', '700')" />


				<?php
				include $_SELF[INC].'/dbconn.php3';
				$sql = "SELECT * FROM member";
				$res = @mysql_query($sql);
				if(!$res)
				{
					echo "
					<input type='button' name='btn' value=' 회원 디비 입력 ' onClick=location.href='./member_sql.php' class='inputbtn' />";
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
			<td width='20%' class='tableTR' title='음 여그는말이지 파일이 1개씩만들어가야된다 물론 파일명은 상관이없지만서도..'>회원가입 헤더</td>
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
			<td width='20%' class='tableTR'>회원가입 풋터</td>
			<td><? if($aFooterData) select('footer', $aFooterData, 'one', $footer); ?>&nbsp;</td>
			<td width='15'><input type='submit' value='  전 송  ' class='inputbtn' /></td>
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
			<td class='tableTR'>회원약관</td>
		</tr>
		<tr>
			<td><textarea name='article' style='width:100%;height:200px' onKeyup='var m=200;var s=this.scrollHeight;if(s>=m)this.style.pixelHeight=s+4'  class='textarea'><?=$article?></textarea></td>
		</tr>
		<tr>
			<td align='center'><input type='submit' value='  전 송  ' class='inputbtn' /></td>
		</tr>
		</table>
	</form>

</body>
</html>