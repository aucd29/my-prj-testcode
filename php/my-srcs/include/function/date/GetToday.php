<?php
function GetToday()
{
	$w = date('w',time());
	$aDateType = array('��','��','ȭ','��','��','��','��');
	echo date("Y�� m�� d��");
	echo ' ('.$aDateType[$w].')';
}
?>