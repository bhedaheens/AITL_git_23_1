package com.allintheloop.Fragment.EditProfileModule;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Activity.SearchApp_Activity;
import com.allintheloop.Bean.Attendee.AttendeeFilterList;
import com.allintheloop.Bean.editProfileAbout.EditProfileAboutYouGeneralClass;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.allintheloop.MainActivity.callbackManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class Attendee_ProfileEdit_Fragment extends DialogFragment implements VolleyInterface {

    public static final String OAUTH_CALLBACK_HOST = "litestcalback";
    private static final int RESULT_LOAD_IMAGE = 5;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    EditText txt_firstname, txt_surName, txt_company, txt_title, txt_email, txt_password, txt_repassword;
    LinearLayout linear_reTypePassword, linear_additional;
    //    Button btn_edit,btn_clearData,btn_done;
    //    Button btn_edit,btn_clearData,btn_done;
    SessionManager sessionManager;
    LoginButton btnFacebook;
    String FbEmailId = "", Fb_name, FbId, Eventid, Fb_img, facebook_status;
    ImageView btnLoginWithFace, img_linkedin;
    CircleImageView profile_image;
    String str_firstname = "", str_lastname = "", str_title = "", str_company = "", str_email = "", str_salutation = "";
    String str_password, str_rePassword;
    String picturePath = "";
    Bitmap bitmapImage = null;
    ProgressBar progressBar1;
    ContentValues values;
    Uri imageUri;
    Bitmap thumbnail;
    String FieldTitle, FieldType, FieldValue;

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
    EditText EdtAddiFirst, EdtAddiDescripation, EdtAddiNumber;
    boolean isvalid = false;
    Dialog dialog;
    //    ImageView dailog_profile_close;
    String isFaceook = "", isLinkedin = "";
    Context context;

    ProgressDialog progressDialog;
    List<String> permissionsNeeded;
    List<String> permissionsList;
    TextView txt_profile_edit;
    BoldTextView btn_save, buttonCancel;
    LinearLayout linear_password, linear_hideProfile;
    SwitchCompat swith_identifyProfile;
    String str_hideIdentity = "", str_enable_hide_identity = "";
    boolean isAdditionalData = false;
    View view_breakLine;
    private LoginManager loginManager;

    List<AttendeeFilterList.Data> mainKeywordList;
    List<AttendeeFilterList.Data> selectedkeywordList;
    ArrayList<EditProfileAboutYouGeneralClass> generalClassArrayList;

    BoldTextView gola_edit, about_edit, txt_goals, txt_about;
    RelativeLayout relative_goals, relative_about;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();

            GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    getFacebookData(object);
                    getProfileData();
                    getFormData();
                    if (isvalid)
                        updateSocial();
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location"); // Parámetros que pedimos a facebook
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Log.i("Cancel", "Cancel");
            //    login_Layout.setVisibility(View.VISIBLE);
            //    fb_layout.setVisibility(View.GONE);
        }

        @Override
        public void onError(FacebookException e) {
            e.printStackTrace();
            //  login_Layout.setVisibility(View.VISIBLE);
            // fb_layout.setVisibility(View.GONE);
        }
    };


    public Attendee_ProfileEdit_Fragment() {

        // Required empty public constructor
    }

    // This method is used to make permissions to retrieve data from linkedin
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_layout);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return dialog;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.attendee_profile_new, container, false);

        context = getActivity();

        txt_firstname = (EditText) rootView.findViewById(R.id.txt_firstname);
        txt_surName = (EditText) rootView.findViewById(R.id.txt_surName);
        txt_company = (EditText) rootView.findViewById(R.id.txt_company);
        txt_title = (EditText) rootView.findViewById(R.id.txt_title);
//        btn_edit = (Button) rootView.findViewById(R.id.btn_edit);
        btn_save = rootView.findViewById(R.id.btn_save);
        txt_goals = (BoldTextView) rootView.findViewById(R.id.txt_goals);
        about_edit = (BoldTextView) rootView.findViewById(R.id.about_edit);
        gola_edit = (BoldTextView) rootView.findViewById(R.id.gola_edit);
        txt_about = (BoldTextView) rootView.findViewById(R.id.txt_about);
        buttonCancel = rootView.findViewById(R.id.buttonCancel);
        txt_profile_edit = (TextView) rootView.findViewById(R.id.txt_profile_edit);
        txt_password = (EditText) rootView.findViewById(R.id.txt_password);
        img_linkedin = (ImageView) rootView.findViewById(R.id.img_linkedin);
        btnFacebook = (LoginButton) rootView.findViewById(R.id.btnface_button);
        btnFacebook.setFragment(this);
        btnLoginWithFace = (ImageView) rootView.findViewById(R.id.btnLoginWithFace);

        txt_email = (EditText) rootView.findViewById(R.id.txt_email);
        txt_repassword = (EditText) rootView.findViewById(R.id.txt_repassword);
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        profile_image = (CircleImageView) rootView.findViewById(R.id.profile_image);
        sessionManager = new SessionManager(context);
        linear_reTypePassword = (LinearLayout) rootView.findViewById(R.id.linear_reTypePassword);
        linear_additional = (LinearLayout) rootView.findViewById(R.id.linear_additional);
        linear_password = (LinearLayout) rootView.findViewById(R.id.linear_password);
        linear_hideProfile = (LinearLayout) rootView.findViewById(R.id.linear_hideProfile);
        view_breakLine = (View) rootView.findViewById(R.id.view_breakLine);
        swith_identifyProfile = (SwitchCompat) rootView.findViewById(R.id.swith_identifyProfile);
        relative_goals = rootView.findViewById(R.id.relative_goals);
        relative_about = rootView.findViewById(R.id.relative_about);


        linear_additional.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Eventid = sessionManager.getEventId();
        btnFacebook.setReadPermissions(Arrays.asList("public_profile, email"));
        btnFacebook.registerCallback(callbackManager, callback);
        loginManager = LoginManager.getInstance();

        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
        } else {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
        }

        Log.d("AITL HASHKEY", "" + GlobalData.printKeyHash(getActivity()));
        img_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GlobalData.isNetworkAvailable(getActivity())) {
                    LISessionManager.getInstance(getActivity()).clearSession();
                    login_linkedin();
                } else {
                    ToastC.show(getActivity(), getString(R.string.noInernet));
                }
            }
        });

        gola_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), ProfileEditGolas_Activity.class), 2000);
            }
        });
        about_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(),
                        ProfileEditAboutYou_activity.class)
                        .putExtra("keywordData", (Serializable) mainKeywordList)
                        .putExtra("selectedKeywordData", (Serializable) selectedkeywordList), 3000);
            }
        });

        btnLoginWithFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginManager.logOut();
                btnFacebook.performClick();
            }
        });


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

        mainKeywordList = new ArrayList<>();
        selectedkeywordList = new ArrayList<>();

        setProfilePicAndSocial();
        linear_hideProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    if (str_hideIdentity.equalsIgnoreCase("0")) {
                        hideIdentityDailog();
                    } else if (str_hideIdentity.equalsIgnoreCase("1")) {
                        swith_identifyProfile.setChecked(false);
                        str_hideIdentity = "0";
                        apiHideAttendeeIdentity();
                    }
                } else {
                    ToastC.show(getActivity(), getString(R.string.noInernet));
                }
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getFormData();
                getProfileData();
                if (isvalid) {
                    updateProfile();
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (isCameraPermissionGranted()) {
                        loadCamera();
                    } else {
                        requestPermission();
                    }
                } else {
                    loadCamera();
                }
            }
        });
        getAdditionalData();
        getAttendeeFilerdata();
        getSelectedKeywordData();
        return rootView;
    }


    private void hideIdentityDailog() {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("WARNING")
                .content("By hiding your identity you will hide yourself from any attendee directories. You will no longer be able to contact other users on the platform or be contacted on the platform")
                .negativeText("Cancel")
                .positiveText("Continue")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        swith_identifyProfile.setChecked(true);
                        str_hideIdentity = "1";
                        apiHideAttendeeIdentity();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        swith_identifyProfile.setChecked(false);
                        str_hideIdentity = "0";
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void loadCamera() {
        String[] item = {"Gallery", "Camera"};
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Choose Image From")
                .items(item)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (which == 0) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            intent.putExtra("crop", "true");
                            intent.putExtra("aspectX", 1);
                            intent.putExtra("aspectY", 1);
                            intent.putExtra("outputX", 200);
                            intent.putExtra("outputY", 200);

                            try {

                                intent.putExtra("return-data", true);
                                startActivityForResult(Intent.createChooser(intent,
                                        "Complete action using"), RESULT_LOAD_IMAGE);

                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }

                        } else if (which == 1) {

                            values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "New Picture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                            imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 1);

                        }
                    }

                })
                .build();
        dialog.show();

    }

    private void getProfileData() {

        str_company = txt_company.getText().toString();
        str_firstname = txt_firstname.getText().toString();
        str_lastname = txt_surName.getText().toString();
        str_salutation = "";
        str_title = txt_title.getText().toString();
        str_password = txt_password.getText().toString();
        str_rePassword = txt_repassword.getText().toString();


        if (str_firstname.trim().length() == 0) {
            txt_firstname.setError("Please Enter First Name");
            isvalid = false;
        } else if (str_lastname.trim().length() == 0) {
            txt_surName.setError("Please Enter SurName");
            isvalid = false;
        } else if (!str_password.equalsIgnoreCase(str_rePassword)) {
            txt_repassword.setError("please enter same password");
            isvalid = false;
        } else {
            isvalid = true;
        }
    }

//    private void deleteUser() {
//        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.deleteUser, Param.deleteUser(sessionManager.getUserId(), sessionManager.getRolId()), 5, false, this); // postponed - pending by serverside developer
//    }


    private void apiHideAttendeeIdentity() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.hideAttendeeIdentity, Param.hideAttendeeIndentity(sessionManager.getEventId(), sessionManager.getUserId(), str_hideIdentity), 6, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void setProfilePicAndSocial() {
        txt_firstname.setText(sessionManager.getFirstName());
        txt_surName.setText(sessionManager.getLastName());
        txt_company.setText(sessionManager.getCompany_name());
        txt_email.setText(sessionManager.getEmail());
        txt_title.setText(sessionManager.getTitle());
        txt_goals.setText(sessionManager.getProfileGoal());
        txt_about.setText(sessionManager.getProfileAbout());

        if (sessionManager.getHideMyIdentityStatus().equalsIgnoreCase("0")) {
            if (!(sessionManager.getImagePath().equalsIgnoreCase(""))) {
                Glide.with(getActivity())
                        .load(MyUrls.thumImgUrl + sessionManager.getImagePath())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                profile_image.setVisibility(View.VISIBLE);
//                                profile_image.setImageDrawable(getResources().getDrawable(R.drawable.profile));
//                            Glide.with(getActivity()).load(MyUrls.thumImgUrl + sessionManager.getImagePath()).centerCrop().fitCenter().placeholder(R.drawable.profile).into(profile_image);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                profile_image.setVisibility(View.VISIBLE);

                                return false;
                            }
                        }).into(profile_image);

            }
            if (isFaceook.equalsIgnoreCase("1")) {
                btnLoginWithFace.setVisibility(View.VISIBLE);
            } else {
                btnLoginWithFace.setVisibility(View.GONE);
            }

            if (isLinkedin.equalsIgnoreCase("1")) {
                img_linkedin.setVisibility(View.VISIBLE);
            } else {
                img_linkedin.setVisibility(View.GONE);
            }

            txt_firstname.setVisibility(View.VISIBLE);
            txt_surName.setVisibility(View.VISIBLE);
            txt_company.setVisibility(View.VISIBLE);
            txt_email.setVisibility(View.VISIBLE);
            txt_title.setVisibility(View.VISIBLE);
            if (sessionManager.getEventType().equalsIgnoreCase("4")) {
                linear_password.setVisibility(View.GONE);
            } else {
                linear_password.setVisibility(View.VISIBLE);
            }
            txt_profile_edit.setVisibility(View.VISIBLE);
            if (isAdditionalData) {
                linear_additional.setVisibility(View.VISIBLE);
            } else {
                linear_additional.setVisibility(View.GONE);
            }
        } else {
            progressBar1.setVisibility(View.GONE);
            profile_image.setVisibility(View.VISIBLE);
            profile_image.setImageDrawable(getResources().getDrawable(R.drawable.profile));
            btnLoginWithFace.setVisibility(View.GONE);
            img_linkedin.setVisibility(View.GONE);

            txt_firstname.setVisibility(View.INVISIBLE);
            txt_surName.setVisibility(View.INVISIBLE);
            txt_company.setVisibility(View.INVISIBLE);
            txt_email.setVisibility(View.INVISIBLE);
            txt_title.setVisibility(View.INVISIBLE);

            txt_profile_edit.setVisibility(View.GONE);
            linear_password.setVisibility(View.GONE);
            linear_additional.setVisibility(View.GONE);
        }

        if (sessionManager.getEventType().equalsIgnoreCase("4") || sessionManager.getEventType().equalsIgnoreCase("1")) {
            if (sessionManager.getFormStatus().equalsIgnoreCase("1")) {
                buttonCancel.setVisibility(View.GONE);
            } else {
                buttonCancel.setVisibility(View.VISIBLE);
            }

        } else {
            buttonCancel.setVisibility(View.VISIBLE);
        }
        btn_save.setVisibility(View.VISIBLE);
    }

    private void getFormData() {
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getAdditionalData() {

        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdditionalData, Param.getAdditionalData(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getUserId()), 3, false, this);

    }


    private void getSelectedKeywordData() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeFiterData, Param.getSelectedKeywordData(sessionManager.getEventId(), "", sessionManager.getUserId()), 8, false, this);
        }
    }

    private void getAttendeeFilerdata() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeFiterData, Param.getAttendeeFilterListData(sessionManager.getEventId(), ""), 7, false, this);
        }
    }

    private void updateProfile() {

        if (GlobalData.isNetworkAvailable(getActivity())) {

            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest(getActivity(), MyUrls.updateProfileAttendeeUid, Param.update_photo(new File(picturePath)), Param.updateProfileAttendee(str_firstname, str_lastname, sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getToken(), str_company, str_title, str_salutation, str_password, StrJsonObj, sessionManager.getUserEventId()), 2, true, this);
            } else {
                new VolleyRequest(getActivity(), MyUrls.updateProfileAttendee, Param.update_photo(new File(picturePath)), Param.updateProfileAttendee(str_firstname, str_lastname, sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getToken(), str_company, str_title, str_salutation, str_password, StrJsonObj, ""), 2, true, this);
            }
        } else {

            ToastC.show(getActivity(), "No Internet Connection");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MainActivity.callbackManager.onActivityResult(requestCode, resultCode, data);
        LISessionManager.getInstance(getActivity()).onActivityResult(getActivity(), requestCode, resultCode, data);
        Log.d("AITL", "ResultOk :-" + requestCode);
        Log.d("AITL", "ResultOk :-" + data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Log.d("AITL", "IF Condtion ");

                try {
                    if (android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
                        if (imageUri != null) {
                            progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setMessage("Please Wait...");
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    performCrop(imageUri);
                                }
                            }).start();
                        }
                    } else {
                        performCrop(imageUri);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (requestCode == RESULT_LOAD_IMAGE) {

                try {
                    Bundle extras2 = data.getExtras();
                    if (extras2 != null) {
                        Bitmap photo = extras2.getParcelable("data");
                        profile_image.setImageBitmap(photo);
                        Uri uri = getImageUri(getActivity(), photo);
                        picturePath = getRealPathFromURI(uri);

//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        // COMPESS IMAGE
//                        photo.compress(Bitmap.CompressFormat.JPEG, 70, stream);
//                        byte_arr = stream.toByteArray();
//                        picturePath = Base64.encodeToString(byte_arr, Base64.DEFAULT);
                        Log.d("AITL URI :-", "" + getImageUri(getActivity(), photo));
                        Log.d("AITL PICTUREPATH :-", "" + getRealPathFromURI(getImageUri(getActivity(), photo)));

                    } else {
                        try {
                            final Uri imageUri = data.getData();
                            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            profile_image.setImageBitmap(selectedImage);
                            picturePath = getRealPathFromURI(imageUri);

//                            if(picturePath.contains("raw")){
//                             picturePath = picturePath.replace("/raw/","");
//                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Log.d("AITL Crop Data", "" + data.getData());
                Log.d("AITL Crop Data", "" + data.getExtras());

                Bundle extras2 = data.getExtras();
                if (extras2 != null) {
                    Bitmap photo = extras2.getParcelable("data");
                    profile_image.setImageBitmap(photo);
                    Uri uri = getImageUri(getActivity(), photo);
                    picturePath = getRealPathFromURI(uri);
                    Log.d("AITL URI :-", "" + getImageUri(getActivity(), photo));
                    Log.d("AITL PICTUREPATH :-", "" + getRealPathFromURI(getImageUri(getActivity(), photo)));
                } else {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        profile_image.setImageBitmap(selectedImage);
                        picturePath = getRealPathFromURI(imageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }


            } else if (requestCode == 2000)  // This for ReloadProfileDataCome from GoalActivity
            {
                if (data.getStringExtra("goals") != null)
                    sessionManager.setProfileGoal(data.getStringExtra("goals"));
                setProfilePicAndSocial();
            } else if (requestCode == 3000) {
                getSelectedKeywordData();
                generalClassArrayList = (ArrayList<EditProfileAboutYouGeneralClass>) data.getSerializableExtra("aboutYouData");
                aboutDataset(generalClassArrayList);

            }
        }
    }

    public void login_linkedin() {
        LISessionManager.getInstance(getActivity()).init(getActivity(), buildScope(), new AuthListener() {
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
        APIHelper apiHelper = APIHelper.getInstance(getActivity());
        apiHelper.getRequest(getActivity(), "https://api.linkedin.com/v1/people/~:" +
                        "(email-address,first-name,last-name,picture-url,headline)",
                new ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse result) {
                        try {

                            JSONObject response = result.getResponseDataAsJson();
                            if (response.getString("headline").contains(" at")) {
                                String[] headline = response.getString("headline").split(" at ");

                                txt_title.setText(headline[0].toString());
                                txt_company.setText(headline[1].toString());

                                str_company = headline[1].toString();
                                str_title = headline[0].toString();
                            } else {
                                String headline = response.getString("headline");
                                txt_title.setText(headline);
                                str_title = headline;
                            }

                            txt_firstname.setText(response.get("firstName").toString());
                            txt_surName.setText(response.get("lastName").toString());

                            str_firstname = response.get("firstName").toString();
                            str_lastname = response.get("lastName").toString();

                            picturePath = response.getString("pictureUrl");
                            Log.d("AITL LinkedIn Url", "PicURl" + picturePath);
                            if (picturePath != null && !picturePath.isEmpty()) {
                                Log.d("AITL LinkedIn Url", "PicURl" + picturePath);
                                Glide.with(getActivity())
                                        .load(picturePath)
                                        .crossFade()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .listener(new RequestListener<String, GlideDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                progressBar1.setVisibility(View.GONE);
                                                profile_image.setVisibility(View.VISIBLE);
                                                Glide.with(getActivity()).load(MyUrls.thumImgUrl + sessionManager.getImagePath()).centerCrop().fitCenter().into(profile_image);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                progressBar1.setVisibility(View.GONE);
                                                profile_image.setVisibility(View.VISIBLE);


                                                return false;
                                            }
                                        })
                                        .into(profile_image);

//                            Picasso.with(LinkedInSampleActivity.this).load(url).into(photo);
//                            photo.setVisibility(0);
                            }

                            getProfileData();
                            getFormData();

                            if (isvalid)
                                updateSocial();

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

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri


            if (android.os.Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
                cropIntent.setDataAndType(uriToBitmap(picUri), "image/*");
            } else {
                cropIntent.setDataAndType(picUri, "image/*");
            }
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 2);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Uri uriToBitmap(Uri selectedFileUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedFileUri);

            if (bitmap != null) {


                return getImageUri(getActivity(), rotateImageIfRequired(bitmap, selectedFileUri));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {


        try {
            ExifInterface ei = new ExifInterface(getRealPathFromURI(selectedImage));
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void updateSocial() {
        if (GlobalData.checkForUIDVersion()) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.updateProfileAttendeeUid, Param.socialUpdateProfile(sessionManager.getUserId(), sessionManager.getEventId(), sessionManager.getToken(), str_salutation, str_firstname, str_lastname, str_company, str_title, picturePath, str_password, StrJsonObj, sessionManager.getUserEventId()), 0, true, this);
        } else {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.updateProfileAttendee, Param.socialUpdateProfile(sessionManager.getUserId(), sessionManager.getEventId(), sessionManager.getToken(), str_salutation, str_firstname, str_lastname, str_company, str_title, picturePath, str_password, StrJsonObj, ""), 0, true, this);
        }
    }


    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();
        String id;
        try {
            Log.d("AITL", "Facebook" + object.toString());
            Log.d("AITL", "Facebook lastName " + object.getString("last_name"));
            id = object.getString("id");
            URL profile_pic = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
            Log.i("profile_pic", profile_pic + "");
            bundle.putString("profile_pic", profile_pic.toString());
            bundle.putString("idFacebook", id);

            Fb_img = profile_pic.toString();
            picturePath = Fb_img;
            if (picturePath != null && !picturePath.isEmpty()) {
                Log.d("AITL LinkedIn Url", "PicURl" + picturePath);
                Glide.with(getActivity())
                        .load(picturePath)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                profile_image.setVisibility(View.VISIBLE);
                                Glide.with(getActivity()).load(MyUrls.thumImgUrl + sessionManager.getImagePath()).centerCrop().fitCenter().into(profile_image);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                profile_image.setVisibility(View.VISIBLE);


                                return false;
                            }
                        })
                        .into(profile_image);

//                            Picasso.with(LinkedInSampleActivity.this).load(url).into(photo);
//                            photo.setVisibility(0);
            }

            FbId = id;
            if (object.has("first_name")) {
                bundle.putString("first_name", object.getString("first_name"));
                str_firstname = object.getString("first_name");
                txt_firstname.setText(str_firstname);

            }
            if (object.has("last_name")) {
                bundle.putString("last_name", object.getString("last_name"));
                str_lastname = object.getString("last_name");
                txt_surName.setText(str_lastname);
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


//  For PermissionCamera

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {

        switch (volleyResponse.type) {
            case 0: // Linked in
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);

                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL UpdateJson", jsonObject.toString());

                        sessionManager.updateProfile(jsonObject);
                        sessionManager.setFormStatus("0");
                        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                            GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 2: // Update Profile
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        sessionManager.updateProfile(jsonObject);
                        sessionManager.setFormStatus("0");
                        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                            GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                        dialog.dismiss();
                        Log.d("AITL UPDATE PROFILE", jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            case 3: // Get Additional Data
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        JSONArray jsonArrayField = jsonObject.optJSONArray("data");
                        JSONObject jsonObjectSocial = jsonObject.getJSONObject("social");
                        isFaceook = jsonObjectSocial.getString("is_facebook");
                        isLinkedin = jsonObjectSocial.getString("is_linkedin");

                        str_hideIdentity = jsonObject.getString("identity_hidden");
                        str_enable_hide_identity = jsonObject.getString("enable_hide_identity");


                        sessionManager.setHideMyIdentityStatus(str_hideIdentity);
                        if (str_hideIdentity.equalsIgnoreCase("0")) {
                            swith_identifyProfile.setChecked(false);
                        } else {
                            swith_identifyProfile.setChecked(true);
                        }

                        if (jsonArrayField.length() != 0) {
                            isAdditionalData = true;
                        } else {
                            isAdditionalData = false;
                        }
                        setProfilePicAndSocial();
                        if (str_enable_hide_identity.equalsIgnoreCase("1")) {
                            linear_hideProfile.setVisibility(View.VISIBLE);
                            view_breakLine.setVisibility(View.VISIBLE);
                        } else {
                            linear_hideProfile.setVisibility(View.GONE);
                            view_breakLine.setVisibility(View.GONE);
                        }
                        for (int i = 0; i < jsonArrayField.length(); i++) {

                            JSONObject jsonObjField = (JSONObject) jsonArrayField.get(i);
                            FieldTitle = jsonObjField.optString("title");
                            FieldType = jsonObjField.optString("type");
                            FieldValue = jsonObjField.optString("user_value");

                            if (FieldType.equalsIgnoreCase("element-single-line-text")) {

                                EdtAddiFirst = new EditText(context);
                                EdtAddiFirst.setHint(FieldTitle);
                                EdtAddiFirst.setText(FieldValue);
                                EdtAddiFirst.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                EdtAddiFirst.setTextSize(15);
                                EdtAddiFirst.setPadding(20, 25, 0, 25);
                                EdtAddiFirst.setBackground(getResources().getDrawable(R.drawable.edittext_roundedcornar_profile));
                                //   EdtAddiFirst.setBackgroundResource(R.drawable.square_bg);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                EdtAddiFirst.setLayoutParams(params);
                                linear_additional.addView(EdtAddiFirst);
                                linear_additional.setPadding(0, 0, 0, 10);

                                SinglelineArray.add(EdtAddiFirst);
                                StrSinglelineArray.add(FieldTitle);

                            } else if (FieldType.equalsIgnoreCase("element-paragraph-text")) {

                                EdtAddiDescripation = new EditText(context);
                                EdtAddiDescripation.setHint(FieldTitle);
                                EdtAddiDescripation.setText(FieldValue);
                                EdtAddiDescripation.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                EdtAddiDescripation.setTextSize(15);
                                EdtAddiDescripation.setPadding(20, 25, 0, 25);
                                EdtAddiDescripation.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                // EdtAddiDescripation.setBackgroundResource(R.drawable.square_bg);
                                EdtAddiDescripation.setBackground(getResources().getDrawable(R.drawable.edittext_roundedcornar_profile));
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                EdtAddiDescripation.setLayoutParams(params);
                                linear_additional.addView(EdtAddiDescripation);
                                linear_additional.setPadding(0, 0, 0, 10);

                                MultilineArray.add(EdtAddiDescripation);
                                StrMultilineArray.add(FieldTitle);
                            } else if (FieldType.equalsIgnoreCase("element-multiple-choice")) {

                                TextView txtchk = new TextView(context);
                                txtchk.setText(FieldTitle);
                                txtchk.setTextSize(15);
                                txtchk.setTypeface(null, Typeface.BOLD);
                                txtchk.setPadding(20, 25, 0, 25);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                txtchk.setLayoutParams(params);
                                linear_additional.addView(txtchk);
/*
                                JSONArray fieldArray = new JSONArray(FieldValue);
                                String field[] = new String[fieldArray.length()];
                                for (int j = 0; j < fieldArray.length(); j++) {

                                    field[j] = fieldArray.getString(j);
                                    Log.d("field", "getVolleyRequestResponse: " + field[j]);

                                }*/

                                JSONArray jArrayChkMultiple = jsonObjField.optJSONArray("choices");
                                StrchkboxArray.add(FieldTitle);
                                for (int chk = 0; chk < jArrayChkMultiple.length(); chk++) {
                                    JSONObject jObjectChk = (JSONObject) jArrayChkMultiple.get(chk);
                                    final CheckBox chkBox = new CheckBox(context);
                                    chkBox.setText(jObjectChk.optString("title"));
                                    chkBox.setPadding(10, 15, 0, 15);
                                    chkBox.setTextSize(15);
                                    chkBox.setId(chk);
/*
                                    for (int j = 0; j < field.length; j++) {

                                        if (chkBox.getText().toString().equalsIgnoreCase(field[j])) {
                                            chkBox.setChecked(true);
                                            arrayAddiChkList.add(chkBox.getText().toString());
                                        }
                                    }*/

                                    if (jObjectChk.getBoolean("checked")) {
                                        chkBox.setChecked(true);
                                        arrayAddiChkList.add(chkBox.getText().toString());
                                    }

                                    chkBox.setTextColor(getResources().getColor(R.color.hintcolor));
                                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    params1.leftMargin = 20;
                                    chkBox.setLayoutParams(params1);
                                    linear_additional.addView(chkBox);
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

                                linear_additional.setPadding(0, 0, 0, 10);

                            } else if (FieldType.equalsIgnoreCase("element-section-break")) {

                                View line = new View(context);
                                line.setBackgroundColor(getResources().getColor(R.color.hintcolor));
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
                                line.setLayoutParams(params);
                                linear_additional.addView(line);
                                linear_additional.setPadding(0, 0, 0, 10);

                            } else if (FieldType.equalsIgnoreCase("element-number")) {

                                EdtAddiNumber = new EditText(context);
                                EdtAddiNumber.setHint(FieldTitle);
                                EdtAddiNumber.setText(FieldValue);
                                EdtAddiNumber.setHintTextColor(getResources().getColor(R.color.hintcolor));
                                EdtAddiNumber.setTextSize(15);
                                EdtAddiNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                                EdtAddiNumber.setPadding(20, 25, 0, 25);
                                // EdtAddiNumber.setBackgroundResource(R.drawable.square_bg);
                                EdtAddiNumber.setBackground(getResources().getDrawable(R.drawable.edittext_roundedcornar_profile));
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.topMargin = 10;
                                EdtAddiNumber.setLayoutParams(params);
                                linear_additional.addView(EdtAddiNumber);
                                linear_additional.setPadding(0, 0, 0, 10);
                                StrNumberArray.add(FieldTitle);
                                NumberArray.add(EdtAddiNumber);

                            } else if (FieldType.equalsIgnoreCase("element-checkboxes")) {

                                TextView txtRdo = new TextView(context);
                                txtRdo.setText(FieldTitle);
                                txtRdo.setTextSize(15);
                                txtRdo.setTypeface(null, Typeface.BOLD);
                                txtRdo.setPadding(10, 15, 0, 15);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                txtRdo.setLayoutParams(params);
                                linear_additional.addView(txtRdo);
                                StrRadioGroupArray.add(FieldTitle);
                                JSONArray jArrayRdo = jsonObjField.optJSONArray("choices");
                                Log.d("ArraySize", "" + jArrayRdo.length());
                                RadioGroup radioGroup = new RadioGroup(context);

                                for (int j = 0; j < jArrayRdo.length(); j++) {
                                    JSONObject jObjectRdo = (JSONObject) jArrayRdo.get(j);
                                    Log.d("Count", " " + j);

                                    RadioButton radioButton = new RadioButton(context);
                                    radioButton.setText(jObjectRdo.optString("title"));
                                    radioButton.setPadding(15, 15, 0, 15);
                                    radioButton.setTextSize(15);
                                    radioButton.setTextColor(getResources().getColor(R.color.hintcolor));
                                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    params1.leftMargin = 20;
                                    radioGroup.addView(radioButton);
                                    radioButton.setLayoutParams(params1);

                                    if (jObjectRdo.getBoolean("checked")) {
                                        radioButton.setChecked(true);
                                    }

                                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                                            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                                            StrRadio = radioButton.getText().toString();
                                            Log.d("Radio button", StrRadio);
                                        }
                                    });
                                }

                                linear_additional.addView(radioGroup);
                                linear_additional.setPadding(0, 0, 0, 10);
                                RadioGroupArray.add(radioGroup);

                            } else if (FieldType.equalsIgnoreCase("element-dropdown")) {
                                TextView txtSpinner = new TextView(context);
                                txtSpinner.setText(FieldTitle);
                                txtSpinner.setTextSize(15);

                                txtSpinner.setTypeface(null, Typeface.BOLD);
                                txtSpinner.setPadding(10, 15, 0, 15);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                txtSpinner.setLayoutParams(params);
                                linear_additional.addView(txtSpinner);

                                JSONArray jArraySpinner = jsonObjField.optJSONArray("choices");
                                final Spinner spinner = new Spinner(context);

                                int pos = 0;
                                ArrayList<String> arrayList = new ArrayList<>();
                                for (int k = 0; k < jArraySpinner.length(); k++) {
                                    JSONObject jObjectSpinner = (JSONObject) jArraySpinner.get(k);

                                    arrayList.add(jObjectSpinner.optString("title"));
                                    if (jObjectSpinner.optBoolean("checked"))
                                        pos = k;

                                }
                                ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayList);
                                spinner.setAdapter(adp);
                                spinner.setLayoutParams(params);
                                spinner.setSelection(pos);

                                spinner.setBackground(getResources().getDrawable(R.drawable.edittext_roundedcornar_profile));
                                linear_additional.addView(spinner);
                                SpinnerArray.add(spinner);
                                StrSpinnerArray.add(FieldTitle);
                                StrSpinner = spinner.getItemAtPosition(0).toString();
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        StrSpinner = spinner.getSelectedItem().toString();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                linear_additional.setPadding(0, 0, 0, 10);
                            }
                        }
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
                        startActivity(new Intent(getActivity(), SearchApp_Activity.class));
                        getActivity().finish();
                        dialog.dismiss();
                        sessionManager.setFormStatus("0");
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
                        sessionManager.setHideMyIdentityStatus(str_hideIdentity);
                        setProfilePicAndSocial();
                        Log.d("AITL HideAttendee", jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);

                    if (jsonObject.getString("success").equalsIgnoreCase("true")) ;
                    {
                        Gson gson = new Gson();
                        AttendeeFilterList data = gson.fromJson(jsonObject.toString(), AttendeeFilterList.class);
                        mainKeywordList = new ArrayList<>();
                        mainKeywordList = data.getData();


                        if (sessionManager.getIsUserGoalEnabled().equalsIgnoreCase("1")) {
                            relative_goals.setVisibility(View.VISIBLE);
                        } else {
                            relative_goals.setVisibility(View.GONE);
                        }

                        if (mainKeywordList.size() != 0) {
                            relative_about.setVisibility(View.VISIBLE);
                        } else {
                            relative_about.setVisibility(View.GONE);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);

                    if (jsonObject.getString("success").equalsIgnoreCase("true")) ;
                    {
                        Gson gson = new Gson();
                        AttendeeFilterList data = gson.fromJson(jsonObject.toString(), AttendeeFilterList.class);
                        selectedkeywordList = new ArrayList<>();
                        selectedkeywordList = data.getData();
                        ArrayList<EditProfileAboutYouGeneralClass> generalClasseslist = new ArrayList<>();
                        for (AttendeeFilterList.Data keywordData : selectedkeywordList) {
                            for (String keyword : keywordData.getKeywords()) {
                                generalClasseslist.add(new EditProfileAboutYouGeneralClass(keyword, keywordData.getId(), keywordData.getColumnName(), keywordData.getK()));
                            }
                        }

                        aboutDataset(generalClasseslist);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void aboutDataset(ArrayList<EditProfileAboutYouGeneralClass> generalClasseslist) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < generalClasseslist.size(); i++) {
            stringBuffer.append(generalClasseslist.get(i).getCategoryName() + " - " + generalClasseslist.get(i).getKeyword() + "\n");
        }
        sessionManager.setProfileAbout(stringBuffer.toString());
        setProfilePicAndSocial();
    }

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (((MainActivity) getActivity()).checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ((MainActivity) getActivity()).checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean camerAaddPermission(List<String> permissionsList, String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (((MainActivity) getActivity()).checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    public void requestPermission() {
        permissionsNeeded = new ArrayList<String>();
        permissionsNeeded.clear();
        permissionsList = new ArrayList<String>();
        permissionsList.clear();

        if (!camerAaddPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");
        if (!camerAaddPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");

        if (permissionsList.size() > 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                    Log.i("niral", permissions[i] + " :" + perms.get(permissions[i]));
                }
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    loadCamera();
//                    GlobalData.cameraPermissionbroadCast(MainActivity.this);
                    Log.d("Bhavdip", "MainActivty PermissionGranted");

                } else {
                    // Permission Denied
                    requestPermission();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}