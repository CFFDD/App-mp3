package com.animee.localmusic;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity_IntMp3 extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity_IntMp3";
    private static final int CHANGE_UI = 0;
    //    private static DiscoverySession handler;
    ImageView nextIv,playIv,lastIv,albumIv;
    Button select;
    TextView singerTv,songTv;
    EditText keyword;
    RecyclerView musicRv;
    //    数据源
    static List<Song>mDatas;
    private static IntMusicAdapter adapter;




    //    记录当前正在播放的音乐的位置
    int currnetPlayPosition = -1;
    //    记录暂停音乐时进度条的位置
    int currentPausePositionInSong = 0;
    MediaPlayer player2;


    private static Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) {
                case CHANGE_UI:
                    //在这里可以进行UI操作
                    adapter.notifyDataSetChanged();

                    break;
                default:
                    break;
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        player2 = new MediaPlayer();
        mDatas = new ArrayList<>();

//     创建适配器对象
        adapter = new IntMusicAdapter(MainActivity_IntMp3.this, mDatas);
        Log.i(TAG,"adapter---------"+adapter);
        Log.i(TAG,"创建适配器对象");
        musicRv.setAdapter(adapter);
        Log.i(TAG,"musicRv.setAdapter(adapter);");
//        设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
/*//        加载本地数据源
        loadLocalMusicData();
        Log.i(TAG,"加载本地数据源");

        //        数据源变化，提示适配器更新
        adapter.notifyDataSetChanged();
        Log.i(TAG, "已提示适配器更新");

//        设置每一项的点击事件
        setEventListener();
        Log.i(TAG,"设置每一项的点击事件");*/
    }

    private void setEventListener() {
        /* 设置每一项的点击事件*/
        adapter.setOnItemClickListener(new IntMusicAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(View view, int position) {
                Log.i(TAG,"*****************setEventListener***************");
                Log.i(TAG,"用户点击了一项Item,调用了setEventListener");
                currnetPlayPosition = position;
                Song musicBean = mDatas.get(position);
                Log.i(TAG,"将position给Song musicBean");
                Log.i(TAG,"musicBean.path=="+musicBean.path);
                Log.i(TAG,"musicBean.path.length()=="+musicBean.path.length());
                if(musicBean.path == "null") {
                    Log.i(TAG,"该音乐暂无数据源");
                    return;
                }else{
                    Log.i(TAG,"音乐源存在，开始装载");
                    playMusicInMusicBean(musicBean);
                    Log.i(TAG,"musicBean已装入playMusicInMusicBean中");
                    Log.i(TAG,"musicBean="+musicBean);
                }

            }
        });
    }

    public void playMusicInMusicBean(Song musicBean) {
        /*根据传入对象播放音乐*/
        //设置底部显示的歌手名称和歌曲名
        Log.i(TAG,"**********************playMusicInMusicBean*********");
        singerTv.setText(musicBean.getSingername());
        songTv.setText(musicBean.getSongname());
        Log.i(TAG,"已设置text view");
        stopMusic();
//                重置多媒体播放器
        Log.i(TAG,"stopMusic");
        player2.reset();
        Log.i(TAG,"reset");
//                设置新的播放路径
        try {
            Log.i(TAG,"musicBean.path="+musicBean.getPath());
            boolean bool = musicBean.getPath().getClass().getName().equals("java.lang.String");
            Log.i(TAG,"bool=="+bool);   //bool=ture则是String类型
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


    private void loadLocalMusicData() throws UnsupportedEncodingException {
        /* 加载本地存储当中的音乐mp3文件到集合当中*/
        Log.i(TAG, "keyword.toString()=="+keyword.getText().toString().trim());
        String name=keyword.getText().toString().trim();
        String param = URLEncoder.encode(name,"utf-8").replaceAll("\\+","%20");

        query2(param);
        Log.i(TAG, "成功返回，开始建立列表");
/*//        数据源变化，提示适配器更新
        adapter.notifyDataSetChanged();
        Log.i(TAG, "已提示适配器更新");*/
    }





    private void initView() {
        /* 初始化控件的函数*/
        nextIv = findViewById(R.id.local_music_bottom_iv_next);
        playIv = findViewById(R.id.local_music_bottom_iv_play);
        lastIv = findViewById(R.id.local_music_bottom_iv_last);
        albumIv = findViewById(R.id.local_music_bottom_iv_icon);
        singerTv = findViewById(R.id.local_music_bottom_tv_singer);
        songTv = findViewById(R.id.local_music_bottom_tv_song);
        keyword = findViewById(R.id.keywords);
        musicRv = findViewById(R.id.local_music_rv);

        select = findViewById(R.id.select);
        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);

        select.setOnClickListener(this);
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
                break;
            case R.id.select:
                mDatas.clear();
                adapter.notifyDataSetChanged();


                //        加载本地数据源
                try {
                    loadLocalMusicData();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.i(TAG,"加载本地数据源");
                Log.i(TAG,"adapter==="+adapter);
                //        数据源变化，提示适配器更新
                //adapter.notifyDataSetChanged();
                Log.i(TAG, "已提示适配器更新");
                Log.i(TAG,"mDatas==="+mDatas);
                Log.i(TAG,"adapter==="+adapter);
                //        设置每一项的点击事件
                setEventListener();
                Log.i(TAG,"设置每一项的点击事件");


                break;
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

    private static String songpath;
    private static String lyric;





    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */



    public static void query2(final String key) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try {

                    Log.i(TAG, "开始建立连接");
                    HttpURLConnection connection;
                    //String finalTitle = URLEncoder.encode(title,"utf-8");
                    URL url = new URL("http://192.168.1.108:3000/search?keywords="+key+"&limit=15");
                    Log.i(TAG, "url=="+url);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(60 * 1000);
                    connection.setReadTimeout(60 * 1000);
                    connection.connect();
                    Log.i(TAG, "成功建立连接");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Log.i(TAG, "BufferedReader");
                    String s;
                    if ((s = reader.readLine()) != null) {
//                        data_compilation m=new data_compilation();
                        doJson(s,mDatas);
                        Log.i(TAG, "得到数据");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "即将从query2返回");

                Message message = new Message();
                message.what = CHANGE_UI;
                handler.sendMessage(message);
            }
        }).start();
        Log.i(TAG, "退出子线程");
    }

    public static void doJson(String json,List mDatas){

        Song song1 = null;
        String id="";
        JSONObject jsonObject = null;
        JSONObject result = null;

        try {
            Log.i(TAG,"json");
            jsonObject = new JSONObject(json);
            result = new JSONObject(jsonObject.getString("result"));
            JSONArray songs_array = new JSONArray(result.getString("songs"));
            Log.i(TAG,"songs");
            for (int i=0;i<songs_array.length();i++){
                JSONObject songs = songs_array.getJSONObject(i);
                Log.i(TAG,"songs_array.getJSONObject");
                String songid = songs.getString("id");
                String songname =songs.getString("name");
                //前往下一层 获取歌手数据
                JSONArray singer = new JSONArray(songs.getString("artists"));
                JSONObject artistneme=new JSONObject(singer.getString(0));
                //  ↑  获取数组0号元素作为对象，这里的0号元素是由在目标API中，目标数据在artists数组中的位置确定的
                String singerid = artistneme.getString("id");
                String singername = artistneme.getString("name");


                String path = getAdress(songid);

                String lyric = getLyric(songid);


                Song song = new Song(songid,songname,singerid,singername,path,lyric);
                Log.i(TAG,"song.songid="+song.songid);
                Log.i(TAG,"song.songname="+song.songname);
                Log.i(TAG,"song.singerid="+song.singerid);
                Log.i(TAG,"song.singername="+song.singername);
                Log.i(TAG,"**************开始获取歌曲路径+歌词路径****************");
                Log.i(TAG,"song.path="+song.path);
                Log.i(TAG,"song.path="+song.lyric);
                Log.i(TAG,"!!!!!!!!!!!!!!     获取结束     !!!!!!!!!!!!!!!");
                Log.i(TAG,"mDatas==="+ mDatas);
                mDatas.add(song);
                Log.i(TAG,"mDatas==="+ mDatas);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"即将返回");

    }


    private static String getLyric(final String songid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    HttpURLConnection connection;
                    //URL url = new URL("http://api.5288z.com/weixin/musicapi.php?q="+finalTitle);
                    URL url = new URL("http://192.168.137.1:3000/lyric?id="+songid);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(60*1000);
                    connection.setReadTimeout(60*1000);
                    connection.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String s;
                    if ((s=reader.readLine())!=null){
                        s = s.replace("\\","");//去掉\\
                        try {
                            JSONObject object = new JSONObject(s);
                            JSONObject object1 = object.getJSONObject("lrc");

                            lyric = object1.getString("lyric");
                            Log.v("tagadress",lyric);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return lyric;
    }

    private static String getAdress(final String songid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    HttpURLConnection connection;
                    //URL url = new URL("http://api.5288z.com/weixin/musicapi.php?q="+finalTitle);
                    URL url = new URL("http://192.168.137.1:3000/song/url?id="+songid);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(60*1000);
                    connection.setReadTimeout(60*1000);
                    connection.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String s;
                    if ((s=reader.readLine())!=null){
                        s = s.replace("(","");//去掉(
                        s = s.replace(")","");//去掉)
                        try {
                            JSONObject object = new JSONObject(s);
                            JSONArray object1 = object.getJSONArray("data");
                            JSONObject sUrl = object1.getJSONObject(0);
                            songpath = sUrl.getString("url");
                            Log.v("tagadress",songpath);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return songpath;
    }




}


