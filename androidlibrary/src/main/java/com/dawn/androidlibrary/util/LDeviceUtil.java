package com.dawn.androidlibrary.util;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.UUID;

/**
 * 设备信息工具
 */
@SuppressWarnings("unused")
public class LDeviceUtil {
    private final static String TAG = LDeviceUtil.class.getSimpleName();
    /**
     * 获取Android ID
     * @param ctx 上下文
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID(Context ctx) {
        return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取设备IMEI码
     * @param ctx 上下文
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if(tm != null)
            return tm.getImei();
        return null;
    }

    /**
     * 获取设备的IMSI码
     * @param ctx 上下文
     */
    @SuppressLint("HardwareIds")
    public static String getIMSI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if(tm != null)
            return tm.getSubscriberId();
        return null;
    }

    /**
     * 获取有线的mac
     */
    public static String getLanMacAddr(){
        try {
            return LFileUtil.loadFileAsString("/sys/class/net/eth0/address")
                    .toUpperCase().substring(0, 17);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 获取WiFi的MAC地址
     * @param ctx 上下文
     */
    @SuppressLint("HardwareIds")
    public static String getWifiMacAddr(Context ctx) {
        String macAddr = "";
        try {
            WifiManager wifi = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if(wifi != null)
            macAddr = wifi.getConnectionInfo().getMacAddress();
            if (macAddr == null) {
                macAddr = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.exception(TAG, e);
        }
        return macAddr;
    }

    /**
     * 获取网络IP地址（优先获取WiFi的地址）
     * @param ctx 上下文
     */
    public static String getIP(Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager == null)
            return null;
        return wifiManager.isWifiEnabled() ? getWifiIP(wifiManager) : getGPRSIP();
    }

    /**
     * 获取WiFi连接下的IP地址
     * @param wifiManager 实体类
     */
    private static String getWifiIP(WifiManager wifiManager) {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return intToIp(wifiInfo.getIpAddress());
    }

    /**
     * 获取GPRS连接下的IP地址
     */
    private static String getGPRSIP() {
        String ip = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                for (Enumeration<InetAddress> enumIpAddr = en.nextElement().getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            ip = null;
            LLog.exception(TAG, e);
        }
        return ip;
    }

    /**
     * IP的int转换成字符串类型
     * @param i ip的值
     */
    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    /**
     * 获取设备的序列号
     */
    @SuppressLint("HardwareIds")
    public static String getSerial() {
        return Build.SERIAL;
    }

    /**
     * 获取SIM序列号
     * @param ctx 上下文
     */
    @SuppressLint("HardwareIds")
    public static String getSIMSerial(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if(tm == null)
            return null;
        return tm.getSimSerialNumber();
    }

    /**
     * 获取网络运营商 46000，46002，46007 中国移动，46001中国联通，46003中国电信
     * @param ctx 上下文
     */
    public static String getMNC(Context ctx) {
        String providersName = "";
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null && telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
            providersName = telephonyManager.getSimOperator();
            providersName = providersName == null ? "" : providersName;
        }
        return providersName;
    }

    /**
     * 获取网络运营商：中国电信，中国移动，中国联通
     * @param ctx 上下文
     */
    public static String getCarrier(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if(tm == null)
            return null;
        return tm.getNetworkOperatorName().toLowerCase(Locale.getDefault());
    }

    /**
     * 获取硬件型号
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取编译厂商
     */
    public static String getBuildBrand() {
        return Build.BRAND;
    }

    /**
     * 获取编译服务器主机
     */
    public static String getBuildHost() {
        return Build.HOST;
    }

    /**
     * 获取描述Build的标签
     */
    public static String getBuildTags() {
        return Build.TAGS;
    }

    /**
     * 获取系统编译时间
     */
    public static long getBuildTime() {
        return Build.TIME;
    }

    /**
     * 获取系统编译作者
     */
    public static String getBuildUser() {
        return Build.USER;
    }

    /**
     * 获取编译系统版本（5.1）
     */
    public static String getBuildVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取开发代号
     */
    public static String getBuildVersionCodename() {
        return Build.VERSION.CODENAME;
    }

    /**
     * 获取源码控制版本号
     */
    public static String getBuildVersionIncremental() {
        return Build.VERSION.INCREMENTAL;
    }

    /**
     * 获取编译的SDK
     */
    public static int getBuildVersionSDK() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取修订版本列表（LMY47D）
     */
    public static String getBuildID() {
        return Build.ID;
    }

    /**
     * CPU指令集
     */
    public static String[] getSupportedABIS() {
        String[] result = new String[]{"-"};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            result = Build.SUPPORTED_ABIS;
        }
        if (result == null || result.length == 0) {
            result = new String[]{"-"};
        }
        return result;
    }

    /**
     * 获取硬件制造厂商
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取系统启动程序版本号
     */
    public static String getBootloader() {
        return Build.BOOTLOADER;
    }

    /**
     *
     * @param ctx 上下文
     */
    public static String getScreenDisplayID(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        if(wm == null)
            return null;
        return String.valueOf(wm.getDefaultDisplay().getDisplayId());
    }

    /**
     * 获取系统版本号
     */
    public static String getDisplayVersion() {
        return Build.DISPLAY;
    }

    /**
     * 获取语言
     */
    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取国家
     * @param ctx 上下文
     */
    public static String getCountry(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if(tm == null)
            return null;
        Locale locale = Locale.getDefault();
        return tm.getSimState() == TelephonyManager.SIM_STATE_READY ? tm.getSimCountryIso().toLowerCase(Locale.getDefault()) : locale.getCountry().toLowerCase(locale);
    }

    /**
     * 获取系统版本：5.1.1
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取GSF序列号
     * @param context 上下文
     */
    //<uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES"/>
    public static String getGSFID(Context context) {
        String result;
        final Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
        final String ID_KEY = "android_id";
        String[] params = {ID_KEY};
        Cursor c = context.getContentResolver().query(URI, null, null, params, null);
        if (c == null || !c.moveToFirst() || c.getColumnCount() < 2) {
            return null;
        } else {
            result = Long.toHexString(Long.parseLong(c.getString(1)));
        }
        c.close();
        return result;
    }

    /**
     * 获取蓝牙地址
     * @param context 上下文
     */
    //<uses-permission android:name="android.permission.BLUETOOTH"/>
    @SuppressLint("HardwareIds")
    public static String getBluetoothMAC(Context context) {
        String result = null;
        try {
            if (context.checkCallingOrSelfPermission(Manifest.permission.BLUETOOTH)
                    == PackageManager.PERMISSION_GRANTED) {
                BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
                result = bta.getAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.exception(TAG, e);
        }
        return result;
    }

    /**
     * Android设备物理唯一标识符
     */
    public static String getPsuedoUniqueID() {
        String devIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            devIDShort += (Build.SUPPORTED_ABIS[0].length() % 10);
        } else {
            devIDShort += (Build.CPU_ABI.length() % 10);
        }
        devIDShort += (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            return new UUID(devIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception e) {
            serial = "ESYDV000";
            LLog.exception(TAG, e);
        }
        return new UUID(devIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * 构建标志，包括brand,name,device,version,release,id,incremental,type,tags这些信息
     */
    public static String getFingerprint() {
        return Build.FINGERPRINT;
    }

    /**
     * 获取硬件信息
     */
    public static String getHardware() {
        return Build.HARDWARE;
    }

    /**
     * 获取产品信息
     */
    public static String getProduct() {
        return Build.PRODUCT;
    }

    /**
     * 获取设备信息
     */
    public static String getDevice() {
        return Build.DEVICE;
    }

    /**
     * 获取主板信息
     */
    public static String getBoard() {
        return Build.BOARD;
    }

    /**
     * 获取基带版本（无线电固件版本Api14以上）
     */
    public static String getRadioVersion() {
        return Build.getRadioVersion();
    }

    /**
     * 获取的浏览器指纹（User-Agent）
     * @param ctx 上下文
     */
    public static String getUA(Context ctx) {
        final String system_ua = System.getProperty("http.agent");
        return new WebView(ctx).getSettings().getUserAgentString() + "__" + system_ua;
    }

    /**
     * 获取屏幕密度
     * @param ctx 上下文
     */
    public static String getDensity(Context ctx) {
        String densityStr = null;
        final int density = ctx.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                densityStr = "LDPI";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                densityStr = "MDPI";
                break;
            case DisplayMetrics.DENSITY_TV:
                densityStr = "TVDPI";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                densityStr = "HDPI";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                densityStr = "XHDPI";
                break;
            case DisplayMetrics.DENSITY_400:
                densityStr = "XMHDPI";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                densityStr = "XXHDPI";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                densityStr = "XXXHDPI";
                break;
        }
        return densityStr;
    }

    /**
     * 获取Google账号
     * @param ctx 上下文
     */
    //<uses-permission android:name="android.permission.GET_ACCOUNTS"/>
    public static String[] getGoogleAccounts(Context ctx) {
        if (ctx.checkCallingOrSelfPermission(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
            Account[] accounts = AccountManager.get(ctx).getAccountsByType("com.google");
            String[] result = new String[accounts.length];
            for (int i = 0; i < accounts.length; i++) {
                result[i] = accounts[i].name;
            }
            return result;
        }
        return null;
    }

    /**
     * 重启手机
     */
    public static void reboot(){
        try {
            Runtime.getRuntime().exec(new String[]{"su","-c","reboot"});
        } catch (IOException e) {
            e.printStackTrace();
            LLog.exception(TAG, e);
        }
    }

    /**
     * 关闭手机
     */
    public static void powerOff(){
        try{
            //Process proc =Runtime.getRuntime().exec(new String[]{"su","-c","shutdown"}); //关机
            Process proc =Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"}); //关机
            proc.waitFor();
        }catch(Exception e){
            e.printStackTrace();
            LLog.exception(TAG, e);
        }
    }
    private final static String COMMAND_AIRPLANE_ON = "settings put global airplane_mode_on 1 \n " +
            "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true\n ";
    private final static String COMMAND_AIRPLANE_OFF = "settings put global airplane_mode_on 0 \n" +
            " am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false\n ";

    /**
     * 改变飞行模式
     * @param isEnabled 是否飞行模式
     */
    public static void changeAirPlane(boolean isEnabled){
        try{
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
            String command;
            if(isEnabled){
                command = COMMAND_AIRPLANE_ON;
            }else{
                command = COMMAND_AIRPLANE_OFF;
            }
            outputStream.writeBytes(command);
            outputStream.flush();

            outputStream.writeBytes("exit\n");
            outputStream.flush();
            try {
                su.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
                LLog.exception(TAG, e);
            }

            outputStream.close();
        }catch(Exception e){
            e.printStackTrace();
            LLog.exception(TAG, e);
        }
    }

    /**
     * 执行cmd指令
     * @param cmd cmd指令
     */
    public int execRootCmdSilent(String cmd) {
        int result = -1;
        DataOutputStream dos = null;

        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            result = p.waitFor();
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException var12) {
                    var12.printStackTrace();
                }
            }

        }

        return result;
    }

}
