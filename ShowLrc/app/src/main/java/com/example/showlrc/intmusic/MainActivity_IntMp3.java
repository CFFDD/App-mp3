package com.example.showlrc.intmusic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.support.annotation.RequiresApi;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.showlrc.R;
import com.example.showlrc.showlrc.MusicActivity;
import com.example.showlrc.songlistdetails.songlistdetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity_IntMp3 extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity_IntMp3";
    private static final int CHANGE_UI = 0;
    private static String re = null;

    public String loveid;       //收藏列表id
    public String cookie;       //动作令牌

    //    private static DiscoverySession handler;
    ImageView nextIv,playIv,lastIv,albumIv;
    Button select,lrc;
    TextView singerTv,songTv;
    EditText keyword;
    RecyclerView musicRv;
    //    数据源
    static List<IntMusicBean> mDatas;
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
        getsome_Data();
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

    private void getsome_Data() {
        Intent intent = getIntent();            //通过getsome_Data()方法实现intent信息的获取

        cookie = intent.getStringExtra("cookie");
        loveid = intent.getStringExtra("loveid");
    }

    private void setEventListener() {
        /* 设置每一项的点击事件*/
        adapter.setOnItemClickListener(new IntMusicAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(View view, int position) {
                Log.i(TAG,"*****************setEventListener***************");
                Log.i(TAG,"用户点击了一项Item,调用了setEventListener");
                currnetPlayPosition = position;
                IntMusicBean musicBean = mDatas.get(position);
                Log.i(TAG,"将position给Song musicBean");
                Log.i(TAG,"mData=="+mDatas);
                Log.i(TAG,"musicBean.path=="+musicBean.path);
                Log.i(TAG,"musicBean.length()=="+musicBean.length);
                if(musicBean.path.trim() == "null") {
                    Log.i(TAG,"该音乐暂无数据源");
                    return;
                }else{
                    Log.i(TAG,"音乐源存在，开始装载");
                    playMusicInMusicBean(musicBean);
                    Log.i(TAG,"musicBean已装入playMusicInMusicBean中");
                    Log.i(TAG,"musicBean="+musicBean);
                }

            }

            @Override
            public void onItemClick(View v, IntMusicAdapter.ViewName practise, int position) {

                Toast.makeText(MainActivity_IntMp3.this, "hi   我是love的点击事件", Toast.LENGTH_LONG).show();
                //定义一个新的对话框对象
                AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(MainActivity_IntMp3.this);
                //设置对话框提示内容
                alertdialogbuilder.setMessage("要收藏歌曲  "+mDatas.get(position).getSongname()+"  吗");
                currnetPlayPosition =position;
                //定义对话框2个按钮标题及接受事件的函数
                alertdialogbuilder.setPositiveButton("是的",click1);
                alertdialogbuilder.setNegativeButton("取消",click2);
                //创建并显示对话框
                AlertDialog alertdialog1=alertdialogbuilder.create();
                alertdialog1.show();
            }
        });
    }

    private DialogInterface.OnClickListener click2=new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface arg0,int arg1)
        {
            //当按钮click2被按下时则取消操作
            arg0.cancel();
        }
    };


    private DialogInterface.OnClickListener click1=new DialogInterface.OnClickListener()
    {
        //使用该标记是为了增强程序在编译时候的检查，如果该方法并不是一个覆盖父类的方法，在编译时编译器就会报告错误。
        @Override

        public void onClick(DialogInterface arg0,int arg1)
        {
            re =null;
            Toast.makeText(MainActivity_IntMp3.this, "稍等，收藏操作进行中", Toast.LENGTH_LONG).show();
            Log.i(TAG,"love  songname=="+mDatas.get(currnetPlayPosition).getSongname());
            int timestamp=(int) (Math.random()*900+100);

            re = lovesong(loveid,mDatas.get(currnetPlayPosition).getSongid(),timestamp,cookie);        //删除
            if(re == "成功删除"){
                Toast.makeText(MainActivity_IntMp3.this, "已收藏，可前往 我的收藏 页面查看", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(MainActivity_IntMp3.this, re, Toast.LENGTH_LONG).show();
            }


        }
    };



    public void playMusicInMusicBean(IntMusicBean musicBean) {
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
            Log.i(TAG,"已暂停音乐");
            //playIv.setImageResource(R.mipmap.icon_play);
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
        musicRv = findViewById(R.id.local_music_rv);
        keyword = findViewById(R.id.keywords);
        select = findViewById(R.id.select);
        lrc = findViewById(R.id.lrc);

        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);

        select.setOnClickListener(this);
        lrc.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.local_music_bottom_iv_last:       //上一首
                if (currnetPlayPosition ==0) {
                    Toast.makeText(this,"已经是第一首了，没有上一曲！", Toast.LENGTH_SHORT).show();
                    return;
                }
                currnetPlayPosition = currnetPlayPosition-1;
                IntMusicBean lastBean = mDatas.get(currnetPlayPosition);
                if(lastBean.path==null){
                    Toast.makeText(this,"该音乐暂无播放源", Toast.LENGTH_SHORT).show();}
                else{ playMusicInMusicBean(lastBean);}
                break;
            case R.id.local_music_bottom_iv_next:       //下一首
                if (currnetPlayPosition ==mDatas.size()-1) {
                    Toast.makeText(this,"已经是最后一首了，没有下一曲！", Toast.LENGTH_SHORT).show();
                    return;
                }
                currnetPlayPosition = currnetPlayPosition+1;
                IntMusicBean nextBean = mDatas.get(currnetPlayPosition);
                if(nextBean.path==null){
                    Toast.makeText(this,"该音乐暂无播放源", Toast.LENGTH_SHORT).show();}
                else{ playMusicInMusicBean(nextBean);}
                break;
            case R.id.local_music_bottom_iv_play:       //播放与暂停
                if (currnetPlayPosition == -1) {
//                    并没有选中要播放的音乐
                    Toast.makeText(this,"请选择想要播放的音乐", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mDatas.get(currnetPlayPosition).path==null) {
                    Toast.makeText(this,"该音乐暂无播放源", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (player2.isPlaying()) {
//                    此时处于播放状态，需要暂停音乐
                    pauseMusic();
                    playIv.setImageResource(R.mipmap.icon_play);
                }else {
//                    此时没有播放音乐，点击开始播放音乐
                    playMusic();
                    playIv.setImageResource(R.mipmap.icon_pause);
                }
                break;
            case R.id.select:   //搜索
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

            case R.id.lrc:      //歌词-进度条
                if(player2 ==null){
                    Toast.makeText(this,"请先选择一首歌曲", Toast.LENGTH_SHORT).show();
                }else{
                        //先暂停音乐,获取当前播放进度
                    pauseMusic();
                        //              跳转至歌词页面
                    Intent intent = new Intent();
                    intent.setClass(MainActivity_IntMp3.this, MusicActivity.class);

                    //把数据封装至bundle对象中
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",currnetPlayPosition);
                    bundle.putInt("PausePosition",currentPausePositionInSong);
                    Log.i(TAG,"PausePosition===="+currentPausePositionInSong);
                    //Log.i(TAG,"mDatas===="+mDatas);
                    bundle.putSerializable("music", (Serializable) mDatas);
                    //Log.i(TAG,"(Serializable) mDatas===="+(Serializable) mDatas);

                    //把bundle对象封装至intent对象中
                    intent.putExtras(bundle);

                    Log.i(TAG,"已将对象mData封装入intent");

                    startActivity(intent);
                    Log.i(TAG,"执行跳转");
                }
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
                    URL url = new URL("http://192.168.1.108:3000/search?keywords="+key+"&limit=20");
                    //URL url = new URL("http://192.168.137.1:3000/search?keywords="+key+"&limit=3");
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

    public static void doJson(String json, List mDatas){


        JSONObject jsonObject = null;
        JSONObject result = null;

        try {
            Log.i(TAG,"json");
            jsonObject = new JSONObject(json);
            result = new JSONObject(jsonObject.getString("result"));
            JSONArray songs_array = new JSONArray(result.getString("songs"));
            Log.i(TAG,"songs");
            int idbyme = 1 ;
            for (int i=0;i<songs_array.length();i++){
                JSONObject songs = songs_array.getJSONObject(i);
                Log.i(TAG,"songs_array.getJSONObject");
                String songid = songs.getString("id");
                String songname =songs.getString("name");
                int length = songs.getInt("duration");
                //前往下一层 获取歌手数据
                JSONArray singer = new JSONArray(songs.getString("artists"));
                JSONObject artistneme=new JSONObject(singer.getString(0));
                //  ↑  获取数组0号元素作为对象，这里的0号元素是由在目标API中，目标数据在artists数组中的位置确定的
                String singerid = artistneme.getString("id");
                String singername = artistneme.getString("name");


                String path = getAdress(songid);
//
//                String lyric = getLyric(songid);
//
                IntMusicBean intmusic = new IntMusicBean(idbyme,songid,songname,singerid,singername,path,length);
//                IntMusicBean intmusic = new IntMusicBean(idbyme,songid,songname,singerid,singername,path,lyric,length);
                Log.i(TAG,"song.idbyme="+intmusic.idbyme);
                Log.i(TAG,"song.songid="+intmusic.songid);
                Log.i(TAG,"song.songname="+intmusic.songname);
                Log.i(TAG,"song.length="+intmusic.length);
                Log.i(TAG,"song.singerid="+intmusic.singerid);
                Log.i(TAG,"song.singername="+intmusic.singername);
                Log.i(TAG,"**************开始获取歌曲路径+歌词路径****************");
                Log.i(TAG,"song.path="+intmusic.path);
                Log.i(TAG,"song.lyric="+intmusic.lyric);
                Log.i(TAG,"!!!!!!!!!!!!!!     获取结束     !!!!!!!!!!!!!!!");
                Log.i(TAG,"mDatas==="+ mDatas);
                mDatas.add(intmusic);
                Log.i(TAG,"mDatas==="+ mDatas);
                idbyme++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"即将返回");

    }

/*

    private static String getLyric(final String songid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    HttpURLConnection connection;
                    //URL url = new URL("http://api.5288z.com/weixin/musicapi.php?q="+finalTitle);
                    URL url = new URL("http://192.168.1.108:3000/lyric?id="+songid);        //本地IP经常变动，记得每次测试前先看看IP
                    //URL url = new URL("http://192.168.137.1:3000/lyric?id="+songid);
                    Log.i(TAG,"开始建立关于歌词的连接");
                    Log.i(TAG,"url==="+url);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(60 * 1000);
                    connection.setReadTimeout(60 * 1000);
                    connection.connect();
                    Log.i(TAG,"连接上了");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String s;
                    s=reader.readLine();
                    Log.i(TAG,"S>>>>>>"+s.substring(0,10));
                    if (s != null){

                        Log.i(TAG,"S>>>>>>"+s.substring(0,10));         //截取S的前10个字符
                        //s = s.replace("\\","");//去掉\\
                        try {
                            Log.i(TAG,"开始try");                     //开始try
                            JSONObject object = new JSONObject(s);
                            JSONObject object1 = object.getJSONObject("lrc");
                            lyric = object1.getString("lyric");
                            Log.v("tagadress",lyric);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Log.i(TAG,"噢，这个S是空的");
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


 */
    private static String getAdress(final String songid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    HttpURLConnection connection;
                    //URL url = new URL("http://api.5288z.com/weixin/musicapi.php?q="+finalTitle);
                    URL url = new URL("http://192.168.1.108:3000/song/url?id="+songid);
                    //URL url = new URL("http://192.168.137.1:3000/song/url?id="+songid);
                    Log.i(TAG,"开始建立关于音乐地址的连接");
                    Log.i(TAG,"url==="+url);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(60*1000);
                    connection.setReadTimeout(60*1000);
                    connection.connect();
                    Log.i(TAG,"音乐地址资源已链接");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String s;
                    s=reader.readLine();
                    Log.i(TAG,"S>>>>>>>"+s.substring(0,10));
                    if (s != null){
                        Log.i(TAG,"S>>>>>>"+s.substring(0,10));
                        s = s.replace("(","");//去掉(
                        s = s.replace(")","");//去掉)
                        try {
                            Log.i(TAG,"开始try");
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


    private static String lovesong(final String listid,final String songid,final int timestamp,final String cookie) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int timestamp=(int) (Math.random()*900+100);
                    //int m=timestamp+1;      //+"&timestamp="+m        //+"&cookie="+cookie
                    HttpURLConnection connection;
                    //URL url = new URL("http://api.5288z.com/weixin/musicapi.php?q="+finalTitle);
                    URL url = new URL("http://192.168.1.108:3000/playlist/tracks?"+"op=add&pid="+listid
                            +"&tracks=" + songid +"&timestamp="+timestamp);        //本地IP经常变动，记得每次测试前先看看IP
                    Log.i(TAG, "开始建立关于收藏歌曲的连接");
                    Log.i(TAG, "url===" + url);
                    connection = (HttpURLConnection) url.openConnection();
                    //直接加cookie在url后似乎无法读取输入流，会抛异常“java.io.FileNotFoundException”
                    //解决办法：https://blog.csdn.net/kimqcn4/article/details/52473085
                    connection.setRequestProperty("Cookie", cookie);
                    Log.i(TAG, "Cookie===" + cookie);
                    connection.setConnectTimeout(60 * 1000);
                    connection.setReadTimeout(60 * 1000);
                    connection.connect();
                    Log.i(TAG, "连接上了");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Log.i(TAG, "reader====" + reader);
                    String s;
                    s = reader.readLine();
                    //Log.i(TAG, "S>>>>>>" + s);
                    if (s != null) {
                        Log.i(TAG, "S>>>>>>" + s);
                        //s = s.replace("\\","");//去掉\\
                        try {
                            Log.i(TAG, "开始try");                     //开始try
                            JSONObject object = new JSONObject(s);
                            String string1 = object.getString("code");
                            int code = Integer.valueOf(string1);
                            Log.i(TAG, "code====" + code);
                            if (code == 200) {
                                re ="成功收藏";
                            } else {
                                re = object.getString("message");
                                Log.i(TAG, "message====" + re);
                                if(re == null ){re = "收藏失败，请重试";}
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i(TAG, "这个S是空的");
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
        return re;

    }



}


