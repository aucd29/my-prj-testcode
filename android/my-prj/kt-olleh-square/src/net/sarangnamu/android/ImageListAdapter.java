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
	 * �ƴ��͸� ���� �Ѵ�.
	 *
	 * @param context parent �� �Ǵ� context
	 * @param id res/layout �� ���� ����Ʈ �����ۿ� �ش��ϴ� xml id
	 */
	ImageListAdapter(Context context, int id) {
		super();

		_context = context;
		_listId = id;
		_id = new ArrayList<Integer>();
		_list = new ArrayList<ImageListViewType>();
	}


	/**
	 * ����Ʈ�� ImageListViewType �� �߰� �Ѵ�.
	 * ImageListViewType list �� 1 row �� �ش��ϴ� ������ �̴�.
	 *
	 * @param list ImageListViewType
	 */
	public void add(ImageListViewType list) {
		_list.add(list);
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

		_id.add(id);
	}


	/**
	 * ����Ʈ�� ���� _listId ���� �̹���id �� ���� �Ѵ�.
	 *
	 *		adapter.setImageViewId(R.id.imageView);
	 *
	 * @param id �̹��� id
	 */
	public void setImageViewId(int id) {
		_imageViewId = id;
	}


	/**
	 * �Էµ� _list �� ������ ��ȯ �Ѵ�. �̴� list �� ��ü row �� �̴�.
	 *
	 * @return _list.size() ��
	 */
	@Override
	public int getCount() {
		return _list.size();
	}


	/**
	 * ���ϴ� ��ġ�� ImageListViewType �� ��ȯ �Ѵ�.
	 *
	 * @return _list ���� ���ϴ� index �� �ش��ϴ� ImageListViewType ��
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
