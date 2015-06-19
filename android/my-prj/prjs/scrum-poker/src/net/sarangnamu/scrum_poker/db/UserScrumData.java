/*
 * UserScrumData.java
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
package net.sarangnamu.scrum_poker.db;

import java.util.ArrayList;

import android.text.TextUtils;

public class UserScrumData {
    private String mTitle;
    private ArrayList<String> mContents;
    private long mDate;

    String getTitle() {
        return mTitle;
    }

    String getContents() {
        return TextUtils.join("|", mContents);
    }

    long getDate() {
        return mDate;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setContents(ArrayList<String> contents) {
        this.mContents = contents;
    }
}
