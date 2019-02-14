package com.allintheloop;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Activity.LoginMainScreen;
import com.allintheloop.Activity.SearchApp_Activity;
import com.allintheloop.Activity.SuperActivity;
import com.allintheloop.Adapter.Adapter_notiList;
import com.allintheloop.Adapter.DrawerItemAdapter;
import com.allintheloop.Adapter.LanguageAdapter;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.AgendaData.AgendaCategory;
import com.allintheloop.Bean.AgendaData.AgendaData;
import com.allintheloop.Bean.CmsGroupData.CmsListandDetailList;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.DrawerItem;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorLead_MyLeadData_Offline;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorOfflineData;
import com.allintheloop.Bean.GeoLocation.EventBusGeoLocationData;
import com.allintheloop.Bean.GeoLocation.GeoLocationData;
import com.allintheloop.Bean.GeoLocation.MyLocationListener;
import com.allintheloop.Bean.GroupingData.GroupModuleData;
import com.allintheloop.Bean.GroupingData.GrouppingOfflineList;
import com.allintheloop.Bean.HomePageDynamicImageArray;
import com.allintheloop.Bean.HomeScreenMapDetailData;
import com.allintheloop.Bean.IdArray;
import com.allintheloop.Bean.Languages;
import com.allintheloop.Bean.Map.MapCoordinatesDetails;
import com.allintheloop.Bean.Map.MapListData;
import com.allintheloop.Bean.NotificationData;
import com.allintheloop.Bean.Speaker.SpeakerListMainClass;
import com.allintheloop.Bean.SponsorClass.SponsorMainListClasss;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.Fragment.ActivityModule.ActivityCommentView_Fragment;
import com.allintheloop.Fragment.ActivityModule.ActivityDetailAllInOneModule_Fragment;
import com.allintheloop.Fragment.ActivityModule.ActivitySharePost_Fragment;
import com.allintheloop.Fragment.ActivityModule.Activtiy_ModuleAllInoneDesign_Fragment;
import com.allintheloop.Fragment.AgendaModule.AgendaListFragment;
import com.allintheloop.Fragment.AgendaModule.Agenda_Bookstatus_Fragment;
import com.allintheloop.Fragment.AgendaModule.View_Agenda_Fragment;
import com.allintheloop.Fragment.AgendaModule.View_PendingAgenda_Fragment;
import com.allintheloop.Fragment.AgendaModule.View_userWise_Agenda;
import com.allintheloop.Fragment.Agenda_Fragment;
import com.allintheloop.Fragment.AttandeeFragments.Attendance_Detail_Fragment;
import com.allintheloop.Fragment.AttandeeFragments.AttendeeFullDirectory_Fragment;
import com.allintheloop.Fragment.AttandeeFragments.AttendeeMainCategoryFragment;
import com.allintheloop.Fragment.BeaconModule.BeaconFinder_Fragment;
import com.allintheloop.Fragment.BeaconModule.Beacon_FindDeviceListing_Fragment;
import com.allintheloop.Fragment.CheckIn_Portal_Fragment;
import com.allintheloop.Fragment.Document_Fragment;
import com.allintheloop.Fragment.EditProfileModule.Attendee_ProfileEdit_Fragment;
import com.allintheloop.Fragment.EditProfileModule.Edit_Profile_Tab;
import com.allintheloop.Fragment.EditProfileModule.Profile_FragmentDialog;
import com.allintheloop.Fragment.ExhibitorFragment.ExhibitorList_Fragment_New;
import com.allintheloop.Fragment.ExhibitorFragment.ExhibitorListingFromSubCategoryList;
import com.allintheloop.Fragment.ExhibitorFragment.ExhibitorSubCategory_Fragment;
import com.allintheloop.Fragment.ExhibitorFragment.Exhibitor_Detail_Fragment;
import com.allintheloop.Fragment.ExhibitorFragment.Exhibitor_Lead_Fragment;
import com.allintheloop.Fragment.ExhibitorFragment.Exhibitor_ParentCategoryList_Fragment;
import com.allintheloop.Fragment.FacebookModule.FacebookFeed_Fragment;
import com.allintheloop.Fragment.Favourite_FragmentList;
import com.allintheloop.Fragment.FundraisingModule.AddItem_Fragment;
import com.allintheloop.Fragment.FundraisingModule.CartDetail_Fragment;
import com.allintheloop.Fragment.FundraisingModule.CheckOut_Fragment;
import com.allintheloop.Fragment.FundraisingModule.EditItemFragment;
import com.allintheloop.Fragment.FundraisingModule.Fundraising_Donation_Fragment;
import com.allintheloop.Fragment.FundraisingModule.Fundrising_Home_Fragment;
import com.allintheloop.Fragment.FundraisingModule.Item_Fragment_Tab;
import com.allintheloop.Fragment.FundraisingModule.Order_FragmentTab;
import com.allintheloop.Fragment.Fundraising_auctionModule.LiveAuctionDetail;
import com.allintheloop.Fragment.Fundraising_auctionModule.OnlineShop_detail;
import com.allintheloop.Fragment.Fundraising_auctionModule.Pledge_Detail_Fragment;
import com.allintheloop.Fragment.Fundraising_auctionModule.SilentAuction_Fragment;
import com.allintheloop.Fragment.Fundraising_auctionModule.SilentAuction_ProductDetail;
import com.allintheloop.Fragment.GamiiFication_Fragment;
import com.allintheloop.Fragment.GroupModuleList.AgendaGroupListFragment;
import com.allintheloop.Fragment.GroupModuleList.CMSChildGroupFragment;
import com.allintheloop.Fragment.GroupModuleList.CMSListFragment;
import com.allintheloop.Fragment.GroupModuleList.CMSMainGroupListing;
import com.allintheloop.Fragment.GroupModuleList.MapGroupListFragment;
import com.allintheloop.Fragment.GroupModuleList.QandAGroupFragment;
import com.allintheloop.Fragment.GroupModuleList.SponsorGroupListFragment;
import com.allintheloop.Fragment.HomeFragment;
import com.allintheloop.Fragment.InstagramModule.InstagramFeed_Fragment;
import com.allintheloop.Fragment.MapModule.Map_Detail_Fragment;
import com.allintheloop.Fragment.MapModule.Map_Fragment;
import com.allintheloop.Fragment.MapModule.New_MapDetail_Fragment;
import com.allintheloop.Fragment.MatchmakingTab_Fragment;
import com.allintheloop.Fragment.NoteDetail_Fragment;
import com.allintheloop.Fragment.Notes_fragment;
import com.allintheloop.Fragment.NotificationListing_Fragment;
import com.allintheloop.Fragment.OpenPdfFragment;
import com.allintheloop.Fragment.PhotoFilter.PhotoFilter_Fragment;
import com.allintheloop.Fragment.PhotoFilter.PhotoFilter_filterListing;
import com.allintheloop.Fragment.PhotoFilter.PhotoFilter_seeMyPhotoFragment;
import com.allintheloop.Fragment.Photo_Fragment;
import com.allintheloop.Fragment.PresantationModule.Presantation_Detail_Fragment;
import com.allintheloop.Fragment.PresantationModule.Presentation_Fragment;
import com.allintheloop.Fragment.PrivateMessage.PrivateMessageDetail_Fragment;
import com.allintheloop.Fragment.PrivateMessage.PrivateMessageListing_Fragment;
import com.allintheloop.Fragment.PrivateMessage.PrivateMessageViewProfile_Fragment;
import com.allintheloop.Fragment.PrivateMessage.Private_Message_Fragment;
import com.allintheloop.Fragment.PrivateMessage.StartPrivateMessageConversion_Fragment;
import com.allintheloop.Fragment.PublicMessage_Fragment;
import com.allintheloop.Fragment.QandAModule.QADetailModule_Fragment;
import com.allintheloop.Fragment.QandAModule.QAHideQuestionModule_Fragment;
import com.allintheloop.Fragment.QandAModule.QAInstructionModule_Fragment;
import com.allintheloop.Fragment.QandAModule.QAModule_Fragment;
import com.allintheloop.Fragment.QrCodeScanner_ShareContact_Fragment;
import com.allintheloop.Fragment.RequestMeetingModule.RequestMeetingInviteMore_Fragment;
import com.allintheloop.Fragment.RequestMeetingModule.RequestMettingListFragment_newModule;
import com.allintheloop.Fragment.RequestMeetingModule.ViewRequestedMettingDateTimeList_Fragment;
import com.allintheloop.Fragment.Social_Fragment;
import com.allintheloop.Fragment.SpeakerList_newFragment;
import com.allintheloop.Fragment.Speaker_Detail_Fragment;
import com.allintheloop.Fragment.SponsorListFragment;
import com.allintheloop.Fragment.Sponsor_Detail_Fragment;
import com.allintheloop.Fragment.SurveyModule.SurveyCategorytListingFragment;
import com.allintheloop.Fragment.SurveyModule.Survey_Fragment;
import com.allintheloop.Fragment.TwitterFeed_Fragment;
import com.allintheloop.Fragment.TwitterHashTagListingFragment;
import com.allintheloop.Fragment.View_Note_Fragment;
import com.allintheloop.Fragment.VirtualSuperMarker_Fragment;
import com.allintheloop.Fragment.cms.Test1_Fragment;
import com.allintheloop.Fragment.cms.Webview_Fragment;
import com.allintheloop.Util.AppController;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MenuId;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.NetworkChangeReceiverNougat;
import com.allintheloop.Util.NetworkExhibitorReceiverNougat;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.linkedin.platform.LISessionManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.crosswall.photo.pick.PickConfig;

import static com.allintheloop.Util.GlobalData.NotificationsimpleDialog;
import static com.allintheloop.Util.GlobalData.NotificationsimpleDialogwithAction;
import static com.allintheloop.Util.GlobalData.UpdateCounterFromNotification;
import static com.allintheloop.Util.GlobalData.Update_Profile;
import static com.allintheloop.Util.GlobalData.getexhibitorMyLeadUpdate;
import static com.allintheloop.Util.GlobalData.logoutNoti;

public class MainActivity extends SuperActivity implements VolleyInterface {
    public static int mSelectedItem = 2;
    public static FragmentManager fragmentManager;
    public static CallbackManager callbackManager;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public CircleImageView user_profile_image;
    public RelativeLayout badge_layout;
    public ImageView img_cart;
    public TextView txt_cart_badge;
    public FrameLayout frme_cart;
    public int selectedTabCount = 0;
    public LocationManager locationManager;
    public MyLocationListener listener;
    TextView userName;
    ArrayList<DrawerItem> ObjItem;
    ArrayList<IdArray> idArrays;
    ArrayList<NotificationData> notificationDataArrayList;
    ArrayList<HomePageDynamicImageArray> listImageArray;
    ArrayList<HomeScreenMapDetailData> listImageDetailArray;
    ArrayList<List<MapCoordinatesDetails>> rectanglesList = new ArrayList<>();
    ArrayList<Languages> languagesArray = new ArrayList<>();
    ImageView img_lang;
    ProgressBar progressBar1;
    ListView mDrawerList;
    DrawerItemAdapter itemAdapter;
    Menu menu;
    Toolbar toolbar;
    ImageView edtnoteImg, img_noti, dailog_noti_close;
    TextView login, txt_noti_badge, txt_link, txt_notiPrivate, txt_notipublic, txt_notiSystem, dailog_noti_title;
    RelativeLayout notification_layout;
    FrameLayout frme_noti, frame_userProfile;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    FragmentTransaction transaction;
    SessionManager sessionManager;
    String notiMessage_type = "", str_showMessageBell = "", requestMettingtag = "", defualtLangId = "", top = "", left = "", right = "", bottom = "", firstName = "", img_url = "", mnuId = "", mnuName = "", cmsId = "", cmsmnuName = "", notes_status = "", noticount = "", cmsposition = "", Eid = "", Token_id = "", imgpath = "", event_Type = "";
    Timer Notitimer;
    Dialog noti_dailog;
    RecyclerView rv_viewNotification;
    Adapter_notiList adapterNotiList;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Cursor cursor;
    LinearLayout linear_dynamicHome;
    Paint paint;
    ScrollView scrollView;
    LanguageAdapter languageAdapter;
    DefaultLanguage.DefaultLang defaultLang;
    MenuItem menuItemMyProfile, menuItemMyMetting, menuItemLogout, menuItemCheckInPortal, menuItemAddNote;
    ProgressBar progressBarImage;
    String updateDate = "", groupUpdateDate = "";
    String notificationId = "";
    ArrayList<GeoLocationData.GeoLocationList> locationLists;
    int position = 0;
    UidCommonKeyClass uidCommonKeyClass;
    BroadcastReceiver changeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Build.VERSION.SDK_INT >= 24) {
                new NetworkExhibitorReceiverNougat(MainActivity.this).updateLeadData();
                new NetworkChangeReceiverNougat(MainActivity.this).updateData();
            }
        }
    };
    private LoginManager loginManager;
    private ProgressDialog mProgressDialog;
    private BroadcastReceiver simpleDialogBroadCast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (sessionManager.isLogin()) {
                MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .title(sessionManager.notiTitle)
                        .content(sessionManager.notiMessage)
                        .positiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                dialog.dismiss();
//                        if (messageType.equalsIgnoreCase("Private") || messageType)
                            }
                        }).show();
                dialog.setCancelable(false);
            }

        }
    };
    // notify Simple dialog when Notification Occur
    private BroadcastReceiver simpleWithActionDialogBroadCast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

            if (sessionManager.isLogin()) {
                dailogClickwithaction(context);
            }

        }
    };
    // notify for Exhibitor OfflineData Download
    private BroadcastReceiver updateExhitiorMyLeadData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (sessionManager.isLogin()) {
                getExhibitorLeadOffline();
            }
        }
    };
    // notify for logout user
    private BroadcastReceiver notificationBroadcasr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (sessionManager.isLogin()) {
                sessionManager.logout();
                startActivity(new Intent(MainActivity.this, SearchApp_Activity.class));
                finish();
            }

        }
    };
    // notify for UpdateNotification Counter
    private BroadcastReceiver UpdateNotificationCounter = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (sessionManager.isLogin()) {
                if (str_showMessageBell.equalsIgnoreCase("1")) {
                    getNotificationCount();
                }
            }

        }
    };
    // notify for when Organizer Update User profile Image
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (sessionManager.getNotification_UserId().equalsIgnoreCase(sessionManager.getUserId())) {
                sessionManager.setImagePath(sessionManager.getUserProfile());

                Glide.with(getApplicationContext()).load(MyUrls.Imgurl + sessionManager.getUserProfile()).into(user_profile_image);
                if (Edit_Profile_Tab.imageView != null) {
                    Glide.with(getApplicationContext()).load(MyUrls.Imgurl + sessionManager.getUserProfile()).into(Edit_Profile_Tab.imageView);
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Notitimer = new Timer();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        listImageArray = new ArrayList<>();
        listImageDetailArray = new ArrayList<>();
        languagesArray = new ArrayList<>();
        linear_dynamicHome = (LinearLayout) findViewById(R.id.linear_dynamicHome);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        progressBarImage = (ProgressBar) findViewById(R.id.progressBarImage);
        fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        //*************************************
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        sessionManager = new SessionManager(getApplicationContext());
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();

        defaultLang = sessionManager.getMultiLangString();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getApplicationContext());
        toggle.setDrawerIndicatorEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_menu);

        idArrays = new ArrayList<>();
        ObjItem = new ArrayList<>();

        toggle.syncState();
        //*************************************
        notificationDataArrayList = new ArrayList<>();
        badge_layout = (RelativeLayout) findViewById(R.id.cart_layout);
        notification_layout = (RelativeLayout) findViewById(R.id.notification_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        userName = (TextView) toolbar.findViewById(R.id.username);
        txt_link = (TextView) findViewById(R.id.txt_link);
        user_profile_image = (CircleImageView) toolbar.findViewById(R.id.user_profile_image);
        progressBar1 = (ProgressBar) toolbar.findViewById(R.id.progressBar1);
        frame_userProfile = (FrameLayout) toolbar.findViewById(R.id.frame_userProfile);
        login = (TextView) toolbar.findViewById(R.id.Login);

        txt_cart_badge = (TextView) toolbar.findViewById(R.id.txt_cart_badge);
        txt_noti_badge = (TextView) toolbar.findViewById(R.id.txt_noti_badge);


        frme_cart = (FrameLayout) toolbar.findViewById(R.id.frme_cart);
        frme_noti = (FrameLayout) toolbar.findViewById(R.id.frme_noti);

        edtnoteImg = (ImageView) toolbar.findViewById(R.id.edt_note);
        img_cart = (ImageView) toolbar.findViewById(R.id.img_cart);
        img_noti = (ImageView) toolbar.findViewById(R.id.img_noti);
        img_lang = (ImageView) toolbar.findViewById(R.id.img_lang);

        edtnoteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteDetailDialog();
            }
        });

        defualtLangId = sessionManager.getLangId();
        setLangImg(sessionManager.getLangImgUrl());
        mDrawerList.setFriction(ViewConfiguration.getScrollFriction() * 5);
        Eid = sessionManager.getEventId();
        Token_id = sessionManager.getToken();  //71dd07494c5ee54992a27746d547e25dee01bd97
        firstName = sessionManager.getFirstName();
        imgpath = sessionManager.getImagePath();
        event_Type = sessionManager.getEventType();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginMainScreen.class));
                finish();
            }
        });

        txt_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.ExhibiorLead_Fragment;
                loadFragment();
            }
        });

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DrawerItem DrawerObj = ObjItem.get(position);
                            cmsposition = DrawerObj.getId().toString();
                            sessionManager.setMenuid(DrawerObj.getId().toString());
                            mSelectedItem = position;
                            sessionManager.set_isForceLogin(DrawerObj.getIs_forceLogin());
                            SelectedItem(DrawerObj.getId().toString(), DrawerObj.getType(), DrawerObj);
                            itemAdapter.notifyDataSetChanged();
                        }
                    }, 100);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        if (sessionManager.isLogin()) {
            login.setVisibility(View.GONE);
            user_profile_image.setVisibility(View.VISIBLE);
            progressBar1.setVisibility(View.VISIBLE);
            userName.setVisibility(View.VISIBLE);
            edtnoteImg.setVisibility(View.VISIBLE);
            img_noti.setVisibility(View.VISIBLE);
            notification_layout.setVisibility(View.VISIBLE);
            txt_noti_badge.setVisibility(View.VISIBLE);
            crashAnlyticslogUser();
            getExhibitorLeadofllineData();

            Glide.with(getApplicationContext())
                    .load(img_url)
                    .fitCenter()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar1.setVisibility(View.GONE);
                            user_profile_image.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(img_url).centerCrop().fitCenter().placeholder(R.drawable.profile).into(user_profile_image);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar1.setVisibility(View.GONE);
                            user_profile_image.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(user_profile_image);

            userName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.PROFILE_FRAGMENT;
                    loadFragment();
                }
            });
            user_profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenEditDialog();
                }
            });


            userName.setText(firstName);

            img_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.CartDetailFragment;
                    loadFragment();
                }
            });

        }


        getList();


        if (GlobalData.isNetworkAvailable(getApplicationContext())) {
            if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {

                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                mSelectedItem = 2;
                loadFragment();
            } else {

//            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                mSelectedItem = 2;
                loadFragment();
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                        mSelectedItem = 2;
                        try {
                            loadFragment();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

//            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                        mSelectedItem = 2;
                        try {
                            loadFragment();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 1000);
        }

        img_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLangListDialog();
            }
        });
        img_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topNotiClick();
            }
        });
    }

    // TopBarNotificationClick
    private void topNotiClick() {
        noti_dailog = new Dialog(MainActivity.this);
        noti_dailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        noti_dailog.setContentView(R.layout.notification_dailog);
        txt_notiPrivate = (TextView) noti_dailog.findViewById(R.id.txt_notiPrivate);
        txt_notipublic = (TextView) noti_dailog.findViewById(R.id.txt_notipublic);
        notificationPrivatePublicHide();
        rv_viewNotification = (RecyclerView) noti_dailog.findViewById(R.id.rv_viewNotification);
        rv_viewNotification.setNestedScrollingEnabled(false);
        dailog_noti_close = (ImageView) noti_dailog.findViewById(R.id.dailog_noti_close);
        txt_notiSystem = (TextView) noti_dailog.findViewById(R.id.txt_notiSystem);
        dailog_noti_title = (TextView) noti_dailog.findViewById(R.id.dailog_noti_title);


        dailog_noti_title.setText(defaultLang.getBellIconNotification());
        txt_notiPrivate.setText(defaultLang.getBellIconSeeAllPrivate());
        txt_notipublic.setText(defaultLang.getBellIconSeeAllPublic());
        txt_notiSystem.setText(defaultLang.getBellIconPushNotifications());

        dailog_noti_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noti_dailog.dismiss();
            }
        });

        txt_notiPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notiMessage_type = "0";
                notificationSeen();
                noti_dailog.dismiss();
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.PRIVATE_MESSAGE;
                loadFragment();

            }
        });

        txt_notipublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notiMessage_type = "1";
                notificationSeen();
                noti_dailog.dismiss();
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                loadFragment();
            }
        });

        txt_notiSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noti_dailog.dismiss();
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.notificationListingFragment;
                loadFragment();
            }
        });

        noti_dailog.show();
        if (noticount.equalsIgnoreCase("0")) {

            rv_viewNotification.setVisibility(View.GONE);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = noti_dailog.getWindow();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        } else {

            if (notificationDataArrayList.size() != 0) {
                //  tmp_notificationDataArrayList.addAll(notificationDataArrayList);

                if (notificationDataArrayList.size() <= 5) {
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    Window window = noti_dailog.getWindow();
                    lp.copyFrom(window.getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    window.setAttributes(lp);
                } else {
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    Window window = noti_dailog.getWindow();
                    lp.copyFrom(window.getAttributes());
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    window.setAttributes(lp);
                }
                adapterNotiList = new Adapter_notiList(notificationDataArrayList, getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rv_viewNotification.setLayoutManager(mLayoutManager);
                rv_viewNotification.setItemAnimator(new DefaultItemAnimator());
                rv_viewNotification.setAdapter(adapterNotiList);

                rv_viewNotification.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        NotificationData notiObj = notificationDataArrayList.get(position);


                        if (notiObj.getIspublic().equalsIgnoreCase("0")) {
                            notiMessage_type = "0";
                            notificationSeen();
                            noti_dailog.dismiss();
                            sessionManager.setMenuid("12");
                            sessionManager.private_senderId = notiObj.getSender_id();
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                            loadFragment();

                        } else if (notiObj.getIspublic().equalsIgnoreCase("1")) {
                            notiMessage_type = "1";
                            notificationSeen();
                            noti_dailog.dismiss();
                            sessionManager.setMenuid("13");
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                            loadFragment();
                        } else if (notiObj.getIspublic().equalsIgnoreCase("2")) {
                            String sender_Id = notiObj.getSender_id();
                            notiMessage_type = "0";
                            notificationSeen();
                            attendeeAproved(sender_Id);
                            noti_dailog.dismiss();
                            attendeeRedirection(false, false);
                        } else if (notiObj.getIspublic().equalsIgnoreCase("3")) {
                            notiMessage_type = "0";
                            noti_dailog.dismiss();
                            notificationSeen();
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                            loadFragment();
                        } else if (notiObj.getIspublic().equalsIgnoreCase("4")) {
                            notiMessage_type = "0";
                            noti_dailog.dismiss();
                            notificationSeen();
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                            loadFragment();
                        } else if (notiObj.getIspublic().equalsIgnoreCase("5")) {
                            notiMessage_type = "0";
                            noti_dailog.dismiss();
                            notificationSeen();
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.viewSuggestedTimeList;
                            loadFragment();
                        } else if (notiObj.getIspublic().equalsIgnoreCase("6")) {
                            notiMessage_type = "0";
                            noti_dailog.dismiss();
                            notificationSeen();
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.moderatorRequestMettingFragment;
                            loadFragment();
                        }

                    }
                }));
            } else {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = noti_dailog.getWindow();
                lp.copyFrom(window.getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
            }

        }
    }

    private void getList() {
        if (GlobalData.isNetworkAvailable(MainActivity.this)) {
            cursor = sqLiteDatabaseHandler.getEventHomeData(sessionManager.getEventId());
            if (cursor.moveToFirst()) {

                try {
                    JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.Home_EventData)));
                    loadMenuData(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST,
                        MyUrls.home_pageIndexUid, Param.getDrawerList(Token_id, Eid,
                        "demo", event_Type, sessionManager.getUserId()), 0,
                        false, this);
            else
                new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST,
                        MyUrls.home_pageIndex, Param.getDrawerList(Token_id, Eid,
                        "demo", event_Type, sessionManager.getUserId()), 0,
                        false, this);
        } else {
            cursor = sqLiteDatabaseHandler.getEventHomeData(sessionManager.getEventId());
            if (cursor.moveToFirst()) {

                try {
                    JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.Home_EventData)));
                    loadMenuData(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void SelectedItem(String position, final String menuType, final DrawerItem dashboardItemListObj) {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (menuType.equalsIgnoreCase("menu")) {


            sessionManager.strMenuId = position;
            sessionManager.strModuleId = "0";
            sessionManager.isNoteCms = "0";
            switch (position) {
                case MenuId.HOME_ID:
                    if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {

                        GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                    } else {
                        GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                    }
                    break;
                case MenuId.AGENDA_ID:
                    if (dashboardItemListObj.getSub_groupId().isEmpty() && dashboardItemListObj.getCategoryId().isEmpty()) {

                        isAgendaGroupAndCategoryExistForSimpleLeft();
                    } else if (!dashboardItemListObj.getSub_groupId().isEmpty()) {
                        sessionManager.setAgendagroupID(dashboardItemListObj.getSub_groupId());
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.AgendaGroupWiseFragment;
                    } else {
                        sessionManager.setAgendaCategoryId(dashboardItemListObj.getCategoryId());
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.AGENDA_FRAGMENT;
                    }
                    break;
                case MenuId.ATTENDEE_ID:
                    attendeeRedirection(false, true);
                    break;
                case MenuId.EXIBITOR_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.EXHIBITOR_FRAGMENT;
                    break;
                case MenuId.SPEAKER_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.SPEAKER_FRAGMENT;
                    break;
                case MenuId.SPONSOR_ID:
                    if (dashboardItemListObj.getSub_groupId().isEmpty()) {
                        isSponsorGroupExistSimpleLeft();
                    } else {
                        sessionManager.setSponsorPrentGroupID(dashboardItemListObj.getSub_groupId());
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.SponsorGroupWiseFragment;
                    }
                    break;
                case MenuId.SOCIAL_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.SOCIAL_FRAGMENT;
                    break;
                case MenuId.MAP_ID:
                    sessionManager.Map_coords = "";
                    sessionManager.exhibitor_standNumber = "";
                    if (dashboardItemListObj.getSub_groupId().isEmpty()) {
                        sessionManager.setMapPrentGroupID("");
                        isMapGroupExistSimpleLeft();
                    } else {
                        sessionManager.setMapPrentGroupID(dashboardItemListObj.getSub_groupId());
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.MapGroupWiseFragment;
                    }
                    break;
                case MenuId.VIEW_NOTES_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.View_Note_Fragment;
                    break;
                case MenuId.TWITTER_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.TweitterFeed;
                    break;
                case MenuId.PRESENTATION_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Presantation_Fragment;
                    break;
                case MenuId.PRIVATE_MSG_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.PRIVATE_MESSAGE;

                    break;
                case MenuId.PUBLIC_MSG_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;

                    break;
                case MenuId.SURVEY_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Survey_Fragment;
                    break;
                case MenuId.PHOTOS_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Photo_Fragment;
                    break;
                case MenuId.DOCUMENT_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Document_Fragment;
                    break;
                case MenuId.FUNDRAISING_HOME_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                    break;
                case MenuId.SILENT_AUCTION_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Silent_Auction;
                    break;
                case MenuId.LIVE_AUCTION_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.live_Auction;
                    break;
                case MenuId.BUY_NOW_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Online_shop;
                    break;
                case MenuId.PLEDGE_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.DonateFor_veteran;
                    break;
                case MenuId.ACTIVITY_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.activityFeed;
                    break;
                case MenuId.INSTAGRAM_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.InstagramFeed;
                    break;
                case MenuId.FACEBOOK_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.facebookFeed;
                    break;
                case MenuId.FAVOURITES_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.FavoriteList_Fragment;
                    break;
                case MenuId.QA_SESSION_ID:
                    sessionManager.setQandAgroupID("");
                    if (dashboardItemListObj.getSub_groupId().isEmpty()) {
                        sessionManager.setQandAgroupID("");
                        if (dashboardItemListObj.getIs_contains_data().equalsIgnoreCase("1")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.QAList_Fragment;
                        } else if (dashboardItemListObj.getIs_contains_data().equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.QandAGroupWiseFragment;
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.QandAGroupWiseFragment;
                        }
                    } else {
                        sessionManager.setQandAgroupID(dashboardItemListObj.getSub_groupId());
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.QandAGroupWiseFragment;
                    }
                    break;
                case MenuId.VIRTUAL_SUPERMARKET_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.VirtualSuperMarket;
                    break;
                case MenuId.GAMIFICATION_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Gamification_Fragment;
                    break;
                case MenuId.LEAD_RETRIVAL_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.ExhibiorLead_Fragment;
                    break;
                case MenuId.PHOTO_FILTER_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.PhotoFilter_Fragment;
                    break;
                case MenuId.NOTIFICATION_CENTER_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.notificationListingFragment;
                    break;
                case MenuId.MY_MEETINGS_ID:
                    if (GlobalData.checkForUIDVersion())
                    {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                        loadFragment();
                    }
                    else
                    {
                        if (sessionManager.getRolId().equalsIgnoreCase("4")) {//postponed - pending
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                            loadFragment();
                        }
                        else if (sessionManager.getRolId().equalsIgnoreCase("6"))
                        {//postponed - pending
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                            loadFragment();
                        }
                        else if (sessionManager.getRolId().equalsIgnoreCase("7") && //changes applied
                                sessionManager.isModerater().equalsIgnoreCase("1")) {//changes applied
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.moderatorRequestMettingFragment;
                            loadFragment();

                        }
                    }
                    break;
                case MenuId.GROUP_ID:

                    break;
                case MenuId.CMS_ID:
                    if (dashboardItemListObj.getSup_group_id().isEmpty() && dashboardItemListObj.getSub_groupId().isEmpty()) {
                        sessionManager.setCmsSuperGroupId("");
                        sessionManager.setCmsChildGroupId("");
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.CMSGroupListFragment;
                    } else if (!(dashboardItemListObj.getSup_group_id().isEmpty())) {
                        sessionManager.setCmsSuperGroupId(dashboardItemListObj.getSup_group_id());
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.CMSGroupChildListFragment;
                    } else if (!(dashboardItemListObj.getSub_groupId().isEmpty())) {
                        sessionManager.setCmsChildGroupId(dashboardItemListObj.getSub_groupId());
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.CMSListFragment;
                    }
                    break;
                case MenuId.BADGE_SCANNER_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.QrcodeScannerSharecontactFragment;
                    break;
                case MenuId.MATCH_MAKING_ID:
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.MatchMakingFragment;
                    break;

            }
        } else if (menuType.equalsIgnoreCase("cms_menu")) {
            for (int i = 0; i < idArrays.size(); i++) {
                IdArray idObj = idArrays.get(i);
                if (idObj.getId().equals(position)) {
                    Log.d("AITL PostionCMS", idObj.getId().toString());
                    sessionManager.cms_id(position);
                    sessionManager.strMenuId = "0";
                    sessionManager.strModuleId = position;
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.CMS_FRAGMENT_TEST1;
                }
            }
        }
        loadFragment();
    }

    public void logoutUser()  // call Api for Logout
    {
        if (!sessionManager.getFacebookId().equals("")) {
            loginManager.getInstance().logOut();
        }
        LISessionManager.getInstance(MainActivity.this).clearSession();
        sessionManager.logout();
        startActivity(new Intent(MainActivity.this, SearchApp_Activity.class));
        finish();
        ToastC.show(MainActivity.this, getResources().getString(R.string.afterlogout));
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (!(getSupportActionBar().isShowing())) {
            getSupportActionBar().show();
            Log.d("AITL ACTION BAR", "NOT SHOW");
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            handleBackStack();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        defaultLang = sessionManager.getMultiLangString();
        this.menu = menu;

        if (defaultLang.get6AddNotes() == null) {
            defaultLang.set6AddNotes("Add Notes");
        }
        if (sessionManager.isLogin()) {

            if (GlobalData.checkForUIDVersion()) {

                if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {


                    if (uidCommonKeyClass.getShowCheckInPortalMenu().equalsIgnoreCase("1")) {
                        getMenuInflater().inflate(R.menu.checking_menu, menu);
                        Log.d("AITL Checking", defaultLang.getMyProfileCheckInPortal());

                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemAddNote = menu.findItem(R.id.addNote);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemCheckInPortal = menu.findItem(R.id.itm_checkIn);

                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());

                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                        menuItemCheckInPortal.setTitle(defaultLang.getMyProfileCheckInPortal());

                    } else if (uidCommonKeyClass.getShowMyMeetingsMenu().equalsIgnoreCase("1")) {

                        getMenuInflater().inflate(R.menu.requestmetting_menu, menu);

                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemAddNote = menu.findItem(R.id.addNote);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemMyMetting = menu.findItem(R.id.item_reuqestMeeting);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemMyMetting.setTitle(defaultLang.getMyProfileMyMeetings());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());

                    } else if (uidCommonKeyClass.getShowMyMeetingsMenu().equalsIgnoreCase("0")) {
                        getMenuInflater().inflate(R.menu.home_menu, menu);

                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());

                    } else if (uidCommonKeyClass.getShowBeaconMenu().equalsIgnoreCase("1")) {

                        getMenuInflater().inflate(R.menu.organization_login, menu);

                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                    } else {

                        getMenuInflater().inflate(R.menu.home_menu, menu);

                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                    }
                    //   }
                } else {

                    if (uidCommonKeyClass.getShowCheckInPortalMenu().equalsIgnoreCase("1")) {

                        getMenuInflater().inflate(R.menu.fund_checkinmenu, menu);

                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemCheckInPortal = menu.findItem(R.id.itm_checkIn);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                        menuItemCheckInPortal.setTitle(defaultLang.getMyProfileCheckInPortal());
                    } else if (uidCommonKeyClass.getShowMyMeetingsMenu().equalsIgnoreCase("1")) {

                        getMenuInflater().inflate(R.menu.requestmetting_menu, menu);

                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemMyMetting = menu.findItem(R.id.item_reuqestMeeting);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemMyMetting.setTitle(defaultLang.getMyProfileMyMeetings());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                    } else if (uidCommonKeyClass.getShowMyMeetingsMenu().equalsIgnoreCase("0")) {
                        getMenuInflater().inflate(R.menu.home_menu, menu);

                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());

                    } else if (uidCommonKeyClass.getShowBeaconMenu().equalsIgnoreCase("1")) {

                        getMenuInflater().inflate(R.menu.organization_login, menu);

                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                    } else {
                        getMenuInflater().inflate(R.menu.fund_homemenu, menu);
                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                    }
                }


            } else {


                if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {


                    if (sessionManager.getRolId().equalsIgnoreCase("95")) {//changes applied
                        getMenuInflater().inflate(R.menu.checking_menu, menu);
                        Log.d("AITL Checking", defaultLang.getMyProfileCheckInPortal());

                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemAddNote = menu.findItem(R.id.addNote);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemCheckInPortal = menu.findItem(R.id.itm_checkIn);

                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());

                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                        menuItemCheckInPortal.setTitle(defaultLang.getMyProfileCheckInPortal());

                    } else if (sessionManager.getRolId().equalsIgnoreCase("6")) {//changes applied
                        getMenuInflater().inflate(R.menu.requestmetting_menu, menu);
                        requestMettingtag = "1";
                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemAddNote = menu.findItem(R.id.addNote);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemMyMetting = menu.findItem(R.id.item_reuqestMeeting);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemMyMetting.setTitle(defaultLang.getMyProfileMyMeetings());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());


                    } else if (sessionManager.getRolId().equalsIgnoreCase("4")) {//changes applied

                        if (sessionManager.getAttendee_hide_request_meeting().equalsIgnoreCase("1")) {
                            requestMettingtag = "0";
                            getMenuInflater().inflate(R.menu.requestmetting_menu, menu);

                            menuItemMyProfile = menu.findItem(R.id.Profile);
                            menuItemLogout = menu.findItem(R.id.logout);
                            menuItemMyMetting = menu.findItem(R.id.item_reuqestMeeting);
                            menuItemAddNote = menu.findItem(R.id.addNote);

                            menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                            menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                            menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                            menuItemMyMetting.setTitle(defaultLang.getMyProfileMyMeetings());
                        } else if (sessionManager.getAttendee_hide_request_meeting().equalsIgnoreCase("0")) {
                            getMenuInflater().inflate(R.menu.home_menu, menu);

                            menuItemMyProfile = menu.findItem(R.id.Profile);
                            menuItemLogout = menu.findItem(R.id.logout);
                            menuItemAddNote = menu.findItem(R.id.addNote);

                            menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                            menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                            menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                        }
                    } else if (sessionManager.getRolId().equalsIgnoreCase("3")) {//changes applied

                        getMenuInflater().inflate(R.menu.organization_login, menu);


                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                    } else if (sessionManager.getRolId().equalsIgnoreCase("7") && //changes applied
                            sessionManager.isModerater().equalsIgnoreCase("1")) {//changes applied


                        getMenuInflater().inflate(R.menu.requestmetting_menu, menu);

                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemMyMetting = menu.findItem(R.id.item_reuqestMeeting);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                        menuItemMyMetting.setTitle(defaultLang.getMyProfileMyMeetings());

                    } else {

                        getMenuInflater().inflate(R.menu.home_menu, menu);

                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                    }
                    //   }
                } else {

                    if (sessionManager.getRolId().equalsIgnoreCase("95")) {//changes applied

                        getMenuInflater().inflate(R.menu.fund_checkinmenu, menu);

                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemCheckInPortal = menu.findItem(R.id.itm_checkIn);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                        menuItemCheckInPortal.setTitle(defaultLang.getMyProfileCheckInPortal());
                    } else if (sessionManager.getRolId().equalsIgnoreCase("6")) {//changes applied

                        getMenuInflater().inflate(R.menu.requestmetting_menu, menu);
                        requestMettingtag = "1";
                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemMyMetting = menu.findItem(R.id.item_reuqestMeeting);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemMyMetting.setTitle(defaultLang.getMyProfileMyMeetings());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                    } else if (sessionManager.getRolId().equalsIgnoreCase("4")) {//changes applied

                        if (sessionManager.getAttendee_hide_request_meeting().equalsIgnoreCase("1")) {
                            requestMettingtag = "0";
                            getMenuInflater().inflate(R.menu.requestmetting_menu, menu);

                            menuItemMyProfile = menu.findItem(R.id.Profile);
                            menuItemLogout = menu.findItem(R.id.logout);
                            menuItemMyMetting = menu.findItem(R.id.item_reuqestMeeting);
                            menuItemAddNote = menu.findItem(R.id.addNote);

                            menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                            menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                            menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                            menuItemMyMetting.setTitle(defaultLang.getMyProfileMyMeetings());
                        } else if (sessionManager.getAttendee_hide_request_meeting().equalsIgnoreCase("0")) {
                            getMenuInflater().inflate(R.menu.home_menu, menu);

                            menuItemMyProfile = menu.findItem(R.id.Profile);
                            menuItemLogout = menu.findItem(R.id.logout);
                            menuItemAddNote = menu.findItem(R.id.addNote);

                            menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                            menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                            menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                        }
                    } else if (sessionManager.getRolId().equalsIgnoreCase("3")) {//changes applied

                        getMenuInflater().inflate(R.menu.organization_login, menu);


                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                    } else if (sessionManager.getRolId().equalsIgnoreCase("7") &&//changes applied
                            sessionManager.isModerater().equalsIgnoreCase("1")) {//changes applied

                        getMenuInflater().inflate(R.menu.requestmetting_menu, menu);

                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemMyMetting = menu.findItem(R.id.item_reuqestMeeting);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                        menuItemMyMetting.setTitle(defaultLang.getMyProfileMyMeetings());
                    } else {
                        getMenuInflater().inflate(R.menu.fund_homemenu, menu);
                        menuItemMyProfile = menu.findItem(R.id.Profile);
                        menuItemLogout = menu.findItem(R.id.logout);
                        menuItemAddNote = menu.findItem(R.id.addNote);

                        menuItemAddNote.setTitle(defaultLang.get6AddNotes());
                        menuItemMyProfile.setTitle(defaultLang.getMyProfileMyProfile());
                        menuItemLogout.setTitle(defaultLang.getMyProfileLogout());
                    }
                }


            }


            if (sessionManager.getNoteStatus().equalsIgnoreCase("1")) {
                menuItemAddNote.setVisible(true);
            } else {
                menuItemAddNote.setVisible(false);
            }
        }

//


        return true;
    }

    private void OpenEditDialog() {
        if (GlobalData.checkForUIDVersion()) {
            FragmentManager fm = getSupportFragmentManager();
            Attendee_ProfileEdit_Fragment fragment = new Attendee_ProfileEdit_Fragment();
            fragment.show(fm, "DialogFragment");
        } else {
            if (sessionManager.getRolId().equalsIgnoreCase("4")) {//changes applied
                FragmentManager fm = getSupportFragmentManager();
                Attendee_ProfileEdit_Fragment fragment = new Attendee_ProfileEdit_Fragment();
                fragment.show(fm, "DialogFragment");


            } else {
                FragmentManager fm = getSupportFragmentManager();
                Profile_FragmentDialog fragment = new Profile_FragmentDialog();
                fragment.show(fm, "DialogFragment");
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.Profile) {
            OpenEditDialog();
        } else if (id == R.id.itm_checkIn) {
            Log.d("AITL CLICK", "PORTAL");
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.CheckIn_Portal;
            loadFragment();
        } else if (id == R.id.order) {

            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Order_Fragment;
            loadFragment();
        } else if (id == R.id.item) {

            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.ItemListFragment;
            loadFragment();
        } else if (id == R.id.item_beacon) {
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.BeaconFinder_Fragment;
            loadFragment();
        } else if (id == R.id.item_reuqestMeeting) {

            if (GlobalData.checkForUIDVersion()) {

                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                loadFragment();

            } else {
                if (sessionManager.getRolId().equalsIgnoreCase("7") && //changes applied
                        sessionManager.isModerater().equalsIgnoreCase("1")) {//changes applied

                    Log.d("AITL", "onOptionsItemSelected: moderatoLogin");
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.moderatorRequestMettingFragment;
                    loadFragment();

                } else {

                    if (requestMettingtag.equalsIgnoreCase("0"))   // Exhibitor
                    {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                        loadFragment();
                    } else if (requestMettingtag.equalsIgnoreCase("1")) // AttendeeListing
                    {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                        loadFragment();
                    }

                }
            }
        } else if (id == R.id.logout) {
            MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                    .title(getResources().getString(R.string.exitfromEventMessage))
                    .items(getResources().getString(R.string.logout_message))
                    .positiveColor(getResources().getColor(R.color.colorAccent))
                    .positiveText(defaultLang.getMyProfileLogout())
                    .negativeText(getResources().getString(R.string.cancelText))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            logoutUser();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .cancelable(false)
                    .build();

            if (!isFinishing())
                dialog.show();
        } else if (id == R.id.addNote) {
            noteDetailDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    public void noteDetailDialog() {
        Bundle bundle = new Bundle();
        bundle.putString("edit", "0");
        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
        GlobalData.CURRENT_FRAG = GlobalData.NotesDetail_Fragment;
        loadFragment(bundle);
    }

    public void handleBackStack() {

        GlobalData.hideSoftKeyboard(MainActivity.this);

        if (SilentAuction_ProductDetail.timer != null) {

            SilentAuction_ProductDetail.timer.cancel();
        } else if (Presantation_Detail_Fragment.timer != null) {

            Presantation_Detail_Fragment.timer.cancel();
        } else if (Presantation_Detail_Fragment.swipeTimer != null) {

            Presantation_Detail_Fragment.swipeTimer.cancel();
        }

        if (GlobalData.CURRENT_FRAG == GlobalData.requestMettingInvteMoreFragment) {
            GlobalData.reuestMeetingReloadData(MainActivity.this);
        }

        if (GlobalData.CURRENT_FRAG != 79) {
            sessionManager.strModuleId = "0";
            sessionManager.isNoteCms = "0";

        }
        if (GlobalData.CURRENT_FRAG == 28) {
            sessionManager.folder_id("0");

        }
        if (GlobalData.CURRENT_FRAG == 83) {
            if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                if (sessionManager.getExhibitorParentCategoryId().equalsIgnoreCase("")) {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                    loadFragment();
                }
            } else if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                loadFragment();
            }

        }

        if (AppController.socket != null) {
            if (AppController.socket.isConnected()) {
                Log.d("AITL SocketConnected", "" + AppController.socket.isConnected());
                AppController.getInstance().socketDisconnected();
                Log.d("AITL ", "" + AppController.socket.isConnected());
                Log.d("AITL  Close", "" + AppController.socket.isClosed());
            }
        }
        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
            if (GlobalData.CURRENT_FRAG == GlobalData.HOME_FRAGMENT) {

                if (!(sessionManager.getExhibitorParentCategoryId().equalsIgnoreCase(""))) {
                    exitDailogFromApp();
                } else {
                    sessionManager.setExhibitorParentCategoryId("A");
                }
            } else {

                if (GlobalData.Fragment_Stack.size() > 0) {
                    fragmentBackStackMaintain();
                    if (GlobalData.CURRENT_FRAG == GlobalData.HOME_FRAGMENT) {

                        setDrawerState(true);
                        GlobalData.currentModuleForOnResume = "";
                        frme_cart.setVisibility(View.GONE);
                    }

                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.HOME_FRAGMENT);
                    loadFragment();
                }
            }
        } else {
            if (GlobalData.CURRENT_FRAG == GlobalData.Fundrising_Home) {

                if (!(sessionManager.getExhibitorParentCategoryId().equalsIgnoreCase(""))) {

                    exitDailogFromApp();
                } else {
                    sessionManager.setExhibitorParentCategoryId("A");
                }
            } else {
                if (GlobalData.Fragment_Stack.size() > 0) {

                    fragmentBackStackMaintain();
                    if (GlobalData.CURRENT_FRAG == GlobalData.Fundrising_Home) {
                        setDrawerState(true);
                        GlobalData.currentModuleForOnResume = "";
                    }
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.Fundrising_Home);
                    loadFragment();
                }
            }
        }

    }

    private void exitDailogFromApp() {
        MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                .title(getResources().getString(R.string.exitfromAppTitle))
                .items(getResources().getString(R.string.exit_message))
                .positiveColor(getResources().getColor(R.color.colorAccent))
                .positiveText("Exit")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        GlobalData.Fragment_Stack.clear();
                        if (Notitimer != null)
                            Notitimer.cancel();
                        startActivity(new Intent(MainActivity.this, SearchApp_Activity.class));
                        finish();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .cancelable(false)
                .build();
        if (!isFinishing())
            dialog.show();
    }

    public void fragmentBackStackMaintain() {
        GlobalData.CURRENT_FRAG = GlobalData.Fragment_Stack.pop();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate();
        toolbar.setTitle("");
    }

    public void getNotificationCount() {
        if (GlobalData.isNetworkAvailable(getApplicationContext())) {
            new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.getNotificationCount, Param.getNotificationCount(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId()), 2, false, this);
        }
    }

    private void notificationSeen() {
        if (GlobalData.isNetworkAvailable(getApplicationContext())) {
            new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.NotificationSeen, Param.NotificationSeen(sessionManager.getUserId(), sessionManager.getEventId(), notiMessage_type), 3, false, this);
        }
    }

    private void attendeeAproved(String sender_Id) {
        if (GlobalData.isNetworkAvailable(getApplicationContext())) {
            new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.approvedshareContactDetail, Param.shareContactInformation(sessionManager.getEventId(), sender_Id, sessionManager.getUserId()), 5, false, this);
        }
    }

    private void transactionAnimation() {
        transaction = getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.fragment_slide_left_enter,
                R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_right_exit);
    }

    public void loadFragment() {
        transactionAnimation();
        switch (GlobalData.CURRENT_FRAG) {
            case GlobalData.ActivitySharePost_Fragment:
                transaction.add(R.id.Container, new ActivitySharePost_Fragment());   // item id 0
                transaction.addToBackStack("ActivitySharePost_Fragment");
                transaction.commit();
                break;
            case GlobalData.HOME_FRAGMENT:
                transaction.add(R.id.Container, new HomeFragment());   // item id 0
                transaction.addToBackStack("Home Fragment");
                transaction.commit();
                break;

            case GlobalData.Fundrising_Home:
                transaction.add(R.id.Container, new Fundrising_Home_Fragment());// item id 0
                transaction.addToBackStack("Fundrising Fragment");
                transaction.commit();
                break;
            case GlobalData.AGENDA_FRAGMENT:
                transaction.add(R.id.Container, new Agenda_Fragment());// Id :-1
                transaction.addToBackStack("Agenda");
                transaction.commit();
                break;
            case GlobalData.AgendaGroupListFragment:
                transaction.add(R.id.Container, new AgendaGroupListFragment());  // Id :-1
                transaction.addToBackStack("AgendaGroupListFragment");
                transaction.commit();
                break;
            case GlobalData.AgendaGroupWiseFragment:
                transaction.add(R.id.Container, new AgendaListFragment());  // Id :-1
                transaction.addToBackStack("AgendaGroupWiseFragment");
                transaction.commit();
                break;
            case GlobalData.CMS_FRAGMENT_TEST1:
                transaction.add(R.id.Container, new Test1_Fragment());  // ID :35
                transaction.addToBackStack("CMS");
                transaction.commit();
                break;
            case GlobalData.PRIVATE_MESSAGE:
                transaction.add(R.id.Container, new PrivateMessageListing_Fragment());  // ID :12
                transaction.addToBackStack("Private");
                transaction.commit();
                break;
            case GlobalData.NOTES_FRAGMENT:
                transaction.add(R.id.Container, new Notes_fragment());   // ID :6
                transaction.addToBackStack("Notes");
                transaction.commit();
                break;

            case GlobalData.attndeeShareContactFragment:
                transaction.add(R.id.Container, new AttendeeFullDirectory_Fragment(), "0");   // ID :6
                transaction.addToBackStack("AttendeeShare");
                transaction.commit();
                break;

            case GlobalData.attndeeShareContactFragmentTag:
                transaction.add(R.id.Container, new AttendeeFullDirectory_Fragment(), "1");   // ID :6
                transaction.addToBackStack("Notes");
                transaction.commit();
                break;
            case GlobalData.View_Note_Fragment:
                transaction.add(R.id.Container, new View_Note_Fragment());   // ID :6
                transaction.addToBackStack("View Notes");
                transaction.commit();
                break;

            case GlobalData.EXHIBITOR_FRAGMENT:
                transaction.add(R.id.Container, new Exhibitor_ParentCategoryList_Fragment(), "exhibitor");   // ID :3
                transaction.addToBackStack("exhibitor Fragment");
                transaction.commit();
                break;

            case GlobalData.ExhibitorCategoryWiseData:
                transaction.add(R.id.Container, new ExhibitorList_Fragment_New(), "exhibitorCategoryWiseFragment");   // ID :3
                transaction.addToBackStack("exhibitor Fragment");
                transaction.commit();
                break;

            case GlobalData.SPEAKER_FRAGMENT:
                transaction.add(R.id.Container, new SpeakerList_newFragment(), "Speaker");
                transaction.addToBackStack("Speaker Fragment");// ID :7
                transaction.commit();
                break;

            case GlobalData.Sponsor_Fragment:
                transaction.add(R.id.Container, new SponsorGroupListFragment(), "Sponsors");
                transaction.addToBackStack("Sponsors Fragment");// ID :43
                transaction.commit();
                break;
            case GlobalData.SponsorGroupWiseFragment:
                transaction.add(R.id.Container, new SponsorListFragment(), "Sponsors");
                transaction.addToBackStack("Sponsors Fragment");// ID :43
                transaction.commit();
                break;
            case GlobalData.SOCIAL_FRAGMENT:
                transaction.add(R.id.Container, new Social_Fragment());// ID :7
                transaction.addToBackStack("SocialFragment");
                transaction.commit();
                break;

            case GlobalData.Map_fragment:
                transaction.add(R.id.Container, new MapGroupListFragment());   // ID :3
                transaction.addToBackStack("MapGroupListFragment");
                transaction.commit();
                break;
            case GlobalData.MapGroupWiseFragment:
                transaction.add(R.id.Container, new Map_Fragment());   // ID :3
                transaction.addToBackStack("Map Fragment");
                transaction.commit();
                break;
            case GlobalData.View_Agenda_Fragment:
                transaction.add(R.id.Container, new View_Agenda_Fragment());
                transaction.addToBackStack("View Agenda Fragment");
                transaction.commit();
                break;

            case GlobalData.View_UserWise_Agenda:
                transaction.add(R.id.Container, new View_userWise_Agenda());
                transaction.addToBackStack("View userwise Fragment");
                transaction.commit();
                break;

            case GlobalData.Attendance_Detail_Fragment:
                transaction.add(R.id.Container, new Attendance_Detail_Fragment());
                transaction.addToBackStack("Attendance Fragment");
                transaction.commit();
                break;

            case GlobalData.Exhibitor_Detail_Fragment:
                transaction.add(R.id.Container, new Exhibitor_Detail_Fragment());
                transaction.addToBackStack("Exhibitor Detail Fragment");
                transaction.commit();
                Log.e("aiyaz", "pushed ===>> " + GlobalData.Fragment_Stack.size());
                break;

            case GlobalData.Sponsor_Detail_Fragment:
                transaction.add(R.id.Container, new Sponsor_Detail_Fragment());
                transaction.addToBackStack("Sponser Detail Fragment");
                transaction.commit();
                break;

            case GlobalData.Speaker_Detail_Fragment:
                transaction.add(R.id.Container, new Speaker_Detail_Fragment());
                transaction.addToBackStack("Speaker Detail Fragment");
                transaction.commit();
                break;

            case GlobalData.Presantation_Fragment:
                transaction.add(R.id.Container, new Presentation_Fragment());
                transaction.addToBackStack("Presentation Fragment");
                transaction.commit();
                break;

            case GlobalData.Document_Fragment:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                sessionManager.folder_id("0");
                transaction.add(R.id.Container, new Document_Fragment(), "folder_file");
                transaction.addToBackStack("Document Fragment");
                transaction.commit();
                break;

            case GlobalData.Document_file_Fragment:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                transaction.add(R.id.Container, new Document_Fragment(), "file");
                transaction.addToBackStack("Document File Fragment");
                transaction.commit();
                break;

            case GlobalData.Survey_Fragment:
                transaction.add(R.id.Container, new SurveyCategorytListingFragment());
                transaction.addToBackStack("Survey Fragment");
                transaction.commit();
                break;

            case GlobalData.SurveyCategoryWiseFragment:
                transaction.add(R.id.Container, new Survey_Fragment());
                transaction.addToBackStack("Survey Detail Fragment");
                transaction.commit();
                break;

            case GlobalData.PublicMessage_Fragment:
                transaction.add(R.id.Container, new PublicMessage_Fragment());
                transaction.addToBackStack("Public Message Fragment");
                transaction.commit();
                break;

            case GlobalData.Photo_Fragment:
                transaction.add(R.id.Container, new Photo_Fragment());
                transaction.addToBackStack("Photo Fragment");
                transaction.commit();
                break;
            case GlobalData.Silent_Auction:
                transaction.add(R.id.Container, new SilentAuction_Fragment(), "1");
                transaction.addToBackStack("SilentAuction 1 Fragment");
                transaction.commit();
                break;

            case GlobalData.live_Auction:
                transaction.add(R.id.Container, new SilentAuction_Fragment(), "2");
                transaction.addToBackStack("SilentAuction 2 Fragment");
                transaction.commit();
                break;

            case GlobalData.Online_shop:
                transaction.add(R.id.Container, new SilentAuction_Fragment(), "3");
                transaction.addToBackStack("SilentAuction 3 Fragment");
                transaction.commit();
                break;
            case GlobalData.DonateFor_veteran:
                transaction.add(R.id.Container, new SilentAuction_Fragment(), "4");
                transaction.addToBackStack("SilentAuction 4 Fragment");
                transaction.commit();
                break;

            case GlobalData.SilentAuctionDetail_Fragment:
                Log.d("AITL", "==========>>>>>> called");
                transaction.add(R.id.Container, new SilentAuction_ProductDetail());
                transaction.addToBackStack("SilentAuction Product Detail Fragment");
                transaction.commit();
                break;

            case GlobalData.LiveAuction_Detail:
                transaction.add(R.id.Container, new LiveAuctionDetail());
                transaction.addToBackStack("LiveAuction Fragment");
                transaction.commit();
                break;

            case GlobalData.OnlineShop_Detail:
                transaction.add(R.id.Container, new OnlineShop_detail());
                transaction.addToBackStack("Online ShopFragment");
                transaction.commit();
                break;

            case GlobalData.PledgeDetailFragment:
                transaction.add(R.id.Container, new Pledge_Detail_Fragment());
                transaction.addToBackStack("Pledge Detail Fragment");
                transaction.commit();
                break;
            case GlobalData.CartDetailFragment:
                transaction.add(R.id.Container, new CartDetail_Fragment());
                transaction.addToBackStack("Cart Detail Fragment");
                transaction.commit();
                break;

            case GlobalData.CheckOut_Fragment:
                transaction.add(R.id.Container, new CheckOut_Fragment());
                transaction.addToBackStack("Check out Fragment");
                transaction.commit();
                break;
            case GlobalData.TwitterDetailFragment:
                transaction.add(R.id.Container, new TwitterFeed_Fragment());
                transaction.addToBackStack("TwitterFeed Fragment");
                transaction.commit();
                break;

            case GlobalData.fund_donation_fragment:
                transaction.add(R.id.Container, new Fundraising_Donation_Fragment());
                transaction.addToBackStack("Fundraising Donation Fragment");
                transaction.commit();
                break;

            case GlobalData.Order_Fragment:
                transaction.add(R.id.Container, new Order_FragmentTab());
                transaction.addToBackStack("Order Tab Fragment");
                transaction.commit();
                break;
            case GlobalData.Pending_AgendaFragment:
                transaction.add(R.id.Container, new View_PendingAgenda_Fragment());
                transaction.addToBackStack("View PendingAgenda Fragment");
                transaction.commit();
                break;
            case GlobalData.Agenda_BookStatus:
                transaction.add(R.id.Container, new Agenda_Bookstatus_Fragment());
                transaction.addToBackStack("Agenda Book Status Fragment");
                transaction.commit();
                break;

            case GlobalData.ItemListFragment:
                transaction.add(R.id.Container, new Item_Fragment_Tab());
                transaction.addToBackStack("Item Tab Fragment");
                transaction.commit();
                break;

            case GlobalData.AdditemFragment:
                transaction.add(R.id.Container, new AddItem_Fragment());
                transaction.addToBackStack("Add Item Fragment");
                transaction.commit();
                break;

            case GlobalData.EditItemFragment:
                transaction.add(R.id.Container, new EditItemFragment());
                transaction.addToBackStack("Edit Item Fragment");
                transaction.commit();
                break;

            case GlobalData.activityFeed:
                transaction.add(R.id.Container, new Activtiy_ModuleAllInoneDesign_Fragment());
                transaction.addToBackStack("Activity Module Fragment");
                transaction.commit();
//                else {
//                    transaction.add(R.id.Container, new ActivityTab_AllFragment());
//                    transaction.addToBackStack("Activity Module Fragment");
//                     transaction.commit();
//                }   15/10/2018
                break;

            case GlobalData.activityDetailFeed:
                transaction.add(R.id.Container, new ActivityDetailAllInOneModule_Fragment());
                transaction.addToBackStack("Activity Details Module Fragment");
                transaction.commit();
                break;

            case GlobalData.Map_Detail_Fragment:
                transaction.add(R.id.Container, new Map_Detail_Fragment());
                transaction.addToBackStack("Map Detail Fragment");
                transaction.commit();
                break;
            case GlobalData.NewMapDetail_Fragment:
                transaction.add(R.id.Container, new New_MapDetail_Fragment());
                transaction.addToBackStack("New Map Detail Fragment");
                transaction.commit();
                break;
            case GlobalData.InstagramFeed:
                transaction.add(R.id.Container, new InstagramFeed_Fragment());
                transaction.addToBackStack("Instagram Feed Fragment");
                transaction.commit();
                break;
            case GlobalData.exhibitorRequestMettingFragment:
                transaction.add(R.id.Container, new RequestMettingListFragment_newModule(), "0");
                transaction.addToBackStack("Exhibitor Request 0 Fragment");
                transaction.commit();
                break;

            case GlobalData.attendeeRequestMettingFragment:
                transaction.add(R.id.Container, new RequestMettingListFragment_newModule(), "1");
                transaction.addToBackStack("Exhibitor Request 0 Fragment");
                transaction.commit();
                break;


            case GlobalData.moderatorRequestMettingFragment:
                transaction.add(R.id.Container, new RequestMettingListFragment_newModule(), "2");
                transaction.addToBackStack("Exhibitor Request 0 Fragment");
                transaction.commit();
                break;

            case GlobalData.CheckIn_Portal:
                transaction.add(R.id.Container, new CheckIn_Portal_Fragment());
                transaction.addToBackStack("CheckIn Portal Fragment");
                transaction.commit();
                break;
            case GlobalData.notificationListingFragment:
                transaction.add(R.id.Container, new NotificationListing_Fragment());
                transaction.addToBackStack("Notification Listing Fragment");
                transaction.commit();
                break;
            case GlobalData.viewSuggestedTimeList:
                transaction.add(R.id.Container, new ViewRequestedMettingDateTimeList_Fragment());
                transaction.addToBackStack("View Request Metting Date Fragment");
                transaction.commit();
                break;
            case GlobalData.facebookFeed:
                transaction.add(R.id.Container, new FacebookFeed_Fragment());
                transaction.addToBackStack("Exhibitor Request 0 Fragment");
                transaction.commit();
                break;

            case GlobalData.FavoriteList_Fragment:
                transaction.add(R.id.Container, new Favourite_FragmentList());
                transaction.addToBackStack("FavoriteList Request 0 Fragment");
                transaction.commit();
                break;
            case GlobalData.QAList_Fragment:
                transaction.add(R.id.Container, new QandAGroupFragment());
                transaction.addToBackStack("QA GroupFragment");
                transaction.commit();
                break;
            case GlobalData.QandAGroupWiseFragment:
                transaction.add(R.id.Container, new QAModule_Fragment());
                transaction.addToBackStack("QA Fragment");
                transaction.commit();
                break;
            case GlobalData.PrivateMessageSenderWiseDetail:
                transaction.add(R.id.Container, new PrivateMessageDetail_Fragment());
                transaction.addToBackStack("Private MessageDetail Fragment");
                transaction.commit();
                break;
            case GlobalData.getAllCommonData:
                transaction.add(R.id.Container, new StartPrivateMessageConversion_Fragment());
                transaction.addToBackStack("Start Private Conversion Fragment");
                transaction.commit();
                break;
            case GlobalData.VirtualSuperMarket:
                transaction.add(R.id.Container, new VirtualSuperMarker_Fragment());
                transaction.addToBackStack("View Virtual SuperMarket");
                transaction.commit();
                break;
            case GlobalData.BeaconFinder_Fragment:
                transaction.add(R.id.Container, new BeaconFinder_Fragment());
                transaction.addToBackStack("View BeaconFinder");
                transaction.commit();
                break;
            case GlobalData.getAllBeaconList:
                transaction.add(R.id.Container, new Beacon_FindDeviceListing_Fragment());
                transaction.addToBackStack("View BeaconFindDeviceListingFragment");
                transaction.commit();
                break;
            case GlobalData.TweitterFeed:
                transaction.add(R.id.Container, new TwitterHashTagListingFragment());
                transaction.addToBackStack("View TwitterListingFragment");
                transaction.commit();
                break;
            case GlobalData.OpenPdfFragment:
                transaction.add(R.id.Container, new OpenPdfFragment());
                transaction.addToBackStack("View OpenPdfFragment");
                transaction.commit();
                break;
            case GlobalData.Attendee_EditProfile_Fragment:
                transaction.add(R.id.Container, new Attendee_ProfileEdit_Fragment());
                transaction.addToBackStack("View Attendee_ProfileEdit_Fragment");
                transaction.commit();
                break;
            case GlobalData.Gamification_Fragment:
                transaction.add(R.id.Container, new GamiiFication_Fragment());
                transaction.addToBackStack("Gamification_Fragment");
                transaction.commit();
                break;
            case GlobalData.PhotoFilter_Fragment:
                transaction.add(R.id.Container, new PhotoFilter_Fragment());
                transaction.addToBackStack("PhotoFilter");
                transaction.commit();
                break;
            case GlobalData.ExhibiorLead_Fragment:
                transaction.add(R.id.Container, new Exhibitor_Lead_Fragment());
                transaction.addToBackStack("ExhibitorLeadFragment");
                transaction.commit();
                break;
            case GlobalData.CMSGroupListFragment:
                transaction.add(R.id.Container, new CMSMainGroupListing());
                transaction.addToBackStack("CMSGroupListFragment");
                transaction.commit();
                break;
            case GlobalData.CMSGroupChildListFragment:
                transaction.add(R.id.Container, new CMSChildGroupFragment());
                transaction.addToBackStack("CMSGroupChildListFragment");
                transaction.commit();
                break;
            case GlobalData.CMSListFragment:
                transaction.add(R.id.Container, new CMSListFragment());
                transaction.addToBackStack("CMSListFragment");
                transaction.commit();
                break;
            case GlobalData.ExhibitorSubCategoryListFragment:
                transaction.add(R.id.Container, new ExhibitorSubCategory_Fragment());
                transaction.addToBackStack("ExhibitorSubCategoryListFragment");
                transaction.commit();
                break;
            case GlobalData.ExhibitorSubCategoryWiseListFragment:
                transaction.add(R.id.Container, new ExhibitorListingFromSubCategoryList());
                transaction.addToBackStack("ExhibitorSubCategoryWiseListFragment");
                transaction.commit();
                break;
            case GlobalData.requestMettingInvteMoreFragment:
                transaction.add(R.id.Container, new RequestMeetingInviteMore_Fragment());
                transaction.addToBackStack("requestMettingInvteMoreFragment");
                transaction.commit();
                break;
            case GlobalData.QrcodeScannerSharecontactFragment:
                transaction.add(R.id.Container, new QrCodeScanner_ShareContact_Fragment());
                transaction.addToBackStack("QrcodeScannerSharecontactFragment");
                transaction.commit();
                break;
            case GlobalData.MatchMakingFragment:
                transaction.add(R.id.Container, new MatchmakingTab_Fragment());
                transaction.addToBackStack("MatchmakingTab_Fragment");
                transaction.commit();
                break;
            case GlobalData.AttendeeMainGroupFragment:
                transaction.add(R.id.Container, new AttendeeMainCategoryFragment());
                transaction.addToBackStack("AttendeeMainGroupFragment");
                transaction.commit();
                break;
            case GlobalData.QaHideQuestionModule_Fragment:
                transaction.add(R.id.Container, new QAHideQuestionModule_Fragment());
                transaction.addToBackStack("QAHideQuestionModule_Fragment");
                transaction.commit();
                break;
            case GlobalData.QaModeratorInstructionModule_Fragment:
                transaction.add(R.id.Container, new QAInstructionModule_Fragment());
                transaction.addToBackStack("QAInstructionModule_Fragment");
                transaction.commit();
                break;
            case GlobalData.PhotoFilter_seeAllFitlers:
                transaction.add(R.id.Container, new PhotoFilter_filterListing());
                transaction.addToBackStack("PhotoFilter_filterListing");
                transaction.commit();
                break;
            case GlobalData.PhotoFilter_seeAllPhotos:
                transaction.add(R.id.Container, new PhotoFilter_seeMyPhotoFragment());
                transaction.addToBackStack("PhotoFilter_filterListing");
                transaction.commit();
                break;
            default:
                break;
        }
    }

    public void loadFragment(Bundle bundle) {
        transactionAnimation();
        switch (GlobalData.CURRENT_FRAG) {

            case GlobalData.HOME_FRAGMENT:
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);
                transaction.add(R.id.Container, homeFragment);
                transaction.addToBackStack("Home Fragment");
                transaction.commit();
                break;
            case GlobalData.View_Agenda_Fragment:
                View_Agenda_Fragment view_agenda_fragment = new View_Agenda_Fragment();
                view_agenda_fragment.setArguments(bundle);
                transaction.add(R.id.Container, view_agenda_fragment);
                transaction.addToBackStack("View Agenda Fragment");
                transaction.commit();
                break;
            case GlobalData.Webview_fragment:
                Webview_Fragment webview_fragment = new Webview_Fragment();
                webview_fragment.setArguments(bundle);
                transaction.add(R.id.Container, webview_fragment);
                transaction.addToBackStack("WebView_Fragment");
                transaction.commit();
                break;
            case GlobalData.Map_Detail_Fragment:
                Map_Detail_Fragment map_detail_fragment = new Map_Detail_Fragment();
                map_detail_fragment.setArguments(bundle);
                transaction.add(R.id.Container, map_detail_fragment);
                transaction.addToBackStack("Map_Detail_Fragment");
                transaction.commit();
                break;

            case GlobalData.Attendance_Detail_Fragment:
                Attendance_Detail_Fragment attendace_detail = new Attendance_Detail_Fragment();
                attendace_detail.setArguments(bundle);
                transaction.add(R.id.Container, attendace_detail);
                transaction.addToBackStack("Attendance_Detail");
                transaction.commit();
                break;
            case GlobalData.Exhibitor_Detail_Fragment:
                Exhibitor_Detail_Fragment exhibitor_detail = new Exhibitor_Detail_Fragment();
                exhibitor_detail.setArguments(bundle);
                transaction.add(R.id.Container, exhibitor_detail, "exhibitor");
                transaction.addToBackStack("Exhibitor_Detail");
                transaction.commit();

                break;

            case GlobalData.Presantation_Detail_Fragment:
                Presantation_Detail_Fragment presantation_detail = new Presantation_Detail_Fragment();
                presantation_detail.setArguments(bundle);
                transaction.add(R.id.Container, presantation_detail);
                transaction.addToBackStack("Presantation");
                transaction.commit();
                break;
            case GlobalData.QAListDetail_Fragment:
                QADetailModule_Fragment qaDetailModule = new QADetailModule_Fragment();
                qaDetailModule.setArguments(bundle);
                transaction.add(R.id.Container, qaDetailModule);   // ID :6
                transaction.addToBackStack("Q&ADetailModule");
                transaction.commit();
                break;
            case GlobalData.privateMessageViewProfileFragment:
                PrivateMessageViewProfile_Fragment privateMesageViewprofile = new PrivateMessageViewProfile_Fragment();
                privateMesageViewprofile.setArguments(bundle);
                transaction.add(R.id.Container, privateMesageViewprofile);   // ID :6
                transaction.addToBackStack("privateMesageViewprofile");
                transaction.commit();
                break;
            case GlobalData.NotesDetail_Fragment:
                NoteDetail_Fragment noteDetail_fragment = new NoteDetail_Fragment();
                noteDetail_fragment.setArguments(bundle);
                transaction.add(R.id.Container, noteDetail_fragment);
                transaction.addToBackStack("NotesDetail Fragment");
                transaction.commit();
                break;

            case GlobalData.requestMettingInvteMoreFragment:
                RequestMeetingInviteMore_Fragment inviteMore_fragment = new RequestMeetingInviteMore_Fragment();
                inviteMore_fragment.setArguments(bundle);
                transaction.add(R.id.Container, inviteMore_fragment);
                transaction.addToBackStack("requestMettingInvteMoreFragment");
                transaction.commit();
                break;

            case GlobalData.activityDetailFeed:
                ActivityDetailAllInOneModule_Fragment activityDetailAllInOneModule_fragment = new ActivityDetailAllInOneModule_Fragment();
                activityDetailAllInOneModule_fragment.setArguments(bundle);
                transaction.add(R.id.Container, activityDetailAllInOneModule_fragment);
                transaction.addToBackStack("Activity Details Module Fragment");
                transaction.commit();
                break;
            case GlobalData.ActivityCommentView_Fragment:
                ActivityCommentView_Fragment activityCommentView_fragment = new ActivityCommentView_Fragment();
                activityCommentView_fragment.setArguments(bundle);
                transaction.add(R.id.Container, activityCommentView_fragment);
                transaction.addToBackStack("ActivityCommentView_Fragment");
                transaction.commit();
                break;
            default:
                break;
        }
    }

    public void reloadFragment() {

        transactionAnimation();
        switch (GlobalData.CURRENT_FRAG) {

            case GlobalData.Attendance_Detail_Fragment:

                getSupportFragmentManager().popBackStack("Attendance Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                transaction.add(R.id.Container, new Attendance_Detail_Fragment());
                transaction.addToBackStack("Attendance Fragment");
                transaction.commit();
                break;

            case GlobalData.PublicMessage_Fragment:

                getSupportFragmentManager().popBackStack("Public Message Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                transaction.add(R.id.Container, new PublicMessage_Fragment());   // item id 0
                transaction.addToBackStack("Public Message Fragment");
                Log.e("aiyaz", "loading home fragment");
                transaction.commit();
                break;


            case GlobalData.EXHIBITOR_FRAGMENT:
                getSupportFragmentManager().popBackStack("exhibitor Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.add(R.id.Container, new ExhibitorList_Fragment_New(), "exhibitor");   // ID :3
                transaction.addToBackStack("exhibitor Fragment");
                transaction.commit();
                break;

            case GlobalData.PRIVATE_MESSAGE:

                getSupportFragmentManager().popBackStack("Private", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                transaction.add(R.id.Container, new Private_Message_Fragment());  // ID :12
                transaction.addToBackStack("Private");
                transaction.commit();
                break;

            case GlobalData.Speaker_Detail_Fragment:

                getSupportFragmentManager().popBackStack("Speaker Detail Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                transaction.add(R.id.Container, new Speaker_Detail_Fragment());
                transaction.addToBackStack("Speaker Detail Fragment");
                transaction.commit();
                break;

            case GlobalData.Exhibitor_Detail_Fragment:

                getSupportFragmentManager().popBackStack("Exhibitor Detail Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                transaction.add(R.id.Container, new Exhibitor_Detail_Fragment());
                transaction.addToBackStack("Exhibitor Detail Fragment");
                transaction.commit();
                break;

            case GlobalData.Photo_Fragment:

                getSupportFragmentManager().popBackStack("Photo Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                transaction.add(R.id.Container, new Photo_Fragment());
                transaction.addToBackStack("Photo Fragment");
                transaction.commit();
                break;

            case GlobalData.View_Agenda_Fragment:

                getSupportFragmentManager().popBackStack("View Agenda Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.add(R.id.Container, new View_Agenda_Fragment());
                transaction.addToBackStack("View Agenda Fragment");
                transaction.commit();
                break;
            case GlobalData.SilentAuctionDetail_Fragment:
                getSupportFragmentManager().popBackStack("SilentAuction Product Detail Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.add(R.id.Container, new SilentAuction_ProductDetail());
                transaction.addToBackStack("SilentAuction Product Detail Fragment");
                transaction.commit();
                break;
            case GlobalData.LiveAuction_Detail:
                getSupportFragmentManager().popBackStack("LiveAuction Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.add(R.id.Container, new LiveAuctionDetail());
                transaction.addToBackStack("LiveAuction Fragment");
                transaction.commit();
                break;
            case GlobalData.OnlineShop_Detail:
                getSupportFragmentManager().popBackStack("Online ShopFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.add(R.id.Container, new OnlineShop_detail());
                transaction.addToBackStack("Online ShopFragment");
                transaction.commit();
                break;
            case GlobalData.PledgeDetailFragment:
                getSupportFragmentManager().popBackStack("Pledge Detail Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.add(R.id.Container, new Pledge_Detail_Fragment());
                transaction.addToBackStack("Pledge Detail Fragment");
                transaction.commit();
                break;
            case GlobalData.CartDetailFragment:
                getSupportFragmentManager().popBackStack("Cart Detail Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.add(R.id.Container, new CartDetail_Fragment());
                transaction.addToBackStack("Cart Detail Fragment");
                transaction.commit();
                break;
            case GlobalData.PrivateMessageSenderWiseDetail:
                getSupportFragmentManager().popBackStack("Private MessageDetail Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.add(R.id.Container, new PrivateMessageDetail_Fragment());
                transaction.addToBackStack("Private MessageDetail Fragment");
                transaction.commit();
                break;
            case GlobalData.getAllBeaconList:
                getSupportFragmentManager().popBackStack("View BeaconFindDeviceListingFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.add(R.id.Container, new Beacon_FindDeviceListing_Fragment());
                transaction.addToBackStack("View BeaconFindDeviceListingFragment");
                transaction.commit();
                break;
            case GlobalData.SurveyCategoryWiseFragment:
                getSupportFragmentManager().popBackStack("Survey Detail Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.add(R.id.Container, new Survey_Fragment());
                transaction.addToBackStack("Survey Detail Fragment");
                transaction.commit();
                break;

            case GlobalData.ExhibitorCategoryWiseData:
                getSupportFragmentManager().popBackStack("exhibitorCategoryWiseFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.add(R.id.Container, new ExhibitorList_Fragment_New(), "exhibitorCategoryWiseFragment");   // ID :3
                transaction.addToBackStack("exhibitor Fragment");
                transaction.commit();
                break;

            case GlobalData.exhibitorRequestMettingFragment:
                getSupportFragmentManager().popBackStack("Exhibitor Request 0 Fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.add(R.id.Container, new RequestMettingListFragment_newModule(), "0");
                transaction.addToBackStack("Exhibitor Request 0 Fragment");
                transaction.commit();
                break;

            default:
                break;
        }
    }

    public void setDrawerState(boolean isEnabled) {

        Log.e("allintheloop CURR" + isEnabled, "" + GlobalData.CURRENT_FRAG);

        if (isEnabled) {
            sessionManager.strMenuId = "0";
            sessionManager.strModuleId = "Home";
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.onDrawerStateChanged(DrawerLayout.STATE_DRAGGING);
            toggle.setDrawerIndicatorEnabled(false);
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sessionManager.strMenuId = "0";
                    sessionManager.strModuleId = "Home";
                    drawer.openDrawer(Gravity.LEFT);
                }
            });
            toggle.syncState();

        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Log.d("AITL CURR Fragment", "" + GlobalData.CURRENT_FRAG);
                    try {
                        handleBackStack();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });

            toggle.syncState();
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            toolbar.getNavigationIcon().setColorFilter(Color.parseColor(sessionManager.getFunTopTextColor()), PorterDuff.Mode.SRC_IN);
            toolbar.getOverflowIcon().setColorFilter(Color.parseColor(sessionManager.getFunTopTextColor()), PorterDuff.Mode.SRC_IN);
        } else {
            toolbar.getNavigationIcon().setColorFilter(Color.parseColor(sessionManager.getTopTextColor()), PorterDuff.Mode.SRC_IN);
            toolbar.getOverflowIcon().setColorFilter(Color.parseColor(sessionManager.getTopTextColor()), PorterDuff.Mode.SRC_IN);
        }
    }

    private void getAgendaListData() {
        if (GlobalData.isNetworkAvailable(MainActivity.this)) {
            new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.get_AgendaList_offline, Param.getSponsorOfflineList(sessionManager.getEventId()), 9, true, this);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    private void getExhibitorLeadofllineData() {
        if (GlobalData.isNetworkAvailable(MainActivity.this)) {
            new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.getofllineDataForExhibitorLead, Param.getOfflineDataForExhibtior(sessionManager.getUserId(), sessionManager.getEventId()), 6, false, this);
        }
    }

    private void notificationPrivatePublicHide() {
        if (sessionManager.getIsprivate_message_enabled().equalsIgnoreCase("1")) {
            txt_notiPrivate.setVisibility(View.VISIBLE);
        } else {
            txt_notiPrivate.setVisibility(View.GONE);
        }

        if (sessionManager.getIspublic_message_enabled().equalsIgnoreCase("1")) {
            txt_notipublic.setVisibility(View.VISIBLE);
        } else {
            txt_notipublic.setVisibility(View.GONE);
        }


    }

    public void setAppcolor() {

        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            toolbar.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            toolbar.setTitleTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            userName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            login.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));

            img_cart.setColorFilter(Color.parseColor(sessionManager.getFunTopTextColor()));
            txt_noti_badge.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            edtnoteImg.setColorFilter(Color.parseColor(sessionManager.getFunTopTextColor())); // White Tint

            mDrawerList.setSelector(GlobalData.getAdaptiveRippleDrawable(Color.parseColor(sessionManager.getFunTopBackColor())
                    , Color.parseColor(sessionManager.getMenuHoveColor())));


            img_noti.setColorFilter(Color.parseColor(sessionManager.getFunTopTextColor())); // White Tint
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            }
        } else {
            toolbar.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            toolbar.setTitleTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            userName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            login.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            txt_noti_badge.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            img_cart.setColorFilter(Color.parseColor(sessionManager.getTopTextColor()));
            edtnoteImg.setColorFilter(Color.parseColor(sessionManager.getTopTextColor())); // White Tint
            img_noti.setColorFilter(Color.parseColor(sessionManager.getTopTextColor())); // White Tint
            mDrawerList.setSelector(GlobalData.getAdaptiveRippleDrawable(Color.parseColor(sessionManager.getTopBackColor())
                    , Color.parseColor(sessionManager.getMenuHoveColor())));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor(sessionManager.getTopBackColor()));
            }

        }
    }

    private void loadMenuData(JSONObject jsonObject) {
        try {
            if (GlobalData.checkForUIDVersion())
                sessionManager.setUidCommonKey(jsonObject.getJSONObject("key_data"));

            idArrays = new ArrayList<>();
            ObjItem = new ArrayList<>();
            ObjItem.add(new DrawerItem("Logo", "", "menu", "0", "0", "", "", ""));
            ObjItem.add(new DrawerItem("EventName", "", "menu", "0", "0", "", "", ""));
            ObjItem.add(new DrawerItem(defaultLang.getLeftHandMenuHome(), "0", "menu", "0", "0", "", "", ""));

            sessionManager.setHostName(jsonObject.getString("host_name"));
            sessionManager.setShowMeetingLocation(jsonObject.getString("show_meeting_location"));
            JSONArray jsonArray = jsonObject.optJSONArray("events");
            for (int e = 0; e < jsonArray.length(); e++) {
                JSONObject jObjectevent = (JSONObject) jsonArray.get(e);
                sessionManager.setAllowShowAllAgenda(jObjectevent.getString("allow_show_all_agenda"));
                sessionManager.setBeaconUUID(jObjectevent.getString("UUID"));

                str_showMessageBell = jObjectevent.getString("show_message_bell_icon");
                if (sessionManager.isLogin()) {
                    if (jObjectevent.getString("show_message_bell_icon").equalsIgnoreCase("1")) {

                        frme_noti.setVisibility(View.VISIBLE);
                        if (sessionManager.isLogin()) {
                            Notitimer = new Timer();
                            Notitimer.schedule(new Refresh(), 0, 40000);
                        }

                    } else {
                        frme_noti.setVisibility(View.GONE);
                        if (Notitimer != null) {
                            Notitimer.cancel();
                        }
                    }
                    if (jObjectevent.getString("show_notes_icon").equalsIgnoreCase("1")) {
                        edtnoteImg.setVisibility(View.VISIBLE);
                    } else {
                        edtnoteImg.setVisibility(View.GONE);
                    }
                    sessionManager.saveNoteStatus(jObjectevent.getString("show_notes_icon"));
                }
                sessionManager.appColor(jObjectevent);
                invalidateOptionsMenu();
            }
            setAppcolor();
            JSONArray jArrayMenu = jsonObject.optJSONArray("menu_list");
            for (int i = 0; i < jArrayMenu.length(); i++) {
                JSONObject index = (JSONObject) jArrayMenu.get(i);

                mnuId = index.getString("id");
                mnuName = index.getString("menuname");
                if (mnuId.equalsIgnoreCase("1")) {
                    JSONArray jsonArrayAgendaCategory = index.getJSONArray("category_list");
                    JSONArray jsonArrayAgendaGroup = index.getJSONArray("group");

                    if (jsonArrayAgendaCategory.length() != 0 || jsonArrayAgendaGroup.length() != 0) {


                        for (int c = 0; c < jsonArrayAgendaCategory.length(); c++) {
                            JSONObject jsonCategoryIndex = jsonArrayAgendaCategory.getJSONObject(c);
                            if (!jsonCategoryIndex.getString("menuname").equalsIgnoreCase("")) {
                                ObjItem.add(new DrawerItem(jsonCategoryIndex.getString("menuname")
                                        , jsonCategoryIndex.getString("id")
                                        , "menu"
                                        , jsonCategoryIndex.getString("is_force_login")
                                        , jsonCategoryIndex.getString("category_id")
                                        , "", "", ""));
                            }
                        }

                        for (int g = 0; g < jsonArrayAgendaGroup.length(); g++) {
                            JSONObject jsonCategoryIndex = jsonArrayAgendaGroup.getJSONObject(g);
                            if (!jsonCategoryIndex.getString("group_name").equalsIgnoreCase("")) {
                                ObjItem.add(new DrawerItem(jsonCategoryIndex.getString("group_name")
                                        , jsonCategoryIndex.getString("menu_id")
                                        , "menu"
                                        , jsonCategoryIndex.getString("is_force_login")
                                        , ""
                                        , ""
                                        , jsonCategoryIndex.getString("module_group_id"), ""));
                            }
                        }

                    } else {
                        if (!mnuName.equalsIgnoreCase("")) {
                            ObjItem.add(new DrawerItem(mnuName, mnuId, "menu"
                                    , index.getString("is_force_login")
                                    , index.getString("category_id"), "", "", ""));

                        }
                    }
                } else if (mnuId.equalsIgnoreCase("21")) {
                    JSONArray jsonArrayGroup = index.getJSONArray("super_group");
                    JSONArray jsonArraySubGroup = index.getJSONArray("group");

                    if (jsonArrayGroup.length() != 0 || jsonArraySubGroup.length() != 0) {


                        for (int c = 0; c < jsonArraySubGroup.length(); c++) {
                            JSONObject jsonCategoryIndex = jsonArraySubGroup.getJSONObject(c);
                            if (!jsonCategoryIndex.getString("group_name").equalsIgnoreCase("")) {
                                ObjItem.add(new DrawerItem(jsonCategoryIndex.getString("group_name")
                                        , jsonCategoryIndex.getString("menu_id")
                                        , "menu"
                                        , jsonCategoryIndex.getString("is_force_login")
                                        , ""
                                        , ""
                                        , jsonCategoryIndex.getString("module_group_id"), ""));
                            }
                        }

                        for (int g = 0; g < jsonArrayGroup.length(); g++) {
                            JSONObject jsonCategoryIndex = jsonArrayGroup.getJSONObject(g);
                            if (!jsonCategoryIndex.getString("group_name").equalsIgnoreCase("")) {
                                ObjItem.add(new DrawerItem(jsonCategoryIndex.getString("group_name")
                                        , jsonCategoryIndex.getString("menu_id")
                                        , "menu"
                                        , jsonCategoryIndex.getString("is_force_login")
                                        , ""
                                        , jsonCategoryIndex.getString("id")
                                        , "", ""));
                            }
                        }

                    } else {
                        if (!mnuName.equalsIgnoreCase("")) {
                            ObjItem.add(new DrawerItem(mnuName, mnuId, "menu"
                                    , index.getString("is_force_login")
                                    , index.getString("category_id"), "", "", ""));

                        }
                    }


                } else if (mnuId.equalsIgnoreCase("43")) {
                    JSONArray jsonArrayGroup = index.getJSONArray("group");

                    if (jsonArrayGroup.length() != 0) {
                        for (int g = 0; g < jsonArrayGroup.length(); g++) {
                            JSONObject jsonCategoryIndex = jsonArrayGroup.getJSONObject(g);
                            if (!jsonCategoryIndex.getString("group_name").equalsIgnoreCase("")) {
                                ObjItem.add(new DrawerItem(jsonCategoryIndex.getString("group_name")
                                        , jsonCategoryIndex.getString("menu_id")
                                        , "menu"
                                        , jsonCategoryIndex.getString("is_force_login")
                                        , ""
                                        , ""
                                        , jsonCategoryIndex.getString("module_group_id"), ""));
                            }
                        }

                    } else {
                        if (!mnuName.equalsIgnoreCase("")) {
                            ObjItem.add(new DrawerItem(mnuName, mnuId, "menu"
                                    , index.getString("is_force_login")
                                    , index.getString("category_id"), "", "", ""));

                        }
                    }
                } else if (mnuId.equalsIgnoreCase("10")) {
                    JSONArray jsonArrayGroup = index.getJSONArray("group");

                    if (jsonArrayGroup.length() != 0) {
                        for (int g = 0; g < jsonArrayGroup.length(); g++) {
                            JSONObject jsonCategoryIndex = jsonArrayGroup.getJSONObject(g);
                            if (!jsonCategoryIndex.getString("group_name").equalsIgnoreCase("")) {
                                ObjItem.add(new DrawerItem(jsonCategoryIndex.getString("group_name")
                                        , jsonCategoryIndex.getString("menu_id")
                                        , "menu"
                                        , jsonCategoryIndex.getString("is_force_login")
                                        , ""
                                        , ""
                                        , jsonCategoryIndex.getString("module_group_id"), ""));
                            }
                        }

                    } else {
                        if (!mnuName.equalsIgnoreCase("")) {
                            ObjItem.add(new DrawerItem(mnuName, mnuId, "menu"
                                    , index.getString("is_force_login")
                                    , index.getString("category_id"), "", "", ""));

                        }
                    }
                } else if (mnuId.equalsIgnoreCase("50")) {
                    JSONArray jsonArrayGroup = index.getJSONArray("group");
                    if (jsonArrayGroup.length() != 0) {
                        for (int g = 0; g < jsonArrayGroup.length(); g++) {
                            JSONObject jsonCategoryIndex = jsonArrayGroup.getJSONObject(g);
                            if (!jsonCategoryIndex.getString("group_name").equalsIgnoreCase("")) {
                                ObjItem.add(new DrawerItem(jsonCategoryIndex.getString("group_name")
                                        , jsonCategoryIndex.getString("menu_id")
                                        , "menu"
                                        , jsonCategoryIndex.getString("is_force_login")
                                        , ""
                                        , ""
                                        , jsonCategoryIndex.getString("module_group_id"), jsonCategoryIndex.getString("is_contains_data")));
                            }
                        }
                    } else {
                        if (!mnuName.equalsIgnoreCase("")) {
                            ObjItem.add(new DrawerItem(mnuName, mnuId, "menu"
                                    , index.getString("is_force_login")
                                    , index.getString("category_id"), "", "", "0"));

                        }
                    }
                } else {
                    if (!mnuName.equalsIgnoreCase("")) {
                        ObjItem.add(new DrawerItem(mnuName, mnuId, "menu"
                                , index.getString("is_force_login")
                                , index.getString("category_id"), "", "", ""));
                    }
                }
            }
            if (jsonObject.has("cmsmenu")) {

                JSONArray jArrayCmsMenu = jsonObject.getJSONArray("cmsmenu");
                for (int i = 0; i < jArrayCmsMenu.length(); i++) {
                    JSONObject jsonCmsMenu = jArrayCmsMenu.getJSONObject(i);
                    cmsId = jsonCmsMenu.getString("Id");
                    cmsmnuName = jsonCmsMenu.getString("Menu_name");
                    if (!cmsmnuName.equalsIgnoreCase("")) {
                        ObjItem.add(new DrawerItem(cmsmnuName, cmsId, "cms_menu", "0"));
                        idArrays.add(new IdArray(cmsId));
                    }
                }

                if (sessionManager.isLogin()) {
                    ObjItem.add(new DrawerItem(defaultLang.getLeftHandMenuNotifications(), MenuId.NOTIFICATION_CENTER_ID, "menu", "0", "", "", "", ""));
                }
            }

            itemAdapter = new DrawerItemAdapter(MainActivity.this, R.layout.listview_item_row, ObjItem, getApplicationContext(), "#ffffff");
            mDrawerList.setAdapter(itemAdapter);
            scrollView.setVisibility(View.GONE);
            linear_dynamicHome.setVisibility(View.GONE);
            mDrawerList.setVisibility(View.VISIBLE);

            leftMenuDyanmic(jsonObject);
            parseMultilang(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void leftMenuDyanmic(JSONObject jsonObject) {
        try {


            JSONObject jsonObjectImage = jsonObject.getJSONObject("banner_images");
            final JSONArray jsonArrayImage = jsonObjectImage.getJSONArray("images");
            listImageArray.clear();
            rectanglesList.clear();
            for (int img = 0; img < jsonArrayImage.length(); img++) {

                listImageDetailArray = new ArrayList<>();
                JSONObject index = jsonArrayImage.getJSONObject(img);
                JSONArray jsonArrayDetail = index.getJSONArray("details");

                if (index.getString("image_type").equalsIgnoreCase("1")) {
                    for (int det = 0; det < jsonArrayDetail.length(); det++) {
                        JSONObject jsonImageDetail = jsonArrayDetail.getJSONObject(det);
                        listImageDetailArray.add(new HomeScreenMapDetailData(jsonImageDetail.getString("coords"), jsonImageDetail.getString("menuid"), jsonImageDetail.getString("redirect_url"), jsonImageDetail.getString("cmsid"), jsonImageDetail.getString("is_force_login"), jsonImageDetail.getString("agenda_id"), jsonImageDetail.getString("group_id"), jsonImageDetail.getString("exhi_id"), jsonImageDetail.getString("super_group_id"), jsonImageDetail.getString("keyword"), jsonImageDetail.getString("all_exhi_sub_cat"), jsonImageDetail.getString("is_contains_data"), ""));
                    }
                    listImageArray.add(new HomePageDynamicImageArray(index.getString("id")
                            , MyUrls.Imgurl + index.getString("Image")
                            , index.getString("Content")
                            , index.getInt("height")
                            , index.getInt("width"), listImageDetailArray));
                }
            }

            if (listImageArray.size() != 0) {

                progressBarImage.setVisibility(View.VISIBLE);
                linear_dynamicHome.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                mDrawerList.setVisibility(View.GONE);
                if (linear_dynamicHome.getChildCount() != 0) {
                    linear_dynamicHome.removeAllViews();
                }
                for (int k = 0; k < listImageArray.size(); k++) {

                    final int position = k;
                    final RelativeLayout relativeLayout = new RelativeLayout(MainActivity.this);
                    final RelativeLayout.LayoutParams vp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    relativeLayout.setLayoutParams(vp);
                    final ImageView image = new ImageView(MainActivity.this);
                    image.setAdjustViewBounds(true);
                    Log.d("AITL LEft Image", listImageArray.get(k).getImageUrl());
                    image.setCropToPadding(false);
                    image.setLayoutParams(vp);
                    Glide.with(MainActivity.this).load(listImageArray.get(k).getImageUrl()).asBitmap().override(listImageArray.get(k).getWidth(), listImageArray.get(k).getHeight()).thumbnail(0.7f)
                            .skipMemoryCache(false)
                            .listener(new RequestListener<String, Bitmap>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    progressBarImage.setVisibility(View.GONE);
                                    scrollView.setVisibility(View.VISIBLE);
                                    image.setImageBitmap(resource);
                                    image.setTag(position);
                                    Canvas canvas = new Canvas(resource);
                                    final List<MapCoordinatesDetails> rectangles = new ArrayList<>();
                                    for (int det = 0; det < listImageArray.get(position).getMapDetailDatas().size(); det++) {

                                        final int pos = det;
                                        String[] split_coords = listImageArray.get(position).getMapDetailDatas().get(det).getCoords().split(",");
                                        top = split_coords[0];
                                        left = split_coords[1];
                                        right = split_coords[2];
                                        bottom = split_coords[3];
                                        paint = new Paint();

                                        paint.setStrokeWidth(5);
                                        paint.setColor(Color.parseColor("#00000000"));
//                                                                        paint.setColor(Color.parseColor("#e50000"));
                                        paint.setStyle(Paint.Style.STROKE);

                                        final Rect rect = new Rect(Integer.parseInt(top), Integer.parseInt(left), Integer.parseInt(right), Integer.parseInt(bottom));
                                        canvas.drawRect(rect, paint);
                                        rectangles.add(new MapCoordinatesDetails(rect, listImageArray.get(position).getMapDetailDatas().get(det).getCoords(), listImageArray.get(position).getMapDetailDatas().get(det).getMenuid(), listImageArray.get(position).getMapDetailDatas().get(det).getRedirect_url(), listImageArray.get(position).getMapDetailDatas().get(det).getCmsid(), listImageArray.get(position).getMapDetailDatas().get(det).getIs_force_login(), listImageArray.get(position).getMapDetailDatas().get(det).getAgenda_id(), listImageArray.get(position).getMapDetailDatas().get(det).getGroup_id(), listImageArray.get(position).getMapDetailDatas().get(det).getExhi_id(), listImageArray.get(position).getMapDetailDatas().get(det).getSuper_group_id(), listImageArray.get(position).getMapDetailDatas().get(det).getExhi_sub_cat_id(), listImageArray.get(position).getMapDetailDatas().get(det).getAll_exhi_sub_cat(), listImageArray.get(position).getMapDetailDatas().get(det).getIs_contains_data(), listImageArray.get(position).getMapDetailDatas().get(det).getIs_user_agenda()));

                                        final GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.OnGestureListener() {
                                            @Override
                                            public boolean onDown(MotionEvent e) {
                                                return false;
                                            }

                                            @Override
                                            public void onShowPress(MotionEvent e) {

                                            }

                                            @Override
                                            public boolean onSingleTapUp(MotionEvent e) {
                                                final List<MapCoordinatesDetails> rectangles = rectanglesList.get(position);
                                                for (int i = 0; i < rectangles.size(); i++) {
                                                    Rect rect = rectangles.get(i).getRect();
                                                    RectF rectF = new RectF(rect);
                                                    Matrix currentMatrix = image.getImageMatrix();
                                                    currentMatrix.mapRect(rectF);

                                                    if (rectF.contains((int) e.getX(), (int) e.getY())) {
//                                                        ToastC.show(getActivity(),""+rect);
                                                        if (!(rectangles.get(i).getCmsid().equalsIgnoreCase(""))) {
                                                            sessionManager.strMenuId = rectangles.get(i).getCmsid().toString();
                                                            sessionManager.strModuleId = "0";
                                                            sessionManager.cms_id(rectangles.get(i).getCmsid().toString());
                                                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                            GlobalData.CURRENT_FRAG = GlobalData.CMS_FRAGMENT_TEST1;
                                                            loadFragment();
                                                        } else if (!(rectangles.get(i).getMenuid().equalsIgnoreCase(""))) {
                                                            sessionManager.strMenuId = rectangles.get(i).getMenuid();
                                                            sessionManager.strModuleId = "0";
                                                            sessionManager.setMenuid(rectangles.get(i).getMenuid());
                                                            sessionManager.set_isForceLogin(rectangles.get(i).getIs_force_login());


                                                            if (rectangles.get(i).getMenuid().equalsIgnoreCase("19")) {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                                                                loadFragment();
//                                                                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                                                                drawer.closeDrawer(GravityCompat.START);
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("1")) {
                                                                GlobalData.is_user_agenda = false;
                                                                if (rectangles.get(i).getGroup_id().isEmpty() && rectangles.get(i).getAgenda_id().isEmpty()) {
                                                                    isAgendaGroupAndCategoryExist();
                                                                } else if (!rectangles.get(i).getGroup_id().isEmpty()) {
                                                                    sessionManager.setAgendagroupID(rectangles.get(i).getGroup_id());
                                                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                    GlobalData.CURRENT_FRAG = GlobalData.AgendaGroupWiseFragment;
                                                                    loadFragment();
                                                                } else {
                                                                    sessionManager.setAgendaCategoryId(rectangles.get(i).getAgenda_id());
                                                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                    GlobalData.CURRENT_FRAG = GlobalData.AGENDA_FRAGMENT;
                                                                    loadFragment();
                                                                }
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("3")) {

                                                                if (rectangles.get(i).getExhi_id().isEmpty() && rectangles.get(i).getAll_exhi_sub_cat().equalsIgnoreCase("1")) {
                                                                    sessionManager.setExhibitorParentCategoryId(rectangles.get(i).getExhi_id());
                                                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                    GlobalData.CURRENT_FRAG = GlobalData.ExhibitorSubCategoryListFragment;
                                                                    loadFragment();
                                                                } else if (!rectangles.get(i).getExhi_id().isEmpty()) {
                                                                    sessionManager.setExhibitorParentCategoryId(rectangles.get(i).getExhi_id());
                                                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                    GlobalData.CURRENT_FRAG = GlobalData.ExhibitorSubCategoryListFragment;
                                                                    loadFragment();
                                                                } else if ((!rectangles.get(i).getExhi_sub_cat_id().isEmpty())) {
                                                                    sessionManager.setExhibitorSubCategoryDesc(rectangles.get(i).getExhi_sub_cat_id());
                                                                    sessionManager.setExhibitorSponsorCategoryId("");
                                                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                    GlobalData.CURRENT_FRAG = GlobalData.ExhibitorSubCategoryWiseListFragment;
                                                                    loadFragment();
                                                                } else {
                                                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                    GlobalData.CURRENT_FRAG = GlobalData.EXHIBITOR_FRAGMENT;
                                                                    loadFragment();
                                                                }


                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("2")) {
                                                                attendeeRedirection(false, false);

                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("7")) {

                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.SPEAKER_FRAGMENT;
                                                                loadFragment();

                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("27")) {

                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.notificationListingFragment;
                                                                loadFragment();

                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("6")) {

                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.View_Note_Fragment;
                                                                loadFragment();

                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("10")) {
                                                                sessionManager.Map_coords = "";
                                                                sessionManager.exhibitor_standNumber = "";
                                                                isMapGroupExist();

                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("17")) {

                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.SOCIAL_FRAGMENT;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("12")) {

                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.PRIVATE_MESSAGE;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("9"))  // Presantation
                                                            {

                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.Presantation_Fragment;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("11")) // Photos
                                                            {

                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.Photo_Fragment;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("13")) // Message Wall
                                                            {

                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("15")) // Survey
                                                            {

                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.Survey_Fragment;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("16")) // Document
                                                            {

                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.Document_Fragment;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("22")) // Silent Aucation
                                                            {

                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.Silent_Auction;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("43")) {

                                                                isSponsorGroupExist();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("44")) {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.TweitterFeed;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("24")) {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.Online_shop;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("23")) {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.live_Auction;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("45")) {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.activityFeed;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("46")) {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.InstagramFeed;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("20"))  // Fundraising
                                                            {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("25")) {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.DonateFor_veteran;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("47")) {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.facebookFeed;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("50"))  // Fundraising
                                                            {
                                                                sessionManager.setQandAgroupID("");
                                                                if (rectangles.get(i).getIs_contains_data().equalsIgnoreCase("1")) {
                                                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                    GlobalData.CURRENT_FRAG = GlobalData.QAList_Fragment;
                                                                    loadFragment();
                                                                } else {

                                                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                    GlobalData.CURRENT_FRAG = GlobalData.QandAGroupWiseFragment;
                                                                    loadFragment();
                                                                }

                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("49")) {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.FavoriteList_Fragment;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("52")) {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.Gamification_Fragment;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("53")) {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.ExhibiorLead_Fragment;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("54")) {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.PhotoFilter_Fragment;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("57")) {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.QrcodeScannerSharecontactFragment;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("59")) {
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.MatchMakingFragment;
                                                                loadFragment();
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("21")) {

                                                                if (rectangles.get(i).getSuper_group_id().isEmpty() && rectangles.get(i).getGroup_id().isEmpty()) {
                                                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                    GlobalData.CURRENT_FRAG = GlobalData.CMSGroupListFragment;
                                                                    loadFragment();
                                                                } else if (!(rectangles.get(i).getSuper_group_id().isEmpty())) {
                                                                    sessionManager.setCmsSuperGroupId(rectangles.get(i).getSuper_group_id());
                                                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                    GlobalData.CURRENT_FRAG = GlobalData.CMSGroupChildListFragment;
                                                                    loadFragment();
                                                                } else if (!(rectangles.get(i).getGroup_id().isEmpty())) {
                                                                    sessionManager.setCmsChildGroupId(rectangles.get(i).getGroup_id());
                                                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                    GlobalData.CURRENT_FRAG = GlobalData.CMSListFragment;
                                                                    loadFragment();
                                                                }
                                                            } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("56")) {
                                                                if (GlobalData.checkForUIDVersion())
                                                                {
                                                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                    GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                                                                    loadFragment();
                                                                }
                                                                else
                                                                {
                                                                    if (sessionManager.getRolId().equalsIgnoreCase("4")) {//postponed - pending
                                                                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                        GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                                                                        loadFragment();
                                                                    }
                                                                    else if (sessionManager.getRolId().equalsIgnoreCase("6"))
                                                                    {//postponed - pending
                                                                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                        GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                                                                        loadFragment();
                                                                    }
                                                                    else if (sessionManager.getRolId().equalsIgnoreCase("7") && //changes applied
                                                                            sessionManager.isModerater().equalsIgnoreCase("1")) {//changes applied
                                                                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                        GlobalData.CURRENT_FRAG = GlobalData.moderatorRequestMettingFragment;
                                                                        loadFragment();

                                                                    }
                                                                }
                                                            }
                                                        } else if (!(rectangles.get(i).getRedirect_url().equalsIgnoreCase(""))) {
                                                            Bundle bundle = new Bundle();
                                                            if (rectangles.get(i).getRedirect_url().contains("deep_link")) {
                                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(rectangles.get(i).getRedirect_url()));
                                                                startActivity(intent);
                                                            } else {
                                                                bundle.putString("Social_url", rectangles.get(i).getRedirect_url());
                                                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                                                                loadFragment(bundle);
                                                            }
                                                        }

                                                    }
                                                }
                                                return false;
                                            }

                                            @Override
                                            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                                                return false;
                                            }

                                            @Override
                                            public void onLongPress(MotionEvent e) {

                                            }

                                            @Override
                                            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                                                return false;
                                            }
                                        });
                                        image.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {
                                                gestureDetector.onTouchEvent(event);

                                                switch (event.getAction()) {
                                                    case MotionEvent.ACTION_DOWN:
                                                        return true;
                                                    case MotionEvent.ACTION_UP:
                                                        return true;
                                                    case MotionEvent.ACTION_MOVE:
                                                        return true;
                                                }
                                                return false;
                                            }
                                        });
                                    }
                                    rectanglesList.add(rectangles);
                                    return false;
                                }
                            }).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT).into(image);
                    relativeLayout.addView(image);
                    linear_dynamicHome.addView(relativeLayout);
                }
                txt_link.setTextColor(Color.parseColor(sessionManager.getMenuTextColor()));
                txt_link.setBackgroundColor(Color.parseColor(sessionManager.getMenuBackColor()));

                if (sessionManager.isLogin() && sessionManager.getIsLeadRetrivalEnabled().equalsIgnoreCase("1")) {

                    txt_link.setVisibility(View.VISIBLE);
                } else {
                    txt_link.setVisibility(View.GONE);
                }
            } else {
                linear_dynamicHome.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                mDrawerList.setVisibility(View.VISIBLE);
                txt_link.setVisibility(View.GONE);
            }
            if (sessionManager.isLogin() && sessionManager.getIsLeadRetrivalEnabled().equalsIgnoreCase("1")) {
                getExhibitorLeadOffline();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logoutUserFromApp() {
        if (sessionManager.isLoginNoti) {
            if (sessionManager.isLogin()) {
                sessionManager.logout();
                sessionManager.isLoginNoti = false;
                startActivity(new Intent(MainActivity.this, SearchApp_Activity.class));
                finish();
            }

        }

    }


    private void notificationUpdate() {
        if (GlobalData.isNetworkAvailable(MainActivity.this)) {
            new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.NotificationUpdateLogs, Param.notificationUpdateLog(sessionManager.getUserId(), sessionManager.getEventId(), sessionManager.notificationId, "2"), 21, false, this);
        }
    }

    public void redirectionFromNotifcationTray(Intent intent) {
        if (intent.hasExtra("message_type")) {
            if (intent.getStringExtra("message_type").equalsIgnoreCase("Private")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {
                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                    loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                    loadFragment();
                }
            } else if (intent.getStringExtra("message_type").equalsIgnoreCase("Won Auction")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {
                    GlobalData.CURRENT_FRAG = GlobalData.CheckOut_Fragment;
                    loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.CheckOut_Fragment;
                    loadFragment();
                }
            } else if (intent.getStringExtra("message_type").equalsIgnoreCase("Outbid Auction")) {

                sessionManager.slilentAuctionP_id = intent.getStringExtra("product_id");
                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.SilentAuctionDetail_Fragment;
                    loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.SilentAuctionDetail_Fragment;
                    loadFragment();
                }
            } else if (intent.getStringExtra("message_type").equalsIgnoreCase("Attendee")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragmentTag;
                    loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragmentTag;
                    loadFragment();
                }
            } else if (intent.getStringExtra("message_type").equalsIgnoreCase("RequestMeeting")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                    loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                    loadFragment();
                }
            } else if (intent.getStringExtra("message_type").equalsIgnoreCase("RespondRequest")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                    loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                    loadFragment();
                }
            } else if (intent.getStringExtra("message_type").equalsIgnoreCase("SuggestRequestTime")) {

                sessionManager.mettingId = intent.getStringExtra("product_id");

                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.viewSuggestedTimeList;
                    loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.viewSuggestedTimeList;
                    loadFragment();
                }
            } else if (intent.getStringExtra("message_type").equalsIgnoreCase("AttendeeRequestMeeting")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                    loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                    loadFragment();
                }
            } else if (intent.getStringExtra("message_type").equalsIgnoreCase("AttendeeRespondRequest")) {

                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                    loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                    loadFragment();
                }

            } else if (intent.getStringExtra("message_type").equalsIgnoreCase("ModeratorRequestMeeting")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.moderatorRequestMettingFragment;
                    loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.moderatorRequestMettingFragment;
                    loadFragment();
                }
            } else if (intent.getStringExtra("message_type").equalsIgnoreCase("cms")) {

                sessionManager.mettingId = intent.getStringExtra("product_id");
                sessionManager.setMenuid(intent.getStringExtra("product_id"));

                String menuId = intent.getStringExtra("product_id");
                switch (menuId) {
                    case MenuId.AGENDA_ID:
                        isAgendaGroupAndCategoryExist();
                        break;
                    case MenuId.ATTENDEE_ID:
                        attendeeRedirection(true, false);
                        break;
                    case MenuId.EXIBITOR_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {
                            GlobalData.CURRENT_FRAG = GlobalData.EXHIBITOR_FRAGMENT;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.EXHIBITOR_FRAGMENT;
                            loadFragment();
                        }
                        break;
                    case MenuId.SPEAKER_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.SPEAKER_FRAGMENT;
                            loadFragment();
                        } else {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.SPEAKER_FRAGMENT;
                            loadFragment();
                        }
                        break;
                    case MenuId.SPONSOR_ID:
                        isSponsorGroupExist();
                        break;
                    case MenuId.SOCIAL_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.SOCIAL_FRAGMENT;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.SOCIAL_FRAGMENT;
                            loadFragment();
                        }
                        break;
                    case MenuId.MAP_ID:
                        sessionManager.Map_coords = "";
                        sessionManager.exhibitor_standNumber = "";
                        isMapGroupExist();
                        break;
                    case MenuId.VIEW_NOTES_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.View_Note_Fragment;
                            loadFragment();
                        } else {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.View_Note_Fragment;
                            loadFragment();
                        }
                        break;
                    case MenuId.TWITTER_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.TweitterFeed;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.TweitterFeed;
                            loadFragment();
                        }
                        break;
                    case MenuId.PRESENTATION_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.Presantation_Fragment;
                            loadFragment();
                        } else {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Presantation_Fragment;
                            loadFragment();
                        }
                        break;
                    case MenuId.PRIVATE_MSG_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.PRIVATE_MESSAGE;
                            loadFragment();
                        } else {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.PRIVATE_MESSAGE;
                            loadFragment();
                        }
                        break;
                    case MenuId.PUBLIC_MSG_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                            loadFragment();
                        }
                        break;
                    case MenuId.SURVEY_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.Survey_Fragment;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Survey_Fragment;
                            loadFragment();
                        }
                        break;
                    case MenuId.PHOTOS_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.Photo_Fragment;
                            loadFragment();
                        } else {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Photo_Fragment;
                            loadFragment();
                        }
                        break;
                    case MenuId.DOCUMENT_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.Document_Fragment;

                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Document_Fragment;
                            loadFragment();
                        }
                        break;
                    case MenuId.FUNDRAISING_HOME_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {
                            GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                            loadFragment();
                        }
                        break;
                    case MenuId.SILENT_AUCTION_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.Silent_Auction;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Silent_Auction;
                            loadFragment();
                        }
                        break;
                    case MenuId.LIVE_AUCTION_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.live_Auction;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.live_Auction;
                            loadFragment();
                        }

                        break;
                    case MenuId.BUY_NOW_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.Online_shop;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Online_shop;
                            loadFragment();
                        }
                        break;
                    case MenuId.PLEDGE_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.DonateFor_veteran;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.DonateFor_veteran;
                            loadFragment();
                        }
                        break;
                    case MenuId.ACTIVITY_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.activityFeed;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.activityFeed;
                            loadFragment();
                        }
                        break;
                    case MenuId.INSTAGRAM_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.InstagramFeed;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.InstagramFeed;
                            loadFragment();
                        }
                        break;
                    case MenuId.FACEBOOK_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.facebookFeed;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.facebookFeed;
                            loadFragment();
                        }
                        break;
                    case MenuId.FAVOURITES_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.FavoriteList_Fragment;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.FavoriteList_Fragment;
                            loadFragment();
                        }
                        break;
                    case MenuId.QA_SESSION_ID:
                        sessionManager.setQandAgroupID("");
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.QAList_Fragment;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.QAList_Fragment;
                            loadFragment();
                        }
                        break;
                    case MenuId.VIRTUAL_SUPERMARKET_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.VirtualSuperMarket;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.VirtualSuperMarket;
                            loadFragment();
                        }
                        break;
                    case MenuId.GAMIFICATION_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.Gamification_Fragment;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Gamification_Fragment;
                            loadFragment();
                        }
                        break;
                    case MenuId.LEAD_RETRIVAL_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.ExhibiorLead_Fragment;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.ExhibiorLead_Fragment;
                            loadFragment();
                        }
                        break;
                    case MenuId.PHOTO_FILTER_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.PhotoFilter_Fragment;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.PhotoFilter_Fragment;
                            loadFragment();
                        }
                        break;
                    case MenuId.NOTIFICATION_CENTER_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.notificationListingFragment;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.notificationListingFragment;
                            loadFragment();
                        }
                        break;
                    case MenuId.CMS_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.CMSGroupListFragment;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.CMSGroupListFragment;
                            loadFragment();
                        }
                        break;
                    case MenuId.BADGE_SCANNER_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.QrcodeScannerSharecontactFragment;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.QrcodeScannerSharecontactFragment;
                            loadFragment();
                        }
                        break;
                    case MenuId.MATCH_MAKING_ID:
                        if (GlobalData.Fragment_Stack.size() >= 1) {

                            GlobalData.CURRENT_FRAG = GlobalData.MatchMakingFragment;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.MatchMakingFragment;
                            loadFragment();
                        }
                        break;
                }

            } else if (intent.getStringExtra("message_type").equalsIgnoreCase("AttendeeSuggestRequestTime")) {

                sessionManager.mettingId = intent.getStringExtra("product_id");

                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.viewSuggestedTimeList;
                    loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.viewSuggestedTimeList;
                    loadFragment();
                }
            } else if (sessionManager.notiMessageType.equalsIgnoreCase("custom_page")) {
                sessionManager.cms_id(intent.getStringExtra("product_id"));
                sessionManager.strMenuId = "0";
                sessionManager.strModuleId = intent.getStringExtra("product_id");
                if (GlobalData.Fragment_Stack.size() >= 1) {
                    GlobalData.CURRENT_FRAG = GlobalData.CMS_FRAGMENT_TEST1;
                    loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.CMS_FRAGMENT_TEST1;
                    loadFragment();
                }

            }

            intent.removeExtra("message_type");
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        handleIntent(intent);
        redirectionFromNotifcationTray(intent);
    }


    public void handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri uri = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && uri != null) {
            if (uri.getScheme().equalsIgnoreCase(GlobalData.deeplinkHostName)) {
                String menuId = GlobalData.getDataFromBaseEncrypt(uri.getQueryParameter("node_main"));
                String moduleId = GlobalData.getDataFromBaseEncrypt(uri.getQueryParameter("node_sub"));
                String neventId = GlobalData.getDataFromBaseEncrypt(uri.getQueryParameter("nevent"));
                if (sessionManager.getEventId().equalsIgnoreCase(neventId)) {
                    deepLinkRedirectionMethod(menuId, moduleId, true);
                }

            }
        }

        if (intent.hasExtra("notification_id") && intent.hasExtra("message_type")) {
            if (intent.getStringExtra("message_type").equalsIgnoreCase("cms")) {
                notificationUpdate();
            }
//            notificationId=intent.getStringExtra("notification_id");
        }


    }


    public void deepLinkRedirectionMethod(String menuId, String moduleId, boolean isFromActivity) {
        if (isFromActivity) {
            if (menuId.equalsIgnoreCase("1")) {
                sessionManager.agenda_id(moduleId);
                if (GlobalData.Fragment_Stack.size() >= 1) {
                    GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                }

            } else if (menuId.equalsIgnoreCase("7")) {
                SessionManager.speaker_id = moduleId;
                if (GlobalData.Fragment_Stack.size() >= 1) {
                    GlobalData.CURRENT_FRAG = GlobalData.Speaker_Detail_Fragment;
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Speaker_Detail_Fragment;
                }
            }
        } else {
            if (menuId.equalsIgnoreCase("1")) {
                sessionManager.agenda_id(moduleId);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
            } else if (menuId.equalsIgnoreCase("7")) {
                SessionManager.speaker_id = moduleId;
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Speaker_Detail_Fragment;
            }
        }
        loadFragment();

    }


    private void dailogClickwithaction(Context context) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(sessionManager.notiTitle)
                .content(sessionManager.notiMessage)
                .positiveText("Open")
                .onPositive
                        (new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                if (!sessionManager.notificationId.isEmpty())
                                    notificationUpdate();
                                if (sessionManager.notiMessageType.equalsIgnoreCase("Private")) {

                                    dialog.dismiss();
                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                                    loadFragment();
                                } else if (sessionManager.notiMessageType.equalsIgnoreCase("RequestMeeting")) {
                                    dialog.dismiss();
                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                                    loadFragment();
                                } else if (sessionManager.notiMessageType.equalsIgnoreCase("Outbid Auction")) {

                                    dialog.dismiss();

                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.SilentAuctionDetail_Fragment;
                                    loadFragment();
                                } else if (sessionManager.notiMessageType.equalsIgnoreCase("AttendeeRequestMeeting")) {

                                    dialog.dismiss();
                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                                    loadFragment();
                                } else if (sessionManager.notiMessageType.equalsIgnoreCase("ModeratorRequestMeeting")) {

                                    dialog.dismiss();
                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.moderatorRequestMettingFragment;
                                    loadFragment();
                                } else if (sessionManager.notiMessageType.equalsIgnoreCase("Attendee")) {

                                    dialog.dismiss();
                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragmentTag;
                                    loadFragment();
                                } else if (sessionManager.notiMessageType.equalsIgnoreCase("cms")) {

                                    sessionManager.setMenuid(sessionManager.Notificationmenu_id);
                                    NotificationRedirection(sessionManager.Notificationmenu_id);

                                } else if (sessionManager.notiMessageType.equalsIgnoreCase("custom_page")) {
                                    sessionManager.cms_id(sessionManager.Notificationmenu_id);
                                    sessionManager.strMenuId = "0";
                                    sessionManager.strModuleId = sessionManager.Notificationmenu_id;

                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.CMS_FRAGMENT_TEST1;
                                    loadFragment();
                                } else {
                                    dialog.dismiss();
                                }
                            }
                        }).show();

        dialog.setCancelable(true);
    }


    public void NotificationRedirection(String menuId) {

        switch (menuId) {

            case MenuId.AGENDA_ID:
                isAgendaGroupAndCategoryExist();
                break;
            case MenuId.ATTENDEE_ID:
                attendeeRedirection(false, false);
                break;
            case MenuId.EXIBITOR_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.EXHIBITOR_FRAGMENT;
                loadFragment();
                break;
            case MenuId.SPEAKER_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.SPEAKER_FRAGMENT;
                loadFragment();
                break;
            case MenuId.SPONSOR_ID:
                isSponsorGroupExist();
                break;
            case MenuId.SOCIAL_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.SOCIAL_FRAGMENT;
                loadFragment();
                break;
            case MenuId.MAP_ID:
                sessionManager.Map_coords = "";
                sessionManager.exhibitor_standNumber = "";
                isMapGroupExist();
                break;
            case MenuId.VIEW_NOTES_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.View_Note_Fragment;
                loadFragment();
                break;
            case MenuId.TWITTER_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.TweitterFeed;
                loadFragment();
                break;
            case MenuId.PRESENTATION_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Presantation_Fragment;
                loadFragment();
                break;
            case MenuId.PRIVATE_MSG_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.PRIVATE_MESSAGE;
                loadFragment();
                break;
            case MenuId.PUBLIC_MSG_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                loadFragment();
                break;
            case MenuId.SURVEY_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Survey_Fragment;
                loadFragment();
                break;
            case MenuId.PHOTOS_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Photo_Fragment;
                loadFragment();
                break;
            case MenuId.DOCUMENT_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Document_Fragment;
                loadFragment();
                break;
            case MenuId.FUNDRAISING_HOME_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                loadFragment();
                break;
            case MenuId.SILENT_AUCTION_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Silent_Auction;
                loadFragment();
                break;
            case MenuId.LIVE_AUCTION_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.live_Auction;
                loadFragment();
                break;
            case MenuId.BUY_NOW_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Online_shop;
                loadFragment();
                break;
            case MenuId.PLEDGE_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.DonateFor_veteran;
                loadFragment();
                break;
            case MenuId.ACTIVITY_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.activityFeed;
                loadFragment();
                break;
            case MenuId.INSTAGRAM_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.InstagramFeed;
                loadFragment();
                break;
            case MenuId.FACEBOOK_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.facebookFeed;
                loadFragment();
                break;
            case MenuId.FAVOURITES_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.FavoriteList_Fragment;
                loadFragment();
                break;
            case MenuId.QA_SESSION_ID:
                sessionManager.setQandAgroupID("");
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.QAList_Fragment;
                loadFragment();
                break;
            case MenuId.VIRTUAL_SUPERMARKET_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.VirtualSuperMarket;
                break;
            case MenuId.GAMIFICATION_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Gamification_Fragment;
                loadFragment();
                break;
            case MenuId.LEAD_RETRIVAL_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.ExhibiorLead_Fragment;
                loadFragment();
                break;
            case MenuId.PHOTO_FILTER_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.PhotoFilter_Fragment;
                loadFragment();
                break;
            case MenuId.NOTIFICATION_CENTER_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.notificationListingFragment;
                loadFragment();
                break;
            case MenuId.CMS_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.CMSGroupListFragment;
                loadFragment();
                break;
            case MenuId.BADGE_SCANNER_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.QrcodeScannerSharecontactFragment;
                loadFragment();
                break;
            case MenuId.MATCH_MAKING_ID:
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.MatchMakingFragment;
                loadFragment();
                break;

        }

    }


    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(changeReceiver, intentFilter);

        registerReceiver(broadcastReceiver, new IntentFilter(Update_Profile));
        registerReceiver(notificationBroadcasr, new IntentFilter(logoutNoti));
        registerReceiver(simpleDialogBroadCast, new IntentFilter(NotificationsimpleDialog));
        registerReceiver(simpleWithActionDialogBroadCast, new IntentFilter(NotificationsimpleDialogwithAction));
        registerReceiver(UpdateNotificationCounter, new IntentFilter(UpdateCounterFromNotification));
        registerReceiver(updateExhitiorMyLeadData, new IntentFilter(getexhibitorMyLeadUpdate));

        try {
            GlobalData.hideSoftKeyboard(MainActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logoutUserFromApp();
    }

    protected void onPause() {
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(notificationBroadcasr);
        unregisterReceiver(simpleDialogBroadCast);
        unregisterReceiver(simpleWithActionDialogBroadCast);
        unregisterReceiver(UpdateNotificationCounter);
        unregisterReceiver(updateExhitiorMyLeadData);
        if (Notitimer != null)
            Notitimer.cancel();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(changeReceiver);
        if (Notitimer != null) {
            Notitimer.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LISessionManager.getInstance(MainActivity.this).onActivityResult(MainActivity.this, requestCode, resultCode, data);
        if (requestCode == 2000) {
            checkForGPS();
        } else if (requestCode == PickConfig.PICK_REQUEST_CODE) {

//                  selectImages = data.getStringArrayListExtra("data");
//                    counter = data.getIntExtra("cnt", 0);
            if (data != null) {
//
                if (data.getIntExtra(PickConfig.EXTRA_FLAG, PickConfig.FLAG_MODE) == 3) {
                    PrivateMessageDetail_Fragment.linearimage_load.setVisibility(View.VISIBLE);
                    PrivateMessageDetail_Fragment.selectImages.addAll(data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST));

                    PrivateMessageDetail_Fragment.gallaryBeansarraylist.clear();
                    for (int j = 0; j < PrivateMessageDetail_Fragment.selectImages.size(); j++) {
                        PrivateMessageDetail_Fragment.selectimage(PrivateMessageDetail_Fragment.selectImages.get(j).toString());

                    }
                } else if (data.getIntExtra(PickConfig.EXTRA_FLAG, PickConfig.FLAG_MODE) == 4) {
                    Photo_Fragment.linearimage_load.setVisibility(View.VISIBLE);
                    Photo_Fragment.selectImages.addAll(data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST));

                    Photo_Fragment.gallaryBeansarraylist.clear();
                    for (int j = 0; j < Photo_Fragment.selectImages.size(); j++) {
                        Photo_Fragment.selectimage(Photo_Fragment.selectImages.get(j).toString());

                    }
                } else if (data.getIntExtra(PickConfig.EXTRA_FLAG, PickConfig.FLAG_MODE) == 5) {
                    PublicMessage_Fragment.linearimage_load.setVisibility(View.VISIBLE);
                    PublicMessage_Fragment.selectImages.addAll(data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST));

                    PublicMessage_Fragment.gallaryBeansarraylist.clear();
                    for (int j = 0; j < PublicMessage_Fragment.selectImages.size(); j++) {
                        PublicMessage_Fragment.selectimage(PublicMessage_Fragment.selectImages.get(j).toString());

                    }
                } else if (data.getIntExtra(PickConfig.EXTRA_FLAG, PickConfig.FLAG_MODE) == 7) {
                    AddItem_Fragment.linearimage_load.setVisibility(View.VISIBLE);
                    AddItem_Fragment.selectImages.addAll(data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST));

                    AddItem_Fragment.gallaryBeansarraylist.clear();
                    for (int j = 0; j < AddItem_Fragment.selectImages.size(); j++) {
                        AddItem_Fragment.selectimage(AddItem_Fragment.selectImages.get(j).toString());

                    }
                } else if (data.getIntExtra(PickConfig.EXTRA_FLAG, PickConfig.FLAG_MODE) == 8) {
                    EditItemFragment.selectImages.addAll(data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST));
                    EditItemFragment.gallaryBeansarraylist.clear();
                    sessionManager.gallryimg = "gallryimg";
                    for (int j = 0; j < EditItemFragment.selectImages.size(); j++) {
                        EditItemFragment.selectimage(EditItemFragment.selectImages.get(j).toString(), "0");

                    }
                } else if (data.getIntExtra(PickConfig.EXTRA_FLAG, PickConfig.FLAG_MODE) == 9) {
                    ActivitySharePost_Fragment.linearimage_load.setVisibility(View.VISIBLE);
                    ActivitySharePost_Fragment.horizontalScrollView1.setVisibility(View.VISIBLE);
                    ActivitySharePost_Fragment.selectImages.addAll(data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST));

                    ActivitySharePost_Fragment.gallaryBeansarraylist.clear();
                    for (int j = 0; j < ActivitySharePost_Fragment.selectImages.size(); j++) {
                        ActivitySharePost_Fragment.selectimage(ActivitySharePost_Fragment.selectImages.get(j).toString());
                    }
                }

            }
        } else {
            if (GlobalData.currentFragment == GlobalData.CurrentFragment.PublicMessage) {

                PublicMessage_Fragment.message_adapter.onActivityResult(requestCode, resultCode, data);

            } else if (GlobalData.currentFragment == GlobalData.CurrentFragment.PrivateMessage) {

                Private_Message_Fragment.message_adapter.onActivityResult(requestCode, resultCode, data);

            }

            if (GlobalData.currentFragment == GlobalData.CurrentFragment.photo) {

                Photo_Fragment.photo_adapter.onActivityResult(requestCode, resultCode, data);
            }

            if (GlobalData.currentFragment == GlobalData.CurrentFragment.ExhibitorList_Fragment_New) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (fragment instanceof ExhibitorList_Fragment_New) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {


                if (GlobalData.currentFragment == GlobalData.CurrentFragment.PublicMessage) {

                    PublicMessage_Fragment.message_adapter.onRequestPermessionView(requestCode, permissions, grantResults);

                } else if (GlobalData.currentFragment == GlobalData.CurrentFragment.PrivateMessage) {

                    Private_Message_Fragment.message_adapter.onRequestPermessionView(requestCode, permissions, grantResults);

                }
                if (GlobalData.currentFragment == GlobalData.CurrentFragment.photo) {
                    Photo_Fragment.photo_adapter.onRequestPermessionView(requestCode, permissions, grantResults);
                }

            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void parseMultilang(JSONObject jsonObject) {
        languagesArray.clear();
        try {
            JSONArray jArrayLang = jsonObject.optJSONArray("lang_list");
            for (int i = 0; i < jArrayLang.length(); i++) {
                JSONObject object = jArrayLang.getJSONObject(i);
                if (object != null) {
                    Languages languages = new Languages();
                    languages.lang_id = object.getString("lang_id");
                    languages.lang_default = object.getString("lang_default");
                    languages.lang_icon = object.getString("lang_icon");
                    languages.lang_name = object.getString("lang_name");
                    if (sessionManager.getLangId().isEmpty()) {
                        if (languages.lang_default.equalsIgnoreCase("1")) {
                            defualtLangId = languages.lang_id;
                            sessionManager.setLangId(defualtLangId);
                            sessionManager.setLangImgUrl(languages.lang_icon);
                            setLangImg(languages.lang_icon);
                        }
                    }
                    languagesArray.add(languages);
                }

            }

            if (languagesArray.size() != 0) {
                if (languagesArray.size() > 1) {
                    img_lang.setVisibility(View.VISIBLE);
                } else {
                    img_lang.setVisibility(View.GONE);
                }
            } else {
                img_lang.setVisibility(View.GONE);
            }
            languageAdapter = new LanguageAdapter(MainActivity.this, R.layout.lang_list_item, languagesArray, getApplicationContext());
            getDefaultLangString(Eid, defualtLangId);
            Log.e("AITL", "parseMultilang: " + languagesArray.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDefaultLangString(String event_id, String lang_id) {
        if (GlobalData.isNetworkAvailable(MainActivity.this)) {
            new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.getDefaultLang, Param.getDefaultLangParam(event_id, lang_id), 7, false, this);

        }
    }

    public void getDefultLanOnClick(String event_id, String lang_id) {
        if (GlobalData.isNetworkAvailable(MainActivity.this)) {
            new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.getDefaultLang, Param.getDefaultLangParam(event_id, lang_id), 8, true, this);

        }
    }

    public void showLangListDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setAdapter(languageAdapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO - Code when list item is clicked (int which - is param that gives you the index of clicked item)
                        dialog.dismiss();
                        Log.e("AITL", "showLangListDialog: onClick");
                        Languages lang = languagesArray.get(which);
                        defualtLangId = lang.lang_id;
                        sessionManager.setLangId(defualtLangId);
                        sessionManager.setLangImgUrl(lang.lang_icon);
                        setLangImg(lang.lang_icon);
                        getDefultLanOnClick(Eid, defualtLangId);
//                        finish();
//                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("AITL", "showLangListDialog: setNegativeButton");
                        dialog.dismiss();

                    }
                })
                .setTitle("Select Default Language")
                .setCancelable(true)
                .show();
    }

    // Check for SponsorGroupExist
    public void isSponsorGroupExist() {
        sessionManager.setSponsorPrentGroupID("");
        ArrayList<GroupModuleData> groupModuleDataArrayList = new ArrayList<>();
        groupModuleDataArrayList = sqLiteDatabaseHandler.getMainGroupData(sessionManager.getEventId(), sessionManager.getMenuid());
        if (groupModuleDataArrayList.size() == 0) {

            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.SponsorGroupWiseFragment;
            loadFragment();
        } else {
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Sponsor_Fragment;
            loadFragment();
        }
    }

    // Check for  SponsorGroupExist from LeftSide
    public void isSponsorGroupExistSimpleLeft() {
        ArrayList<GroupModuleData> groupModuleDataArrayList = new ArrayList<>();
        groupModuleDataArrayList = sqLiteDatabaseHandler.getMainGroupData(sessionManager.getEventId(), sessionManager.getMenuid());
        sessionManager.setSponsorPrentGroupID("");
        if (groupModuleDataArrayList.size() == 0) {

            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.SponsorGroupWiseFragment;
        } else {
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Sponsor_Fragment;
        }
    }

    // Check for MapGroupExist
    public void isMapGroupExist() {
        ArrayList<GroupModuleData> groupModuleDataArrayList = new ArrayList<>();
        groupModuleDataArrayList = sqLiteDatabaseHandler.getMainGroupData(sessionManager.getEventId(), sessionManager.getMenuid());
        sessionManager.setMapPrentGroupID("");
        if (groupModuleDataArrayList.size() == 0) {
            sessionManager.setMapPrentGroupID("");
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.MapGroupWiseFragment;
            loadFragment();
        } else {
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Map_fragment;
            loadFragment();
        }
    }

    // Check for MapGroupExist From LeftSide
    public void isMapGroupExistSimpleLeft() {
        ArrayList<GroupModuleData> groupModuleDataArrayList = new ArrayList<>();
        groupModuleDataArrayList = sqLiteDatabaseHandler.getMainGroupData(sessionManager.getEventId(), sessionManager.getMenuid());
        sessionManager.setMapPrentGroupID("");
        if (groupModuleDataArrayList.size() == 0) {

            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.MapGroupWiseFragment;

        } else {
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Map_fragment;
        }
    }

    // Check for AgendaGroupExistandCategory
    public void isAgendaGroupAndCategoryExist() {
        ArrayList<GroupModuleData> groupModuleDataArrayList = new ArrayList<>();
        groupModuleDataArrayList = sqLiteDatabaseHandler.getMainGroupData(sessionManager.getEventId(), sessionManager.getMenuid());
        sessionManager.setAgendagroupID("");
        sessionManager.setAgendaCategoryId("");
        if (groupModuleDataArrayList.size() == 0) {

            ArrayList<AgendaCategory> mapNewDataArrayList = new ArrayList<>();
            mapNewDataArrayList = sqLiteDatabaseHandler.getAgendaCatListData(sessionManager.getEventId(), sessionManager.getAgendagroupID(), sessionManager.isLogin()
                    , sessionManager.getAllowShowAllAgenda()
                    , sessionManager.getAttendee_agendaList());

            if (mapNewDataArrayList.size() == 0) {
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.AGENDA_FRAGMENT;
                loadFragment();
            } else {
                sessionManager.setAgendaCategoryId("");
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.AgendaGroupWiseFragment;
                loadFragment();
            }
        } else {
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.AgendaGroupListFragment;
            loadFragment();
        }
    }

    // Check for AgendaGroupExistandCategory  from LeftSide
    public void isAgendaGroupAndCategoryExistForSimpleLeft() {
        ArrayList<GroupModuleData> groupModuleDataArrayList = new ArrayList<>();
        sessionManager.setAgendagroupID("");
        sessionManager.setAgendaCategoryId("");
        groupModuleDataArrayList = sqLiteDatabaseHandler.getMainGroupData(sessionManager.getEventId(), sessionManager.getMenuid());
        if (groupModuleDataArrayList.size() == 0) {

            ArrayList<AgendaCategory> mapNewDataArrayList = new ArrayList<>();
            mapNewDataArrayList = sqLiteDatabaseHandler.getAgendaCatListData(sessionManager.getEventId(), sessionManager.getAgendagroupID(), sessionManager.isLogin()
                    , sessionManager.getAllowShowAllAgenda()
                    , sessionManager.getAttendee_agendaList());

            if (mapNewDataArrayList.size() == 0) {
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.AGENDA_FRAGMENT;

            } else {
                sessionManager.setAgendaCategoryId("");
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.AgendaGroupWiseFragment;
            }
        } else {
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.AgendaGroupListFragment;
        }
    }

    // set LangFlag in ImageView in topView
    private void setLangImg(String url) {
        img_lang.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext())
                .load(MyUrls.LangImgUrl + url)
                .into(img_lang);
    }

    // set backGroundColor of Scrollview
    public void setDrawerBackColor() {
        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            scrollView.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
        } else {
            scrollView.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
        }
    }

    // get ExhibitorLead Offline at firstTime
    public void getExhibitorLeadOffline() {
        if (sessionManager.isLogin() && sessionManager.getIsLeadRetrivalEnabled().equalsIgnoreCase("1")) {
            if (GlobalData.isNetworkAvailable(MainActivity.this)) {
                new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.getMyLeadOffline, Param.getExhibitorLeadDataOffline(sessionManager.getEventId(), sessionManager.getUserId()), 10, false, this);
            } else {
                GlobalData.exhibitorMyLeadTabLoad(MainActivity.this);
            }
        }
    }

    // Insert ExhibitorLead Offline at FirstTime
    private void loadMyExiLeadOfflineData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("data")) {
                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                JSONArray jsonArray = jsonObjectData.getJSONArray("leads");
                if (jsonArray != null && jsonArray.length() > 0) {
                    if (sqLiteDatabaseHandler.isMyExiLeadData(sessionManager.getEventId(), sessionManager.getUserId())) {
                        sqLiteDatabaseHandler.deleteMyExiLeadData(sessionManager.getEventId(), sessionManager.getUserId());
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        ExhibitorLead_MyLeadData_Offline leadData_offline = new ExhibitorLead_MyLeadData_Offline();
                        leadData_offline.setId(object.getString("Id"));
                        leadData_offline.setRoleId(object.getString("Role_id"));
                        leadData_offline.setOrganisorId(object.getString("Organisor_id"));
                        leadData_offline.setFirstname(object.getString("Firstname"));
                        leadData_offline.setLastname(object.getString("Lastname"));
                        leadData_offline.setEmail(object.getString("Email"));
                        leadData_offline.setTitle(object.getString("Title"));
                        leadData_offline.setCompanyName(object.getString("Company_name"));
                        leadData_offline.setData(object.getString("data").toString());
                        leadData_offline.setBadgeNumber(object.getString("Email"));
                        sqLiteDatabaseHandler.insertMyExiLeadData(leadData_offline, sessionManager.getEventId(), sessionManager.getUserId());
                    }
                    GlobalData.exhibitorMyLeadTabLoad(MainActivity.this);
                } else {
                    sqLiteDatabaseHandler.deleteMyExiLeadData(sessionManager.getEventId(), sessionManager.getUserId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // set User to CrashAnlyticsLogUser
    private void crashAnlyticslogUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        if (sessionManager.getUserId() != null && sessionManager.getUserId().length() > 0)
            Crashlytics.setUserIdentifier(sessionManager.getUserId());
        if (sessionManager.getEmail() != null && sessionManager.getEmail().length() > 0) {
            Crashlytics.setUserEmail(sessionManager.getEmail());
        }
        if (sessionManager.getFirstName() != null && sessionManager.getFirstName().length() > 0)
            Crashlytics.setUserName(sessionManager.getFirstName());
    }

    public void setSnackBarForUpdateData(String moduleName) {
        try {
            Snackbar snackbar = Snackbar.make(drawer, getString(R.string.snackbarUpdateMessage), Snackbar.LENGTH_INDEFINITE);
            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                snackbar.getView().setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                snackbar.setActionTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            } else {
                snackbar.getView().setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                snackbar.setActionTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            }
            snackbar.setAction(getString(R.string.reloadMessage), new MyUpdateListener(snackbar, moduleName));
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getUpdatedDataFromParticularmodule(String str_module) {
        if (GlobalData.isNetworkAvailable(MainActivity.this)) {
            new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.getUpdatedDateByModule, Param.getUpdatedDateByModule(sessionManager.getEventId(), str_module), 11, false, this);
        }
    }

    public void getExhibitorUpdateData() {

        if (GlobalData.isNetworkAvailable(MainActivity.this)) {
            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.get_ExhibitorDataUid,
                        Param.getExhibitorList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), "", 1, "", sessionManager.getExhibitorParentCategoryId(), sessionManager.getIsLastCategoryName()),
                        12, false, this);
            } else {
                new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.get_ExhibitorData,
                        Param.getExhibitorList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), "", 1, "", sessionManager.getExhibitorParentCategoryId(), sessionManager.getIsLastCategoryName()),
                        12, false, this);
            }
        }
    }

    private void getSpeakerListData() {
        if (GlobalData.isNetworkAvailable(MainActivity.this)) {
            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.get_SpeakerList_offlineUid, Param.getSpeakerList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 13, false, this);
            } else {
                new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.get_SpeakerList_offline, Param.getSpeakerList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 13, false, this);
            }
        }
    }

    private void getMapListData() {
        if (GlobalData.isNetworkAvailable(MainActivity.this)) {
            new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.get_MapList, Param.getMapList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 14, false, this);
        }
    }

    private void getGroupModuleData() {
        if (GlobalData.isNetworkAvailable(getApplicationContext())) {
            if (GlobalData.isNetworkAvailable(MainActivity.this)) {
                new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.getGroupModuleData,
                        Param.getGroupingData(sessionManager.getEventId()),
                        15, false, this);
            }
        }
    }

    private void getSponsorListData() {
        if (GlobalData.isNetworkAvailable(MainActivity.this)) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.get_SponsorList_offlineUid, Param.getSponsorOfflineList(sessionManager.getEventId()), 16, false, this);
            else
                new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.get_SponsorList_offline, Param.getSponsorOfflineList(sessionManager.getEventId()), 16, false, this);

        }
    }

    private void getCmsListData() {
        if (GlobalData.isNetworkAvailable(MainActivity.this)) {
            new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.getCMSofflineData, Param.getMapList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 18, false, this);
        }
    }

    private void getAgendaListDataInAction() {
        if (GlobalData.isNetworkAvailable(MainActivity.this)) {
            new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.get_AgendaList_offline, Param.getSponsorOfflineList(sessionManager.getEventId()), 17, false, this);
        }
    }

    private void loadExhibitorData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("data")) {
                if (sqLiteDatabaseHandler.isUpdateDataExist(sessionManager.getEventId(), GlobalData.exhibitorModuleNo)) {
                    sqLiteDatabaseHandler.deleteUpdateModuleData(sessionManager.getEventId(), GlobalData.exhibitorModuleNo);
                    sqLiteDatabaseHandler.insertUpdateModuleData(GlobalData.exhibitorModuleNo, "exhibitor", updateDate, sessionManager.getEventId());
                } else {
                    sqLiteDatabaseHandler.insertUpdateModuleData(GlobalData.exhibitorModuleNo, "exhibitor", updateDate, sessionManager.getEventId());
                }

                Gson gson = new Gson();
                ExhibitorOfflineData offlineData = gson.fromJson(jsonObject.get("data").toString(), ExhibitorOfflineData.class);
                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                if (sqLiteDatabaseHandler.isExhibitorDataExist(sessionManager.getEventId())) {

                    sqLiteDatabaseHandler.deleteExhibitorListData(sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertUpdateAllParentCategory(offlineData.getPcategories(), sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertUpdateAllSubCategory(offlineData.getCategories(), sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertUpdateExhibitorListdata(offlineData.getExhibitor_list(), sessionManager.getEventId(), sessionManager.getExhibitorParentCategoryId(), sessionManager.getUserId(), sessionManager.getEventType());
                    sqLiteDatabaseHandler.insertExhibitorParentGroupData(offlineData.getExhibitorParentCatGroups(), sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertUpdateAllCountries(offlineData.getCountries(), sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertExhibitorDetailFromSplash(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObjectData);
                } else {
                    sqLiteDatabaseHandler.insertUpdateAllParentCategory(offlineData.getPcategories(), sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertUpdateAllSubCategory(offlineData.getCategories(), sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertUpdateExhibitorListdata(offlineData.getExhibitor_list(), sessionManager.getEventId(), sessionManager.getExhibitorParentCategoryId(), sessionManager.getUserId(), sessionManager.getEventType());
                    sqLiteDatabaseHandler.insertUpdateAllCountries(offlineData.getCountries(), sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertExhibitorParentGroupData(offlineData.getExhibitorParentCatGroups(), sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertExhibitorDetailFromSplash(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObjectData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSpeakerData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("data")) {

                if (sqLiteDatabaseHandler.isUpdateDataExist(sessionManager.getEventId(), GlobalData.speakerModuleNo)) {
                    sqLiteDatabaseHandler.deleteUpdateModuleData(sessionManager.getEventId(), GlobalData.speakerModuleNo);
                    sqLiteDatabaseHandler.insertUpdateModuleData(GlobalData.speakerModuleNo, "SpeakerData", updateDate, sessionManager.getEventId());
                } else {
                    sqLiteDatabaseHandler.insertUpdateModuleData(GlobalData.speakerModuleNo, "SpeakerData", updateDate, sessionManager.getEventId());
                }

                Gson gson = new Gson();
                SpeakerListMainClass speakerListClass = gson.fromJson(jsonObject.get("data").toString(), SpeakerListMainClass.class);
                if (sqLiteDatabaseHandler.isSpeakerListExist(sessionManager.getEventId())) {
                    sqLiteDatabaseHandler.deleteSpeakerListExistData(sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertUpdateSpeakerdata(speakerListClass.getSponsorListNewDataArrayList(), sessionManager.getEventId(), sessionManager.getUserId());
                } else {
                    sqLiteDatabaseHandler.insertUpdateSpeakerdata(speakerListClass.getSponsorListNewDataArrayList(), sessionManager.getEventId(), sessionManager.getUserId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMapListData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("data")) {

                if (sqLiteDatabaseHandler.isUpdateDataExist(sessionManager.getEventId(), GlobalData.mapModuleNo)) {
                    sqLiteDatabaseHandler.deleteUpdateModuleData(sessionManager.getEventId(), GlobalData.mapModuleNo);
                    sqLiteDatabaseHandler.insertUpdateModuleData(GlobalData.mapModuleNo, "map", updateDate, sessionManager.getEventId());
                } else {
                    sqLiteDatabaseHandler.insertUpdateModuleData(GlobalData.mapModuleNo, "map", updateDate, sessionManager.getEventId());
                }

                Gson gson = new Gson();
                MapListData offlineData = gson.fromJson(jsonObject.get("data").toString(), MapListData.class);

                //addToDatabase
                if (sqLiteDatabaseHandler.isMapListExist(sessionManager.getEventId())) {
                    sqLiteDatabaseHandler.deleteMapListExistData(sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertUpdateAllMapListData(offlineData.getMapNewDataArrayList(), sessionManager.getEventId());
                } else {
                    sqLiteDatabaseHandler.insertUpdateAllMapListData(offlineData.getMapNewDataArrayList(), sessionManager.getEventId());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGroupData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("data")) {

                if (sqLiteDatabaseHandler.isUpdateDataExist(sessionManager.getEventId(), GlobalData.groupModuleNo)) {
                    sqLiteDatabaseHandler.deleteUpdateModuleData(sessionManager.getEventId(), GlobalData.groupModuleNo);
                    sqLiteDatabaseHandler.insertUpdateModuleData(GlobalData.groupModuleNo, "group", groupUpdateDate, sessionManager.getEventId());
                } else {
                    sqLiteDatabaseHandler.insertUpdateModuleData(GlobalData.groupModuleNo, "group", groupUpdateDate, sessionManager.getEventId());
                }

                Gson gson = new Gson();
                GrouppingOfflineList offlineData = gson.fromJson(jsonObject.get("data").toString(), GrouppingOfflineList.class);

                // Simple All GroupDataInsert
                if (sqLiteDatabaseHandler.isGroupDataExist(sessionManager.getEventId())) {
                    sqLiteDatabaseHandler.deleteGroupExistData(sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertUpdateAllGroupModuleData(offlineData.getGroupModuleData());
                } else {
                    sqLiteDatabaseHandler.insertUpdateAllGroupModuleData(offlineData.getGroupModuleData());
                }

                // SuperGroupDataInsert
                if (sqLiteDatabaseHandler.isSuperGroupDataExist(sessionManager.getEventId())) {
                    sqLiteDatabaseHandler.deleteSuperGroupExistData(sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertSuperGroupModuleData(offlineData.getSuperGroupDataArrayList());
                } else {
                    sqLiteDatabaseHandler.insertSuperGroupModuleData(offlineData.getSuperGroupDataArrayList());
                }

                // insert SuperGroupRelationData

                if (sqLiteDatabaseHandler.isSuperGroupRelationDataExist(sessionManager.getEventId())) {
                    sqLiteDatabaseHandler.deleteSuperRelationGroupExistData(sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertSuperGroupRelationModuleData(offlineData.getSuperGroupRelationDataArrayList(), sessionManager.getEventId());
                } else {
                    sqLiteDatabaseHandler.insertSuperGroupRelationModuleData(offlineData.getSuperGroupRelationDataArrayList(), sessionManager.getEventId());
                }

                // GroupRelationDataInsert
                if (sqLiteDatabaseHandler.isGroupRelationDataExist(sessionManager.getEventId())) {
                    sqLiteDatabaseHandler.deleteGroupRelationExistData(sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertUpdateAllGroupModuleRelationData(offlineData.getGroupRelationModuleDataArrayList(), sessionManager.getEventId());
                } else {
                    sqLiteDatabaseHandler.insertUpdateAllGroupModuleRelationData(offlineData.getGroupRelationModuleDataArrayList(), sessionManager.getEventId());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSponsorData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("data")) {
                if (sqLiteDatabaseHandler.isUpdateDataExist(sessionManager.getEventId(), GlobalData.sponsorModuleNo)) {
                    sqLiteDatabaseHandler.deleteUpdateModuleData(sessionManager.getEventId(), GlobalData.sponsorModuleNo);
                    sqLiteDatabaseHandler.insertUpdateModuleData(GlobalData.sponsorModuleNo, "sponsor", updateDate, sessionManager.getEventId());
                } else {
                    sqLiteDatabaseHandler.insertUpdateModuleData(GlobalData.sponsorModuleNo, "sponsor", updateDate, sessionManager.getEventId());
                }

                Gson gson = new Gson();
                SponsorMainListClasss offlineSponsorData = gson.fromJson(jsonObject.get("data").toString(), SponsorMainListClasss.class);
                if (sqLiteDatabaseHandler.isSponsorDataExist(sessionManager.getEventId())) {
                    sqLiteDatabaseHandler.deleteSponsorListData(sessionManager.getEventId());
                    sqLiteDatabaseHandler.deleteSponsorTypeData(sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertUpdateSponsordata(offlineSponsorData.getSponsorListNewDataArrayList(), sessionManager.getEventId(), sessionManager.getUserId());
                } else {
                    sqLiteDatabaseHandler.insertUpdateSponsordata(offlineSponsorData.getSponsorListNewDataArrayList(), sessionManager.getEventId(), sessionManager.getUserId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCMSData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("data")) {

                if (sqLiteDatabaseHandler.isUpdateDataExist(sessionManager.getEventId(), GlobalData.cmsModuleNo)) {
                    sqLiteDatabaseHandler.deleteUpdateModuleData(sessionManager.getEventId(), GlobalData.cmsModuleNo);
                    sqLiteDatabaseHandler.insertUpdateModuleData(GlobalData.cmsModuleNo, "CMSDATA", updateDate, sessionManager.getEventId());
                } else {
                    sqLiteDatabaseHandler.insertUpdateModuleData(GlobalData.cmsModuleNo, "CMSDATA", updateDate, sessionManager.getEventId());
                }

                Gson gson = new Gson();
                CmsListandDetailList cmsListandDetailList = gson.fromJson(jsonObject.get("data").toString(), CmsListandDetailList.class);
                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                if (sqLiteDatabaseHandler.isCmsPageExistFromSplah(sessionManager.getEventId())) {
                    sqLiteDatabaseHandler.deleteCmsPageDataFromSplash(sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertCmsPageFromSplash(sessionManager.getEventId(), jsonObjectData);
                } else {
                    sqLiteDatabaseHandler.insertCmsPageFromSplash(sessionManager.getEventId(), jsonObjectData);
                }

                if (sqLiteDatabaseHandler.isCMSLISTDataExist(sessionManager.getEventId())) {
                    sqLiteDatabaseHandler.deleteCMSLISTData(sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertCMSLISTData(cmsListandDetailList.getCmsListDataArrayList(), sessionManager.getEventId());
                } else {
                    sqLiteDatabaseHandler.insertCMSLISTData(cmsListandDetailList.getCmsListDataArrayList(), sessionManager.getEventId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAgendaData(JSONObject jsonObject, String isUpdateDate) {
        try {
            if (jsonObject.has("data")) {

                if (isUpdateDate.equalsIgnoreCase("1")) {
                    if (sqLiteDatabaseHandler.isUpdateDataExist(sessionManager.getEventId(), GlobalData.agendaModuleNo)) {
                        sqLiteDatabaseHandler.deleteUpdateModuleData(sessionManager.getEventId(), GlobalData.agendaModuleNo);
                        sqLiteDatabaseHandler.insertUpdateModuleData(GlobalData.agendaModuleNo, "Agenda", updateDate, sessionManager.getEventId());
                    } else {
                        sqLiteDatabaseHandler.insertUpdateModuleData(GlobalData.agendaModuleNo, "Agenda", updateDate, sessionManager.getEventId());
                    }
                }
                Gson gson = new Gson();
                AgendaData agendaData = gson.fromJson(jsonObject.get("data").toString(), AgendaData.class);
                if (sqLiteDatabaseHandler.isAgendaDataExist(sessionManager.getEventId())) {
                    sqLiteDatabaseHandler.deleteAgendaCatData(sessionManager.getEventId());
                    sqLiteDatabaseHandler.deleteAgendaCatRelationData(sessionManager.getEventId());
                    sqLiteDatabaseHandler.deleteAgendaListData(sessionManager.getEventId());
                    sqLiteDatabaseHandler.deleteAgendaTypeData(sessionManager.getEventId());
                    sqLiteDatabaseHandler.insertUpdateAgendadata(agendaData, sessionManager.getEventId());
                } else {
                    sqLiteDatabaseHandler.insertUpdateAgendadata(agendaData, sessionManager.getEventId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getResponseOfUpdateDateModule(JSONObject jsonObject) {
        try {
            if (jsonObject.has("date") && jsonObject.has("module")) {
                updateDate = jsonObject.getString("date");
                groupUpdateDate = jsonObject.getString("group");
                String date = jsonObject.getString("date");
                String module = jsonObject.getString("module");
                switchCaseForUpdateModuleData(module, date, groupUpdateDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchCaseForUpdateModuleData(String module, String date, String groupUpdateDate) {
        switch (module) {
            case GlobalData.exhibitorModuleid:
                Cursor exhibitorcursor = sqLiteDatabaseHandler.getUpdateModuleData(sessionManager.getEventId(), GlobalData.exhibitorModuleNo);
                if (exhibitorcursor.getCount() > 0) {
                    String previoisDate = "";
                    if (exhibitorcursor.moveToFirst()) {
                        previoisDate = exhibitorcursor.getString(exhibitorcursor.getColumnIndex(sqLiteDatabaseHandler.Update_DATE));
                    }
                    if (!(previoisDate.equalsIgnoreCase(date))) {
                        setSnackBarForUpdateData(module);
                    }
                }
                break;
            case GlobalData.speakerModuleid:
                Cursor speakerCursor = sqLiteDatabaseHandler.getUpdateModuleData(sessionManager.getEventId(), GlobalData.speakerModuleNo);
                if (speakerCursor.getCount() > 0) {
                    String previoisDate = "";
                    if (speakerCursor.moveToFirst()) {
                        previoisDate = speakerCursor.getString(speakerCursor.getColumnIndex(sqLiteDatabaseHandler.Update_DATE));
                    }
                    if (!(previoisDate.equalsIgnoreCase(date))) {
                        setSnackBarForUpdateData(module);
                    }
                }
                break;
            case GlobalData.mapModuleid:
                Cursor mapCuror = sqLiteDatabaseHandler.getUpdateModuleData(sessionManager.getEventId(), GlobalData.mapModuleNo);
                if (mapCuror.getCount() > 0) {
                    String previoisDate = "";
                    if (mapCuror.moveToFirst()) {
                        previoisDate = mapCuror.getString(mapCuror.getColumnIndex(sqLiteDatabaseHandler.Update_DATE));
                    }
                    if (!(previoisDate.equalsIgnoreCase(date))) {
                        setSnackBarForUpdateData(module);
                    } else {
                        checkGroupDataUpdate("1");
                    }
                }
                break;
            case GlobalData.sponsorModuleid:
                Cursor sponsorCuror = sqLiteDatabaseHandler.getUpdateModuleData(sessionManager.getEventId(), GlobalData.sponsorModuleNo);
                if (sponsorCuror.getCount() > 0) {
                    String previoisDate = "";
                    if (sponsorCuror.moveToFirst()) {
                        previoisDate = sponsorCuror.getString(sponsorCuror.getColumnIndex(sqLiteDatabaseHandler.Update_DATE));
                    }
                    if (!(previoisDate.equalsIgnoreCase(date))) {
                        setSnackBarForUpdateData(module);
                    } else {
                        checkGroupDataUpdate("1");
                    }
                }
                break;
            case GlobalData.agendaModuleid:
                Cursor agendaCuror = sqLiteDatabaseHandler.getUpdateModuleData(sessionManager.getEventId(), GlobalData.agendaModuleNo);
                if (agendaCuror.getCount() > 0) {
                    String previoisDate = "";
                    if (agendaCuror.moveToFirst()) {
                        previoisDate = agendaCuror.getString(agendaCuror.getColumnIndex(sqLiteDatabaseHandler.Update_DATE));
                    }
                    if (!(previoisDate.equalsIgnoreCase(date))) {
                        setSnackBarForUpdateData(module);
                    } else {
                        checkGroupDataUpdate("1");
                    }
                }
                break;
            case GlobalData.cmsModuleid:
                Cursor cmsCuror = sqLiteDatabaseHandler.getUpdateModuleData(sessionManager.getEventId(), GlobalData.cmsModuleNo);
                if (cmsCuror.getCount() > 0) {
                    String previoisDate = "";
                    if (cmsCuror.moveToFirst()) {
                        previoisDate = cmsCuror.getString(cmsCuror.getColumnIndex(sqLiteDatabaseHandler.Update_DATE));
                    }
                    if (!(previoisDate.equalsIgnoreCase(date))) {
                        setSnackBarForUpdateData(module);
                    } else {
                        checkGroupDataUpdate("1");
                    }
                }
                break;
            case GlobalData.groupModuleid:
                Cursor groupCursor = sqLiteDatabaseHandler.getUpdateModuleData(sessionManager.getEventId(), GlobalData.groupModuleNo);
                if (groupCursor.getCount() > 0) {
                    String previoisDate = "";
                    if (groupCursor.moveToFirst()) {
                        previoisDate = groupCursor.getString(groupCursor.getColumnIndex(sqLiteDatabaseHandler.Update_DATE));
                    }
                    if (!(previoisDate.equalsIgnoreCase(date))) {
                        setSnackBarForUpdateData(module);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void switchCaseForofflineAPICall(String moduleName) {
        switch (moduleName) {
            case GlobalData.exhibitorModuleid:
                getExhibitorUpdateData();
                break;
            case GlobalData.speakerModuleid:
                getSpeakerListData();
                break;
            case GlobalData.mapModuleid:
                checkGroupDataUpdate("0");
                getMapListData();
                break;
            case GlobalData.sponsorModuleid:
                checkGroupDataUpdate("0");
                getSponsorListData();
                break;
            case GlobalData.agendaModuleid:
                checkGroupDataUpdate("0");
                getAgendaListDataInAction();
                break;
            case GlobalData.cmsModuleid:
                checkGroupDataUpdate("0");
                getCmsListData();
                break;
            case GlobalData.groupModuleid:
                getGroupModuleData();
                break;
            default:
                break;
        }

    }

    private void checkGroupDataUpdate(String isShowPopup) {
        Cursor groupDataCursor = sqLiteDatabaseHandler.getUpdateModuleData(sessionManager.getEventId(), GlobalData.groupModuleNo);
        if (groupDataCursor.getCount() > 0) {
            String previoisDate = "";
            if (groupDataCursor.moveToFirst()) {
                previoisDate = groupDataCursor.getString(groupDataCursor.getColumnIndex(sqLiteDatabaseHandler.Update_DATE));
            }
            if (!(previoisDate.equalsIgnoreCase(groupUpdateDate))) {
                if (isShowPopup.equalsIgnoreCase("0")) {
                    getGroupModuleData();
                } else {
                    setSnackBarForUpdateData(GlobalData.groupModuleid);
                }
            }
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {

        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject mainJson = new JSONObject(volleyResponse.output);
                    if (mainJson.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        JSONObject jsonObject = mainJson.getJSONObject("data");

                        notes_status = jsonObject.getString("note_status");
                        loadMenuData(jsonObject);
                        if (sessionManager.isLogin())
                            getGeoLocationData();
//                        if (sessionManager.isLogin())
//                            Notitimer.schedule(new Refresh(), 0, 40000);
                        if (sqLiteDatabaseHandler.isEventDataExist(sessionManager.getEventId()))  // Use To store EventHomeData
                        {
                            sqLiteDatabaseHandler.UpdateEventHomeData(sessionManager.getEventId(), jsonObject.toString());
                        } else {
                            sqLiteDatabaseHandler.insertEventHomeData(sessionManager.getEventId(), jsonObject.toString());
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 1:
                try {
                    JSONObject mainJson = new JSONObject(volleyResponse.output);
                    if (mainJson.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        JSONObject jsonObject = mainJson.getJSONObject("data");

                        notes_status = jsonObject.getString("note_status");
//                            setAppcolor();
                        loadMenuData(jsonObject);
                        if (sqLiteDatabaseHandler.isEventDataExist(sessionManager.getEventId()))  // Use To store EventHomeData
                        {
                            sqLiteDatabaseHandler.UpdateEventHomeData(sessionManager.getEventId(), jsonObject.toString());
                        } else {
                            sqLiteDatabaseHandler.insertEventHomeData(sessionManager.getEventId(), jsonObject.toString());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jObjectNoti = new JSONObject(volleyResponse.output);
                    if (jObjectNoti.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jData = jObjectNoti.getJSONObject("data");
                        noticount = String.valueOf(jData.getInt("count"));
                        txt_noti_badge.setText(noticount);
                        notificationDataArrayList = new ArrayList<>();

                        JSONArray jArrayNotidata = jData.getJSONArray("notify_data");
                        for (int i = 0; i < jArrayNotidata.length(); i++) {
                            JSONObject index = jArrayNotidata.getJSONObject(i);
                            notificationDataArrayList.add(new NotificationData(index.getString("id")
                                    , index.getString("Message")
                                    , index.getString("Firstname")
                                    , index.getString("Lastname")
                                    , index.getString("Ispublic")
                                    , index.getString("Issent")
                                    , index.getString("Logo")
                                    , index.getString("Sender_id")));

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        getNotificationCount();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONArray jsonSurveyArray = jsonObject.getJSONArray("survey");
                        JSONArray jsonCustomeColumnArray = jsonObject.getJSONArray("custom_column");

                        if (jsonSurveyArray.length() != 0) {
                            sessionManager.setofflineSurveyDataExhiLead(jsonObject.toString());
                        } else {
                            sessionManager.setofflineSurveyDataExhiLead("");
                        }


                        if (jsonCustomeColumnArray.length() != 0) {
                            sessionManager.setofflineCustomeColumnExhiLead(jsonCustomeColumnArray.toString());
                        } else {
                            sessionManager.setofflineCustomeColumnExhiLead("");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject object = jsonObject.getJSONObject("default_lang");
//                        sessionManager.setMultiLangString(jsonObject.toString());
                        sessionManager.setMultiLangString(object.toString());

                        DefaultLanguage.DefaultLang defaultLang = sessionManager.getMultiLangString();
                        login.setText(defaultLang.getMyProfileLogin());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject object = jsonObject.getJSONObject("default_lang");
//                        sessionManager.setMultiLangString(jsonObject.toString());
                        sessionManager.setMultiLangString(object.toString());

                        DefaultLanguage.DefaultLang defaultLang = sessionManager.getMultiLangString();
                        login.setText(defaultLang.getMyProfileLogin());
                        getList();
                        getAgendaListData();

                        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                            mSelectedItem = 2;
                            loadFragment();
                        } else {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                            mSelectedItem = 2;
                            loadFragment();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 9:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("AITL AgendaList", jsonObject.toString());

                        loadAgendaData(jsonObject, "0");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("Bhavdip MyExiLeadList", jsonObject.toString());

                        new updateMyLeadExhibitorOfflineData(jsonObject).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 11:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("AITL UPDATEDATE", jsonObject.toString());
                        getResponseOfUpdateDateModule(jsonObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 12:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {

                        new updateDatabase(jsonObject, GlobalData.exhibitorModuleid).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 13:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        new updateDatabase(jsonObject, GlobalData.speakerModuleid).execute();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 14:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("Bahvdip MapListFragment", jsonObject.toString());
                        new updateDatabase(jsonObject, GlobalData.mapModuleid).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 15:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("Bahvdip MapListFragment", jsonObject.toString());
                        new updateDatabase(jsonObject, GlobalData.groupModuleid).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 16:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("Bahvdip SponsorList", jsonObject.toString());
                        new updateDatabase(jsonObject, GlobalData.sponsorModuleid).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 17:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("Bahvdip SponsorList", jsonObject.toString());
                        new updateDatabase(jsonObject, GlobalData.agendaModuleid).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 18:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("Bahvdip SponsorList", jsonObject.toString());
                        new updateDatabase(jsonObject, GlobalData.cmsModuleid).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 19:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {

                        Gson gson = new Gson();
                        GeoLocationData datalist = gson.fromJson(jsonObject.getJSONObject("data").toString(), GeoLocationData.class);
//                        sqLiteDatabaseHandler.deleteGeoLocationData(sessionManager.getEventId());
//                        sqLiteDatabaseHandler.insertGeoLocationData(datalist.getData());
                        locationLists = new ArrayList<>();
                        locationLists.addAll(datalist.getData());
                        if (locationLists != null && locationLists.size() > 0)
                            checkForGPS();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 20:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {

                        try {
                            locationLists.get(position).setStatus(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 21:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("NotificationUpdateLog", jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void attendeeRedirection(boolean isFromNotification, boolean isFromLefhandMenu) {
        if (isFromNotification) {

            if (sessionManager.getIsAttendeecategoryShow().equalsIgnoreCase("1")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {
                    GlobalData.CURRENT_FRAG = GlobalData.AttendeeMainGroupFragment;

                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.AttendeeMainGroupFragment;
                }
            } else {
                if (GlobalData.Fragment_Stack.size() >= 1) {
                    sessionManager.setAttendeeMainCategoryData("");
                    GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragment;
                } else {
                    sessionManager.setAttendeeMainCategoryData("");
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragment;
                }
            }
            if (!isFromLefhandMenu)
                loadFragment();
        } else {
            if (sessionManager.getIsAttendeecategoryShow().equalsIgnoreCase("1")) {
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.AttendeeMainGroupFragment;
            } else {
                sessionManager.setAttendeeMainCategoryData("");
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragment;
            }
            if (!isFromLefhandMenu)
                loadFragment();
        }
    }

    public void updateProfilePic() {
        if (sessionManager.isLogin()) {
            userName.setText(sessionManager.getFirstName());
            if (!(sessionManager.getImagePath().equalsIgnoreCase(""))) {
                Glide.with(MainActivity.this)
                        .load(MyUrls.thumImgUrl + sessionManager.getImagePath())
                        .fitCenter()
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                user_profile_image.setVisibility(View.VISIBLE);
                                Glide.with(MainActivity.this).load("").centerCrop().fitCenter().placeholder(R.drawable.profile).into(user_profile_image);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                user_profile_image.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(user_profile_image);
            } else {
                user_profile_image.setVisibility(View.VISIBLE);
                Glide.with(MainActivity.this).load("").placeholder(R.drawable.profile).into(user_profile_image);
            }
        }
    }

    public void updateCartCount(String cartCount) {
        if (sessionManager.isLogin()) {
            img_cart.setVisibility(View.VISIBLE);
            txt_cart_badge.setVisibility(View.VISIBLE);
            badge_layout.setVisibility(View.VISIBLE);
            frme_cart.setVisibility(View.VISIBLE);
            txt_cart_badge.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            txt_cart_badge.setText(cartCount);

        } else {
            img_cart.setVisibility(View.GONE);
            frme_cart.setVisibility(View.GONE);
            badge_layout.setVisibility(View.GONE);
        }
        invalidateOptionsMenu();
    }

    public void updateOnlieShopDetailCartCount(String product_preview, String str_cart_count) {
        if (sessionManager.isLogin()) {

            if (product_preview.equalsIgnoreCase("1")) {
                img_cart.setVisibility(View.GONE);
                txt_cart_badge.setVisibility(View.GONE);
                badge_layout.setVisibility(View.GONE);
                frme_cart.setVisibility(View.GONE);
            } else if (product_preview.equalsIgnoreCase("0")) {
                img_cart.setVisibility(View.VISIBLE);
                txt_cart_badge.setVisibility(View.VISIBLE);
                badge_layout.setVisibility(View.VISIBLE);
                frme_cart.setVisibility(View.VISIBLE);
            }

            txt_cart_badge.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            txt_cart_badge.setText(str_cart_count);

        } else {
            img_cart.setVisibility(View.GONE);
            badge_layout.setVisibility(View.GONE);
        }
        invalidateOptionsMenu();
    }

    public void updateCartCountFromHome() {
        if (sessionManager.isLogin()) {

            frme_cart.setVisibility(View.GONE);
            txt_cart_badge.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            txt_cart_badge.setText("0");

        } else {
            frme_cart.setVisibility(View.GONE);
        }
    }

    public void updateCartCountFromFundHome(String cart_status) {
        if (sessionManager.isLogin()) {
            frme_cart.setVisibility(View.VISIBLE);
            badge_layout.setVisibility(View.VISIBLE);
            txt_cart_badge.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            txt_cart_badge.setText(cart_status);

        } else {
            frme_cart.setVisibility(View.GONE);
            badge_layout.setVisibility(View.GONE);
        }
        invalidateOptionsMenu();
    }

    public void sendGeoLocationBasedNotification(String notiId, int position) {
        try {
            this.position = position;
            if (GlobalData.isNetworkAvailable(MainActivity.this)) {
                new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.updateUserNoti, Param.updateUserNotification(sessionManager.getEventId(), sessionManager.getUserId(), notiId, sessionManager.getGcm_id(), "Android"), 20, false, this);
            } else {
                locationLists.get(position).setStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLocationService() {


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener(MainActivity.this, locationLists, sessionManager);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, listener);
    }

    private void checkForGPS() {
        if (isGPSEnabled()) {
            startLocationService();
//            startService(new Intent(this, MyLocationService.class));
        } else {
            sessionManager.alertDailogForGeoLocation(MainActivity.this, new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 2000);
                }
            });

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusGeoLocationData(EventBusGeoLocationData data) {
        getGeoLocationData();
    }

    public boolean isGPSEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (lm != null) {
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } else {
            return false;
        }
    }

    private void getGeoLocationData() {
        if (sessionManager.isLogin()) {
            if (GlobalData.isNetworkAvailable(MainActivity.this)) {
                new VolleyRequest(MainActivity.this, VolleyRequest.Method.POST, MyUrls.getGeoLocationData, Param.getGeolocationData(sessionManager.getEventId(), sessionManager.getUserId()), 19, false, this);
            }
        }

    }

    class Refresh extends TimerTask {
        public void run() {

            try {
                getNotificationCount();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Update ExhibitorLead AsynTask Offline at FirstTime
    public class updateMyLeadExhibitorOfflineData extends AsyncTask<Void, Void, Boolean> {
        JSONObject jsonObject;

        public updateMyLeadExhibitorOfflineData(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (jsonObject != null)
                loadMyExiLeadOfflineData(jsonObject);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    public class MyUpdateListener implements View.OnClickListener {

        Snackbar snackbar;
        String moduleName;

        public MyUpdateListener(Snackbar snackbar, String moduleName) {
            this.snackbar = snackbar;
            this.moduleName = moduleName;
        }

        @Override
        public void onClick(View v) {
            try {
                if (GlobalData.isNetworkAvailable(MainActivity.this)) {

                    if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                        GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                        loadFragment();
                    } else {
                        GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                        loadFragment();
                    }
                    if (!isFinishing()) {
                        mProgressDialog = new ProgressDialog(MainActivity.this);
                        mProgressDialog.setMessage(getString(R.string.updateMessage));
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setCanceledOnTouchOutside(false);
                        mProgressDialog.show();
                    }
                    switchCaseForofflineAPICall(moduleName);
                } else {
                    ToastC.show(MainActivity.this, getString(R.string.noInernet));
                }
                snackbar.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class updateDatabase extends AsyncTask<Void, Void, Boolean> {

        JSONObject jsonObject;
        String module;

        public updateDatabase(JSONObject jsonObject, String module) {
            this.jsonObject = jsonObject;
            this.module = module;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (jsonObject != null) {
                switch (module) {
                    case GlobalData.exhibitorModuleid:
                        loadExhibitorData(jsonObject);
                        break;
                    case GlobalData.speakerModuleid:
                        loadSpeakerData(jsonObject);
                        break;
                    case GlobalData.mapModuleid:
                        loadMapListData(jsonObject);
                        break;
                    case GlobalData.groupModuleid:
                        loadGroupData(jsonObject);
                        break;
                    case GlobalData.sponsorModuleid:
                        loadSponsorData(jsonObject);
                        break;
                    case GlobalData.cmsModuleid:
                        loadCMSData(jsonObject);
                        break;
                    case GlobalData.agendaModuleid:
                        loadAgendaData(jsonObject, "1");
                        break;
                    default:
                        break;
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (mProgressDialog != null)
                mProgressDialog.dismiss();
            getList();
            super.onPostExecute(aBoolean);
        }
    }
}