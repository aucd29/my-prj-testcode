<?php
/***************************************************************************
 *
 *		Date			: 2004-08-27
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			:
 *
 *		ù��° ��,�ι�° ���� �� �ܿ����� �����´�. timestamp
 *
 ***************************************************************************/

function DateGap($f,$s)
{
	$gap = $s - $f;
	return (int)strftime("%j", $gap);
}
?>