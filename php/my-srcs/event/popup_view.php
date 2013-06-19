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
<title><?=$sPage?>쓰기</title>
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
	<td class='tableTR' width='20%'>제목</td>
	<td><?php echo $sTitle;?></td>
</tr>
<tr>
	<td class='tabletr'>종류</td>
	<td><?php echo (!$nType || $nType == 1)?"이미지":"HTML";?></td>
</tr>
<tr>
	<td class='tableTR'>HTML</td>
	<td><textarea name='html' style='width:100%;height:100px' onKeyup="var m=50;var s=this.scrollHeight;if(s>=m)this.style.pixelHeight=s+4" class='textarea'><?=$html?></textarea></td>
</tr>
<tr>
	<td class='tableTR'>베너선택시</td>
	<td>
		<table cellspacing='0' cellpadding='0' border='0'>
		<tr>
			<td>
			<?php
			if($nClicked == 1 || !$nClicked)
				$sClickChk = '닫기';
			else if($nClicked == 2)
				$sClickChk = '베너에서 열기';
			else
				$sClickChk = '홈페이지에서 열기';
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
	<td class='tableTR'>그림보기</td>
	<td><? $objCommon->prvUpload( $sPage, $nType , $upload ); ?>&nbsp;</td>
</tr>
<tr>
	<td class='tabletr'>창크기</td>
	<td>
	가로 <input type='text' name='nWidth' class='input' style='width:60' value='<?=$nWidth?>' msg='창 가로크기를 입력해주세요' required />
	세로 <input type='text' name='nHeight' class='input' style='width:60' value='<?=$nHeight?>' msg='창 세로크기를 입력해주세요' required />
	</td>
</tr>
<tr>
	<td class='tabletr'>창이동</td>
	<td>
	가로 <input type='text' name='nHorWinPos' class='input' style='width:60' value='<?=$nHorWinPos?>' />
	세로 <input type='text' name='nVerWinPos' class='input' style='width:60' value='<?=$nVerWinPos?>' />
	</td>
</tr>
<tr>
	<td class='tabletr'>스크롤</td>
	<td><?php $nScrollChk = ( $nScroll ) ? 'checked' : '';?>
	<input type='checkbox' name='nScroll' value='1' class='inputradio' <?=$nScrollChk?> /> 스크롤 생성 </td>
</tr>
<tr>
	<td class='tabletr'>이벤트 기간</td>
	<td>시작 : <?php echo $nStart; ?>종료 : <?php echo $nEnd; ?></td>
</tr>

<tr>
	<td colspan='2' align='center'>
	<input type='submit' name='submit' value='  전 송  ' class='inputbtn' />
	<input type='button' name='back' value='  뒤 로  ' class='inputbtn' onClick='history.back();' />
	</td>
</tr>
</table>
</form>
</body>
</html>