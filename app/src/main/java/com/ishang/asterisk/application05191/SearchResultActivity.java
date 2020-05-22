package com.ishang.asterisk.application05191;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchResultActivity extends AppCompatActivity {

    private LinearLayout layout=null;
    private int num=0;
    private String cabintype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        final Intent intent=getIntent();
        String reqstr = intent.getStringExtra("reqstr");
        System.out.println(reqstr);
        cabintype=intent.getStringExtra("cabintype");

        //layout = new LinearLayout(this);
        //layout.setOrientation(LinearLayout.VERTICAL);
        layout = (LinearLayout) findViewById(R.id.linearlayoutdetail) ;
        //setContentView(layout);

        getdata(reqstr);

        Button btnback = (Button)findViewById(R.id.btnback_rst);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(SearchResultActivity.this,SearchFlightActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void getdata(final String reqstr) {
        new Thread(){

            @Override
            public void run() {
                try{
                    String path = "http://10.0.2.2:5000"+reqstr;
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int rescode=conn.getResponseCode();
                    InputStream is = conn.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    int len =-1;
                    byte[] buffer = new byte[1024]; // 用于分批(1kb)读入
                    while ((len=is.read(buffer))!=-1){
                        baos.write(buffer,0,len);
                    }
                    is.close();
                    String content= new String(baos.toByteArray());
                    System.out.println(content);

                    if(rescode==200){

                        try{
                            Looper.prepare();

                            JSONArray array = new JSONArray(content);
                            num= array.length();
                            TextView texttotal=(TextView)findViewById(R.id.texttotal);
                            texttotal.setText("Total "+num+" flights");


                            for(int i=0; i<num; i++){
                                final JSONObject obj = array.getJSONObject(i);
                                final View view = View.inflate(getApplicationContext(), R.layout.flight_detail, null);

                                String airline = obj.getString("AirlineName");
                                TextView textairline=(TextView)view.findViewById(R.id.textairline);
                                textairline.setText(airline);

                                String avltkt=obj.getString("AvailableTickets");
                                TextView textavltkt=(TextView)view.findViewById(R.id.texttickets);
                                textavltkt.setText(avltkt+" avaliable tickets");

                                final String fltnum = obj.getString("FlightNumber");
                                TextView textfltnum=(TextView)view.findViewById(R.id.textfltNum);
                                textfltnum.setText(fltnum);

                                String price = obj.getString("Price");
                                TextView textprice=(TextView)view.findViewById(R.id.textprice);
                                textprice.setText("$"+price);

                                final String aircraft = obj.getString("Aircraft");
                                TextView textaircraft=(TextView)view.findViewById(R.id.textcraft);
                                textaircraft.setText(aircraft);

                                final String depstr=obj.getString("DepartureTime");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Date depdate = sdf.parse(depstr);
                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
                                String date =sdf2.format(depdate);
                                String time = sdf3.format(depdate);
                                TextView textdate=(TextView)view.findViewById(R.id.textdate);
                                textdate.setText(date);
                                TextView texttime=(TextView)view.findViewById(R.id.texttime);
                                texttime.setText(time);

                                TextView textcabin=(TextView)view.findViewById(R.id.textcabin);
                                textcabin.setText(cabintype);

                                layout.addView(view);

                                final int id = obj.getInt("Id");
                                if(cabintype.equals("First")){
                                    // TODO: 5/22/2020  不能在线程上操控控件的解决方法
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            view.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View view) {
                                                    Intent seatpage = new Intent(SearchResultActivity.this, SelectSeatActivity.class);
                                                    seatpage.putExtra("fltid", id);
                                                    System.out.println(""+id);
                                                    seatpage.putExtra("deptime",depstr);
                                                    seatpage.putExtra("fltnum",fltnum);
                                                    seatpage.putExtra("aircraft",aircraft);
                                                    startActivity(seatpage);
                                                }
                                            });
                                        }
                                    });
                                }
                            }

                            Looper.loop();
                        }catch (Exception e){
                            System.out.println("报错了2");
                            System.out.println(e.toString());
                        }
                    }
                }catch (Exception e){
                    System.out.println("报错了1");
                    System.out.println(e.toString());
                }
            }
        }.start();
    }


}
