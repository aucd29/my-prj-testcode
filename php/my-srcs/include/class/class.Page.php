<?php
class Page
{
	var $total_number;
	var $total_page_number;		// 총페이지수
	var $total_block_number;	// 총블록수
	var $page_number;			// 현재 페이지
	var $article_per_page;		// 페이지당 출력 수
	var $page_per_block;		// 페이지당 블록 수
	var $append;				// 첨가 쿼리

	function setData($total_number, $page_number, $article_per_page, $page_per_block=10, $append=null)
	{
		$this->total_number			= $total_number;
		$this->total_page_number	= ceil($total_number/$article_per_page);
		$this->page_number			= $page_number;
		$this->article_per_page		= $article_per_page;
		if(!$page_per_block) $page_per_block=10;
		$this->page_per_block		= $page_per_block;
		$this->append				= $append;
	}

	function pageView(&$strHTML)
	{
		$total_block	= ceil($this->total_page_number/$this->page_per_block);
		$block			= ceil($this->page_number/$this->page_per_block);
		$first_page		= ($block-1)*$this->page_per_block;
		$last_page		= $block*$this->page_per_block;

		if($total_block <= $block) $last_page = $this->total_page_number;

		if($block > 1)
		{
			$my_page = $first_page;
			if ($this->append)	$aReplace[0] = "$_SERVER[PHP_SELF]?$this->append&nPage=$my_page";
			else				$aReplace[0] = "$_SERVER[PHP_SELF]?nPage=$my_page";
		}
		else $aReplace[0] = "#1";

		for($direct_page = $first_page+1; $direct_page <= $last_page; ++$direct_page)
		{
			if($this->page_number == $direct_page)
				$aReplace[1] .= " <b class='Page' style='color:black'> $direct_page </b> ";
			else
			{
				if ($this->append)
					$aReplace[1] .=" <a href='$_SERVER[PHP_SELF]?$this->append&nPage=$direct_page' class='Page'> $direct_page </a>";
				else
					$aReplace[1] .=" <a href='$_SERVER[PHP_SELF]?nPage=$direct_page' class='Page'> $direct_page </a>";
			}
			$aReplace[1] .= $direct_page != $last_page?' | ':'';
		}

		if (!$aReplace[1]) $aReplace[1] = " <b class='Page'>1</b> ";

		if($block < $total_block)
		{
			$my_page = $last_page+1;
			if ($this->append)	$aReplace[2] = "$_SERVER[PHP_SELF]?$this->append&nPage=$my_page";
			else				$aReplace[2] = "$_SERVER[PHP_SELF]?nPage=$my_page";
		}
		else $aReplace[2] = "#1";
		$aProto = array(':LEFT:',':PAGE_BLOCK:',':RIGHT:');
		return str_replace($aProto, $aReplace, $strHTML);
	}

	function srhBox( $srhItem, $prevSlt, $prevWord, $srhIcon = NULL, $aAddData=NULL )
	{
		$aAddData = ( $aAddData ) ? "?$aAddData" : '';
		echo "
		<form action='$_SERVER[PHP_SELF]$aAddData' method='post' style='border=0;margin:0' name='frm_search' onSubmit='return SoftCheck(this)'>
		<table border='0' cellspacing='0' cellpadding='3' align='center'>
		<tr>
			<td height='40' align='center' align='center'>
			<select name='srhList' class='input'>";

			// search Item print
			foreach( $srhItem as $srhOpt => $srhName )
			{
				$sltList = ( $srhOpt == $prevSlt ) ? 'selected' : '';
				echo"<option value='$srhOpt' $sltList> $srhName";
			}

			echo "
			</select>
			</td>
			<td><input type='text' name='srhWord' value='$prevWord' class='input'></td>
			<td>";

			// search icon exists
			if($srhIcon) echo "<input type='image' src='$srhIcon' align='absbottom'>";
			else echo "<input type='submit' name='srhSubmit' value='검색' class='inputbtn'>";

			echo "
			</td>
		</tr>
		</table>
		</form>";
	}
}

if(!$objPage) $objPage = & new Page;
?>