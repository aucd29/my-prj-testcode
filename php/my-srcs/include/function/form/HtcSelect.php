<?php
/***************************************************************************
 *
 *		Date			: 2004-07-12
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			: HTC Control 
 *
 *		���� ���� htc��Ʈ���� ����غ���.
 *		�̻� ����Ʈ �ڽ��� �����Ѵ�. HTC Control�� �̿��ؼ� ���
 *
 ***************************************************************************/

function HtcSelectInit($sRoot='/include/htc/selectBox.htc')
{
	echo "
	<style>
	select{behavior: url('$sRoot');}
	</style>";
}
?>