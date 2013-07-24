/*
 * LockScreenBase.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.ui_test.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

/**
 * 
 * @author @aucd29
 * 
 */
public abstract class LockScreenBase extends Activity {
    private static final String TAG = "LockScreenBase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLockScreen();
        setContentView(getLayoutId());
        initLayout();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void setLockScreen() {
        requestWindowFeature(1);
        setDefaultKeyMode(DEFAULT_KEYS_DISABLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        // KeyguardManager km =
        // (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        // KeyguardManager.KeyguardLock keyLock =
        // km.newKeyguardLock(KEYGUARD_SERVICE);
        // keyLock.disableKeyguard(); //순정 락스크린 해제
    }

    // @Override
    // public void onAttachedToWindow() {
    // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    // WindowManager.LayoutParams.FLAG_FULLSCREEN);
    // getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
    //
    // super.onAttachedToWindow();
    //
    // //
    // getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    // //
    // getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    // //
    // // //
    // this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
    // // // KeyguardManager keyguardManager = (KeyguardManager)
    // getSystemService(KEYGUARD_SERVICE);
    // // // KeyguardLock lock =
    // keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
    // // // lock.disableKeyguard();
    // }

    //    @Override
    //    public boolean onKeyDown(int keyCode, KeyEvent event) {
    //        boolean bool = true;
    //
    //        switch (keyCode) {
    //        case KeyEvent.KEYCODE_HOME:
    //        case KeyEvent.KEYCODE_BACK:
    //        case KeyEvent.KEYCODE_VOLUME_UP:
    //        case KeyEvent.KEYCODE_VOLUME_DOWN:
    //            return bool;
    //
    //        default:
    //            return super.onKeyDown(keyCode, event);
    //        }
    //    }

    @Override
    public boolean onKeyLongPress(int paramInt, KeyEvent paramKeyEvent) {
        return true;
    }

    // //////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    // //////////////////////////////////////////////////////////////////////////////////

    public abstract int getLayoutId();

    protected abstract void initLayout();
}
