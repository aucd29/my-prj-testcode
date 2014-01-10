/*
 * BkDownloader.java
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import net.sarangnamu.common.BkFile;
import net.sarangnamu.common.BkString;
import net.sarangnamu.common.DLog;

/**
 * {@code
 * <pre>
    BkDownloader dn = new BkDownloader();
    dn.downloadThread("http://", "/mnt/sdcard/down", null);
 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com.com">Burke Choi</a>
 */
public class BkDownloader extends BkHandler {
    private static final String TAG = "BkDownloader";
    protected int connTimeout = 3000;
    protected int readTimeout = 3000;

    public BkDownloader() {

    }

    public void setConnTimeout(int time) {
        connTimeout = time;
    }

    public void setReadTimeout(int time) {
        readTimeout = time;
    }

    public void downloadThread(final String addr, final String dest, final BkHandlerListener l) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    download(addr, dest, l);
                } catch (Exception e) {
                    DLog.e(TAG, "tDownload", e);
                }
            }
        }).start();
    }

    public void download(final String addr, String dest, final BkHandlerListener l) throws Exception {
        URL url = new URL(addr);
        URLConnection conn = url.openConnection();

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setConnectTimeout(connTimeout);
        conn.setReadTimeout(readTimeout);
        conn.connect();

        int fileSize = conn.getContentLength();
        if (l != null) {
            totalSize(l, fileSize);
        }

        BkFile.mkdirs(dest);
        String fileName = BkString.getFileName(addr);
        File file = new File(dest, fileName);

        InputStream input   = new BufferedInputStream(url.openStream());
        OutputStream output = new FileOutputStream(file);

        int count;
        long total = 0;
        byte data[] = new byte[2048];

        while ((count = input.read(data)) != -1) {
            if (l.isCancelled()) {
                break;
            }

            total += count;
            output.write(data, 0, count);

            if (l != null) {
                sendMessage(UPDATE, (int) (total * 100 / fileSize), l);
                Thread.sleep(1);
            }
        }

        output.flush();
        output.close();
        input.close();

        if (l.isCancelled()) {
            cancelled(l, dest, fileName);
            Thread.sleep(1);
        } else {
            sendMessage(SUCCESS, 0, l);
            Thread.sleep(1);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // BkHandler
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void success(BkHandlerListener l) {
        l.onSuccess();
    }

    @Override
    protected void update(BkHandlerListener l, int value) {
        l.onUpdate(value);
    }

    @Override
    protected void totalSize(BkHandlerListener l, long value) {
        Object obj[] = new Object[2];
        obj[0] = value;
        obj[1] = l;
        sendMessage(TOTAL_SIZE, 0, obj);
    }

    @Override
    protected void cancelled(BkHandlerListener l, String dest, String fileName) throws Exception {
        dest = BkString.setLastSlash(dest);
        BkFile.deleteFile(dest + fileName);
        sendMessage(CANCELLED, 0, l);
    }
}
