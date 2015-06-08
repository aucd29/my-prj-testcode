/*
 * AdMobDecorator.java
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
package net.sarangnamu.common.admob;

import android.app.Activity;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * <pre>
 * </pre>
 * @see manual :  https://developers.google.com/admob/android/eclipse
 * @see Where is ad unit id : apps.admob.com -> Monetize -> app list -> Ad Unit ID
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class AdMobDecorator {
    private AdView mAdView;

    public AdMobDecorator(Activity act, int id, String adUnitId) {
        mAdView = (AdView) act.findViewById(id);
        mAdView.setAdUnitId(adUnitId);
    }

    public AdMobDecorator(Activity act, String adUnitId) {
        mAdView = new AdView(act);
        mAdView.setAdUnitId(adUnitId);
    }

    public void load() {
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    public void load(ViewGroup vg) {
        mAdView.loadAd(new AdRequest.Builder().build());
        vg.addView(mAdView);
    }

    public void destroy() {
        mAdView.destroy();
    }
}
