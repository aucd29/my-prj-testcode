<?php
/***************************************************************************
 *
 *		Date			: 2004-06-19
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			: Function
 *
 *		Article New Icon
 *
 ***************************************************************************/

// void newicon($date,$type,$print=null)

function newicon($date,$type=null,$print=null)
{
	if($type=='t') // time
	{
		$date = strtotime("+1 day", $date);	// new icon life time one day
		if($date > time()) $view = true;
	}
	else	// date
	{
		$deny = array('-','[',']',':','(',')','.');
		$date = str_replace($deny,'',$date);
		$len  = strlen($date);

		$aDate[] = substr($date,0,4);
		for($i=4; $i<$len; $i+=2) $aDate[] = substr($date,$i,2);
		$time = mktime($aDate[3],$aDate[4],$aDate[5],$aDate[1],$aDate[2],$aDate[0]);
		$time = strtotime("+1 day", $time);
		if($time > time()) $view = true;
	}

	if($view == true)
	{
		if($print)
			echo "<img src='$print'>";
		else	// text type new
			echo "<span class='tahoma8' style='color:red'>NEW</span>";
	}
}
?>