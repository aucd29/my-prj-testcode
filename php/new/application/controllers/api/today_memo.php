<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Today_Memo extends CI_Controller {
	var $db;

	function __construct()
	{
		parent::__construct();		// Call the Model constructor

		$this->load->model("mdl_today_memo");
		$this->db = $this->mdl_today_memo;
		$this->db->setTable("todaymemo");
	}

	public function index()
	{
		echo json_encode($this->db->getList(1, 10));
	}
}

?>