package net.sarangnamu.ems_tracking;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.DimTool;
import net.sarangnamu.common.sqlite.DbManager;
import net.sarangnamu.common.ui.dlg.DlgTimer;
import net.sarangnamu.ems_tracking.api.Api;
import net.sarangnamu.ems_tracking.api.xml.Ems;
import net.sarangnamu.ems_tracking.cfg.Config;
import net.sarangnamu.ems_tracking.db.EmsDbHelper;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends ListActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final long SHOW_PROGRESS = 100000000;
    private static final int SHOW_POPUP = 1;
    private static final int SLIDING_MARGIN = 130;

    private Button add;
    private TextView title, empty; //, path, dev, tvSearch;
    private EditText emsNum;
    private EmsAdapter adapter;
    private ProgressDialog dlg;

    private final Handler handler = new Handler() {
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

        add      = (Button) findViewById(R.id.add);
        title    = (TextView) findViewById(R.id.title);
        empty    = (TextView) findViewById(android.R.id.empty);
        emsNum   = (EditText) findViewById(R.id.emsNum);

        initLabel();
        initData();
    }

    private void initLabel() {
        title.setText(Html.fromHtml(getString(R.string.appName)));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String num = emsNum.getText().toString();

                if (num == null || num.length() < 1) {
                    showPopup(getString(R.string.plsInputNum));
                    return ;
                }

                if (!Config.isEmsNumber(num)) {
                    showPopup(getString(R.string.invalidEmsNum));
                    return ;
                }

                new AsyncTask<Context, Void, Boolean>() {
                    @Override
                    protected void onPreExecute() {
                        showProgress();
                    }

                    @Override
                    protected Boolean doInBackground(Context... contexts) {
                        Context context = contexts[0];

                        Ems ems = Api.tracking(num);
                        //ems.trace();

                        return EmsDbHelper.insert(ems);
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        dlg.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                }.execute(getApplicationContext());
            }
        });
    }

    @Override
    protected void onResume() {
        DbManager.getInstance().open(MainActivity.this, new EmsDbHelper(MainActivity.this));

        super.onResume();
    }

    private void initData() {
        DbManager.getInstance().open(MainActivity.this, new EmsDbHelper(MainActivity.this));

        new AsyncTask<Context, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                showProgress();
            }

            @Override
            protected Boolean doInBackground(Context... contexts) {
                Context context = contexts[0];

                try {
                    Cursor cr = EmsDbHelper.select();
                    while (cr.moveToNext()) {
                        // load tracking numbers
                        String num = cr.getString(0);
                        Ems ems = Api.tracking(num);

                        EmsDbHelper.update(cr.getInt(1), ems);
                    }
                } catch (Exception e) {
                    DLog.e(TAG, "initData", e);

                    return false;
                }

                return true;
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

    private void hideProgress() {
        if (dlg != null) {
            dlg.dismiss();
        }
    }

    private void initListView() {
        //        checkedList = new boolean[data.size()];
        adapter = new EmsAdapter(MainActivity.this, EmsDbHelper.selectDesc());
        setListAdapter(adapter);
        getListView().setEmptyView(empty);
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

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
        DlgTimer dlg = new DlgTimer(MainActivity.this, R.layout.dlg_timer);
        dlg.setMessage(msg);
        dlg.setTime(1000);
        dlg.show();
        dlg.setTransparentBaseLayout();
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
    // ADAPTER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        TextView emsNum, status, detail;
        LinearLayout btnLayout;
        RelativeLayout row;
    }

    class EmsAdapter extends CursorAdapter {
        public EmsAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public void bindView(View view, Context context, Cursor cr) {
            TextView emsNum = (TextView) view.findViewById(R.id.emsNum);
            TextView date   = (TextView) view.findViewById(R.id.date);
            TextView status = (TextView) view.findViewById(R.id.status);
            TextView office = (TextView) view.findViewById(R.id.office);
            TextView detail = (TextView) view.findViewById(R.id.detail);

            int pos = 1;
            emsNum.setText(cr.getString(pos++));
            date.setText(cr.getString(pos++));
            status.setText(cr.getString(pos++));
            office.setText(String.format(getString(R.string.postOffice), cr.getString(pos++)));

            String str = cr.getString(pos++);
            if (str != null && str.length() > 0) {
                detail.setText(str);
            } else {
                detail.setVisibility(View.GONE);
            }
        }

        @Override
        public View newView(Context context, Cursor arg1, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item, parent, false);
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
