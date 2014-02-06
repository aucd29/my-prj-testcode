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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class ImageListAdapter extends BaseAdapter {

	/**
	 * 아답터를 설정 한다.
	 *
	 * @param context parent 가 되는 context
	 * @param id res/layout 중 개별 리스트 아이템에 해당하는 xml id
	 */
	ImageListAdapter(Context context, int id) {
		super();

		_context = context;
		_listId = id;
		_id = new ArrayList<Integer>();
		_list = new ArrayList<ImageListViewType>();
	}


	/**
	 * 리스트에 ImageListViewType 을 추가 한다.
	 * ImageListViewType list 상에 1 row 에 해당하는 데이터 이다.
	 *
	 * @param list ImageListViewType
	 */
	public void add(ImageListViewType list) {
		_list.add(list);
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

		_id.add(id);
	}


	/**
	 * 리스트에 사용될 _listId 내에 이미지id 를 설정 한다.
	 *
	 *		adapter.setImageViewId(R.id.imageView);
	 *
	 * @param id 이미지 id
	 */
	public void setImageViewId(int id) {
		_imageViewId = id;
	}


	/**
	 * 입력된 _list 에 개수를 반환 한다. 이는 list 에 전체 row 값 이다.
	 *
	 * @return _list.size() 값
	 */
	@Override
	public int getCount() {
		return _list.size();
	}


	/**
	 * 원하는 위치에 ImageListViewType 를 반환 한다.
	 *
	 * @return _list 내에 원하는 index 에 해당하는 ImageListViewType 값
	 */
	@Override
	public Object getItem(int position) {
		return _list.get(position);
	}


	/**
	 *

	@Override
	public long getItemId(int position) {
		return position;
	} */


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
			imageListView = new ImageListView(_context, _listId);

			for (int i=0; i<_id.size(); ++i) {
				imageListView.setText(_id.get(i), _list.get(position).getText(i));
			}

		} else {
			imageListView = (ImageListView) convertView;

			for (int i=0; i<_id.size(); ++i) {
				imageListView.resetText(i, _list.get(position).getText(i));
			}
		}

		imageListView.setImage(_imageViewId, _list.get(position).getDrawable());

		return imageListView;
	}


	/*
	 * Attributes
	 */
	protected ArrayList<ImageListViewType> _list;

	protected ArrayList<Integer> _id;

	protected Context _context;

	protected int _listId, _imageViewId;

}
