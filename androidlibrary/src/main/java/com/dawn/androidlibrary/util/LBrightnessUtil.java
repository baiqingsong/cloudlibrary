package com.dawn.androidlibrary.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.Settings;

/**
 * 亮度调节工具类
 */
@SuppressWarnings("unused")
public class LBrightnessUtil {
    /**
     * 设置手动设置显示屏幕亮度模式
     */
    private static void setBrightnessModelManual(Activity activity){
        ContentResolver contentResolver = activity.getContentResolver();
        try {
            int mode = Settings.System.getInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置自动设置显示屏幕亮度模式
     */
    public static void setBrightnessModelAuto(Activity activity){
        ContentResolver contentResolver = activity.getContentResolver();
        try {
            int mode = Settings.System.getInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL) {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取屏幕亮度
     * @return
     * <uses-permission android:name="android.permission.WRITE_SETTINGS"/>
     */
    public static int getBrightness(Activity activity){
        ContentResolver contentResolver = activity.getContentResolver();
        int defVal = 125;
        return Settings.System.getInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, defVal);
    }

    /**
     * 设置屏幕亮度，范围是0-255
     * @param brightness 亮度值
     */
    public static void setBrightness(Activity activity, int brightness){
        setBrightnessModelManual(activity);
        ContentResolver contentResolver = activity.getContentResolver();
        Settings.System.putInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, brightness);
    }
}
