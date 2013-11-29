package net.sarangnamu.apk_extractor;

import java.io.File;
import java.util.ArrayList;

import net.sarangnamu.apk_extractor.AppList.PkgInfo;
import net.sarangnamu.apk_extractor.cfg.Cfg;
import net.sarangnamu.apk_extractor.dlg.DlgEmail;
import net.sarangnamu.apk_extractor.dlg.DlgLicense;
import net.sarangnamu.common.BkCfg;
import net.sarangnamu.common.BkFile;
import net.sarangnamu.common.BkFile.FileCopyListener;
import net.sarangnamu.common.BkString;
import net.sarangnamu.common.DLog;
import net.sarangnamu.common.DimTool;
import net.sarangnamu.common.fonts.FontLoader;
import net.sarangnamu.common.ui.MenuManager;
import net.sarangnamu.common.ui.dlg.DlgTimer;
import net.sarangnamu.common.ui.list.AniBtnListView;
import net.sarangnamu.common.ui.list.LockListView;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class MainActivity extends ListActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final long SHOW_PROGRESS = 10000000;
    private static final int SHOW_POPUP = 1;
    private static final int SLIDING_MARGIN = 130;

    private static final int ET_SDCARD = 0;
    private static final int ET_EMAIL  = 1;
    private static final int ET_MENU   = 2;

    private boolean sendEmail = false, searchedList = false;
    private boolean[] checkedList;
    private TextView title, path, dev, tvSearch;
    private EditText search;
    private AppAdapter adapter;
    private ImageButton menu;
    private RelativeLayout titleBar;
    private ProgressDialog dlg;
    private ArrayList<PkgInfo> data;
    private ArrayList<PkgInfo> searchedData;

    private View clickedView = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case SHOW_POPUP:
                String fileName = (String) msg.obj;
                String dlgMsg = String.format("<b>%s</b><br/>%s", getString(R.string.extractOk), fileName);

                DlgTimer dlg = new DlgTimer(MainActivity.this, R.layout.dlg_timer);
                dlg.setMessage(Html.fromHtml(dlgMsg));
                dlg.setTime(2000);
                dlg.show();
                dlg.setTransparentBaseLayout();
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
        path     = (TextView) findViewById(R.id.path);
        dev      = (TextView) findViewById(R.id.dev);
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        search   = (EditText) findViewById(R.id.search);
        menu     = (ImageButton) findViewById(R.id.menu);
        titleBar = (RelativeLayout) findViewById(R.id.titleBar);

        initLabel();
        initMenu();
        initSearch();
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
            } else if (resultCode == RESULT_CANCELED) {
            } else {
                showPopup(getString(R.string.sendMailFail));
            }
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

    private void initLabel() {
        title.setText(Html.fromHtml(getString(R.string.appName)));

        String src = String.format("<b>%s</b> : %s", getString(R.string.downloadPath), Cfg.getDownPath());
        path.setText(Html.fromHtml(src));

        src = String.format("<b>%s</b> <a href='http://sarangnamu.net'>@aucd29</a>", getString(R.string.dev));
        dev.setText(Html.fromHtml(src));
    }

    private void initMenu() {
        MenuManager.getInstance().setListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                case R.id.mnu_search: {
                    setSearchUi();
                }
                break;

                case R.id.mnu_email: {
                    DlgEmail dlg = new DlgEmail(MainActivity.this);
                    dlg.show();
                }
                break;

                case R.id.mnu_license: {
                    DlgLicense dlg = new DlgLicense(MainActivity.this);
                    dlg.show();
                }
                break;
                }

                return false;
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuManager.getInstance().showMenu(MainActivity.this, v, R.menu.main);
            }
        });
    }

    private void initSearch() {
        search.setImeOptions(EditorInfo.IME_ACTION_DONE);
        search.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
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

                    setSearchUi();
                    adapter.notifyDataSetChanged();
                }

                return false;
            }
        });

        search.setTypeface(FontLoader.getInstance(MainActivity.this).getFont("Roboto-Light"));
    }

    private void setSearchUi() {
        if (search.getVisibility() == View.GONE) {
            search.setVisibility(View.VISIBLE);
            tvSearch.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
            titleBar.setBackgroundResource(R.color.dBgSearch);
            search.setText("");

            BkCfg.showKeyboard(MainActivity.this, search);
        } else {
            search.setVisibility(View.GONE);
            tvSearch.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            titleBar.setBackgroundResource(R.color.dBg);

            BkCfg.hideKeyboard(MainActivity.this);
        }
    }

    private void initData() {
        new AsyncTask<Context, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                showProgress();
            }

            @Override
            protected Boolean doInBackground(Context... contexts) {
                Context context = contexts[0];
                data = AppList.getInstance().getInstalledApps(context);

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
        checkedList = new boolean[data.size()];
        adapter = new AppAdapter();
        setListAdapter(adapter);

        AniBtnListView list = (AniBtnListView) getListView();
        list.setSlidingMargin(SLIDING_MARGIN);
        list.setBtnLayoutId(R.id.btnLayout);
        list.setRowId(R.id.row);

        //        ((LockListView) getListView()).setOnTouchListener(new TouchUpListener() {
        //            @Override
        //            public void up() {
        //                if (clickedView != null) {
        //                    PosHolder  ph = (PosHolder) clickedView.getTag();
        //                    showAnimation(clickedView, ph.position);
        //                    clickedView = null;
        //
        //                    ((LockListView) getListView()).setLock();
        //                }
        //            }
        //        });
    }

    private void sendToSd(int position) {
        final PkgInfo info;

        if (searchedList) {
            info = searchedData.get(position);
        } else {
            info = data.get(position);
        }

        if (info.size > SHOW_PROGRESS) {
            showProgress();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File src = new File(info.srcDir);
                    BkFile.copyFile(src, Cfg.getDownPath(), new FileCopyListener() {
                        @Override
                        public void onCancelled() {
                        }

                        @Override
                        public boolean isCancelled() {
                            return false;
                        }

                        @Override
                        public void copyFile(String name) {
                            if (sendEmail) {
                                sendToEmail(info, name);
                            } else {
                                if (info.size > SHOW_PROGRESS) {
                                    dlg.dismiss();
                                }

                                String fileName = BkString.getFileName(name);
                                sendMessage(SHOW_POPUP, fileName);
                            }
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
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {regEmail});
        }

        intent.putExtra(Intent.EXTRA_SUBJECT, "[APK Extractor] Backup " + info.appName);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + target));

        try {
            startActivityForResult(Intent.createChooser(intent, "Send mail..."), 100);
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

    //    private void showAnimation(final View view, int position) {
    //        final int moveX = dpToPixelInt(SLIDING_MARGIN);
    //        final ViewHolder vh = (ViewHolder)((RelativeLayout) view.getParent()).getTag();
    //        final int endX;
    //
    //        if (!checkedList[position]) {
    //            endX = moveX * -1;
    //            checkedList[position] = true;
    //        } else {
    //            endX = 0;
    //            checkedList[position] = false;
    //        }
    //
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
    //    }

    private int dpToPixelInt(int dp) {
        return DimTool.dpToPixelInt(getApplicationContext(), dp);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // APP ADAPTER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        ImageView icon;
        TextView name, size, pkgName, version, sd, email;
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
                holder.row       = (RelativeLayout) convertView.findViewById(R.id.row);
                holder.btnLayout = (LinearLayout) convertView.findViewById(R.id.btnLayout);

                holder.sd.setOnClickListener(MainActivity.this);
                holder.email.setOnClickListener(MainActivity.this);
                holder.row.setOnClickListener(MainActivity.this);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PkgInfo info;
            if (searchedList) {
                info = searchedData.get(position);
            } else {
                info = data.get(position);
            }

            holder.icon.setBackgroundDrawable(info.icon);
            holder.name.setText(info.appName);
            holder.size.setText(info.appSize);
            holder.pkgName.setText(info.pkgName);
            holder.version.setText("(" + info.versionName + ")");

            holder.sd.setTag(new PosHolder(position, ET_SDCARD, holder.row));
            holder.email.setTag(new PosHolder(position, ET_EMAIL, holder.row));
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
            ((LockListView) getListView()).setLock();

            if (clickedView == null) {
                clickedView = v;
                ((AniBtnListView) getListView()).showAnimation(v);
                //                showAnimation(v, ph.position);
            } else {
                ph = (PosHolder) clickedView.getTag();
                ((AniBtnListView) getListView()).showAnimation(clickedView);
                //                showAnimation(clickedView, ph.position);
                clickedView = null;
            }
        } else {
            sendEmail = ph.type == 0 ? false : true;
            sendToSd(ph.position);
        }
    }
}
