/*
 * DlgOrderBy.java
 * Copyright 2015 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.apk_extractor.dlg;

import net.sarangnamu.apk_extractor.R;
import net.sarangnamu.apk_extractor.cfg.Cfg;
import net.sarangnamu.common.ui.dlg.DlgBase;
import android.content.Context;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class DlgOrderBy extends DlgBase {
    private RadioGroup group;

    public DlgOrderBy(Context context) {
        super(context);
    }

    @Override
    protected int getBaseLayoutId() {
        return R.layout.dlg_orderby;
    }

    @Override
    protected void initLayout() {
        group = (RadioGroup) findViewById(R.id.orderGroup);
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                case R.id.defaultOrder:
                    Cfg.setOrderBy(getContext(), "default");
                    break;
                case R.id.alphabet:
                    Cfg.setOrderBy(getContext(), "alphabet");
                    break;
                case R.id.installTime:
                    Cfg.setOrderBy(getContext(), "installTime");
                    break;
                }

                dismiss();
            }
        });
    }
}
