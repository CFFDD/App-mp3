package com.animee.localmusic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.library.entity.LrcBean;
import com.example.library.utils.LrcUtil;
import com.example.library.view.LrcView;

import java.io.IOException;
import java.util.List;

public class lrc extends AppCompatActivity{

    private LrcView lrcView;
    private MediaPlayer player;
    /*private String lrc =
            "[ti:喜欢你]\n" +
                    "[ar:.]\n" +
                    "[al:]\n" +
                    "[by:]\n" +
                    "[offset:0]\n" +
                    "[00:00.10]喜欢你 - G.E.M. 邓紫棋 (Gem Tang)  \n" +
                    "[00:00.20]词：黄家驹\n" +
                    "[00:00.30]曲：黄家驹\n" +
                    "[00:00.40]编曲：Lupo Groinig\n" +
                    "[00:00.50]\n" +
                    "[00:12.65]细雨带风湿透黄昏的街道\n" +
                    "[00:18.61]抹去雨水双眼无故地仰望\n" +
                    "[00:24.04]望向孤单的晚灯\n" +
                    "[00:26.91]\n" +
                    "[00:27.44]是那伤感的记忆\n" +
                    "[00:30.52]\n" +
                    "[00:34.12]再次泛起心里无数的思念\n" +
                    "[00:39.28]\n" +
                    "[00:40.10]以往片刻欢笑仍挂在脸上\n" +
                    "[00:45.49]愿你此刻可会知\n" +
                    "[00:48.23]\n" +
                    "[00:48.95]是我衷心的说声\n" +
                    "[00:53.06]\n" +
                    "[00:54.35]喜欢你 那双眼动人\n" +
                    "[00:59.35]\n" +
                    "[01:00.10]笑声更迷人\n" +
                    "[01:02.37]\n" +
                    "[01:03.15]愿再可 轻抚你\n" +
                    "[01:08.56]\n" +
                    "[01:09.35]那可爱面容\n" +
                    "[01:12.40]挽手说梦话\n" +
                    "[01:14.78]\n" +
                    "[01:15.48]像昨天 你共我\n" +
                    "[01:20.84]\n" +
                    "[01:26.32]满带理想的我曾经多冲动\n" +
                    "[01:32.45]屡怨与她相爱难有自由\n" +
                    "[01:37.82]愿你此刻可会知\n" +
                    "[01:40.40]\n" +
                    "[01:41.25]是我衷心的说声\n" +
                    "[01:44.81]\n" +
                    "[01:46.39]喜欢你 那双眼动人\n" +
                    "[01:51.72]\n" +
                    "[01:52.42]笑声更迷人\n" +
                    "[01:54.75]\n" +
                    "[01:55.48]愿再可 轻抚你\n" +
                    "[02:00.93]\n" +
                    "[02:01.68]那可爱面容\n" +
                    "[02:03.99]\n" +
                    "[02:04.73]挽手说梦话\n" +
                    "[02:07.13]\n" +
                    "[02:07.82]像昨天 你共我\n" +
                    "[02:14.53]\n" +
                    "[02:25.54]每晚夜里自我独行\n" +
                    "[02:29.30]随处荡 多冰冷\n" +
                    "[02:35.40]\n" +
                    "[02:37.83]以往为了自我挣扎\n" +
                    "[02:41.62]从不知 她的痛苦\n" +
                    "[02:52.02]\n" +
                    "[02:54.11]喜欢你 那双眼动人\n" +
                    "[03:00.13]笑声更迷人\n" +
                    "[03:02.38]\n" +
                    "[03:03.14]愿再可 轻抚你\n" +
                    "[03:08.77]\n" +
                    "[03:09.33]那可爱面容\n" +
                    "[03:11.71]\n" +
                    "[03:12.41]挽手说梦话\n" +
                    "[03:14.61]\n" +
                    "[03:15.45]像昨天 你共我";*/

    private String lrc="[00:00.000] 作词 : 浣姬\n"+
            "[00:29.309]手心上 亘古的月光\n[00:32.289]那道伤 一笑而过的苍凉\n"+
            "[00:36.569]翘首觐向 你伫立一方\n[00:40.800]是你生而为龙的狷狂\n"+
            "[00:43.369]谨记你的姓名是炎黄\n[00:50.519]\n[00:51.990]烽燧上 战地的残阳\n"+
            "[00:54.390]断刃旁 岁月悄然的流淌\n[00:59.099]十二章纹 你遥祭四望\n"+
            "[01:02.069]血脉奔腾的黄河长江\n[01:05.269]是你与生俱来的张扬\n[01:12.099]\n"+
            "[01:13.199]风雨打尽红墙和绿瓦\n[01:16.399]丹青留下明日的黄花\n"+
            "[01:20.139]汉字里墨香温存的一笔一划\n[01:24.209]世代传承的表达\n"+
            "[01:27.790]盛衰荣辱斑驳了脸颊\n[01:30.509]千载过后洗净了铅华\n"+
            "[01:34.869]一直坚守的土壤在你脚下\n[01:39.600]至死不渝的回答\n"+
            "[01:45.690]\n[01:51.280]烽燧上 战地的残阳\n[01:54.390]断刃旁 岁月悄然的流淌\n"+
            "[01:57.559]十二章纹 你遥祭四望\n[02:01.029]血脉奔腾的黄河长江\n"+
            "[02:04.319]是你与生俱来的张扬\n[02:10.799]\n[02:12.299]风雨打尽红墙和绿瓦\n"+
            "[02:14.999]丹青留下明日的黄花\n[02:19.129]汉字里墨香温存的一笔一划\n"+
            "[02:23.289]世代传承的表达\n[02:26.539]盛衰荣辱斑驳了脸颊\n[02:26.699]\n"+
            "[02:29.549]千载过后洗净了铅华\n[02:34.099]一直坚守的土壤在你脚下\n"+
            "[02:38.019]至死不渝的回答\n[02:44.059]\n[03:01.390]风雨打尽红墙和绿瓦\n"+
            "[03:04.390]丹青留下明日的黄花\n[03:07.119]汉字里墨香温存的一笔一划\n"+
            "[03:11.209]世代传承的表达\n[03:14.239]\n[03:14.519]盛衰荣辱斑驳了脸颊\n"+
            "[03:17.639]千载过后洗净了铅华\n[03:21.869]一直坚守的土壤在你脚下\n"+
            "[03:26.599]至死不渝的回答\n[03:32.009]\n[03:33.889]藏心上 亘古的月光\n"+
            "[03:36.839]怀中殇 不再回眸的苍凉\n[03:41.699]翘首觐向 你伫立此方\n"+
            "[03:45.290]谨记生而为龙的模样\n[03:47.679]谨记我的姓名是炎黄\n";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lrc);
        lrcView = findViewById(R.id.lrcView);
        requestPermission();
    }

    private void initPlayer(){
        player = MediaPlayer.create(this,R.raw.test);
        player.start();
    }

    private void initLrc(){
        lrcView.setLrc(lrc).setPlayer(player).draw();
    }

    //申请权限
    private void requestPermission(){
        if (ContextCompat.checkSelfPermission(lrc.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(lrc.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {
            initPlayer();
            initLrc();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initPlayer();
                initLrc();
            } else {
                Toast.makeText(this, "拒绝该权限无法使用该程序", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
