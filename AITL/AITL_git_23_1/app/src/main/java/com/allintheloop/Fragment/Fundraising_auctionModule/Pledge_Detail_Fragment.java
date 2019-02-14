package com.allintheloop.Fragment.Fundraising_auctionModule;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
public class Pledge_Detail_Fragment extends Fragment implements VolleyInterface {

    Spinner spr_amount;
    ArrayList<String> list;
    ArrayList<FundraisingHome_footer> footerArrayList;
    ArrayList<Exhibitor_DetailImage> viewpager_arraylist;
    ArrayList<String> productIdarrray;
    FundraisingHome_footer_adapter footer_adapter;
    Exhibitor_ImageAdapter adapter;


    LinearLayout linear_desc, linear_footer, linear_nextPrivious, linear_btnDonate, linear_enterbid, layout_data;
    ViewPager pledge_viewPager, footer_pager;
    //    CirclePageIndicator indicator;
    ViewPagerIndicator pageIndicator;
    FrameLayout frame_viewpager;
    EditText edt_cmt, edt_amout;
    Button btn_donate, btn_previous, btn_next;
    TextView txt_productName, txt_currency, desc_productName, txt_label;
    WebView webViewContent;
    CheckBox chk_btn;
    String str_id, str_name, str_shortdesc, str_desc, str_price, str_currency, str_notestatus, str_cart_count, str_firstName, str_lastName, str_logo, str_productName, str_amt, bids_donations_display, str_commt = "", str_sprAmt = "", str_edtAmt = "", str_visibility = "1";
    UidCommonKeyClass uidCommonKeyClass;

    SessionManager sessionManager;
    int index = 0;
    boolean isFirst;
    CardView card_noproductDetail;


    public Pledge_Detail_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pledge_detail, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        list = new ArrayList<>();
        footerArrayList = new ArrayList<>();
        productIdarrray = new ArrayList<>();
        sessionManager = new SessionManager(getActivity());
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();

        linear_desc = (LinearLayout) rootView.findViewById(R.id.linear_desc);
        linear_footer = (LinearLayout) rootView.findViewById(R.id.linear_footer);
        linear_nextPrivious = (LinearLayout) rootView.findViewById(R.id.linear_nextPrivious);
        linear_btnDonate = (LinearLayout) rootView.findViewById(R.id.linear_btnDonate);
        linear_enterbid = (LinearLayout) rootView.findViewById(R.id.linear_enterbid);
        layout_data = (LinearLayout) rootView.findViewById(R.id.layout_data);
        frame_viewpager = (FrameLayout) rootView.findViewById(R.id.frame_viewpager);

        edt_cmt = (EditText) rootView.findViewById(R.id.edt_cmt);
        edt_amout = (EditText) rootView.findViewById(R.id.edt_amout);

        btn_donate = (Button) rootView.findViewById(R.id.btn_donate);
        btn_previous = (Button) rootView.findViewById(R.id.btn_previous);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        chk_btn = (CheckBox) rootView.findViewById(R.id.chk_btn);
        spr_amount = (Spinner) rootView.findViewById(R.id.spr_amount);

        webViewContent = (WebView) rootView.findViewById(R.id.webViewContent);
        webViewContent.getSettings().setJavaScriptEnabled(true);
        webViewContent.getSettings().setAllowContentAccess(true);
        webViewContent.setVerticalScrollBarEnabled(true);
        webViewContent.getSettings().setDefaultTextEncodingName("utf-8");
        webViewContent.setHorizontalScrollBarEnabled(true);

        card_noproductDetail = (CardView) rootView.findViewById(R.id.card_noproductDetail);

        txt_currency = (TextView) rootView.findViewById(R.id.txt_currency);
        desc_productName = (TextView) rootView.findViewById(R.id.desc_productName);
        txt_productName = (TextView) rootView.findViewById(R.id.txt_productName);
        txt_label = (TextView) rootView.findViewById(R.id.txt_label);
        pledge_viewPager = (ViewPager) rootView.findViewById(R.id.pledge_viewPager);
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


        btn_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_commt = edt_cmt.getText().toString();
                str_edtAmt = edt_amout.getText().toString();
                if (list.size() == 0) {
                    if (str_edtAmt.trim().length() == 0) {
                        ToastC.show(getActivity(), "please Enter Amount");
                    }

                } else if (str_sprAmt.trim().length() == 0) {
                    ToastC.show(getActivity(), "please Select Amount");

                } else if (str_commt.trim().length() == 0) {
                    ToastC.show(getActivity(), "Please Enter Comment");
                } else {
                    Log.d("AITL sprAmt", str_sprAmt);
                    Log.d("AITL EdtAmt", str_edtAmt);
                    Log.d("AITL EdtComment", str_edtAmt);
                    Log.d("AITL Visibility", str_visibility);

                    pledge_addTocart();
                }

            }
        });

        chk_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chk_btn.isChecked()) {
                    str_visibility = "0";

                } else {
                    str_visibility = "1";
                }
            }
        });

        if (GlobalData.isNetworkAvailable(getActivity())) {
//            if(sessionManager.isLogin())
//            {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()),5, false, this);
//            }

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getProdcuId, Param.detail_productId(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), "4"), 1, false, this);
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }


        return rootView;
    }

    private void pledge_addTocart() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.addTocart, Param.pledge_addTocart(sessionManager.getEventId(), sessionManager.getEventType(), str_id, sessionManager.getUserId(), str_sprAmt, str_edtAmt, str_commt, str_visibility), 2, true, this);
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }
    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 6, false, this);
        }
    }

    private void getProductdetail(String id) {
        sessionManager.strModuleId = id;
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.DonateDetail, Param.detail_product(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), sessionManager.getUserId(), id), 0, false, this);
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
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
//                        str_notestatus = jdata.getString("note_status");


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

                            if (jArrayProduct.length() != 0) {
                                for (int i = 0; i < jArrayProduct.length(); i++) {
                                    JSONObject index = jArrayProduct.getJSONObject(i);
                                    str_id = index.getString("product_id");
                                    str_name = index.getString("name");
                                    str_shortdesc = index.getString("short_description");
                                    str_desc = index.getString("description");
//                                str_price = index.getString("amount");

                                    JSONArray jArrayamount = index.getJSONArray("amount");
                                    list.add("Select Amount");
                                    for (int am = 0; am < jArrayamount.length(); am++) {
                                        list.add(jArrayamount.getString(am).toString());
                                    }

                                    JSONArray jArrayImage = index.getJSONArray("image_arr");
                                    viewpager_arraylist = new ArrayList<>();
                                    for (int z = 0; z < jArrayImage.length(); z++) {
                                        String url = MyUrls.Fund_Imgurl + jArrayImage.get(z).toString();
                                        viewpager_arraylist.add(new Exhibitor_DetailImage(url, "pledge"));
                                        Log.d("AITL", "silent_productDetail Url : " + url);

                                    }
                                }
                                if (list.size() == 0) {
                                    spr_amount.setVisibility(View.GONE);
                                    edt_amout.setVisibility(View.VISIBLE);
                                } else {
                                    spr_amount.setVisibility(View.VISIBLE);
                                    edt_amout.setVisibility(View.GONE);
                                }

                                if (viewpager_arraylist.size() == 0) {
                                    frame_viewpager.setVisibility(View.GONE);
                                } else {
                                    frame_viewpager.setVisibility(View.VISIBLE);
                                    adapter = new Exhibitor_ImageAdapter(getActivity(), viewpager_arraylist);
                                    pledge_viewPager.setAdapter(adapter);
                                    pageIndicator.setupWithViewPager(pledge_viewPager);

                                }
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
                                footerArrayList.add(new FundraisingHome_footer(str_firstName, str_lastName, MyUrls.Imgurl + str_logo, str_productName, str_amt, "pledge"));
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
                            getProductdetail(sessionManager.pledge_id);
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
                    JSONObject jsoncart = new JSONObject(volleyResponse.output);
                    if (jsoncart.getString("success").equalsIgnoreCase("true")) {
                        GlobalData.CURRENT_FRAG = GlobalData.PledgeDetailFragment;
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

        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/Lato_Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
        String pas = "</body></html>";
        String myHtmlString = pish + str_desc + pas;
        webViewContent.loadDataWithBaseURL("file:///android_asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);

        //  webViewContent.loadData(str_desc, "text/html; charset=utf-8","utf-8");


        if (str_currency.equalsIgnoreCase("euro")) {

            txt_currency.setText(getActivity().getResources().getString(R.string.euro));

        } else if (str_currency.equalsIgnoreCase("gbp")) {

            txt_currency.setText(getActivity().getResources().getString(R.string.pound_sign));

        } else if (str_currency.equalsIgnoreCase("usd") || str_currency.equalsIgnoreCase("aud")) {

            txt_currency.setText(getActivity().getResources().getString(R.string.dollor));

        }
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spr_amount.setAdapter(dataAdapter);
//
//        spr_amount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//            {
//                String profileLst = parent.getItemAtPosition(position).toString();
//
//               String spr_profile_id = String.valueOf(parent.getSelectedItemId());
//                Log.d("profileLst", profileLst + "ID:-" + spr_profile_id);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spr_amount.setAdapter(dataAdapter);
        spr_amount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    // Notify the selected item text
                    str_sprAmt = parent.getSelectedItem().toString();
                    Log.d("AITL spinnerAmount", str_sprAmt);

                }
//                String profileLst = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void checkNoteStatus() {
        ((MainActivity) getActivity()).updateCartCount(str_cart_count);
    }

    private void setAppColor() {
        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {
            linear_footer.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
            txt_label.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
        } else {
            linear_footer.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
            txt_label.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
        }

    }


}
