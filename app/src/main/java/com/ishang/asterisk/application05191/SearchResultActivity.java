package com.ishang.asterisk.application05191;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private TextView texttotal=null;
    private int num=0;
    private String cabintype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent=getIntent();
        String reqstr = intent.getStringExtra("reqstr");
        System.out.println(reqstr);
        cabintype=intent.getStringExtra("cabintype");

        //layout = new LinearLayout(this);
        //layout.setOrientation(LinearLayout.VERTICAL);
        layout = (LinearLayout) findViewById(R.id.linearlayoutdetail) ;
        //setContentView(layout);

        texttotal=(TextView)findViewById(R.id.texttotal);
        getdata(reqstr);

//        createcontrols();
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
                    /*
                    * I/System.out: [
                    * {"Id":"1","AirlineName":"Air China","FlightNumber":"CA101","Price":"1200","DepartureTime":"2018-06-15 08:00","ArriveTime":"2018-06-15 12:00","Aircraft":"Boeing 737-800","AvailableTickets":"120","From":"PEK","To":"PVG"},
                    * {"Id":"2","AirlineName":"Air China","FlightNumber":"CA120","Price":"1800","DepartureTime":"2018-06-15 05:00","ArriveTime":"2018-06-15 13:00","Aircraft":"Boeing 737-800","AvailableTickets":"120","From":"PEK","To":"LAX"},
                    * {"Id":"3","AirlineName":"American Airlines","FlightNumber":"AA180","Price":"1800","DepartureTime":"2018-06-15 16:00","ArriveTime":"2018-06-15 23:55","Aircraft":"Airbus 319","AvailableTickets":"90","From":"LAX","To":"PEK"},
                    * {"Id":"4","AirlineName":"Air China","FlightNumber":"CA136","Price":"2000","DepartureTime":"2018-06-15 08:00","ArriveTime":"2018-06-15 18:00","Aircraft":"Airbus 319","AvailableTickets":"90","From":"PEK","To":"LAX"},
                    * {"Id":"5",
                    * "AirlineName":"Air China",
                    * "FlightNumber":"CA186",
                    * "Price":"1900",
                    * "DepartureTime":"2018-06-15 10:00",
                    * "ArriveTime":"2018-06-15 22:10",
                    * "Aircraft":"Airbus 319",
                    * "AvailableTickets":"90",
                    * "From":"PEK",
                    * "To":"LAX"}]
                    * */
                    if(rescode==200){

                        try{
                            Looper.prepare();

                            JSONArray array = new JSONArray(content);
                            num= array.length();
//                            TextView texttotal=(TextView)findViewById(R.id.texttotal);
//                            texttotal.setText(num);

                            for(int i=0; i<num; i++){
                                JSONObject obj = array.getJSONObject(i);
                                View view = View.inflate(getApplicationContext(), R.layout.flight_detail, null);

                                int id = obj.getInt("Id");

                                String airline = obj.getString("AirlineName");
                                TextView textairline=(TextView)view.findViewById(R.id.textairline);
                                textairline.setText(airline);

                                String avltkt=obj.getString("AvailableTickets");
                                TextView textavltkt=(TextView)view.findViewById(R.id.texttickets);
                                textavltkt.setText(avltkt);

                                String fltnum = obj.getString("FlightNumber");
                                TextView textfltnum=(TextView)view.findViewById(R.id.textfltNum);
                                textfltnum.setText(fltnum);

                                String price = obj.getString("Price");
                                TextView textprice=(TextView)view.findViewById(R.id.textprice);
                                textprice.setText(price);

                                String aircraft = obj.getString("Aircraft");
                                TextView textaircraft=(TextView)view.findViewById(R.id.textcraft);
                                textaircraft.setText(aircraft);

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Date depdate = sdf.parse(obj.getString("DepartureTime"));
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
