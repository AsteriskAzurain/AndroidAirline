package com.ishang.asterisk.application05191;

import android.view.View;
import android.widget.Button;

/**
 * Created by Asterisk on 5/21/2020.
 */
public class mybtnListener implements View.OnClickListener {

    private Button mybtn;

    public mybtnListener(Button btn) {
        this.mybtn=btn;
    }

    @Override
    public void onClick(View view) {
        mybtn.setText("text changed");
    }
}
