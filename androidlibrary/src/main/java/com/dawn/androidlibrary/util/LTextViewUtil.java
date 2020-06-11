package com.dawn.androidlibrary.util;

import android.graphics.BlurMaskFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.MaskFilterSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;

/**
 * text view的相关操作
 */
public class LTextViewUtil {
    /**
     * 字符串的拉伸
     * @param str 字符串
     * @param scaleX 水平拉伸倍数
     */
    public static SpannableString getStretchStr(String str, int scaleX){
        if(LStringUtil.isEmpty(str))
            return new SpannableString("");
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ScaleXSpan(scaleX), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 字符串的边缘模糊
     * @param str 需要转换的字符串
     */
    public static SpannableStringBuilder getBlurSolidStr(String str){
        if(LStringUtil.isEmpty(str))
            return new SpannableStringBuilder("");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        //定制滤镜的方式：BlurMaskFilter.Blur.SOLID
        MaskFilterSpan maskFilterSpan = new MaskFilterSpan(new BlurMaskFilter(21, BlurMaskFilter.Blur.SOLID));
        spannableStringBuilder.setSpan(maskFilterSpan, 0, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 字符串的中心镂空
     * @param str 需要处理的字符串
     */
    public static SpannableStringBuilder getBlurOuterStr(String str){
        if(LStringUtil.isEmpty(str))
            return new SpannableStringBuilder("");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        //定制滤镜的方式：BlurMaskFilter.Blur.OUTER
        MaskFilterSpan maskFilterSpan = new MaskFilterSpan(new BlurMaskFilter(21, BlurMaskFilter.Blur.OUTER));
        spannableStringBuilder.setSpan(maskFilterSpan, 0, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 整体模糊
     * @param str 需要处理的字符串
     */
    public static SpannableStringBuilder getBlurNormalStr(String str){
        if(LStringUtil.isEmpty(str))
            return new SpannableStringBuilder("");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        //定制滤镜的方式：BlurMaskFilter.Blur.NORMAL
        MaskFilterSpan maskFilterSpan = new MaskFilterSpan(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
        spannableStringBuilder.setSpan(maskFilterSpan, 0, str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 添加删除线
     * @param str 需要处理的字符串
     */
    public static SpannableString getStrikethroughSpanStr(String str, int start, int end){
        if(LStringUtil.isEmpty(str))
            return new SpannableString("");
        SpannableString spannableString= new SpannableString(str);
        spannableString.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
