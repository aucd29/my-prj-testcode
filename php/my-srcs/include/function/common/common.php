<?php
function msg($message, $url=NULL, $pos='self')
{
	$string = "<script language='javascript'>window.alert (\"$message\");";
	if(!$url)
		$string .= "history.back();";
	else if($url == 'close')
		$string .= "self.close();";
	else if($url == 'none') {
	} else
		$string .= "$pos.location.href=\"$url\";";

	$string .= "</script>";
	echo $string;
	if($url != 'none') exit;
}

function closed($type=NULL)
{
	$str = "<script language='javascript'> self.close(); ";
	if( $type==1 ) $str .= "opener.location.reload();";
	else $str .="opener.$type.submit();";
	$str .="</script>";
	echo $str;
	exit;
}

function go($url, $time=0, $isReturn=FALSE)
{
	$string = "<meta http-equiv='Refresh' content='$time; URL=$url'>";
	if ($isReturn)
		return $string;
	else
		echo $string;
	exit;
}


function printR(&$mixed, $bExit = FALSE)
{
	echo "<pre>";
	print_r($mixed);
	echo "</pre>";

	if($bExit) exit;
}

function expDate($date, $his=NULL)
{
	if(!$his)
	{
		ereg("([0-9]{4})+([0-9]{2})+([0-9]{2})",$date,$aD);
		return "$aD[1]-$aD[2]-$aD[3]";
	}
	else
	{
		ereg("([0-9]{4})+([0-9]{2})+([0-9]{2}+([0-9]{2}+([0-9]{2}+([0-9]{2})",$date,$aD);
		return "$aD[1]-$aD[2]-$aD[3] $aD[4]:$aD[5]:$aD[6]";
	}
}

function expTime($nTime, $his=NULL)
{
	return (!$his)?strftime("%Y-%m-%d", $nTime):strftime("%Y-%m-%d (%H:%M)", $nTime);
}

?>