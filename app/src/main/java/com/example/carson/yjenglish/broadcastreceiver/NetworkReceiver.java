package com.example.carson.yjenglish.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.example.carson.yjenglish.utils.NetUtils;

/**
 * Created by 84594 on 2018/8/18.
 */

public class NetworkReceiver extends BroadcastReceiver {

    private NetUtils.NetEvent netEvent;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            //检查网络状态的类型
            int networkState = NetUtils.getNetWorkState(context);
            if (netEvent != null) {
                netEvent.onNetworkChange(networkState);
            }
        }
    }

    public void setNetEvent(NetUtils.NetEvent netEvent) {
        this.netEvent = netEvent;
    }
}
