package com.ishang.asterisk.application05191.global;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ishang.asterisk.application05191.R;

public class EmptyActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        for(int i=0; i<20;i++){
            Button btn = new Button(this);
            btn.setId(i);
            btn.setText("btn"+i);
            btn.setOnClickListener(new mybtnListener(btn));
            linearLayout.addView(btn);

        }
    }
}
