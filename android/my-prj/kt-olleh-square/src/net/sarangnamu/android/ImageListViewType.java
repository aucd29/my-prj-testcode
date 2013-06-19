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
	 * 리스트에 실 데이터에 해당하는 부분
     * 이미지 한개와 문자열 배열로 구성되어
	 * 문자열이 추가되도 클래스 변화 없이 사용하도록
	 * 개발됨.
	 *
	 * @param thumbnail 리스트상에 썸네일 정보
	 * @param text 리스트 상에 문자열 데이터들
	 */
	ImageListViewType(Drawable thumbnail, String[] text) {
		_thumbnail = thumbnail;
		_text = text.clone();
	}


	/**
	 * 리스트에서 원하는 문자열 데이터
	 *
	 * @param i 문자열에 INDEX
	 * @return 선택한 문자열
	 */
	public String getText(int i) {
		return _text[i];
	}


	/**
	 * 리스트에서 출력할 썸네일 정보
	 *
	 * @return 썸네일
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