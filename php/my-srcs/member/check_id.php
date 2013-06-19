<?php
//
// Default Setting
//
require '../global.php';

//
// Function
//
require $_SELF[FUN].'/common/common.php';
require $_SELF[FUN].'/sql/query_engin.php';
require $_SELF[INC].'/dbconn.php3';
?>
<html>
<head>
<title>아이디 중복 검사</title>
<meta name='author' content='interkorea'>
<meta name='keywords' content=''>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>
<link rel='stylesheet' href='/include/css.css' type='text/css'>
<script language='javascript' src='/include/main.js'></script>
<script language='javascript'>
<!--
function replace_id()
{
	opener.document.frm_write.elements('sID').value = '<?php echo $_GET[sID];?>';
	opener.document.frm_write.elements('nRequiredIDCheck').value = 1;
	self.close();
}
//-->
</script>
</head>
<body style='margin:0;border:0'  background='img/check_id.gif'>
<?php
$rs = sql_query("SELECT COUNT(*) FROM member WHERE sID='$_GET[sID]'",MYSQL_NUM);
if($rs[0])
{
?>
	<form method='post' action='check_id.php' name='check_id' onSubmit='return SoftCheck(this)'>
	<table width="100%" border="0" cellspacing="5" align="center">
	<tr><td height=82></td></tr>
	<tr>
		<td align="center">
		신청하신 [ <font color='darkred'><?php echo $_GET[sID]; ?></font> ] 아이디는 이미 등록</font>되어 있습니다.</td>
	</tr>
	<tr><td height='30'></td></tr>
	<tr><td align='center'><font color='red'>다른 아이디를 입력하시고 중복검사 버튼을 클릭해 주십시오.</font></td></tr>
	<tr><td height='20'></td></tr>
	<tr>
		<td align="center">
			<input type="text" name='id' size='12' minbyte='5' maxbyte='8' hname='아이디' required='required'>
			<input type='submit' value='  검  사  ' class='inputbtn' />
		</td>
	</tr>
	</table>
	</form>
<?
}
else
{
?>
	<table width="100%" border="0" cellspacing="5" align="center">
	<tr><td height=82></td></tr>
	<tr>
		<td align="center">신청하신 [ <font color='darkred'><?php echo $_GET[sID]; ?></font> ] 아이디는 사용가능합니다.</td>
	</tr>
	<tr><td height=50></td></tr>
	<tr>
		<td align="center" align="center">
		<input type="button" onClick="replace_id()" value="이 아이디로 신청합니다.">
		</td>
	</tr>
	</table>
<?
}
?>
</body>
</html>