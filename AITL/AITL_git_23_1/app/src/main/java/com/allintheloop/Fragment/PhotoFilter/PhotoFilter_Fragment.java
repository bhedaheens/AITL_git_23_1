package com.allintheloop.Fragment.PhotoFilter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Bean.PhotoFilter.EventBusgetPhotoFilterData;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyCustomProgressDialog;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;
import com.wonderkiln.camerakit.Size;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFilter_Fragment extends Fragment implements VolleyInterface {


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    int intheight, intwidth;
    SessionManager sessionManager;
    LinearLayout linear_buttonClick;
    String isClick = "0";
    ImageView camera_swipe, imageViewFrame, imageViewPreview, img_previewDone;
    int currentCameraId;
    ImageView image_share, img_back, img_fbShare, img_twitter_share, img_linkedin_share, img_internalShare;
    LinearLayout linear_photoClick, linear_shareView;
    File myImage;
    String picturePath = "";
    int relative_surface_width, relative_surface_height;
    RelativeLayout relative_surface;
    CameraView camera_surface;
    File storagePath;
    Context context;
    Handler handler;
    Bitmap bitmapoffline;
    List<String> permissionsNeeded;
    List<String> permissionsList;
    MyCustomProgressDialog mProgressDialog;
    LinearLayout linear_cameraChange;
    TextView txt_seeAllPhoto, txt_seeAllFilters;
    int width;
    int height;

    public PhotoFilter_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_photo_filter, container, false);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        intheight = displayMetrics.heightPixels;
        intwidth = displayMetrics.widthPixels;
        sessionManager = new SessionManager(getActivity());
        handler = new Handler();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (Build.VERSION.SDK_INT >= 23) {
            if (!isCameraPermissionGranted()) {
                requestPermission();
            }
        }
        // create path to image
        context = getActivity();
        camera_swipe = rootView.findViewById(R.id.camera_swipe);
        image_share = rootView.findViewById(R.id.image_share);
        img_back = rootView.findViewById(R.id.img_back);
        img_fbShare = rootView.findViewById(R.id.img_fbShare);
        img_internalShare = rootView.findViewById(R.id.img_internalShare);
        img_linkedin_share = rootView.findViewById(R.id.img_linkedin_share);
        img_twitter_share = rootView.findViewById(R.id.img_twitter_share);
        img_previewDone = rootView.findViewById(R.id.img_previewDone);

        imageViewFrame = rootView.findViewById(R.id.imageViewFrame);
        imageViewPreview = rootView.findViewById(R.id.imageViewPreview);
        linear_buttonClick = rootView.findViewById(R.id.linear_buttonClick);
        linear_shareView = rootView.findViewById(R.id.linear_shareView);
        linear_photoClick = rootView.findViewById(R.id.linear_photoClick);
        linear_cameraChange = rootView.findViewById(R.id.linear_cameraChange);
        relative_surface = rootView.findViewById(R.id.relative_surface);
        camera_surface = rootView.findViewById(R.id.camera_surface);
        txt_seeAllPhoto = rootView.findViewById(R.id.txt_seeAllPhoto);
        txt_seeAllFilters = rootView.findViewById(R.id.txt_seeAllFilters);

        if (sessionManager.isLogin()) {
            txt_seeAllPhoto.setVisibility(View.VISIBLE);
        } else
            txt_seeAllPhoto.setVisibility(View.GONE);

        txt_seeAllFilters.setOnClickListener(v -> {

            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.PhotoFilter_seeAllFitlers;
            ((MainActivity) getActivity()).loadFragment();
        });

        txt_seeAllPhoto.setOnClickListener(v -> {
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.PhotoFilter_seeAllPhotos;
            ((MainActivity) getActivity()).loadFragment();
        });

        camera_surface.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(final CameraKitImage cameraKitImage) {
                new Handler().postDelayed(() -> {

                    byte[] data = cameraKitImage.getJpeg();
                    if (data != null) {

                        try {
                            Bitmap bm = cameraKitImage.getBitmap();
                            if (bm.getWidth() > bm.getHeight()) {

                                if (currentCameraId == CameraKit.Constants.FACING_BACK) {
                                    Matrix mtx = new Matrix();
                                    mtx.postRotate(90);
                                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mtx, true);
                                } else {
                                    Matrix mtx = new Matrix();
                                    mtx.postRotate(270);
                                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mtx, true);
                                }
                            }
                            Size size = camera_surface.getCaptureSize();

                            Bitmap scaled;
                            if (currentCameraId == CameraKit.Constants.FACING_BACK) {
                                scaled = Bitmap.createScaledBitmap(bm, size.getHeight(), size.getWidth(), true);
                            } else {
                                scaled = bm;
                            }
//
                            showImage(scaled);
                            new Handler().postDelayed(() -> getScreenShot(), 1000);

                            //new SaveImageNew(scaled).execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 200);

            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });


        relative_surface.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        // make sure it is not called anymore
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            relative_surface.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            relative_surface.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        width = relative_surface.getMeasuredWidth();
                        height = relative_surface.getMeasuredHeight();

                        relative_surface_width = width;
                        relative_surface_height = height;
                        setPhotoframe(width, height);
                    }
                });
        linear_buttonClick.setOnClickListener(v -> {

            try {

                mProgressDialog = GlobalData.getProgressDialog(getActivity());
                String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AllInTheLoop/" + sessionManager.getEventName() + "/" + "PhotoFilter";
                storagePath = new File(rootPath);
                if (!storagePath.exists()) {
                    storagePath.mkdirs();
                }
                myImage = new File(storagePath, System.currentTimeMillis() + "_PhotFrame.jpg");
                Log.d("Bhadvip MyImage", "" + myImage);
                camera_surface.captureImage();


            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        image_share.setOnClickListener(view -> {
            linear_photoClick.setVisibility(View.GONE);
            linear_shareView.setVisibility(View.VISIBLE);
        });

        img_back.setOnClickListener(view -> {
            linear_photoClick.setVisibility(View.VISIBLE);
            linear_shareView.setVisibility(View.GONE);
        });

        img_twitter_share.setOnClickListener(view -> {

            if (imageViewPreview.getVisibility() == View.VISIBLE) {
                if (myImage.exists()) {
                    Uri uri = Uri.fromFile(myImage);
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setPackage("com.twitter.android");
                    share.setType("image/jpg");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    if (isIntentAvailable(getActivity(), share)) {
                        startActivity(share);
                    } else {
                        showDialog("Twitter", "com.twitter.android");
                    }
                }
            } else {
                ToastC.show(getActivity(), "There is no photo to share,Click a Photo to share");
            }
        });


        img_linkedin_share.setOnClickListener(view -> {

            if (imageViewPreview.getVisibility() == View.VISIBLE) {

                if (myImage.exists()) {
                    uploadImage();
                }
            } else {
                ToastC.show(getActivity(), "There is no photo to share,Click a Photo to share");
            }
        });

        img_internalShare.setOnClickListener(view -> {

            if (GlobalData.isNetworkAvailable(getActivity())) {
                if (sessionManager.isLogin()) {

                    try {
                        UploadePhotoAPI(picturePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sessionManager.alertDailogLogin(getActivity());
                }
            } else {
                ToastC.show(getActivity(), getString(R.string.noInernet));
            }
        });
        camera_swipe.setOnClickListener(view -> {
            try {
                new Handler().postDelayed(() -> camera_swipe.setEnabled(true), 1000);
                camera_swipe.setEnabled(false);
                if (currentCameraId == CameraKit.Constants.FACING_BACK) {
                    camera_surface.setFacing(CameraKit.Constants.FACING_FRONT);
                    currentCameraId = CameraKit.Constants.FACING_FRONT;
                } else {
                    camera_surface.setFacing(CameraKit.Constants.FACING_BACK);
                    currentCameraId = CameraKit.Constants.FACING_BACK;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setCornerRadius(13.0f);
        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
//            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
//            linear_buttonClick.setBackgroundDrawable(drawable);
            img_internalShare.setColorFilter(Color.parseColor(sessionManager.getFunTopBackColor()));
//
        } else {
//            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
//            linear_buttonClick.setBackgroundDrawable(drawable);
            img_internalShare.setColorFilter(Color.parseColor(sessionManager.getTopBackColor()));
        }
        return rootView;
    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void scanFile() {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(myImage)));
    }

    private void showDialog(String app, final String packageName) {
        String msg = "Please download " + app + " to continue";
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Download App " + app)
                .items(msg)
                .positiveColor(getResources().getColor(R.color.colorAccent))
                .positiveText("Continue")
                .negativeText(getResources().getString(R.string.cancelText))
                .onPositive((dialog12, which) -> openPlaystore(packageName))
                .onNegative((dialog1, which) -> dialog1.dismiss())
                .cancelable(false)
                .build();

        dialog.show();
    }

    private void openPlaystore(String packageName) {
        Intent viewIntent =
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + packageName));
        getActivity().startActivity(viewIntent);
    }

    private void showImage(Bitmap bmp) {


        imageViewPreview.setVisibility(View.VISIBLE);
        img_previewDone.setVisibility(View.GONE);
        camera_surface.setVisibility(View.GONE);
        imageViewPreview.setImageBitmap(null);
        imageViewPreview.destroyDrawingCache();
        imageViewPreview.setImageResource(0);
        imageViewPreview.setBackground(null);//S
        imageViewPreview.setImageResource(android.R.color.transparent);
        BitmapFactory.Options options = new BitmapFactory.Options();//<---
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;//<---
        imageViewPreview.setVisibility(View.VISIBLE);
        imageViewPreview.setImageBitmap(bmp);

    }

    private void uploadImage() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), MyUrls.getPhotoFilterUrl, Param.message_img(new File(picturePath)), Param.savePhotoFilterImage(sessionManager.getEventId(), sessionManager.getUserId()), 1, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void UploadePhotoAPI(String multipleImg) {
        Log.d("AITL ", "MultipleImageAPI" + multipleImg);
        new VolleyRequest(getActivity(), MyUrls.savePhotoFilterImage, Param.message_img(new File(multipleImg)), Param.savePhotoFilterImage(sessionManager.getEventId(), sessionManager.getUserId()), 0, true, this);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL ImagePost", jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        Intent insta = new Intent(Intent.ACTION_SEND);
                        insta.setType("text/plain");
                        insta.setPackage("com.linkedin.android");
                        insta.putExtra(Intent.EXTRA_TEXT, jsonObject.getString("url"));
                        if (isIntentAvailable(getActivity(), insta)) {
                            startActivity(insta);
                        } else {
                            showDialog("LinkedIn", "com.linkedin.android");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        imageViewPreview.setVisibility(View.GONE);
                        img_previewDone.setVisibility(View.GONE);
                        camera_surface.setVisibility(View.VISIBLE);
                        GlobalData.dismissDialog(mProgressDialog);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

    }

    public void setPhotoframe(final int width, final int height) {
        //==============================
//        camera_surface.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
        Glide.with(getActivity())
                .load(MyUrls.Imgurl + sessionManager.getPhotoFilterImage())
                .asBitmap()
                .into(new ImageViewTarget<Bitmap>(imageViewFrame) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        Bitmap bitmap = transfromBitmap(resource, width, height);
                        imageViewFrame.setImageBitmap(bitmap);
                        bitmapoffline = bitmap;
                    }
                });
    }

    public Bitmap transfromBitmap(Bitmap b, int reqWidth, int reqHeight) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, b.getWidth(), b.getHeight()), new RectF(0, 0, reqWidth, reqHeight), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        camera_surface.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        camera_surface.stop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusgetPhotoFilterData(EventBusgetPhotoFilterData data) {
        setDynamicPhotoFrame(data.getData());
    }

    public void setDynamicPhotoFrame(String url) {
        Glide.with(getActivity())
                .load(url)
                .asBitmap()
                .into(new ImageViewTarget<Bitmap>(imageViewFrame) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        Bitmap bitmap = transfromBitmap(resource, width, height);
                        imageViewFrame.setImageBitmap(bitmap);
                        bitmapoffline = bitmap;
                    }
                });
    }

    private void getScreenShot() {
        relative_surface.setDrawingCacheEnabled(true);
        Bitmap bitmap = relative_surface.getDrawingCache();

        try {
            FileOutputStream out = new FileOutputStream(myImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            picturePath = myImage.getPath();
            scanFile();

            Log.d("Bhavdip ImagePath", picturePath);
//                        camera_surface.stop();
        } catch (FileNotFoundException e) {
            Log.d("In Saving File", e + "");
        } catch (IOException e) {
            Log.d("In Saving File", e + "");
        }
        Log.d("fileSaved", "onPictureTaken: ");
        relative_surface.destroyDrawingCache();
        uploadUserPhoto();

    }


    private void uploadUserPhoto() {
        if (picturePath != null && !picturePath.isEmpty()) {
            if (GlobalData.isNetworkAvailable(getActivity())) {
                new VolleyRequest(getActivity(), MyUrls.uploadUserPhoto, Param.message_img(new File(picturePath)), Param.savePhotoFilterImage(sessionManager.getEventId(), sessionManager.getUserId()), 2, false, this);
            } else {
                ToastC.show(getActivity(), getString(R.string.noInernet));
            }

        }
    }

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (((MainActivity) getActivity()).checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

                // Fill with results
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                    Log.i("niral", permissions[i] + " :" + perms.get(permissions[i]));
                }
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    //
}