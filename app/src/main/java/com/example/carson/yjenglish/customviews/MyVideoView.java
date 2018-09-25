package com.example.carson.yjenglish.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.carson.yjenglish.MyApplication;
import com.example.carson.yjenglish.R;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import android.os.Handler;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * Created by 84594 on 2018/8/2.
 */

public class MyVideoView extends ConstraintLayout {

    private View rootView;

    private PLVideoTextureView mVideoView;

    private View mClickView;

    private ImageView playOrPauseView;

    private LinearLayout controlView;

    private ImageView playOrPauseControl;

    private SeekBar seekBar;

    private ImageView fullScreen;

    private ImageView playFormer;

    private ImageView playLatter;

    private ImageView coverImg;

    private static final int SHOW_CONTROL = 0x0001;

    private static final int HIDE_CONTROL = 0x0002;

    private static final int UPDATE_POSITION = 0x0003;

    private boolean isTrackingTouch = false;

    private boolean isShowControl = false;

    private boolean isPause = false;

    private boolean isFull = false;

    private boolean hasMulVideos = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_CONTROL:
                    showControl();
                    break;
                case HIDE_CONTROL:
                    hideControl();
                    break;
                case UPDATE_POSITION:
                    updatePosition();
                    break;
                default:
                    break;
            }
        }
    };

    private IFullScreenListener listener;

    private IOnStopListener onStopListener;

    private IOnChangeSourceListener changeSourceListener;

    public void setFullScreenListener(IFullScreenListener listener) {
        this.listener = listener;
    }

    public MyVideoView(Context context) {
        this(context, false, false);
//        initViews();
    }

    public MyVideoView(Context context, boolean hasMulVideos, boolean isFull) {
        super(context);
        this.hasMulVideos = hasMulVideos;
        this.isFull = isFull;
        initViews();
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.video_view, this, true);
        mClickView = rootView.findViewById(R.id.video_click);
        mVideoView = rootView.findViewById(R.id.video_view);
        playOrPauseView = rootView.findViewById(R.id.video_playpause);
        controlView = rootView.findViewById(R.id.video_control_ll);
        playOrPauseControl = rootView.findViewById(R.id.video_control_play_pause);
        seekBar = rootView.findViewById(R.id.video_seekbar);
        fullScreen = rootView.findViewById(R.id.video_full_screen);
        playFormer = rootView.findViewById(R.id.video_play_former);
        playLatter = rootView.findViewById(R.id.video_play_next);
        coverImg = rootView.findViewById(R.id.cover_img);
        initSetting();
        initEvents();
    }

    private void initSetting() {
        playOrPauseView.setImageResource(R.mipmap.ic_video_play);
        showControl();
        playOrPauseControl.setImageResource(R.mipmap.ic_video_play);
        seekBar.setProgress(0);
        if (isFull) {
            fullScreen.setImageResource(R.mipmap.ic_video_grabscreen);
            playFormer.setImageResource(R.mipmap.ic_video_former_large);
            playLatter.setImageResource(R.mipmap.ic_video_next_large);
        } else {
            fullScreen.setImageResource(R.mipmap.ic_video_fullscreen);
            playFormer.setImageResource(R.mipmap.ic_video_former_normal);
            playLatter.setImageResource(R.mipmap.ic_video_next_normal);
        }
        if (hasMulVideos) {
            playLatter.setVisibility(VISIBLE);
            playFormer.setVisibility(VISIBLE);
        } else {
            playFormer.setVisibility(GONE);
            playLatter.setVisibility(GONE);
        }
        seekBar.setEnabled(false);
    }

    private void initEvents() {
        Glide.with(MyApplication.getContext()).load(R.mipmap.gif_loading_video).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.bg_plan_box).into(coverImg);
        mVideoView.setBufferingIndicator(coverImg);
        mVideoView.setOnCompletionListener(new PLOnCompletionListener() {
            @Override
            public void onCompletion() {
                stop();
            }
        });
        mVideoView.setOnErrorListener(new PLOnErrorListener() {
            @Override
            public boolean onError(int what) {
                Toast.makeText(getContext(), "播放出错", Toast.LENGTH_SHORT).show();
                coverImg.setVisibility(VISIBLE);
                Glide.with(MyApplication.getContext()).load(R.mipmap.gif_loading_video).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.mipmap.bg_plan_box).into(coverImg);
                return false;
            }
        });

        mVideoView.setOnPreparedListener(new PLOnPreparedListener() {
            @Override
            public void onPrepared(int i) {
                playOrPauseView.setImageResource(R.mipmap.ic_video_pause);
                playOrPauseControl.setImageResource(R.mipmap.ic_video_pause);
                seekBar.setMax((int) mVideoView.getDuration());
                seekBar.setProgress((int) mVideoView.getCurrentPosition());
                seekBar.setEnabled(true);
                mHandler.sendEmptyMessage(UPDATE_POSITION);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = true;
                mHandler.removeMessages(HIDE_CONTROL);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = false;
                mHandler.sendEmptyMessageDelayed(HIDE_CONTROL, 3000);
                int position = seekBar.getProgress();
                if (mVideoView.isPlaying()) {
                    mVideoView.seekTo(position);
                }
            }
        });
        mClickView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowControl) {
                    hideControl();
                } else {
                    showControl();
                }
            }
        });
        playOrPauseView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVideoView.isPlaying()) {
                    pause();
                } else {
                    resume();
                }
            }
        });
        playOrPauseControl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVideoView.isPlaying()) {
                    pause();
                } else {
                    resume();
                }
            }
        });
        fullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
                isFull = !isFull;
                setFullScreen(isFull);
                if (listener != null) {
                    listener.onClickFull(isFull);
                }
            }
        });
        playFormer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initSetting();
                mHandler.removeCallbacksAndMessages(null);
                mVideoView.stopPlayback();
//                pause();
                if (changeSourceListener != null) {
                    changeSourceListener.onFormerClick();
                }
            }
        });
        playLatter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initSetting();
                mHandler.removeCallbacksAndMessages(null);
                mVideoView.stopPlayback();
//                pause();
                if (changeSourceListener != null) {
                    changeSourceListener.onLatterClick();
                }
            }
        });
    }

    /**
     * 更新进度
     */
    private void updatePosition() {
        mHandler.removeMessages(UPDATE_POSITION);
        if (mVideoView.isPlaying()) {
            int currentPos = (int) mVideoView.getCurrentPosition();
            if (!isTrackingTouch) {
                seekBar.setProgress(currentPos);
            }
            mHandler.sendEmptyMessageDelayed(UPDATE_POSITION, 500);
        }
    }

    /**
     * 隐藏控制条
     */
    private void hideControl() {
        isShowControl = false;
        mHandler.removeMessages(HIDE_CONTROL);
        playOrPauseView.setVisibility(GONE);
        if (hasMulVideos) {
            playLatter.setVisibility(GONE);
            playFormer.setVisibility(GONE);
        }
        controlView.clearAnimation();
        controlView.animate().translationY(controlView.getHeight()).setDuration(500).start();
    }

    /**
     * 显示进度条
     */
    private void showControl() {
        isShowControl = true;
        mHandler.sendEmptyMessageDelayed(HIDE_CONTROL, 3000);
        playOrPauseView.setVisibility(VISIBLE);
        if (hasMulVideos) {
            playFormer.setVisibility(VISIBLE);
            playLatter.setVisibility(VISIBLE);
        }
        controlView.clearAnimation();
        controlView.animate().translationY(0).setDuration(500).start();
    }

    /**
     * 设置播放地址
     * @param path
     */
    public void setVideoPath(String path) {
        mVideoView.setVideoPath(path);
    }

    /**
     * 开始播放
     */
    public void start() {
        isPause = false;
        mVideoView.start();
        showControl();
    }

    /**
     * 设置播放旋转角度
     */
    public void onRotate(boolean isFull) {
        if (isFull) {
            mVideoView.setDisplayOrientation(90);
            playOrPauseView.setRotation(-90);
            controlView.setRotation(-90);
        } else {
            mVideoView.setDisplayOrientation(-90);
            playOrPauseView.setRotation(90);
            controlView.setRotation(90);
        }
    }

    public void setControlRotate(ConstraintLayout.LayoutParams params) {
        controlView.setLayoutParams(params);
    }

    /**
     * 是否正在播放
     */
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    /**
     * 暂停
     */
    public void pause() {
        isPause = true;
        mHandler.removeMessages(UPDATE_POSITION);
        mVideoView.pause();
        playOrPauseView.setImageResource(R.mipmap.ic_video_play);
        playOrPauseControl.setImageResource(R.mipmap.ic_video_play);
    }

    /**
     * 是否处于暂停状态
     */
    public boolean isPause() {
        return isPause;
    }

    /**
     * 继续
     */
    public void resume() {
        isPause = false;
        mHandler.sendEmptyMessageDelayed(UPDATE_POSITION, 500);
        mVideoView.start();
        playOrPauseView.setImageResource(R.mipmap.ic_video_pause);
        playOrPauseControl.setImageResource(R.mipmap.ic_video_pause);
    }

    public int getPosition() {
        return (int) mVideoView.getCurrentPosition();
    }

    public void seekTo(int position) {
        mVideoView.seekTo(position);
    }

    /**
     * 停止
     */
    public void stop() {
        initSetting();
        mHandler.removeCallbacksAndMessages(null);
        mVideoView.stopPlayback();
        if (onStopListener != null) {
            onStopListener.onVideoStop();
        }
    }

    public void release() {
        initSetting();
        mHandler.removeCallbacksAndMessages(null);
        mVideoView.stopPlayback();
    }

    private void setFullScreen(boolean fullScreen) {
        if (getContext() != null && getContext() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getContext()).getSupportActionBar();
            if (actionBar != null) {
                if (fullScreen) {
                    actionBar.hide();
                } else {
                    actionBar.show();
                }
            }
        }
    }

    public boolean isFull() {
        return isFull;
    }

    public void setOnStopListener(IOnStopListener onStopListener) {
        this.onStopListener = onStopListener;
    }

    public void setChangeSourceListener(IOnChangeSourceListener changeSourceListener) {
        this.changeSourceListener = changeSourceListener;
    }

    public interface IFullScreenListener {
        void onClickFull(boolean isFull);
    }

    public interface IOnStopListener {
        void onVideoStop();
    }

    public interface IOnChangeSourceListener {
        void onFormerClick();
        void onLatterClick();
    }


}
