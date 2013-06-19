<?php
/***************************************************************************
 *
 *		Date			: 2004-07-12
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			: HTC Control 
 *
 *		좀더 쉽게 htc컨트롤을 사용해보자.
 *		이쁜 셀렉트 박스를 생성한다. HTC Control을 이용해서 사용
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