<?php
/***************************************************************************
 *
 *		Date			: 2004-06-21
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			: Menu
 *
 *		메뉴를 생성시 캐쉬화 한다.
 *		$sPageName를 기준으로 $arrSubMenu값을 가져오게 된다.
 *
 ***************************************************************************/

if($menu)
	$File = !$menu?$sPageName.'_1':$menu;
else if($table)
	$File = $table;
else if($section)
	$File = $section;
else if($category)
	$File = $category;


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
		// 리프레시를 위해 이전에 있던 파일들을 삭제한다.
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

	if($arrSubMenu[$sPageName])
	{
?>
<!------------------------------ Left Menu --------------------------------->
<table cellspacing='0' cellpadding='0' border='0' valign='top'>
<?php
if(is_file($_SERVER[DOCUMENT_ROOT]."/subimg/${sPageName}_top.gif"))
	echo "<tr><td colspan='2'valign='top'><img src='/subimg/${sPageName}_top.gif' /></td></tr>";
	$i=1;
	foreach($arrSubMenu[$sPageName] as $item => $img)
	{
		if($item !=	'MouseOverColor')
		{
			$aSize = @getimagesize($_SERVER[DOCUMENT_ROOT]."/subimg/{$sPageName}{$i}.gif");
			$items = explode("?", $item);
			parse_str($items[1],$this);

			if( ($_GET[section]  && $_GET[section]==$this[section]) ||
				($_GET[menu]	 && $_GET[menu]==$this[menu]) ||
				($_GET[table]	 && $_GET[table]==$this[table]) ||
				($_GET[category] && $_GET[category] == $this[category]) ||
				$item==$_SERVER[PHP_SELF] )
			{
				echo "<tr><td><img src='/subimg/{$sPageName}{$i}_over.gif' alt='$img' $aSize[3] /></td></tr>";
			}
			else
			{
				echo "<tr><td><a href='$item' class='rollover' onFocus='this.blur()'><img src='/subimg/{$sPageName}{$i}.gif' border='0' alt='$img' $aSize[3] /><img src='/subimg/{$sPageName}{$i}_over.gif' border='0' alt='$img' class='over' $aSize[3] /></a></td></tr>";
			}
			++$i;
		}
	}
}
?>
<tr><td	height=20 bgcolor='#ffffff'></td></tr></table>
<?php
$result = ob_get_contents();
$fp = fopen($_SERVER[DOCUMENT_ROOT]."/config/menu/$File.php", w);
fwrite($fp, $result);
fclose($fp);
}
?>