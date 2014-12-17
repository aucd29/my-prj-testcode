/*
 * PageBaseFrgmt.java
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

import android.os.AsyncTask;
import android.widget.Toast;

public abstract class ApiTaskFrgmt extends PageBaseFrgmt {
    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ApiTask
    //
    ////////////////////////////////////////////////////////////////////////////////////

    protected class ApiTask extends AsyncTask<Integer, Void, Boolean> {
        protected int page;
        private ApiTaskListener listener;

        public ApiTask(ApiTaskListener l) {
            listener = l;
        }

        @Override
        protected void onPreExecute() {
            showIconProgress();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            page = params[0];

            if (listener != null) {
                return listener.doBackground(page);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (page == 1) {
                hideDlgProgress();
            }

            hideIconProgress();

            if (!result) {
                Toast.makeText(getActivity(), "Loading fail", Toast.LENGTH_SHORT).show();
                return ;
            }

            if (listener != null) {
                listener.onPostExecute(page);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ApiTaskListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public interface ApiTaskListener {
        public boolean doBackground(int page);
        public void onPostExecute(int page);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    protected abstract void loadTask(int page);
}
