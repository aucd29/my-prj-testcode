function prevMovie(obj,pos)
{
	img			= obj.value;
	if (img.length == 0) return;
	idx			= img.lastIndexOf("\\");
	filename	= img.substring(idx+1);
	idx2		= filename.lastIndexOf(".");
	ext			= filename.substring(idx2+1);
	ext2		= ext.toLowerCase();

	pos = eval(pos);
	if( ext2 != "asf" && ext2 != "wmv" && ext2 != "avi" && ext2 != 'mpg' && ext2 != 'mpeg' )
	{
		alert('����Ȯ���ڰ� asf, wmv, avi, mpg, mpeg ������ �ƴմϴ�.\n�� ������ ���ε带 �� �� �����ϴ�.');
		obj.select();
	}
	else
	{
		pos.innerHTML = "<embed src=\"file://" + img + "\">";
	}
}

function prevMp3(obj,pos)
{
	img			= obj.value;
	if (img.length == 0) return;
	idx			= img.lastIndexOf("\\");
	filename	= img.substring(idx+1);
	idx2		= filename.lastIndexOf(".");
	ext			= filename.substring(idx2+1);
	ext2		= ext.toLowerCase();

	pos = eval(pos);
	if( ext2 != "mp3" && ext2 != "mp2" && ext2 != "wma" && ext2 != "ogg" )
	{
		alert('����Ȯ���ڰ� mp3,mp2,wma,ogg ������ �ƴմϴ�.\n�� ������ ���ε带 �� �� �����ϴ�.');
		obj.select();
	}
	else
	{
		pos.innerHTML = "<embed src=\"file://" + img + "\">";
	}
}