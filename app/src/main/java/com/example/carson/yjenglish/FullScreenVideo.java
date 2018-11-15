package com.example.carson.yjenglish;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.carson.yjenglish.customviews.MyVideoView;
import com.example.carson.yjenglish.utils.NetUtils;
import com.example.carson.yjenglish.utils.UserConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FullScreenVideo extends AppCompatActivity implements MyVideoView.IOnPrepareListener {

    private FrameLayout fullscreen;
    private MyVideoView videoView;

    private int progress;
    private String path;
    private String video_id;
    private int mCurPos;
    private boolean hasMulVideos;

    private List<String> videoList = new ArrayList<>();
    private List<String> videoIds = new ArrayList<>();

    private int type;
    private List<VideoCaptionModel> mCaptions;

    public static final int RESULT_VIDEO_COMPLETE = 0x1001;
    public static final int RESULT_VIDEO_NOT_FINISH = 0x1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        video_id = intent.getStringExtra("video_ids");
        progress = intent.getIntExtra("progress", 0);
        hasMulVideos = intent.getBooleanExtra("has_multi_videos", false);
        mCurPos = intent.getIntExtra("current_position", 0);
        type = intent.getIntExtra("caption_type", 0);//0表示没有caption，1表示有caption

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        fullscreen = findViewById(R.id.full_screen);
        videoView = new MyVideoView(this, hasMulVideos, true);
        fullscreen.addView(videoView, new ViewGroup.LayoutParams(-1, -1));
//        videoView.setPrepareListener(this);
        videoView.pause();

        if (hasMulVideos) {
            String[] mPath = path.split("；");
            String[] mVideoIds = video_id.split("；");
            videoList.addAll(Arrays.asList(mPath));
            videoIds.addAll(Arrays.asList(mVideoIds));
            videoView.setVideoPath(videoList.get(mCurPos));
            videoView.seekTo(progress);
            executeCaptionTask(true);
//            videoView.start();
        } else {
            //没有字幕的情况只会出现在这里
            if (type == 0) {
                videoView.setVideoPath(path);
                videoView.seekTo(progress);
                videoView.resume();
            } else {
                videoIds.add(video_id);
                mCurPos = 0;
                executeCaptionTask(true);
            }
//            videoView.start();
        }


//        videoView.resume();
        videoView.setOnStopListener(new MyVideoView.IOnStopListener() {
            @Override
            public void onVideoStop() {
//                fullscreen.removeAllViews();
//                onBackPressed();
                if (hasMulVideos) {
                    mCurPos ++;
                    if (mCurPos < videoList.size()) {
                        videoView.setVideoPath(videoList.get(mCurPos));
                        executeCaptionTask(false);
//                        videoView.start();
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
                        executeCaptionTask(false);
//                        videoView.start();
                    } else {
                        Toast.makeText(FullScreenVideo.this, "没有上一个视频噢", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onLatterClick() {
                    if (mCurPos + 1 < videoList.size()) {
                        mCurPos ++;
                        videoView.setVideoPath(videoList.get(mCurPos));
                        executeCaptionTask(false);
//                        videoView.start();
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

    private void executeCaptionTask(final boolean isResume) {
        Retrofit retrofit = NetUtils.getInstance().getRetrofitInstance(UserConfig.HOST);
        retrofit.create(VideoService.class).getVideoInfo(UserConfig.getToken(this),
                videoIds.get(mCurPos)).enqueue(new Callback<VideoCaptionInfo>() {
            @Override
            public void onResponse(Call<VideoCaptionInfo> call, Response<VideoCaptionInfo> response) {
                VideoCaptionInfo info = response.body();
                if (info.getStatus().equals("200")) {
                    mCaptions = info.getData();
                    videoView.setCaption(mCaptions);
                    if (isResume) {
                        videoView.resume();
                    } else {
                        videoView.start();
                    }
                } else {
                    Log.e("Full", info.getMsg());
                }
            }

            @Override
            public void onFailure(Call<VideoCaptionInfo> call, Throwable t) {
                Toast.makeText(FullScreenVideo.this, "连接超时", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_top_rotate_sign_out, R.anim.anim_top_rotate_get_into);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.release();
        }
    }

    @Override
    public void onPrepared() {

//        videoView.stop();
//        videoView.start();
        videoView.seekTo(progress);
        videoView.resume();
    }
}
