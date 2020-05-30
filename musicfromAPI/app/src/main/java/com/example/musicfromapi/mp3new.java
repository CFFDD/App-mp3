package com.example.musicfromapi;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class mp3new<MyVolumeReceiver> extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG ="mp3" ;
    private Button play_pause;
    private Button getout;
    public EditText songname;
    public boolean isplay=false;

    private MediaPlayer player = null;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp3new);
        player = new MediaPlayer();
        initView();
    }



    private void initView() {
        play_pause = (Button) findViewById(R.id.begin2);
        getout=findViewById(R.id.getout);
        songname=findViewById(R.id.url);
        play_pause.setOnClickListener(this);
        getout.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁当前activity  音乐暂停
        player.stop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.begin2:
                startmusic();
                break;
            case R.id.getout:
                tomp3();
                break;
        }
    }

    private void tomp3() {
                                    //由于MP3文件已存放于file的目标路径中，所以player!=null永远为真，
            // 而应当更改为ifplay来判断音乐是否播放中以确定是否需要释放资源
        if(isplay){                                //由于MP3文件已存放于file的目标路径中，所以player!=null永远为真，
            // 而应当更改为ifplay来判断音乐是否播放中以确定是否需要释放资源
            player.release();
            Log.i(TAG,"MadiePlayer已释放");
        }
        Intent intent3 = new Intent(mp3new.this,mp3.class) ;
        startActivity(intent3);
        finish();


    }

    private void startmusic() {

        try {

            final String data=songname.getText().toString().trim();
            //final String data = "http://m7.music.126.net/20200528184952/b5288ca5073b086dfc688bb2014c0593/ymusic/6e4b/49c7/6116/3b0ff11c6755f24d35a90626b27bfba0.mp3";
            player.setDataSource(String.valueOf(Uri.parse(data)));
            //3 准备播放
            player.prepareAsync();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            isplay=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
