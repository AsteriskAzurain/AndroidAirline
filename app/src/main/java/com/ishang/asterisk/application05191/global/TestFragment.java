package com.ishang.asterisk.application05191.global;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishang.asterisk.application05191.R;

/**
 * Created by Asterisk on 5/23/2020.
 */

public class TestFragment extends Fragment {

    Context mContext;
    ImageView imageView;
    TextView textView;
    String text;

    public TestFragment(String text) {
        super();
        this.text=text;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 用于将布局文件转化为view
        View view = inflater.inflate(R.layout.frag_welcome,container,false);
        textView=view.findViewById(R.id.textwelcome1);
//        textView= new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
//        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
//        imageView = new ImageView(mContext);
//        imageView.setImageResource(R.drawable.logo);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textView.setText(text);
        System.out.println("QAQ");
    }
}
