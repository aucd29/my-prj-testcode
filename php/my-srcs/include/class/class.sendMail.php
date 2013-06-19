<?php
class sendMail
{
	// �����»��, �����������, �޴»������, ����, ����, ÷������
	function mail($sFrom, $sFromName, $sTo, $sTitle, $sContent, $sAttachFile=NULL)
	{
		// ����÷��
		if($sAttachFile)
		{
			$sFileName = $sAttachFile[name];
			$fp		   = fopen($sAttachFile[tmp_name], 'rb');
			$sFile	   = fread($fp, $sAttachFile[size]);
			$sFileType = $sAttachFile[type];
			fclose($fp);

			$boundary = "--------";
			// Multipart/mixed �ϰ�� ÷�������� �ִٴ� ���� �ǹ��Ѵ�.
			$sHeader .= "MIME-Version: 1.0\r\n";
			$sHeader .= "Content-Type: Multipart/mixed; boundary = \"".$boundary."\"\r\n";

			// ���� ��Ʈ�� �ۼ��Ѵ�.
			$sContents = "This is a multi-part message in MIME format.\r\n\r\n";
			$sContents .= "--".$boundary."\r\n";
			$sContents .= "Content-Type: text/html; charset=\"euc-kr\"\r\n";
			$sContents .= "Content-Transfer-Encoding: base64\r\n\r\n";
			$sContents .= base64_encode($sContent)."\r\n\r\n";

			// ����÷�� ��Ʈ�� �ۼ��Ѵ�.
			$sContents .= "--".$boundary."\r\n";
			$sContents .= "Content-Type: ".$sFileType."; name=\"".$sFileName."\"\r\n";
			$sContents .= "Content-Transfer-Encoding: base64\r\n";
			$sContents .= "Content-Disposition: attachment; filename=\"".$sFileName."\"\r\n\r\n";
			$sContents .= base64_encode($sFile)."\r\n\r\n";

			// ��Ƽ��Ʈ ���Ḧ �ۼ��Ѵ�.
			$sContents .= "--".$boundary."--\r\n";
		}
		else
		{
			$sContents = $sContent;
		}

		$sHeader .= "Return-path : {$sFromName} <$sFrom> \r\n";		// �����»�� �����ּ�
		$sHeader .= "From : {$sFromName} <$sFrom> \r\n";			// �����»�� �����ּ�
		$sHeader .= "Replay-To : {$sFromName} <$sFrom> \r\n";		// �����»�� �̸�
		$sHeader .= "InterKorea : Kurome \r\n";
		$sHeader .= "Content-Type:text/html;charset=euc-kr \r\n";
		$sHeader .= "X-Priority: 1\n";								// ��޸޼��� ǥ��
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