<?php
/***************************************************************************
 *
 *		Date			: 2004-10-01
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			:
 *
 *		카운터 인스톨 하자.
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

// 파일이 있으면 인스톨 한 것
if(is_dir('./data')) msg('저장 디렉토리가 없습니다. ./data를 생성해야합니다.', null); // close, none
if(is_file("./data/ok.php")) msg('이미 인스톨 되엇꾸나.', null); // close, none

@chmod('./data', 0777);

msg('완료', './admin/'); // close, none
?>