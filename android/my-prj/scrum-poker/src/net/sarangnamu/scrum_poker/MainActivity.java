package net.sarangnamu.scrum_poker;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private GridView grid;
    private ListView leftMenu;
    private DrawerLayout drawer;
    private ArrayList<String> defaultValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid = (GridView) findViewById(R.id.grid);
        leftMenu = (ListView) findViewById(R.id.leftMenu);
        drawer = (DrawerLayout) findViewById(R.id.drawer);

        initDefaultValue();
        initDrawer();
        initActionBar();
        initAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void initDefaultValue() {
        if (defaultValue == null) {
            defaultValue = new ArrayList<String>();
        }

        defaultValue.add("0");
        defaultValue.add("1/2");
        defaultValue.add("2");
        defaultValue.add("3");
        defaultValue.add("5");
        defaultValue.add("8");
        defaultValue.add("13");
        defaultValue.add("20");
        defaultValue.add("40");
        defaultValue.add("50");
        defaultValue.add("100");
    }

    private void initActionBar() {

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
    }

    private void initAdapter() {
        grid.setAdapter(new ScrumAdapter());
        grid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        leftMenu.setAdapter(new MenuAdapter());
        leftMenu.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            }
        });
    }

    // //////////////////////////////////////////////////////////////////////////////////
    //
    // SCRUM ADAPTER
    //
    // //////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        TextView number;
    }

    class ScrumAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (defaultValue == null) {
                return 0;
            }

            return defaultValue.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.scrum_item, null);

                holder = new ViewHolder();
                holder.number = (TextView) convertView.findViewById(R.id.number);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.number.setText(defaultValue.get(position));

            return convertView;
        }
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
