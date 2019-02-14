package com.allintheloop.Fragment.ActivityModule;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Adapter.GallaryAdepter;
import com.allintheloop.Bean.GallaryBean;
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

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.crosswall.photo.pick.PickConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitySharePost_Fragment extends Fragment implements VolleyInterface {

    EditText edt_message;
    BoldTextView txt_publicPost, txt_sharePicture, txt_selfiCame;
    SessionManager sessionManager;

    List<String> permissionsNeeded;
    List<String> permissionsList;

    public static LinearLayout linear_addPhoto, linearimage_load;
    public static HorizontalScrollView horizontalScrollView1;
    public static RecyclerView recycler_img_gallary_picker;
    public static ArrayList<String> selectImages;
    public static ArrayList<GallaryBean> gallaryBeansarraylist;
    static GallaryAdepter gallaryAdepter;
    String picturePath = " ";
    Bitmap bitmapImage = null;
    static Context context;
    public static int counter = 0;
    int isLast = 0;
    private static final int RESULT_OK = -1;
    String str_nessge = "", str_message_id = "";
    ImageView img_correctSign;
    LinearLayout linear_postNowButton;

    public ActivitySharePost_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activity_share_post, container, false);
        initView(rootView);
        otherInitView();
        onClick();
        return rootView;
    }

    private void onClick() {
        txt_sharePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPhoto();
            }
        });


        txt_publicPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sessionManager.isLogin()) {
                    str_nessge = edt_message.getText().toString();
                    if (str_nessge.trim().length() == 0) {
                        ToastC.show(getActivity(), "Please Enter Message");
                    } else if (gallaryBeansarraylist.size() >= 0 && str_nessge.trim().length() == 0) {
                        ToastC.show(getActivity(), "Please enter Message First");
                    } else {
                        if (GlobalData.isNetworkAvailable(getActivity())) {
                            sessionManager.keyboradHidden(edt_message);
                            PostPublish();
                        } else {
                            ToastC.show(getActivity(), getString(R.string.noInernet));
                        }
                    }
                } else {
                    sessionManager.alertDailogLogin(getActivity());
                }

            }
        });

        txt_selfiCame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.PhotoFilter_Fragment;
                ((MainActivity) getActivity()).loadFragment();
            }
        });
    }

    private void otherInitView() {
        sessionManager = new SessionManager(getActivity());
        setShareButtonBackground();
        setButtonPublicPost();

        if (sessionManager.getPhotoFilterEnabled().equalsIgnoreCase("1")) {
            txt_selfiCame.setVisibility(View.VISIBLE);
        } else {
            txt_selfiCame.setVisibility(View.GONE);
        }

    }


    private void PostPublish() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.postUpdate, Param.public_message(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId(), str_nessge), 1, true, this);
    }

    private void UploadePhotoAPI(String multipleImg) {
        Log.d("AITL ", "MultipleImageAPI" + multipleImg);
        new VolleyRequest(getActivity(), MyUrls.saveActivityImages, Param.message_img(new File(multipleImg)), Param.addActivityImageRequest(str_message_id), 2, true, this);
    }

    public static void selectimage(String images) {

        gallaryBeansarraylist.add(new GallaryBean(images, "ActivityModule"));

        Log.d("AITL", "gallaryBeansarraylist" + gallaryBeansarraylist.toString());
        Log.d("AITL", "gallaryBeansarraylistSIZE" + String.valueOf(gallaryBeansarraylist.size()));

        gallaryAdepter = new GallaryAdepter(gallaryBeansarraylist, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recycler_img_gallary_picker.setLayoutManager(mLayoutManager);
        recycler_img_gallary_picker.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recycler_img_gallary_picker.setAdapter(gallaryAdepter);
    }

    private void setButtonPublicPost() {
        GradientDrawable drawablepublicPostButton = new GradientDrawable();
        drawablepublicPostButton.setShape(GradientDrawable.RECTANGLE);
        drawablepublicPostButton.setCornerRadius(70.0f);

        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            drawablepublicPostButton.setStroke(3, Color.parseColor(sessionManager.getFunTopBackColor()));
            drawablepublicPostButton.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            linear_postNowButton.setBackgroundDrawable(drawablepublicPostButton);
            txt_publicPost.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            img_correctSign.setColorFilter(Color.parseColor(sessionManager.getFunTopTextColor()));
        } else {
            drawablepublicPostButton.setStroke(3, Color.parseColor(sessionManager.getTopBackColor()));
            drawablepublicPostButton.setColor(Color.parseColor(sessionManager.getTopBackColor()));
            linear_postNowButton.setBackgroundDrawable(drawablepublicPostButton);
            txt_publicPost.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            img_correctSign.setColorFilter(Color.parseColor(sessionManager.getTopTextColor()));
        }
    }

    private void setShareButtonBackground() {
        GradientDrawable drawableShareButton = new GradientDrawable();
        drawableShareButton.setShape(GradientDrawable.RECTANGLE);
        drawableShareButton.setCornerRadius(70.0f);


        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            drawableShareButton.setStroke(3, Color.parseColor(sessionManager.getFunTopBackColor()));
            drawableShareButton.setColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            txt_sharePicture.setBackgroundDrawable(drawableShareButton);
            txt_sharePicture.setTextColor(Color.parseColor(sessionManager.getFunTopBackColor()));


            txt_selfiCame.setBackgroundDrawable(drawableShareButton);
            txt_selfiCame.setTextColor(Color.parseColor(sessionManager.getFunTopBackColor()));

        } else {
            drawableShareButton.setStroke(3, Color.parseColor(sessionManager.getTopBackColor()));
            drawableShareButton.setColor(Color.parseColor(sessionManager.getTopTextColor()));
            txt_sharePicture.setBackgroundDrawable(drawableShareButton);
            txt_sharePicture.setTextColor(Color.parseColor(sessionManager.getTopBackColor()));

            txt_selfiCame.setBackgroundDrawable(drawableShareButton);
            txt_selfiCame.setTextColor(Color.parseColor(sessionManager.getTopBackColor()));
        }

    }

    private void initView(View rootView) {
        txt_publicPost = (BoldTextView) rootView.findViewById(R.id.txt_publicPost);
        txt_sharePicture = (BoldTextView) rootView.findViewById(R.id.txt_sharePicture);
        txt_selfiCame = (BoldTextView) rootView.findViewById(R.id.txt_selfiCame);
        edt_message = (EditText) rootView.findViewById(R.id.edt_message);
        img_correctSign = (ImageView) rootView.findViewById(R.id.img_correctSign);
        linear_postNowButton = (LinearLayout) rootView.findViewById(R.id.linear_postNowButton);


        recycler_img_gallary_picker = (RecyclerView) rootView.findViewById(R.id.recycler_img_gallary_picker);
        selectImages = new ArrayList<>();
        gallaryBeansarraylist = new ArrayList<>();
        context = getContext();
        linearimage_load = (LinearLayout) rootView.findViewById(R.id.linearimage_load);
        horizontalScrollView1 = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Log.d("AITL", "ResultOk");
            if (requestCode == 1) {
                try {

                    linearimage_load.setVisibility(View.VISIBLE);
                    horizontalScrollView1.setVisibility(View.VISIBLE);
                    bitmapImage = (Bitmap) data.getExtras().get("data");
                    Log.d("AITL ImageBitMap", bitmapImage.toString());
                    Uri tempUri = getImageUri(getActivity(), bitmapImage);
                    picturePath = getRealPathFromURI(tempUri);
                    Log.d("Camerapath", picturePath);

                    selectImages.add(picturePath);
                    gallaryBeansarraylist.clear();
                    for (int j = 0; j < selectImages.size(); j++) {
                        selectimage(selectImages.get(j).toString());
                        Log.d("Camera ", selectImages.get(j).toString());
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Log.d("AITL ImageUri", inImage.toString());
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.d("AITL ImageUri", path.toString());
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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

    private void loadPhoto() {
        if (sessionManager.isLogin()) {
            String[] item = {"Gallery", "Camera"};
            MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                    .title("Choose Image From")
                    .items(item)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            if (which == 0) {
                                //gallery
                                new PickConfig.Builder(getActivity())
                                        .pickMode(PickConfig.MODE_MULTIP_PICK)
                                        .maxPickSize(10)
                                        .spanCount(3)
                                        .flag(9)
                                        .toolbarColor(R.color.colorPrimary)
                                        .build();

                            } else if (which == 1) {
                                //camera
                                //   status = "camera";
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
                        }

                    })
                    .build();
            dialog.show();
        } else {
            sessionManager.alertDailogLogin(getActivity());
        }
    }

    private void loadCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 1);
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

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 1:
                try {
                    JSONObject jsonmessage = new JSONObject(volleyResponse.output);
                    if (jsonmessage.getString("success").equalsIgnoreCase("true")) {
                        str_message_id = jsonmessage.getString("message_id");
                        if (gallaryBeansarraylist.size() == 0) {
                            edt_message.setText("");
                            GlobalData.activiyReloadedfromSharePicture(getActivity());
                            ((MainActivity) getActivity()).fragmentBackStackMaintain();
                        } else {
                            for (int j = 0; j < gallaryBeansarraylist.size(); j++) {
                                isLast = j + 1;
                                UploadePhotoAPI(gallaryBeansarraylist.get(j).getImages());
                            }
                        }
                    } else {
                        ToastC.show(getActivity(), jsonmessage.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject imgupload = new JSONObject(volleyResponse.output);
                    Log.d("AITL UploadImg", imgupload.toString());
                    if (isLast == gallaryBeansarraylist.size()) {
                        GlobalData.activiyReloadedfromSharePicture(getActivity());
                        ((MainActivity) getActivity()).fragmentBackStackMaintain();
                        edt_message.setText("");
                        gallaryBeansarraylist.clear();
                        selectImages.clear();
                        linearimage_load.setVisibility(View.GONE);
                        horizontalScrollView1.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
