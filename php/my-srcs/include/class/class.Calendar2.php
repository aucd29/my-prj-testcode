<?php
class Calendar2
{
	var $nYear;
	var $nMonth;
	var $sOverColor;

	function Calendar2()
	{
		$this->nYear = $GLOBALS[nYear];
		$this->nMonth= $GLOBALS[nMonth];
		$this->sOverColor = $GLOBALS[sOverColor];
	}

	function setData(&$aData)
	{
		foreach($aData as $index => $value) $this->$index = $value;
	}

	// 이번달의 마지막 날을 구한다.
	function getLastDay()
	{
		$this->nLastDay = date(t, mktime(0, 0, 0, $this->nMonth, 1, $this->nYear));
	}

	// 달의 첫번째 날요일구하기
	function getFirstDay()
	{
		$this->nFirstDay = date(w, mktime(0, 0, 0, $this->nMonth, 1, $this->nYear));
	}

	// 년, 월을 변경해주는 함수
	function change($nType)
	{
		$setTime = mktime(0,0,0,$this->nMonth, 1, $this->nYear);
		switch( $nType )
		{
			case('1') : $rtn = strtotime("-1 year", $setTime); break;
			case('2') : $rtn = strtotime("-1 month", $setTime); break;
			case('3') : $rtn = strtotime("+1 month", $setTime); break;
			case('4') : $rtn = strtotime("+1 year", $setTime); break;
		}
		return "nYear=".date(Y,$rtn)."&nMonth=".date(m,$rtn);
	}

	function printFirstBlank()
	{
		for($i=0; $i<$this->nFirstDay; $i++) echo "<td class='calendar_black_td'>&nbsp;</td>";
	}

	function Calendar()
	{
		echo '<tr>';
		$this->getFirstDay();
		$this->printFirstBlank();
		$this->getLastDay();

		$j = $this->nFirstDay+1;
		for($i=1; $i<=$this->nLastDay; $i++)
		{
			$nWeek = $j%7;
			switch($nWeek)
			{
				case('1'):	$sTxtColor = "calendar_sun"; break;
				case('0'):	$sTxtColor = "calendar_sat"; break;
				default:	$sTxtColor = "calendar_nor"; break;
			}
			$day = $i<10?'0'.$i:$i;
			$sTodayColor = (date(Ymd) == $this->nYear.$this->nMonth.$day) ? ' calendar_today' : '';

			echo "<td onMouseOver=this.style.backgroundColor='$this->sOverColor' onMouseOut=this.style.backgroundColor='' class='calendar_td $sTxtColor$sTodayColor'>$i</td>";
			if($nWeek == 0) echo '</tr><tr>';
			++$j;
		}
		--$j;
		while($j%7 != 0)
		{
			echo "<td class='calendar_black_td'></td>";
			++$j;
		}
	}
}
if(!$objCalendar) $objCalendar = & new Calendar2;
?>