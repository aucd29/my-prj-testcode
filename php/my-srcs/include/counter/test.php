<?php
echo $_SERVER[HTTP_USER_AGENT];
ECHO "<BR><BR><BR>";
$aAgentInfo = explode('(compatible;',$_SERVER[HTTP_USER_AGENT]); //[0] => Mozilla/4.0 [1] => MSIE 6.0; Windows NT 5.1) 
$aAgentInfo2 = explode(';', $aAgentInfo[1]);

print_r($aAgentInfo);
echo '<br>';
print_r($aAgentInfo2);

?>