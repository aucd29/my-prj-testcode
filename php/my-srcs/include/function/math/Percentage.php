<?php
/***************************************************************************
 *
 *		Date			: 2004-07-20
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			: Function
 *
 *		����� ���ϱ� �ƾ� ������ ����α��� �̷��� ������ �� �ȳ��ٴ�
 *
 ***************************************************************************/

function &Percentage($nTotal,$nRight,$cut=2)
{
	return round(($nRight/$nTotal)*100,$cut);	// ������ ��, ��ü �� 
}
?>