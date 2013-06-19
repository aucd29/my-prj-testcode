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

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ImageListView extends LinearLayout {
	private ImageView image;
	private ArrayList<TextView> text;

	/**
	 * 레이아웃을 설정한다.
	 *
	 * @param context parent 가 될 context를 설정한다.
	 * @param id res/layout 에 사용자가 정의한 xml layout을 설정 한다.
	 */
	ImageListView(final Context context, final int id) {
		super(context);

		// inflater
		// xml 문서에 정의된 레이아웃과 차일드 뷰의 속성을 읽어 실제 뷰 객체를 생성한다.
		// 레이아웃의 정보대로 객체를 생성하고, 속성 변경 메소드를 순서대로 호출한다.
		//

		// xml 로 구성된 layout을 설정한 뒤 설정한 layout 형태로 list를 한땀 한땀 만든다.
		//
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(id, this, true);

		text = new ArrayList<TextView>();
	}


	/**
	 * res/layout 에 대칭되는 id 와 string 을 설정 한다. 이는 현재 view 가 되는
	 * 데이터를 출력하기 위함이다.
	 *
	 * @param id list 에 순서
	 * @param data list 순서에 해당하는 데이터
	 */
	public void setText(final int id, final String data) {
		TextView tv;
		tv = (TextView)findViewById(id);
		tv.setText(data);

		text.add(tv);
	}


	/**
	 *
	 * @param pos
	 * @param data
	 */
	public void resetText(final int pos, final String data) {
		text.get(pos).setText(data);
	}


	/**
	 *
	 * @param imageViewId
	 * @param drawable
	 */
	public void setImage(final int imageViewId, final Drawable drawable) {
		image = (ImageView)findViewById(imageViewId);
		image.setImageDrawable(drawable);
	}

}