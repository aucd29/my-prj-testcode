function ReadCookie(name)
{
	var label = name + "=";
	var labelLen = label.length;
	var cLen = document.cookie.length;
	var i = 0;

	while (i < cLen) {
		var j = i + labelLen;

		if (document.cookie.substring(i, j) == label) {
			var cEnd = document.cookie.indexOf(";", j);
			if (cEnd == -1) cEnd = document.cookie.length;

			return unescape(document.cookie.substring(j, cEnd));
		}

		i++;
	}
	return "";
}


function SaveCookie(name, value, expire)
{
	var eDate = new Date();
	eDate.setDate(eDate.getDate() + expire);
	document.cookie = name + "=" + value + "; expires=" +  eDate.toGMTString()+ "; path=/";
}


function RefreshStaticMenu()
{
	var stmnStartPoint, stmnEndPoint, stmnRefreshTimer;

	stmnStartPoint = parseInt(STATICMENU.style.top, 10);
	stmnEndPoint = document.body.scrollTop + stmnGAP2;
	if (stmnEndPoint < stmnGAP1) stmnEndPoint = stmnGAP1;

	stmnRefreshTimer = stmnActivateSpeed;

	if ( stmnStartPoint != stmnEndPoint ) {
		stmnScrollAmount = Math.ceil( Math.abs( stmnEndPoint - stmnStartPoint ) / 15 );
		STATICMENU.style.top = parseInt(STATICMENU.style.top, 10) + ( ( stmnEndPoint<stmnStartPoint ) ? -stmnScrollAmount : stmnScrollAmount );
		stmnRefreshTimer = stmnScrollSpeed;
	}

	stmnTimer = setTimeout ("RefreshStaticMenu();", stmnRefreshTimer);
}


function ToggleAnimate()
{
	if (ANIMATE.checked) {
		RefreshStaticMenu();
		SaveCookie("ANIMATE", "true", 300);
	}
	else {
		clearTimeout(stmnTimer);
		STATICMENU.style.top = stmnGAP1;
		SaveCookie("ANIMATE", "false", 300);
	}
}


function InitializeStaticMenu()
{
	if (ReadCookie("ANIMATE") == "false")
	{
		ANIMATE.checked = false;
		STATICMENU.style.top = document.body.scrollTop + stmnGAP1;
	}
	else
	{
		ANIMATE.checked = true;
		STATICMENU.style.top = document.body.scrollTop + stmnBASE;
		RefreshStaticMenu();
	}
}