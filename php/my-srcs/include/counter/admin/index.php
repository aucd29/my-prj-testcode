<?php
/***************************************************************************
 *
 *		Date			: 2004-10-01
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			:
 *
 *		������ üũ
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
<title>ī���� ����</title>
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
	<td>��ȣ</td>
	<td>ī���͸�</td>
	<td>�ʱ�ȭ</td>
	<td>��ü</td>
	<td>����</td>
	<td>�ִ�</td>
	<td>�ִ뵿��������</td>
	<td>����</td>
	<td>����</td>
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
				<td><a href=''>�ʱ�ȭ</a></td>
				<td align='center'>$total</td>
				<td align='center'>$today</td>
				<td align='center'>$max</td>
				<td align='center'>$maxpp</td>
				<td align='center'><a href=''>����</a></td>
				<td align='center'><a href=\"javascript:delChker('process.php?psMode=REMOVE&db=$data')\">����</a></td>
			</tr>";

			++$i;
		}
	}
}
$objDir->close();
if(!$i) echo "<tr><td colspan=9 align='center'>ī���Ͱ������ϴ�.</td></tr>";
?>
</table>
<form method='post' action='process.php?psMode=WRITE' name='frm_write' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>
	<table cellspacing='0' cellpadding='0' border='0' align='center' width='95%'>
	<tr>
		<td width='100'><input type='text' name='db' class='input' style='width:100%' /></td>
		<td>&nbsp;<input type='submit' value='  ī���� ����  ' class='inputbtn' /></td>
		<td width='50%' align='right' class=tahoma8>Coder Kurome 2004</td>
	</tr>
	</table>
</form>
</body>
</html>
