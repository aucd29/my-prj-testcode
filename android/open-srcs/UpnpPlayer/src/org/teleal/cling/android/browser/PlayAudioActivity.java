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

			Toast.makeText(this, "����� ������ �����ϴ�.", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		String uri = mPlayList.get(mCurrentIndex);
		mPlayer = new MediaPlayer();

		// ��ư���� Ŭ�� ������ ���
		mFileName = (TextView)findViewById(R.id.filename);
		mPlayBtn = (ImageButton)findViewById(R.id.play);
		mPlayBtn.setOnClickListener(mClickPlay);
		findViewById(R.id.stop).setOnClickListener(mClickStop);
		findViewById(R.id.prev).setOnClickListener(mClickPrevNext);
		findViewById(R.id.next).setOnClickListener(mClickPrevNext);
		
		// �Ϸ� ������, ��ũ�� ���� ������ ���
		mPlayer.setOnCompletionListener(mOnComplete);
		mPlayer.setOnSeekCompleteListener(mOnSeekComplete);
		mProgress = (SeekBar)findViewById(R.id.progress);
		mProgress.setOnSeekBarChangeListener(mOnSeek);
		mProgressHandler.sendEmptyMessageDelayed(0,200);
		
		// ù �� �б� �� �غ�
		if (LoadMedia(mCurrentIndex) == false) {
			Toast.makeText(this, "������ ���� �� �����ϴ�.", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	// ��Ƽ��Ƽ ����� ��� ���� ����
	public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
        	mPlayer.release();
        	mPlayer = null;
        }
    }

    // �׻� �غ� ���¿��� �Ѵ�.
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

    // ��� �� �Ͻ� ����
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

	// ��� ����. ������� ���� �̸� �غ��� ���´�.
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

			// ������ ������̾����� ���� �� �ٷ� ���
			if (wasPlaying) {
				mPlayer.start();
				mPlayBtn.setImageResource(android.R.drawable.ic_media_pause);		
			}
		}
    };

    // ��� �Ϸ�Ǹ� ����������
    MediaPlayer.OnCompletionListener mOnComplete = new MediaPlayer.OnCompletionListener() {
		public void onCompletion(MediaPlayer arg0) {
			mCurrentIndex = (mCurrentIndex == mPlayList.size() - 1 ? 0 : mCurrentIndex + 1);
			mPlayer.reset();
			LoadMedia(mCurrentIndex);
			mPlayer.start();
		}
    };

    // ���� �߻��� �޽��� ���
    MediaPlayer.OnErrorListener mOnError = new MediaPlayer.OnErrorListener() {
		public boolean onError(MediaPlayer mp, int what, int extra) {
			String err = "OnError occured. what = " + what + " ,extra = " + extra;
			Toast.makeText(PlayAudioActivity.this, err, Toast.LENGTH_LONG).show();
			return false;
		}
    };

    // ��ġ �̵� �Ϸ� ó��
    MediaPlayer.OnSeekCompleteListener mOnSeekComplete = new MediaPlayer.OnSeekCompleteListener() {
		public void onSeekComplete(MediaPlayer mp) {
			if (wasPlaying) {
				mPlayer.start();
			}
		}
    };

    // 0.2�ʿ� �ѹ��÷� ��� ��ġ ����
	Handler mProgressHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (mPlayer == null) return;
			if (mPlayer.isPlaying()) {
				mProgress.setProgress(mPlayer.getCurrentPosition());
			}
			mProgressHandler.sendEmptyMessageDelayed(0,200);
		}
	};

	// ��� ��ġ �̵�
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

