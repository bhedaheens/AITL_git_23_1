package com.allintheloop.Fragment.ActivityModule;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Adapter.AdapterActivity.Adapter_Activity_CommentView;
import com.allintheloop.Adapter.Adapter_InternalFeedCommentView;
import com.allintheloop.Bean.ActivityModule.ActivityCommentClass;
import com.allintheloop.Bean.ActivityModule.ActivityCommonClass;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.RoundedImageConverter;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityCommentView_Fragment extends Fragment implements VolleyInterface {


    FrameLayout frame_image;
    LinearLayout linearLayout_bottomVew;
    ImageView imggallaryimages, img_btndelete, img_send, img_selectImage, img_userProfile;
    EditText edt_message;
    Bundle bundle;
    //    Activity_Internal_Feed activityInternalFeedObj;
//    Activity_Facebook_Feed activityFacebookFeedObj;
    ArrayList<Object> objectsArrayList;
    ArrayList<ActivityCommentClass> commentArrayList;
    ActivityCommonClass activityCommonClass;
    Adapter_Activity_CommentView adapterActivityCommentView;
    Adapter_InternalFeedCommentView adapter_internalFeedCommentView;
    RecyclerView rv_ViewComments;

    String type = "";
    int position;
    TextView txt_noComments, txt_CommentView;
    SessionManager sessionManager;
    String picturePath = "";


    List<String> permissionsNeeded;
    List<String> permissionsList;
    Bitmap bitmapImage = null;
    private static final int RESULT_OK = -1;
    private static final int RESULT_LOAD_IMAGE = 5;

    public ActivityCommentView_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_actiivty_comment_view_, container, false);

        initView(rootView);
        Onclick();
        bundleInit();
        return rootView;
    }

    private void Onclick() {

        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentMeg = edt_message.getText().toString();
                if (commentMeg.trim().length() != 0) {
                    commentMessage(activityCommonClass.getType(), activityCommonClass.getId(), sessionManager.getUserId(), commentMeg, sessionManager.getEventId(), picturePath);
                } else {
                    ToastC.show(getActivity(), "Please Enter Message");
//                        Log.d("AITL Comment", "Please Enter Message");
                }
            }
        });

        img_selectImage.setOnClickListener(new View.OnClickListener() {
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


        img_btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picturePath = "";
                bitmapImage = null;
                frame_image.setVisibility(View.GONE);
            }
        });
    }

    private void commentMessage(String moduleType, String id, String userId, String message, String eventId, String image) {
        Log.d("AITL  CommentMessage", message);
        new VolleyRequest(getActivity(), MyUrls.ActivtyCommentMessage, Param.message_img(new File(image)), Param.activtyCommentMessage(moduleType, id, userId, eventId, message), 1, true, this);
    }


    private void initView(View rootView) {
        frame_image = (FrameLayout) rootView.findViewById(R.id.frame_image);
        linearLayout_bottomVew = (LinearLayout) rootView.findViewById(R.id.linearLayout_bottomVew);
        imggallaryimages = (ImageView) rootView.findViewById(R.id.imggallaryimages);
        img_btndelete = (ImageView) rootView.findViewById(R.id.img_btndelete);
        img_send = (ImageView) rootView.findViewById(R.id.img_send);
        img_selectImage = (ImageView) rootView.findViewById(R.id.img_selectImage);
        img_userProfile = (ImageView) rootView.findViewById(R.id.img_userProfile);
        txt_noComments = (TextView) rootView.findViewById(R.id.txt_noComments);
        txt_CommentView = (TextView) rootView.findViewById(R.id.txt_CommentView);
        edt_message = (EditText) rootView.findViewById(R.id.edt_message);

        rv_ViewComments = (RecyclerView) rootView.findViewById(R.id.rv_ViewComments);
        sessionManager = new SessionManager(getActivity());


        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            txt_CommentView.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            txt_CommentView.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        } else {
            txt_CommentView.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            txt_CommentView.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        }

        Glide.with(getActivity()).load(MyUrls.thumImgUrl + sessionManager.getImagePath()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                img_userProfile.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                img_userProfile.setVisibility(View.VISIBLE);
                return false;
            }
        }).placeholder(getActivity().getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(img_userProfile) {
            @Override
            protected void setResource(Bitmap resource) {
                img_userProfile.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, getActivity()));
            }
        });

    }

    private void bundleInit() {
//        bundle = getArguments();
//        if (bundle != null) {
//            type = bundle.getString("type");
//            position = bundle.getInt("position");
//            if (type.equalsIgnoreCase("1"))  // InternalData
//            {
//                linearLayout_bottomVew.setVisibility(View.VISIBLE);
//                activityInternalFeedObj = bundle.getParcelable("obj");
//                setUpInternalData();
//            } else if (type.equalsIgnoreCase("2"))  // FacebookData
//            {
//                linearLayout_bottomVew.setVisibility(View.GONE);
//                activityFacebookFeedObj = bundle.getParcelable("obj");
//                setUpFacebookData();
//            }
//
//        }
    }

    private void setUpFacebookData() {
//        txt_noComments.setText(R.string.txt_noCommentforFb);
//        objectsArrayList = new ArrayList<>();
//        if (activityFacebookFeedObj.getComments().getData() != null) {
//            objectsArrayList.addAll(activityFacebookFeedObj.getComments().getData());
//            if (objectsArrayList != null) {
//                if (objectsArrayList.size() != 0) {
//                    rv_ViewComments.setVisibility(View.VISIBLE);
//                    txt_noComments.setVisibility(View.GONE);
//                    adapterActivityCommentView = new Adapter_Activity_CommentView(objectsArrayList, getActivity());
//                    rv_ViewComments.setItemAnimator(new DefaultItemAnimator());
//                    rv_ViewComments.setLayoutManager(new LinearLayoutManager(getContext()));
//                    rv_ViewComments.setAdapter(adapterActivityCommentView);
//                } else {
//                    rv_ViewComments.setVisibility(View.GONE);
//                    txt_noComments.setVisibility(View.VISIBLE);
//                }
//            } else {
//                rv_ViewComments.setVisibility(View.GONE);
//                txt_noComments.setVisibility(View.VISIBLE);
//            }
//        } else {
//            rv_ViewComments.setVisibility(View.GONE);
//            txt_noComments.setVisibility(View.VISIBLE);
//        }


//
    }

    private void setUpInternalData() {
//        commentArrayList = (ArrayList<ActivityCommentClass>) activityInternalFeedObj.getComments();
//        if (commentArrayList != null) {
//            if (commentArrayList.size() != 0) {
//                rv_ViewComments.setVisibility(View.VISIBLE);
//                txt_noComments.setVisibility(View.GONE);
//                adapter_internalFeedCommentView = new Adapter_InternalFeedCommentView(commentArrayList, getActivity());
//                rv_ViewComments.setItemAnimator(new DefaultItemAnimator());
//                rv_ViewComments.setLayoutManager(new LinearLayoutManager(getContext()));
//                rv_ViewComments.setAdapter(adapter_internalFeedCommentView);
//            } else {
//                rv_ViewComments.setVisibility(View.GONE);
//                txt_noComments.setVisibility(View.VISIBLE);
//            }
//        } else {
//            rv_ViewComments.setVisibility(View.GONE);
//            txt_noComments.setVisibility(View.VISIBLE);
//        }

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
                            try {
                                startActivityForResult(Intent.createChooser(intent,
                                        "Complete action using"), RESULT_LOAD_IMAGE);

                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }

                        } else if (which == 1) {
                            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(i, 1);
                        }
                    }

                })
                .build();
        dialog.show();
    }

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
                    frame_image.setVisibility(View.VISIBLE);
                    bitmapImage = (Bitmap) data.getExtras().get("data");
                    Uri tempUri = getImageUri(getActivity(), bitmapImage);
                    picturePath = getRealPathFromURI(tempUri);
                    selectimage(picturePath);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (requestCode == RESULT_LOAD_IMAGE) {
                try {
                    Bundle extras2 = data.getExtras();
                    if (extras2 != null) {
                        frame_image.setVisibility(View.VISIBLE);
                        Bitmap photo = extras2.getParcelable("data");
                        Uri uri = getImageUri(getActivity(), photo);
                        picturePath = getRealPathFromURI(uri);
                        selectimage(picturePath);
                    } else {
                        try {
                            frame_image.setVisibility(View.VISIBLE);
                            final Uri imageUri = data.getData();
                            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            picturePath = getRealPathFromURI(imageUri);
                            selectimageBitymap(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void selectimage(String images) {
        Context context = getActivity();
        Glide.with(context).load(images).placeholder(R.drawable.defult_attende).centerCrop().into(imggallaryimages);
    }

    public void selectimageBitymap(Bitmap images) {
        imggallaryimages.setImageBitmap(images);
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

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 1:
                try {
                    JSONObject jsonObject1 = new JSONObject(volleyResponse.output);
                    if (jsonObject1.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL CommentMessage", jsonObject1.toString());
                        sessionManager.keyboradHidden(edt_message);
                        picturePath = "";
                        bitmapImage = null;
                        frame_image.setVisibility(View.GONE);
                        edt_message.setText("");
                        GlobalData.activiyReloadedfromSharePicture(getActivity());
                        ((MainActivity) getActivity()).fragmentBackStackMaintain();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
