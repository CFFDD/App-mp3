package com.example.musicfromapi;

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
import com.example.musicfromapi.data_compilation;

public class getUrl extends AppCompatActivity {
    private static final String TAG ="getUrl" ;
    data_compilation m=new data_compilation();
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

    bselect.setOnClickListener(new MyClick2());
    }

    class MyClick2 implements View.OnClickListener{
        public void onClick(View v){
            String name = esongname.getText().toString();
            if (name.length() == 0) {
                Toast.makeText(getUrl.this, "请输入歌曲名称", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getUrl.this,"已开始查询，请耐心等待",Toast.LENGTH_SHORT).show();
                query2();

                Log.i(TAG,"从query2返回url ="+m.anurl);
                tUrl.setText(m.anurl);
                Log.i(TAG,"成功setText");
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
                    URL url = new URL("http://192.168.137.1:3000/song/url?id=29017036,33894312");
                    //http://192.168.137.1:3000/song/url?id=29017036
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
                        m.anurl = doJson(s);
                        Log.i(TAG, "得到a/url =" + m.anurl);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "即将从query2返回");
            }
        }).start();
        Log.i(TAG, "退出子线程");
    }




    public String doJson(String json){
        String anUrl ="";
        JSONObject jsonObject = null;
        try {
            //去掉括号
            //json = json.replace("(","");
            //json = json.replace(")","");
            Log.i(TAG,"json");
            jsonObject = new JSONObject(json);
            JSONArray array = new JSONArray(jsonObject.getString("data"));
            Log.i(TAG,"JSONArray");
            for (int i=0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                Log.i(TAG,"array.getJSONObject");
                String songname = object.getString("url");
                Log.i(TAG,"songname");
                anUrl += songname;
            }
            Log.i(TAG,"循环结束");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"即将返回anUrl="+anUrl);
        return  anUrl;
    }



}




