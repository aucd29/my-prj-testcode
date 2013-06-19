<?php
/***************************************************************************
 *
 *		Date			: 2004-06-18
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			: function
 *		
 *		Folder Remover
 *
 ***************************************************************************/

include $_SERVER[DOCUMENT_ROOT].'/include/function/file/delDir.php';

$remove = $_SERVER[DOCUMENT_ROOT].'/c';
delDir($remove);
@rmdir('./c');
@unlink("./make.php");
@unlink("./remove.php");
?>