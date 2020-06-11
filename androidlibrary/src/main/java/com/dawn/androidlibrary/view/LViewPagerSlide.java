package com.dawn.androidlibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 90449 on 2018/6/14.
 */
@SuppressWarnings("unused")
public class LViewPagerSlide extends ViewPager {
    private boolean isSlide = false;//是否可以进行滑动

    public void setSlide(boolean slide) {
        isSlide = slide;
    }

    public LViewPagerSlide(Context context) {
        super(context);
    }
    public LViewPagerSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSlide && super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isSlide && super.onTouchEvent(ev);

    }
}
