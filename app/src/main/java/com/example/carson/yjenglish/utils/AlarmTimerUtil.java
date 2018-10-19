package com.example.carson.yjenglish.utils;

/**
 * Created by 84594 on 2018/10/13.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.io.Serializable;
import java.util.Map;

/**
 * 定时闹钟工具类
 */
public class AlarmTimerUtil {

    /**
     * 设置定时闹钟
     * @param context
     * @param alarmId
     * @param time
     * @param action
     * @param map 要传递的参数
     */

    public static void setAlarmTimer(Context context, int alarmId, long time, String action,
                                     Map<String, Serializable> map) {
        Intent mIntent = new Intent();
        mIntent.setAction(action);
        if (map != null) {
            for (String key : map.keySet()) {
                mIntent.putExtra(key, map.get(key));
            }
        }

        PendingIntent sender = PendingIntent.getService(context, alarmId, mIntent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //MARSHMALLOW OR ABOVE
            if (alarmId == UserConfig.ALARM_START_STUDY_ID) {
                Log.e("Alarm", "equals target id");
                assert alarmManager != null;
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time,
                        AlarmManager.INTERVAL_DAY, sender);
            } else {
                assert alarmManager != null;
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, sender);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //LOLLIPOP 21 OR ABOVE
            if (alarmId == UserConfig.ALARM_START_STUDY_ID) {
                Log.e("Alarm", "equals target id");
                assert alarmManager != null;
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time,
                        AlarmManager.INTERVAL_DAY, sender);
            } else {
                AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(time, sender);
                assert alarmManager != null;
                alarmManager.setAlarmClock(alarmClockInfo, sender);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //KITKAT 19 OR ABOVE
            if (alarmId == UserConfig.ALARM_START_STUDY_ID) {
                Log.e("Alarm", "equals target id");
                assert alarmManager != null;
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time,
                        AlarmManager.INTERVAL_DAY, sender);
            } else {
                assert alarmManager != null;
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, sender);
            }
        } else { //FOR BELOW KITKAT ALL DEVICES
            if (alarmId == UserConfig.ALARM_START_STUDY_ID) {
                Log.e("Alarm", "equals target id");
                assert alarmManager != null;
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time,
                        AlarmManager.INTERVAL_DAY, sender);
            } else {
                assert alarmManager != null;
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, sender);
            }
        }

    }

    public static void cancelAlarmTimer(Context context, String action, int alarmId) {
        Intent mIntent = new Intent();
        mIntent.setAction(action);

        PendingIntent sender = PendingIntent.getService(context, alarmId, mIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
