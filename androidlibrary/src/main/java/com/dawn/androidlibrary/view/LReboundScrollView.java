package com.dawn.androidlibrary.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * 回弹的scroll view
 */
@SuppressWarnings("unused")
public class LReboundScrollView extends ScrollView {
 //拉出屏幕时的拖拽系数,如果是1，几乎相当于拖动效果
 private static final float MOVE_DELAY = 0.3f;
 //回弹动画持续时间
 private static final int ANIM_TIME = 300;
 private View myChildView;
 private boolean bMoved;
 private Rect myOriginalView = new Rect();
 private float startY;
 public LReboundScrollView(Context context, AttributeSet attrs, int defStyle) {
  super(context, attrs, defStyle);
 }
 public LReboundScrollView(Context context, AttributeSet attrs){super(context, attrs); }
 public LReboundScrollView(Context context) {super(context);}
 @Override
 protected void onFinishInflate() {
  super.onFinishInflate();
  if (getChildCount() > 0) { myChildView = getChildAt(0); }
 }
 @Override
 protected void onLayout(boolean changed, int l, int t, int r, int b) {
  super.onLayout(changed, l, t, r, b);
  if (myChildView == null)  return;
  myOriginalView.set(myChildView.getLeft(), myChildView.getTop(),
          myChildView.getRight(), myChildView.getBottom());
 }
 @Override
 public boolean dispatchTouchEvent(MotionEvent ev) {
  if (myChildView == null) { return super.dispatchTouchEvent(ev);}
  int myAction = ev.getAction();
  switch (myAction) {
   case MotionEvent.ACTION_DOWN:
    startY = ev.getY();
    break;
   case MotionEvent.ACTION_UP:
   case MotionEvent.ACTION_CANCEL:
    if (!bMoved)  break;
    //执行回弹动画
    TranslateAnimation myAnim = new TranslateAnimation(0, 0,
            myChildView.getTop(), myOriginalView.top);
    myAnim.setDuration(ANIM_TIME);
    myChildView.startAnimation(myAnim);
    bMoved = false;
    resetViewLayout();
    break;
   case MotionEvent.ACTION_MOVE:
    float nowY = ev.getY();
    int deltaY = (int) (nowY - startY);
    int offset = (int) (deltaY * MOVE_DELAY);
    myChildView.layout(myOriginalView.left, myOriginalView.top + offset,
            myOriginalView.right, myOriginalView.bottom + offset);
    bMoved = true;
    break;
   default:
    break;
  }
  return super.dispatchTouchEvent(ev);
 }
 public void resetViewLayout() {
  myChildView.layout(myOriginalView.left, myOriginalView.top,
          myOriginalView.right, myOriginalView.bottom);
 }  }
