<?php
// +----------------------------------------------------------------------+
// | 2004-04-27 | function, category                                      |
// +----------------------------------------------------------------------+
// | Authors: Kurome <aucd29@kornet.net>                                  |
// +----------------------------------------------------------------------+

// return bool ���� ī�װ��� �����ϴ��� �˻�
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


// return *char no ���� �޾Ƽ� sCatePath�� �ѱ��.
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




// ���� �ڱ� ��ġ���� ���ش�.
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
                echo " <b>$sItemName ��ǰ�ڼ�������</b>";
            }*/
        }
    }
}
?>