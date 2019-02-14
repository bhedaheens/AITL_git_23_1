package com.allintheloop.Fragment.AttandeeFragments;


import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Activity.RegisterActivity;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.BitmapLoader;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Attendee_edit_FragmentDailog extends android.support.v4.app.DialogFragment implements VolleyInterface {

    Dialog dialog;
    ImageView dailog_profile_close;
    CircleImageView profile_image;
    Button btn_submit;
    TextView txt_selectImage;
    EditText edt_age, edt_firstname, edt_lastname, edt_company;
    SessionManager sessionManager;
    String picturePath = "";
    Bitmap bitmapImage = null;
    ProgressBar progressBar1;
    ContentValues values;
    Uri imageUri;
    Bitmap thumbnail;
    private static final int RESULT_OK = -1;
    private static int RESULT_LOAD_IMAGE = 5;
    String fieldKey, fieldValue;
    EditText EdtAddiFirst;
    TextView txtDynamic, txt_profileName;
    LinearLayout linear_textview;

    ArrayList<EditText> SinglelineArray;
    ArrayList<String> StrSinglelineArray;
    String str_first, str_last, str_cmpny = "";
    String str_personInfo = "", str_CustomeInfo = "";
    boolean isProfileUpdate;

    List<String> permissionsNeeded;
    List<String> permissionsList;
    ProgressDialog progressDialog;

    public Attendee_edit_FragmentDailog() {
        // Required empty public constructor
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_layout);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_attendee_edit__fragment_dailog, container, false);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        dailog_profile_close = (ImageView) rootView.findViewById(R.id.dailog_profile_close);
        profile_image = (CircleImageView) rootView.findViewById(R.id.profile_image);
        sessionManager = new SessionManager(getActivity());
        edt_firstname = (EditText) rootView.findViewById(R.id.edt_firstname);
        edt_lastname = (EditText) rootView.findViewById(R.id.edt_lastname);
        edt_company = (EditText) rootView.findViewById(R.id.edt_company);
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        linear_textview = (LinearLayout) rootView.findViewById(R.id.linear_textview);
        txt_selectImage = (TextView) rootView.findViewById(R.id.txt_selectImage);
        SinglelineArray = new ArrayList<>();
        StrSinglelineArray = new ArrayList<>();
        dailog_profile_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.ViewAttnedeePortal, Param.checkInPortal(sessionManager.getEventId(), sessionManager.portalCheckInAttenDeeId), 0, true, this);
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }


        txt_selectImage.setOnClickListener(new View.OnClickListener() {
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


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_submit.setText("Updating..");
                str_first = edt_firstname.getText().toString();
                str_last = edt_lastname.getText().toString();
                str_cmpny = edt_company.getText().toString();

                if (str_first.trim().length() <= 0) {
                    edt_firstname.setError("Please Enter First Name");

                } else if (str_last.trim().length() <= 0) {
                    edt_lastname.setError("Please Enter Last Name");
                } else {
                    try {
                        JSONObject jsonpersonal = new JSONObject();
                        jsonpersonal.put("Firstname", str_first);
                        jsonpersonal.put("Lastname", str_last);
                        jsonpersonal.put("Company_name", str_cmpny);

                        str_personInfo = jsonpersonal.toString();
                        Log.d("AITL str_personInfo", str_personInfo);

                        JSONObject jsonCustome = new JSONObject();
                        if (SinglelineArray.size() != 0) {
                            for (int i = 0; i < SinglelineArray.size(); i++) {
                                jsonCustome.put(StrSinglelineArray.get(i).toString(), SinglelineArray.get(i).getText().toString());
                            }
                            str_CustomeInfo = jsonCustome.toString();
                            Log.d("AITL CUSTOME INFo", "DATA :-" + str_CustomeInfo);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    UpdateData();
                }


            }
        });

        return rootView;
    }


    private void UpdateData() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (isProfileUpdate) {
                new VolleyRequest(getActivity(), MyUrls.saveAttendee, Param.update_photo(new File(picturePath)), Param.saveAttendee(sessionManager.getEventId(), sessionManager.portalCheckInAttenDeeId, str_personInfo, str_CustomeInfo), 1, true, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.saveAttendee, Param.saveAttendee(sessionManager.getEventId(), sessionManager.portalCheckInAttenDeeId, str_personInfo, str_CustomeInfo), 1, true, this);
            }
        } else {
            ToastC.show(getActivity(), "No InterNet Connection");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            isProfileUpdate = true;
            Log.d("AITL", "ResultOk");
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

                }
            } else if (requestCode == RESULT_LOAD_IMAGE) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    Log.d("AITL Load picturepath", picturePath);
                    cursor.close();
                    profile_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL CHECKIN PORTAL", jsonObject.toString());

                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONObject jsonPersonalInfo = jsonData.getJSONObject("personal_info");
                        JSONArray jArrayPersonalInfo = jsonData.getJSONArray("custom_info");

                        edt_firstname.setText(jsonPersonalInfo.getString("Firstname"));
                        edt_lastname.setText(jsonPersonalInfo.getString("Lastname"));
                        edt_company.setText(jsonPersonalInfo.getString("Company_name"));

//                            GradientDrawable drawable = new GradientDrawable();
//
//                            Random rnd = new Random();
                        if (jsonPersonalInfo.getString("Logo").equalsIgnoreCase("")) {
                            progressBar1.setVisibility(View.GONE);
                            profile_image.setVisibility(View.VISIBLE);
                            Glide.with(getActivity()).load("").centerCrop().fitCenter().placeholder(R.drawable.profile).into(profile_image);
//                                txt_profileName.setVisibility(View.VISIBLE);

//                                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//                                Log.d("AITL Color",""+color);
//                                if(!(jsonPersonalInfo.getString("Firstname").equalsIgnoreCase("")) || (!jsonPersonalInfo.getString("Lastname").equalsIgnoreCase("")))
//                                {
//                                    txt_profileName.setText(jsonPersonalInfo.getString("Firstname").charAt(0) + "" + jsonPersonalInfo.getString("Lastname").charAt(0));
//                                }
//                                drawable.setShape(GradientDrawable.OVAL);
//                                drawable.setColor(color);
//                                txt_profileName.setBackgroundDrawable(drawable);

                        } else {

                            Glide.with(getActivity())
                                    .load(MyUrls.Imgurl + jsonPersonalInfo.getString("Logo"))
                                    .crossFade()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            progressBar1.setVisibility(View.GONE);
                                            profile_image.setVisibility(View.VISIBLE);
//                                               txt_profileName.setVisibility(View.VISIBLE);
                                            Glide.with(getActivity()).load("").centerCrop().fitCenter().placeholder(R.drawable.profile).into(profile_image);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            progressBar1.setVisibility(View.GONE);
                                            profile_image.setVisibility(View.VISIBLE);
//                                                txt_profileName.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(profile_image);

                        }


                        for (int i = 0; i < jArrayPersonalInfo.length(); i++) {


                            JSONObject index = jArrayPersonalInfo.getJSONObject(i);
                            fieldKey = index.getString("key");
                            fieldValue = index.getString("value");

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            txtDynamic = new TextView(getActivity());
                            txtDynamic.setText(fieldKey);
                            txtDynamic.setTextSize(15);
                            txtDynamic.setPadding(30, 10, 0, 10);
                            txtDynamic.setLayoutParams(params);

                            linear_textview.addView(txtDynamic);

                            EdtAddiFirst = new EditText(getActivity());
                            EdtAddiFirst.setText(fieldValue);
                            EdtAddiFirst.setTextColor(getResources().getColor(R.color.GrayColor));
                            EdtAddiFirst.setTextSize(15);
                            EdtAddiFirst.setGravity(Gravity.CENTER_VERTICAL);
                            EdtAddiFirst.setPadding(30, 30, 0, 30);
                            EdtAddiFirst.setBackgroundResource(R.drawable.square_bg);

                            EdtAddiFirst.setLayoutParams(params);
                            linear_textview.addView(EdtAddiFirst);

                            StrSinglelineArray.add(fieldKey);
                            SinglelineArray.add(EdtAddiFirst);


                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL UPDATE DATA", jsonObject.toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        dialog.dismiss();
                        GlobalData.CURRENT_FRAG = GlobalData.CheckIn_Portal;
                        ((MainActivity) getActivity()).loadFragment();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

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

    private void loadCamera() {

        String[] item = {"Gallery", "Camera"};
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Choose Image From")
                .items(item)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (which == 0) {
                            //gallery
                            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            i.setType("image/*");
                            startActivityForResult(i, RESULT_LOAD_IMAGE);

                        } else if (which == 1) {
                            //camera
                            //   status = "camera";
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

}
