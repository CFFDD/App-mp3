package com.example.showlrc.intmusic;

import java.io.Serializable;

public class IntMusicBean implements Serializable {

    /**
     * 歌曲id/name
     */
    public String songname;
    public String songid;

    public int idbyme;
    /**
     * 歌手id\name
     */
    public String singerid;
    public String singername;
    /**
     * 歌曲的地址
     */
    public String path=null;        //默认为空，以便seebar页面进行判断
    /**
     * 歌词的地址
     */
    public String lyric=null;       //默认为空，以便seebar页面进行判断
    /**
     * 歌曲的长度
     */
    public int length;


    public IntMusicBean(){}

    public IntMusicBean(int idbyme,String songid, String songname, String singerid, String singername, String path, String lyric,int length) {
        this.idbyme = idbyme;
        this.songid = songid;
        this.songname = songname;
        this.singerid = singerid;
        this.singername = singername;
        this.path = path;
        this.lyric = lyric;
        this.length = length;
    }

    public IntMusicBean(int idbyme,String songid, String songname, String singerid, String singername,int length) {
        this.idbyme = idbyme;
        this.songid = songid;
        this.songname = songname;
        this.singerid = singerid;
        this.singername = singername;
        this.path = path;
        this.lyric = lyric;
        this.length = length;
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

    public int getIdbyme() {
        return idbyme;
    }

    public void setIdbyme(int idbyme) {
        this.idbyme = idbyme;
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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isPlaying;
}
