/*
 * StudyDetailFrgmt.java
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

import net.sarangnamu.home.R;
import net.sarangnamu.home.api.json.Study;
import net.sarangnamu.home.page.PageBaseFrgmt;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StudyDetailFrgmt extends PageBaseFrgmt {
    private static final String TAG = "StudyDetailFrgmt";
    private Study data;

    private TextView title, read, content;

    @Override
    protected void initLayout() {
        super.initLayout();

        title   = (TextView) view.findViewById(R.id.title);
        read    = (TextView) view.findViewById(R.id.read);
        content = (TextView) view.findViewById(R.id.content);

        pageRefresh.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (data != null) {
            title.setText(data.title);
            read.setText(data.read + "");
            content.post(new Runnable() {
                @Override
                public void run() {
                    content.setText(data.content);
                }
            });
        }
    }

    public void setStudyData(Study data) {
        this.data = data;
    }
}
