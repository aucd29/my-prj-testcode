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

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Aquilegia
 */
public class PlayAudioActivity extends Activity {
	MediaPlayer mPlayer;
	private ArrayList<String> mPlayList = null;
	private ArrayList<String> mUriList = null;
	private int mCurrentIndex = -1;
	ImageButton mPlayBtn;
	TextView mFileName;
	SeekBar mProgress;
	boolean wasPlaying;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playaudio);
		
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

		String uri = mPlayList.get(mCurrentIndex);
		mPlayer = new MediaPlayer();

		// 버튼들의 클릭 리스너 등록
		mFileName = (TextView)findViewById(R.id.filename);
		mPlayBtn = (ImageButton)findViewById(R.id.play);
		mPlayBtn.setOnClickListener(mClickPlay);
		findViewById(R.id.stop).setOnClickListener(mClickStop);
		findViewById(R.id.prev).setOnClickListener(mClickPrevNext);
		findViewById(R.id.next).setOnClickListener(mClickPrevNext);
		
		// 완료 리스너, 시크바 변경 리스너 등록
		mPlayer.setOnCompletionListener(mOnComplete);
		mPlayer.setOnSeekCompleteListener(mOnSeekComplete);
		mProgress = (SeekBar)findViewById(R.id.progress);
		mProgress.setOnSeekBarChangeListener(mOnSeek);
		mProgressHandler.sendEmptyMessageDelayed(0,200);
		
		// 첫 곡 읽기 및 준비
		if (LoadMedia(mCurrentIndex) == false) {
			Toast.makeText(this, "파일을 읽을 수 없습니다.", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	// 액티비티 종료시 재생 강제 종료
	public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
        	mPlayer.release();
        	mPlayer = null;
        }
    }

    // 항상 준비 상태여야 한다.
    boolean LoadMedia(int idx) {
		try {
			mPlayer.setDataSource(mUriList.get(idx));
		} catch (IllegalArgumentException e) {
			return false;
		} catch (IllegalStateException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		if (Prepare() == false) {
			return false;
		}
		mFileName.setText(mPlayList.get(idx));
		mProgress.setMax(mPlayer.getDuration());
		return true;
    }
    
    boolean Prepare() {
		try {
			mPlayer.prepare();
		} catch (IllegalStateException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
    }

    // 재생 및 일시 정지
    Button.OnClickListener mClickPlay = new View.OnClickListener() {
		public void onClick(View v) {
			if (mPlayer.isPlaying() == false) {
				mPlayer.start();
				mPlayBtn.setImageResource(android.R.drawable.ic_media_pause);		
			} else {
				mPlayer.pause();
				mPlayBtn.setImageResource(android.R.drawable.ic_media_play);		
			}
		}
	};

	// 재생 정지. 재시작을 위해 미리 준비해 놓는다.
    Button.OnClickListener mClickStop = new View.OnClickListener() {
		public void onClick(View v) {
			mPlayer.stop();
			mPlayBtn.setImageResource(android.R.drawable.ic_media_play);		
			mProgress.setProgress(0);
			Prepare();
		}
    };
    
    Button.OnClickListener mClickPrevNext = new View.OnClickListener() {
		public void onClick(View v) {
			boolean wasPlaying = mPlayer.isPlaying();
			
			if (v.getId() == R.id.prev) {
				mCurrentIndex = (mCurrentIndex == 0 ? mPlayList.size() - 1 : mCurrentIndex - 1);
			} else {
				mCurrentIndex = (mCurrentIndex == mPlayList.size() - 1 ? 0 : mCurrentIndex + 1);
			}
			
			mPlayer.reset();
			LoadMedia(mCurrentIndex);

			// 이전에 재생중이었으면 다음 곡 바로 재생
			if (wasPlaying) {
				mPlayer.start();
				mPlayBtn.setImageResource(android.R.drawable.ic_media_pause);		
			}
		}
    };

    // 재생 완료되면 다음곡으로
    MediaPlayer.OnCompletionListener mOnComplete = new MediaPlayer.OnCompletionListener() {
		public void onCompletion(MediaPlayer arg0) {
			mCurrentIndex = (mCurrentIndex == mPlayList.size() - 1 ? 0 : mCurrentIndex + 1);
			mPlayer.reset();
			LoadMedia(mCurrentIndex);
			mPlayer.start();
		}
    };

    // 에러 발생시 메시지 출력
    MediaPlayer.OnErrorListener mOnError = new MediaPlayer.OnErrorListener() {
		public boolean onError(MediaPlayer mp, int what, int extra) {
			String err = "OnError occured. what = " + what + " ,extra = " + extra;
			Toast.makeText(PlayAudioActivity.this, err, Toast.LENGTH_LONG).show();
			return false;
		}
    };

    // 위치 이동 완료 처리
    MediaPlayer.OnSeekCompleteListener mOnSeekComplete = new MediaPlayer.OnSeekCompleteListener() {
		public void onSeekComplete(MediaPlayer mp) {
			if (wasPlaying) {
				mPlayer.start();
			}
		}
    };

    // 0.2초에 한번꼴로 재생 위치 갱신
	Handler mProgressHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (mPlayer == null) return;
			if (mPlayer.isPlaying()) {
				mProgress.setProgress(mPlayer.getCurrentPosition());
			}
			mProgressHandler.sendEmptyMessageDelayed(0,200);
		}
	};

	// 재생 위치 이동
	SeekBar.OnSeekBarChangeListener mOnSeek = new SeekBar.OnSeekBarChangeListener() {
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (fromUser) {
				mPlayer.seekTo(progress);
			}
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
			wasPlaying = mPlayer.isPlaying();
			if (wasPlaying) {
				mPlayer.pause();
			}
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	};
}

