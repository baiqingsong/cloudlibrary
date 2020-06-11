package com.dawn.androidlibrary.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.IdRes;
import android.view.SurfaceHolder;

/**
 * 视频音频的简单应用
 */
@SuppressWarnings("unused")
public class LMediaUtil {
    /**
     * 播放本地的音乐文件
     * @param musicUrl 音乐文件的路径
     */
    public static void startMusicLocal(String musicUrl){
        if(LStringUtil.isEmpty(musicUrl))
            return;
        try{
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(musicUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 开始播放本地音乐
     * @param musicId 音乐ID
     */
    public static void startMusicLocal(Context context, @IdRes int musicId){
        if(musicId == 0)
            return;
        try{
            MediaPlayer mediaPlayer = MediaPlayer.create(context, musicId);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 播放本地的视频文件
     * @param surfaceHolder 视频渲染的类
     * @param videoUrl 视频的本地路径
     */
    public static void startVideoLocal(SurfaceHolder surfaceHolder, String videoUrl){
        if(surfaceHolder == null || LStringUtil.isEmpty(videoUrl))
            return;
        try{
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 播放网络的音乐文件
     * @param musicUrl 网络音乐文件的地址
     */
    public static void startMusicNetwork(String musicUrl){
        if(LStringUtil.isEmpty(musicUrl))
            return;
        try{
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(musicUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取总时长的字符串
     * @param mediaPlayer 播放类
     */
    public static String getPlayerDurationToString(MediaPlayer mediaPlayer){
        int second = mediaPlayer.getDuration()/1000%60;
        return mediaPlayer.getDuration()/1000/60 + ":" + (second < 0 ? "0" + second : second);
    }

    /**
     * 获取当前的时间字符串
     * @param mediaPlayer 播放类
     */
    public static String getPlayerCurrentPositionToString(MediaPlayer mediaPlayer){
        int second = mediaPlayer.getCurrentPosition()/1000%60;
        return mediaPlayer.getCurrentPosition()/1000/60 + ":" + (second < 0 ? "0" + second : second);
    }
}
