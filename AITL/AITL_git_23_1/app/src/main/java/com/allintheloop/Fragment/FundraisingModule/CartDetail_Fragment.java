package com.allintheloop.Fragment.FundraisingModule;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.CartdetailAdapter;
import com.allintheloop.Adapter.FundraisingHome_footer_adapter;
import com.allintheloop.Bean.Fundraising.CartDetail;
import com.allintheloop.Bean.Fundraising.FundraisingHome_footer;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartDetail_Fragment extends Fragment implements VolleyInterface {

    public static RecyclerView rv_viewCart;
    ArrayList<CartDetail> cartArrayList;
    ArrayList<FundraisingHome_footer> footerArrayList;
    FundraisingHome_footer_adapter footer_adapter;
    LinearLayout linear_footer, linear_total;
    ViewPager footer_pager;
    CartdetailAdapter cartdetailAdapter;
    SessionManager sessionManager;
    public static String str_currency;
    String str_cart_count = "";
    String bids_donations_display;
    String id, product_id, name, price, qty, auction_type, subtotal, str_grandtotal;
    String str_firstName, str_lastName, str_logo, str_productName, str_amt;
    TextView grand_total, txt_cartempty;
    Button btn_checkout, btn_otherproduct;
    public static Button btn_updatecart;

    public CartDetail_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cartdetail, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        Log.e("AITL", "cart details");
        sessionManager.strModuleId = "My Cart";
        rv_viewCart = (RecyclerView) rootView.findViewById(R.id.rv_viewCart);
        btn_checkout = (Button) rootView.findViewById(R.id.btn_checkout);
        grand_total = (TextView) rootView.findViewById(R.id.grand_total);
        footer_pager = (ViewPager) rootView.findViewById(R.id.footer_pager);

        linear_footer = (LinearLayout) rootView.findViewById(R.id.linear_footer);
        linear_total = (LinearLayout) rootView.findViewById(R.id.linear_total);
        sessionManager = new SessionManager(getActivity());
        footerArrayList = new ArrayList<>();
        cartArrayList = new ArrayList<>();

        txt_cartempty = (TextView) rootView.findViewById(R.id.txt_cartempty);
        btn_otherproduct = (Button) rootView.findViewById(R.id.btn_otherproduct);
        btn_updatecart = (Button) rootView.findViewById(R.id.btn_updatecart);


        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.CheckOut_Fragment;
                ((MainActivity) getActivity()).loadFragment();
            }
        });


        btn_otherproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                    GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                    ((MainActivity) getActivity()).loadFragment();
                }

            }
        });
        if (GlobalData.isNetworkAvailable(getActivity())) {
//            if(sessionManager.isLogin())
//            {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()), 1, false, this);
//            }

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.CartDetail, Param.cart_detail(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId()), 0, false, this);
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

                        JSONObject jdata = jsonObject.getJSONObject("data");

                        str_currency = jdata.getString("currency");
                        str_cart_count = jdata.getString("cart_count");
//                        str_notestatus = jdata.getString("note_status");
                        str_grandtotal = jdata.getString("grand_total");

                        JSONArray jsonArray = jdata.optJSONArray("events");
                        for (int e = 0; e < jsonArray.length(); e++) {
                            JSONObject jObjectevent = (JSONObject) jsonArray.get(e);
                            Log.d("AITL", "jObjectevent" + jObjectevent);
                            sessionManager.appColor(jObjectevent);
                        }

                        JSONArray jArrayFund = jdata.getJSONArray("fundraising_settings");
                        for (int j = 0; j < jArrayFund.length(); j++) {
                            JSONObject jObjectFund = jArrayFund.getJSONObject(j);

                            bids_donations_display = jObjectFund.getString("bids_donations_display");
                        }


                        JSONArray jArrayProduct = jdata.getJSONArray("products");
                        Log.d("AITL Product", jArrayProduct.toString());
                        for (int i = 0; i < jArrayProduct.length(); i++) {
                            JSONObject index = jArrayProduct.getJSONObject(i);
                            id = index.getString("Id");
                            product_id = index.getString("product_id");
                            name = index.getString("name");
                            price = index.getString("price");
                            qty = index.getString("quantity");
                            auction_type = index.getString("auctionType");
                            subtotal = index.getString("subtotal");

                            cartArrayList.add(new CartDetail(id, product_id, name, price, qty, auction_type, subtotal));
                        }

                        if (cartArrayList.size() == 0) {
                            txt_cartempty.setVisibility(View.VISIBLE);
                            btn_otherproduct.setVisibility(View.VISIBLE);
                            rv_viewCart.setVisibility(View.GONE);
                            btn_updatecart.setVisibility(View.GONE);
                            linear_total.setVisibility(View.GONE);

                        } else {
                            txt_cartempty.setVisibility(View.GONE);
                            btn_otherproduct.setVisibility(View.GONE);
                            rv_viewCart.setVisibility(View.VISIBLE);
                            btn_updatecart.setVisibility(View.VISIBLE);
                            linear_total.setVisibility(View.VISIBLE);

                            cartdetailAdapter = new CartdetailAdapter(cartArrayList, getActivity());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            rv_viewCart.setLayoutManager(mLayoutManager);
                            rv_viewCart.setAdapter(cartdetailAdapter);
                        }
                        JSONArray jArrayFooter = jdata.getJSONArray("latest_pleadge_bids");
                        for (int f = 0; f < jArrayFooter.length(); f++) {
                            JSONObject jObjectFooter = jArrayFooter.getJSONObject(f);

                            str_firstName = jObjectFooter.getString("Firstname");
                            str_lastName = jObjectFooter.getString("Lastname");
                            str_logo = jObjectFooter.getString("Logo");
                            str_amt = jObjectFooter.getString("amt");
                            str_productName = jObjectFooter.getString("product_name");
                            Log.d("AITL", "Logo" + str_logo);
                            footerArrayList.add(new FundraisingHome_footer(str_firstName, str_lastName, MyUrls.Imgurl + str_logo, str_productName, str_amt, "cart_detail"));
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }


    private void loadData() {
        if (str_currency.equalsIgnoreCase("euro")) {
            grand_total.setText("GRAND TOTAL :" + " " + getActivity().getResources().getString(R.string.euro) + str_grandtotal);
        } else if (str_currency.equalsIgnoreCase("gbp")) {
            grand_total.setText("GRAND TOTAL :" + " " + getActivity().getResources().getString(R.string.pound_sign) + str_grandtotal);
        } else if (str_currency.equalsIgnoreCase("usd") || str_currency.equalsIgnoreCase("aud")) {
            grand_total.setText("GRAND TOTAL :" + " " + getActivity().getResources().getString(R.string.dollor) + str_grandtotal);
        }
    }

    private void checkNoteStatus() {
        ((MainActivity) getActivity()).updateCartCount(str_cart_count);
    }

    private void setAppColor() {
        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {
            linear_footer.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
        } else {
            linear_footer.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
        }

    }
}
