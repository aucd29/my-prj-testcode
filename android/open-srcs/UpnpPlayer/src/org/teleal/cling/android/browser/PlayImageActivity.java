/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.teleal.cling.android.browser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.Gallery.LayoutParams;

/**
 * @author Aquilegia
 */
public class PlayImageActivity extends Activity implements ViewSwitcher.ViewFactory {
	
	private ArrayList<String> mImageList = null;
	private ArrayList<String> mUriList = null;
	private int mCurrentIndex = -1;
	ImageButton mPlayBtn;
	TextView mFileName;
	private Bitmap bm;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.playimage);
		
		Bundle bundle = getIntent().getExtras().getBundle("playlist");
		mImageList = bundle.getStringArrayList("list");
		mUriList = bundle.getStringArrayList("urilist");
		mCurrentIndex = bundle.getInt("index");
		if (mImageList == null || mCurrentIndex < 0
				|| mCurrentIndex >= mImageList.size()) {
			
			Toast.makeText(this, "보여줄 파일이 없습니다.", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));
		
		// 버튼들의 클릭 리스너 등록
		mFileName = (TextView)findViewById(R.id.image_filename);
		findViewById(R.id.image_prev).setOnClickListener(mClickPrevNext);
		findViewById(R.id.image_next).setOnClickListener(mClickPrevNext);
		
		// 첫 곡 읽기 및 준비
		if (LoadMedia(mCurrentIndex) == false) {
			Toast.makeText(this, "파일을 읽을 수 없습니다.", Toast.LENGTH_LONG).show();
			finish();
		}
	}
	
	// 항상 준비 상태여야 한다.
	boolean LoadMedia(int idx) {
		//Uri uri = Uri.parse(mUriList.get(idx));
		//mSwitcher.setImageURI(uri);
		if (bm != null)
			System.gc();
		
		Options opts = new Options();
		opts.inJustDecodeBounds = true;

		try {
			URL aURL = new URL(mUriList.get(idx));
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			// bm = BitmapFactory.decodeStream(bis);
			BitmapFactory.decodeStream(bis, null, opts);
			bis.close();
			is.close();
		} catch (IOException e) {
			//mSwitcher.setImageResource(R.drawable.file_not_found);
			Log.e("DEBUGTAG", "Remote Image Exception", e);
		}
		
		/* If the image is more than 500KB, then the image must be scaled down.*/
		int k = Integer.highestOneBit((int)Math.floor(opts.outWidth * opts.outHeight / 500000));
		Toast.makeText(this, "width="+opts.outWidth+", height="+opts.outHeight+", k="+k, Toast.LENGTH_LONG).show();
		if (k==0) k=1;
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = k;
		
		try {
			URL aURL = new URL(mUriList.get(idx));
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			if (k != 1)
				bm = BitmapFactory.decodeStream(bis, null, opts);
			else 
				bm = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			// Drawable 객체 생성
			final Drawable image = new BitmapDrawable(bm);
			mSwitcher.setImageDrawable(image);
		} catch (IOException e) {
			//mSwitcher.setImageResource(R.drawable.file_not_found);
			Log.e("DEBUGTAG", "Remote Image Exception", e);
		}
		mFileName.setText(mImageList.get(idx));
		//mProgress.setMax(mPlayer.getDuration());
		return true;
	}
	
	Button.OnClickListener mClickPrevNext = new View.OnClickListener() {
		public void onClick(View v) {
			if (v.getId() == R.id.image_prev) {
				mCurrentIndex = (mCurrentIndex == 0 ? mImageList.size() - 1 : mCurrentIndex - 1);
			} else {
				mCurrentIndex = (mCurrentIndex == mImageList.size() - 1 ? 0 : mCurrentIndex + 1);
			}
			LoadMedia(mCurrentIndex);
		}
	};
	
	//public void onNothingSelected(AdapterView parent) {
	//}
	
	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		return i;
	}
	
	private ImageSwitcher mSwitcher;
}
