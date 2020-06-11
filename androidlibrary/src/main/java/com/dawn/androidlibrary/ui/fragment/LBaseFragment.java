package com.dawn.androidlibrary.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dawn.androidlibrary.LBaseApplication;
import com.dawn.androidlibrary.util.LStringUtil;


/**
 * fragment的基类
 */
@SuppressWarnings("unused")
public abstract class LBaseFragment extends Fragment {
    protected Activity activity;
    protected View rootView;
    public Context getContext() {
        if (activity == null) {
            return LBaseApplication.getAppContext();
        }
        return activity;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();
        if(rootView == null){
            rootView = inflater.inflate(getContextView(), container, false);
        }
        initView();
        addListener();
        return rootView;
    }
    protected abstract void initData();
    protected abstract @LayoutRes int getContextView();
    protected abstract void initView();
    protected abstract void addListener();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup)rootView.getParent()).removeView(rootView);
    }

    protected void jumpToActivity(Class<?> mClass){
        startActivity(new Intent(getContext(), mClass));
    }
    protected void jumpToActivity(Class<?> mClass, int requestCode){
        startActivityForResult(new Intent(getContext(), mClass), requestCode);
    }
    protected void jumpToActivity(Class<?> mClass, Bundle bundle){
        startActivity(new Intent(getContext(), mClass).putExtras(bundle));
    }
    protected void jumpToActivity(Class<?> mClass, Bundle bundle, int requestCode){
        startActivityForResult(new Intent(getContext(), mClass).putExtras(bundle), requestCode);
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
    protected void toast(String msg){
        Toast.makeText(getContext(), LStringUtil.isEmpty(msg) ? "" : msg, Toast.LENGTH_SHORT).show();
    }
    protected void toastLong(String msg){
        Toast.makeText(getContext(), LStringUtil.isEmpty(msg) ? "" : msg, Toast.LENGTH_LONG).show();
    }
}
