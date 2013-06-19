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
require '../../global.php';
$sPage = 'sendmail';

//
// Function
//
require $_SELF[FUN].'/common/common.php';
require $_SELF[SPR]."/admin_auth.php";
?>
<html>
<head>
<title>리스트보기</title>
<meta name='author' content='interkorea'>
<meta name='keywords' content=''>
<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>
<link rel='stylesheet' href='/include/css.css' type='text/css'>
<script language='javascript' src='/include/main.js'></script>
</head>
<body>
<?php
$RECNUM="10";
if(!$p) $p="1";
if(!is_file("./data/list.cgi")) touch("./data/list.cgi");
$IDX = file("./data/list.cgi");
$DATA_NUM = sizeof($IDX);
$PRINT_DATA_NUM = number_format($DATA_NUM);
if($RECNUM == 1)
	$TOTAL_PAGE_NUM = intval($DATA_NUM/$RECNUM);
else
{
	$TOTAL_DATA_NUM2 = $DATA_NUM % $RECNUM;
	if($TOTAL_DATA_NUM2 == 0)
		$TOTAL_PAGE_NUM = intval($DATA_NUM/$RECNUM);
	else
		$TOTAL_PAGE_NUM = intval($DATA_NUM/$RECNUM)+1;
}
?>
<table width='90%' align='center'><tr><td>총게시물 : <?=$DATA_NUM?>개</td></tr></table>
<table border='0' width='95%'  align='center' cellspacing='0' cellpadding='0'><tr><td height='5' bgcolor='black'></td></tr></table>
<table cellspacing='0' align='center' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6'>
<tr class='tableTR'>
	<td>제목</td>
	<td>작성일</td>
	<td>삭제</td>
</tr>
<?php
for($i = ($p-1)*$RECNUM; $i < (($p-1)*$RECNUM)+$RECNUM;++$i)
{
	$dcolor = ($dcolor == "FFFFFF")?'#f4f4f4':'#ffffff';
	$FILENAME = chop($IDX[$i]);
	if ($FILENAME)
	{
		include "./data/$FILENAME.php";
		echo "
		<tr>
			<td><a href='./sendmail_view.php?no=$no&sTable=$sTable'>$sFromName 님이 보내신 메일입니다.</a></td>
			<td align='center'>".expDate($wdate)."</td>
			<td align='center'><a href='javascript:;' onClick=\"delChker('sendmail_process.php','psMode=REMOVE&no=$no&sTable=./data/$sTable&sCage=$sTable')\">삭제</a></td>
		</tr>";
	}
}
echo"</TABLE>
$ACT = "$PHP_SELF?";
include $_SELF[FUN].'/file/page.php';
?>
</body>
</html>