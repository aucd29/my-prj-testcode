<?php
/***************************************************************************
 *
 *		Date			: 2004-08-28
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			:
 *
 *		�� ������ Author : 0172 (http://romeo.snu.ac.kr/~parkmc2)
 *
 ***************************************************************************/

//
// ���ϴ� ����Ʈ�� �ܾ�´�.
//
function readURL($url)
{
	$fp = @fopen($url,"rb");
	$file = $fp;
	settype($file, boolean);
	if($file)
	{
		while (!feof($fp))
		{
			$tmp = fgets($fp, 1024);
			$temp .= $tmp;
		}
		fclose($fp);
	}
	else
	{
		$temp = "";
	}
	return $temp;
}

//
// ���ϴ� �κ��� �ɰ��ش�. | �ɰ������Ϳ���,����,������
//
function GetData($d,$f,$l,$tag=null)
{
	$tmp = strstr($d,$f);
	$tmp = substr($tmp,0,strpos($tmp,$l));
	if(!$tag)	$tmp = trim(strip_tags(str_replace($f,'',$tmp)));
	else		$tmp = trim(strip_tags(str_replace($f,'',$tmp),$tag));
	return $tmp;
}
?>