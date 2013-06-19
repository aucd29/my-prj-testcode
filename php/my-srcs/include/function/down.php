<?php
// +----------------------------------------------------------------------+
// | function download
// +----------------------------------------------------------------------+
// | Authors: Kurome <aucd29@kornet.net>								  |
// +----------------------------------------------------------------------+


function download($szFile)
{
	$arrPathInfo = pathinfo($szFile);

	if(eregi("msie", $_SERVER[HTTP_USER_AGENT]) && eregi("5\.5", $_SERVER[HTTP_USER_AGENT]))
	{
		header("content-type: doesn/matter");
		header("content-length: ".filesize($szFile));
		header("content-disposition: attachment; filename=$arrPathInfo[basename]");
		header("content-transfer-encoding: binary");
	}
	else
	{
		header("content-type: file/unknown");
		header("content-length: ".filesize($szFile));
		header("content-disposition: attachment; filename=$arrPathInfo[basename]");
		header("content-description: php generated data");
	}
	header("Pragma:no-cache");
	header("Expires:0");

	if(is_file($szFile))
	{
		$fp = @fopen($szFile, "rb");

		if(!$fp)
			return FALSE;
		else
			fpassthru($fp);
		fclose($fp);
	}
	else
	{
		echo"
		<script> alert('해당위치에 파일이 존재하지 않습니다.'); history.back(); </script>";
		exit;
	}	
}
?>