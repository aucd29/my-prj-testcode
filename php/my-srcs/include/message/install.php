<?php
/***************************************************************************
 *
 *		Date			: 2004-11-10
 *		Copyright		: Kurome
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			:
 *
 *
 *
 ***************************************************************************/

//
// Default Setting
//
require '../../global.php';

//
// Function
//
require $_SELF[FUN].'/common/common.php';
require $_SELF[INC].'/dbconn.php3';
require $_SELF[FUN].'/sql/query_engin.php';

if(!is_dir('./data'))
{
	echo "디렉토리를 생성하게";
}
else
{
	if(!is_file("./data/ok.php"))
	{
		$sFilename  = './db/message.sql';
		$fp			= fopen($sFilename, r);
		$nFilesize	= filesize($sFilename);
		$sql		= fread($fp, $nFilesize);
		fclose($fp);
		$sql = "CREATE TABLE `message` (
		`no` int(11) NOT NULL auto_increment,
		`sFrom` varchar(30) NOT NULL default '',
		`sTo` varchar(30) NOT NULL default '',
		`sTitle` varchar(120) NOT NULL default '',
		`sContent` text NOT NULL,
		`wdate` varchar(8) NOT NULL default '0',
		`nRead` tinyint(1) NOT NULL default '0',
		PRIMARY KEY  (`no`)
		) TYPE=MyISAM;";
		sql_query($sql);

		$sql = "CREATE TABLE `message_wish` (
		`no` int(11) NOT NULL auto_increment,
		`sMyID` varchar(20) NOT NULL default '',
		`sName` varchar(20) NOT NULL default '',
		`sMemberID` varchar(20) NOT NULL default '',
		PRIMARY KEY  (`no`)
		) TYPE=MyISAM;";
		sql_query($sql);
		touch("./data/ok.php"); // 인스톨 햇다고 모양 남기자.
		echo "ok complete";
	}
	else echo '이미 설치했다 아이가';
}
?>