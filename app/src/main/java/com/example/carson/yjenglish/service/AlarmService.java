package com.example.carson.yjenglish.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.carson.yjenglish.NotifyObject;
import com.example.carson.yjenglish.utils.NotificationUtil;

import java.io.IOException;

/**
 * Created by 84594 on 2018/10/13.
 */

public class AlarmService extends Service {
    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String str = intent.getStringExtra("KEY_NOTIFY");
        if(str == null || str.trim().length() == 0) {
            //保证service在内存空间足够时被重新build 但不一定能接收到intent的信息
            flags = START_STICKY;
            return super.onStartCommand(intent, flags, startId);
        }
        try {
            Log.e("AlarmService", "onStartCommand");
            NotifyObject obj = NotifyObject.from(str);
            NotificationUtil.notifyByAlarmByReceiver(this,obj);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }
}
