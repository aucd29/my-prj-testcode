<!--------------------------------------------------------------------
 +
 +		date			: 2004-07-07
 +		copyright		: interkorea(kurome)
 +		e-mail			: aucd29@daum.net, help@mota.co.kr
 +
 +		ZipCode Search
 +		전달값 : sForm,sZipcode,sZipcode1,sAddr,sTel,nWhat
 +
 + ------------------------------------------------------------------>
<?php
if(!$_REQUEST[nWhat]&&!$_REQUEST[sForm])
{
	echo"
	<script> alert('전달값이 없습니다.'); self.close(); </script>";
	exit;
}
?>
<html>
<head>
<title>우편번호 검색</title>
<meta name='author' content='interkorea'>
<meta name='keywords' content=''>
<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='pragma' content='no-cache'>
<meta http-equiv='content-type' content='text/html; charset=euc-kr'>
<link rel='stylesheet' href='/include/css.css' type='text/css'>
<script language='javascript' src='/include/main.js'></script>
<style type='text/css'>
.post a{color:46ABB8}
.post a:hover{color:#FF9900;text-decoration:none}
</style>
<script language='javascript'>
<!--
	function Transfer(obj)
	{
		<?php
		if($_REQUEST[nWhat]==1)
		{
			$sForm		= 'signform';
			$sZipcode	= 'Hzipcode1';
			$sZipcode1	= 'Hzipcode2';
			$sAddr		= 'Haddr';
			$sTel		= 'Hphone1';
		}
		else if($_REQUEST[nWhat]==2)
		{
			$sForm		= 'signform';
			$sZipcode	= 'Ozipcode1';
			$sZipcode1	= 'Ozipcode2';
			$sAddr		= 'Oaddr';
			$sTel		= 'Ophone1';
		}
		else
		{
			$sForm		= $_REQUEST[sForm];
			$sZipcode	= $_REQUEST[sZipcode];
			$sZipcode1	= $_REQUEST[sZipcode1];
			$sAddr		= $_REQUEST[sAddr];
			$sTel		= $_REQUEST[sTel];
		}
		?>
		var frm = eval('opener.document.<?php echo $sForm;?>');
		var frm2 = document.frm_write;
		id = document.all(obj);

		frm.<?php echo $sZipcode;?>.value	= id[0].value;
		frm.<?php echo $sZipcode1;?>.value	= id[1].value;
		frm.<?php echo $sTel;?>.value		= id[2].value;
		frm.<?php echo $sAddr;?>.value		= id[3].value;

		self.close();
	}
//-->
</script>
</head>
<body style='margin:0'>
<table cellspacing='0' cellpadding='0' border='0' width='350' align='center' height='390'>
<tr>
	<td height='90'><img src='./img/top.gif' onFocus='this.blur()' /></td>
</tr>
<form method='post' name='frm_write' enctype='multipart/form-data' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>

<!-- HIDDEN VALUE sForm,sZipcode,sZipcode1,sAddr,sTel,nWhat -->
<input type='hidden' name='sForm'		value='<?php echo $_REQUEST[sForm];?>' />
<input type='hidden' name='sZipcode'	value='<?php echo $_REQUEST[sZipcode];?>' />
<input type='hidden' name='sZipcode1'	value='<?php echo $_REQUEST[sZipcode1];?>' />
<input type='hidden' name='sAddr'		value='<?php echo $_REQUEST[sAddr];?>' />
<input type='hidden' name='sTel'		value='<?php echo $_REQUEST[sTel];?>' />
<input type='hidden' name='nWhat'		value='<?php echo $_REQUEST[nWhat];?>' />

<tr>
	<td height='100' background='./img/mid.gif' style='padding:0 20 10 20'>
		<b style='color:46ABB8'>찾고자 하는 주소의 동(읍/면/리)을 입력 하십시오.</b>
		<br>※ 주소 검색이 안되면 쌍촌동 → 쌍촌
		<br><br>
		<table cellspacing='0' cellpadding='0' border='0' align='center'>
		<tr>
			<td><img src='./img/title.gif' border='0' onFocus='this.blur()'></td>
			<td><input type='text' name='search' value='<?php echo $_POST[search];?>' errmsg='찾고자 하는 주소의 동(읍/면/리)을 입력 하십시오' required='required' style='background-color:F6F6F6;border:1px B3B3B3 solid' class='input' style='width:120' /></td>
			<td><button type='submit' onFocus='this.blur()'><img src='./img/btn_search.gif'></button></td>
		</tr>
		</table>
	</td>
</tr>
<tr>
	<td height='1' background='./img/mid.gif'><hr style='height:1px;color:#ececec;width:90%;margin:0;border:0'></hr></td>
</tr>
</form>
<?php
//
// 검색 을 했을때
//
if($_POST)
{
	include "./zipconn.php";
	echo '<tr valign=top><td background=./img/mid.gif><table cellpadding=0 align=center width=315 border=0 cellspacing=0 class=post>';
	$query = "SELECT uid,code,addr1,addr2,addr3,addr4,gukbun FROM zipcode WHERE addr3 LIKE '%$search%' ORDER BY uid";
	$result = mysql_query($query) or die(mysql_error());
	while($rs = mysql_fetch_array($result,MYSQL_ASSOC))
	{
		++$i;
		$nZipCode = explode('-', $rs[code]);
		echo "<tr onMouseOver=bgColor='#f9f9f9' onMouseOut=bgColor=''><td width=45 class='tahoma8' height=22 style='color:6b6b6b'>$rs[code]</td><td><a href=\"javascript:Transfer('info$i')\" class='tahoma8'>$rs[addr1] $rs[addr2] $rs[addr3] $rs[addr4]</a></td></tr><tr><td colspan=2 height=1 bgcolor=#ececec></td></tr>
		<input type='hidden' id='info${i}' name='info${i}[]' value='$nZipCode[0]' />
		<input type='hidden' id='info${i}' name='info${i}[]' value='$nZipCode[1]' />
		<input type='hidden' id='info${i}' name='info${i}[]' value='$rs[gukbun]' />
		<input type='hidden' id='info${i}' name='info${i}[]' value='$rs[addr1] $rs[addr2] $rs[addr3]' />";
	}

	if(!$i) echo '<tr><td align=center>검색결과가 없습니다.</td></tr>';

	echo '</table></td></tr>';
}
else
{
	echo "
	<tr>
		<td height='150' background='./img/mid.gif' align='center'>검색어를 입력해주세요</td>
	</tr>";
}
?>
<tr>
	<td background='./img/bottom.gif' height='36' align='right' style='padding:15 10 0 0'><button type='button' onFocus='this.blur()' onClick='self.close()' style='width:9;height:9'><img width='9' height='9' src='./img/close.gif' border='0' onFocus='this.blur()'></button></td>
</tr>
</table>
</body>
</html>
