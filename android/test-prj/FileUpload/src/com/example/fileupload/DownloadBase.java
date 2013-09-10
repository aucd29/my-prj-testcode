package com.example.fileupload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 파일을 다운로드 한다.
 * 
 * {@code
    <pre>
    DownloadBase down = new DownloadBase();
    down.getFile("http://daum.net", "/sdcard/tmp", new IHandlerBase() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onUpdate(int read) {

        }

        @Override
        public void onError(String erroCode) {
        }

        @Override
        public void onTotalSize(long size) {
        }

        @Override
        public void onCancelled() {

        }
    });
    </pre>
 * }
 * @author <a href="Burke.Choi@obigo.com">krinbuch</a>
 *
 */
public class DownloadBase extends HandlerBase {
    private static final String TAG = "DownloadBase";

    protected int connTimeout = 3000;
    protected int readTimeout = 3000;

    @Override
    protected void success(IHandlerBase l) {
        l.onSuccess();
    }

    @Override
    protected void update(IHandlerBase l, int value) {
        l.onUpdate(value);
    }

    @Override
    protected void sendTotalSizeMessage(IHandlerBase l, long value) {
        Object obj[] = new Object[2];
        obj[0] = value;
        obj[1] = l;
        sendMessage(TOTAL_SIZE, 0, obj);
    }

    @Override
    protected void sendCancelledMessage(IHandlerBase l, String dest, String fileName) throws Exception {
        dest = StringExt.endsWithSlash(dest);
        FileExt.deleteFile(dest + fileName);
        sendMessage(CANCELLED, 0, l);
    }

    /**
     * 파일을 thread 를 사용하여 다운로드 한다.
     * 
     * @param url
     * @param dest
     * @param l
     */
    public void getFile(final String url, final String dest, final IHandlerBase l) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!checkListener(TAG, l)) {
                    return ;
                }

                try {
                    download(url, dest, l);
                } catch (Exception e) {
                    sendErrorMessage(e, l);
                }
            }
        }).start();
    }

    /**
     * 파일을 다운로드 한다.
     * 
     * @param addr
     * @param dest
     * @param l
     * @throws Exception
     */
    public void download(final String addr, String dest, final IHandlerBase l) throws Exception {
        URL url = new URL(addr);
        URLConnection conn = url.openConnection();

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setConnectTimeout(connTimeout);
        conn.setReadTimeout(readTimeout);
        conn.connect();

        int fileSize = conn.getContentLength();
        if (l != null) {
            sendTotalSizeMessage(l, fileSize);
        }

        FileExt.mkdirs(dest);
        String fileName = StringExt.getLastFileName(addr);
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
            sendCancelledMessage(l, dest, fileName);
            Thread.sleep(1);
        } else {
            sendMessage(SUCCESS, 0, l);
            Thread.sleep(1);
        }
    }

    /**
     * 연결시간을 제한 한다.
     * @param time
     */
    public void setConnTimeout(int time) {
        connTimeout = time;
    }

    /**
     * 
     * @param time
     */
    public void setReadTimeout(int time) {
        readTimeout = time;
    }
}
