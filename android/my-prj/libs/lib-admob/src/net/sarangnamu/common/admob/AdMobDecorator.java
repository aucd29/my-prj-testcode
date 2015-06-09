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
import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

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
    private static AdMobDecorator mInst;
    private InterstitialAd mInterstitial;

    public static AdMobDecorator getInstance() {
        if (mInst == null) {
            mInst = new AdMobDecorator();
        }

        return mInst;
    }

    private AdMobDecorator() {

    }

    public void load(Activity act, int id) {
        mAdView = (AdView) act.findViewById(id);
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    public void load(Dialog dlg, int id) {
        mAdView = (AdView) dlg.findViewById(id);
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    public void load(View view, int id) {
        mAdView = (AdView) view.findViewById(id);
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    public void destroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    public void initInterstitial(Context context, String adId, String testDeviceId) {
        mInterstitial = new InterstitialAd(context);
        mInterstitial.setAdUnitId(adId);

        AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        .addTestDevice(testDeviceId).build();

        mInterstitial.loadAd(adRequest);
    }

    public InterstitialAd getInterstitialAd() {
        return mInterstitial;
    }
}
