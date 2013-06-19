<?php
//
// Default Setting
//
require '../../global.php';

//
// Function
//
require $_SELF[FUN].'/common/common.php';
require $_SELF[FUN].'/file/insert.php';
include $_SELF[FUN].'/file/delDir.php';

//
// Class
//
$objCnt = & new CreateCnt;

class CreateCnt
{
	var $db;
	var $psMode;
	var $root = '../data/';

	// construct
	function CreateCnt()
	{
		// data sort
		foreach($_POST as $index => $value) $this->$index = $value;
		foreach($_GET as $index => $value)  $this->$index = $value;

		if(!$this->db) msg('잘못 접근하였습니다.', null); // close, none

		if($this->psMode == 'WRITE')
			$this->Create();
		else if($this->psMode == 'MODIFY')
			$this->Modify();
		else if($this->psMode == 'REMOVE')
			$this->Remove();
		else
			$this->Go();
	}

	function Create()
	{
		// dir setting
		$dir[base]	= $this->root.$this->db;
		$dir[br]	= $dir[base].'/br';
		$dir[ip]	= $dir[base].'/ip';
		$dir[os]	= $dir[base].'/os';
		$dir[week]	= $dir[base].'/week';

		if(is_dir($dir[base])) msg('이미 존재합니다.', null); // close, none

		// create dir
		foreach($dir as $ix => $v) @mkdir($v, 0777);

		// setting data
		$set = array('total'=>0,'today'=>0,'max'=>0,'maxpp'=>0);
		insert($dir[base], &$set, 'db.php');

		go("./");
	}

	function Modify()
	{

	}

	function Remove()
	{		
		$sDelDir = $this->root.$this->db;
		if(is_dir($sDelDir)) delDir($sDelDir);
		go("./");
	}

	function Go()
	{
		go("/");
	}
}
?>