package com.dawn.androidlibrary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 */
@SuppressWarnings("unused")
public class LDateUtil {
    private static final String TAG = LDateUtil.class.getSimpleName();
    private static final SimpleDateFormat DATE_FORMAT_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat DATE_FORMAT_TIME = new SimpleDateFormat("HH:mm:ss", Locale.US);

    /**
     * 格式化日期时间
     * @param date 日期时间long类型
     */
    public static String formatDataTime(long date) {
        return DATE_FORMAT_DATETIME.format(new Date(date));
    }

    /**
     * 格式化日期
     * @param date 日期long类型
     */
    public static String formatDate(long date) {
        return DATE_FORMAT_DATE.format(new Date(date));
    }

    /**
     * 格式化时间
     * @param date 时间long类型
     */
    public static String formatTime(long date) {
        return DATE_FORMAT_TIME.format(new Date(date));
    }

    /**
     * 自定义格式的格式化日期时间
     * @param beginDate 时间long
     * @param format 格式化的类型字符串
     */
    public static String formatDateCustom(String beginDate, String format) {
        return new SimpleDateFormat(format, Locale.US).format(new Date(Long.parseLong(beginDate)));
    }

    /**
     * 自定义格式的格式化日期时间
     * @param beginDate 时间date类型
     * @param format 格式字符串
     */
    public static String formatDateCustom(Date beginDate, String format) {
        return new SimpleDateFormat(format, Locale.US).format(beginDate);
    }

    /**
     * 将时间字符串转换成Date
     * @param s 时间字符串
     * @param format 时间的格式
     */
    public static Date string2Date(String s, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
        Date date = null;
        if (s == null || s.length() < 6) {
            return null;
        }
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            LLog.exception(TAG, e);
        }
        return date;
    }

    /**
     * 获取系统时间
     */
    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        return cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
    }

    /**
     * 获取系统日期
     */
    public static String getDate() {
        return new SimpleDateFormat("yyyyMMdd", Locale.US).format(System.currentTimeMillis());
    }

    /**
     * 获取系统日期时间
     */
    public static String getDateTime(){
        return DATE_FORMAT_DATETIME.format(System.currentTimeMillis());
    }

    /**
     * 根据自定义格式获取系统日期时间
     * @param format 格式的字符串
     */
    public static String getDateTime(String format){
        return new SimpleDateFormat(format, Locale.US).format(System.currentTimeMillis());
    }

    /**
     * 计算两个时间差
     * @param dateStart 开始时间
     * @param dateEnd 结束时间
     */
    public static long subtractDate(Date dateStart, Date dateEnd) {
        return dateEnd.getTime() - dateStart.getTime();
    }

    /**
     * 获取几天后的日期
     * @param d 日期
     * @param day 间隔天数
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 获取当前时间为本月的第几周
     */
    public static int getWeekOfMonth() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        return week - 1;
    }

    /**
     * 获取当前时间是本周的第几天
     */
    public static int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 1) {
            day = 7;
        } else {
            day = day - 1;
        }
        return day;
    }

    /**
     * long转换成date类型
     * @param date 日期的long类型
     */
    public static Date long2Date(long date){
        return new Date(date);
    }

    /**
     * date转换成long类型
     * @param date 日期的date类型
     */
    public static long date2Long(Date date){
        if(date == null)
            return -1;
        return date.getTime();
    }
}
