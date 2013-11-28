/**
 * Detail.java
 * Copyright 2013 Burke Choi All rights reserved.
 *             http://www.sarangnamu.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sarangnamu.ems_tracking;

import net.sarangnamu.common.BkCfg;
import net.sarangnamu.common.DLog;
import net.sarangnamu.common.DimTool;
import net.sarangnamu.common.sqlite.DbManager;
import net.sarangnamu.common.ui.dlg.DlgBtnBase.DlgBtnListener;
import net.sarangnamu.common.ui.dlg.DlgNormal;
import net.sarangnamu.common.ui.dlg.DlgTimer;
import net.sarangnamu.common.ui.list.AniBtnListView;
import net.sarangnamu.ems_tracking.EmsDataManager.EmsDataListener;
import net.sarangnamu.ems_tracking.api.Api;
import net.sarangnamu.ems_tracking.api.xml.Ems;
import net.sarangnamu.ems_tracking.cfg.Config;
import net.sarangnamu.ems_tracking.db.EmsDbHelper;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends ListActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int SLIDING_MARGIN = 124;

    private Button add;

    private TextView title, empty;
    private EditText emsNum;
    private EmsAdapter adapter;
    private ImageButton refersh;
    private ProgressDialog dlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add      = (Button) findViewById(R.id.add);
        title    = (TextView) findViewById(R.id.title);
        empty    = (TextView) findViewById(android.R.id.empty);
        emsNum   = (EditText) findViewById(R.id.emsNum);
        refersh  = (ImageButton) findViewById(R.id.refersh);

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
                    String errMsg;

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
                        emsNum.setText("");

                        if (result) {
                            Cursor cr = EmsDbHelper.selectDesc();
                            adapter.changeCursor(cr);
                        } else {
                            showPopup(errMsg);
                        }
                    }
                }.execute(getApplicationContext());
            }
        });

        refersh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refersh.setEnabled(false);
                loadEmsData();
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
                    Cursor cr = EmsDbHelper.select();

                    while (cr.moveToNext()) {
                        String num = cr.getString(0);
                        String status = cr.getString(2);

                        // 배달완료된 항목은 첫 로딩시 체크하지 않는다.
                        if (!status.equals("배달완료")) {
                            Ems ems = Api.tracking(num);
                            EmsDataManager.getInstance().setEmsData(num, ems);
                            EmsDbHelper.update(cr.getInt(1), ems);
                        }
                    }
                } catch (Exception e) {
                    DLog.e(TAG, "initData", e);

                    return false;
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                refersh.setEnabled(true);

                hideProgress();
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

        AniBtnListView list = (AniBtnListView) getListView();
        list.setSlidingMargin(SLIDING_MARGIN);
        list.setBtnLayoutId(R.id.btnLayout);
        list.setRowId(R.id.row);
    }

    private void showPopup(String msg) {
        DlgTimer dlg = new DlgTimer(MainActivity.this, R.layout.dlg_timer);
        dlg.setMessage(msg);
        dlg.setTime(1000);
        dlg.show();
        dlg.setTransparentBaseLayout();
    }

    private void showDetail(final String emsNum) {
        EmsDataManager.getInstance().getAsyncEmsData(MainActivity.this, emsNum, new EmsDataListener() {
            @Override
            public void onEmsData(Ems ems) {
                if (ems == null) {
                    DLog.e(TAG, "onEmsData ems == null");
                    return ;
                }

                Intent intent = new Intent(MainActivity.this, Detail.class);
                intent.putExtra(EmsDataManager.EMS_NUM, ems.emsNum);
                startActivity(intent);
            }
        });
    }

    private void deleteItem(final int id) {
        boolean res = EmsDbHelper.delete(id);

        if (res) {
            Cursor cr = EmsDbHelper.selectDesc();
            adapter.changeCursor(cr);

            showPopup(getString(R.string.deleted));
        }
    }

    private int dpToPixelInt(int dp) {
        return DimTool.dpToPixelInt(MainActivity.this, dp);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ADAPTER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        TextView emsNum, date, status, office, delete, detail;
        LinearLayout btnLayout;
        RelativeLayout row;
    }

    class DetailType {
        String emsNum;
        View row;

        DetailType(String emsNum, View row) {
            this.emsNum = emsNum;
            this.row = row;
        }
    }

    class EmsAdapter extends CursorAdapter {
        public EmsAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public void bindView(View view, Context context, Cursor cr) {
            ViewHolder vh = new ViewHolder();

            vh.emsNum    = (TextView) view.findViewById(R.id.emsNum);
            vh.date      = (TextView) view.findViewById(R.id.date);
            vh.status    = (TextView) view.findViewById(R.id.status);
            vh.office    = (TextView) view.findViewById(R.id.office);
            vh.delete    = (TextView) view.findViewById(R.id.delete);
            vh.detail    = (TextView) view.findViewById(R.id.detail);
            vh.btnLayout = (LinearLayout) view.findViewById(R.id.btnLayout);
            vh.row       = (RelativeLayout) view.findViewById(R.id.row);

            int pos = 0;
            vh.delete.setTag(cr.getInt(pos++));
            vh.delete.setOnClickListener(MainActivity.this);

            String emsNumber = cr.getString(pos++);
            vh.emsNum.setText(emsNumber);
            vh.date.setText(cr.getString(pos++));

            String statusValue = cr.getString(pos++);
            vh.status.setText(statusValue);
            vh.detail.setTag(new DetailType(emsNumber, vh.row));
            vh.detail.setOnClickListener(MainActivity.this);

            vh.office.setText(cr.getString(pos++));
            vh.row.setOnClickListener(MainActivity.this);

            view.setTag(vh);
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
        if (v instanceof RelativeLayout) {
            ((AniBtnListView) getListView()).showAnimation(v);
        } else if (v instanceof TextView) {
            final Object obj = v.getTag();

            if (obj instanceof Integer) {
                DlgNormal dlg = new DlgNormal(MainActivity.this);
                dlg.setCancelable(false);
                dlg.setMessage(R.string.deleteMsg);
                dlg.setOnBtnListener(new DlgBtnListener() {
                    @Override
                    public void ok() {
                        int id = (Integer) obj;
                        if (id != 0) {
                            deleteItem(id);
                        }
                    }
                });
                dlg.show();
                dlg.hideTitle();
                dlg.setDialogSize(dpToPixelInt(330), -1);
            } else if (obj instanceof DetailType) {
                final DetailType type = (DetailType) obj;
                if (type != null) {
                    //((AniBtnListView) getListView()).showAnimation(type.row);
                    //type.row.performClick();

                    showDetail(type.emsNum);
                }
            }
        }
    }
}
