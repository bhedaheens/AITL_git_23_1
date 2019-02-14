package com.allintheloop.Fragment.PrivateMessage;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.bumptech.glide.Glide;
import com.allintheloop.Adapter.Attendee.Attendee_message_Adapter;
import com.allintheloop.Adapter.GallaryAdepter;
import com.allintheloop.Adapter.PrivateMessage_Sppr_Adapter;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.Attendee.Attendee_Comment;
import com.allintheloop.Bean.Attendee.Attendee_message;
import com.allintheloop.Bean.GallaryBean;
import com.allintheloop.Bean.PrivateMessage_Sppiner;
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

import java.util.ArrayList;

import me.crosswall.photo.pick.PickConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class Private_Message_Fragment extends Fragment implements VolleyInterface {


    TextView textViewNoDATA;
    SessionManager sessionManager;


    String str_message, str_img = "";

    public static LinearLayout linearimage_load;
    LinearLayout linear_photo;
    LinearLayout private_message;
    EditText edt_message;
    ImageView img_upload;

    //  Spinner spn_user;
    public static TextView spn_user;
    TextView txt_Msgname;

    static Context context;
    public static RecyclerView rv_viewUser, recycler_img_gallary_picker;
    RecyclerView rv_viewMessage;

    ArrayList<String> list_speName;
    ArrayList<String> list_speiD;
    public static ArrayList<String> selectImages;
    public static ArrayList<GallaryBean> gallaryBeansarraylist;
    public static ArrayList<PrivateMessage_Sppiner> sppinerArrayList;

    int isLast = 0;

    ArrayList<Attendee_message> messageArrayList;
    ArrayList<Attendee_Comment> commentArrayList;
    public static Attendee_message_Adapter message_adapter;
    LinearLayoutManager linearLayoutManager;

    String res_id, res_message, res_senderName, res_reciverName, res_senderLogo, res_timeStamp, sender_id, is_clickable, str_desiredRecevier = "";
    String sp_fullname, spat_id;
    Button btn_send_message, btn_load_more;
    int total_pages, page_count = 1;
    boolean isFirstTime = false;
    boolean btn_load;

    public static JSONArray arrayReqid;
    JSONArray jArraymessageId;

    static GallaryAdepter gallaryAdepter;
    public static PrivateMessage_Sppr_Adapter spprAdapter;
    public static String str_message_id;
    private static final int RESULT_OK = -1;
    public static int counter = 0;
    String picturePath = " ";
    Bitmap bitmapImage = null;


    ImageView headerAdd_image, footerAdd_image, header_btndelete, footer_btndelete;
    WebView headerAdd_webView, footerAdd_webView;
//    FrameLayout linear_header,linear_footer;

    String header_adsense, footer_adsense, header_link, footer_link;
    Bundle bundle;

    ContentValues values;
    Uri imageUri;
    Bitmap thumbnail;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String tag = "PrivateMessageListing";
    String adverties_id = "";

    boolean isLoading;
    Handler handler;
    NestedScrollView scrollView;
    ProgressBar progressBar2;

    public Private_Message_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_private__message, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        sessionManager = new SessionManager(getActivity());
        handler = new Handler();
        list_speName = new ArrayList<>();
        list_speiD = new ArrayList<>();
        selectImages = new ArrayList<>();
        gallaryBeansarraylist = new ArrayList<>();
        sppinerArrayList = new ArrayList<>();
        context = getContext();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        recycler_img_gallary_picker = (RecyclerView) rootView.findViewById(R.id.recycler_img_gallary_picker);
        rv_viewMessage = (RecyclerView) rootView.findViewById(R.id.rv_viewMessage);
        rv_viewMessage.setNestedScrollingEnabled(false);
        rv_viewUser = (RecyclerView) rootView.findViewById(R.id.rv_viewUser);
        img_upload = (ImageView) rootView.findViewById(R.id.img_upload);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        btn_load_more = (Button) rootView.findViewById(R.id.btn_load_more);
        btn_send_message = (Button) rootView.findViewById(R.id.btn_send_message);
        edt_message = (EditText) rootView.findViewById(R.id.edt_message);
//        spn_user = (Spinner) rootView.findViewById(R.id.spn_user);
        spn_user = (TextView) rootView.findViewById(R.id.spn_user);
        txt_Msgname = (TextView) rootView.findViewById(R.id.txt_Msgname);
        messageArrayList = new ArrayList<>();
        commentArrayList = new ArrayList<>();
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rv_viewMessage.setLayoutManager(linearLayoutManager);
        progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        linear_photo = (LinearLayout) rootView.findViewById(R.id.linear_photo);
        linearimage_load = (LinearLayout) rootView.findViewById(R.id.linearimage_load);
        private_message = (LinearLayout) rootView.findViewById(R.id.private_message);

        headerAdd_image = (ImageView) rootView.findViewById(R.id.headerAdd_image);
        footerAdd_image = (ImageView) rootView.findViewById(R.id.footerAdd_image);
        header_btndelete = (ImageView) rootView.findViewById(R.id.header_btndelete);
        footer_btndelete = (ImageView) rootView.findViewById(R.id.footer_btndelete);
        headerAdd_webView = (WebView) rootView.findViewById(R.id.headerAdd_webView);
        footerAdd_webView = (WebView) rootView.findViewById(R.id.footerAdd_webView);

        bundle = new Bundle();

        footer_btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                footerAdd_image.setVisibility(View.GONE);
                footer_btndelete.setVisibility(View.GONE);
            }
        });

        header_btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headerAdd_image.setVisibility(View.GONE);
                header_btndelete.setVisibility(View.GONE);
            }
        });

        headerAdd_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLogin()) {
                    advertiesClick();
                }
                bundle.putString("Social_url", header_link);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);

                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);
            }
        });

        footerAdd_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sessionManager.isLogin()) {
                    advertiesClick();
                }

                bundle.putString("Social_url", footer_link);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);
            }
        });

        arrayReqid = new JSONArray();
        if (sessionManager.isLogin()) {
            textViewNoDATA.setVisibility(View.GONE);
            private_message.setVisibility(View.VISIBLE);
            txt_Msgname.setVisibility(View.VISIBLE);

        } else {
            txt_Msgname.setVisibility(View.GONE);
            private_message.setVisibility(View.VISIBLE);
            textViewNoDATA.setVisibility(View.VISIBLE);
            textViewNoDATA.setText("Login or Sign Up to proceed. To sign up or login tap the Sign Up button on the top right of the screen.");
            textViewNoDATA.setTextSize(15);

        }

        rv_viewMessage.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Attendee_message attendee_messageObj = message_adapter.getItem(position);

                str_message_id = attendee_messageObj.getRes_id();
                SessionManager.viewImg_tag = "privateMessage";

            }
        }));


        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLogin()) {
                    str_message = edt_message.getText().toString();

                    if (str_message.trim().length() == 0) {
                        ToastC.show(getActivity(), "Please Enter Message");
                    } else if (arrayReqid.length() == 0) {
                        ToastC.show(getActivity(), "Please Select User ");
                    } else if (gallaryBeansarraylist.size() >= 0 && str_message.trim().length() == 0) {
                        ToastC.show(getActivity(), "Please enter Message First");
                    } else {
                        if (GlobalData.isNetworkAvailable(getActivity())) {
                            sessionManager.keyboradHidden(edt_message);
                            sendMessageApi();
                        } else {
                            ToastC.show(getActivity(), "No Internet Connection");
                        }
                    }
                } else {
                    sessionManager.alertDailogLogin(getActivity());
                }


            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    messageArrayList.clear();
                    page_count = 1;
                    isFirstTime = false;
                    viewMessageApi();
                }
            }
        });

        spn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManager.isLogin()) {
                    if (GlobalData.isNetworkAvailable(getActivity())) {
                        FragmentManager fm = getFragmentManager();
                        PrivateMessageSpeakerAndAttendeeList fragment = new PrivateMessageSpeakerAndAttendeeList();
                        fragment.show(fm, "DialogFragment");
                    } else {
                        ToastC.show(getActivity(), getString(R.string.noInernet));
                    }
                } else {
                    sessionManager.alertDailogLogin(getActivity());
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
                    values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1);
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
                            .flag(3)
                            .toolbarColor(R.color.colorPrimary)
                            .build();
                } else {
                    sessionManager.alertDailogLogin(getActivity());
                }
            }
        });

//        btn_load_more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (page_count == total_pages) {
//                    btn_load_more.setVisibility(View.GONE);
//                }
//                page_count++;
//              getPrivateMessageListing();
//
//            }
//        });


        if (sessionManager.isLogin()) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 4, false, this);
            if (GlobalData.isNetworkAvailable(getActivity())) {

                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPrivateSpeaker, Param.getAllprivateSpeaker(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken()), 1, true, this);
            }
        }

        viewMessageApi();

        return rootView;
    }


    private void getPrivateMessageListing() {
        if (sessionManager.isLogin()) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPrivateMessage, Param.getAllprivateMessage(sessionManager.getEventId(), sessionManager.getUserId(), page_count, sessionManager.getToken()), 5, false, this);

        } else {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPrivateMessage, Param.getAllprivateMessage(sessionManager.getEventId(), "", page_count, sessionManager.getToken()), 5, false, this);
        }
    }


    private void advertiesClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", adverties_id, "AD", ""), 6, true, this);
        }
    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 6, false, this);
        }
    }

    private void viewMessageApi() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            messageArrayList.clear();

            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (sessionManager.isLogin()) {
                    isFirstTime = true;
                    if (isFirstTime) {
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPrivateMessage, Param.getAllprivateMessage(sessionManager.getEventId(), sessionManager.getUserId(), page_count, sessionManager.getToken()), 0, false, this);
                        isFirstTime = false;
                    } else {
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPrivateMessage, Param.getAllprivateMessage(sessionManager.getEventId(), sessionManager.getUserId(), page_count, sessionManager.getToken()), 0, false, this);
                    }
                } else {
                    isFirstTime = true;
                    if (isFirstTime) {
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPrivateMessage, Param.getAllprivateMessage(sessionManager.getEventId(), "", page_count, sessionManager.getToken()), 0, false, this);
                        isFirstTime = false;
                    } else {
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPrivateMessage, Param.getAllprivateMessage(sessionManager.getEventId(), "", page_count, sessionManager.getToken()), 0, false, this);
                    }
                }
            } else {
                if (sessionManager.isLogin()) {
                    isFirstTime = true;
                    if (isFirstTime) {
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPrivateMessage, Param.getAllprivateMessage(sessionManager.getEventId(), sessionManager.getUserId(), page_count, sessionManager.getToken()), 0, true, this);
                        isFirstTime = false;
                    } else {
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPrivateMessage, Param.getAllprivateMessage(sessionManager.getEventId(), sessionManager.getUserId(), page_count, sessionManager.getToken()), 0, true, this);
                    }
                } else {
                    isFirstTime = true;
                    if (isFirstTime) {
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPrivateMessage, Param.getAllprivateMessage(sessionManager.getEventId(), "", page_count, sessionManager.getToken()), 0, true, this);
                        isFirstTime = false;
                    } else {
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPrivateMessage, Param.getAllprivateMessage(sessionManager.getEventId(), "", page_count, sessionManager.getToken()), 0, true, this);
                    }
                }
            }


        } else {
            messageArrayList.clear();
            btn_load_more.setVisibility(View.GONE);
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
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

                        try {
                            if (page_count <= total_pages) {

//                                message_adapter.addFooter();
                                progressBar2.setVisibility(View.VISIBLE);
                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

//                                        message_adapter.removeFooter();
                                        progressBar2.setVisibility(View.GONE);
                                        isLoading = true;

                                        try {
                                            if (GlobalData.isNetworkAvailable(getActivity()))
                                                getPrivateMessageListing();
                                            else
                                                Toast.makeText(getActivity(), getString(R.string.noInernet), Toast.LENGTH_SHORT).show();
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, 2000);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    str_desiredRecevier = jObjectMessage.getString("desired_receiver");
                    sender_id = jObjectMessage.getString("Sender_id");
                    is_clickable = jObjectMessage.getString("is_clickable");
                    JSONArray jArrayImg = jObjectMessage.getJSONArray("image");
                    if (jArrayImg.length() != 0) {
                        if ((!jArrayImg.getString(0).toString().equalsIgnoreCase(""))) {
                            str_img = MyUrls.Imgurl + jArrayImg.getString(0).toString();

                        }

                    }
                    JSONArray jArrayCommt = jObjectMessage.getJSONArray("comment");
                    commentArrayList = new ArrayList<>();
                    for (int z = 0; z < jArrayCommt.length(); z++) {
                        JSONObject jObjCommt = jArrayCommt.getJSONObject(z);
                        commentArrayList.add(new Attendee_Comment(jObjCommt.getString("comment_id"), jObjCommt.getString("user_id"), jObjCommt.getString("user_name"), jObjCommt.getString("comment"), MyUrls.Imgurl + jObjCommt.getString("Logo"), MyUrls.Imgurl + jObjCommt.getString("image"), jObjCommt.getString("Time"), jObjCommt.getString("time_stamp"), "private_message"));

                    }

                    messageArrayList.add(new Attendee_message(res_id, sender_id, res_message, res_senderName, res_reciverName, res_senderLogo, res_timeStamp, str_img, commentArrayList, "private_message", is_clickable, str_desiredRecevier));
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
                    str_desiredRecevier = jObjectMessage.getString("desired_receiver");
                    is_clickable = jObjectMessage.getString("is_clickable");
                    JSONArray jArrayImg = jObjectMessage.getJSONArray("image");
                    if (jArrayImg.length() != 0) {
                        if ((!jArrayImg.getString(0).toString().equalsIgnoreCase(""))) {
                            str_img = MyUrls.Imgurl + jArrayImg.getString(0).toString();

                        }

                    }
                    JSONArray jArrayCommt = jObjectMessage.getJSONArray("comment");
                    commentArrayList = new ArrayList<>();
                    for (int z = 0; z < jArrayCommt.length(); z++) {
                        JSONObject jObjCommt = jArrayCommt.getJSONObject(z);
                        commentArrayList.add(new Attendee_Comment(jObjCommt.getString("comment_id"), jObjCommt.getString("user_id"), jObjCommt.getString("user_name"), jObjCommt.getString("comment"), MyUrls.Imgurl + jObjCommt.getString("Logo"), MyUrls.Imgurl + jObjCommt.getString("image"), jObjCommt.getString("Time"), jObjCommt.getString("time_stamp"), "private_message"));

                    }
                    tmp_mArrayList.add(new Attendee_message(res_id, sender_id, res_message, res_senderName, res_reciverName, res_senderLogo, res_timeStamp, str_img, commentArrayList, "private_message", is_clickable, str_desiredRecevier));
                }
                messageArrayList.addAll(tmp_mArrayList);
            }
            set_recycler();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendMessageApi() {

        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.private_messageSend, Param.private_messageSend(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getToken(), str_message, arrayReqid.toString()), 2, true, this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
                    linearimage_load.setVisibility(View.VISIBLE);
                    picturePath = getRealPathFromURI(imageUri);
                    thumbnail = BitmapLoader.loadBitmap(picturePath, 100, 100);

                    selectImages.add(picturePath);
                    gallaryBeansarraylist.clear();
                    for (int j = 0; j < selectImages.size(); j++) {
                        selectimage(selectImages.get(j).toString());
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {

                message_adapter.onActivityResult(requestCode, resultCode, data);
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
                    JSONObject jObjectlist = new JSONObject(volleyResponse.output);
                    if (jObjectlist.getString("success").equalsIgnoreCase("true")) {
                        JSONArray jArraySpeaker = jObjectlist.getJSONArray("speakers");
                        for (int sp = 0; sp < jArraySpeaker.length(); sp++) {
                            JSONObject jObjectspeaker = jArraySpeaker.getJSONObject(sp);
                            sp_fullname = jObjectspeaker.getString("Firstname") + " " + jObjectspeaker.getString("Lastname");
                            spat_id = jObjectspeaker.getString("Id");
                            list_speName.add(sp_fullname);
                            list_speiD.add(spat_id);
                        }

                        list_speName.add("AITL");
                        list_speiD.add("0");

                        JSONArray jArrayattndee = jObjectlist.getJSONArray("attendees");
                        for (int at = 0; at < jArrayattndee.length(); at++) {
                            JSONObject jObjectattendee = jArrayattndee.getJSONObject(at);
                            sp_fullname = jObjectattendee.getString("Firstname") + " " + jObjectattendee.getString("Lastname");
                            spat_id = jObjectattendee.getString("Id");
                            list_speName.add(sp_fullname);
                            list_speiD.add(spat_id);
                        }
                    } else {
                        ToastC.show(getActivity(), jObjectlist.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 2:
                try {

                    JSONObject jsonmessage = new JSONObject(volleyResponse.output);

                    if (jsonmessage.getString("success").equalsIgnoreCase("true")) {

                        JSONArray jArraymessage = jsonmessage.getJSONArray("message_id");
                        jArraymessageId = new JSONArray();
                        for (int id = 0; id < jArraymessage.length(); id++) {
                            jArraymessageId.put(jArraymessage.getString(id));
                        }
                        if (gallaryBeansarraylist.size() == 0) {

                            edt_message.setText("");
                            spn_user.setHint("Select");
                            sppinerArrayList.clear();
                            GlobalData.CURRENT_FRAG = GlobalData.PRIVATE_MESSAGE;
                            ((MainActivity) getActivity()).reloadFragment();
                        } else {

                            for (int j = 0; j < gallaryBeansarraylist.size(); j++) {

                                isLast = j + 1;
//                                UploadePhotoAPI(gallaryBeansarraylist.get(j).getImages());
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


                    if (isLast == gallaryBeansarraylist.size()) {

                        edt_message.setText("");
                        gallaryBeansarraylist.clear();
                        spn_user.setHint("Select");
                        sppinerArrayList.clear();
                        linearimage_load.setVisibility(View.GONE);
                        GlobalData.CURRENT_FRAG = GlobalData.PRIVATE_MESSAGE;
                        ((MainActivity) getActivity()).reloadFragment();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (sessionManager.isLogin()) {
                        pagewiseClick();
                    }
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {


                        if (jsonObject.has("data")) {
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            adverties_id = jsonData.getString("Id");
                            header_adsense = jsonData.getString("Google_header_adsense");
                            footer_adsense = jsonData.getString("Google_footer_adsense");

                            JSONArray header_img = jsonData.getJSONArray("H_images");
                            JSONArray footer_img = jsonData.getJSONArray("F_images");

                            header_link = jsonData.getString("Header_link");
                            footer_link = jsonData.getString("Footer_link");


                            if (header_img.length() == 0) {
                                headerAdd_image.setVisibility(View.GONE);
                                header_btndelete.setVisibility(View.GONE);
                            } else {
                                headerAdd_image.setVisibility(View.VISIBLE);
                                header_btndelete.setVisibility(View.GONE);
                                Glide.with(getActivity()).load(MyUrls.Imgurl + header_img.getString(0).toString()).centerCrop().fitCenter().into(headerAdd_image);


                            }

                            if (footer_img.length() == 0) {
                                footerAdd_image.setVisibility(View.GONE);
                                footer_btndelete.setVisibility(View.GONE);
                            } else {
                                footerAdd_image.setVisibility(View.VISIBLE);
                                footer_btndelete.setVisibility(View.GONE);
                                Glide.with(getActivity()).load(MyUrls.Imgurl + footer_img.getString(0).toString()).centerCrop().fitCenter().into(footerAdd_image);

                            }


                            if (header_adsense.equalsIgnoreCase("")) {
                                headerAdd_webView.setVisibility(View.GONE);
                            } else {
                                headerAdd_webView.setVisibility(View.VISIBLE);
                                headerAdd_webView.getSettings().setJavaScriptEnabled(true);
                                headerAdd_webView.loadData(header_adsense, "text/html", "charset=UTF-8");
                            }

                            if (footer_adsense.equalsIgnoreCase("")) {
                                footerAdd_webView.setVisibility(View.GONE);
                            } else {
                                footerAdd_webView.setVisibility(View.VISIBLE);
                                footerAdd_webView.getSettings().setJavaScriptEnabled(true);
                                footerAdd_webView.loadData(footer_adsense, "text/html", "charset=UTF-8");
                            }
                        } else {

                            headerAdd_image.setVisibility(View.GONE);
                            footerAdd_image.setVisibility(View.GONE);

                            headerAdd_webView.setVisibility(View.GONE);
                            footerAdd_webView.setVisibility(View.GONE);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
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
        }
    }
}