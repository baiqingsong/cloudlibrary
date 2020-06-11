package com.dawn.androidlibrary.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
@SuppressWarnings("unused")
public class LCrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = LCrashHandler.class.getSimpleName();
    private static final boolean DEBUG = true;
    /**
     * 文件名
     */
    public static final String FILE_NAME = "crash";
    /**
     * 异常日志 存储位置为根目录下的 Crash文件夹
     */
    private static String PATH = null;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);//日期格式;
    /**
     * 文件名后缀
     */
    private static final String FILE_NAME_SUFFIX = ".trace";

    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private String fileNameException;


    private static LCrashHandler sInstance = new LCrashHandler();
    private LCrashHandler() {}
    public static LCrashHandler getInstance() {
        return sInstance;
    }

    private OnSetCrashHandlerListener mListener;
    public void setListener(OnSetCrashHandlerListener listener){
        this.mListener = listener;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        //得到系统的应用异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前应用异常处理器改为默认的
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();

        PATH = getFilePath(context);
        checkFilePath(PATH);
    }


    /**
     * 这个是最关键的函数，当系统中有未被捕获的异常，系统将会自动调用 uncaughtException 方法
     *
     * @param thread 为出现未捕获异常的线程
     * @param ex     为未捕获的异常 ，可以通过e 拿到异常信息
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        //导入异常信息到SD卡中
        dumpExceptionToSDCard(ex);
        //这里可以上传异常信息到服务器，便于开发人员分析日志从而解决Bug
        uploadExceptionToServer(ex);
//        ex.printStackTrace();
        //如果系统提供了默认的异常处理器，则交给系统去结束程序，否则就由自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            Process.killProcess(Process.myPid());
        }

    }

    /**
     * 将异常信息写入SD卡
     */
    private void dumpExceptionToSDCard(Throwable e) {
        //如果SD卡不存在或无法使用，则无法将异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                return;
            }
        }
        checkFilePath(PATH);//检测文件夹

        String fileName = getFileName(new Date());//log日志名，使用时间命名，保证不重复

        //得到当前年月日时分秒
        long current = System.currentTimeMillis();
        Date date = new Date(current);
        String time = dateFormat.format(date);
        fileNameException = getFileName(date);
        //在定义的Crash文件夹下创建文件
        checkFileSize(fileName);

        try{
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(fileNameException))));
            //写入时间
            pw.println(time);
            //写入手机信息
            dumpPhoneInfo(pw);
            pw.println();//换行
            e.printStackTrace(pw);
            pw.close();//关闭输入流
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    /**
     * 获取手机各项信息
     */
    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        //得到包管理器
        PackageManager pm = mContext.getPackageManager();
        //得到包对象
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(),PackageManager.GET_ACTIVITIES);
        //写入APP版本号
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print("_");
        pw.println(pi.versionCode);
        //写入 Android 版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        //CPU架构
        pw.print("CPU ABI: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pw.println(Arrays.toString(Build.SUPPORTED_ABIS));
        }else {
            pw.println(Build.CPU_ABI);
        }
    }

    /**
     * 获得文件存储路径
     */
    @SuppressWarnings("WeakerAccess")
    public static String getFilePath(Context context) {
        if(context == null)
            return null;
        if (!Environment.isExternalStorageRemovable()) {//如果外部储存可用
            File file = context.getExternalFilesDir(null);
            if(file != null && file.exists()){
                return file.getPath() + "/Crash/";//获得外部存储路径,默认路径为 /storage/emulated/Android/data/com.../files/Logs/log_2018-03-14.txt
            }else
                return null;
        } else {
            return context.getFilesDir().getPath() + "/Crash/";//直接存在/data/data里，非root手机是看不到的
        }
    }

    /**
     * 获取日志写入文件名称
     * @param date 日期
     */
    @SuppressWarnings("WeakerAccess")
    public static String getFileName(Date date){
        return PATH + "crash_" + dateFormat.format(date) + ".trace";
    }

    /**
     * 检查文件目录，主要是查看是否存在，是否暂用内存过多
     * @param pathName 路径
     */
    private static void checkFilePath(String pathName){
        File file = new File(pathName);
        if (file.exists()) {
            if (getFolderSize(file) > 100 * 1024 * 1024) {
                deleteFolder(file);
            }
        } else {
            file.mkdirs();
        }
    }

    /**
     * 查看文件大小，限制文件小于20M
     * @param fileName 文件地址
     */
    private static void checkFileSize(String fileName){
        try{
            File file = new File(fileName);
            if(file.exists() && file.length() > 10 * 1024  * 1024){
                file.delete();
            }
            file.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取文件大小
     * @param file 文件
     */
    @SuppressWarnings("WeakerAccess")
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for(File fileSingle: fileList){
                if(fileSingle.isDirectory()){
                    size +=  getFolderSize(fileSingle);
                }else{
                    size += fileSingle.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除日志
     * @param file 需要删除的文件
     */
    @SuppressWarnings("WeakerAccess")
    public static boolean deleteFolder(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            boolean deleteAll = true;
            for(File fileSingle: files){
                if(!deleteFolder(fileSingle)){
                    deleteAll = false;
                }
            }
            return deleteAll;
//            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    /**
     * 将错误信息上传至服务器
     */
    private void uploadExceptionToServer(Throwable ex) {
        if(mListener != null)
            mListener.exceptionHandling(mContext, ex, fileNameException);
    }
    public interface OnSetCrashHandlerListener{
        void exceptionHandling(Context context, Throwable e, String fileName);
    }
}
