/*
 * EndLessListView.java
 * Copyright 2014 Burke Choi All right reserverd.
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
package net.sarangnamu.common.ui.list.endless;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * <pre>
 * {@code
 * EndLessListView list = (EndLessListView) findViewById(R.id.list);
 * list.setOnEndlessListViewListener(new EndlessListViewListener() {
 *      public void onTaskStart() {
 *
 *      }
 *
 *      public void onTaskBackground(int page) {
 *
 *      }
 *
 *      public void onTaskSetAdapter() {
 *
 *      }
 * });
 * }
 * </pre>
 *
 * @see http://benjii.me/2010/08/endless-scrolling-listview-in-android/
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class EndLessListView extends ListView implements OnScrollListener {
    private int mVisibleThreshold = 5;
    private int mCurrentPage = 0;
    private int mPreviousTotal = 0;
    private boolean mLoading = true;
    private EndlessListViewListener mListener;

    public EndLessListView(Context context) {
        super(context);
        initLayout();
    }

    public EndLessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public EndLessListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout();
    }

    protected void initLayout() {

    }

    private void doLoadTask(final int page) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                if (mListener != null) {
                    mListener.onTaskStart();
                }
            }

            @Override
            protected Boolean doInBackground(Void... argv) {
                if (mListener != null) {
                    mListener.onTaskBackground(page);
                }

                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (page == 1) {
                    if (mListener != null) {
                        mListener.onTaskSetAdapter();
                    }
                } else {
                    ((BaseAdapter) getAdapter()).notifyDataSetChanged();
                }
            }
        }.execute();
    }

    public void setVisibleThreshold(int visibleThreshold) {
        this.mVisibleThreshold = visibleThreshold;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // OnScrollListener;
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mLoading) {
            if (totalItemCount > mPreviousTotal) {
                mLoading = false;
                mPreviousTotal = totalItemCount;
                mCurrentPage++;
            }
        }

        if (!mLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + mVisibleThreshold)) {
            doLoadTask(mCurrentPage + 1);
            mLoading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // EndlessListViewListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public void setOnEndlessListViewListener(EndlessListViewListener l) {
        mListener = l;
    }

    public interface EndlessListViewListener {
        public void onTaskStart();
        public void onTaskBackground(int page);
        public void onTaskSetAdapter();
    }
}
