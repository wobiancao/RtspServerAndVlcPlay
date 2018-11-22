# rstp vlc player wifi录屏直播 或：局域网无纸化会议封装，只需三步实现推流和播放


![结构图](https://upload-images.jianshu.io/upload_images/1216032-c421b0f9febc170d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/400)

![vlc播放效果](https://upload-images.jianshu.io/upload_images/1216032-f140994e9470a544.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/237/format/webp)

 **[上篇文章](https://www.jianshu.com/p/793fc98cc1a1)**


步骤：
--------------
#### 1.下载lib到本地，然后依赖

 **[RtspServerAndVlcPlay](https://github.com/wobiancao/RtspServerAndVlcPlay)**
 
``
git clone https://github.com/wobiancao/RtspServerAndVlcPlay.git
``
1) 如果只是推流端，只需要依赖 **[rtsplibrary](https://github.com/wobiancao/RtspServerAndVlcPlay/tree/master/rtsplibrary)**

2) 如果只是播放端，只需要依赖 **[libvlc-android](https://github.com/wobiancao/RtspServerAndVlcPlay/tree/master/libvlc-android)**

`` 重点声明: rtsplibrary是基于libstreaming开源封装 libvlc-android是别人开源vlc项目编译好的，非本人编译 ``

libvlc-android 原项目地址： **[vlc-android-sdk](https://github.com/mrmaffen/vlc-android-sdk)** 
多点几个star给作者鼓励吧

#### 2.推流端

1) 初始化
```Android
    public static final int REQUEST_CODE = 1001;
    private RtspServer mRtspServer;
    private MediaProjectionManager mMediaProjectionManager;
    private ScreenRecordThread mScreenRecord;

    mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
    private ServiceConnection mRtspServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRtspServer = ((RtspServer.LocalBinder)service).getService();
            mRtspServer.addCallbackListener(mRtspCallbackListener);
            mRtspServer.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };
```
2) 授权成功后开启录屏线程服务

```
            //请求授权
            Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
            startActivityForResult(captureIntent, REQUEST_CODE);
            bindService(new Intent(this,RtspServer.class), mRtspServiceConnection, Context.BIND_AUTO_CREATE);
            //授权回调
             public void onActivityResult(int requestCode, int resultCode, Intent data) {
              try {
                  MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
                  if(mediaProjection == null){
                      T.showShort(ServerActivity.this, "程序发生错误:MediaProjection@1");
                      RunState.getInstance().setRun(false);
                    return;
            }
           //开启录屏线程
            mScreenRecord = new ScreenRecordThread(this,mediaProjection, this);
            mScreenRecord.start();
        }
        catch (Exception e){
        }
    }
```
3) 实现H264DataCollecter接口回调，设置推流数据

```
@Override
    public void collect(H264Data data) {
        DataUtil.getInstance().putData(data);
    }
```
#### 3.播放端

1) 布局
```
<SurfaceView
        android:id="@+id/player_surface"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
2) 实例化
```
    SurfaceView mPlayView;
    private String playUrl = "";
    private LibVLC libVLC = null;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder mSurfaceHolder;

        ArrayList<String> options = new ArrayList<>();
        libVLC = new LibVLC(getApplication(), options);
        mediaPlayer = new MediaPlayer(libVLC);
        mSurfaceHolder = mPlayView.getHolder();//用于设置视图
        mSurfaceHolder.setFixedSize(DisplayUtils.getDisplayW(this), DisplayUtils.getDisplayH(this));
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mediaPlayer.getVLCVout().setVideoSurface(mPlayView.getHolder().getSurface(), mSurfaceHolder);
        mediaPlayer.getVLCVout().attachViews();
        Media media = new Media(libVLC, Uri.parse(playUrl));
        mediaPlayer.setMedia(media);
```
3) 播放
``调用mediaPlayer.play();方法即可开始播放``
```
//设置全屏去掉状态栏虚拟键 屏幕常亮
private void onPreCreate() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉虚拟按键全屏显示
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                        // bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕常亮
    }

public void onExit(View view) {
        finish();
    }

    //物理按键 虚拟按键 全部屏蔽
    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

```

体验
--------------

![qr.png](https://upload-images.jianshu.io/upload_images/1216032-38d5489b5b2e9917.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/240)

**[apk下载地址](https://fir.im/pxg5)**

**[本项目github地址](https://github.com/wobiancao/RtspServerAndVlcPlay)**

**欢迎star**

Thanks
----------------
**[libstreaming](https://github.com/fyhertz/libstreaming)**

**[AndroidShow](https://github.com/sszhangpengfei/AndroidShow)**

**[ScreenRecorder](https://github.com/glt/ScreenRecorder)**

**[vlc-android-sdk](https://github.com/mrmaffen/vlc-android-sdk)**
