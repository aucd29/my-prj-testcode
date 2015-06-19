/*
 * MainFrgmt.java
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
package net.sarangnamu.scrum_poker.page.sub;

import java.util.ArrayList;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.FrgmtBase;
import net.sarangnamu.common.ui.grid.edit.EditGridData;
import net.sarangnamu.common.ui.grid.edit.EditGridView;
import net.sarangnamu.scrum_poker.R;
import net.sarangnamu.scrum_poker.cfg.Cfg;
import net.sarangnamu.scrum_poker.db.DbHelper;
import net.sarangnamu.scrum_poker.db.UserScrumData;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddFrgmt extends FrgmtBase {
    private static final String TAG = "AddFrgmt";

    private EditText mTitle;
    private ImageButton mSubmit;
    private EditGridView mGrid;

    @Override
    protected int getLayoutId() {
        return R.layout.page_add;
    }

    @Override
    protected void initLayout() {
    	mBaseView.setPadding(0, dpToPixelInt(Cfg.ACTION_BAR_HEIGHT), 0, 0);

        mTitle    = (EditText) mBaseView.findViewById(R.id.edtTitle);
        mSubmit      = (ImageButton) mBaseView.findViewById(R.id.submit);
        mGrid        = (EditGridView) mBaseView.findViewById(R.id.grid);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DLog.d(TAG, "===================================================================");
                DLog.d(TAG, "Add Rule");
                DLog.d(TAG, "===================================================================");

                if (getString(R.string.license).equals(mTitle.getText()) ||
                    getString(R.string.add_rule).equals(mTitle.getText())) {
                    String msg = String.format(getActivity().getString(R.string.doNotUseThisWord), mTitle.getText());
                    showDlgTimer(msg);
                    return ;
                }

                ArrayList<EditGridData> datas = mGrid.getGridData();
                if (datas == null) {
                    showDlgTimer(R.string.invalidData);
                    DLog.e(TAG, "onClick <ArrayList<EditGridData> is null>");
                    return ;
                }

                ArrayList<String> contents = new ArrayList<String>();
                for (EditGridData data : datas) {
                    contents.add(data.value);
                }

                UserScrumData scrumData = new UserScrumData();
                scrumData.setTitle(mTitle.getText().toString());
                scrumData.setContents(contents);

                if (!DbHelper.insert(scrumData)) {
                    Toast.makeText(getActivity(), R.string.errInsert, Toast.LENGTH_SHORT).show();
                    return ;
                }

                mTitle.setText("");
                mGrid.reset();

                showDlgTimer(R.string.insertComplete);
            }
        });
    }

    private void showDlgTimer(int resid) {
        showDlgTimer(getActivity().getString(resid));
    }

    private void showDlgTimer(String msg) {
        /*DlgTimer dlg = new DlgTimer(getActivity(), R.layout.);
        dlg.setMessage(msg);
        dlg.setTime(1500);
        dlg.show();
        dlg.setTransparentBaseLayout();*/
    }
}
