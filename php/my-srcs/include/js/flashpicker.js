/**
 * by 행복한고니 (20041008)
 *
 * Homepage : http://www.mygony.com
 */

var ColorPicker = function() {};

ColorPicker.appendSWF = false;
ColorPicker.show = function(obj, handler) {
	if (ColorPicker.path == undefined) ColorPicker.path = "/include/res/ColorPicker.swf";
	if (ColorPicker.align == undefined) ColorPicker.align = "left";
	if (ColorPicker.handler == undefined) {
		if (handler == undefined) ColorPicker.handler = function(str){};
		else ColorPicker.handler = handler;
	}

	if (ColorPicker.appendSWF == false) ColorPicker.DoAppendSWF();

	var X, Y;
	var pos = ColorPicker.getOffsetPos(obj);
	var W = obj.offsetWidth, H = obj.offsetHeight;
	
	with (ColorPicker) {
		align = align.toLowerCase();
		layer.style.display = "block";
		X = pos.x, Y = pos.y;

		if (align == "left") {
			Y += H;
		} else if (align == "right") {
			X = pos.x - (layer.offsetWidth - W);
			Y += H;
		} else if (align == "top") {
			X += W;
		} else if (align == "middle") {
			X += W;
			Y -= Math.round((layer.offsetHeight-H)/2);
		} else if (align == "bottom") {
			X += W;
			Y -= layer.offsetHeight - H;
		}

		layer.style.top = Y + 'px';
		layer.style.left = X + 'px';
	}
}

ColorPicker.hide = function() {
	if (ColorPicker.appendSWF == false) ColorPicker.DoAppendSWF();

	ColorPicker.layer.style.display = "none";
}

ColorPicker.getOffsetPos = function(obj) {
	if (obj.offsetParent == null) {
		return {"x":obj.offsetLeft, "y":obj.offsetTop};
	} else {
		var pos = ColorPicker.getOffsetPos(obj.offsetParent);
		return {"x":obj.offsetLeft + pos.x, "y":obj.offsetTop + pos.y};
	}
}

ColorPicker.DoAppendSWF = function() {
	var DIV = document.createElement("DIV");
	var BODY = document.getElementsByTagName("BODY")[0];

	DIV.innerHTML = "<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0\" width=\"150\" height=\"130\"><param name=\"movie\" value=\""+ColorPicker.path+"\"><param name=\"quality\" value=\"high\"><embed src=\""+ColorPicker.path+"\" quality=\"high\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" type=\"application/x-shockwave-flash\" width=\"150\" height=\"130\"></embed></object>";
	DIV.style.position = "absolute";
	DIV.style.display = "none";
	ColorPicker.layer = BODY.appendChild(DIV);

	ColorPicker.appendSWF = true;
}