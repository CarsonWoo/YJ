package com.example.carson.yjenglish.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

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

    private static final int SHOW_CONTROL = 0x0001;

    private static final int HIDE_CONTROL = 0x0002;

    private static final int UPDATE_POSITION = 0x0003;

    private boolean isTrackingTouch = false;

    private boolean isShowControl = false;

    private boolean isPause = false;

    private boolean isFull = false;

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

    public void setFullScreenListener(IFullScreenListener listener) {
        this.listener = listener;
    }

    public MyVideoView(Context context) {
        super(context);
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
        initSetting();
        initEvents();
    }

    private void initSetting() {
        playOrPauseView.setImageResource(R.drawable.ic_play);
        showControl();
        playOrPauseControl.setImageResource(R.drawable.ic_play);
        seekBar.setProgress(0);
        fullScreen.setImageResource(R.drawable.ic_fullscreen);
        seekBar.setEnabled(false);
    }

    private void initEvents() {
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
                return false;
            }
        });
        mVideoView.setOnPreparedListener(new PLOnPreparedListener() {
            @Override
            public void onPrepared(int i) {
                playOrPauseView.setImageResource(R.drawable.ic_pause);
                playOrPauseControl.setImageResource(R.drawable.ic_pause);
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
                isFull = !isFull;
                setFullScreen(isFull);
                if (listener != null) {
                    listener.onClickFull(isFull);
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
        playOrPauseView.setImageResource(R.drawable.ic_play);
        playOrPauseControl.setImageResource(R.drawable.ic_play);
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
        playOrPauseView.setImageResource(R.drawable.ic_pause);
        playOrPauseControl.setImageResource(R.drawable.ic_pause);
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

    public interface IFullScreenListener {
        void onClickFull(boolean isFull);
    }

    public interface IOnStopListener {
        void onVideoStop();
    }


}
