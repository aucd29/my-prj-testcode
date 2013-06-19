<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Welcome extends CI_Controller {

	public function index()
	{
		$this->load->helper('cookie');
		$this->load->view('html/header');
		$this->load->view('indexpage');
		$this->load->view('html/footer');
	}
}
