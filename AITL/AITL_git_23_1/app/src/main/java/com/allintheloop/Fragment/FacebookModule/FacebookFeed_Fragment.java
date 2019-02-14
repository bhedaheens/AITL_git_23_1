package com.allintheloop.Fragment.FacebookModule;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintheloop.Adapter.Adapter_facebookFeedFragment;
import com.allintheloop.Bean.FacebookFeedData;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacebookFeed_Fragment extends Fragment implements VolleyInterface {

    CardView card_noData;
    TextView txt_noData;
    RecyclerView rv_viewfacebook;
    LinearLayout layout_data;
    //    NestedScrollView scrollView;
    boolean isLoading;
    Handler handler;
    ProgressBar progressBar1;
    WrapContentLinearLayoutManager linearLayoutManager;
    int total_pages, page_count = 1;
    SessionManager sessionManager;

    public ArrayList<FacebookFeedData> feedDataArrayList;
    public ArrayList<String> img_array;
    Adapter_facebookFeedFragment adapter_facebookFeedFragment;
    String id = "", name = "", message = "", picture = "", link = "", bottom_name = "", icon = "", caption = "", description = "", created_time = "", total_like = "", total_comment = "";
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data;

    public FacebookFeed_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).setTitle("");
        View rootView = inflater.inflate(R.layout.fragment_facebook_feed, container, false);
        card_noData = (CardView) rootView.findViewById(R.id.card_noData);
        txt_noData = (TextView) rootView.findViewById(R.id.txt_noData);
        rv_viewfacebook = (RecyclerView) rootView.findViewById(R.id.rv_viewfacebook);
        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
//        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);
        layout_data = (LinearLayout) rootView.findViewById(R.id.layout_data);
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        sessionManager = new SessionManager(getActivity());
        handler = new Handler();
        linearLayoutManager = new WrapContentLinearLayoutManager(getActivity());
        rv_viewfacebook.setLayoutManager(linearLayoutManager);
        feedDataArrayList = new ArrayList<>();
        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            if (GlobalData.isNetworkAvailable(getActivity())) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getFacebookFeed, Param.getFacebookFeed(sessionManager.getEventId(), page_count), 0, false, this);
            } else {
                ToastC.show(getActivity(), getString(R.string.noInernet));
            }
        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getFacebookFeed, Param.getFacebookFeed(sessionManager.getEventId(), page_count), 0, false, this);
                } else {
                    ToastC.show(getActivity(), getString(R.string.noInernet));
                }
            }

        }

        return rootView;

    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 5, false, this);
        }
    }

    private void btnLoadviewFeedApi() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getFacebookFeed, Param.getFacebookFeed(sessionManager.getEventId(), page_count), 1, false, this);

    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        if (sessionManager.isLogin()) {
                            pagewiseClick();
                        }
                        total_pages = jsonObject.getInt("total_page");
                        feedDataArrayList.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");


                        loadData(jsonArray);

                    }
                } catch (Exception e) {

                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        total_pages = jsonObject.getInt("total_page");
                        Log.d("AITL TOTAL PAGES", "PAGES" + total_pages);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        loadData(jsonArray);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private void loadData(JSONArray jsonArray) {
        try {
            if (!isLoading) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                    id = jsonObjectData.getString("id");
                    if (jsonObjectData.has("message")) {
                        message = jsonObjectData.getString("message");
                    } else {
                        message = "";
                    }
                    if (jsonObjectData.has("full_picture")) {
                        picture = jsonObjectData.getString("full_picture");
                        if (jsonObjectData.has("attachments")) {
                            JSONObject jsonObjectattachment = jsonObjectData.getJSONObject("attachments");
                            if (jsonObjectattachment.has("media")) {
                                img_array = new ArrayList<>();
                                JSONArray jsonArrayMedia = jsonObjectattachment.getJSONArray("media");

                                for (int j = 0; j < jsonArrayMedia.length(); j++) {
                                    JSONObject jsonObjectMedia = jsonArrayMedia.getJSONObject(j);
                                    img_array.add(jsonObjectMedia.getString("src"));
                                }
                            }
                        }
                    } else {
                        picture = "";
                    }
                    if (jsonObjectData.has("name")) {
                        bottom_name = jsonObjectData.getString("name");
                    } else {
                        bottom_name = "";
                    }
                    if (jsonObjectData.has("caption")) {
                        caption = jsonObjectData.getString("caption");
                    } else {
                        caption = "";
                    }
                    if (jsonObjectData.has("description")) {
                        description = jsonObjectData.getString("description");
                    } else {
                        description = "";
                    }
                    if (jsonObjectData.has("logo")) {
                        icon = jsonObjectData.getString("logo");
                    } else {
                        icon = "";
                    }

                    if (jsonObjectData.has("created_time")) {
                        created_time = jsonObjectData.getString("created_time");

                    } else {
                        created_time = "";
                    }

                    if (jsonObjectData.has("link")) {
                        link = jsonObjectData.getString("link");

                    } else {
                        link = "";
                    }

                    if (jsonObjectData.has("likes")) {
                        JSONObject jsonObjectLikes = jsonObjectData.getJSONObject("likes");
                        JSONArray jsonArrayLike = jsonObjectLikes.getJSONArray("data");
                        if (jsonArrayLike.length() > 0) {
                            total_like = String.valueOf(jsonArrayLike.length());

                        }
                    } else {
                        total_like = "";
                    }


                    if (jsonObjectData.has("comments")) {
                        JSONObject jsonObjectComments = jsonObjectData.getJSONObject("comments");
                        JSONArray jsonArrayComments = jsonObjectComments.getJSONArray("data");
                        if (jsonArrayComments.length() > 0) {
                            total_comment = String.valueOf(jsonArrayComments.length());
                        }
                    } else {
                        total_comment = "";
                    }
                    if (jsonObjectData.has("from")) {
                        JSONObject jsonObjectFrom = jsonObjectData.getJSONObject("from");

                        if (jsonObjectFrom.has("name")) {
                            name = jsonObjectFrom.getString("name");
                        }
                    } else {
                        name = "";
                    }
                    feedDataArrayList.add(new FacebookFeedData(id, name, created_time, message, picture, link, bottom_name, description, caption, icon, total_like, total_comment, img_array));
                }
            } else {
                ArrayList<FacebookFeedData> tmp_facebookArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                    id = jsonObjectData.getString("id");
                    if (jsonObjectData.has("message")) {
                        message = jsonObjectData.getString("message");
                    } else {
                        message = "";
                    }
                    if (jsonObjectData.has("full_picture")) {
                        picture = jsonObjectData.getString("full_picture");
                        if (jsonObjectData.has("attachments")) {
                            JSONObject jsonObjectattachment = jsonObjectData.getJSONObject("attachments");
                            if (jsonObjectattachment.has("media")) {
                                img_array = new ArrayList<>();
                                JSONArray jsonArrayMedia = jsonObjectattachment.getJSONArray("media");

                                for (int j = 0; j < jsonArrayMedia.length(); j++) {
                                    JSONObject jsonObjectMedia = jsonArrayMedia.getJSONObject(j);
                                    img_array.add(jsonObjectMedia.getString("src"));
                                }

                                Log.d("AITL MEDIA", "SIZE " + jsonArrayMedia.length());
                            }
//                            img_array.clear();
                        }
                    } else {
                        picture = "";
                    }
                    if (jsonObjectData.has("name")) {
                        bottom_name = jsonObjectData.getString("name");
                    } else {
                        bottom_name = "";
                    }
                    if (jsonObjectData.has("caption")) {
                        caption = jsonObjectData.getString("caption");
                    } else {
                        caption = "";
                    }
                    if (jsonObjectData.has("description")) {
                        description = jsonObjectData.getString("description");
                    } else {
                        description = "";
                    }
                    if (jsonObjectData.has("logo")) {
                        icon = jsonObjectData.getString("logo");
                    } else {
                        icon = "";
                    }

                    if (jsonObjectData.has("created_time")) {
                        created_time = jsonObjectData.getString("created_time");

                    } else {
                        created_time = "";
                    }

                    if (jsonObjectData.has("link")) {
                        link = jsonObjectData.getString("link");

                    } else {
                        link = "";
                    }

                    if (jsonObjectData.has("likes")) {
                        JSONObject jsonObjectLikes = jsonObjectData.getJSONObject("likes");
                        JSONArray jsonArrayLike = jsonObjectLikes.getJSONArray("data");
                        if (jsonArrayLike.length() > 0) {
                            total_like = String.valueOf(jsonArrayLike.length());

                        }
                    } else {
                        total_like = "";
                    }


                    if (jsonObjectData.has("comments")) {
                        JSONObject jsonObjectComments = jsonObjectData.getJSONObject("comments");
                        JSONArray jsonArrayComments = jsonObjectComments.getJSONArray("data");
                        if (jsonArrayComments.length() > 0) {
                            total_comment = String.valueOf(jsonArrayComments.length());
                        }
                    } else {
                        total_comment = "";
                    }
                    if (jsonObjectData.has("from")) {
                        JSONObject jsonObjectFrom = jsonObjectData.getJSONObject("from");

                        if (jsonObjectFrom.has("name")) {
                            name = jsonObjectFrom.getString("name");
                        }
                    } else {
                        name = "";
                    }

                    tmp_facebookArrayList.add(new FacebookFeedData(id, name, created_time, message, picture, link, bottom_name, description, caption, icon, total_like, total_comment, img_array));
                }
                feedDataArrayList.addAll(tmp_facebookArrayList);
            }
            set_recycler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void set_recycler() {
        try {
            if (!isLoading) {

                Log.d("AITL ARRAYSIZE", "FALSE" + feedDataArrayList.size());

                if (feedDataArrayList.size() == 0) {

                    rv_viewfacebook.setVisibility(View.GONE);
                    card_noData.setVisibility(View.VISIBLE);
                } else {
                    rv_viewfacebook.setVisibility(View.VISIBLE);
                    card_noData.setVisibility(View.GONE);
                    adapter_facebookFeedFragment = new Adapter_facebookFeedFragment(feedDataArrayList, rv_viewfacebook, getActivity(), linearLayoutManager, getActivity());
                    rv_viewfacebook.setAdapter(adapter_facebookFeedFragment);

                }
            } else {
                Log.d("AITL ARRAYSIZE", "TRUE" + feedDataArrayList.size());

                adapter_facebookFeedFragment.notifyDataSetChanged();
                adapter_facebookFeedFragment.setLoaded();
                isLoading = false;
            }

            if (feedDataArrayList.size() != 0)

                adapter_facebookFeedFragment.setOnLoadMoreListener(new OnLoadMoreListener() {

                    @Override
                    public void onLoadMore() {
                        Log.d("AITL BEFORE TOTAL PAGE", "" + total_pages);
                        if (total_pages != -1) {
                            page_count++;
                        }
                        Log.d("AITL AFTER TOTAL PAGE", "" + total_pages);
                        Log.d("AITL PAGE COUNT", "" + page_count);

                        if (total_pages != -1) {
                            Log.d("AITL PAGE INSIDE IF COUNT", "" + page_count);
                            //     adapter_facebookFeedFragment.addFooter();
                            progressBar1.setVisibility(View.VISIBLE);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                    adapter_facebookFeedFragment.removeFooter();
                                    progressBar1.setVisibility(View.GONE);
                                    isLoading = true;
                                    try {

                                        if (GlobalData.isNetworkAvailable(getActivity()))
                                            btnLoadviewFeedApi();
                                        else
                                            Toast.makeText(getActivity(), getString(R.string.noInernet), Toast.LENGTH_SHORT).show();
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 2000);
                        }

                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}