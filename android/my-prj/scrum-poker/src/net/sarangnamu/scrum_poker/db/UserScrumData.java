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
    private String title;
    private ArrayList<String> contents;
    private long date;

    String getTitle() {
        return title;
    }

    String getContents() {
        return TextUtils.join("|", contents);
    }

    long getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }
}
