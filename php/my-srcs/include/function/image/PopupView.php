<?php
/***************************************************************************
 *
 *		Date			: 2004-07-27
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			: Preview
 *
 *		�̹����� �׳� �˾����� ���� �ְ� ���ش�.
 *
 ***************************************************************************/

function PopupInit($pos='/include/poper.js')
{
	echo "<DIV class='popper' id='topdeck'  style='position : absolute; visibility : hidden;' class='border shadow7'></DIV>
	<script language='javascript' src='$pos'></script>";
}

function PopupView($sImagePath,$sDomain=NULL)
{
	if(is_file($sImagePath))
	{
		$imgsize = getimagesize($sImagePath);
		$prev_img = "<img src=$sImagePath width=$imgsize[0] height=$imgsize[1]>";
		$img = "<img src='$sDomain/include/img/image.gif' onMouseOver=\"pop('$prev_img')\" onMouseOut='kill()' width='14' height='14' align='absbottom'>";

		return $img;
	}
}
?>