function GO_selected1()
{
	var check = document.okok;
	var sURL = 'member_write.php';
	if(!check.agree.checked == true)
	{
		if(confirm("회원으로 가입하실려면 회원약관에 동의하시겠습니까?"))
			window.location="./"+sURL;
		else
			window.location="./";
	}

	if(check.agree.checked == true) window.location="./"+sURL;
}

function CheckID(obj)
{
	var ref = 'check_id.php';
	if (!obj.value)
	{
		alert('아이디(ID)를 입력하신 후에 확인하세요!');
		obj.focus();
		return;
	}
	else 
	{
		if (obj.value.length <5)
		{
			alert('아이디는 5자 이상입니다');
			obj.focus();
			return;
		}
		else
		{
			ref = ref + "?sID=" + obj.value;
			winOpen(ref,'chkwin',400,280);
		}
	}
}

function inputbirth()
{
	var obj = document.forms['frm_write'];
	var n = obj.nJumin1.value.substring(0,1);
	var y = obj.nJumin.value.substring(0,2);

	if (n<3)
		obj.nBirthY.value = '19' + y;
	else
		obj.nBirthY.value = '20' + y;

	obj.nBirthM.value = obj.nJumin.value.substring(2,4);
	obj.nBirthD.value = obj.nJumin.value.substring(4,6);

	if (n==1||n==3)
		obj.nSex[0].checked = true;
	else
		obj.nSex[1].checked = true;
}

function NextJumin(obj)
{
	var n = obj.value.length;
	if (n > 5)
	{
		obj.form.nJumin1.focus();
	}
}

var img = new Image();
function ImageSize(obj)
{	
	img.src = obj.value;
	if (img.width > 80 || img.height > 60)
	{
		alert('이미지 사이즈는 가로 80, 세로 60 이하로 해주시기 바랍니다');
		obj.value = '';
		return;
	}
	else 
	{
		prevImage(obj,'prevImages')
	}
}