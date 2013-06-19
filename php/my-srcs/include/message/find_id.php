<?php
/***************************************************************************
 *
 *		Date			: 2004-11-10
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

//
// Function
//
require $_SELF[FUN].'/common/common.php';
require $_SELF[CLS].'/dbconn.php';

if(!$_COOKIE[member_id]) msg('로그인 후 이용하세요', null);
?>
<html>
<head>
<title>친구검색하기</title>
<meta name='author' content='interkorea'>
<meta name='keywords' content=''>
<meta http-equiv='cache-control' content='no-cache'>
<meta http-equiv='pragma' content='no-cache'>
<meta http-equiv='content-type' content='text/html; charset=euc-kr'>
<link rel='stylesheet' href='/include/css.css' type='text/css'>
<script language='javascript' src='/include/main.js'></script>
<script language='javascript' src='/include/js/selectAll.js'></script>
<script language='javascript'>
<!--
	function sltFriend(obj)
	{
		var sum=0, txt;
		var old = opener.frm_write.sTo.value;

		for(i=0; i<obj['chkBox[]'].length; ++i)
		{
			if(obj['chkBox[]'][i].checked)
			{
				++sum;
				if(sum==1) txt = obj['chkBox[]'][i].value;
				else 	   txt = txt+','+obj['chkBox[]'][i].value;
			}
		}

		if(sum < 2) alert('다중 선택은 최소 2개 이상 선택해주세요');
		else
		{
			if(old) opener.frm_write.sTo.value = old+','+txt;
			else	opener.frm_write.sTo.value = txt;
			self.close();
		}
	}

	function sltFriend2(txt)
	{
		var old = opener.frm_write.sTo.value;
		if(old) opener.frm_write.sTo.value = old+','+txt;
		else	opener.frm_write.sTo.value = txt;
		self.close();
	}
//-->
</script>
</head>
<body>

	<form method='post' action='process.php?psMode=DELFRIEND&sURL=<?php echo $_SERVER[REQUEST_URI]?>' name='frm_write' enctype='multipart/form-data' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>

		<table cellspacing='0' align='center' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6'>
		<tr class='tableTR'>
			<td width='10%'><input type='button' name='btn' value='▼' class='inputbtn' onClick='selectAll(this)' /></td>
			<td width='50%'>친구</td>
		</tr>
		<?php
		// ------------------------------------------------------------------------
		// 친구 등록 파일 가져오기
		// ------------------------------------------------------------------------
		$sOrder = 'ORDER BY no DESC';
		$sWhere = "sMyID = '$_COOKIE[member_id]'";
		$sField = '*';
		$sTable = 'message_wish';

		$objDB->select($sTable, $sField, $sWhere, $sOrder);
		for($i=0; $rs = $objDB->fetch(); $i++)
		{
			$wdate = expDate($rs[wdate]);
			echo "
			<tr>
				<td align='center'><input type='checkbox' name='chkBox[]' value='$rs[sMemberID]' /></td>
				<td align='center'><a href=\"javascript:sltFriend2('$rs[sMemberID]')\">$rs[sName]($rs[sMemberID])</a></td>
			</tr>";
		}

		if(!$i)
		{
			echo "
			<tr>
				<td colspan='10' align='center'>등록된 친구가 없습니다.</td>
			</tr>";
		}
		?>
		</table>

		<table cellspacing='0' cellpadding='0' border='0'  width='95%' align='center'>
		<tr>
			<td height='10'></td>
		</tr>
		<tr>
			<td align='center'>
				<input type='button' name='btn' value=' 친구선택 ' onClick='sltFriend(this.form)' class='inputbtn' />
				<input type='submit' name='submit' value='  친구삭제  ' class='inputbtn' />
				<input type='button' name='back' value=' 닫 기 ' class='inputbtn' onClick='self.close()' />
			</td>
		</tr>
		</table>

	</form>

</body>
</html>