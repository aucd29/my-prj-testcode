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
<title>���̵� �ߺ� �˻�</title>
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
		��û�Ͻ� [ <font color='darkred'><?php echo $_GET[sID]; ?></font> ] ���̵�� �̹� ���</font>�Ǿ� �ֽ��ϴ�.</td>
	</tr>
	<tr><td height='30'></td></tr>
	<tr><td align='center'><font color='red'>�ٸ� ���̵� �Է��Ͻð� �ߺ��˻� ��ư�� Ŭ���� �ֽʽÿ�.</font></td></tr>
	<tr><td height='20'></td></tr>
	<tr>
		<td align="center">
			<input type="text" name='id' size='12' minbyte='5' maxbyte='8' hname='���̵�' required='required'>
			<input type='submit' value='  ��  ��  ' class='inputbtn' />
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
		<td align="center">��û�Ͻ� [ <font color='darkred'><?php echo $_GET[sID]; ?></font> ] ���̵�� ��밡���մϴ�.</td>
	</tr>
	<tr><td height=50></td></tr>
	<tr>
		<td align="center" align="center">
		<input type="button" onClick="replace_id()" value="�� ���̵�� ��û�մϴ�.">
		</td>
	</tr>
	</table>
<?
}
?>
</body>
</html>