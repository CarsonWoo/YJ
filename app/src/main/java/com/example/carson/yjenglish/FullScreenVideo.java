package com.example.carson.yjenglish;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.carson.yjenglish.customviews.MyVideoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FullScreenVideo extends AppCompatActivity {

    private FrameLayout fullscreen;
    private MyVideoView videoView;

    private int progress;
    private String path;
    private int mCurPos;
    private boolean hasMulVideos;

    private List<String> videoList = new ArrayList<>();

    public static final int RESULT_VIDEO_COMPLETE = 0x1001;
    public static final int RESULT_VIDEO_NOT_FINISH = 0x1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        progress = intent.getIntExtra("progress", 0);
        hasMulVideos = intent.getBooleanExtra("has_multi_videos", false);
        mCurPos = intent.getIntExtra("current_position", 0);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fullscreen = findViewById(R.id.full_screen);
        videoView = new MyVideoView(this, hasMulVideos, true);

        fullscreen.addView(videoView, new ViewGroup.LayoutParams(-1, -1));
        if (hasMulVideos) {
            String[] mPath = path.split("；");
            videoList.addAll(Arrays.asList(mPath));
            videoView.setVideoPath(videoList.get(mCurPos));
        } else {
            videoView.setVideoPath(path);
        }
        videoView.stop();
        videoView.seekTo(progress);
        videoView.start();
        videoView.setOnStopListener(new MyVideoView.IOnStopListener() {
            @Override
            public void onVideoStop() {
//                fullscreen.removeAllViews();
//                onBackPressed();
                if (hasMulVideos) {
                    mCurPos ++;
                    if (mCurPos < videoList.size()) {
                        videoView.setVideoPath(videoList.get(mCurPos));
                        videoView.start();
                    } else {
                        Toast.makeText(FullScreenVideo.this, "没有下个视频了", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setResult(RESULT_VIDEO_COMPLETE);
                    onBackPressed();
                }
            }
        });
        if (hasMulVideos) {
            videoView.setChangeSourceListener(new MyVideoView.IOnChangeSourceListener() {
                @Override
                public void onFormerClick() {
                    if (mCurPos > 0) {
                        mCurPos --;
                        videoView.setVideoPath(videoList.get(mCurPos));
                        videoView.start();
                    } else {
                        Toast.makeText(FullScreenVideo.this, "没有上一个视频噢", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onLatterClick() {
                    if (mCurPos + 1 < videoList.size()) {
                        mCurPos ++;
                        videoView.setVideoPath(videoList.get(mCurPos));
                        videoView.start();
                    } else {
                        Toast.makeText(FullScreenVideo.this, "没有下个视频了", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
