<?php
class sendMail
{
	// 보내는사람, 보낸사람메일, 받는사람메일, 제목, 내용, 첨부파일
	function mail($sFrom, $sFromName, $sTo, $sTitle, $sContent, $sAttachFile=NULL)
	{
		// 파일첨부
		if($sAttachFile)
		{
			$sFileName = $sAttachFile[name];
			$fp		   = fopen($sAttachFile[tmp_name], 'rb');
			$sFile	   = fread($fp, $sAttachFile[size]);
			$sFileType = $sAttachFile[type];
			fclose($fp);

			$boundary = "--------";
			// Multipart/mixed 일경우 첨부파일이 있다는 것을 의미한다.
			$sHeader .= "MIME-Version: 1.0\r\n";
			$sHeader .= "Content-Type: Multipart/mixed; boundary = \"".$boundary."\"\r\n";

			// 본문 파트를 작성한다.
			$sContents = "This is a multi-part message in MIME format.\r\n\r\n";
			$sContents .= "--".$boundary."\r\n";
			$sContents .= "Content-Type: text/html; charset=\"euc-kr\"\r\n";
			$sContents .= "Content-Transfer-Encoding: base64\r\n\r\n";
			$sContents .= base64_encode($sContent)."\r\n\r\n";

			// 파일첨부 파트를 작성한다.
			$sContents .= "--".$boundary."\r\n";
			$sContents .= "Content-Type: ".$sFileType."; name=\"".$sFileName."\"\r\n";
			$sContents .= "Content-Transfer-Encoding: base64\r\n";
			$sContents .= "Content-Disposition: attachment; filename=\"".$sFileName."\"\r\n\r\n";
			$sContents .= base64_encode($sFile)."\r\n\r\n";

			// 멀티파트 종료를 작성한다.
			$sContents .= "--".$boundary."--\r\n";
		}
		else
		{
			$sContents = $sContent;
		}

		$sHeader .= "Return-path : {$sFromName} <$sFrom> \r\n";		// 보내는사람 메일주소
		$sHeader .= "From : {$sFromName} <$sFrom> \r\n";			// 보내는사람 메일주소
		$sHeader .= "Replay-To : {$sFromName} <$sFrom> \r\n";		// 보내는사람 이름
		$sHeader .= "InterKorea : Kurome \r\n";
		$sHeader .= "Content-Type:text/html;charset=euc-kr \r\n";
		$sHeader .= "X-Priority: 1\n";								// 긴급메세지 표시
		$sHeader .= "Content-Transfer-Encoding: 8Bit\n";
		$sHeader .= "\n\n";

		if(mail($sTo, $sTitle, $sContents, $sHeader))
			return true;
		else
			return false;
	}
}

if(!$objMail) $objMail = & new sendMail;
?>