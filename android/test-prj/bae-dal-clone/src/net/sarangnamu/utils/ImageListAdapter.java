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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class ImageListAdapter extends BaseAdapter {
	protected ArrayList<ImageListViewType> list;
	protected ArrayList<Integer> textViewId;
	protected ArrayList<Integer> imageViewId;
	protected Context context;
	protected int listId;

	/**
	 * �ƴ��͸� ���� �Ѵ�.
	 *
	 * @param context parent �� �Ǵ� context
	 * @param id res/layout �� ���� ����Ʈ �����ۿ� �ش��ϴ� xml id
	 */
	public ImageListAdapter(Context context, int id) {
		super();

		this.context = context;
		this.listId = id;
		this.textViewId = new ArrayList<Integer>();
		this.imageViewId = new ArrayList<Integer>();
		this.list = new ArrayList<ImageListViewType>();
	}


	/**
	 * ����Ʈ�� ImageListViewType �� �߰� �Ѵ�.
	 * ImageListViewType list �� 1 row �� �ش��ϴ� ������ �̴�.
	 *
	 * @param list ImageListViewType
	 */
	public void add(ImageListViewType list) {
		this.list.add(list);
	}


	/**
	 * ����Ʈ�� ���� _listId ���� ���ڿ� id ���� ���� �Ѵ�.
	 *
	 *		adapter.addLisViewId(R.id.groupName);
	 *		adapter.addLisViewId(R.id.date);
	 *		adapter.addLisViewId(R.id.contents);
	 *
	 * @param id ���ڿ� id
	 */
	public void addLisViewId(int id) {
		Log.d("addLisViewtId", Integer.toString(id));
		textViewId.add(id);
	}


	/**
	 * ����Ʈ�� ���� _listId ���� �̹���id �� ���� �Ѵ�.
	 *
	 *		adapter.setImageViewId(R.id.imageView);
	 *
	 * @param id �̹��� id
	 */
	public void addImageViewId(int id) {
		imageViewId.add(id);
	}


	/**
	 * �Էµ� _list �� ������ ��ȯ �Ѵ�. �̴� list �� ��ü row �� �̴�.
	 *
	 * @return _list.size() ��
	 */
	@Override
	public int getCount() {
		return list.size();
	}


	/**
	 * ���ϴ� ��ġ�� ImageListViewType �� ��ȯ �Ѵ�.
	 *
	 * @return _list ���� ���ϴ� index �� �ش��ϴ� ImageListViewType ��
	 */
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * android ���� listview �� ���� screen �� ��Ÿ���� list �� �����͸�
	 * show �ϰ� ���Ǿ� �ִ� �̿� ���� screen area �� �ش��ϴ� data �� �䱸��
	 * �� �̸� ��� �ϴµ� �� data�� �䱸�ϴ� �κ��� �ٷ� getView �̴�.
	 *
	 * @param position list �� index ��
	 * @parem convertView list �� ����� �ؾ��� view
	 * @param parent
	 * @return position �� �ش��ϴ�  ImageListView ������
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageListView imageListView;
		Log.d("@log", "position : " + Integer.toString(position));

		if (convertView == null) {
			Log.d("@log", "convertView == null");

			imageListView = new ImageListView(context, listId);

			for (int i=0; i<textViewId.size(); ++i) {
				imageListView.setText(textViewId.get(i), list.get(position).getText(i));
			}
		} else {
			imageListView = (ImageListView) convertView;

			for (int i=0; i<textViewId.size(); ++i) {
				imageListView.resetText(i, list.get(position).getText(i));
			}
		}

		for (int i=0; i<imageViewId.size(); ++i) {
			imageListView.setImage(imageViewId.get(i), list.get(position).getDrawable(i));
		}

		return imageListView;
	}
}
