<?php
/***************************************************************************
 *
 *		Date			: 2004-10-01
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			:
 *
 *		관리자 체크
 *
 ***************************************************************************/

//
// Default Setting
//
require '../../global.php';
require "../../superadmin/admin_auth.php";

//
// Function
//
require $_SELF[FUN].'/common/common.php';
?>
<!--------------------------------------------------------------------
 +
 +		Date			:
 +		Copyright		: InterKorea(Kurome)
 +		E-mail			: aucd29@daum.net, help@mota.co.kr
 +
 +
 +
 + ------------------------------------------------------------------>
<html>
<head>
<title>카운터 생성</title>
<meta name='author' content='interkorea'>
<meta name='keywords' content=''>
<meta http-equiv='Cache-Control' content='no-cache'>
<meta http-equiv='Pragma' content='no-cache'>
<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>
<link rel='stylesheet' href='/include/css.css' type='text/css'>
<script language='javascript' src='/include/main.js'></script>
</head>
<body>

<table cellspacing='0' align='center' cellpadding='3' border='1' width='95%' bordercolordark='white' bordercolorlight='#D6D6D6'>
<tr class='tableTR b'>
	<td>번호</td>
	<td>카운터명</td>
	<td>초기화</td>
	<td>전체</td>
	<td>오늘</td>
	<td>최대</td>
	<td>최대동시접속자</td>
	<td>수정</td>
	<td>삭제</td>
</tr>
<?php
$sRoot = '../data';
$i = 1;
$objDir	= dir($sRoot);
while (false !== ($data	= $objDir->read()))
{
	if($data != '.' && $data != '..')
	{
		$sDir = '../data/'.$data;
		if(is_dir($sDir))
		{
			//include $sDir."/config.php";
			include $sDir."/db.php";	// max,today,total,maxpp data

			echo "
			<tr onMouseOver=this.style.backgroundColor='#ececec' onMouseOut=this.style.backgroundColor=''>
				<td align='center'>$i</td>
				<td><a href=''>$data</a></td>
				<td><a href=''>초기화</a></td>
				<td align='center'>$total</td>
				<td align='center'>$today</td>
				<td align='center'>$max</td>
				<td align='center'>$maxpp</td>
				<td align='center'><a href=''>수정</a></td>
				<td align='center'><a href=\"javascript:delChker('process.php?psMode=REMOVE&db=$data')\">삭제</a></td>
			</tr>";

			++$i;
		}
	}
}
$objDir->close();
if(!$i) echo "<tr><td colspan=9 align='center'>카운터가없습니다.</td></tr>";
?>
</table>
<form method='post' action='process.php?psMode=WRITE' name='frm_write' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>
	<table cellspacing='0' cellpadding='0' border='0' align='center' width='95%'>
	<tr>
		<td width='100'><input type='text' name='db' class='input' style='width:100%' /></td>
		<td>&nbsp;<input type='submit' value='  카운터 생성  ' class='inputbtn' /></td>
		<td width='50%' align='right' class=tahoma8>Coder Kurome 2004</td>
	</tr>
	</table>
</form>
</body>
</html>
