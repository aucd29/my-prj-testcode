/*
 * EndLessListView.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
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
 *      public void onTaskEnd() {
 *
 *      }
 * });
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class EndLessListView extends ListView implements OnScrollListener {
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private EndlessListViewListener listener;

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
        new AsyncTask<Context, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                if (listener != null) {
                    listener.onTaskStart();
                }
            }

            @Override
            protected Boolean doInBackground(Context... contexts) {
                if (listener != null) {
                    listener.onTaskBackground(page);
                }

                return false;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (listener != null) {
                    listener.onTaskEnd();
                }

                if (page != 1) {
                    ((BaseAdapter) getAdapter()).notifyDataSetChanged();
                }
            }
        }.execute(getContext());
    }

    public void setVisibleThreshold(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // OnScrollListener;
    //
    ////////////////////////////////////////////////////////////////////////////////////

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
            doLoadTask(currentPage + 1);
            loading = true;
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
        listener = l;
    }

    public interface EndlessListViewListener {
        public void onTaskStart();
        public void onTaskBackground(int page);
        public void onTaskEnd();
    }
}
