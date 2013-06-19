<?php
/***************************************************************************
 *
 *		Date			: 2004-06-04
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			: function
 *
 *		배열로 받은 쿼리 변수를 맞게 처리후 되돌려주기
 *
 ***************************************************************************/

function &sql_query($sql)
{
	global $echosql;
	$result = mysql_query($sql) or die("<p>$sql<p>" . mysql_errno() . " : " .  mysql_error());
	if($echosql) echo $sql;
	return $result;
}

// 한행을 얻는다.
function &sql_fetch($sql,$opt=MYSQL_ASSOC)
{
	$rs = @mysql_fetch_array(sql_query($sql),$opt);
	return $rs;
}

function query_engin($table, &$arr, $type=null, $where=null)
{
	//
	// 업데이트인지 아니면 인서트인지 체킹 type이 있으면 update
	//
	if($type)
	{
		$rtn = 'UPDATE '.$table.' SET ';
		foreach($arr as $index => $value) $argv[] = $index."='".$value."'";
		$rtn = $rtn.implode(',', $argv);
		if($where)
		{
			if(!eregi('where',$where)) $rtn .= ' WHERE ';
			$rtn .= $where;
		}
	}
	else
	{
		$rtn = 'INSERT INTO '.$table.'(';
		foreach($arr as $index => $value)
		{
			$field[] = $index;
			$value2[] = "'".$value."'";
		}
		$rtn .= implode(',',$field).') VALUES ('.implode(',', $value2).')';
	}

	sql_query($rtn);
}
?>