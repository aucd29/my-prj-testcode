<link rel='stylesheet' href='/include/css.css' type='text/css'>
<table cellspacing='0' cellpadding='3' border='0' width='95%' align='center'>

<?php
include $_SERVER[DOCUMENT_ROOT]."/include/linksData.php";
$yes=null;
foreach($arrSubMenu as $index => $value)
{
	if(!is_dir('./c/'.$index))
	{
		echo '<tr><td width=120>/c/'.$index.'</td><td>���丮 �����Ϸ�</td></tr>';
		@mkdir('./c/'.$index, 0777);
		@mkdir('./c/'.$index.'/img', 0777);

		// create default page
		$fp = fopen('./c/'.$index.'/index.html', w);
		fwrite($fp, "<?php\n");
		fwrite($fp, "if(!\$menu) \$menu='${index}_1';\n");
		fwrite($fp, "include \$_SERVER[DOCUMENT_ROOT].'/html/${index}_header.php3';\n");		
		fwrite($fp, "if(is_file(\$_SERVER[DOCUMENT_ROOT].\"/title/\${menu}.gif\")) echo \"<img src='/title/\${menu}.gif'><br>\";\n");
		fwrite($fp, "if(is_file(\"./\$menu.html\"))\n");
		fwrite($fp, "{\n");
		fwrite($fp, "	include \"./\$menu.html\";\n");
		fwrite($fp, "}\n");
		fwrite($fp, "else\n");
		fwrite($fp, "{\n");
		fwrite($fp, "	echo '������������ �غ����Դϴ�';\n");
		fwrite($fp, "}\n");
		fwrite($fp, "include \$_SERVER[DOCUMENT_ROOT].'/html/default_footer.php3';\n");
		fwrite($fp, "?>");
		fclose($fp);
		chmod('./c/'.$index.'/index.html',0777);


		// create header file
		$fp = fopen('./html/'.$index.'_header.php3', w);
		fwrite($fp, "<?php\n");
		fwrite($fp, "\$sPageName = \"$index\";\n");
		fwrite($fp, "include \$_SERVER[DOCUMENT_ROOT].'/html/top_header.php3';\n");
		fwrite($fp, "?>\n");
		fclose($fp);
		@chmod('./html/'.$index.'_header.php3', 0777);

		$yes=1;
	}
}

if($yes)
{
	echo '<tr><td style=color:red><br><br>���������� ���丮�� �ʱ������� �����Ǿ����ϴ�.<br>��������(make_dir.php)�� �������ּ���</td></tr>';
}
?>
</table>