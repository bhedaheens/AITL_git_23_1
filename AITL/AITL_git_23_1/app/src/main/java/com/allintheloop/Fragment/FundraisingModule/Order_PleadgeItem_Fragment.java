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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.FundraisingHome_footer_adapter;
import com.allintheloop.Adapter.PledgeAdapter;
import com.allintheloop.Bean.Fundraising.FundraisingHome_footer;
import com.allintheloop.Bean.Fundraising.PledgeItemList;
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
public class Order_PleadgeItem_Fragment extends Fragment implements VolleyInterface {

    TextView txt_info;
    EditText edt_search;
    ImageView img_empty;
    RecyclerView rv_viewOrderList;
    ArrayList<PledgeItemList> pledgeItemLists;
    ArrayList<FundraisingHome_footer> footerArrayList;
    FundraisingHome_footer_adapter footer_adapter;
    LinearLayout linear_footer;
    public static ViewPager footer_pager;
    PledgeAdapter pledgeAdapter;
    SessionManager sessionManager;
    String str_firstName, str_lastName, str_logo, str_productName, str_amt, bids_donations_display;
    public static String str_currencyStatus, note_status, cart_count;


    public Order_PleadgeItem_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_order_pleadgeitem, container, false);

        txt_info = (TextView) rootView.findViewById(R.id.txt_info);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        img_empty = (ImageView) rootView.findViewById(R.id.img_empty);
        rv_viewOrderList = (RecyclerView) rootView.findViewById(R.id.rv_viewOrderList);
        sessionManager = new SessionManager(getActivity());
        pledgeItemLists = new ArrayList<>();
        footerArrayList = new ArrayList<>();
        linear_footer = (LinearLayout) rootView.findViewById(R.id.linear_footer);
        footer_pager = (ViewPager) rootView.findViewById(R.id.footer_pager);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pledgeItemLists.size() > 0) {
                    pledgeAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pledgeItemLists.size() > 0) {
                    pledgeAdapter.getFilter().filter(s);
                }
            }
        });


        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getorderList, Param.getOrder(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId()), 0, true, this);
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
                    JSONObject jsonMain = new JSONObject(volleyResponse.output);
                    if (jsonMain.getString("success").equalsIgnoreCase("true")) {

                        str_currencyStatus = jsonMain.getString("currency");
                        cart_count = jsonMain.getString("cart_count");
                        note_status = jsonMain.getString("note_status");

                        JSONArray jsonArray = jsonMain.getJSONArray("pledges");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonorder = jsonArray.getJSONObject(i);
                            pledgeItemLists.add(new PledgeItemList(jsonorder.getString("name"), jsonorder.getString("thumb"), jsonorder.getString("pledge_amt"), jsonorder.getString("created_date")));
                        }

                        JSONArray jArraylatestbid = jsonMain.getJSONArray("latest_pleadge_bids");
                        for (int j = 0; j < jArraylatestbid.length(); j++) {
                            JSONObject jObjectFooter = jArraylatestbid.getJSONObject(j);
                            str_firstName = jObjectFooter.getString("Firstname");
                            str_lastName = jObjectFooter.getString("Lastname");
                            str_logo = jObjectFooter.getString("Logo");
                            str_amt = jObjectFooter.getString("amt");
                            str_productName = jObjectFooter.getString("product_name");
                            Log.d("AITL", "Logo" + str_logo);
                            footerArrayList.add(new FundraisingHome_footer(str_firstName, str_lastName, MyUrls.Imgurl + str_logo, str_productName, str_amt, "pledge_ItemList"));
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

                        if (pledgeItemLists.size() == 0) {
                            edt_search.setVisibility(View.GONE);
                            rv_viewOrderList.setVisibility(View.GONE);
                            txt_info.setVisibility(View.VISIBLE);
                            img_empty.setVisibility(View.VISIBLE);
                        } else {
                            edt_search.setVisibility(View.VISIBLE);
                            rv_viewOrderList.setVisibility(View.VISIBLE);
                            txt_info.setVisibility(View.GONE);
                            img_empty.setVisibility(View.GONE);
                            pledgeAdapter = new PledgeAdapter(pledgeItemLists, getActivity());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            rv_viewOrderList.setLayoutManager(mLayoutManager);
                            rv_viewOrderList.setItemAnimator(new DefaultItemAnimator());
                            rv_viewOrderList.setAdapter(pledgeAdapter);
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
