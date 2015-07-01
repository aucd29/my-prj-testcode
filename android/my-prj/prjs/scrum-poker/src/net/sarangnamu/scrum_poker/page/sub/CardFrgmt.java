/*
 * CardFrgmt.java
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
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.TextView;

public class CardFrgmt extends FrgmtBase {
    private TextView mNumber;

    @Override
    protected int getLayoutId() {
        return R.layout.page_card;
    }

    @Override
    protected void initLayout() {
        mNumber = (TextView) mBaseView.findViewById(R.id.value);

        mNumber.setText("TAP");
        mNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animator ani = ObjectAnimator.ofFloat(mNumber, "alpha", 0).setDuration(500);
                ani.addListener(new AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) { }
                    @Override
                    public void onAnimationRepeat(Animator animation) { }
                    @Override
                    public void onAnimationCancel(Animator animation) { }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animation.removeListener(this);
                        mNumber.setText(getArguments().getString(Cfg.SCRUM_DATA, "ERROR"));

                        Animator ani = ObjectAnimator.ofFloat(mNumber, "alpha", 1).setDuration(500);
                        ani.addListener(new AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) { }
                            @Override
                            public void onAnimationRepeat(Animator animation) { }
                            @Override
                            public void onAnimationCancel(Animator animation) { }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mNumber.setClickable(false);
                            }
                        });
                        ani.start();
                    }
                });
                ani.start();
            }
        });
    }
}
