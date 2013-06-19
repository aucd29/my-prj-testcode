<?php
/***************************************************************************
 *
 *		Date			: 2004-07-02
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			: Function
 *
 *		이미지 관련 평션
 *
 ***************************************************************************/

// 상대 경로로 들어오면 고맙지.
function ShowImage($sImgPath,$nW=null,$nH=null,$nThumb=null,$sNoImg=null)
{
	if(is_file($sImgPath))
	{
		if($nThumb)
		{
			if(!$nW&&!$nH)
			{
				echo"
				<script> alert('가로,세로값을 넣지 않았습니다. 썸네일을 만들려면 가로세로가 필요합니다.'); history.back(); </script>";
				exit;
			}
			$sFile	= BaseName($sImgPath);
			$sDir	= DirName($sImgPath).'/thumb';
			if(Thumbnail(&$sImgPath, $sDir, $nW, $nH))
				echo "<img src='$sDir$sFile' width='$nW' height='$nH'>";
		}
		else
		{
			if($nW && $nH)
				$aImgS = GoodImgSize(&$sImgPath,$nW,$nH);
			else
				$aImgS = GetImageSize($sImgPath);

			echo "<img src='$sImgPath' class='hand' $aImgS[3] />";
		}
	}
	else
	{
		$nW = $nW?$nW:'100%';
		if($sNoImg)
			echo "<img src='$sNoImage' onFocus='this.blur()' />";
		else
			echo "<table class='border' width='$nW'><tr><td align='center'>준비중</td></tr></table>";
	}
}

function GoodImgSize($sImgPath,$nW,$nH)
{
	$aImgS = GetImageSize($sImgPath);
	if($aImgS[0] <= $nW and $aImgS[1] <= $nW)	// 이미지 사이즈가 원하는 사이즈보다 작으면은 그냥 출력
	{
		$rtn[0] = $aImgS[0];
		$rtn[1] = $aImgS[1];
		$rtn[3] = $aImgS[3];
	}
	else
	{
		$width  = $aImgS[0];
		$height = $aImgS[1];

		if($width > $height) {
			$height2 = ($height > $nW) ? $nW : $height;
			$width2=($height2*$width) / $height;
		} else {
			$width2 = ($width > $nH) ? $nH : $width;
			$height2=($height*$width2) / $width;
		}

		$rtn[0] = round($width2);
		$rtn[1] = round($height2);
		$rtn[3] = "width='$rtn[0]' height='$rtn[1]'";
	}

	return $rtn;
}

function Thumbnail($sOrigin, $sThumbRoot, $thumbW, $thubmH )
{
	if(!is_dir($sThumbRoot)) @mkdir($sThumbRoot ,0777);
	$aImg		= getimagesize($sOrigin);
	$aPathInfo  = pathinfo($sOrigin);

	$aData = GoodImgSize($sOrigin,$thumbW,$thubmH);
	$width = $aData[0];
	$height = $aData[1];

	if($aPathInfo[extension] == 'jpg'|| $aPathInfo[extension]=='jpeg')
		$tmpImage = imagecreatefromjpeg($sOrigin);
	else if($aPathInfo[extension] == 'gif')
		$tmpImage = imagecreatefromgif($sOrigin);
	else
		$tmpImage = imagecreatefrompng($sOrigin);

	$Image  = imageCreateTrueColor($thumbW, $thubmH);
	$f7f7f7 = ImageColorAllocate($Image, 247,247,247);
	ImageFilledRectangle($Image, 0, 0, $thumbW, $thubmH, $f7f7f7); //채운 흰색사각형을 그린다.

	$go_x = round(($thumbW - $width) / 2);	// 가운데서 자르고
	$go_y = round(($thubmH - $height)/ 2);
	imageCopyReSampled($Image, $tmpImage, $go_x, $go_y, 0, 0, $width, $height, $aImg[0], $aImg[1]);	// 만든다.

	$Thumbnail = $sThumbRoot.'/'.$aPathInfo[basename];
	if(imagepng($Image, $Thumbnail))
	{
		imagedestroy($Image);
		return TRUE;
	}
	else
	{
		return FALSE;
	}
}
?>