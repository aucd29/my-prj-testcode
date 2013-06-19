<?php
/***************************************************************************
 *
 *		Date			: 2004-08-27
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			:
 *
 *		첫번째 날,두번째 날의 비교 잔여일을 가져온다. timestamp
 *
 ***************************************************************************/

function DateGap($f,$s)
{
	$gap = $s - $f;
	return (int)strftime("%j", $gap);
}
?>