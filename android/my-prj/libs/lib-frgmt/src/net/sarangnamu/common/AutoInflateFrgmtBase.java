/*
 * AutoInflateFrgmtBase.java
 * Copyright 2014 Burke.Choi All rights reserved.
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
package net.sarangnamu.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AutoInflateFrgmtBase extends FrgmtBase {
    private static final String TAG = "AutoInflateFrgmtBase";

    private static final String PREFIX_MENU = "mnu_";
    private static final String PREFIX_PAGE = "page_";
    private static final String SUFFIX_FRAGMENT = "Frgmt";

    private static final String IDENTIFIER_STRING = "string";
    private static final String IDENTIFIER_LAYOUT = "layout";

    protected View mView;
    protected ViewGroup mPageContent; // /< child view
    protected String mParseClassName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = (ViewGroup) inflate(getLayoutId());

        autoInflate();
        initLayout();

        return mBaseView;
    }

    private void autoInflate() {
        int id = getLayoutIdentifier();
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "inflate id : " + id);
        DLog.d(TAG, "===================================================================");

        // base <- pageContent <- other View (class_name.xml)
        mPageContent = (ViewGroup) mBaseView.findViewById(getPageContentId());

        if (id != 0) {
            mView = inflate(id);
            mPageContent.addView(mView);
        } else {
            DLog.e(TAG, "initLayout, not found layout id for pageContent");
        }
    }

    private String getClassSimpleName() {
        if (mParseClassName != null && mParseClassName.length() > 0) {
            return mParseClassName;
        }

        StringBuilder sb = new StringBuilder();
        String tmpName = getClass().getSimpleName().replace(getSuffixForFragment(), "");
        sb.append(Character.toLowerCase(tmpName.charAt(0)));
        for (int i = 1; i < tmpName.length(); ++i) {
            char c = tmpName.charAt(i);

            if (Character.isUpperCase(c)) {
                sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }

        mParseClassName = sb.toString();

        return mParseClassName;
    }

    /**
     * for title name
     *
     * @param name
     * @return
     */
    protected int getStringTitleIdentifier() {
        return getResources().getIdentifier(getPrefixForMenu() + getClassSimpleName(),
                IDENTIFIER_STRING, getActivity().getPackageName());
    }

    /**
     * for layout name
     *
     * @param name
     * @return
     */
    protected int getLayoutIdentifier() {
        String layoutFileName = getPrefixForPage() + getClassSimpleName();
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "layoutName " + layoutFileName);
        DLog.d(TAG, "===================================================================");
        return getResources().getIdentifier(layoutFileName, IDENTIFIER_LAYOUT,
                getActivity().getPackageName());
    }

    protected String getPrefixForMenu() {
        return PREFIX_MENU;
    }

    protected String getPrefixForPage() {
        return PREFIX_PAGE;
    }

    protected String getSuffixForFragment() {
        return SUFFIX_FRAGMENT;
    }

    // //////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    // //////////////////////////////////////////////////////////////////////////////////

    protected abstract int getPageContentId();
}
