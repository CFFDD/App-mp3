package com.example.showlrc;

import android.graphics.Bitmap;

/**
 * Created 2019/9/4.
 */

public class Music {
//    /**
//     * 在这里所有的属性都是用public修饰的，所以在以后调用时直接调用就可以了
//     * 如果用private修饰是需要构建set和get方法
//     */
//    //歌名
//    public String title;
//    //歌手
//    public String artist;
//    //专辑名
//    public  String album;
//    public  int length;
//    //专辑图片
//    public Bitmap albumBip;
//    public String path;


    /**
     * 歌曲id/name
     */
    public String songname;
    public String songid;
    /**
     * 歌手id\name
     */
    public String singerid;
    public String singername;
    /**
     * 歌曲的地址
     */
    public String path;
    /**
     * 歌词的地址
     */
    public String lyric;



    public Music(){}

    public Music(String songid, String songname, String singerid,String singername, String path, String lyric) {
        this.songid = songid;
        this.songname = songname;
        this.singerid = singerid;
        this.singername = singername;
        this.path = path;
        this.lyric = lyric;
    }


    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

    public String getSingerid() {
        return singerid;
    }

    public void setSingerid(String singerid) {
        this.singerid = singerid;
    }

    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }


    public boolean isPlaying;
}
