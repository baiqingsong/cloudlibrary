package com.dawn.androidlibrary.util;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by 90449 on 2018/1/9.
 */
@SuppressWarnings("unused")
public class LActivityUtil {
    private static Stack<Activity> mActivityStack;

    /**
     * 添加一个Activity到堆栈中
     */
    public static void addActivity(Activity activity) {
        if (null == mActivityStack) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 从堆栈中移除指定的Activity
     */
    public static void removeActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
        }
    }

    /**
     * 获取顶部的Activity
     */
    public static Activity getTopActivity() {
        if (mActivityStack.isEmpty()) {
            return null;
        } else {
            return mActivityStack.get(mActivityStack.size() - 1);
        }
    }

    /**
     * 结束所有的Activity，退出应用
     */
    public static void removeAllActivity() {
        if (mActivityStack != null && mActivityStack.size() > 0) {
            for (Activity activity : mActivityStack) {
                activity.finish();
            }
        }
    }

    /**
     * 关闭其他页面
     */
    public static boolean removeOtherActivity(Activity activityKeep){
        boolean onlyOnce = false;
        if(mActivityStack != null && mActivityStack.size() > 0 && activityKeep != null){
            if(mActivityStack.size() > 1)
                onlyOnce = true;
            for(Activity activity: mActivityStack){
                if(!activity.getClass().equals(activityKeep.getClass())){
                    activity.finish();
                }
            }
        }
        return onlyOnce;
    }
}
