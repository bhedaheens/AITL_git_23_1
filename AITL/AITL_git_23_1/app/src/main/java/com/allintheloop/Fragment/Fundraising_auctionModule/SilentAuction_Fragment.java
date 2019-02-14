package com.allintheloop.Fragment.Fundraising_auctionModule;


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

import com.allintheloop.Adapter.FundraisingHome_footer_adapter;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Adapter.SilentAuctionAdapter;
import com.allintheloop.Bean.Fundraising.FundraisingHome_footer;
import com.allintheloop.Bean.Fundraising.SilentAuction;
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
public class SilentAuction_Fragment extends Fragment implements VolleyInterface {
    ViewPager footer_pager;
    ArrayList<SilentAuction> productArrayList;
    ArrayList<FundraisingHome_footer> footerArrayList;
    SilentAuctionAdapter product_adapter;
    FundraisingHome_footer_adapter footer_adapter;
    TextView txtNoDataFoud;
    LinearLayout linear_footer;
    LinearLayoutManager linearLayoutManager;
    boolean isFirstTime = false, btn_load;
    int page_count = 1, total_page;
    RecyclerView rv_viewProduct;
    SessionManager sessionManager;
    String currency_status = "", notes_status = "", cart_status = "", pro_id = "", pro_name = "", pro_desc = "", pro_thumb = "", pro_action_type = "", pro_price = "", pro_maxBid = "", product_flag_price = "", product_preview = "", str_firstName = "", str_lastName = "", str_logo = "", str_productName = "", str_amt = "", bids_donations_display = "", tag = "";
    Button btn_load_more;
    Bundle bundle;

    public SilentAuction_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_silentauction, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        sessionManager = new SessionManager(getActivity());
        productArrayList = new ArrayList<>();
        footerArrayList = new ArrayList<>();

        tag = getTag();
        Log.d("AITL Tag", tag);
        footer_pager = (ViewPager) rootView.findViewById(R.id.footer_pager);
        linear_footer = (LinearLayout) rootView.findViewById(R.id.linear_footer);
        rv_viewProduct = (RecyclerView) rootView.findViewById(R.id.rv_viewProduct);
        btn_load_more = (Button) rootView.findViewById(R.id.btn_load_more);
        txtNoDataFoud = (TextView) rootView.findViewById(R.id.txtNoDataFoud);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rv_viewProduct.setLayoutManager(linearLayoutManager);

        bundle = new Bundle();


        rv_viewProduct.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SilentAuction silentObj = product_adapter.getItem(position);

                Log.d("AITL ProductID", silentObj.getId());
                if (tag.equalsIgnoreCase("1")) {
                    sessionManager.slilentAuctionP_id = silentObj.getId();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.SilentAuctionDetail_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else if (tag.equalsIgnoreCase("2")) {
                    sessionManager.liveAuctionP_id = silentObj.getId();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.LiveAuction_Detail;
                    ((MainActivity) getActivity()).loadFragment();
                } else if (tag.equalsIgnoreCase("3")) {
                    sessionManager.OnlineShop_id = silentObj.getId();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.OnlineShop_Detail;
                    ((MainActivity) getActivity()).loadFragment();
                } else if (tag.equalsIgnoreCase("4")) {
                    sessionManager.pledge_id = silentObj.getId();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.PledgeDetailFragment;
                    ((MainActivity) getActivity()).loadFragment();
                }


            }
        }));


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

        if (GlobalData.isNetworkAvailable(getActivity())) {
//            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()),1, false, this);
//            if(sessionManager.isLogin())
//            {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()),5, false, this);
//            }
            ProductList();

        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
        return rootView;
    }


    private void ProductList() {
        isFirstTime = true;
        if (isFirstTime) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Silentauction_productListUid, Param.fundrising_Product(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), sessionManager.getUserId(), tag, page_count), 0, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Silentauction_productList, Param.fundrising_Product(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), sessionManager.getUserId(), tag, page_count), 0, false, this);
            isFirstTime = false;
        } else {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Silentauction_productListUid, Param.fundrising_Product(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), sessionManager.getUserId(), tag, page_count), 0, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Silentauction_productList, Param.fundrising_Product(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), sessionManager.getUserId(), tag, page_count), 0, false, this);
        }
    }

    private void setAppColor() {
        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {
            linear_footer.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
        } else {

            linear_footer.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
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
                        JSONObject jsonObject = mainObject.getJSONObject("data");
                        notes_status = jsonObject.getString("note_status");
                        currency_status = jsonObject.getString("currency");
                        cart_status = jsonObject.getString("cart_count");

                        JSONArray jsonArray = jsonObject.optJSONArray("events");
                        for (int e = 0; e < jsonArray.length(); e++) {
                            JSONObject jObjectevent = (JSONObject) jsonArray.get(e);
                            Log.d("AITL", "jObjectevent" + jObjectevent);
                            sessionManager.appColor(jObjectevent);
                        }

                        JSONArray jArrayFund = jsonObject.getJSONArray("fundraising_settings");
                        for (int j = 0; j < jArrayFund.length(); j++) {
                            JSONObject jObjectFund = jArrayFund.getJSONObject(j);

                            bids_donations_display = jObjectFund.getString("bids_donations_display");
                        }
                        Log.d("AITL Bid Donation Display", bids_donations_display);
                        total_page = jsonObject.getInt("total_page");

                        Log.d("AITL", "Total Page" + total_page);
                        Log.d("AITL", "Product" + jsonObject.toString());

                        JSONArray jArrayProduct = jsonObject.getJSONArray("products");

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
                                if (index.has("product_preview")) {
                                    product_preview = index.getString("product_preview");
                                } else {
                                    product_preview = "0";
                                }
                                productArrayList.add(new SilentAuction(pro_id, pro_name, pro_desc, MyUrls.Fund_Imgurl + pro_thumb, pro_action_type, pro_price, pro_maxBid, product_flag_price, tag, product_preview));
                                Log.d("AITL", " IF ProductImg" + MyUrls.Fund_Imgurl + pro_thumb);
                            }
                            product_adapter = new SilentAuctionAdapter(productArrayList, rv_viewProduct, getActivity(), linearLayoutManager, currency_status);
                            rv_viewProduct.setAdapter(product_adapter);
                            btn_load = true;
                        } else {
                            ArrayList<SilentAuction> tmp_pArrayList = new ArrayList<>();
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
                                if (index.has("product_preview")) {
                                    product_preview = index.getString("product_preview");
                                } else {
                                    product_preview = "0";
                                }
                                tmp_pArrayList.add(new SilentAuction(pro_id, pro_name, pro_desc, MyUrls.Fund_Imgurl + pro_thumb, pro_action_type, pro_price, pro_maxBid, product_flag_price, tag, product_preview));
                                Log.d("AITL", "ELSE ProductImg" + MyUrls.Fund_Imgurl + pro_thumb);
                            }
                            productArrayList.addAll(tmp_pArrayList);
                            product_adapter = new SilentAuctionAdapter(productArrayList, rv_viewProduct, getActivity(), linearLayoutManager, currency_status);
                            rv_viewProduct.setAdapter(product_adapter);
                        }

                        if (productArrayList.size() == 0) {
                            rv_viewProduct.setVisibility(View.GONE);
                            txtNoDataFoud.setVisibility(View.VISIBLE);
                        } else {
                            txtNoDataFoud.setVisibility(View.GONE);
                            rv_viewProduct.setVisibility(View.VISIBLE);
                            if (total_page > 1) {
                                if (page_count == total_page) {
                                    btn_load_more.setVisibility(View.GONE);
                                } else
                                    btn_load_more.setVisibility(View.VISIBLE);
                            } else {
                                btn_load_more.setVisibility(View.GONE);
                            }


                        }
                        JSONArray jArrayFooter = jsonObject.getJSONArray("latest_pleadge_bids");

                        for (int f = 0; f < jArrayFooter.length(); f++) {
                            JSONObject jObjectFooter = jArrayFooter.getJSONObject(f);

                            str_firstName = jObjectFooter.getString("Firstname");
                            str_lastName = jObjectFooter.getString("Lastname");
                            str_logo = jObjectFooter.getString("Logo");
                            str_amt = jObjectFooter.getString("amt");
                            str_productName = jObjectFooter.getString("product_name");
                            Log.d("AITL", "Logo" + str_logo);
                            footerArrayList.add(new FundraisingHome_footer(str_firstName, str_lastName, MyUrls.Imgurl + str_logo, str_productName, str_amt, "silent_auction"));
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
                        checkNoteStatus();
                        getActivity().invalidateOptionsMenu();
                        setAppColor();// call for set color
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void checkNoteStatus() {
        ((MainActivity) getActivity()).updateCartCount(cart_status);
    }
}
