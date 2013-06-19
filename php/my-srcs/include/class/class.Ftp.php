<?php
// +----------------------------------------------------------------------+
// | 2004-02-03 | ftp 클래스                                              |
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
	// 변수 초기화
	// ------------------------------------------------------------------------
	function setData(&$aData)
	{
		foreach($aData as $index => $value)
		{
			$this->$index = $value;
		}
	}

	// ------------------------------------------------------------------------
	// ftp 접속
	// ------------------------------------------------------------------------
	function connect()
	{
		$this->ftpConn = ftp_connect($this->sHost, $this->sPort);

		// 패시브 모드 체킹
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
	// ftp 닫기
	// ------------------------------------------------------------------------
	function closed()
	{
		if($this->ftpConn)
		{
			ftp_quit($this->ftpConn);
		}
	}


	// ------------------------------------------------------------------------
	// ftp실제 로그인하기
	// ------------------------------------------------------------------------
	function ftpLogin()
	{
		if(!$this->sUser || !$this->sPasswd)
		{
			$this->objCommon->msg('아이디나 패스워드가 없네요', null); // close, none
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
	// 현재의 대렉토리 구하기
	// ------------------------------------------------------------------------
	function ftpDir()
	{
		$rs = ftp_pwd($this->ftpConn);

		if(!$rs)
		{
			$this->objCommon->msg('현재 디렉토리 정보를 구할수 없습니다.', null); // close, none
		}
		else
		{
			return $rs;
		}
	}


	// ------------------------------------------------------------------------
	// 디렉토리 생성
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
			$this->objCommon->msg('생성할 디렉토리 명을 넣어주세요', null); // close, none
		}
	}

	// ------------------------------------------------------------------------
	// 디렉토리 삭제
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
			$this->objCommon->msg('삭제할 디렉코리 명을 넣어주세요', null); // close, none
		}
	}

	// ------------------------------------------------------------------------
	// 파일 삭제
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
	// 파일 업로드
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
	// 파일 리스트 가져오기
	// ------------------------------------------------------------------------
	function getList($sDir)
	{
		$sFtpdir = ftp_rawlist($this->ftpConn, $sDir);
	}
}
?>