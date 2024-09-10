//package com.dawn.androidlibrary.receiver;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.hardware.usb.UsbDevice;
//import android.hardware.usb.UsbManager;
//
//import com.dawn.androidlibrary.util.LStringUtil;
//import com.github.mjdev.libaums.UsbMassStorageDevice;
//import com.github.mjdev.libaums.fs.FileSystem;
//import com.github.mjdev.libaums.fs.UsbFile;
//import com.github.mjdev.libaums.fs.UsbFileInputStream;
//import com.github.mjdev.libaums.partition.Partition;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * 监听usb设备的连接广播
// *         IntentFilter usbFilter = new IntentFilter();
// *         usbFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
// *         usbFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
// *         registerReceiver(mReceiver, usbFilter);
// *
// *     <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
// *     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
// *     <uses-permission android:name="android.hardware.usb.host"
// *         android:required="false"/>
// *     <uses-feature android:name="android.hardware.usb.host"
// *         android:required="true" />
// */
//@SuppressWarnings("unused")
//public class LUSBBroadCastReceiver extends BroadcastReceiver {
//
//    private UsbListener usbListener;
//
//    public static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String action = intent.getAction();
//        if(action == null)
//            return;
//        switch (action) {
//            case ACTION_USB_PERMISSION:
//                //接受到自定义广播
//                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
//                    //允许权限申请
//                    if (usbDevice != null) {
//                        //回调
//                        if (usbListener != null) {
//                            usbListener.getReadUsbPermission(usbDevice);
//                        }
//                    }
//                } else {
//                    if (usbListener != null) {
//                        usbListener.failedReadUsb(usbDevice);
//                    }
//                }
//                break;
//            case UsbManager.ACTION_USB_DEVICE_ATTACHED://接收到存储设备插入广播
//                UsbDevice device_add = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                if (device_add != null) {
//                    if (usbListener != null) {
//                        usbListener.insertUsb(device_add);
//                    }
//                }
//                break;
//            case UsbManager.ACTION_USB_DEVICE_DETACHED:
//                //接收到存储设备拔出广播
//                UsbDevice device_remove = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                if (device_remove != null) {
//                    if (usbListener != null) {
//                        usbListener.removeUsb(device_remove);
//                    }
//                }
//                break;
//        }
//    }
//
//    /**
//     * 设置usb回调函数
//     * @param usbListener 回调接口
//     */
//    public void setUsbListener(UsbListener usbListener) {
//        this.usbListener = usbListener;
//    }
//
//    /**
//     * 获取usb设备下该名称文件的内容
//     * @param context 上下文
//     * @param fileName 文件名称
//     * @return 文件内容
//     */
//    public String getTextFromUsbFile(Context context, String fileName){
//        if(LStringUtil.isEmpty(fileName))
//            return null;
//        UsbMassStorageDevice device = getUsbDevice(context);
//        if(device == null)
//            return null;
//        List<UsbFile> usbFiles = readDevice(device);
//        if(usbFiles == null || usbFiles.size() == 0)
//            return null;
//        for(int i = 0; i < usbFiles.size(); i ++){
//            UsbFile usbFile = usbFiles.get(i);
//            if(!usbFile.isDirectory() && usbFile.getName().equals(fileName)){
//                return readTxtFromUDisk(usbFile);
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 获取usb设备，默认获取第一个设备
//     * @param context 上下文
//     * @return usb实体类
//     */
//    public UsbMassStorageDevice getUsbDevice(Context context){
//        UsbMassStorageDevice[] devices = UsbMassStorageDevice.getMassStorageDevices(context);
//        if(devices != null && devices.length > 0)
//            return devices[0];
//        return null;
//    }
//
//    /**
//     * 获取所有usb实体类
//     * @param context 上下文
//     * @return usb实体类列表
//     */
//    public UsbMassStorageDevice[] getAllUsbDevice(Context context){
//        return UsbMassStorageDevice.getMassStorageDevices(context);
//    }
//
//    /**
//     * 获取usb设备列表
//     * @param device usb实体类对象
//     * @return
//     * UsbMassStorageDevice[] devices = UsbMassStorageDevice.getMassStorageDevices(this); 获取所有设备的方法
//     */
//    public ArrayList<UsbFile> readDevice(UsbMassStorageDevice device) {
//        ArrayList<UsbFile> usbFiles = new ArrayList<>();
//        try {
//            //初始化
//            device.init();
//            //获取partition
//            Partition partition = device.getPartitions().get(0);
//            FileSystem currentFs = partition.getFileSystem();
//            //获取根目录
//            UsbFile root = currentFs.getRootDirectory();
//            //将文件列表添加到ArrayList中
//            Collections.addAll(usbFiles, root.listFiles());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return usbFiles;
//    }
//
//    /**
//     * 获取文件夹下所有文件盒文件夹
//     * @param usbFolder usb文件夹
//     * @return 所有文件的集合
//     */
//    public ArrayList<UsbFile> getUsbFolderFileList(UsbFile usbFolder) {
//        //更换当前目录
//        ArrayList<UsbFile> usbFiles = new ArrayList<>();
//        try {
//            Collections.addAll(usbFiles, usbFolder.listFiles());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return usbFiles;
//    }
//
//    /**
//     * 获取usbFile下的文本
//     * @param usbFile usb文件
//     * @return 文件内容
//     */
//    private String readTxtFromUDisk(UsbFile usbFile) {
//        //读取文件内容
//        InputStream is = new UsbFileInputStream(usbFile);
//        //读取秘钥中的数据进行匹配
//        StringBuilder sb = new StringBuilder();
//        BufferedReader bufferedReader = null;
//        try {
//            bufferedReader = new BufferedReader(new InputStreamReader(is));
//            String read;
//            while ((read = bufferedReader.readLine()) != null) {
//                sb.append(read);
//            }
//            return sb.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bufferedReader != null) {
//                    bufferedReader.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//    /**
//     * USB 操作监听
//     */
//    public interface UsbListener {
//        //USB 插入
//        void insertUsb(UsbDevice device_add);
//
//        //USB 移除
//        void removeUsb(UsbDevice device_remove);
//
//        //获取读取USB权限
//        void getReadUsbPermission(UsbDevice usbDevice);
//
//        //读取USB信息失败
//        void failedReadUsb(UsbDevice usbDevice);
//    }
//}
