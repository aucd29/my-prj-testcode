<?php
// +----------------------------------------------------------------------+
// | 2004-05-31 | 논리 검색 하기 |
// +----------------------------------------------------------------------+
// | Authors: Kurome <aucd29@kornet.net>                                  |
// +----------------------------------------------------------------------+

// array:필드 string:검색어
function logicSearch(&$aField,$search)
{
	$search = trim($search);	
	$aWord  = explode(" ",$search);
	$nField = count($aField)-1;
	unset($que);

	for($i=0; $i<count($aWord); $i++)
	{
		if(strtoupper($aWord[$i])=='AND' or strtoupper($aWord[$i])=='OR' or strtoupper($aWord[$i])=='NOT')
		{
			if(strtoupper($aWord[$i])=='NOT')
			{
				$i++;
				$que .= " AND (";
				foreach($aField as $index => $value)
				{
					$que .= " $value NOT LIKE '%".$aWord[$i]."%'";
					if($index != $nField) $que .= " AND ";
				}
				$que .= ")";
			}
			else
			{
				if(strtoupper($aWord[$i])=='AND')
				{
					$i++;
					$que .= " AND (";
					foreach($aField as $index => $value)
					{
						$que .= " $value LIKE '%".$aWord[$i]."%'";
						if($index != $nField) $que .= " OR ";
					}
					$que .= ")";
				}
				else
				{
					$i++;
					$que .= " OR (";
					foreach($aField as $index => $value)
					{
						$que .= " $value LIKE '%".$aWord[$i]."%'";
						if($index != $nField) $que .= " OR ";
					}
					$que .= ")";
				}
			}
		}
		else
		{
			if($i>0)
			{
				$que .= " AND ";
			}

			$que  .= " (";
			foreach($aField as $index => $value)
			{
				$que .= " $value LIKE '%".$aWord[$i]."%'";
				if($index != $nField) $que .= " OR ";
			}
			$que .= ")";
		}
	}

	return $que;
}
?>