package net.sarangnamu.ems_tracking;

import net.sarangnamu.common.BkCfg;
import net.sarangnamu.common.DLog;
import net.sarangnamu.common.DimTool;
import net.sarangnamu.common.sqlite.DbManager;
import net.sarangnamu.common.ui.dlg.DlgTimer;
import net.sarangnamu.common.ui.list.LockListView;
import net.sarangnamu.common.ui.list.LockListView.TouchUpListener;
import net.sarangnamu.common.ui.swipe.SwipeListenerBase;
import net.sarangnamu.ems_tracking.api.Api;
import net.sarangnamu.ems_tracking.api.xml.Ems;
import net.sarangnamu.ems_tracking.cfg.Config;
import net.sarangnamu.ems_tracking.db.EmsDbHelper;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private static final int SLIDING_MARGIN = 124;

    //private boolean[] checkedList;
    private int currPos;
    private boolean checkedList;

    private Button add;
    private TextView title, empty;
    private EditText emsNum;
    private EmsAdapter adapter;
    private ProgressDialog dlg;

    //    private final Handler handler = new Handler() {
    //        @Override
    //        public void handleMessage(Message msg) {
    //            switch (msg.what) {
    //            case SHOW_POPUP:
    //                //                String fileName = (String) msg.obj;
    //                //
    //                //                DlgTimer dlg = new DlgTimer(MainActivity.this, R.layout.dlg_timer);
    //                //                dlg.setMessage(Html.fromHtml(dlgMsg));
    //                //                dlg.setTime(2000);
    //                //                dlg.show();
    //                //                dlg.setTransparentBaseLayout();
    //                break;
    //            }
    //        }
    //    };

    //    private void sendMessage(int type, Object obj) {
    //        Message msg = handler.obtainMessage();
    //        msg.what = type;
    //        msg.obj  = obj;
    //        handler.sendMessage(msg);
    //    }

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
                //FIXME emsNum.setText("");

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
                        BkCfg.hideKeyboard(MainActivity.this);
                        showProgress();
                    }

                    @Override
                    protected Boolean doInBackground(Context... contexts) {
                        Ems ems = Api.tracking(num);

                        return EmsDbHelper.insert(ems);
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        hideProgress();

                        if (result) {
                            Cursor cr = EmsDbHelper.selectDesc();
                            adapter.changeCursor(cr);
                        }
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
        loadEmsData();
    }

    private void loadEmsData() {
        new AsyncTask<Context, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                showProgress();
            }

            @Override
            protected Boolean doInBackground(Context... contexts) {
                try {
                    // FIXME
                    //                    Cursor cr = EmsDbHelper.select();
                    //
                    //                    while (cr.moveToNext()) {
                    //                        String num = cr.getString(0);
                    //                        Ems ems = Api.tracking(num);
                    //                        EmsDataManager.getInstance().setEmsData(num, ems);
                    //
                    //                        EmsDbHelper.update(cr.getInt(1), ems);
                    //                    }
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

    public void showProgress() {
        dlg = new ProgressDialog(MainActivity.this);
        dlg.setCancelable(false);
        dlg.setMessage(getString(R.string.plsWait));
        dlg.show();
        dlg.setContentView(R.layout.dlg_progress);
    }

    public void hideProgress() {
        if (dlg != null) {
            dlg.dismiss();
            dlg = null;
        }
    }

    private void initListView() {
        adapter = new EmsAdapter(MainActivity.this, EmsDbHelper.selectDesc());
        setListAdapter(adapter);
        getListView().setEmptyView(empty);
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String emsNum = (String) view.getTag();

                ((LockListView) getListView()).setLock();
                showAnimation(view, position);

                //                EmsDataManager.getInstance().getAsyncEmsData(MainActivity.this, emsNum, new EmsDataListener() {
                //                    @Override
                //                    public void onEmsData(Ems ems) {
                //                        if (ems == null) {
                //                            DLog.e(TAG, "onEmsData ems == null");
                //                            return ;
                //                        }
                //
                //                        Intent intent = new Intent(MainActivity.this, Detail.class);
                //                        intent.putExtra(EmsDataManager.EMS_NUM, ems.emsNum);
                //                        startActivity(intent);
                //                    }
                //                });
            }
        });
        ((LockListView) getListView()).setOnTouchListener(new TouchUpListener() {
            @Override
            public void up() {
                if (currPos != -1) {
                    View view = getListView().getChildAt(currPos);
                    showAnimation(view, currPos);

                    ((LockListView) getListView()).setLock();
                }
            }
        });
        //getListView().setOnTouchListener(new MyListSwipeListener(MainActivity.this));
    }

    private void showPopup(String msg) {
        DlgTimer dlg = new DlgTimer(MainActivity.this, R.layout.dlg_timer);
        dlg.setMessage(msg);
        dlg.setTime(1000);
        dlg.show();
        dlg.setTransparentBaseLayout();
    }

    private void showAnimation(final View view, int position) {
        final int endX;
        final int moveX = dpToPixelInt(SLIDING_MARGIN);
        View tempView;

        if (checkedList) {
            endX = 0;
            tempView = getListView().getChildAt(currPos);
            currPos = -1;
        } else {
            endX = moveX * -1;
            tempView = view;
            currPos = position;
        }

        checkedList = !checkedList;

        final View btnLayout = tempView.findViewById(R.id.btnLayout);
        final View row       = tempView.findViewById(R.id.row);

        ObjectAnimator.ofFloat(btnLayout, "translationX", endX).start();
        final ObjectAnimator objAni = ObjectAnimator.ofFloat(row, "translationX", endX);
        objAni.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //view.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                objAni.removeAllListeners();
                //view.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animator animation) { }
            @Override
            public void onAnimationCancel(Animator animation) { }
        });
        objAni.start();
    }

    private int dpToPixelInt(int dp) {
        return DimTool.dpToPixelInt(getApplicationContext(), dp);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MyListSwipeListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class MyListSwipeListener extends SwipeListenerBase {
        public MyListSwipeListener(Context context) {
            super(context);

            setThreshold(100);
            setVelocity(100);
        }

        @Override
        public void onSwipeTop(int position) {
        }

        @Override
        public void onSwipeRight(int position) {
        }

        @Override
        public void onSwipeLeft(int position) {
            View view = getListView().getChildAt(position);
            showAnimation(view, position);
        }

        @Override
        public void onSwipeBottom(int position) {
        }

        @Override
        protected int getPosition(int x, int y) {
            return getListView().pointToPosition(x, y) - getListView().getFirstVisiblePosition();
        }
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
            TextView delete = (TextView) view.findViewById(R.id.delete);

            int pos = 0;
            delete.setTag(cr.getInt(pos++));
            delete.setOnClickListener(MainActivity.this);

            String emsNumber = cr.getString(pos++);
            emsNum.setText(emsNumber);
            date.setText(cr.getString(pos++));

            String statusValue = cr.getString(pos++);
            status.setText(statusValue);
            if (statusValue.equals("배달완료")) {
                status.setTextColor(0xff278736);
            }

            office.setText(cr.getString(pos++));
            view.setTag(emsNumber);
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
        // 삭제 문구를 넣을까?
        if (v instanceof TextView) {
            int key = (Integer) v.getTag();
            if (key != 0) {
                boolean res = EmsDbHelper.delete(key);

                if (res) {
                    Cursor cr = EmsDbHelper.selectDesc();
                    adapter.changeCursor(cr);
                }
            }
        }
    }
}
