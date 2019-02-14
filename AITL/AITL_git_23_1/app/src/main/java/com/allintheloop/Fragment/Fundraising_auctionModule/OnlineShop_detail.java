package com.allintheloop.Fragment.Fundraising_auctionModule;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineShop_detail extends Fragment implements VolleyInterface {

    ArrayList<FundraisingHome_footer> footerArrayList;
    ArrayList<Exhibitor_DetailImage> viewpager_arraylist;
    ArrayList<String> productIdarrray;
    FundraisingHome_footer_adapter footer_adapter;
    Exhibitor_ImageAdapter adapter;
    LinearLayout linear_enterbid, linear_desc, linear_footer, linear_nextPrivious, linear_btnDonate;
    public static ViewPager online_viewPager, footer_pager;
    //    CirclePageIndicator indicator;
    ViewPagerIndicator pageIndicator;
    public static FrameLayout frame_viewpager;
    EditText edt_bid;
    Button btn_buyNow, btn_previous, btn_next;
    TextView txt_productName, txt_startPrice, txt_warning, desc_productName, txt_total, txt_quntity;
    WebView webViewContent;
    UidCommonKeyClass uidCommonKeyClass;

    String str_id, str_name, str_shortdesc, str_desc, str_price, str_qty;
    String strReq_qty;
    ImageView btn_minus, btn_plus;
    public static String str_currency, str_notestatus;
    String str_cart_count = "";
    String str_firstName, str_lastName, str_logo, str_productName, str_amt, bids_donations_display;

    SessionManager sessionManager;
    int index = 0;
    boolean isFirst;
    Bundle bundle;

    int count_value = 0;
    int txt_value = 0;
    int fix_value;
    int total;
    int quntity;
    String str_qtyValue;
    CardView card_noproductDetail;
    LinearLayout layout_data;
    String product_preview = "";

    public OnlineShop_detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_onlineshop_detail, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        footerArrayList = new ArrayList<>();
        productIdarrray = new ArrayList<>();
        bundle = new Bundle();
        sessionManager = new SessionManager(getActivity());
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();
        linear_desc = (LinearLayout) rootView.findViewById(R.id.linear_desc);
        linear_enterbid = (LinearLayout) rootView.findViewById(R.id.linear_enterbid);
        linear_footer = (LinearLayout) rootView.findViewById(R.id.linear_footer);
        linear_nextPrivious = (LinearLayout) rootView.findViewById(R.id.linear_nextPrivious);
        linear_btnDonate = (LinearLayout) rootView.findViewById(R.id.linear_btnDonate);
        layout_data = (LinearLayout) rootView.findViewById(R.id.layout_data);
        frame_viewpager = (FrameLayout) rootView.findViewById(R.id.frame_viewpager);

        edt_bid = (EditText) rootView.findViewById(R.id.edt_bid);
        btn_buyNow = (Button) rootView.findViewById(R.id.btn_buyNow);
        btn_previous = (Button) rootView.findViewById(R.id.btn_previous);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        txt_productName = (TextView) rootView.findViewById(R.id.txt_productName);
        txt_startPrice = (TextView) rootView.findViewById(R.id.txt_startPrice);
        desc_productName = (TextView) rootView.findViewById(R.id.desc_productName);
        txt_warning = (TextView) rootView.findViewById(R.id.txt_warning);
        txt_quntity = (TextView) rootView.findViewById(R.id.txt_quntity);
        txt_total = (TextView) rootView.findViewById(R.id.txt_total);
        webViewContent = (WebView) rootView.findViewById(R.id.webViewContent);
        webViewContent.getSettings().setJavaScriptEnabled(true);
        webViewContent.getSettings().setAllowContentAccess(true);
        webViewContent.setVerticalScrollBarEnabled(true);
        webViewContent.getSettings().setDefaultTextEncodingName("utf-8");
        webViewContent.setHorizontalScrollBarEnabled(true);

        btn_minus = (ImageView) rootView.findViewById(R.id.btn_minus);
        btn_plus = (ImageView) rootView.findViewById(R.id.btn_plus);
        card_noproductDetail = (CardView) rootView.findViewById(R.id.card_noproductDetail);
        online_viewPager = (ViewPager) rootView.findViewById(R.id.online_viewPager);
        footer_pager = (ViewPager) rootView.findViewById(R.id.footer_pager);
        pageIndicator = (ViewPagerIndicator) rootView.findViewById(R.id.pageIndicator);
        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {
            Log.d("AITL", "Fundraising Color");
            btn_next.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            btn_previous.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            btn_next.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_previous.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        } else {
            Log.d("AITL", "EvenApp Color");
            btn_next.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            btn_previous.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            btn_next.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_previous.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        }


        if (sessionManager.isLogin()) {
            if (GlobalData.checkForUIDVersion()) {
                if (uidCommonKeyClass.getIsOrganizerUser().equalsIgnoreCase("1")) {
                    linear_enterbid.setVisibility(View.GONE);
                    linear_btnDonate.setVisibility(View.GONE);

                } else {
                    linear_enterbid.setVisibility(View.VISIBLE);
                    linear_btnDonate.setVisibility(View.VISIBLE);

                }
            } else {
                if (sessionManager.getRolId().equalsIgnoreCase("3")) { //changes applied
                    linear_enterbid.setVisibility(View.GONE);
                    linear_btnDonate.setVisibility(View.GONE);

                } else {
                    linear_enterbid.setVisibility(View.VISIBLE);
                    linear_btnDonate.setVisibility(View.VISIBLE);

                }
            }

        } else {
            linear_enterbid.setVisibility(View.GONE);
            linear_btnDonate.setVisibility(View.GONE);

        }

        edt_bid.setText("1");
        edt_bid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    txt_value = Integer.parseInt(s.toString());
                    if (s.equals("")) {
                        total = 0;
                        txt_total.setVisibility(View.GONE);
                    } else if (txt_value <= 0) {
                        txt_total.setVisibility(View.GONE);
                        txt_warning.setText("You have entered an invalid amount");
                        txt_warning.setVisibility(View.VISIBLE);
                        btn_minus.setEnabled(false);
                    } else if (txt_value > quntity) {
                        btn_plus.setEnabled(false);
                        txt_warning.setVisibility(View.VISIBLE);
                        txt_warning.setText(" You exceeded the available amount");

                        txt_total.setVisibility(View.GONE);
                    } else {
                        total = fix_value * txt_value;
                        btn_minus.setEnabled(true);
                        btn_plus.setEnabled(true);
                        txt_warning.setVisibility(View.GONE);
                        txt_total.setVisibility(View.VISIBLE);


                        txt_total.setText("Total Price :" + total);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        btn_buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strReq_qty = edt_bid.getText().toString();
                if (strReq_qty.trim().length() > 0) {
                    if (Integer.parseInt(strReq_qty.toString()) <= 0) {
                        ToastC.show(getActivity(), "Please Enter Quantity");
                    } else {
                        if (GlobalData.isNetworkAvailable(getActivity())) {
                            addTocart();
                        } else {
                            ToastC.show(getActivity(), "No Internet Connection");
                        }
                    }
                } else {
                    ToastC.show(getActivity(), "Please Enter Quantity");
                }
            }
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    str_qtyValue = edt_bid.getText().toString();
                    txt_value = Integer.parseInt(str_qtyValue);
                    count_value = txt_value;
                    count_value--;
                    if (count_value <= 0) {
                        txt_total.setVisibility(View.GONE);
                        txt_warning.setVisibility(View.VISIBLE);
                        txt_warning.setText("You have entered an invalid amount");
                        count_value = 0;
                        edt_bid.setText("" + count_value);
                        btn_minus.setEnabled(false);
                    } else {
                        txt_total.setVisibility(View.VISIBLE);
                        txt_warning.setVisibility(View.GONE);
                        btn_plus.setEnabled(true);
                    }

                    Log.d("AITL CountValue", "" + count_value);
                    edt_bid.setText("" + count_value);
                    total = fix_value * count_value;
                    if (str_currency.equalsIgnoreCase("euro")) {
                        txt_total.setText("Total Price :" + getActivity().getResources().getString(R.string.euro) + total);

                    } else if (str_currency.equalsIgnoreCase("gbp")) {
                        txt_total.setText("Total Price :" + getActivity().getResources().getString(R.string.pound_sign) + total);

                    } else if (str_currency.equalsIgnoreCase("usd") || str_currency.equalsIgnoreCase("aud")) {
                        txt_total.setText("Total Price :" + getActivity().getResources().getString(R.string.dollor) + total);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    str_qtyValue = edt_bid.getText().toString();

                    txt_value = Integer.parseInt(str_qtyValue);
                    count_value = txt_value;
                    count_value++;
                    if (count_value > quntity) {
                        txt_total.setVisibility(View.GONE);
                        txt_warning.setVisibility(View.VISIBLE);
                        txt_warning.setText(" You exceeded the available amount");
                        edt_bid.setText("" + quntity);
                        btn_plus.setEnabled(false);
                    } else {
                        txt_total.setVisibility(View.VISIBLE);
                        txt_warning.setVisibility(View.GONE);
                        btn_minus.setEnabled(true);
                    }

                    Log.d("AITL CountValue", "" + count_value);
                    edt_bid.setText("" + count_value);
                    total = fix_value * count_value;
                    if (str_currency.equalsIgnoreCase("euro")) {
                        txt_total.setText("Total Price :" + getActivity().getResources().getString(R.string.euro) + total);

                    } else if (str_currency.equalsIgnoreCase("gbp")) {
                        txt_total.setText("Total Price :" + getActivity().getResources().getString(R.string.pound_sign) + total);

                    } else if (str_currency.equalsIgnoreCase("usd") || str_currency.equalsIgnoreCase("aud")) {
                        txt_total.setText("Total Price :" + getActivity().getResources().getString(R.string.dollor) + total);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

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

                getProductdetail(productIdarrray.get(index).toString());

            }
        });


//        if (GlobalData.isNetworkAvailable(getActivity()))
//        {
//            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.OnlineShopDetail, Param.detail_product(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), sessionManager.getUserId(), sessionManager.OnlineShop_id), 0, true, this);
//        } else {
//            ToastC.show(getActivity(), "No Internet Connection");
//        }


        if (GlobalData.isNetworkAvailable(getActivity())) {
//            if (sessionManager.isLogin()) {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()), 5, false, this);
//            }
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getProdcuId, Param.detail_productId(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), "3"), 1, false, this);
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }

        return rootView;
    }


    private void getProductdetail(String id) {
        sessionManager.strModuleId = id;
        if (GlobalData.isNetworkAvailable(getActivity())) {


            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.OnlineShopDetail, Param.detail_product(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), sessionManager.getUserId(), id), 0, true, this);
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }
    }

    private void addTocart() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.addTocart, Param.addTocart(sessionManager.getEventId(), sessionManager.getEventType(), str_id, sessionManager.getUserId(), strReq_qty), 2, true, this);
    }


    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 6, false, this);
        }
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
                        str_notestatus = jdata.getString("note_status");


                        if (jArrayProduct.length() == 0) {
                            layout_data.setVisibility(View.GONE);
                            card_noproductDetail.setVisibility(View.VISIBLE);
                        } else {
                            layout_data.setVisibility(View.VISIBLE);
                            card_noproductDetail.setVisibility(View.GONE);


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

                            Log.d("AITL Product", jArrayProduct.toString());
                            for (int i = 0; i < jArrayProduct.length(); i++) {
                                JSONObject index = jArrayProduct.getJSONObject(i);
                                str_id = index.getString("product_id");
                                str_name = index.getString("name");
                                str_shortdesc = index.getString("short_description");
                                str_desc = index.getString("description");
                                str_price = index.getString("price");
                                if (index.has("product_preview")) {
                                    product_preview = index.getString("product_preview");
                                } else {
                                    product_preview = "0";
                                }


                                str_qty = index.getString("quantity");
                                if (!(str_qty.equalsIgnoreCase(""))) {
                                    quntity = Integer.parseInt(str_qty);
                                }

                                JSONArray jArrayImage = index.getJSONArray("image_arr");
                                viewpager_arraylist = new ArrayList<>();
                                for (int z = 0; z < jArrayImage.length(); z++) {
                                    String url = MyUrls.Fund_Imgurl + jArrayImage.get(z).toString();
                                    viewpager_arraylist.add(new Exhibitor_DetailImage(url, "Online_shop"));
                                    Log.d("AITL", "silent_productDetail Url : " + url);

                                }
                            }


                            if (viewpager_arraylist.size() == 0) {
                                frame_viewpager.setVisibility(View.GONE);
                            } else {
                                frame_viewpager.setVisibility(View.VISIBLE);
                                adapter = new Exhibitor_ImageAdapter(getActivity(), viewpager_arraylist);
                                online_viewPager.setAdapter(adapter);
                                pageIndicator.setupWithViewPager(online_viewPager);

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
                                footerArrayList.add(new FundraisingHome_footer(str_firstName, str_lastName, MyUrls.Imgurl + str_logo, str_productName, str_amt, "Online_shop"));
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
                            getProductdetail(sessionManager.OnlineShop_id);
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
                    JSONObject jObjectaddCart = new JSONObject(volleyResponse.output);
                    Log.d("AITL AddtoCart", jObjectaddCart.toString());

                    if (jObjectaddCart.getString("success").equalsIgnoreCase("true")) {
                        GlobalData.CURRENT_FRAG = GlobalData.OnlineShop_Detail;
                        ((MainActivity) getActivity()).reloadFragment();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    private void loadData() {
        desc_productName.setText(str_name);
        txt_productName.setText(str_name);
        txt_quntity.setText(str_qty + " " + "available");
        fix_value = Integer.parseInt(str_price);

        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/Lato_Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
        String pas = "</body></html>";
        String myHtmlString = pish + str_desc + pas;
        webViewContent.loadDataWithBaseURL("file:///android_asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);


        if (product_preview.equalsIgnoreCase("1")) {
            linear_enterbid.setVisibility(View.GONE);
            linear_btnDonate.setVisibility(View.GONE);
            txt_startPrice.setVisibility(View.GONE);


        } else if (product_preview.equalsIgnoreCase("0")) {
            linear_enterbid.setVisibility(View.VISIBLE);
            linear_btnDonate.setVisibility(View.VISIBLE);
            txt_startPrice.setVisibility(View.VISIBLE);
        }

        //  webViewContent.loadData(str_desc, "text/html; charset=utf-8", "utf-8");


        if (str_currency.equalsIgnoreCase("euro")) {

            txt_startPrice.setText("" + getActivity().getResources().getString(R.string.euro) + str_price);

        } else if (str_currency.equalsIgnoreCase("gbp")) {

            txt_startPrice.setText("" + getActivity().getResources().getString(R.string.pound_sign) + str_price);

        } else if (str_currency.equalsIgnoreCase("usd") || str_currency.equalsIgnoreCase("aud")) {

            txt_startPrice.setText("" + getActivity().getResources().getString(R.string.dollor) + str_price);

        }
    }

    private void checkNoteStatus() {
        ((MainActivity) getActivity()).updateOnlieShopDetailCartCount(product_preview, str_cart_count);
    }

    private void setAppColor() {
        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {

            linear_footer.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
        } else {

            linear_footer.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
        }

    }

}
