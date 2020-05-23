package com.ishang.asterisk.application05191;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ishang.asterisk.application05191.global.TestFragment;
import com.ishang.asterisk.application05191.global.WelcomeAdapter;
import com.ishang.asterisk.application05191.global.WelcomeFragment;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    ViewPager viewPager;
    List<WelcomeFragment> fragments;
    WelcomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        /*
        * 1. 在fragment布局文件中创建对应的控件
        * 2. 创建对应的fragment类
        * 3. 创建适配器
        * */
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        fragments= new ArrayList<>();
        fragments.add(new WelcomeFragment("Sunshine Airline\n Management System\n\n V1.0"));
        fragments.add(new WelcomeFragment("You can search flights and book flights in this app."));
        fragments.add(new WelcomeFragment("Our App will give you a good experience."));
        adapter= new WelcomeAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
    }
}
