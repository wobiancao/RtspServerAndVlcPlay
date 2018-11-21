package app.meibei.com.rtspserverdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.com.rtsplibrary.constants.Constant;
import app.com.rtsplibrary.data.DataUtil;
import app.com.rtsplibrary.data.H264DataCollecter;
import app.com.rtsplibrary.media.H264Data;
import app.com.rtsplibrary.record.ScreenRecordThread;
import app.com.rtsplibrary.rtsp.RtspServer;
import app.com.rtsplibrary.util.RunState;
import app.com.rtsplibrary.util.T;

public class ServerActivity extends AppCompatActivity implements H264DataCollecter {

    private EditText mEditPortView;
    private TextView mTextUrlView;
    private Button mBtnView;

    public static final int REQUEST_CODE = 1001;
    private RtspServer mRtspServer;
    private MediaProjectionManager mMediaProjectionManager;
    private ScreenRecordThread mScreenRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        initView();
        initServer();
    }

    private void initView() {
        mEditPortView = findViewById(R.id.edit_port);
        mTextUrlView = findViewById(R.id.tv_url);
        mBtnView = findViewById(R.id.btn_start);
    }

    private void initServer() {
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        Constant.ISPAD = false;
    }

    public void onStart(View view) {
        String port = mEditPortView.getText().toString().trim();
        if (!TextUtils.isEmpty(port)){
            Constant.changePort(Integer.valueOf(port));
        }
        if (!RunState.getInstance().isRun()){
            RunState.getInstance().setRun(true);
            Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
            startActivityForResult(captureIntent, REQUEST_CODE);
            bindService(new Intent(this,RtspServer.class), mRtspServiceConnection, Context.BIND_AUTO_CREATE);
        }else {
            RunState.getInstance().setRun(false);
            if (mScreenRecord != null) mScreenRecord.release();
            if (mRtspServer != null) mRtspServer.removeCallbackListener(mRtspCallbackListener);
            unbindService(mRtspServiceConnection);
            changeView();
        }
    }



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

    private RtspServer.CallbackListener mRtspCallbackListener = new RtspServer.CallbackListener() {

        @Override
        public void onError(RtspServer server, Exception e, int error) {
            // We alert the user that the port is already used by another app.
            if (error == RtspServer.ERROR_BIND_FAILED) {
                new AlertDialog.Builder(ServerActivity.this)
                        .setTitle("端口被占用用")
                        .setMessage("你需要选择另外一个端口")
                        .show();
            }
        }

        @Override
        public void onMessage(RtspServer server, int message) {
            if (message==RtspServer.MESSAGE_STREAMING_STARTED) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        T.showShort(ServerActivity.this, "用户接入，推流开始");
                    }
                });
            } else if (message==RtspServer.MESSAGE_STREAMING_STOPPED) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        T.showShort(ServerActivity.this, "推流结束");
                    }
                });
            }
        }
    };


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            if(mediaProjection == null){
                T.showShort(ServerActivity.this, "程序发生错误:MediaProjection@1");
                RunState.getInstance().setRun(false);
                return;
            }
            changeView();
            mScreenRecord = new ScreenRecordThread(this,mediaProjection, this);
            mScreenRecord.start();
        }
        catch (Exception e){

        }


    }

    private void changeView() {
        if (RunState.getInstance().isRun()){
            String playUrl = Constant.displayIpAddress(this);
            mTextUrlView.setText(playUrl);
            mBtnView.setText("STOP");
        }else {
            mTextUrlView.setText("");
            mBtnView.setText("START");
        }
    }

    @Override
    public void collect(H264Data data) {
        DataUtil.getInstance().putData(data);
    }
}
