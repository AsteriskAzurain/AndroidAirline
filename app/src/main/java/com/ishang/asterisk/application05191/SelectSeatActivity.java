package com.ishang.asterisk.application05191;

import android.content.Intent;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ishang.asterisk.application05191.global.GlobalVariable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SelectSeatActivity extends AppCompatActivity {

    private ImageView imageView1A,imageView1C,imageView1J,imageView1L,imageView2A,imageView2C,imageView2J,imageView2L,imageView3A,imageView3C,imageView3J,imageView3L;
    private List<ImageView> seatimgarray= new ArrayList<>();
    int fltid;  String aircraft;
    TextView t1, t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat);

        Intent intent = getIntent();
        fltid = intent.getIntExtra("fltid",1);
        String depstr=intent.getStringExtra("deptime");
        String fltnum = intent.getStringExtra("fltnum");
        aircraft= intent.getStringExtra("aircraft");

        TextView textdep = (TextView)findViewById(R.id.textdeptime1);
        textdep.setText(depstr);
        TextView textfltnum = (TextView)findViewById(R.id.textfltnum1);
        textfltnum.setText(fltnum);
        TextView textaircraft = (TextView)findViewById(R.id.textaircraft1);
        textaircraft.setText(aircraft);

        TextView t1=(TextView)findViewById(R.id.r3c0);
        TextView t2=(TextView)findViewById(R.id.r3c6);
        if(aircraft.equals("Airbus 319")){
            t1.setText("3");
            t2.setText("3");
        }else {
            t1.setText("");
            t2.setText("");
        }

        getseatinfo();

        /*try {
            test();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/

    }

    private void removeimg(String aircraft) {
        if(aircraft.equals("Boeing 737-800")){
            //imageView.setImageDrawable(null)
            int[] rms={R.id.s3A,R.id.s3C,R.id.s3J,R.id.s3L};
            for(int sid:rms){
                ImageView rmimg = (ImageView)findViewById(sid);
                rmimg.setImageDrawable(null);
            }
            TextView tv= (TextView)findViewById(R.id.r3c0);
            tv.setText("");
            tv=(TextView)findViewById(R.id.r3c6);
            tv.setText("");
        }
    }

    private void getseatinfo() {

        new Thread(){

            @Override
            public void run() {
                try{
                    String path="http://10.0.2.2:5000/api/order?FlightId="+fltid+"&CabinType=first";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int rescode = conn.getResponseCode();
                    InputStream is = conn.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    int len=-1;
                    byte[] buffer = new byte[1024];
                    while ((len=is.read(buffer))!=-1){
                        baos.write(buffer,0,len);
                    }
                    String content = new String (baos.toByteArray());
                    System.out.println(content);

                    Looper.prepare();;
                    JSONArray array = new JSONArray(content);
                    for (int i=0; i<array.length();i++){
                        final JSONObject obj = array.getJSONObject(i);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getimgview(obj);
                                } catch (JSONException e) {
                                    System.out.println("报错了2");
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    Looper.loop();

                }catch (Exception e){
                    System.out.println("报错了1");
                    System.out.println(e.toString());
                }
            }
        }.start();

    }

    public void getimgview(JSONObject obj) throws JSONException {
        int nowuser = GlobalVariable.getUserid();
        int userid= obj.getInt("UserId");
        String col = obj.getString("ColumnName");
        int row= obj.getInt("RowNumber");
        String[] cols={"A","C","J","L"};
        Boolean hascol= Arrays.asList(cols).contains(col);
        if( row<=3 && hascol ){
            String objidstr="s"+row+col;
            int objid=getResources().getIdentifier(objidstr,"id",getPackageName());
            final ImageView objimg = (ImageView)findViewById(objid);
            objimg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.chair_occupied));
            if(userid==nowuser) {
                objimg.setTag(R.id.tag_iv,"chosen");
                objimg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.chair_yourchosen));
                objimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        objimg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.chair_available));
                    }
                });
            }else{
                objimg.setTag(R.id.tag_iv,"occupied");
                objimg.setClickable(false);
            }
            System.out.println("after get img view: "+objimg.getTag(R.id.tag_iv));
        }
        afterloadgrid();
    }

    private void afterloadgrid() {
        imageView1A=(ImageView)findViewById(R.id.s1A); imageView1A.setTag(R.id.tag_row,"1"); imageView1A.setTag(R.id.tag_col,"A"); seatimgarray.add(imageView1A);
        imageView1C=(ImageView)findViewById(R.id.s1C); imageView1C.setTag(R.id.tag_row,"1"); imageView1C.setTag(R.id.tag_col,"C"); seatimgarray.add(imageView1C);
        imageView1J=(ImageView)findViewById(R.id.s1J); imageView1J.setTag(R.id.tag_row,"1"); imageView1J.setTag(R.id.tag_col,"J"); seatimgarray.add(imageView1J);
        imageView1L=(ImageView)findViewById(R.id.s1L); imageView1L.setTag(R.id.tag_row,"1"); imageView1L.setTag(R.id.tag_col,"L"); seatimgarray.add(imageView1L);

        imageView2A=(ImageView)findViewById(R.id.s2A); imageView2A.setTag(R.id.tag_row,"2"); imageView2A.setTag(R.id.tag_col,"A"); seatimgarray.add(imageView2A);
        imageView2C=(ImageView)findViewById(R.id.s2C); imageView2C.setTag(R.id.tag_row,"2"); imageView2C.setTag(R.id.tag_col,"C"); seatimgarray.add(imageView2C);
        imageView2J=(ImageView)findViewById(R.id.s2J); imageView2J.setTag(R.id.tag_row,"2"); imageView2J.setTag(R.id.tag_col,"J"); seatimgarray.add(imageView2J);
        imageView2L=(ImageView)findViewById(R.id.s2L); imageView2L.setTag(R.id.tag_row,"2"); imageView2L.setTag(R.id.tag_col,"L"); seatimgarray.add(imageView2L);

        imageView3A=(ImageView)findViewById(R.id.s3A); imageView3A.setTag(R.id.tag_row,"3"); imageView3A.setTag(R.id.tag_col,"A");
        imageView3C=(ImageView)findViewById(R.id.s3C); imageView3C.setTag(R.id.tag_row,"3"); imageView3C.setTag(R.id.tag_col,"C");
        imageView3J=(ImageView)findViewById(R.id.s3J); imageView3J.setTag(R.id.tag_row,"3"); imageView3J.setTag(R.id.tag_col,"J");
        imageView3L=(ImageView)findViewById(R.id.s3L); imageView3L.setTag(R.id.tag_row,"3"); imageView3L.setTag(R.id.tag_col,"L");

        System.out.println("1. row: "+imageView1A.getTag(R.id.tag_row));
        System.out.println("2. col: "+imageView1A.getTag(R.id.tag_col));
        System.out.println("3. tag: "+imageView1A.getTag(R.id.tag_iv));

        if(aircraft.equals("Airbus 319")){
            seatimgarray.add(imageView3A);
            seatimgarray.add(imageView3C);
            seatimgarray.add(imageView3J);
            seatimgarray.add(imageView3L);
        }else {
            imageView3A.setImageDrawable(null);
            imageView3C.setImageDrawable(null);
            imageView3J.setImageDrawable(null);
            imageView3L.setImageDrawable(null);
        }

        for(ImageView iv:seatimgarray){
            Object obj = iv.getTag(R.id.tag_iv);
            if(null==obj) {
                iv.setTag(R.id.tag_iv,"available");
                iv.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.chair_available));
            }
//            System.out.println("1. row: "+iv.getTag(R.id.tag_row));
//            System.out.println("2. col: "+iv.getTag(R.id.tag_col));
//            System.out.println("3. tag: "+iv.getTag(R.id.tag_iv));
        }

        Button btnback = (Button)findViewById(R.id.btnback_seat);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(SelectSeatActivity.this,SearchResultActivity.class);
                startActivity(back);
            }
        });

        Button btnsubmit =(Button)findViewById(R.id.btnsubmit);
        final List <ImageView> chosenlist = new ArrayList<>();
        final int userid = GlobalVariable.getUserid();

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(ImageView iv: seatimgarray){
                    String tag=iv.getTag(R.id.tag_iv).toString();
                    final String thisrow=iv.getTag(R.id.tag_row).toString();
                    final String thiscol=iv.getTag(R.id.tag_col).toString();
                    if(tag.equals("chosen")){
                        chosenlist.add(iv);
                        final String path =("http://10.0.2.2:5000/api/order?FlightId="+fltid+"&UserId="+userid+"&CabinType=first&ColumnName="+thiscol+"&RowNumber="+thisrow);
                        new Thread(){
                            @Override
                            public void run() {
                                try{
                                    URL url= new URL(path);
                                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                                    conn.setConnectTimeout(5000);
                                    conn.setRequestMethod("POST");
                                    int rescode = conn.getResponseCode();
                                    InputStream is = conn.getInputStream();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                    int len = -1;
                                    byte[] buffer = new byte[1024];
                                    while ((len=is.read(buffer))!=-1){
                                        baos.write(buffer,0,len);
                                    }
                                    if(rescode==200){
                                        System.out.println(thisrow+thiscol+" post success");
                                    }
                                }catch (Exception e){
                                    System.out.println("报错了1");
                                    System.out.println(e.toString());
                                }
                            }
                        }.start();
                    }
                }
            }
        });
    }

    public void test() throws NoSuchFieldException, IllegalAccessException {
        /*
        Android 通过拼接字符串的方式获取对应id的控件
        getIdentifier
        int id = context.getResources().getIdentifier("tv_col"+(i+1),"id",context.getPackageName());
        textView = findViewById(id);
        ⭐除此之外，它还可以用于获取一些其他res资源（如，string字符串，图片），对应第二参数可能要修改成 “string”
        */
        String teststr="1A";
        int testid=getResources().getIdentifier("s"+teststr,"id",getPackageName());
        System.out.println(R.id.s1A==testid);
        ImageView img1 = (ImageView)findViewById(R.id.s1A);
        ImageView img2 = (ImageView)findViewById(testid);
        System.out.println(img1==img2);
    }


    public void onClick(View view){
        String tag = view.getTag(R.id.tag_iv).toString();
        ImageView imgview =(ImageView)view;
        switch (tag){
            case "chosen":
                imgview.setTag(R.id.tag_iv,"available");
                imgview.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.chair_available));
                break;
            case "available":
                imgview.setTag(R.id.tag_iv,"chosen");
                imgview.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.chair_yourchosen));
                break;
            default:break;
        }
    }
}
