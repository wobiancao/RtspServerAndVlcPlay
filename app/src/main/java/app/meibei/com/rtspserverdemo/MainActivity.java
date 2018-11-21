package app.meibei.com.rtspserverdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import app.com.rtsplibrary.util.T;

public class MainActivity extends AppCompatActivity{
    private EditText mPlayUrlView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayUrlView = findViewById(R.id.edit_url);
    }


    public void onServer(View view) {
        Intent intent = new Intent(this, ServerActivity.class);
        startActivity(intent);

    }

    public void onPlay(View view) {
        String url = mPlayUrlView.getText().toString().trim();
        if (TextUtils.isEmpty(url)){
            T.showShort(this, "请输入直播流地址");
            return;
        }
        PlayActivity.gotoPlayActivity(this, url);
    }
}
