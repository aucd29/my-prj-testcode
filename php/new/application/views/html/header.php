<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html lang="ko">
<head>
<title>Sarangnamu</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<meta name="keywords" content="sarangnamu, php, embedded, linux, 최철동, kurome, aucd29, MFC, android, windows mobile">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=Ubuntu">
<link rel="stylesheet" href="/include/css.css" type="text/css">
<script language="javascript" src="/include/main.js"></script>
<script language="javascript" src="/include/js/login.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.0.0.js"></script>

<script type="text/javascript">
var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-20236035-1']);
_gaq.push(['_trackPageview']);

(function() {
var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();


WebFontConfig = {
google: { families: [ 'Ubuntu' ] }
};
(function() {
var wf = document.createElement('script');
wf.src = ('https:' == document.location.protocol ? 'https' : 'http') +
	'://ajax.googleapis.com/ajax/libs/webfont/1/webfont.js';
wf.type = 'text/javascript';
wf.async = 'true';
var s = document.getElementsByTagName('script')[0];
s.parentNode.insertBefore(wf, s);
})();
</script>
</head>
<body style="margin:20px 0px 0px 0px">

<!-- Login Form Load -->
<div id="Login">
<?php require $this->input->server('DOCUMENT_ROOT').'/member/_login.php';?>
</div>

<!-- Big title -->
<div class='BigTitle'>Sarangnamu.net June 17, 2003</div>

<!-- Main top menu -->
<div id="TopMenu">
	<a href="/">Home</a>
	<?php
	if(!$this->input->cookie('sMemberID'))
	{
		echo '<a href="Javascript:LoginShow()">Login</a>';
	}
	else
	{
		echo '<a href="/member/Logout.php">Logout</a>';
	}

	$aMenu = array(
		'/profile/' => 'Profile',
		'/basic/basic_list.php'		 => 'Study',
		'/OpenProject/' => 'Open Project',
		'/flickr/' => 'My Flickr',
	);

	if($this->input->cookie('sMemberID'))
	{
		$aMenu['/address/basic_list.php'] = 'Address';
	}

	foreach($aMenu as $index => $value) echo '<a href="'.$index.'">'.$value.'</a>  ';
	?>

	<a href="http://code.google.com/hosting/" target="_blank">Google Code</a>
</div>
