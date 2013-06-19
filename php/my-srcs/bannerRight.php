<?php
$nGAP = ($sPageName == 'main') ? '230' : '210'; ?>
<script language='javascript'>
<!--	
	var stmnGAP1			= <?=$nGAP?>;
	var stmnGAP2			= 0;
	var stmnBASE			= <?=$nGAP?>;
	var stmnActivateSpeed	= 200;
	var stmnScrollSpeed		= 10;
	var stmnTimer;
//-->
</script>
<script language='javascript' src='/include/js/scroll.js'></script>
<div id='STATICMENU' style="position: absolute; top: 230">
<table cellspacing=0 cellpadding=0 border=0>
<tr>
	<!-- 이미지 넣는 곳 -->
	<td>
	
	</td>
</tr>
<tr>
	<td align='center'><INPUT id='ANIMATE' onclick='ToggleAnimate();' type='hidden'></td>
</tr>
</table>
<script language=javascript>InitializeStaticMenu();</script>
</div>