package com.allintheloop.Fragment.FundraisingModule;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.allintheloop.Activity.CoutryList_Activity;
import com.allintheloop.Adapter.FundrDonationSuppAdapter;
import com.allintheloop.Adapter.FundraisingHome_footer_adapter;
import com.allintheloop.Bean.Fundraising.FundraisingHome_footer;
import com.allintheloop.Bean.Fundraising.FundraisongdonationSupporter;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.CircularTextView;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.model.Charge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fundraising_Donation_Fragment extends Fragment implements VolleyInterface {

    TextView make_donate, txt_submitdonate, txt_comtt, txt_supporter, fundring_amount, txt_of, target_amount, label, txt_donateMore, txt_curreny;
    CircularTextView first_donate, second_donate;
    ViewPager footer_pager;
    EditText edt_commt, edt_moreAmt;

    LinearLayout linear_mainProgress, linear_raised, linear_progress, linear_footer, linear_more, linear_donateMore;
    ProgressBar fund_progress_bar;
    SessionManager sessionManager;

    ArrayList<FundraisongdonationSupporter> supporterArrayList;
    FundrDonationSuppAdapter donationSuppAdapter;
    ArrayList<FundraisingHome_footer> footerArrayList;
    FundraisingHome_footer_adapter footer_adapter;

    String bids_donations_display, target_raisedsofar_display, str_raised_price, str_target_amount, str_fun_donation_first, str_fun_donation_second, pushnoti_display;
    String currency_status, cart_count;
    String str_firstName, str_lastName, str_logo, str_productName, str_amt;
    int fund_amount, tar_amount, progress_amount;

    EditText dialog_email, dialog_name, dialog_address, dialog_zip, dialog_city, dialog_country;
    TextView dialog_txt_label;
    Button dialog_btnPayment;
    String str_fundAmt = "", str_edtComm = "", str_status = "completed", str_amtwithoutcurr = "";
    String str_email, str_name, str_add, str_zip, str_city;
    String countryName, countryCode = "n/a";
    String stripe_cardNumber, stripe_cardMonth, stripe_cardYear, stripe_cardCvv;
    ImageView btnclose_dailog, btncardclose_dailog;
    EditText cedt_cardholderNumber, cedt_cardMonth, cedt_cardYear, cedt_cvvnumber;
    Button carddialog_btnPayment;
    ProgressDialog progressDialog;
    Dialog dialog, card_dialog;

    RecyclerView rv_viewSupporter;
    int year, edt_year, edt_month, month;

    public Fundraising_Donation_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fundraising_donation, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        sessionManager = new SessionManager(getActivity());
        make_donate = (TextView) rootView.findViewById(R.id.make_donate);
        first_donate = (CircularTextView) rootView.findViewById(R.id.first_donate);
        second_donate = (CircularTextView) rootView.findViewById(R.id.second_donate);
        txt_submitdonate = (TextView) rootView.findViewById(R.id.txt_submitdonate);
        txt_comtt = (TextView) rootView.findViewById(R.id.txt_comtt);
        txt_supporter = (TextView) rootView.findViewById(R.id.txt_supporter);
        fundring_amount = (TextView) rootView.findViewById(R.id.fundring_amount);
        txt_of = (TextView) rootView.findViewById(R.id.txt_of);
        target_amount = (TextView) rootView.findViewById(R.id.target_amount);
        label = (TextView) rootView.findViewById(R.id.label);
        txt_donateMore = (TextView) rootView.findViewById(R.id.txt_donateMore);
        txt_curreny = (TextView) rootView.findViewById(R.id.txt_curreny);

        edt_commt = (EditText) rootView.findViewById(R.id.edt_commt);
        edt_moreAmt = (EditText) rootView.findViewById(R.id.edt_moreAmt);

        sessionManager.strModuleId = "Donation";
        footerArrayList = new ArrayList<>();
        supporterArrayList = new ArrayList<>();

        linear_mainProgress = (LinearLayout) rootView.findViewById(R.id.linear_mainProgress);
        linear_raised = (LinearLayout) rootView.findViewById(R.id.linear_raised);
        linear_progress = (LinearLayout) rootView.findViewById(R.id.linear_progress);
        linear_footer = (LinearLayout) rootView.findViewById(R.id.linear_footer);
        linear_more = (LinearLayout) rootView.findViewById(R.id.linear_more);
        linear_donateMore = (LinearLayout) rootView.findViewById(R.id.linear_donateMore);

        fund_progress_bar = (ProgressBar) rootView.findViewById(R.id.fund_progress_bar);

        footer_pager = (ViewPager) rootView.findViewById(R.id.footer_pager);
        rv_viewSupporter = (RecyclerView) rootView.findViewById(R.id.rv_viewSupporter);
        txt_donateMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_donateMore.setVisibility(View.GONE);
                linear_donateMore.setVisibility(View.VISIBLE);
            }
        });

        first_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_submitdonate.setText(first_donate.getText().toString());


            }
        });

        second_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_submitdonate.setText(second_donate.getText().toString());
            }
        });

        edt_moreAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (currency_status.equalsIgnoreCase("euro")) {
                    txt_submitdonate.setText(getContext().getResources().getString(R.string.euro) + s.toString());


                } else if (currency_status.equalsIgnoreCase("gbp")) {
                    txt_submitdonate.setText(getContext().getResources().getString(R.string.pound_sign) + s.toString());
                } else if (currency_status.equalsIgnoreCase("usd") || currency_status.equalsIgnoreCase("aud")) {
                    txt_submitdonate.setText(getContext().getResources().getString(R.string.dollor) + s.toString());
                }
            }
        });

        txt_submitdonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_submitdonate.getText().toString().equalsIgnoreCase("Tap Here to submit your donation")) {


                    new AlertDialogWrapper.Builder(getActivity())
                            .setMessage("Plase Enter Donation Amount")
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {


                    str_fundAmt = txt_submitdonate.getText().toString();
                    str_edtComm = edt_commt.getText().toString();

                    str_amtwithoutcurr = str_fundAmt.replaceAll("[^0-9.]", "");
                    Log.d("AITL Amount ", str_amtwithoutcurr);
                    dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custome_dialog_instantdonation);
                    btnclose_dailog = (ImageView) dialog.findViewById(R.id.btnclose_dailog);
                    dialog_email = (EditText) dialog.findViewById(R.id.dialog_email);
                    dialog_name = (EditText) dialog.findViewById(R.id.dialog_name);
                    dialog_address = (EditText) dialog.findViewById(R.id.dialog_address);
                    dialog_zip = (EditText) dialog.findViewById(R.id.dialog_zip);
                    dialog_city = (EditText) dialog.findViewById(R.id.dialog_city);
                    dialog_country = (EditText) dialog.findViewById(R.id.dialog_country);
                    dialog_btnPayment = (Button) dialog.findViewById(R.id.dialog_btnPayment);
                    dialog_txt_label = (TextView) dialog.findViewById(R.id.txt_label);
                    dialog_txt_label.setText("Fundraising Donation");

                    dialog_btnPayment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            str_email = dialog_email.getText().toString();
                            str_name = dialog_name.getText().toString();
                            str_add = dialog_address.getText().toString();
                            str_zip = dialog_zip.getText().toString();
                            str_city = dialog_city.getText().toString();

                            if (str_email.trim().length() == 0) {
                                dialog_email.setError("Please Enter Email");
                            } else if (str_name.trim().length() == 0) {
                                dialog_name.setError("Please Enter Name");
                            } else if (str_add.trim().length() == 0) {
                                dialog_address.setError("Please Enter Address");
                            } else if (str_zip.trim().length() == 0) {
                                dialog_zip.setError("Please Enter ZipCode");
                            } else if (str_city.trim().length() == 0) {
                                dialog_city.setError("Please Enter City");
                            } else if (countryName.trim().length() == 0) {
                                dialog_country.setError("Please Enter Country");
                            } else {

                                card_dialog = new Dialog(getActivity());
                                if (sessionManager.getIsStripeEnabled().equalsIgnoreCase("1")) {
                                    saveInstantDonation();
                                } else if (sessionManager.getIsStripeEnabled().equalsIgnoreCase("0")) {
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
                                                year = calendar.get(Calendar.YEAR);
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
                                            } else if (edt_year < year) {
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


                                                if ((!Fundrising_Home_Fragment.stripePk.equalsIgnoreCase(""))) {
                                                    submitCard();
                                                } else {
                                                    new AlertDialogWrapper.Builder(getActivity())
                                                            .setMessage("Account Not Available")
                                                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                }
                                                            }).show();
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

                            }

                        }
                    });


                    dialog_country.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                Intent intent = new Intent(getContext(), CoutryList_Activity.class);
                                intent.putExtra("status", "country");
                                startActivityForResult(intent, 32111);
                            }
                        }
                    });

                    btnclose_dailog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            }
        });
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_fundraising_donation_details, Param.get_fundraising_donation_details(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType()), 0, false, this);
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }
        return rootView;
    }

    private void saveInstantDonation() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.save_fundraising_donation_details, Param.saveFundraisingDonation(sessionManager.getEventId(), str_email, str_name, str_add, str_city, str_zip, countryName, str_amtwithoutcurr, str_edtComm, str_status), 1, false, this);
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 32111) {
                String respStatus = data.getStringExtra("status");

                if (respStatus.equalsIgnoreCase("country")) {
                    countryCode = data.getStringExtra("code");
                    countryName = data.getStringExtra("name");
                    dialog_country.setText(countryName);
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
                    Log.d("AITL Fund Donation", jsonObject.toString());

                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jObjectfund = jsonObject.getJSONObject("fundraising_data");

                        currency_status = jsonObject.getString("currency");
                        cart_count = jsonObject.getString("cart_count");


                        bids_donations_display = jObjectfund.getString("bids_donations_display");
                        target_raisedsofar_display = jObjectfund.getString("target_raisedsofar_display");
                        str_raised_price = jsonObject.getString("raised_amount");
                        str_target_amount = jObjectfund.getString("Fundraising_target");

                        Log.d("AITL raised price", str_raised_price);
                        Log.d("AITL Target price", str_target_amount);

                        str_fun_donation_first = jObjectfund.getString("fun_donation_first");
                        str_fun_donation_second = jObjectfund.getString("fun_donation_second");
                        pushnoti_display = jObjectfund.getString("pushnoti_display");


                        JSONArray jsonArraySupporter = jsonObject.getJSONArray("supporter_data");
                        for (int s = 0; s < jsonArraySupporter.length(); s++) {
                            JSONObject jObjectSuppoter = jsonArraySupporter.getJSONObject(s);
                            supporterArrayList.add(new FundraisongdonationSupporter(jObjectSuppoter.getString("id"), jObjectSuppoter.getString("name"), jObjectSuppoter.getString("donation_amount"), jObjectSuppoter.getString("time")));
                        }

                        if (jsonArraySupporter.length() == 0) {
                            rv_viewSupporter.setVisibility(View.GONE);
                        } else {
                            rv_viewSupporter.setVisibility(View.VISIBLE);
                            donationSuppAdapter = new FundrDonationSuppAdapter(supporterArrayList, getActivity(), currency_status);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            rv_viewSupporter.setLayoutManager(mLayoutManager);
                            rv_viewSupporter.setItemAnimator(new DefaultItemAnimator());
                            rv_viewSupporter.setAdapter(donationSuppAdapter);

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
                            footerArrayList.add(new FundraisingHome_footer(str_firstName, str_lastName, MyUrls.Imgurl + str_logo, str_productName, str_amt, "donation"));
                        }

                        if (currency_status.equalsIgnoreCase("euro")) {
//
                            first_donate.setText(getContext().getResources().getString(R.string.euro) + jObjectfund.getString("fun_donation_first"));
                            second_donate.setText(getContext().getResources().getString(R.string.euro) + jObjectfund.getString("fun_donation_second"));
                            txt_curreny.setText(getContext().getResources().getString(R.string.euro));

                        } else if (currency_status.equalsIgnoreCase("gbp")) {
                            first_donate.setText(getContext().getResources().getString(R.string.pound_sign) + jObjectfund.getString("fun_donation_first"));
                            second_donate.setText(getContext().getResources().getString(R.string.pound_sign) + jObjectfund.getString("fun_donation_second"));
                            txt_curreny.setText(getContext().getResources().getString(R.string.pound_sign));
                        } else if (currency_status.equalsIgnoreCase("usd") || currency_status.equalsIgnoreCase("aud")) {
                            first_donate.setText(getContext().getResources().getString(R.string.dollor) + jObjectfund.getString("fun_donation_first"));
                            second_donate.setText(getContext().getResources().getString(R.string.dollor) + jObjectfund.getString("fun_donation_second"));
                            txt_curreny.setText(getContext().getResources().getString(R.string.dollor));
                        }

                        GradientDrawable bgShape = (GradientDrawable) linear_more.getBackground();
                        bgShape.setColor(Color.parseColor(sessionManager.getFunThemeColor()));

                        GradientDrawable drawable = new GradientDrawable();
                        drawable.setShape(GradientDrawable.RECTANGLE);
                        drawable.setStroke(5, Color.parseColor(sessionManager.getFunThemeColor()));
                        linear_mainProgress.setBackgroundDrawable(drawable);

                        make_donate.setTextColor(Color.parseColor(sessionManager.getFunThemeColor()));
                        txt_submitdonate.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
                        txt_submitdonate.setTextColor(getResources().getColor(R.color.white));
                        first_donate.setTextColor(getResources().getColor(R.color.white));

                        txt_comtt.setTextColor(Color.parseColor(sessionManager.getFunThemeColor()));

                        first_donate.setStrokeWidth(1);
                        first_donate.setStrokeColor("#ffffff");
                        first_donate.setSolidColor(sessionManager.getFunThemeColor());

                        second_donate.setTextColor(getResources().getColor(R.color.white));
                        second_donate.setStrokeWidth(1);
                        second_donate.setStrokeColor("#ffffff");
                        second_donate.setSolidColor(sessionManager.getFunThemeColor());

                        if (target_raisedsofar_display.equalsIgnoreCase("1")) {

                            linear_mainProgress.setVisibility(View.VISIBLE);
                            txt_supporter.setVisibility(View.VISIBLE);
//                                linear_progress.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));

                            txt_of.setTextColor(Color.parseColor(sessionManager.getFunThemeColor()));
                            fundring_amount.setTextColor(Color.parseColor(sessionManager.getFunThemeColor()));
                            target_amount.setTextColor(Color.parseColor(sessionManager.getFunThemeColor()));

                            if (currency_status.equalsIgnoreCase("euro")) {
//
                                fundring_amount.setText(getContext().getResources().getString(R.string.euro) + str_raised_price);
                                target_amount.setText(getContext().getResources().getString(R.string.euro) + str_target_amount + " Raised!");

                            } else if (currency_status.equalsIgnoreCase("gbp")) {
                                fundring_amount.setText(getContext().getResources().getString(R.string.pound_sign) + str_raised_price);
                                target_amount.setText(getContext().getResources().getString(R.string.pound_sign) + str_target_amount + " Raised!");
                            } else if (currency_status.equalsIgnoreCase("usd") || currency_status.equalsIgnoreCase("aud")) {
                                fundring_amount.setText(getContext().getResources().getString(R.string.dollor) + str_raised_price);
                                target_amount.setText(getContext().getResources().getString(R.string.dollor) + str_target_amount + " Raised!");
                            }

                            //  target_amount.setText( str_fund_target + " Raised!");
                            fund_progress_bar.setMax(100);
                            fund_amount = Integer.parseInt(str_raised_price);
                            tar_amount = Integer.parseInt(str_target_amount);
                            if (tar_amount != 0) {
//                                    fund_progress_bar.set(ColorStateList.valueOf(Color.parseColor(str_theme_color)));
                                progress_amount = ((fund_amount * 100) / tar_amount);
                                fund_progress_bar.setProgress(progress_amount);
                            } else {
                                fund_progress_bar.setProgress(0);
                            }
                        } else {
                            txt_supporter.setVisibility(View.GONE);
                            linear_mainProgress.setVisibility(View.GONE);
                        }

                        if (bids_donations_display.equalsIgnoreCase("1")) {


                            if (footerArrayList.size() == 0) {
                                footer_pager.setVisibility(View.GONE);
                                linear_footer.setVisibility(View.GONE);
                            } else {
                                linear_footer.setVisibility(View.VISIBLE);
                                linear_footer.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
                                footer_pager.setVisibility(View.VISIBLE);
                                footer_adapter = new FundraisingHome_footer_adapter(getActivity(), footerArrayList, currency_status);
                                footer_pager.setAdapter(footer_adapter);
                            }
                        } else {
                            linear_footer.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonPayment = new JSONObject(volleyResponse.output);
                    if (jsonPayment.getString("success").equalsIgnoreCase("true")) {
                        dialog.dismiss();
                        ToastC.show(getActivity(), "Thanks for your Donation.");
                        if (sessionManager.getIsStripeEnabled().equalsIgnoreCase("0")) {
                            card_dialog.dismiss();
                            progressDialog.dismiss();
                        }
                        Log.d("AITL", jsonPayment.toString());

                        GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                        ((MainActivity) getActivity()).loadFragment();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void checkNoteStatus() {
        ((MainActivity) getActivity()).updateCartCount(cart_count);
    }


    public void submitCard() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Wait a Moment....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // TODO: replace with your own test key
        // final String publishableApiKey = "pk_test_swCQso5rudazT5aTyl1DZxkq";

        Card card = new Card(stripe_cardNumber,
                Integer.valueOf(stripe_cardMonth),
                Integer.valueOf(stripe_cardYear),
                stripe_cardCvv);

        Stripe stripe = new Stripe();
        stripe.createToken(card, Fundrising_Home_Fragment.stripePk, new TokenCallback() {
            public void onError(Exception error) {
                Log.d("Stripe", error.getLocalizedMessage());

            }

            @Override
            public void onSuccess(com.stripe.android.model.Token token) {
                String tokid = token.getId();
                str_status = "completed";
                gettoken(tokid);

            }
        });
    }

    private void gettoken(final String token) {
        Log.d("AITL", "reciving token :" + token);
        new Thread(new Runnable() {
            @Override
            public void run() {
                com.stripe.Stripe.apiKey = Fundrising_Home_Fragment.stripeSk;
                //"sk_test_MQdSDqW91LmspHHYRzZ5FNyk";
                try {
                    Map<String, Object> chargeParams = new HashMap<String, Object>();
                    chargeParams.put("amount", Integer.parseInt(str_amtwithoutcurr) * 100); // Amount in cents
                    chargeParams.put("currency", "usd");
                    chargeParams.put("source", token);
                    chargeParams.put("description", "AllInTheLoop");

                    Charge charge = Charge.create(chargeParams);
                    Log.d("AITL", "data in charge" + charge.getBalanceTransaction());
                    saveInstantDonation();
                    // call for save InstantDonation
                } catch (Exception e) {
                    // The card has been declined
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
