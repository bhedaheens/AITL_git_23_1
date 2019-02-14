package com.allintheloop.Fragment.FundraisingModule;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Adapter.Exhibitor.Exhibitor_ImageAdapter;
import com.allintheloop.Adapter.FundraisingHomeMenuAdapter;
import com.allintheloop.Adapter.FundraisingHome_footer_adapter;
import com.allintheloop.Adapter.Fundraising_Product_Adapter;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.ExhibitorListClass.Exhibitor_DetailImage;
import com.allintheloop.Bean.Fundraising.FundraisingHome_footer;
import com.allintheloop.Bean.Fundraising.Fundraising_HomeMenu;
import com.allintheloop.Bean.Fundraising.Fundraising_Product;
import com.allintheloop.Fragment.EditProfileModule.Attendee_ProfileEdit_Fragment;
import com.allintheloop.Fragment.InstagramModule.InstatDonation_Dialog_Fragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fundrising_Home_Fragment extends Fragment implements VolleyInterface {

    public static ViewPager viewPager, footer_pager;

    ArrayList<Exhibitor_DetailImage> ImgarrayList;
    ArrayList<Fundraising_HomeMenu> homeMenuArrayList;
    ArrayList<Fundraising_Product> fundraising_productArrayList;
    ArrayList<FundraisingHome_footer> footerArrayList;

    FundraisingHomeMenuAdapter menuAdapter;
    Exhibitor_ImageAdapter adapter;
    Fundraising_Product_Adapter product_adapter;
    FundraisingHome_footer_adapter footer_adapter;

    LinearLayout linear_footer, linear_progressview;

    Boolean isFirstTime = false;
    int page_count = 1, total_page;
    LinearLayoutManager linearLayoutManager;
    boolean isLoading, btn_load;
    FrameLayout layout;
    RecyclerView rv_homeMenu, rv_viewProduct;
    ImageView share_fb, tweet_twitter;
    int fund_amount, tar_amount, progress_amount;
    public static String notes_status, currency_status, cart_status;

    ProgressBar fund_progress_bar;

    String str_fbShare = "", str_tweeet = "", str_fund_target = "", web_contnt = "", video_link = "", instant_enbled = "", fundraising_enbled = "", instant_Backcolor = "#ffffff", fundraising_Backcolor = "#ffffff", instant_Textcolor = "#ffffff", fundrasing_TextColor = "#ffffff", instant_name = "", fundraising_name = "", str_fundring_amount = "", target_raisedsofar_display = "", bids_donations_display = "", pro_id, pro_name = "", pro_desc = "", pro_thumb = "", pro_action_type = "", pro_price = "", pro_maxBid = "", product_flag_price = "", str_firstName = "", str_lastName = "", str_logo = "", str_productName = "", str_amt = "";
    SessionManager sessionManager;
    TextView target_amount, fundring_amount;
    WebView webViewContent, webViewVideo;
    Button btn_instant, btn_fund, btn_load_more;
    Bundle bundle;
    Handler handler;

    String social_media;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Cursor cursor;
    public static String stripeSk = "", stripePk = "";

    public Fundrising_Home_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fundrising__home, container, false);

        sessionManager = new SessionManager(getActivity());

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(true);

//        if (sessionManager.getMenuid().equalsIgnoreCase("20"))
//        {
//            ((MainActivity) getActivity()).setTitle("");
//            ((MainActivity) getActivity()).setDrawerState(false);
//        }
//        else
//        {
//            ((MainActivity) getActivity()).setTitle("");
//            ((MainActivity) getActivity()).setDrawerState(true);
//        }
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        ImgarrayList = new ArrayList<>();
        homeMenuArrayList = new ArrayList<>();
        fundraising_productArrayList = new ArrayList<>();
        footerArrayList = new ArrayList<>();

        bundle = new Bundle();
        handler = new Handler();
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        footer_pager = (ViewPager) rootView.findViewById(R.id.footer_pager);
        webViewContent = (WebView) rootView.findViewById(R.id.webViewContent);
        webViewVideo = (WebView) rootView.findViewById(R.id.webViewVideo);

        linear_footer = (LinearLayout) rootView.findViewById(R.id.linear_footer);
        linear_progressview = (LinearLayout) rootView.findViewById(R.id.linear_progressview);
//        layout= (FrameLayout) rootView.findViewById(R.id.layout_fund);


        fund_progress_bar = (ProgressBar) rootView.findViewById(R.id.fund_progress_bar);

        share_fb = (ImageView) rootView.findViewById(R.id.share_fb);
        tweet_twitter = (ImageView) rootView.findViewById(R.id.tweet_twitter);
        rv_homeMenu = (RecyclerView) rootView.findViewById(R.id.rv_homeMenu);
        rv_homeMenu.setNestedScrollingEnabled(false);

        rv_viewProduct = (RecyclerView) rootView.findViewById(R.id.rv_viewProduct);
        rv_viewProduct.setNestedScrollingEnabled(false);
        webViewVideo.getSettings().setJavaScriptEnabled(true);
        webViewVideo.getSettings().setAllowContentAccess(true);
        webViewVideo.setVerticalScrollBarEnabled(true);
        webViewVideo.setHorizontalScrollBarEnabled(true);

        webViewContent.getSettings().setJavaScriptEnabled(true);
        webViewContent.getSettings().setAllowContentAccess(true);
        webViewContent.getSettings().setDefaultTextEncodingName("utf-8");
        webViewContent.setVerticalScrollBarEnabled(true);
        webViewContent.setHorizontalScrollBarEnabled(true);

        if (sessionManager.getEventType().equalsIgnoreCase("4") || sessionManager.getEventType().equalsIgnoreCase("1")) {
            if (sessionManager.getFormStatus().equalsIgnoreCase("1")) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Attendee_ProfileEdit_Fragment fragment = new Attendee_ProfileEdit_Fragment();
                fragment.show(fm, "DialogFragment");
            }
        }
        ((MainActivity) getActivity()).updateProfilePic();

        btn_instant = (Button) rootView.findViewById(R.id.btn_instant);
        btn_fund = (Button) rootView.findViewById(R.id.btn_fund);
        btn_load_more = (Button) rootView.findViewById(R.id.btn_load_more);

        target_amount = (TextView) rootView.findViewById(R.id.target_amount);
        fundring_amount = (TextView) rootView.findViewById(R.id.fundring_amount);


        btn_instant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                InstatDonation_Dialog_Fragment fragment = new InstatDonation_Dialog_Fragment();
                fragment.show(fm, "DialogFragment");

            }
        });


        btn_fund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.fund_donation_fragment;
                ((MainActivity) getActivity()).loadFragment();
            }
        });

        if (getActivity().getIntent().hasExtra("message_type")) {
            Log.d("AITL NOTIFICATION", getActivity().getIntent().getStringExtra("message_type"));
            //transaction = getSupportFragmentManager().beginTransaction();

            if (getActivity().getIntent().getStringExtra("message_type").equalsIgnoreCase("Private")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {
                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                    ((MainActivity) getActivity()).loadFragment();
                }
            } else if (getActivity().getIntent().getStringExtra("message_type").equalsIgnoreCase("Won Auction")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {
                    GlobalData.CURRENT_FRAG = GlobalData.CheckOut_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.CheckOut_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                }
            } else if (getActivity().getIntent().getStringExtra("message_type").equalsIgnoreCase("Outbid Auction")) {

                sessionManager.slilentAuctionP_id = getActivity().getIntent().getStringExtra("product_id");
                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.SilentAuctionDetail_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.SilentAuctionDetail_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                }
            } else if (getActivity().getIntent().getStringExtra("message_type").equalsIgnoreCase("Attendee")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragmentTag;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragmentTag;
                    ((MainActivity) getActivity()).loadFragment();
                }
            } else if (getActivity().getIntent().getStringExtra("message_type").equalsIgnoreCase("RequestMeeting")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                    ((MainActivity) getActivity()).loadFragment();
                }
            } else if (getActivity().getIntent().getStringExtra("message_type").equalsIgnoreCase("RespondRequest")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                    ((MainActivity) getActivity()).loadFragment();
                }
            } else if (getActivity().getIntent().getStringExtra("message_type").equalsIgnoreCase("SuggestRequestTime")) {

                sessionManager.mettingId = getActivity().getIntent().getStringExtra("product_id");

                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.viewSuggestedTimeList;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.viewSuggestedTimeList;
                    ((MainActivity) getActivity()).loadFragment();
                }
            } else if (getActivity().getIntent().getStringExtra("message_type").equalsIgnoreCase("AttendeeRequestMeeting")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                    ((MainActivity) getActivity()).loadFragment();
                }
            } else if (getActivity().getIntent().getStringExtra("message_type").equalsIgnoreCase("AttendeeRespondRequest")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                    ((MainActivity) getActivity()).loadFragment();
                }
            } else if (getActivity().getIntent().getStringExtra("message_type").equalsIgnoreCase("AttendeeSuggestRequestTime")) {

                sessionManager.mettingId = getActivity().getIntent().getStringExtra("product_id");

                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.viewSuggestedTimeList;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.viewSuggestedTimeList;
                    ((MainActivity) getActivity()).loadFragment();
                }
            } else if (getActivity().getIntent().getStringExtra("message_type").equalsIgnoreCase("ModeratorRequestMeeting")) {
                if (GlobalData.Fragment_Stack.size() >= 1) {

                    GlobalData.CURRENT_FRAG = GlobalData.moderatorRequestMettingFragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.moderatorRequestMettingFragment;
                    ((MainActivity) getActivity()).loadFragment();
                }
            } else if (getActivity().getIntent().getStringExtra("message_type").equalsIgnoreCase("cms")) {

                sessionManager.mettingId = getActivity().getIntent().getStringExtra("product_id");
                if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("1")) {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.AGENDA_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.AGENDA_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("2")) {

                    ((MainActivity) getActivity()).attendeeRedirection(true, false);

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("3"))  // Exibitors
                {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.EXHIBITOR_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.EXHIBITOR_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("20"))  // Fundraising
                {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("12")) // private Message
                {

                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("6")) // Notes
                {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.View_Note_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.View_Note_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("7")) {

                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.SPEAKER_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.SPEAKER_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("8"))  // Exibitors
                {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.NOTES_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.NOTES_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("9"))  // Exibitors
                {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.Presantation_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Presantation_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("10"))  // Map
                {

                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.Map_fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Map_fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("11"))  // Exibitors
                {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.Photo_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Photo_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("13"))  // Exibitors
                {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("15"))  // Exibitors
                {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.Survey_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Survey_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("16"))  // Exibitors
                {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.Document_Fragment;

                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Document_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("17"))  // Exibitors
                {

                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.SOCIAL_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.SOCIAL_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("22"))  // Exibitors
                {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.Silent_Auction;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Silent_Auction;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("23"))  // Exibitors
                {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.live_Auction;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.live_Auction;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("24"))  // Exibitors
                {

                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.Online_shop;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Online_shop;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("25"))  // Exibitors
                {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.DonateFor_veteran;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.DonateFor_veteran;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("43"))  // Exibitors
                {

                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.Sponsor_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Sponsor_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("44"))  // Exibitors
                {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.TweitterFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.TweitterFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("45"))  // ActivityFeed
                {
                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.activityFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.activityFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("165")) {

                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.NOTES_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.NOTES_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("46")) {

                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.InstagramFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.InstagramFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("49")) {

                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.FavoriteList_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.FavoriteList_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }

                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("47")) {

                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.facebookFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.facebookFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    }
                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("50")) {

                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.QAList_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.QAList_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }
                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("27")) {

                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.notificationListingFragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.notificationListingFragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }
                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("52")) {

                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.Gamification_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Gamification_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }
                } else if (getActivity().getIntent().getStringExtra("product_id").equalsIgnoreCase("53")) {

                    if (GlobalData.Fragment_Stack.size() >= 1) {

                        GlobalData.CURRENT_FRAG = GlobalData.ExhibiorLead_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.ExhibiorLead_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }
                }


            } else if (getActivity().getIntent().getStringExtra("message_type").equalsIgnoreCase("custom_page")) {
                sessionManager.cms_id(getActivity().getIntent().getStringExtra("product_id"));
                sessionManager.strMenuId = "0";
                sessionManager.strModuleId = getActivity().getIntent().getStringExtra("product_id");
                if (GlobalData.Fragment_Stack.size() >= 1) {
                    GlobalData.CURRENT_FRAG = GlobalData.CMS_FRAGMENT_TEST1;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.CMS_FRAGMENT_TEST1;
                    ((MainActivity) getActivity()).loadFragment();
                }

            }
            getActivity().getIntent().removeExtra("message_type");
        }

        btn_load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (page_count == total_page) {
                    btn_load_more.setVisibility(View.GONE);
                }
                page_count++;
                ProductList();
            }
        });
        share_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse(str_fbShare);
                Intent insta = new Intent(Intent.ACTION_VIEW, uri);
                insta.setPackage("com.facebook");
                if (isIntentAvailable(getActivity(), insta)) {
                    startActivity(insta);
                } else {
                    bundle.putString("Social_url", str_fbShare);
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                    ((MainActivity) getActivity()).loadFragment(bundle);
                }
            }
        });

        tweet_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(str_tweeet);
                Intent insta = new Intent(Intent.ACTION_VIEW, uri);
                insta.setPackage("com.twitter.android");

                if (isIntentAvailable(getActivity(), insta)) {
                    startActivity(insta);
                } else {
                    bundle.putString("Social_url", str_tweeet);
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                    ((MainActivity) getActivity()).loadFragment(bundle);
                }
            }
        });
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rv_viewProduct.setLayoutManager(linearLayoutManager);

        if (GlobalData.isNetworkAvailable(getActivity())) {

//            if(sessionManager.isLogin())
//            {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()),5, false, this);
//            }
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Fundrising_Home, Param.fundrising_home(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId()), 0, true, this);
        } else {
            cursor = sqLiteDatabaseHandler.getEventHomeData(sessionManager.getEventId());
            if (cursor.moveToFirst()) {
                Log.d("AITL DataBase HomeData ", cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.Home_EventData)));
                try {
                    JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.Home_EventData)));

                    Log.d("AITL HomeFund Oflline", jsonObject.toString());
                    loadFundHomeData(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return rootView;
    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();

        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        Log.d("AITL FBshare", list.toString());
        return list.size() > 0;
    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 6, false, this);
        }
    }

    private void setAppColor() {
        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            if (sessionManager.getFunThemeColor().equalsIgnoreCase("0")) {
                linear_footer.setBackgroundColor(Color.parseColor("#3f8acd"));
            } else {
                linear_footer.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
            }
        } else {
            if (sessionManager.getFunThemeColor().equalsIgnoreCase("0")) {
                linear_footer.setBackgroundColor(Color.parseColor("#3f8acd"));
            } else {
                linear_footer.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
            }
        }

    }

    public void ProductList() {

        if (GlobalData.isNetworkAvailable(getActivity())) {

            isFirstTime = true;
            if (isFirstTime) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Fundrising_Product, Param.fundrising_Product(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), page_count), 1, false, this);
                isFirstTime = false;
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Fundrising_Product, Param.fundrising_Product(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), page_count), 1, false, this);
            }
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject mainObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL", "Fundrising" + mainObject.toString());
                    if (mainObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        if (sessionManager.isLogin())
                            pagewiseClick();
                        JSONObject jsonObject = mainObject.getJSONObject("data");
                        loadFundHomeData(jsonObject);
                        ProductList();   // call Service for ProductList

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:

                try {

                    JSONObject jObjectProduct = new JSONObject(volleyResponse.output);

                    if (jObjectProduct.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {

                        JSONObject jsonProductData = jObjectProduct.getJSONObject("data");

                        total_page = jsonProductData.getInt("total_page");

                        Log.d("AITL", "Total Page" + total_page);
                        Log.d("AITL", "Product" + jObjectProduct.toString());

                        JSONArray jArrayProduct = jsonProductData.getJSONArray("products");

                        if (!btn_load) {
                            for (int i = 0; i < jArrayProduct.length(); i++) {

                                JSONObject index = jArrayProduct.getJSONObject(i);
                                pro_id = index.getString("product_id");
                                pro_name = index.getString("name");
                                pro_desc = index.getString("description");
                                pro_thumb = index.getString("thumb");
                                pro_action_type = index.getString("auctionType");
                                pro_price = index.getString("price");
                                pro_maxBid = index.getString("max_bid");
                                product_flag_price = index.getString("flag_price");

                                fundraising_productArrayList.add(new Fundraising_Product(pro_id, pro_name, pro_desc, MyUrls.Fund_Imgurl + pro_thumb, pro_action_type, pro_price, pro_maxBid, product_flag_price));
                                Log.d("AITL", " IF ProductImg" + MyUrls.Fund_Imgurl + pro_thumb);

                            }
                            product_adapter = new Fundraising_Product_Adapter(fundraising_productArrayList, rv_viewProduct, getActivity(), linearLayoutManager);
                            rv_viewProduct.setAdapter(product_adapter);
                            btn_load = true;
                        } else {
                            ArrayList<Fundraising_Product> tmp_pArrayList = new ArrayList<>();
                            for (int i = 0; i < jArrayProduct.length(); i++) {
                                JSONObject index = jArrayProduct.getJSONObject(i);
                                pro_id = index.getString("product_id");
                                pro_name = index.getString("name");
                                pro_desc = index.getString("description");
                                pro_thumb = index.getString("thumb");
                                pro_action_type = index.getString("auctionType");
                                pro_price = index.getString("price");
                                pro_maxBid = index.getString("max_bid");
                                product_flag_price = index.getString("flag_price");

                                tmp_pArrayList.add(new Fundraising_Product(pro_id, pro_name, pro_desc, MyUrls.Fund_Imgurl + pro_thumb, pro_action_type, pro_price, pro_maxBid, product_flag_price));
                                Log.d("AITL", "ELSE ProductImg" + MyUrls.Fund_Imgurl + pro_thumb);
                            }
                            fundraising_productArrayList.addAll(tmp_pArrayList);
                            product_adapter = new Fundraising_Product_Adapter(fundraising_productArrayList, rv_viewProduct, getActivity(), linearLayoutManager);
                            rv_viewProduct.setAdapter(product_adapter);
                        }

                        if (fundraising_productArrayList.size() == 0) {
                            rv_viewProduct.setVisibility(View.GONE);
                        } else {
                            rv_viewProduct.setVisibility(View.VISIBLE);


                            if (total_page > 1) {
                                if (page_count == total_page) {
                                    btn_load_more.setVisibility(View.GONE);
                                } else
                                    btn_load_more.setVisibility(View.VISIBLE);
                            } else {
                                btn_load_more.setVisibility(View.GONE);
                            }


                            rv_viewProduct.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Fundraising_Product productObj = product_adapter.getItem(position);

                                    Log.d("AITL Home ProductID", productObj.getId());
                                    Log.d("AITL AuctionType", productObj.getAction_type());
                                    if (productObj.getAction_type().equalsIgnoreCase("1")) {
                                        sessionManager.slilentAuctionP_id = productObj.getId();
                                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                        GlobalData.CURRENT_FRAG = GlobalData.SilentAuctionDetail_Fragment;
                                        ((MainActivity) getActivity()).loadFragment();
                                    } else if (productObj.getAction_type().equalsIgnoreCase("2")) {
                                        sessionManager.liveAuctionP_id = productObj.getId();
                                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                        GlobalData.CURRENT_FRAG = GlobalData.LiveAuction_Detail;
                                        ((MainActivity) getActivity()).loadFragment();
                                    } else if (productObj.getAction_type().equalsIgnoreCase("3")) {
                                        sessionManager.OnlineShop_id = productObj.getId();
                                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                        GlobalData.CURRENT_FRAG = GlobalData.OnlineShop_Detail;
                                        ((MainActivity) getActivity()).loadFragment();
                                    } else if (productObj.getAction_type().equalsIgnoreCase("4")) {
                                        sessionManager.pledge_id = productObj.getId();
                                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                        GlobalData.CURRENT_FRAG = GlobalData.PledgeDetailFragment;
                                        ((MainActivity) getActivity()).loadFragment();
                                    }
                                }
                            }));

                        }
                        //set_recycler();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void loadFundHomeData(JSONObject jsonObject) {
        try {
            notes_status = jsonObject.getString("note_status");
            currency_status = jsonObject.getString("currency");
            cart_status = jsonObject.getString("cart_count");

            JSONArray jArrayDonation = jsonObject.getJSONArray("donation_settings");
            if (jArrayDonation.length() != 0) {
                for (int d = 0; d < jArrayDonation.length(); d++) {
                    JSONObject jsonDonation = jArrayDonation.getJSONObject(d);
                    stripeSk = jsonDonation.getString("secret_key");
                    stripePk = jsonDonation.getString("public_key");
                    sessionManager.setIsStripeEnabled(jsonDonation.getString("hide_stripe"));

                }
            }

            Log.d("AITL Stripe", "SecretKey" + stripeSk);
            Log.d("AITL Stripe", "PublicKey" + stripePk);


            JSONArray jsonArray = jsonObject.optJSONArray("events");
            for (int e = 0; e < jsonArray.length(); e++) {
                JSONObject jObjectevent = (JSONObject) jsonArray.get(e);

                social_media = jObjectevent.getString("social_media");
                sessionManager.setPhotoFilterImage(jObjectevent.getString("photo_filter_image"));
                Log.d("AITL", "jObjectevent" + jObjectevent);
                sessionManager.appColor(jObjectevent);
            }


            if (social_media.equalsIgnoreCase("1")) {
                share_fb.setVisibility(View.VISIBLE);
                tweet_twitter.setVisibility(View.VISIBLE);
            } else {
                share_fb.setVisibility(View.GONE);
                tweet_twitter.setVisibility(View.GONE);
            }

            str_fbShare = jsonObject.getString("facebook_url");
            Log.d("AITL FBSHARE", str_fbShare);
            str_tweeet = jsonObject.getString("twitter_url");
//            str_tweeet=URLDecoder.decode(jsonObject.getString("twitter_url"),"UTF-8");
            Log.d("AITL TWeET", str_tweeet);
            str_fundring_amount = jsonObject.getString("raised_so_far");

            // JSONArray jsonArray = jsonObject.optJSONArray("events");
            JSONArray jArraySlides = jsonObject.getJSONArray("slides");
            JSONArray jArrayHome = jsonObject.getJSONArray("home_menu");
            JSONArray jArrayFund = jsonObject.getJSONArray("fundraising_settings");

            JSONArray jArrayFooter = jsonObject.getJSONArray("latest_pleadge_bids");

            for (int f = 0; f < jArrayFooter.length(); f++) {
                JSONObject jObjectFooter = jArrayFooter.getJSONObject(f);

                str_firstName = jObjectFooter.getString("Firstname");
                str_lastName = jObjectFooter.getString("Lastname");
                str_logo = jObjectFooter.getString("Logo");
                str_amt = jObjectFooter.getString("amt");
                str_productName = jObjectFooter.getString("product_name");
                Log.d("AITL", "Logo" + str_logo);
                footerArrayList.add(new FundraisingHome_footer(str_firstName, str_lastName, MyUrls.Imgurl + str_logo, str_productName, str_amt, "fundraising"));
            }


            for (int i = 0; i < jArraySlides.length(); i++) {
                JSONObject jObjectSlide = jArraySlides.getJSONObject(i);
                String id = jObjectSlide.getString("id");
                String img_name = jObjectSlide.getString("image");

                ImgarrayList.add(new Exhibitor_DetailImage(MyUrls.Fund_Imgurl + img_name, id, "Fundrising"));
                Log.d("AITL", MyUrls.Fund_Imgurl + img_name);
            }

            if (ImgarrayList.size() == 0) {
                viewPager.setVisibility(View.GONE);
            } else {
                viewPager.setVisibility(View.VISIBLE);
                adapter = new Exhibitor_ImageAdapter(getActivity(), ImgarrayList);
                viewPager.setAdapter(adapter);
            }
            for (int k = 0; k < jArrayHome.length(); k++) {
                JSONObject jObjecthome = jArrayHome.getJSONObject(k);

                homeMenuArrayList.add(new Fundraising_HomeMenu(jObjecthome.getString("id"), jObjecthome.getString("is_feture_products"), jObjecthome.getString("menuname")));

            }

            menuAdapter = new FundraisingHomeMenuAdapter(homeMenuArrayList, getActivity());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rv_homeMenu.setLayoutManager(mLayoutManager);
            rv_homeMenu.setItemAnimator(new DefaultItemAnimator());
            rv_homeMenu.setAdapter(menuAdapter);

            rv_homeMenu.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Fundraising_HomeMenu dashboardItemListObj = homeMenuArrayList.get(position);
                    Log.d("AITL MenuId", dashboardItemListObj.getId());
                    if (dashboardItemListObj.getId().equalsIgnoreCase("22")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Silent_Auction;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getId().equalsIgnoreCase("23")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.live_Auction;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getId().equalsIgnoreCase("24")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Online_shop;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getId().equalsIgnoreCase("25")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.DonateFor_veteran;
                        ((MainActivity) getActivity()).loadFragment();
                    }

//
                }
            }));

            for (int j = 0; j < jArrayFund.length(); j++) {
                JSONObject jObjectFund = jArrayFund.getJSONObject(j);
                str_fund_target = jObjectFund.getString("Fundraising_target");
                web_contnt = jObjectFund.getString("content");
                video_link = jObjectFund.getString("event_video_link");

                instant_enbled = jObjectFund.getString("instant_donate_enbled");
                instant_Backcolor = jObjectFund.getString("instant_donate_backgroundcolor");
                instant_Textcolor = jObjectFund.getString("instant_donate_color");

                fundraising_enbled = jObjectFund.getString("fundraising_donation_enbled");
                fundraising_Backcolor = jObjectFund.getString("fundraising_donate_backgroundcolor");
                fundrasing_TextColor = jObjectFund.getString("fundraising_donate_color");

                instant_name = jObjectFund.getString("instant_donate_name");
                fundraising_name = jObjectFund.getString("fundraising_donate_name");

                target_raisedsofar_display = jObjectFund.getString("target_raisedsofar_display");
                bids_donations_display = jObjectFund.getString("bids_donations_display");

            }
            setAppColor();// call for set color

//            Log.d("AITL Video","Video Link :- "+video_link);
            if (video_link.equalsIgnoreCase("")) {
                webViewVideo.setVisibility(View.GONE);
            } else {
                webViewVideo.setVisibility(View.VISIBLE);
                WebSettings webSettings = webViewVideo.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webViewVideo.setWebViewClient(new WebViewClient());
//                webViewVideo.setWebViewClient(new WebViewClient() {
//
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, String url)
//                    {
//                        view.loadUrl(video_link);
//                        return true;
//                    }
//                });
                webViewVideo.loadUrl(video_link);

            }
            if (target_raisedsofar_display.equalsIgnoreCase("1")) {

                linear_progressview.setVisibility(View.VISIBLE);
                linear_progressview.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
                // fund_progress_bar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.btn_bidNow), PorterDuff.Mode.MULTIPLY);
                if (Fundrising_Home_Fragment.currency_status.equalsIgnoreCase("euro")) {
//
                    fundring_amount.setText(getContext().getResources().getString(R.string.euro) + str_fundring_amount);
                    target_amount.setText(getContext().getResources().getString(R.string.euro) + str_fund_target + " Raised!");

                } else if (Fundrising_Home_Fragment.currency_status.equalsIgnoreCase("gbp")) {
                    fundring_amount.setText(getContext().getResources().getString(R.string.pound_sign) + str_fundring_amount);
                    target_amount.setText(getContext().getResources().getString(R.string.pound_sign) + str_fund_target + " Raised!");
                } else if (Fundrising_Home_Fragment.currency_status.equalsIgnoreCase("usd") || Fundrising_Home_Fragment.currency_status.equalsIgnoreCase("aud")) {
                    fundring_amount.setText(getContext().getResources().getString(R.string.dollor) + str_fundring_amount);
                    target_amount.setText(getContext().getResources().getString(R.string.dollor) + str_fund_target + " Raised!");
                }

                //  target_amount.setText( str_fund_target + " Raised!");
                fund_progress_bar.setMax(100);
                fund_amount = Integer.parseInt(str_fundring_amount);
                tar_amount = Integer.parseInt(str_fund_target);
                if (tar_amount != 0) {
                    progress_amount = ((fund_amount * 100) / tar_amount);
                    fund_progress_bar.setProgress(progress_amount);
                } else {
                    fund_progress_bar.setProgress(0);
                }

//                            linear_raised.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
//                            linear_progress.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
            } else {
                linear_progressview.setVisibility(View.GONE);
            }

            if (bids_donations_display.equalsIgnoreCase("1")) {


                if (footerArrayList.size() == 0) {
                    footer_pager.setVisibility(View.GONE);
                    linear_footer.setVisibility(View.GONE);
                } else {
                    linear_footer.setVisibility(View.VISIBLE);
                    footer_pager.setVisibility(View.VISIBLE);
                    footer_adapter = new FundraisingHome_footer_adapter(getActivity(), footerArrayList, currency_status);
                    footer_pager.setAdapter(footer_adapter);

                }
            } else {
                linear_footer.setVisibility(View.GONE);
            }
            String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/Lato_Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
            String pas = "</body></html>";
            String myHtmlString = pish + web_contnt + pas;
            webViewContent.loadDataWithBaseURL("file:///android_asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);

            if (instant_enbled.equalsIgnoreCase("1")) {
                btn_instant.setVisibility(View.VISIBLE);
                btn_instant.setText(instant_name);
                btn_instant.setBackgroundColor(Color.parseColor(instant_Backcolor));
                btn_instant.setTextColor(Color.parseColor(instant_Textcolor));

            } else {
                btn_instant.setVisibility(View.GONE);
            }
            if (fundraising_enbled.equalsIgnoreCase("1")) {
                btn_fund.setVisibility(View.VISIBLE);
                btn_fund.setText(fundraising_name);
                btn_fund.setBackgroundColor(Color.parseColor(fundraising_Backcolor));
                btn_fund.setTextColor(Color.parseColor(fundrasing_TextColor));
            } else {
                btn_fund.setVisibility(View.GONE);
            }

            checkNoteStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        webViewVideo.loadUrl("about:blank");
    }

    private void checkNoteStatus() {
        ((MainActivity) getActivity()).updateCartCountFromFundHome(cart_status);
    }

}
