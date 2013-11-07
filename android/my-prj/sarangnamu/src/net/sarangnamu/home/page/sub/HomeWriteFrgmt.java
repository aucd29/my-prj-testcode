/*
 * HomeWriteFrgmt.java
 * Copyright 2013 Burke.Choi All rights reserved.
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
package net.sarangnamu.home.page.sub;

import net.sarangnamu.common.BkCfg;
import net.sarangnamu.common.DLog;
import net.sarangnamu.home.R;
import net.sarangnamu.home.api.Api;
import net.sarangnamu.home.page.Navigator;
import net.sarangnamu.home.page.PageBaseFrgmt;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HomeWriteFrgmt extends PageBaseFrgmt {
    private static final String TAG = "HomeWriteFrgmt";
    private EditText msg;
    private Button submit;

    @Override
    protected void initLayout() {
        super.initLayout();

        msg    = (EditText) view.findViewById(R.id.msg);
        submit = (Button) view.findViewById(R.id.submit);

        BkCfg.showKeyboard(getActivity(), msg);
        pageRefresh.setVisibility(View.GONE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Context, Void, Boolean>() {
                    @Override
                    protected void onPreExecute() {
                        submit.setClickable(false);
                        msg.setEnabled(false);
                        showIconProgress();
                    }

                    @Override
                    protected Boolean doInBackground(Context... contexts) {
                        Context context = contexts[0];

                        try {
                            Api.noticeWrite(msg.getText().toString());

                            return true;
                        } catch (Exception e) {
                            DLog.e(TAG, "doInBackground", e);
                        }

                        return false;
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        hideIconProgress();
                        msg.setText("");
                        msg.setEnabled(true);
                        submit.setClickable(true);
                        backFrgmt();

                        HomeFrgmt frgmt = (HomeFrgmt) getFrgmtByName(Navigator.HOME);
                        frgmt.reload();
                    }
                }.execute(getActivity());
            }
        });
    }
}
