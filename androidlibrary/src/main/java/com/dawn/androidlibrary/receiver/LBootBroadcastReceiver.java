package com.dawn.androidlibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 开机启动广播
 *
 <receiver android:name=".receiver.BootBroadcastReceiver"
     android:enabled="true"
     android:exported="true">
     <intent-filter>
         <action android:name="android.intent.action.BOOT_COMPLETED"></action>
         <category android:name="android.intent.category.LAUNCHER" />
         <category android:name="android.intent.category.HOME"/>
     </intent-filter>
 </receiver>
 */
public abstract class LBootBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(ACTION.equals(intent.getAction())){
            Intent intentStart = new Intent(context, getOpenScreenActivity());
            intentStart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentStart);
        }
    }
    public abstract Class getOpenScreenActivity();
}
