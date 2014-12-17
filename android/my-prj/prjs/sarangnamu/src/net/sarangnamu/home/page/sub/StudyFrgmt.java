/*
 * StudyFrgmt.java
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
import net.sarangnamu.home.api.json.Study;
import net.sarangnamu.home.page.ListApiTaskFrgmt;
import net.sarangnamu.home.page.Navigator;
import net.sarangnamu.home.ui.EndlessScrollListener;
import net.sarangnamu.home.ui.EndlessScrollListener.LoadTaskListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StudyFrgmt extends ListApiTaskFrgmt {
    private static final String TAG = "StudyFrgmt";

    private ArrayList<Study> studies;
    private EndlessScrollListener endlessListener;

    @Override
    protected void initLayout() {
        super.initLayout();

        pageRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTask(1); // refresh
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
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                Study study = studies.get(pos);

                StudyDetailFrgmt frg = (StudyDetailFrgmt) Navigator.getInstance(getActivity()).replace(R.id.content, StudyDetailFrgmt.class);
                frg.setStudyData(study);
            }
        });
    }

    @Override
    protected void loadTask(int page) {
        new ApiTask(new ApiTaskListener() {
            @Override
            public boolean doBackground(int page) {
                try {
                    ArrayList<Study> nt = Api.study(page);

                    if (nt == null) {
                        DLog.e(TAG, "loadStudyData studies null");

                        return false;
                    }

                    if (page == 1) {
                        studies = nt;
                    } else {
                        studies.addAll(nt);
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
                    adapter = new StudyAdapter();
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
        TextView title, date, category;
    }

    class StudyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (studies == null) {
                return 0;
            }

            return studies.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.study_list_item, null);

                holder = new ViewHolder();
                holder.category = (TextView) convertView.findViewById(R.id.category);
                holder.title    = (TextView) convertView.findViewById(R.id.title);
                holder.date     = (TextView) convertView.findViewById(R.id.date);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Study nt = studies.get(position);
            holder.category.setText(nt.category);
            holder.title.setText(nt.title);
            holder.date.setText(nt.date);

            return convertView;
        }
    }
}
