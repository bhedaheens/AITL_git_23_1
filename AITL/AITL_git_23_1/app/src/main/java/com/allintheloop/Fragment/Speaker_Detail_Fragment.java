package com.allintheloop.Fragment;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Adapter.SpeakerDetailSessionPagerAdapter;
import com.allintheloop.Adapter.SpeakerDocumentListingAdapter;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.Speaker.SpeakerAgendaSessionData;
import com.allintheloop.Bean.Speaker.SpeakerDocumentListing;
import com.allintheloop.Bean.Speaker.SpeakerReloadedEventBus;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.Fragment.AttandeeFragments.AttendeeRequestMettingDialog;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.HomeCustomViewPager;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.RoundedImageConverter;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static com.allintheloop.Util.GlobalData.Update_Profile;

/**
 * A simple {@link Fragment} subclass.
 */
public class Speaker_Detail_Fragment extends Fragment implements VolleyInterface {

    public static int counter = 0;
    static Context context;
    Button btn_askQuestion;
    ImageView speaker_img;
    //    ImageView speaker_img;
    ProgressBar progressBar1;
    TextView textViewNoDATA, txt_Msgname;
    String first_name, last_name, title, company_name, logo, designation, str_webview, str_unReadcount;
    CardView message_section, webView_card, card_nospeaker;
    LinearLayout session_card;
    SessionManager sessionManager;
    int cnt = 1, web_cnt = 1;
    WebView webView;
    String fb_url, twitter_url, linked_url, insta_url;
    BoldTextView btn_fb, btn_twitter, btn_linkin, btn_instagram, txt_full_name, txt_designation;
    Bundle bundle;
    boolean isFirstTime = false;
    boolean btn_load;
    LinearLayoutManager linearLayoutManager;
    int total_pages, page_count = 1;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView txt_profileName;
    LinearLayout layout_data, layout_document;
    boolean isLoading;
    HomeCustomViewPager rv_viewSession;
    RecyclerView rv_viewDocument;
    Handler handler;
    NestedScrollView scrollView;
    ProgressBar progressBar2;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String Tag = "SpeakerDetail";
    SpeakerDetailSessionPagerAdapter detailSessionPagerAdapter;
    SpeakerDocumentListingAdapter documentListingAdapter;
    ArrayList<SpeakerAgendaSessionData> speakerAgendaSessionDataArrayList;
    ArrayList<SpeakerDocumentListing> speakerDocumentArrayList;
    ImageView img_leftArrowagenda, img_rightArrowagenda;
    int currentPage = 0;
    RelativeLayout btn_askQuestion_layout;
    DefaultLanguage.DefaultLang defaultLang;
    Button btn_seeMyMetting, btn_request;
    JSONObject jsonDateData;
    ImageView speaker_fav;
    String is_favorited = "0";
    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            ToastC.show(getActivity(), "Download Completed");
            getActivity().unregisterReceiver(onComplete);
        }
    };
    BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show();
            getActivity().unregisterReceiver(onNotificationClick);
        }
    };
    private UidCommonKeyClass uidCommonKeyClass;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {


            // TODO Auto-generated method stub
            if (sessionManager.getNotification_role().equalsIgnoreCase("Speaker") && sessionManager.getNotification_UserId().equalsIgnoreCase(SessionManager.speaker_id)) {

                Glide.with(getActivity()).load(MyUrls.Imgurl + sessionManager.getUserProfile()).into(speaker_img);
            }
        }
    };

    public Speaker_Detail_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rooView = inflater.inflate(R.layout.fragment_speaker__detail, container, false);


        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        btn_askQuestion = (Button) rooView.findViewById(R.id.btn_askQuestion);
        btn_seeMyMetting = (Button) rooView.findViewById(R.id.btn_seeMyMetting);
        btn_request = (Button) rooView.findViewById(R.id.btn_request);
        txt_Msgname = (TextView) rooView.findViewById(R.id.txt_Msgname);
        txt_profileName = (TextView) rooView.findViewById(R.id.txt_profileName);

        scrollView = (NestedScrollView) rooView.findViewById(R.id.scrollView);
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        speakerAgendaSessionDataArrayList = new ArrayList<>();
        speakerDocumentArrayList = new ArrayList<>();
        context = getContext();
        handler = new Handler();
        layout_data = (LinearLayout) rooView.findViewById(R.id.layout_data);
        layout_document = (LinearLayout) rooView.findViewById(R.id.layout_document);
        jsonDateData = new JSONObject();
        rv_viewSession = (HomeCustomViewPager) rooView.findViewById(R.id.rv_viewSession);
        progressBar2 = (ProgressBar) rooView.findViewById(R.id.progressBar2);
        session_card = (LinearLayout) rooView.findViewById(R.id.session_card);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mSwipeRefreshLayout = (SwipeRefreshLayout) rooView.findViewById(R.id.swiperefresh);

        speaker_img = (ImageView) rooView.findViewById(R.id.speaker_img);
        img_leftArrowagenda = (ImageView) rooView.findViewById(R.id.img_leftArrowagenda);
        img_rightArrowagenda = (ImageView) rooView.findViewById(R.id.img_rightArrowagenda);
        speaker_fav = (ImageView) rooView.findViewById(R.id.speaker_fav);
        progressBar1 = (ProgressBar) rooView.findViewById(R.id.progressBar1);
        txt_full_name = (BoldTextView) rooView.findViewById(R.id.full_name);
        txt_designation = (BoldTextView) rooView.findViewById(R.id.designation);
        message_section = (CardView) rooView.findViewById(R.id.message_section);
        webView_card = (CardView) rooView.findViewById(R.id.webView_card);
        card_nospeaker = (CardView) rooView.findViewById(R.id.card_nospeaker);
        rv_viewDocument = (RecyclerView) rooView.findViewById(R.id.rv_viewDocument);

        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();

        btn_askQuestion.setText(defaultLang.get7AskAQuestion());

        GlobalData.currentModuleForOnResume = GlobalData.speakerModuleid;
        img_rightArrowagenda.setColorFilter(Color.parseColor("#9BA1A1"));
        img_leftArrowagenda.setColorFilter(Color.parseColor("#9BA1A1"));

        sessionManager.strModuleId = SessionManager.speaker_id;
//        sessionManager.setMenuid("7");
        sessionManager.strMenuId = "7";
        img_rightArrowagenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rv_viewSession.setCurrentItem(currentPage + 1, true);
            }
        });

        img_leftArrowagenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rv_viewSession.setCurrentItem(currentPage - 1, true);
            }
        });


        speaker_fav.setOnClickListener(view -> {
            if (GlobalData.isNetworkAvailable(getActivity())) {
                if (GlobalData.checkForUIDVersion())
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_SpeakerDetailUid, Param.getSpeaker_Detail(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), SessionManager.speaker_id, sessionManager.getUserId(), page_count, "1"), 1, true, this);
                else
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_SpeakerDetail, Param.getSpeaker_Detail(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), SessionManager.speaker_id, sessionManager.getUserId(), page_count, "1"), 1, true, this);
            } else {
                ToastC.show(getActivity(), "No Internet Connection");
            }
        });


        rv_viewSession.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                currentPage = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        message_section = (CardView) rooView.findViewById(R.id.message_section);

        btn_fb = (BoldTextView) rooView.findViewById(R.id.btn_fb);
        btn_twitter = (BoldTextView) rooView.findViewById(R.id.btn_twitter);
        btn_linkin = (BoldTextView) rooView.findViewById(R.id.btn_linkin);
        btn_instagram = (BoldTextView) rooView.findViewById(R.id.btn_instagram);

        bundle = new Bundle();

        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));

//            btn_askQuestion.setBackgroundDrawable(drawable);
            btn_seeMyMetting.setBackgroundDrawable(drawable);
            btn_request.setBackgroundDrawable(drawable);

//            btn_askQuestion.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_request.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_seeMyMetting.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

        } else {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
//            btn_askQuestion.setBackgroundDrawable(drawable);
            btn_seeMyMetting.setBackgroundDrawable(drawable);
            btn_request.setBackgroundDrawable(drawable);

//            btn_askQuestion.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_request.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_seeMyMetting.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        }


        textViewNoDATA = (TextView) rooView.findViewById(R.id.textViewNoDATA);

        webView = (WebView) rooView.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");

        rv_viewDocument.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                SpeakerDocumentListing docObj = speakerDocumentArrayList.get(position);

                String extension = MimeTypeMap.getFileExtensionFromUrl(docObj.getDocument_file());

                if (extension.equalsIgnoreCase("doc") || extension.equalsIgnoreCase("docx") || extension.equalsIgnoreCase("pdf")) {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                    bundle.putString("document_file", docObj.getDocument_file());
                    bundle.putString("file_name", docObj.getDocument_file());
                    ((MainActivity) getActivity()).loadFragment(bundle);
                } else if (extension.equalsIgnoreCase("ppt") || extension.equalsIgnoreCase("odg")) {
                    File storagePath = new File(Environment.getExternalStorageDirectory(), "AllInTheLoop");

                    if (!storagePath.exists()) {
                        storagePath.mkdirs();
                    }
                    getActivity().registerReceiver(onComplete,
                            new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    getActivity().registerReceiver(onNotificationClick,
                            new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
                    downloadFile(docObj.getDocument_file(), storagePath.getPath(), docObj.getDocument_file());

                }

            }
        }));


        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jsonDateData.length() > 0) {
                    Bundle bundle = new Bundle();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    bundle.putString("attendeeName", first_name + " " + last_name);
                    bundle.putString("attendeeId", sessionManager.speaker_id);
                    bundle.putString("jsonDateData", jsonDateData.toString());
                    AttendeeRequestMettingDialog fragment = new AttendeeRequestMettingDialog();
                    fragment.setArguments(bundle);
                    fragment.show(fm, "MettingDailog");
                } else {
                    ToastC.show(getActivity(), "Please Wait...");
                }

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (GlobalData.isNetworkAvailable(getActivity())) {

                    isFirstTime = false;
                    page_count = 1;
                    viewMessageApi();
                } else {
                    ToastC.show(getActivity(), "No Internet Connection");
                }
            }
        });


        if (sessionManager.isLogin()) {
            textViewNoDATA.setVisibility(View.GONE);
        } else {
            textViewNoDATA.setVisibility(View.VISIBLE);
            textViewNoDATA.setText("Login or Sign Up to proceed. To sign up or login tap the Sign Up button on the top right of the screen.");
        }

        btn_seeMyMetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                ((MainActivity) getActivity()).loadFragment();
            }
        });


        btn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle.putString("Social_url", fb_url);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);

                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);

            }
        });

        btn_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle.putString("Social_url", twitter_url);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);

                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);

            }
        });

        btn_linkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle.putString("Social_url", linked_url);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);

                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);

            }
        });

        btn_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle.putString("Social_url", insta_url);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);

                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);

            }
        });


        btn_askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLogin()) {
                    sessionManager.private_senderId = SessionManager.speaker_id;
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    sessionManager.alertDailogLogin(getActivity());
                }

            }
        });


//        if (GlobalData.isNetworkAvailable(getActivity())) {
//            if (GlobalData.checkForUIDVersion())
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeRequestMeetingDateNewUid, Param.getRequestMettingDateTimeNew(sessionManager.getEventId(), SessionManager.speaker_id, sessionManager.getUserId()), 5, false, this);
//            else
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeRequestMeetingDateNew, Param.getRequestMettingDateTimeNew(sessionManager.getEventId(), SessionManager.speaker_id, sessionManager.getUserId()), 5, false, this);
//        }

        viewMessageApi();
        return rooView;
    }

    public void downloadFile(String uRl, String dir, String filename) {
        DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("Downloading")
                .setDestinationInExternalPublicDir(dir, filename);
        mgr.enqueue(request);
    }

    private void viewMessageApi() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            Cursor cursor = sqLiteDatabaseHandler.getAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), SessionManager.speaker_id, Tag);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jArrayevent = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.AtteendeeSpeakerDetail_Data)));
                        loadData(jArrayevent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_SpeakerDetailUid, Param.getSpeaker_Detail(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), SessionManager.speaker_id, sessionManager.getUserId(), page_count, "0"), 0, true, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_SpeakerDetail, Param.getSpeaker_Detail(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), SessionManager.speaker_id, sessionManager.getUserId(), page_count, "0"), 0, false, this);
        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), SessionManager.speaker_id, Tag);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jArrayevent = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.AtteendeeSpeakerDetail_Data)));
                        loadData(jArrayevent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                layout_data.setVisibility(View.GONE);
                card_nospeaker.setVisibility(View.VISIBLE);
            }
        }
    }

    private void loadData(JSONObject json_data) {
        try {
            sessionManager.setKeySpeakerAllowmessage(json_data.getString("allow_msg_keypeople_to_attendee"));
            JSONObject json_attendetail = json_data.getJSONObject("speaker_details");
            str_unReadcount = json_data.getString("unread_count");
            if (sessionManager.getKeySpeakerAllowmessage().equalsIgnoreCase("0")) {
                btn_askQuestion.setVisibility(View.GONE);
            } else {
                if (sessionManager.getUserId().equalsIgnoreCase(SessionManager.speaker_id)) {
                    btn_askQuestion.setVisibility(View.GONE);
                } else {
                    btn_askQuestion.setVisibility(View.VISIBLE);
                }
            }

            if (json_attendetail.length() == 0) {
                layout_data.setVisibility(View.GONE);
                card_nospeaker.setVisibility(View.VISIBLE);
            } else {
                layout_data.setVisibility(View.VISIBLE);
                card_nospeaker.setVisibility(View.GONE);


                first_name = json_attendetail.getString("Firstname");
//                        txt_Msgname.setText("Question  To " + first_name);
                last_name = json_attendetail.getString("Lastname");
                company_name = json_attendetail.getString("Company_name");
                title = json_attendetail.getString("Title");
                logo = json_attendetail.getString("Logo");
                str_webview = json_attendetail.getString("Speaker_desc");

                fb_url = json_attendetail.getString("Facebook_url");
                twitter_url = json_attendetail.getString("Twitter_url");
                linked_url = json_attendetail.getString("Linkedin_url");
                insta_url = json_attendetail.getString("Instagram_url");
                is_favorited = json_attendetail.getString("is_favorited");
                JSONArray jsonArrayAgenda = json_data.getJSONArray("agenda");
                speakerAgendaSessionDataArrayList.clear();
                for (int a = 0; a < jsonArrayAgenda.length(); a++) {
                    JSONObject index = jsonArrayAgenda.getJSONObject(a);
                    speakerAgendaSessionDataArrayList.add(new SpeakerAgendaSessionData(
                            index.getString("Id")
                            , index.getString("Heading")
                            , index.getString("Start_date")
                            , index.getString("Start_time_new")
                            , index.getString("Types")
                            , index.getString("custom_location")));
                }
                if (speakerAgendaSessionDataArrayList.size() != 0) {
                    session_card.setVisibility(View.VISIBLE);
                    detailSessionPagerAdapter = new SpeakerDetailSessionPagerAdapter(speakerAgendaSessionDataArrayList, getActivity());
                    rv_viewSession.setAdapter(detailSessionPagerAdapter);
                } else {
                    session_card.setVisibility(View.GONE);
                }
                JSONArray jsonArrayDocument = json_data.getJSONArray("document");
                speakerDocumentArrayList.clear();
                for (int a = 0; a < jsonArrayDocument.length(); a++) {
                    JSONObject index = jsonArrayDocument.getJSONObject(a);
                    speakerDocumentArrayList.add(new SpeakerDocumentListing(index.getString("id")
                            , index.getString("title")
                            , index.getString("document_file")));
                }
                if (speakerDocumentArrayList.size() != 0) {
                    layout_document.setVisibility(View.GONE);
                    documentListingAdapter = new SpeakerDocumentListingAdapter(speakerDocumentArrayList, getActivity());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    rv_viewDocument.setLayoutManager(mLayoutManager);
                    rv_viewDocument.setItemAnimator(new DefaultItemAnimator());
                    rv_viewDocument.setAdapter(documentListingAdapter);
                } else {
                    layout_document.setVisibility(View.GONE);
                }

                if (fb_url.equalsIgnoreCase("")) {
                    btn_fb.setVisibility(View.GONE);
                } else {
                    btn_fb.setVisibility(View.VISIBLE);
                }

                if (sessionManager.isLogin()) {
                    if (GlobalData.checkForUIDVersion()) {
                        if (sessionManager.getfavIsEnabled().equalsIgnoreCase("1") &&
                                uidCommonKeyClass.getIsOnlyAttendeeUser().equalsIgnoreCase("1")) {
                            speaker_fav.setVisibility(View.VISIBLE);
                        } else {
                            speaker_fav.setVisibility(View.GONE);
                        }
                    } else {
                        if (sessionManager.getfavIsEnabled().equalsIgnoreCase("1") &&
                                sessionManager.getRolId().equalsIgnoreCase("4")) {
                            speaker_fav.setVisibility(View.VISIBLE);
                        } else {
                            speaker_fav.setVisibility(View.GONE);
                        }
                    }

                } else {
                    speaker_fav.setVisibility(View.GONE);
                }


                String topColor = "#FFFFFF";
                if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                    topColor = sessionManager.getFunTopBackColor();
                } else {
                    topColor = sessionManager.getTopBackColor();
                }
                speaker_fav.setColorFilter(Color.parseColor(topColor));
                if (is_favorited.equalsIgnoreCase("1")) {
                    speaker_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_selected));
                } else {
                    speaker_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_normal));
                }

                if (twitter_url.equalsIgnoreCase("")) {
                    btn_twitter.setVisibility(View.GONE);
                } else {
                    btn_twitter.setVisibility(View.VISIBLE);
                }

                if (linked_url.equalsIgnoreCase("")) {
                    btn_linkin.setVisibility(View.GONE);
                } else {
                    btn_linkin.setVisibility(View.VISIBLE);
                }

                if (insta_url.equalsIgnoreCase("")) {
                    btn_instagram.setVisibility(View.GONE);
                } else {
                    btn_instagram.setVisibility(View.VISIBLE);
                }

                txt_full_name.setText(first_name + " " + last_name);

                if (!(title.equalsIgnoreCase(""))) {
                    if (!(company_name.equalsIgnoreCase(""))) {
                        txt_designation.setVisibility(View.VISIBLE);
                        designation = title + "\n" + company_name;
                        txt_designation.setText(designation);
                    } else {
                        txt_designation.setVisibility(View.VISIBLE);
                        txt_designation.setText(title);
                    }
                } else {
                    txt_designation.setVisibility(View.GONE);
                }

                if (!str_webview.isEmpty()) {
                    String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/Lato-Light.ttf\")}body {font-family:MyFont;text-align: left;}</style></head><body>";
                    String pas = "</body></html>";
                    String myHtmlString = pish + str_webview + pas;
                    webView.setVisibility(View.VISIBLE);
                    webView.loadDataWithBaseURL("file:///android_asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);
                } else {
                    webView.setVisibility(View.GONE);
                }
                final String imgpath = MyUrls.Imgurl + logo;
                if (logo.equalsIgnoreCase("")) {
                    progressBar1.setVisibility(View.GONE);
                    speaker_img.setVisibility(View.GONE);
                    txt_profileName.setVisibility(View.VISIBLE);
                    GradientDrawable drawable = new GradientDrawable();
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                    if (!(first_name.equalsIgnoreCase(""))) {
                        if (!(last_name.equalsIgnoreCase(""))) {
                            String name = first_name.charAt(0) + "" + last_name.charAt(0);
                            txt_profileName.setText(name);
                        } else {
                            txt_profileName.setText("" + first_name.charAt(0));
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
                    }
                } else {


                    Glide.with(getActivity()).load(imgpath).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            progressBar1.setVisibility(View.GONE);
                            speaker_img.setVisibility(View.GONE);
                            txt_profileName.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar1.setVisibility(View.GONE);
                            speaker_img.setVisibility(View.VISIBLE);
                            txt_profileName.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(new BitmapImageViewTarget(speaker_img) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            speaker_img.setImageBitmap(RoundedImageConverter.getRoundedCornerBitmap(resource, Color.WHITE, 80, 0, getContext()));
                        }
                    });
                }
            }

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

                        mSwipeRefreshLayout.setRefreshing(false);
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        loadData(jsonObjectData);
                        if (sqLiteDatabaseHandler.isAttdeeSpeaker_DetailExist(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), SessionManager.speaker_id, Tag)) {
                            sqLiteDatabaseHandler.deleteAttdeeSpeaker_DetailData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), SessionManager.speaker_id, Tag);
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObjectData.toString(), SessionManager.speaker_id, Tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObjectData.toString(), SessionManager.speaker_id, Tag);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        mSwipeRefreshLayout.setRefreshing(false);
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        loadData(jsonObjectData);
                        if (sqLiteDatabaseHandler.isAttdeeSpeaker_DetailExist(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), SessionManager.speaker_id, Tag)) {
                            sqLiteDatabaseHandler.deleteAttdeeSpeaker_DetailData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), SessionManager.speaker_id, Tag);
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObjectData.toString(), SessionManager.speaker_id, Tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObjectData.toString(), SessionManager.speaker_id, Tag);
                        }
                        sqLiteDatabaseHandler.updateSpeakerFavAdapter(sessionManager.getEventId(), sessionManager.getUserId(), SessionManager.speaker_id, is_favorited);
                        EventBus.getDefault().post(new SpeakerReloadedEventBus(""));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    mSwipeRefreshLayout.setRefreshing(false);
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        loadData(jsonObjectData);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
//
                        jsonDateData = jsonObject;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Update_Profile));
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.speakerModuleid)) {
            ((MainActivity) getActivity()).getUpdatedDataFromParticularmodule(GlobalData.speakerModuleid);
        }
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(broadcastReceiver);

        super.onPause();
    }

}
