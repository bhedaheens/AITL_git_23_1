package com.allintheloop.Fragment.PrivateMessage;


import android.Manifest;
import android.content.ContentValues;
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
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Adapter.ChatRoomAdapter;
import com.allintheloop.Adapter.GallaryAdepter;
import com.allintheloop.Bean.Customer;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.GallaryBean;
import com.allintheloop.Bean.Message;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.BitmapLoader;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import me.crosswall.photo.pick.PickConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivateMessageDetail_Fragment extends Fragment implements VolleyInterface {


    private static final int RESULT_OK = -1;
    public static LinearLayout linearimage_load;
    public static RecyclerView recycler_img_gallary_picker;
    public static ArrayList<String> selectImages;
    public static ArrayList<GallaryBean> gallaryBeansarraylist;
    public static int counter = 0;
    static Context context;
    static GallaryAdepter gallaryAdepter;
    private static int RESULT_LOAD_IMAGE = 5;
    private static int SELECT_PHOTO = 101;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    String message, product_id;
    WrapContentLinearLayoutManager linearLayoutManager;
    int page_count = 1, total_pages;
    boolean isLoading;
    Handler handler;
    TextView txt_profileName;
    ImageView imgBack;
    CircleImageView imgProduct;
    SessionManager sessionManager;
    String res_id, res_message, res_senderName, res_reciverName, res_senderLogo, res_timeStamp, sender_id, is_clickable, str_desiredRecevier = "";
    TextView txt_userName, txt_companyName;
    CircleImageView img_profile;
    Button btn_viewProfile, btn_addPhoto;
    ImageView img_addphotobtn;
    String str_message;
    String str_senderID = "", senderName = "", senderCompanyName = "", senderTitle = "", senderLogo = "", moduleType = "", senderPageId = "";
    LinearLayout linear_photo;
    JSONArray jArraymessageId;
    ContentValues values;
    Uri imageUri;
    Bitmap thumbnail;
    String picturePath = " ";
    Bitmap bitmapImage = null;
    int isLast = 0;
    ProgressBar img_proress;
    DefaultLanguage.DefaultLang defaultLang;
    List<String> permissionsNeeded;
    List<String> permissionsList;
    private RecyclerView recyclerView;
    private ChatRoomAdapter chatRoomAdapter;
    private ArrayList<Message> messageArrayList;
    private EditText inputMessage;
    private Button btnSend;


    public PrivateMessageDetail_Fragment() {
        // Required empty public constructor
    }

    public static void selectimage(String images) {

        gallaryBeansarraylist.add(new GallaryBean(images, "private_message"));


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
        View rootView = inflater.inflate(R.layout.fragment_private_message_detail, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        inputMessage = (EditText) rootView.findViewById(R.id.message);
        btnSend = (Button) rootView.findViewById(R.id.btn_send);
        txt_userName = (TextView) rootView.findViewById(R.id.txt_userName);
        txt_companyName = (TextView) rootView.findViewById(R.id.txt_companyName);
        txt_profileName = (TextView) rootView.findViewById(R.id.txt_profileName);
        btn_viewProfile = (Button) rootView.findViewById(R.id.btn_viewProfile);
        img_addphotobtn = (ImageView) rootView.findViewById(R.id.img_addphotobtn);
        btn_addPhoto = (Button) rootView.findViewById(R.id.btn_addPhoto);
        img_proress = (ProgressBar) rootView.findViewById(R.id.img_proress);
        img_profile = (CircleImageView) rootView.findViewById(R.id.img_profile);
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        linearLayoutManager = new WrapContentLinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        handler = new Handler();
        recyclerView.setLayoutManager(linearLayoutManager);
        messageArrayList = new ArrayList<>();

        linear_photo = (LinearLayout) rootView.findViewById(R.id.linear_photo);
        inputMessage.setMaxLines(2);
        inputMessage.setVerticalScrollBarEnabled(true);
        inputMessage.setMovementMethod(new ScrollingMovementMethod());
        inputMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;

            }
        });
        img_addphotobtn.setColorFilter(getResources().getColor(R.color.white));

        sessionManager.strModuleId = sessionManager.private_senderId;
//        sessionManager.setMenuid("12");
        sessionManager.strMenuId = "12";

        selectImages = new ArrayList<>();
        gallaryBeansarraylist = new ArrayList<>();
        context = getContext();
        recycler_img_gallary_picker = rootView.findViewById(R.id.recycler_img_gallary_picker);
        linearimage_load = rootView.findViewById(R.id.linearimage_load);

        btn_viewProfile.setText(defaultLang.get12ViewProfile());
        inputMessage.setHint(defaultLang.get12TypeYourMessageHere());
        btn_addPhoto.setText(defaultLang.get12AddPhoto());
        btnSend.setText(defaultLang.get12Send());

        btn_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchPhotoFromCamera();
//                getVideos();
            }
        });


        linear_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchPhotoFromCamera();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManager.isLogin()) {


                    str_message = inputMessage.getText().toString().trim();

                    if (str_message.equalsIgnoreCase("")) {
                        Log.d("AITL MESSAGE", "EMPTY");
                        if (gallaryBeansarraylist.size() == 0) {
                            ToastC.show(getActivity(), "Please select Image or Enter Message");
                        } else {
                            if (GlobalData.isNetworkAvailable(getActivity())) {
                                for (int j = 0; j < gallaryBeansarraylist.size(); j++) {
                                    isLast = j + 1;
                                    UploadePhotoAPI(gallaryBeansarraylist.get(j).getImages());
                                }
                            } else {
                                ToastC.show(getActivity(), getString(R.string.noInernet));
                            }
                        }
                    } else {

                        if (str_message.trim().length() == 0) {
                            ToastC.show(getActivity(), "Please Enter Message");
                        } else {
                            if (GlobalData.isNetworkAvailable(getActivity())) {
                                sessionManager.keyboradHidden(inputMessage);
                                sendMessageApi();
                            } else {
                                ToastC.show(getActivity(), "No Internet Connection");
                            }
                        }
                    }
                } else {
                    sessionManager.alertDailogLogin(getActivity());
                }
            }
        });

        btn_viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("AITL BUTTON CLICK ", "CLICK" + moduleType);

                if (moduleType.equalsIgnoreCase("2")) {
                    SessionManager.AttenDeeId = str_senderID;
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Attendance_Detail_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else if (moduleType.equalsIgnoreCase("3")) {
                    sessionManager.exhibitor_id = str_senderID;
                    sessionManager.exhi_pageId = senderPageId;
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else if (moduleType.equalsIgnoreCase("7")) {
                    SessionManager.speaker_id = str_senderID;
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Speaker_Detail_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else if (moduleType.equalsIgnoreCase("43")) {
                    sessionManager.sponsor_id = str_senderID;
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Sponsor_Detail_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                }
            }
        });


        if (GlobalData.isNetworkAvailable(getActivity())) {
            getPrivateMessageListing();
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }

        return rootView;
    }

    private void fetchPhotoFromCamera() {
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

    private void UploadePhotoAPI(String multipleImg) {
        Log.d("AITL MultipleImageAPI", multipleImg);
        new VolleyRequest(getActivity(), MyUrls.privateMessageSendImage, Param.message_img(new File(multipleImg)), Param.private_image_request(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getToken(), sessionManager.private_senderId), 3, true, this);
    }

    private void sendMessageApi() {
        if (GlobalData.checkForUIDVersion())
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.private_messageSendtextUid, Param.private_messageSend(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getToken(), str_message, sessionManager.private_senderId), 2, true, this);
        else
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.private_messageSendtext, Param.private_messageSend(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getToken(), str_message, sessionManager.private_senderId), 2, true, this);
    }

    private void getPrivateMessageListing() {
        if (sessionManager.isLogin()) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPrivateUnreadMessageSenderwise, Param.getAllprivateMessageSenderWise(sessionManager.getEventId(), sessionManager.getUserId(), page_count, sessionManager.getToken(), sessionManager.private_senderId), 0, false, this);

        } else {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPrivateUnreadMessageSenderwise, Param.getAllprivateMessageSenderWise(sessionManager.getEventId(), "", page_count, sessionManager.getToken(), sessionManager.private_senderId), 0, false, this);
        }
    }

    private void getPrivateMessageListingonButton() {
        if (sessionManager.isLogin()) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPrivateUnreadMessageSenderwise, Param.getAllprivateMessageSenderWise(sessionManager.getEventId(), sessionManager.getUserId(), page_count, sessionManager.getToken(), sessionManager.private_senderId), 1, false, this);

        } else {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPrivateUnreadMessageSenderwise, Param.getAllprivateMessageSenderWise(sessionManager.getEventId(), "", page_count, sessionManager.getToken(), sessionManager.private_senderId), 1, false, this);
        }
    }

    private void getLoadData(JSONObject json_data) {
        try {

            total_pages = json_data.getInt("total_pages");
            JSONArray jArraySenderDetail = json_data.getJSONArray("sender_detail");
            for (int i = 0; i < jArraySenderDetail.length(); i++) {
                JSONObject jObjectSender = jArraySenderDetail.getJSONObject(0);
//                        JSONObject jObjectSender = json_data.getJSONObject("sender_detail");
                str_senderID = jObjectSender.getString("id");
                senderName = jObjectSender.getString("Sender_name");
                senderCompanyName = jObjectSender.getString("Company_name");
                senderTitle = jObjectSender.getString("Title");
                senderLogo = jObjectSender.getString("logo");
                moduleType = jObjectSender.getString("module_type");
                senderPageId = jObjectSender.getString("Page_id");
            }


            GradientDrawable drawable = new GradientDrawable();
            Random rnd = new Random();

            txt_userName.setText(senderName);
            if (!(senderTitle.equalsIgnoreCase(""))) {
                if (!(senderCompanyName.equalsIgnoreCase(""))) {
                    txt_companyName.setVisibility(View.VISIBLE);
                    txt_companyName.setText(senderTitle + " at " + senderCompanyName);
                }
            } else {
                txt_companyName.setVisibility(View.VISIBLE);
            }

            Log.d("AITL LOGO FRAGMENT", "LOGO " + senderLogo);
            if (senderLogo.equalsIgnoreCase("")) {
                img_proress.setVisibility(View.GONE);
                img_profile.setVisibility(View.GONE);
                txt_profileName.setVisibility(View.VISIBLE);

                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                if (!(senderName.equalsIgnoreCase(""))) {
                    txt_profileName.setText("" + senderName.charAt(0));
                } else {
                    txt_profileName.setText("#");
                }
                if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                    drawable.setShape(GradientDrawable.OVAL);
                    drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                    txt_profileName.setBackgroundDrawable(drawable);
                    txt_profileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                } else {
                    drawable.setShape(GradientDrawable.OVAL);
                    drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                    txt_profileName.setBackgroundDrawable(drawable);
                    txt_profileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                }

            } else {
                Glide.with(getActivity())
                        .load(MyUrls.Imgurl + senderLogo)
                        .skipMemoryCache(false)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                img_proress.setVisibility(View.GONE);
                                img_profile.setVisibility(View.VISIBLE);
                                txt_profileName.setVisibility(View.GONE);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                img_proress.setVisibility(View.GONE);
                                img_profile.setVisibility(View.VISIBLE);
                                txt_profileName.setVisibility(View.GONE);

                                return false;
                            }
                        })
                        .thumbnail(0.7f)
                        .into(img_profile);
            }
            loadData(json_data);

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
                        JSONObject json_data = jsonObject.getJSONObject("data");
                        getLoadData(json_data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject json_data = jsonObject.getJSONObject("data");
                        loadData(json_data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {

                    JSONObject jsonmessage = new JSONObject(volleyResponse.output);

                    if (jsonmessage.getString("success").equalsIgnoreCase("true")) {
                        if (gallaryBeansarraylist.size() == 0) {
                            inputMessage.setText("");
                            messageArrayList.clear();
                            JSONObject json_data = jsonmessage.getJSONObject("data");
                            getLoadData(json_data);
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

            case 3:
                try {

                    JSONObject imgupload = new JSONObject(volleyResponse.output);
                    Log.d("AITL UploadImg", imgupload.toString());
                    if (isLast == gallaryBeansarraylist.size()) {
                        Log.d("AITL IMAGE", "ARRAYLIST GET CLEARED");
                        inputMessage.setText("");
                        gallaryBeansarraylist.clear();
                        linearimage_load.setVisibility(View.GONE);
                        messageArrayList.clear();
                        JSONObject json_data = imgupload.getJSONObject("data");
                        getLoadData(json_data);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private void loadData(JSONObject json_data) {
        try {

            JSONArray jMessageArray = json_data.getJSONArray("messages");
            if (!isLoading) {
                for (int k = 0; k < jMessageArray.length(); k++) {
                    String str_img = "";
                    JSONObject jObjectMessage = jMessageArray.getJSONObject(k);
                    res_id = jObjectMessage.getString("message_id");
                    res_message = jObjectMessage.getString("message");
                    res_senderName = jObjectMessage.getString("Sendername");
                    res_reciverName = jObjectMessage.getString("Recivername");
                    res_senderLogo = jObjectMessage.getString("image");
                    Log.d("AITL ", "FRAGMENT IMAGE  " + res_senderLogo);
                    res_timeStamp = jObjectMessage.getString("time_stamp");
                    str_desiredRecevier = jObjectMessage.getString("desired_receiver");
                    sender_id = jObjectMessage.getString("Sender_id");
                    is_clickable = jObjectMessage.getString("is_clickable");
                    messageArrayList.add(new Message(res_id, res_message, res_timeStamp, new Customer(sender_id, res_senderName, ""), "0", "0", res_senderLogo, is_clickable));
                }

            } else {
                ArrayList<Message> tmp_mArrayList = new ArrayList<>();
                for (int k = 0; k < jMessageArray.length(); k++) {
                    String str_img = "";
                    JSONObject jObjectMessage = jMessageArray.getJSONObject(k);
                    res_id = jObjectMessage.getString("message_id");
                    res_message = jObjectMessage.getString("message");
                    res_senderName = jObjectMessage.getString("Sendername");
                    res_reciverName = jObjectMessage.getString("Recivername");
                    res_senderLogo = jObjectMessage.getString("image");
                    res_timeStamp = jObjectMessage.getString("time_stamp");
                    sender_id = jObjectMessage.getString("Sender_id");
                    str_desiredRecevier = jObjectMessage.getString("desired_receiver");
                    is_clickable = jObjectMessage.getString("is_clickable");

                    tmp_mArrayList.add(new Message(res_id, res_message, res_timeStamp, new Customer(sender_id, res_senderName, ""), "0", "0", res_senderLogo, is_clickable));
                }
                messageArrayList.addAll(tmp_mArrayList);
            }
            set_recycler();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void set_recycler() {

        if (!isLoading) {

            Log.i("AAKASH", "IF LOAD " + isLoading + "");

            chatRoomAdapter = new ChatRoomAdapter(recyclerView, linearLayoutManager, messageArrayList, sessionManager.getUserId(), true, getActivity());
            recyclerView.setAdapter(chatRoomAdapter);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {

                    recyclerView.smoothScrollToPosition(0);
                }
            });
        } else {

            Log.i("AAKASH", "ELSE LOAD " + isLoading + "");
            chatRoomAdapter.notifyDataSetChanged();
            chatRoomAdapter.setLoaded();
            isLoading = false;
        }


        chatRoomAdapter.setOnLoadMoreListener(() -> {

            page_count++;

            if (page_count <= total_pages) {

                chatRoomAdapter.addFooter();

                handler.postDelayed(() -> {
                    //   remove progress item
                    Log.i("AAKASH", "BEFORE LOAD" + isLoading + "");

                    chatRoomAdapter.removeFooter();
                    //add items one by one

                    isLoading = true;
                    Log.i("AAKASH", "AFTER LOAD" + isLoading + "");

                    try {
                        if (GlobalData.isNetworkAvailable(getActivity()))
                            getPrivateMessageListingonButton();
                        else
                            Toast.makeText(getActivity(), getString(R.string.noInernet), Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }, 2000);
            }
        });
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

        String[] item = {defaultLang.get12Gallery(), defaultLang.get12Camera()};
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(defaultLang.get12ChooseFrom())
                .items(item)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (which == 0) {
                            new PickConfig.Builder(getActivity())
                                    .pickMode(PickConfig.MODE_MULTIP_PICK)
                                    .maxPickSize(10)
                                    .spanCount(3)
                                    .flag(3)
                                    .toolbarColor(R.color.colorPrimary)
                                    .build();

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
