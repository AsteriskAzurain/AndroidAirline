package com.ishang.asterisk.application05191;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ishang.asterisk.application05191.global.GlobalVariable;

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
                System.out.println("current position:"+playingPos);
                Intent page = new Intent(MainActivity.this, MileageActivity.class);
                startActivity(page);
            }
        });

    }

    public void getnewpos() {
        //Intent page =getIntent();
        //int newpos= page.getIntExtra("newpos",0);
        int newpos = GlobalVariable.getPlayingpos();
        System.out.println(newpos);
        videoView.seekTo(newpos);
    }
}
