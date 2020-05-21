package com.ishang.asterisk.application05191;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ishang.asterisk.application05191.global.GlobalVariable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MileageActivity extends AppCompatActivity {

    int userID= GlobalVariable.getUserid();
    String userName=GlobalVariable.getUseremail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mileage);

        Button btnback = (Button)findViewById(R.id.btnback_mile);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MileageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        if(userID>0){

            TextView textuser = (TextView)findViewById(R.id.textuser);
            textuser.setText("Hi,"+userName+", Your total mileage points is");

            new Thread(){
                @Override
                public void run() {
                    try {
                        String path ="http://10.0.2.2:5000/api/mileagepoints/"+userID;
                        URL url = new URL(path);
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setRequestMethod("GET");
                        int rescode =conn.getResponseCode();
                        InputStream is = conn.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        int len =-1;
                        byte[] buffer = new byte[1024];
                        while ((len=is.read(buffer))!=-1){
                            baos.write(buffer,0,len);
                        }
                        String content = new String(baos.toByteArray());

                        if(rescode==200) loaddata(content);

                    }catch (Exception e){
                        System.out.println("报错了1");
                        System.out.println(e.toString());
                    }

                }
            }.start();
        }else{
            Toast.makeText(MileageActivity.this, "Your personal information is undefined.\n Back to Login page.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MileageActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }

    private void loaddata(String data) {
        TableLayout layout = (TableLayout)findViewById(R.id.tablelayout);
        layout.setStretchAllColumns(true);

        try {
            JSONArray array =new JSONArray(data);
            int sumpoints= 0;
            for (int i=0; i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                int points = obj.getInt("Points");
                sumpoints+=points;
                String datestr=obj.getString("Date");

                TableRow row =new TableRow(getApplicationContext());
                TextView textpoint = new TextView(getApplicationContext());
                TextView textdate = new TextView(getApplicationContext());
                textpoint.setText(points+" Points");
                textdate.setText(datestr);
                row.addView(textpoint);
                row.addView(textdate);

                layout.addView(row);

            }
            TextView textsum = (TextView)findViewById(R.id.textpoints);
            textsum.setText(sumpoints+ " Pointes");

        }catch (Exception e){
            System.out.println("报错了2");
            System.out.println(e.toString());
        }

    }
}
