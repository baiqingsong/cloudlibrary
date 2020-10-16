package com.dawn.androidlibrary.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.dawn.androidlibrary.R;
import com.dawn.androidlibrary.util.LDensityUtil;


/**
 * dialog的基类
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class LBaseDialog {
    private final String TAG = LBaseDialog.class.getSimpleName();
    protected Context mContext;
    private Dialog dialog;
    protected View view;
    private OnLBaseDialogListener mListener;
    private Object[] objs;
    private boolean isOutSide = true;
    private int dialogStyle = R.style.base_dialog;

    /**
     * 构造方法
     * @param context 上下文
     * @param listener 回调接口
     * @param objs 其他参数
     */
    public LBaseDialog(Context context, OnLBaseDialogListener listener, Object ... objs) {
        this.mContext = context;
        this.mListener = listener;
        this.objs = objs;
    }

    /**
     * 设置回调
     * @param listener 回调接口
     */
    public void setListener(OnLBaseDialogListener listener){
        this.mListener = listener;
    }

    public OnLBaseDialogListener getListener(){return mListener;}

    public void setOutSide(boolean isOutSide){
        this.isOutSide = isOutSide;
    }
    public void setDialogStyle(int dialogStyle){
        this.dialogStyle = dialogStyle;
    }

    /**
     * 显示dialog
     */
    public void showDialog(){
        setDialog(isOutSide);
        if(dialog != null)
            dialog.show();
    }

    /**
     * 取消dialog
     */
    public void dismissDialog(){
        if(dialog != null){
            stopProcess();
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 获取dialog实例
     * @return 实体类
     */
    public Dialog getDialog(){
        return dialog;
    }
    private void setDialog(boolean isOutSide){
        if(dialog == null){
            dialog = new Dialog(mContext, this.dialogStyle);
            view  = LayoutInflater.from(mContext).inflate(getDialogLayoutRes(), null, false);
            if(view != null){
                dialog.setContentView(view);
                dialog.setCanceledOnTouchOutside(isOutSide);
                dialog.setCancelable(isOutSide);
                setDialogListener(objs);
            }else{
                dialog = null;
            }

        }
    }

    /**
     * 设置弹窗内容全屏
     */
    protected void setDialogFullScreen(){
        if(view != null && mContext != null){
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LDensityUtil.getScreenW(mContext), LDensityUtil.getScreenH(mContext));
            view.setLayoutParams(layoutParams);
        }
    }

    /**
     * 设置弹窗居中
     */
    protected void setDialogCenter(){
        if(dialog == null)
            return;
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        }
    }

    protected abstract @LayoutRes int getDialogLayoutRes();

    protected abstract void setDialogListener(Object[] objs);

    protected abstract void stopProcess();

    protected interface OnLBaseDialogListener{

    }
}
