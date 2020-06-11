package com.dawn.androidlibrary.util;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * 图片展示工具类
 * 依赖Picasso包
 */
@SuppressWarnings("unused")
public class LImageUtil {
    /**
     * 显示本地图片
     * @param imgRes 图片地址
     * @param imageView 控件
     */
    public static void displayImage(Context context, @IdRes int imgRes, ImageView imageView){
        Picasso.with(context).load(imgRes).into(imageView);
    }

    /**
     * 显示网络，assets下，SD卡下图片
     * 网络图片imgUrl直接是图片地址
     * assets下图片imgUrl格式"file:///android_asset/..."
     * sd卡下图片imgUrl格式"file:///sdcard/..."
     * @param imgUrl 图片地址
     * @param imageView 控件
     */
    public static void displayImage(Context context, String imgUrl, ImageView imageView){
        Picasso.with(context).load(imgUrl).into(imageView);
    }

    /**
     * 显示uri图片
     * @param uri 图片uri
     * @param imageView 控件
     */
    public static void displayImage(Context context, Uri uri, ImageView imageView){
        Picasso.with(context).load(uri).into(imageView);
    }

    /**
     * 显示url图片，加载中或者错误显示默认图片
     * @param imgUrl 图片地址
     * @param imageView 控件
     * @param imgDefault 默认图片
     */
    public static void displayImage(Context context, String imgUrl, ImageView imageView, @IdRes int imgDefault){
        Picasso.with(context).load(imgUrl).error(imgDefault).placeholder(imgDefault).into(imageView);
    }
}
