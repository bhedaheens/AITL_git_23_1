package com.allintheloop.Fragment.ExhibitorFragment;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Adapter.Adapter_ExhibitorDetailAttendeeList;
import com.allintheloop.Adapter.Attendee.AttendeeDetailPagerAdapter;
import com.allintheloop.Bean.Attendee.AttendeeDetailShare;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorDetailAttendeeList;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.Fragment.RequestMeetingModule.RequestMettingDailogFragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
import com.allintheloop.Util.CoordinatedImageView;
import com.allintheloop.Util.GlobalData;
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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import static com.allintheloop.Util.GlobalData.Update_Profile;

/**
 * A simple {@link Fragment} subclass.
 */
public class Exhibitor_Detail_Fragment extends Fragment implements VolleyInterface {


    boolean isdatabase = false;
    LinearLayout linear_stand, linear_keyword, layout_data, linear_viewOnMap, linear_markVisited;
    TextView txt_keyword, keyword, exhibitor_name, textViewNoDATA, txt_compnyname, txt_viewOnMap, btn_askQuestion, textwholeNoDATA, txt_profileName, txt_reuqestPending, txt_email, txt_number, txt_conutry;
    ImageView exhibitor_img;
    BoldTextView btn_fb, btn_twitter, btn_linkin, btn_youtube, btn_insta;
    ProgressBar progressBar1;
    BoldTextView stand_number, btn_request, website_url;
    ImageView exhibitor_fav;
    String str_heading, str_keyword, str_desc, fb_url, twitter_url, linked_url, logo, str_standnumber, str_websiteUrl, str_instaUrl, str_youtubeUrl, approvedStatus = "", str_unReadcount, res_exhibitor_id, map_coords = "", map_id = "", str_is_visible_view_btn = "", user_name, Company_name, str_email, str_number, str_conutry, check_dwg_files, str_is_visible_markbtn = "", str_isMarked = "0", str_isFavorite = "";
    Bundle bundle;
    SessionManager sessionManager;

    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    CardView card_noexhibitor;
    WebView webview_exhi_description;
    Handler handler;
    NestedScrollView scrollView;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    ArrayList<AttendeeDetailShare> attendeeDetailShareArrayList;
    AttendeeDetailPagerAdapter attendeeDetailPagerAdapter;

    CoordinatedImageView img_frame;
    RecyclerView rv_attendeeView;
    ArrayList<ExhibitorDetailAttendeeList> exhibitorDetailAttendeeLists;
    Adapter_ExhibitorDetailAttendeeList adapterExhibitorDetailAttendeeList;
    UidCommonKeyClass uidCommonKeyClass;
    DefaultLanguage.DefaultLang defaultLang;
    SwitchCompat swith_visited;
    LinearLayout top_card;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @SuppressLint("LongLogTag")
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (sessionManager.getNotification_role().equalsIgnoreCase("Exibitor") && sessionManager.getNotification_UserId().equalsIgnoreCase(sessionManager.exhibitor_id)) {
                Glide.with(getActivity()).load(MyUrls.Imgurl + sessionManager.getUserProfile())
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                exhibitor_img.setImageBitmap(resource);
                            }
                        });
            }

        }
    };


    //    CoordinatedImageView imageView;
    public Exhibitor_Detail_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exhibitor__detail, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        sessionManager = new SessionManager(getActivity());
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();
        defaultLang = sessionManager.getMultiLangString();
        bundle = new Bundle();

        exhibitorDetailAttendeeLists = new ArrayList<>();

        handler = new Handler();

        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        linear_stand = (LinearLayout) rootView.findViewById(R.id.linear_stand);
        rv_attendeeView = (RecyclerView) rootView.findViewById(R.id.rv_attendeeView);
        linear_keyword = (LinearLayout) rootView.findViewById(R.id.linear_keyword);
        top_card = (LinearLayout) rootView.findViewById(R.id.top_card);
        layout_data = (LinearLayout) rootView.findViewById(R.id.layout_data);
        linear_viewOnMap = (LinearLayout) rootView.findViewById(R.id.linear_viewOnMap);


        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);
        sessionManager.strModuleId = SessionManager.exhi_pageId;
//        sessionManager.setMenuid("3");
        sessionManager.strMenuId = "3";
        GlobalData.currentModuleForOnResume = GlobalData.exhibitorModuleid;
//

        linearLayoutManager = new LinearLayoutManager(getActivity());
        txt_email = (TextView) rootView.findViewById(R.id.txt_email);
        txt_number = (TextView) rootView.findViewById(R.id.txt_number);
        txt_conutry = (TextView) rootView.findViewById(R.id.txt_conutry);

        card_noexhibitor = (CardView) rootView.findViewById(R.id.card_noexhibitor);

        txt_reuqestPending = (TextView) rootView.findViewById(R.id.txt_reuqestPending);
        txt_viewOnMap = (TextView) rootView.findViewById(R.id.txt_viewOnMap);
        txt_compnyname = (TextView) rootView.findViewById(R.id.txt_compnyname);

        stand_number = (BoldTextView) rootView.findViewById(R.id.stand_number);

        website_url = (BoldTextView) rootView.findViewById(R.id.website_url);
        textwholeNoDATA = (TextView) rootView.findViewById(R.id.textwholeNoDATA);
        txt_profileName = (TextView) rootView.findViewById(R.id.txt_profileName);
        txt_keyword = (TextView) rootView.findViewById(R.id.txt_keyword);
        keyword = (TextView) rootView.findViewById(R.id.keyword);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        exhibitor_name = (TextView) rootView.findViewById(R.id.exhibitor_name);
        webview_exhi_description = (WebView) rootView.findViewById(R.id.webview_exhi_description);

        btn_fb = (BoldTextView) rootView.findViewById(R.id.btn_fb);
        btn_twitter = (BoldTextView) rootView.findViewById(R.id.btn_twitter);
        btn_linkin = (BoldTextView) rootView.findViewById(R.id.btn_linkin);
        btn_insta = (BoldTextView) rootView.findViewById(R.id.btn_instagram);
        btn_youtube = (BoldTextView) rootView.findViewById(R.id.btn_youtube);


        exhibitor_img = (ImageView) rootView.findViewById(R.id.exhibitor_img);
        img_frame = (CoordinatedImageView) rootView.findViewById(R.id.img_frame);

        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        btn_askQuestion = (TextView) rootView.findViewById(R.id.btn_askQuestion);
        btn_request = (BoldTextView) rootView.findViewById(R.id.btn_request);

        exhibitor_fav = (ImageView) rootView.findViewById(R.id.exhibitor_fav);
        swith_visited = (SwitchCompat) rootView.findViewById(R.id.swith_visited);
        linear_markVisited = rootView.findViewById(R.id.linear_markVisited);


        txt_viewOnMap.setText(defaultLang.get3ViewOnMap());
        txt_keyword.setText(defaultLang.get3Keywords());
        btn_askQuestion.setText(defaultLang.get3SendMessage());


        btn_request.setText(defaultLang.get3RequestAMeeting());


        webview_exhi_description.getSettings().setJavaScriptEnabled(true);
        webview_exhi_description.getSettings().setAllowContentAccess(true);
        webview_exhi_description.setVerticalScrollBarEnabled(true);
        webview_exhi_description.setHorizontalScrollBarEnabled(true);
        webview_exhi_description.getSettings().setDefaultTextEncodingName("utf-8");
        webview_exhi_description.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webview_exhi_description.setLongClickable(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (GlobalData.isNetworkAvailable(getActivity())) {


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
            textViewNoDATA.setText(getResources().getString(R.string.login_message));
        }

        buttonClick();
        setButton();
        sqLiteDatabaseHandler.UpdateExhibitorDetailUserId(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.exhibitor_id, sessionManager.exhi_pageId);
        viewMessageApi();
        return rootView;
    }

    private void addorRemoveFav() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.savtoMyfavoriteUid, Param.getExhbitor_Detail(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.exhibitor_id, sessionManager.exhi_pageId, 0, sessionManager.getUserId(), sessionManager.getToken()), 8, true, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.savtoMyfavorite, Param.getExhbitor_Detail(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.exhibitor_id, sessionManager.exhi_pageId, 0, sessionManager.getUserId(), sessionManager.getToken()), 8, true, this);

            }
        } else {

            ToastC.show(getActivity(), getActivity().getString(R.string.noInernet));
        }
    }

    private void shareContactButton() {

        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.exhibitor_shareContact,
                Param.exhibitorShareContact(sessionManager.getEventId(),
                        sessionManager.getUserId(), res_exhibitor_id), 4, true, this);
    }

    private void buttonClick() {

        exhibitor_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addorRemoveFav();


            }
        });

        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManager.isLogin()) {
                    Bundle bundle = new Bundle();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    bundle.putString("exhibitorName", str_heading);
                    bundle.putString("exhibitorid", sessionManager.exhi_pageId);
                    RequestMettingDailogFragment fragment = new RequestMettingDailogFragment();
                    fragment.setArguments(bundle);
                    fragment.show(fm, "DialogFragment");
                } else {
                    sessionManager.alertDailogLogin(getActivity());
                }
            }
        });


        linear_viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (check_dwg_files.equalsIgnoreCase("1")) {
//                    if (str_standnumber.contains(",")) {
//                        String split[] = str_standnumber.split(",");
//                        new_standNumber = split[0];
//                    } else {
//                        new_standNumber = str_standnumber;
//                    }
//
//                    if (GlobalData.temp_Fragment == 81) {
//
//                        sessionManager.exhibitor_standNumber = new_standNumber;
//                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
//                        GlobalData.CURRENT_FRAG = GlobalData.NewMapDetail_Fragment;
//                        ((MainActivity) getActivity()).replaceFragment();
//                    } else {
//                        sessionManager.exhibitor_standNumber = new_standNumber;
//                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
//                        GlobalData.CURRENT_FRAG = GlobalData.NewMapDetail_Fragment;
//                        ((MainActivity) getActivity()).loadFragment();
//                    }
                } else {
                    if (!map_id.isEmpty()) {
                        sessionManager.setMapid(map_id);
                        sessionManager.Map_coords = map_coords;
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Map_Detail_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }
                }
            }
        });


        swith_visited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (str_isMarked.equalsIgnoreCase("0")) {
                    onclickMarkasread();
                }


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


        btn_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("Social_url", str_youtubeUrl);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);
            }
        });

        btn_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("Social_url", str_instaUrl);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);
            }
        });


        btn_askQuestion.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {

                if (sessionManager.isLogin()) {

                    sessionManager.private_senderId = sessionManager.exhibitor_id;
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    sessionManager.alertDailogLogin(getActivity());
                }
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

        website_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("Social_url", str_websiteUrl);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);

            }
        });
    }

    private void setButton() {
        if (str_isMarked.equalsIgnoreCase("0")) {
            swith_visited.setChecked(false);
            swith_visited.setClickable(true);
        } else {
            swith_visited.setChecked(true);
            swith_visited.setClickable(false);
        }
    }

    private void onclickMarkasread() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.exhibitorMarkAsVisitedUid, Param.markasVisited(sessionManager.getEventId(), res_exhibitor_id, sessionManager.getUserId(), sessionManager.getMenuid(), SessionManager.exhi_pageId, sessionManager.getEventType()), 9, true, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.exhibitorMarkAsVisited, Param.markasVisited(sessionManager.getEventId(), res_exhibitor_id, sessionManager.getUserId(), sessionManager.getMenuid(), SessionManager.exhi_pageId, sessionManager.getEventType()), 9, true, this);
        } else {

            ToastC.show(getActivity(), getActivity().getString(R.string.noInernet));
        }
    }

    private void viewMessageApi() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            isdatabase = true;
            exhibitorDetailAttendeeLists.clear();
            Cursor cursor = sqLiteDatabaseHandler.getExhibitorDetail(sessionManager.getEventId(), sessionManager.exhibitor_id, sessionManager.exhi_pageId, sessionManager.getEventType(), sessionManager.getUserId());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jArrayevent = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ExhibitorDetail_Data)));
                        loadData(jArrayevent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_ExhibitorDetailUid, Param.getExhbitor_Detail(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.exhibitor_id, sessionManager.exhi_pageId, 0, sessionManager.getUserId(), sessionManager.getToken()), 0, false, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_ExhibitorDetail, Param.getExhbitor_Detail(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.exhibitor_id, sessionManager.exhi_pageId, 0, sessionManager.getUserId(), sessionManager.getToken()), 0, false, this);
            }
        } else {

            exhibitorDetailAttendeeLists.clear();
            Cursor cursor = sqLiteDatabaseHandler.getExhibitorDetail(sessionManager.getEventId(), sessionManager.exhibitor_id, sessionManager.exhi_pageId, sessionManager.getEventType(), sessionManager.getUserId());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jArrayevent = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ExhibitorDetail_Data)));
                        loadData(jArrayevent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                layout_data.setVisibility(View.GONE);
                card_noexhibitor.setVisibility(View.VISIBLE);
            }

        }
    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.exhi_pageId, "", "", "EX", ""), 3, false, this);
        }
    }

    private void loadData(JSONObject jsonObject) {
        try {
            sessionManager.setRequestexhibitorKey(jsonObject.getString("hide_request_meeting"));
            sessionManager.setKeyExhibitorAllowmessage(jsonObject.getString("allow_msg_user_to_exhibitor"));
            str_is_visible_markbtn = jsonObject.getString("show_visited_button");
            JSONObject json_attendetail = jsonObject.getJSONObject("exhibitor_details");
            if (json_attendetail.length() == 0) {
                layout_data.setVisibility(View.GONE);
                card_noexhibitor.setVisibility(View.VISIBLE);
            } else {
                layout_data.setVisibility(View.VISIBLE);
                card_noexhibitor.setVisibility(View.GONE);
                str_heading = json_attendetail.getString("Heading");
                str_keyword = json_attendetail.getString("Short_desc");
                str_desc = json_attendetail.getString("Description");
                str_standnumber = json_attendetail.getString("stand_number");
                str_websiteUrl = json_attendetail.getString("website_url");
                user_name = json_attendetail.getString("user_name");
                str_is_visible_view_btn = json_attendetail.getString("is_visible_view_btn");
                Company_name = json_attendetail.getString("Company_name");
                fb_url = json_attendetail.getString("facebook_url");
                twitter_url = json_attendetail.getString("twitter_url");
                str_instaUrl = json_attendetail.getString("instagram_url");
                str_youtubeUrl = json_attendetail.getString("youtube_url");
                check_dwg_files = json_attendetail.getString("check_dwg_files");
                res_exhibitor_id = json_attendetail.getString("exhibitor_id");
                linked_url = json_attendetail.getString("linkedin_url");
                str_isMarked = json_attendetail.getString("is_visited");
                approvedStatus = json_attendetail.getString("approval_status");
                str_isFavorite = json_attendetail.getString("is_favorites");

                JSONArray jContactArray = json_attendetail.getJSONArray("contact_details");
                exhibitorDetailAttendeeLists = new ArrayList<>();
                JSONArray jsonArrayAttendeeList = json_attendetail.getJSONArray("linked_attendees");
                for (int a = 0; a < jsonArrayAttendeeList.length(); a++) {
                    JSONObject indexAttendee = jsonArrayAttendeeList.getJSONObject(a);
                    exhibitorDetailAttendeeLists.add(new ExhibitorDetailAttendeeList(indexAttendee.getString("Id"),
                            indexAttendee.getString("Firstname"),
                            indexAttendee.getString("Lastname"),
                            indexAttendee.getString("Logo")));

                }
                if (exhibitorDetailAttendeeLists.size() != 0) {
                    top_card.setVisibility(View.VISIBLE);
                    adapterExhibitorDetailAttendeeList = new Adapter_ExhibitorDetailAttendeeList(exhibitorDetailAttendeeLists, getActivity());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    rv_attendeeView.setLayoutManager(mLayoutManager);
                    rv_attendeeView.setItemAnimator(new DefaultItemAnimator());
                    rv_attendeeView.setAdapter(adapterExhibitorDetailAttendeeList);
                } else {
                    top_card.setVisibility(View.GONE);
                }

                JSONArray jShareDetalArray = json_attendetail.getJSONArray("share_details");
                attendeeDetailShareArrayList = new ArrayList<>();
                for (int j = 0; j < jShareDetalArray.length(); j++) {
                    JSONObject json_shareDetail = jShareDetalArray.getJSONObject(j);
                    attendeeDetailShareArrayList.add(new AttendeeDetailShare(json_shareDetail.getString("attendee_id")
                            , json_shareDetail.getString("Email")
                            , json_shareDetail.getString("phone_no")
                            , json_shareDetail.getString("country_name"), "Exhibitor"));
                }
                JSONObject jObjectMap = json_attendetail.getJSONObject("map_details");
                if (jObjectMap.length() != 0) {
                    if (jObjectMap.has("coords")) {
                        map_coords = jObjectMap.getString("coords");
                    }
                    if (jObjectMap.has("map_id")) {
                        map_id = jObjectMap.getString("map_id");
                    }
                }


                logo = json_attendetail.getString("company_logo");
                if (logo.equalsIgnoreCase("")) {
                    progressBar1.setVisibility(View.GONE);
                    exhibitor_img.setVisibility(View.GONE);
                    txt_profileName.setVisibility(View.VISIBLE);
                    GradientDrawable drawable = new GradientDrawable();
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                    if (!(str_heading.equalsIgnoreCase(""))) {
                        txt_profileName.setText("" + str_heading.charAt(0));
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

                    Glide.with(getActivity()).load(MyUrls.Imgurl + logo).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            progressBar1.setVisibility(View.GONE);
                            exhibitor_img.setVisibility(View.GONE);
                            txt_profileName.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar1.setVisibility(View.GONE);
                            exhibitor_img.setVisibility(View.VISIBLE);
                            txt_profileName.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(new BitmapImageViewTarget(exhibitor_img) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            exhibitor_img.setImageBitmap(RoundedImageConverter.getRoundedCornerBitmap(resource, Color.WHITE, 30, 0, getContext()));
                        }
                    });

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        hideShowView(jsonObject);
        setButton();
    }

    private void hideShowView(JSONObject jsonObject) {

        try {

            if (jsonObject.has("unread_count"))
                str_unReadcount = jsonObject.getString("unread_count");
            if (sessionManager.getKeyExhibitorAllowmessage().equalsIgnoreCase("0")) {
                btn_askQuestion.setVisibility(View.GONE);
            } else {

                if (sessionManager.getUserId().equalsIgnoreCase(sessionManager.exhibitor_id)) {
                    btn_askQuestion.setVisibility(View.GONE);

                } else {
                    btn_askQuestion.setVisibility(View.VISIBLE);
                }

            }

            if (sessionManager.isLogin()) {

                if (str_is_visible_markbtn.equalsIgnoreCase("1")) {

                    linear_markVisited.setVisibility(View.VISIBLE);
                } else {
                    linear_markVisited.setVisibility(View.GONE);
                }

                if (res_exhibitor_id.equalsIgnoreCase(sessionManager.getUserId())) {
                    exhibitor_fav.setVisibility(View.GONE);
                } else {

                    if (GlobalData.checkForUIDVersion()) {
                        if (uidCommonKeyClass.getExhibitorShowMyfavoriteBtn().equalsIgnoreCase("1") &&
                                sessionManager.getfavIsEnabled().equalsIgnoreCase("1")) {
                            exhibitor_fav.setVisibility(View.VISIBLE);

                            if (isdatabase) {
                                if (sessionManager.isExhibitorFav.equalsIgnoreCase("1") &&
                                        sessionManager.exhibitor_id.equalsIgnoreCase(res_exhibitor_id)) {
                                    exhibitor_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_selected));
                                } else if (sessionManager.isExhibitorFav.equalsIgnoreCase("0")) {
                                    exhibitor_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_normal));
                                } else if (sessionManager.isExhibitorFav.equalsIgnoreCase("1")) {
                                    exhibitor_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_selected));
                                } else {

                                    if (str_isFavorite.equalsIgnoreCase("1")) {
                                        exhibitor_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_selected));
                                    } else {
                                        exhibitor_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_normal));

                                    }
                                }
                                isdatabase = false;
                                sessionManager.isExhibitorFav = "";
                            } else {
                                if (str_isFavorite.equalsIgnoreCase("1")) {
                                    exhibitor_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_selected));

                                } else {
                                    exhibitor_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_normal));
                                }
                            }
                        } else {
                            exhibitor_fav.setVisibility(View.GONE);
                        }


                    } else {


                        if (sessionManager.getRolId().equalsIgnoreCase("4") && // changes applied
                                sessionManager.getfavIsEnabled().equalsIgnoreCase("1")) {
                            exhibitor_fav.setVisibility(View.VISIBLE);
                            if (isdatabase) {
                                if (sessionManager.isExhibitorFav.equalsIgnoreCase("1") &&
                                        sessionManager.exhibitor_id.equalsIgnoreCase(res_exhibitor_id)) {
                                    exhibitor_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_selected));
                                } else if (sessionManager.isExhibitorFav.equalsIgnoreCase("0")) {
                                    exhibitor_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_normal));
                                } else if (sessionManager.isExhibitorFav.equalsIgnoreCase("1")) {
                                    exhibitor_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_selected));
                                } else {

                                    if (str_isFavorite.equalsIgnoreCase("1")) {
                                        exhibitor_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_selected));
                                    } else {
                                        exhibitor_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_normal));

                                    }
                                }
                                isdatabase = false;
                                sessionManager.isExhibitorFav = "";
                            } else {
                                if (str_isFavorite.equalsIgnoreCase("1")) {
                                    exhibitor_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_selected));

                                } else {
                                    exhibitor_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_normal));
                                }
                            }
                        } else {
                            exhibitor_fav.setVisibility(View.GONE);
                        }
                    }

                    String topColor = "#FFFFFF";
                    if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                        topColor = sessionManager.getFunTopBackColor();
                    } else {
                        topColor = sessionManager.getTopBackColor();
                    }
                    exhibitor_fav.setColorFilter(Color.parseColor(topColor));
                }


                if (GlobalData.checkForUIDVersion()) {
                    if (uidCommonKeyClass.getExhibitorShowShareContact().equalsIgnoreCase("1")) {

                        if (sessionManager.getUserId().equalsIgnoreCase(res_exhibitor_id)) {
                            btn_request.setVisibility(View.GONE);

                        } else {
                            if (sessionManager.getRequestexhibitorKey().equalsIgnoreCase("0")) {
                                btn_request.setVisibility(View.GONE);
                            } else {
                                if (uidCommonKeyClass.getExhibitorShowRequestMeeting().equalsIgnoreCase("1")) {
                                    btn_request.setVisibility(View.VISIBLE);

                                } else {
                                    btn_request.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                } else {
                    if (sessionManager.getRolId().equalsIgnoreCase("4") || //changes applied
                            sessionManager.getRolId().equalsIgnoreCase("6")) { //changes applied

                        if (sessionManager.getUserId().equalsIgnoreCase(res_exhibitor_id)) {
                            btn_request.setVisibility(View.GONE);

                        } else {
                            if (sessionManager.getRequestexhibitorKey().equalsIgnoreCase("0")) {
                                btn_request.setVisibility(View.GONE);
                            } else {
                                if (sessionManager.getRolId().equalsIgnoreCase("4")) { //changes applied
                                    btn_request.setVisibility(View.VISIBLE);

                                } else {
                                    btn_request.setVisibility(View.GONE);
                                }
                            }
                        }

                    }

                }


            } else {
                exhibitor_fav.setVisibility(View.GONE);
                linear_markVisited.setVisibility(View.GONE);
            }


            if (str_standnumber.equalsIgnoreCase("")) {
                linear_stand.setVisibility(View.GONE);
            } else {
                linear_stand.setVisibility(View.VISIBLE);
                stand_number.setText(str_standnumber);
                if (str_is_visible_view_btn.equalsIgnoreCase("1")) {
                    txt_viewOnMap.setVisibility(View.VISIBLE);
                } else {
                    txt_viewOnMap.setVisibility(View.GONE);

                }
            }


            if (str_heading.trim().length() != 0) {
                txt_compnyname.setText(str_heading);
                txt_compnyname.setVisibility(View.VISIBLE);
            } else
                txt_compnyname.setVisibility(View.VISIBLE);

            if (fb_url.equalsIgnoreCase("")) {
                btn_fb.setVisibility(View.GONE);
            } else {
                btn_fb.setVisibility(View.VISIBLE);
            }

            if (str_youtubeUrl.equalsIgnoreCase("")) {
                btn_youtube.setVisibility(View.GONE);
            } else {
                btn_youtube.setVisibility(View.VISIBLE);
            }

            if (str_instaUrl.equalsIgnoreCase("")) {
                btn_insta.setVisibility(View.GONE);
            } else {
                btn_insta.setVisibility(View.VISIBLE);
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

            if (str_heading.equalsIgnoreCase("")) {
                exhibitor_name.setVisibility(View.GONE);
            } else {
                exhibitor_name.setVisibility(View.GONE);
                exhibitor_name.setText(str_heading);
            }

            if (str_desc.equalsIgnoreCase("")) {
                webview_exhi_description.setVisibility(View.GONE);
            } else {
                webview_exhi_description.setVisibility(View.VISIBLE);
                String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/Lato-Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: left;}</style></head><body>";
                String pas = "</body></html>";
                String myHtmlString = pish + str_desc + pas;
                webview_exhi_description.loadDataWithBaseURL("file:///android_asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);
            }


            if (str_websiteUrl.equalsIgnoreCase("")) {
                website_url.setVisibility(View.GONE);
            } else {
                website_url.setVisibility(View.VISIBLE);
            }

            if (str_keyword.equalsIgnoreCase("")) {
                linear_keyword.setVisibility(View.GONE);
            } else {
                linear_keyword.setVisibility(View.GONE);
                txt_keyword.setText("Keywords: ");
                keyword.setText(str_keyword);
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
                    mSwipeRefreshLayout.setRefreshing(false);
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        if (sessionManager.isLogin()) {
                            pagewiseClick(); // hit API
                        }
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        sqLiteDatabaseHandler.deleteExhibitorDetailData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), "9000", "9000");
                        if (sqLiteDatabaseHandler.isExhibitorExist(sessionManager.getEventId(), sessionManager.exhibitor_id, sessionManager.exhi_pageId, sessionManager.getEventType(), sessionManager.getUserId())) {
                            sqLiteDatabaseHandler.deleteExhibitorDetailData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionManager.exhibitor_id, sessionManager.exhi_pageId);
                            sqLiteDatabaseHandler.insertExhibitorDetail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObjectData.toString(), sessionManager.exhibitor_id, sessionManager.exhi_pageId);
                        } else {
                            sqLiteDatabaseHandler.insertExhibitorDetail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObjectData.toString(), sessionManager.exhibitor_id, sessionManager.exhi_pageId);
                        }

                        loadData(jsonObjectData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    mSwipeRefreshLayout.setRefreshing(false);
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        viewMessageApi();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 3:
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {


                        ToastC.show(getActivity(), jsonObject.getString("message"));
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragmentTag;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        exhibitorDetailAttendeeLists.clear();
                        sqLiteDatabaseHandler.updateExhibitorFav(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.exhi_pageId, "1");

                        loadData(jsonObjectData);
//                        GlobalData.exhibitorListngData(getActivity(), res_exhibitor_id, "1");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 9:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        exhibitorDetailAttendeeLists.clear();
                        loadData(jsonObjectData);
//                        GlobalData.exhibitorListngData(getActivity(), res_exhibitor_id, "1");
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
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.exhibitorModuleid)) {
            ((MainActivity) getActivity()).getUpdatedDataFromParticularmodule(GlobalData.exhibitorModuleid);
        }
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(broadcastReceiver);

        super.onPause();
    }


}
