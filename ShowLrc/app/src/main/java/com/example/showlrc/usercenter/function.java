package com.example.showlrc.usercenter;

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

import com.example.showlrc.R;
import com.example.showlrc.intmusic.IntMusicBean;
import com.example.showlrc.intmusic.MainActivity_IntMp3;
import com.example.showlrc.localmusic.MainActivity;
import com.example.showlrc.songlistdetails.songlistdetail;
import com.example.showlrc.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class function extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "function";
    private static final int CHANGE_UI = 0;


    public int uid;
    Bitmap user_avatar;
    public String cookie;

    ImageView avatarIV;
    TextView nikenameTV,localTV,likeTV,my_likelistTV,collect_listTV;
    ImageButton selectmusic_ibtn,local_ibtn,like_ibtn;
    RecyclerView my_likelistRV,collect_listRV;

    //设置数据源
    static List<MusiclistBean> mDatas;
    static List<MusiclistBean_collect> m2Datas;
    static List<LovesonglistBean> m3Datas;

    private static MusiclistAdapter adapter;
    private static MusiclistAdapter_collect adapter_collect;
    //当前点击位置（歌单）
    int currnetlistPosition = 0 ;


    private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case CHANGE_UI:
                    //在这里可以进行UI操作
                    adapter.notifyDataSetChanged();
                    adapter_collect.notifyDataSetChanged();
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
        get_set_user();     //用户信息显示

        mDatas = new ArrayList<>();
        m2Datas = new ArrayList<>();
        m3Datas = new ArrayList<>();
        //     创建适配器对象
        adapter = new MusiclistAdapter(function.this, mDatas);
        adapter_collect= new MusiclistAdapter_collect(function.this, m2Datas);
        Log.i(TAG,"adapter---------"+adapter);
        Log.i(TAG,"创建适配器对象");
        my_likelistRV.setAdapter(adapter);
        collect_listRV.setAdapter(adapter_collect);
        Log.i(TAG,"likelistRV.setAdapter(adapter);");
        //        设置布局管理器
        LinearLayoutManager layoutManager_my_likelistRV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        my_likelistRV.setLayoutManager(layoutManager_my_likelistRV);
        LinearLayoutManager layoutManager_collect_listRV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        collect_listRV.setLayoutManager(layoutManager_collect_listRV);

        loadlistData(uid);
        Log.i(TAG,"loadlistData()完成");
        setEventListener();
        setEventListener_collect();
    }


    private void get_set_user() {
        Intent intent = getIntent();                                                    //通过getIntent()方法实现intent信息的获取
        uid = intent.getIntExtra("uid", 0);
        Bitmap avatar = intent.getParcelableExtra("avatar");
        String nikename = intent.getStringExtra("nikename");
        cookie = intent.getStringExtra("cookie");
        avatarIV.setImageBitmap(avatar);            //显示用户头像
        nikenameTV.setText(nikename);               //显示用户昵称
    }

    private void bindID() {
        avatarIV = findViewById(R.id.avatarIV);                     //用户头像
        nikenameTV = findViewById(R.id.nicknameTV);
        localTV = findViewById(R.id.localTV);
        likeTV = findViewById(R.id.likeTV);
        selectmusic_ibtn = findViewById(R.id.selectmusic_ibtn);     //搜索按钮
        local_ibtn = findViewById(R.id.local_ibtn);                 //本地歌曲按钮
        like_ibtn = findViewById(R.id.like_ibtn);                   //歌曲收藏列表按钮
        my_likelistRV = findViewById(R.id.my_likelistRV);           //自建歌单展示
        collect_listRV= findViewById(R.id.collect_listRV);          //收藏歌单展示
        my_likelistTV= findViewById(R.id.my_likelistTV);
        collect_listTV= findViewById(R.id.collect_listTV);

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
                Intent intent4 = new Intent();
                intent4.setClass(function.this, songlistdetail.class);
                Bundle bundle =new Bundle();
                bundle.putString("listid",listcBean.listid);
                bundle.putString("listname",listcBean.listname);
                bundle.putString("creatorname",listcBean.creatorname);
                //压缩图片，如果传输的bitmap大于40KB会出现“!!! FAILED BINDER TRANSACTION !!!”错误
                Bitmap a =compressImage(listcBean.coverImg);    //压缩到40kb以下
                bundle.putParcelable("listbmp",a);
//                bundle.putInt("position", currnetlistPosition);        //当前歌单位置
//                bundle.putSerializable("aboutlist", (Serializable) mDatas);     //当前用户歌单列表相关数据
                bundle.putString("cookie",cookie);      //传递令牌
                bundle.putString("loveid",m3Datas.get(0).listid);        //数据源中第一个对象是收藏列表
                Log.i(TAG,"bundle=="+bundle);
                Log.i(TAG,"装载完成准备跳转");
                intent4.putExtras(bundle);
                startActivity(intent4);
            }
        });
    }
    private void setEventListener_collect() {
        /* 设置每一项的点击事件*/
        adapter_collect.setOnItemClickListener(new MusiclistAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(View view, int position) {
                Log.i(TAG,"*****************setEventListener***************");
                Log.i(TAG,"用户点击了一项Item,调用了setEventListener");
                currnetlistPosition = position;
                MusiclistBean_collect listcBean = m2Datas.get(position);
                Intent intent5 = new Intent();
                intent5.setClass(function.this, songlistdetail.class);
                Bundle bundle =new Bundle();
                bundle.putString("listid",listcBean.listid);
                bundle.putString("listname",listcBean.listname);
                bundle.putString("creatorname",listcBean.creatorname);
                //压缩图片，如果传输的bitmap大于40KB会出现“!!! FAILED BINDER TRANSACTION !!!”错误
                Bitmap a =compressImage(listcBean.coverImg);    //压缩到40kb以下
                bundle.putParcelable("listbmp",a);
//                bundle.putInt("position", currnetlistPosition);        //当前歌单位置
//                bundle.putSerializable("aboutlist", (Serializable) mDatas);     //当前用户歌单列表相关数据
                bundle.putString("cookie",cookie);      //传递令牌
                bundle.putString("loveid",m3Datas.get(0).listid);        //数据源中第一个对象是收藏列表
                intent5.putExtras(bundle);
                Log.i(TAG,"bundle=="+bundle);
                Log.i(TAG,"装载完成准备跳转");
                startActivity(intent5);
            }
        });


    }



    private static void loadlistData(final int uid) {
        /* 加载用户歌单到集合当中*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int m=(int) (Math.random()*900+100);
                    Log.i(TAG,"loadlistData()开始");
                    Log.i(TAG, "开始建立连接");
                    HttpURLConnection connection;
                    //String finalTitle = URLEncoder.encode(title,"utf-8");
                    URL url = new URL("http://192.168.1.108:3000/user/playlist?uid="+uid+"&timestamp="+m);
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
                        doJson(s,mDatas,m2Datas,m3Datas);
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

    public static void doJson(String json, List mDatas,List m2Datas,List m3Datas){


        JSONObject jsonObject = null;
        JSONArray playlist = null;

        try {
            Log.i(TAG,"json");
            jsonObject = new JSONObject(json);
            playlist = new JSONArray(jsonObject.getString("playlist"));
            Log.i(TAG,"playlist");


            for (int i=0;i<playlist.length();i++){          //用户歌单中第一个歌单一般是用户收藏的音乐，而不是用户自建歌单
                JSONObject lists = playlist.getJSONObject(i);
                Log.i(TAG,"playlist.getJSONObject");

                String listid = lists.getString("id");
                String listname = lists.getString("name");
                String count = lists.getString("trackCount")+"首";
                String coverurl =lists.getString("coverImgUrl");
                Bitmap coverImg = getBitmap(coverurl+"?param=200y200");     //对图片增加大小控制，防止在页面间传输时因图片过大而报错
                Log.i(TAG,"get bitmap");

                //前往下一层 获取创建者
                JSONObject creator = new JSONObject(lists.getString("creator"));
                //  ↑  获取数组0号元素作为对象，这里的0号元素是由在目标API中，目标数据在artists数组中的位置确定的
                boolean defaultAvatar = creator.getBoolean("defaultAvatar");
                Log.i(TAG,"defaultAvatar=="+defaultAvatar);
                String creatorid = creator.getString("userId");             //创建者id
                String creatorname = creator.getString("nickname");         //创建者名称

                Log.i(TAG,"musiclist.ifcreator="+defaultAvatar);       //是否创建者
                Log.i(TAG,"musiclist.listid="+listid);
                Log.i(TAG,"musiclist.listname="+listname);
                Log.i(TAG,"musiclist.count"+count);
                Log.i(TAG,"musiclist.coverImg="+coverImg);        //歌单封面
                Log.i(TAG,"!!!!!!!!!!!!!!     获取结束     !!!!!!!!!!!!!!!");
                if(i==0){
                    LovesonglistBean   musiclist = new LovesonglistBean(defaultAvatar,listid,listname,count, coverImg,creatorid,creatorname);
                    Log.i(TAG,"mDatas==="+ mDatas);
                    m3Datas.add(musiclist);
                    Log.i(TAG,"mDatas==="+ mDatas);
                } else if (defaultAvatar ==true) {
                    MusiclistBean musiclist = new MusiclistBean(defaultAvatar,listid,listname,count,coverImg,creatorid,creatorname);
                    Log.i(TAG,"mDatas==="+ mDatas);
                    mDatas.add(musiclist);
                    Log.i(TAG,"mDatas==="+ mDatas);
                }
                else {
                    MusiclistBean_collect musiclist = new MusiclistBean_collect
                            (defaultAvatar, listid,listname,count,coverImg, creatorid,creatorname);
                    Log.i(TAG,"mDatas==="+ mDatas);
                    m2Datas.add(musiclist);
                    Log.i(TAG,"mDatas==="+ mDatas);
                }

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
                intent.setClass(function.this, MainActivity_IntMp3.class);
                startActivity(intent);
                break;
            case R.id.local_ibtn:
                Intent intent2 = new Intent();
                intent2.setClass(function.this, MainActivity.class);
                startActivity(intent2);
                break;
            case R.id.like_ibtn:
                LovesonglistBean listcBean = m3Datas.get(0);

                Intent intent3 = new Intent();
                intent3.setClass(function.this, songlistdetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("listid",listcBean.listid);
                bundle.putString("listname",listcBean.listname);
                bundle.putString("creatorname",listcBean.creatorname);
                //压缩图片，如果传输的bitmap大于40KB会出现“!!! FAILED BINDER TRANSACTION !!!”错误
                Bitmap a =compressImage(listcBean.coverImg);    //压缩到40kb以下
                bundle.putParcelable("listbmp",a);
                //bundle.putInt("position", 0);
                //bundle.putSerializable("aboutlist", (Serializable) mDatas);
                Log.i(TAG,"lovelist name=="+ m3Datas.get(0).listname);
                bundle.putString("cookie",cookie);      //传递令牌
                bundle.putString("loveid",mDatas.get(0).listid);        //数据源中第一个对象是收藏列表
                startActivity(intent3);
                break;
        }
    }


    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        Log.i(TAG, "compressImage。。。。。。。。。。。");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        Log.i(TAG, "baos.toByteArray().length / 1024 ==="+(baos.toByteArray().length / 1024));
        int options = 100;
        while ((baos.toByteArray().length / 1024 > 40)&& (options>=10)) {  //循环判断如果压缩后图片是否大于40kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            Log.i(TAG, "baos.toByteArray().length / 1024 ==="+(baos.toByteArray().length / 1024));
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


}
