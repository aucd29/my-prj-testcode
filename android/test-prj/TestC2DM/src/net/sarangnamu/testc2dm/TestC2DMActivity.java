package net.sarangnamu.testc2dm;

import java.net.URLEncoder;

import net.sarangnamu.testc2dm.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TestC2DMActivity extends Activity {
    private EditText msg_text;
    private Button msg_send;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        C2DMManager.getInstance().initRegistrationID(this, "aucd29@gmail.com");
        C2DMManager.getInstance().initAuthToken(this, "aucd29@gmail.com", "xxxxxx");

        msg_text = (EditText) findViewById(R.id.msg_text);
        msg_send = (Button) findViewById(R.id.msg_send);

        msg_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 메세지를 보낸다.
                try {
                    String msg = "data.msg=" + URLEncoder.encode("Hello My Friends");
                    C2DMManager.getInstance().send(C2DMManager.getInstance().getRegistrationID(), msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}