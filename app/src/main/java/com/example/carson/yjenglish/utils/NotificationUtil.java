package com.example.carson.yjenglish.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.carson.yjenglish.NotifyObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 84594 on 2018/10/13.
 */

public class NotificationUtil {

    private static final String TAG = "NotificationUtil";

    public static void notifySpecificByAlarm(Context context, NotifyObject obj) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (obj == null) {
            return;
        }
        if (obj.times.size() <= 0) {
            if (obj.firstTime > 0) {
                try {
                    Map<String, Serializable> map = new HashMap<>();
                    map.put("KEY_NOTIFY_ID", obj.type);
                    map.put("KEY_NOTIFY", NotifyObject.to(obj));
                    AlarmTimerUtil.setAlarmTimer(context, obj.type, obj.firstTime, "TIMER_ACTION", map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (long time : obj.times) {
                if (time > 0) {
                    try {
                        Map<String,Serializable> map = new HashMap<>();
                        map.put("KEY_NOTIFY_ID",obj.type);
                        map.put("KEY_NOTIFY",NotifyObject.to(obj));
                        AlarmTimerUtil.setAlarmTimer(context, obj.type, time, "TIMER_ACTION", map);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /** 只要count在外部调用时从1开始计算 则alarmId = nid */

    public static void notifyByAlarm(Context context, Map<Integer, NotifyObject> notifyObjectMap) {
        //将数据存储起来
        int count = 0;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Set<Integer> keySet = notifyObjectMap.keySet();
        for (Integer key0 : keySet) {
            if (!notifyObjectMap.containsKey(key0)) {
                break;
            }

            NotifyObject obj = notifyObjectMap.get(key0);
            if (obj == null) {
                break;
            }

            if (obj.times.size() <= 0) {
                if (obj.firstTime > 0) {
                    try {
                        Map<String, Serializable> map = new HashMap<>();
                        map.put("KEY_NOTIFY_ID", obj.type);
                        map.put("KEY_NOTIFY", NotifyObject.to(obj));
                        AlarmTimerUtil.setAlarmTimer(context, ++count, obj.firstTime, "TIMER_ACTION", map);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (long time : obj.times) {
                    if (time > 0) {
                        try {
                            Map<String,Serializable> map = new HashMap<>();
                            map.put("KEY_NOTIFY_ID",obj.type);
                            map.put("KEY_NOTIFY",NotifyObject.to(obj));
                            AlarmTimerUtil.setAlarmTimer(context,++count,time,"TIMER_ACTION",map);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        SharedPreferences sp = context.getSharedPreferences("SHARE_PREFERENCE_NOTIFICATION", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("KEY_MAX_ALARM_ID", count);
        editor.apply();

    }

    public static void  notifyByAlarmByReceiver(Context context,NotifyObject obj){
        if(context == null || obj== null)return;
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.e(TAG, "receive");
        notifyMsg(context,obj,obj.type,System.currentTimeMillis(),manager);
    }

    public static void notifyMsg(Context context, NotifyObject obj, int nid, long time, NotificationManager manager) {
        if (context == null || obj == null) {
            return;
        }
        if (manager == null) {
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (time <= 0) {
            return;
        }

        Intent intent = new Intent(context, obj.activityClass);
        if (obj.param != null && obj.param.trim().length() > 0) {
            intent.putExtra("param", obj.param);
        }

        Notification notification = null;
        String contentText = obj.content;
        //构建PendingIntent
        PendingIntent pi = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //版本兼容

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//兼容Android8.0
            String id ="my_channel_01";
            int importance =NotificationManager.IMPORTANCE_LOW;
            CharSequence name = "notice";
            NotificationChannel mChannel =new NotificationChannel(id, name,importance);
            mChannel.enableLights(true);
            mChannel.setDescription("just show notice");
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            manager.createNotificationChannel(mChannel);

            Notification.Builder builder = new Notification.Builder(context,id);
            builder.setAutoCancel(true)
                    .setContentIntent(pi)
                    .setContentTitle(obj.title)
                    .setContentText(obj.content)
                    .setOngoing(false)
                    .setSmallIcon(obj.icon)
                    .setWhen(System.currentTimeMillis());
            if(obj.subText != null && obj.subText.trim().length() > 0){
                builder.setSubText(obj.subText);
            }
            notification =  builder.build();
        }else if (Build.VERSION.SDK_INT >= 23) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle(obj.title)
                    .setContentText(contentText)
                    .setSmallIcon(obj.icon)
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .setWhen(System.currentTimeMillis());
            if(obj.subText != null && obj.subText.trim().length() > 0){
                builder.setSubText(obj.subText);
            }
            notification = builder.build();
        } else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setAutoCancel(true)
                    .setContentIntent(pi)
                    .setContentTitle(obj.title)
                    .setContentText(obj.content)
                    .setOngoing(false)
                    .setSmallIcon(obj.icon)
                    .setWhen(System.currentTimeMillis());
            if(obj.subText != null && obj.subText.trim().length() > 0){
                builder.setSubText(obj.subText);
            }
            notification =  builder.build();
        }
        if(notification != null){
            manager.notify(nid, notification);
        }
    }

    /**
     * 取消所有通知 同时取消定时闹钟
     * @param context
     */
    public static void clearAllNotifyMsg(Context context){
        try{

            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyMgr.cancelAll();

            SharedPreferences mPreferences = context.getSharedPreferences("SHARE_PREFERENCE_NOTIFICATION",Context.MODE_PRIVATE);
            int max_id = mPreferences.getInt("KEY_MAX_ALARM_ID",0);
            for (int i = 1;i <= max_id;i++){
                AlarmTimerUtil.cancelAlarmTimer(context,"TIMER_ACTION",i);
            }
            //清除数据
            mPreferences.edit().remove("KEY_MAX_ALARM_ID").apply();

        } catch (Exception e){
            Log.e(TAG,"取消通知失败",e);
        }
    }

    public static void clearSpecificNotification(Context context, int alarmId) {
        try {

            NotificationManager mNotifyMgr =
                    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyMgr.cancel(alarmId);

            AlarmTimerUtil.cancelAlarmTimer(context, "TIMER_ACTION", alarmId);

        } catch (Exception e){
            Log.e(TAG,"取消通知失败",e);
        }
    }


}


