package net.sarangnamu.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * ������ �̹����� �̿��� �̹��� ������ �ٸ� ������ �� �ְ� �Ѵ�.
 * 
 * @author kurome
 *
 */
public class ImageRatingBar {
	private int full = -1, half = -1, empty = -1;
	private LinearLayout layout;
	private static final int MAX = 5;
	
	public ImageRatingBar(LinearLayout parent) {
		parent.removeAllViews();
		this.layout = parent;		
	}

	/**
	 * �̹��� �� �����Ѵ�.
	 * 
	 * @param full
	 * @param half
	 * @param empty
	 */
	public void setImage(int full, int half, int empty) {
		this.full = full;
		this.half = half;
		this.empty = empty;
	}
	
	/**
	 * ���� ���� �̹����� ����Ѵ�.
	 * 
	 * @param context
	 * @param value
	 */
	public void draw(Context context, double value) {
		if (full == -1 || half == -1 || empty == -1) {
			return ;
		}
		
    	for (int i=0; i<MAX; ++i) {
    		ImageView image = new ImageView(context);
    		
    		if (value > 0.9) {
    			image.setImageResource(full);
    		} else if (value > 0.4) {
    			image.setImageResource(half);
    		} else {
    			image.setImageResource(empty);
    		}
    		
    		value -= 1.0f;
    		layout.addView(image);
    	}
	}
}
