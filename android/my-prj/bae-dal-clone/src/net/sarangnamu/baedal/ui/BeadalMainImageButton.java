package net.sarangnamu.baedal.ui;

import android.app.Activity;
import android.content.Intent;
import net.sarangnamu.baedal.MarketList;
import net.sarangnamu.utils.EventImageButton;

public class BeadalMainImageButton extends EventImageButton {
	public BeadalMainImageButton(Activity activity, int id, int title, int category) {
		super(activity, id, null);

		intent = new Intent(activity.getApplicationContext(), MarketList.class);
		intent.putExtra(MarketList.TITLE, title);
		intent.putExtra(MarketList.CATEGORY, category);
	}
}
