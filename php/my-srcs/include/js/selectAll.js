// +----------------------------------------------------------------------+
// | 2004-02-11 | ����Ʈ���� ��ü �����ϰ� �ϴ� �ҽ�                      |
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
			chkBox.checked = nData;		// 1���� �װ��� �迭�� �ƴϴ�.
		}
		else 
		{
			for(i=0; i<chkLen; ++i) chkBox[i].checked = nData;
		}
	}	
}

//<input type='button' name='btn' value='S' class='inputbtn' onClick='selectAll(this)' />
//<input type='checkbox' name='chkBox[]' value='<?=$data[sTable]?>' />