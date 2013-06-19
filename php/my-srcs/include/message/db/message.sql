CREATE TABLE `message` (
`no` int(11) NOT NULL auto_increment,
`sFrom` varchar(30) NOT NULL default '',
`sTo` varchar(30) NOT NULL default '',
`sTitle` varchar(120) NOT NULL default '',
`sContent` text NOT NULL,
`wdate` varchar(8) NOT NULL default '0',
`nRead` tinyint(1) NOT NULL default '0',
PRIMARY KEY  (`no`)
) TYPE=MyISAM;

CREATE TABLE `message_wish` (
`no` int(11) NOT NULL auto_increment,
`sMyID` varchar(20) NOT NULL default '',
`sName` varchar(20) NOT NULL default '',
`sMemberID` varchar(20) NOT NULL default '',
PRIMARY KEY  (`no`)
) TYPE=MyISAM;