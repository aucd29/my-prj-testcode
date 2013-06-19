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

	// 데이터 값 넣기
	function setData(&$aPost)
	{
		if($aPost[submit]) unset($aPost[submit]);
		foreach($aPost as $index => $value) $this->$index = $value;
	}

	// 현재 카테고리가 존재하는지 검사
	function categoryChk($nCategoryID, $sName)
	{
		$sWhere = "nParent = ".$nCategoryID." AND sName = '".$sName."'";
		// 반환값이 있으면 사용 못헌다.
		if($this->objDB->getCount($this->sTable, $sWhere))	return TRUE;
		else	return FALSE;
	}

	// 정렬을 위해서 젤로큰 번호를 넘겨준다.
	function SortChk()
	{
		$this->nSort = $this->objDB->getMax($this->sTable, 'nCategoryID');
	}


	// 하위 카테고리시 카테고리 패스 값 주기
	function catePathFind(  )
	{
		// 하위 카테고리라면 카테고리에 해당하는 부모의 카테고리 패스를 가져와서
		// 자신의 이름을 붙이면 자신의 카테고리 패스가 된다.
		if($this->nCategoryID)
		{
			$sWhere = "nCategoryID = '$this->nCategoryID'";
			$data	= $this->objDB->slt( $this->sTable, 'sCatePath', $sWhere );
			$this->sCatePath = $data[sCatePath].'>>'.$this->sName;
		}
		// 아니라면 그냥 이름이 카테고리 패스가된다.
		else
		{
			$this->sCatePath = '>>'.$this->sName;
		}
	}

	// 부모값이 있는지 검사.
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


	// 카테고리를 써보자.
	function categoryWrite()
	{
		// 있는 카테고린지 검사.
		if( $this->categoryChk( $this->nCategoryID, $this->sName ) )
			$this->objCommon->msg( '사용할수없는 카테고립니다.', NULL );

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


	// 받은 no로 카테고리 패스값을 넘겨준다.
	function noCatePathFind($nCategoryID)
	{
		$sWhere = "nCategoryID=$nCategoryID";
		$data = $this->objDB->slt( $this->sTable, 'sCatePath', $sWhere );
		$this->sCatePath = $data[sCatePath];
	}

	// 카테고리 삭제해블자
	function removeSub($nCategoryID)
	{
		$this->objDB->delete( $this->sTable, "nCategoryID = $nCategoryID" );
	}


	// 카테고리 삭제하기위해 찾자. 받았던 카테고리 패스를 기준으로 하위를 모두 찾는다.
	function categoryRemove($nCategoryID)
	{
		$this->noCatePathFind($nCategoryID);

		$sOrder = 'ORDER BY nCategoryID DESC';
		$sWhere = "sCatePath LIKE '$this->sCatePath%' AND sCatePath != '$this->sCatePath'";
		$sField = 'nCategoryID, sCatePath';

		# 리턴할내용
		$rtn = "삭제한 카테고리는 \\n$this->sCatePath";

		$res = $this->objDB->select($this->sTable, $sField, $sWhere);
		for( $i=0; $data = $this->objDB->fetch($res); $i++ )
		{
			$rtn .= "\\n$data[sCatePath]";
			$this->removeSub($data[nCategoryID]);
		}
		$this->removeSub($nCategoryID);

		$rtn .= "입니다.";
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

		$rtn = "함께 수정된 카테고리는\\n";
		$res = $this->objDB->select($this->sTable, $sField, $sWhere);

		for($i=0; $data = $this->objDB->fetch($res); ++$i)
		{
			$rtn .= "$data[sCatePath] \\n ";
			$sChangeCatePath = str_replace($sBfCatePath, $sAfCatePath, $data[sCatePath]);
			$this->modifyCatePath($data[nCategoryID], $sChangeCatePath);
		}
		$rtn .= "입니다.";

		return $rtn;
	}

	// 하위 디자인들을 변경한다.
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

	// 하위 디자인을 변경한다.
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


	// 현재 자기 위치값을 내준다.
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

				if($sItemName) echo " <b>$sItemName 상품자세히보기</b>";
			}
		}
	}


	// 최상위 카테고리 값을 넘겨준다.
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


	// 최상위 카테고리 갯수를 넘겨준다.
	function topCategoryTotal()
	{
		$sWhere = "nParent = 0";
		$rtn = $this->objDB->getCount($this->sTable, $sWhere);

		return $rtn;
	}


	// 해당 카테고리의 갯수를 넘겨준다.(하위도 포함);
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