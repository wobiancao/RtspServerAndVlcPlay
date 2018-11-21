package app.meibei.com.rtspserverdemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

import app.meibei.com.rtspserverdemo.util.DisplayUtil;


public class PlayActivity extends AppCompatActivity {
    SurfaceView mPlayView;
    private static final String PLAY_URL = "playUrl";
    private String playUrl = "";
    private LibVLC libVLC = null;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder mSurfaceHolder;

    public static void gotoPlayActivity(Activity context, String playUrl) {
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtra(PLAY_URL, playUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onPreCreate();
        setContentView(R.layout.activity_play_video);
        mPlayView = findViewById(R.id.player_surface);
        initData();
    }

    private void initData() {
        playUrl = getIntent().getStringExtra(PLAY_URL);
        if (TextUtils.isEmpty(playUrl)){
            finish();
            return;
        }
        ArrayList<String> options = new ArrayList<>();
        libVLC = new LibVLC(getApplication(), options);
        mediaPlayer = new MediaPlayer(libVLC);
        mSurfaceHolder = mPlayView.getHolder();
        mSurfaceHolder.setFixedSize(DisplayUtil.getDisplayW(this), DisplayUtil.getDisplayH(this));
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mediaPlayer.getVLCVout().setVideoSurface(mPlayView.getHolder().getSurface(), mSurfaceHolder);
        mediaPlayer.getVLCVout().attachViews();
        Media media = new Media(libVLC, Uri.parse(playUrl));
        mediaPlayer.setMedia(media);
        mediaPlayer.play();
    }


    private void onPreCreate() {
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

}
