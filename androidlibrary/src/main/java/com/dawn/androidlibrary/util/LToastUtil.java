package com.dawn.androidlibrary.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dawn.androidlibrary.R;

@SuppressWarnings({"unused", "WeakerAccess"})
public class LToastUtil {
    @ColorInt
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");

    @ColorInt
    private static final int ERROR_BACKGROUND_COLOR = Color.parseColor("#FD4C5B");

    @ColorInt
    private static final int INFO_BACKGROUND_COLOR = Color.parseColor("#3F51B5");

    @ColorInt
    private static final int SUCCESS_BACKGROUND_COLOR = Color.parseColor("#388E3C");

    @ColorInt
    private static final int WARNING_BACKGROUND_COLOR = Color.parseColor("#FFA900");

    private static final String TOAST_TYPEFACE = "sans-serif-condensed";

    private static Toast currentToast;
    private static Toast mToast;
    private static long mExitTime;

    //normal
    public static void normal(Context context, @NonNull String message) {
        normal(context, message, Toast.LENGTH_SHORT, null, false).show();
    }

    public static void normal(Context context, @NonNull String message, int duration) {
        normal(context, message, duration, null, false).show();
    }

    public static void normal(Context context, @NonNull String message, Drawable icon) {
        normal(context, message, Toast.LENGTH_SHORT, icon, true).show();
    }

    public static void normal(Context context, @NonNull String message, int duration, Drawable icon) {
        normal(context, message, duration, icon, true).show();
    }


    //warning
    public static void warning(Context context, @NonNull String message) {
        warning(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public static void warning(Context context, @NonNull String message, int duration) {
        warning(context, message, duration, true).show();
    }

    public static Toast warning(Context context, @NonNull String message, int duration, boolean withIcon) {
        return custom(context, message, getDrawable(context, R.mipmap.ic_error_outline_white_48dp), DEFAULT_TEXT_COLOR, WARNING_BACKGROUND_COLOR, duration, withIcon, true);
    }


    //info
    public static void info(Context context, @NonNull String message) {
        info(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public static void info(Context context, @NonNull String message, int duration) {
        info(context, message, duration, true).show();
    }

    public static Toast info(Context context, @NonNull String message, int duration, boolean withIcon) {
        return custom(context, message, getDrawable(context, R.mipmap.ic_info_outline_white_48dp), DEFAULT_TEXT_COLOR, INFO_BACKGROUND_COLOR, duration, withIcon, true);
    }


    //success
    public static void success(Context context, @NonNull String message) {
        success(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public static void success(Context context, @NonNull String message, int duration) {
        success(context, message, duration, true).show();
    }

    public static Toast success(Context context, @NonNull String message, int duration, boolean withIcon) {
        return custom(context, message, getDrawable(context, R.mipmap.ic_check_white_48dp), DEFAULT_TEXT_COLOR, SUCCESS_BACKGROUND_COLOR, duration, withIcon, true);
    }


    //error
    public static void error(Context context, @NonNull String message) {
        error(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public static void error(Context context, @NonNull String message, int duration) {
        error(context, message, duration, true).show();
    }

    public static Toast error(Context context, @NonNull String message, int duration, boolean withIcon) {
        return custom(context, message, getDrawable(context, R.mipmap.ic_clear_white_48dp), DEFAULT_TEXT_COLOR, ERROR_BACKGROUND_COLOR, duration, withIcon, true);
    }



    //******************************************simple Toast **************************************

    /**
     * simple Toast : need waiting
     */
    public static void showToastShort(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }


    public static void showToastLong(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }


    /**
     *simple Toast : show rightNow
     */
    @SuppressLint("ShowToast")
    public static void showToast(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }


    /**
     * click two time for exit
     */
    public static boolean doubleClickExit(Context context) {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            LToastUtil.normal(context, "再按一次退出");
            mExitTime = System.currentTimeMillis();
            return false;
        }
        return true;
    }



    //*******************************************private method start********************************************

    @CheckResult
    private static Toast normal(@NonNull Context context, @NonNull String message, int duration, Drawable icon, boolean withIcon) {
        return custom(context, message, icon, DEFAULT_TEXT_COLOR, duration, withIcon);
    }


    @CheckResult
    private static Toast custom(@NonNull Context context, @NonNull String message, Drawable icon, @SuppressWarnings("SameParameterValue") @ColorInt int textColor, int duration, boolean withIcon) {
        return custom(context, message, icon, textColor, -1, duration, withIcon, false);
    }


    @CheckResult
    private static Toast custom(@NonNull Context context, @NonNull String message, @DrawableRes int iconRes, @ColorInt int textColor, @ColorInt int tintColor, int duration, boolean withIcon, boolean shouldTint) {
        return custom(context, message, getDrawable(context, iconRes), textColor, tintColor, duration, withIcon, shouldTint);
    }

    @CheckResult
    private static Toast custom(@NonNull Context context, @NonNull String message, Drawable icon, @ColorInt int textColor, @ColorInt int tintColor, int duration, boolean withIcon, boolean shouldTint) {
        if (currentToast == null) {
            currentToast = new Toast(context);
        }
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(layoutInflater == null)
            return null;
        @SuppressLint("InflateParams") final View toastLayout = layoutInflater.inflate(R.layout.toast_layout, null);
        final ImageView toastIcon = toastLayout.findViewById(R.id.toast_icon);
        final TextView toastTextView = toastLayout.findViewById(R.id.toast_text);
        Drawable drawableFrame;

        if (shouldTint) {
            drawableFrame = tint9PatchDrawableFrame(context, tintColor);
        } else {
            drawableFrame = getDrawable(context, R.mipmap.toast_frame);
        }
        setBackground(toastLayout, drawableFrame);

        if (withIcon) {
            if (icon == null)
                throw new IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true");
            setBackground(toastIcon, icon);
        } else
            toastIcon.setVisibility(View.GONE);

        toastTextView.setTextColor(textColor);
        toastTextView.setText(message);
        toastTextView.setTypeface(Typeface.create(TOAST_TYPEFACE, Typeface.NORMAL));

        currentToast.setView(toastLayout);
        currentToast.setDuration(duration);
        return currentToast;
    }

    private static Drawable tint9PatchDrawableFrame(@NonNull Context context, @ColorInt int tintColor) {
        final NinePatchDrawable toastDrawable = (NinePatchDrawable) getDrawable(context, R.mipmap.toast_frame);
        toastDrawable.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        return toastDrawable;
    }

    private static void setBackground(@NonNull View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(drawable);
        else
            //noinspection deprecation
            view.setBackgroundDrawable(drawable);
    }

    private static Drawable getDrawable(@NonNull Context context, @DrawableRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return context.getDrawable(id);
        else
            //noinspection deprecation
            return context.getResources().getDrawable(id);
    }


    //===========================================private method end============================================

}
