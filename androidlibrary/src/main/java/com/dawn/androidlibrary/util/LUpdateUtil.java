package com.dawn.androidlibrary.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 版本更新工具类
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class LUpdateUtil {
    private final static String TAG = LUpdateUtil.class.getSimpleName();
    private ProgressDialog pd;

    /**
     * 从服务器中下载APK
     * @param url 地址
     */
    public void downLoadApk(final Activity activity, final String url, final InstallApkListener listener) {
//        final Activity activity = activity1.getParent();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                final ProgressDialog pd;    //进度条对话框
                pd = new ProgressDialog(activity);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setMessage("正在下载更新版本");
                if(!activity.isFinishing())
                    pd.show();
                new Thread() {
                    @Override
                    public void run() {
                        try{
                            File file = getFileFromServer(activity, url, pd);
                            sleep(5000);
                            dismissProgress(); //结束掉进度条对话框
                            if(file != null){
                                if(listener != null)
                                    listener.installApkMethod(file.getAbsolutePath());
                                else
                                    installApk(activity, file);
                            }else{
                                if(listener != null)
                                    listener.rebootSystem();
                                else
                                    LDeviceUtil.reboot();
                            }
                        } catch(Exception e) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "版本更新失败!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                            LLog.exception(TAG, e);
                            if(listener != null)
                                listener.rebootSystem();
                            else
                                LDeviceUtil.reboot();
                        }
                    }
                }.start();
            }
        });

    }

    /**
     * 关闭下载进度条
     */
    public void dismissProgress(){
        if(pd != null)
            pd.dismiss();
    }

    /**
     * 安装apk
     * @param file 路径
     */
    public void installApk(Context context, File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".FileProvider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        //执行的数据类型
        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * apk更新的提示窗
     * @param url 路径
     */
    public void showUpdateDialog(final Activity activity, final String url, final UpdateListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("更新");
        builder.setMessage("检测到有更新,是否立刻更新？");
        builder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null)
                    listener.updateFail();
            }
        });
        builder.setPositiveButton("立刻更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!LNetUtil.isWiFi(activity)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("提示");
                    builder.setMessage("您当前正在使用移动网络，继续下载将消耗流量");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downLoadApk(activity,url, null);
                        }
                    });
                    builder.create().show();
                } else{
                    downLoadApk(activity, url, null);
                }
            }
        });
        builder.create().show();
    }

    /**
     * 根据地址下载文件并显示到进度条控件上
     * @param path 地址
     * @param pd 控件
     */
    public static File getFileFromServer(Context context, String path, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String tempStr = path.substring(path.lastIndexOf("/") + 1);
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            if(pd != null)
                pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(context.getExternalCacheDir(), tempStr);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                if(pd != null)
                    pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else{
            return null;
        }
    }
    public interface UpdateListener{
        void updateFail();
    }

    public interface InstallApkListener{
        void installApkMethod(String url);
        void rebootSystem();
    }

}
