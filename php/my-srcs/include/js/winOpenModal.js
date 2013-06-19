function winOpenModal(sURL, sName, w,h)
{
	var bResult;
    bResult = window.showModalDialog(sURL,sName,"dialogHeight:"+h+"px;dialogWidth:"+w+"px;resizable:no;center:yes;help:no;status:no");
}