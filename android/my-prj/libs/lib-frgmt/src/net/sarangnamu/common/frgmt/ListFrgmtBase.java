/*
 * FrgmtBase.java
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
package net.sarangnamu.common.frgmt;

import net.sarangnamu.common.DimTool;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public abstract class ListFrgmtBase extends ListFragment {
    protected View mBaseView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflate(getLayoutId());
        initLayout();

        return mBaseView;
    }

    protected int dpToPixelInt(int dp) {
        return DimTool.dpToPixelInt(getActivity(), dp);
    }

    protected float dpToPixel(float dp) {
        return DimTool.dpToPixel(getActivity(), dp);
    }

    protected View inflate(int id) {
        return LayoutInflater.from(getActivity()).inflate(id, null);
    }

    // //////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    // //////////////////////////////////////////////////////////////////////////////////

    protected abstract int getLayoutId();

    protected abstract void initLayout();
}
