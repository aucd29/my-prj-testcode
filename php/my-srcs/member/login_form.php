<?php
$url  = $_SERVER[REQUEST_URI];
$imgRoot = '/member/skin/ilovepear';

// 로그인 전
if(!$member_sid)
{
?>
<style>
    .loginForm{background-color:F4F8EE; border:1px #7EB03D solid; width:105; font-size:9pt}
</style>

<form name='loginform' method='POST' action='/Process/member_process.php?psMode=LOGIN' onsubmit='return SoftCheck(this);'  style='margin:0;border=0'>

    <input type='hidden' name='url' value='<?php echo $url;?>' />

    <table border='0' cellpadding='0' cellspacing='0' height='115' align=center width='170'>
    <tr bgcolor='#9EC735'>
        <td background='<?php echo $imgRoot;?>/login_back.gif' style='padding:7 0 0 0'>

            <table cellspacing='0' cellpadding='0' border='0' width='170' align='center'>
            <tr>
                <td align='right' style='padding:0 30 0 0' colspan='2'><input type='image' src='/<?php echo $imgRoot;?>/btn_login.gif' /></td>
            </tr>
            <tr>
                <td height='10' colspan='2'></td>
            </tr>
            <tr>
                <td style='padding:0 0 0 16' width='25'><img src='/<?php echo $imgRoot;?>/id.gif' border='0' onFocus='this.blur()' />&nbsp;</td>
                <td width='100'><input type=text name='memberid' size='10' maxlength='15' msg='아이디를 입력해주세요' required  class='loginForm'></td>
            </tr>
            <tr> 
                <td style='padding:0 0 0 16' width='25'><img src='/<?php echo $imgRoot;?>/pw.gif' border='0' onFocus='this.blur()' />&nbsp;</td>
                <td width='100'><input type='password' name='passwd' size='10' maxlength='8' msg='비밀번호를 입력해주세요' required   class='loginForm'></td>
            </tr>
            <tr>
                <td colspan='2' height='5'></td>
            </tr>
            <tr> 
                <td align='center' colspan='2'>

                    <img src='/<?php echo $imgRoot;?>/dot.gif' border='0' onFocus='this.blur()' />
                    <a href='/member/member_write.php' class='white'>회원가입</a>&nbsp;

                    <img src='/<?php echo $imgRoot;?>/dot.gif' border='0' onFocus='this.blur()' />
                    <a href="JavaScript:winOpen('/member/search_id.php','FIND_ID_PW','400','300')" class='white'>ID/PW찾기</a>
                
                </td>
            </tr>
            </table>

        </td>
    </tr>				
    </table>

</form>

<?php
} else {
?>

<table border='0' cellpadding='0' cellspacing='0' height='115' align=center width='170'>
<tr bgcolor='#9EC735'>
    <td background='/<?php echo $imgRoot;?>/login_back.gif' style='padding:10 0 0 0'>

        <table cellspacing='0' cellpadding='0' border='0'  width='100%' height='90' align='center'>
        <tr>
            <td align='right' style='padding:0 30 0 0'><a href='/Process/member_process.php?psMode=LOGOUT'><img src='/<?php echo $imgRoot;?>/btn_logout.gif' onFocus='this.blur()' border='0' /></a></td>
        </tr>
        <tr>
            <td height='10'></td>
        </tr>
        <tr>
            <td align='center'>
            
                <b><?php echo $_COOKIE[member_name]; ?></b>님 로그인 중
                <br />

                <?php 
                if($_COOKIE[member_level] == 'admin')
                {
                    echo "
                    <a href='/superadmin/'>[관리자페이지]</a><br />";
                }    
                ?>

                <a href='/member/member_write.php?mode=modify'>[정보변경]</a>
            
            </td>
        </tr>
        </table>

    </td>
</tr>				
</table>

<?php } ?>