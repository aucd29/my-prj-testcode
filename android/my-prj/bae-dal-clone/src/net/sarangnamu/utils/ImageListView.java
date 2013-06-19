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

		text = new ArrayList<TextView>();
	}


	/**
	 * res/layout �� ��Ī�Ǵ� id �� string �� ���� �Ѵ�. �̴� ���� view �� �Ǵ�
	 * �����͸� ����ϱ� �����̴�.
	 *
	 * @param id list �� ����
	 * @param data list ������ �ش��ϴ� ������
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