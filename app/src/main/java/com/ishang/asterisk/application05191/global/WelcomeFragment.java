package com.ishang.asterisk.application05191.global;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishang.asterisk.application05191.R;

/**
 * Created by Asterisk on 5/23/2020.
 */

public class WelcomeFragment extends Fragment {

    Context context;
    String showtext;
    TextView textView;

    public WelcomeFragment(String showtext) {
        super();
        this.showtext=showtext;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_welcome,container,false);
        textView=view.findViewById(R.id.textwelcome1);
        textView.setGravity(Gravity.CENTER);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView.setText(showtext);
    }
}
