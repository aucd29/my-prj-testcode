/*
 * MainActivity.java
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
import net.sarangnamu.common.BkSystem;
import net.sarangnamu.common.DLog;
import net.sarangnamu.common.DimTool;
import net.sarangnamu.common.ani.Resize;
import net.sarangnamu.common.ani.Resize.ResizeAnimationListener;
import net.sarangnamu.common.fonts.FontLoader;
import net.sarangnamu.common.sqlite.DbManager;
import net.sarangnamu.common.ui.StatusBar;
import net.sarangnamu.common.ui.dlg.DlgBtnBase.DlgBtnListener;
import net.sarangnamu.common.ui.dlg.DlgLicense;
import net.sarangnamu.common.ui.dlg.DlgNormal;
import net.sarangnamu.common.ui.dlg.DlgTimer;
import net.sarangnamu.common.ui.list.AniBtnListView;
import net.sarangnamu.ems_tracking.EmsDataManager.EmsDataListener;
import net.sarangnamu.ems_tracking.api.Api;
import net.sarangnamu.ems_tracking.api.xml.Ems;
import net.sarangnamu.ems_tracking.cfg.Cfg;
import net.sarangnamu.ems_tracking.db.EmsDbHelper;
import net.sarangnamu.ems_tracking.widget.StatusWidget;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
    private static final int SLIDING_MARGIN = 186;

    private Button add;

    private TextView title, empty;
    private EditText emsNum, anotherName;
    private EmsAdapter adapter;
    private ImageButton refresh;
    private RelativeLayout editLayout;
    private ProgressDialog dlg;
    private boolean expandLayout = false;
    private int modifyId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add         = (Button) findViewById(R.id.add);
        title       = (TextView) findViewById(R.id.title);
        empty       = (TextView) findViewById(android.R.id.empty);
        emsNum      = (EditText) findViewById(R.id.emsNum);
        anotherName = (EditText) findViewById(R.id.anotherName);
        editLayout  = (RelativeLayout) findViewById(R.id.editLayout);
        refresh     = (ImageButton) findViewById(R.id.refersh);

        initLabel();
        initData();
        
        StatusBar.setColor(getWindow(), 0xfff23131);
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

                if (!Cfg.isEmsNumber(num)) {
                    showPopup(getString(R.string.invalidEmsNum));
                    return ;
                }

                trackingAndInsertDB(num);
                Cfg.setAnotherName(getApplicationContext(), num.toUpperCase(), anotherName.getText().toString());
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh.setEnabled(false);
                loadEmsData();
            }
        });

        emsNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {
                int height;
                if (emsNum.getText().length() == 0) {
                    height = (int) getResources().getDimension(R.dimen.emsLayoutMinHeight);
                    Resize.height(editLayout, height, new ResizeAnimationListener() {
                        @Override
                        public void onAnimationEnd() {
                            expandLayout = false;
                        }

                        @Override
                        public void onAnimationStart() {
                            anotherName.setVisibility(View.GONE);
                        }
                    });
                } else {
                    if (expandLayout) {
                        return ;
                    }

                    expandLayout = true;
                    height = (int) getResources().getDimension(R.dimen.emsLayoutMaxHeight);
                    Resize.height(editLayout, height, new ResizeAnimationListener() {
                        @Override
                        public void onAnimationEnd() {
                            if (modifyId == -1) {
                                anotherName.setText("");
                            }
                            anotherName.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationStart() {

                        }
                    });
                }
            }
        });

        BkCfg.engKeyboard(emsNum);

        //        getWindow().setSoftInputMode(
        //                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void trackingAndInsertDB(final String num) {
        new AsyncTask<Context, Void, Boolean>() {
            String errMsg;

            @Override
            protected void onPreExecute() {
                BkCfg.hideKeyboard(emsNum);
                showProgress();
            }

            @Override
            protected Boolean doInBackground(Context... contexts) {
                Ems ems = Api.tracking(num);

                if (modifyId == -1) {
                    return EmsDbHelper.insert(ems);
                } else {
                    return EmsDbHelper.update(modifyId, ems);
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                hideProgress();
                emsNum.setText("");
                anotherName.setText("");

                if (modifyId != -1) {
                    modifyId = -1;
                    add.setText(R.string.add);
                }

                if (result) {
                    Cursor cr = EmsDbHelper.selectDesc();
                    adapter.changeCursor(cr);
                } else {
                    showPopup(errMsg);
                }

                reloadWidget();
            }
        }.execute(getApplicationContext());
    }

    @Override
    protected void onResume() {
        DbManager.getInstance().open(this, new EmsDbHelper(this));

        super.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        if (Cfg.getOptionName(MainActivity.this)) {
            getMenuInflater().inflate(R.menu.main, menu);
        } else {
            getMenuInflater().inflate(R.menu.main_another_name, menu);
        }

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
        case R.id.mnu_hideDlgForAnotherName:
            Cfg.setOptionName(MainActivity.this, false);
            break;
        case R.id.mnu_showDlgForAnotherName:
            Cfg.setOptionName(MainActivity.this, true);
            break;
        case R.id.mnu_license:
            DlgLicense dlg = new DlgLicense(MainActivity.this);
            dlg.setTitleTypeface(FontLoader.getInstance(getApplicationContext()).getRobotoLight());
            dlg.show();
            break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    private void initData() {
        DbManager.getInstance().open(this, new EmsDbHelper(this));
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
                        String num    = cr.getString(0);
                        String status = cr.getString(2);

                        // 배달완료된 항목은 로딩시 체크하지 않는다.
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
                refresh.setEnabled(true);

                hideProgress();
                initListView();
            }
        }.execute(getApplicationContext());
    }

    public void showProgress() {
        dlg = new ProgressDialog(this);
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
        adapter = new EmsAdapter(this, EmsDbHelper.selectDesc());
        setListAdapter(adapter);
        getListView().setEmptyView(empty);

        AniBtnListView list = (AniBtnListView) getListView();
        list.setSlidingMargin(SLIDING_MARGIN);
        list.setBtnLayoutId(R.id.btnLayout);
        list.setRowId(R.id.row);
    }

    private void showPopup(final String msg) {
        DlgTimer dlg = new DlgTimer(this, R.layout.dlg_timer);
        dlg.setMessage(msg);
        dlg.setTime(1000);
        dlg.show();
        dlg.setTransparentBaseLayout();
    }

    private void showDetail(final String emsNum) {
        EmsDataManager.getInstance().getAsyncEmsData(this, emsNum, new EmsDataListener() {
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
            reloadWidget();
        }
    }

    private int dpToPixelInt(int dp) {
        return DimTool.dpToPixelInt(this, dp);
    }

    private void reloadWidget() {
        BkSystem.sendBroadcast(getApplicationContext(), StatusWidget.class, StatusWidget.BTN_REFRESH, null);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ADAPTER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        TextView emsNum, date, status, office, delete, detail, modify;
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

    class TagBase {
        int id;
        String emsNum;

        TagBase(int id, String emsNum) {
            this.id = id;
            this.emsNum = emsNum;
        }
    }

    class DeleteType extends TagBase {
        DeleteType(int id, String emsNum) {
            super(id, emsNum);
        }
    }

    class ModifyType extends TagBase {
        ModifyType(int id, String emsNum) {
            super(id, emsNum);
        }
    }

    class EmsAdapter extends CursorAdapter {
        public EmsAdapter(Context context, Cursor c) {
            super(context, c, true);
        }

        @Override
        public void bindView(View view, Context context, Cursor cr) {
            ViewHolder vh = new ViewHolder();

            vh.emsNum       = (TextView) view.findViewById(R.id.emsNum);
            vh.date         = (TextView) view.findViewById(R.id.date);
            vh.status       = (TextView) view.findViewById(R.id.status);
            vh.office       = (TextView) view.findViewById(R.id.office);
            vh.delete       = (TextView) view.findViewById(R.id.delete);
            vh.detail       = (TextView) view.findViewById(R.id.detail);
            vh.modify       = (TextView) view.findViewById(R.id.modify);
            vh.btnLayout    = (LinearLayout) view.findViewById(R.id.btnLayout);
            vh.row          = (RelativeLayout) view.findViewById(R.id.row);

            int pos = 0;
            int id  = cr.getInt(pos++);
            vh.delete.setOnClickListener(MainActivity.this);
            vh.modify.setOnClickListener(MainActivity.this);

            String emsNumber   = cr.getString(pos++);
            String anotherName = Cfg.getAnotherName(MainActivity.this, emsNumber);
            vh.delete.setTag(new DeleteType(id, emsNumber));
            vh.modify.setTag(new ModifyType(id, emsNumber));

            if (anotherName.equals("")) {
                vh.emsNum.setText(emsNumber);
            } else {
                vh.emsNum.setText(anotherName);
            }

            vh.date.setText(cr.getString(pos++));
            vh.status.setText(cr.getString(pos++));
            vh.detail.setTag(new DetailType(emsNumber, vh.row));
            vh.detail.setOnClickListener(MainActivity.this);
            vh.row.setOnClickListener(MainActivity.this);

            if (anotherName.equals("")) {
                vh.office.setText(cr.getString(pos++));
            } else {
                vh.office.setText("(" + emsNumber + ") " + cr.getString(pos++));
            }

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

            if (obj instanceof DeleteType) {
                DlgNormal dlg = new DlgNormal(this);
                dlg.setCancelable(false);
                dlg.setMessage(R.string.deleteMsg);
                dlg.setOnBtnListener(new DlgBtnListener() {
                    @Override
                    public void ok() {
                        DeleteType typeObj = (DeleteType) obj;
                        int id = typeObj.id;
                        if (id != 0) {
                            deleteItem(id);
                        }

                        Cfg.setAnotherName(MainActivity.this, typeObj.emsNum, "");
                    }
                });
                dlg.show();
                dlg.hideTitle();
                dlg.setDialogSize(dpToPixelInt(330), -1);
            } else if (obj instanceof DetailType) {
                final DetailType type = (DetailType) obj;
                if (type != null) {
                    showDetail(type.emsNum);
                }
            } else if (obj instanceof ModifyType) {
                ModifyType typeObj = (ModifyType) obj;

                emsNum.setText(typeObj.emsNum);
                anotherName.setText(Cfg.getAnotherName(MainActivity.this, typeObj.emsNum));
                add.setText(R.string.modify);

                modifyId = typeObj.id;

                //                DlgAnotherName dlg = new DlgAnotherName(MainActivity.this, num, anotherName);
                //                dlg.setOnDismissListener(new OnDismissListener() {
                //                    @Override
                //                    public void onDismiss(DialogInterface dialog) {
                //                        if (adapter != null) {
                //                            adapter.notifyDataSetChanged();
                //                        }
                //                    }
                //                });
                //                dlg.show();
            }
        }
    }
}
