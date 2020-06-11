package com.dawn.androidlibrary.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 * 日志工具类
 */
@SuppressWarnings("unused")
public class LLog {
    private static String TAG = "ghost";
    private static boolean LOG_DEBUG = true;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final int VERBOSE = 2;
    private static final int DEBUG = 3;
    private static final int INFO = 4;
    private static final int WARN = 5;
    private static final int ERROR = 6;
    private static final int ASSERT = 7;
    private static final int JSON = 8;
    private static final int XML = 9;
    private static final char CHAR_VERBOSE = 'v';
    private static final char CHAR_DEBUG = 'd';
    private static final char CHAR_INFO = 'i';
    private static final char CHAR_WARN = 'w';
    private static final char CHAR_ERROR = 'e';

    private static final int JSON_INDENT = 4;

    private static String logPath = null;//log日志存放路径

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);//日期格式;
    private static SimpleDateFormat dateFormat_log = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);//日期格式;

    /**
     * 初始化日志开关和TAG（默认之日为开，TAG为‘ghost’）
     * @param isDebug 是否打印
     * @param tag 日志tag
     */
    public static void init(Context context, boolean isDebug, String tag) {
        TAG = tag;
        LOG_DEBUG = isDebug;
        logPath = getFilePath(context);//获得文件储存路径,在后面加"/Logs"建立子文件夹
        checkFilePath(logPath);
    }

    /**
     * VERBOSE
     * @param msg 内容
     */
    public static void v(String msg) {
        log(VERBOSE, null, msg);
        writeToFile(CHAR_VERBOSE, "", msg);
    }

    /**
     * 带tag的VERBOSE
     * @param tag 标识
     * @param msg 内容
     */
    public static void v(String tag, String msg) {
        log(VERBOSE, tag, msg);
        writeToFile(CHAR_VERBOSE, tag, msg);
    }

    /**
     * DEBUG
     * @param msg 内容
     */
    public static void d(String msg) {
        log(DEBUG, null, msg);
        writeToFile(CHAR_DEBUG, "", msg);
    }

    /**
     * 带tag的DEBUG
     * @param tag 标识
     * @param msg 内容
     */
    public static void d(String tag, String msg) {
        log(DEBUG, tag, msg);
        writeToFile(CHAR_DEBUG, tag, msg);
    }

    /**
     * INFO
     * @param msg 内容
     */
    public static void i(Object... msg) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : msg) {
            sb.append(obj);
            sb.append(",");
        }
        log(INFO, null, String.valueOf(sb));
        writeToFile(CHAR_INFO, "", String.valueOf(sb));
    }

    /**
     * WARN
     * @param msg 内容
     */
    public static void w(String msg) {
        log(WARN, null, msg);
        writeToFile(CHAR_WARN, "", msg);
    }

    /**
     * 带tag的WARN
     * @param tag 标识
     * @param msg 内容
     */
    public static void w(String tag, String msg) {
        log(WARN, tag, msg);
        writeToFile(CHAR_WARN, tag, msg);
    }

    /**
     * ERROR
     * @param msg 内容
     */
    public static void e(String msg) {
        msg = "报错：" + msg;
        log(ERROR, null, msg);
        writeToFile(CHAR_ERROR, "", msg);
    }

    /**
     * 带tag的ERROR
     * @param msg 内容
     * @param tr 报错
     */
    public static void e(String msg, Throwable tr) {
        String errorStr = msg + "\n" + tr.getMessage() + "\n" + getThrowableStr(tr);
        log(ERROR, null, errorStr);
        writeToFile(CHAR_ERROR, "", errorStr);
    }

    /**
     * ASSERT
     * @param msg 内容
     */
    public static void a(String msg) {
        log(ASSERT, null, msg);
    }

    /**
     * 带tag的ASSERT
     * @param tag 标识
     * @param msg 内容
     */
    public static void a(String tag, String msg) {
        log(ASSERT, tag, msg);
    }

    /**
     * 输出json
     * @param json 字符串
     */
    public static void json(String json) {
        log(JSON, null, json);
    }

    /**
     * 带tag的json输出
     * @param tag 标识
     * @param json json字符串
     */
    public static void json(String tag, String json) {
        log(JSON, tag, json);
    }

    /**
     * 输出xml
     * @param xml 字符串
     */
    public static void xml(String xml) {
        log(XML, null, xml);
    }

    /**
     * 带tag的输出xml
     * @param tag 标识
     * @param xml 字符串
     */
    public static void xml(String tag, String xml) {
        log(XML, tag, xml);
    }

    /**
     * 输出异常信息
     * @param e 报错
     */
    public static void exception(Exception e){
        writeToFile(CHAR_ERROR, "", Log.getStackTraceString(e));
    }

    /**
     * 输出带tag的异常信息
     * @param tag 标识
     * @param e 异常
     */
    public static void exception(String tag, Exception e){
        writeToFile(CHAR_ERROR, tag, Log.getStackTraceString(e));
    }

    /**
     * 打印日志
     * @param logType 级别
     * @param tagStr 标识
     * @param objects 内容
     */
    private static void log(int logType, String tagStr, Object objects) {
        String[] contents = wrapperContent(tagStr, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];
        if (LOG_DEBUG) {
            switch (logType) {
                case VERBOSE:
                case DEBUG:
                case INFO:
                case WARN:
                case ERROR:
                case ASSERT:
                    printDefault(logType, tag, headString + msg);
                    break;
                case JSON:
                    printJson(tag, msg, headString);
                    break;
                case XML:
                    printXml(tag, msg, headString);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 打印正常log，并且将长的log截断
     * @param type 类型
     * @param tag 标识
     * @param msg 内容
     */
    private static void printDefault(int type, String tag, String msg) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        int index = 0;
        int maxLength = 4000;
        int countOfSub = msg.length() / maxLength;

        if (countOfSub > 0) {  // The log is so long
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                printSub(type, tag, sub);
                index += maxLength;
            }
            //printSub(type, msg.substring(index, msg.length()));
        } else {
            printSub(type, tag, msg);
        }

    }

    /**
     * 过长的log打印
     * @param type 类型
     * @param tag 标识
     * @param sub 内容
     */
    private static void printSub(int type, String tag, String sub) {
        if (tag == null) {
            tag = TAG;
        }
        printLine(tag, true);
        switch (type) {
            case VERBOSE:
                Log.v(tag, sub);
                break;
            case DEBUG:
                Log.d(tag, sub);
                break;
            case INFO:
                Log.i(tag, sub);
                break;
            case WARN:
                Log.w(tag, sub);
                break;
            case ERROR:
                Log.e(tag, sub);
                break;
            case ASSERT:
                Log.wtf(tag, sub);
                break;
        }
        printLine(tag, false);
    }

    /**
     * 打印json
     * @param tag 标识
     * @param json 内容
     * @param headString 头信息
     */
    private static void printJson(String tag, String json, String headString) {
        if (TextUtils.isEmpty(json)) {
            d("Empty/Null json content");
            return;
        }
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        String message;

        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                message = jsonObject.toString(JSON_INDENT);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                message = jsonArray.toString(JSON_INDENT);
            } else {
                message = json;
            }
        } catch (JSONException e) {
            message = json;
        }

        printLine(tag, true);
        message = headString + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "|" + line);
        }
        printLine(tag, false);
    }

    /**
     * 打印xml
     * @param tag 标识
     * @param xml 内容
     * @param headString 头内容
     */
    private static void printXml(String tag, String xml, String headString) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        if (xml != null) {
            try {
                Source xmlInput = new StreamSource(new StringReader(xml));
                StreamResult xmlOutput = new StreamResult(new StringWriter());
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(xmlInput, xmlOutput);
                xml = xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
            xml = headString + "\n" + xml;
        } else {
            xml = headString + "Log with null object";
        }

        printLine(tag, true);
        String[] lines = xml.split(LINE_SEPARATOR);
        for (String line : lines) {
            if (!TextUtils.isEmpty(line)) {
                Log.d(tag, "|" + line);
            }
        }
        printLine(tag, false);
    }

    /**
     * 获取log的信息，包括头，信息，方法名等显示信息
     * @param tag 标识
     * @param objects 内容
     */
    private static String[] wrapperContent(String tag, Object... objects) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement targetElement = stackTrace[5];
        String className = targetElement.getClassName();
        className = className.replaceAll("\\$\\d", "");
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + ".java";
        }
        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();
        if (lineNumber < 0) {
            lineNumber = 0;
        }
        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        String msg = (objects == null) ? "Log with null object" : getObjectsString(objects);
        String headString = "[(" + className + ":" + lineNumber + ")#" + methodNameShort + " ] ";
        return new String[]{tag, msg, headString};
    }

    /**
     * 多条信息整理成一条字符串
     * @param objects 内容
     */
    private static String getObjectsString(Object... objects) {

        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append("param").append("[").append(i).append("]").append(" = ").append("null").append("\n");
                } else {
                    stringBuilder.append("param").append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
                }
            }
            return stringBuilder.toString();
        } else {
            Object object = objects[0];
            return object == null ? "null" : object.toString();
        }
    }

    /**
     * 显示上下线，用于区分每条日志
     * @param tag 标识
     * @param isTop 是否是上边的日志
     */
    private static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }


    /**
     * 获得文件存储路径
     */
    @SuppressWarnings("WeakerAccess")
    public static String getFilePath(Context context) {
        if ( !Environment.isExternalStorageRemovable()) {//如果外部储存可用
            File file = context.getExternalFilesDir(null);
            if(file != null && file.exists())
                return file.getPath() + "/Logs/";//获得外部存储路径,默认路径为 /storage/emulated/Android/data/com.../files/Logs/log_2018-03-14.txt
            else
                return null;
        } else {
            return context.getFilesDir().getPath() + "/Logs/";//直接存在/data/data里，非root手机是看不到的
        }
    }

    /**
     * 检查文件目录，主要是查看是否存在，是否暂用内存过多
     * @param pathName 文件名称
     */
    @SuppressWarnings("WeakerAccess")
    public static void checkFilePath(String pathName){
        File file = new File(pathName);
        if (file.exists()) {
            if (getFolderSize(file) > 500 * 1024 * 1024) {
                deleteFolder(file);
            }
        } else {
            file.mkdirs();
        }
    }

    /**
     * 查看文件大小，限制文件小于20M
     * @param fileName 文件名称
     */
    private static void checkFileSize(String fileName){
        try{
            File file = new File(fileName);
            if(file.exists() && file.length() > 10 * 1024  * 1024){
                file.delete();
                file.createNewFile();
            }else{
                file.createNewFile();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取文件大小
     * @param file 文件
     */
    private static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for(File fileSingle: fileList){
                if(fileSingle.isDirectory())
                    size += getFolderSize(fileSingle);
                else
                    size += fileSingle.length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除日志
     * @param file 删除的文件或文件夹
     */
    @SuppressWarnings("WeakerAccess")
    public static void deleteFolder(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for(File fileSingle: files)
                deleteFolder(fileSingle);
//            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 将log信息写入文件中
     *
     * @param type 类型
     * @param tagStr 标识
     * @param obj 内容
     */
    private static void writeToFile(char type, String tagStr, String obj) {
        if (null == logPath) {
            Log.e(TAG, "logPath == null ，未初始化LogToFile");
            return;
        }
        String[] contents = wrapperContent(tagStr, obj);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];
        String fileName = getFileName(new Date());//log日志名，使用时间命名，保证不重复
//        String log = dateFormat_log.format(new Date()) + " " + type + " " + tag + " " + msg + "\n";//log日志内容，可以自行定制
        String log = dateFormat_log.format(new Date()) + " " + headString + "" + msg + "\n";
        checkFilePath(logPath);//检测文件夹，查看是否存在，是否占用内存过多

        FileOutputStream fos;//FileOutputStream会自动调用底层的close()方法，不用关闭
        BufferedWriter bw = null;
        try {
            checkFileSize(fileName);//检测文件，查看是否存在，是否占用内存过多
            fos = new FileOutputStream(fileName, true);//这里的第二个参数代表追加还是覆盖，true为追加，flase为覆盖
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(log);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();//关闭缓冲流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取日志得保存路径
     */
    public static String getLogPath(){
        return logPath;
    }

    /**
     * 获取日志写入文件名称
     * @param date 日期
     */
    @SuppressWarnings("WeakerAccess")
    public static String getFileName(Date date){
        return logPath + "log_" + dateFormat.format(date) + ".txt";
    }

    /**
     * 获取throwable字符串
     * @param tr 异常
     */
    private static String getThrowableStr(Throwable tr){
        StringBuilder err = new StringBuilder();
        StackTraceElement[] stacks = tr.getStackTrace();
        for(StackTraceElement stackTraceElement : stacks){
            err.append("\tat ");
            err.append(stackTraceElement.toString());
            err.append("\n");
        }

        return err.toString();
    }

}
