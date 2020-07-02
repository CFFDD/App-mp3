package com.example.showlrc.usercenter;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MusiclistBean {
    public boolean ifcrator;     //是否创建者
    public String listid;       //歌单id
    public String listname;     //歌单名
    public String count;        //歌单歌曲数
    public Bitmap coverImg;          //歌单封面
    public String creatorid;          //创建者id
    public String creatorname;          //创建者名称



    public MusiclistBean() {}

    public MusiclistBean(boolean ifcrator, String listid, String listname, String count, Bitmap coverImg, String creatorid, String creatorname) {
        this.ifcrator = ifcrator;
        this.listid = listid;
        this.listname = listname;
        this.count = count;
        this.coverImg = coverImg;
        this.creatorid = creatorid;
        this.creatorname = creatorname;
    }

    protected MusiclistBean(Parcel in) {
        ifcrator = in.readByte() != 0;
        listid = in.readString();
        listname = in.readString();
        count = in.readString();
        coverImg = in.readParcelable(Bitmap.class.getClassLoader());
        creatorid = in.readString();
        creatorname = in.readString();
    }

    public boolean isIfcrator() {
        return ifcrator;
    }

    public void setIfcrator(boolean ifcrator) {
        this.ifcrator = ifcrator;
    }

    public String getListid() {
        return listid;
    }

    public void setListid(String listid) {
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

    public String getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(String creatorid) {
        this.creatorid = creatorid;
    }

    public String getCreatorname() {
        return creatorname;
    }

    public void setCreatorname(String creatorname) {
        this.creatorname = creatorname;
    }

}
