<?php
/***************************************************************************
 *
 *		Date			: 2004-10-30
 *		Copyright		: Kurome
 *		E-mail			: aucd29@daum.net
 *		Type			:
 *
 *
 *
 ***************************************************************************/

// Init
$sOverColor = '#ffffff';
$nYear  = (!$_GET[nYear])  ? date(Y) : $_GET[nYear];
$nMonth = (!$_GET[nMonth]) ? date(m) : $_GET[nMonth];
$nDay   = (!$_GET[nDay])   ? date(d) : $_GET[nDay];

// Class
require "../global.php";
require $_SELF[CLS].'/class.Calendar2.php';

$nBackYear  = $objCalendar->change('1', $sAppendix);
$nBackMonth = $objCalendar->change('2', $sAppendix);
$nNextMonth = $objCalendar->change('3', $sAppendix);
$nNextYear  = $objCalendar->change('4', $sAppendix);

$sAppendix = null;
require $_SELF[RES]."/calendar/patten1.html";
?>