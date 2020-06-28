package com.example.login_formusic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class function extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "function";
    private static final int CHANGE_UI = 0;

    public int uid;
    Bitmap user_avatar;

    ImageView avatarIV;
    TextView nikenameTV,localTV,likeTV;
    ImageButton selectmusic_ibtn,local_ibtn,like_ibtn;
    RecyclerView likelistRV;

    //设置数据源
    static List<MusiclistBean> mDatas;
    private static MusiclistAdapter adapter;
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
        setContentView(R.layout.function);


        bindID();
        get_set_user();

        mDatas = new ArrayList<>();
        //     创建适配器对象
        adapter = new MusiclistAdapter(function.this, mDatas);
        Log.i(TAG,"adapter---------"+adapter);
        Log.i(TAG,"创建适配器对象");
        likelistRV.setAdapter(adapter);
        Log.i(TAG,"likelistRV.setAdapter(adapter);");
        //        设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        likelistRV.setLayoutManager(layoutManager);


        loadlistData(uid);
        Log.i(TAG,"loadlistData()完成");
        setEventListener();

    }

    private void get_set_user() {
        Intent intent = getIntent();                                                    //通过getIntent()方法实现intent信息的获取
        uid = intent.getIntExtra("uid", 0);
        Bitmap avatar = intent.getParcelableExtra("avatar");
        String nikename = intent.getStringExtra("nikename");
        avatarIV.setImageBitmap(avatar);
        nikenameTV.setText(nikename);               //显示用户昵称
    }

    private void bindID() {
        avatarIV = findViewById(R.id.avatarIV);
        nikenameTV = findViewById(R.id.nicknameTV);
        localTV = findViewById(R.id.localTV);
        likeTV = findViewById(R.id.likeTV);
        selectmusic_ibtn = findViewById(R.id.selectmusic_ibtn);
        local_ibtn = findViewById(R.id.local_ibtn);
        like_ibtn = findViewById(R.id.like_ibtn);
        likelistRV = findViewById(R.id.likelistRV);

        selectmusic_ibtn.setOnClickListener(this);
        local_ibtn.setOnClickListener(this);
        like_ibtn.setOnClickListener(this);
    }

    private void setEventListener() {
        /* 设置每一项的点击事件*/
        adapter.setOnItemClickListener(new MusiclistAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(View view, int position) {
                Log.i(TAG,"*****************setEventListener***************");
                Log.i(TAG,"用户点击了一项Item,调用了setEventListener");
                currnetlistPosition = position;
                MusiclistBean listcBean = mDatas.get(position);
                Log.i(TAG,"将position 给  listcBean");
                Log.i(TAG,"mData=="+mDatas);
                Log.i(TAG,"listcBean.listid=="+listcBean.listid);

                Intent intent4 = new Intent();
                intent4.setClass(function.this, test.class);
                Bundle bundle =new Bundle();
                bundle.putInt("listid",listcBean.listid);
                Log.i(TAG,"listcBean.listid=="+listcBean.listid);
                intent4.putExtras(bundle);
                startActivity(intent4);
            }
        });
    }


    private static void loadlistData(final int uid) {
        /* 加载用户歌单到集合当中*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG,"loadlistData()开始");
                    Log.i(TAG, "开始建立连接");
                    HttpURLConnection connection;
                    //String finalTitle = URLEncoder.encode(title,"utf-8");
                    URL url = new URL("http://192.168.1.108:3000/user/playlist?uid="+uid);
                    //URL url = new URL("http://192.168.137.1:3000/search?keywords="+key+"&limit=3");
                    Log.i(TAG, "url=="+url);
                    Log.i(TAG, "开始建立关于获取用户歌单的连接");
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
        JSONArray playlist = null;

        try {
            Log.i(TAG,"json");
            jsonObject = new JSONObject(json);
            playlist = new JSONArray(jsonObject.getString("playlist"));
            Log.i(TAG,"playlist");
            for (int i=1;i<playlist.length();i++){          //用户歌单中第一个歌单一般是用户收藏的音乐，而不是用户自建歌单
                JSONObject lists = playlist.getJSONObject(i);
                Log.i(TAG,"playlist.getJSONObject");
                int listid = lists.getInt("id");
                String listname = lists.getString("name");
                String count = lists.getString("trackCount")+"首";
                String coverurl =lists.getString("coverImgUrl");
                Bitmap coverImg = getBitmap(coverurl);
                Log.i(TAG,"get bitmap");

                //前往下一层 获取创建者
                JSONObject creator = new JSONObject(lists.getString("creator"));
                //  ↑  获取数组0号元素作为对象，这里的0号元素是由在目标API中，目标数据在artists数组中的位置确定的
                boolean defaultAvatar = creator.getBoolean("defaultAvatar");
                Log.i(TAG,"defaultAvatar=="+defaultAvatar);


                MusiclistBean musiclist = new MusiclistBean(defaultAvatar,listid,listname,count,coverImg);
                Log.i(TAG,"musiclist.ifcreator="+musiclist.ifcrator);
                Log.i(TAG,"musiclist.listid="+musiclist.listid);
                Log.i(TAG,"musiclist.listname="+musiclist.listname);
                Log.i(TAG,"musiclist.count"+musiclist.count);
                Log.i(TAG,"musiclist.coverImg="+musiclist.coverImg);
                Log.i(TAG,"!!!!!!!!!!!!!!     获取结束     !!!!!!!!!!!!!!!");
                Log.i(TAG,"mDatas==="+ mDatas);
                mDatas.add(musiclist);
                Log.i(TAG,"mDatas==="+ mDatas);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"即将返回");

    }

    private static Bitmap getBitmap(String coverurl) {
        try {
            Log.e("coverurl==",coverurl);
            URL url = new URL(coverurl);
            Log.i(TAG, "coverurl=="+coverurl);
            Log.i(TAG, "开始建立关于获取图片数据的连接");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.i(TAG,"i have a Bitmap"+ myBitmap);
            //input.close();        原文里没有这一句
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectmusic_ibtn:
                Intent intent = new Intent();
                intent.setClass(function.this, test.class);
                startActivity(intent);
                break;
            case R.id.local_ibtn:
                Intent intent2 = new Intent();
                intent2.setClass(function.this, test.class);
                startActivity(intent2);
                break;
            case R.id.like_ibtn:
                Intent intent3 = new Intent();
                intent3.setClass(function.this, test.class);
                startActivity(intent3);
                break;
        }
    }
}
