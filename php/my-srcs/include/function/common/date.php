<?php
function getToday()
{
	$nY = date(Y);
	$nM = date(m);
	$nD = date(d);
	$today = array(
		'��','��','ȭ','��','��','��','��'
	);
	$ntoday = date(w, mktime(0, 0, 0, $nM, $nD, $nY));
	echo "${nY}�� ${nM}�� ${nD}�� ($today[$ntoday])";
}
?>