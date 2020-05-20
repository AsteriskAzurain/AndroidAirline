package com.ishang.asterisk.application05191;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn=(Button)findViewById(R.id.btnlogin);
        btn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                new Thread(){

                    @Override
                    public void run(){
                        try{
                            TextView textemail = (TextView)findViewById(R.id.textemail);
                            TextView textpswd = (TextView)findViewById(R.id.textpswd);
                            String email = textemail.getText().toString();
                            String pswd = textemail.getText().toString();

                            String path = "http://10.0.2.2:5000/api/login?Email=tt@gmail.com&Password=123456";
                            URL url= new URL(path);
                            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setRequestMethod("POST");
                            int rescode = conn.getResponseCode();
                            InputStream is = conn.getInputStream();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();

                            int len=-1;
                            byte[] buffer = new byte[1024];
                            while ((len=is.read(buffer))!=-1){
                                baos.write(buffer,0,len);
                            }
                            is.close();
                            String content = new String(baos.toByteArray());
                            Looper.prepare();
                            if(rescode==200){
                                try{
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }catch (Exception e){
                                    System.out.println("报错了2");
                                    System.out.println(e.toString());
                                }
                            }
                            Looper.loop();

                        }catch (Exception e){
                            System.out.println("报错了1");
                            System.out.println(e.toString());
                        }
                    }
                }.start();
            }
        });
    }
}