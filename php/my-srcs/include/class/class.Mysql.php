<?php
class Mysql
{
	var $szHost;        // ȣ��Ʈ
	var $szUser;        // �����
	var $szPassword;    // ��й�ȣ
	var $szDataBase;    // �����ͺ��̽�
	var $resConn;       // ���� �ڵ�
	var $resQuery;      // ���� �ڵ�
	var $szQuery;       // ������

	// ������
	function Mysql($szHost, $szUser, $szPassword)
	{
		$this->szHost       = $szHost;
		$this->szUser       = $szUser;
		$this->szPassword   = $szPassword;
	}

	// ����
	function connect()
	{
		$this->resConn = @mysql_connect($this->szHost, $this->szUser, $this->szPassword) or die ("DB ���ῡ �����Ͽ����ϴ�");
	}

	// DB ����
	function selectDB($szDataBase = NULL)
	{
		if(isset($szDataBase)) $this->szDataBase = $szDataBase;
		@mysql_select_db($this->szDataBase, $this->resConn) or die ($this->szDataBase . " �����ͺ��̽��� �������� �ʰų� ���ٱ����� �����ϴ�.");
	}

	// ������ �����Ѵ�
	function &query($szQuery)
	{
		$this->szQuery  = $szQuery;
		$this->resQuery = @mysql_query($this->szQuery, $this->resConn) or die(mysql_error().$this->szQuery);
		return $this->resQuery;
	}

	# �������� ����
	function getQuery()
	{
		return $this->szQuery;
	}

	function &select($szTable, $szField='*', $szWhere=NULL, $szOrderBy=NULL, $nCount=NULL, $nStart=NULL)
	{
		$szQuery = "SELECT $szField FROM $szTable";
		if (isset($szWhere))		$szQuery .= " WHERE $szWhere";
		if (isset($szOrderBy))		$szQuery .= " $szOrderBy";
		if (isset($nStart))			$szQuery .= " LIMIT $nStart, $nCount";
		else if (isset($nCount))	$szQuery .= " LIMIT $nCount";
		return $this->query($szQuery);
	}

	function &slt($szTable, $szField = '*', $szWhere = NULL, $szOrderBy = NULL, $nCount = NULL, $nStart = NULL)
	{
		$szQuery = "SELECT $szField FROM $szTable";
		if (isset($szWhere))
			$szQuery .= " WHERE $szWhere";

		if (isset($szOrderBy))
			$szQuery .= " $szOrderBy";

		if (isset($nStart))
			$szQuery .= " LIMIT $nStart, $nCount";
		else if (isset($nCount))
			$szQuery .= " LIMIT $nCount";

		$res = $this->query($szQuery);
		$rs	 = $this->fetch($res);

		return $rs;
	}

	// $const = MYSQL_NUM, MYSQL_ASSOC, and MYSQL_BOTH
	function &fetch($resQuery = NULL, $const = MYSQL_ASSOC)
	{
		if (!isset($resQuery))
			$resQuery = $this->resQuery;

		return mysql_fetch_array($resQuery, $const);
	}

	function freeResult($resQuery = NULL)
	{
		if (!isset($resQuery))
			$resQuery = $this->resQuery;

		return @mysql_free_result($resQuery);
	}

	function numRows($resQuery = NULL)
	{
		if (!isset($resQuery))
			$resQuery = $this->resQuery;

		return @mysql_num_rows($resQuery);
	}

	function getCount($szTable, $szWhere = NULL, $szField = '*', $sOrder= NULL )
	{
		$szQuery = "SELECT COUNT($szField) FROM $szTable";

		if (isset($szWhere))
			$szQuery .= " WHERE $szWhere";

		if( $sOrder )
			$szQuery .= $sOrder;

		$res = $this->query($szQuery);
		$rs  = $this->fetch($res, MYSQL_NUM);

		return $rs[0];
	}

	function getMax($szTable, $szField = '*', $szWhere = NULL)
	{
		$szQuery = "SELECT MAX($szField) FROM $szTable";

		if (isset($szWhere))
			$szQuery .= " WHERE $szWhere";

		$res = $this->query($szQuery);
		$rs  = $this->fetch($res, MYSQL_NUM);

		return $rs[0];
	}

	function getMin($szTable, $szField = '*', $szWhere = NULL)
	{
		$szQuery = "SELECT MIN($szField) FROM $szTable";

		if (isset($szWhere))
			$szQuery .= " WHERE $szWhere";

		$res = $this->query($szQuery);
		$rs  = $this->fetch($res, MYSQL_NUM);

		return $rs[0];
	}

	function &insert($szTable, $arrFieldValue, $bOpt = FALSE) // ������ �Է�
	{
		$nTmp = 0;

		if ($bOpt)
		{
			$nSize = sizeof($arrFieldValue) - 1;

			foreach($arrFieldValue as $arrTmp)
			{
				$nTmp1=0;
				foreach($arrTmp as $szField => $szValue)
				{
					if ($nTmp==0) $szFields .= ($nTmp1==0)? $szField : ", $szField";
					$szValues .= ($nTmp1 == 0)? "('$szValue'" : ", '$szValue'";
					$nTmp1++;
				}
				$szValues .= ($nTmp == $nSize) ? ")" : "),";
				$nTmp++;
			}
		}
		else
		{
			foreach($arrFieldValue as $szField => $szValue)
			{
				$szFields .= ($nTmp==0)? $szField : ", $szField";
				$szValues .= ($nTmp==0)? "('$szValue'" : ", '$szValue'";
				$nTmp++;
			}
			$szValues.=")";
		}
		$szQuery = sprintf("INSERT INTO %s (%s) VALUES %s", $szTable, $szFields, $szValues);
		return $this->query($szQuery);
	}

	function &update($szTable, $arrFieldValue, $szWhere = NULL) // ������ ����(�迭�� �̿�)
	{
		$nTmp=0;

		foreach ($arrFieldValue as $szField => $szValue)
		{
			$szFields .= ($nTmp==0)? "$szField='$szValue'" : ", $szField='$szValue'";
			$nTmp++;
		}

		if ($szWhere)
			$szWhere = "WHERE $szWhere";

		$szQuery = sprintf("UPDATE %s SET %s %s ", $szTable, $szFields, $szWhere);

		return $this->query($szQuery);
	}

	function &delete($szTable, $szWhere = NULL)
	{
		$szQuery = "DELETE FROM $szTable";
		if (isset($szWhere))
			$szQuery .= " WHERE $szWhere";

		return $this->query($szQuery);
	}

	function dataSeek($n, $resQuery = NULL)
	{
		if (!isset($resQuery))
			$resQuery = $this->resQuery;

		return mysql_data_seek($resQuery, $n);
	}

	function close()
	{
		if ($this->resConn)
			@mysql_close($this->resConn);
	}
}
?>