// 서브메뉴 변경해주는것
// onMouseOver=change(topsub1)
var old_menu = '';
function change(submenu)
{
	if( old_menu != submenu )
	{
		if( old_menu !='' ) old_menu.style.display = 'none';
		submenu.style.display = '';
		topsub0.style.display = 'none';
		old_menu = submenu;
	}
}