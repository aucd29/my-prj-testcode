package net.sarangnamu.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 임의의 이미지를 이용해 이미지 레이팅 바를 생성할 수 있게 한다.
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
	 * 이미지 를 설정한다.
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
	 * 값에 따른 이미지를 출력한다.
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
