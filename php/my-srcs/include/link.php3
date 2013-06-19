<?php
include_once $_SERVER[DOCUMENT_ROOT]."/include/linksData.php";
?>

// 메인
function  main() {
	self.location.href = "/main.html?code=<?=$time?>";
}

<?php
// 2004-02-02 | 탑메뉴 js자동생성
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