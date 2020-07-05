# App-mp3
当前更新至 finish_resolvesomeproblem 分支
======
基于
------
    前端：AndroidStudio
    后端:https://github.com/Binaryify/NeteaseCloudMusicApi
当前已完成功能：
------
    1.本地歌曲：上下曲、播放、暂停        (在 localmusic-MainActivity.java中实现)
    2.网络歌曲（url）：
        搜索界面-上下曲、暂停与播放、将歌曲加入到“我的收藏”中       (在 MainActivity_IntMp3.java中实现)                 
        歌词进度条界面-拖动进度条更改播放进度、当前播放进度显示、歌曲时长显示、上下曲、暂停与播放、循环模式切换    （在 showlrc-MusicActiviy.java中实现）
    3.登录、注册、修改密码
    4.用户中心:显示用户名、用户头像、用户歌单      （在function.java中实现）
    4.歌单详情页面：将歌曲加入到“我的收藏”中，从歌单内删除歌曲、判断用户权限(不能对收藏歌单内的歌曲进行删除操作）   （在 songlistdetails.java中实现）
