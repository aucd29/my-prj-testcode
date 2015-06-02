/*
 * DlgProgressBarFlat.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
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
package net.sarangnamu.common.ui.dlg;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DlgProgressBarFlat extends DlgBase {
    private static final int MSG_UPDATE_TITLE       = 1;
    private static final int MSG_UPDATE_PROGRESS    = 2;
    private static final int MSG_UPDATE_SIZE        = 3;

    private TextView mTitle, mSize;
    private ProgressBar mProgressbar;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_UPDATE_TITLE:
                mTitle.setText((String) msg.obj);
                break;
            case MSG_UPDATE_PROGRESS:
                mProgressbar.setProgress(msg.arg1);
                break;
            case MSG_UPDATE_SIZE:
                mSize.setText(String.format("%s/%s", msg.arg1, msg.arg2));
                break;
            }
        }
    };

    private void sendMessage(int type, int arg1, int arg2, Object obj) {
        Message msg = mHandler.obtainMessage();
        msg.what = type;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj  = obj;
        mHandler.sendMessage(msg);
    }

    public DlgProgressBarFlat(Context context) {
        super(context);
    }

    @Override
    protected int getBaseLayoutId() {
        return R.layout.dlg_progressbar_flat;
    }

    @Override
    protected void initLayout() {
        mTitle       = (TextView) findViewById(R.id.title);
        mSize        = (TextView) findViewById(R.id.size);
        mProgressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    public void setTitle(final String title) {
        sendMessage(MSG_UPDATE_TITLE, 0, 0, title);
    }

    public void setProgress(int progress) {
        sendMessage(MSG_UPDATE_PROGRESS, progress, 0, null);
    }

    public void setProgressText(final int current, final int max) {
        sendMessage(MSG_UPDATE_SIZE, current, max, null);
    }
}
