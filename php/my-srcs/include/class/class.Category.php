<?php
class Category
{
	var $objDB;
	var $objCommon;
	var $sTable = 'category';

	var $nCategoryID;
	var $sName;
	var $sHeader;
	var $sFooter;
	var $sSkin;
	var $nParent;
	var $sCatePath;
	var $nRelation;
	var $nSort;
	var $subDesign;
	var $subSkin;
	var $nDepth;

	function Category()
	{
		$this->objDB	 = $GLOBALS[objDB];
		$this->objCommon = $GLOBALS[objCommon];
	}

	// ������ �� �ֱ�
	function setData(&$aPost)
	{
		if($aPost[submit]) unset($aPost[submit]);
		foreach($aPost as $index => $value) $this->$index = $value;
	}

	// ���� ī�װ��� �����ϴ��� �˻�
	function categoryChk($nCategoryID, $sName)
	{
		$sWhere = "nParent = ".$nCategoryID." AND sName = '".$sName."'";
		// ��ȯ���� ������ ��� �����.
		if($this->objDB->getCount($this->sTable, $sWhere))	return TRUE;
		else	return FALSE;
	}

	// ������ ���ؼ� ����ū ��ȣ�� �Ѱ��ش�.
	function SortChk()
	{
		$this->nSort = $this->objDB->getMax($this->sTable, 'nCategoryID');
	}


	// ���� ī�װ��� ī�װ� �н� �� �ֱ�
	function catePathFind(  )
	{
		// ���� ī�װ���� ī�װ��� �ش��ϴ� �θ��� ī�װ� �н��� �����ͼ�
		// �ڽ��� �̸��� ���̸� �ڽ��� ī�װ� �н��� �ȴ�.
		if($this->nCategoryID)
		{
			$sWhere = "nCategoryID = '$this->nCategoryID'";
			$data	= $this->objDB->slt( $this->sTable, 'sCatePath', $sWhere );
			$this->sCatePath = $data[sCatePath].'>>'.$this->sName;
		}
		// �ƴ϶�� �׳� �̸��� ī�װ� �н����ȴ�.
		else
		{
			$this->sCatePath = '>>'.$this->sName;
		}
	}

	// �θ��� �ִ��� �˻�.
	function parentFind()
	{
		if($this->nCategoryID)
			$this->nParent = $this->nCategoryID;
		else
			$this->nParent = 0;
	}

	function CategoryDepth()
	{
		if(!$this->nParent)
		{
			$this->nDepth = 1;
		}
		else
		{
			$rs = $this->objDB->slt($this->sTable,'nDepth',"nCategoryID=$this->nParent");
			$this->nDepth = $rs[nDepth]+1;
		}
	}


	// ī�װ��� �Ẹ��.
	function categoryWrite()
	{
		// �ִ� ī�װ��� �˻�.
		if( $this->categoryChk( $this->nCategoryID, $this->sName ) )
			$this->objCommon->msg( '����Ҽ����� ī�װ��ϴ�.', NULL );

		$this->SortChk();
		$this->catePathFind();
		$this->parentFind();
		$this->CategoryDepth();

		$aPost = array(
			'sName'			=> $this->sName,
			'sHeader'		=> $this->sHeader,
			'sFooter'		=> $this->sFooter,
			'nParent'		=> $this->nParent,
			'sCatePath'		=> $this->sCatePath,
			'nSort'			=> $this->nSort,
			'sSkin'			=> $this->sSkin,
			'nDepth'		=> $this->nDepth,
			'wdate'			=> date(Ymd),
		);

		return $aPost;
	}


	// ���� no�� ī�װ� �н����� �Ѱ��ش�.
	function noCatePathFind($nCategoryID)
	{
		$sWhere = "nCategoryID=$nCategoryID";
		$data = $this->objDB->slt( $this->sTable, 'sCatePath', $sWhere );
		$this->sCatePath = $data[sCatePath];
	}

	// ī�װ� �����غ���
	function removeSub($nCategoryID)
	{
		$this->objDB->delete( $this->sTable, "nCategoryID = $nCategoryID" );
	}


	// ī�װ� �����ϱ����� ã��. �޾Ҵ� ī�װ� �н��� �������� ������ ��� ã�´�.
	function categoryRemove($nCategoryID)
	{
		$this->noCatePathFind($nCategoryID);

		$sOrder = 'ORDER BY nCategoryID DESC';
		$sWhere = "sCatePath LIKE '$this->sCatePath%' AND sCatePath != '$this->sCatePath'";
		$sField = 'nCategoryID, sCatePath';

		# �����ҳ���
		$rtn = "������ ī�װ��� \\n$this->sCatePath";

		$res = $this->objDB->select($this->sTable, $sField, $sWhere);
		for( $i=0; $data = $this->objDB->fetch($res); $i++ )
		{
			$rtn .= "\\n$data[sCatePath]";
			$this->removeSub($data[nCategoryID]);
		}
		$this->removeSub($nCategoryID);

		$rtn .= "�Դϴ�.";
		return $rtn;
	}


	function modifyCatePath($nCategoryID, $sCatePath)
	{
		$this->objDB->query("UPDATE $this->sTable SET sCatePath = '$sCatePath' WHERE nCategoryID = $nCategoryID");
	}


	function modifySub($sBfCatePath, $sAfCatePath)
	{
		$sOrder = 'ORDER BY nCategoryID DESC';
		$sWhere = "sCatePath LIKE '$sBfCatePath%' AND sCatePath != '$sBfCatePath'";
		$sField = 'sCatePath, nCategoryID';

		$rtn = "�Բ� ������ ī�װ���\\n";
		$res = $this->objDB->select($this->sTable, $sField, $sWhere);

		for($i=0; $data = $this->objDB->fetch($res); ++$i)
		{
			$rtn .= "$data[sCatePath] \\n ";
			$sChangeCatePath = str_replace($sBfCatePath, $sAfCatePath, $data[sCatePath]);
			$this->modifyCatePath($data[nCategoryID], $sChangeCatePath);
		}
		$rtn .= "�Դϴ�.";

		return $rtn;
	}

	// ���� �����ε��� �����Ѵ�.
	function subDesign($nCategoryID)
	{
		$this->noCatePathFind($nCategoryID);
		$sOrder = 'ORDER BY nCategoryID DESC';
		$sWhere = "sCatePath LIKE '$this->sCatePath%' AND sCatePath != '$this->sCatePath'";
		$sField = 'nCategoryID';

		$res = $this->objDB->select($this->sTable, $sField, $sWhere, $sOrder);

		for($i=0; $rs = $this->objDB->fetch($res); $i++)
		{
			$this->modifyDesign($rs[nCategoryID], $nMode);
		}
	}

	// ���� �������� �����Ѵ�.
	function modifyDesign($nCategoryID)
	{
		$sWhere = "nCategoryID = $nCategoryID";

		if($this->subDesign)
		{
			$aPost[sHeader] = $this->sHeader;
			$aPost[sFooter] = $this->sFooter;
		}

		if($this->subSkin)
			$aPost[sSkin] = $this->sSkin;

		if(is_array($aPost))
			$this->objDB->update($this->sTable, $aPost, $sWhere);
	}

	function modify()
	{
		$this->catePathFind();
		$this->parentFind();
		$this->CategoryDepth();

		$aPost = array(
			'sName'			=> $this->sName,
			'sHeader'		=> $this->sHeader,
			'sFooter'		=> $this->sFooter,
			'nParent'		=> $this->nParent,
			'sCatePath'		=> $this->sCatePath,
			'sSkin'			=> $this->sSkin,
			'nDepth'		=> $this->nDepth,
		);

		return $aPost;
	}


	function selfnCategoryIDFind( $sCatePath )
	{
		$sWhere = "nCategoryID = $nCategoryID";
		$data   = $objDB->slt( $this->sTable, 'nCategoryID', $sWhere );

		return $data[nCategoryID];
	}


	// ���� �ڱ� ��ġ���� ���ش�.
	function selfFind( $nCategoryID, $sItemName=NULL )
	{
		$sWhere = "nCategoryID = $nCategoryID";
		$data   = $objDB->slt( $this->sTable, 'sCatePath', $sWhere );

		$aCatePath = explode( '>>', $data[sCatePath] );
		foreach( $aCatePath as $index => $value )
		{
			if( !$index )
			{
				echo "<a href='/'>HOME</a> &gt;&gt; ";
			}
			else
			{
				$nCategoyID = selfnCategoryIDFind($value);
				echo "<a href='$PHP_SELF?nCategoryID=$nCategoryID'>$value</a> &gt;&gt; ";

				if($sItemName) echo " <b>$sItemName ��ǰ�ڼ�������</b>";
			}
		}
	}


	// �ֻ��� ī�װ� ���� �Ѱ��ش�.
	function topCategoryFind($nSort=NULL)
	{
		$nSort = (!$nSort)?'nCategoryID':$nSort;

		$sOrder = "ORDER BY $nSort DESC";
		$sWhere = "nParent=0";
		$sField = 'sName, nCategoryID';

		$res = $this->objDB->select($this->sTable, $sField, $sWhere);
		for($i=0; $data = $this->objDB->fetch($res); ++$i)
		{
			$nCategoryID = $data[nCategoryID];
			$topCategoryFind[$nCategoryID] = $data[sName];
		}
		return $topCategoryFind;
	}


	// �ֻ��� ī�װ� ������ �Ѱ��ش�.
	function topCategoryTotal()
	{
		$sWhere = "nParent = 0";
		$rtn = $this->objDB->getCount($this->sTable, $sWhere);

		return $rtn;
	}


	// �ش� ī�װ��� ������ �Ѱ��ش�.(������ ����);
	function subCateogryTotal($nCategoryID)
	{
		$this->noCatePathFind($nCategoryID);

		$sWhere = "sCatePath LIKE '$this->sCatePath%'";
		$rtn = $this->objDB->getCount($this->sTable, $sWhere);

		return $rtn;
	}
}

if(!$objCategory) $objCategory = & new Category;
?>