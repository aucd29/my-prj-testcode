<?php
$sltDateJsRoot     = null;
$sltDateJsWriteChk = null;

function sltDate($sName, $sBefore=null, $nSize='30%', $class='input', $sltDateJsRoot=null, $disabled=null)
{
	if(!$GLOBALS[sltDateJsRoot])
	{
		$GLOBALS[sltDateJsRoot] = "<div id='CalendarLayer' style='display:none; width:172px;'><iframe name='CalendarFrame' src='/include/js/lib.calendar.html' width='172' height='175' border='0' frameborder='0' scrolling='no'></iframe></div>";
	}

	if(!$GLOBALS[sltDateJsWriteChk])
	{
		echo $GLOBALS[sltDateJsRoot];
		$GLOBALS[sltDateJsWriteChk] = 1;
	}

	$sValue = ($sBefore) ? "value='$sBefore'" : '';

	echo "<input name='$sName' type='text' onfocus='new CalendarFrame.Calendar(this)' $sValue class='$class' style='width:$nSize' $disabled READONLY />";
}
?>