<?php
/***************************************************************************
 *
 *		Date			: 2004-10-01
 *		Copyright		: InterKorea(Kurome)
 *		E-mail			: aucd29@daum.net, help@mota.co.kr
 *		Type			:
 *
 *		ī���� �ν��� ����.
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

// ������ ������ �ν��� �� ��
if(is_dir('./data')) msg('���� ���丮�� �����ϴ�. ./data�� �����ؾ��մϴ�.', null); // close, none
if(is_file("./data/ok.php")) msg('�̹� �ν��� �Ǿ��ٳ�.', null); // close, none

@chmod('./data', 0777);

msg('�Ϸ�', './admin/'); // close, none
?>