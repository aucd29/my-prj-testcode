/*
 * Menu.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package com.sarangnamu.test_fragments.top;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sarangnamu.test_fragments.MainActivity;
import com.sarangnamu.test_fragments.R;

public class Menu extends Fragment {
    private Button button1, button2;
    private EditText edtTxt;
    private boolean flag = false;
    private boolean flag2 = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_menu, null);

        button1 = (Button)view.findViewById(R.id.button1);
        button2 = (Button)view.findViewById(R.id.button2);
        edtTxt = (EditText)view.findViewById(R.id.editText1);

        view.setBackgroundColor(0xffff0000);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mainName;

                if (flag) {
                    mainName = "default";
                } else {
                    mainName = "sub";
                }

                flag = !flag;
                MainActivity actv = (MainActivity)getActivity();
                actv.changeMain(mainName);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity actv = (MainActivity)getActivity();

                if (flag2) {
                    actv.sendMessageToFragment("sub", "HELLO SUB");
                } else {
                    actv.sendMessageToFragment("default", "HELLO DEFALUT");
                }

                flag2 = !flag2;
            }
        });

        return view;
    }
}
