package com.allintheloop.Activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.MainActivity;
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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class LoginActivity extends AppCompatActivity implements VolleyInterface {
    public static LinearLayout login_Layout;
    BoldTextView txtSignUp;
    BoldTextView txtforgot, btnLogin;
    EditText txtEmail, txtPass;
    EditText EdtAddiFirst, EdtAddiDescripation, EdtAddiNumber;
    Button btn_submitExtraInfo, btn_cancelButton;
    ImageView btnClose;
    Intent i;
    ImageView btnroundfacebook, btnLoginWithLinkedIn;
    LoginButton btnFacebook;
    String FbEmailId = "", Fb_name, FbId, Eventid, Fb_img, facebook_status;
    String userEmail, userPass, res_imgurl;
    LinearLayout fb_layout, linkedIn_layout, linear_extraInfo, linear_addextrainfo, static_fieldLayout;
    String gcm_id;
    SessionManager sessionManager;
    String linkedIn_firstName, linkedIn_lastName, linkedIn_img, linkedIn_email, linkedIn_title = "", linkedIn_company_name;
    String android_id;
    String EventId, FieldTitle, FieldType;
    ArrayList<String> arrayAddiChkList;
    ArrayList<EditText> SinglelineArray;
    ArrayList<EditText> MultilineArray;
    ArrayList<CheckBox> chkboxArray;
    ArrayList<RadioGroup> RadioGroupArray;
    ArrayList<Spinner> SpinnerArray;
    ArrayList<EditText> NumberArray;
    ArrayList<String> StrSinglelineArray;
    ArrayList<String> StrMultilineArray;
    ArrayList<String> StrchkboxArray;
    ArrayList<String> StrRadioGroupArray;
    ArrayList<String> StrSpinnerArray;
    ArrayList<String> StrNumberArray;
    String StrRadio, StrSpinner, StrJsonObj;
    String formStatus = "0";
    boolean isvalid = true;
    BoldTextView checkBox_terms;
    ImageView txtLoginFacebook;
    DefaultLanguage.DefaultLang defaultLang;
    CheckBox chkSelect;
    String str_logopath, str_contentText;
    ImageView img_logo, img_extrainfo;
    WebView webview_content;
    int[] colorIntArray;
    ColorStateList colorStateList;
    UidCommonKeyClass uidCommonKeyClass;
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private AccessTokenTracker accessTokenTracker;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    //    TextView webview_content;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();

            GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Bundle bFacebookData = getFacebookData(object);
                    //Toast.makeText(getApplicationContext(),"GDA"+bFacebookData.getString("__gda__"),Toast.LENGTH_SHORT).show();
                    //  Log.d("Profile_Pic",bFacebookData.getString("profile_pic"));

                    // FacebookServiceCall
                    FbLogin();
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name,email,gender, birthday,location"); // Parámetros que pedimos a facebook
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Log.i("Cancel", "Cancel");
            login_Layout.setVisibility(View.VISIBLE);
            fb_layout.setVisibility(View.GONE);
        }

        @Override
        public void onError(FacebookException e) {
            e.printStackTrace();
            login_Layout.setVisibility(View.VISIBLE);
            fb_layout.setVisibility(View.GONE);
        }
    };

    // This method is used to make permissions to retrieve data from linkedin
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };
        accessTokenTracker.startTracking();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sessionManager = new SessionManager(getApplicationContext());
        defaultLang = sessionManager.getMultiLangString();
        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        chkSelect = (CheckBox) findViewById(R.id.chkAgree);
        checkBox_terms = findViewById(R.id.checkBox_terms);
//        Spannable wordtoSpan = new SpannableString(defaultLang.getSignUpProcessIAgreeToTheTermsConditions());
//        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.btn_blue)), 14, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        checkBox_terms.setText(Html.fromHtml("<u>"+defaultLang.getSignUpProcessIAgreeToTheTermsConditions()+"</u>"));
        colorIntArray = new int[]
                {
                        getResources().getColor(R.color.darkGrayColor) //disabled
                        , getResources().getColor(R.color.darkGrayColor) //enabled

                };
        colorStateList = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled}, //disabled
                new int[]{android.R.attr.state_enabled} //enabled
        },
                colorIntArray
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            chkSelect.setButtonTintList(colorStateList);
        }
        checkBox_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManager.getTermsAndCondition() != null && sessionManager.getTermsAndCondition().length() > 0) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sessionManager.getTermsAndCondition()));
                    startActivity(browserIntent);
                }
            }
        });
        Eventid = sessionManager.getEventId();
        facebook_status = sessionManager.getFacebookStatus();

        Log.d("EventId", Eventid);
        Log.d("facebook_status", facebook_status);
        Log.d("AITL LINCKED", "" + sessionManager.getLinkedInStatus());
        txtSignUp = findViewById(R.id.txtSign);
        txtLoginFacebook = (ImageView) findViewById(R.id.txtLoginFacebook);
        txtEmail = (EditText) findViewById(R.id.txtLoginEmail);
        txtPass = (EditText) findViewById(R.id.txtLoginPass);
        btnLogin = findViewById(R.id.btnLogin);
        btn_cancelButton = (Button) findViewById(R.id.btn_cancelButton);
        btn_submitExtraInfo = (Button) findViewById(R.id.btn_submitExtraInfo);
        btnClose = (ImageView) findViewById(R.id.btnClose);
        btnFacebook = (LoginButton) findViewById(R.id.btnface_button);
        btnroundfacebook = (ImageView) findViewById(R.id.btnLoginWithFace);
        btnLoginWithLinkedIn = (ImageView) findViewById(R.id.btnLoginWithLinkedIn);
        login_Layout = (LinearLayout) findViewById(R.id.login_Layout);
        linkedIn_layout = (LinearLayout) findViewById(R.id.linkedIn_layout);
        linear_extraInfo = (LinearLayout) findViewById(R.id.linear_extraInfo);
        linear_addextrainfo = (LinearLayout) findViewById(R.id.linear_addextrainfo);
        static_fieldLayout = (LinearLayout) findViewById(R.id.static_fieldLayout);
        txtforgot = findViewById(R.id.txtforgot);
        img_logo = (ImageView) findViewById(R.id.img_logo);
        img_extrainfo = (ImageView) findViewById(R.id.img_extrainfo);
        webview_content = (WebView) findViewById(R.id.webview_content);
//        linear_linkedInbtnLayout= (LinearLayout) findViewById(R.id.linear_linkedInbtnLayout);
//        linear_fbbtnLayout= (LinearLayout) findViewById(R.id.linear_fbbtnLayout);
        fb_layout = (LinearLayout) findViewById(R.id.fb_layout);

        btn_cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();

            }
        });

        if (sessionManager.getEventType().equalsIgnoreCase("4")) {
            txtSignUp.setVisibility(View.GONE);
            txtforgot.setVisibility(View.GONE);
            txtPass.setVisibility(View.GONE);
        } else {
            if (sessionManager.getEventType().equalsIgnoreCase("1")) {
                txtSignUp.setVisibility(View.GONE);
            } else {
                txtSignUp.setVisibility(View.VISIBLE);
            }
            txtforgot.setVisibility(View.VISIBLE);
            txtPass.setVisibility(View.VISIBLE);
        }

        if (facebook_status.equalsIgnoreCase("1")) {
            if (sessionManager.getEventType().equalsIgnoreCase("4")) {
                txtLoginFacebook.setVisibility(View.GONE);
                btnroundfacebook.setVisibility(View.GONE);

            } else {
                txtLoginFacebook.setVisibility(View.VISIBLE);
                btnroundfacebook.setVisibility(View.VISIBLE);
            }
        } else {
            txtLoginFacebook.setVisibility(View.GONE);
            btnroundfacebook.setVisibility(View.GONE);
        }

        if (sessionManager.getLinkedInStatus().equalsIgnoreCase("1")) {
            if (sessionManager.getEventType().equalsIgnoreCase("4")) {
                txtLoginFacebook.setVisibility(View.GONE);
                btnLoginWithLinkedIn.setVisibility(View.GONE);
            } else {
                txtLoginFacebook.setVisibility(View.VISIBLE);
                btnLoginWithLinkedIn.setVisibility(View.VISIBLE);
            }
        } else {
            txtLoginFacebook.setVisibility(View.GONE);
            btnLoginWithLinkedIn.setVisibility(View.GONE);
        }


        arrayAddiChkList = new ArrayList<>();

        SinglelineArray = new ArrayList<>();
        MultilineArray = new ArrayList<>();
        chkboxArray = new ArrayList<>();
        RadioGroupArray = new ArrayList<>();
        SinglelineArray = new ArrayList<>();
        NumberArray = new ArrayList<>();

        StrSpinnerArray = new ArrayList<>();
        StrSinglelineArray = new ArrayList<>();
        StrMultilineArray = new ArrayList<>();
        StrchkboxArray = new ArrayList<>();
        StrRadioGroupArray = new ArrayList<>();
        StrSinglelineArray = new ArrayList<>();
        StrNumberArray = new ArrayList<>();
        SpinnerArray = new ArrayList<>();

        txtEmail.setHint(defaultLang.getSignUpProcessEmail());
        txtPass.setHint(defaultLang.getSignUpProcessPassword());
        btnLogin.setText(defaultLang.getSignUpProcessLogIn());
        txtforgot.setText(defaultLang.getSignUpProcessForgotPassword());
//        txtLoginFacebook.setText(defaultLang.getSignUpProcessOr());
        txtSignUp.setText(defaultLang.getSignUpProcessRegisterForTheApp());

        txtforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswrodActivity.class));
                finish();
            }
        });


        btn_submitExtraInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sessionManager.keyboradHidden(EdtAddiFirst);
                    JSONObject jsonObject = new JSONObject();
                    for (int f = 0; f < SinglelineArray.size(); f++) {
                        if (SinglelineArray.get(f).getText().toString().length() <= 0) {
                            isvalid = false;
                            EdtAddiFirst.setError(getResources().getString(R.string.validValue));
                            break;
                        } else {
                            isvalid = true;
                            jsonObject.put(StrSinglelineArray.get(f).toString(), SinglelineArray.get(f).getText().toString());
                        }
                    }
                    for (int d = 0; d < MultilineArray.size(); d++) {
                        jsonObject.put(StrMultilineArray.get(d).toString(), MultilineArray.get(d).getText().toString());
                    }
                    for (int a = 0; a < StrchkboxArray.size(); a++) {
                        JSONArray jsonChkArray = new JSONArray();
                        for (int c = 0; c < arrayAddiChkList.size(); c++) {
                            jsonChkArray.put(arrayAddiChkList.get(c).toString());
                        }
                        jsonObject.put(StrchkboxArray.get(a).toString(), jsonChkArray);
                    }
                    for (int s = 0; s < SpinnerArray.size(); s++) {
                        jsonObject.put(StrSpinnerArray.get(s).toString(), StrSpinner);
                    }
                    for (int n = 0; n < NumberArray.size(); n++) {
                        jsonObject.put(StrNumberArray.get(n).toString(), NumberArray.get(n).getText().toString());
                    }
                    for (int r = 0; r < RadioGroupArray.size(); r++) {

                        jsonObject.put(StrRadioGroupArray.get(r).toString(), StrRadio);
                    }
                    Log.d("AITL", jsonObject.toString());
                    //jsonArray.put(jsonObject);
                    StrJsonObj = jsonObject.toString();
                    if (isvalid)
                        submitFormData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("newToken", newToken);
            sessionManager.setGcm_id(newToken);
        });


        // GCM NOTIFCATION


//
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
//
//                    gcm_id = intent.getStringExtra("token");
//                    ;
//                    Log.d("gcmid", gcm_id);
//                    sessionManager.setGcm_id(gcm_id);
//                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
//                    Log.d("AITL", "GCM registration error");
////                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
//                } else {
//                    Log.d("AITL", "Error occurred");
////                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
//                }
//            }
//        };
//
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
//
//        //if play service is not available
//        if (ConnectionResult.SUCCESS != resultCode) {
//            //If play service is supported but not installed
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                //Displaying message that play service is not installed
//                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
//                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
//
//                //If play service is not supported
//                //Displaying an error message
//            } else {
//                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
//            }
//
//            //If play service is available
//        } else {
//            //Starting intent to register device
//            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
//            startService(itent);
//        }

        btnLoginWithLinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //linkedInLogin();
                login_linkedin();
                //printKeyHash(LoginActivity.this);
            }
        });


        btnroundfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnFacebook.performClick();
                login_Layout.setVisibility(View.GONE);
                fb_layout.setVisibility(View.VISIBLE);

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnFacebook.setReadPermissions(Arrays.asList("public_profile, email"));
        btnFacebook.registerCallback(callbackManager, callback);

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.getEventType().equalsIgnoreCase("4")) {
                    if (txtEmail.getText().length() <= 0) {
                        txtEmail.setError(getResources().getString(R.string.emailvalid));
                    }
                    if (!GlobalData.checkEmailValid(txtEmail.getText().toString())) {
                        txtEmail.setError(getResources().getString(R.string.mailValid));
                    }
                    if (!chkSelect.isChecked()) {
                        ToastC.show(LoginActivity.this, getResources().getString(R.string.TandO));
                    } else {
                        sessionManager.keyboradHidden(txtEmail);
                        userEmail = txtEmail.getText().toString();
                        authorizedLogin();

                    }
                } else {
                    if (txtEmail.getText().length() <= 0) {
                        txtEmail.setError(getResources().getString(R.string.emailvalid));

                    }
                    if (!GlobalData.checkEmailValid(txtEmail.getText().toString())) {
                        txtEmail.setError(getResources().getString(R.string.mailValid));
                    }
                    if (txtPass.getText().length() <= 0 || txtPass.getText().length() < 6) {
                        txtPass.setError(getResources().getString(R.string.passwordValid));
                    }
                    if (!chkSelect.isChecked()) {
                        ToastC.show(LoginActivity.this, getResources().getString(R.string.TandO));
                    } else {
                        if (GlobalData.isNetworkAvailable(getApplicationContext())) {
                            //new LoginService().execute();
                            userEmail = txtEmail.getText().toString();
                            userPass = txtPass.getText().toString();
                            Log.d("UserEmail", userEmail + userPass);

                            sessionManager.keyboradHidden(txtEmail);

                            Login(); // Login Service Call

                        }
                    }
                }
            }
        });

        getLoginLogo();
    }

    private void getLoginLogo() {
        if (GlobalData.isNetworkAvailable(LoginActivity.this)) {
            new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST, MyUrls.getloginScreenLogo, Param.getLoginLogo(sessionManager.getEventId()), 6, false, this);
        }
    }

    private void deleteUser() {
        new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST,
                MyUrls.deleteUser, Param.deleteUser(sessionManager.getUserId(),
                sessionManager.getRolId()), 5, false, this); //Postponed by server side developers - pending
    }

    public void login_linkedin() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
//                Toast.makeText(getApplicationContext(), "success" +
//                                LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(),
//                        Toast.LENGTH_LONG).show();
                getUserData();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                Log.d("AITL DataError", error.toString());
            }
        }, true);
    }

    public void getUserData() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(LoginActivity.this, "https://api.linkedin.com/v1/people/~:" +
                        "(email-address,first-name,last-name,picture-url,headline)",
                new ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse result) {
                        try {

                            JSONObject response = result.getResponseDataAsJson();
                            linkedIn_email = response.get("emailAddress").toString();
                            linkedIn_firstName = response.get("firstName").toString();
                            linkedIn_lastName = response.get("lastName").toString();
                            linkedIn_img = response.getString("pictureUrl");

                            linkedIn_company_name = response.getString("headline");
                            // linkedIn_title = response.getString("headline").split(" ")[0];

                            loginWithLinkedIn();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onApiError(LIApiError error) {
                        // ((TextView) findViewById(R.id.error)).setText(error.toString());
                    }
                });
    }

    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");

        // GCM NOTIFCATION

//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();
        String id;
        try {
            Log.d("AITL", "Facebook" + object.toString());
            id = object.getString("id");
            URL profile_pic = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
            Log.i("profile_pic", profile_pic + "");
            bundle.putString("profile_pic", profile_pic.toString());
            bundle.putString("idFacebook", id);

            Fb_img = profile_pic.toString();

            FbId = id;
            if (object.has("first_name")) {
                bundle.putString("first_name", object.getString("first_name"));
                Fb_name = object.getString("first_name");
                Log.d("FbName", Fb_name);
            }
            if (object.has("email")) {
                FbEmailId = object.getString("email");
                Log.d("AITL FBEMAIL", object.getString("email"));
                bundle.putString("email", object.getString("email"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bundle;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Log.d("first", "onActivityResult: " + requestCode + "  " + resultCode);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }


    private void submitFormData() {
        new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST, MyUrls.submitFormBuilderData, Param.submitFormData(StrJsonObj, sessionManager.getUserId()), 3, true, this);
    }


    private void authorizedLogin() {
        if (GlobalData.checkForUIDVersion()) {
            new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST, MyUrls.authorizedLoginUid, Param.authorizedLogin(userEmail, Eventid, sessionManager.getVersionCodeId()), 4, true, this);
        } else {
            new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST, MyUrls.authorizedLogin, Param.authorizedLogin(userEmail, Eventid, sessionManager.getVersionCodeId()), 4, true, this);
        }
    }

    private void Login() {

        if (GlobalData.checkForUIDVersion()) {
            new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST, MyUrls.LoginUid, Param.Login(userEmail, userPass, Eventid, sessionManager.getVersionCodeId()), 0, true, this);
        } else {
            new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST, MyUrls.Login, Param.Login(userEmail, userPass, Eventid, sessionManager.getVersionCodeId()), 0, true, this);
        }
    }

    private void loginWithLinkedIn() {
        if (GlobalData.checkForUIDVersion()) {
            new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST, MyUrls.linedInLoginUid, Param.loginWithLinkedIn(sessionManager.getEventId(), linkedIn_email, linkedIn_firstName, linkedIn_lastName, linkedIn_img, "android", linkedIn_title, linkedIn_company_name, sessionManager.getVersionCodeId()), 1, true, this);
        } else {
            new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST, MyUrls.linedInLogin, Param.loginWithLinkedIn(sessionManager.getEventId(), linkedIn_email, linkedIn_firstName, linkedIn_lastName, linkedIn_img, "android", linkedIn_title, linkedIn_company_name, sessionManager.getVersionCodeId()), 1, true, this);
        }
    }

    private void saveDeviceId() {
        if (GlobalData.isNetworkAvailable(LoginActivity.this)) {
            new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST, MyUrls.OpenApp, Param.saveDeviceId(sessionManager.getEventId(), android_id), 5, false, this);
        }
    }


    private void FbLogin() {
        if (!FbEmailId.equalsIgnoreCase("")) {
            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST, MyUrls.FbLoginUid, Param.FacebookLogin(FbEmailId, FbId, Fb_name, Eventid, Fb_img, "android", sessionManager.getVersionCodeId()), 1, true, this);
            } else {
                new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST, MyUrls.FbLogin, Param.FacebookLogin(FbEmailId, FbId, Fb_name, Eventid, Fb_img, "android", sessionManager.getVersionCodeId()), 1, true, this);
            }
        } else {
            login_Layout.setVisibility(View.VISIBLE);
            fb_layout.setVisibility(View.GONE);
            loginManager.getInstance().logOut();
//          ToastC.show(getApplicationContext(), "Email not available");
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.emailNot), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");

        login_Layout.setVisibility(View.VISIBLE);


        // GCM NOTIFICATION

//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0://log in
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL Login", jsonObject.toString());
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        //ToastC.show(LoginActivity.this, jsonObject.optString(Param.KEY_MESSAGE));
                        Log.d("AITL LOGINRESPONSE", jsonObject.toString());
                        sessionManager.loginResponse(jsonObject, true);
                        sessionManager.setFormStatus("1");
                        updateGCM();
                        if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
                            saveDeviceId();
                        }
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        login_Layout.setVisibility(View.VISIBLE);
                        linkedIn_layout.setVisibility(View.GONE);
                        fb_layout.setVisibility(View.GONE);
                        ToastC.show(LoginActivity.this, jsonObject.optString(Param.KEY_MESSAGE));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1: //Fb Login
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL FBLOFINLogin", jsonObject.toString());
                    if (jsonObject.optString("success").equalsIgnoreCase("true")) {
                        sessionManager.setFormStatus("1");
                        formStatus = jsonObject.getString("status");
                        if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                            arrayAddiChkList = new ArrayList<>();

                            SinglelineArray = new ArrayList<>();
                            MultilineArray = new ArrayList<>();
                            chkboxArray = new ArrayList<>();
                            RadioGroupArray = new ArrayList<>();
                            SinglelineArray = new ArrayList<>();
                            NumberArray = new ArrayList<>();

                            StrSpinnerArray = new ArrayList<>();
                            StrSinglelineArray = new ArrayList<>();
                            StrMultilineArray = new ArrayList<>();
                            StrchkboxArray = new ArrayList<>();
                            StrRadioGroupArray = new ArrayList<>();
                            StrSinglelineArray = new ArrayList<>();
                            StrNumberArray = new ArrayList<>();
                            SpinnerArray = new ArrayList<>();

                            Log.d("AITL INSIDE STATUS", jsonObject.getString("status"));
                            sessionManager.loginResponse(jsonObject, true);
                            updateGCM();

                            if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
                                saveDeviceId();
                            }
                            login_Layout.setVisibility(View.VISIBLE);
                            fb_layout.setVisibility(View.GONE);
                            linkedIn_layout.setVisibility(View.GONE);
                            static_fieldLayout.setVisibility(View.GONE);
                            linear_extraInfo.setVisibility(View.VISIBLE);
                            btnClose.setVisibility(View.GONE);
                            JSONObject jsonData = jsonObject.optJSONObject("formbuilder_data");
                            JSONArray jsonArrayField = jsonData.optJSONArray("fields");
                            for (int i = 0; i < jsonArrayField.length(); i++) {
                                JSONObject jsonObjField = (JSONObject) jsonArrayField.get(i);
                                FieldTitle = jsonObjField.optString("title");
                                FieldType = jsonObjField.optString("type");

                                if (FieldType.equalsIgnoreCase("element-single-line-text")) {
                                    EdtAddiFirst = new EditText(LoginActivity.this);
                                    EdtAddiFirst.setHint(FieldTitle);
                                    EdtAddiFirst.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                    EdtAddiFirst.setTextSize(15);
                                    EdtAddiFirst.setPadding(20, 25, 0, 25);
                                    EdtAddiFirst.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    EdtAddiFirst.setLayoutParams(params);
                                    linear_addextrainfo.addView(EdtAddiFirst);

                                    SinglelineArray.add(EdtAddiFirst);
                                    StrSinglelineArray.add(FieldTitle);

                                } else if (FieldType.equalsIgnoreCase("element-paragraph-text")) {
                                    EdtAddiDescripation = new EditText(LoginActivity.this);
                                    EdtAddiDescripation.setHint(FieldTitle);
                                    EdtAddiDescripation.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                    EdtAddiDescripation.setTextSize(15);
                                    EdtAddiDescripation.setPadding(20, 25, 0, 25);
                                    EdtAddiDescripation.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                    EdtAddiDescripation.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    EdtAddiDescripation.setLayoutParams(params);
                                    linear_addextrainfo.addView(EdtAddiDescripation);
                                    MultilineArray.add(EdtAddiDescripation);
                                    StrMultilineArray.add(FieldTitle);
                                } else if (FieldType.equalsIgnoreCase("element-multiple-choice")) {
                                    TextView txtchk = new TextView(LoginActivity.this);
                                    txtchk.setText(FieldTitle);
                                    txtchk.setTextSize(15);
                                    txtchk.setTypeface(null, Typeface.BOLD);
                                    txtchk.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                    txtchk.setPadding(20, 25, 0, 25);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    txtchk.setLayoutParams(params);
                                    linear_addextrainfo.addView(txtchk);

                                    JSONArray jArrayChkMultiple = jsonObjField.optJSONArray("choices");
                                    StrchkboxArray.add(FieldTitle);
                                    for (int chk = 0; chk < jArrayChkMultiple.length(); chk++) {
                                        JSONObject jObjectChk = (JSONObject) jArrayChkMultiple.get(chk);
                                        final CheckBox chkBox = new CheckBox(LoginActivity.this);
                                        chkBox.setText(jObjectChk.optString("title"));
                                        chkBox.setPadding(10, 15, 0, 15);
                                        chkBox.setTextSize(15);
                                        chkBox.setId(chk);
                                        if (chkBox.getId() == 0) {
                                            chkBox.setChecked(true);
                                            arrayAddiChkList.add(chkBox.getText().toString());
                                        }

                                        chkBox.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            chkBox.setButtonTintList(colorStateList);
                                        }
                                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        params1.leftMargin = 20;
                                        chkBox.setLayoutParams(params1);
                                        linear_addextrainfo.addView(chkBox);
                                        chkboxArray.add(chkBox);
                                        chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                if (isChecked) {


                                                    arrayAddiChkList.add(chkBox.getText().toString());
                                                    chkboxArray.add(chkBox);

                                                } else {
                                                    arrayAddiChkList.remove(chkBox.getText().toString());
                                                    chkboxArray.add(chkBox);

                                                }
                                            }
                                        });

                                    }

                                } else if (FieldType.equalsIgnoreCase("element-section-break")) {
                                    View line = new View(LoginActivity.this);
                                    line.setBackgroundColor(getResources().getColor(R.color.hintcolor));
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
                                    line.setLayoutParams(params);
                                    linear_addextrainfo.addView(line);
                                } else if (FieldType.equalsIgnoreCase("element-number")) {
                                    EdtAddiNumber = new EditText(LoginActivity.this);
                                    EdtAddiNumber.setHint(FieldTitle);
                                    EdtAddiNumber.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                    EdtAddiNumber.setTextSize(15);
                                    EdtAddiNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    EdtAddiNumber.setPadding(20, 25, 0, 25);
                                    EdtAddiNumber.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    params.topMargin = 10;
                                    EdtAddiNumber.setLayoutParams(params);
                                    linear_addextrainfo.addView(EdtAddiNumber);
                                    StrNumberArray.add(FieldTitle);
                                    NumberArray.add(EdtAddiNumber);

                                } else if (FieldType.equalsIgnoreCase("element-checkboxes")) {
                                    TextView txtRdo = new TextView(LoginActivity.this);
                                    txtRdo.setText(FieldTitle);
                                    txtRdo.setTextSize(15);
                                    txtRdo.setTypeface(null, Typeface.BOLD);
                                    txtRdo.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                    txtRdo.setPadding(10, 15, 0, 15);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    txtRdo.setLayoutParams(params);
                                    linear_addextrainfo.addView(txtRdo);
                                    StrRadioGroupArray.add(FieldTitle);
                                    JSONArray jArrayRdo = jsonObjField.optJSONArray("choices");
                                    Log.d("ArraySize", "" + jArrayRdo.length());
                                    RadioGroup radioGroup = new RadioGroup(LoginActivity.this);

                                    for (int j = 0; j < jArrayRdo.length(); j++) {
                                        JSONObject jObjectRdo = (JSONObject) jArrayRdo.get(j);
                                        Log.d("Count", " " + j);

                                        RadioButton radioButton = new RadioButton(LoginActivity.this);
                                        radioButton.setText(jObjectRdo.optString("title"));
                                        radioButton.setPadding(15, 15, 0, 15);
                                        radioButton.setTextSize(15);
                                        radioButton.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            radioButton.setButtonTintList(colorStateList);
                                        }
                                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        params1.leftMargin = 20;
                                        radioGroup.addView(radioButton);
                                        radioButton.setLayoutParams(params1);

                                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                                                StrRadio = radioButton.getText().toString();
                                                Log.d("Radio button", StrRadio);
                                            }
                                        });

                                    }
                                    linear_addextrainfo.addView(radioGroup);
                                    RadioGroupArray.add(radioGroup);

                                } else if (FieldType.equalsIgnoreCase("element-dropdown")) {
                                    TextView txtSpinner = new TextView(LoginActivity.this);
                                    txtSpinner.setText(FieldTitle);
                                    txtSpinner.setTextSize(15);
                                    txtSpinner.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                    txtSpinner.setTypeface(null, Typeface.BOLD);
                                    txtSpinner.setPadding(10, 15, 0, 15);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                    txtSpinner.setLayoutParams(params);
                                    linear_addextrainfo.addView(txtSpinner);

                                    JSONArray jArraySpinner = jsonObjField.optJSONArray("choices");
                                    final Spinner spinner = new Spinner(LoginActivity.this);

                                    ArrayList<String> arrayList = new ArrayList<>();
                                    for (int k = 0; k < jArraySpinner.length(); k++) {
                                        JSONObject jObjectSpinner = (JSONObject) jArraySpinner.get(k);

                                        arrayList.add(jObjectSpinner.optString("title"));

                                    }
                                    ArrayAdapter<String> adp = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1, arrayList);
                                    spinner.setAdapter(adp);
                                    spinner.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_white_profile));
                                    spinner.setLayoutParams(params);
                                    linear_addextrainfo.addView(spinner);
                                    SpinnerArray.add(spinner);
                                    StrSpinnerArray.add(FieldTitle);
                                    StrSpinner = spinner.getItemAtPosition(0).toString();
                                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            StrSpinner = spinner.getSelectedItem().toString();
                                            ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                }
                            }
                        } else if (jsonObject.getString("status").equalsIgnoreCase("0")) {
                            static_fieldLayout.setVisibility(View.GONE);
                            sessionManager.loginResponse(jsonObject, true);
                            updateGCM();
                            if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
                                saveDeviceId();
                            }
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        login_Layout.setVisibility(View.VISIBLE);
                        fb_layout.setVisibility(View.GONE);
                        linkedIn_layout.setVisibility(View.GONE);
                        ToastC.show(LoginActivity.this, jsonObject.optString(Param.KEY_MESSAGE));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL Login", jsonObject.toString());

                    Log.d("AITL UPDATEGCM", "UPDATEGCM");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL  SUBMITDATA", jsonObject.toString());

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4: //Authorized Login
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL Authorized", jsonObject.toString());
                    if (jsonObject.optString("success").equalsIgnoreCase("true")) {

                        if (jsonObject.getString("login_status").equalsIgnoreCase("true")) {

                            arrayAddiChkList = new ArrayList<>();

                            SinglelineArray = new ArrayList<>();
                            MultilineArray = new ArrayList<>();
                            chkboxArray = new ArrayList<>();
                            RadioGroupArray = new ArrayList<>();
                            SinglelineArray = new ArrayList<>();
                            NumberArray = new ArrayList<>();

                            StrSpinnerArray = new ArrayList<>();
                            StrSinglelineArray = new ArrayList<>();
                            StrMultilineArray = new ArrayList<>();
                            StrchkboxArray = new ArrayList<>();
                            StrRadioGroupArray = new ArrayList<>();
                            StrSinglelineArray = new ArrayList<>();
                            StrNumberArray = new ArrayList<>();
                            SpinnerArray = new ArrayList<>();

                            sessionManager.loginResponse(jsonObject, true);
                            updateGCM();
                            if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
                                saveDeviceId();
                            }

                            if (GlobalData.checkForUIDVersion()) {
                                uidCommonKeyClass = sessionManager.getUidCommonKey();
                                if (uidCommonKeyClass.getIsOnlyAttendeeUser().equalsIgnoreCase("1")) {
                                    formStatus = jsonObject.getString("status");
                                    sessionManager.setFormStatus("1");
                                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                                        Log.d("AITL INSIDE STATUS", jsonObject.getString("status"));
//                                    SessionManager sessionManager = new SessionManager(getApplicationContext());
//                                    sessionManager.loginResponse(jsonObject, true);
//                                    updateGCM();
//                                    if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
//                                        saveDeviceId();
//                                    }
                                        login_Layout.setVisibility(View.VISIBLE);
                                        fb_layout.setVisibility(View.GONE);
                                        linkedIn_layout.setVisibility(View.GONE);
                                        btnClose.setVisibility(View.GONE);
                                        static_fieldLayout.setVisibility(View.GONE);
                                        linear_extraInfo.setVisibility(View.VISIBLE);
                                        JSONObject jsonData = jsonObject.optJSONObject("formbuilder_data");
                                        JSONArray jsonArrayField = jsonData.optJSONArray("fields");
                                        for (int i = 0; i < jsonArrayField.length(); i++) {
                                            JSONObject jsonObjField = (JSONObject) jsonArrayField.get(i);
                                            FieldTitle = jsonObjField.optString("title");
                                            FieldType = jsonObjField.optString("type");

                                            if (FieldType.equalsIgnoreCase("element-single-line-text")) {
                                                EdtAddiFirst = new EditText(LoginActivity.this);
                                                EdtAddiFirst.setHint(FieldTitle);
                                                EdtAddiFirst.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                                EdtAddiFirst.setTextSize(15);
                                                EdtAddiFirst.setPadding(20, 25, 0, 25);
                                                EdtAddiFirst.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                EdtAddiFirst.setLayoutParams(params);
                                                linear_addextrainfo.addView(EdtAddiFirst);

                                                SinglelineArray.add(EdtAddiFirst);
                                                StrSinglelineArray.add(FieldTitle);

                                            } else if (FieldType.equalsIgnoreCase("element-paragraph-text")) {
                                                EdtAddiDescripation = new EditText(LoginActivity.this);
                                                EdtAddiDescripation.setHint(FieldTitle);
                                                EdtAddiDescripation.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                                EdtAddiDescripation.setTextSize(15);
                                                EdtAddiDescripation.setPadding(20, 25, 0, 25);
                                                EdtAddiDescripation.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                                EdtAddiDescripation.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                EdtAddiDescripation.setLayoutParams(params);
                                                linear_addextrainfo.addView(EdtAddiDescripation);
                                                MultilineArray.add(EdtAddiDescripation);
                                                StrMultilineArray.add(FieldTitle);
                                            } else if (FieldType.equalsIgnoreCase("element-multiple-choice")) {
                                                TextView txtchk = new TextView(LoginActivity.this);
                                                txtchk.setText(FieldTitle);
                                                txtchk.setTextSize(15);
                                                txtchk.setTypeface(null, Typeface.BOLD);
                                                txtchk.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                                txtchk.setPadding(20, 25, 0, 25);
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                txtchk.setLayoutParams(params);
                                                linear_addextrainfo.addView(txtchk);

                                                JSONArray jArrayChkMultiple = jsonObjField.optJSONArray("choices");
                                                StrchkboxArray.add(FieldTitle);
                                                for (int chk = 0; chk < jArrayChkMultiple.length(); chk++) {
                                                    JSONObject jObjectChk = (JSONObject) jArrayChkMultiple.get(chk);
                                                    final CheckBox chkBox = new CheckBox(LoginActivity.this);
                                                    chkBox.setText(jObjectChk.optString("title"));
                                                    chkBox.setPadding(10, 15, 0, 15);
                                                    chkBox.setTextSize(15);
                                                    chkBox.setId(chk);
                                                    if (chkBox.getId() == 0) {
                                                        chkBox.setChecked(true);
                                                        arrayAddiChkList.add(chkBox.getText().toString());
                                                    }


                                                    chkBox.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                        chkBox.setButtonTintList(colorStateList);
                                                    }
                                                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                    params1.leftMargin = 20;
                                                    chkBox.setLayoutParams(params1);
                                                    linear_addextrainfo.addView(chkBox);
                                                    chkboxArray.add(chkBox);
                                                    chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                        @Override
                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                            if (isChecked) {


                                                                arrayAddiChkList.add(chkBox.getText().toString());
                                                                chkboxArray.add(chkBox);

                                                            } else {
                                                                arrayAddiChkList.remove(chkBox.getText().toString());
                                                                chkboxArray.add(chkBox);

                                                            }
                                                        }
                                                    });

                                                }

                                            } else if (FieldType.equalsIgnoreCase("element-section-break")) {
                                                View line = new View(LoginActivity.this);
                                                line.setBackgroundColor(getResources().getColor(R.color.hintcolor));
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
                                                line.setLayoutParams(params);
                                                linear_addextrainfo.addView(line);
                                            } else if (FieldType.equalsIgnoreCase("element-number")) {
                                                EdtAddiNumber = new EditText(LoginActivity.this);
                                                EdtAddiNumber.setHint(FieldTitle);
                                                EdtAddiNumber.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                                EdtAddiNumber.setTextSize(15);
                                                EdtAddiNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                EdtAddiNumber.setPadding(20, 25, 0, 25);
                                                EdtAddiNumber.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                params.topMargin = 10;
                                                EdtAddiNumber.setLayoutParams(params);
                                                linear_addextrainfo.addView(EdtAddiNumber);
                                                StrNumberArray.add(FieldTitle);
                                                NumberArray.add(EdtAddiNumber);

                                            } else if (FieldType.equalsIgnoreCase("element-checkboxes")) {
                                                TextView txtRdo = new TextView(LoginActivity.this);
                                                txtRdo.setText(FieldTitle);
                                                txtRdo.setTextSize(15);
                                                txtRdo.setTypeface(null, Typeface.BOLD);
                                                txtRdo.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                                txtRdo.setPadding(10, 15, 0, 15);
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                txtRdo.setLayoutParams(params);
                                                linear_addextrainfo.addView(txtRdo);
                                                StrRadioGroupArray.add(FieldTitle);
                                                JSONArray jArrayRdo = jsonObjField.optJSONArray("choices");
                                                Log.d("ArraySize", "" + jArrayRdo.length());
                                                RadioGroup radioGroup = new RadioGroup(LoginActivity.this);

                                                for (int j = 0; j < jArrayRdo.length(); j++) {
                                                    JSONObject jObjectRdo = (JSONObject) jArrayRdo.get(j);
                                                    Log.d("Count", " " + j);

                                                    RadioButton radioButton = new RadioButton(LoginActivity.this);
                                                    radioButton.setText(jObjectRdo.optString("title"));
                                                    radioButton.setPadding(15, 15, 0, 15);
                                                    radioButton.setTextSize(15);
                                                    radioButton.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                        radioButton.setButtonTintList(colorStateList);
                                                    }
                                                    radioButton.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                    params1.leftMargin = 20;
                                                    radioGroup.addView(radioButton);
                                                    radioButton.setLayoutParams(params1);

                                                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                        @Override
                                                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                                                            StrRadio = radioButton.getText().toString();
                                                            Log.d("Radio button", StrRadio);
                                                        }
                                                    });

                                                }
                                                linear_addextrainfo.addView(radioGroup);
                                                RadioGroupArray.add(radioGroup);

                                            } else if (FieldType.equalsIgnoreCase("element-dropdown")) {
                                                TextView txtSpinner = new TextView(LoginActivity.this);
                                                txtSpinner.setText(FieldTitle);
                                                txtSpinner.setTextSize(15);
                                                txtSpinner.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                                txtSpinner.setTypeface(null, Typeface.BOLD);
                                                txtSpinner.setPadding(10, 15, 0, 15);
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                txtSpinner.setLayoutParams(params);
                                                linear_addextrainfo.addView(txtSpinner);


                                                JSONArray jArraySpinner = jsonObjField.optJSONArray("choices");
                                                final Spinner spinner = new Spinner(LoginActivity.this);

                                                ArrayList<String> arrayList = new ArrayList<>();
                                                for (int k = 0; k < jArraySpinner.length(); k++) {
                                                    JSONObject jObjectSpinner = (JSONObject) jArraySpinner.get(k);

                                                    arrayList.add(jObjectSpinner.optString("title"));

                                                }
                                                ArrayAdapter<String> adp = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1, arrayList);
                                                spinner.setAdapter(adp);
                                                spinner.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_white_profile));
                                                spinner.setLayoutParams(params);
                                                linear_addextrainfo.addView(spinner);
                                                SpinnerArray.add(spinner);
                                                StrSpinnerArray.add(FieldTitle);
                                                StrSpinner = spinner.getItemAtPosition(0).toString();
                                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        StrSpinner = spinner.getSelectedItem().toString();
                                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });

                                            }
                                        }
                                    } else if (jsonObject.getString("status").equalsIgnoreCase("0")) {

                                        login_Layout.setVisibility(View.VISIBLE);
                                        linear_extraInfo.setVisibility(View.GONE);
                                        static_fieldLayout.setVisibility(View.VISIBLE);
//                                    SessionManager sessionManager = new SessionManager(getApplicationContext());
//                                    sessionManager.loginResponse(jsonObject, true);
//                                    updateGCM();
//                                    if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
//                                        saveDeviceId();
//                                    }
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                } else {
                                    login_Layout.setVisibility(View.VISIBLE);
                                    linear_extraInfo.setVisibility(View.GONE);
                                    static_fieldLayout.setVisibility(View.VISIBLE);
//                                SessionManager sessionManager = new SessionManager(getApplicationContext());
//                                sessionManager.loginResponse(jsonObject, true);
//                                updateGCM();
//                                if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
//                                    saveDeviceId();
//                                }
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }

                            } else {

                                if (sessionManager.getRolId().equalsIgnoreCase("4")) { //changes applied
                                    formStatus = jsonObject.getString("status");
                                    sessionManager.setFormStatus("1");
                                    if (jsonObject.getString("status").equalsIgnoreCase("1")) {

                                        Log.d("AITL INSIDE STATUS", jsonObject.getString("status"));
//                                    SessionManager sessionManager = new SessionManager(getApplicationContext());
//                                    sessionManager.loginResponse(jsonObject, true);
//                                    updateGCM();
//                                    if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
//                                        saveDeviceId();
//                                    }
                                        login_Layout.setVisibility(View.VISIBLE);
                                        fb_layout.setVisibility(View.GONE);
                                        linkedIn_layout.setVisibility(View.GONE);
                                        btnClose.setVisibility(View.GONE);
                                        static_fieldLayout.setVisibility(View.GONE);
                                        linear_extraInfo.setVisibility(View.VISIBLE);
                                        JSONObject jsonData = jsonObject.optJSONObject("formbuilder_data");
                                        JSONArray jsonArrayField = jsonData.optJSONArray("fields");
                                        for (int i = 0; i < jsonArrayField.length(); i++) {
                                            JSONObject jsonObjField = (JSONObject) jsonArrayField.get(i);
                                            FieldTitle = jsonObjField.optString("title");
                                            FieldType = jsonObjField.optString("type");

                                            if (FieldType.equalsIgnoreCase("element-single-line-text")) {
                                                EdtAddiFirst = new EditText(LoginActivity.this);
                                                EdtAddiFirst.setHint(FieldTitle);
                                                EdtAddiFirst.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                                EdtAddiFirst.setTextSize(15);
                                                EdtAddiFirst.setPadding(20, 25, 0, 25);
                                                EdtAddiFirst.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                EdtAddiFirst.setLayoutParams(params);
                                                linear_addextrainfo.addView(EdtAddiFirst);

                                                SinglelineArray.add(EdtAddiFirst);
                                                StrSinglelineArray.add(FieldTitle);

                                            } else if (FieldType.equalsIgnoreCase("element-paragraph-text")) {
                                                EdtAddiDescripation = new EditText(LoginActivity.this);
                                                EdtAddiDescripation.setHint(FieldTitle);
                                                EdtAddiDescripation.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                                EdtAddiDescripation.setTextSize(15);
                                                EdtAddiDescripation.setPadding(20, 25, 0, 25);
                                                EdtAddiDescripation.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                                EdtAddiDescripation.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                EdtAddiDescripation.setLayoutParams(params);
                                                linear_addextrainfo.addView(EdtAddiDescripation);
                                                MultilineArray.add(EdtAddiDescripation);
                                                StrMultilineArray.add(FieldTitle);
                                            } else if (FieldType.equalsIgnoreCase("element-multiple-choice")) {
                                                TextView txtchk = new TextView(LoginActivity.this);
                                                txtchk.setText(FieldTitle);
                                                txtchk.setTextSize(15);
                                                txtchk.setTypeface(null, Typeface.BOLD);
                                                txtchk.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                                txtchk.setPadding(20, 25, 0, 25);
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                txtchk.setLayoutParams(params);
                                                linear_addextrainfo.addView(txtchk);

                                                JSONArray jArrayChkMultiple = jsonObjField.optJSONArray("choices");
                                                StrchkboxArray.add(FieldTitle);
                                                for (int chk = 0; chk < jArrayChkMultiple.length(); chk++) {
                                                    JSONObject jObjectChk = (JSONObject) jArrayChkMultiple.get(chk);
                                                    final CheckBox chkBox = new CheckBox(LoginActivity.this);
                                                    chkBox.setText(jObjectChk.optString("title"));
                                                    chkBox.setPadding(10, 15, 0, 15);
                                                    chkBox.setTextSize(15);
                                                    chkBox.setId(chk);
                                                    if (chkBox.getId() == 0) {
                                                        chkBox.setChecked(true);
                                                        arrayAddiChkList.add(chkBox.getText().toString());
                                                    }


                                                    chkBox.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                        chkBox.setButtonTintList(colorStateList);
                                                    }
                                                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                    params1.leftMargin = 20;
                                                    chkBox.setLayoutParams(params1);
                                                    linear_addextrainfo.addView(chkBox);
                                                    chkboxArray.add(chkBox);
                                                    chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                        @Override
                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                            if (isChecked) {


                                                                arrayAddiChkList.add(chkBox.getText().toString());
                                                                chkboxArray.add(chkBox);

                                                            } else {
                                                                arrayAddiChkList.remove(chkBox.getText().toString());
                                                                chkboxArray.add(chkBox);

                                                            }
                                                        }
                                                    });

                                                }

                                            } else if (FieldType.equalsIgnoreCase("element-section-break")) {
                                                View line = new View(LoginActivity.this);
                                                line.setBackgroundColor(getResources().getColor(R.color.hintcolor));
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
                                                line.setLayoutParams(params);
                                                linear_addextrainfo.addView(line);
                                            } else if (FieldType.equalsIgnoreCase("element-number")) {
                                                EdtAddiNumber = new EditText(LoginActivity.this);
                                                EdtAddiNumber.setHint(FieldTitle);
                                                EdtAddiNumber.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                                EdtAddiNumber.setTextSize(15);
                                                EdtAddiNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                EdtAddiNumber.setPadding(20, 25, 0, 25);
                                                EdtAddiNumber.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                params.topMargin = 10;
                                                EdtAddiNumber.setLayoutParams(params);
                                                linear_addextrainfo.addView(EdtAddiNumber);
                                                StrNumberArray.add(FieldTitle);
                                                NumberArray.add(EdtAddiNumber);

                                            } else if (FieldType.equalsIgnoreCase("element-checkboxes")) {
                                                TextView txtRdo = new TextView(LoginActivity.this);
                                                txtRdo.setText(FieldTitle);
                                                txtRdo.setTextSize(15);
                                                txtRdo.setTypeface(null, Typeface.BOLD);
                                                txtRdo.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                                txtRdo.setPadding(10, 15, 0, 15);
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                txtRdo.setLayoutParams(params);
                                                linear_addextrainfo.addView(txtRdo);
                                                StrRadioGroupArray.add(FieldTitle);
                                                JSONArray jArrayRdo = jsonObjField.optJSONArray("choices");
                                                Log.d("ArraySize", "" + jArrayRdo.length());
                                                RadioGroup radioGroup = new RadioGroup(LoginActivity.this);

                                                for (int j = 0; j < jArrayRdo.length(); j++) {
                                                    JSONObject jObjectRdo = (JSONObject) jArrayRdo.get(j);
                                                    Log.d("Count", " " + j);

                                                    RadioButton radioButton = new RadioButton(LoginActivity.this);
                                                    radioButton.setText(jObjectRdo.optString("title"));
                                                    radioButton.setPadding(15, 15, 0, 15);
                                                    radioButton.setTextSize(15);
                                                    radioButton.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                        radioButton.setButtonTintList(colorStateList);
                                                    }
                                                    radioButton.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                    params1.leftMargin = 20;
                                                    radioGroup.addView(radioButton);
                                                    radioButton.setLayoutParams(params1);

                                                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                        @Override
                                                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                                                            StrRadio = radioButton.getText().toString();
                                                            Log.d("Radio button", StrRadio);
                                                        }
                                                    });

                                                }
                                                linear_addextrainfo.addView(radioGroup);
                                                RadioGroupArray.add(radioGroup);

                                            } else if (FieldType.equalsIgnoreCase("element-dropdown")) {
                                                TextView txtSpinner = new TextView(LoginActivity.this);
                                                txtSpinner.setText(FieldTitle);
                                                txtSpinner.setTextSize(15);
                                                txtSpinner.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                                txtSpinner.setTypeface(null, Typeface.BOLD);
                                                txtSpinner.setPadding(10, 15, 0, 15);
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                                txtSpinner.setLayoutParams(params);
                                                linear_addextrainfo.addView(txtSpinner);


                                                JSONArray jArraySpinner = jsonObjField.optJSONArray("choices");
                                                final Spinner spinner = new Spinner(LoginActivity.this);

                                                ArrayList<String> arrayList = new ArrayList<>();
                                                for (int k = 0; k < jArraySpinner.length(); k++) {
                                                    JSONObject jObjectSpinner = (JSONObject) jArraySpinner.get(k);

                                                    arrayList.add(jObjectSpinner.optString("title"));

                                                }
                                                ArrayAdapter<String> adp = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1, arrayList);
                                                spinner.setAdapter(adp);
                                                spinner.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_white_profile));
                                                spinner.setLayoutParams(params);
                                                linear_addextrainfo.addView(spinner);
                                                SpinnerArray.add(spinner);
                                                StrSpinnerArray.add(FieldTitle);
                                                StrSpinner = spinner.getItemAtPosition(0).toString();
                                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                        StrSpinner = spinner.getSelectedItem().toString();
                                                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> parent) {

                                                    }
                                                });

                                            }
                                        }
                                    } else if (jsonObject.getString("status").equalsIgnoreCase("0")) {

                                        login_Layout.setVisibility(View.VISIBLE);
                                        linear_extraInfo.setVisibility(View.GONE);
                                        static_fieldLayout.setVisibility(View.VISIBLE);
//                                    SessionManager sessionManager = new SessionManager(getApplicationContext());
//                                    sessionManager.loginResponse(jsonObject, true);
//                                    updateGCM();
//                                    if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
//                                        saveDeviceId();
//                                    }
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                } else {
                                    login_Layout.setVisibility(View.VISIBLE);
                                    linear_extraInfo.setVisibility(View.GONE);
                                    static_fieldLayout.setVisibility(View.VISIBLE);
//                                SessionManager sessionManager = new SessionManager(getApplicationContext());
//                                sessionManager.loginResponse(jsonObject, true);
//                                updateGCM();
//                                if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
//                                    saveDeviceId();
//                                }
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }


                            }

                        } else {
                            ToastC.show(LoginActivity.this, jsonObject.getString("message"));
                        }


                    } else {
                        login_Layout.setVisibility(View.VISIBLE);
                        fb_layout.setVisibility(View.GONE);
                        linkedIn_layout.setVisibility(View.GONE);
                        ToastC.show(LoginActivity.this, jsonObject.optString(Param.KEY_MESSAGE));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL DeleteUser", jsonObject.toString());
                    if (jsonObject.optString("success").equalsIgnoreCase("true")) {
                        txtEmail.setText("");
                        txtPass.setText("");
                        userEmail = "";
                        userPass = "";
                        login_Layout.setVisibility(View.VISIBLE);
                        fb_layout.setVisibility(View.GONE);
                        linkedIn_layout.setVisibility(View.GONE);
                        static_fieldLayout.setVisibility(View.VISIBLE);
                        linear_extraInfo.setVisibility(View.GONE);
                        btnClose.setVisibility(View.VISIBLE);
                        sessionManager.logout();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL DeleteUser", jsonObject.toString());
                    if (jsonObject.optString("success").equalsIgnoreCase("true")) {
                        setLoginLogoandContent(jsonObject);
                    } else {
                        if (!jsonObject.getString("URL").isEmpty()) {
                            sessionManager.setTermsAndCondition(jsonObject.getString("URL"));
                        } else {
                            sessionManager.setTermsAndCondition("http://allintheloop.com/terms.html");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private void setLoginLogoandContent(JSONObject jsonObject) {
        try {
            if (!jsonObject.getString("URL").isEmpty()) {
                sessionManager.setTermsAndCondition(jsonObject.getString("URL"));
            } else {
                sessionManager.setTermsAndCondition("http://allintheloop.com/terms.html");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (jsonObject.has("data")) {
            try {
                JSONObject jsonData = jsonObject.getJSONObject("data");
                if (jsonData.length() != 0) {
                    str_logopath = MyUrls.loginImgurl + jsonData.getString("login_screen_image");
                    str_contentText = jsonData.getString("login_screen_text");

                    if (jsonData.getString("login_screen_image").length() > 0) {
                        img_logo.setVisibility(View.VISIBLE);
                        img_extrainfo.setVisibility(View.VISIBLE);
                        Glide.with(LoginActivity.this)
                                .load(str_logopath)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        e.printStackTrace();
                                        img_extrainfo.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        img_extrainfo.setVisibility(View.VISIBLE);
                                        return false;
                                    }
                                })
                                .into(img_extrainfo);

                        Glide.with(LoginActivity.this)
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
                        img_extrainfo.setVisibility(View.GONE);
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

    private void updateGCM() {
        if (GlobalData.checkForUIDVersion())
            new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST, MyUrls.update_gcmUid, Param.update_Notification(sessionManager.getEventId(), sessionManager.getGcm_id(), sessionManager.getUserId(), sessionManager.getToken(), "android"), 2, false, this);
        else
            new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST, MyUrls.update_gcm, Param.update_Notification(sessionManager.getEventId(), sessionManager.getGcm_id(), sessionManager.getUserId(), sessionManager.getToken(), "android"), 2, false, this);
    }

    @Override
    public void onBackPressed() {

        Log.d("LOGIN ACTIVITY", "" + formStatus);
        finish();
    }

    private class MyWebChromeclient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {

            WebView newWebView = new WebView(LoginActivity.this);
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

}
