<?php
/***************************************************************************
 *
 *		Date			: 2004-11-08
 *		Copyright		: Kurome
 *		E-mail			: aucd29@daum.net
 *		Type			: Class
 *
 *		$sUploadRoot	= $_SELF[UPD].'/';
 *		$aThumb			= array(300,300);
 *		require $_SELF[CLS].'/class.Upload3.php';
 *		$_POST[sUpload] = $objUpload->Upload($_FILES[sUpload]); // $_POST[sOldFile], $_POST[sDelFile]
 *
 ***************************************************************************/

Class Upload3
{
	// upload
	var $nFilesize;
	var $sFileName;
	var $sTempFile;
	var $sExtension;
	var $sUploadType;
	var $sNameType = 'time';
	var $sUploadRoot;
	var $sAbsFullpath;

	// file type
	var $sDenyType  = 'php|cgi|pl|htm';
	var $sAllowType = 'gif|jpg|jpeg|png';

	// thumbnail
	var $aThumb;

	// modify
	var $sOldFile;
	var $sOldDel;

	function Upload3()
	{
		$this->sUploadRoot = $GLOBALS[sUploadRoot];
		$this->aThumb	   = $GLOBALS[aThumb];
		if(!is_dir($this->sUploadRoot)) @mkdir($this->sUploadRoot, 0777);
	}

	function &Extension()
	{
		$tmp = pathinfo($this->sFileName);
		$this->sExtension = strtolower($tmp[extension]);
	}

	function isUploaded()
	{
		if(!is_uploaded_file($this->sTempFile)) $this->msg('is_uploaded_file error');
	}

	function FileTypeCheck()
	{
		if(ereg('image',$this->sUploadType))
		{
			if(!ereg($this->sExtension,$this->sAllowType))
				$this->msg('uploaded allow type : '.$this->sAllowType);
		}
		else
		{
			if(ereg($this->sExtension,$this->sDenyType))
				$this->msg('uploaded deny type : '.$this->sDenyType);
		}
	}

	function FileNameCheck()
	{
		if(is_dir(!$this->sUploadRoot)) @mkdir($this->sUploadRoot, 0777);
		// 파일명이 time이면 타임 형식으로다가 이름을 리턴해준다.
		$sName = ($this->sNameType == 'time')?time():basename($this->sFileName,".$this->sExtension");
		$sFile = "$this->sUploadRoot/$sName.$this->sExtension";

		// 동일 파일명 검사
		$i = 0;
		do
		{
			if($i) $sFile = sprintf("%s/%s(%d).%s", $this->sUploadRoot, $sName, $i, $this->sExtension);
			++$i;
		}
		while(is_file($sFile));
		$this->sAbsFullpath = $sFile;
	}

	function Upload(&$sFiles,$sOldFile=null,$sOldDel=null)
	{
		if($sFiles[name])
		{
			$this->nFilesize   = $sFiles[size];
			$this->sFileName   = $sFiles[name];
			$this->sTempFile   = $sFiles[tmp_name];
			$this->sUploadType = $sFiles[type];
			$this->Extension();
			$this->isUploaded();
			$this->FileTypeCheck();
			$this->FileNameCheck();
			$this->MoveUpload();
			if($this->aThumb) $this->Thumbnail();

			$sRtn = basename($this->sAbsFullpath);
			$this->RemoveOldFile($sOldFile,$sOldDel);

			return $sRtn;
		}
		else if($sOldFile && !$sFiles[name])	// 아무파일없이 그냥들어올때 이전걸로
			return $this->RemoveOldFile($sOldFile,$sOldDel);
		else
			return '';
	}

	function MUpload(&$sFiles,$sOldFile=null,$sDelFile=null)
	{
		$sRtn = '';
		$nCnt = count($sFiles[size]);
		for($i=0; $i<$nCnt; ++$i)
		{
			$sFile[tmp_name] = $sFiles[tmp_name][$i];
			$sFile[name]	 = $sFiles[name][$i];
			$sFile[size]	 = $sFiles[size][$i];
			$sFile[type]	 = $sFiles[type][$i];
			$sRtn			.= $this->Upload($sFile,$sOldFile,$sDelFile).'|';
		}
		return $sRtn;
	}

	function RemoveOldFile($sOldFile=null,$sOldDel=null)
	{
		if($sOldFile&&$sOldDel)
		{
			@unlink("$this->sUploadRoot/$sOldFile");
			if($this->sThumb) @unlink("$this->sUploadRoot/thumb/$sOldFile");
			return '';
		}
		else return $sOldFile;
	}

	function MoveUpload()
	{
		if(!move_uploaded_file($this->sTempFile,$this->sAbsFullpath)) $this->msg('false uploaded');
	}

	function msg(&$msg)
	{
		echo"<script> alert('$msg'); history.back(); </script>";
		exit;
	}

	function Thumbnail()
	{
		$sTRoot = $this->sUploadRoot.'/thumb';
		if(!is_dir($sTRoot)) @mkdir($sTRoot ,0777);

		$aImg = getimagesize($this->sAbsFullpath);
		$nW = $aImg[0];
		$nH = $aImg[1];

		if($nW > $nH)
		{
			$nH2 = $nH>$this->aThumb[1]?$this->aThumb[1]:$nH;
			$nW2 = $nH2*$nW/$nH;
		}
		else
		{
			$nW2 = $nW>$this->aThumb[0]?$this->aThumb[0]:$nW;
			$nW2 = $nH*$nW2/$nW;
		}

		$nW = round($nW2);
		$nH = round($nH2);

		if(ereg($this->sExtension,'jpg|jpeg'))
			$tmpImage = imagecreatefromjpeg($this->sAbsFullpath);
		else if($this->sExtension == 'gif')
			$tmpImage = imagecreatefromgif($this->sAbsFullpath);
		else
			$tmpImage = imagecreatefrompng($this->sAbsFullpath);

		$Image  = imageCreateTrueColor($this->aThumb[0], $this->aThumb[1]);
		$f7f7f7 = ImageColorAllocate($Image, 247,247,247);
		ImageFilledRectangle($Image, 0, 0, $this->aThumb[0], $this->aThumb[1], $f7f7f7); //채운 흰색사각형을 그린다.

		$go_x = round(($this->aThumb[0] - $nW)/2);
		$go_y = round(($this->aThumb[1] - $nH)/2);

		imageCopyReSampled($Image, $tmpImage, $go_x, $go_y, 0, 0, $nW, $nH, $aImg[0], $aImg[1]);
		$Thumbnail = $sTRoot.'/'.basename($this->sAbsFullpath);

		if(ereg($this->sExtension,'jpg|jpeg'))
			imagejpeg($Image, $Thumbnail);
		else if($this->sExtension == 'gif') //imagegif($Image, $Thumbnail);
			imagepng($Image, $Thumbnail);	// 2004-07-22 서버에 안깔려있다.
		else
			imagepng($Image, $Thumbnail);

		imagedestroy($Image);
		//echo $this->sAbsFullpath;
		//echo '<br>';
		///print_r($this->aThumb);
		//print_r($aImg);
		//exif_thumbnail. 5.0
	}
}

if(!$objUpload) $objUpload = & new Upload3;
?>