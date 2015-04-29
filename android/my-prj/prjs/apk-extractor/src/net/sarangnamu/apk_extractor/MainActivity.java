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
package net.sarangnamu.apk_extractor;

import java.io.File;
import java.util.ArrayList;

import net.sarangnamu.apk_extractor.AppList.PkgInfo;
import net.sarangnamu.apk_extractor.cfg.Cfg;
import net.sarangnamu.apk_extractor.dlg.DlgEmail;
import net.sarangnamu.apk_extractor.dlg.DlgSpecialThanks;
import net.sarangnamu.common.BkCfg;
import net.sarangnamu.common.BkFile;
import net.sarangnamu.common.BkFile.FileCopyDetailListener;
import net.sarangnamu.common.BkString;
import net.sarangnamu.common.DLog;
import net.sarangnamu.common.DimTool;
import net.sarangnamu.common.ani.FadeColor;
import net.sarangnamu.common.explorer.DirChooserActivity;
import net.sarangnamu.common.fonts.FontLoader;
import net.sarangnamu.common.ui.MenuManager;
import net.sarangnamu.common.ui.StatusBar;
import net.sarangnamu.common.ui.dlg.DlgLicense;
import net.sarangnamu.common.ui.dlg.DlgTimer;
import net.sarangnamu.common.ui.list.AniBtnListView;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends ListActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private static final long SHOW_PROGRESS = 2000000;
    private static final int SHOW_POPUP = 1;
    private static final int UPDATE_PROGRESS_BAR = 2;
    private static final int HIDE_PROGRESS_BAR = 3;

    private static final int SLIDING_MARGIN = 160;

    private static final int ET_SDCARD = 0;
    private static final int ET_EMAIL  = 1;
    private static final int ET_MENU   = 2;
    private static final int ET_DELETE = 3;

    private static final int EMAIL_ACTIVITY = 100;
    private static final int DIR_ACTIVITY = 200;
    private static final int DEL_ACTIVITY = 300;

    private boolean sendEmail = false, searchedList = false;
    private int deletedPosition = -1;
    private TextView title, path, dev, tvSearch, empty;
    private EditText search;
    private AppAdapter adapter;
    private ImageButton menu;
    private RelativeLayout titleBar;
    private ProgressBar sdProgressBar;
    private ProgressDialog dlg;
    private ArrayList<PkgInfo> data;
    private ArrayList<PkgInfo> searchedData;

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // HANDLER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case SHOW_POPUP:
                String fileName = (String) msg.obj;

                DlgTimer dlg = new DlgTimer(MainActivity.this, R.layout.dlg_timer_extract_file);
                dlg.setMessage(fileName);
                dlg.setTime(1500);
                dlg.show();
                dlg.setTransparentBaseLayout();
                break;

            case UPDATE_PROGRESS_BAR:
                sdProgressBar.setProgress(msg.arg1);
                break;

            case HIDE_PROGRESS_BAR:
                sdProgressBar.setVisibility(View.GONE);
                sdProgressBar.setProgress(0);
                break;
            }
        }
    };

    private void sendMessage(int type, Object obj) {
        Message msg = handler.obtainMessage();
        msg.what = type;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    private void sendMessage(int type, int arg) {
        Message msg = handler.obtainMessage();
        msg.what = type;
        msg.arg1 = arg;
        handler.sendMessage(msg);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // SYSTEM METHODS
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title    = (TextView) findViewById(R.id.title);
        path     = (TextView) findViewById(R.id.path);
        dev      = (TextView) findViewById(R.id.dev);
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        empty    = (TextView) findViewById(android.R.id.empty);
        search   = (EditText) findViewById(R.id.search);
        menu     = (ImageButton) findViewById(R.id.menu);
        titleBar = (RelativeLayout) findViewById(R.id.titleBar);
        sdProgressBar = (ProgressBar) findViewById(R.id.sdProgressBar);

        initLabel();
        initMenu();
        initSearch();
        initData(true);

        StatusBar.setColor(getWindow(), 0xff0e5cbc);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
        case DIR_ACTIVITY:
            if (resultCode == RESULT_OK) {
                String path = intent.getStringExtra("path");
                if (path != null) {
                    Cfg.setUserPath(this, path);
                    setDownloadPath();
                }
            }
            break;

        case EMAIL_ACTIVITY:
            if (resultCode == RESULT_OK) {
            } else if (resultCode == RESULT_CANCELED) {

            } else {
                showPopup(getString(R.string.sendMailFail));
            }
            break;

        case DEL_ACTIVITY:
            if (deletedPosition == -1) {
                return ;
            }

            PkgInfo info = getPkgInfo(deletedPosition);
            try {
                getPackageManager().getApplicationInfo(info.pkgName, 0);
            } catch (NameNotFoundException e) {
                DLog.e(TAG, "onActivityResult", e);
                removeDataListAndRefereshList(deletedPosition);
            }

            deletedPosition = -1;
            break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            menu.performClick();
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (searchedList) {
            searchedData.clear();
            searchedList = false;

            BaseAdapter tmpAdapter = (BaseAdapter) getListAdapter();
            if (tmpAdapter != null) {
                tmpAdapter.notifyDataSetChanged();
            }

            return;
        } else if (search.getVisibility() != View.GONE) {
            setSearchUi();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        BkCfg.hideKeyboard(search);

        super.onPause();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // PRIVATE
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private void initLabel() {
        title.setText(Html.fromHtml(getString(R.string.appName)));

        setDownloadPath();

        String src = String.format("<b>%s</b> <a href='http://sarangnamu.net'>@aucd29</a>", getString(R.string.dev));
        dev.setText(Html.fromHtml(src));
        sdProgressBar.setMax(100);
    }

    private void setDownloadPath() {
        String dnPath = Cfg.getDownPath(this);

        String src = String.format("<b>%s</b> : %s", getString(R.string.downloadPath), dnPath.replace(BkCfg.sdPath(), "/sdcard"));
        path.setText(Html.fromHtml(src));
    }

    private void initMenu() {
        MenuManager.getInstance().setListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                case R.id.mnu_search:
                    setSearchUi();
                    break;
                case R.id.mnu_email:
                    showEmailDlg();
                    break;
                case R.id.mnu_license:
                    showLicenseDlg();
                    break;
                case R.id.mnu_setSdPath:
                    showFileExplorer();
                    break;
                case R.id.mnu_showSystemApp:
                    showSystemApp();
                    break;
                case R.id.mnu_showInstalledApp:
                    showInstalledApp();
                    break;
                case R.id.mnu_specialThanks:
                    showSpecialThanks();
                    break;
                }

                return false;
            }

            void showEmailDlg() {
                DlgEmail dlg = new DlgEmail(MainActivity.this);
                dlg.show();
            }

            void showLicenseDlg() {
                DlgLicense dlg = new DlgLicense(MainActivity.this);
                dlg.setTitleTypeface(FontLoader.getInstance(getApplicationContext()).getRobotoLight());
                dlg.show();
            }

            void showSpecialThanks() {
                DlgSpecialThanks dlg = new DlgSpecialThanks(MainActivity.this);
                dlg.setTitleTypeface(FontLoader.getInstance(getApplicationContext()).getRobotoLight());
                dlg.setTitle("Special Thanks");
                dlg.show();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resid;

                if (getShowOption()) {
                    resid = R.menu.main;
                } else {
                    resid = R.menu.main2;
                }

                MenuManager.getInstance().showMenu(MainActivity.this, v, resid);
            }
        });
    }

    private void initSearch() {
        BkCfg.forceHideKeyboard(getWindow());

        search.setImeOptions(EditorInfo.IME_ACTION_DONE);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchedData == null) {
                    searchedData = new ArrayList<PkgInfo>();
                }

                searchedData.clear();
                String keyword = search.getText().toString();

                if (keyword != null && keyword.length() > 0) {
                    searchedList = true;
                    keyword = keyword.toLowerCase();

                    for (PkgInfo info : data) {
                        if (info.appName.toLowerCase().contains(keyword)) {
                            searchedData.add(info);
                        }
                    }
                } else {
                    searchedList = false;
                }

                BaseAdapter tmpAdapter = (BaseAdapter) getListAdapter();
                if (tmpAdapter != null) {
                    tmpAdapter.notifyDataSetChanged();
                }
            }
        });

        search.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    setSearchUi();
                }

                return false;
            }
        });

        search.setTypeface(FontLoader.getInstance(MainActivity.this).getFont("Roboto-Light"));
        BkCfg.hideKeyboard(search);
    }

    private void setSearchUi() {
        if (search.getVisibility() == View.GONE) {
            search.setVisibility(View.VISIBLE);
            tvSearch.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
            FadeColor.startResource(titleBar, R.color.dBg, R.color.dBgSearch, null);

            search.setText("");

            BkCfg.showKeyboard(search);
        } else {
            search.setVisibility(View.GONE);
            tvSearch.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            FadeColor.startResource(titleBar, R.color.dBgSearch, R.color.dBg, null);

            BkCfg.hideKeyboard(search);
        }
    }

    private PkgInfo getPkgInfo(int position) {
        if (searchedList) {
            return searchedData.get(position);
        } else {
            return data.get(position);
        }
    }

    private void removeDataListAndRefereshList(int pos) {
        if (searchedList) {
            searchedData.remove(deletedPosition);
        } else {
            data.remove(deletedPosition);
        }

        BaseAdapter adapter = (BaseAdapter) getListAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void initData(final boolean initList) {
        new AsyncTask<Context, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                showProgress();
            }

            @Override
            protected Boolean doInBackground(Context... contexts) {
                Context context = contexts[0];

                if (data != null) {
                    // TODO OutOfMemoryError
                    data.clear();
                    data = null;
                }

                data = AppList.getInstance().getAllApps(context, getShowOption());

                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                dlg.dismiss();

                if (initList) {
                    initListView();
                } else {
                    adapter = null;
                    adapter = new AppAdapter();

                    AniBtnListView list = (AniBtnListView) getListView();
                    list.resetCheckedList();
                    list.setAdapter(adapter);
                }
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

//    private void hideProgress() {
//        if (dlg != null && dlg.isShowing()) {
//            dlg.dismiss();
//            sdProgressBar.setVisibility(View.GONE);
//        }
//    }

    private void initListView() {
        adapter = new AppAdapter();
        setListAdapter(adapter);

        AniBtnListView list = (AniBtnListView) getListView();
        list.setSlidingMargin(SLIDING_MARGIN);
        list.setBtnLayoutId(R.id.btnLayout);
        list.setRowId(R.id.row);
        list.setEmptyView(empty);
    }

    private void sendToSd(int position) {
        final PkgInfo info = getPkgInfo(position);
        if (info.size > SHOW_PROGRESS) {
            showProgress();
            sdProgressBar.setVisibility(View.VISIBLE);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File src = new File(info.srcDir);

                    String fileName = BkString.getFileName(info.srcDir);
                    int pos = fileName.lastIndexOf("-");
                    if (pos != -1) {
                        fileName = fileName.substring(0, pos);
                        fileName += "-" + info.versionName + ".apk";
                    } else {
                        fileName = fileName.replace(".apk", "-" + info.versionName + ".apk");
                    }

                    BkFile.copyFileTo(src, Cfg.getDownPath(MainActivity.this) + fileName, new FileCopyDetailListener() {
                        long fileSize;
                        private boolean cancelFlag = false;

                        @Override
                        public void onCancelled() {
                        }

                        @Override
                        public boolean isCancelled() {
                            return cancelFlag;
                        }

                        @Override
                        public void onFinish(String name) {
                            if (info.size > SHOW_PROGRESS) {
                                sendMessage(HIDE_PROGRESS_BAR, null);
                                dlg.dismiss();
                            }

                            if (sendEmail) {
                                sendToEmail(info, name);
                            } else {
                                String fileName = BkString.getFileName(name);
                                sendMessage(SHOW_POPUP, fileName);
                            }
                        }

                        @Override
                        public void onProcess(int percent) {
                            DLog.d(TAG, "copy progress : " + percent);
                            sendMessage(UPDATE_PROGRESS_BAR, percent);
                        }

                        @Override
                        public void onFileSize(long size) {
                            fileSize = size;
                        }

                        @Override
                        public long getFileSize() {
                            return fileSize;
                        }

                        @Override
                        public void onError(String errMsg) {
                        }
                    });
                } catch (Exception e) {
                    DLog.e(TAG, "onItemClick", e);
                }
            }
        }).start();
    }

    private void sendToEmail(PkgInfo info, String target) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");

        String regEmail = Cfg.getEmail(MainActivity.this);

        if (regEmail == null) {
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {});
        } else {
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { regEmail });
        }

        intent.putExtra(Intent.EXTRA_SUBJECT, "[APK Extractor] Backup " + info.appName);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + target));
        // intent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivityForResult(Intent.createChooser(intent, "Send mail..."), EMAIL_ACTIVITY);
        } catch (android.content.ActivityNotFoundException ex) {
            showPopup(getString(R.string.errorEmail));
        }
    }

    private void showPopup(String msg) {
        DlgTimer dlg = new DlgTimer(MainActivity.this, R.layout.dlg_timer);
        dlg.setMessage(msg);
        dlg.setTime(1000);
        dlg.show();
        dlg.setTransparentBaseLayout();
    }

    private int dpToPixelInt(int dp) {
        return DimTool.dpToPixelInt(getApplicationContext(), dp);
    }

    private void showFileExplorer() {
        Intent intent = new Intent(this, DirChooserActivity.class);

        startActivityForResult(intent, DIR_ACTIVITY);
    }

    private void showSystemApp() {
        Cfg.setShowOption(MainActivity.this, "1");
        initData(false);
    }

    private void showInstalledApp() {
        Cfg.setShowOption(MainActivity.this, "0");
        initData(false);
    }

    private boolean getShowOption() {
        String opt = Cfg.getShowOption(MainActivity.this);
        if (opt.equals("0")) {
            return true;
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // APP ADAPTER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        ImageView icon;
        TextView name, size, pkgName, version, sd, email, delete;
        LinearLayout btnLayout;
        RelativeLayout row;
    }

    class PosHolder {
        int position;
        int type;
        View row;

        public PosHolder(int pos, int type, View row) {
            this.position = pos;
            this.type = type;
            this.row = row;
        }
    }

    class AppAdapter extends BaseAdapter {
        public int margin;

        public AppAdapter() {
            margin = dpToPixelInt(SLIDING_MARGIN) * -1;
        }

        @Override
        public int getCount() {
            if (searchedList) {
                if (searchedData == null) {
                    return 0;
                }

                return searchedData.size();
            }

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

        @SuppressWarnings("deprecation")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item, null);

                holder = new ViewHolder();
                holder.icon      = (ImageView) convertView.findViewById(R.id.icon);
                holder.name      = (TextView) convertView.findViewById(R.id.name);
                holder.size      = (TextView) convertView.findViewById(R.id.size);
                holder.pkgName   = (TextView) convertView.findViewById(R.id.pkgName);
                holder.version   = (TextView) convertView.findViewById(R.id.version);
                holder.sd        = (TextView) convertView.findViewById(R.id.sd);
                holder.email     = (TextView) convertView.findViewById(R.id.email);
                holder.delete    = (TextView) convertView.findViewById(R.id.delete);
                holder.row       = (RelativeLayout) convertView.findViewById(R.id.row);
                holder.btnLayout = (LinearLayout) convertView.findViewById(R.id.btnLayout);

                holder.sd.setOnClickListener(MainActivity.this);
                holder.email.setOnClickListener(MainActivity.this);
                holder.delete.setOnClickListener(MainActivity.this);
                holder.row.setOnClickListener(MainActivity.this);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PkgInfo info = getPkgInfo(position);
            if (info.icon != null) {
                if (Build.VERSION_CODES.JELLY_BEAN < Build.VERSION.SDK_INT) {
                    holder.icon.setBackground(info.icon);
                } else {
                    holder.icon.setBackgroundDrawable(info.icon);
                }
            }
            holder.name.setText(info.appName);
            holder.size.setText(info.appSize);
            holder.pkgName.setText(info.pkgName);
            holder.version.setText("(" + info.versionName + ")");

            holder.sd.setTag(new PosHolder(position, ET_SDCARD, holder.row));
            holder.email.setTag(new PosHolder(position, ET_EMAIL, holder.row));
            holder.delete.setTag(new PosHolder(position, ET_DELETE, holder.row));
            holder.row.setTag(new PosHolder(position, ET_MENU, holder.row));

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
        PosHolder ph = (PosHolder) v.getTag();

        if (ph.type == ET_MENU) {
            if (search.getVisibility() != View.GONE) {
                search.setVisibility(View.GONE);
                tvSearch.setVisibility(View.GONE);
                title.setVisibility(View.VISIBLE);
                FadeColor.startResource(titleBar, R.color.dBgSearch, R.color.dBg, null);

                BkCfg.hideKeyboard(search);
            }

            ((AniBtnListView) getListView()).showAnimation(v);
        } else if (ph.type == ET_DELETE) {
            PkgInfo info = getPkgInfo(ph.position);
            deletedPosition = ph.position;

            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + info.pkgName));

            startActivityForResult(intent, DEL_ACTIVITY);
        } else {
            sendEmail = ph.type == 0 ? false : true;
            sendToSd(ph.position);
        }
    }
}
