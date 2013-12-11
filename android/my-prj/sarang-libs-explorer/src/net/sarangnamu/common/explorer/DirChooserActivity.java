/*
 * DirChooserActivity.java
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

import net.sarangnamu.common.DimTool;
import net.sarangnamu.common.fonts.FontLoader;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.FileListFragment;

public class DirChooserActivity extends FileChooserActivity {
    private LinearLayout layout;
    private Button createDir, setPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createDir = (Button) findViewById(R.id.createDir);
        setPath   = (Button) findViewById(R.id.setPath);
        layout    = (LinearLayout) findViewById(R.id.layout);

        FontLoader.getInstance(this).applyChild("Roboto-Light", layout, Button.class);

        createDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        setPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPath(null);
            }
        });
    }

    @Override
    protected FileListFragment instListFragment() {
        return DirListFrgmt.newInstance(mPath);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.dir_chooser;
    }

    public void setPath(final DialogInterface.OnClickListener cancelListener) {
        TextView view = new TextView(DirChooserActivity.this);
        view.setText(R.string.setCurrentDir);
        view.setPadding(0, DimTool.dpToPixelInt(DirChooserActivity.this, 15), 0, 0);
        view.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        view.setTypeface(FontLoader.getInstance(DirChooserActivity.this).getFont("Roboto-Light"));

        new AlertDialog.Builder(DirChooserActivity.this)
        .setView(view)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent intent = new Intent();
                intent.putExtra("path", mPath);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        })
        .setNegativeButton(android.R.string.no, cancelListener)
        .show();
    }
}
