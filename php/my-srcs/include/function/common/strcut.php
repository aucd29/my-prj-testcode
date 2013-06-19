<?php
function strcut($strMessage, $nCutSize, $strTail = "...")
{
	// $nCutSize 가 0 이하이면 $strMessage를 리턴
	if($nCutSize <= 0)
	{
		return $strMessage;
	}
	else
	{
		// $strMessage 의 바이트 수를 구한다.
		$dwMessage = strlen($strMessage);

		// 문자열이 자르고자 하는 길이보다 적을땐 전체 문자열을 리턴
		if($dwMessage > $nCutSize)
		{
			for($i=0; $i<$nCutSize; $i++)
			{
				// $strMessage[$i]가 한글이면 자르는 길이를 1증가시킨 후에 다음 문자로 이동
				if(ord($strMessage[$i])>126)
				{
					$nCutSize++;
					$i++;

					if($dwMessage <= $nCutSize)
					{
						return $strMessage;
					}
				}
			}

			return substr($strMessage, 0, $nCutSize).$strTail;
		}
		else
		{
			return $strMessage;
		}
	}
}
?>