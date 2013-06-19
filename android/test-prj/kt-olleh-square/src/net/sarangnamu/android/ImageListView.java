/*
 *	Copyright 2011 cheol-dong, choi, twitter @aucd29
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package net.sarangnamu.android;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ImageListView extends LinearLayout {

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

		_text = new ArrayList<TextView>();
	}


	/**
	 * res/layout 에 대칭되는 id 와 string 을 설정 한다. 이는 현재 view 가 되는
	 * 데이터를 출력하기 위함이다.
	 *
	 * @param id list 에 순서
	 * @param data list 순서에 해당하는 데이터
	 */
	public void setText(final int id, final String data) {
		//Log.d("@log", "ImageListView");

		TextView tv;
		tv = (TextView)findViewById(id);
		tv.setText(data);

		//Log.d("@log", "ImageListView setText ok");

		_text.add(tv);
	}


	/**
	 *
	 * @param pos
	 * @param data
	 */
	public void resetText(final int pos, final String data) {
		_text.get(pos).setText(data);
	}


	/**
	 *
	 * @param imageViewId
	 * @param drawable
	 */
	public void setImage(final int imageViewId, final Drawable drawable) {
		_image = (ImageView)findViewById(imageViewId);
		_image.setImageDrawable(drawable);
	}


	/*
	 * Attributes
	 */
	private ImageView _image;

	private ArrayList<TextView> _text;
}