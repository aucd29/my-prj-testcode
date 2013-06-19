package net.sarangnamu.testdownload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

public class TestFileDownloadActivity extends Activity {
    private static final String WEBBY_HOST_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp/obigo/webbyhost";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            //ProgressDialog.show(this, "file", "msg");
            File file = new File(WEBBY_HOST_PATH + "/file.pdf");
            getFile("http://webby.obigo.com/webbyhost/voAMRWBEncoderSDK.pdf", file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFile(String addr, File file) throws Exception {
        URL url = new URL(addr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(1000);
        conn.setReadTimeout(3000);

        InputStream input = new BufferedInputStream(conn.getInputStream());
        File dir = new File(WEBBY_HOST_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        OutputStream output = new FileOutputStream(file);
        byte data[] = new byte[4096];

        int count = 0;
        while ((count = input.read(data)) != -1) {
            output.write(data, 0, count);
        }

        output.flush();
        output.close();
        input.close();
    }
}