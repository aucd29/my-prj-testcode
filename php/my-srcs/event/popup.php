<?php
//
// Default
//
include "../global.php";

//
// Include Data
//
$nList = file( "./data/list.cgi" );

//
// $$nMark ��Ŀ��� �Ѱ��� ��Ű�� �̿��ϵ��� in_array�������� ����
//
$aMark = explode('|', $_COOKIE[popup_mark]);
foreach($nList as $no)
{
	$no = trim($no);
	include "./data/$no.php";

	echo "$no | $nMark | ". $$nMark . " <br /> ";

	// ��¥������
	if($nApply)
	{
		$nToday = date(Ymd);
		if($nToday >= str_replace('-','',$nStart) && $nToday <= str_replace('-','',$nEnd))
			$nPass = 1;
		else
			$nPass = 0;
	}
	else
	{
		$nPass = 1;
	}
	
	if($nPass == 1 && !in_array($nMark, $aMark))
	{
		if($nScroll)
		{
			$nWidth = $nWidth + 13;
			$sScroll = ' scrollbars = yes';
			unset($nScroll);
		}
		else
		{
			$nWidth = $nWidth-3;
			unset($sScroll);
		}
		$nHeight = $nHeight + 20;
		$sMoveTo = $nMvApply?", left=$nHorWinPos, top=$nVerWinPos":'';

		echo "
		<script language='javascript'>
		<!--
			window.open('/event/event.php?no=$no','$no','toolbars=no, status=no, $sScroll, width=$nWidth, height=$nHeight $sMoveTo');
		//-->
		</script>\n";
	}
	unset($sScroll);
}
?>