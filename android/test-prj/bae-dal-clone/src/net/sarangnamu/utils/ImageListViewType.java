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

import android.graphics.drawable.Drawable;


public class ImageListViewType {
	protected ArrayList<String> text = new ArrayList<String>();
	protected ArrayList<Drawable> image = new ArrayList<Drawable>();

	public ImageListViewType() {
		
	}
	
	public ImageListViewType(ArrayList<Drawable> image, ArrayList<String> text) {
		this.image = image;
		this.text = text; 
	}
	
	public void addString(String text) {
		this.text.add(text);
	}
	
	public void addDrawable(Drawable image) {
		this.image.add(image);
	}

	/**
	 * 리스트에서 원하는 문자열 데이터
	 *
	 * @param i 문자열에 INDEX
	 * @return 선택한 문자열
	 */
	public String getText(int i) {
		return text.get(i);
	}

	/**
	 * 리스트에서 출력할 썸네일 정보
	 *
	 * @return 썸네일
	 */
	public Drawable getDrawable(int i) {
		return image.get(i);
	}
}