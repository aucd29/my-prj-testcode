package net.sarangnamu.home;

import net.sarangnamu.home.page.home.HomeFrgmt;
import net.sarangnamu.home.page.openprj.OpenPrjFrgmt;
import net.sarangnamu.home.page.qna.QnaFrgmt;
import net.sarangnamu.home.page.study.StudyFrgmt;
import net.sarangnamu.home.ui.Navigator;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity {
    private RadioGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        group = (RadioGroup) findViewById(R.id.rdoMenu);

        initNaviation();
        initMenu();
    }

    private void initNaviation() {
        Navigator nv = Navigator.getInstance(this);
        nv.setBaseLayoutId(R.id.content);
        nv.add(Navigator.HOME, HomeFrgmt.class);
        nv.add(Navigator.STUDY, StudyFrgmt.class);
        nv.add(Navigator.OPENPRJ, OpenPrjFrgmt.class);
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

                case R.id.mnu_openprj:
                    nv.show(Navigator.OPENPRJ);
                    break;

                case R.id.mnu_study:
                    nv.show(Navigator.STUDY);
                    break;

                case R.id.mnu_qna:
                    nv.show(Navigator.QNA);
                    break;
                }
            }
        });
    }
}
