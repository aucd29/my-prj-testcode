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
		echo"<script> alert('ó���� �Ҽ� �����ϴ�. �ٽ��ѹ� �������ּ���'); history.back(); </script>";
		exit;
	}
	fclose($fp);

	if(!$fp)
	{
		echo"<script> alert('�Է¿���'); history.back(); </script>";
		exit;
	}
}
?>