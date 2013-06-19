<?php
function delDir($dir)
{
	$current_dir = @opendir($dir);
	while($entryname = @readdir($current_dir))
	{
		if(is_dir("$dir/$entryname") && ($entryname != "." && $entryname!=".."))
			delDir("${dir}/${entryname}");
		else if($entryname != "." and $entryname!="..")
			@unlink("${dir}/${entryname}");
	}
	@closedir($current_dir);
	@rmdir(${dir});
}
?>