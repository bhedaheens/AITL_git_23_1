package com.allintheloop.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
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

public class LoginMainScreen extends AppCompatActivity implements VolleyInterface {


    WebView webview_content;
    ImageView img_logo;
    SessionManager sessionManager;
    String str_logopath, str_contentText;
    TextView btnLogin, txtSign;
    DefaultLanguage.DefaultLang defaultLang;
    LinearLayout linear_registerButton;
    ImageView btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main_screen);

        img_logo = findViewById(R.id.img_logo);
        webview_content = findViewById(R.id.webview_content);
        linear_registerButton = findViewById(R.id.linear_registerButton);
        btnLogin = findViewById(R.id.btnLogin);
        btnClose = findViewById(R.id.btnClose);
        txtSign = findViewById(R.id.txtSign);
        sessionManager = new SessionManager(LoginMainScreen.this);
        defaultLang = sessionManager.getMultiLangString();

        btnLogin.setText(defaultLang.getSignUpProcessLogIn());
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginMainScreen.this, LoginActivity.class));
            }
        });
        txtSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginMainScreen.this, RegisterActivity.class));
            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2") || sessionManager.getEventType().equalsIgnoreCase("4")) {
                    if (sessionManager.get_show_login_screen().equalsIgnoreCase("1")) {
                        startActivity(new Intent(LoginMainScreen.this, SearchApp_Activity.class));
                        finish();
                    } else {
                        startActivity(new Intent(LoginMainScreen.this, MainActivity.class));
                        finish();
                    }
                } else {
                    startActivity(new Intent(LoginMainScreen.this, MainActivity.class));
                    finish();
                }
            }
        });


        if (sessionManager.getEventType().equalsIgnoreCase("4")) {
            linear_registerButton.setVisibility(View.GONE);
        } else {
            if (sessionManager.getEventType().equalsIgnoreCase("1")) {
                linear_registerButton.setVisibility(View.GONE);
            } else {
                linear_registerButton.setVisibility(View.VISIBLE);
            }
        }

        getLoginLogo();
    }


    private void getLoginLogo() {
        if (GlobalData.isNetworkAvailable(LoginMainScreen.this)) {
            new VolleyRequest(LoginMainScreen.this, VolleyRequest.Method.POST, MyUrls.getloginScreenLogo, Param.getLoginLogo(sessionManager.getEventId()), 6, false, this);
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 6:
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
                    str_logopath = MyUrls.loginImgurl + jsonData.getString("login_screen_image");
                    str_contentText = jsonData.getString("login_screen_text");

                    if (jsonData.getString("login_screen_image").length() > 0) {
                        img_logo.setVisibility(View.VISIBLE);
                        Glide.with(LoginMainScreen.this)
                                .load(str_logopath)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        e.printStackTrace();
                                        img_logo.setVisibility(View.GONE);
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
                        img_logo.setVisibility(View.GONE);
                    }
                    if (str_contentText.length() > 0) {
                        webview_content.setVisibility(View.VISIBLE);
                        webview_content.setBackgroundColor(Color.TRANSPARENT);
//                        webview_content.setText(Html.fromHtml(str_contentText));
                        webview_content.getSettings().setDefaultTextEncodingName("utf-8");
                        webview_content.getSettings().setJavaScriptEnabled(true);
                        webview_content.getSettings().setAllowFileAccess(true);
                        webview_content.getSettings().setJavaScriptEnabled(true);
                        webview_content.getSettings().setSupportMultipleWindows(true);

                        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///asset/Lato-Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
                        String pas = "</body></html>";
                        String myHtmlString = pish + str_contentText + pas;
                        webview_content.loadDataWithBaseURL("file:///asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);

                        webview_content.setWebChromeClient(new MyWebChromeclient());

                    } else {
                        webview_content.setVisibility(View.GONE);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyWebChromeclient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {

            WebView newWebView = new WebView(LoginMainScreen.this);
            view.addView(newWebView);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(newWebView);
            resultMsg.sendToTarget();
            newWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(url));
                        startActivity(browserIntent);
                        return true;
                    } else if (url.startsWith("tel:")) {
                        Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                        startActivity(tel);
                        return true;
                    } else if (url.startsWith("mailto:")) {
                        Intent mail = new Intent(Intent.ACTION_SEND);
                        mail.setType("application/octet-stream");
                        mail.putExtra(Intent.EXTRA_EMAIL, new String[]{"email address"});
                        mail.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                        mail.putExtra(Intent.EXTRA_TEXT, "");
                        startActivity(mail);
                        return true;
                    }

                    return true;
                }
            });
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2") || sessionManager.getEventType().equalsIgnoreCase("4")) {
            if (sessionManager.get_show_login_screen().equalsIgnoreCase("1")) {
                startActivity(new Intent(LoginMainScreen.this, SearchApp_Activity.class));
                finish();
            } else {
                startActivity(new Intent(LoginMainScreen.this, MainActivity.class));
                finish();
            }
        } else {
            startActivity(new Intent(LoginMainScreen.this, MainActivity.class));
            finish();
        }
    }
}
