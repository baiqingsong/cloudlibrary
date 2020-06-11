package com.dawn.androidlibrary.util;

import android.os.SystemClock;
@SuppressWarnings("unused")
public class LClickUtil {
    private int counts;//点击次数
    private long duration;//点击间隔的有效时间
    private long[] mHits;
    private OnClickSumListener mListener;

    public LClickUtil(int counts, long duration, OnClickSumListener listener){
        this.counts = counts;
        this.duration = duration;
        this.mListener = listener;
        mHits = new long[counts];
    }
    public void continuousClick(){
        System.arraycopy(mHits, 1, mHits, 0, mHits.length -1);//每次点击时，数组向前移动一位
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();//为数组最后一位赋值
        if(mHits[0] >= (SystemClock.uptimeMillis() - duration)){
            mHits = new long[counts];//重新初始化数组
            if(mListener != null)
                mListener.clickSum();
        }
    }
    public interface OnClickSumListener{
        void clickSum();
    }
}
