package net.sarangnamu.ems_tracking;

import net.sarangnamu.common.DimTool;
import net.sarangnamu.common.sqlite.DbManager;
import net.sarangnamu.ems_tracking.db.EmsDbHelper;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends ListActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final long SHOW_PROGRESS = 100000000;
    private static final int SHOW_POPUP = 1;

    private TextView title, path, dev, tvSearch;
    private AppAdapter adapter;
    private ProgressDialog dlg;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case SHOW_POPUP:
                //                String fileName = (String) msg.obj;
                //
                //                DlgTimer dlg = new DlgTimer(MainActivity.this, R.layout.dlg_timer);
                //                dlg.setMessage(Html.fromHtml(dlgMsg));
                //                dlg.setTime(2000);
                //                dlg.show();
                //                dlg.setTransparentBaseLayout();
                break;
            }
        }
    };

    private void sendMessage(int type, Object obj) {
        Message msg = handler.obtainMessage();
        msg.what = type;
        msg.obj  = obj;
        handler.sendMessage(msg);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title    = (TextView) findViewById(R.id.title);
        dev      = (TextView) findViewById(R.id.dev);

        initLabel();
        initData();
    }

    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        // Inflate the menu; this adds items to the action bar if it is present.
    //        getMenuInflater().inflate(R.menu.main, menu);
    //        return true;
    //    }

    private void initLabel() {
        title.setText(Html.fromHtml(getString(R.string.appName)));

        String src;

        src = String.format("<b>%s</b> <a href='http://sarangnamu.net'>@aucd29</a>", getString(R.string.dev));
        dev.setText(Html.fromHtml(src));
    }

    private void initData() {
        new AsyncTask<Context, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                showProgress();
                DbManager.getInstance().open(MainActivity.this, new EmsDbHelper(MainActivity.this));
            }

            @Override
            protected Boolean doInBackground(Context... contexts) {
                Context context = contexts[0];
                //                data = AppList.getInstance().getInstalledApps(context);

                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                dlg.dismiss();
                initListView();
            }
        }.execute(getApplicationContext());
    }

    private void showProgress() {
        dlg = new ProgressDialog(MainActivity.this);
        dlg.setCancelable(false);
        dlg.setMessage(getString(R.string.plsWait));
        dlg.show();
        dlg.setContentView(R.layout.dlg_progress);
    }

    private void initListView() {
        //        checkedList = new boolean[data.size()];
        adapter = new AppAdapter();
        setListAdapter(adapter);

        //        ((LockListView) getListView()).setOnTouchListener(new TouchUpListener() {
        //            @Override
        //            public void up() {
        //                //                if (clickedView != null) {
        //                //                    PosHolder  ph = (PosHolder) clickedView.getTag();
        //                //                    showAnimation(clickedView, ph.position);
        //                //                    clickedView = null;
        //                //
        //                //                    ((LockListView) getListView()).setLock();
        //                //                }
        //            }
        //        });
    }

    private void showPopup(String msg) {
        //        DlgTimer dlg = new DlgTimer(MainActivity.this, R.layout.dlg_timer);
        //        dlg.setMessage(msg);
        //        dlg.setTime(1000);
        //        dlg.show();
        //        dlg.setTransparentBaseLayout();
    }

    private void showAnimation(final View view, int position) {
        //        final int moveX = dpToPixelInt(SLIDING_MARGIN);
        //        final ViewHolder vh = (ViewHolder)((RelativeLayout) view.getParent()).getTag();
        //        final int endX;

        //        if (!checkedList[position]) {
        //            endX = moveX * -1;
        //            checkedList[position] = true;
        //        } else {
        //            endX = 0;
        //            checkedList[position] = false;
        //        }

        //        ObjectAnimator.ofFloat(vh.btnLayout, "translationX", endX).start();
        //        final ObjectAnimator objAni = ObjectAnimator.ofFloat(view, "translationX", endX);
        //        objAni.addListener(new AnimatorListener() {
        //            @Override
        //            public void onAnimationStart(Animator animation) {
        //                view.setClickable(false);
        //            }
        //
        //            @Override
        //            public void onAnimationEnd(Animator animation) {
        //                objAni.removeAllListeners();
        //                view.setClickable(true);
        //            }
        //
        //            @Override
        //            public void onAnimationRepeat(Animator animation) { }
        //            @Override
        //            public void onAnimationCancel(Animator animation) { }
        //        });
        //        objAni.start();
    }

    private int dpToPixelInt(int dp) {
        return DimTool.dpToPixelInt(getApplicationContext(), dp);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // APP ADAPTER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        TextView trackingNumber, origin, destination, status, delete;
        LinearLayout btnLayout;
        RelativeLayout row;
    }

    //    class PosHolder {
    //        int position;
    //        int type;
    //        View row;
    //
    //        public PosHolder(int pos, int type, View row) {
    //            this.position = pos;
    //            this.type = type;
    //            this.row = row;
    //        }
    //    }

    class AppAdapter extends BaseAdapter {
        public int margin;

        public AppAdapter() {
            //            margin = dpToPixelInt(SLIDING_MARGIN) * -1;
        }

        @Override
        public int getCount() {
            //            if (searchedList) {
            //                return searchedData.size();
            //            }
            //
            //            return data.size();

            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        //        @Override
        //        public int getItemViewType(int position) {
        //            return checkedList[position] ? 1 : 0;
        //        }
        //
        //        @Override
        //        public int getViewTypeCount() {
        //            return 2;
        //        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item, null);

                holder = new ViewHolder();
                holder.trackingNumber   = (TextView) convertView.findViewById(R.id.name);
                holder.origin           = (TextView) convertView.findViewById(R.id.origin);
                holder.destination      = (TextView) convertView.findViewById(R.id.destination);
                holder.status           = (TextView) convertView.findViewById(R.id.status);
                holder.delete           = (TextView) convertView.findViewById(R.id.delete);
                holder.row              = (RelativeLayout) convertView.findViewById(R.id.row);
                holder.btnLayout        = (LinearLayout) convertView.findViewById(R.id.btnLayout);

                holder.delete.setOnClickListener(MainActivity.this);
                //                holder.email.setOnClickListener(MainActivity.this);
                //                holder.row.setOnClickListener(MainActivity.this);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //            PkgInfo info;
            //            if (searchedList) {
            //                info = searchedData.get(position);
            //            } else {
            //                info = data.get(position);
            //            }
            //
            //            holder.icon.setBackgroundDrawable(info.icon);
            //            holder.name.setText(info.appName);
            //            holder.size.setText(info.appSize);
            //            holder.pkgName.setText(info.pkgName);
            //            holder.version.setText("(" + info.versionName + ")");
            //
            //            holder.sd.setTag(new PosHolder(position, ET_SDCARD, holder.row));
            //            holder.email.setTag(new PosHolder(position, ET_EMAIL, holder.row));
            //            holder.row.setTag(new PosHolder(position, ET_MENU, holder.row));

            return convertView;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // View.OnClickListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View v) {
        //        PosHolder ph = (PosHolder) v.getTag();

        //        if (ph.type == ET_MENU) {
        //            ((LockListView) getListView()).setLock();
        //
        //            if (clickedView == null) {
        //                clickedView = v;
        //                showAnimation(v, ph.position);
        //            } else {
        //                ph = (PosHolder) clickedView.getTag();
        //                showAnimation(clickedView, ph.position);
        //                clickedView = null;
        //            }
        //        } else {
        //            sendEmail = ph.type == 0 ? false : true;
        //            sendToSd(ph.position);
        //        }
    }
}
