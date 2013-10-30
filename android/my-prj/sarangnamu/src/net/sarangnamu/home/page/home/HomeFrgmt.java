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

import net.sarangnamu.home.page.PageBaseFrgmt;
import android.widget.ListView;


public class HomeFrgmt extends PageBaseFrgmt {
    private ListView list;

    @Override
    protected void initLayout() {
        super.initLayout();

        list = (ListView) view.findViewById(android.R.id.list);
    }
}
