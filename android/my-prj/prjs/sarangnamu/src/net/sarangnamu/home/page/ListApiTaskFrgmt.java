/*
 * ListApiTaskFrgmt.java
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
package net.sarangnamu.home.page;

import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;

public abstract class ListApiTaskFrgmt extends ApiTaskFrgmt {
    protected int lastPos;
    protected ListView list;
    protected BaseAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (adapter == null) {
            showDlgProgress();
            loadTask(1);
        } else {
            list.setAdapter(adapter);
            list.scrollTo(0, lastPos);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (list != null) {
            lastPos = list.getScrollY();
        }
    }
}
