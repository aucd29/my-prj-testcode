<?php
// +----------------------------------------------------------------------+
// | 2004-01-26 | 로그파일 삭제하기                                       |
// +----------------------------------------------------------------------+
// | Authors: Kurome <aucd29@kornet.net>                                  |
// +----------------------------------------------------------------------+

// 삭제할 시간 분단위
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



// 파일 게시판이 존재시 (킴스보드)
if(is_dir($_SERVER[DOCUMENT_ROOT].'/board'))
{
    // 게시판 로그인 디렉토리
    $sBoardRoot = $_SERVER[DOCUMENT_ROOT].'/board/member/login_user';
    autoDel($sBoardRoot);
}

// 일반 로그인위치
$sLoginRoot = $_SERVER[DOCUMENT_ROOT].'/member/login_user';
autoDel($sLoginRoot);

$sLoginRoot = $_SERVER[DOCUMENT_ROOT].'/config/login';  // 관리자 로그인 파일
autoDel($sLoginRoot);
?>