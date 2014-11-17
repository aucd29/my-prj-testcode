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
import android.view.View;
import android.widget.ImageButton;

public class AddFrgmt extends FrgmtBase {
    private static final String TAG = "AddFrgmt";

    private EditGridView grid;
    private ImageButton btn;

    @Override
    protected int getLayoutId() {
        return R.layout.page_add;
    }

    @Override
    protected void initLayout() {
        base.setPadding(0, dpToPixelInt(Cfg.ACTION_BAR_HEIGHT), 0, 0);

        grid = (EditGridView) base.findViewById(R.id.grid);
        btn  = (ImageButton) base.findViewById(R.id.submit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DLog.d(TAG, "===================================================================");
                DLog.d(TAG, "Add Rule");
                DLog.d(TAG, "===================================================================");

                ArrayList<EditGridData> datas = grid.getGridData();
            }
        });
    }
}
