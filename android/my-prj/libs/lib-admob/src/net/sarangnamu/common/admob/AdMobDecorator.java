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

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

/**
 * <pre>
 * {@code
    private AdMobDecorator admob;

    private void initAdView() {
        admob = new AdMobDecorator(this, Cfg.ADMOB_ID);
        admob.load(adLayout);
    }

    @Override
    protected void onDestroy() {
        admob.destroy();

        super.onDestroy();
    }
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class AdMobDecorator {
    private AdView adView;

    public AdMobDecorator(Activity act, String id) {
        adView = new AdView(act, AdSize.BANNER, id);
    }

    public AdMobDecorator(Activity act, AdSize size, String id) {
        adView = new AdView(act, size, id);
    }

    public void load(ViewGroup parent) {
        parent.addView(adView);
        adView.loadAd(new AdRequest());
    }

    public void destroy() {
        adView.destroy();
    }
}
