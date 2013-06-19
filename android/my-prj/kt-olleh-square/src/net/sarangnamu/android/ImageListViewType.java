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

import android.graphics.drawable.Drawable;


public class ImageListViewType {

	/**
	 * ����Ʈ�� �� �����Ϳ� �ش��ϴ� �κ�
     * �̹��� �Ѱ��� ���ڿ� �迭�� �����Ǿ�
	 * ���ڿ��� �߰��ǵ� Ŭ���� ��ȭ ���� ����ϵ���
	 * ���ߵ�.
	 *
	 * @param thumbnail ����Ʈ�� ����� ����
	 * @param text ����Ʈ �� ���ڿ� �����͵�
	 */
	ImageListViewType(Drawable thumbnail, String[] text) {
		_thumbnail = thumbnail;
		_text = text.clone();
	}


	/**
	 * ����Ʈ���� ���ϴ� ���ڿ� ������
	 *
	 * @param i ���ڿ��� INDEX
	 * @return ������ ���ڿ�
	 */
	public String getText(int i) {
		return _text[i];
	}


	/**
	 * ����Ʈ���� ����� ����� ����
	 *
	 * @return �����
	 */
	public Drawable getDrawable() {
		return _thumbnail;
	}


	/*
	 * Attributes
	 */
	protected String[] _text;

	protected Drawable _thumbnail;
}