/*
 * Copyright (c) 2003-2011, cheol-dong choi, twitter @aucd29
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.sarangnamu.utils;

import net.sarangnamu.activity.BrowserActivity;
import net.sarangnamu.utils.EventImageButton;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * �̹��� ��ư�� �̿��� ����Ʈ ���¸� ������ Ŭ����
 * 
 * @author kurome
 *
 */
public class EventImageButtonList {
	private Activity activity;
	private ViewGroup layout;
	private int seperator = 0;

	public EventImageButtonList(Activity activity) {
		this.activity = activity;
	}

	public void setLayout(final int resId) {
		this.layout = (ViewGroup) activity.findViewById(resId);
	}

	/**
	 * ��ư ���̿� ���� �̹��� ����
	 *  
	 * @param resId ���� �̹����� ���ҽ�
	 */
	public void addSeperator(final int resId) {
		this.seperator = resId;
	}

	/**
	 * ��ư�� �̹����� onclick �� �߻��� �̺�Ʈ intent�� ����
	 * 
	 * @param resId
	 * @param intent
	 */
	public void addButton(final int resId, Intent intent) {
		if (seperator > 0) {
			addImageView(seperator);
		}

		layout.addView(new EventImageButton(activity, resId, intent));
	}
	
	/**
	 * ��ư�� �̹����� onclick �� ������� uri �� ����
	 * 
	 * @param resId
	 * @param uri
	 */
	public void addButtonWithBrowser(final int resId, final String uri) {
		if (seperator > 0) {
			addImageView(seperator);
		}
		
		Intent intent = new Intent(activity.getApplicationContext(), BrowserActivity.class);
		intent.putExtra(BrowserActivity.URI, uri);

		layout.addView(new EventImageButton(activity, resId, intent));
	}


	public void addView(View view) {
		layout.addView(view);
	}


	public void addImageView(int resId) {
		ImageView view = new ImageView(activity.getApplicationContext());
		view.setImageResource(resId);
		layout.addView(view);
	}
}
