<?php
//SetCookie('regId', $_POST['regId'], 0, '/');

$fp = fopen("./regId.php", w);
fputs($fp, '<?php $regId="'.$_POST[regId].'";?>');
fclose($fp);

?>