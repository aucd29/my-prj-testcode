document.onmousedown = click;
document.onkeydown   = CheckKeyPress;
document.onkeyup	 = CheckKeyPress;

function click()
{
	if((event.button==2) || (event.button==3)) 
	{
		return false;
	}
}

function CheckKeyPress()
{
	ekey = event.keyCode;

	if(ekey == 38 || ekey == 40 || ekey == 78 || ekey == 112 || ekey ==17 || ekey == 18 || ekey == 25 || ekey == 116 || ekey == 122)
	{
		alert("�Է��Ͻ� Ư��Ű�� ����Ҽ� �����ϴ�.");
		event.keyCode = 0;
		return false;
	}
}