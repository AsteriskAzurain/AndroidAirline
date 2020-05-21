package com.ishang.asterisk.application05191;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.ishang.asterisk.application05191.global.GlobalVariable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.microedition.khronos.opengles.GL;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;
    int playingPos = 0;

    private Button btnsearch;
    private Button btnpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getuserid();

        getsponsorinfo();

        videoView = (VideoView) findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.video;
        videoView.setVideoURI(Uri.parse(uri));
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
        videoView.requestFocus();
        videoView.start();
        getnewpos();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){

            //VideoView是MediaPlayer，在VideoView中持有一个MediaPlayer成员变量，可以通过该MediaPlayer设置循环播放
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        });


        btnsearch=(Button)findViewById(R.id.btnflt);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playingPos=videoView.getCurrentPosition();
                GlobalVariable.setPlayingpos(playingPos);
                System.out.println("current position:"+GlobalVariable.getPlayingpos());
                Intent page = new Intent(MainActivity.this, SearchFlightActivity.class);
                //page.putExtra("videopos", playingPos);
                startActivity(page);
            }
        });
        btnpoint=(Button)findViewById(R.id.btnpoint);
        btnpoint.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                playingPos=videoView.getCurrentPosition();
                GlobalVariable.setPlayingpos(playingPos);
                System.out.println("current position:"+GlobalVariable.getPlayingpos());
                Intent page = new Intent(MainActivity.this, MileageActivity.class);
                startActivity(page);
            }
        });
        Button btnlogout = (Button)findViewById(R.id.btnout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable.setUseremail(null);
                GlobalVariable.setUserid(0);
                Intent intent1 = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent1);
            }
        });

    }

    private void getuserid() {
        final String email = GlobalVariable.getUseremail();
        if(null==email || email.isEmpty()) GlobalVariable.setUserid(0);
        else{
            new Thread(){
                @Override
                public void run() {
                    try{
                        String path ="http://10.0.2.2:5000/api/user/"+email;
                        URL url =new URL(path);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setRequestMethod("GET");
                        int rescode = conn.getResponseCode();
                        InputStream is = conn.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        int len = -1;
                        byte[] buffer = new byte[1024];
                        while ((len=is.read(buffer))!=-1){
                            baos.write(buffer,0,len);
                        }
                        String content = new String(baos.toByteArray());

                        JSONObject userinfo = new JSONArray(content).getJSONObject(0);
                        GlobalVariable.setUserid(userinfo.getInt("ID"));
                        String username=(userinfo.getString("FirstName")+" "+userinfo.getString("LastName"));
                        if(userinfo.getString("Gender").equals("M")) username ="Mr. "+username;
                        else username = "Mrs. "+username;
                        GlobalVariable.setUsername(username);
                        System.out.println("name: "+GlobalVariable.getUsername()+"\nID: "+GlobalVariable.getUserid());

                    }catch (Exception e){
                        System.out.println("报错了1");
                        System.out.println(e.toString());
                    }
                }
            }.start();
        }
    }

    private void getsponsorinfo() {
        final TextView textsp= (TextView) findViewById(R.id.textsponsor);
        textsp.setText("test sponsor");
        new Thread(){
            @Override
            public void run(){
                try{
                    String path= "http://10.0.2.2:5000/api/sponsor/list";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int rescode = conn.getResponseCode();
                    InputStream is = conn.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    int len = -1;
                    byte[] buffer = new byte[1024];
                    while ((len=is.read(buffer))!=-1){
                        baos.write(buffer,0,len);
                    }
                    String content = new String(baos.toByteArray());

                    Looper.prepare();
                    String sponsors="Sponsors: ";
                    if(rescode==200){
                        JSONArray array = new JSONArray(content);
                        for (int i =0; i<array.length();i++){
                            JSONObject obj = array.getJSONObject(i);
                            sponsors+=obj.getString("Name");
                            sponsors+="           ";
                        }
                        textsp.setText(sponsors);
                        textsp.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        textsp.setSingleLine(true);
                        textsp.setSelected(true);
                        textsp.setFocusable(true);
                        textsp.setFocusableInTouchMode(true);
                    }
                    Looper.loop();

                }catch (Exception e){
                    System.out.println("报错了1");
                    System.out.println(e.toString());
                }
            }
        }.start();
    }

    public void getnewpos() {
        //Intent page =getIntent();
        //int newpos= page.getIntExtra("newpos",0);
        int newpos = GlobalVariable.getPlayingpos();
        System.out.println("new pos:"+newpos);
        videoView.seekTo(newpos);
    }
}
