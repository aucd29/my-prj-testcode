var old_menu = '';
function showHide(submenu)
{
	if(old_menu != submenu)
	{
		if(old_menu !='') old_menu.style.display = 'none';
		submenu.style.display = 'block';
		old_menu = submenu;
	}
	else
	{
		submenu.style.display = 'none';
		old_menu = '';
	}
}