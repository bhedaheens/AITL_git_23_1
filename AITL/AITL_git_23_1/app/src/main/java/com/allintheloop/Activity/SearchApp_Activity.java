package com.allintheloop.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;

import java.util.List;

public class SearchApp_Activity extends SuperActivity {
    LinearLayout btnSecureKey;
    String SecretKey;
    Intent i;

    SessionManager sessionManager;
    EditText edt_secureKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_app);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        edt_secureKey = (EditText) findViewById(R.id.edt_secureKey);
        sessionManager = new SessionManager(getApplicationContext());


        edt_secureKey.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_secureKey.setCursorVisible(true);
                return false;
            }
        });


        edt_secureKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SecretKey = v.getText().toString();
                    if (SecretKey.length() == 0) {
                        ToastC.show(SearchApp_Activity.this, getResources().getString(R.string.secretKeyValid));
                    } else {
                        i = new Intent(SearchApp_Activity.this, EventList_Activity.class);
                        sessionManager.setSecretKey(SecretKey);
                        sessionManager.setPrivatePublicStatus("0");
                        startActivity(i);
                        finish();
                    }
                }
                return false;
            }
        });

        edt_secureKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    edt_secureKey.setCursorVisible(false);
                    sessionManager.keyboradHidden(edt_secureKey);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
