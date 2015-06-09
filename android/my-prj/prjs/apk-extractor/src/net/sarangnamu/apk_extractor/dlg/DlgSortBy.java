/*
 * DlgOrderBy.java
 * Copyright 2015 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.apk_extractor.dlg;

import net.sarangnamu.apk_extractor.R;
import net.sarangnamu.apk_extractor.cfg.Cfg;
import net.sarangnamu.common.admob.AdMobDecorator;
import net.sarangnamu.common.fonts.FontLoader;
import net.sarangnamu.common.ui.dlg.DlgBase;
import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class DlgSortBy extends DlgBase {
    private RadioGroup mGroup;
    private RadioButton mDefault, mAlphabetAsc, mAlphabetDesc;
    private RadioButton mFirstInstallTime, mLastInstallTime;

    public DlgSortBy(Context context) {
        super(context);
    }

    @Override
    protected int getBaseLayoutId() {
        return R.layout.dlg_sortby;
    }

    @Override
    protected void initLayout() {
        mGroup = (RadioGroup) findViewById(R.id.sortGroup);
        mDefault = (RadioButton) findViewById(R.id.sortDefault);
        mAlphabetAsc = (RadioButton) findViewById(R.id.alphabetAsc);
        mAlphabetDesc = (RadioButton) findViewById(R.id.alphabetDesc);
        mFirstInstallTime = (RadioButton) findViewById(R.id.firstInstallTime);
        mLastInstallTime = (RadioButton) findViewById(R.id.lastInstallTime);

        AdMobDecorator.getInstance().load(this, R.id.adView);
        String sortBy = Cfg.getSortBy(getContext());

        if (sortBy.equals(Cfg.SORT_DEFAULT)) {
            mDefault.setChecked(true);
        } else if (sortBy.equals(Cfg.SORT_ALPHABET_ASC)) {
            mAlphabetAsc.setChecked(true);
        } else if (sortBy.equals(Cfg.SORT_ALPHABET_DESC)) {
            mAlphabetDesc.setChecked(true);
        } else if (sortBy.equals(Cfg.SORT_FIRST_INSTALL_TIME)) {
            mFirstInstallTime.setChecked(true);
        } else if (sortBy.equals(Cfg.SORT_LAST_INSTALL_TIME)) {
            mLastInstallTime.setChecked(true);
        }

        mGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                case R.id.sortDefault:
                    Cfg.setSortBy(getContext(), Cfg.SORT_DEFAULT);
                    break;
                case R.id.alphabetAsc:
                    Cfg.setSortBy(getContext(), Cfg.SORT_ALPHABET_ASC);
                    break;
                case R.id.alphabetDesc:
                    Cfg.setSortBy(getContext(), Cfg.SORT_ALPHABET_DESC);
                    break;
                case R.id.firstInstallTime:
                    Cfg.setSortBy(getContext(), Cfg.SORT_FIRST_INSTALL_TIME);
                    break;
                case R.id.lastInstallTime:
                    Cfg.setSortBy(getContext(), Cfg.SORT_LAST_INSTALL_TIME);
                    break;
                }

                dismiss();
            }
        });

        FontLoader.getInstance(getContext()).applyChild("Roboto-Light", mGroup);
    }
}
