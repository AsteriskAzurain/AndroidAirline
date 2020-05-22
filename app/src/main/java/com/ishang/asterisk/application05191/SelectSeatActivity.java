package com.ishang.asterisk.application05191;

import android.content.Intent;
import android.os.Looper;
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
import java.util.Arrays;

public class SelectSeatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat);

        Intent intent = getIntent();
        final int fltid = intent.getIntExtra("fltid",1);
        String depstr=intent.getStringExtra("deptime");
        String fltnum = intent.getStringExtra("fltnum");
        String aircraft= intent.getStringExtra("aircraft");

        removeimg(aircraft);

        TextView textdep = (TextView)findViewById(R.id.textdeptime1);
        textdep.setText(depstr);
        TextView textfltnum = (TextView)findViewById(R.id.textfltnum1);
        textfltnum.setText(fltnum);
        TextView textaircraft = (TextView)findViewById(R.id.textaircraft1);
        textaircraft.setText(aircraft);

        getseatinfo(fltid);

        Button btnback = (Button)findViewById(R.id.btnback_seat);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(SelectSeatActivity.this,SearchResultActivity.class);
                startActivity(back);
            }
        });

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

    private void getseatinfo(final int fltid) {

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
        ImageView img = new ImageView(this);
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
                objimg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.chair_yourchosen));
                objimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        objimg.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.chair_available));
                    }
                });
            }else{
                objimg.setClickable(false);
            }
        }
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
        System.out.println(view.toString());
    }
}
