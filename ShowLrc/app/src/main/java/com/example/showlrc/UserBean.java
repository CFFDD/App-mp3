package com.example.showlrc;

import android.graphics.Bitmap;

public class UserBean {
    public int uid;
    public String nikename;
    public Bitmap avatar;

    public UserBean() {
    }

    public UserBean(int uid, String nikename, Bitmap avatar) {
        this.uid = uid;
        this.nikename = nikename;
        this.avatar = avatar;
    }
}
