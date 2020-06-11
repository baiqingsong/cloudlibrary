package com.dawn.androidlibrary.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
@SuppressWarnings("unused")
public class LQrCodeUtil {
    /**
     * 显示二维码图片
     */
    public static void showQrCode(final Context context, final ImageView ivQrCode, final String qrCodeStr){
        if(LStringUtil.isEmpty(qrCodeStr) || ivQrCode == null)
            return ;
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
//                Bitmap logoBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
                QRCodeEncoder.HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                return QRCodeEncoder.syncEncodeQRCode(qrCodeStr, BGAQRCodeUtil.dp2px(context, 260), Color.parseColor("#000000")/*, logoBitmap*/);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    ivQrCode.setImageBitmap(bitmap);
                }
            }
        }.execute();
    }
}
