function GoTop() 
{
window.scrollTo(0,0);
}

function GoTop_Call() 
{
document.body.ondblclick = Top;	
}

var Xpos = 0;
var Ypos = 0;
var Ygravity = 0.85;
var scrollPos = 0;
var oldScrollPos = 0;

function FloatMenu()
{
docWidth	 = document.body.clientWidth; // Update document width
docHeight	 = document.body.clientHeight; // Update document height
oldScrollPos = scrollPos;
scrollPos	 = document.body.scrollTop; // Update scrollbar position

Xpos = 795;
//Xpos = (docWidth - floater.offsetWidth) - 10; //오른쪽 여백 배너가 오른쪽에 붙을경우
//Xpos = (docWidth / 2) + 410; // 중앙정렬일경우. " + 410 은 테이블 크기 / 2 "
Yboundary = ((scrollPos + docHeight) - floater.offsetHeight) - 15; //하단여백

if (floater.offsetTop < Yboundary - 1) // Object is behind boundary
Ypos += 2;

if (floater.offsetTop > Yboundary + 1) // Object is past boundary
Ypos -= 1;

Ypos *= Ygravity; // Slow object down

floater.style.pixelLeft = Xpos;
floater.style.pixelTop += Ypos; // Make object bounce
}

window.setInterval("FloatMenu()", 1); //매개변수중 뒷쪽 숫자는 내려오는 속도