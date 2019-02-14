package com.allintheloop.Fragment.InstagramModule;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.allintheloop.Activity.CoutryList_Activity;
import com.allintheloop.Fragment.FundraisingModule.Fundrising_Home_Fragment;
import com.allintheloop.R;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstatDonation_Dialog_Fragment extends DialogFragment implements VolleyInterface {

    TextView txt_currSign;
    EditText edt_amt, edt_commt;
    LinearLayout instantdonation;
    Button btn_paywithCard;
    Button carddialog_btnPayment;
    ImageView btnclose, btnclose_dailog, btncardclose_dailog;
    String str_amt, str_email, str_name, str_add, str_zip, str_city, str_cmt = "", str_status = "completed";
    SessionManager sessionManager;
    public static String currency_status;
    EditText dialog_email, dialog_name, dialog_address, dialog_zip, dialog_city, dialog_country;
    Button dialog_btnPayment;
    String stripe_cardNumber, stripe_cardMonth, stripe_cardYear, stripe_cardCvv;
    EditText cedt_cardholderNumber, cedt_cardMonth, cedt_cardYear, cedt_cvvnumber;
    public String countryName, countryCode = "n/a";
    ProgressDialog progressDialog;
    Dialog dialog, card_dialog;
    int year, edt_year, edt_month, month;

    public InstatDonation_Dialog_Fragment() {
        // Required empty public constructor
    }

    String str_year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_instatdonation, container, false);

        sessionManager = new SessionManager(getActivity());
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        txt_currSign = (TextView) rootView.findViewById(R.id.txt_currSign);
        edt_amt = (EditText) rootView.findViewById(R.id.edt_amt);
        edt_commt = (EditText) rootView.findViewById(R.id.edt_commt);

        btn_paywithCard = (Button) rootView.findViewById(R.id.btn_paywithCard);
        btnclose = (ImageView) rootView.findViewById(R.id.btnclose);

        instantdonation = (LinearLayout) rootView.findViewById(R.id.instantdonation);


        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btn_paywithCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_amt = edt_amt.getText().toString();
                str_cmt = edt_commt.getText().toString();
                if (str_amt.trim().length() == 0) {
                    ToastC.show(getActivity(), "Please Enter Amount");
                } else {
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

                                if (sessionManager.getIsStripeEnabled().equalsIgnoreCase("1")) {
                                    saveInstantDonation();
                                } else if (sessionManager.getIsStripeEnabled().equalsIgnoreCase("0")) {

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
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Fundrising_Home, Param.fundrising_home(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId()), 0, false, this);
        }
        return rootView;
    }

    private void saveInstantDonation() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.save_instant_donation_details, Param.saveInstantDonation(sessionManager.getEventId(), str_email, str_name, str_add, str_city, str_zip, countryName, str_amt, str_cmt, str_status), 1, false, this);
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

                        currency_status = jsonObject.getString("currency");

                        JSONArray jsonArray = jsonObject.optJSONArray("events");
                        for (int e = 0; e < jsonArray.length(); e++) {
                            JSONObject jObjectevent = (JSONObject) jsonArray.get(e);
                            Log.d("AITL", "jObjectevent" + jObjectevent);
                            sessionManager.appColor(jObjectevent);
                        }
                        setColor();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonPayment = new JSONObject(volleyResponse.output);
                    if (jsonPayment.getString("success").equalsIgnoreCase("true")) {
                        getDialog().dismiss();
                        dialog.dismiss();
                        card_dialog.dismiss();
                        progressDialog.dismiss();
                        progressDialog.dismiss();
                        Log.d("AITL", jsonPayment.toString());


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
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

    private void setColor() {

        if (currency_status.equalsIgnoreCase("euro")) {
            txt_currSign.setText(getActivity().getResources().getString(R.string.euro));
        } else if (currency_status.equalsIgnoreCase("gbp")) {
            txt_currSign.setText(getActivity().getResources().getString(R.string.pound_sign));
        } else if (currency_status.equalsIgnoreCase("usd") || currency_status.equalsIgnoreCase("aud")) {
            txt_currSign.setText(getActivity().getResources().getString(R.string.dollor));
        }

    }

    public void submitCard() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Wait a Moment....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // TODO: replace with your own test key
        //  final String publishableApiKey = "pk_test_swCQso5rudazT5aTyl1DZxkq";

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
                    chargeParams.put("amount", Integer.parseInt(str_amt) * 100); // Amount in cents
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
