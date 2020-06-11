package com.dawn.androidlibrary.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;

/**
 * 滚动文字的text view
 */
@SuppressWarnings("unused")
public class LScrollTextView extends AppCompatTextView {
    int curLines = 0, myLines = 0, perLines = 10;
    int lineHeight, myHeight, i = 0;
    boolean bScroll = true;
    private ScrollHandler mHandler = new ScrollHandler(this);
    public LScrollTextView(Context context) {
        super(context);
        init();
    }
    public LScrollTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }
    public void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //noinspection InfiniteLoopStatement
                while (true){
                    if(!bScroll){
                        continue;
                    }
                    if(myHeight != 0){
                        curLines += 1;
                        if(curLines == (myLines - 3) * perLines){
                            curLines = 0;
                        }
                        Message message = mHandler.obtainMessage();
                        message.arg1 = curLines;
                        message.sendToTarget();
                        try{
                            if(curLines == 0)
                                Thread.sleep(1000);
                            else
                                Thread.sleep(150);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        curLines = 0;
        postInvalidate();
        lineHeight = getLineHeight() / perLines;
        myLines = getLineCount() - 1;
        myHeight = getLineCount() * lineHeight * perLines;
        int height = getMeasuredHeight();
        i = (int)(height/getTextSize());
        bScroll = myLines > 1;
    }

    private static class ScrollHandler extends Handler{
        private WeakReference<LScrollTextView> weakReference;
        ScrollHandler(LScrollTextView scrollTextView){
            weakReference = new WeakReference<>(scrollTextView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LScrollTextView scrollTextView = weakReference.get();
            scrollTextView.scrollTo(0, scrollTextView.lineHeight * scrollTextView.curLines);
        }
    }
}
