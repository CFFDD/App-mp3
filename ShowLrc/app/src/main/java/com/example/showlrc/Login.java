package com.example.showlrc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.showlrc.usercenter.function;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Login extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private static String re=null;
    private static String[] uData = new String[3];
    EditText phoneTV,pwdTV;
    Button loginbtn,registerbtn,resetbtn;

    String user = null;
    String pwd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        bindID();
    }

    private void bindID() {
        phoneTV = findViewById(R.id.phonenumberTV);
        pwdTV = findViewById(R.id.passwordTV);
        loginbtn = findViewById(R.id.loginbtn);
        registerbtn = findViewById(R.id.registerbtn);
        resetbtn = findViewById(R.id.resetbtn);
        loginbtn.setOnClickListener(this);
        registerbtn.setOnClickListener(this);
        resetbtn.setOnClickListener(this);


    }
    private void SetString() {
        user = phoneTV.getText().toString().trim();
        pwd = pwdTV.getText().toString().trim();
    }


    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.loginbtn:
                SetString();
                String restring = login(user,pwd);
                Log.i(TAG, "restring==="+restring);
                if (user.length() == 11) {
                    if(restring == "成功登陆"){
                        Log.i(TAG, "if成功登陆");
                        ReadytoFunction();
                    }else{
                        Log.i(TAG, "if失败,msg==="+restring);
                        if (re == null) {
                            Toast.makeText(Login.this, "手机号不正确", Toast.LENGTH_LONG).show();
                            //若是密码错误，可以从API读取到message
                        }else{
                            Toast.makeText(Login.this, restring, Toast.LENGTH_LONG).show();
                        }
                    }
                } else if(user.length() == 0){
                    Toast.makeText(Login.this, "请输入手机号", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Login.this, "手机号格式不正确", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.registerbtn:
                Intent intent2 = new Intent();
                intent2.setClass(Login.this, register.class);
                startActivity(intent2);
                break;
            case R.id.resetbtn:
                Intent intent3 = new Intent();
                intent3.setClass(Login.this, resetpwd.class);
                startActivity(intent3);
                break;

        }
    }

    private void ReadytoFunction() {
            UserBean userBean =new UserBean();
            getuserdata(user,pwd,userBean);
            Intent intent = new Intent();
            intent.setClass(Login.this, function.class);
            Bundle bundle = new Bundle();
            bundle.putInt("uid",userBean.uid);        //用户ID
            bundle.putParcelable("avatar",userBean.avatar);              //用户头像
            bundle.putString("nikename",userBean.nikename);           //用户昵称
            intent.putExtras(bundle);
            Log.i(TAG, "准备进行页面跳转");
            startActivity(intent);

    }

    private static void getuserdata(final String user, final String pwd,final UserBean userBean ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    HttpURLConnection connection;
                    //URL url = new URL("http://api.5288z.com/weixin/musicapi.php?q="+finalTitle);
                    URL url = new URL("http://192.168.1.108:3000/login/cellphone?phone=" + user + "&password=" + pwd);        //本地IP经常变动，记得每次测试前先看看IP
                    //URL url = new URL("http://192.168.137.1:3000/lyric?id="+songid);
                    Log.i(TAG, "开始建立关于获取用户数据的连接");      //用用户ID可以获取用户信息
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
                        //Log.i(TAG, "S>>>>>>" + s);         //截取S的前10个字符
                        //s = s.replace("\\","");//去掉\\
                        try {
                            Log.i(TAG, "开始try");                     //开始try
                            JSONObject object = new JSONObject(s);
                            String string1 = object.getString("code");
                            int code = Integer.valueOf(string1);
                            Log.i(TAG, "code====" + code);
                            JSONObject profile =object.getJSONObject("profile");

                            userBean.uid = profile.getInt("userId");            //用户ID
                            String avatarUrl =profile.getString("avatarUrl");   //用户头像 图片URL
                            userBean.nikename=profile.getString("nickname");   //用户昵称
                            userBean.avatar = getBitmap(avatarUrl);


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

    private static String login(final String user, final String pwd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    HttpURLConnection connection;
                    //URL url = new URL("http://api.5288z.com/weixin/musicapi.php?q="+finalTitle);
                    URL url = new URL("http://192.168.1.108:3000/login/cellphone?phone=" + user + "&password=" + pwd);        //本地IP经常变动，记得每次测试前先看看IP
                    //URL url = new URL("http://192.168.137.1:3000/lyric?id="+songid);
                    Log.i(TAG, "开始建立关于手机号登录的连接");
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
                        //Log.i(TAG, "S>>>>>>" + s);         //截取S的前10个字符
                        //s = s.replace("\\","");//去掉\\
                        try {
                            Log.i(TAG, "开始try");                     //开始try
                            JSONObject object = new JSONObject(s);
                            String string1 = object.getString("code");
                            int code = Integer.valueOf(string1);
                            Log.i(TAG, "code====" + code);
                            if (code == 200) {
                                re ="成功登陆";
                            }
                            else if(code == 400) {
                                re = "该手机号码格式不正确";
                            }else {
                                re = object.getString("message");
                                if(re == null ){re = "登陆失败";}
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
                Log.i(TAG, "re====" + re);
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
