<?php
/***************************************************************************
 *
 *		Date			: 2004-07-20
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			: Function
 *
 *		백분율 구하기 아아 기억력의 감퇴로구나 이런게 생각이 잘 안나다니
 *
 ***************************************************************************/

function &Percentage($nTotal,$nRight,$cut=2)
{
	return round(($nRight/$nTotal)*100,$cut);	// 맞은갯 수, 전체 수 
}
?>