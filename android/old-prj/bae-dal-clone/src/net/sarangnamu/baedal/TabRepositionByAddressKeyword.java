package net.sarangnamu.baedal;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TabRepositionByAddressKeyword extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView view = new TextView(this);
		view.setText("Reposition by address keyword");
		setContentView(view);
	}
}
