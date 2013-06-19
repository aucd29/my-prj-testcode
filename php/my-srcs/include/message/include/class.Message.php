<?php
// +----------------------------------------------------------------------+
// | 2004-02-10 | 쪽지 기능 만들기                                        |
// +----------------------------------------------------------------------+
// | Authors: Kurome <aucd29@kornet.net>                                  |
// +----------------------------------------------------------------------+

class Message
{
	var $objDB;
	var $sTable = 'message';

	var $sTo;
	var $sFrom;
	var $sContent;
	var $sTitle;

	function Message()
	{
		$this->objDB     = $GLOBALS[objDB];
	}

	function setData(&$aPost)
	{
		foreach($aPost as $index => $value) $this->$index = strip_tags($value,"<b><font>");
	}

	function msgInsert()
	{
		if(eregi(',',$this->sTo))
			$this->aTo();
		else
			$this->dbInsert();
	}

	function aTo()
	{
		$aTo = explode(',', $this->sTo);
		foreach($aTo as $value) $this->dbInsert($value);
	}

	function dbInsert($sTo = null)
	{
		$sTo = $sTo ? $sTo : $this->sTo;

		$aPost = array(
			'sFrom'    => $_COOKIE[member_id],
			'sTo'      => $sTo,
			'sTitle'   => $this->sTitle,
			'sContent' => $this->sContent,
			'wdate'    => date(Ymd),
		);

		$this->objDB->insert($this->sTable, $aPost);

		// ------------------------------------------------------------------------
		// 파일을 만들고 이 파일이 있으면 새 메시지가 있는걸로 간주하자.
		// ------------------------------------------------------------------------
		touch("./data/$sTo");
	}


	function addFriend($chkBox)
	{
		$i = 0;
		foreach($chkBox as $index => $value)
		{
			$aData = explode('|', $value);
			if(!$this->objDB->getCount("message_wish", "sMyID='$_COOKIE[member_id]' AND sMemberID='$aData[0]'"))
			{
				$this->objDB->query("INSERT INTO message_wish(sMyID,sName,sMemberID) VALUES('$_COOKIE[member_id]','$aData[1]','$aData[0]')");
				$i++;
			}
		}

		return $i;
	}
}

if(!$objMsg)
{
	$objMsg = & new Message;
}
?>