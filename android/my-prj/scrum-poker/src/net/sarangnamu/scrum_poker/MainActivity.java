package net.sarangnamu.scrum_poker;

import net.sarangnamu.common.ui.ActionBarDecorator;
import net.sarangnamu.scrum_poker.page.MainFrgmt;
import net.sarangnamu.scrum_poker.page.PageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private ListView leftMenu;
    private DrawerLayout drawer;
    private ActionBarDecorator actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        leftMenu = (ListView) findViewById(R.id.leftMenu);
        drawer   = (DrawerLayout) findViewById(R.id.drawer);

        initActionBar();

        if (savedInstanceState == null) {
            initPageManager();
        }

        initActionBar();
        initDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void initPageManager() {
        PageManager.getInstance(this).add(R.id.content_frame, MainFrgmt.class);
    }

    private void initActionBar() {
        //actionBar = new ActionBarDecorator(this);
        //actionBar.init(R.layout.actionbar);
    }

    private void initDrawer() {
        drawer.setDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerStateChanged(int arg0) {
            }

            @Override
            public void onDrawerSlide(View arg0, float arg1) {
            }

            @Override
            public void onDrawerOpened(View arg0) {
            }

            @Override
            public void onDrawerClosed(View arg0) {
            }
        });

        leftMenu.setAdapter(new MenuAdapter());
        leftMenu.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // MENU ADAPTER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class MenuViewHolder {
        TextView menu;
    }

    class MenuAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            MenuViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.scrum_item, null);

                holder = new MenuViewHolder();

                convertView.setTag(holder);
            } else {
                holder = (MenuViewHolder) convertView.getTag();
            }


            return convertView;
        }
    }
}
