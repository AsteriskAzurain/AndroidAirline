package com.ishang.asterisk.application05191;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ishang.asterisk.application05191.global.GlobalVariable;
import com.ishang.asterisk.application05191.global.TestFragment;
import com.ishang.asterisk.application05191.global.WelcomeAdapter;
import com.ishang.asterisk.application05191.global.WelcomeFragment;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity{

    private ViewPager viewPager;
    private List<WelcomeFragment> fragments;
    private WelcomeAdapter adapter;
    private LinearLayout dotgroup;
    private List<ImageView> imgarray;

    private boolean isLastPage = false;
    private boolean isDragPage = false;
    private boolean canJumpPage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // TODO 第一次运行程序显示欢迎页 之后不显示
        int id= GlobalVariable.userid;
        if(id>0) toLogin();

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
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            /**
             * 在屏幕滚动过程中不断被调用
             * @param position
             * @param positionOffset  是当前页面滑动比例，如果页面向右翻动，这个值不断变大，最后在趋近1的情况后突变为0。如果页面向左翻动，这个值不断变小，最后变为0
             * @param positionOffsetPixels  是当前页面滑动像素，变化情况和positionOffset一致
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.v("拖动日志",isLastPage+"   "+isDragPage+"   "+positionOffsetPixels);
                if (isLastPage && isDragPage && positionOffsetPixels == 0){   //当前页是最后一页，并且是拖动状态，并且像素偏移量为0
                    if (canJumpPage){
                        canJumpPage = false;
                        toLogin();
                        System.out.println("当前页是最后一页，并且是拖动状态，并且像素偏移量为0");
                    }
                }
            }

            /**
             * 这个方法有一个参数position，代表哪个页面被选中
             * 切换view时，下方圆点的变化。
             * @param position 当前view的位置
             */
            @Override
            public void onPageSelected(int position) {
                isLastPage = position == imgarray.size()-1;
                for(ImageView img : imgarray) img.setBackgroundResource(R.mipmap.page_indicator_unfocused);
                imgarray.get(position).setBackgroundResource(R.mipmap.page_indicator_focused);
            }

            /**
             * 在手指操作屏幕的时候发生变化
             * @param state  有三个值：0（END）  1(PRESS)  2(UP)
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                isDragPage = state == 1;

            }
        });

        dotgroup= (LinearLayout) findViewById(R.id.linear_dotgroup);
        imgarray = new ArrayList<>();
        for (int i=0; i<fragments.size();i++){
            ImageView dotimg = new ImageView(this);
            // LayoutParams 的作用是：子控件告诉父控件，自己要如何布局。
            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(50,50);
            params.setMargins(50, 0, 50, 50); //left top right bottom
            dotimg.setLayoutParams(params);
            dotimg.setBackgroundResource(R.mipmap.page_indicator_unfocused);
            if(i==0) dotimg.setBackgroundResource(R.mipmap.page_indicator_focused);
            imgarray.add(dotimg);
            dotgroup.addView(dotimg);
        }


        Button btnskip = (Button) findViewById(R.id.btnskip);
        btnskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLogin();
            }
        });
    }

    private void toLogin() {
        Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
        startActivity(intent);
    }

}
