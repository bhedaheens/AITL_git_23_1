package com.allintheloop.Fragment;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.Exhibitor.Exhibitor_ImageAdapter;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.ExhibitorListClass.Exhibitor_DetailImage;
import com.allintheloop.Bean.SponsorClass.SponsorReloadEventBus;
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

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class Sponsor_Detail_Fragment extends Fragment implements VolleyInterface {


    LinearLayout linear_website;

    BoldTextView spon_companyName, spon_name, txt_cmpnyname;
    WebView exhi_description;
    ImageView spon_img;
    ProgressBar progressBar1;
    //public static HomeCustomViewPager headerViewPager;
    ArrayList<Exhibitor_DetailImage> arrayList;
    Exhibitor_ImageAdapter adapter;
    BoldTextView btn_fb, btn_twitter, btn_linkin, btn_insta, btn_youtube;
    TextView txt_vistiWebsite;
    String str_sponName, str_cmpName, str_desc, fb_url, twitter_url, linked_url, logo, str_websiteUrl, str_instaUrl, str_youtubeUrl;
    TextView txt_profileName;
    Bundle bundle;
    SessionManager sessionManager;
    LinearLayout linear_wholelayout;
    CardView card_nosponsor;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    TextView txt_sponsorType;
    String str_type, str_typeColor;
    ImageView sponser_fav;
    UidCommonKeyClass uidCommonKeyClass;
    DefaultLanguage.DefaultLang defaultLang;
    private String is_favorited = "0";

    public Sponsor_Detail_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sponsor__detil, container, false);


        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        sessionManager = new SessionManager(getActivity());
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();
        defaultLang = sessionManager.getMultiLangString();
        sessionManager.strModuleId = SessionManager.sponsor_id;
//        sessionManager.setMenuid("43");
        sessionManager.strMenuId = "43";
        GlobalData.currentModuleForOnResume = GlobalData.sponsorModuleid;
        bundle = new Bundle();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        arrayList = new ArrayList<>();
        linear_website = rootView.findViewById(R.id.linear_website);
        linear_wholelayout = rootView.findViewById(R.id.linear_wholelayout);
        /*txt_website = (TextView) rootView.findViewById(R.id.txt_website);
        website_url = (TextView) rootView.findViewById(R.id.website_url);*/

        exhi_description = rootView.findViewById(R.id.exhi_description);
        exhi_description.getSettings().setDefaultTextEncodingName("utf-8");

        spon_name = rootView.findViewById(R.id.spon_name);
        txt_cmpnyname = rootView.findViewById(R.id.txt_cmpnyname);
        txt_sponsorType = rootView.findViewById(R.id.txt_sponsorType);
        card_nosponsor = rootView.findViewById(R.id.card_nosponsor);

        spon_companyName = rootView.findViewById(R.id.spon_companyName);
        txt_profileName = rootView.findViewById(R.id.txt_profileName);
        btn_fb = rootView.findViewById(R.id.btn_fb);
        btn_twitter = rootView.findViewById(R.id.btn_twitter);
        btn_linkin = rootView.findViewById(R.id.btn_linkin);
        btn_insta = rootView.findViewById(R.id.btn_insta);
        btn_youtube = rootView.findViewById(R.id.btn_youtube);
        txt_vistiWebsite = rootView.findViewById(R.id.txt_visitWebsite);
        sponser_fav = rootView.findViewById(R.id.sponser_fav);

        //headerViewPager = (HomeCustomViewPager) rootView.findViewById(R.id.headerViewPager);

        spon_img = rootView.findViewById(R.id.spon_img);
        progressBar1 = rootView.findViewById(R.id.progressBar1);

        sponser_fav.setOnClickListener(view -> {
            if (GlobalData.isNetworkAvailable(getActivity())) {
                if (GlobalData.checkForUIDVersion())
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_SponsorDetailv2Uid, Param.get_SponsorDetail(sessionManager.getUserId(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.sponsor_id, sessionManager.getToken(), "1"), 2, true, this);
                else
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_SponsorDetailv2, Param.get_SponsorDetail(sessionManager.getUserId(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.sponsor_id, sessionManager.getToken(), "1"), 2, true, this);
            } else {
                ToastC.show(getActivity(), "No Internet Connection");
            }
        });

        btn_fb.setOnClickListener(v -> {

            bundle.putString("Social_url", fb_url);
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
            ((MainActivity) getActivity()).loadFragment(bundle);

        });
        btn_insta.setOnClickListener(v -> {
            bundle.putString("Social_url", str_instaUrl);
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
            ((MainActivity) getActivity()).loadFragment(bundle);
        });

        btn_youtube.setOnClickListener(v -> {

            bundle.putString("Social_url", str_youtubeUrl);
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
            ((MainActivity) getActivity()).loadFragment(bundle);

        });


        btn_twitter.setOnClickListener(v -> {

            bundle.putString("Social_url", twitter_url);
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
            ((MainActivity) getActivity()).loadFragment(bundle);

        });

        btn_linkin.setOnClickListener(v -> {

            bundle.putString("Social_url", linked_url);
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
            ((MainActivity) getActivity()).loadFragment(bundle);

        });

        txt_vistiWebsite.setOnClickListener(v -> {
            bundle.putString("Social_url", str_websiteUrl);
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
            ((MainActivity) getActivity()).loadFragment(bundle);

        });
        if (GlobalData.isNetworkAvailable(getActivity())) {
//            if(sessionManager.isLogin())
//            {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()),5, false, this);
//            }

            Cursor cursor = sqLiteDatabaseHandler.getSponsorDetail(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), sessionManager.sponsor_id);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jArrayevent = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.sponsorDetail_Data)));
                        JSONObject jsonObject = jArrayevent.getJSONObject("data");
                        Log.d("AITL  Oflline", jsonObject.toString());
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (GlobalData.checkForUIDVersion())
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_SponsorDetailv2Uid, Param.get_SponsorDetail(sessionManager.getUserId(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.sponsor_id, sessionManager.getToken(), "0"), 0, false, this);
                    else
                        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_SponsorDetailv2, Param.get_SponsorDetail(sessionManager.getUserId(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.sponsor_id, sessionManager.getToken(), "0"), 0, false, this);
                }
            } else {
                if (GlobalData.checkForUIDVersion())
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_SponsorDetailv2Uid, Param.get_SponsorDetail(sessionManager.getUserId(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.sponsor_id, sessionManager.getToken(), "0"), 0, false, this);
                else
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_SponsorDetailv2, Param.get_SponsorDetail(sessionManager.getUserId(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.sponsor_id, sessionManager.getToken(), "0"), 0, false, this);
            }
        } else {
            Cursor cursor = sqLiteDatabaseHandler.getSponsorDetail(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), sessionManager.sponsor_id);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                      JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jArrayevent = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.sponsorDetail_Data)));
                        JSONObject jsonObject = jArrayevent.getJSONObject("data");
                        Log.d("AITL  Oflline", jsonObject.toString());
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                linear_wholelayout.setVisibility(View.GONE);
                card_nosponsor.setVisibility(View.VISIBLE);
            }
        }

        return rootView;
    }

    private void loadData(JSONObject json_data) {
        try {
            JSONObject json_Spondetail = json_data.getJSONObject("sponsors_details");
            if (json_Spondetail.length() == 0) {
                linear_wholelayout.setVisibility(View.GONE);
                card_nosponsor.setVisibility(View.VISIBLE);
            } else {

                linear_wholelayout.setVisibility(View.VISIBLE);
                card_nosponsor.setVisibility(View.GONE);
                str_sponName = json_Spondetail.getString("Sponsors_name");
                str_cmpName = json_Spondetail.getString("Company_name");
                str_desc = json_Spondetail.getString("Description");
                str_websiteUrl = json_Spondetail.getString("website_url");
                fb_url = json_Spondetail.getString("facebook_url");
                twitter_url = json_Spondetail.getString("twitter_url");
                linked_url = json_Spondetail.getString("linkedin_url");
                str_instaUrl = json_Spondetail.getString("instagram_url");
                str_youtubeUrl = json_Spondetail.getString("youtube_url");
                logo = json_Spondetail.getString("company_logo");
                str_type = json_Spondetail.getString("type");
                str_typeColor = json_Spondetail.getString("type_color");
                is_favorited = json_Spondetail.getString("is_favorited");

                if (sessionManager.isLogin()) {

                    if (GlobalData.checkForUIDVersion()) {
                        if (sessionManager.getfavIsEnabled().equalsIgnoreCase("1")
                                && uidCommonKeyClass.getIsOnlyAttendeeUser().equalsIgnoreCase("1")) {
                            sponser_fav.setVisibility(View.VISIBLE);
                        } else {
                            sponser_fav.setVisibility(View.GONE);
                        }
                    } else {
                        if (sessionManager.getfavIsEnabled().equalsIgnoreCase("1")
                                && sessionManager.getRolId().equalsIgnoreCase("4")) {//changes applied
                            sponser_fav.setVisibility(View.VISIBLE);
                        } else {
                            sponser_fav.setVisibility(View.GONE);
                        }
                    }
                } else {
                    sponser_fav.setVisibility(View.GONE);
                }


                String topColor = "#FFFFFF";
                if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                    topColor = sessionManager.getFunTopBackColor();
                } else {
                    topColor = sessionManager.getTopBackColor();
                }
                sponser_fav.setColorFilter(Color.parseColor(topColor));
                if (is_favorited.equalsIgnoreCase("1")) {
                    sponser_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_selected));
                } else {
                    sponser_fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_normal));
                }

                if (str_type.equalsIgnoreCase("")) {
                    txt_sponsorType.setVisibility(View.GONE);
                } else {
                    txt_sponsorType.setVisibility(View.VISIBLE);
                    txt_sponsorType.setText(str_type);
                    if (str_typeColor.equalsIgnoreCase("")) {
                        txt_sponsorType.setBackgroundColor(getResources().getColor(R.color.black));
                    } else {
                        txt_sponsorType.setBackgroundColor(Color.parseColor(str_typeColor));
                    }

                }

                JSONArray jArrayImage = json_Spondetail.getJSONArray("Images");
                for (int i = 0; i < jArrayImage.length(); i++) {
                    String url = MyUrls.Imgurl + jArrayImage.get(i).toString();
                    arrayList.add(new Exhibitor_DetailImage(url, "Sponsor"));
                    Log.d("AITL", "Exhibitor Url : " + url);

                }
                /*if (arrayList.size() == 0) {
                    headerViewPager.setVisibility(View.GONE);
                } else {
                    headerViewPager.setVisibility(View.VISIBLE);
                    adapter = new Exhibitor_ImageAdapter(getActivity(), arrayList);
                    headerViewPager.setAdapter(adapter);

                }*/
                Log.d("AITL websiteUrl", str_websiteUrl);
                Log.d("AITL Keyword", str_websiteUrl);


                if (fb_url.equalsIgnoreCase("")) {
                    btn_fb.setVisibility(View.GONE);
                } else {
                    btn_fb.setVisibility(View.VISIBLE);
                }

                if (twitter_url.equalsIgnoreCase("")) {
                    btn_twitter.setVisibility(View.GONE);
                } else {
                    btn_twitter.setVisibility(View.VISIBLE);
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


                if (linked_url.equalsIgnoreCase("")) {
                    btn_linkin.setVisibility(View.GONE);
                } else {
                    btn_linkin.setVisibility(View.VISIBLE);
                }
                if (str_sponName.equalsIgnoreCase("")) {
                    spon_name.setVisibility(View.GONE);
                } else {
                    spon_name.setVisibility(View.GONE);
                    spon_name.setText(str_sponName);
                }

                if (str_desc.equalsIgnoreCase("")) {
                    exhi_description.setVisibility(View.GONE);
                } else {
                    exhi_description.setVisibility(View.VISIBLE);

                    String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/Lato-Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: left;}</style></head><body>";
                    String pas = "</body></html>";
                    String myHtmlString = pish + str_desc + pas;
                    exhi_description.loadDataWithBaseURL("file:///android_asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);
                }

                if (str_cmpName.equalsIgnoreCase("")) {
                    txt_cmpnyname.setVisibility(View.GONE);
                    spon_companyName.setVisibility(View.GONE);
                } else {
                    txt_cmpnyname.setVisibility(View.VISIBLE);
                    spon_companyName.setVisibility(View.VISIBLE);
                    txt_cmpnyname.setText(str_cmpName);
                    spon_companyName.setText(str_cmpName);
                }

                if (str_websiteUrl.equalsIgnoreCase("")) {
                    linear_website.setVisibility(View.GONE);
                } else {
                    linear_website.setVisibility(View.VISIBLE);
//                    txt_website.setText(defaultLang.get43Website() + " : ");
//                    website_url.setText(str_websiteUrl);
                }

                Log.d("AITL", "Exhibitor Detail" + MyUrls.Imgurl + logo);
                //Glide.with(getActivity()).load(MyUrls.Imgurl+logo).centerCrop().fitCenter().placeholder(R.drawable.defult_attende).into(spon_img);
                final String imgpath = MyUrls.Imgurl + logo;
                if (logo.equalsIgnoreCase("")) {
                    progressBar1.setVisibility(View.GONE);
                    spon_img.setVisibility(View.GONE);
                    txt_profileName.setVisibility(View.VISIBLE);
                    GradientDrawable drawable = new GradientDrawable();
                    Random rnd = new Random();

                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    Log.d("AITL SPEAKER Color", "" + color);
                    if (!(str_cmpName.equalsIgnoreCase(""))) {
                        txt_profileName.setText("" + str_cmpName.charAt(0));
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

                    Glide.with(getActivity()).load(imgpath).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            progressBar1.setVisibility(View.GONE);
                            spon_img.setVisibility(View.GONE);
                            txt_profileName.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar1.setVisibility(View.GONE);
                            spon_img.setVisibility(View.VISIBLE);
                            txt_profileName.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(new BitmapImageViewTarget(spon_img) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            spon_img.setImageBitmap(RoundedImageConverter.getRoundedCornerBitmap(resource, Color.WHITE, 30, 0, getContext()));
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", sessionManager.sponsor_id, "", "SP", ""), 1, false, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.sponsorModuleid)) {
            ((MainActivity) getActivity()).getUpdatedDataFromParticularmodule(GlobalData.sponsorModuleid);
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject json_data = jsonObject.getJSONObject("data");

                        loadData(json_data);
                        if (sqLiteDatabaseHandler.isSponsorExist(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), sessionManager.sponsor_id)) {
                            sqLiteDatabaseHandler.deleteSponsorDetailData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionManager.sponsor_id);
                            sqLiteDatabaseHandler.insertSponsorDetail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObject.toString(), sessionManager.sponsor_id);
                        } else {
                            sqLiteDatabaseHandler.insertSponsorDetail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObject.toString(), sessionManager.sponsor_id);
                        }
                        if (sessionManager.isLogin()) {
                            pagewiseClick(); // hit API

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject json_data = jsonObject.getJSONObject("data");

                        loadData(json_data);
                        if (sqLiteDatabaseHandler.isSponsorExist(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), sessionManager.sponsor_id)) {
                            sqLiteDatabaseHandler.deleteSponsorDetailData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionManager.sponsor_id);
                            sqLiteDatabaseHandler.insertSponsorDetail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObject.toString(), sessionManager.sponsor_id);
                        } else {
                            sqLiteDatabaseHandler.insertSponsorDetail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObject.toString(), sessionManager.sponsor_id);
                        }
                        sqLiteDatabaseHandler.updateSponsorFavAdapter(sessionManager.getEventId(), sessionManager.getUserId(), SessionManager.sponsor_id, is_favorited);
                        EventBus.getDefault().post(new SponsorReloadEventBus(""));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
