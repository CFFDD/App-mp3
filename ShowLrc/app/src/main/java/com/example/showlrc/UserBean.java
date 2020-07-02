package com.example.showlrc;

import android.graphics.Bitmap;

public class UserBean {
    public int uid;
    public String nikename;
    public Bitmap avatar;
    public String cookie;

    public UserBean() {
    }

    public UserBean(int uid, String nikename, Bitmap avatar,String cookie) {
        this.uid = uid;
        this.nikename = nikename;
        this.avatar = avatar;
        this.cookie = cookie;
    }
}
