package com.allintheloop.Util;

import android.content.Context;
import android.graphics.Typeface;

public class TypeFactory {
    final String LATO_BOLD = "Lato-Bold.ttf";
    final String LATO_LIGHT = "Lato-Light.ttf";
    final String LATO_REGULAR = "lato_regular.ttf";

    Typeface bold, light, regular;

    public TypeFactory(Context context) {
        bold = Typeface.createFromAsset(context.getAssets(), LATO_BOLD);
        light = Typeface.createFromAsset(context.getAssets(), LATO_LIGHT);
        regular = Typeface.createFromAsset(context.getAssets(), LATO_REGULAR);
    }

    public Typeface getLight() {
        return light;
    }

    public Typeface getBold() {
        return bold;
    }

    public Typeface getRegular() {
        return regular;
    }
}
