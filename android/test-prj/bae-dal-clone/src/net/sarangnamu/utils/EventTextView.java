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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * ONCLICK 이벤트를 받는 TextView 래퍼 클래스
 * 
 * @author kurome
 *
 */
public class EventTextView extends TextView implements OnClickListener {
	protected Activity activity = null;
	protected Intent intent = null;

	public EventTextView(Context context) {
		super(context);
	}

	public EventTextView(Activity activity, String text, Intent intent) {
		super(activity.getApplicationContext());

		this.activity = activity;
		setOnClickListener(this);
		setText(text);
		setIntent(intent);
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}
	
//	public void setTextColor(long color) {
//		setTextColor(color);
//	}

	@Override
	public void onClick(View v) {
		if (intent == null || activity == null) {
			return ;
		}

		activity.startActivity(intent);
	}
}
