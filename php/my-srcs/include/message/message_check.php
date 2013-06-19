<?php
// ------------------------------------------------------------------------
// 자신의 아이디 파일이 있으면 새로운 메세지가 도착했다는 뜻이다.
// ------------------------------------------------------------------------
if($_COOKIE[member_id])
{
	$sMessageRoot = "../message/data/$_COOKIE[member_id]";
	if(is_file($sMessageRoot))
	{
		@unlink($sMessageRoot);
		echo "<embed src='../message/include/i_memo.swf'></embed>"; // 쪽지가 도착햇구먼유 | 로그인 파일쪽에 넣어주면 대략 조쿠나.
	}
}
unset($sMessageRoot);
?>