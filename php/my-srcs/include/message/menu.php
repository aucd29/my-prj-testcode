<table cellspacing='1' cellpadding='3' border='0' align='center' width='98%'>
<tr>
	<td height='3' bgcolor='#FB7904'></td>
</tr>
<tr class='tableTD'>
	<td>

		<a href='./write.php?nPos=1' <?php if($_GET[nPos] == 1) echo "style='font-size:10pt;font-weight:bold;color:#FF6600'"; ?>>쪽지 쓰기</a> |
		<a href='./receive.php?nPos=2' <?php if($_GET[nPos] == 2 || !$_GET[nPos]) echo "style='font-size:10pt;font-weight:bold;color:#FF6600'"; ?>>받은편지함</a> |
		<a href='./receive.php?type=sFrom&nPos=3' <?php if($_GET[nPos] == 3) echo "style='font-size:10pt;font-weight:bold;color:#FF6600'"; ?>>보낸 편지함</a> |
		<a href='./wish.php?nPos=4' <?php if($_GET[nPos] == 4) echo "style='font-size:10pt;font-weight:bold;color:#FF6600'"; ?>>친구등록</a>

	</td>
</tr>
<tr>
	<td height='2' bgcolor='ececec'></td>
</tr>
</table>

<br />