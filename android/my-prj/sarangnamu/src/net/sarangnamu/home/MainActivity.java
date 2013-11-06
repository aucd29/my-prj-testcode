package net.sarangnamu.home;

import net.sarangnamu.common.fonts.FontLoader;
import net.sarangnamu.home.api.Api;
import net.sarangnamu.home.page.Navigator;
import net.sarangnamu.home.page.PageBaseFrgmt;
import net.sarangnamu.home.page.dlg.DlgLogin;
import net.sarangnamu.home.page.dlg.DlgLogin.DlgLoginListener;
import net.sarangnamu.home.page.sub.HomeFrgmt;
import net.sarangnamu.home.page.sub.HomeWriteFrgmt;
import net.sarangnamu.home.page.sub.QnaFrgmt;
import net.sarangnamu.home.page.sub.StudyDetailFrgmt;
import net.sarangnamu.home.page.sub.StudyFrgmt;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login   = (TextView) findViewById(R.id.login);
        group   = (RadioGroup) findViewById(R.id.rdoMenu);
        sliding = (SlidingPaneLayout) findViewById(R.id.sliding);

        initNaviation();
        initMenu();
        initLogin();
    }

    private void initNaviation() {
        Navigator nv = Navigator.getInstance(this);
        nv.setBaseLayoutId(R.id.content);
        nv.add(Navigator.HOME, HomeFrgmt.class);
        nv.add(Navigator.HOME_WRITE, HomeWriteFrgmt.class);
        nv.add(Navigator.QNA, QnaFrgmt.class);
        nv.add(Navigator.STUDY, StudyFrgmt.class);
        nv.add(Navigator.STUDY_DETAIL, StudyDetailFrgmt.class);
        nv.add(Navigator.QNA, QnaFrgmt.class);
        nv.setBase(Navigator.HOME);
    }

    private void initMenu() {
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Navigator nv = Navigator.getInstance(MainActivity.this);

                switch (checkedId) {
                case R.id.mnu_home:
                    nv.setBase(Navigator.HOME);
                    break;

                case R.id.mnu_study:
                    nv.setBase(Navigator.STUDY);
                    break;

                case R.id.mnu_qna:
                    nv.setBase(Navigator.QNA);
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

                    PageBaseFrgmt base = (PageBaseFrgmt) Navigator.getInstance(MainActivity.this).getCurrent();
                    if (base instanceof HomeFrgmt) {
                        base.showWriteButton();
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
        PageBaseFrgmt ft = (PageBaseFrgmt) Navigator.getInstance(this).getCurrent();

        if (ft == null || !ft.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
