<!------------------------------ page list --------------------------------->
<br>
<style type="text/css">
	.page_list_td{text-align:center;padding:3 7 3 7;}
	.page_list td{font-family:tahoma;font-size:8pt;font-weight:bold;color:#999999}

	a.page_a:hover{color:<?php echo $hoverColor; ?>;text-decoration:none;}
	a.page_a:link{color:<?php echo $linkColor; ?>;text-decoration:none;}
</style>

<table border='0' cellspacing='0' cellpadding='0' class='page_list' height='9' align='center'>
<tr>
	<td	align='right'>
	<?php
	$total_block = ceil($total_page/$page_per_block);
	$block		 = ceil($nPage/$page_per_block);
	$first_page  = ($block-1)*$page_per_block;
	$last_page	 = $block*$page_per_block;

	if($total_block	<= $block) $last_page = $total_page;

	// ���������� ------------------------------------------------------------
	if($block > 1)
	{
		$my_page = $first_page;
		echo"<a href='$PHP_SELF?nPage=$my_page$sAddWhere' onFocus='this.blur()' class='page_a'>[����]</a>";
	}
	else
	{
		echo"[����]";
	}

	echo "&nbsp;&nbsp;</td><td style='width:1' nowrap bgcolor='ececec'></td>";

	// ������ ������ ----------------------------------------------------------
	for($direct_page = $first_page+1; $direct_page <= $last_page; ++$direct_page)
	{
		echo "<td class='page_list_td' onMouseOver=bgColor='#f7f7f7' onMouseOut=bgColor=''>";
		if($nPage == $direct_page) {
			echo "<span style='color:$directColor' class='tahoma8'>$direct_page</span>";
		} else {
			echo"<a href='$PHP_SELF?nPage=$direct_page$sAddWhere' onFocus='this.blur()' class='page_a'>$direct_page</a>";
		}
		echo "</td><td style='width:1' nowrap bgcolor='ececec'></td>";
	}

	if(!$nTotal)
	{
		echo "<td class='page_list_td' onMouseOver=bgColor='#f7f7f7' onMouseOut=bgColor=''><span style='color:$directColor;' class='tahoma8'>1</span></td><td style='width:1' nowrap bgcolor='ececec'></td>";
	}

	echo "<td>&nbsp;&nbsp;";

	// ����	������ ------------------------------------------------------------
	if($block < $total_block)
	{
		$my_page = $last_page+1;
		echo"<a href='$PHP_SELF?nPage=$my_page$sAddWhere' onFocus='this.blur()' class='page_a'>[����]</a>";
	}
	else
	{
		echo"[����]";
	}
	?>

	</td>
</tr>
</table>
