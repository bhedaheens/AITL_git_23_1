package com.allintheloop.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.allintheloop.R;

/**
 * Created by Aiyaz on 12/12/16.
 */

public class MyCustomProgressDialog extends ProgressDialog {

    public MyCustomProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog);
    }
}
