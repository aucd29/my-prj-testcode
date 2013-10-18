package net.sarangnamu.apk_extractor;

import java.util.ArrayList;

import net.sarangnamu.apk_extractor.AppList.PkgInfo;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
    private AppAdapter adapter;
    private ProgressDialog dlg;
    private ArrayList<PkgInfo> data;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (TextView) findViewById(R.id.title);
        title.setText(Html.fromHtml(getString(R.string.appName)));

        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void initData() {
        new AsyncTask<Context, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                dlg = new ProgressDialog(MainActivity.this);
                dlg.setMessage(getString(R.string.plsWait));
                dlg.show();
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
                initAdapter();
            }
        }.execute(getApplicationContext());
    }

    private void initAdapter() {
        adapter = new AppAdapter();
        setListAdapter(adapter);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // adapter
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        ImageView icon;
        TextView name, size, pkgName, version;
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

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PkgInfo info = data.get(position);
            holder.icon.setBackgroundDrawable(info.icon);
            holder.name.setText(info.appName);
            holder.size.setText(info.size);
            holder.pkgName.setText(info.pkgName);
            holder.version.setText("(" + info.versionName + ")");

            return convertView;
        }
    }
}
