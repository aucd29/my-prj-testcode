package net.sarangnamu.home;

import net.sarangnamu.common.fonts.FontLoader;
import net.sarangnamu.home.page.Navigator;
import net.sarangnamu.home.page.PageBaseFrgmt;
import net.sarangnamu.home.page.sub.HomeFrgmt;
import net.sarangnamu.home.page.sub.QnaFrgmt;
import net.sarangnamu.home.page.sub.StudyDetailFrgmt;
import net.sarangnamu.home.page.sub.StudyFrgmt;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";

    private RadioGroup group;
    private SlidingPaneLayout sliding;
    private ProgressDialog popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        group   = (RadioGroup) findViewById(R.id.rdoMenu);
        sliding = (SlidingPaneLayout) findViewById(R.id.sliding);

        initNaviation();
        initMenu();
    }

    private void initNaviation() {
        Navigator nv = Navigator.getInstance(this);
        nv.setBaseLayoutId(R.id.content);
        nv.add(Navigator.HOME, HomeFrgmt.class);
        nv.add(Navigator.STUDY, StudyFrgmt.class);
        nv.add(Navigator.STUDY_DETAIL, StudyDetailFrgmt.class);
        //nv.add(Navigator.OPENPRJ, OpenPrjFrgmt.class);
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
                    nv.showBase();
                    break;

                    //                case R.id.mnu_openprj:
                    //                    nv.show(Navigator.OPENPRJ);
                    //                    break;

                case R.id.mnu_study:
                    nv.show(Navigator.STUDY);
                    break;

                case R.id.mnu_qna:
                    nv.show(Navigator.QNA);
                    break;
                }

                sliding.closePane();
            }
        });

        FontLoader.getInstance(MainActivity.this).applyChild("Ubuntu-L", group, RadioButton.class);
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
