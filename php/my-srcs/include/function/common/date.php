<?php
function getToday()
{
	$nY = date(Y);
	$nM = date(m);
	$nD = date(d);
	$today = array(
		'일','월','화','수','목','금','토'
	);
	$ntoday = date(w, mktime(0, 0, 0, $nM, $nD, $nY));
	echo "${nY}년 ${nM}월 ${nD}일 ($today[$ntoday])";
}
?>