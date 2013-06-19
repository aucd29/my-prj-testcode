package net.sarangnamu.cellreminder;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog.Calls;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CallSMSReminderActivity extends Activity {
    private static final String TAG = "CallSMSReminderActivity";

    private TextView txt = null;
    private Button btn = null;

    private int missedCall = 0;
    private int missedSms = 0;
    public static CallSMSReminderActivity reminder = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        txt = (TextView) findViewById(R.id.reminder);
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        reminder = this;
        refresh();
    }

    @Override
    public void onResume() {
        refresh();
        super.onResume();
    }

    public void getCallInfo() {
        Cursor cursor = getContentResolver().query(Calls.CONTENT_URI, null,
                Calls.TYPE + " = ? AND " + Calls.NEW + " = ?",
                new String[] { Integer.toString(Calls.MISSED_TYPE), "1" },
                Calls.DATE + " DESC ");

        missedCall = cursor.getCount();
        cursor.close();
    }

    public void getSMSInfo() {
        Cursor c = getContentResolver().query(Uri.parse("content://sms/inbox"),
                null, "read = 0", null, null);
        missedSms = c.getCount();
        c.close();
    }

    public void refresh() {
        if (reminder == null) {
            return;
        }

        getCallInfo();
        getSMSInfo();

        StringBuilder sb = new StringBuilder();
        sb.append("call : ");
        sb.append(missedCall);
        sb.append(", sms : ");
        sb.append(missedSms);

        txt.setText(sb.toString());
    }
}