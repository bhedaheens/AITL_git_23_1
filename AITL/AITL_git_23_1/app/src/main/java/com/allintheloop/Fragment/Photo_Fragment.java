package com.allintheloop.Fragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Adapter.GallaryAdepter;
import com.allintheloop.Adapter.Photo_Adapter;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.Attendee.Attendee_Comment;
import com.allintheloop.Bean.GallaryBean;
import com.allintheloop.Bean.photoSection;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.crosswall.photo.pick.PickConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class Photo_Fragment extends Fragment implements VolleyInterface {


    SessionManager sessionManager;
    public static LinearLayout linear_photo, linearimage_load;
    LinearLayout private_message;
    ImageView img_upload;

    Button btn_load_more, btn_photo;

    static Context context;
    public static RecyclerView recycler_img_gallary_picker;
    public static RecyclerView rv_viewMessage;

    public static ArrayList<String> selectImages;
    public static ArrayList<GallaryBean> gallaryBeansarraylist;
    LinearLayoutManager linearLayoutManager;

    public static Photo_Adapter photo_adapter;
    public ArrayList<photoSection> photoArrayList;
    ArrayList<Attendee_Comment> commentArrayList;
    static GallaryAdepter gallaryAdepter;

    private static final int RESULT_OK = -1;
    public static int counter = 0;
    String picturePath = " ";
    Bitmap bitmapImage = null;
    public static String str_feedID;
    boolean isFirstTime = false;
    boolean btn_load;
    String feed_id, res_time, res_senderName, res_totalLike, res_totalComment, res_isLike, res_senderLogo, res_timeStamp, sender_id;
    String photoFeed_id = "";
    int total_pages, page_count = 1;
    int isLast = 0;
    int cnt;

    TextView textViewNoDATA;


    Bundle bundle;


    ContentValues values;
    Uri imageUri;
    Bitmap thumbnail;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String tag = "PhotoListing";
    String adverties_id = "";

    NestedScrollView scrollView;
    boolean isLoading;
    Handler handler;
    ProgressBar progressBar2;
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout;
    LinearLayout linear_content;
    TextView txt_deviceCamera, txt_AddPhoto, txt_Msgname;

    DefaultLanguage.DefaultLang defaultLang;


    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;

    List<String> permissionsNeeded;
    List<String> permissionsList;

    public Photo_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photo, container, false);

        selectImages = new ArrayList<>();
        gallaryBeansarraylist = new ArrayList<>();

        context = getContext();

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        photoArrayList = new ArrayList<>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        linear_photo = (LinearLayout) rootView.findViewById(R.id.linear_photo);
        linearimage_load = (LinearLayout) rootView.findViewById(R.id.linearimage_load);
        private_message = (LinearLayout) rootView.findViewById(R.id.private_message);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);
        handler = new Handler();
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        btn_load_more = (Button) rootView.findViewById(R.id.btn_load_more);
        txt_deviceCamera = (TextView) rootView.findViewById(R.id.txt_deviceCamera);
        txt_AddPhoto = (TextView) rootView.findViewById(R.id.txt_AddPhoto);
        txt_Msgname = (TextView) rootView.findViewById(R.id.txt_Msgname);
        btn_photo = (Button) rootView.findViewById(R.id.btn_photo);
        recycler_img_gallary_picker = (RecyclerView) rootView.findViewById(R.id.recycler_img_gallary_picker);
        rv_viewMessage = (RecyclerView) rootView.findViewById(R.id.rv_viewMessage);
        rv_viewMessage.setNestedScrollingEnabled(false);
        img_upload = (ImageView) rootView.findViewById(R.id.img_upload);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rv_viewMessage.setLayoutManager(linearLayoutManager);
        progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar2);


        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();

        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);

        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);


        btn_photo.setText(defaultLang.get11SendYourPhoto());
        txt_deviceCamera.setText(defaultLang.get11TakeAPhotoOnYourDeviceCamera());
        txt_AddPhoto.setText(defaultLang.get11AddPhoto());
        txt_Msgname.setText(defaultLang.get11Feed());


        bundle = new Bundle();


        rv_viewMessage.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                photoSection photoSectionObj = photo_adapter.getItem(position);

                str_feedID = photoSectionObj.getFeed_id();
                SessionManager.viewImg_tag = "photoFragment";
                Log.d("AITL", photoSectionObj.getFeed_id());

            }
        }));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    photoArrayList.clear();
                    page_count = 1;
                    viewphotoFeed();
                } else {
                    ToastC.show(getActivity(), getString(R.string.noInernet));
                }
            }
        });

        img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //camera
                //   status = "camera";
//                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(i, 1);
                if (sessionManager.isLogin()) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (isCameraPermissionGranted()) {
                            loadCamera();
                        } else {
                            requestPermission();
                        }
                    } else {
                        loadCamera();
                    }
                } else {
                    sessionManager.alertDailogLogin(getActivity());
                }
            }

        });

        linear_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (sessionManager.isLogin()) {
                    new PickConfig.Builder(getActivity())
                            .pickMode(PickConfig.MODE_MULTIP_PICK)
                            .maxPickSize(10)
                            .spanCount(3)
                            .flag(4)
                            .toolbarColor(R.color.colorPrimary)
                            .build();

                } else {
                    sessionManager.alertDailogLogin(getActivity());
                }


            }
        });

        btn_load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (page_count == total_pages) {
                    btn_load_more.setVisibility(View.GONE);
                }
                page_count++;
                getPhotoListing();

            }
        });


        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sessionManager.isLogin()) {
                    if (gallaryBeansarraylist.size() == 0) {
                        ToastC.show(getActivity(), "Please select photo");
                    } else {
                        uploadFeed(gallaryBeansarraylist.get(cnt).getImages());
                        Log.d("AITL arrayList SiZe", "" + gallaryBeansarraylist.size());
                        Log.d("UploadRecipePhotoAPI", gallaryBeansarraylist.get(cnt).getImages());
                        cnt = 1;
                    }

                } else {
                    sessionManager.alertDailogLogin(getActivity());
                }
            }
        });


        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            viewphotoFeed();
        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                viewphotoFeed();
            }
        }
        getAdvertiesment();
        return rootView;
    }


    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 2, false, this);

        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAdvertiesMentData(sessionManager.getEventId(), sessionManager.getMenuid());
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.adverties_Data)));
                        Log.d("AITL  Map Oflline", jsonObject.toString());
                        getAdvertiesment(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void viewphotoFeed() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            photoArrayList.clear();
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));

                        Log.d("AITL  Photo  Oflline", jsonObject.toString());
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                isFirstTime = true;
                if (isFirstTime) {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPhotoFeed, Param.getAllphotoFeed(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), page_count, sessionManager.getUserId()), 0, false, this);
                    isFirstTime = false;
                } else {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPhotoFeed, Param.getAllphotoFeed(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), page_count, sessionManager.getUserId()), 0, false, this);
                }
            } else {
                isFirstTime = true;
                if (isFirstTime) {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPhotoFeed, Param.getAllphotoFeed(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), page_count, sessionManager.getUserId()), 0, false, this);
                    isFirstTime = false;
                } else {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPhotoFeed, Param.getAllphotoFeed(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), page_count, sessionManager.getUserId()), 0, false, this);
                }
            }
        } else {
            photoArrayList.clear();
            btn_load_more.setVisibility(View.GONE);
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));

                        Log.d("AITL  Photo  Oflline", jsonObject.toString());
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                ToastC.show(getActivity(), getString(R.string.noInernet));
            }
        }
    }

    private void uploadFeed(String multipleImg) {
        Log.d("AITL", multipleImg);
        File file = new File(multipleImg);

        long fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
//        long fileSizeInMB = fileSizeInKB / 1024;

        GlobalData globalData = new GlobalData();
        String imgPath;

        if (fileSizeInKB > 1024) {
            imgPath = globalData.compressImage(multipleImg, getContext());
            File file1 = new File(imgPath);
            long afterCompress = file1.length();
            long fileInKB = afterCompress / 1024;
            Log.d("AITL After Comparess", "Compress" + fileInKB);

        } else {
            imgPath = multipleImg;
        }

        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), MyUrls.uploadPhotoFeed, Param.message_img(new File(imgPath)), Param.uploadPhotofeed(sessionManager.getEventId(), sessionManager.getToken(), photoFeed_id, sessionManager.getUserId()), 1, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }


    private void advertiesClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", adverties_id, "AD", ""), 5, true, this);
        }
    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 5, false, this);
        }
    }

    public static void selectimage(String images) {

        gallaryBeansarraylist.add(new GallaryBean(images, "photo_section"));

        Log.d("gallaryBeansarraylist", gallaryBeansarraylist.toString());
        Log.d("gallaryBeansarraylistSIZE", String.valueOf(gallaryBeansarraylist.size()));

        gallaryAdepter = new GallaryAdepter(gallaryBeansarraylist, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recycler_img_gallary_picker.setLayoutManager(mLayoutManager);
        recycler_img_gallary_picker.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recycler_img_gallary_picker.setAdapter(gallaryAdepter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                Log.d("AITL", "ResultOk");
                if (requestCode == 1) {
                    try {
                        linearimage_load.setVisibility(View.VISIBLE);
                        picturePath = getRealPathFromURI(imageUri);
                        thumbnail = BitmapLoader.loadBitmap(picturePath, 100, 100);

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
                } else {

                    photo_adapter.onActivityResult(requestCode, resultCode, data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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


    private void getAdvertiesment(JSONObject jsonObject) {

        try {
            JSONObject jsonObjectAdavertiesment = jsonObject.getJSONObject("data");
            JSONArray jArrayHeader = jsonObjectAdavertiesment.getJSONArray("header");
            JSONArray jArrayFooter = jsonObjectAdavertiesment.getJSONArray("footer");

            topAdverViewArrayList = new ArrayList<>();
            bottomAdverViewArrayList = new ArrayList<>();
            for (int i = 0; i < jArrayHeader.length(); i++) {
                JSONObject index = jArrayHeader.getJSONObject(i);
                topAdverViewArrayList.add(new AdvertiesmentTopView(index.getString("image"), index.getString("url"), index.getString("id")));
            }

            for (int i = 0; i < jArrayFooter.length(); i++) {
                JSONObject index = jArrayFooter.getJSONObject(i);
                bottomAdverViewArrayList.add(new AdvertiesMentbottomView(index.getString("image"), index.getString("url"), index.getString("id")));
            }

            if (jsonObjectAdavertiesment.getString("show_sticky").equalsIgnoreCase("1")) {
                sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_Data, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(getContext(), "1", MainLayout, relativeLayout_Data, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "1", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getPhotoListing() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPhotoFeed, Param.getAllphotoFeed(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), page_count, sessionManager.getUserId()), 3, false, this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void loadData(JSONObject json_data) {
        try {
            total_pages = json_data.getInt("total_pages");
            JSONArray jMessageArray = json_data.getJSONArray("public_feeds");
            if (!isLoading) {
                for (int k = 0; k < jMessageArray.length(); k++) {
                    String str_img = "";
                    JSONObject jObjectMessage = jMessageArray.getJSONObject(k);
                    feed_id = jObjectMessage.getString("feed_id");
                    res_time = jObjectMessage.getString("Time");
                    res_senderName = jObjectMessage.getString("Sendername");
                    res_senderLogo = jObjectMessage.getString("Senderlogo");
                    res_timeStamp = jObjectMessage.getString("time_stamp");
                    sender_id = jObjectMessage.getString("Sender_id");
                    res_totalComment = jObjectMessage.getString("total_comments");
                    res_isLike = jObjectMessage.getString("is_like");
                    res_totalLike = jObjectMessage.getString("total_likes");

                    Log.d("AITL Fragment Logo", res_senderLogo);

                    JSONArray jArrayImg = jObjectMessage.getJSONArray("image");
                    if (jArrayImg.length() != 0) {
                        if ((!jArrayImg.getString(0).toString().equalsIgnoreCase(""))) {
                            str_img = MyUrls.Imgurl + jArrayImg.getString(0).toString();
                            Log.d("AITL Image Private", str_img);
                        }

                    }

                    JSONArray jArrayCommt = jObjectMessage.getJSONArray("comment");
                    commentArrayList = new ArrayList<>();
                    for (int z = 0; z < jArrayCommt.length(); z++) {
                        JSONObject jObjCommt = jArrayCommt.getJSONObject(z);
                        commentArrayList.add(new Attendee_Comment(jObjCommt.getString("comment_id"), jObjCommt.getString("user_id"), jObjCommt.getString("user_name"), jObjCommt.getString("comment"), MyUrls.Imgurl + jObjCommt.getString("Logo"), MyUrls.Imgurl + jObjCommt.getString("image"), jObjCommt.getString("Time"), jObjCommt.getString("time_stamp"), "photo_section"));

                    }
                    photoArrayList.add(new photoSection(feed_id, sender_id, res_time, res_senderName, res_senderLogo, res_totalLike, res_isLike, res_timeStamp, res_totalComment, commentArrayList, str_img));
                }

            } else {
                ArrayList<photoSection> tmp_mArrayList = new ArrayList<>();
                for (int k = 0; k < jMessageArray.length(); k++) {
                    String str_img = "";
                    JSONObject jObjectMessage = jMessageArray.getJSONObject(k);
                    feed_id = jObjectMessage.getString("feed_id");
                    res_time = jObjectMessage.getString("Time");
                    res_senderName = jObjectMessage.getString("Sendername");
                    res_senderLogo = jObjectMessage.getString("Senderlogo");
                    res_timeStamp = jObjectMessage.getString("time_stamp");
                    sender_id = jObjectMessage.getString("Sender_id");
                    res_totalComment = jObjectMessage.getString("total_comments");
                    res_isLike = jObjectMessage.getString("is_like");
                    res_totalLike = jObjectMessage.getString("total_likes");

                    Log.d("AITL Fragment Logo", res_senderLogo);

                    JSONArray jArrayImg = jObjectMessage.getJSONArray("image");
                    if (jArrayImg.length() != 0) {
                        if ((!jArrayImg.getString(0).toString().equalsIgnoreCase(""))) {
                            str_img = MyUrls.Imgurl + jArrayImg.getString(0).toString();
                            Log.d("AITL Image Private", str_img);
                        }
                    }
                    JSONArray jArrayCommt = jObjectMessage.getJSONArray("comment");
                    commentArrayList = new ArrayList<>();
                    for (int z = 0; z < jArrayCommt.length(); z++) {
                        JSONObject jObjCommt = jArrayCommt.getJSONObject(z);
                        commentArrayList.add(new Attendee_Comment(jObjCommt.getString("comment_id"), jObjCommt.getString("user_id"), jObjCommt.getString("user_name"), jObjCommt.getString("comment"), MyUrls.Imgurl + jObjCommt.getString("Logo"), MyUrls.Imgurl + jObjCommt.getString("image"), jObjCommt.getString("Time"), jObjCommt.getString("time_stamp"), "photo_section"));

                    }

                    tmp_mArrayList.add(new photoSection(feed_id, sender_id, res_time, res_senderName, res_senderLogo, res_totalLike, res_isLike, res_timeStamp, res_totalComment, commentArrayList, str_img));
                }
                photoArrayList.addAll(tmp_mArrayList);
            }
            set_recycler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void set_recycler() {
        try {
            if (!isLoading) {

                if (photoArrayList.size() == 0) {

                    rv_viewMessage.setVisibility(View.GONE);

                } else {
                    rv_viewMessage.setVisibility(View.VISIBLE);
                    photo_adapter = new Photo_Adapter(photoArrayList, rv_viewMessage, getActivity(), linearLayoutManager, getActivity(), scrollView);
                    rv_viewMessage.setAdapter(photo_adapter);

                }
            } else {
                photo_adapter = new Photo_Adapter(photoArrayList, rv_viewMessage, getActivity(), linearLayoutManager, getActivity(), scrollView);
                rv_viewMessage.setAdapter(photo_adapter);
                photo_adapter.setLoaded();
                isLoading = false;
            }

            if (photoArrayList.size() != 0)
                photo_adapter.setOnLoadMoreListener(new OnLoadMoreListener() {

                    @Override
                    public void onLoadMore() {

                        page_count++;

                        if (page_count <= total_pages) {

                            //    photo_adapter.addFooter();
                            progressBar2.setVisibility(View.VISIBLE);
                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    // photo_adapter.removeFooter();
                                    progressBar2.setVisibility(View.GONE);
                                    isLoading = true;

                                    try {
                                        if (GlobalData.isNetworkAvailable(getActivity()))
                                            getPhotoListing();
                                        else
                                            Toast.makeText(getActivity(), getString(R.string.noInernet), Toast.LENGTH_SHORT).show();
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, 2000);
                        }

                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    photoArrayList.clear();
                    mSwipeRefreshLayout.setRefreshing(false);
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    JSONObject json_data = jsonObject.getJSONObject("data");

                    if (sqLiteDatabaseHandler.isAttendeeListExist(sessionManager.getEventId(), sessionManager.getUserId(), tag)) {
                        sqLiteDatabaseHandler.deleteListingData(sessionManager.getEventId(), tag, sessionManager.getUserId());
                        sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), json_data.toString(), tag);
                    } else {
                        sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), json_data.toString(), tag);
                    }
                    loadData(json_data);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject imgupload = new JSONObject(volleyResponse.output);
                    Log.d("AITL UploadImg", imgupload.toString());
                    if (photoFeed_id.equalsIgnoreCase("")) {
                        Log.d("AITL IF feedID", imgupload.getString("feed_id"));
                        photoFeed_id = imgupload.getString("feed_id");
                    } else {
                        Log.d("AITL ELSE feedID", imgupload.getString("feed_id"));
                    }

                    if (cnt < gallaryBeansarraylist.size()) {
                        uploadFeed(gallaryBeansarraylist.get(cnt).getImages());

                        Log.d("AITL counter", "" + cnt);
                        cnt++;
                    } else {
                        Log.d("AITL IMAGE", "ARRAYLIST GET CLEARED");
                        gallaryBeansarraylist.clear();
                        linearimage_load.setVisibility(View.GONE);
                        GlobalData.CURRENT_FRAG = GlobalData.Photo_Fragment;
                        cnt = 0;
                        ((MainActivity) getActivity()).reloadFragment();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    mSwipeRefreshLayout.setRefreshing(false);
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    JSONObject json_data = jsonObject.getJSONObject("data");
                    loadData(json_data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    pagewiseClick();
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) ;
                    {
                        Log.d("AITL Advertiesment", jsonObject.toString());

                        if (sqLiteDatabaseHandler.isAdvertiesMentExist(sessionManager.getEventId(), sessionManager.getMenuid())) {
                            sqLiteDatabaseHandler.deleteAdvertiesMentData(sessionManager.getEventId(), sessionManager.getMenuid());
                            sqLiteDatabaseHandler.insertAdvertiesmentData(sessionManager.getEventId(), sessionManager.getMenuid(), jsonObject.toString());
                        } else {
                            sqLiteDatabaseHandler.insertAdvertiesmentData(sessionManager.getEventId(), sessionManager.getMenuid(), jsonObject.toString());
                        }

                        getAdvertiesment(jsonObject);
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

    private void loadCamera() {
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1);
    }

}
