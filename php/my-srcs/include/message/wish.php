<?php
// ------------------------------------------------------------------------
// 친구검색, 친구등록
// ------------------------------------------------------------------------
include "../../global.php";
include $_SELF[CLS].'/class.Common.php';

if(!$_COOKIE[member_id]) $objCommon->msg('로그인 하세요');
?>
<html>
<head>
<title></title>
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
	function nView(obj)
	{
		if(obj == view1)
		{
			view1.style.display = '';
			view2.style.display = 'none';
		}
		else
		{
			view2.style.display = '';
			view1.style.display = 'none';
		}
	}
//-->
</script>
</head>
<body <?php if($srhMode) echo "onLoad='nView(view2)'"; ?>>

	<?php include "./menu.php"; ?>

	<table cellspacing='0' align='center' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6'>
	<tr class='tableTR'>
		<td>

			<input type='radio' name='nView' value=1 class='inputradio' checked onClick='nView(view1)' />로그인중 회원
			<input type='radio' name='nView' value=2 class='inputradio' onClick='nView(view2)' <?php if($srhMode) echo "checked"; ?> />디비검색

		</td>
	</tr>
	</table>

	<br />

	<table cellspacing='0' cellpadding='0' border='0'  width='100%' align='center'>
	<tr id='view1'>
		<td>

			<form method='post' action='process.php?psMode=ADDFRIEND' name='frm_write' enctype='multipart/form-data' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>

				<table cellspacing='0' align='center' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6'>
				<tr class='tableTR'>
					<td><input type='button' name='btn' value='▼' class='inputbtn' onClick='selectAll(this)' /></td>
					<td>아이디</td>
					<td>로그인 시간</td>
				</tr>
				<?php
				include $_SELF[CLS].'/class.Filecontrol.php';
				$sDir = $_SELF[DOC].'/member/login_user';
				$aFile = $objFctl->readData($sDir, 'file');

				foreach($aFile as $index => $value)
				{
					$nTime = date("d일 H시 i분", filemtime($sDir."/$value"));
					$nData = file($sDir.'/'.$value);
					$aData = explode('|', $nData[0]);
					$sName = $aData[1];
					$sID   = $aData[0];

					$value = basename($value, '.cgi');

					if($value != $_COOKIE[member_id])
					{
						echo "
						<tr>
							<td align='center'><input type='checkbox' name='chkBox[]' value='$sID|$sName' /></td>
							<td align='center'>$sID($sName)</td>
							<td align='center'>$nTime</td>
						</tr>";
					}
				}
				?>
				</table>

				<br />

				<div align='center'><input type='submit' name='submit' value='  친구추가  ' class='inputbtn' /></div>

			</form>

		</td>
	</tr>
	</table>



	<table cellspacing='0' cellpadding='0' border='0'  width='100%' align='center'>
	<tr id='view2' style='display:none'>
		<td>

			<form method='post' name='frm_write' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>

				<input type='hidden' name='srhMode' value='1' />
				<table cellspacing='0' align='center' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6'>
				<tr>
					<td class='tableTR'>종류</td>
					<td>

						<?php
						if($srhItem == 'id')
						{
							$srhItemChk1 = 'checked';
						}
						else
						{
							$srhItemChk2 = 'checked';
						}
						?>
						<input type='radio' name='srhItem' value='id' class='inputradio' <?php echo $srhItemChk1;?>/> 아이디
						<input type='radio' name='srhItem' value='name' class='inputradio' <?php echo $srhItemChk2;?> /> 이름

					</td>
				</tr>
				<tr>
					<td class='tableTR'>검색어</td>
					<td><input type='text' name='srhWord' class='input' style='width:60%' value='<?php echo $srhWord;?>' /></td>
				</tr>
				<tr>
					<td colspan='2' align='center'><input type='submit' name='submit' value='  전 송  ' class='inputbtn' /></td>
				</tr>
				</table>


			</form>

			<?php
			if($srhMode)
			{
				include $_SELF[INC].'/dbconn.php';

				echo "
				<br />

				<form method='post' action='./process.php?psMode=ADDFRIEND' name='frm_write2' style='border:0;margin:0' onSubmit='return SoftCheck(this)'>

					<table cellspacing='0' align='center' cellpadding='3' border='1' width='95%' bordercolordark='white'  bordercolorlight='#D6D6D6'>
					<tr class='tableTR'>
						<td><input type='button' name='btn' value='▼' class='inputbtn' onClick='selectAll(this)' /></td>
						<td>아이디 및 이름</td>
						<td>가입일</td>
					</tr>";

						$sOrder = 'ORDER BY uid DESC';

						if($srhItem == 'id')
						{
							$sWhere = "$srhItem='$srhWord'";
						}
						else
						{
							$sWhere = "$srhItem LIKE '%$srhWord%'";
						}

						$sWhere .= " AND id != '$_COOKIE[member_id]'";	// 자기 자신은 친구로 할 순 없지

						$sField = 'signdate, name, id';
						$sTable = 'member';

						$objDB->select($sTable, $sField, $sWhere, $sOrder);

						if(is_file("./data/$_COOKIE[member_id]"))
						{
							$aFile = file("./data/$_COOKIE[member_id]");
							$aData = explode('|', $aFile);
						}

						for($i=0; $data = $objDB->fetch(); $i++)
						{
							$signdate = date("Y-m-d", $data[signdate]);

							echo "
							<tr>
								<td align='center'><input type='checkbox' name='chkBox[]' value='$data[id]|$data[name]' /></td>
								<td>$data[name]($data[id])</td>
								<td align='center'>$signdate</td>
							</tr>";
						}

					echo"
					</table>

					<table cellspacing='0' cellpadding='0' border='0'  width='95%' align='center'>
					<tr>
						<td align='center'><input type='submit' value='  친구 등록  ' class='inputbtn' /></td>
					</tr>
					</table>

				</form>";
			}
			?>
		</td>
	</tr>
	</table>


</body>
</html>