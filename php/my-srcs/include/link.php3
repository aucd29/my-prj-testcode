<?php
include_once $_SERVER[DOCUMENT_ROOT]."/include/linksData.php";
?>

// ����
function  main() {
	self.location.href = "/main.html?code=<?=$time?>";
}

<?php
// 2004-02-02 | ž�޴� js�ڵ�����
foreach($arrSubMenu as $index => $value)
{
	$i=1;

	foreach($value as $index2 => $value2)
	{
		if($index2 != 'MouseOverColor')
		{
			echo "
			function $index$i()
			{
				self.location.href = '$index2&code=$time';
			}";
			$i++;
		}
	}
}
?>