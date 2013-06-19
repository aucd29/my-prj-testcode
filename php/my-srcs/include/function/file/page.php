<table width='98%' cellpadding='14' cellspacing='0' border='0' align='center'>
<tr><td align='center'>
<?php
$LISTNUM=30;
if($p > $LISTNUM)
{
	if($p != 1)
	{
		$PrevPage = $p-1;
		echo "<a href=$ACT&p=1>[1]</a>\n";
	}
}
$term = $LISTNUM;$f = 1;$l = $term;
while($f <= $TOTAL_PAGE_NUM)
{
	if(($f <= $p) && ($p <= $l))
	{
		$prevp = $f - 1;
		if($prevp > 0) echo "<a href=$ACT&p=$prevp>[¿Ã¿¸]</a>-";
		if($l <= $TOTAL_PAGE_NUM)
		{
			for($page = $f; $page <= $l; ++$page)
			{
				if($page == $p) echo "[$page]";
				else echo "<a href=$ACT&p=$page>[$page]</a>";
			}
		}
		else
		{
			for($page = $f; $page <= $TOTAL_PAGE_NUM; ++$page)
			{
				if ($page == $p) echo "[$page]";
				else echo "<a href=$ACT&p=$page>[$page]</a>";
			}
		}
		$nextp = $l + 1;
		if($nextp <= $TOTAL_PAGE_NUM) echo "-<a href=$ACT&p=$nextp>[Next]</a>";
	}

	$f += $term;
	$l += $term;
}
if($nextp <= $TOTAL_PAGE_NUM) echo "<a href=$ACT&p=$TOTAL_PAGE_NUM>[$TOTAL_PAGE_NUM]</a>\n";
?>
</td></tr></table>