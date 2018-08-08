package com.example.carson.yjenglish;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.carson.yjenglish.customviews.MyVideoView;

public class FullScreenVideo extends AppCompatActivity {

    private FrameLayout fullscreen;
    private MyVideoView videoView;

    private int progress;
    private String path;

    public static final int RESULT_VIDEO_COMPLETE = 0x1001;
    public static final int RESULT_VIDEO_NOT_FINISH = 0x1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fullscreen = findViewById(R.id.full_screen);
        videoView = new MyVideoView(this);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        progress = intent.getIntExtra("progress", 0);


        fullscreen.addView(videoView, new ViewGroup.LayoutParams(-1, -1));
        videoView.setVideoPath(path);
        videoView.stop();

        videoView.seekTo(progress);
        videoView.start();
        videoView.setOnStopListener(new MyVideoView.IOnStopListener() {
            @Override
            public void onVideoStop() {
//                fullscreen.removeAllViews();
//                onBackPressed();
                setResult(RESULT_VIDEO_COMPLETE);
                onBackPressed();
            }
        });
        videoView.setFullScreenListener(new MyVideoView.IFullScreenListener() {
            @Override
            public void onClickFull(boolean isFull) {
                videoView.pause();
                Intent data = new Intent();
                data.putExtra("progress", videoView.getPosition());
                setResult(RESULT_VIDEO_NOT_FINISH, data);
//                fullscreen.removeAllViews();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_top_rotate_sign_out, R.anim.anim_top_rotate_get_into);
    }
}
