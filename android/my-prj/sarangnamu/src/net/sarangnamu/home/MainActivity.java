/*
 * Cfg.java
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
package net.sarangnamu.home;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.fonts.FontLoader;
import net.sarangnamu.common.ui.gesture.Gesture;
import net.sarangnamu.common.ui.gesture.Gesture.GestureRightListener;
import net.sarangnamu.home.api.Api;
import net.sarangnamu.home.page.Navigator;
import net.sarangnamu.home.page.PageBaseFrgmt;
import net.sarangnamu.home.page.dlg.DlgLogin;
import net.sarangnamu.home.page.dlg.DlgLogin.DlgLoginListener;
import net.sarangnamu.home.page.sub.HomeFrgmt;
import net.sarangnamu.home.page.sub.QnaFrgmt;
import net.sarangnamu.home.page.sub.StudyDetailFrgmt;
import net.sarangnamu.home.page.sub.StudyFrgmt;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";

    private TextView login;
    private RadioGroup group;
    private SlidingPaneLayout sliding;
    private ProgressDialog popup;
    private Gesture gesture;
    public static int SWIPE_THRESHOLD = 300;
    public static int SWIPE_VELOCITY_THRESHOLD = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gesture = Gesture.newInstance(this);
        gesture.setOnGestureRightListener(new GestureRightListener() {
            @Override
            public void toRight() {
                onBackPressed();
            }
        });

        login   = (TextView) findViewById(R.id.login);
        group   = (RadioGroup) findViewById(R.id.rdoMenu);
        sliding = (SlidingPaneLayout) findViewById(R.id.sliding);

        sliding.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Fragment ft = Navigator.getInstance(MainActivity.this).getCurrentFragment();
                if (ft != null && ft instanceof StudyDetailFrgmt) {
                    gesture.onTouchEvent(event);

                    return true;
                }

                return false;
            }
        });

        if (savedInstanceState == null) {
            initNaviation();
        }
        initMenu();
        initLogin();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "onSaveInstanceState");
        DLog.d(TAG, "===================================================================");
        outState.putString("path", "-");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initNaviation() {
        Navigator nv = Navigator.getInstance(this);
        nv.add(R.id.content, HomeFrgmt.class);
    }

    @Override
    protected void onDestroy() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "on destory");
        DLog.d(TAG, "===================================================================");
        super.onDestroy();
    }

    private void initMenu() {
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Navigator nv = Navigator.getInstance(MainActivity.this);

                switch (checkedId) {
                case R.id.mnu_home:
                    nv.resetAdd(R.id.content, HomeFrgmt.class);
                    break;

                case R.id.mnu_study:
                    nv.resetAdd(R.id.content, StudyFrgmt.class);
                    break;

                case R.id.mnu_qna:
                    nv.resetAdd(R.id.content, QnaFrgmt.class);
                    break;
                }

                sliding.closePane();
            }
        });

        FontLoader.getInstance(MainActivity.this).applyChild("Ubuntu-L", group, RadioButton.class);
    }

    public void initLogin() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DlgLogin dlg = new DlgLogin(MainActivity.this, R.layout.dlg_login);
                dlg.setOnLoginListener(new DlgLoginListener() {
                    @Override
                    public void ok(String id, String pw) {
                        loginTask(id, pw);
                    }
                });
                dlg.show();
            }
        });
    }

    private void loginTask(final String id, final String pw) {
        new AsyncTask<Context, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                showDlgProgress();
            }

            @Override
            protected Boolean doInBackground(Context... contexts) {
                Context context = contexts[0];

                boolean res = false;
                try {
                    res = Api.login(id, pw);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return res;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                hideDlgProgress();

                if (result) {
                    Toast.makeText(MainActivity.this, R.string.loginOk, Toast.LENGTH_SHORT).show();

                    DLog.e(TAG, "onPostExecute result ok");
                    PageBaseFrgmt base = (PageBaseFrgmt) Navigator.getInstance(MainActivity.this).getCurrentFragment();
                    if (base == null) {
                        DLog.e(TAG, "home fragment");

                        base = (PageBaseFrgmt) Navigator.getInstance(MainActivity.this).getFragment(HomeFrgmt.class);
                        base.showWriteButton();
                    } else {
                        DLog.e(TAG, "onPostExecute error fragment");
                    }

                    sliding.closePane();
                }
            }
        }.execute(MainActivity.this);
    }

    public void showDlgProgress() {
        if (popup == null) {
            popup = new ProgressDialog(MainActivity.this);
        }

        popup.show();
        popup.setCancelable(false);
        popup.setContentView(R.layout.progress);
    }

    public void hideDlgProgress() {
        if (popup == null) {
            return ;
        }

        popup.dismiss();
    }

    @Override
    public void onBackPressed() {
        PageBaseFrgmt ft = (PageBaseFrgmt) Navigator.getInstance(this).getCurrentFragment();

        if (ft == null || !ft.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
