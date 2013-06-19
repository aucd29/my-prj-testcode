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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * ONCLICK 의 이벤트를 받는 ImageButton 래퍼 클래스
 * 
 * @author kurome
 *
 */
public class EventImageButton extends ImageButton implements OnClickListener {
	protected Activity activity = null;
	protected Intent intent = null;

	public EventImageButton(Context context) {
		super(context);
	}

	public EventImageButton(Activity activity, int id, Intent intent) {
		super(activity.getApplicationContext());

		this.activity = activity;
		setOnClickListener(this);
		setImageResource(id);
		setIntent(intent);
		setBackgroundResource(0);
		setPadding(0, 0, 0, 0);
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	@Override
	public void onClick(View v) {
		if (intent == null || activity == null) {
			return ;
		}

		activity.startActivity(intent);
	}

}
