<?php
// +----------------------------------------------------------------------+
// | 2004-04-27 | function, category                                      |
// +----------------------------------------------------------------------+
// | Authors: Kurome <aucd29@kornet.net>                                  |
// +----------------------------------------------------------------------+

// return bool 현재 카테고리가 존재하는지 검사
function categoryChk($sTable, $nCategoryID, $sName)
{
    global $dbconn;

    $sWhere = "nParent = $nCategoryID AND sName = '$sName'";

    $sql = "SELECT COUNT(*) FROM $sTable $sWhere";
    $res = mysql_query($sql);
    if(!$res)
    {
        echo '<font color=red style=font-size:9pt>'.mysql_error().'<br>'.$sql.'</font>';
        exit;
    }
    $rs = mysql_fetch_array($res, MYSQL_ASSOC);
    mysql_free_result($res);

    if($rs[0])
    {
        return TRUE;
    }
    else
    {
        return FALSE;
    }
}


// return *char no 값을 받아서 sCatePath를 넘긴다.
function noCatePathFind($sTable, $nCategoryID)
{
    $sWhere = "nCategoryID=$nCategoryID";
    $sql = "SELECT sCatePath FROM $sTable WHERE $sWhere";
    $res = mysql_query($sql);
    if(!$res)
    {
        echo '<font color=red style=font-size:9pt>'.mysql_error().'<br>'.$sql.'</font>';
        exit;
    }
    $rs = mysql_fetch_row($res);
    mysql_free_result($res);

    return $rs[0];
}

function pathToCategoryID()
{
    $sWhere = "nCategoryID=$nCategoryID";
    $sql = "SELECT sCatePath FROM $sTable WHERE $sWhere";
    $res = mysql_query($sql);
    if(!$res)
    {
        echo '<font color=red style=font-size:9pt>'.mysql_error().'<br>'.$sql.'</font>';
        exit;
    }
    $rs = mysql_fetch_row($res);
    mysql_free_result($res);

    return $rs[0];
}




// 현재 자기 위치값을 내준다.
function selfFind( $sFirstWord, $sTable, $nCategoryID )
{
    $sWhere = "nCategoryID = $nCategoryID";
    
    $sql = "SELECT sCatePath FROM $sTable WHERE $sWhere";
    $res = mysql_query($sql);
    if(!$res)
    {
        echo '<font color=red style=font-size:9pt>'.mysql_error().'<br>'.$sql.'</font>';
        exit;
    }
    $rs = mysql_fetch_row($res);
    mysql_free_result($res);
    $aCatepath = explode('>>', $rs[0]);

    foreach( $aCatePath as $index => $value ) 
    {
        if( !$index )
        {
            echo "<a href='/'>$sFirstWord</a> &gt;&gt; ";
        }
        else
        {
            /*
            $nCategoyID = pathToCategoryID( $value );
            echo "<a href='$PHP_SELF?nCategoryID=$nCategoryID'>$value</a> &gt;&gt; ";
            
            if( $sItemName )
            {
                echo " <b>$sItemName 상품자세히보기</b>";
            }*/
        }
    }
}
?>