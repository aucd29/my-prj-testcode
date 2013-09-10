package com.example.fileupload;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.fileupload.HandlerBase.IHandlerBase;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private Button upload, download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost("http://www.sarangnamu.net/test/upload.php");

                        try {
                            MultipartEntity entity = new MultipartEntity();
                            entity.addPart("uploadFile", new ByteArrayBody(new File("/mnt/sdcard/bg_add_ac.png")));

                        )
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);

                Log.d(TAG, EntityUtils.toString(response.getEntity()));
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        download = (Button) findViewById(R.id.download);
        download.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadBase down = new DownloadBase();
                down.getFile("http://sarangnamu.net/test/hk.jpg", "/mnt/sdcard/img/", new IHandlerBase() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Downloaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUpdate(int read) {
                        Log.d(TAG, "downloding " + read);
                    }

                    @Override
                    public void onError(String erroCode) {
                        Log.e(TAG, "onError " + erroCode);
                    }

                    @Override
                    public void onTotalSize(long size) {
                    }

                    @Override
                    public void onCancelled() {

                    }

                    @Override
                    public boolean isCancelled() {
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
