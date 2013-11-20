/**
 * EmsDataManager.java
 * Copyright 2013 Burke Choi All rights reserved.
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
package net.sarangnamu.ems_tracking;

import java.util.HashMap;

import net.sarangnamu.common.DLog;
import net.sarangnamu.ems_tracking.api.Api;
import net.sarangnamu.ems_tracking.api.xml.Ems;
import net.sarangnamu.ems_tracking.db.EmsDbHelper;
import android.os.AsyncTask;

public class EmsDataManager {
    private static final String TAG = "EmsDataManager";
    public static final String EMS_NUM = "emsNum";

    private static EmsDataManager inst;
    private HashMap<String, Ems> emsData;

    public static EmsDataManager getInstance() {
        if (inst == null) {
            inst = new EmsDataManager();
        }

        return inst;
    }

    private EmsDataManager() {

    }

    public interface EmsDataListener {
        public void onEmsData(Ems ems);
    }

    public void getAsyncEmsData(final MainActivity act, final String num, final EmsDataListener l) {
        if (emsData == null) {
            emsData = new HashMap<String, Ems>();
        }

        Ems ems = emsData.get(num);
        if (ems == null) {
            new AsyncTask<Void, Void, Boolean>() {
                Ems ems;

                @Override
                protected void onPreExecute() {
                    act.showProgress();
                }

                @Override
                protected Boolean doInBackground(Void... contexts) {
                    try {
                        ems = Api.tracking(num);
                        setEmsData(num, ems);

                        EmsDbHelper.update(0, ems);
                    } catch (Exception e) {
                        DLog.e(TAG, "initData", e);

                        return false;
                    }

                    return true;
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    act.hideProgress();

                    if (l != null) {
                        l.onEmsData(ems);
                    }
                }
            }.execute();
        }

        if (l != null) {
            l.onEmsData(ems);
        }
    }

    public Ems getEmsData(String ems) {
        try {
            return emsData.get(ems);
        } catch (NullPointerException e) {
            DLog.e(TAG, "getEmsData", e);
        } catch (Exception e) {
            DLog.e(TAG, "getEmsData", e);
        }

        return null;
    }

    public void setEmsData(String num, Ems ems) {
        if (emsData == null) {
            emsData = new HashMap<String, Ems>();
        }

        emsData.put(num, ems);
    }
}
