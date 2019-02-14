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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintheloop.Adapter.Attendee.Attendee_message_Adapter;
import com.allintheloop.Adapter.GallaryAdepter;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.Attendee.Attendee_Comment;
import com.allintheloop.Bean.Attendee.Attendee_message;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.GallaryBean;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.BitmapLoader;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
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
public class PublicMessage_Fragment extends Fragment implements VolleyInterface {


    private static final int RESULT_OK = -1;
    public static LinearLayout linear_photo, linearimage_load;
    public static RecyclerView recycler_img_gallary_picker;
    public static RecyclerView rv_viewMessage;
    public static ArrayList<String> selectImages;
    public static ArrayList<GallaryBean> gallaryBeansarraylist;
    public static Attendee_message_Adapter message_adapter;
    public static int counter = 0;
    public static String str_message_id;
    static Context context;
    static GallaryAdepter gallaryAdepter;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    SessionManager sessionManager;
    LinearLayout private_message;
    ImageView img_upload;
    ArrayList<Attendee_message> messageArrayList;
    ArrayList<Attendee_Comment> commentArrayList;
    LinearLayoutManager linearLayoutManager;
    String res_id, res_message, res_senderName, res_reciverName, res_senderLogo, res_timeStamp, sender_id, is_clickable = "0";
    EditText edt_message;
    Button btn_send_message, btn_load_more;
    String str_message, message_id;
    String picturePath = " ";
    Bitmap bitmapImage = null;
    boolean isFirstTime = false;
    boolean btn_load;
    int total_pages, page_count = 1;
    int isLast = 0;
    Bundle bundle;
    ContentValues values;
    Uri imageUri;
    Bitmap thumbnail;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String tag = "PublicMessageListing";
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String adverties_id = "";
    boolean isLoading;
    Handler handler;
    NestedScrollView scrollView;
    ProgressBar progressBar2;
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout;
    DefaultLanguage.DefaultLang defaultLang;
    TextView txt_sendMessage, txt_AddPhoto;
    LinearLayout linear_content;
    List<String> permissionsNeeded;
    List<String> permissionsList;

    public PublicMessage_Fragment() {
        // Required empty public constructor
    }

    public static void selectimage(String images) {

        gallaryBeansarraylist.add(new GallaryBean(images, "public_message"));

        Log.d("AITL", gallaryBeansarraylist.toString());
        Log.d("AITL", String.valueOf(gallaryBeansarraylist.size()));

        gallaryAdepter = new GallaryAdepter(gallaryBeansarraylist, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recycler_img_gallary_picker.setLayoutManager(mLayoutManager);
        recycler_img_gallary_picker.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recycler_img_gallary_picker.setAdapter(gallaryAdepter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_public_message_, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        selectImages = new ArrayList<>();
        gallaryBeansarraylist = new ArrayList<>();
        messageArrayList = new ArrayList<>();
        commentArrayList = new ArrayList<>();
        handler = new Handler();
        context = getContext();
        linear_photo = rootView.findViewById(R.id.linear_photo);
        linearimage_load = rootView.findViewById(R.id.linearimage_load);
        private_message = rootView.findViewById(R.id.private_message);
        MainLayout = rootView.findViewById(R.id.MainLayout);
        linear_content = rootView.findViewById(R.id.linear_content);
        relativeLayout_Data = rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = rootView.findViewById(R.id.relativeLayout_forceLogin);
        scrollView = rootView.findViewById(R.id.scrollView);

        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();

        edt_message = rootView.findViewById(R.id.edt_message);
        btn_send_message = rootView.findViewById(R.id.btn_send_message);
        txt_sendMessage = rootView.findViewById(R.id.txt_sendMessage);
        txt_AddPhoto = rootView.findViewById(R.id.txt_AddPhoto);
        btn_load_more = rootView.findViewById(R.id.btn_load_more);
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);
        recycler_img_gallary_picker = rootView.findViewById(R.id.recycler_img_gallary_picker);
        rv_viewMessage = rootView.findViewById(R.id.rv_viewMessage);
        rv_viewMessage.setNestedScrollingEnabled(false);
        img_upload = rootView.findViewById(R.id.img_upload);


        txt_sendMessage.setText(defaultLang.get13SendMessage());
        txt_AddPhoto.setText(defaultLang.get13AddPhoto());
        edt_message.setHint(defaultLang.get13TypeYourMessageHere());
        btn_send_message.setText(defaultLang.get13SendMessage());


        linearLayoutManager = new LinearLayoutManager(getActivity());
        rv_viewMessage.setLayoutManager(linearLayoutManager);
        progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar2);


        bundle = new Bundle();


        rv_viewMessage.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Attendee_message attendee_messageObj = message_adapter.getItem(position);

                str_message_id = attendee_messageObj.getRes_id();
                SessionManager.viewImg_tag = "publicMessage";

            }
        }));

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (GlobalData.isNetworkAvailable(getActivity())) {
                messageArrayList.clear();
                page_count = 1;
                isFirstTime = false;
                viewMessage();
            } else {
                ToastC.show(getActivity(), getString(R.string.noInernet));
            }
        });


        btn_send_message.setOnClickListener(v -> {
            if (sessionManager.isLogin()) {
                str_message = edt_message.getText().toString();
                if (str_message.trim().length() == 0) {
                    ToastC.show(getActivity(), "Please Enter Message");
                } else if (gallaryBeansarraylist.size() >= 0 && str_message.trim().length() == 0) {
                    ToastC.show(getActivity(), "Please enter Message First");
                } else {
                    if (GlobalData.isNetworkAvailable(getActivity())) {
                        sessionManager.keyboradHidden(edt_message);
                        sendMessageApi();
                    } else {
                        ToastC.show(getActivity(), getString(R.string.noInernet));
                    }
                }
            } else {
                sessionManager.alertDailogLogin(getActivity());
            }

        });

        img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        linear_photo.setOnClickListener(v -> {

            if (sessionManager.isLogin()) {
                new PickConfig.Builder(getActivity())
                        .pickMode(PickConfig.MODE_MULTIP_PICK)
                        .maxPickSize(10)
                        .spanCount(3)
                        .flag(5)
                        .toolbarColor(R.color.colorPrimary)
                        .build();

            } else {
                sessionManager.alertDailogLogin(getActivity());
            }


        });


        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            viewMessage();

        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                viewMessage();
            }

        }
        getAdvertiesment();
        return rootView;
    }

    private void getPublicMessageListing() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPublicMessageUid, Param.getAllpublic_message(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), page_count), 4, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPublicMessage, Param.getAllpublic_message(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), page_count), 4, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 3, false, this);

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

    private void viewMessage() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            messageArrayList.clear();
            btn_load_more.setVisibility(View.GONE);
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        Log.d("AITL   Oflline", jsonObject.toString());
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                isFirstTime = true;
                if (isFirstTime) {
                    if (GlobalData.checkForUIDVersion())
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPublicMessageUid, Param.getAllpublic_message(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), page_count), 0, false, this);
                    else
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPublicMessage, Param.getAllpublic_message(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), page_count), 0, false, this);
                    isFirstTime = false;
                } else {
                    if (GlobalData.checkForUIDVersion())
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPublicMessageUid, Param.getAllpublic_message(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), page_count), 0, false, this);
                    else
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPublicMessage, Param.getAllpublic_message(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), page_count), 0, false, this);
                }
            } else {
                isFirstTime = true;
                if (isFirstTime) {
                    if (GlobalData.checkForUIDVersion())
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPublicMessageUid, Param.getAllpublic_message(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), page_count), 0, false, this);
                    else
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPublicMessage, Param.getAllpublic_message(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), page_count), 0, false, this);
                    isFirstTime = false;
                } else {
                    if (GlobalData.checkForUIDVersion())
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPublicMessageUid, Param.getAllpublic_message(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), page_count), 0, false, this);
                    else
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPublicMessage, Param.getAllpublic_message(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), page_count), 0, false, this);
                }
            }

        } else {
            messageArrayList.clear();
            btn_load_more.setVisibility(View.GONE);
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        Log.d("AITL    Oflline", jsonObject.toString());
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

    @Override
    public void onResume() {
        super.onResume();

    }

    private void sendMessageApi() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.public_message, Param.public_message(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId(), str_message), 1, true, this);
    }

    private void UploadePhotoAPI(String multipleImg) {
        Log.d("AITL MultipleImageAPI", multipleImg);
        new VolleyRequest(getActivity(), MyUrls.public_images_request, Param.message_img(new File(multipleImg)), Param.public_image_request(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId(), message_id), 2, true, this);
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

                }
            } else {

                message_adapter.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private void loadData(JSONObject json_data) {
        try {
            total_pages = json_data.getInt("total_pages");
            JSONArray jMessageArray = json_data.getJSONArray("messages");
            if (!isLoading) {
                for (int k = 0; k < jMessageArray.length(); k++) {
                    String str_img = "";
                    JSONObject jObjectMessage = jMessageArray.getJSONObject(k);
                    res_id = jObjectMessage.getString("message_id");
                    res_message = jObjectMessage.getString("message");
                    res_senderName = jObjectMessage.getString("Sendername");
                    res_reciverName = jObjectMessage.getString("Recivername");
                    res_senderLogo = jObjectMessage.getString("Senderlogo");
                    res_timeStamp = jObjectMessage.getString("time_stamp");
                    sender_id = jObjectMessage.getString("Sender_id");

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
                        commentArrayList.add(new Attendee_Comment(jObjCommt.getString("comment_id"), jObjCommt.getString("user_id"), jObjCommt.getString("user_name"), jObjCommt.getString("comment"), MyUrls.Imgurl + jObjCommt.getString("Logo"), MyUrls.Imgurl + jObjCommt.getString("image"), jObjCommt.getString("Time"), jObjCommt.getString("time_stamp"), "public_message"));
                    }
                    Log.d("AITL ImageOUT Private", str_img);
                    messageArrayList.add(new Attendee_message(res_id, sender_id, res_message, res_senderName, res_reciverName, res_senderLogo, res_timeStamp, str_img, commentArrayList, "public_message", is_clickable));
                }

            } else {
                ArrayList<Attendee_message> tmp_mArrayList = new ArrayList<>();
                for (int k = 0; k < jMessageArray.length(); k++) {
                    String str_img = "";
                    JSONObject jObjectMessage = jMessageArray.getJSONObject(k);
                    res_id = jObjectMessage.getString("message_id");
                    res_message = jObjectMessage.getString("message");
                    res_senderName = jObjectMessage.getString("Sendername");
                    res_reciverName = jObjectMessage.getString("Recivername");
                    res_senderLogo = jObjectMessage.getString("Senderlogo");
                    res_timeStamp = jObjectMessage.getString("time_stamp");
                    sender_id = jObjectMessage.getString("Sender_id");

                    JSONArray jArrayImg = jObjectMessage.getJSONArray("image");
                    if (jArrayImg.length() != 0) {
                        if ((!jArrayImg.getString(0).toString().equalsIgnoreCase(""))) {
                            str_img = MyUrls.Imgurl + jArrayImg.getString(0).toString();
                            Log.d("AITL Public  Private", str_img);
                        }

                    }

                    JSONArray jArrayCommt = jObjectMessage.getJSONArray("comment");
                    commentArrayList = new ArrayList<>();
                    for (int z = 0; z < jArrayCommt.length(); z++) {
                        JSONObject jObjCommt = jArrayCommt.getJSONObject(z);
                        commentArrayList.add(new Attendee_Comment(jObjCommt.getString("comment_id"), jObjCommt.getString("user_id"), jObjCommt.getString("user_name"), jObjCommt.getString("comment"), MyUrls.Imgurl + jObjCommt.getString("Logo"), MyUrls.Imgurl + jObjCommt.getString("image"), jObjCommt.getString("Time"), jObjCommt.getString("time_stamp"), "public_message"));

                    }
                    Log.d("AITL ImageOUT Private", str_img);
                    tmp_mArrayList.add(new Attendee_message(res_id, sender_id, res_message, res_senderName, res_reciverName, res_senderLogo, res_timeStamp, str_img, commentArrayList, "public_message", is_clickable));
                }
                messageArrayList.addAll(tmp_mArrayList);
            }
            set_recycler();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void set_recycler() {

        try {
            if (!isLoading) {

                if (messageArrayList.size() == 0) {
                    rv_viewMessage.setVisibility(View.GONE);
                } else {
                    rv_viewMessage.setVisibility(View.VISIBLE);
                    message_adapter = new Attendee_message_Adapter(messageArrayList, rv_viewMessage, getActivity(), linearLayoutManager, getActivity(), scrollView);
                    rv_viewMessage.setAdapter(message_adapter);

                }
            } else {

                message_adapter = new Attendee_message_Adapter(messageArrayList, rv_viewMessage, getActivity(), linearLayoutManager, getActivity(), scrollView);
                rv_viewMessage.setAdapter(message_adapter);
                message_adapter.setLoaded();
                isLoading = false;
            }

            if (messageArrayList.size() != 0)
                message_adapter.setOnLoadMoreListener(new OnLoadMoreListener() {

                    @Override
                    public void onLoadMore() {

                        page_count++;

                        if (page_count <= total_pages) {

//                            message_adapter.addFooter();
                            progressBar2.setVisibility(View.VISIBLE);
                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {

//                                    message_adapter.removeFooter();
                                    progressBar2.setVisibility(View.GONE);
                                    isLoading = true;


                                    try {
                                        if (GlobalData.isNetworkAvailable(getActivity()))
                                            getPublicMessageListing();
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

                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        messageArrayList.clear();
                        mSwipeRefreshLayout.setRefreshing(false);
                        JSONObject json_data = jsonObject.getJSONObject("data");
                        if (sqLiteDatabaseHandler.isAttendeeListExist(sessionManager.getEventId(), sessionManager.getUserId(), tag)) {
                            sqLiteDatabaseHandler.deleteListingData(sessionManager.getEventId(), tag, sessionManager.getUserId());
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), json_data.toString(), tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), json_data.toString(), tag);
                        }
                        loadData(json_data);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonmessage = new JSONObject(volleyResponse.output);
                    if (jsonmessage.getString("success").equalsIgnoreCase("true")) {
                        message_id = jsonmessage.getString("message_id");

                        Log.d("AITL MessageId", message_id);
                        if (gallaryBeansarraylist.size() == 0) {
                            edt_message.setText("");
                            GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                            ((MainActivity) getActivity()).reloadFragment();
                        } else {
                            for (int j = 0; j < gallaryBeansarraylist.size(); j++) {
                                isLast = j + 1;
                                UploadePhotoAPI(gallaryBeansarraylist.get(j).getImages());
                                Log.d("AITL arrayList SiZe", "" + gallaryBeansarraylist.size());
                                Log.d("UploadRecipePhotoAPI", gallaryBeansarraylist.get(j).getImages());
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

                        Log.d("AITL IMAGE", "ARRAYLIST GET CLEARED");

                        edt_message.setText("");
                        gallaryBeansarraylist.clear();
                        linearimage_load.setVisibility(View.GONE);

                        GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                        ((MainActivity) getActivity()).reloadFragment();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 4:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        JSONObject json_data = jsonObject.getJSONObject("data");

                        loadData(json_data);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 3:
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

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 5, false, this);
        }
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
