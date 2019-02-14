package com.allintheloop.Fragment.AttandeeFragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Adapter.Attendee.AttendeeDetailExhibitorListAdapter;
import com.allintheloop.Adapter.Attendee.AttendeeDetailPagerAdapter;
import com.allintheloop.Bean.Attendee.AttendeeDetailShare;
import com.allintheloop.Bean.Attendee.AttndeeDetailExhibitorList;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
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
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import static com.allintheloop.Util.GlobalData.Update_Profile;


/**
 * A simple {@link Fragment} subclass.
 */
public class Attendance_Detail_Fragment extends Fragment implements VolleyInterface {

    public String isBlocked = "", blockeByMe = "";
    ImageView attendee_img;
    ProgressBar progressBar1;

    TextView txt_designation, textViewNoDATA;
    String first_name, last_name, title, company_name, logo, designation, attendee_id;
    //    public static LinearLayout linear_photo, linearimage_load;
    LinearLayout layout_data, linear_goals;
    CardView card_noattendee;
    SessionManager sessionManager;
    BoldTextView btn_Block, txt_goals;
    BoldTextView btn_sendMessageCounter, btn_seeMyMetting, btn_request, txt_fullname;
    boolean isFirstTime = false;
    boolean btn_load;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    int total_pages, page_count = 1;
    int isLast = 0;
    String fblink, twitterLink, linkedInLink;
    ImageView btn_fb, btn_twitter, btn_linkin;
    Bundle bundle;
    TextView txt_profileName, txt_reuqestPending;
    String approvedStatus;
    // public static   ViewPager btnViewpager;
    AttendeeDetailPagerAdapter attendeeDetailPagerAdapter;
    TextView txt_email, txt_number, txt_conutry, textViewwholeData, txt_points;
    String str_email, str_number, str_conutry, str_unReadcount;
    boolean isLoading;
    Handler handler;
    NestedScrollView scrollView;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String Tag = "AttendeeDetail";
    String Tag_type = "";
    String attendee_hide_request_meeting = "", allow_meeting_exibitor_to_attendee = "", str_game_is_on, str_game_points;
    ImageView attendee_fav;
    JSONObject jsonDateData;


    RecyclerView rv_viewExhibitor;
    ArrayList<AttendeeDetailShare> attendeeDetailShareArrayList;
    ArrayList<AttndeeDetailExhibitorList> attndeeDetailExhibitorLists;
    AttendeeDetailExhibitorListAdapter exhibitorListAdapter;
    String message_permisson = "", show_send_message = "", show_send_request = "";
    DefaultLanguage.DefaultLang defaultLang;
    String biography = "", enable_block_button = "";
    TextView txt_biography;
    boolean isMaxMeeting;
    String Message, str_goal = "";
    Context context;
    private UidCommonKeyClass uidCommonKeyClass;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("niral", "Reciver Called : " + sessionManager.getUserProfile());
            // TODO Auto-generated method stub
            if (sessionManager.getNotification_role().equalsIgnoreCase("Attendee")
                    && sessionManager.getNotification_UserId().equalsIgnoreCase(attendee_id)) {
                Log.d("AITL   Fragment Img", MyUrls.Imgurl + sessionManager.getUserProfile());
                Glide.with(getActivity()).load(MyUrls.Imgurl + sessionManager.getUserProfile()).into(attendee_img);
            }

        }
    };

    public Attendance_Detail_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_attendance__detail, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        sessionManager = new SessionManager(getActivity());

        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();

        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        handler = new Handler();
        bundle = new Bundle();
        context = getActivity();
        attendee_id = sessionManager.AttenDeeId;
        sessionManager.strModuleId = sessionManager.AttenDeeId;
        sessionManager.strMenuId = "2";
        attndeeDetailExhibitorLists = new ArrayList<>();
        attendee_img = (ImageView) rootView.findViewById(R.id.attendee_img);
        rv_viewExhibitor = (RecyclerView) rootView.findViewById(R.id.rv_viewExhibitor);
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);
        txt_fullname = (BoldTextView) rootView.findViewById(R.id.full_name);
        txt_designation = (TextView) rootView.findViewById(R.id.designation);
        txt_email = (TextView) rootView.findViewById(R.id.txt_email);
        txt_number = (TextView) rootView.findViewById(R.id.txt_number);
        txt_conutry = (TextView) rootView.findViewById(R.id.txt_conutry);
        txt_biography = (TextView) rootView.findViewById(R.id.txt_biography);

        txt_points = (TextView) rootView.findViewById(R.id.txt_points);

        txt_reuqestPending = (TextView) rootView.findViewById(R.id.txt_reuqestPending);
        txt_profileName = (TextView) rootView.findViewById(R.id.txt_profileName);
        btn_seeMyMetting = (BoldTextView) rootView.findViewById(R.id.btn_seeMyMetting);
        btn_sendMessageCounter = (BoldTextView) rootView.findViewById(R.id.btn_sendMessageCounter);
        btn_request = (BoldTextView) rootView.findViewById(R.id.btn_request);

        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        btn_Block = (BoldTextView) rootView.findViewById(R.id.btn_Block);
        txt_goals = (BoldTextView) rootView.findViewById(R.id.txt_goals);

        jsonDateData = new JSONObject();
        card_noattendee = (CardView) rootView.findViewById(R.id.card_noattendee);
        btn_fb = (ImageView) rootView.findViewById(R.id.btn_fb);
        btn_twitter = (ImageView) rootView.findViewById(R.id.btn_twitter);
        btn_linkin = (ImageView) rootView.findViewById(R.id.btn_linkin);
        attendee_fav = (ImageView) rootView.findViewById(R.id.attendee_fav);
        layout_data = (LinearLayout) rootView.findViewById(R.id.layout_data);
        linear_goals = (LinearLayout) rootView.findViewById(R.id.linear_goals);
        linearLayoutManager = new LinearLayoutManager(getActivity());

//        try {
//            setTextViewDrawableColor(btn_seeMyMetting, Color.parseColor(sessionManager.getTopTextColor()));
//            setTextViewDrawableColor(btn_sendMessageCounter, Color.parseColor(sessionManager.getTopTextColor()));
//            setTextViewDrawableColor(btn_request, Color.parseColor(sessionManager.getTopTextColor()));
//            setTextViewDrawableColor(btn_Block, Color.parseColor(sessionManager.getTopTextColor()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        btn_sendMessageCounter.setOnClickListener(view -> {
            if (sessionManager.isLogin()) {
                sessionManager.private_senderId = SessionManager.AttenDeeId;
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                ((MainActivity) getActivity()).loadFragment();
            } else {
                sessionManager.alertDailogLogin(getActivity());
            }
        });


        attendee_fav.setOnClickListener(view -> fullAttendeeaddorRemoveFav());
        btn_request.setOnClickListener(view -> {

            getAttendeeDate();

//          Log.d("AITL Date Data",jsonDateData.toString());

        });

        defaultLang = sessionManager.getMultiLangString();
        btn_sendMessageCounter.setText(defaultLang.get2SendMessage());
        btn_request.setText(defaultLang.get2RequestMeeting());


        btn_seeMyMetting.setOnClickListener(view -> {
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
            ((MainActivity) getActivity()).loadFragment();
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (GlobalData.isNetworkAvailable(getActivity())) {
//                    messageArrayList.clear();
                viewMessageApi();
            } else {
                ToastC.show(getActivity(), "No Internet Connection");
            }
        });


        if (sessionManager.isLogin()) {
            textViewNoDATA.setVisibility(View.GONE);

        } else {
            textViewNoDATA.setVisibility(View.VISIBLE);
            btn_request.setVisibility(View.GONE);
            btn_seeMyMetting.setVisibility(View.GONE);
            textViewNoDATA.setText("Login or Sign Up to proceed. To sign up or login tap the Sign Up button on the top right of the screen.");
        }


        btn_fb.setOnClickListener(v -> {
            bundle.putString("Social_url", fblink);
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
            ((MainActivity) getActivity()).loadFragment(bundle);
        });

        btn_twitter.setOnClickListener(v -> {

            bundle.putString("Social_url", twitterLink);
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
            ((MainActivity) getActivity()).loadFragment(bundle);

        });

        btn_linkin.setOnClickListener(v -> {

            bundle.putString("Social_url", linkedInLink);
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
            ((MainActivity) getActivity()).loadFragment(bundle);

        });

        btn_Block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockuser();
            }
        });


        viewMessageApi();
        return rootView;
    }


    private void getAttendeeDate() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeRequestMeetingDateNewUid, Param.getRequestMettingDateTimeNew(sessionManager.getEventId(), SessionManager.AttenDeeId, sessionManager.getUserId()), 5, true, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeRequestMeetingDateNew, Param.getRequestMettingDateTimeNew(sessionManager.getEventId(), SessionManager.AttenDeeId, sessionManager.getUserId()), 5, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void setTextViewDrawableColor(Button textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            }
        }
    }

    private void fullAttendeeaddorRemoveFav() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.addRemoveAttendee, Param.addonRemoveAttendeeFav(sessionManager.getEventId(), sessionManager.getUserId(), SessionManager.AttenDeeId), 1, true, this);
        } else {
            ToastC.show(getActivity(), getResources().getString(R.string.noInernet));
        }
    }

    private void blockuser() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (blockeByMe.equalsIgnoreCase("1")) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.blockAttendee, Param.blockAttendee(sessionManager.getEventId(), sessionManager.getUserId(), SessionManager.AttenDeeId, "2"), 6, true, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.blockAttendee, Param.blockAttendee(sessionManager.getEventId(), sessionManager.getUserId(), SessionManager.AttenDeeId, "1"), 6, true, this);

            }
        } else {
            ToastC.show(getActivity(), getResources().getString(R.string.noInernet));
        }
    }

    private void shareInformation() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.shareContactInformationUid,
                        Param.shareContactInformation(sessionManager.getEventId(),
                                SessionManager.AttenDeeId, sessionManager.getUserId()),
                        3, true, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST,
                        MyUrls.shareContactInformation, Param.shareContactInformation
                        (sessionManager.getEventId(), SessionManager.AttenDeeId,
                                sessionManager.getUserId()), 3, true,
                        this);

        }
    }

    private void viewMessageApi() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
//            messageArrayList.clear();
            Cursor cursor = sqLiteDatabaseHandler.getAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), SessionManager.AttenDeeId, Tag);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jArrayevent = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.AtteendeeSpeakerDetail_Data)));
                        Log.d("AITL  Oflline", jArrayevent.toString());
                        loadData(jArrayevent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (sessionManager.isLogin()) {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_attendanceDetail, Param.getAttendance_Detail(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), SessionManager.AttenDeeId, sessionManager.getUserId(), page_count), 0, false, this);
                } else {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_attendanceDetail, Param.getAttendance_Detail(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), SessionManager.AttenDeeId, "", page_count), 0, false, this);
                }
            } else {
                if (sessionManager.isLogin()) {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_attendanceDetail, Param.getAttendance_Detail(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), SessionManager.AttenDeeId, sessionManager.getUserId(), page_count), 0, false, this);
                } else {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_attendanceDetail, Param.getAttendance_Detail(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), SessionManager.AttenDeeId, "", page_count), 0, false, this);
                }
            }

        } else {
//            messageArrayList.clear();
            attndeeDetailExhibitorLists.clear();
            Cursor cursor = sqLiteDatabaseHandler.getAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), SessionManager.AttenDeeId, Tag);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jArrayevent = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.AtteendeeSpeakerDetail_Data)));
                        loadData(jArrayevent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                layout_data.setVisibility(View.GONE);
                card_noattendee.setVisibility(View.VISIBLE);
            }
        }

    }

    private void loadData(JSONObject json_data) {
        try {
            JSONArray jArray_attendetail = json_data.getJSONArray("attendee_details");
            str_unReadcount = json_data.getString("unread_count");
            allow_meeting_exibitor_to_attendee = json_data.getString("allow_meeting_exibitor_to_attendee");
            attendee_hide_request_meeting = json_data.getString("attendee_hide_request_meeting");
            str_game_is_on = json_data.getString("game_is_on");
            message_permisson = json_data.getString("message_permisson");
            show_send_message = json_data.getString("show_send_message");
            show_send_request = json_data.getString("show_send_request");
            enable_block_button = json_data.getString("enable_block_button");
            str_goal = json_data.getString("goal");

            str_game_points = json_data.getString("game_points");

            isBlocked = json_data.getString("is_blocked");
            blockeByMe = json_data.getString("blocked_by_me");

            if (enable_block_button.equalsIgnoreCase("1") && sessionManager.isLogin() && !sessionManager.getUserId().equalsIgnoreCase(attendee_id)) {
                setBlockButton();
            } else {
                btn_Block.setVisibility(View.GONE);
            }

            if (str_goal.isEmpty()) {
                linear_goals.setVisibility(View.GONE);
            } else {
                linear_goals.setVisibility(View.VISIBLE);
                txt_goals.setText(str_goal);
            }


            if (sessionManager.isLogin()) {
                if (GlobalData.checkForUIDVersion()) {
                    if (sessionManager.getfavIsEnabled().equalsIgnoreCase("1")
                            && uidCommonKeyClass.getIsOnlyAttendeeUser().equalsIgnoreCase("1")) {
                        if (sessionManager.getUserId().equalsIgnoreCase(attendee_id)) {
                            attendee_fav.setVisibility(View.GONE);
                        } else {
                            attendee_fav.setVisibility(View.VISIBLE);
                        }
                    } else {
                        attendee_fav.setVisibility(View.GONE);
                    }
                } else {
                    if (sessionManager.getfavIsEnabled().equalsIgnoreCase("1")
                            && sessionManager.getRolId().equalsIgnoreCase("4")) {//changes applied
                        if (sessionManager.getUserId().equalsIgnoreCase(attendee_id)) {
                            attendee_fav.setVisibility(View.GONE);
                        } else {
                            attendee_fav.setVisibility(View.VISIBLE);
                        }
                    } else {
                        attendee_fav.setVisibility(View.GONE);
                    }
                }

            } else {
                attendee_fav.setVisibility(View.GONE);
            }


            if (sessionManager.getUserId().equalsIgnoreCase(SessionManager.AttenDeeId)) {
                btn_sendMessageCounter.setVisibility(View.GONE);
            } else {

                if (GlobalData.checkForUIDVersion()) {

                    if (uidCommonKeyClass.getAttendeeMessagePermission().equalsIgnoreCase("1")) {
                        if (uidCommonKeyClass.getAttendeeShowSendMessage().equalsIgnoreCase("1")) {
                            btn_sendMessageCounter.setVisibility(View.VISIBLE);
                        } else {
                            btn_sendMessageCounter.setVisibility(View.GONE);
                        }
                    } else {
                        btn_sendMessageCounter.setVisibility(View.GONE);
                    }

                } else {
                    if (sessionManager.getRolId().equalsIgnoreCase("4")) {//changes applied
                        if (message_permisson.equalsIgnoreCase("1")) {
                            if (show_send_message.equalsIgnoreCase("1")) {
                                btn_sendMessageCounter.setVisibility(View.VISIBLE);
                            } else {
                                btn_sendMessageCounter.setVisibility(View.GONE);
                            }
                        } else {
                            btn_sendMessageCounter.setVisibility(View.GONE);
                        }
                    } else {
                        btn_sendMessageCounter.setVisibility(View.VISIBLE);
                    }
                }
            }


            if (str_game_is_on.equalsIgnoreCase("1")) {
                if (str_game_points.equalsIgnoreCase("0")) {
                    txt_points.setVisibility(View.GONE);
                } else {
                    txt_points.setVisibility(View.GONE);
                    txt_points.setText("Gamification Points : " + str_game_points);
                }
            } else {
                txt_points.setVisibility(View.GONE);
            }


            setUpFavoriteIcon(json_data);

//            if (str_unReadcount.equalsIgnoreCase("0")) {
//                txt_badgeCount.setVisibility(View.GONE);
//            } else {
//                txt_badgeCount.setVisibility(View.GONE);
//                txt_badgeCount.setText("" + str_unReadcount);
//            }

            if (jArray_attendetail.length() == 0) {
                layout_data.setVisibility(View.GONE);
                card_noattendee.setVisibility(View.VISIBLE);
            } else {

                layout_data.setVisibility(View.VISIBLE);
                card_noattendee.setVisibility(View.GONE);

                for (int i = 0; i < jArray_attendetail.length(); i++) {
                    JSONObject json_attendetail = jArray_attendetail.getJSONObject(i);
                    attendee_id = json_attendetail.getString("Id");
                    first_name = json_attendetail.getString("Firstname");
                    last_name = json_attendetail.getString("Lastname");
                    company_name = json_attendetail.getString("Company_name");
                    title = json_attendetail.getString("Title");
                    logo = json_attendetail.getString("Logo");
                    fblink = json_attendetail.getString("Facebook_url");
                    twitterLink = json_attendetail.getString("Twitter_url");
                    linkedInLink = json_attendetail.getString("Linkedin_url");
                    approvedStatus = json_attendetail.getString("approval_status");
                    biography = json_attendetail.getString("Biography");


                    attndeeDetailExhibitorLists = new ArrayList<>();
                    JSONArray jContactArray = json_attendetail.getJSONArray("contact_details");
                    JSONArray jExhibitorContact = json_attendetail.getJSONArray("linked_exhibitors");

                    for (int e = 0; e < jExhibitorContact.length(); e++) {
                        JSONObject indexExhi = jExhibitorContact.getJSONObject(e);
                        attndeeDetailExhibitorLists.add(new AttndeeDetailExhibitorList(indexExhi.getString("exhibitor_page_id"),
                                indexExhi.getString("Heading"),
                                indexExhi.getString("company_logo"),
                                indexExhi.getString("stand_number"),
                                indexExhi.getString("exhibitor_id")));
                    }


                    if (attndeeDetailExhibitorLists.size() != 0) {
                        rv_viewExhibitor.setVisibility(View.VISIBLE);
                        ArrayList<AttndeeDetailExhibitorList> arrayList = new ArrayList<>();
                        arrayList.add(attndeeDetailExhibitorLists.get(0));
                        exhibitorListAdapter = new AttendeeDetailExhibitorListAdapter(arrayList, getActivity());
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        rv_viewExhibitor.setLayoutManager(mLayoutManager);
                        rv_viewExhibitor.setItemAnimator(new DefaultItemAnimator());
                        rv_viewExhibitor.setAdapter(exhibitorListAdapter);
                    } else {
                        rv_viewExhibitor.setVisibility(View.GONE);
                    }
                }


                if (sessionManager.isLogin()) {
                    if (attendee_hide_request_meeting.equalsIgnoreCase("0")
                            || allow_meeting_exibitor_to_attendee.equalsIgnoreCase("0")) {
                        btn_request.setVisibility(View.GONE);
                    } else {
                        if (GlobalData.checkForUIDVersion()) {

//                            if (uidCommonKeyClass.getAttendeeShowRequestMeeting()
//                                    .equalsIgnoreCase("1")) {
                            if (uidCommonKeyClass.getAttendeeMessagePermission().equalsIgnoreCase("1")) {
                                if (sessionManager.getUserId().equalsIgnoreCase(attendee_id)) {
                                    btn_seeMyMetting.setVisibility(View.VISIBLE);
                                    btn_request.setVisibility(View.GONE);
                                } else {
                                    btn_seeMyMetting.setVisibility(View.GONE);
                                    if (uidCommonKeyClass.getAttendeeShowSendRequest().equalsIgnoreCase("1")) {
                                        btn_request.setVisibility(View.VISIBLE);
                                    } else {
                                        btn_request.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                btn_seeMyMetting.setVisibility(View.GONE);
                                btn_request.setVisibility(View.GONE);
                            }

//                            } else {
//                                btn_seeMyMetting.setVisibility(View.GONE);
//                                btn_request.setVisibility(View.GONE);
//                            }


                        } else {

                            if (sessionManager.getRolId().equalsIgnoreCase("4") ||    //changes applied
                                    sessionManager.getRolId().equalsIgnoreCase("6")) {  //changes applied
                                if (message_permisson.equalsIgnoreCase("1")) {
                                    if (sessionManager.getUserId().equalsIgnoreCase(attendee_id)) {
                                        btn_seeMyMetting.setVisibility(View.VISIBLE);
                                        btn_request.setVisibility(View.GONE);
                                    } else {
                                        btn_seeMyMetting.setVisibility(View.GONE);
                                        if (show_send_request.equalsIgnoreCase("1")) {
                                            btn_request.setVisibility(View.VISIBLE);
                                        } else {
                                            btn_request.setVisibility(View.GONE);
                                        }
                                    }
                                } else {
                                    btn_seeMyMetting.setVisibility(View.GONE);
                                    btn_request.setVisibility(View.GONE);
                                }

                            } else {
                                btn_seeMyMetting.setVisibility(View.GONE);
                                btn_request.setVisibility(View.GONE);
                            }
                        }

                    }

                }
                if (!(fblink.equalsIgnoreCase(""))) {
                    btn_fb.setVisibility(View.VISIBLE);
                } else {
                    btn_fb.setVisibility(View.GONE);
                }
                if (!(twitterLink.equalsIgnoreCase(""))) {
                    btn_twitter.setVisibility(View.VISIBLE);
                } else {
                    btn_twitter.setVisibility(View.GONE);
                }
                if (!(linkedInLink.equalsIgnoreCase(""))) {
                    btn_linkin.setVisibility(View.VISIBLE);
                } else {
                    btn_linkin.setVisibility(View.GONE);
                }


                if (biography.equalsIgnoreCase("")) {
                    txt_biography.setVisibility(View.GONE);
                } else {
                    txt_biography.setVisibility(View.VISIBLE);
                    txt_biography.setText(biography);

                }

//
                txt_fullname.setText(first_name + " " + last_name);
                if (!(title.equalsIgnoreCase("") || company_name.equalsIgnoreCase(""))) {
                    designation = title + "\n" + company_name;
                    txt_designation.setVisibility(View.VISIBLE);
                    txt_designation.setText(designation);
                } else {
                    txt_designation.setVisibility(View.GONE);
                }
                Log.d("AITL", "Attendee Detail" + MyUrls.Imgurl + logo);
                final String imgpath = MyUrls.Imgurl + logo;
                //    Glide.with(getActivity()).load(MyUrls.Imgurl + logo).centerCrop().fitCenter().placeholder(R.drawable.defult_attende).into(attendee_img);
                if (logo.equalsIgnoreCase("")) {
                    progressBar1.setVisibility(View.GONE);
                    attendee_img.setVisibility(View.GONE);
                    txt_profileName.setVisibility(View.VISIBLE);
                    GradientDrawable drawable = new GradientDrawable();
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    Log.d("AITL SPEAKER Color", "" + color);


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
                            attendee_img.setVisibility(View.GONE);
                            txt_profileName.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar1.setVisibility(View.GONE);
                            attendee_img.setVisibility(View.VISIBLE);
                            txt_profileName.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(new BitmapImageViewTarget(attendee_img) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            attendee_img.setImageBitmap(RoundedImageConverter.getRoundedCornerBitmap(resource, Color.WHITE, 80, 0, getContext()));
                        }
                    });
                }
            }

            if (isBlocked.equalsIgnoreCase("1")) {
                btn_request.setVisibility(View.GONE);
                btn_sendMessageCounter.setVisibility(View.GONE);
//                if(sessionManager.getUserId().equalsIgnoreCase(attendee_id) ){
//                    btn_seeMyMetting.setVisibility(View.VISIBLE);
//                }else {
//                    btn_seeMyMetting.setVisibility(View.GONE);
//                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpFavoriteIcon(JSONObject json_data) {
        try {
            String topColor = "#FFFFFF";
            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                topColor = sessionManager.getFunTopBackColor();
            } else {
                topColor = sessionManager.getTopBackColor();
            }
            attendee_fav.setColorFilter(Color.parseColor(topColor));
            if (json_data.getString("my_faviorite").equalsIgnoreCase("1")) {
                attendee_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_selected));
            } else {
                attendee_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_normal));
            }
        } catch (JSONException e) {
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
//                        messageArrayList=new ArrayList<>();
                        attndeeDetailExhibitorLists = new ArrayList<>();
                        JSONObject json_data = jsonObject.getJSONObject("data");
                        loadData(json_data);
                        if (sqLiteDatabaseHandler.isAttdeeSpeaker_DetailExist(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), SessionManager.AttenDeeId, Tag)) {
                            sqLiteDatabaseHandler.deleteAttdeeSpeaker_DetailData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), SessionManager.AttenDeeId, Tag);
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), json_data.toString(), SessionManager.AttenDeeId, Tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), json_data.toString(), SessionManager.AttenDeeId, Tag);
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
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                        JSONObject json_data = jsonObject.getJSONObject("data");
                        if (sqLiteDatabaseHandler.isAttdeeSpeaker_DetailExist(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), SessionManager.AttenDeeId, Tag)) {
                            sqLiteDatabaseHandler.deleteAttdeeSpeaker_DetailData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), SessionManager.AttenDeeId, Tag);
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), json_data.toString(), SessionManager.AttenDeeId, Tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), json_data.toString(), SessionManager.AttenDeeId, Tag);
                        }


                        GlobalData.AttendeeFavListngData(getActivity(), sessionManager.AttenDeeId, json_data.getString("my_faviorite"));
                        setUpFavoriteIcon(json_data);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragmentTag;
                        ((MainActivity) getActivity()).loadFragment();
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
                        JSONObject json_data = jsonObject.getJSONObject("data");

                        loadData(json_data);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
//                        Log.d("AITL DATETIME",jsonObject.toString());
                        if (jsonObject.getString("flag").equalsIgnoreCase("1")) {
                            ToastC.show(getActivity(), jsonObject.getString("message"));
                        } else {
                            jsonDateData = jsonObject;
                            if (jsonDateData.length() != 0) {

                                Bundle bundle = new Bundle();
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                bundle.putString("attendeeName", first_name + " " + last_name);
                                bundle.putString("attendeeId", sessionManager.AttenDeeId);
                                bundle.putString("jsonDateData", jsonDateData.toString());
                                AttendeeRequestMettingDialog fragment = new AttendeeRequestMettingDialog();
                                fragment.setArguments(bundle);
                                fragment.show(fm, "MettingDailog");
                            } else {
                                ToastC.show(getActivity(), "Please Wait....");
                            }

                        }
//                        if(isMaxMeeting)
//                        {
//
//                        }else {
//                            ToastC.show(getActivity(),""+Message);
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
//                        Log.d("AITL DATETIME",jsonObject.toString());
//                        jsonDateData = jsonObject;
                        viewMessageApi();
                       /* if(blockeByMe.equalsIgnoreCase("1")){
                            blockeByMe = "0";
                        }else if(blockeByMe.equalsIgnoreCase("0")){
                            blockeByMe="1";
                        }
                        setBlockButton();*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    public void setBlockButton() {
        btn_Block.setPaintFlags(btn_Block.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (blockeByMe.equalsIgnoreCase("0")) {
            btn_Block.setText("BLOCK CONTACT");
        } else {
            btn_Block.setText("UNBLOCK CONTACT");
        }
        btn_Block.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Update_Profile));

    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(broadcastReceiver);

        super.onPause();
    }
}
