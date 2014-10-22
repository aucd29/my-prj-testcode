package net.sarangnamu.scrum_poker;

import java.util.ArrayList;

import net.sarangnamu.common.sqlite.DbManager;
import net.sarangnamu.common.ui.ActionBarDecorator;
import net.sarangnamu.scrum_poker.db.DbHelper;
import net.sarangnamu.scrum_poker.page.PageManager;
import net.sarangnamu.scrum_poker.page.sub.MainFrgmt;
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
    private ArrayList<MenuData> menuData;

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

    @Override
    protected void onResume() {
        DbManager.getInstance().open(this, new DbHelper(this));

        super.onResume();
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

        if (menuData == null) {
            menuData = new ArrayList<MenuData>();
        }

        menuData.add(new MenuData(LEFT_MENU_TYPE_BAR, getString(R.string.app_name)));
        menuData.add(new MenuData(LEFT_MENU_TYPE_ITEM, getString(R.string.add_rule)));

        // TODO USER MENU WITH DB
        menuData.add(new MenuData(LEFT_MENU_TYPE_BAR, getString(R.string.user_rule)));

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

    private static final int LEFT_MENU_TYPE_BAR  = 0;
    private static final int LEFT_MENU_TYPE_ITEM = 1;

    class MenuData {
        public MenuData(int type, String subject) {
            this.type = type;
            this.subject = subject;
        }
        int type;
        String subject;
    }

    class MenuViewHolder {
        TextView menu;
    }

    class MenuAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (menuData == null) {
                return 0;
            }

            return menuData.size();
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
            return menuData.get(position).type;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MenuViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(getInflateId(position), null);
                holder = new MenuViewHolder();

                convertView.setTag(holder);
            } else {
                holder = (MenuViewHolder) convertView.getTag();
            }

            MenuData data = menuData.get(position);


            return convertView;
        }

        private int getInflateId(int position) {
            switch (menuData.get(position).type) {
            case LEFT_MENU_TYPE_BAR:
                return R.layout.page_main_menu_bar;
            default:
                return R.layout.page_main_menu_item;
            }
        }
    }
}
