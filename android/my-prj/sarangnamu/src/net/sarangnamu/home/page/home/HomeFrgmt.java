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
package net.sarangnamu.home.page.home;

import java.util.ArrayList;

import net.sarangnamu.common.DLog;
import net.sarangnamu.home.R;
import net.sarangnamu.home.api.Api;
import net.sarangnamu.home.api.json.Notice;
import net.sarangnamu.home.page.PageBaseFrgmt;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class HomeFrgmt extends PageBaseFrgmt {
    private static final String TAG = "HomeFrgmt";

    private ListView list;
    private NoticeAdapter adapter;
    private ArrayList<Notice> notices;
    private ProgressBar progress;

    @Override
    protected void initLayout() {
        super.initLayout();

        list = (ListView) view.findViewById(android.R.id.list);
        list.setOnScrollListener(new EndlessScrollListener());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadNoticeData();
    }

    public void loadNoticeData() {
        showDlgProgress();
        new LoadNoticeTask().execute(0);
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
            holder.content.setText(Html.fromHtml(nt.content));
            holder.date.setText(nt.date);

            return convertView;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // EndlessScrollListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * @see http://benjii.me/2010/08/endless-scrolling-listview-in-android/
     */
    public class EndlessScrollListener implements OnScrollListener {
        private int visibleThreshold = 5;
        private int currentPage = 0;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }

            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                new LoadNoticeTask().execute(currentPage);
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // LoadNoticeTask
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class LoadNoticeTask extends AsyncTask<Integer, Void, Boolean> {
        private int page;

        @Override
        protected void onPreExecute() {
            showIconProgress();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                page = params[0];



                if (page == 0) {
                    notices = Api.notices(page);

                    if (notices == null) {
                        DLog.e(TAG, "loadNoticeData notices null");

                        return false;
                    }
                } else {
                    ArrayList<Notice> nt = Api.notices(page);

                    if (nt == null) {
                        DLog.e(TAG, "loadNoticeData notices null");

                        return false;
                    }

                    notices.addAll(nt);
                }
            } catch (Exception e) {
                DLog.e(TAG, "doInBackground", e);

                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (page == 0) {
                hideDlgProgress();
            }

            hideIconProgress();

            if (!result) {
                Toast.makeText(getActivity(), "Network checking", Toast.LENGTH_SHORT).show();
                return;
            }

            if (page == 0) {
                adapter = new NoticeAdapter();
                list.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }
}
