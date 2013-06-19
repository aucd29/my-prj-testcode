var oldv=""
function number_format(tx)
{
	if(oldv==tx.value) return;
	oldv=tx.value;
	tx.value=comma(oldv);
}

function comma(s)
{
	s=s.replace(/\D/g,"");
	l=s.length-3;
	while(l>0) {
		s=s.substr(0,l)+","+s.substr(l);
		l-=3;
	}
	return s;
}
// <input name="bid_sum" type="text"  onKeyUp='comma(this)' size="23" maxlength="23">
// onKeyUp only