package com.ishang.asterisk.application05191.global;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ishang.asterisk.application05191.R;

/**
 * Created by Asterisk on 5/21/2020.
 */

public class FlightDetail extends RelativeLayout {

    private static final String TAG = FlightDetail.class.getSimpleName();

    private final String mNameSpace = "http://schemas.android.com/apk/res/global.flightdetail";

    private TextView dtext_airline=null;
    private TextView dtext_ticket=null;
    private TextView dtext_fltnum =null;
    private TextView dtext_price =null;
    private TextView dtext_cabin =null;
    private TextView dtext_aircraft =null;
    private TextView dtext_time=null;

    public FlightDetail(Context context) {
        super(context);
    }

    public FlightDetail(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlightDetail(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //将打气筒根据自定义控件的布局文件，创建的view 对象挂载到当前类上面，然后显示
        View view = (View) View.inflate(context, R.layout.activity_flight_detail, this);
        dtext_airline = (TextView) findViewById(R.id.textairline);
        dtext_ticket = (TextView) findViewById(R.id.texttickets);
        dtext_fltnum = (TextView) findViewById(R.id.textfltNum);
        dtext_price = (TextView) findViewById(R.id.textprice);
        dtext_cabin = (TextView) findViewById(R.id.textcabin);
        dtext_aircraft = (TextView) findViewById(R.id.textcraft);
        dtext_time = (TextView) findViewById(R.id.texttime);

        //初始化相关自定义属性
        initStyle(attrs);
    }

    private void initStyle(AttributeSet attrs) {
        dtext_airline.setText(attrs.getAttributeValue(mNameSpace,"airline"));
    }

    public FlightDetail(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
