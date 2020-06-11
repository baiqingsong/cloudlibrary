package com.dawn.androidlibrary.util;

/**
 * 进制工具类
 */
@SuppressWarnings("unused")
public class LRadixUtil {
    /**
     * 16进制转2进制
     * @param hexString 16进制字符串
     */
    public static String hexString2binaryString(String hexString) {
        if (hexString.length() % 2 != 0) {
            hexString = "0" + hexString;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hexString.length(); i++) {
            String tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            sb.append(tmp.substring(tmp.length() - 4));
        }
        return sb.toString();
    }

    /**
     * 2进制转16进制
     * @param bString 二进制字符串
     */
    public static String binaryString2hexString(String bString) {
        if (bString.length() % 8 != 0) {
            String sbuwei = "00000000";
            bString = sbuwei.substring(0, sbuwei.length() - bString.length() % 8) + bString;
        }
        StringBuilder tmp = new StringBuilder();
        int iTmp;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }

    /**
     * 二进制相加
     * @param a 第一个二进制字符串
     * @param b 第二个二进制字符串
     */
    public static String addBinary(String a, String b) {
        int carry = 0;
        int sum;
        int opa;
        int opb;
        StringBuilder result = new StringBuilder();
        StringBuilder sbA = new StringBuilder();
        StringBuilder sbB = new StringBuilder();
        sbA.append(a);
        sbB.append(b);
        while (sbA.toString().length() != sbB.toString().length()) {
            if (sbA.toString().length() > sbB.toString().length()) {
                sbB.insert(0, "0");
            } else {
                sbA.insert(0, "0");
            }
        }
        a = sbA.toString();
        b = sbB.toString();
        for (int i = a.length() - 1; i >= 0; i--) {
            opa = a.charAt(i) - '0';
            opb = b.charAt(i) - '0';
            sum = opa + opb + carry;
            if (sum >= 2) {
                result.append((char) (sum - 2 + '0'));
                carry = 1;
            } else {
                result.append((char) (sum + '0'));
                carry = 0;
            }
        }
        if (carry == 1) {
            result.append("1");
        }
        return result.reverse().toString();
    }

    /**
     * 16进制转字符串
     * @param data 16进制
     */
    public static String hexArray2String(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        for(byte b: data){
            int value = b & 0xff;
            sb.append(HEX[value / 16]).append(HEX[value % 16]).append(" ");
        }
        return sb.toString();
    }
}
