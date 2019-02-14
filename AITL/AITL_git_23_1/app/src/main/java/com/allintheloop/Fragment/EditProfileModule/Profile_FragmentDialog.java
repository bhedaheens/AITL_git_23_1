package com.allintheloop.Fragment.EditProfileModule;


import android.Manifest;
import android.app.Activity;
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
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Activity.CoutryList_Activity;
import com.allintheloop.Bean.UidCommonKeyClass;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_FragmentDialog extends DialogFragment implements VolleyInterface {
    public static final String OAUTH_CALLBACK_HOST = "litestcalback";
    private static final int RESULT_OK = -1;
    private static int RESULT_LOAD_IMAGE = 5;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    Dialog dialog;
    CircleImageView profile_image;
    TextView txt_selectImage, txt_fullName, spr_country;
    EditText edt_salutation, edt_firstname, edt_lastname, edt_title, edt_company, edt_email;
    Button btn_submit;
    String str_salutation = "", str_firstname = "", str_lastname = "", str_title = "", str_company = "", str_email = "";
    ImageView dailog_profile_close, img_linkedin, image_profile_close;
    SessionManager sessionManager;
    String picturePath = "";
    Bitmap bitmapImage = null;
    ProgressBar progressBar1;
    ContentValues values;
    Uri imageUri;
    Bitmap thumbnail;
    Button btn_mainDetailswap, btn_contactDetailswap, btn_contactsubmit;
    LinearLayout layout_mainDetail, layout_contactDetail;
    boolean isLinkedIn;
    String status, country_code = "";
    String str_password = "", str_Cpassword = "", str_linkedUrl = "", str_fbUrl = "", str_twitterUrl = "", str_contactNumber = "";
    EditText edt_contactEmail, edt_password, edt_Cpassword, edt_linkedUrl, edt_fbUrl, edt_twitterUrl, edt_contactNumber;
    List<String> permissionsNeeded;
    List<String> permissionsList;
    ProgressDialog progressDialog;
    private UidCommonKeyClass uidCommonKeyClass;
    private byte[] byte_arr;

    public Profile_FragmentDialog() {
        // Required empty public constructor
    }

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile__fragment_dialog, container, false);
        sessionManager = new SessionManager(getActivity());
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();
        profile_image = (CircleImageView) rootView.findViewById(R.id.profile_image);
        txt_selectImage = (TextView) rootView.findViewById(R.id.txt_selectImage);
        spr_country = (TextView) rootView.findViewById(R.id.spr_country);
        txt_fullName = (TextView) rootView.findViewById(R.id.txt_fullName);
        edt_salutation = (EditText) rootView.findViewById(R.id.edt_salutation);
        edt_firstname = (EditText) rootView.findViewById(R.id.edt_firstname);
        edt_lastname = (EditText) rootView.findViewById(R.id.edt_lastname);
        edt_title = (EditText) rootView.findViewById(R.id.edt_title);
        edt_company = (EditText) rootView.findViewById(R.id.edt_company);
        edt_email = (EditText) rootView.findViewById(R.id.edt_email);

        edt_contactEmail = (EditText) rootView.findViewById(R.id.edt_contactEmail);
        edt_password = (EditText) rootView.findViewById(R.id.edt_password);
        edt_Cpassword = (EditText) rootView.findViewById(R.id.edt_Cpassword);
        edt_linkedUrl = (EditText) rootView.findViewById(R.id.edt_linkedUrl);
        edt_fbUrl = (EditText) rootView.findViewById(R.id.edt_fbUrl);
        edt_twitterUrl = (EditText) rootView.findViewById(R.id.edt_twitterUrl);
        edt_contactNumber = (EditText) rootView.findViewById(R.id.edt_contactNumber);


        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);

        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        btn_mainDetailswap = (Button) rootView.findViewById(R.id.btn_mainDetailswap);
        btn_contactDetailswap = (Button) rootView.findViewById(R.id.btn_contactDetailswap);
        btn_contactsubmit = (Button) rootView.findViewById(R.id.btn_contactsubmit);

        layout_mainDetail = (LinearLayout) rootView.findViewById(R.id.layout_mainDetail);
        layout_contactDetail = (LinearLayout) rootView.findViewById(R.id.layout_contactDetail);

        dailog_profile_close = (ImageView) rootView.findViewById(R.id.dailog_profile_close);
        image_profile_close = (ImageView) rootView.findViewById(R.id.image_profile_close);
        img_linkedin = (ImageView) rootView.findViewById(R.id.img_linkedin);
        txt_fullName.setText(sessionManager.getFirstName() + " " + sessionManager.getLastName());
        edt_salutation.setText(sessionManager.getSalutationName());
        edt_firstname.setText(sessionManager.getFirstName());
        edt_lastname.setText(sessionManager.getLastName());
        edt_title.setText(sessionManager.getTitle());
        edt_company.setText(sessionManager.getCompany_name());
        edt_email.setText(sessionManager.getEmail());

        edt_contactEmail.setText(sessionManager.getEmail());
        edt_fbUrl.setText(sessionManager.getFacebookUrl());
        edt_linkedUrl.setText(sessionManager.getlinkedInUrl());
        edt_twitterUrl.setText(sessionManager.getTwitterUrl());
        country_code = sessionManager.getCountryId();
        if (!(sessionManager.getCountryName().equalsIgnoreCase(""))) {
            spr_country.setText(sessionManager.getCountryName());
        }
        btn_contactsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_contactNumber = edt_contactNumber.getText().toString();
                str_password = edt_password.getText().toString();
                str_Cpassword = edt_Cpassword.getText().toString();
                str_linkedUrl = edt_linkedUrl.getText().toString();
                str_fbUrl = edt_fbUrl.getText().toString();
                str_twitterUrl = edt_twitterUrl.getText().toString();

                if (!(str_password.equalsIgnoreCase(str_Cpassword.toString()))) {
                    edt_Cpassword.setError("Please Enter Confirm Password Same as Password");
                } else {
                    updateContactDetail();
                }
            }
        });

        spr_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),
                        CoutryList_Activity.class);

                status = "country";
                i.putExtra("status", status);
                startActivityForResult(i, 200);
            }
        });

        if (Build.VERSION.SDK_INT >= 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        image_profile_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_image.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                picturePath = "";
                image_profile_close.setVisibility(View.GONE);
            }
        });


        if (sessionManager.getEventType().equalsIgnoreCase("4")) {
            if (sessionManager.getFormStatus().equalsIgnoreCase("1")) {
                setCancelable(false);
                dailog_profile_close.setVisibility(View.GONE);
            } else {
                setCancelable(true);
                dailog_profile_close.setVisibility(View.VISIBLE);
            }
        } else {
            setCancelable(true);
            dailog_profile_close.setVisibility(View.VISIBLE);
        }

        btn_mainDetailswap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_contactDetail.setVisibility(View.GONE);
                layout_mainDetail.setVisibility(View.VISIBLE);

            }
        });


        btn_contactDetailswap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_contactDetail.setVisibility(View.VISIBLE);
                layout_mainDetail.setVisibility(View.GONE);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                str_salutation = edt_salutation.getText().toString();
                str_firstname = edt_firstname.getText().toString();
                str_lastname = edt_lastname.getText().toString();
                str_title = edt_title.getText().toString();
                str_company = edt_company.getText().toString();
                str_email = edt_email.getText().toString();

                sessionManager.keyboradHidden(edt_salutation);

                if (str_firstname.trim().length() == 0) {
                    edt_firstname.setError("Please Enter First Name");
                } else if (str_lastname.trim().length() == 0) {
                    edt_lastname.setError("Please Enter Last Name");
                } else if (str_email.trim().length() == 0) {
                    edt_email.setError("Please Enter Email");
                } else {

                    updateProfile();
                }
            }
        });


        img_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isLinkedIn = true;
                if (GlobalData.isNetworkAvailable(getActivity())) {

                    LISessionManager.getInstance(getActivity()).clearSession();
                    login_linkedin();
                } else {
                    ToastC.show(getActivity(), getString(R.string.noInernet));
                }
            }
        });
        Log.d("AITL Image", "Profile" + sessionManager.getImagePath());
        Log.d("AITL Image", "Profile" + MyUrls.thumImgUrl + sessionManager.getImagePath());

        if (!(sessionManager.getImagePath().equalsIgnoreCase(""))) {

            if (sessionManager.getRolId().equalsIgnoreCase("4")) {//pending
                image_profile_close.setVisibility(View.VISIBLE);
            } else {
                image_profile_close.setVisibility(View.GONE);
            }


            Glide.with(getActivity())
                    .load(MyUrls.thumImgUrl + sessionManager.getImagePath())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar1.setVisibility(View.GONE);
                            profile_image.setVisibility(View.VISIBLE);
                            Glide.with(getActivity()).load(MyUrls.thumImgUrl + sessionManager.getImagePath()).centerCrop().fitCenter().placeholder(R.drawable.profile).into(profile_image);
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

        } else {
            image_profile_close.setVisibility(View.GONE);
        }
        txt_selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        dailog_profile_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.keyboradHidden(edt_salutation);
                getDialog().dismiss();
            }
        });

        return rootView;
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

                                edt_title.setText(headline[0].toString());
                                edt_company.setText(headline[1].toString());

                                str_company = headline[1].toString();
                                str_title = headline[0].toString();
                            } else {
                                String headline = response.getString("headline");
                                edt_title.setText(headline);
                                str_title = headline;
                            }

                            txt_fullName.setText(response.get("firstName").toString() + " " + response.get("lastName").toString());
                            edt_firstname.setText(response.get("firstName").toString());
                            edt_lastname.setText(response.get("lastName").toString());

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

                            updateLinkeInProfile();

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

    private void loadCamera() {
        isLinkedIn = false;
        String[] item = {"Gallery", "Camera"};
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Choose Image From")
                .items(item)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (which == 0) {
                            //gallery

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
                            //camera
                            //   status = "camera";


                        }
                    }

                })
                .build();
        dialog.show();
    }

    private void updateContactDetail() {
        if (GlobalData.checkForUIDVersion()) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.updateContactDetailUid, Param.updateContactDetail(sessionManager.getUserId(), str_linkedUrl, str_fbUrl, str_twitterUrl, str_contactNumber, country_code, str_password, sessionManager.getUserEventId()), 1, true, this);
        } else {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.updateContactDetail, Param.updateContactDetail(sessionManager.getUserId(), str_linkedUrl, str_fbUrl, str_twitterUrl, str_contactNumber, country_code, str_password, ""), 1, true, this);
        }
    }

    private void updateLinkeInProfile() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Update_profile, Param.LinkedInupdate_Photo(sessionManager.getUserId(), sessionManager.getEventId(), sessionManager.getToken(), str_salutation, str_firstname, str_lastname, str_company, str_title, picturePath), 0, true, this);
    }

    private void updateProfile() {

        if (GlobalData.isNetworkAvailable(getActivity())) {
//            if (!isLinkedIn)
//            {
            new VolleyRequest(getActivity(), MyUrls.Update_profile, Param.update_photo(new File(picturePath)), Param.update_Photo(sessionManager.getUserId(), sessionManager.getEventId(), sessionManager.getToken(), str_salutation, str_firstname, str_lastname, str_company, str_title), 0, true, this);
            // }
//            else
//            {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Update_profile, Param.LinkedInupdate_Photo(sessionManager.getUserId(), sessionManager.getEventId(), sessionManager.getToken(), str_salutation, str_firstname, str_lastname, str_company, str_title, picturePath), 0, true, this);
//            }
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            if (data.getStringExtra("status").equalsIgnoreCase("country")) {
                spr_country.setText(data.getStringExtra("name"));
                country_code = data.getStringExtra("code");
                Log.d("CoutnryCode", country_code);
            }
        }


        if (resultCode == RESULT_OK) {

            Log.d("AITL", "ResultOk :-" + requestCode);
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

                    if (sessionManager.getRolId().equalsIgnoreCase("4"))//pending
                        image_profile_close.setVisibility(View.VISIBLE);

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
                if (sessionManager.getRolId().equalsIgnoreCase("4"))//pending
                    image_profile_close.setVisibility(View.VISIBLE);
            }
        }
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
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

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
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
//                        startActivity(new Intent(getActivity(), MainActivity.class));
//                        getActivity().finish();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        sessionManager.loginResponse(jsonObject, true);

                        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                            GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                            ((MainActivity) getActivity()).loadFragment();
                        }
//                            startActivity(new Intent(getActivity(), MainActivity.class));
//                            getActivity().finish();
                        dialog.dismiss();
                        Log.d("AITL UPDATE PROFILE", jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
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