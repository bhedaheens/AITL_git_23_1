package com.allintheloop.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import com.allintheloop.Bean.CountryList;
import com.allintheloop.Bean.DefaultLanguage;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements VolleyInterface {


    public static String status;
    TextView spnCountry;
    BoldTextView checkBox_terms;
    BoldTextView Error;
    Button btn_submitExtraInfo;
    BoldTextView btnNext, btnSubmit;
    BoldTextView backLogin;
    EditText EdtEmail, EdtFName, EdtLName, EdtPass, EdtRePass, EdtCName, EdtTitle, EdtAddiFirst, EdtAddiDescripation, EdtAddiNumber;
    String EventId, FieldTitle, FieldType, filedMulti;
    String c_status;
    String strEmail, strFname, strLname, strpss, strCpass, strcmpName, strTitle, item, StrRadio, StrSpinner, StrJsonObj;
    CheckBox chkSelect;
    String Countryid = "";
    String checkvalidatation = "^(?=.*[a-z])(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{6,}";
    String linkedIn_firstName, linkedIn_lastName, linkedIn_img, linkedIn_email, linkedIn_title = "", linkedIn_company_name;
//    SharedPreferences preferences;
//    SharedPreferences.Editor editor;

    SessionManager sessionManager;

    LinearLayout linearLayout;
    ArrayList<CountryList> countryArray;
    List<String> countryArrayList;
    String StrAdditFirsName, StrAddiDesc, StrAddiNumber, StrAddiRadio;
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

    DefaultLanguage.DefaultLang defaultLang;
    ArrayList<String> ansRadioGrp;
    ArrayList<String> ansCheckBoxData;

    int[] colorIntArray;
    ColorStateList colorStateList;
    ImageView btnClose;
    String facebook_status = "";
    String formStatus = "0";
    boolean isvalid = true;
    ImageView btnroundfacebook, btnLoginWithLinkedIn;
    LoginButton btnFacebook;
    LinearLayout fb_layout, linkedIn_layout, login_Layout, static_fieldLayout, linear_extraInfo, linear_addextrainfo;
    String FbEmailId = "", Fb_name = "", FbId, Eventid = "", Fb_img;
    String android_id;
    ImageView img_logo;
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

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
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

    private void FbLogin() {
        if (!FbEmailId.equalsIgnoreCase("")) {
            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest(RegisterActivity.this, VolleyRequest.Method.POST, MyUrls.FbLoginUid, Param.FacebookLogin(FbEmailId, FbId, Fb_name, Eventid, Fb_img, "android", sessionManager.getVersionCodeId()), 3, true, this);
            } else {
                new VolleyRequest(RegisterActivity.this, VolleyRequest.Method.POST, MyUrls.FbLogin, Param.FacebookLogin(FbEmailId, FbId, Fb_name, Eventid, Fb_img, "android", sessionManager.getVersionCodeId()), 3, true, this);
            }
        } else {
            login_Layout.setVisibility(View.VISIBLE);
            fb_layout.setVisibility(View.GONE);
            loginManager.getInstance().logOut();
//            ToastC.show(getApplicationContext(), "Email not available");
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.emailNot), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
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
        setContentView(R.layout.activity_register);

        sessionManager = new SessionManager(getApplicationContext());
        defaultLang = sessionManager.getMultiLangString();
        Eventid = sessionManager.getEventId();
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        EdtEmail = (EditText) findViewById(R.id.EdtRegEmail);
        EdtFName = (EditText) findViewById(R.id.EdtRegFirstName);
        EdtLName = (EditText) findViewById(R.id.EdtRegLastName);
        EdtPass = (EditText) findViewById(R.id.EdtRegPassword);
        EdtRePass = (EditText) findViewById(R.id.EdtRegCPassword);
        EdtCName = (EditText) findViewById(R.id.EdtRegCompanyName);
        EdtTitle = (EditText) findViewById(R.id.EdtRegTitle);
        chkSelect = (CheckBox) findViewById(R.id.chkAgree);
        spnCountry = (TextView) findViewById(R.id.SpnCountry);
        btnClose = (ImageView) findViewById(R.id.btnClose);
        checkBox_terms = findViewById(R.id.checkBox_terms);
        Error = findViewById(R.id.Error);
        backLogin = findViewById(R.id.BacktoLogin);
        btnNext = findViewById(R.id.btnRegNext);
        btn_submitExtraInfo = (Button) findViewById(R.id.btn_submitExtraInfo);
        btnSubmit = findViewById(R.id.btnRegSubmit);
        linearLayout = (LinearLayout) findViewById(R.id.RegDynamic);
        fb_layout = (LinearLayout) findViewById(R.id.fb_layout);
        login_Layout = (LinearLayout) findViewById(R.id.login_Layout);
        linkedIn_layout = (LinearLayout) findViewById(R.id.linkedIn_layout);
        static_fieldLayout = (LinearLayout) findViewById(R.id.static_fieldLayout);
        linear_extraInfo = (LinearLayout) findViewById(R.id.linear_extraInfo);
        linear_addextrainfo = (LinearLayout) findViewById(R.id.linear_addextrainfo);

        btnFacebook = (LoginButton) findViewById(R.id.btnface_button);
        btnroundfacebook = (ImageView) findViewById(R.id.btnLoginWithFace);
        btnLoginWithLinkedIn = (ImageView) findViewById(R.id.btnLoginWithLinkedIn);
        img_logo = findViewById(R.id.img_logo);

        EdtEmail.setHint(defaultLang.getSignUpProcessEmail());
        EdtFName.setHint(defaultLang.getSignUpProcessFirstName());
        EdtLName.setHint(defaultLang.getSignUpProcessLastName());
        EdtPass.setHint(defaultLang.getSignUpProcessPassword());
        EdtRePass.setHint(defaultLang.getSignUpProcessPasswordAgain());
        spnCountry.setHint(defaultLang.getSignUpProcessSelectCountry());
        EdtCName.setHint(defaultLang.getSignUpProcessCompanyName());
        EdtTitle.setHint(defaultLang.getSignUpProcessTitle());
        btnSubmit.setText(defaultLang.getSignUpProcessSubmit());
        btnNext.setText(defaultLang.getSignUpProcessNext());

//        Spannable wordtoSpan = new SpannableString(defaultLang.getSignUpProcessIAgreeToTheTermsConditions());
//        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.btn_blue)),14,wordtoSpan.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        chkSelect.setButtonTintList(colorStateList);
        checkBox_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sessionManager.getTermsAndCondition()));
                startActivity(browserIntent);
            }
        });
//
//        preferences = getSharedPreferences(GlobalData.USER_PREFRENCE, MODE_PRIVATE);
//        editor=preferences.edit();

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


        facebook_status = sessionManager.getFacebookStatus();
        if (facebook_status.equalsIgnoreCase("1")) {
            if (sessionManager.getEventType().equalsIgnoreCase("4")) {

                btnroundfacebook.setVisibility(View.GONE);

            } else {

                btnroundfacebook.setVisibility(View.VISIBLE);
            }
        } else {

            btnroundfacebook.setVisibility(View.GONE);
        }

        if (sessionManager.getLinkedInStatus().equalsIgnoreCase("1")) {
            if (sessionManager.getEventType().equalsIgnoreCase("4")) {

                btnLoginWithLinkedIn.setVisibility(View.GONE);
            } else {

                btnLoginWithLinkedIn.setVisibility(View.VISIBLE);
            }
        } else {

            btnLoginWithLinkedIn.setVisibility(View.GONE);
        }

        btnFacebook.setReadPermissions(Arrays.asList("public_profile, email"));
        btnFacebook.registerCallback(callbackManager, callback);
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

        EventId = sessionManager.getEventId();   // getId From SessionManager
        Log.d("Regsiter_ListEventID", EventId);

        arrayAddiChkList = new ArrayList<>();
        countryArray = new ArrayList<>();
        countryArrayList = new ArrayList<String>();
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
        ansRadioGrp = new ArrayList<>();
        ansCheckBoxData = new ArrayList<>();

//        chkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (chkSelect.isChecked()) {
//
//                }
//            }
//        });

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(EdtEmail.getWindowToken(), 0);
                finish();
            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(EdtEmail.getWindowToken(), 0);
                finish();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);
            }
        });

        spnCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        CoutryList_Activity.class);

                c_status = "country";
                i.putExtra("status", c_status);
                startActivityForResult(i, 200);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                strEmail = EdtEmail.getText().toString();
                strFname = EdtFName.getText().toString();
                strLname = EdtLName.getText().toString();
                strpss = EdtPass.getText().toString();
                strCpass = EdtRePass.getText().toString();
                strcmpName = EdtCName.getText().toString();
                strTitle = EdtTitle.getText().toString();


                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(EdtEmail.getWindowToken(), 0);

                try {

                    JSONObject jsonObject = new JSONObject();
                    for (int f = 0; f < SinglelineArray.size(); f++) {
                        if (SinglelineArray.get(f).getText().toString().length() <= 0) {
                            EdtAddiFirst.setError(getResources().getString(R.string.validValue));
                        } else {
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


                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (strEmail.trim().length() <= 0) {
                    EdtEmail.setError(getResources().getString(R.string.emailvalid));
                    EdtEmail.requestFocus();
                } else if (!GlobalData.checkEmailValid(EdtEmail.getText().toString())) {
                    EdtEmail.setError(getResources().getString(R.string.mailValid));
                    EdtEmail.requestFocus();
                } else if (strFname.trim().length() <= 0) {
                    EdtFName.setError(getResources().getString(R.string.firstNameValid));
                    EdtFName.requestFocus();
                } else if (strLname.trim().length() <= 0) {
                    EdtLName.setError(getResources().getString(R.string.lastNameValid));
                    EdtLName.requestFocus();
                } else if (strpss.trim().length() == 0 || strpss.trim().length() < 6) {
                    // ToastC.show(RegisterActivity.this, "Please Enter Password");
                    EdtPass.setError(getResources().getString(R.string.passwordValid));
                    EdtPass.requestFocus();
                } else if (strCpass.trim().length() <= 0) {
                    //ToastC.show(RegisterActivity.this, "Please Enter Confirm Password");
                    EdtRePass.setError(getResources().getString(R.string.CpasswordValid));
                    EdtRePass.requestFocus();
                } else if (!strpss.trim().equals(strCpass)) {
                    EdtRePass.setError(getResources().getString(R.string.CCorrectPasswordValid));
                    EdtRePass.requestFocus();
                } else if (!chkSelect.isChecked()) {
                    ToastC.show(RegisterActivity.this, getResources().getString(R.string.TandO));
                } else {
                    RegisterApi();
                }
            }
        });
        if (GlobalData.isNetworkAvailable(RegisterActivity.this)) {
            new VolleyRequest(RegisterActivity.this, VolleyRequest.Method.POST, MyUrls.GET_FORM_DATA, Param.GetFormData(EventId), 0, false, RegisterActivity.this);
        }
        getLoginLogo();
    }

    private void getLoginLogo() {
        if (GlobalData.isNetworkAvailable(this)) {
            new VolleyRequest(this, VolleyRequest.Method.POST, MyUrls.getloginScreenLogo, Param.getLoginLogo(sessionManager.getEventId()), 6, false, this);
        }
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

    private void submitFormData() {
        new VolleyRequest(RegisterActivity.this, VolleyRequest.Method.POST, MyUrls.submitFormBuilderData, Param.submitFormData(StrJsonObj, sessionManager.getUserId()), 5, true, this);
    }

    public void getUserData() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(RegisterActivity.this, "https://api.linkedin.com/v1/people/~:" +
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

    private void loginWithLinkedIn() {
        if (GlobalData.checkForUIDVersion()) {
            new VolleyRequest(RegisterActivity.this, VolleyRequest.Method.POST, MyUrls.linedInLoginUid, Param.loginWithLinkedIn(sessionManager.getEventId(), linkedIn_email, linkedIn_firstName, linkedIn_lastName, linkedIn_img, "android", linkedIn_title, linkedIn_company_name, sessionManager.getVersionCodeId()), 3, true, this);

        } else {
            new VolleyRequest(RegisterActivity.this, VolleyRequest.Method.POST, MyUrls.linedInLogin, Param.loginWithLinkedIn(sessionManager.getEventId(), linkedIn_email, linkedIn_firstName, linkedIn_lastName, linkedIn_img, "android", linkedIn_title, linkedIn_company_name, sessionManager.getVersionCodeId()), 3, true, this);
        }
    }

    private void RegisterApi() {
        if (GlobalData.isNetworkAvailable(RegisterActivity.this)) {
            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest(RegisterActivity.this, VolleyRequest.Method.POST, MyUrls.Register_dataUid, Param.RegisterForm(strEmail, strFname, strLname, strpss, EventId, Countryid, strTitle, strcmpName, StrJsonObj, status, "android"), 2, true, RegisterActivity.this);
            } else {
                new VolleyRequest(RegisterActivity.this, VolleyRequest.Method.POST, MyUrls.Register_data, Param.RegisterForm(strEmail, strFname, strLname, strpss, EventId, Countryid, strTitle, strcmpName, StrJsonObj, status, "android"), 2, true, RegisterActivity.this);
            }
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject mainObj = new JSONObject(volleyResponse.output);
                    if (mainObj.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        new VolleyRequest(RegisterActivity.this, VolleyRequest.Method.POST, MyUrls.Get_CountryList, Param.GetCountryList(), 1, true, RegisterActivity.this);
                        Error.setText(defaultLang.getSignUpProcessCreateYourAccountToGainAccessTo() + " \n" + mainObj.getString("event_name"));
                        Log.d("RegisterResponse", mainObj.toString());
                        // JSONObject jsonData=mainObj.optJSONObject("data");
                        status = mainObj.getString("status");

                        if (status.equalsIgnoreCase("0")) {
//                            ToastC.show(RegisterActivity.this, mainObj.optString(Param.KEY_MESSAGE));
                            btnSubmit.setVisibility(View.VISIBLE);

                        } else {
                            ansCheckBoxData.clear();
                            ansRadioGrp.clear();
                            //ToastC.show(RegisterActivity.this, mainObj.optJSONObject("data").toString());
                            btnNext.setVisibility(View.VISIBLE);

                            JSONObject jsonData = mainObj.optJSONObject("data");
                            JSONArray jsonArrayField = jsonData.optJSONArray("fields");
                            for (int i = 0; i < jsonArrayField.length(); i++) {
                                JSONObject jsonObjField = (JSONObject) jsonArrayField.get(i);
                                FieldTitle = jsonObjField.optString("title");
                                FieldType = jsonObjField.optString("type");
                                filedMulti = jsonObjField.optString("lang_title");

                                if (FieldType.equalsIgnoreCase("element-single-line-text")) {
                                    EdtAddiFirst = new EditText(RegisterActivity.this);
                                    if (filedMulti.equalsIgnoreCase("")) {
                                        EdtAddiFirst.setHint(FieldTitle);
                                    } else {
                                        EdtAddiFirst.setHint(filedMulti);
                                    }
                                    EdtAddiFirst.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                    EdtAddiFirst.setTextSize(15);
                                    EdtAddiFirst.setPadding(20, 25, 0, 25);
                                    EdtAddiFirst.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(0, 0, 0, 10);
                                    EdtAddiFirst.setLayoutParams(params);
                                    linearLayout.addView(EdtAddiFirst);

                                    SinglelineArray.add(EdtAddiFirst);
                                    StrSinglelineArray.add(FieldTitle);

                                } else if (FieldType.equalsIgnoreCase("element-paragraph-text")) {
                                    EdtAddiDescripation = new EditText(RegisterActivity.this);
                                    if (filedMulti.equalsIgnoreCase("")) {
                                        EdtAddiDescripation.setHint(FieldTitle);
                                    } else {
                                        EdtAddiDescripation.setHint(filedMulti);
                                    }
                                    EdtAddiDescripation.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                    EdtAddiDescripation.setTextSize(15);
                                    EdtAddiDescripation.setPadding(20, 25, 0, 25);
                                    EdtAddiDescripation.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                    EdtAddiDescripation.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(0, 0, 0, 15);
                                    EdtAddiDescripation.setLayoutParams(params);
                                    linearLayout.addView(EdtAddiDescripation);
                                    MultilineArray.add(EdtAddiDescripation);
                                    StrMultilineArray.add(FieldTitle);
                                } else if (FieldType.equalsIgnoreCase("element-multiple-choice")) {
                                    TextView txtchk = new TextView(RegisterActivity.this);
                                    if (filedMulti.equalsIgnoreCase("")) {
                                        txtchk.setText(FieldTitle);
                                    } else {
                                        txtchk.setText(filedMulti);
                                    }
                                    txtchk.setTextSize(15);
                                    txtchk.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                    txtchk.setTypeface(null, Typeface.BOLD);
                                    txtchk.setPadding(20, 25, 0, 25);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    txtchk.setLayoutParams(params);
                                    linearLayout.addView(txtchk);

                                    JSONArray jArrayChkMultiple = jsonObjField.optJSONArray("choices");
                                    StrchkboxArray.add(FieldTitle);
                                    for (int chk = 0; chk < jArrayChkMultiple.length(); chk++) {
                                        JSONObject jObjectChk = (JSONObject) jArrayChkMultiple.get(chk);
                                        final CheckBox chkBox = new CheckBox(RegisterActivity.this);
                                        ansCheckBoxData.add(jObjectChk.optString("title"));
                                        if (jObjectChk.optString("lang_title").equalsIgnoreCase("")) {
                                            chkBox.setText(jObjectChk.optString("title"));
                                        } else {
                                            chkBox.setText(jObjectChk.optString("lang_title"));
                                        }

                                        chkBox.setPadding(10, 15, 0, 15);
                                        chkBox.setTextSize(15);
                                        chkBox.setId(chk);
                                        if (chkBox.getId() == 0) {
                                            chkBox.setChecked(true);
                                            if (ansCheckBoxData.size() != 0)
                                                arrayAddiChkList.add(ansCheckBoxData.get(0).toString());
                                        }

                                        chkBox.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            chkBox.setButtonTintList(colorStateList);
                                        }
                                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        params1.leftMargin = 20;
                                        chkBox.setLayoutParams(params1);
                                        linearLayout.addView(chkBox);
                                        chkboxArray.add(chkBox);
                                        chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                if (isChecked) {
                                                    Log.d("chceckBox CheckId ADD", "" + chkBox.getText().toString());

                                                    for (int i = 0; i < ansCheckBoxData.size(); i++) {
                                                        if (chkBox.getId() == i) {
                                                            Log.d("chceckBox Index", ansCheckBoxData.get(i).toString());
                                                            arrayAddiChkList.add(ansCheckBoxData.get(i).toString());
                                                            chkboxArray.add(chkBox);
                                                        }
                                                    }

                                                } else {
                                                    Log.d("CheckBoc CheckId Remove", "" + chkBox.getText().toString());
                                                    for (int i = 0; i < ansCheckBoxData.size(); i++) {
                                                        if (chkBox.getId() == i) {
                                                            Log.d("chceckBox Index", ansCheckBoxData.get(i).toString());
                                                            arrayAddiChkList.remove(ansCheckBoxData.get(i).toString());
                                                            chkboxArray.add(chkBox);
                                                        }
                                                    }
                                                }
                                            }
                                        });

                                    }

                                } else if (FieldType.equalsIgnoreCase("element-section-break")) {
                                    View line = new View(RegisterActivity.this);
                                    line.setBackgroundColor(getResources().getColor(R.color.darkGrayColor));
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
                                    line.setLayoutParams(params);
                                    linearLayout.addView(line);
                                } else if (FieldType.equalsIgnoreCase("element-number")) {
                                    EdtAddiNumber = new EditText(RegisterActivity.this);
                                    if (filedMulti.equalsIgnoreCase("")) {
                                        EdtAddiNumber.setHint(FieldTitle);
                                    } else {
                                        EdtAddiNumber.setHint(filedMulti);
                                    }
                                    EdtAddiNumber.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                    EdtAddiNumber.setTextSize(15);
                                    EdtAddiNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    EdtAddiNumber.setPadding(20, 25, 0, 25);
                                    EdtAddiNumber.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    params.topMargin = 15;
                                    EdtAddiNumber.setLayoutParams(params);
                                    linearLayout.addView(EdtAddiNumber);
                                    StrNumberArray.add(FieldTitle);
                                    NumberArray.add(EdtAddiNumber);

                                } else if (FieldType.equalsIgnoreCase("element-checkboxes")) {
                                    TextView txtRdo = new TextView(RegisterActivity.this);
                                    if (filedMulti.equalsIgnoreCase("")) {
                                        txtRdo.setText(FieldTitle);
                                    } else {
                                        txtRdo.setText(filedMulti);
                                    }
                                    txtRdo.setTextSize(15);
                                    txtRdo.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                    txtRdo.setTypeface(null, Typeface.BOLD);
                                    txtRdo.setPadding(10, 15, 0, 15);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    txtRdo.setLayoutParams(params);
                                    linearLayout.addView(txtRdo);
                                    StrRadioGroupArray.add(FieldTitle);
                                    JSONArray jArrayRdo = jsonObjField.optJSONArray("choices");
                                    Log.d("ArraySize", "" + jArrayRdo.length());
                                    final RadioGroup radioGroup = new RadioGroup(RegisterActivity.this);

                                    for (int j = 0; j < jArrayRdo.length(); j++) {
                                        JSONObject jObjectRdo = (JSONObject) jArrayRdo.get(j);
                                        Log.d("Count", " " + j);

                                        RadioButton radioButton = new RadioButton(RegisterActivity.this);
                                        ansRadioGrp.add(jObjectRdo.optString("title"));
                                        if (jObjectRdo.optString("lang_title").equalsIgnoreCase("")) {
                                            radioButton.setText(jObjectRdo.optString("title"));
                                        } else {
                                            radioButton.setText(jObjectRdo.optString("lang_title"));
                                        }
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
                                                Log.d("Radio checkedId", "" + checkedId);
                                                for (int i = 1; i <= ansRadioGrp.size(); i++) {
                                                    if (checkedId == i) {
                                                        Log.d("Radio Index", ansRadioGrp.get(i - 1).toString());
                                                        StrRadio = ansRadioGrp.get(i - 1).toString();
                                                        Log.d("Radio button", StrRadio);
                                                    }
                                                }

                                            }
                                        });

                                    }
                                    linearLayout.addView(radioGroup);
                                    RadioGroupArray.add(radioGroup);

                                } else if (FieldType.equalsIgnoreCase("element-dropdown")) {
                                    TextView txtSpinner = new TextView(RegisterActivity.this);
                                    if (filedMulti.equalsIgnoreCase("")) {
                                        txtSpinner.setText(FieldTitle);
                                    } else {
                                        txtSpinner.setText(filedMulti);
                                    }
                                    txtSpinner.setTextSize(15);
                                    txtSpinner.setTypeface(null, Typeface.BOLD);
                                    txtSpinner.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                    txtSpinner.setPadding(10, 15, 0, 15);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    txtSpinner.setLayoutParams(params);
                                    linearLayout.addView(txtSpinner);

                                    JSONArray jArraySpinner = jsonObjField.optJSONArray("choices");
                                    final Spinner spinner = new Spinner(RegisterActivity.this);

                                    ArrayList<String> arrayList = new ArrayList<>();
                                    for (int k = 0; k < jArraySpinner.length(); k++) {
                                        JSONObject jObjectSpinner = (JSONObject) jArraySpinner.get(k);
                                        if (jObjectSpinner.optString("lang_title").equalsIgnoreCase("")) {
                                            arrayList.add(jObjectSpinner.optString("title"));
                                        } else {
                                            arrayList.add(jObjectSpinner.optString("lang_title"));
                                        }

                                    }
                                    ArrayAdapter<String> adp = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_list_item_1, arrayList);
                                    spinner.setAdapter(adp);
//                                    spinner.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                                    spinner.setLayoutParams(params);
                                    linearLayout.addView(spinner);
                                    SpinnerArray.add(spinner);
                                    spinner.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_white_profile));
                                    StrSpinnerArray.add(FieldTitle);
                                    StrSpinner = spinner.getItemAtPosition(0).toString();
                                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            StrSpinner = spinner.getSelectedItem().toString();
                                            ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.black));
//                                            ((TextView) parent.getChildAt(0)).setBackground(getResources().getDrawable(R.drawable.edittext_rounded_white_profile));

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject resObj = new JSONObject(volleyResponse.output);
                    Log.d("ResponseData", resObj.toString());

                    if (resObj.optString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonData = resObj.optJSONObject("data");
                        ToastC.show(getApplicationContext(), getResources().getString(R.string.registrationSuccess));
                        finish();
                    } else if (resObj.optString("success").equalsIgnoreCase("false")) {
                        ToastC.show(getApplicationContext(), resObj.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3://Fb Login
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL FBLOFINLogin", jsonObject.toString());
                    if (jsonObject.optString("success").equalsIgnoreCase("true")) {
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

//                            if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
//                                saveDeviceId();
//                            }
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
                                    EdtAddiFirst = new EditText(RegisterActivity.this);
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
                                    EdtAddiDescripation = new EditText(RegisterActivity.this);
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
                                    TextView txtchk = new TextView(RegisterActivity.this);
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
                                        final CheckBox chkBox = new CheckBox(RegisterActivity.this);
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
                                    View line = new View(RegisterActivity.this);
                                    line.setBackgroundColor(getResources().getColor(R.color.hintcolor));
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
                                    line.setLayoutParams(params);
                                    linear_addextrainfo.addView(line);
                                } else if (FieldType.equalsIgnoreCase("element-number")) {
                                    EdtAddiNumber = new EditText(RegisterActivity.this);
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
                                    TextView txtRdo = new TextView(RegisterActivity.this);
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
                                    RadioGroup radioGroup = new RadioGroup(RegisterActivity.this);

                                    for (int j = 0; j < jArrayRdo.length(); j++) {
                                        JSONObject jObjectRdo = (JSONObject) jArrayRdo.get(j);
                                        Log.d("Count", " " + j);

                                        RadioButton radioButton = new RadioButton(RegisterActivity.this);
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
                                    TextView txtSpinner = new TextView(RegisterActivity.this);
                                    txtSpinner.setText(FieldTitle);
                                    txtSpinner.setTextSize(15);
                                    txtSpinner.setTextColor(getResources().getColor(R.color.darkGrayColor));
                                    txtSpinner.setTypeface(null, Typeface.BOLD);
                                    txtSpinner.setPadding(10, 15, 0, 15);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                    txtSpinner.setLayoutParams(params);
                                    linear_addextrainfo.addView(txtSpinner);

                                    JSONArray jArraySpinner = jsonObjField.optJSONArray("choices");
                                    final Spinner spinner = new Spinner(RegisterActivity.this);

                                    ArrayList<String> arrayList = new ArrayList<>();
                                    for (int k = 0; k < jArraySpinner.length(); k++) {
                                        JSONObject jObjectSpinner = (JSONObject) jArraySpinner.get(k);

                                        arrayList.add(jObjectSpinner.optString("title"));

                                    }
                                    ArrayAdapter<String> adp = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_list_item_1, arrayList);
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
//                            if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
//                                saveDeviceId();
//                            }
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        login_Layout.setVisibility(View.VISIBLE);
                        fb_layout.setVisibility(View.GONE);
                        linkedIn_layout.setVisibility(View.GONE);
                        ToastC.show(RegisterActivity.this, jsonObject.optString(Param.KEY_MESSAGE));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL Login", jsonObject.toString());

                    Log.d("AITL UPDATEGCM", "UPDATEGCM");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL  SUBMITDATA", jsonObject.toString());

                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
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

    @Override
    public void onBackPressed() {
        finish();
    }


    private void updateGCM() {
//        new VolleyRequest(LoginActivity.this, VolleyRequest.Method.POST, MyUrls.update_gcm, Param.update_Notification(sessionManager.getEventId(), FirebaseInstanceId.getInstance().getToken(), sessionManager.getUserId(), sessionManager.getToken(), "android"), 2, false, this);
        if (GlobalData.checkForUIDVersion())
            new VolleyRequest(RegisterActivity.this, VolleyRequest.Method.POST, MyUrls.update_gcmUid, Param.update_Notification(sessionManager.getEventId(), sessionManager.getGcm_id(), sessionManager.getUserId(), sessionManager.getToken(), "android"), 4, false, this);
        else
            new VolleyRequest(RegisterActivity.this, VolleyRequest.Method.POST, MyUrls.update_gcm, Param.update_Notification(sessionManager.getEventId(), sessionManager.getGcm_id(), sessionManager.getUserId(), sessionManager.getToken(), "android"), 4, false, this);
    }

    private void saveDeviceId() {
        if (GlobalData.isNetworkAvailable(RegisterActivity.this)) {
            new VolleyRequest(RegisterActivity.this, VolleyRequest.Method.POST, MyUrls.OpenApp, Param.saveDeviceId(sessionManager.getEventId(), android_id), 5, false, this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            if (data.getStringExtra("status").equalsIgnoreCase("country")) {
                spnCountry.setText(data.getStringExtra("name"));
                Countryid = data.getStringExtra("code");

                Log.d("AITL", spnCountry.getText().toString() + Countryid);
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
        }
    }

}
