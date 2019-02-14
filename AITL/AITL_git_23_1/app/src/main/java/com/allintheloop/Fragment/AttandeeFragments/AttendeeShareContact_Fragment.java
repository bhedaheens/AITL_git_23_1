package com.allintheloop.Fragment.AttandeeFragments;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.Attendee.AttendeeShareFunctionPagerAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.DefaultLanguage;
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


public class AttendeeShareContact_Fragment extends Fragment implements VolleyInterface {

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    TabLayout tabLayout;
    ViewPager viewPager;
    SessionManager sessionManager;
    AttendeeShareFunctionPagerAdapter pagerAdapter;
    Bundle bundle;
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout;
    LinearLayout linear_content;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    DefaultLanguage.DefaultLang defaultLang;
    TextView txt_fullList, txt_myContact;

    AttendeeFullDirectory_Fragment fullDirectory_fragment;
    AttendeeMyContact_Fragment myContact_fragment;
    FragmentManager fragmentManager;
    String fragmentTag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_attendeemainlayout_new, container, false);
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        txt_fullList = (TextView) rootView.findViewById(R.id.txt_fullList);
        txt_myContact = (TextView) rootView.findViewById(R.id.txt_myContact);
        fragmentTag = getTag();
        txt_fullList.setText(defaultLang.get2FullDirectory());
        txt_myContact.setText(defaultLang.get2MyContacts());
//        topAdverViewArrayList = new ArrayList<>();
//        bottomAdverViewArrayList = new ArrayList<>();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
//
        fragmentManager = getChildFragmentManager();
        fullDirectory_fragment = (AttendeeFullDirectory_Fragment) fragmentManager.findFragmentById(R.id.fragfull);
        myContact_fragment = (AttendeeMyContact_Fragment) fragmentManager.findFragmentById(R.id.fragmy);


        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        bundle = new Bundle();


        if (fragmentTag.equalsIgnoreCase("0")) {
            setButtonColor(true);
            hideShowFragment(true, true);
        } else {
            setButtonColor(false);
            sessionManager.setAttendeeMainCategoryData("");
            hideShowFragment(false, true);
        }

        txt_fullList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(true);
                hideShowFragment(true, false);
            }
        });

        txt_myContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonColor(false);
                hideShowFragment(false, false);
            }
        });

        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
            }

        }
        getAdvertiesment();

        return rootView;
    }

    private void hideShowFragment(boolean isFullList, boolean isFirstTime) {
        if (isFullList) {
            fragmentManager.beginTransaction()
                    .show(fullDirectory_fragment)
                    .commit();
            fragmentManager.beginTransaction()
                    .hide(myContact_fragment)
                    .commit();
            if (!isFirstTime)
                fullDirectory_fragment.refreshFragment();
        } else {
            fragmentManager.beginTransaction()
                    .hide(fullDirectory_fragment)
                    .commit();
            fragmentManager.beginTransaction()
                    .show(myContact_fragment)
                    .commit();
            if (!isFirstTime)
                myContact_fragment.refreshFragment();
        }

    }


    private void setButtonColor(boolean isFullList) {
        GradientDrawable drawableFulllist = new GradientDrawable();
        GradientDrawable drawableMycontact = new GradientDrawable();

        drawableFulllist.setShape(GradientDrawable.RECTANGLE);
        drawableFulllist.setCornerRadius(13.0f);

        drawableMycontact.setShape(GradientDrawable.RECTANGLE);
        drawableMycontact.setCornerRadius(13.0f);


        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {
            drawableFulllist.setStroke(2, Color.parseColor(sessionManager.getFunTopBackColor()));
            drawableMycontact.setStroke(2, Color.parseColor(sessionManager.getFunTopBackColor()));

            if (isFullList) {
                drawableFulllist.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                txt_fullList.setBackgroundDrawable(drawableFulllist);
                txt_fullList.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));

                drawableMycontact.setColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                txt_myContact.setBackgroundDrawable(drawableMycontact);
                txt_myContact.setTextColor(Color.parseColor(sessionManager.getFunTopBackColor()));


            } else {
                drawableFulllist.setColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                txt_fullList.setBackgroundDrawable(drawableFulllist);
                txt_fullList.setTextColor(Color.parseColor(sessionManager.getFunTopBackColor()));

                drawableMycontact.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                txt_myContact.setBackgroundDrawable(drawableMycontact);
                txt_myContact.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            }
        } else {

            drawableFulllist.setStroke(2, Color.parseColor(sessionManager.getTopBackColor()));
            drawableMycontact.setStroke(2, Color.parseColor(sessionManager.getTopBackColor()));

            if (isFullList) {
                drawableFulllist.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                txt_fullList.setBackgroundDrawable(drawableFulllist);
                txt_fullList.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

                drawableMycontact.setColor(Color.parseColor(sessionManager.getTopTextColor()));
                txt_myContact.setBackgroundDrawable(drawableMycontact);
                txt_myContact.setTextColor(Color.parseColor(sessionManager.getTopBackColor()));


            } else {
                drawableFulllist.setColor(Color.parseColor(sessionManager.getTopTextColor()));
                txt_fullList.setBackgroundDrawable(drawableFulllist);
                txt_fullList.setTextColor(Color.parseColor(sessionManager.getTopBackColor()));

                drawableMycontact.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                txt_myContact.setBackgroundDrawable(drawableMycontact);
                txt_myContact.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            }
        }
    }


    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 0, false, this);

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

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {

            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
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

            sessionManager.HeaderView(getContext(), "1", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
//            sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_Data, linear_content, bottomAdverViewArrayList, getActivity());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
