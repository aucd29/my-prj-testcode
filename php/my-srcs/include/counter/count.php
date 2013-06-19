<?php 
/***************************************************************************
 *
 *		Date			: 2004-10-01
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			:
 *
 *		카운트를 올려보자.
 *
 ***************************************************************************/

//
// Default Setting
//
require '../global.php';

//
// Function
//
require $_SELF[FUN].'/common/common.php';

//
// Class
//
require "./include/class.Counter.php";

if(!$_GET[db]) msg('디비명이 없습니다.', null);
?>