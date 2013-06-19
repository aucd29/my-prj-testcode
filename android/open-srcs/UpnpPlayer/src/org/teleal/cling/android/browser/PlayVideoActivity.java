/*
 * Copyright (C) 2011 Aquilegia, South Korea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.teleal.cling.android.browser;

import java.util.ArrayList;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * @author Aquilegia
 */
public class PlayVideoActivity extends Activity {
	private ArrayList<String> mPlayList = null;
	private ArrayList<String> mUriList = null;
	private int mCurrentIndex = -1;
	private VideoView video;
	private MediaController mc;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playvideo);
		
		video = (VideoView)findViewById(R.id.videoview);
		//video.setOnTouchListener(this);
		
		Bundle bundle = getIntent().getExtras().getBundle("playlist");
		mPlayList = bundle.getStringArrayList("list");
		mUriList = bundle.getStringArrayList("urilist");
		mCurrentIndex = bundle.getInt("index");
		
		if (mPlayList == null || mCurrentIndex < 0
				|| mCurrentIndex >= mPlayList.size()) {

			Toast.makeText(this, "재생할 파일이 없습니다.", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
		mc = new MediaController(PlayVideoActivity.this);
		video.setMediaController(mc);
		
		Uri uri = Uri.parse(mUriList.get(mCurrentIndex));
		video.setVideoURI(uri);
		//video.setVideoURI(Uri.parse("rtsp:"+uri.getEncodedSchemeSpecificPart()));
		
		video.postDelayed(new Runnable() {
			public void run() {
				mc.show(0);
			}
		},100);
		//video.start();
	}
}

