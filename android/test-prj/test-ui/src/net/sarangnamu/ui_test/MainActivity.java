package net.sarangnamu.ui_test;

import java.util.ArrayList;
import java.util.Collections;

import net.sarangnamu.ui_test.common.ConfigPref;
import net.sarangnamu.ui_test.common.ListAnimationManager;
import net.sarangnamu.ui_test.common.ListAnimationManager.ListAnimationListener;
import net.sarangnamu.ui_test.common.ListSwipeListener;
import net.sarangnamu.ui_test.lockscreen.LockScreenService;
import net.sarangnamu.ui_test.notification.NotificationBase;
import net.sarangnamu.ui_test.receiver.NotificationReceiver;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private ListView list;
    private CardAdapter adapter;
    private ArrayList<String> data = new ArrayList<String>();
    private NotificationBase notif;
    private LinearLayout root;
    private ImageButton option;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(MainActivity.this, LockScreenService.class));
        Log.d(TAG, "== onCreate ==");

        root   = (LinearLayout) findViewById(R.id.rootview);
        option = (ImageButton) findViewById(R.id.option);
        txt = (TextView) findViewById(R.id.txt);

        initListView();
        initNotification();

        option.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ListAnimationManager.start(MainActivity.this, list.getChildAt(2), null);
                ListAnimationManager.start(MainActivity.this, list.getChildAt(0), new ListAnimationListener() {
                    @Override
                    public void onAnimationEnd() {
                        Collections.swap(data, 0, 2);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d(TAG, "== onNewIntent ==");

        Bundle bd   = intent.getExtras();
        if (bd == null) {
            return ;
        }

        String data = bd.getString(NotificationReceiver.NOTI, null);

        if (data != null) {
            updateNotification();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initListView() {
        list = (ListView) findViewById(R.id.list);

        data.add("Monitering 0");
        data.add("Energy 1");
        data.add("Media 2");
        data.add("Monitering 3");
        data.add("Energy 4");
        data.add("Media 5");

        adapter = new CardAdapter();

        list.setAdapter(adapter);
        list.setOnTouchListener(new ListSwipeListener(MainActivity.this, list) {
            @Override
            public void onSwipeTop(int position) {
            }

            @Override
            public void onSwipeRight(final int position) {
                Log.i(TAG, "onSwipeRight " + position);
                Log.i(TAG, "onSwipeRight " + position + " count " + list.getChildCount() );
                Log.i(TAG, "onSwipeRight " + position);

                ListAnimationManager.start(MainActivity.this, list.getChildAt(position), new ListAnimationListener() {
                    @Override
                    public void onAnimationEnd() {
                        data.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onSwipeLeft(int position) {
            }

            @Override
            public void onSwipeBottom(int position) {
            }

            @Override
            public void onPinchZoom(int position) {
                Log.d(TAG, "== onPinchZoom ==");

                Holder holder = (Holder) list.getChildAt(position).getTag();
                holder.title.setText("change pinch zoom ======");
            }
        });
    }

    private void initNotification() {
        notif = new NotificationBase(this, R.layout.remote);
        updateNotification();
    }

    private static int reqNumber = 0;

    private void updateNotification() {
        RemoteViews rview = notif.getRemoteViews();

        notif.setOnClickPendingIntent(R.id.btn, reqNumber++, "cmd", "plus");

        int btnValue = Integer.parseInt(ConfigPref.getValue(MainActivity.this, NotificationReceiver.BTN));
        for (int i=0; i<4; ++i) {
            int id = getResources().getIdentifier("btn" + (i+1), "id", getPackageName());
            int type = getResources().getIdentifier("type" + (i+1), "id", getPackageName());

            notif.setOnClickPendingIntent(id, reqNumber++, "btn", "" + i);

            if (btnValue == i) {
                rview.setImageViewResource(id, R.drawable.advisor_icon_info);
                rview.setInt(type, "setVisibility", View.VISIBLE);
            } else {
                rview.setImageViewResource(id, R.drawable.advisor_icon_alert);
                rview.setInt(type, "setVisibility", View.GONE);
            }
        }

        notif.invalidate();
    }

    class Holder {
        TextView title;
        LinearLayout content;
    }

    class CardAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.main_cardbase, null);

                holder = new Holder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.content = (LinearLayout) convertView.findViewById(R.id.content);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.title.setText(data.get(position));

            return convertView;
        }
    }
}

