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
	 * ���̾ƿ��� �����Ѵ�.
	 *
	 * @param context parent �� �� context�� �����Ѵ�.
	 * @param id res/layout �� ����ڰ� ������ xml layout�� ���� �Ѵ�.
	 */
	ImageListView(final Context context, final int id) {
		super(context);

		// inflater
		// xml ������ ���ǵ� ���̾ƿ��� ���ϵ� ���� �Ӽ��� �о� ���� �� ��ü�� �����Ѵ�.
		// ���̾ƿ��� ������� ��ü�� �����ϰ�, �Ӽ� ���� �޼ҵ带 ������� ȣ���Ѵ�.
		//

		// xml �� ������ layout�� ������ �� ������ layout ���·� list�� �Ѷ� �Ѷ� �����.
		//
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(id, this, true);

		_text = new ArrayList<TextView>();
	}


	/**
	 * res/layout �� ��Ī�Ǵ� id �� string �� ���� �Ѵ�. �̴� ���� view �� �Ǵ�
	 * �����͸� ����ϱ� �����̴�.
	 *
	 * @param id list �� ����
	 * @param data list ������ �ش��ϴ� ������
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