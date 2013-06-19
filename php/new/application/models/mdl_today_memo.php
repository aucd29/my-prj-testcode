<?php
class Mdl_Today_Memo extends CI_Model {
	var $table;
	var $num_per_page = 5;
	var $page_per_block = 10;

	function __construct()
	{
		parent::__construct(); // Call the Model constructor
	}

	function setTable($table)
	{
		$this->table = $table;
	}

	function getTable()
	{
		return $this->table;
	}

	function getCount()
	{
		return $this->db->count_all($this->table);
	}

	function getList($page, $total)
	{
		$str  = "SELECT * FROM " . $this->table . " ORDER BY no DESC LIMIT ";
		$str .= $page.",".$this->num_per_page;

		$sql = $this->db->query($str);
		return $sql->result();
	}

/*
	function findById($id) {
		$qry = $this->db->query("select * from Groupby where id=" . $this->db->escape($id));
		$rslt = $qry->result();
		if($qry->num_rows()>0) {
			return $rslt[0];
		} else {
			return null;
		}
	}

	function findByEmail($_email = '') {
		$subQry = "";
		if($_email) {
			$subQry = " AND email=" . $this->db->escape($_email);
		}
		$qry = $this->db->query("select
				id as groupby_id,
				email,
				title,
				author,
				description,
				confirmation,
				if(fileName!='', concat('http://webby.obigo.com/~sec/upload/groupby/', fileName), '') as fileName,
				fileSize,
				yymmdd as date
			from Groupby
			where 1 AND fileName!='' and fileSize>0 {$subQry}
			limit ". (($this->_pageID - 1) * $this->_pageSize) .",{$this->_pageSize}");
		$rslt = $qry->result();
		if($qry->num_rows()>0) {
			return $rslt;
		} else {
			return null;
		}
	}

	function get_num_rowsByEmail($_email = '') {
		$subQry = "";
		if($_email) {
			$subQry = " AND email=" . $this->db->escape($_email);
		}
		$qry = $this->db->query("select count(*) as cnt
				from Groupby
				where 1 AND fileName!='' and fileSize>0 {$subQry}");
		$rslt = $qry->result();
		return $rslt[0]->{'cnt'};
	}


	//return T/F
	function create($data) {
		foreach($data as $key=>$var) {
			$data[$key] = $this->db->escape( $var );
		}
		return $this->db->simple_query("insert into Groupby (email, title, author, description, fileName, fileSize, yymmdd, confirmation) values ({$data['email']}, {$data['title']}, {$data['author']}, {$data['description']}, {$data['fileName']}, {$data['fileSize']}, now(), {$data['confirmation']} )");
	}

	function modify($data, $id) {

		if($data['fileName'] and $data['fileSize']) {
			$this->db->simple_query("update Groupby set fileName=". $this->db->escape($data['fileName']).", fileSize=". $this->db->escape($data['fileSize']) ." where id={$id}");
		}

		if($data['chk_del_img']) {
			$this->db->simple_query("update Groupby set fileName='', fileSize='' where id={$id}");
		}

		foreach($data as $key=>$var) {
			$data[$key] = $this->db->escape( $var );
		}
		return $this->db->simple_query("update Groupby set email={$data['email']}, title={$data['title']}, author={$data['author']}, description={$data['description']}, confirmation={$data['confirmation']} where id={$id}");
	}

	function delete($_id) {
		$this->delImg($_id);
		return $this->db->simple_query("delete from Groupby where id=" . $this->db->escape($_id) );
	}

	function delImg($_id) {
		if($_id > 0) {
			$tmp_gb_info = $this->findById($_id);
			if($tmp_gb_info->{'fileName'} !="") {
				@unlink("./upload/groupby/{$tmp_gb_info->{'fileName'}}");
			}
		}
	}
*/
}
?>