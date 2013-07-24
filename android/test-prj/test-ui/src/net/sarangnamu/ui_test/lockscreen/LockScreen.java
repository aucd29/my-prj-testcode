/*
 * LockScreen.java
 * Copyright 2013 OBIGO Inc. All rights reserved.
 *             http://www.obigo.com
 */
package net.sarangnamu.ui_test.lockscreen;

import net.sarangnamu.ui_test.R;
import net.sarangnamu.ui_test.common.LockScreenBase;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LockScreen extends LockScreenBase {
    private static final String TAG = "LockScreen";
    private Button btn;

    @Override
    public int getLayoutId() {
        return R.layout.lockscreen;
    }

    @Override
    protected void initLayout() {
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick finish");
                finish();
            }
        });
    }
}
