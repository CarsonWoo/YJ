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
        if(str == null || str.trim().length() == 0)return  super.onStartCommand(intent, flags, startId);
        try {
            Log.e("AlarmService", "onStartCommand");
            NotifyObject obj = NotifyObject.from(str);
            NotificationUtil.notifyByAlarmByReceiver(this,obj);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
