<?php
function Thumbnail($sOrigin, $sThumbRoot, $thumbW, $thubmH )
{
	if(!is_dir( $sThumbRoot )) @mkdir( $sThumbRoot ,0777 );
	$aImg = getimagesize($sOrigin);
	$aPathInfo = pathinfo($sOrigin);

	// ------------------------------------------------------------------------
	// 생님꺼 추가
	// ------------------------------------------------------------------------
	$width  = $aImg[0];
	$height = $aImg[1];

	if($width > $height) {
		$height2 = ($height > $thubmH) ? $thubmH : $height;
		$width2=($height2*$width) / $height;
	} else {
		$width2 = ($width > $thumbW) ? $thumbW : $width;
		$height2=($height*$width2) / $width;
	}

	$width  = round($width2);
	$height = round($height2);

	if( $aPathInfo[extension] == 'jpg'|| $aPathInfo[extension]=='jpeg')
		$tmpImage = imagecreatefromjpeg( $sOrigin );
	else if( $aPathInfo[extension] == 'gif' ) 
		$tmpImage = imagecreatefromgif( $sOrigin );
	else
		$tmpImage = imagecreatefrompng( $sOrigin );
	
	$Image = imageCreateTrueColor($thumbW, $thubmH);
	$f7f7f7 = ImageColorAllocate($Image, 247,247,247);
	ImageFilledRectangle($Image, 0, 0, $thumbW, $thubmH, $f7f7f7); //채운 흰색사각형을 그린다.

	$go_x = round(($thumbW - $width) / 2);
	$go_y = round(($thubmH - $height)/ 2);

	imageCopyReSampled( $Image, $tmpImage, $go_x, $go_y, 0, 0, $width, $height, $aImg[0], $aImg[1] );

	
	
	$basename = str_replace('.'.$aPathInfo[extension],'.png',$aPathInfo[basename]);

	$Thumbnail = $sThumbRoot.'/'.$aPathInfo[basename];
	if(imagepng( $Image, $Thumbnail ))
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