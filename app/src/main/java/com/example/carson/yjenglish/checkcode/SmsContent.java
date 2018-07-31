package com.example.carson.yjenglish.checkcode;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 84594 on 2018/7/31.
 */

public class SmsContent extends ContentObserver {

    private static SmsContent INSTANCE = null;

    private Cursor cursor = null;
    private Context context;
    private OnReadFinishListener mListener;

    public static SmsContent getInstance(Handler handler, Context ctx, OnReadFinishListener listener) {
        if (INSTANCE == null) {
            INSTANCE = new SmsContent(handler, ctx, listener);
        }
        return INSTANCE;
    }

    private SmsContent(Handler handler, Context context, OnReadFinishListener listener) {
        super(handler);
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        Log.e("SMSTest", "Start");

//        cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), new String[] {
//                "_id", "address", "read", "body"
//        }, null, null, "_id desc");
        cursor = context.getContentResolver().query(Uri.parse("content://sms//inbox"), new String[] {
                "_id", "address", "read", "body"
        }, null, null, "_id desc");
        Log.e("SMSTest", "cursor.isBeforeFirst(): " + cursor.isBeforeFirst() + " cursor.getCount(): " +
                cursor.getCount());
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int smsbodyColumn = cursor.getColumnIndex("body");
            String smsBody = cursor.getString(smsbodyColumn);
            Log.e("SMSTest", "smsBody = " + smsBody);

            if (mListener != null) {
                mListener.onReadFinish(getDynamicCode(smsBody));
            }
        }

        if (Build.VERSION.SDK_INT < 14) {
            cursor.close();
        }

    }

    public String getDynamicCode(String str) {
        Pattern continousNumPattern = Pattern.compile("[0-9\\.]+");
        Matcher m = continousNumPattern.matcher(str);
        String code = "";
        while (m.find()) {
            if (m.group().length() == 4) {
                Log.e("SmsContent", m.group());
                code = m.group();
                break;
            }
        }
        return code;
    }

    public interface OnReadFinishListener {
        void onReadFinish(String smsMsg);
    }

}
