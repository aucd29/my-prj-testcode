<?php
function radio($name, $arrData, $type = 'one', $prevData = NULL, $sADD=NULL)
{
    if($type == 'one')
    {
        foreach($arrData as $index => $value) 
        {
            $chk = ($prevData == $value||(!$index&&!$prevData)) ? 'CHECKED' : '';
            echo "<input type='radio' name='$name' value='$value' class='inputradio' $chk $sADD id='$name$index' onClick='this.blur()'> <label for='$name$index' onFocus='this.blur()'>$value</label>";
        }
    }
    else
    {
        $i=0;
        foreach($arrData as $item => $value) 
        {
            //echo $i.$prevData;
            $chk = ($prevData == $item||(!$i&&!$prevData)) ? 'CHECKED' : '';
            echo "<input type='radio' name='$name' value='$item' class='inputradio' $chk $sADD id='$name$i' onClick='this.blur()'> <label for='$name$i' onFocus='this.blur()'>$value</label>";
            ++$i;
        }
    }
}
?>