function prevPopup(obj)
{
	var img = new Image();
	img.src = obj.value;
	if(img.src.length == 0) return;	// �̹����� ������ ���

	idx			= img.src.lastIndexOf("\\");
	filename	= img.src.substring(idx+1);
	idx2		= filename.lastIndexOf(".");
	ext			= filename.substring(idx2+1);
	ext2		= ext.toLowerCase();

	if(ext2 != "jpg" && ext2 != "gif" && ext2 != "jpeg" && ext2 != 'swf' && ext2 != 'png')
	{
		alert('����Ȯ���ڰ� jpg�� gif�� swf �� png ������ �ƴմϴ�.\n�� ������ ���ε带 �� �� �����ϴ�.');
		return;
	}
	else
	{
		obj.form.elements['nWidth'].value  = img.width;
		obj.form.elements['nHeight'].value = img.height;

		if (ext2 == 'swf')
			prevImages.innerHTML = "<embed src=" + img.src + ">";
		else
			prevImages.innerHTML = "<img src=" + img.src + ">";
	}
}

// ��ũ�� ������������ Ȱ��ȭ�ǰ��ϱ�
function Clicked(obj)
{
	with(obj.form)
	{
		if(elements['nClicked'][0].checked) elements['nClickedTxt'].disabled = true;
		else								elements['nClickedTxt'].disabled = false;
	}
}

function TypeCheck(obj)
{
	if(obj.form)
	{
		if(elements['nType'][0].checked) elements('html').disabled = true;
		else							 elements('html').disabled = false;
	}
}