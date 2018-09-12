package com.example.carson.yjenglish.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.carson.yjenglish.R;
import com.example.carson.yjenglish.broadcastreceiver.LockScreenBroadcastReceiver;

import java.io.IOException;

/**
 * Created by 84594 on 2018/8/24.
 */

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mPlayer;
    private int res;
    private String lrcText;
    private MusicBinder mBinder;
    private int size;

    private LockScreenBroadcastReceiver receiver;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        String path = intent.getStringExtra("path");
        Log.e("Service", "onStartCommand");

        res = intent.getIntExtra("res", R.raw.rap_god);
        lrcText = intent.getStringExtra("lrcText");
        init();
        return START_NOT_STICKY;
    }

    private void init() {
        if (mPlayer != null) {
            mPlayer.reset();
        }
        try {
            mPlayer.setDataSource(getResources().openRawResourceFd(res));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.prepareAsync();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                size = mediaPlayer.getDuration();
            }
        });
        mPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Service", "onCreate");
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        } else {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            } else {
                mPlayer.start();
            }
        }
        receiver = new LockScreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(receiver, filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mBinder = new MusicBinder();
        return mBinder;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (mBinder != null) {
            mBinder.complete();
//            res = mBinder.getDataSource();
//            init();
        }
        stopSelf();
    }

    public class MusicBinder extends Binder {

        private int dataSource;
        private OnCompleteListener onCompleteListener;

        //判断是否处于播放状态
        public boolean isPlaying() {
            return mPlayer.isPlaying();
        }

        //播放或暂停
        public void play() {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            } else {
                mPlayer.start();
            }
        }

        //返回歌曲长度
        public int getDuration() {
            return size == 0 ? mPlayer.getDuration() : size;
        }

        //返回歌曲目前进度
        public int getCurrentPosition() {
            return mPlayer.getCurrentPosition();
        }

        //设置进度
        public void seekTo(int msec) {
            mPlayer.seekTo(msec);
        }

        public void setCompleteListner(OnCompleteListener listener) {
            this.onCompleteListener = listener;
        }

        public void complete() {
            Log.e("Binder", "complete");
//            mPlayer.stop();
//            mPlayer.reset();
//            mPlayer.seekTo(0);
            if (onCompleteListener != null) {
                onCompleteListener.onComplete();
            }
        }

        public void setDataSource(int dataSource) {
//            mPlayer.reset();
            this.dataSource = dataSource;
        }

        private int getDataSource() {
            return dataSource;
        }

        //通过此方法使播放另一首单词曲目
        public void onNext() {
            res = getDataSource();
            init();
        }
    }
    public interface OnCompleteListener {
        void onComplete();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        Log.e("MusicService", "onDestroy");
        mPlayer.release();
    }
}
