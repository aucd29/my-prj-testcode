<?php
function GetToday()
{
	$w = date('w',time());
	$aDateType = array('일','월','화','수','목','금','토');
	echo date("Y년 m월 d일");
	echo ' ('.$aDateType[$w].')';
}
?>