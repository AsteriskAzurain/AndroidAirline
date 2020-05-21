package com.ishang.asterisk.application05191;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ishang.asterisk.application05191.global.GlobalVariable;

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
//                            TextView textemail = (TextView)findViewById(R.id.textemail);
//                            TextView textpswd = (TextView)findViewById(R.id.textpswd);
                            EditText editTextemail = (EditText)findViewById(R.id.editemail);
                            EditText editTextpswd=(EditText)findViewById(R.id.editpswd);
                            String email = editTextemail.getText().toString();
                            String pswd = editTextpswd.getText().toString();
                            if(null==email || email.isEmpty()) email="tt@gmail.com";
                            if(null==pswd || pswd.isEmpty()) pswd="123456";

                            String path = "http://10.0.2.2:5000/api/login?Email="+email+"&Password="+pswd;
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
                                    GlobalVariable.setUseremail(email);
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
