package com.dawn.androidlibrary.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


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

    /**
     * 保存bitmap到文件
     * @param bitmap 图片
     * @param imgPath 保存路径
     * @param imgName 保存文件名
     */
    public static String saveBitmapToFile(Bitmap bitmap, String imgPath, String imgName){
        if(LStringUtil.isEmpty(imgPath))
            return null;
        if(LStringUtil.isEmpty(imgName))
            return null;
        //判断指定文件夹的路径是否存在
        File fileDir = new File(imgPath);
        if(!fileDir.exists())
            fileDir.mkdir();
        //如果指定文件夹创建成功，那么我们则需要进行图片存储操作
        File saveFile = new File(imgPath, imgName);
        if(saveFile.exists()){
            saveFile.delete();
        }
        try {
            saveFile.createNewFile();
            FileOutputStream saveImgOut = new FileOutputStream(saveFile);
            // compress - 压缩的意思
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, saveImgOut);
            //存储完成后需要清除相关的进程
            saveImgOut.flush();
            saveImgOut.close();
            return imgPath + imgName;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * bitmap转base64
     * @param bitmap 图片
     */
    public static String bitmapToBase64(Bitmap bitmap){
        if (bitmap == null) {
            return "";
        }
        String string = "";
        try{
            ByteArrayOutputStream btString = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, btString);
            byte[] bytes = btString.toByteArray();
            string = Base64.encodeToString(bytes, Base64.URL_SAFE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return string;
    }

    /**
     * base64转bitmap
     * @param base64Data base64数据
     */
    public static Bitmap base64ToBitmap(String base64Data){
        if (TextUtils.isEmpty(base64Data)) {
            return null;
        }
        Bitmap bitmap = null;
        try{
            byte[] decode = Base64.decode(base64Data.toString().trim(), Base64.URL_SAFE);
            bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 从Asset文件中读取bitmap
     * @param context   上下文
     * @param fileName 文件名
     * @param scale 缩放比例
     */
    public static Bitmap readBitmapFromAssets(Context context, String fileName, int scale){
        //以最省内存的方式读取本地资源的图片
        BitmapFactory.Options options = getOption(scale);
        AssetManager asm = context.getAssets();
        InputStream is;
        Bitmap bitmap = null;
        try {
            is = asm.open(fileName);
            bitmap = BitmapFactory.decodeStream(is,null, options);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 从SD卡文件中读取bitmap
     * @param context  上下文
     * @param fileName 文件名
     * @param scale 缩放比例
     */
    public static Bitmap readBitmapFromFile(Context context, String fileName, int scale){
        BitmapFactory.Options options = getOption(scale);
        Bitmap bitmap = null;
        try {
            FileInputStream fis = new FileInputStream(fileName);
            bitmap = BitmapFactory.decodeStream(fis, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 获取Options
     * @param scale 缩放比例
     */
    private static BitmapFactory.Options getOption(int scale){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = scale;//图片宽高都为原来的二分之一，即图片为原来的四分之一
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return options;
    }

    /**
     * 从路径中提取名称
     * @param path 路径
     * @return 名称
     */
    public static String getFileName(String path){

        int start=path.lastIndexOf("/");
        int end=path.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return path.substring(start+1,end);
        }else{
            return null;
        }

    }

    /**
     * 获取文件全名
     * @param path 路径
     * @return 文件名
     */
    public static String getAllFileName(String path){

        int start=path.lastIndexOf("/");
        if(start!=-1){
            return path.substring(start+1);
        }else{
            return null;
        }
    }

    /**
     * 图片旋转
     * @param bm 旋转前图片
     * @param orientationDegree 旋转角度
     * @return 旋转后图片
     */
    public static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else if (orientationDegree == 270) {
            targetX = 0;
            targetY = bm.getWidth();
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }

        final float[] values = new float[9];
        m.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];

        m.postTranslate(targetX - x1, targetY - y1);

        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);

        if(bm.isRecycled())
            bm.recycle();

        return bm1;
    }

}
