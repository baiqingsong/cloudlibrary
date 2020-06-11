package com.dawn.androidlibrary.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class LBaseService extends Service {
    protected ServiceBroadcastReceiver mReceiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mReceiver != null)
            unregisterReceiver(mReceiver);
    }

    /**
     * 广播注册
     */
    private void registerReceiver(){
        mReceiver = new ServiceBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getReceiverAction());
        registerReceiver(mReceiver, intentFilter);
    }

    protected class ServiceBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent == null)
                return;
            getReceiverMsg(intent);
        }
    }

    protected abstract @NonNull String getReceiverAction();
    protected abstract void getReceiverMsg(Intent intent);
}
