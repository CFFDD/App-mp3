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

public class register extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "register";
    private static String re = null;

    TextView userTV,pwdTV,confirmTV,phoneTV,VercodeTV;
    EditText userET,pwdET,confirmET,phoneET,VercodeET;
    Button getVercodeBtn,registerBtn;
    ImageButton back;

    String username = null;
    String pwd = null;
    String confirm = null;
    String phone = null;
    String vercode = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        bindID();
    }

    private void bindID() {
        userTV = findViewById(R.id.userTV);
        userET = findViewById(R.id.userET);
        pwdTV = findViewById(R.id.pwdTV);
        pwdET = findViewById(R.id.pwdET);
        confirmTV = findViewById(R.id.confirmTV);
        confirmET = findViewById(R.id.confirmET);
        phoneTV = findViewById(R.id.phoneTV);
        phoneET = findViewById(R.id.phoneET);
        VercodeTV = findViewById(R.id.VercodeTV);
        VercodeET = findViewById(R.id.VercodeET);
        getVercodeBtn = findViewById(R.id.getVercodeBtn);
        registerBtn = findViewById(R.id.registerBtn);
        back = findViewById(R.id.ibtn_back_register);
        getVercodeBtn.setOnClickListener(register.this);
        registerBtn.setOnClickListener(register.this);
        back.setOnClickListener(register.this);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.getVercodeBtn:
                phone = phoneET.getText().toString().trim();
                Log.i(TAG, "phone.length===" + phone.length());
                if (phone.length() == 11) {
                    re = sentmessage(phone);
                    Log.i(TAG, "restring===" + re);
                    if (re == null) {
                        Toast.makeText(register.this, "手机号格式不正确，或今日发送验证码超过5次", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(register.this, re, Toast.LENGTH_LONG).show();
                    }
                } else if(phone.length() == 0){
                    Toast.makeText(register.this, "请输入手机号", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(register.this, "手机号格式不正确", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.registerBtn:
                setString();
                if (equals(pwd) == equals(confirm)) {       //直接使用  pwd == confirm  会出现  密码与确认密码不一致  的错误
                    re = regi(username, pwd, phone, vercode);
                    Log.i(TAG, "restring===" + re);
                    if (re == null) {
                        Toast.makeText(register.this, "验证码错误，或该昵称已被注册，或手机号格式不正确", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(register.this, re, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(register.this, "密码与确认密码不一致！！", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ibtn_back_register:
                Intent intent = new Intent();
                intent.setClass(register.this, Login.class);
                startActivity(intent);
                break;
        }
    }

    private void setString() {
        username = userET.getText().toString().trim();
        Log.i(TAG, "username===" + username);
        pwd = pwdET.getText().toString().trim();
        Log.i(TAG, "pwd===" + pwd);
        confirm = confirmET.getText().toString().trim();
        Log.i(TAG, "confirm===" + confirm);
        phone = phoneET.getText().toString().trim();
        Log.i(TAG, "phone===" + phone);
        vercode = VercodeET.getText().toString().trim();
        Log.i(TAG, "vercode===" + vercode);
        Log.i(TAG, "各字符串长度===" + username.length()+"  "+pwd.length()+"   "+confirm.length()+"     "+phone.length()+"   "+vercode.length());
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
                            }else if(code == 400) {
                                re = "该手机号码格式不正确";
                            }else {
                                re = object.getString("message");
                                if(re == null ){re = "发送验证码失败";}
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
    private static String regi(final String user, final String pwd,final String phone,final String vercode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    HttpURLConnection connection;
                    //URL url = new URL("http://api.5288z.com/weixin/musicapi.php?q="+finalTitle);
                    URL url = new URL("http://192.168.1.108:3000/register/cellphone?phone=" + phone + "&password=" + pwd +"&captcha="+ vercode +"&nickname="+user);        //本地IP经常变动，记得每次测试前先看看IP
                    //URL url = new URL("http://192.168.137.1:3000/lyric?id="+songid);
                    Log.i(TAG, "开始建立关用户注册的连接");
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
                                re = "成功注册";
                            }else if(code == 400) {
                                re = "该手机号码格式不正确";
                            }else {
                                re = object.getString("message");
                                if(re == null ){re = "注册失败";}
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