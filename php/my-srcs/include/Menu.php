<?php
// ------------------------------------------------------------------------
// ��Ÿ	�ɼ� ����
// 2003-10-10 ���� ����޴�	�ؽ�Ʈ & �̹���	ó��
// ��ġ	  :	/img/leftMenu
// ���ϸ� :	leftMenuImg, leftMenu_$sPageName
// $sPageName �� �������� ��ȯ �����ʹ�	$arrSubMenu[$sPageName]	�� �Է¹���
// ------------------------------------------------------------------------

$sTblHeight		 = 25;	// ���̺� ���̰�
$sTblOutPadding	 = 25;	// ���콺 �ƿ��� �⺻ ���̰�
$sTblOverPadding = 25;	// ���콺 ������ ���� ���̰�

if($section)
	$File = $section;
else if($category)
	$File = $category;
else if($table)
	$File = $table;
else
	$File = !$menu?$sPageName.'_1':$menu;

$nFileTime = filemtime($_SERVER[DOCUMENT_ROOT].'/include/linksData.php');
if(is_file($_SERVER[DOCUMENT_ROOT]."/config/menu_timer.php"))
{
	include $_SERVER[DOCUMENT_ROOT].'/config/menu_timer.php';
}
else
{
	$fp = fopen($_SERVER[DOCUMENT_ROOT]."/config/menu_timer.php", w);
	fwrite($fp, "<?php\n");
	fwrite($fp, "\$menu_timer = \"$nFileTime\";");
	fwrite($fp, "\n?>");
	fclose($fp);
}

if(($menu_timer == $nFileTime && $menu_timer)&& is_file($_SERVER[DOCUMENT_ROOT]."/config/menu/$File.php"))
{
	include $_SERVER[DOCUMENT_ROOT]."/config/menu/$File.php";
}
else
{
	if($menu_timer != $nFileTime)
	{
		$fp = fopen($_SERVER[DOCUMENT_ROOT]."/config/menu_timer.php", w);
		fwrite($fp, "<?php\n");
		fwrite($fp, "\$menu_timer = \"$nFileTime\";");
		fwrite($fp, "\n?>");
		fclose($fp);

		//
		// �������ø� ���� ������ �ִ� ���ϵ��� �����Ѵ�.
		//
		$remove_menu_dir = $_SERVER[DOCUMENT_ROOT].'/config/menu';
		$objDir	= dir($remove_menu_dir);
		while (false !== ($data	= $objDir->read()))
		{
			if($data != '.' && $data != '..') @unlink($remove_menu_dir."/".$data);
		}
		$objDir->close();
	}

	ob_start();
	include $_SERVER[DOCUMENT_ROOT]."/include/linksData.php";
	
	echo "
<!------------------------------ Left Menu --------------------------------->
<table border='0' cellspacing='0' cellpadding='0' valign='top'>";
	if($arrSubMenu[$sPageName])
	{
		if(is_file($_SERVER[DOCUMENT_ROOT]."/subimg/${sPageName}_top.gif"))
		{
	?>
	<tr><td><img src='/subimg/<?php echo $sPageName?>_top.gif'></td></tr><tr><td height='5'></td></tr>
	<?php
		}

echo "
<style type='text/css'>
.${sPageName}_d	{ height:$sTblHeight;padding:0 0 0 $sTblOutPadding;background-image:url('/subimg/leftmenu_bg_${sPageName}.gif');cursor:pointer; }
.${sPageName}_o	{ height:$sTblHeight;color:$arrData[MouseOverColor]; padding:0 0 0 $sTblOverPadding;background-image:url('/subimg/leftmenu_obg_${sPageName}.gif');cursor:pointer; }
.${sPageName}_o	a:link {color:$arrData[MouseOverColor];	text-decoration:none; }
.${sPageName}_o	a:visited {color:$arrData[MouseOverColor]; text-decoration:none; }
</style>";

		foreach( $arrSubMenu[$sPageName] as	$item => $value	)
		{
			$items = explode("?", $item);
			parse_str($items[1],$this);

			// ------------------------------------------------------------------------
			// ���콺 ������� index�� ���	���̹Ƿ� ������	�ʰ� �Ѵ�.
			// ------------------------------------------------------------------------
			if($item !=	'MouseOverColor')
			{
				if( ($_GET[section]  && $_GET[section]==$this[section]) ||
					($_GET[menu]	 && $_GET[menu]==$this[menu]) ||
					($_GET[table]	 && $_GET[table]==$this[table]) ||
					($_GET[category] && $_GET[category] == $this[category]) ||
					$item==$_SERVER[PHP_SELF])
				{
					echo "<tr><td class='${sPageName}_o'>$value</td></tr>";
				}
				else
				{
					echo "<tr><td class='${sPageName}_d' onmouseover=\"className='${sPageName}_o'\"; onmouseout=\"className='${sPageName}_d'\"; onclick=self.location.href='$item'><a href='$item'>$value</a></td></tr>";
				}
			}
		}
	}
	?>
	<tr><td	height='20'></td></tr></table>

<?php
$result = ob_get_contents();
$fp = fopen($_SERVER[DOCUMENT_ROOT]."/config/menu/$File.php", w);
fwrite($fp, $result);
fclose($fp);
}
?>