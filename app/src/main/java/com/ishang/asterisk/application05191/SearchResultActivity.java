package com.ishang.asterisk.application05191;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent=getIntent();
        String reqstr = intent.getStringExtra("reqstr");
        System.out.println(reqstr);

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
                            for(int i=0; i<array.length(); i++){
                                JSONObject obj = array.getJSONObject(i);
                                int id = obj.getInt("Id");
                                /* total num tickets leave
                                 cabintype   */
                                String fltnum = obj.getString("FlightNumber");
                                int price = obj.getInt("Price");
                                String aircraft = obj.getString("Aircraft");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Date depdate = sdf.parse(obj.getString("DepartureTime"));
                                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                                String time = sdf2.format(depdate);
                                System.out.println(time);
                                System.out.println("flight detail :"
                                        + "\nfltnum:" + fltnum
                                        + "\nprices:" + price
                                        + "\naircraft:" + aircraft
                                        + "\ntime:" + time
                                );
                                /*
                                I/System.out: flight detail :
                                I/System.out: fltnum:CA101
                                I/System.out: prices:1200
                                I/System.out: aircraft:Boeing 737-800
                                I/System.out: time:08:00
                                * */


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

    private void createcontrols() {
    }
}
