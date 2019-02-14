package com.allintheloop.Fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Adapter.TwitterHashTagListAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.TwitterHashTagList;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterHashTagListingFragment extends Fragment implements VolleyInterface {


    RecyclerView rv_viewtweet;
    ArrayList<TwitterHashTagList> feedArrayList;
    TwitterHashTagListAdapter feedAdapter;
    SessionManager sessionManager;
    CardView card_no_hashtag;
    TextView txt_nologin;

    RelativeLayout MainLayout, relativeLayout_static;
    LinearLayout linear_content;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;

    public TwitterHashTagListingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_twitter_hash_tag_listing, container, false);
        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).setTitle("");
        feedArrayList = new ArrayList<>();
        rv_viewtweet = (RecyclerView) rootView.findViewById(R.id.rv_viewtweet);
        relativeLayout_static = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_static);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        card_no_hashtag = (CardView) rootView.findViewById(R.id.card_no_hashtag);
        txt_nologin = (TextView) rootView.findViewById(R.id.txt_nologin);
        sessionManager = new SessionManager(getActivity());
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();


        getHashTagList();

        rv_viewtweet.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                TwitterHashTagList tagList = feedArrayList.get(position);

                sessionManager.twitterHashTagName = tagList.getName();
                sessionManager.strModuleId = tagList.getName();
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.TwitterDetailFragment;
                ((MainActivity) getActivity()).loadFragment();
                //startActivity(new Intent(getActivity(), Map_Detail_Activity.class).putExtra("map_id",mapObj.getId()));
            }
        }));
//

        getAdvertiesment();

        return rootView;
    }

    private void getHashTagList() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getTwitterHashTagList, Param.getTwitterHashTagList(sessionManager.getEventId(), sessionManager.getEventType()), 0, false, this);
        } else {
            ToastC.show(getActivity(), getResources().getString(R.string.noInernet));
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

            if (jsonObjectAdavertiesment.getString("show_sticky").equalsIgnoreCase("1")) {
                sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_static, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_static, linear_content, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(getContext(), "1", MainLayout, relativeLayout_static, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "1", MainLayout, relativeLayout_static, linear_content, topAdverViewArrayList, getActivity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 5, false, this);

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
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        feedArrayList.clear();
                        if (jsonObject.has("data")) {
                            JSONObject jsondata = jsonObject.getJSONObject("data");
                            if (jsondata.length() != 0) {
                                JSONArray jsonArray = jsondata.getJSONArray("hashtag");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    feedArrayList.add(new TwitterHashTagList(jsonArray.get(i).toString()));
                                }
                            }

                            if (feedArrayList.size() == 0) {

                                txt_nologin.setText(jsonObject.getString("message"));
                                card_no_hashtag.setVisibility(View.VISIBLE);
                                rv_viewtweet.setVisibility(View.GONE);

                            } else {

                                card_no_hashtag.setVisibility(View.GONE);
                                rv_viewtweet.setVisibility(View.VISIBLE);
                                feedAdapter = new TwitterHashTagListAdapter(getContext(), feedArrayList);
                                rv_viewtweet.setItemAnimator(new DefaultItemAnimator());
                                rv_viewtweet.setLayoutManager(new LinearLayoutManager(getContext()));
                                rv_viewtweet.setAdapter(feedAdapter);
                            }
                        } else {
                            txt_nologin.setText(jsonObject.getString("message"));
                            card_no_hashtag.setVisibility(View.VISIBLE);
                            rv_viewtweet.setVisibility(View.GONE);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
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
}
