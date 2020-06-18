package com.animee.localmusic;

public class Song {
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



    public Song(){}

    public Song(String songid, String songname, String singerid,String singername, String path, String lyric) {
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



}
