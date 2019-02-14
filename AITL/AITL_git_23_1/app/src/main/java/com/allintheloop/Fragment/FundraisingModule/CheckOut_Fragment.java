package com.allintheloop.Fragment.FundraisingModule;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.allintheloop.Activity.CoutryList_Activity;
import com.allintheloop.Adapter.CheckoutAdapter;
import com.allintheloop.Adapter.FundraisingHome_footer_adapter;
import com.allintheloop.Bean.Fundraising.FundraisingHome_footer;
import com.allintheloop.Bean.Fundraising.checkOutDetail;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckOut_Fragment extends Fragment implements VolleyInterface {


    ArrayList<FundraisingHome_footer> footerArrayList;
    ArrayList<checkOutDetail> checkOutDetailArrayList;
    CheckoutAdapter checkoutAdapter;
    FundraisingHome_footer_adapter footer_adapter;
    Spinner spr_cardOption;
    ArrayList<String> list;
    CardView card_shippingAddress;
    CheckBox chk_ship;
    LinearLayout linear_authorized, linear_footer, linear_radio;
    public static ViewPager footer_pager;
    public static FrameLayout frame_viewpager;
    SessionManager sessionManager;
    public static String str_currency, str_notestatus, str_cart_count;
    String str_firstName, str_lastName, str_logo, str_productName, str_amt, bids_donations_display, str_grand_total;
    String order_id, order_thumb, order_name, order_qty, order_price;
    String StrRadio = "";
    EditText cedt_firstname, cedt_lastname, cedt_email, cedt_phoneNumber, cedt_cmpName, cedt_address, cedt_city, cedt_zipcode;
    EditText cedt_shippfirstname, cedt_shipplastname, cedt_shippemail, cedt_shippphoneNumber, cedt_shippcmpName, cedt_shippaddress, cedt_shippcity, cedt_shippzipcode;
    TextView grand_total, ctxt_country, ctxt_state, ctxt_shippcountry, ctxt_shippstate;

    RecyclerView rv_viewOrderHistroy;

    String str_cedtfirstname, str_cedtlastname, str_cedtemail, str_cedtphoneNumber, str_cedtcmpName, str_cedtaddress, str_cedtcity, str_cedtzipcode;
    String str_cedtshippfirstname, str_cedtshipplastname, str_cedtshippemail, str_cedtshippphoneNumber, str_cedtshippcmpName, str_cedtshippaddress, str_cedtshippcity, str_cedtshippzipcode, str_cedtshippcountry, str_cedtshippstate;
    String strmode;
    public String countryName = "", countryCode = "n/a", stateName = "", stateCode, status;
    public String shipCountryName, shipCountryCode = "n/a", shipStateName, shipStateCode;
    public boolean isShippingDetails = false;

    JSONObject jsonaddress, jsonShipAddress;
    JSONArray jarryaddress, jarrayShipAddress, jArrayOrganizer;
    String transaction_id = "", order_status, is_shipping = "0";
    Button btn_complete;

    Dialog dialog, card_dialog;
    String stripe_cardNumber, stripe_cardMonth, stripe_cardYear, stripe_cardCvv;
    EditText cedt_cardholderNumber, cedt_cardMonth, cedt_cardYear, cedt_cvvnumber;
    ImageView btncardclose_dailog;
    Button carddialog_btnPayment;

    String res_stripeSK, res_stripePK, res_persntage, resIsStripe, res_stripeUserId;
    String stripe_token;
    EditText cedt_autorizedName, cedt_autorizedNumber, cedt_autorizedMonth, cedt_autorizedYear, cedt_autorizedCVV;
    String authorized_name, cardNumber, month, year, cvv;

    private static final String CONFIG_CLIENT_ID = "AaJID45UindC6mepZGKuTyKD6k6hVEQ97x7odOU7xKD0SD-Y6m4WSs3sZWGiWlGLv-LGyDVZaScnDJcE";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final int REQUEST_CODE_PAYMENT = 1;

/// For Authorized Payment Gateway
//    private final String TRANCTION_KEY = "9aqz64eH266JB2Kw";
//    private final String API_LOGIN_ID = "7KB6p7tQ"; // replace with your API LOGIN_ID


    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            .merchantName("AllInTheLoop")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    PayPalPayment thingToBuy;
    ProgressDialog progressDialog;
    int int_year, edt_year, edt_month, int_month;

    public CheckOut_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_checkout, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        sessionManager.strModuleId = "Checkout";
        footerArrayList = new ArrayList<>();
        checkOutDetailArrayList = new ArrayList<>();

        sessionManager = new SessionManager(getActivity());
        spr_cardOption = (Spinner) rootView.findViewById(R.id.spr_cardOption);

        card_shippingAddress = (CardView) rootView.findViewById(R.id.card_shippingAddress);
//        rg_payment = (RadioGroup) rootView.findViewById(R.id.rg_payment);
        chk_ship = (CheckBox) rootView.findViewById(R.id.chk_ship);
        footer_pager = (ViewPager) rootView.findViewById(R.id.footer_pager);


        btn_complete = (Button) rootView.findViewById(R.id.btn_complete);

        cedt_firstname = (EditText) rootView.findViewById(R.id.cedt_firstname);
        cedt_lastname = (EditText) rootView.findViewById(R.id.cedt_lastname);
        cedt_email = (EditText) rootView.findViewById(R.id.cedt_email);
        grand_total = (TextView) rootView.findViewById(R.id.grand_total);
        cedt_phoneNumber = (EditText) rootView.findViewById(R.id.cedt_phoneNumber);
        cedt_cmpName = (EditText) rootView.findViewById(R.id.cedt_cmpName);
        cedt_address = (EditText) rootView.findViewById(R.id.cedt_address);
        cedt_city = (EditText) rootView.findViewById(R.id.cedt_city);
        cedt_zipcode = (EditText) rootView.findViewById(R.id.cedt_zipcode);
        ctxt_country = (TextView) rootView.findViewById(R.id.ctxt_country);
        ctxt_state = (TextView) rootView.findViewById(R.id.ctxt_state);


        cedt_shippfirstname = (EditText) rootView.findViewById(R.id.cedt_shippfirstname);
        cedt_shipplastname = (EditText) rootView.findViewById(R.id.cedt_shipplastname);
        cedt_shippemail = (EditText) rootView.findViewById(R.id.cedt_shippemail);
        cedt_shippphoneNumber = (EditText) rootView.findViewById(R.id.cedt_shippphoneNumber);
        cedt_shippcmpName = (EditText) rootView.findViewById(R.id.cedt_shippcmpName);
        cedt_shippaddress = (EditText) rootView.findViewById(R.id.cedt_shippaddress);
        cedt_shippcity = (EditText) rootView.findViewById(R.id.cedt_shippcity);
        cedt_shippzipcode = (EditText) rootView.findViewById(R.id.cedt_shippzipcode);
        ctxt_shippcountry = (TextView) rootView.findViewById(R.id.ctxt_shippcountry);
        ctxt_shippstate = (TextView) rootView.findViewById(R.id.ctxt_shippstate);

        cedt_autorizedName = (EditText) rootView.findViewById(R.id.cedt_autorizedName);
        cedt_autorizedNumber = (EditText) rootView.findViewById(R.id.cedt_autorizedNumber);
        cedt_autorizedMonth = (EditText) rootView.findViewById(R.id.cedt_autorizedMonth);
        cedt_autorizedYear = (EditText) rootView.findViewById(R.id.cedt_autorizedYear);
        cedt_autorizedCVV = (EditText) rootView.findViewById(R.id.cedt_autorizedCVV);


        jsonaddress = new JSONObject();
        jarryaddress = new JSONArray();

        jsonShipAddress = new JSONObject();
        jarrayShipAddress = new JSONArray();

        linear_authorized = (LinearLayout) rootView.findViewById(R.id.linear_authorized);
        linear_footer = (LinearLayout) rootView.findViewById(R.id.linear_footer);
        linear_radio = (LinearLayout) rootView.findViewById(R.id.linear_radio);
        rv_viewOrderHistroy = (RecyclerView) rootView.findViewById(R.id.rv_viewOrderHistroy);

        list = new ArrayList<>();
        list.add("VISA");
        list.add("Master Card");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spr_cardOption.setAdapter(dataAdapter);

        spr_cardOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String profileLst = parent.getItemAtPosition(position).toString();

                String spr_profile_id = String.valueOf(parent.getSelectedItemId());
                Log.d("profileLst", profileLst + "ID:-" + spr_profile_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        chk_ship.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    is_shipping = "1";
                    card_shippingAddress.setVisibility(View.VISIBLE);
                } else {
                    is_shipping = "0";
                    card_shippingAddress.setVisibility(View.GONE);
                }
            }
        });


        ctxt_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CoutryList_Activity.class);
                intent.putExtra("status", "country");
                startActivityForResult(intent, 32111);
            }
        });
        ctxt_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!countryCode.equalsIgnoreCase("n/a")) {
                    Intent intent = new Intent(getContext(), CoutryList_Activity.class);
                    intent.putExtra("status", "state");
                    intent.putExtra("country_code", countryCode);
                    startActivityForResult(intent, 32111);
                } else {
                    ToastC.show(getContext(), "Please select country first.");
                }
            }
        });

        ctxt_shippcountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShippingDetails = true;
                Intent intent = new Intent(getContext(), CoutryList_Activity.class);
                intent.putExtra("status", "country");
                startActivityForResult(intent, 32111);
            }
        });

        ctxt_shippstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShippingDetails = true;
                if (!shipCountryCode.equalsIgnoreCase("n/a")) {
                    Intent intent = new Intent(getContext(), CoutryList_Activity.class);
                    intent.putExtra("status", "state");
                    intent.putExtra("country_code", shipCountryCode);
                    startActivityForResult(intent, 32111);
                } else {
                    ToastC.show(getContext(), "Please select country first.");
                }
            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (StrRadio.equalsIgnoreCase("")) {
                    ToastC.show(getActivity(), "Please Select Payment Method");
                } else {
                    getDatafromField();
                }
            }
        });

        if (GlobalData.isNetworkAvailable(getActivity())) {
//            if (sessionManager.isLogin()) {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()), 5, false, this);
//            }
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.checkOutDetail, Param.checkoutdetail(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), sessionManager.getToken()), 0, false, this);
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }

        return rootView;
    }


    private void getDatafromField() {
        str_cedtfirstname = cedt_firstname.getText().toString();
        str_cedtlastname = cedt_lastname.getText().toString();
        str_cedtemail = cedt_email.getText().toString();
        str_cedtphoneNumber = cedt_phoneNumber.getText().toString();
        str_cedtcmpName = cedt_cmpName.getText().toString();
        str_cedtaddress = cedt_address.getText().toString();
        str_cedtcity = cedt_city.getText().toString();
        str_cedtzipcode = cedt_zipcode.getText().toString();


        if (str_cedtfirstname.trim().length() == 0) {
            cedt_firstname.setError("Please Enter FirstName");
            cedt_firstname.requestFocus();
        } else if (str_cedtlastname.trim().length() == 0) {
            cedt_lastname.setError("Please Enter LastName");
            cedt_lastname.requestFocus();
        } else if (str_cedtemail.trim().length() == 0) {
            cedt_email.setError("Please Enter Email");
            cedt_email.requestFocus();
        } else if (str_cedtphoneNumber.trim().length() == 0) {
            cedt_phoneNumber.setError("Please Enter Phone Number");
            cedt_phoneNumber.requestFocus();
        } else if (str_cedtcmpName.trim().length() == 0) {
            cedt_cmpName.setError("Please Enter Company Name");
            cedt_cmpName.requestFocus();

        } else if (str_cedtaddress.trim().length() == 0) {
            cedt_address.setError("Please Enter Address");
            cedt_address.requestFocus();

        } else if (str_cedtcity.trim().length() == 0) {
            cedt_city.setError("Please Enter City");
            cedt_city.requestFocus();
        } else if (str_cedtzipcode.trim().length() == 0) {
            cedt_zipcode.setError("Please Enter ZipCode");
            cedt_zipcode.requestFocus();
        } else if (countryName.trim().length() == 0) {
            ctxt_country.setError("Please Select Country");
        } else if (stateName.trim().length() == 0) {
            ctxt_state.setError("Please Select State");
        } else {
            try {
                jsonaddress.put("Firstname", str_cedtfirstname);
                jsonaddress.put("Lastname ", str_cedtlastname);
                jsonaddress.put("Email", str_cedtemail);
                jsonaddress.put("Company_name", str_cedtcmpName);
                jsonaddress.put("Mobile", str_cedtphoneNumber);
                jsonaddress.put("Street", "");
                jsonaddress.put("Suburb", str_cedtaddress);
                jsonaddress.put("Postcode", str_cedtzipcode);
                jsonaddress.put("Country", countryCode);
                jsonaddress.put("State", stateCode);


                jarryaddress.put(jsonaddress);
                Log.d("AITL JsonArrayAddress", jarryaddress.toString());


            } catch (Exception e) {
                e.printStackTrace();
            }

            if (isShippingDetails == true) {
                str_cedtshippfirstname = cedt_shippfirstname.getText().toString();
                str_cedtshipplastname = cedt_shipplastname.getText().toString();
                str_cedtshippemail = cedt_shippemail.getText().toString();
                str_cedtshippphoneNumber = cedt_shippphoneNumber.getText().toString();
                str_cedtshippcmpName = cedt_shippcmpName.getText().toString();
                str_cedtshippaddress = cedt_shippaddress.getText().toString();
                str_cedtshippcity = cedt_shippcity.getText().toString();
                str_cedtshippzipcode = cedt_shippzipcode.getText().toString();

                if (str_cedtshippfirstname.trim().length() == 0) {
                    cedt_shippfirstname.setError("Please Enter FirstName");
                    cedt_shippfirstname.requestFocus();
                } else if (str_cedtshipplastname.trim().length() == 0) {
                    cedt_shipplastname.setError("Please Enter LastName");
                    cedt_shipplastname.requestFocus();
                } else if (str_cedtshippemail.trim().length() == 0) {
                    cedt_shippemail.setError("Please Enter Email");
                    cedt_shippemail.requestFocus();
                } else if (str_cedtshippphoneNumber.trim().length() == 0) {
                    cedt_shippphoneNumber.setError("Please Enter Phone Number");
                    cedt_shippphoneNumber.requestFocus();
                } else if (str_cedtshippcmpName.trim().length() == 0) {
                    cedt_shippcmpName.setError("Please Enter Company Name");
                    cedt_shippcmpName.requestFocus();

                } else if (str_cedtshippaddress.trim().length() == 0) {
                    cedt_shippaddress.setError("Please Enter Address");
                    cedt_shippaddress.requestFocus();
                } else if (str_cedtshippcity.trim().length() == 0) {
                    cedt_shippcity.setError("Please Enter City");
                    cedt_shippcity.requestFocus();
                } else if (str_cedtshippzipcode.trim().length() == 0) {
                    cedt_shippzipcode.setError("Please Enter ZipCode");
                    cedt_shippzipcode.requestFocus();
                } else if (shipCountryName.trim().length() == 0) {
                    ctxt_shippcountry.setError("Please Select Country");
                } else if (shipStateName.trim().length() == 0) {
                    ctxt_shippcountry.setError("Please Select State");
                } else {
                    try {
                        jsonShipAddress.put("Firstname", str_cedtshippfirstname);
                        jsonShipAddress.put("Lastname ", str_cedtshipplastname);
                        jsonShipAddress.put("Email", str_cedtshippemail);
                        jsonShipAddress.put("Company_name", str_cedtshippcmpName);
                        jsonShipAddress.put("Mobile", str_cedtshippphoneNumber);
                        jsonShipAddress.put("Street", "");
                        jsonShipAddress.put("Suburb", str_cedtshippaddress);
                        jsonShipAddress.put("Postcode", str_cedtshippzipcode);
                        jsonShipAddress.put("Country", shipCountryCode);
                        jsonShipAddress.put("State", shipStateCode);


                        jarrayShipAddress.put(jsonaddress);
                        checkPaymentMethod();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                checkPaymentMethod();
            }

        }
    }


    private void checkPaymentMethod() {
        if (StrRadio.equalsIgnoreCase("Paypal Payment")) {

            Intent intent = new Intent(getActivity(), PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            getActivity().startService(intent);

            if (str_currency.equalsIgnoreCase("euro")) {
                thingToBuy = new PayPalPayment(new BigDecimal(str_grand_total), "EURO", "Total", PayPalPayment.PAYMENT_INTENT_SALE);

            } else if (str_currency.equalsIgnoreCase("gbp")) {
                thingToBuy = new PayPalPayment(new BigDecimal(str_grand_total), "GBP", "Total", PayPalPayment.PAYMENT_INTENT_SALE);
            } else if (str_currency.equalsIgnoreCase("usd") || str_currency.equalsIgnoreCase("aud")) {
                thingToBuy = new PayPalPayment(new BigDecimal(str_grand_total), "USD", "Total", PayPalPayment.PAYMENT_INTENT_SALE);
            }

//                        thingToBuy = new PayPalPayment(new BigDecimal(str_grand_total), "USD", "Total", PayPalPayment.PAYMENT_INTENT_SALE);
            Intent intent1 = new Intent(getActivity(), PaymentActivity.class);
            intent1.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
            startActivityForResult(intent1, REQUEST_CODE_PAYMENT);
        } else if (StrRadio.equalsIgnoreCase("Cash on Delivery")) {
            order_status = "completed";
            compleOrder();
        } else if (StrRadio.equalsIgnoreCase("Stripe Payment")) {
            Log.d("AITL", "Stripe");
            stripeGateway();
        } else if (StrRadio.equalsIgnoreCase("Authorize Payment")) {
            AuthorizedGateway();
            //ToastC.show(getActivity(),"Under Development");
        }
    }

    private void AuthorizedGateway() {
        //authorized_name=cedt_autorizedName.getText().toString();
        cardNumber = cedt_autorizedNumber.getText().toString();
        month = cedt_autorizedMonth.getText().toString();
        cvv = cedt_autorizedCVV.getText().toString();
        year = cedt_autorizedYear.getText().toString();

        if (year.trim().length() != 0) {
            Calendar calendar = Calendar.getInstance();
            int_year = calendar.get(Calendar.YEAR);
            edt_year = Integer.parseInt(year);

        } else if (month.trim().length() != 0) {
            edt_month = Integer.parseInt(month);
        }

        if (cardNumber.trim().length() == 0) {
            cedt_autorizedNumber.setError("Please Enter 16 Digit CardNumber");
            cedt_autorizedNumber.requestFocus();
        } else if (month.trim().length() == 0) {
            cedt_autorizedMonth.setError("Please Enter Month in Two Digit");
            cedt_autorizedMonth.requestFocus();
        } else if (edt_month > 12) {
            cedt_autorizedYear.setError("Please Enter Valid Month");
            cedt_autorizedYear.requestFocus();
        } else if (year.trim().length() == 0) {
            cedt_autorizedYear.setError("Please Enter Year in Four Digit");
            cedt_autorizedYear.requestFocus();

        } else if (edt_year < int_year) {
            cedt_autorizedYear.setError("Please Enter Valid Year");
            cedt_autorizedYear.requestFocus();
        } else if (cvv.trim().length() == 0) {
            cedt_autorizedCVV.setError("Please Enter CVV in Three Digit");
            cedt_autorizedCVV.requestFocus();
        } else {
            Log.d("AITL cardNumber", cardNumber);
            Log.d("AITL Month", month);
            Log.d("AITL Year", year);
            Log.d("AITL CVV", cvv);

            authorizedAPI();

        }
    }


    private void stripeGateway() {
        card_dialog = new Dialog(getActivity());
        card_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        card_dialog.setContentView(R.layout.card_payment_layout);
        btncardclose_dailog = (ImageView) card_dialog.findViewById(R.id.btncardclose_dailog);
        cedt_cardholderNumber = (EditText) card_dialog.findViewById(R.id.cardholderNumber);
        cedt_cardMonth = (EditText) card_dialog.findViewById(R.id.cardMonth);
        cedt_cardYear = (EditText) card_dialog.findViewById(R.id.cardYear);
        cedt_cvvnumber = (EditText) card_dialog.findViewById(R.id.cvvnumber);
        carddialog_btnPayment = (Button) card_dialog.findViewById(R.id.carddialog_btnPayment);


        carddialog_btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stripe_cardNumber = cedt_cardholderNumber.getText().toString();
                stripe_cardMonth = cedt_cardMonth.getText().toString();
                stripe_cardYear = cedt_cardYear.getText().toString();
                stripe_cardCvv = cedt_cvvnumber.getText().toString();

                if (stripe_cardYear.trim().length() != 0) {
                    Calendar calendar = Calendar.getInstance();
                    int_year = calendar.get(Calendar.YEAR);
                    edt_year = Integer.parseInt(cedt_cardYear.getText().toString());

                } else if (stripe_cardMonth.trim().length() != 0) {
                    edt_month = Integer.parseInt(cedt_cardMonth.getText().toString());
                }

                if (stripe_cardNumber.trim().length() == 0) {
                    cedt_cardholderNumber.setError("Please Enter 16 Digit CardNumber");
                    cedt_cardholderNumber.requestFocus();
                } else if (stripe_cardMonth.trim().length() == 0) {
                    cedt_cardMonth.setError("Please Enter Month in Two Digit");
                    cedt_cardMonth.requestFocus();
                } else if (edt_month > 12) {
                    cedt_cardMonth.setError("Please Enter Valid Month");
                    cedt_cardMonth.requestFocus();
                } else if (stripe_cardYear.trim().length() == 0) {
                    cedt_cardYear.setError("Please Enter Year in Four Digit");
                    cedt_cardYear.requestFocus();
                } else if (edt_year < int_year) {
                    cedt_cardYear.setError("Please Enter Valid Year");
                    cedt_cardYear.requestFocus();
                } else if (stripe_cardCvv.trim().length() == 0) {
                    cedt_cvvnumber.setError("Please Enter CVV in Three Digit");
                    cedt_cvvnumber.requestFocus();
                } else {
                    Log.d("AITL cardNumber", stripe_cardNumber);
                    Log.d("AITL Month", stripe_cardMonth);
                    Log.d("AITL Year", stripe_cardYear);
                    Log.d("AITL CVV", stripe_cardCvv);

                    if (jArrayOrganizer.length() != 0) {
                        submitCard();
                    } else {
                        ToastC.show(getActivity(), "Not Available For Stripe");
                    }
                }
            }
        });

        btncardclose_dailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_dialog.dismiss();
            }
        });
        card_dialog.show();

    }


    private void compleOrder() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (sessionManager.isLogin()) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.confirm_order, Param.complateOrder(sessionManager.getUserId(), transaction_id, order_status, jarryaddress.toString(), sessionManager.getEventId(), is_shipping, jarrayShipAddress.toString(), strmode), 1, true, this);
            } else {
                ToastC.show(getActivity(), "Please Login First");
            }

        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }
    }

    private void authorizedAPI() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (sessionManager.isLogin()) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.authorized_payment, Param.authorizedPayment(str_cedtfirstname, str_cedtlastname, cardNumber, month + year, str_grand_total, sessionManager.getOrganizer_id()), 3, true, this);
            } else {
                ToastC.show(getActivity(), "Please Login First");
            }

        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }
    }


    private void stripePayment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (sessionManager.isLogin()) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.stripePayment, Param.StripePayment(res_persntage, resIsStripe, str_grand_total, sessionManager.getOrganizer_id(), res_stripeUserId, stripe_token, str_currency, res_stripeSK), 4, false, this);
            } else {
                ToastC.show(getActivity(), "Please Login First");
            }

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

                        JSONObject jdata = jsonObject.getJSONObject("data");
                        str_currency = jdata.getString("currency");
                        str_cart_count = jdata.getString("cart_count");
                        str_notestatus = jdata.getString("note_status");
                        str_grand_total = jdata.getString("grand_total");

                        JSONArray jsonArray = jdata.optJSONArray("events");
                        for (int e = 0; e < jsonArray.length(); e++) {
                            JSONObject jObjectevent = (JSONObject) jsonArray.get(e);
                            Log.d("AITL", "jObjectevent" + jObjectevent);
                            sessionManager.appColor(jObjectevent);
                        }


                        JSONObject jObjectbilling = jdata.getJSONObject("billing_details");
//                            Log.e("aiyaz",jObjectbilling.toString());
                        cedt_firstname.setText(jObjectbilling.getString("Firstname"));
                        cedt_lastname.setText(jObjectbilling.getString("Lastname"));
                        cedt_email.setText(jObjectbilling.getString("Email"));

                        if (!(jObjectbilling.getString("mobile").equalsIgnoreCase(""))) {
                            cedt_phoneNumber.setText(jObjectbilling.getString("mobile"));
                        }

                        JSONArray jArrayRdo = jdata.optJSONArray("payment_method");
                        Log.d("ArraySize", "" + jArrayRdo.length());
                        RadioGroup radioGroup = new RadioGroup(getActivity());

                        for (int j = 0; j < jArrayRdo.length(); j++) {
//                                JSONObject jObjectRdo = (JSONObject) jArrayRdo.get(j);
//                                Log.d("Count", " " + j);

                            RadioButton radioButton = new RadioButton(getActivity());
                            radioButton.setText(jArrayRdo.get(j).toString());
                            radioButton.setPadding(15, 15, 0, 15);
                            radioButton.setTextSize(15);
                            radioButton.setTextColor(getResources().getColor(R.color.survey_question));
                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params1.leftMargin = 20;
                            radioGroup.addView(radioButton);
                            radioButton.setLayoutParams(params1);

                            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                                    StrRadio = radioButton.getText().toString();
                                    Log.d("Radio button", StrRadio);

                                    if (StrRadio.equalsIgnoreCase("Paypal Payment")) {
                                        strmode = "paypal";
                                        linear_authorized.setVisibility(View.GONE);
                                    } else if (StrRadio.equalsIgnoreCase("Authorize Payment")) {
                                        strmode = "Authorize";
                                        linear_authorized.setVisibility(View.VISIBLE);
                                    } else if (StrRadio.equalsIgnoreCase("Cash on Delivery")) {
                                        strmode = "cod";
                                        linear_authorized.setVisibility(View.GONE);
                                    } else if (StrRadio.equalsIgnoreCase("Stripe Payment")) {
                                        strmode = "stripe";
                                        linear_authorized.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        linear_radio.addView(radioGroup);


                        JSONObject jsonPayInfo = jdata.getJSONObject("payment_info");

                        JSONArray jArrayFundInfo = jsonPayInfo.getJSONArray("fund_info");
                        for (int f = 0; f < jArrayFundInfo.length(); f++) {
                            JSONObject jsonfunInfo = jArrayFundInfo.getJSONObject(f);
                            res_persntage = jsonfunInfo.getString("fund_percantage");
                            resIsStripe = jsonfunInfo.getString("stripe_show");
                        }

                        if (!(res_persntage.equalsIgnoreCase(""))) {
                            res_persntage = "0";
                        }

                        JSONArray jArrayAdmin = jsonPayInfo.getJSONArray("super_admin_info");

                        for (int a = 0; a < jArrayAdmin.length(); a++) {
                            JSONObject jsonadminInfo = jArrayAdmin.getJSONObject(a);
                            res_stripeSK = jsonadminInfo.getString("secret_key");
                            res_stripePK = jsonadminInfo.getString("public_key");
                        }
                        jArrayOrganizer = jsonPayInfo.getJSONArray("organisor_info");
                        for (int o = 0; o < jArrayOrganizer.length(); o++) {
                            JSONObject jsonOrganisor = jArrayOrganizer.getJSONObject(o);
//                            res_stripeSK=jsonOrganisor.getString("secret_key");
//                            res_stripePK=jsonOrganisor.getString("public_key");
                            res_stripeUserId = jsonOrganisor.getString("stripe_user_id");
                        }

                        JSONArray jArrayOrderDetail = jdata.getJSONArray("order_details");

                        Log.e("AITL", jArrayOrderDetail.toString());
                        for (int d = 0; d < jArrayOrderDetail.length(); d++) {
                            JSONObject jObjectdetail = jArrayOrderDetail.getJSONObject(d);
                            order_id = jObjectdetail.getString("Id");
                            order_thumb = jObjectdetail.getString("thumb");
                            order_name = jObjectdetail.getString("name");
                            order_qty = jObjectdetail.getString("quantity");
                            order_price = jObjectdetail.getString("price");

                            checkOutDetailArrayList.add(new checkOutDetail(order_id, order_thumb, order_name, order_qty, order_price));
                        }

                        checkoutAdapter = new CheckoutAdapter(checkOutDetailArrayList, getActivity());
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        rv_viewOrderHistroy.setLayoutManager(mLayoutManager);
                        rv_viewOrderHistroy.setAdapter(checkoutAdapter);

                        JSONArray jArrayFund = jdata.getJSONArray("fundraising_settings");
                        for (int j = 0; j < jArrayFund.length(); j++) {
                            JSONObject jObjectFund = jArrayFund.getJSONObject(j);

                            bids_donations_display = jObjectFund.getString("bids_donations_display");
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
                            footerArrayList.add(new FundraisingHome_footer(str_firstName, str_lastName, MyUrls.Imgurl + str_logo, str_productName, str_amt, "check_out"));
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
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL COD", jsonObject.toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(getActivity(), jsonObject.getString("message"));

                        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                            GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else {
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL Stripe", jsonObject.toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {

                            progressDialog.dismiss();
                            card_dialog.dismiss();

                            GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            progressDialog.dismiss();
                            card_dialog.dismiss();

                            GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else {
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);

                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL", jsonObject.toString());
                        order_status = jsonObject.getString("order_status");
                        transaction_id = jsonObject.getString("transaction_id");
                        compleOrder();
                    } else {
                        Log.d("AITL", jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);

                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        progressDialog.dismiss();
                        card_dialog.dismiss();

                        Log.d("AITL", jsonObject.toString());
                        order_status = jsonObject.getString("message");
                        transaction_id = jsonObject.getString("transaction_id");
                        compleOrder();
                    } else {
                        progressDialog.dismiss();
                        card_dialog.dismiss();

                        order_status = jsonObject.getString("message");
                        transaction_id = jsonObject.getString("transaction_id");
                        compleOrder();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

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

    private void loadData() {
        if (str_currency.equalsIgnoreCase("euro")) {
            grand_total.setText(" TOTAL :" + " " + getActivity().getResources().getString(R.string.euro) + str_grand_total);
        } else if (str_currency.equalsIgnoreCase("gbp")) {
            grand_total.setText(" TOTAL :" + " " + getActivity().getResources().getString(R.string.pound_sign) + str_grand_total);
        } else if (str_currency.equalsIgnoreCase("usd") || str_currency.equalsIgnoreCase("aud")) {
            grand_total.setText(" TOTAL :" + " " + getActivity().getResources().getString(R.string.dollor) + str_grand_total);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 32111) {
                String respStatus = data.getStringExtra("status");
                if (isShippingDetails) {
                    if (respStatus.equalsIgnoreCase("country")) {
                        shipCountryCode = data.getStringExtra("code");
                        shipCountryName = data.getStringExtra("name");
                        ctxt_shippcountry.setText(shipCountryName);
                    } else if (respStatus.equalsIgnoreCase("state")) {
                        shipStateCode = data.getStringExtra("code");
                        shipStateName = data.getStringExtra("name");
                        ctxt_shippstate.setText(shipStateName);
                    }
                    isShippingDetails = false;
                } else {
                    if (respStatus.equalsIgnoreCase("country")) {
                        countryCode = data.getStringExtra("code");
                        countryName = data.getStringExtra("name");
                        ctxt_country.setText(countryName);
                    } else if (respStatus.equalsIgnoreCase("state")) {
                        stateCode = data.getStringExtra("code");
                        stateName = data.getStringExtra("name");
                        ctxt_state.setText(stateName);
                    }
                }
            } else if (requestCode == REQUEST_CODE_PAYMENT) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.d("AITL Paypal", confirm.toJSONObject().toString(4));

                        JSONObject jsonconfirm = new JSONObject(confirm.toJSONObject().toString(4));
                        JSONObject jsonResponse = jsonconfirm.getJSONObject("response");
                        transaction_id = jsonResponse.getString("id");
                        Log.d("AITL TransctionId", jsonResponse.getString("id"));
                        Log.d("AITL TransctionIdString", transaction_id);

                        Log.d("AITL Paypal Payment", confirm.getPayment().toJSONObject().toString(4));
                        order_status = "completed";
                        compleOrder();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //order_status = "failed";
            ToastC.show(getActivity(), "Some Problem  Occured");
            // compleOrder();

        }
    }

    @Override
    public void onDestroy() {
// Stop service when done
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }


    public void submitCard() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Wait a Moment....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // TODO: replace with your own test key     // USer res_StripePk when live
        final String publishableApiKey = res_stripePK;


        Card card = new Card(stripe_cardNumber,
                Integer.valueOf(stripe_cardMonth),
                Integer.valueOf(stripe_cardYear),
                stripe_cardCvv);

        Stripe stripe = new Stripe();
        stripe.createToken(card, publishableApiKey, new TokenCallback() {
            public void onError(Exception error) {
                Log.d("Stripe", error.getLocalizedMessage());

            }

            @Override
            public void onSuccess(com.stripe.android.model.Token token) {
                stripe_token = token.getId();
                Log.d("AITL TOken", stripe_token);
                stripePayment();
            }
        });
    }

//    private void gettoken(final String token)
//    {
//
//        Log.d("AITL", "reciving token :" + token);
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                com.stripe.Stripe.apiKey="sk_test_MQdSDqW91LmspHHYRzZ5FNyk"; // res_stripeSK   // used res_StripeSK when it's live
//                try
//                {
////                  if(resIsStripe.equalsIgnoreCase("0"))
////                  {
//                      Log.d("AITL",resIsStripe);
//                      String  str_amtwithoutcurr= str_grand_total.replaceAll("[^0-9.]", "");
//                      Map<String, Object> chargeParams = new HashMap<String, Object>();
//                      chargeParams.put("amount",Integer.parseInt(str_amtwithoutcurr)); // Amount in cents
//                      chargeParams.put("currency", "usd");
//                      chargeParams.put("source", token);
//                      chargeParams.put("description", "AllInTheLoop");
//
//                      Charge charge = Charge.create(chargeParams);
//                      order_status = "completed";
//                      transaction_id=charge.getBalanceTransaction();
//                      Log.d("AITL","data in charge" + charge.getBalanceTransaction());
//                      stripePayment();
// //                 }
////                  else
////                  {
////                      Log.d("AITL",resIsStripe);
////                      int appicationFee=Integer.parseInt(res_persntage);
////                      appicationFee=(Integer.parseInt(str_grand_total)*appicationFee)/100;
////
////                      Log.d("AITL ApplicationFee",""+appicationFee);
////                      String  str_amtwithoutcurr= str_grand_total.replaceAll("[^0-9.]", "");
////                      Map<String, Object> chargeParams = new HashMap<String, Object>();
////                      chargeParams.put("amount",Integer.parseInt(str_amtwithoutcurr)); // Amount in cents
////                      chargeParams.put("currency", "usd");
////                      chargeParams.put("source", token);
////                      chargeParams.put("description", "AllIntheLoop");
//////                     chargeParams.put("application_fee", appicationFee);
////
//////
////      //                RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(res_stripeUserId).build();
////                      Charge charge = Charge.create(chargeParams,requestOptions);
////                      order_status = "completed";
////                      transaction_id=charge.getBalanceTransaction();
////                      Log.d("AITL","data in charge" + charge.getBalanceTransaction());
////                      stripePayment();
////                  }
//                    // call for save InstantDonation
//                }
//                catch (Exception e)
//                {
//                    // The card has been declined
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }


}
