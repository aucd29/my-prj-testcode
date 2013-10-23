package net.sarangnamu.apk_extractor;

import java.io.File;
import java.util.ArrayList;

import net.sarangnamu.apk_extractor.AppList.PkgInfo;
import net.sarangnamu.apk_extractor.cfg.Cfg;
import net.sarangnamu.apk_extractor.dlg.DlgEmail;
import net.sarangnamu.apk_extractor.dlg.DlgLicense;
import net.sarangnamu.common.BkFile;
import net.sarangnamu.common.BkFile.FileCopyListener;
import net.sarangnamu.common.BkString;
import net.sarangnamu.common.DLog;
import net.sarangnamu.common.ui.DimTool;
import net.sarangnamu.common.ui.MenuManager;
import net.sarangnamu.common.ui.dlg.DlgTimer;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
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
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends ListActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final long SHOW_PROGRESS = 100000000;
    private static final int SHOW_POPUP = 1;
    private static final int SLIDING_MARGIN = 130;

    private static final int ET_SDCARD = 0;
    private static final int ET_EMAIL  = 1;
    private static final int ET_MENU   = 2;

    private boolean sendEmail = false;
    private boolean[] checkedList;
    private TextView title, path, dev;
    private AppAdapter adapter;
    private ImageButton menu;
    private ProgressDialog dlg;
    private ArrayList<PkgInfo> data;

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

        title   = (TextView) findViewById(R.id.title);
        path    = (TextView) findViewById(R.id.path);
        dev     = (TextView) findViewById(R.id.dev);
        menu    = (ImageButton) findViewById(R.id.menu);

        initLabel();
        initMenu();
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DLog.e(TAG, "onActivityResult " + requestCode + ", " +resultCode);
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

        getListView().setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    private void sendToSd(int position) {
        final PkgInfo info = data.get(position);

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

    private void showAnimation(final View view, int position) {
        final int moveX = dpToPixelInt(SLIDING_MARGIN);
        final ViewHolder vh = (ViewHolder)((RelativeLayout) view.getParent()).getTag();
        final int endX;

        if (!checkedList[position]) {
            endX = moveX * -1;
            checkedList[position] = true;
        } else {
            endX = 0;
            checkedList[position] = false;
        }

        ObjectAnimator.ofFloat(vh.btnLayout, "translationX", endX).start();
        final ObjectAnimator objAni = ObjectAnimator.ofFloat(view, "translationX", endX);
        objAni.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                objAni.removeAllListeners();

                //                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) view.getLayoutParams();
                //                rlp.leftMargin = endX;
                //                view.setLayoutParams(rlp);

                //                RelativeLayout.LayoutParams llp = (RelativeLayout.LayoutParams) vh.btnLayout.getLayoutParams();
                //                if (endX == 0) {
                //                    llp.rightMargin = moveX * -1;
                //                } else {
                //                    llp.rightMargin = 0;
                //                }
                //                vh.btnLayout.setLayoutParams(llp);

                view.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animator animation) { }
            @Override
            public void onAnimationCancel(Animator animation) { }
        });
        objAni.start();

        //        final ValueAnimator anim = ValueAnimator.ofInt(startX, endX);
        //        anim.addListener(new AnimatorListener() {
        //            @Override
        //            public void onAnimationStart(Animator animation) {
        //                view.setClickable(false);
        //
        //                vh.sd.setText("");
        //                vh.email.setText("");
        //            }
        //
        //            @Override
        //            public void onAnimationRepeat(Animator animation) {}
        //
        //            @Override
        //            public void onAnimationEnd(Animator animation) {
        //                view.setClickable(true);
        //
        //                vh.sd.setText(R.string.sdcard);
        //                vh.email.setText(R.string.email);
        //
        //                anim.removeAllListeners();
        //            }
        //
        //            @Override
        //            public void onAnimationCancel(Animator animation) {}
        //        });
        //
        //        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        //            @Override
        //            public void onAnimationUpdate(ValueAnimator valueAnimator) {
        //                int val = (Integer) valueAnimator.getAnimatedValue();
        //
        //                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
        //                lp.leftMargin = val;
        //
        //                view.setLayoutParams(lp);
        //            }
        //        });
        //        anim.setDuration(200);
        //        anim.start();
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
        public int getItemViewType(int position) {
            return checkedList[position] ? 1 : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
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

            PkgInfo info = data.get(position);
            holder.icon.setBackgroundDrawable(info.icon);
            holder.name.setText(info.appName);
            holder.size.setText(info.appSize);
            holder.pkgName.setText(info.pkgName);
            holder.version.setText("(" + info.versionName + ")");

            holder.sd.setTag(new PosHolder(position, ET_SDCARD, holder.row));
            holder.email.setTag(new PosHolder(position, ET_EMAIL, holder.row));
            holder.row.setTag(new PosHolder(position, ET_MENU, holder.row));

            //            RelativeLayout.LayoutParams llp = (RelativeLayout.LayoutParams) holder.btnLayout.getLayoutParams();
            //            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) holder.row.getLayoutParams();
            //            switch (getItemViewType(position)) {
            //            case 0:
            //                rlp.leftMargin = 0;
            //                //                llp.rightMargin = margin;
            //                break;
            //
            //            default:
            //                rlp.leftMargin = margin;
            //                //                llp.rightMargin = 0;
            //                break;
            //            }
            //            holder.row.setLayoutParams(rlp);
            //            holder.btnLayout.setLayoutParams(llp);

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
            showAnimation(v, ph.position);
        } else {
            sendEmail = ph.type == 0 ? false : true;
            sendToSd(ph.position);
            showAnimation(ph.row, ph.position);
        }
    }
}
