<?php
/***************************************************************************
 *
 *		Date			: 2004-11-01
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
require $_SELF[FUN].'/sql/query_engin.php';
require $_SELF[INC].'/dbconn.php3';

$sql = "
CREATE TABLE `member` (
  `no`			int(10)			unsigned NOT NULL auto_increment,
  `sName`		varchar(15)		NOT NULL default '',
  `sID`			varchar(15)		NOT NULL default '',
  `sPasswd`		varchar(15)		NOT NULL default '',
  `nJumin`		char(6)			NOT NULL default '',
  `nJumin1`		char(7)			NOT NULL default '',
  `nBirthDay`	varchar(8)		NOT NULL default '',
  `nLunar`		tinyint(1)		NOT NULL default '0',
  `nSex`		tinyint(1)		NOT NULL default '0',
  `sEmail`		varchar(60)		NOT NULL default '',
  `sURL`		varchar(100)	NOT NULL default '',
  `sJob`		varchar(50)		NOT NULL default '',
  `sPcs`		varchar(15)		NOT NULL default '',
  `sHAddr`		varchar(120)	NOT NULL default '',
  `sHAddr2`		varchar(100)	NOT NULL default '',
  `nZip`		char(3)			NOT NULL default '',
  `nZip1`		char(3)			NOT NULL default '',
  `sTel`		varchar(15)		NOT NULL default '',
  `sCompany`	varchar(100)	NOT NULL default '',
  `sTeam`		varchar(100)	NOT NULL default '',
  `sCAddr`		varchar(120)	NOT NULL default '',
  `sCAddr2`		varchar(100)	NOT NULL default '',
  `nCZip`		char(3)			NOT NULL default '',
  `nCZip1`		char(3)			NOT NULL default '',
  `sCTel`		varchar(15)		NOT NULL default '',
  `wdate`		varchar(10)		NOT NULL default '',
  `sUserLevel`	varchar(10)		NOT NULL default '',
  `sUpload`		varchar(15)		NOT NULL default '',
  PRIMARY KEY  (`no`),
  KEY `sID` (`sID`,`sPasswd`)
) TYPE=MyISAM AUTO_INCREMENT=1 ;";

sql_query($sql);
//sql_query("INSERT INTO member VALUES (1, '관리자', 'damota', '33281', '111111', '11111111', '19111212', '0', '0', 'w3master@mota.co.kr', '', '', '', '광주광역시 북구 두암1동 동강대학', '500', '714', '062-528-8202', '인터코리아', '', '', '', '', '', 1062831886, 'admin')");

msg('생성되었습니다.', './config_write.php');
?>