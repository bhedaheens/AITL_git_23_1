package com.allintheloop.Util;

import android.content.Context;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import com.allintheloop.R;


public class BoldTextView extends android.support.v7.widget.AppCompatTextView {

    private int typeface;

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BoldTextView, 0, 0);
        try {
            typeface = array.getInteger(R.styleable.BoldTextView_font_name, 0);
        } finally {
            array.recycle();
        }
        if (!isInEditMode()) {
            setTypeface(AppController.getInstance().getTypeFace(typeface));
        }

    }
}
