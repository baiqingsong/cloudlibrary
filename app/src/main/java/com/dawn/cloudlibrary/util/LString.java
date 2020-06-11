package com.dawn.cloudlibrary.util;

public class LString {

    /**
     * 是否是空字符串
     * @param str 判断的字符串
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
