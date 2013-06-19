<?php
class Filecontrol
{
	var $sFilename;

	// ------------------------------------------------------------------------
	// 파일의 기준 인덱스를 설정하는 파일 경로를 지정하면 list.cgi로 생성
	// ------------------------------------------------------------------------
	function number($sRoot,$number=null)
	{
		$sList = $sRoot.'/list.cgi';
		if(!is_file($sList))
		{
			$fp = fopen($sList, w);
			if(flock($fp, LOCK_EX))
			{
				fwrite($fp, "1\n");
				flock($fp, LOCK_UN);
			}
			else $this->msg('처리를 할수 없습니다. 다시한번 실행해주세요');
			fclose($fp);
			$sFilename = 1;
		}
		else
		{
			$aList		= file($sList);
			$sFilename	= $number?$number:chop($aList[0]) + 1;	// 직접 들어온 번호가 존재하면 그걸로 하고 아니면 ++ 한다.

			$fp			= fopen($sList, "w");
			if(flock($fp, LOCK_EX))
			{
				fwrite($fp, "$sFilename\n");
				foreach($aList as $value) fwrite($fp, $value);
				flock($fp, LOCK_UN);
			}
			else $this->msg('처리를 할수 없습니다. 다시한번 실행해주세요');
			fclose($fp);
		}
		$this->sFilename = $sFilename;
	}

	function insert($sSaveRoot, &$aData,$number=null)
	{
		$this->number($sSaveRoot,&$number);
		$aData[no]    = $this->sFilename;
		$aData[wdate] = date(Ymd);

		$sReturnTxt .= "<?php\n";
		foreach($aData as $item => $value) $sReturnTxt .= "$"."$item = \"$value\"; \n";
		$sReturnTxt .= "?>";

		$fp = fopen($sSaveRoot.'/'.$this->sFilename.'.php', w);
		if(flock($fp, LOCK_EX))
		{
			fputs($fp, $sReturnTxt);
			flock($fp, LOCK_UN);
		}
		else $this->msg('처리를 할수 없습니다. 다시한번 실행해주세요');
		fclose($fp);
	}

	function insert2($sSaveRoot, &$aData, $sFileName)
	{
		$aData[wdate] = date(Ymd);

		$sReturnTxt .= "<?php\n";
		foreach( $aData as $item => $value )$sReturnTxt .= "$"."$item = \"$value\"; \n";
		$sReturnTxt .= "?>";

		$fp = fopen("$sSaveRoot/$sFileName.php", w);
		if(flock( $fp, LOCK_EX))
		{
			fputs($fp,$sReturnTxt);
			flock($fp, LOCK_UN);
		}
		else $this->msg('처리를 할수 없습니다. 다시한번 실행해주세요');
		fclose($fp);
	}

	function modify($sSaveRoot, &$aData, $sFilename)
	{
		$sReturnTxt .= "<?\n";
		foreach($aData as $item => $value) $sReturnTxt .= "$"."$item = \"$value\"; \n";
		$sReturnTxt .= "\n?>";

		$fp = fopen("$sSaveRoot/$sFilename.php", w);
		if(flock($fp, LOCK_EX))
		{
			fputs($fp,$sReturnTxt);
			flock($fp, LOCK_UN);
		}
		else $this->msg('처리를 할수 없습니다. 다시한번 실행해주세요');
		fclose($fp);
	}

	function delete($nNO, $sSaveRoot = './data')
	{
		$sList = "$sSaveRoot/list.cgi";
		$aList = file($sList); //리스트 파일에서 번호 지우기

		$fp = fopen($sList, "w");
		if(flock($fp, LOCK_EX))
		{
			foreach($aList as $value)
			{
				$value = trim($value);
				if($nNO != $value) fwrite($fp, "$value\n");
			}
			flock($fp, LOCK_UN);
		}
		else $this->msg('처리를 할수 없습니다. 다시한번 실행해주세요');
		fclose($fp);

		if(@unlink("$sSaveRoot/$nNO.php"))	return TRUE;
		else								return FALSE;
	}

	function delDir($dir)
	{
		$current_dir = @opendir($dir);
		while($entryname = @readdir($current_dir))
		{
			if(is_dir("$dir/$entryname") and ($entryname != "." and $entryname!=".."))
			{
				$this->delDir("${dir}/${entryname}");
			}
			else if($entryname != "." and $entryname!="..")
			{
				@unlink("${dir}/${entryname}");
			}
		}
		@closedir($current_dir);
		@rmdir(${dir});
	}

	// 2003-12-06 현재 원하는 디렉토리의 퍼미션 가져오기
	function getPermition($sObj)
	{
		return substr(decoct(fileperms($sObj)),2,3);
	}

	// 2003-12-06 지금 쓰고있는 디렉토리를 리턴
	function getSelfDir($sRoot)
	{
		$sBasename = basename($sRoot);
		$RTN = str_replace('/'.$sBasename, '', $sRoot);
		return $RTN;
	}

	function msg($msg)
	{
		echo"
		<script> alert('$msg'); history.back(); </script>";
		exit;
	}
}

if(!$objFctl) $objFctl = & new Filecontrol;
?>