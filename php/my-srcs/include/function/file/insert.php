<?php
function insert($sSaveRoot, &$aData, $sFileName)
{
	$aData[wdate] = date(Ymd);
	$sReturnTxt .= "<?php\n";
	foreach($aData as $item => $value) $sReturnTxt .= "$"."$item = \"$value\"; \n";
	$sReturnTxt .= "?>";
	
	$fp = fopen("$sSaveRoot/$sFileName.php", w);
	if(flock($fp, LOCK_EX))
	{
		fputs($fp,$sReturnTxt);
		flock($fp, LOCK_UN);
	}
	else
	{
		echo"<script> alert('처리를 할수 없습니다. 다시한번 실행해주세요'); history.back(); </script>";
		exit;
	}
	fclose($fp);

	if(!$fp)
	{
		echo"<script> alert('입력에러'); history.back(); </script>";
		exit;
	}
}
?>