package com.allintheloop.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.daimajia.androidanimations.library.Techniques;

import org.json.JSONException;
import org.json.JSONObject;


public class ForgotPasswrodActivity extends AppCompatActivity implements VolleyInterface {

    BoldTextView btnRegSubmit;
    BoldTextView btn_backTologin;
    EditText edt_email;
    String str_email;
    SessionManager sessionManager;
    ImageView img_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_passwrod);

        btn_backTologin = findViewById(R.id.btn_backTologin);
        img_logo = findViewById(R.id.img_logo);
        btnRegSubmit = findViewById(R.id.btnRegSubmit);
        edt_email = (EditText) findViewById(R.id.edt_email);
        sessionManager = new SessionManager(ForgotPasswrodActivity.this);
        btn_backTologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswrodActivity.this, LoginActivity.class));
                finish();
            }
        });


        btnRegSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                str_email = edt_email.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_email.getWindowToken(), 0);
                if (str_email.toString().length() <= 0) {
                    edt_email.setError(getResources().getString(R.string.emailvalid));

                } else if (!GlobalData.checkEmailValid(edt_email.getText().toString())) {
                    edt_email.setError(getResources().getString(R.string.mailValid));
                } else {
                    if (GlobalData.isNetworkAvailable(ForgotPasswrodActivity.this)) {
                        forgotPassword();
                    } else {
                        ToastC.show(ForgotPasswrodActivity.this, getResources().getString(R.string.noInernet));
                    }
                }
            }
        });

        getLoginLogo();
    }


    private void getLoginLogo() {
        if (GlobalData.isNetworkAvailable(this)) {
            new VolleyRequest(this, VolleyRequest.Method.POST, MyUrls.getloginScreenLogo, Param.getLoginLogo(sessionManager.getEventId()), 1, false, this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgotPasswrodActivity.this, LoginActivity.class));
        finish();
    }


    private void forgotPassword() {
        if (GlobalData.checkForUIDVersion()) {
            new VolleyRequest(ForgotPasswrodActivity.this, VolleyRequest.Method.POST, MyUrls.forgotPasswordUid, Param.forgotPassword(sessionManager.getEventId(), str_email), 0, true, this);
        } else {
            new VolleyRequest(ForgotPasswrodActivity.this, VolleyRequest.Method.POST, MyUrls.forgotPassword, Param.forgotPassword(sessionManager.getEventId(), str_email), 0, true, this);
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonData = jsonObject.getJSONObject("data");
//                            Toast.makeText(getApplicationContext(),jsonData.getString("msg").toString(),Toast.LENGTH_SHORT).show();

                        new AlertDialogWrapper.Builder(ForgotPasswrodActivity.this)
                                .setMessage(jsonData.getString("msg").toString())
                                .setNegativeButton(getResources().getString(R.string.txtOk), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        startActivity(new Intent(ForgotPasswrodActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }).show();
                    } else {
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        Toast.makeText(getApplicationContext(), jsonData.getString("msg").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL DeleteUser", jsonObject.toString());
                    if (jsonObject.optString("success").equalsIgnoreCase("true")) {
                        setLoginLogoandContent(jsonObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void setLoginLogoandContent(JSONObject jsonObject) {
        if (jsonObject.has("data")) {
            try {
                JSONObject jsonData = jsonObject.getJSONObject("data");
                if (jsonData.length() != 0) {
                    String str_logopath = MyUrls.loginImgurl + jsonData.getString("login_screen_image");
                    if (jsonData.getString("login_screen_image").length() > 0) {
                        img_logo.setVisibility(View.VISIBLE);
                        Glide.with(this)
                                .load(str_logopath)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        e.printStackTrace();
                                        img_logo.setVisibility(View.INVISIBLE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        img_logo.setVisibility(View.VISIBLE);
                                        return false;
                                    }
                                })
                                .into(img_logo);
                    } else {
                        img_logo.setVisibility(View.INVISIBLE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
