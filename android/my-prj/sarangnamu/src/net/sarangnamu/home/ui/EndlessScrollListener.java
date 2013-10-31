/*
 * EndlessScrollListener.java
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
package net.sarangnamu.home.ui;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * @see http://benjii.me/2010/08/endless-scrolling-listview-in-android/
 *
 */
public class EndlessScrollListener implements OnScrollListener {
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private LoadTaskListener listener;

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
            if (listener != null) {
                listener.onLoadTask(currentPage + 1);
            }

            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public void setOnLoadTaskListener(LoadTaskListener l) {
        listener = l;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // LoadTaskListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public interface LoadTaskListener {
        public void onLoadTask(int page);
    }
}
