package com.animee.localmusic;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity_IntMp3 extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity_IntMp3";
    ImageView nextIv,playIv,lastIv,albumIv;
    Button begin;
    TextView singerTv,songTv;
    RecyclerView musicRv;
//    数据源
    List<Song>mDatas;
    private IntMusicAdapter adapter;

//    记录当前正在播放的音乐的位置
    int currnetPlayPosition = -1;
//    记录暂停音乐时进度条的位置
    int currentPausePositionInSong = 0;
    MediaPlayer player2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        player2 = new MediaPlayer();
        mDatas = new ArrayList<>();
//     创建适配器对象
        adapter = new IntMusicAdapter(this, mDatas);
        musicRv.setAdapter(adapter);
//        设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
//        加载本地数据源
        loadLocalMusicData();
//        设置每一项的点击事件
        setEventListener();
    }

    private void setEventListener() {
        /* 设置每一项的点击事件*/
        adapter.setOnItemClickListener(new IntMusicAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(View view, int position) {
                currnetPlayPosition = position;
                Song musicBean = mDatas.get(position);
                if(musicBean.path==null) {
                    Log.i(TAG,"该音乐暂无数据源");
                    return;
                }
                playMusicInMusicBean(musicBean);
            }
        });
    }

    public void playMusicInMusicBean(Song musicBean) {
        /*根据传入对象播放音乐*/
        //设置底部显示的歌手名称和歌曲名
        singerTv.setText(musicBean.getSingername());
        songTv.setText(musicBean.getSongname());
        stopMusic();
//                重置多媒体播放器
        player2.reset();
//                设置新的播放路径
        try {
            player2.setDataSource(String.valueOf(Uri.parse(musicBean.path)));
            Log.i(TAG,"数据源设置成功");
            //3 准备播放
            player2.prepareAsync();
            playMusic();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * 点击播放按钮播放音乐，或者暂停从新播放
    * 播放音乐有两种情况：
    * 1.从暂停到播放
    * 2.从停止到播放
    * */
    private void playMusic() {
        /* 播放音乐的函数*/
        if (player2!=null&&!player2.isPlaying()) {
            if (currentPausePositionInSong == 0) {
                try {
                    player2.prepareAsync();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                Log.i(TAG,"即将开始播放");
                player2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        Log.i(TAG,"即将开始播放222");
                        player2.start();
                        Log.i(TAG,"开始啦~~~");
                    }
                });
            }else{
//                从暂停到播放
                player2.seekTo(currentPausePositionInSong);
                player2.start();
            }
            playIv.setImageResource(R.mipmap.icon_pause);
        }
    }
    private void pauseMusic() {
        /* 暂停音乐的函数*/
        if (player2!=null&&player2.isPlaying()) {
            currentPausePositionInSong = player2.getCurrentPosition();
            player2.pause();
            playIv.setImageResource(R.mipmap.icon_play);
        }
    }
    private void stopMusic() {
        /* 停止音乐的函数*/
        if (player2!=null) {
            currentPausePositionInSong = 0;
            player2.pause();
            player2.seekTo(0);
            player2.stop();
            playIv.setImageResource(R.mipmap.icon_play);
        }

    }


    private void loadLocalMusicData() {
        /* 加载本地存储当中的音乐mp3文件到集合当中*/
            MusicUtils.query2();
        Log.i(TAG, "成功返回，开始建立列表");
//        数据源变化，提示适配器更新
        adapter.notifyDataSetChanged();
    }


    private String getAlbumArt(String album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = this.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + album_id),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        return album_art;
    }
    private void initView() {
        /* 初始化控件的函数*/
        nextIv = findViewById(R.id.local_music_bottom_iv_next);
        playIv = findViewById(R.id.local_music_bottom_iv_play);
        lastIv = findViewById(R.id.local_music_bottom_iv_last);
        albumIv = findViewById(R.id.local_music_bottom_iv_icon);
        singerTv = findViewById(R.id.local_music_bottom_tv_singer);
        songTv = findViewById(R.id.local_music_bottom_tv_song);
        musicRv = findViewById(R.id.local_music_rv);
        begin = findViewById(R.id.begin);
        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
        begin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.local_music_bottom_iv_last:       //上一首
                if (currnetPlayPosition ==0) {
                    Toast.makeText(this,"已经是第一首了，没有上一曲！",Toast.LENGTH_SHORT).show();
                    return;
                }
                currnetPlayPosition = currnetPlayPosition-1;
                Song lastBean = mDatas.get(currnetPlayPosition);
                if(lastBean.path==null){Toast.makeText(this,"该音乐暂无播放源",Toast.LENGTH_SHORT).show();}
                else{ playMusicInMusicBean(lastBean);}
                break;
            case R.id.local_music_bottom_iv_next:       //下一首
                if (currnetPlayPosition ==mDatas.size()-1) {
                    Toast.makeText(this,"已经是最后一首了，没有下一曲！",Toast.LENGTH_SHORT).show();
                    return;
                }
                currnetPlayPosition = currnetPlayPosition+1;
                Song nextBean = mDatas.get(currnetPlayPosition);
                if(nextBean.path==null){Toast.makeText(this,"该音乐暂无播放源",Toast.LENGTH_SHORT).show();}
                else{ playMusicInMusicBean(nextBean);}
                break;
            case R.id.local_music_bottom_iv_play:       //播放与暂停
                if (currnetPlayPosition == -1) {
//                    并没有选中要播放的音乐
                    Toast.makeText(this,"请选择想要播放的音乐",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mDatas.get(currnetPlayPosition).path==null) {
                    Toast.makeText(this,"该音乐暂无播放源",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (player2.isPlaying()) {
//                    此时处于播放状态，需要暂停音乐
                    pauseMusic();
                }else {
//                    此时没有播放音乐，点击开始播放音乐
                    playMusic();
                }
            case R.id.begin:        //开始搜索资源
                loadLocalMusicData();
        }
    }



    //来电处理
    protected void onDestroy() {
        if(player2 != null){
            if(player2.isPlaying()){
                player2.stop();
            }
            player2.release();
        }
        super.onDestroy();
    }

    protected void onPause() {
        if(player2 != null){
            if(player2.isPlaying()){
                player2.pause();
            }
        }
        super.onPause();
    }

    protected void onResume() {
        if(player2 != null){
            if(!player2.isPlaying()){
                player2.start();
            }
        }
        super.onResume();
    }


}
