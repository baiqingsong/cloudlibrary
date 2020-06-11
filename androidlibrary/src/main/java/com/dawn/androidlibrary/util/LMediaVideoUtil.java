package com.dawn.androidlibrary.util;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;
@SuppressWarnings("unused")
public class LMediaVideoUtil {
    private final static String TAG = LMediaUtil.class.getSimpleName();
    private Context mContext;
    private ExoPlayer player;

    public LMediaVideoUtil(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 创建播放器
     */
    public void createMedia(){
        // step1. 创建一个默认的TrackSelector
        // 创建带宽
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        // 创建轨道选择工厂
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);

        // 创建轨道选择器实例
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        //step2. 创建播放器
        player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

    }
    /**
     * 准备播放器
     */
    public void prepareMedia(int resId, int loopCount){
        try{
            DataSpec dataSpec = new DataSpec(RawResourceDataSource.buildRawResourceUri(resId));
            RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(mContext);
            rawResourceDataSource.open(dataSpec);
            prepareMedia(rawResourceDataSource.getUri(), loopCount);
        }catch (Exception e){
            e.printStackTrace();
            LLog.exception(TAG, e);
        }
    }
    /**
     * 资源准备
     * @param uri 资源uri
     */
    @SuppressWarnings({"WeakerAccess"})
    public void prepareMedia(Uri uri, int loopCount){
        try {
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory factory =  new DefaultDataSourceFactory(mContext,
                    Util.getUserAgent(mContext, "com.dawn.bless"), bandwidthMeter);

            @SuppressWarnings("deprecation") ExtractorMediaSource mediaSource = new ExtractorMediaSource(uri,
                    factory, new DefaultExtractorsFactory(), null, null);
            LoopingMediaSource loopingMediaSource;
            if(loopCount == 0){
                loopingMediaSource = new LoopingMediaSource(mediaSource);
            }else{
                loopingMediaSource = new LoopingMediaSource(mediaSource, loopCount);
            }
            if(player != null){
                player.prepare(loopingMediaSource);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LLog.exception(TAG, e);
        }
    }

    /**
     * 开启资源
     */
    public void playMedia(){
        if(player != null){
            player.setPlayWhenReady(true);
        }
    }

    /**
     * 播放视频资源
     * @param simpleExoPlayerView 实体类
     */
    public void playMedia(@SuppressWarnings("deprecation") SimpleExoPlayerView simpleExoPlayerView){
        if(player != null){
            simpleExoPlayerView.setPlayer(player);
            simpleExoPlayerView.hideController();
            player.setPlayWhenReady(true);
        }
    }

    /**
     * 暂停资源
     */
    public void pauseMedia(){
        if(player != null && player.getPlayWhenReady()){
            player.setPlayWhenReady(false);
        }
    }

    /**
     * 释放资源
     */
    public void releaseMedia(){
        if(player != null){
            player.stop();
            player.release();
            player = null;
        }
    }
}
