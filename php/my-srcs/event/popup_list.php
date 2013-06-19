<?php
//
// Default
//
include '../global.php';
include "../superadmin/admin_auth.php";
$sPage = 'popup';

//
// Function
//
require $_SELF[FUN].'/common/common.php';
?>
<html>
<head>
<title><?=$sPage?> 리스트</title>
<meta name='author' content='interkorea'>
<meta name='keywords' content=''>
<meta http-equiv='Cache-Control' content='no-cache'>
<meta http-equiv='Pragma' content='no-cache'>
<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>
<link rel='stylesheet' href='/include/css.css' type='text/css'>
<script language='javascript' src='/include/main.js'></script>
</head>
<body>
<?php
$RECNUM=30;
$sListFile = './data/list.cgi';
require $_SELF[FUN].'/file/page_calculation.php';
?>
<table cellspacing='0' cellpadding='0' border='0' width='95%' align='center'>
<tr>
	<td>등록된 총게시물 : <?=$DATA_NUM?></td>
	<td align='right' style='padding:0 10 0 0'><a href='<?=$sPage?>_write.php'>글쓰기</a></td>
</tr>
</table>
<table border='0' width='95%'  align='center' cellspacing='0' cellpadding='0'><tr><td height='5' bgcolor='black'></td></tr></table>
<table cellspacing='0' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6' align='center'>
<tr class='tableTR'>
	<td>번호</td>
	<td>제목</td>
	<td>작성일</td>
	<td>수정</td>
	<td>삭제</td>
</tr>
<?php
for($i = ($p-1)*$RECNUM; $i < (($p-1)*$RECNUM)+$RECNUM; ++$i)
{
	$FILENAME = chop($IDX[$i]);
	if($FILENAME)
	{
		include './data/'.$FILENAME.'.php';
		$wdate = expDate($wdate);
		$nList = $DATA_NUM - $i;

		echo "
		<tr>
			<td align='center'>$nList</td>
			<td><a href='${sPage}_view.php?no=$no'>$sTitle</a></td>
			<td align='center'>$wdate</td>
			<td align='center'><a href='${sPage}_write.php?mode=modify&no=$no'>[수정]</a></td>
			<td align='center'><a href=\"JavaScript:delChker2('${sPage}_process.php?psMode=REMOVE&no=$no&upload=$upload')\">[삭제]</a></td>
		</tr>";
	}
}
if(!$i) echo '<tr><td align=center colspan=5>게시물이 없습니다.</td></tr>';
echo '</table>';
$LISTNUM=30;
$ACT = $_SERVER[PHP_SELF].'?';
require $_SELF[FUN].'/file/page_list.php';
?>
</body>
</html>