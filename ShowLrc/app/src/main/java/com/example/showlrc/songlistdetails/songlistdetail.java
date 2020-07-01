package com.example.showlrc.songlistdetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.showlrc.R;
import com.example.showlrc.intmusic.IntMusicBean;
import com.example.showlrc.intmusic.MainActivity_IntMp3;
import com.example.showlrc.showlrc.MusicActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class songlistdetail extends AppCompatActivity {
    private static final String TAG = "songlistdetail";
    private static final int CHANGE_UI = 0;
    private static String re = null;

    public String listid = null;

    ImageButton back;
    TextView albumname_top,albumTV_songlist,craetorTV,craetornameTV;
    ImageView albumIV_songlist;
    RecyclerView listdetail_RV;

    //设置数据源
    static List<IntMusicBean> ldDatas;
    private static ListdetailAdapter adapter;
    //当前点击位置（歌单）
    int currnetlistPosition = 0 ;


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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songlist_details);
        bindID();
        Log.i(TAG,"bindID()完成");
        get_set_list();     //歌单信息的显示
        Log.i(TAG,"get_set_list()完成");
        ldDatas = new ArrayList<>();
        //     创建适配器对象
        adapter = new ListdetailAdapter(songlistdetail.this, ldDatas);
        Log.i(TAG,"adapter---------"+adapter);
        Log.i(TAG,"创建适配器对象");
        listdetail_RV.setAdapter(adapter);
        Log.i(TAG,"likelistRV.setAdapter(adapter);");
        //        设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listdetail_RV.setLayoutManager(layoutManager);


        loadsongData(listid);
        Log.i(TAG,"loadlistData()完成");
        setEventListener();


    }

    private void get_set_list() {

        Intent intent = getIntent();                                                    //通过getIntent()方法实现intent信息的获取
        listid = intent.getStringExtra("listid");
        String listname = intent.getStringExtra("listname");
        Log.i(TAG,"listname=="+listname);
        Bitmap corverimg = intent.getParcelableExtra("listbmp");
        String creatorname = intent.getStringExtra("creatorname");        //创建者名称
        Log.i(TAG,"从Intent获取数据完成");
        albumIV_songlist.setImageBitmap(corverimg);
        albumname_top.setText(listname);
        albumTV_songlist.setText(listname);
        craetornameTV.setText(creatorname);

    }

    private void bindID() {
        albumname_top =findViewById(R.id.albumname_top);            //顶部显示 歌单名称
        back =findViewById(R.id.ibtm_back_reset_songlist);          //返回 按钮
        albumTV_songlist =findViewById(R.id.albumTV_songlist);      //专辑名
        albumIV_songlist =findViewById(R.id.albumIV_songlist);      //专辑封面
        craetorTV =findViewById(R.id.craetorTV);                    //"创建者:"TV
        craetornameTV =findViewById(R.id.craetornameTV);            //创建者名称TV
        listdetail_RV =findViewById(R.id.listdetail_RV);            //列表内歌曲展示

    }


    private void setEventListener() {
        /* 设置每一项的点击事件*/
        adapter.setmOnItemClickListener(new ListdetailAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(View view, int position) {
                Log.i(TAG,"*****************setEventListener***************");
                Log.i(TAG,"用户点击了一项Item,调用了setEventListener");
                currnetlistPosition = position;
                IntMusicBean listcBean = ldDatas.get(position);
                Log.i(TAG,"将position 给  listcBean");
                Log.i(TAG,"mData=="+ldDatas);
                Log.i(TAG,"listcBean.songid=="+listcBean.songid);

                String path = getAdress(listcBean.songid);          //加载歌曲链接和歌词
                String lyric = getLyric(listcBean.songid);
                listcBean.path = path;
                listcBean.lyric = lyric;

               Intent intent = new Intent();
                intent.setClass(songlistdetail.this, MusicActivity.class);

                //把数据封装至bundle对象中
                Bundle bundle = new Bundle();
                bundle.putInt("position",currnetlistPosition);
//                bundle.putInt("PausePosition",currentPausePositionInSong);
//                Log.i(TAG,"PausePosition===="+currentPausePositionInSong);
                //Log.i(TAG,"mDatas===="+mDatas);
                bundle.putSerializable("music", (Serializable) ldDatas);
                //Log.i(TAG,"(Serializable) mDatas===="+(Serializable) mDatas);

                //把bundle对象封装至intent对象中
                intent.putExtras(bundle);

                Log.i(TAG,"已将对象mData封装入intent");

                startActivity(intent);
                Log.i(TAG,"执行跳转");
            }

            @Override
            public void onItemClick(View v, ListdetailAdapter.ViewName practise, int position) {
                Toast.makeText(songlistdetail.this, "hi   我是button的点击事件", Toast.LENGTH_LONG).show();
                //定义一个新的对话框对象
                AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(songlistdetail.this);
                //设置对话框提示内容
                alertdialogbuilder.setMessage("要删除歌单  "+ldDatas.get(position).getSongname()+"  吗");
                //定义对话框2个按钮标题及接受事件的函数
                alertdialogbuilder.setPositiveButton("是的",click1);
                alertdialogbuilder.setNegativeButton("取消",click2);
                //创建并显示对话框
                AlertDialog alertdialog1=alertdialogbuilder.create();
                alertdialog1.show();
            }


        });
    }

    private DialogInterface.OnClickListener click1=new DialogInterface.OnClickListener()
    {
        //使用该标记是为了增强程序在编译时候的检查，如果该方法并不是一个覆盖父类的方法，在编译时编译器就会报告错误。
        @Override

        public void onClick(DialogInterface arg0,int arg1)
        {
            Toast.makeText(songlistdetail.this, "你选择了是的", Toast.LENGTH_LONG).show();
            Log.i(TAG,"delete  songid=="+ldDatas.get(currnetlistPosition).getSongname());
            int timestamp=(int) (Math.random()*900+100);

            re = deletesong(listid,ldDatas.get(currnetlistPosition).getSongid(),timestamp);        //删除
            if(re == "成功删除"){
                ldDatas.clear();                     //清空数据
                adapter.notifyDataSetChanged();     //通知适配器更新，以待重新载入数据
                loadsongData(listid);      //开始载入数据
                setEventListener();
            }else{
                Toast.makeText(songlistdetail.this, "删除失败，请重试", Toast.LENGTH_LONG).show();
            }


        }
    };


    private static String deletesong(final String listid,final String songid,final int timestamp) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //int timestamp=(int) (Math.random()*900+100);
                    int m=timestamp+1;
                    HttpURLConnection connection;
                    //URL url = new URL("http://api.5288z.com/weixin/musicapi.php?q="+finalTitle);
                    URL url = new URL("http://192.168.1.108:3000/playlist/tracks?op=del&pid="+listid+"&tracks=" + songid +"&timestamp="+m);        //本地IP经常变动，记得每次测试前先看看IP
                    //URL url = new URL("http://192.168.137.1:3000/lyric?id="+songid);
                    Log.i(TAG, "开始建立关于删除歌单的连接");      //用用户ID可以获取用户信息
                    Log.i(TAG, "url===" + url);
                    connection = (HttpURLConnection) url.openConnection();
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
                                re ="成功删除";
                            } else {
                                re = object.getString("message");
                                Log.i(TAG, "message====" + re);
                                if(re == null ){re = "删除失败";}
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

    private DialogInterface.OnClickListener click2=new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface arg0,int arg1)
        {
            //当按钮click2被按下时则取消操作
            arg0.cancel();
        }
    };


    private static String songpath;
    private static String lyric;


    private static void loadsongData(final String listid) {
        /* 加载歌单——歌曲数据到集合当中*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int timestamp=(int) (Math.random()*900+100);
                    Log.i(TAG,"！！！！！！！！loadlistData()开始！！！！！！！！！！");
                    Log.i(TAG, "开始建立连接");
                    HttpURLConnection connection;
                    //String finalTitle = URLEncoder.encode(title,"utf-8");
                    URL url = new URL("http://192.168.1.108:3000/playlist/detail?id="+listid+"&timestamp="+timestamp);
                    //URL url = new URL("http://192.168.137.1:3000/search?keywords="+key+"&limit=3");
                    Log.i(TAG, "url=="+url);
                    Log.i(TAG, "开始建立关于获取歌单详情的连接");
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
                        doJson(s,ldDatas);
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
        JSONObject playlist = null;

        try {
            Log.i(TAG,"json");
            jsonObject = new JSONObject(json);
            playlist = jsonObject.getJSONObject("playlist");
            JSONArray tracks = new JSONArray(playlist.getString("tracks"));
            Log.i(TAG,"tracks");
            int idbyme = 1;
            for (int i=0;i<playlist.length();i++){
                JSONObject songs = tracks.getJSONObject(i);
                Log.i(TAG,"…………………………………………………………tracks.getJSONObject…………………………………………");
                String songid = songs.getString("id");
                String songname = songs.getString("name");
                //前往下一层 获取创建者
                JSONArray ar = new JSONArray(songs.getString("ar"));
                JSONObject artistneme=new JSONObject(ar.getString(0));
                //  ↑  获取数组0号元素作为对象，这里的0号元素是由在目标API中，目标数据在artists数组中的位置确定的
                String singerid = artistneme.getString("id");
                String singer = artistneme.getString("name");
                Log.i(TAG,"singer=="+singer);

                int length = songs.getInt("dt");


                IntMusicBean listdetail = new IntMusicBean(idbyme,songid,songname,singerid,singer,length);
                Log.i(TAG,"musiclist.idbyme="+listdetail.idbyme);
                Log.i(TAG,"musiclist.songid="+listdetail.songid);
                Log.i(TAG,"musiclist.songname="+listdetail.songname);
                Log.i(TAG,"musiclist.singer"+listdetail.singerid);
                Log.i(TAG,"musiclist.singer"+listdetail.singername);
                Log.i(TAG,"musiclist.path"+listdetail.path);
                Log.i(TAG,"musiclist.lyric"+listdetail.lyric);
                Log.i(TAG,"musiclist.length"+listdetail.length);
                Log.i(TAG,"mDatas==="+ mDatas);
                mDatas.add(listdetail);
                Log.i(TAG,"mDatas==="+ mDatas);
                Log.i(TAG,"…………………………………    第一个对象 获取结束   ……………………………");
                idbyme++;
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








}
