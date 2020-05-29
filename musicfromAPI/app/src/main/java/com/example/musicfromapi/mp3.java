package com.example.musicfromapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class mp3<MyVolumeReceiver> extends AppCompatActivity {
    private static final String TAG ="mp3" ;
    private Button stop, reset;
    private EditText editText;

    private SeekBar seekbar;
    private String musicName = "test2.mp3";
    private MediaPlayer player = null;
    private boolean ifplay = false;
    private boolean iffirst = true;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private boolean isChanging=false;//互斥变量，防止定时器与SeekBar拖动时进度冲突
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp3);
        player = new MediaPlayer();
        findViews();// 各组件
    }

    private void findViews() {
        stop = findViewById(R.id.button1);
        reset =  findViewById(R.id.button2);
        stop.setOnClickListener(new MyClick());
        reset.setOnClickListener(new MyClick());
        editText=(EditText)findViewById(R.id.editText5);

        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new MySeekbar());
    }

    class MyClick implements View.OnClickListener {
        public void onClick(View v) {
            final String data=editText.getText().toString().trim();
            //final String data = "http://m7.music.126.net/20200528184952/b5288ca5073b086dfc688bb2014c0593/ymusic/6e4b/49c7/6116/3b0ff11c6755f24d35a90626b27bfba0.mp3";

            //localhost:3000/song/url?id=29017036
            switch (v.getId()) {
                default:
                    break;
                case R.id.button1:
                    StartAndPause(data);
                    break;
                case R.id.button2:
                    resetmusic(data);
                    break;
                }

        }
    }
    private void resetmusic(final String data){
        if (ifplay) {
            //不需要    play_pause.setImageDrawable(getResources().getDrawable(R.drawable.begin));
            player.seekTo(0);
        } else {
            player.reset();
            try {
                player.setDataSource(String.valueOf(Uri.parse(data)));
                //3 准备播放
                player.prepareAsync();
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    private void StartAndPause(final String data){

        if (!ifplay) {
            Log.i(TAG,"播放资源存在");

            stop.setText("暂停");
            if (iffirst) {
                //player.reset();
                try {
                    player.setDataSource(String.valueOf(Uri.parse(data)));
                    //3 准备播放
                    player.prepareAsync();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(TAG,"啦啦啦我准备好了");
                seekbar.setMax(player.getDuration());//设置进度条
                //----------定时器记录播放进度---------//
                mTimer = new Timer();
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if(isChanging==true) {
                            return;
                        }
                        seekbar.setProgress(player.getCurrentPosition());
                    }
                };
                mTimer.schedule(mTimerTask, 0, 10);
                iffirst=false;
            }
            Log.i(TAG,"即将开始播放");
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            ifplay = true;
        } else if (ifplay) {
            stop.setText("开始");
            player.pause();
            Log.i(TAG,"year暂停成功");
            ifplay = false;
        }


    }




    //进度条处理
    class MySeekbar implements OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            isChanging=true;
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            player.seekTo(seekbar.getProgress());
            isChanging=false;
        }

    }
    //来电处理
    protected void onDestroy() {
        if(player != null){
            if(player.isPlaying()){
                player.stop();
            }
            player.release();
        }
        super.onDestroy();
    }

    protected void onPause() {
        if(player != null){
            if(player.isPlaying()){
                player.pause();
            }
        }
        super.onPause();
    }

    protected void onResume() {
        if(player != null){
            if(!player.isPlaying()){
                player.start();
            }
        }
        super.onResume();
    }

    public void return_user (View view){
        /*if (player != null && !ifplay) {
            Log.i(TAG,"并无播放中资源");
        }else if (ifplay) {
            play_pause.setImageDrawable(getResources().getDrawable(R.drawable.begin));
            player.pause();
            ifplay = false;
        }*/
        /*if(player!=null){
            player.release();
            Log.i(TAG,"MadiePlayer已释放");
        }*/
        if(ifplay){                                //由于MP3文件已存放于file的目标路径中，所以player!=null永远为真，
            // 而应当更改为ifplay来判断音乐是否播放中以确定是否需要释放资源
            player.release();
            Log.i(TAG,"MadiePlayer已释放");
        }
        Intent intent3 = new Intent(mp3.this,mp3new.class) ;
        startActivity(intent3);
        finish();

    }

}