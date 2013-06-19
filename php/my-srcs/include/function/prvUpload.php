<?php
function prvUpload($sPage, $nType, $sUpload = NULL, $sID='prevImages', $sUploadRoot="/UploadFiles", $nAbsoluteSize=600)
{
	if(!$sPage)
	{
		echo"<script> alert('sPage'); history.back(); </script>";
		exit;
	}
	if(!$nType)
	{
		echo"<script> alert('nType'); history.back(); </script>";
		exit;
	}

	if($nType == 1)
	{
		if($sUpload)
		{
			if($sUploadRoot=="/UploadFiles")
			{
				$sUploadRoot2 = "/UploadFiles/$sPage";
				$aPathInfo = pathinfo("$_SERVER[DOCUMENT_ROOT]$sUploadRoot2/$sUpload");
				$imgType   = getimagesize("$_SERVER[DOCUMENT_ROOT]$sUploadRoot2/$sUpload");
			}
			else
			{
				$aPathInfo = pathinfo("$sUploadRoot/$sUpload");
				$imgType   = getimagesize("$sUploadRoot/$sUpload");

				$sUploadRoot2 = $sUploadRoot;
			}

			$ext = $aPathInfo[extension];
			if($ext == 'swf')
			{
				echo "<div id='$sID'><embed src='$sUploadRoot2/$sUpload' width='$imgType[0]' height='$imgType[1]' border='0'></div>";
			}
			else
			{
				if($imgType[0]>$nAbsoluteSize)
				{
					$width = $nAbsoluteSize;
					echo "<div id='$sID'><img src='$sUploadRoot2/$sUpload' width='$width' border='0' onClick=\"javascript:showImgWin2('$sUploadRoot2/$sUpload')\" style='cursor:hand' /></div>";
				}
				else
				{
					echo "<div id='$sID'><img src='$sUploadRoot2/$sUpload' $imgType[3] border='0' /></div>";
				}
			}
		}
		else
		{
			echo "<div id='$sID'>&nbsp;</div>";
		}
	}
	else if($nType == 2)
	{
		if($sUpload)
		{
			# 다운로드 파일에 urlencode추가하기
			$sUpload = urlencode($sUpload);
			echo "<a href='/include/download.php?down=$sUpload&sPage=$sPage'>$sUpload</a>";
		}
		else
		{
			echo "&nbsp;";
		}
	}
	else if($nType == 3)
	{
		if($sUpload)
		{
			$sUpload = urlencode($sUpload);
			echo"<embed EnableContextMenu='0' src='$sUploadRoot/$sPage/$sUpload'></embed>";
		}
		else
		{
			echo "&nbsp";
		}
	}
}
?>