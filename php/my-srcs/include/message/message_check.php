<?php
// ------------------------------------------------------------------------
// �ڽ��� ���̵� ������ ������ ���ο� �޼����� �����ߴٴ� ���̴�.
// ------------------------------------------------------------------------
if($_COOKIE[member_id])
{
	$sMessageRoot = "../message/data/$_COOKIE[member_id]";
	if(is_file($sMessageRoot))
	{
		@unlink($sMessageRoot);
		echo "<embed src='../message/include/i_memo.swf'></embed>"; // ������ �����ޱ����� | �α��� �����ʿ� �־��ָ� �뷫 ����.
	}
}
unset($sMessageRoot);
?>