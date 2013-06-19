//<script language='Javascript' src='/include/poper.js'></script>
//onMouseOver=pop('$content') onMouseOut=kill()

	var nav = (document.layers);
	var iex = (document.all);
	var skn = (nav) ? document.topdeck : topdeck.style;
	if (nav) document.captureEvents(Event.MOUSEMOVE);
	document.onmousemove = get_mouse;

function pop(msg) {
	var content ="<table border=0 cellpadding=5 cellspacing=2 bgcolor=#4AAAC6 width=200><tr bgcolor=white><td>"+msg+"</td></tr></table>";

	if (nav) {
		skn.document.write(content);
		skn.document.close();
		skn.visibility = "visible";
	} else if (iex) {
		document.all("topdeck").innerHTML = content;
		skn.visibility = "visible";
	}
}

function get_mouse(e) {
	var x = (nav) ? e.pageX : event.x+document.body.scrollLeft;
	var y = (nav) ? e.pageY : event.y+document.body.scrollTop;
//	var x = (nav) ? e.pageX : event.x;
//	var y = (nav) ? e.pageY : event.y;
	if (x>660&&x<800&&y>125&&y<200)
	{
	skn.left = x - 215;
	skn.top  = y - 130;
	}
	else {
	skn.left = x - 10;
	skn.top  = y + 10;
	}
}

function kill() {
	skn.visibility = "hidden";
}
