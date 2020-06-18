package com.animee.localmusic;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//import com.example.lenovo.a06266.Song;    //这个在原文里是有的但是在这里报错了，就先注释掉

/**
 * Created by Lenovo on 2019/6/26.
 */

public class MusicUtils {
    private static final String TAG = "MusicUtils";
    private static String songpath;
    private static String lyric;



    /**
 * 扫描系统里面的音频文件，返回一个list集合
 */



    public static void query2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Log.i(TAG, "开始建立连接");
                    HttpURLConnection connection;
                    //String finalTitle = URLEncoder.encode(title,"utf-8");
                    URL url = new URL("http://192.168.1.108:3000/search?keywords=%20%E6%B5%B7%E9%98%94%E5%A4%A9%E7%A9%BA&limit=3");
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




    public static void doJson(String json){

        Song song1 = null;
        String id="";
        JSONObject jsonObject = null;
        JSONObject result = null;
        List<Song>mDatas;
        try {
            Log.i(TAG,"json");
            jsonObject = new JSONObject(json);
            result = new JSONObject(jsonObject.getString("result"));
            JSONArray songs_array = new JSONArray(result.getString("songs"));
            Log.i(TAG,"songs");
            mDatas = new ArrayList<>();
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

                mDatas.add(song);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"即将返回");

    }




    public static String getAdress(final String songid){
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



    public static String getLyric(final String songid){
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

}
