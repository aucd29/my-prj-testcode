<?php
function select($name, &$arrData, $type='one', $overLap=NULL, $dataChk=NULL, $oneBlank=NULL, $js=null)
{
    $dataChk = ($dataChk) ? "msg='$dataChk 선택하세요' required" : '';

    echo "<select name='$name' class='input' $dataChk $js>";

    if($oneBlank)
        if($oneBlank==1)
            echo "<option value=''> 선택하세요";
        else
            echo "<option value=''> $oneBlank";

    if($type == 'one')
    {
        foreach($arrData as $value) 
        {
            if(isset($overLap)) $selected = ($overLap == $value) ? 'selected' : '';
            echo "<option value='$value' $selected>$value";
        }
    }
    else
    {
        foreach($arrData as $stData => $value) 
        {
            if(isset($overLap)) $selected = ($overLap == $stData) ? 'selected' : '';
            echo "<option value='$stData' $selected>$value";
        }
    }        
    echo "</select>";
}
?>