// +----------------------------------------------------------------------+
// | 2004-02-11 | 리스트에서 전체 선택하게 하는 소스                      |
// +----------------------------------------------------------------------+
// | Authors: Kurome <aucd29@kornet.net>                                  |
// +----------------------------------------------------------------------+

var nData;
function selectAll(obj)
{
	var chkBox = obj.form.elements['chkBox[]'];

	if(chkBox)
	{
		var chkLen = chkBox.length;	
		nData = (nData) ? 0 : 1;

		if(!chkLen)
		{
			chkBox.checked = nData;		// 1개면 그것은 배열이 아니다.
		}
		else 
		{
			for(i=0; i<chkLen; ++i) chkBox[i].checked = nData;
		}
	}	
}

//<input type='button' name='btn' value='S' class='inputbtn' onClick='selectAll(this)' />
//<input type='checkbox' name='chkBox[]' value='<?=$data[sTable]?>' />