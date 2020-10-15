# App-mp3
当前更新至 finish_resolvesomeproblem 分支
======
基于
------
    前端：AndroidStudio
    后端:https://github.com/Binaryify/NeteaseCloudMusicApi
引用他人源代码/成品部分：
------
    API（服务器）：https://github.com/Binaryify/NeteaseCloudMusicApi
    本地音乐页面：https://www.bilibili.com/video/BV1oJ41197fi
    歌词滚动实现：https://github.com/jsyjst/Yuan-LrcView
当前已完成功能：
------
    1.本地歌曲：                     (在 localmusic-MainActivity.java中实现)
        上下曲、播放、暂停
    2.网络歌曲（url）：
        搜索界面：                   (在 MainActivity_IntMp3.java中实现)
            上下曲、暂停与播放、将歌曲加入到“我的收藏”中                 
        歌词进度条界面：            （在 showlrc-MusicActiviy.java中实现）
            拖动进度条更改播放进度、当前播放进度显示、歌曲时长显示
            上下曲、暂停与播放、循环模式切换
    3.登录、注册、修改密码
            注册和修改密码需要使用手机验证码
    4.用户中心:                     （在function.java中实现）
            显示用户名、用户头像、用户歌单
    4.歌单详情页面：                （在 songlistdetails.java中实现）
            将歌曲加入到“我的收藏”中、从歌单内删除歌曲
            判断用户权限(不能对收藏歌单内的歌曲进行删除操作）
            
