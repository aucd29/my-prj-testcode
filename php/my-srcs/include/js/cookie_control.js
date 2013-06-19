// ÄíÅ° ÀÔ·Â gnu_board
function set_cookie(name, value, expirehours) 
{
	var today = new Date();
	today.setTime(today.getTime() + (60*60*1000*expirehours));
	document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + today.toGMTString() + ";";
}

// ÄíÅ° ¾òÀ½
function get_cookie(name) 
{
    var find_sw = false;
    var start, end;
    var i = 0;

	for (i=0; i<= document.cookie.length; i++)
	{
		start = i;
		end = start + name.length;

		if(document.cookie.substring(start, end) == name) 
		{
			find_sw = true
			break
		}
	}

    if (find_sw == true) 
	{
        start = end + 1;
        end = document.cookie.indexOf(";", start);

        if(end < start)
            end = document.cookie.length;

        return document.cookie.substring(start, end);
    }
    return "";
}

// ÄíÅ° Áö¿ò
function delete_cookie(name) 
{
	var today = new Date();

	today.setTime(today.getTime() - 1);
	var value = getCookie(name);
	if(value != "")
		document.cookie = name + "=" + value + "; path=/; expires=" + today.toGMTString();
}