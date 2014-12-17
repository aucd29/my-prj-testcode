/*
 * BkHandler.java
 * Copyright 2013 Burke Choi All rights reserved.
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
package net.sarangnamu.common.network;

import android.os.Handler;
import android.os.Message;

/**
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public abstract class BkHandler extends Handler {
    protected static final int SUCCESS    = 0;
    protected static final int UPDATE     = 1;
    protected static final int ERROR      = 2;
    protected static final int TOTAL_SIZE = 3;
    protected static final int CANCELLED  = 4;

    protected void sendMessage(int type, int val, Object obj) {
        Message msg = obtainMessage();
        msg.what = type;
        msg.arg1 = val;
        msg.obj  = obj;

        sendMessage(msg);
    }

    protected void sendErrorMessage(final Exception e, BkHandlerListener l) {
        e.printStackTrace();

        Object[] obj = new Object[2];
        obj[0] = e.getMessage();
        obj[1] = l;

        if (l != null) {
            sendMessage(ERROR, 0, obj);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        BkHandlerListener l;
        Object[] obj = null;

        if (msg.what == ERROR || msg.what == TOTAL_SIZE) {
            obj = (Object[]) msg.obj;
            l = (BkHandlerListener) obj[1];
        } else {
            l = (BkHandlerListener) msg.obj;
        }

        switch (msg.what) {
        case SUCCESS:
            success(l);
            break;

        case UPDATE:
            update(l, msg.arg1);
            break;

        case ERROR:
            l.onError((String) obj[0]);
            break;

        case TOTAL_SIZE:
            l.onTotalSize((Long) obj[0]);
            break;

        case CANCELLED:
            l.onCancelled();
            break;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // BkHandlerListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public interface BkHandlerListener {
        public void onSuccess();
        public void onUpdate(int read);
        public void onError(String errMessage);
        public void onTotalSize(long size);
        public void onCancelled();
        public boolean isCancelled();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    protected abstract void success(BkHandlerListener l);
    protected abstract void update(BkHandlerListener l, int value);
    protected abstract void totalSize(BkHandlerListener l, long value);
    protected abstract void cancelled(BkHandlerListener l, String dest, String fileName) throws Exception;
}
