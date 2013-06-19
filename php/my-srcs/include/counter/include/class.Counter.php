<?php
class cnt
{
	var $wdate = array();
	var $sDir = './data/';
	var $sDB;
	var $sFile;
	var $aDT;
	var $Br;
	var $Os;

	// ������.
	function cnt()
	{
		$this->wdate = array('y'=>date(Y),'m'=>date(m),'d'=>date(d));
		$this->sDB = $_GET[db];
		$this->IsFileChk();
		$this->Increment();
	}

	function IsFileChk()
	{
		$sThis = $this->sDir.$this->sDB.'/'.$this->wdate[y].'/'.$this->wdate[m];

		if(!is_file($sThis))
		{
			print(`mkdir $sThis -p`);
			$this->sFile = $sThis.'/'.$this->wdate[d].".php";
			$fp = fopen($this->sFile, w);

			//0	: ���ÿ¼�
			//���� �ð��� ����Ÿ
			fwrite($fp, "0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0|0");
			fclose($fp);
		}
	}

	// ��������.
	function Increment()
	{
		$this->DataExplode();
		$this->aDT[0]++;
		$this->TimeCheck();
		$this->WriteData();
		$this->AgentInfo();
		$this->Browser();
		$this->OperationgSystem();
		$this->InternetProtocol();
		$this->WeekCheck();
	}

	// �����͸� ��������.
	function DataExplode()
	{
		$sFilename  = $this->sFile;
		$fp			= fopen($sFilename, r);
		if(flock($fp, LOCK_EX))
		{
			$nFilesize	= filesize($sFilename);
			$sRead		= fread($fp, $nFilesize);
			flock($fp, LOCK_UN);
		}
		else
		{
			$this->DataExplode();
		}
		fclose($fp);
		$this->aDT = explode('|', $sRead);
		unset($sRead);
	}

	// �ð��� �°� �÷�����.
	function TimeCheck()
	{
		$h = date(h);
		$this->aDT[$h]++;
	}

	// �ۼ� ���� �ɰ�
	function WriteData()
	{
		$fp = fopen($this->sFile, w);
		fwrite($fp, implode('|', $this->aDT));
		fclose($fp);
	}

	//HTTP_USER_AGENT  Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)
	function AgentInfo()
	{
		$aAgentInfo = explode('(compatible;',$_SERVER[HTTP_USER_AGENT]); //[0] => Mozilla/4.0 [1] => MSIE 6.0; Windows NT 5.1) 
		$aAgentInfo2 = explode(';', $aAgentInfo[1]);

		$this->Br = trim($aAgentInfo2[0]);
		$this->Os = trim(str_replace(')','',$aAgentInfo2[1]));
	}

	function IncrementFile($dir,$file)
	{
		if(!is_file($dir.'/'.$file))
		{
			$fp = fopen($dir.'/'.$file, w);
			fwrite($fp, "1");
			fclose($fp);
		}
		else
		{
			$sFilename  = $dir.'/'.$file;
			$fp			= fopen($sFilename, r);
			$nFilesize	= filesize($sFilename);
			$sRead		= fread($fp, $nFilesize);
			fclose($fp);

			$fp = fopen($sFilename, w);
			++$sRead;
			fwrite($fp, $sRead);
			fclose($fp);
		}
	}

	function Browser()
	{
		$sThis = $this->sDir.$this->sDB.'/br';
		$this->IncrementFile($sThis,$this->Br);		
	}

	function OperationgSystem()
	{
		$sThis = $this->sDir.$this->sDB.'/os';
		$this->IncrementFile($sThis,$this->Os);
	}

	function InternetProtocol()
	{
		$sThis = $this->sDir.$this->sDB.'/ip';
		$this->IncrementFile($sThis,$_SERVER[REMOTE_ADDR]);
	}

	function WeekCheck()
	{
		$sThis = $this->sDir.$this->sDB.'/week';
		$this->IncrementFile($sThis,date(w,time()));
	}
}

if(!$objCnt) $objCnt = & new cnt;
?>