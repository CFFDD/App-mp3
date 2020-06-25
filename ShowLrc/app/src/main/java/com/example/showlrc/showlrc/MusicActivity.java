package com.example.showlrc.showlrc;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.annotation.RequiresApi;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.showlrc.IntMusicBean;
import com.example.showlrc.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {

    List<IntMusicBean> mDatas;
    //    记录暂停音乐时进度条的位置
    int currentPausePositionInSong = 0;
    public String lrc = null;
    private LrcView lrcView;



    //定义activity_music.xml的控件对象
    //设置音乐播放模式
    private int i = 0;              //确定播放模式按键点击次数
    private int playMode = 0;       //播放模式选择
    private int buttonWitch = 0;    //上下曲选择
    private ImageView bgImgv;
    private TextView titleTv;
    private TextView artistTv;
    private TextView currrentTv;
    private TextView totalTv;
    private ImageView prevImgv;
    private ImageView nextImgv;
    private int position;
    private MediaPlayer mediaPlayer;
    private ImageView pauseImgv;
    private ImageView downImg;
    private ImageView styleImg;
    private SeekBar seekBar;
    private int totaltime;
    private boolean isStop;
    public int size=0;

    private String TAG = "MusicActivity";

    //public String path="http://m7.music.126.net/20200619120708/48a192657fc29b36d0ad609559cb7b76/ymusic/55ee/ab03/4876/3d736fe60a5e4cb4f6cffc2b3673f852.mp3";
    private String albumBip = "no null";

    //Handler实现向主线程进行传值
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            seekBar.setProgress((int) (msg.what));
            currrentTv.setText(formatTime(msg.what));
        }
    };

    //MusicActivity onCreate（）方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seerbar);
        Log.i(TAG," 跳转完成，执行onCreate中");

        bingID();                                                                           //调用bingID();方法实现对控件的绑定

        Intent intent = getIntent();                                                    //通过getIntent()方法实现intent信息的获取
        position = intent.getIntExtra("position", 0);               //获取position
        //currentPausePositionInSong = intent.getIntExtra("PausePosition", 0);       //获取页面跳转时的音乐播放进度
//        Log.i(TAG," position===="+position);
//        Log.i(TAG,"PausePosition===="+currentPausePositionInSong);
        mDatas = (List<IntMusicBean>) intent.getSerializableExtra("music");
        size=mDatas.size();
//        Log.i(TAG," mDatas===="+mDatas);
//        Log.i(TAG," mDatas.get(position)"+mDatas.get(position));
//        Log.i(TAG," mDatas.get(position).path"+mDatas.get(position).path);
        mediaPlayer = new MediaPlayer();

        try {
            requestPermission();            //申请权限，成功后会调用 "页面展示-播放" 方法

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG," requestPermission完成");
        //prevAndnextplaying(mDatas.get(position).path);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {               //seekbar设置监听，实现指哪放到哪
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    //prevAndnext() 实现页面的展现
    private void prevAndnextplaying(String path) {
        Log.i(TAG," 开始实现页面展现");
        isStop = false;
        mediaPlayer.reset();
        titleTv.setText(mDatas.get(position).songname);
        artistTv.setText(mDatas.get(position).singername + "--" + mDatas.get(position).singerid);
        pauseImgv.setImageResource(R.mipmap.ic_play_btn_pause);

        /*if (albumBip != null) {
            Bitmap bgbm = BlurUtil.doBlur(com.example.showlrc.np.utils.Common.musicList.get(position).albumBip, 10, 5);//将专辑虚化
            bgImgv.setImageBitmap(bgbm);                                    //设置虚化后的专辑图片为背景
        } else {*/
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.touxiang1);
            bgImgv.setImageBitmap(bitmap);
        //}
        try {
            Log.i(TAG,"开始设置数据源");
            mediaPlayer.setDataSource(String.valueOf(Uri.parse(path)));
            mediaPlayer.prepareAsync();                   // 准备
            playMusic();                        // 启动
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {        //音乐结束监听
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(!mediaPlayer.isPlaying()){
                        setPlayMode();      //根据播放模式确定下一首歌
                    }

                }
            });
        } catch (IllegalArgumentException | SecurityException | IllegalStateException
                | IOException e) {
            e.printStackTrace();
        }

        totalTv.setText(formatTime(mDatas.get(position).length));
        seekBar.setMax(mDatas.get(position).length);

        MusicThread musicThread = new MusicThread();                                         //启动线程
        new Thread(musicThread).start();


    }

    private void playMusic() {
        Log.i(TAG," /* 播放音乐的函数*/");
        /* 播放音乐的函数*/
        if (mediaPlayer!=null&&!mediaPlayer.isPlaying()) {
            if (currentPausePositionInSong == 0) {
                try {
                    mediaPlayer.prepareAsync();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                Log.i(TAG,"已异步准备，即将开始播放");
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        Log.i(TAG,"已监听到准备，即将开始播放222");
                        mediaPlayer.start();
                        Log.i(TAG,"开始啦~~~");
                    }
                });
            }else{
//                从暂停到播放
                mediaPlayer.seekTo(currentPausePositionInSong);
                mediaPlayer.start();
            }
        }

    }


    private void pauseMusic() {
        Log.i(TAG," /* 暂停音乐的函数*/");
        /* 暂停音乐的函数*/
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
            currentPausePositionInSong = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setPlayMode()  {                //音乐结束后，根据循环模式，判断下一首歌
        if (playMode == 0)//全部循环，默认循环播放
        {
            if (position == size - 1)//若当前已播放至列表最后一首歌，则下一首歌为列表第一首歌
            {
                position = 0;// 第一首
                mediaPlayer.reset();
//                prevAndnextplaying(mDatas.get(position).path);
                Log.i(TAG,"列表循环模式 position ==="+position);
                try {
                    requestPermission();            //申请权限，成功后会调用 "页面展示-播放" 方法

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(TAG," requestPermission完成");

            } else {
                position++;
                mediaPlayer.reset();
//                prevAndnextplaying(mDatas.get(position).path);
                Log.i(TAG,"列表循环模式 position ==="+position);
                try {
                    requestPermission();            //申请权限，成功后会调用 "页面展示-播放" 方法

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(TAG," requestPermission完成");
            }
        } else if (playMode == 1)//单曲循环
        {
            //position不需要更改
            mediaPlayer.reset();
//            prevAndnextplaying(mDatas.get(position).path);
            Log.i(TAG,"单曲循环模式 position ==="+position);
            try {
                requestPermission();            //申请权限，成功后会调用 "页面展示-播放" 方法

            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG,"单曲循环模式 requestPermission完成");

        } else if (playMode == 2)//随机
        {
            position = (int) (Math.random() * size);//随机播放
            mediaPlayer.reset();
//            prevAndnextplaying(mDatas.get(position).path);
            Log.i(TAG,"随机循环模式 position ==="+position);
            try {
                requestPermission();            //申请权限，成功后会调用 "页面展示-播放" 方法

            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG,"随机循环模式 requestPermission完成");

        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setBtnMode() {         //上下曲点击事件
        if (playMode == 0)//全部循环
        {
            if (position == size - 1)      //在最后一曲上
            {
                Log.i(TAG,"  //在最后一曲上");
                if (buttonWitch == 1) { //上一曲
                    position--;
                    mediaPlayer.reset();
//                    prevAndnextplaying(mDatas.get(position).path);
                    Log.i(TAG,"列表循环模式下 上曲事件 position ==="+position);
                    try {
                        requestPermission();            //申请权限，成功后会调用 "页面展示-播放" 方法

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG," requestPermission完成");

                } else if (buttonWitch == 2) {  //下一曲
                    position = 0;// 第一首
                    mediaPlayer.reset();
//                    prevAndnextplaying(mDatas.get(position).path);
                    Log.i(TAG,"列表循环模式下 下曲事件 position ==="+position);
                    try {
                        requestPermission();            //申请权限，成功后会调用 "页面展示-播放" 方法

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG," requestPermission完成");

                }
            } else if (position == 0) {//在第一曲上
                Log.i(TAG,"//在第一曲上");
                if (buttonWitch == 1) {//上一曲
                    position = size - 1;
                    mediaPlayer.reset();
//                    prevAndnextplaying(mDatas.get(position).path);
                    Log.i(TAG," position ==="+position);
                    try {
                        requestPermission();            //申请权限，成功后会调用 "页面展示-播放" 方法

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG,"列表循环模式下 上曲事件 requestPermission完成");

                } else if (buttonWitch == 2) {//下一曲
                    position++;
                    mediaPlayer.reset();
//                    prevAndnextplaying(mDatas.get(position).path);
                    Log.i(TAG," position ==="+position);
                    try {
                        requestPermission();            //申请权限，成功后会调用 "页面展示-播放" 方法

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG,"列表循环模式下 下曲事件 requestPermission完成");

                }
            }else {                 //不在最后一曲也不在第一曲上
                Log.i(TAG,"//不在最后一曲也不在第一曲上");
                if(buttonWitch ==1){
                    position--;
                    mediaPlayer.reset();
//                    prevAndnextplaying(mDatas.get(position).path);
                    Log.i(TAG,"列表循环模式下 上曲事件 position ==="+position);
                    try {
                        requestPermission();            //申请权限，成功后会调用 "页面展示-播放" 方法

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG," requestPermission完成");

                }else if(buttonWitch ==2){
                    position++;
                    mediaPlayer.reset();
//                    prevAndnextplaying(mDatas.get(position).path);
                    Log.i(TAG,"列表循环模式下 下曲事件 position ==="+position);
                    try {
                        requestPermission();            //申请权限，成功后会调用 "页面展示-播放" 方法

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG," requestPermission完成");

                }
            }
        } else if (playMode == 1)//单曲循环
        {
            //position不需要更改
            mediaPlayer.reset();
//            prevAndnextplaying(mDatas.get(position).path);
            Log.i(TAG,"单曲循环模式下 不需要更改 position ==="+position);
            try {
                requestPermission();            //申请权限，成功后会调用 "页面展示-播放" 方法

            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG," requestPermission完成");

        } else if (playMode == 2)//随机
        {
            position = (int) (Math.random() * size);//随机播放
            mediaPlayer.reset();
//            prevAndnextplaying(mDatas.get(position).path);
            Log.i(TAG,"随机循环模式下 随机选择 position ==="+position);
            try {
                requestPermission();            //申请权限，成功后会调用 "页面展示-播放" 方法

            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG," requestPermission完成");

        }
    }

    //格式化数字
    private String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");    //规定固定的格式
        String totaltime = simpleDateFormat.format(date);
        return totaltime;
    }


    //绑定id，设置监听
    private void bingID() {
        titleTv = findViewById(R.id.music_title_tv);
        artistTv = findViewById(R.id.music_artist_tv);
        bgImgv = findViewById(R.id.music_bg_imgv);
        currrentTv = findViewById(R.id.music_current_tv);
        totalTv = findViewById(R.id.music_total_tv);
        prevImgv = findViewById(R.id.music_prev_imgv);
        nextImgv = findViewById(R.id.music_next_imgv);
        pauseImgv = findViewById(R.id.music_pause_imgv);
        downImg = findViewById(R.id.music_down_imgv);
        seekBar = findViewById(R.id.music_seekbar);
        styleImg = findViewById(R.id.music_play_btn_loop_img);
        lrcView = findViewById(R.id.lrcView);
        pauseImgv.setOnClickListener(this);
        prevImgv.setOnClickListener(this);
        nextImgv.setOnClickListener(this);
        downImg.setOnClickListener(this);
        styleImg.setOnClickListener(this);

    }

    //onClick（）点击监听
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music_prev_imgv:
                mediaPlayer.stop();
                currentPausePositionInSong=0;
                buttonWitch = 1;
                setBtnMode();
                break;
            case R.id.music_next_imgv:
                mediaPlayer.stop();
                currentPausePositionInSong=0;
                buttonWitch = 2;
                setBtnMode();
                break;
            case R.id.music_pause_imgv:
                if (mediaPlayer.isPlaying()) {
                    pauseMusic();
                    pauseImgv.setImageResource(R.mipmap.ic_play_btn_play);
                } else {
                    playMusic();
                    pauseImgv.setImageResource(R.mipmap.ic_play_btn_pause);
                }
                break;
            case R.id.music_play_btn_loop_img:
                i++;
                if (i % 3 == 1) {
                    Toast.makeText(MusicActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();
                    playMode = 1;
                    styleImg.setImageResource(R.mipmap.ic_play_btn_one);

/*                    int m =mDatas.indexOf(mDatas.get(position));
                    Log.i(TAG,"position=="+m);
                    setPlayMode(m);*/
                }
                if (i % 3 == 2) {
                    Toast.makeText(MusicActivity.this, "随机播放", Toast.LENGTH_SHORT).show();
                    playMode = 2;
                    styleImg.setImageResource(R.mipmap.ic_play_btn_shuffle);
/*                    int m =mDatas.indexOf(mDatas.get(position));
                    Log.i(TAG,"position=="+m);
                    setPlayMode(m);*/
                }
                if (i % 3 == 0) {
                    Toast.makeText(MusicActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
                    playMode = 0;
                    styleImg.setImageResource(R.mipmap.ic_play_btn_loop);
/*                    int m =mDatas.indexOf(mDatas.get(position));        //得到当前播放的音乐在LIST中的位置
                    Log.i(TAG,"position=="+m);
                    setPlayMode(m);*/
                }
                break;
            case R.id.music_down_imgv:
                mediaPlayer.stop();
                this.finish();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        for (IntMusicBean music : mDatas
                ) {
            music.isPlaying = false;
        }
        mDatas.get(position).isPlaying = true;
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        i = 0;
        isStop = false;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }


    }

    //创建一个类MusicThread实现Runnable接口，实现多线程
    class MusicThread implements Runnable {
        @Override
        public void run() {
            while (!isStop && mDatas.get(position) != null) {
                try {
                    //让线程睡眠1000毫秒
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //放送给Handler现在的运行到的时间，进行ui更新
                handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());

            }
        }
    }


    private void initLrc(){
        lrcView.setLrc(mDatas.get(position).lyric).setPlayer(mediaPlayer).draw();
    }

    //申请权限
    private void requestPermission() throws IOException {
        if (ContextCompat.checkSelfPermission(MusicActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MusicActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {
            prevAndnextplaying(mDatas.get(position).path);
            Log.i(TAG," prevAndnextplaying完成");
            initLrc();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                prevAndnextplaying(mDatas.get(position).path);
                Log.i(TAG," prevAndnextplaying完成");
                initLrc();
            } else {
                Toast.makeText(this, "拒绝该权限无法使用该程序", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }



}

