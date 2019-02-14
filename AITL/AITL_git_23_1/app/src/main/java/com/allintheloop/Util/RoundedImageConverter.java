package com.allintheloop.Util;

/**
 * Created by nteam on 27/4/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.TypedValue;

/*adopted from http://ruibm.com/2009/06/16/rounded-corner-bitmaps-on-android/*/

public class RoundedImageConverter {


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int color, int cornerDips, int borderDips, Context context) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_4444);
        try {

            Canvas canvas = new Canvas(output);

            final int borderSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) borderDips,
                    context.getResources().getDisplayMetrics());
            final int cornerSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) cornerDips,
                    context.getResources().getDisplayMetrics());
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            // prepare canvas for transfer
            paint.setAntiAlias(true);
            paint.setColor(0xFFFFFFFF);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

            // draw bitmap
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            // draw border
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth((float) borderSizePx);
            canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return output;

    }

    public static RoundedBitmapDrawable getRoundedCornerBitmap1(Bitmap bitmap, int color, int cornerDips, int borderDips, Context context) {
        RoundedBitmapDrawable circularBitmapDrawable = null;
        try {
            circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
            circularBitmapDrawable.setCircular(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return circularBitmapDrawable;
    }
}
