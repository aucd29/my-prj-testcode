/*
 * LockScreenService.java
 * Copyright 2013 OBIGO Inc. All rights reserved.
 *             http://www.obigo.com
 */
package net.sarangnamu.ui_test.lockscreen;

import net.sarangnamu.ui_test.common.LockScreenServiceBase;
import android.content.Context;
import android.content.Intent;

public class LockScreenService extends LockScreenServiceBase {
    @Override
    public Intent getLockScreenIntent(Context context) {
        return new Intent(context, LockScreen.class);
    }
}
