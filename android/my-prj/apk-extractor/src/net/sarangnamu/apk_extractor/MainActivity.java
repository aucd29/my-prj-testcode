package net.sarangnamu.apk_extractor;

import java.io.File;
import java.util.ArrayList;

import net.sarangnamu.apk_extractor.AppList.PkgInfo;
import net.sarangnamu.common.BkFile;
import net.sarangnamu.common.BkFile.FileCopyListener;
import net.sarangnamu.common.BkString;
import net.sarangnamu.common.DLog;
import net.sarangnamu.common.ui.dlg.DlgTimer;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends ListActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final long SHOW_PROGRESS = 100000000;
    private static final int SHOW_TOAST = 1;

    private boolean sendEmail = false;
    private AppAdapter adapter;
    private ProgressDialog dlg;
    private ArrayList<PkgInfo> data;
    private TextView title, path, dev;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case SHOW_TOAST:
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

        title = (TextView) findViewById(R.id.title);
        path  = (TextView) findViewById(R.id.path);
        dev   = (TextView) findViewById(R.id.dev);

        title.setText(Html.fromHtml(getString(R.string.appName)));

        String src = String.format("<b>%s</b> : %s", getString(R.string.downloadPath), Cfg.getDownPath());
        path.setText(Html.fromHtml(src));

        src = String.format("<b>%s</b> <a href='http://sarangnamu.net'>@aucd29</a>", getString(R.string.dev));
        dev.setText(Html.fromHtml(src));

        initData();
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
        dlg.setMessage(getString(R.string.plsWait));
        dlg.show();
    }

    private void initListView() {
        adapter = new AppAdapter();
        setListAdapter(adapter);

        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredWidth(), view.getMeasuredWidth() - 500);
                anim.addListener(new AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}
                    @Override
                    public void onAnimationRepeat(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}
                });

                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();

                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        lp.width = val;

                        view.setLayoutParams(lp);
                    }
                });
                anim.setDuration(500);
                anim.start();


                // animation

                //                Animation outAnimation;
                //                LinearLayout a1 = view;
                //                outAnimation = (Animation) AnimationUtils.loadAnimation (getApplicationContext(), R.anim.slide_out_left);
                //
                //                a1.setAnimation(outAnimation);
                //                outAnimation.setAnimationListener(new  AnimationListener() {
                //                    @Override
                //                    public void onAnimationEnd(Animation arg0) {
                //                        //a1.setVisibility(View.GONE);
                //                    }
                //                    @Override
                //                    public void onAnimationRepeat(Animation animation) {
                //                        // TODO Auto-generated method stub
                //                    }
                //
                //                    @Override
                //                    public void onAnimationStart(Animation animation) {
                //                        // TODO Auto-generated method stub
                //                    }
                //                });
                //                a1.startAnimation(outAnimation);
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // adapter
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        ImageView icon;
        TextView name, size, pkgName, version, sd, email;
        RelativeLayout row;
    }

    class PosHolder {
        int position;
        int type;

        public PosHolder(int pos, int type) {
            this.position = pos;
            this.type = type;
        }
    }

    class AppAdapter extends BaseAdapter {
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
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item, null);

                holder = new ViewHolder();
                holder.icon     = (ImageView) convertView.findViewById(R.id.icon);
                holder.name     = (TextView) convertView.findViewById(R.id.name);
                holder.size     = (TextView) convertView.findViewById(R.id.size);
                holder.pkgName  = (TextView) convertView.findViewById(R.id.pkgName);
                holder.version  = (TextView) convertView.findViewById(R.id.version);
                holder.sd       = (TextView) convertView.findViewById(R.id.sd);
                holder.email    = (TextView) convertView.findViewById(R.id.email);
                holder.row      = (RelativeLayout) convertView.findViewById(R.id.row);

                holder.sd.setOnClickListener(MainActivity.this);
                holder.email.setOnClickListener(MainActivity.this);

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

            holder.sd.setTag(new PosHolder(position, 0));
            holder.email.setTag(new PosHolder(position, 1));

            return convertView;
        }
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
                            DLog.d(TAG, "===================================================================");
                            DLog.d(TAG, "ok copied " + name);
                            DLog.d(TAG, "===================================================================");

                            if (sendEmail) {
                                sendToEmail(info, name);
                            } else {
                                if (info.size > SHOW_PROGRESS) {
                                    dlg.dismiss();
                                }

                                String fileName = BkString.getFileName(name);
                                sendMessage(SHOW_TOAST, fileName);
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
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[] {"aucd29@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "[APK Extractor] Backup " + info.appName);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + target));

        try {
            startActivityForResult(Intent.createChooser(intent, "Send mail..."), 100);
        } catch (android.content.ActivityNotFoundException ex) {
            showPopup(getString(R.string.errorEmail));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                showPopup(getString(R.string.sendMailOk));
            } else if (resultCode == RESULT_CANCELED) {
            } else {
                showPopup(getString(R.string.sendMailFail));
            }
        }
    }

    private void showPopup(String msg) {
        DlgTimer dlg = new DlgTimer(MainActivity.this, R.layout.dlg_timer);
        dlg.setMessage(msg);
        dlg.setTime(1000);
        dlg.show();
        dlg.setTransparentBaseLayout();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // View.OnClickListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View v) {
        PosHolder ph = (PosHolder) v.getTag();

        if (ph.type == 0) {
            sendToSd(ph.position);
        } else {
            sendEmail = true;
            sendToSd(ph.position);
        }
    }

    public class ShowButtonAnimation {

    }
}
