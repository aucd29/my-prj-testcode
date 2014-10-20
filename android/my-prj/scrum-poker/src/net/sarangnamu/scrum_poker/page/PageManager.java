/*
 * Navigator.java
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
package net.sarangnamu.scrum_poker.page;

import net.sarangnamu.common.frgmt.FrgmtManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class PageManager extends FrgmtManager {
    private static PageManager inst;

    public static PageManager getInstance(FragmentActivity act) {
        if (inst == null) {
            inst = new PageManager();
        }

        inst.setFragmentManager(act);

        return inst;
    }

    private PageManager() {

    }

    @Override
    protected void setTransition(FragmentTransaction trans) {
        setUpTransition(trans);
    }
}
