package com.example.login_formusic;

import android.graphics.Bitmap;

public class MusiclistBean {
    public boolean ifcrator;
    public int listid;       //歌单id
    public String listname;     //歌单名
    public String count;        //歌单歌曲数
    public Bitmap coverImg;          //歌单封面

    public MusiclistBean() {}

    public MusiclistBean(boolean ifcrator, int listid, String listname, String count, Bitmap coverImg) {
        this.ifcrator = ifcrator;
        this.listid = listid;
        this.listname = listname;
        this.count = count;
        this.coverImg = coverImg;
    }

    public boolean isIfcrator() {
        return ifcrator;
    }

    public void setIfcrator(boolean ifcrator) {
        this.ifcrator = ifcrator;
    }

    public int getListid() {
        return listid;
    }

    public void setListid(int listid) {
        this.listid = listid;
    }

    public String getListname() {
        return listname;
    }

    public void setListname(String listname) {
        this.listname = listname;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Bitmap getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(Bitmap coverImg) {
        this.coverImg = coverImg;
    }
}
