package com.example.musicfromapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class select extends AppCompatActivity {
    private static final String TAG ="getUrl" ;
    Song m=new Song();
    public TextView tID,tUrl;
    public EditText esongname;
    public Button bselect;
    static public String a=null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geturl);

        findViews();// 各组件
    }

    private void findViews() {
        tID=findViewById(R.id.tId);
        tUrl=findViewById(R.id.tUrl);
        esongname=findViewById(R.id.esongname);
        bselect=findViewById(R.id.select);

        bselect.setOnClickListener(new select.MyClick3());
    }

    class MyClick3 implements View.OnClickListener{
        public void onClick(View v){
            String name = esongname.getText().toString();
            if (name.length() == 0) {
                Toast.makeText(select.this, "请输入歌曲名称", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(select.this,"已开始查询，请耐心等待",Toast.LENGTH_SHORT).show();
                query2();

                Log.i(TAG,"从query2返回");
                //tUrl.setText(m.song);
                //Log.i(TAG,"成功setText");
            }


        }
    }


    private void query2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "开始建立连接");
                    HttpURLConnection connection;
                    //String finalTitle = URLEncoder.encode(title,"utf-8");
                    URL url = new URL("http://192.168.137.1:3000/search?keywords=%20%E6%B5%B7%E9%98%94%E5%A4%A9%E7%A9%BA");
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
                        doJson(s);
                        Log.i(TAG, "得到数据");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "即将从query2返回");
            }
        }).start();
        Log.i(TAG, "退出子线程");
    }




    public void doJson(String json){
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
                //JSONObject singer=new JSONObject(songs.getString("artists"));
                JSONArray singer = new JSONArray(songs.getString("artists"));
                JSONObject artistneme=new JSONObject(singer.getString(0));
                //  ↑  获取数组0号元素作为对象，这里的0号元素是由在目标API中，目标数据在artists数组中的位置确定的
                String singername = artistneme.getString("name");
                Log.i(TAG,"songid="+songid);
                Log.i(TAG,"songname="+songname);
                Log.i(TAG,"singername="+singername);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"即将返回");

    }




}
