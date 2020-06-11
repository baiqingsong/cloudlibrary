package com.dawn.androidlibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dawn.androidlibrary.util.LNetUtil;


/**
 * 网络状态监听广播类
<receiver android:name=".receiver.LNetworkStateReceiver">
    <intent-filter>
    <action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
    <category android:name="android.intent.category.DEFAULT"/>
    </intent-filter>
</receiver>
 */
public abstract class LNetworkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager == null){
                noNetwork();
                return;
            }
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                //恢复网络 可以再这里进行一些网络突然断开未完成的操作
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        boolean hasNetwork = LNetUtil.ping("www.baidu.com", 4, new StringBuffer());
                        if(hasNetwork){
                            hasNetwork();
                        }else{
                            hasNetworkNoPing();
                        }
                    }
                }.start();

            }else{
                //网络断开
                noNetwork();
            }
        }
    }

    public abstract void hasNetwork();//有网络
    public abstract void noNetwork();//无网络
    public abstract void hasNetworkNoPing();//有网络但是不能上网



}


