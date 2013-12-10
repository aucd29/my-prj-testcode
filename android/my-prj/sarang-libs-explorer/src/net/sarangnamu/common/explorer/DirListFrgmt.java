/*
 * DirListFrgmt.java
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
package net.sarangnamu.common.explorer;

import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.Loader;

import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.FileListFragment;

public class DirListFrgmt extends FileListFragment {
    private static final String TAG = "DirListFrgmt";

    public static DirListFrgmt newInstance(String path) {
        DirListFrgmt fragment = new DirListFrgmt();
        Bundle args = new Bundle();
        args.putString(FileChooserActivity.PATH, path);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Loader<List<File>> onCreateLoader(int id, Bundle args) {
        return new DirLoader(getActivity(), mPath);
    }

    @Override
    public void onLoadFinished(Loader<List<File>> loader, List<File> data) {
        if (data != null && data.size() == 0) {
            new AlertDialog.Builder(getActivity())
            .setMessage(R.string.setCurrentDir)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    ((DirChooserActivity) getActivity()).setPath();
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    getActivity().onBackPressed();
                }
            })
            .show();

            return ;
        }

        mAdapter.setListItems(data);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }
}
