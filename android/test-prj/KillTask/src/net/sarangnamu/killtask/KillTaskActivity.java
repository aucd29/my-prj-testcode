package net.sarangnamu.killtask;

import java.util.ArrayList;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class KillTaskActivity extends Activity {
    private static final String TAG = "KillTaskActivity";

    private Button btn;
    private TextView txt;
    private TextView txt2;
    private ArrayList<String> runningTasks = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btn = (Button) findViewById(R.id.button1);
        txt = (TextView) findViewById(R.id.textView1);
        txt2 = (TextView) findViewById(R.id.textView2);

        btn.setText("Kill background process");
        txt.setText(getAvalibleMemory());

        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TaskKillManager.killProcess(KillTaskActivity.this, runningTasks);

                refresh();
                txt.setText(getAvalibleMemory());
            }
        });
    }

    public String getAvalibleMemory() {
        StringBuilder sb = new StringBuilder();
        sb.append("Available Memory: ");
        sb.append(TaskKillManager.getAvailbleMemoryMB(this));
        sb.append("MB");

        return sb.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();

        refresh();
    }

    private void refresh() {
        runningTasks = TaskKillManager.getRunningProcessList(this);
        setRunningData();
    }

    private void setRunningData() {
        StringBuilder sb = new StringBuilder();
        PackageManager pkManager = getPackageManager();

        try {
            for (String data : runningTasks) {
                ApplicationInfo appInfo = pkManager.getApplicationInfo(data, 0);
                String name = " (" + appInfo.loadLabel(pkManager).toString() + ")";
                sb.append(data + name + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        txt2.setText(sb.toString());
    }
}