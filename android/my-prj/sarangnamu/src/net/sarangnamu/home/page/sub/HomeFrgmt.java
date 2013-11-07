/*
 * HomeFrgmt.java
 * Copyright 2013 Burke.Choi All rights reserved.
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
package net.sarangnamu.home.page.sub;

import java.util.ArrayList;

import net.sarangnamu.common.DLog;
import net.sarangnamu.home.R;
import net.sarangnamu.home.api.Api;
import net.sarangnamu.home.api.json.Notice;
import net.sarangnamu.home.page.ListApiTaskFrgmt;
import net.sarangnamu.home.page.Navigator;
import net.sarangnamu.home.ui.EndlessScrollListener;
import net.sarangnamu.home.ui.EndlessScrollListener.LoadTaskListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class HomeFrgmt extends ListApiTaskFrgmt {
    private static final String TAG = "HomeFrgmt";

    private ArrayList<Notice> notices;
    private EndlessScrollListener endlessListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Api.userId != null && Api.userId.length() > 0) {
            pageWrite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        pageRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });

        pageWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFrgmt(Navigator.HOME_WRITE);
            }
        });

        list = (ListView) view.findViewById(android.R.id.list);
        endlessListener = new EndlessScrollListener();
        endlessListener.setOnLoadTaskListener(new LoadTaskListener() {
            @Override
            public void onLoadTask(int page) {
                loadTask(page);
            }
        });
        list.setOnScrollListener(endlessListener);
    }

    public void reload() {
        loadTask(1);
    }

    @Override
    protected void loadTask(int page) {
        new ApiTask(new ApiTaskListener() {
            @Override
            public boolean doBackground(int page) {
                try {
                    ArrayList<Notice> nt = Api.notices(page);
                    if (nt == null) {
                        DLog.e(TAG, "loadNoticeData notices null");

                        return false;
                    }

                    if (page == 1) {
                        notices = nt;
                    } else {
                        notices.addAll(nt);
                    }
                } catch (Exception e) {
                    DLog.e(TAG, "doInBackground", e);

                    return false;
                }

                return true;
            }

            @Override
            public void onPostExecute(int page) {
                if (page == 1) {
                    adapter = new NoticeAdapter();
                    list.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        }).execute(page);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ADAPTER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        TextView content, date;
    }

    class NoticeAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (notices == null) {
                return 0;
            }

            return notices.size();
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.home_list_item, null);

                holder = new ViewHolder();
                holder.content  = (TextView) convertView.findViewById(R.id.content);
                holder.date     = (TextView) convertView.findViewById(R.id.date);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Notice nt = notices.get(position);
            holder.content.setText(nt.content);
            holder.date.setText(nt.date);

            return convertView;
        }
    }
}
