package com.ishang.asterisk.application05191.global;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ishang.asterisk.application05191.R;

import java.util.ArrayList;
import java.util.List;

public class EmptyActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private LinearLayout welcometest;
    ViewPager viewPagertest;
    List<TestFragment> fragments;
    TestviewpagerAdapter adapter;

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

//        GridLayout gridseat=(GridLayout)findViewById(R.id.gridseat_test);
//        for(int i=0;i<6;i++){
//
//        }

        welcometest=(LinearLayout)findViewById(R.id.linear_forviewpager);
        viewPagertest=(ViewPager)findViewById(R.id.viewpager_test);
        fragments= new ArrayList<>();
        fragments.add(new TestFragment("Sunshine Airline\n Management System\n\n V1.0"));
        fragments.add(new TestFragment("You can search flights and book flights in this app."));
        fragments.add(new TestFragment("Our App will give you a good experience."));
        adapter= new TestviewpagerAdapter(getSupportFragmentManager(),fragments);
        viewPagertest.setAdapter(adapter);
        TextView testtext = new TextView(this);
        testtext.setText("Hello");
        welcometest.addView(testtext);
    }
}
