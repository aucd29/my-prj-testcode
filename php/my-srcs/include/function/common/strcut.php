<?php
function strcut($strMessage, $nCutSize, $strTail = "...")
{
	// $nCutSize �� 0 �����̸� $strMessage�� ����
	if($nCutSize <= 0)
	{
		return $strMessage;
	}
	else
	{
		// $strMessage �� ����Ʈ ���� ���Ѵ�.
		$dwMessage = strlen($strMessage);

		// ���ڿ��� �ڸ����� �ϴ� ���̺��� ������ ��ü ���ڿ��� ����
		if($dwMessage > $nCutSize)
		{
			for($i=0; $i<$nCutSize; $i++)
			{
				// $strMessage[$i]�� �ѱ��̸� �ڸ��� ���̸� 1������Ų �Ŀ� ���� ���ڷ� �̵�
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