<!------------------------------ 새창으로 로그인 --------------------------------->
<form name='loginform' method=POST action='/member/member_process.php?psMode=LOGIN' onsubmit='return SoftCheck(this);' style='margin:0;border:0'>

	<input type='hidden' name='sURL' value='close' />

	<table border='0' cellspacing='1' width='360' height='140' align='center'>
	<tr>
		<td><img src='img/loginformttl.gif'></td>
	</tr>
	<tr><td height='60'><li>아이디와 패스워드를 입력하신후 로그인 해주십시요.</li></td></tr>
	<tr>
		<td background='img/loginformback.gif' align='center'>

			<table border='0' cellpadding='2' cellspacing='0' align=center width=260>
			<tr><td colspan=3 height=25></td></tr>
			<tr>
				<td width='50'></td>
				<td align='center'><input tabindex='1' type='text' name='sID' hname='아이디' required='required' size=15 maxlength=15 class='box'></td>
				<td rowspan=2><input tabindex=3 type='image' src='/member/img/loginform.gif' name='image'></td>
			</tr>
			<tr>
				<td width='50'></td>
				<td align='center'><input tabindex=2 type='password' name='sPasswd' msg='비밀번호' required='required' size=15 maxlength=15 class='box'></td>
			</tr>
			<tr><td colspan=3 height=40></td></tr>
			<tr>
				<td colspan='3' align=center> <!-- <font size='1' color='#FF9900'>▶</font><a href='/member/index.html'>회원가입</a> -->
				<font size='1' color='#FF9900'>▶</font><a href='#' onclick="window.open('/member/search_id.php','new','width=400,height=280,scrollbars=no,resizable=no,toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=no,copyhistory=no')"> 비밀번호분실</a>
				</td>
			</tr>
			<tr><td colspan=3 height=20></td></tr>
			</table>
		</td>
	</tr>
	</table>
</form>