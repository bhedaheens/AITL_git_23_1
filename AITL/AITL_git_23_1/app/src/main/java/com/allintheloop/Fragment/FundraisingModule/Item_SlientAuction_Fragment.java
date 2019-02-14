package com.allintheloop.Fragment.FundraisingModule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.FundraisingHome_footer_adapter;
import com.allintheloop.Adapter.ItemAdapter;
import com.allintheloop.Bean.Fundraising.FundraisingHome_footer;
import com.allintheloop.Bean.ItemSilentBuyNowList;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
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
public class Item_SlientAuction_Fragment extends Fragment implements VolleyInterface {


    public static ArrayList<ItemSilentBuyNowList> itemSilentBuyNowLists;
    ArrayList<FundraisingHome_footer> footerArrayList;
    FundraisingHome_footer_adapter footer_adapter;
    LinearLayout linear_footer;
    public static ViewPager footer_pager;
    TextView txt_info;
    EditText edt_search;
    ItemAdapter itemAdapter;
    public static RecyclerView rv_viewSilentList;

    SessionManager sessionManager;
    String str_firstName, str_lastName, str_logo, str_productName, str_amt;
    public static String str_currencyStatus, note_status, cart_count;


    public Item_SlientAuction_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_item__slientauction, container, false);
        itemSilentBuyNowLists = new ArrayList<>();
        footerArrayList = new ArrayList<>();

        sessionManager = new SessionManager(getActivity());

        footer_pager = (ViewPager) rootView.findViewById(R.id.footer_pager);
        linear_footer = (LinearLayout) rootView.findViewById(R.id.linear_footer);
        txt_info = (TextView) rootView.findViewById(R.id.txt_info);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        rv_viewSilentList = (RecyclerView) rootView.findViewById(R.id.rv_viewSilentList);
        rv_viewSilentList.setNestedScrollingEnabled(false);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (itemSilentBuyNowLists.size() > 0) {
                    itemAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (itemSilentBuyNowLists.size() > 0) {
                    itemAdapter.getFilter().filter(s);
                }
            }
        });

        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getItemList, Param.getItem(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), sessionManager.getUserId()), 0, false, this);
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }

        return rootView;
    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsondata = jsonObject.getJSONObject("data");

                        str_currencyStatus = jsondata.getString("currency");
                        note_status = jsondata.getString("note_status");
                        cart_count = jsondata.getString("cart_count");


                        JSONArray jArraySilent = jsondata.getJSONArray("silent_auction_products");
                        for (int i = 0; i < jArraySilent.length(); i++) {
                            JSONObject jsonSilent = jArraySilent.getJSONObject(i);
                            itemSilentBuyNowLists.add(new ItemSilentBuyNowList(jsonSilent.getString("product_id"),
                                    jsonSilent.getString("userid"),
                                    jsonSilent.getString("name"),
                                    jsonSilent.getString("startPrice"),
                                    jsonSilent.getString("reserveBid"),
                                    jsonSilent.getString("approved_status"),
                                    jsonSilent.getString("thumb"),
                                    "silent", jsonSilent.getString("auctionType")));
                        }

                        if (itemSilentBuyNowLists.size() == 0) {
                            edt_search.setVisibility(View.GONE);
                            rv_viewSilentList.setVisibility(View.GONE);
                            txt_info.setVisibility(View.VISIBLE);
                        } else {
                            edt_search.setVisibility(View.VISIBLE);
                            rv_viewSilentList.setVisibility(View.VISIBLE);
                            txt_info.setVisibility(View.GONE);

                            itemAdapter = new ItemAdapter(itemSilentBuyNowLists, getActivity());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            rv_viewSilentList.setLayoutManager(mLayoutManager);
                            rv_viewSilentList.setItemAnimator(new DefaultItemAnimator());
                            rv_viewSilentList.setAdapter(itemAdapter);
                        }


                        JSONArray jArraylatestbid = jsondata.getJSONArray("latest_pleadge_bids");
                        for (int j = 0; j < jArraylatestbid.length(); j++) {
                            JSONObject jObjectFooter = jArraylatestbid.getJSONObject(j);
                            str_firstName = jObjectFooter.getString("Firstname");
                            str_lastName = jObjectFooter.getString("Lastname");
                            str_logo = jObjectFooter.getString("Logo");
                            str_amt = jObjectFooter.getString("amt");
                            str_productName = jObjectFooter.getString("product_name");
                            Log.d("AITL", "Logo" + str_logo);
                            footerArrayList.add(new FundraisingHome_footer(str_firstName, str_lastName, MyUrls.Imgurl + str_logo, str_productName, str_amt, "item_silentAuction"));
                        }

                        if (footerArrayList.size() == 0) {
                            footer_pager.setVisibility(View.GONE);
                            linear_footer.setVisibility(View.GONE);
                        } else {
                            linear_footer.setVisibility(View.VISIBLE);
                            footer_pager.setVisibility(View.VISIBLE);
                            footer_adapter = new FundraisingHome_footer_adapter(getActivity(), footerArrayList, str_currencyStatus);
                            footer_pager.setAdapter(footer_adapter);

                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
