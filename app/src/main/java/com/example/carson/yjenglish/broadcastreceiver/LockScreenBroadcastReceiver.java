package com.example.carson.yjenglish.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.carson.yjenglish.music.LockScreenAty;

/**
 * Created by 84594 on 2018/8/30.
 */

public class LockScreenBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            Intent lockScreen = new Intent(context, LockScreenAty.class);
            lockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(lockScreen);
        }
    }
}
