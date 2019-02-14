package com.allintheloop.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Toast;


public class ToastC {
    public static final boolean isToast = true;
    public static final String GEORGIA = "georgia.ttf";

    public static void show(Context context, String message) {

        if (isToast) {
            try {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
