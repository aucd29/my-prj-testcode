<?php
/***************************************************************************
 *
 *		Date			: 2004-11-10
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
$sTable = $sPage = 'message';
require '../../global.php';

//
// Function
//
require $_SELF[FUN].'/common/common.php';
require $_SELF[FUN].'/form/HtcSelect.php';

//
// Class
//
include $_SELF[INC].'/dbconn.php';
include $_SELF[CLS].'/class.Page.php';
include $_SELF[CLS].'/class.Common.php';

$sSearchWord = ($_GET[type]) ? $_GET[type] : 'sTo';
if(!$_COOKIE[member_id]) msg('�α��� �� �̿��ϼ���');
?>
<html>
<head>
<title>����Ʈ</title>
<meta name='author' content='interkorea'>
<meta name='keywords' content=''>
<meta http-equiv='Cache-Control' content='no-cache'>
<meta http-equiv='Pragma' content='no-cache'>
<meta http-equiv='Content-Type' content='text/html; charset=euc-kr'>
<link rel='stylesheet' href='../css.css' type='text/css'>
<script language='javascript' src='../main.js'></script>
<script language='javascript' src='../js/selectAll.js'></script>
<?php HtcSelectInit(); ?>
</head>
<body>

	<?php include "./menu.php"; ?>

	<form name='frm_write2' method='post' action='./process.php?psMode=MULTIDEL&type=<?php echo $_GET[type]?>&nPos=<?php echo $nPos;?>' style='margin:0;border:0'>

	<table cellspacing='1' cellpadding='3' border='0' align='center' width='98%'>
	<tr>
		<td colspan='4' height='3' bgcolor='#FB7904'></td>
	</tr>
	<tr class='tableTR'>
		<td><input type='button' name='btn' value='��' class='inputbtn' onClick='selectAll(this)' /></td>
		<?php
		if($_GET[type])
			echo "<td>�޴»��</td>";
		else
			echo "<td>�������</td>";
		?>
		<td width='50%'>����</td>
		<td>�ۼ���</td>
	</tr>
	<tr>
		<td colspan='4' height='1' bgcolor='#ececec'></td>
	</tr>

	<input type='hidden' name='sURL' value='<?php echo $_SERVER[REQUEST_URI];?>' />
<?php
$nPage       = ($_GET[nPage]) ? $_GET[nPage] : 1;
$nViewCount  = 7;
$nBlockCount = 10;
$nStart      = ($nPage - 1) * $nViewCount;
// �߰����� �ɼ��� ������ ����ٰ��־��� DB
$sWhere      = "$sSearchWord='$_COOKIE[member_id]'";
// �߰����� �ɼ��� ������ ����ٰ��־��� PAGE
$sAddWhere   = "$sSearchWord=$_COOKIE[member_id]&nPos=$nPos&type=$_GET[type]";

if($srhList)
{
	if($sAddWhere)
		$sAddWhere .= "&srhList=$srhList&srhWord=$srhWord";
	else
		$sAddWhere .= "srhList=$srhList&srhWord=$srhWord";
}

$sTable = $sPage;
$sField = '*';
$sOrder = 'ORDER BY no DESC';

// Search Option
if($srhList && $srhWord)
{
	if($sWhere)
		$sWhere .= " AND $srhList LIKE '%$srhWord%'";
	else
		$sWhere .= " $srhList LIKE '%$srhWord%'";
}

$nTotal = (!$nTotal) ? $objDB->getCount($sTable, $sWhere) : $nTotal;
$nBoNum = $nTotal - $nViewCount * ($nPage - 1);
$objDB->select($sTable, $sField, $sWhere, $sOrder, $nViewCount, $nStart);

for($i=0; $data = $objDB->fetch(); $i++)
{
	if($data[wdate]) $wdate = $objCommon->expDate($data[wdate]);
	$nono = $nBoNum - $i;

	$newIcon = (date(Ymd) <= $data[wdate]+1) ? "<font color='red' size='-5'>NEW</font>" : '';

	echo"
	<tr>
		<td align='center'><input type='checkbox' name='chkBox[]' value='$data[no]' /></td>";

		if($_GET[type])
			echo "<td align='center'>$data[sTo]</td>";
		else
			echo "<td align='center'>$data[sFrom]</td>";

		echo "
		<td><a href='view.php?no=$data[no]&nPos=$nPos&type=$_GET[type]&nPage=$nPage'>$data[sTitle]</a> $newIcon</td>
		<td align='center'>$wdate</td>
	</tr>
	<tr>
		<td colspan='4' height='1' bgcolor='#ececec'></td>
	</tr>";
}
if(!$i) echo "<tr><td align='center' colspan='10'>�Խù��� �����ϴ�.</td></tr>";

echo "
<tr>
	<td colspan='4' height='3' bgcolor='#ececec'></td>
</tr>
<tr>
	<td colspan='4' height='3'></td>
</tr>
</table>

<table cellspacing='0' cellpadding='0' border='0'  width='98%' align='center'>
<tr>
	<td><input type='submit' name='submit' value='�����ϱ�' class='inputbtn' /></td>
</tr>
</table>

</form>";

$sPageView = "
<table cellspacing='0' cellpadding='0' border='0' align='center' width='95%'>
<tr align=center height=30>
	<td colspan='5' width='80%'><font color='#D5935E'>
	<a href=':LEFT:'>[����]</a>
	:PAGE_BLOCK:
	<a href=':RIGHT:'>[����]</a>
	</font></td>
</tr>
</table>";

$objPage->setData($nTotal, $nPage, $nViewCount, $nBlockCount, $sAddWhere);
echo $objPage->pageView($sPageView);

// ------------------------------------------------------------------------
// Search Box
// ------------------------------------------------------------------------
$srhItem = array(
	'sTitle'    => '����',
	'sContent'  => '����',
	'sTo'       => '�޴»��',
);

$objPage->srhBox($srhItem, $srhList, $srhWord, $imgPos, $sAddWhere);
?>
</body>
</html>