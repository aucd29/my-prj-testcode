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
    private static final String TAG = "InflateByClassNameBase";

    private static final String PREFIX_MENU = "mnu_";
    private static final String PREFIX_PAGE = "page_";
    private static final String SUFFIX_FRAGMENT = "Frgmt";

    private static final String IDENTIFIER_STRING = "string";
    private static final String IDENTIFIER_LAYOUT = "layout";

    protected View view;
    protected ViewGroup pageContent; // /< child view
    protected String parseClassName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        base = (ViewGroup) inflate(getLayoutId());

        autoInflate();
        initLayout();

        return base;
    }

    private void autoInflate() {
        int id = getLayoutIdentifier();
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "inflate id : " + id);
        DLog.d(TAG, "===================================================================");

        // base <- pageContent <- other View (class_name.xml)
        pageContent = (ViewGroup) base.findViewById(getPageContentId());

        if (id != 0) {
            view = inflate(id);
            pageContent.addView(view);
        } else {
            DLog.e(TAG, "initLayout, not found layout id for pageContent");
        }
    }

    private String getClassSimpleName() {
        if (parseClassName != null && parseClassName.length() > 0) {
            return parseClassName;
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

        parseClassName = sb.toString();

        return parseClassName;
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
