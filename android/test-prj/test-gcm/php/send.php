<?php
define("GOOGLE_API_KEY", "AIzaSyDuARDciX8KCCzMNO3xnmoN0UEM9ZHjQuc");
define("GOOGLE_GCM_URL", "https://android.googleapis.com/gcm/send");

function send_gcm_notify($reg_id, $message) {

    $fields = array(
        'registration_ids'  => array( $reg_id ),
		'collapse_key'		=> microtime(),
        'data'              => array( "data" => $message ),
    );

    $headers = array(
        'Authorization: key=' . GOOGLE_API_KEY,
        'Content-Type: application/json'
    );

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, GOOGLE_GCM_URL);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

    $result = curl_exec($ch);
    if ($result === FALSE) {
        die('Problem occurred: ' . curl_error($ch));
    }

    curl_close($ch);
    echo $result;
}

include 'regId.php';
if($regId == "")
{
	echo 'Not found regId';
}
else
{
	$reg_id = $regId;
	$msg = "주식 대박 나세요";

	send_gcm_notify($reg_id, $msg);
}


?>