package com.dawn.androidlibrary.util;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * iosocket的类的调用
 */
@SuppressWarnings("unused")
public class LIOSocket {
    private final static String TAG = LIOSocket.class.getSimpleName();
    private Socket mSocket;//socket对象
    public void connect(String socketUrl){
        try {
            mSocket = IO.socket(socketUrl);
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            LLog.exception(TAG, e);
        }
    }

    /**
     * 断开socket连接
     */
    public void disconnect(){
        if(mSocket != null)
            mSocket.disconnect();
    }

    /**
     * 接收socket消息
     * @param event 发送的字符串
     * @param listener 回调
     */
    public void receiverMsg(String event, Emitter.Listener listener){
        if(mSocket != null)
            mSocket.on(event, listener);
    }

    /**
     * 接收socket消息
     * @param event 发送的内容
     * @param listener 回调
     */
    public void receiverMsg(String event, final IOSocketReceiverListener listener){
        if(mSocket != null)
            mSocket.on(event, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if(args != null && args.length > 0 && args[0] != null){
                        String modelJson = args[0].toString();
                        if(listener != null)
                            listener.getReceiverMsgStr(modelJson);
                    }
                }
            });
    }

    /**
     * 发送socket消息
     * @param event 发送的内容
     */
    public void sendMsg(String event, Object... args){
        if(mSocket != null)
            mSocket.emit(event, args);
    }

    /**
     * 接收消息的回调类
     */
    public interface IOSocketReceiverListener{
        void getReceiverMsgStr(String msgStr);
    }
}
