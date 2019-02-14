package com.allintheloop.Fragment.Fundraising_auctionModule;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.allintheloop.Adapter.Exhibitor.Exhibitor_ImageAdapter;
import com.allintheloop.Adapter.FundraisingHome_footer_adapter;
import com.allintheloop.Bean.ExhibitorListClass.Exhibitor_DetailImage;
import com.allintheloop.Bean.Fundraising.FundraisingHome_footer;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import me.grantland.widget.AutofitTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SilentAuction_ProductDetail extends Fragment implements VolleyInterface {


    public static CountDownTimer timer;
    ArrayList<FundraisingHome_footer> footerArrayList;
    ArrayList<Exhibitor_DetailImage> viewpager_arraylist;
    ArrayList<String> productIdarrray;
    FundraisingHome_footer_adapter footer_adapter;
    Exhibitor_ImageAdapter adapter;
    SessionManager sessionManager;
    LinearLayout linear_time, linear_enterbid, linear_btnshare, linear_bidstatus, linear_desc, linear_footer, linear_nextPrivious, linear_timeinside, linear_data;
    ViewPager silent_viewPager, footer_pager;
    ViewPagerIndicator pageIndicator;
    FrameLayout frame_viewpager;
    EditText edt_bid;
    Button btn_bidNow, btn_previous, btn_next;
    TextView txt_productName, txt_startPrice, txt_currentBid, txt_notReached, atxt_warnig, txt_totalBid, txt_highestBid, desc_productName, txt_label, lbl_enterBid, lbl_time, txt_time;
    AutofitTextView txt_bidder;
    ImageView detail_share_fb, detail_tweet_twitter;
    WebView webViewContent;
    String str_id = "", str_bidAmt = "", str_maxbid = "", str_name = "", str_shortdesc = "", str_desc = "", str_startprice = "", str_price = "", str_startdatetime = "", str_currentdatetime = "", str_endDatetime = "", str_increment_amt = "", str_lastBid = "", str_totalBid = "", str_biddername = "", str_winnerName = "", fb_url = "", twitter_url = "", str_currency = "", str_cart_count = "", str_firstName = "", str_lastName = "", str_logo = "", str_productName = "", str_amt = "", bids_donations_display = "", str_currcySet = "", getBidamt = "", srt_enableBid = "", is_auction_start = "", social_media = "";
    boolean isFirst;
    Calendar calendar;
    int bid_amount, edt_amount, index = 0;
    Bundle bundle;
    CardView card_noproductDetail;
    UidCommonKeyClass uidCommonKeyClass;

    public SilentAuction_ProductDetail() {
        // Required empty public constructor
    }

    public static void cancle_timer() {
        if (timer != null) timer.cancel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sailentauction_detail, container, false);
        sessionManager = new SessionManager(getActivity());
        Log.d("AITL SilentPID", "PRODUCT ID" + sessionManager.slilentAuctionP_id);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        footerArrayList = new ArrayList<>();
        productIdarrray = new ArrayList<>();
        bundle = new Bundle();
        linear_time = (LinearLayout) rootView.findViewById(R.id.linear_time);
        linear_enterbid = (LinearLayout) rootView.findViewById(R.id.linear_enterbid);
        linear_btnshare = (LinearLayout) rootView.findViewById(R.id.linear_btnshare);
        linear_bidstatus = (LinearLayout) rootView.findViewById(R.id.linear_bidstatus);
        linear_desc = (LinearLayout) rootView.findViewById(R.id.linear_desc);
        linear_footer = (LinearLayout) rootView.findViewById(R.id.linear_footer);
        linear_timeinside = (LinearLayout) rootView.findViewById(R.id.linear_timeinside);
        linear_nextPrivious = (LinearLayout) rootView.findViewById(R.id.linear_nextPrivious);
        linear_data = (LinearLayout) rootView.findViewById(R.id.linear_data);

        card_noproductDetail = (CardView) rootView.findViewById(R.id.card_noproductDetail);
        frame_viewpager = (FrameLayout) rootView.findViewById(R.id.frame_viewpager);

        edt_bid = (EditText) rootView.findViewById(R.id.edt_bid);
        btn_bidNow = (Button) rootView.findViewById(R.id.btn_bidNow);
        btn_previous = (Button) rootView.findViewById(R.id.btn_previous);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        txt_productName = (TextView) rootView.findViewById(R.id.txt_productName);
        txt_startPrice = (TextView) rootView.findViewById(R.id.txt_startPrice);
        txt_currentBid = (TextView) rootView.findViewById(R.id.txt_currentBid);
        txt_notReached = (TextView) rootView.findViewById(R.id.txt_notReached);
        atxt_warnig = (TextView) rootView.findViewById(R.id.atxt_warnig);
        txt_totalBid = (TextView) rootView.findViewById(R.id.txt_totalBid);
        txt_bidder = (AutofitTextView) rootView.findViewById(R.id.txt_bidder);
        txt_highestBid = (TextView) rootView.findViewById(R.id.txt_highestBid);
        desc_productName = (TextView) rootView.findViewById(R.id.desc_productName);
        lbl_enterBid = (TextView) rootView.findViewById(R.id.lbl_enterBid);
        txt_label = (TextView) rootView.findViewById(R.id.txt_label);
        lbl_time = (TextView) rootView.findViewById(R.id.lbl_time);

//        txt_day=(TextView)rootView.findViewById(R.id.txt_day);
//        txt_hour=(TextView)rootView.findViewById(R.id.txt_hour);
//        txt_min=(TextView)rootView.findViewById(R.id.txt_min);
//        txt_sec=(TextView)rootView.findViewById(R.id.txt_sec);
        txt_time = (TextView) rootView.findViewById(R.id.txt_time);

        detail_share_fb = (ImageView) rootView.findViewById(R.id.detail_share_fb);
        detail_tweet_twitter = (ImageView) rootView.findViewById(R.id.detail_tweet_twitter);

        webViewContent = (WebView) rootView.findViewById(R.id.webViewContent);
        webViewContent.getSettings().setJavaScriptEnabled(true);
        webViewContent.getSettings().setAllowContentAccess(true);
        webViewContent.setVerticalScrollBarEnabled(true);
        webViewContent.getSettings().setDefaultTextEncodingName("utf-8");
        webViewContent.setHorizontalScrollBarEnabled(true);

        calendar = Calendar.getInstance();

        silent_viewPager = (ViewPager) rootView.findViewById(R.id.silent_viewPager);
        footer_pager = (ViewPager) rootView.findViewById(R.id.footer_pager);
        pageIndicator = (ViewPagerIndicator) rootView.findViewById(R.id.pageIndicator);


        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {
            Log.d("AITL", "Fundraising Color");
            btn_next.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            btn_previous.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            btn_next.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_previous.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            linear_timeinside.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            lbl_time.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            txt_time.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));

        } else {
            Log.d("AITL", "EvenApp Color");
            btn_next.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            btn_previous.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            btn_next.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_previous.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            linear_timeinside.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            lbl_time.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            txt_time.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        }
        detail_share_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("Social_url", fb_url);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);
            }
        });

        detail_tweet_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString("Social_url", twitter_url);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);
            }
        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == (productIdarrray.size() - 1)) {

                    btn_next.setVisibility(View.GONE);
                } else {
                    index++;
                    btn_previous.setVisibility(View.VISIBLE);
                    isFirst = true;
                }
                Log.d("AITL ProductArrayID", productIdarrray.get(index).toString());
                cancle_timer();
                getProductdetail(productIdarrray.get(index).toString());

            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 0) {

                    btn_previous.setVisibility(View.GONE);
                } else {
                    index--;
                    btn_next.setVisibility(View.VISIBLE);
                    isFirst = true;
                }
                Log.d("AITL ProductArrayID", productIdarrray.get(index).toString());
                cancle_timer();
                getProductdetail(productIdarrray.get(index).toString());

            }
        });


        btn_bidNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GlobalData.isNetworkAvailable(getActivity())) {

                    try {
                        getBidamt = edt_bid.getText().toString();
                        edt_amount = Integer.parseInt(getBidamt);

                        if (edt_amount != 0) {
                            if (edt_amount < bid_amount) {

                                if (str_currency.equalsIgnoreCase("euro")) {
                                    str_currcySet = "Enter Your Bid " + getActivity().getResources().getString(R.string.euro);

                                } else if (str_currency.equalsIgnoreCase("gbp")) {
                                    str_currcySet = "Enter Your Bid " + getActivity().getResources().getString(R.string.pound_sign);

                                } else if (str_currency.equalsIgnoreCase("usd") || str_currency.equalsIgnoreCase("aud")) {
                                    str_currcySet = "Enter Your Bid " + getActivity().getResources().getString(R.string.dollor);

                                }

                                new AlertDialogWrapper.Builder(getActivity())
                                        .setMessage("Your bid does not exceed the current bid by the increments set - Please bid over" + " " + str_currcySet + bid_amount)
                                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            } else {
                                if (GlobalData.isNetworkAvailable(getActivity())) {
                                    cancle_timer();
                                    addBid();
                                } else {
                                    ToastC.show(getActivity(), "No Internet Connection");
                                }
                            }
                        } else {
                            ToastC.show(getActivity(), "Please Enter Bid Value");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        if (GlobalData.isNetworkAvailable(getActivity())) {
//            if (sessionManager.isLogin()) {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()), 5, false, this);
//            }

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getProdcuId, Param.detail_productId(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), "1"), 1, false, this);
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }
        return rootView;
    }

    private void getProductdetail(String id) {
        sessionManager.strModuleId = id;
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Silentauction_detail, Param.detail_product(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), sessionManager.getUserId(), id), 0, false, this);
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }
    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 6, false, this);
        }
    }

    private void addBid() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.save_bidDetail, Param.save_bidDetail(sessionManager.getEventId(), sessionManager.getEventType(), str_id, sessionManager.getUserId(), getBidamt, sessionManager.getToken()), 2, true, this);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        if (sessionManager.isLogin())
                            pagewiseClick();

                        JSONObject jdata = jsonObject.getJSONObject("data");
                        JSONArray jArrayProduct = jdata.getJSONArray("products");
                        str_currency = jdata.getString("currency");
                        str_cart_count = jdata.getString("cart_count");
                        edt_bid.setText("");


                        if (jArrayProduct.length() == 0) {
                            linear_data.setVisibility(View.GONE);
                            card_noproductDetail.setVisibility(View.VISIBLE);
                        } else {
                            linear_data.setVisibility(View.VISIBLE);
                            card_noproductDetail.setVisibility(View.GONE);

                            JSONArray jsonArray = jdata.optJSONArray("events");
                            for (int e = 0; e < jsonArray.length(); e++) {
                                JSONObject jObjectevent = (JSONObject) jsonArray.get(e);
                                Log.d("AITL", "jObjectevent" + jObjectevent);
                                social_media = jObjectevent.getString("social_media");
                                sessionManager.appColor(jObjectevent);
                            }

                            JSONArray jArrayFund = jdata.getJSONArray("fundraising_settings");
                            for (int j = 0; j < jArrayFund.length(); j++) {
                                JSONObject jObjectFund = jArrayFund.getJSONObject(j);

                                bids_donations_display = jObjectFund.getString("bids_donations_display");
                            }
                            if (social_media.equalsIgnoreCase("1")) {
                                detail_share_fb.setVisibility(View.VISIBLE);
                                detail_tweet_twitter.setVisibility(View.VISIBLE);
                            } else {
                                detail_share_fb.setVisibility(View.GONE);
                                detail_tweet_twitter.setVisibility(View.GONE);
                            }


                            Log.d("AITL Product", jArrayProduct.toString());
                            for (int i = 0; i < jArrayProduct.length(); i++) {
                                JSONObject index = jArrayProduct.getJSONObject(i);
                                str_id = index.getString("product_id");
                                str_bidAmt = index.getString("bid_amt");
                                str_name = index.getString("name");
                                str_maxbid = index.getString("max_bid");
                                str_shortdesc = index.getString("short_description");
                                str_desc = index.getString("description");
                                str_startprice = index.getString("startPrice");
                                srt_enableBid = index.getString("enable_bid");
                                str_price = index.getString("price");
                                str_increment_amt = index.getString("increment_amt");
                                str_startdatetime = index.getString("auctionStartDateTime");
                                str_endDatetime = index.getString("auctionEndDateTime");
                                str_currentdatetime = index.getString("current_date_time");
                                fb_url = index.getString("facebook_url");
                                twitter_url = index.getString("twitter_url");
                                is_auction_start = index.getString("is_auction_start");

                                if (str_increment_amt.trim().length() != 0) {
                                    if (str_maxbid.trim().length() != 0) {
                                        bid_amount = Integer.parseInt(str_maxbid) + Integer.parseInt(str_increment_amt);
                                        Log.d("AITL MaxBidAmount", "" + bid_amount);
                                    }
                                }

                                Log.d("AITL FBshare", fb_url);
                                Log.d("AITL TwitterUrl", twitter_url);
                                JSONArray jArrayImage = index.getJSONArray("image_arr");
                                viewpager_arraylist = new ArrayList<>();
                                for (int z = 0; z < jArrayImage.length(); z++) {
                                    String url = MyUrls.Fund_Imgurl + jArrayImage.get(z).toString();
                                    viewpager_arraylist.add(new Exhibitor_DetailImage(url, "silent_productDetail"));
                                    Log.d("AITL", "silent_productDetail Url : " + url);

                                }
                            }
                            try {

                                String startDate, endDate, startTime, endTime;
                                String[] startDateTime, endDateTime;
                                Date dtStart, dtEnd, curr_date;

                                Calendar c = Calendar.getInstance();
                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                dtStart = inputFormat.parse(str_startdatetime);
                                dtEnd = inputFormat.parse(str_endDatetime);
                                curr_date = inputFormat.parse(str_currentdatetime);

                                startDateTime = str_startdatetime.split(" ");
                                endDateTime = str_endDatetime.split(" ");

                                startDate = startDateTime[0];
                                endDate = endDateTime[0];

                                if (GlobalData.checkForUIDVersion()) {
                                    uidCommonKeyClass = sessionManager.getUidCommonKey();
                                    if (uidCommonKeyClass.getIsOrganizerUser().equalsIgnoreCase("1")) {
                                        linear_time.setVisibility(View.GONE);
                                        linear_enterbid.setVisibility(View.GONE);
                                        linear_bidstatus.setVisibility(View.GONE);
                                    } else {
                                        linear_time.setVisibility(View.VISIBLE);
                                        linear_enterbid.setVisibility(View.VISIBLE);
                                        linear_bidstatus.setVisibility(View.VISIBLE);

                                        Log.d("AITL Enabled BId", srt_enableBid);
                                        if (srt_enableBid.equalsIgnoreCase("1")) {
                                            if (is_auction_start.equalsIgnoreCase("0")) {
                                                linear_time.setVisibility(View.VISIBLE);
                                                getDateDiffString(curr_date, dtEnd);
                                                lbl_time.setText("Auction Ends In :");
                                                Log.d("AITL AfterDate", endDate);
                                                linear_enterbid.setVisibility(View.VISIBLE);
                                                Log.d("AITL Currentdate", new Date().toString());
                                            } else if (is_auction_start.equalsIgnoreCase("1")) {
                                                linear_time.setVisibility(View.VISIBLE);
                                                linear_enterbid.setVisibility(View.VISIBLE);
                                                getDateDiffString(dtStart, curr_date);
                                                lbl_time.setText("Auction Start In :");
                                                Log.d("AITL BeforeDate", startDate);
                                            } else {
                                                linear_time.setVisibility(View.GONE);
                                                linear_enterbid.setVisibility(View.GONE);
                                                txt_notReached.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            linear_time.setVisibility(View.GONE);
                                            linear_enterbid.setVisibility(View.GONE);
                                            txt_notReached.setVisibility(View.GONE);
                                        }


//
                                    }


                                } else {


                                    if (sessionManager.getRolId().equalsIgnoreCase("3")) { //changes applied
                                        linear_time.setVisibility(View.GONE);
                                        linear_enterbid.setVisibility(View.GONE);
                                        linear_bidstatus.setVisibility(View.GONE);
                                    } else {
                                        linear_time.setVisibility(View.VISIBLE);
                                        linear_enterbid.setVisibility(View.VISIBLE);
                                        linear_bidstatus.setVisibility(View.VISIBLE);

                                        Log.d("AITL Enabled BId", srt_enableBid);
                                        if (srt_enableBid.equalsIgnoreCase("1")) {
                                            if (is_auction_start.equalsIgnoreCase("0")) {
                                                linear_time.setVisibility(View.VISIBLE);
                                                getDateDiffString(curr_date, dtEnd);
                                                lbl_time.setText("Auction Ends In :");
                                                Log.d("AITL AfterDate", endDate);
                                                linear_enterbid.setVisibility(View.VISIBLE);
                                                Log.d("AITL Currentdate", new Date().toString());
                                            } else if (is_auction_start.equalsIgnoreCase("1")) {
                                                linear_time.setVisibility(View.VISIBLE);
                                                linear_enterbid.setVisibility(View.VISIBLE);
                                                getDateDiffString(dtStart, curr_date);
                                                lbl_time.setText("Auction Start In :");
                                                Log.d("AITL BeforeDate", startDate);
                                            } else {
                                                linear_time.setVisibility(View.GONE);
                                                linear_enterbid.setVisibility(View.GONE);
                                                txt_notReached.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            linear_time.setVisibility(View.GONE);
                                            linear_enterbid.setVisibility(View.GONE);
                                            txt_notReached.setVisibility(View.GONE);
                                        }


//
                                    }


                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (viewpager_arraylist.size() == 0) {
                                frame_viewpager.setVisibility(View.GONE);
                            } else {
                                frame_viewpager.setVisibility(View.VISIBLE);
                                adapter = new Exhibitor_ImageAdapter(getActivity(), viewpager_arraylist);
                                silent_viewPager.setAdapter(adapter);
                                pageIndicator.setupWithViewPager(silent_viewPager);

                            }
                            JSONObject jBiddata = jdata.getJSONObject("bid_data");
                            str_lastBid = jBiddata.getString("lastbid");
                            str_totalBid = jBiddata.getString("total_bids");
                            str_biddername = jBiddata.getString("name");
                            str_winnerName = jBiddata.getString("winner_name");

                            JSONArray jArrayFooter = jdata.getJSONArray("latest_pleadge_bids");

                            for (int f = 0; f < jArrayFooter.length(); f++) {
                                JSONObject jObjectFooter = jArrayFooter.getJSONObject(f);

                                str_firstName = jObjectFooter.getString("Firstname");
                                str_lastName = jObjectFooter.getString("Lastname");
                                str_logo = jObjectFooter.getString("Logo");
                                str_amt = jObjectFooter.getString("amt");
                                str_productName = jObjectFooter.getString("product_name");
                                Log.d("AITL", "Logo" + str_logo);
                                footerArrayList.add(new FundraisingHome_footer(str_firstName, str_lastName, MyUrls.Imgurl + str_logo, str_productName, str_amt, "silent_auction_detail"));
                            }

                            if (bids_donations_display.equalsIgnoreCase("1")) {
                                if (footerArrayList.size() == 0) {
                                    footer_pager.setVisibility(View.GONE);
                                    linear_footer.setVisibility(View.GONE);
                                } else {
                                    linear_footer.setVisibility(View.VISIBLE);
                                    footer_pager.setVisibility(View.VISIBLE);
                                    footer_adapter = new FundraisingHome_footer_adapter(getActivity(), footerArrayList, str_currency);
                                    footer_pager.setAdapter(footer_adapter);

                                }
                            } else {
                                linear_footer.setVisibility(View.GONE);
                            }
                            loadData();
                            checkNoteStatus();
                            setAppColor();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jObjectProductId = new JSONObject(volleyResponse.output);
                    Log.d("AITL ProductID", jObjectProductId.toString());
                    if (jObjectProductId.getString("success").equalsIgnoreCase("true")) {
                        if (isFirst == false) {
                            getProductdetail(sessionManager.slilentAuctionP_id);
                        }
                        JSONObject jproductData = jObjectProductId.getJSONObject("data");
                        JSONArray jproductarray = jproductData.getJSONArray("product_id_arr");

                        for (int i = 0; i < jproductarray.length(); i++) {
                            JSONObject jproduct = jproductarray.getJSONObject(i);
                            productIdarrray.add(jproduct.getString("product_id"));
                        }
                        if (productIdarrray.size() > 1) {
                            linear_nextPrivious.setVisibility(View.VISIBLE);
                        } else {
                            linear_nextPrivious.setVisibility(View.GONE);
                        }
                        Log.d("AITL ProductId Array", productIdarrray.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jObjectbid = new JSONObject(volleyResponse.output);
                    if (jObjectbid.getString("success").equalsIgnoreCase("true")) {

                        GlobalData.CURRENT_FRAG = GlobalData.SilentAuctionDetail_Fragment;
                        ((MainActivity) getActivity()).reloadFragment();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private void loadData() {
        desc_productName.setText(str_name);
        txt_productName.setText(str_name);
        txt_bidder.setText(str_biddername);
        txt_currentBid.setText(str_maxbid);

        txt_totalBid.setText(str_totalBid);

        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/Lato-Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
        String pas = "</body></html>";
        String myHtmlString = pish + str_desc + pas;
        webViewContent.loadDataWithBaseURL("file:///android_asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);

        //  webViewContent.loadData(str_desc, "text/html; charset=utf-8", "utf-8");


        if (str_maxbid.equalsIgnoreCase("")) {
            txt_currentBid.setVisibility(View.GONE);
        }

        if (str_currency.equalsIgnoreCase("euro")) {
            lbl_enterBid.setText("Enter Your Bid " + getActivity().getResources().getString(R.string.euro));
            txt_startPrice.setText("Start Price :" + getActivity().getResources().getString(R.string.euro) + str_startprice);
            txt_currentBid.setText("Current Bid :" + getActivity().getResources().getString(R.string.euro) + str_maxbid);
            txt_highestBid.setText(getContext().getResources().getString(R.string.euro) + str_lastBid);
        } else if (str_currency.equalsIgnoreCase("gbp")) {
            lbl_enterBid.setText("Enter Your Bid " + getActivity().getResources().getString(R.string.pound_sign));
            txt_startPrice.setText("Start Price :" + getActivity().getResources().getString(R.string.pound_sign) + str_startprice);
            txt_currentBid.setText("Current Bid :" + getActivity().getResources().getString(R.string.pound_sign) + str_maxbid);
            txt_highestBid.setText(getContext().getResources().getString(R.string.pound_sign) + str_lastBid);
        } else if (str_currency.equalsIgnoreCase("usd") || str_currency.equalsIgnoreCase("aud")) {
            lbl_enterBid.setText("Enter Your Bid " + getActivity().getResources().getString(R.string.dollor));
            txt_startPrice.setText("Start Price :" + getActivity().getResources().getString(R.string.dollor) + str_startprice);
            txt_currentBid.setText("Current Bid :" + getActivity().getResources().getString(R.string.dollor) + str_maxbid);
            txt_highestBid.setText(getContext().getResources().getString(R.string.dollor) + str_lastBid);
        }
    }

    private void setAppColor() {
        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {

            linear_footer.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
        } else {
            linear_footer.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
        }

    }

    private void checkNoteStatus() {
        ((MainActivity) getActivity()).updateCartCount(str_cart_count);
    }

    public void getDateDiffString(Date dateOne, Date dateTwo) {
        if (!dateOne.before(dateTwo)) {
            if (timer != null) timer.cancel();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txt_time.setTextColor(getResources().getColor(R.color.survey_question));
                    txt_time.setText("Time Out");
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(1000); //You can manage the time of the blink with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    txt_time.startAnimation(anim);

                    //    btn_bid.setEnabled(false);

                }
            });
            return;
        }

        final Calendar startCalender = Calendar.getInstance();
        startCalender.setTime(dateOne);

        final Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(dateTwo);

        timer = new CountDownTimer(endCalender.getTimeInMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                endCalender.setTimeInMillis(millisUntilFinished);

                long diff = endCalender.getTimeInMillis() - startCalender.getTimeInMillis();

                long seconds = diff / 1000 % 60;
                long minutes = diff / (60 * 1000) % 60;
                long hours = diff / (60 * 60 * 1000) % 24;
                //long days = (int) diff / (24 * 60 * 60 * 1000);
                long days = TimeUnit.MILLISECONDS.toDays(diff);


                String left = "";
                if (diff > 0)
                    left += days + " " + "Days" + " |" + " ";
                if (hours > 0)
                    left += hours + " " + "Hours" + " |" + " ";
                if (minutes > 0)
                    left += minutes + " " + "Min" + " |" + " ";

                left += seconds + " " + "Second";

//                Log.d("AITL DAY HOURS MIN SECONDS",days+":"+hours+":"+minutes+":"+seconds);

                final String finalLeft = left;
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalLeft.equals("0") || finalLeft.contains("-")) {
                                txt_time.setText("TimeOut");
                                txt_time.setTextColor(getResources().getColor(R.color.survey_question));
                                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                                anim.setDuration(1000); //You can manage the time of the blink with this parameter
                                anim.setStartOffset(20);
                                anim.setRepeatMode(Animation.REVERSE);
                                anim.setRepeatCount(Animation.INFINITE);
                                txt_time.startAnimation(anim);

//                            btn_bid.setEnabled(false);
                            } else
                                txt_time.setText(finalLeft);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }

}