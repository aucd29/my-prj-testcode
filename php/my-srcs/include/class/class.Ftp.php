<?php
// +----------------------------------------------------------------------+
// | 2004-02-03 | ftp Ŭ����                                              |
// +----------------------------------------------------------------------+
// | Authors: Kurome <aucd29@kornet.net>                                  |
// +----------------------------------------------------------------------+

class FTP
{
	var $ftpConn;
	var $sHost = 'localhost';
	var $sPort = 21;
	var $sUser;
	var $sPasswd;
	var $isPassive;

	var $objDB;
	var $objCommon;

	function FTP()
	{
		$this->objCommon = $GLOBALS[objCommon];
	}

	// ------------------------------------------------------------------------
	// ���� �ʱ�ȭ
	// ------------------------------------------------------------------------
	function setData(&$aData)
	{
		foreach($aData as $index => $value)
		{
			$this->$index = $value;
		}
	}

	// ------------------------------------------------------------------------
	// ftp ����
	// ------------------------------------------------------------------------
	function connect()
	{
		$this->ftpConn = ftp_connect($this->sHost, $this->sPort);

		// �нú� ��� üŷ
		if($this->isPassive)
		{
			$this->setPassive();
		}


		if($this->ftpLogin())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	function setPassive()
	{
		@ftp_pasv($this->ftpConn, true);
	}

	// ------------------------------------------------------------------------
	// ftp �ݱ�
	// ------------------------------------------------------------------------
	function closed()
	{
		if($this->ftpConn)
		{
			ftp_quit($this->ftpConn);
		}
	}


	// ------------------------------------------------------------------------
	// ftp���� �α����ϱ�
	// ------------------------------------------------------------------------
	function ftpLogin()
	{
		if(!$this->sUser || !$this->sPasswd)
		{
			$this->objCommon->msg('���̵� �н����尡 ���׿�', null); // close, none
		}

		$res = ftp_login($this->ftpConn, $this->sUser, $this->sPasswd);
		if($res)
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	// ------------------------------------------------------------------------
	// ������ �뷺�丮 ���ϱ�
	// ------------------------------------------------------------------------
	function ftpDir()
	{
		$rs = ftp_pwd($this->ftpConn);

		if(!$rs)
		{
			$this->objCommon->msg('���� ���丮 ������ ���Ҽ� �����ϴ�.', null); // close, none
		}
		else
		{
			return $rs;
		}
	}


	// ------------------------------------------------------------------------
	// ���丮 ����
	// ------------------------------------------------------------------------
	function ftpMakeDir($sDir)
	{
		if($sDir)
		{
			if(ftp_mkdir($this->ftpConn, $sDir)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			$this->objCommon->msg('������ ���丮 ���� �־��ּ���', null); // close, none
		}
	}

	// ------------------------------------------------------------------------
	// ���丮 ����
	// ------------------------------------------------------------------------
	function ftpRmdir($sRmdir)
	{
		if($sRmdir)
		{
			if(ftp_rmdir($this->ftpConn, $sRmdir)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			$this->objCommon->msg('������ ���ڸ� ���� �־��ּ���', null); // close, none
		}
	}

	// ------------------------------------------------------------------------
	// ���� ����
	// ------------------------------------------------------------------------
	function ftpDel($sFile)
	{
		if($sFile)
		{
			if(ftp_delete($this->ftpConn, $sFile)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	// ------------------------------------------------------------------------
	// ���� ���ε�
	// ------------------------------------------------------------------------
	function ftpPut($sRemoteFile, $sFile, $nOverwrite = true, $sMode = FTP_BINARY)
	{
		if($sMode != FTP_BINARY)
		{
			$sMode = FTP_ASCII;
		}

		if(ftp_put($this->ftpConn, $sRemoteFile, $sFile, $sMode))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	// ------------------------------------------------------------------------
	// ���� ����Ʈ ��������
	// ------------------------------------------------------------------------
	function getList($sDir)
	{
		$sFtpdir = ftp_rawlist($this->ftpConn, $sDir);
	}
}
?>