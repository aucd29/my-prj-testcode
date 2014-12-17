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

import net.sarangnamu.common.FrgmtBase;
import net.sarangnamu.scrum_poker.R;
import net.sarangnamu.scrum_poker.cfg.Cfg;
import android.widget.TextView;

public class CardFrgmt extends FrgmtBase {
    private TextView value;

    @Override
    protected int getLayoutId() {
        return R.layout.page_card;
    }

    @Override
    protected void initLayout() {
        base.setPadding(0, dpToPixelInt(Cfg.ACTION_BAR_HEIGHT), 0, 0);

        value = (TextView) base.findViewById(R.id.value);
        value.setText(getArguments().getString(Cfg.SCRUM_DATA, "ERROR"));
    }
}
