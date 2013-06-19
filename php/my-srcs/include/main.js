/***************************************************************************
 *
 *		Date			: 2004-10-30
 *		Copyright		: Kurome
 *		E-mail			: aucd29@daum.net
 *		Type			:
 *
 *		default javascript
 *
 *		project: lib.validate.js ver 0.6.1
 *		author: jstoy project	`SoftCheck`
 *
 ***************************************************************************/

/*
option
a. hname='str'														: element name
b. required='required'												: element is require
c. requirenum='2'													: element group define
<input type="text" name="test1" required="test" requirenum="2">
<input type="text" name="test2" required="test">
<input type="text" name="test3" required="test">

d. option='email|hangul|engonly|number|jumin|bizno|phone|homephone|handphone'
e. match='elementname'	: a=b same
<input type="password" name="pw1" match="pw2">
<input type="password" name="pw2">

f. span='int'	: join value
<input type="text" name="tel1" option="tel" span="3">
<input type="text" name="tel2">
<input type="text" name="tel3">

g. glue(span relation)
<input type="text" name="tel1" option="tel" span="3" glue="-">
<input type="text" name="tel2">
<input type="text" name="tel3">

h. pattern(regular expression)
<input type="text" name="test" pattern="^[123]$">

i. errdo	: after action (text|select|check|radio|file|hidden)
j. errmsg	: user message
trim (string)		: string trim
minbyte(int)		: min element value length
maxbyte(int)		: max element value length

<input type="radio">
a.  b. c. d. e. f. g. h. i. j.
<input type="checkbox">
a.  b. c. d. e. f. g. h. i. j.  k. l.
<select>
a.  b. c. d. e. f. g. h. i. j.
<input type="file">
a.  b. c. d. d.

*/
function submitWin(form)
{
	if(SoftCheck(form))
	{
		window.open('',form.target,'top=3000,left=0,width=100,height=100,scrollbars=no');
		return true;
	}
	else
	{
		return false;
	}
}

function SoftCheck(form, fields) {
	var lah = new FormChecker(form);
	if(fields) lah.setCheckFields(fields);
	var wmf = lah.go();
	if(wmf == false) alert(lah.getErrorMessage());
	return wmf;
}

FormChecker = function(form) {
	this.FUNC_MAP = {
		email		: "this.func_email",
		hangul		: "this.func_hangul",
		engonly		: "this.func_engonly",
		engspace	: "this.func_engspace",
		number		: "this.func_number",
		jumin		: "this.func_jumin",
		bizno		: "this.func_bizno",
		phone		: "this.func_phone",
		homephone	: "this.func_homephone",
		handphone	: "this.func_handphone",
		url			: "this.func_url"
	}
	this.ERR_MSG = {
		system		: "FormChecker Error: ",
		required	: "반드시 입력하셔야 하는 사항입니다.",
		requirenum	: "이 항목들 중에 {requirenum}개 이상의 항목이 입력되어야 합니다.",
		notequal	: "입력된 내용이 일치하지 않습니다.",
		invalid		: "입력된 내용이 형식에 어긋납니다.",
		minbyte		: "입력된 내용의 길이가 {minbyte}Byte 이상이어야 합니다.",
		maxbyte		: "입력된 내용의 길이가 {maxbyte}Byte를 초과할 수 없습니다.",
		mincheck	: "{mincheck}개의 항목이상으로 선택하세요.",
		maxcheck	: "{maxcheck}개의 항목이하로 선택하세요."
	}
	this.ERR_DO = {
		text	: "select focus",
		select	: "focus",
		check	: "focus",
		radio	: "focus",
		file	: "focus",
		hidden	: ""
	}
	this.ERR_SYS = '_SYSERR_';
	this.fields = form.elements;
	this.form = form;
	this.errMsg = "";
}

FormChecker.prototype.setForm = function(form) {
	this.form = form;
}

FormChecker.prototype.setFunc = function(map, func) {
	if(typeof(this.FUNC_MAP[map]) == "string") return;
	this.FUNC_MAP[map] = func;
}

FormChecker.prototype.setCheckFields = function(fields) {
	this.fields = [];
	if(typeof(fields) == 'string')
		this.fields = [this.form.elements[fields]];
	else
		for(var i=0, s=fields.length; i<s; i++)
			this.fields[this.fields.length] = this.form.elements[fields[i]];
}

FormChecker.prototype.setUnCheckFields = function(fields) {
	this.fields = [];
	var _isUnCheckEl;

	if(typeof(fields) == 'string')
		fields = [fields];
	for(var i=0, s=this.form.elements.length; i<s; i++) {
		_isUnCheckEl = false;
		for(var j=0, t=fields.length; j<t; j++) {
			if(this.form.elements[i] == this.form.elements[fields[j]]) {
				_isUnCheckEl = true;
				break;
			}
		}
		if(!_isUnCheckEl) this.fields[this.fields.length] = this.form.elements[i];
	}
}

FormChecker.prototype.setParam = function(el, name, value) {
	el.setAttribute(name, value);
}

FormChecker.prototype.delParam = function(el, name) {
	el.removeAttribute(name);
}

FormChecker.prototype.go = function() {
	for(var i=0,s=this.fields.length; i<s; i++) {
		var el = this.fields[i];
		if(!this.isValidElement(el)) continue;

		var elType		= this.getType(el);
		var trim		= el.getAttribute("TRIM");
		var required	= el.getAttribute("REQUIRED");
		var requirenum	= el.getAttribute("REQUIRENUM");
		var minbyte		= parseInt(el.getAttribute("MINBYTE"),10);
		var maxbyte		= parseInt(el.getAttribute("MAXBYTE"),10);
		var mincheck	= parseInt(el.getAttribute("MINCHECK"),10);
		var maxcheck	= parseInt(el.getAttribute("MAXCHECK"),10);
		var option		= el.getAttribute("OPTION");
		var match		= el.getAttribute("MATCH");
		var span		= el.getAttribute("SPAN");
		var glue		= el.getAttribute("GLUE");
		var pattern		= el.getAttribute("PATTERN");

		if(trim != null && (elType == "text" || elType == "hidden")) {
			switch (trim) {
			case "trim":
				el.value = el.value.replace(/^\s+/, "").replace(/\s+$/, "");
				break;
			case "compress":
				el.value = el.value.replace(/\s+/, "");
				break;
			case "ltrim":
				el.value = el.value.replace(/^\s+/, "");
				break;
			case "rtrim":
				el.value = el.value.replace(/\s+$/, "");
				break;
			}
		}

		var elEmpty = this.isEmpty(el, elType);

		if(required != null) {
			if(required == "required") {
				if(elEmpty) return this.raiseError(el, "required");
			} else {
				requirenum = parseInt(requirenum, 10);
				var _num = 0;
				var _name = [];
				if(requirenum > 0) {
					for(var j=0; j<this.form.elements.length; j++) {
						var _el = this.form.elements[j];
						if(required == _el.getAttribute("REQUIRED")) {
							if(!this.isEmpty(_el, this.getType(_el))) _num++;
							_name[_name.length] = this.getName(_el);
						}
					}
					if(_num < requirenum)
						return this.raiseError(el, "requirenum", _name.join(", "));
				}
			}
		}
		// 값과 함께 필수일때만 실행하게끔 변경 && (el.value && required == 'required') || el.value
		if((minbyte > 0 || maxbyte > 0) && (elType == "text" || elType == "hidden") && (el.value && required == 'required' || el.value)) {
			var _tmp = el.value;
			var _len = el.value.length;
			for(j=0; j<_tmp.length; j++) {
				if(_tmp.charCodeAt(j) > 128) _len++;
			}
			if(minbyte > 0 && _len < minbyte) return this.raiseError(el, "minbyte");
			if(maxbyte > 0 && _len > maxbyte) return this.raiseError(el, "maxbyte");
		}
		if(match != null && elType != "file") {
			if(typeof this.form.elements[match] == "undefined")
				return this.raiseError(this.ERR_SYS, "Element '"+ match +"' is not found.");
			else if(el.value != this.form.elements[match].value)
				return this.raiseError(el, "notequal");
		}
		if(option != null && !elEmpty && elType != "file") {
			var _options = option.split(" ");
			for(var j in _options) {
				var _func = eval(this.FUNC_MAP[_options[j]]);
				if(span != null) {
					var _value = [];
					for(var k=0; k<parseInt(span,10); k++) {
						try {
							_value[k] = this.fields[i+k].value;
						} catch (e) {
							return this.raiseError(this.ERR_SYS,  (i+k) +"th Element is not found.");
						}
					}
					try {
						var _result = _func(el, _value.join(glue == null ? "" : glue));
					} catch (e) {
						return this.raiseError(this.ERR_SYS,  "function map '"+ _options[j] +"' is not exist.");
					}
					if(_result !== true) return this.raiseError(el, _result);
				} else {
					try {
						var _result = _func(el);
					} catch (e) {
						return this.raiseError(this.ERR_SYS,  "function map '"+ _options[j] +"' is not exist.");
					}
					if(_result !== true) return this.raiseError(el, _result);
				}
			}
		}
		if(pattern != null && !elEmpty && elType != "file") {
			try {
				pattern = new RegExp(pattern);
			} catch (e) {
				return this.raiseError(this.ERR_SYS, "Invalid Regular Expression '"+ pattern +"'");
			}
			if(!pattern.test(el.value)) return this.raiseError(el, "invalid");
		}
		if((mincheck > 0 || maxcheck > 0) && elType == "check") {
			var _checks = this.form.elements[el.name];
			var _num = 0;
			if(_checks.length) {
				for(var j=0; j<_checks.length; j++) {
					if(_checks[j].checked) _num++;
				}
			} else {
				if(_checks.checked) _num++;
			}
			if(mincheck > 0 && _num < mincheck) return this.raiseError(el, "mincheck");
			if(maxcheck > 0 && _num > maxcheck) return this.raiseError(el, "maxcheck");
		}
	}
	return true;
}

FormChecker.prototype.isValidElement = function(el) {
	return el.name && el.tagName.match(/^input|select|textarea$/i) && !el.disabled;
}

FormChecker.prototype.isEmpty = function(el, type) {
	switch (type) {
	case "file": case "text": case "hidden":
		if(el.value == null || el.value == "") return true;
		break;
	case "select":
		if(el.selectedIndex == -1 || el[el.selectedIndex].value == null ||
				el[el.selectedIndex].value == "") return true;
		break;
	case "check": case "radio":
		var elCheck = this.form.elements[el.name];
		var elChecked = false;
		for(var j=0; j<elCheck.length; j++) {
			if(elCheck[j].checked == true) elChecked = true;
		}
		if(elChecked == false) return true;
		break;
	}
	return false;
}

FormChecker.prototype.getType = function(el) {
	switch (el.tagName.toLowerCase()) {
	case "select": return "select";
	case "textarea": return "text";
	case "input":
		switch (el.type.toLowerCase()) {
		case "radio": return "radio";
		case "checkbox": return "check";
		case "file": return "file";
		case "text": case "password": return "text";
		case "hidden": return "hidden";
		}
		break;
	}
}

FormChecker.prototype.raiseError = function(el, type, elName) {
	if(el == this.ERR_SYS) {
		this.errMsg = this.ERR_MSG["system"] + type;
		return false;
	}
	var pattern = /\{([a-zA-Z0-9_]+)\}/i;
	var msg = this.ERR_MSG[type] ? this.ERR_MSG[type] : type;
	var elType = this.getType(el);
	var elName = elName ? elName : this.getName(el);
	var errDo = el.getAttribute("ERRDO") ? el.getAttribute("ERRDO") : this.ERR_DO[elType];
	var _errDos = errDo ? errDo.split(" ") : [];

	if(el.getAttribute("ERRMSG") != null) msg = el.getAttribute("ERRMSG");
	if(pattern.test(msg) == true) {
		while (pattern.exec(msg)) msg = msg.replace(pattern, el.getAttribute(RegExp.$1));
	}
	for(var i in _errDos) {
		switch (_errDos[i]) {
		case "delete": el.value = ""; break;
		case "select": el.select(); break;
		case "focus":  el.focus(); break;
		}
	}
	this.errMsg = "["+ elName +"]\n   - "+ msg +"\n";
	return false;
}

FormChecker.prototype.getErrorMessage = function() {
	return this.errMsg;
}

FormChecker.prototype.getName = function(el) {
	return (el.getAttribute("HNAME") == null || el.getAttribute("HNAME") == "")
		? el.name : el.getAttribute("HNAME");
}
/**
* validate functions
*/
FormChecker.prototype.func_email = function(el,value) {
	var value = value ? value : el.value;
	var pattern = /^[_a-zA-Z0-9-\.]+@[\.a-zA-Z0-9-]+\.[a-zA-Z]+$/;
	return (pattern.test(value)) ? true : "invalid";
}

FormChecker.prototype.func_hangul = function(el) {
	var pattern = /[가-힝]/;
	return (pattern.test(el.value)) ? true : "반드시 한글을 포함해야 합니다";
}

FormChecker.prototype.func_engonly = function(el) {
	var pattern = /^[a-zA-Z]+$/;
	return (pattern.test(el.value)) ? true : "invalid";
}

FormChecker.prototype.func_engspace = function(el) {
	var pattern = /^[a-zA-Z ]+$/;
	return (pattern.test(el.value)) ? true : "영문+공백 조합만 입력가능합니다.";
}

FormChecker.prototype.func_number = function(el) {
	var pattern = /^[0-9]+$/;
	return (pattern.test(el.value)) ? true : "반드시 숫자로만 입력해야 합니다";
}

FormChecker.prototype.func_jumin = function(el,value) {
	var pattern = /^([0-9]{6})-?([0-9]{7})$/;
	var num = value ? value : el.value;
	if(!pattern.test(num)) return "invalid";
	num = RegExp.$1 + RegExp.$2;

	var sum = 0;
	var last = num.charCodeAt(12) - 0x30;
	var bases = "234567892345";
	for(var i=0; i<12; i++) {
		if(isNaN(num.substring(i,i+1))) return "invalid";
		sum += (num.charCodeAt(i) - 0x30) * (bases.charCodeAt(i) - 0x30);
	}
	var mod = sum % 11;
	return ((11 - mod) % 10 == last) ? true : "invalid";
}

FormChecker.prototype.func_bizno = function(el,value) {
	var pattern = /([0-9]{3})-?([0-9]{2})-?([0-9]{5})/;
	var num = value ? value : el.value;
	if(!pattern.test(num)) return "invalid";
	num = RegExp.$1 + RegExp.$2 + RegExp.$3;
	var cVal = 0;
	for(var i=0; i<8; i++) {
		var cKeyNum = parseInt(((_tmp = i % 3) == 0) ? 1 : ( _tmp  == 1 ) ? 3 : 7);
		cVal += (parseFloat(num.substring(i,i+1)) * cKeyNum) % 10;
	}
	var li_temp = parseFloat(num.substring(i,i+1)) * 5 + "0";
	cVal += parseFloat(li_temp.substring(0,1)) + parseFloat(li_temp.substring(1,2));
	return (parseInt(num.substring(9,10)) == 10-(cVal % 10)%10) ? true : "invalid";
}

FormChecker.prototype.func_phone = function(el,value) {
	var pattern = /^(0[2-6][0-5]?|01[01346-9])-?([1-9]{1}[0-9]{2,3})-?([0-9]{4})$/;
	var num = value ? value : el.value;
	return (pattern.exec(num)) ? true : "invalid";
}

FormChecker.prototype.func_homephone = function(el,value) {
	var pattern = /^(0[2-6][0-5]?)-?([1-9]{1}[0-9]{2,3})-?([0-9]{4})$/;
	var num = value ? value : el.value;
	return (pattern.exec(num)) ? true : "invalid";
}

FormChecker.prototype.func_handphone = function(el,value) {
	var pattern = /^(01[01346-9])-?([1-9]{1}[0-9]{2,3})-?([0-9]{4})$/;
	var num = value ? value : el.value;
	return (pattern.exec(num)) ? true : "invalid";
}

FormChecker.prototype.func_url = function(el) {
	var pattern = /[a-zA-z0-9-]+\.[a-zA-Z]+$/;
	return (pattern.test(el.value)) ? true : "도메인형식이 아닙니다.";
}

// My js
function ZipCode(sForm,sZipcode,sZipcode1,sAddr,sTel)
{
	// sForm,sZipcode,sZipcode1,sAddr,sTel,nWhat
	var sURI = '/include/zipcode/zipcode.php?sForm='+sForm+'&sZipcode='+sZipcode+'&sZipcode1='+sZipcode1+'&sAddr='+sAddr+'&sTel='+sTel;
	winOpen(sURI,'zipWin',370,392,'s');
}

function ZipCode0(nWhat)
{
	var sURI = '/include/zipcode/zipcode.php?nWhat='+nWhat;
	winOpen(sURI,'zipWin',370,392,'s');
}

function delChker(url,msg)
{
	var msg2 = msg?msg:'정말로 삭제하시겠습니까? 삭제하시면 복구가 불가능합니다.';
	if(confirm(msg2)) self.location.href=url;
}

function winOpen(ref, name, width, height, opt)
{
	var window_left = (screen.width-width)/2;
	var window_top  = (screen.height-height)/2;
	opt = opt?', scrollbars=yes':'';
	nWin = window.open(ref, name,'width='+width+', height='+height+', top='+window_top+',left='+window_left + opt);
	nWin.focus();
}

function prevImage(obj,pos)
{
	img	= obj.value;
	if (img.length == 0) return;
	idx=img.lastIndexOf("\\");
	filename= img.substring(idx+1);
	idx2=filename.lastIndexOf(".");
	ext=filename.substring(idx2+1);
	ext2=ext.toLowerCase();

	pos = eval(pos);
	if(ext2 != "jpg" && ext2 != "gif" && ext2 != "jpeg" && ext2 != 'swf' && ext2 != 'png')
	{
		alert('파일확장자가 jpg나 gif나 swf 나 png 형식이 아닙니다.\n이 파일은 업로드를 할 수 없습니다.');
		obj.select();
	}
	else
	{
		if(ext2 == 'swf') pos.innerHTML = "<embed src=\"file://" + img + "\">";
		else			  pos.innerHTML = "<img src=\"file://" + img + "\">";
	}
}

function Trbg(obj,c)
{
	if (c)	obj.style.backgroundColor=c;
	else	obj.style.backgroudnColor='';
}

function Jumin(f,s)
{
	var pattern = /^([0-9]{6})-?([0-9]{7})$/;
	var num = f.value + '-' + s.value;
	if(!pattern.test(num))
	{
		alert('주민번호 형식에 맞지 않습니다.');
		f.value = '';
		s.value = '';
		f.focus();
		return;
	}
	num = RegExp.$1 + RegExp.$2;

	var sum = 0;
	var last = num.charCodeAt(12) - 0x30;
	var bases = "234567892345";
	for(var i=0; i<12; i++) {
		if(isNaN(num.substring(i,i+1))) return "invalid";
		sum += (num.charCodeAt(i) - 0x30) * (bases.charCodeAt(i) - 0x30);
	}
	var mod = sum % 11;
	if(!((11 - mod) % 10 == last))
	{
		alert('주민번호 형식에 맞지 않습니다.');
		f.value = '';
		s.value = '';
		f.focus();
		return;
	}
}

function ClearFocus(obj,msg)
{
	if(msg) alert(msg);
	obj.value = '';
	obj.focus();
}