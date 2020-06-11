package com.dawn.androidlibrary.ui.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dawn.androidlibrary.R;
import com.dawn.androidlibrary.service.LBaseService;
import com.dawn.androidlibrary.util.LActivityUtil;
import com.dawn.androidlibrary.util.LStringUtil;
import com.dawn.androidlibrary.view.LCustomToolBar;

import java.util.ArrayList;
import java.util.List;

/**
 * activity基类
 */
@SuppressWarnings("unused")
public abstract class LBaseActivity extends AppCompatActivity {
    protected LCustomToolBar mLCustomToolBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LActivityUtil.addActivity(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//继承AppCompatActivity隐藏标题栏方法
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initBaseData();
        initData();//请求数据
        setContentView(getContextView());//设置xml页面
        mLCustomToolBar = findViewById(R.id.toolbar);//加载标题控件，需要include l_view_title
        initView();//初始化控件
        addListener();//给控件添加值和添加事件
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LActivityUtil.removeActivity(this);
        if(mReceiver != null)
            unregisterReceiver(mReceiver);
    }

    protected abstract void initBaseData();//基类要统一实现的方法
    protected abstract void initData();//请求页面数据
    protected abstract @LayoutRes int getContextView();//获取页面的xml文件
    protected abstract void initView();//初始化控件
    protected abstract void addListener();//给控件赋值或添加事件
    protected void addLeftListener(){}//左侧图标的点击事件
    protected abstract @NonNull String getReceiverAction();//获取广播的action
    protected abstract void getReceiverMsg(Intent intent);//获取广播发过来的信息

    /**
     * 设置标题
     * @param titleText 标题文字
     */
    protected void setTitleText(String titleText){
        if(mLCustomToolBar != null && !LStringUtil.isEmpty(titleText))
            mLCustomToolBar.setMainTitle(titleText);
    }

    /**
     * 设置左侧图片
     * @param leftRes 左侧图片地址
     */
    protected void setLeftRes(@DrawableRes int leftRes){
        if(mLCustomToolBar != null){
            mLCustomToolBar.setMainTitleLeftDrawable(leftRes);
            if(mLCustomToolBar.getMainTitleLeft() != null)
                mLCustomToolBar.getMainTitleLeft().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addLeftListener();
                    }
                });
        }
    }


    /**
     * 隐藏状态栏
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void setWindowStatus(){
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
        View statusBarView = new View(window.getContext());
        int statusBarHeight = getStatusBarHeight(window.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.WHITE);
        decorViewGroup.addView(statusBarView);
    }

    /**
     * 获取状态栏高度
     * @param context 上下文
     * @return 状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 19) {//for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        } else {// lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
    }

    /////////页面跳转开始/////////////
    protected void jumpToActivity(Class<?> mClass){
        startActivity(new Intent(this, mClass));
    }
    protected void jumpToActivity(Class<?> mClass, int requestCode){
        startActivityForResult(new Intent(this, mClass), requestCode);
    }
    protected void jumpToActivity(Class<?> mClass, Bundle bundle){
        startActivity(new Intent(this, mClass).putExtras(bundle));
    }
    protected void jumpToActivity(Class<?> mClass, Bundle bundle, int requestCode){
        startActivityForResult(new Intent(this, mClass).putExtras(bundle), requestCode);
    }
    protected void jumpToActivity(String className) throws ClassNotFoundException {
        Class<?> mClass = Class.forName(className);
        jumpToActivity(mClass);
    }
    protected void jumpToActivity(String className, int requestCode) throws ClassNotFoundException {
        Class<?> mClass = Class.forName(className);
        jumpToActivity(mClass, requestCode);
    }
    protected void jumpToActivity(String className, Bundle bundle) throws ClassNotFoundException {
        Class<?> mClass = Class.forName(className);
        jumpToActivity(mClass, bundle);
    }
    protected void jumpToActivity(String className, Bundle bundle, int requestCode) throws ClassNotFoundException {
        Class<?> mClass = Class.forName(className);
        jumpToActivity(mClass, bundle, requestCode);
    }
    //////////页面跳转结束////////

    //////////////吐司提示开始////////////////
    protected void toast(String msg){
        Toast.makeText(this, LStringUtil.isEmpty(msg) ? "" : msg, Toast.LENGTH_SHORT).show();
    }
    protected void toastLong(String msg){
        Toast.makeText(this, LStringUtil.isEmpty(msg) ? "" : msg, Toast.LENGTH_LONG).show();
    }
    protected void toastUI(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LBaseActivity.this, LStringUtil.isEmpty(msg) ? "" : msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void toastUILong(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LBaseActivity.this, LStringUtil.isEmpty(msg) ? "" : msg, Toast.LENGTH_LONG).show();
            }
        });
    }
    /////////////吐司提示结束/////////////////

    /**
     * 用户退出弹出窗询问用户是否退出
     */
    protected void appExit() {
        new AlertDialog.Builder(this).setMessage("确定关闭吗？ ")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        System.exit(0);
                        LActivityUtil.removeAllActivity();
                    }
                }).setNegativeButton("取消", null).show();
    }

    /**
     * 关闭其他页面
     */
    protected boolean closeOthers(){
        return LActivityUtil.removeOtherActivity(this);
    }

    /**
     * 权限请求码
     */
    private int mRequestCode;
    /**
     * 请求权限
     * @param permissions 需要的权限列表
     * @param requestCode 请求码
     */
    @SuppressWarnings("unused")
    protected void requestPermission(String[] permissions, int requestCode) {
        this.mRequestCode = requestCode;
        if(checkPermissions(permissions)) {
            permissionSuccess(mRequestCode);
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(this, new String[needPermissions.size()], mRequestCode);
        }
    }

    /**
     * 检查所需的权限是否都已授权
     * @param permissions 授权字符串
     * @return 是否授权成功
     */
    private boolean checkPermissions(String[] permissions) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for(String permission : permissions) {
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取所需权限列表中需要申请权限的列表
     * @param permissions 需要授权列表
     * @return 已经授权列表
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for(String permission : permissions) {
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return  needRequestPermissionList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //系统请求权限回调
        if(requestCode == mRequestCode) {
            if(verifyPermissions(grantResults)) {
                permissionSuccess(mRequestCode);
            } else {
                permissionFail(mRequestCode);
            }
        }
    }

    /**
     * 确认所需权限是否都已授权
     * @param grantResults 授权列表
     * @return 是否授权
     */
    private boolean verifyPermissions(int[] grantResults) {
        for(int grantResult : grantResults) {
            if(grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 权限请求成功
     * @param requestCode 权限请求码
     */
    protected void permissionSuccess(int requestCode) {
        Log.e("dawn", "获取权限成功：" + requestCode);
    }

    /**
     * 权限请求失败
     * @param requestCode 权限请求码
     */
    protected void permissionFail(int requestCode) {
        Log.e("dawn", "获取权限失败：" + requestCode);
    }

    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
    protected ActivityBroadcastReceiver mReceiver;

    /**
     * 广播注册
     */
    protected void registerReceiver(){
        if(LStringUtil.isEmpty(getReceiverAction())){
            return;
        }
        mReceiver = new ActivityBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getReceiverAction());
        registerReceiver(mReceiver, intentFilter);
    }

    protected class ActivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent == null)
                return;
            getReceiverMsg(intent);
        }
    }

}
