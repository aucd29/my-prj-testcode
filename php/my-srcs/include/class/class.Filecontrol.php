<?php
class Filecontrol
{
	var $sFilename;

	// ------------------------------------------------------------------------
	// ������ ���� �ε����� �����ϴ� ���� ��θ� �����ϸ� list.cgi�� ����
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
			else $this->msg('ó���� �Ҽ� �����ϴ�. �ٽ��ѹ� �������ּ���');
			fclose($fp);
			$sFilename = 1;
		}
		else
		{
			$aList		= file($sList);
			$sFilename	= $number?$number:chop($aList[0]) + 1;	// ���� ���� ��ȣ�� �����ϸ� �װɷ� �ϰ� �ƴϸ� ++ �Ѵ�.

			$fp			= fopen($sList, "w");
			if(flock($fp, LOCK_EX))
			{
				fwrite($fp, "$sFilename\n");
				foreach($aList as $value) fwrite($fp, $value);
				flock($fp, LOCK_UN);
			}
			else $this->msg('ó���� �Ҽ� �����ϴ�. �ٽ��ѹ� �������ּ���');
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
		else $this->msg('ó���� �Ҽ� �����ϴ�. �ٽ��ѹ� �������ּ���');
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
		else $this->msg('ó���� �Ҽ� �����ϴ�. �ٽ��ѹ� �������ּ���');
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
		else $this->msg('ó���� �Ҽ� �����ϴ�. �ٽ��ѹ� �������ּ���');
		fclose($fp);
	}

	function delete($nNO, $sSaveRoot = './data')
	{
		$sList = "$sSaveRoot/list.cgi";
		$aList = file($sList); //����Ʈ ���Ͽ��� ��ȣ �����

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
		else $this->msg('ó���� �Ҽ� �����ϴ�. �ٽ��ѹ� �������ּ���');
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

	// 2003-12-06 ���� ���ϴ� ���丮�� �۹̼� ��������
	function getPermition($sObj)
	{
		return substr(decoct(fileperms($sObj)),2,3);
	}

	// 2003-12-06 ���� �����ִ� ���丮�� ����
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