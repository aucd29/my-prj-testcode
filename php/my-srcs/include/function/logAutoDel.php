<?php
// +----------------------------------------------------------------------+
// | 2004-01-26 | �α����� �����ϱ�                                       |
// +----------------------------------------------------------------------+
// | Authors: Kurome <aucd29@kornet.net>                                  |
// +----------------------------------------------------------------------+

// ������ �ð� �д���
$nAutoOutTime = '86400';	// 1DAY = 86400

function autoDel($sRoot)
{
    global $nAutoOutTime;

    $objDir = dir($sRoot);
    while (false !== ($data = $objDir->read()))
    {
        if($data != '.' && $data != '..')
        {
            $nFileTime = filemtime($sRoot.'/'.$data);
            $nTmp      = time() - $nFileTime;

            if($nTmp > ($nAutoOutTime)) 
            {
                @unlink("$sRoot/$data");
            }
        }   
    }
    $objDir->close();
}



// ���� �Խ����� ����� (Ŵ������)
if(is_dir($_SERVER[DOCUMENT_ROOT].'/board'))
{
    // �Խ��� �α��� ���丮
    $sBoardRoot = $_SERVER[DOCUMENT_ROOT].'/board/member/login_user';
    autoDel($sBoardRoot);
}

// �Ϲ� �α�����ġ
$sLoginRoot = $_SERVER[DOCUMENT_ROOT].'/member/login_user';
autoDel($sLoginRoot);

$sLoginRoot = $_SERVER[DOCUMENT_ROOT].'/config/login';  // ������ �α��� ����
autoDel($sLoginRoot);
?>