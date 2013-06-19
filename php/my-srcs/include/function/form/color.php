<?php
$Color_nColorFormChk = null;
function color($sName='sColor', $sBfdata=null, $sSize='30%', $sClass='input')
{
	if(!$GLOBALS[Color_nColorFormChk])
	{
		echo "
		<script src='/include/js/color.js'></script>
		<style>
			.colorOut{}
			.colorOver{background-image:url(\"/include/js/color_slt.gif\");}
		</style>";            
	}
	++$GLOBALS[Color_nColorFormChk];

	echo "<input type='text' name='$sName' class='$sClass' value='$sBfdata' style='width:$sSize;cursor:hand' onclick=\"GetColor(this, sel$GLOBALS[Color_nColorFormChk]);\" /><font size='4' id='sel$GLOBALS[Color_nColorFormChk]' style='color:$sBfdata'>бс</font>";
}
?>