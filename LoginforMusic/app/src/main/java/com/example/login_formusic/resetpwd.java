package com.example.login_formusic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class resetpwd extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "resetpwd";
    private static String re = null;

    TextView pwdTV2,confirmTV2,phoneTV2,VercodeTV2;
    EditText pwdET2,confirmET2,phoneET2,VercodeET2;
    Button getVercodeBtn2,resetBtn;
    ImageButton back2;

    String pwd2 = null;
    String confirm2 = null;
    String phone2 = null;
    String vercode2 = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpwd);
        bindID();
    }

    private void bindID() {
        pwdTV2 = findViewById(R.id.pwdTV2);
        pwdET2 = findViewById(R.id.pwdET2);
        confirmTV2 = findViewById(R.id.confirmTV2);
        confirmET2 = findViewById(R.id.confirmET2);
        phoneTV2 = findViewById(R.id.phoneTV2);
        phoneET2 = findViewById(R.id.phoneET2);
        VercodeTV2 = findViewById(R.id.VercodeTV2);
        VercodeET2 = findViewById(R.id.VercodeET2);
        getVercodeBtn2 = findViewById(R.id.getVercodeBtn2);
        resetBtn = findViewById(R.id.resetBtn);
        back2 = findViewById(R.id.ibtm_back_reset);
        getVercodeBtn2.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
        back2.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getVercodeBtn2:
                phone2 = phoneET2.getText().toString().trim();
                if (phone2 != "") {
                    re = sentmessage(phone2);
                    Log.i(TAG, "restring===" + re);
                    if (re == null) {
                        Toast.makeText(resetpwd.this, "手机号格式不正确，或今日发送验证码超过5次", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(resetpwd.this, re, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(resetpwd.this, "请输入手机号码", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.resetBtn:
                setString2();
                if (equals(pwd2) == equals(confirm2)) {         //直接使用  pwd2 == confirm2  会出现  密码与确认密码不一致  的错误
                    re = checkvercode(phone2,vercode2);
                    Log.i(TAG, "restring===" + re);
                    if(re == "验证码正确"){
                        re = resetpwd(phone2, pwd2, vercode2);
                        Log.i(TAG, "restring===" + re);
                        if (re == null) {
                            Toast.makeText(resetpwd.this, "手机号格式不正确", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(resetpwd.this, re, Toast.LENGTH_LONG).show();
                        }
                    } else{
                        Toast.makeText(resetpwd.this, "验证码错误或手机号不正确", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(resetpwd.this, "密码与确认密码不一致！！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ibtm_back_reset:
                Intent intent = new Intent();
                intent.setClass(resetpwd.this, Login.class);
                startActivity(intent);
                break;

        }




    }

    private void setString2() {
        pwd2 = pwdET2.getText().toString().trim();
        confirm2 = confirmET2.getText().toString().trim();
        phone2 = phoneET2.getText().toString().trim();
        vercode2 = VercodeET2.getText().toString().trim();
        Log.i(TAG, "各字符串长度===" +pwd2.length()+"   "+confirm2.length()+"     "+phone2.length()+"   "+vercode2.length());
    }

    private static String sentmessage(final String phone) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    HttpURLConnection connection;
                    //URL url = new URL("http://api.5288z.com/weixin/musicapi.php?q="+finalTitle);
                    URL url = new URL("http://192.168.1.108:3000/captcha/sent?phone=" + phone);        //本地IP经常变动，记得每次测试前先看看IP
                    //URL url = new URL("http://192.168.137.1:3000/lyric?id="+songid);
                    Log.i(TAG, "开始建立关发送验证码的连接");
                    Log.i(TAG, "url===" + url);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(60 * 1000);
                    connection.setReadTimeout(60 * 1000);
                    connection.connect();
                    Log.i(TAG, "连接上了");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Log.i(TAG, "reader>>>>>>" + reader);
                    String s;
                    s = reader.readLine();
                    Log.i(TAG, "S>>>>>>" + s);
                    if (s != null) {
                        Log.i(TAG, "S>>>>>>" + s);         //截取S的前10个字符
                        //s = s.replace("\\","");//去掉\\
                        try {
                            Log.i(TAG, "开始try");                     //开始try
                            JSONObject object = new JSONObject(s);
                            String string1 = object.getString("code");
                            int code = Integer.valueOf(string1);
                            Log.i(TAG, "code====" + code);
                            if (code == 200) {
                                re ="成功发送验证码";
                            } else {
                                re = object.getString("message");
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

    private static String checkvercode(final String phone,final String vercode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    HttpURLConnection connection;
                    //URL url = new URL("http://api.5288z.com/weixin/musicapi.php?q="+finalTitle);
                    URL url = new URL("http://192.168.1.108:3000/captcha/sent?phone=" + phone +"&captcha=" +vercode);        //本地IP经常变动，记得每次测试前先看看IP
                    //URL url = new URL("http://192.168.137.1:3000/lyric?id="+songid);
                    Log.i(TAG, "开始建立关验证验证码的连接");
                    Log.i(TAG, "url===" + url);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(60 * 1000);
                    connection.setReadTimeout(60 * 1000);
                    connection.connect();
                    Log.i(TAG, "连接上了");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Log.i(TAG, "reader>>>>>>" + reader);
                    String s;
                    s = reader.readLine();
                    Log.i(TAG, "S>>>>>>" + s);
                    if (s != null) {
                        Log.i(TAG, "S>>>>>>" + s);         //截取S的前10个字符
                        //s = s.replace("\\","");//去掉\\
                        try {
                            Log.i(TAG, "开始try");                     //开始try
                            JSONObject object = new JSONObject(s);
                            String string1 = object.getString("code");
                            int code = Integer.valueOf(string1);
                            Log.i(TAG, "code====" + code);
                            if (code == 200) {
                                re ="验证码正确";
                            } else {
                                re = object.getString("message");
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

    private static String resetpwd(final String phone, final String pwd, final String vercode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    HttpURLConnection connection;
                    //URL url = new URL("http://api.5288z.com/weixin/musicapi.php?q="+finalTitle);
                    URL url = new URL("http://192.168.1.108:3000/register/cellphone?phone=" + phone + "&password=" + pwd +"&captcha="+vercode);        //本地IP经常变动，记得每次测试前先看看IP
                    //URL url = new URL("http://192.168.137.1:3000/lyric?id="+songid);
                    Log.i(TAG, "开始建立关修改密码的连接");
                    Log.i(TAG, "url===" + url);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(60 * 1000);
                    connection.setReadTimeout(60 * 1000);
                    connection.connect();
                    Log.i(TAG, "连接上了");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Log.i(TAG, "reader>>>>>>" + reader);
                    String s;
                    s = reader.readLine();
                    Log.i(TAG, "S>>>>>>" + s);
                    if (s != null) {
                        Log.i(TAG, "S>>>>>>" + s);         //截取S的前10个字符
                        //s = s.replace("\\","");//去掉\\
                        try {
                            Log.i(TAG, "开始try");                     //开始try
                            JSONObject object = new JSONObject(s);
                            String string1 = object.getString("code");
                            int code = Integer.valueOf(string1);
                            Log.i(TAG, "code====" + code);
                            if (code == 200) {
                                re ="成功修改";
                            } else {
                                re = object.getString("message");
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
