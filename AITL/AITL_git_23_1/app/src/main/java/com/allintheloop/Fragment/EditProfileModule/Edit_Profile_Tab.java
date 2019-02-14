package com.allintheloop.Fragment.EditProfileModule;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.allintheloop.Activity.CoutryList_Activity;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Edit_Profile_Tab extends Fragment implements VolleyInterface {

    Spinner spr_profile;
    TextView txtcountry, txtstate;
    ArrayList<String> list;
    Button next, btn_submit, back;

    LinearLayout first_layout, second_layout;
    public static CircleImageView imageView;

    private static final int RESULT_OK = -1;
    private static int RESULT_LOAD_IMAGE = 5;
    String picturePath = "";
    Bitmap bitmapImage = null;
    String spr_profile_id, country_code = "", state_code = "";

    EditText edt_salutation, edt_firstname, edt_lastname, edt_title, edt_company, edt_email, edt_password, edt_Cpassword;
    EditText edt_phone_business, edt_mobile_no, edt_street, edt_suburb, edt_zipCode, edt_biography;

    String salutation = "", firstname = "", lastname = "", title = "", company = "", email = "", password = "", Cpassword = "";
    String phone_business = "", mobile_no = "", street = "", suburb = "", zipCode = "", biography = "";

    String imgurl;
    String status;
    SessionManager sessionManager;
    Activity activity;

    List<String> permissionsNeeded;
    List<String> permissionsList;

//    SharedPreferences preferences;
//    SharedPreferences.Editor editor;

    public Edit_Profile_Tab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile_editprofile, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        spr_profile = (Spinner) rootView.findViewById(R.id.spr_profile);

        txtcountry = (TextView) rootView.findViewById(R.id.spr_country);
        txtstate = (TextView) rootView.findViewById(R.id.spr_state);
        activity = getActivity();
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        next = (Button) rootView.findViewById(R.id.btnNext);
        second_layout = (LinearLayout) rootView.findViewById(R.id.linear_secondpart);
        first_layout = (LinearLayout) rootView.findViewById(R.id.linear_firstpart);
        back = (Button) rootView.findViewById(R.id.back);

        edt_salutation = (EditText) rootView.findViewById(R.id.edt_salutation);
        edt_firstname = (EditText) rootView.findViewById(R.id.edt_firstname);
        edt_lastname = (EditText) rootView.findViewById(R.id.edt_lastname);
        edt_title = (EditText) rootView.findViewById(R.id.edt_title);
        edt_company = (EditText) rootView.findViewById(R.id.edt_company);
        edt_email = (EditText) rootView.findViewById(R.id.edt_email);
        edt_password = (EditText) rootView.findViewById(R.id.edt_password);
        edt_Cpassword = (EditText) rootView.findViewById(R.id.edt_Cpassword);
        edt_phone_business = (EditText) rootView.findViewById(R.id.edt_phone_business);
        edt_mobile_no = (EditText) rootView.findViewById(R.id.edt_mobile_no);
        edt_street = (EditText) rootView.findViewById(R.id.edt_street);
        edt_suburb = (EditText) rootView.findViewById(R.id.edt_suburb);
        edt_zipCode = (EditText) rootView.findViewById(R.id.edt_zipCode);
        edt_biography = (EditText) rootView.findViewById(R.id.edt_biography);

//        preferences = getActivity().getSharedPreferences(GlobalData.USER_PREFRENCE, Context.MODE_PRIVATE);
//        editor=preferences.edit();

        sessionManager = new SessionManager(getActivity());

        imageView = (CircleImageView) rootView.findViewById(R.id.profile_image);

        edt_salutation.setText(sessionManager.getSalutationName());
        edt_firstname.setText(sessionManager.getFirstName());
        edt_lastname.setText(sessionManager.getLastName());
        edt_title.setText(sessionManager.getTitle());
        edt_company.setText(sessionManager.getCompany_name());
        edt_email.setText(sessionManager.getEmail());
        edt_phone_business.setText(sessionManager.getPhone_business());
        edt_mobile_no.setText(sessionManager.getMobile());
        edt_street.setText(sessionManager.getStreet());
        edt_suburb.setText(sessionManager.getSuburb());
        edt_zipCode.setText(sessionManager.getPostcode());

        if (!sessionManager.getCountryName().equals("")) {
            txtcountry.setText(sessionManager.getCountryName());
        }
        if (!sessionManager.getStateName().equals("")) {
            txtstate.setText(sessionManager.getStateName());
        }


        txtcountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),
                        CoutryList_Activity.class);

                status = "country";
                i.putExtra("status", status);
                startActivityForResult(i, 200);
            }
        });

        txtstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!country_code.equalsIgnoreCase("")) {

                    Intent i = new Intent(getActivity(),
                            CoutryList_Activity.class);
                    status = "state";
                    i.putExtra("status", status);
                    i.putExtra("country_code", country_code);
                    startActivityForResult(i, 200);
                } else {
                    ToastC.show(getActivity(), "please Select Country");
                }
            }
        });

        Log.d("AITL", "EditProfile" + sessionManager.getImagePath());
        list = new ArrayList<>();
        list.add("Private");
        list.add("Public");


        imgurl = MyUrls.Imgurl + sessionManager.getImagePath();
        if (sessionManager.getImagePath().equals("")) {
            Glide.with(getActivity()).load(R.drawable.profile)
                    .centerCrop()
                    .fitCenter()
                    .placeholder(R.drawable.profile)
                    .into(imageView);
        } else {
            Glide.with(getActivity()).load(imgurl)
                    .centerCrop()
                    .fitCenter()
                    .placeholder(R.drawable.profile)
                    .into(imageView);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spr_profile.setAdapter(dataAdapter);

        spr_profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String profileLst = parent.getItemAtPosition(position).toString();

                spr_profile_id = String.valueOf(parent.getSelectedItemId());
                Log.d("profileLst", profileLst + "ID:-" + spr_profile_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salutation = edt_salutation.getText().toString();
                firstname = edt_firstname.getText().toString();
                lastname = edt_lastname.getText().toString();
                email = edt_email.getText().toString();
                password = edt_password.getText().toString();
                Cpassword = edt_Cpassword.getText().toString();


                if (firstname.trim().length() <= 0) {
                    Log.d("AITL", "Not correct First");
                    edt_firstname.setError("Enter First Name");
                } else if (lastname.trim().length() <= 0) {
                    edt_lastname.setError("Enter Last Name");
                } else if (email.trim().length() <= 0) {
                    edt_email.setError("Enter Email");
                } else if (!GlobalData.checkEmailValid(email)) {
                    edt_email.setError("Please Enter Valid Email");
                } else if (!password.trim().equals(Cpassword)) {
                    edt_Cpassword.setError("Enter Confirm Password");
                } else {
                    second_layout.setVisibility(View.VISIBLE);
                    first_layout.setVisibility(View.GONE);
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                second_layout.setVisibility(View.GONE);
                first_layout.setVisibility(View.VISIBLE);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                title = edt_title.getText().toString();
                company = edt_company.getText().toString();
                phone_business = edt_phone_business.getText().toString();
                mobile_no = edt_mobile_no.getText().toString();
                street = edt_street.getText().toString();
                suburb = edt_suburb.getText().toString();
                zipCode = edt_zipCode.getText().toString();
                biography = edt_biography.getText().toString();

                //     EditProfileApi(); // call EditProfile Api
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
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

        return rootView;
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

                            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(i, 1);
                            //camera
                            //   status = "camera";
                        }
                    }

                })
                .build();
        dialog.show();
    }

//    private void EditProfileApi() {
//        if (GlobalData.isNetworkAvailable(getActivity()))
//        {
//            new VolleyRequest(getActivity(), MyUrls.Update_profile, Param.update_photo(new File(picturePath)), Param.update_profile(firstname, lastname, email, spr_profile_id, sessionManager.getEventId(), sessionManager.getUserId()
//                    , sessionManager.getToken(), company, title, salutation, password, phone_business, mobile_no, street, suburb, zipCode, country_code, state_code, biography), 2, true, this);
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            if (data.getStringExtra("status").equalsIgnoreCase("country")) {
                txtcountry.setText(data.getStringExtra("name"));
                country_code = data.getStringExtra("code");
                Log.d("CoutnryCode", country_code);
            } else if (data.getStringExtra("status").equalsIgnoreCase("state")) {
                txtstate.setText(data.getStringExtra("name"));
                state_code = data.getStringExtra("code");
            }

        } else {

        }

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                imageView.setImageBitmap((Bitmap) data.getExtras().get("data"));
                try {
                    bitmapImage = (Bitmap) data.getExtras().get("data");
                    Uri tempUri = getImageUri(getActivity(), bitmapImage);
                    picturePath = getRealPathFromURI(tempUri);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (requestCode == RESULT_LOAD_IMAGE) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    Log.d("picturepath", picturePath);
                    cursor.close();
                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 2:
                try {
                    JSONObject resjson = new JSONObject(volleyResponse.output);
                    if (resjson.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {

                        JSONObject jsonObject = resjson.getJSONObject("data");
                        //   Log.d("AITL","Update Json"+jsonObject);
                        sessionManager.updateProfile(resjson);

//                        Glide.with(getActivity()).load(imgurl)
//                                .centerCrop()
//                                .fitCenter()
//                                .placeholder(R.drawable.profile)
//                                .into(MainActivity.user_profile);
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
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


}
