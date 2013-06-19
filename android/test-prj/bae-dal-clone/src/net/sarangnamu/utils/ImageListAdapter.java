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
	 * 아답터를 설정 한다.
	 *
	 * @param context parent 가 되는 context
	 * @param id res/layout 중 개별 리스트 아이템에 해당하는 xml id
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
	 * 리스트에 ImageListViewType 을 추가 한다.
	 * ImageListViewType list 상에 1 row 에 해당하는 데이터 이다.
	 *
	 * @param list ImageListViewType
	 */
	public void add(ImageListViewType list) {
		this.list.add(list);
	}


	/**
	 * 리스트에 사용될 _listId 내에 문자열 id 들을 설정 한다.
	 *
	 *		adapter.addLisViewId(R.id.groupName);
	 *		adapter.addLisViewId(R.id.date);
	 *		adapter.addLisViewId(R.id.contents);
	 *
	 * @param id 문자열 id
	 */
	public void addLisViewId(int id) {
		Log.d("addLisViewtId", Integer.toString(id));
		textViewId.add(id);
	}


	/**
	 * 리스트에 사용될 _listId 내에 이미지id 를 설정 한다.
	 *
	 *		adapter.setImageViewId(R.id.imageView);
	 *
	 * @param id 이미지 id
	 */
	public void addImageViewId(int id) {
		imageViewId.add(id);
	}


	/**
	 * 입력된 _list 에 개수를 반환 한다. 이는 list 에 전체 row 값 이다.
	 *
	 * @return _list.size() 값
	 */
	@Override
	public int getCount() {
		return list.size();
	}


	/**
	 * 원하는 위치에 ImageListViewType 를 반환 한다.
	 *
	 * @return _list 내에 원하는 index 에 해당하는 ImageListViewType 값
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
	 * android 에서 listview 는 현재 screen 상에 나타나는 list 의 데이터만
	 * show 하게 끔되어 있다 이에 따라 screen area 에 해당하는 data 를 요구한
	 * 뒤 이를 출력 하는데 이 data를 요구하는 부분이 바로 getView 이다.
	 *
	 * @param position list 상에 index 값
	 * @parem convertView list 상에 출력을 해야할 view
	 * @param parent
	 * @return position 에 해당하는  ImageListView 데이터
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
