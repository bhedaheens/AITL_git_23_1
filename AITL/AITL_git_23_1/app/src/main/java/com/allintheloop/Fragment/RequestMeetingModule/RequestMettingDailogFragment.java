package com.allintheloop.Fragment.RequestMeetingModule;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Bean.DefaultLanguage;
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
public class RequestMettingDailogFragment extends DialogFragment implements VolleyInterface {

    Dialog dialog;
    SessionManager sessionManager;
    Bundle bundle;
    TextView txt_fullName, txt_date, txt_time;
    ArrayList<String> dateArray;
    ArrayList<String> timeArray;
    ImageView img_close;
    Button btn_rquestMetting, btn_yes, btn_no;
    String exhibitorId, str_date, str_time;
    LinearLayout linear_date, linear_time, linear_confirm;
    TextView txt_messge, txt_requestwith, txt_txtDate, txt_txtTime;


    DefaultLanguage.DefaultLang defaultLang;

    public RequestMettingDailogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_request_metting_dailog, container, false);
        txt_fullName = (TextView) rootView.findViewById(R.id.txt_fullName);
        txt_date = (TextView) rootView.findViewById(R.id.txt_date);
        txt_time = (TextView) rootView.findViewById(R.id.txt_time);
        txt_txtDate = (TextView) rootView.findViewById(R.id.txt_txtDate);
        txt_txtTime = (TextView) rootView.findViewById(R.id.txt_txtTime);
        txt_messge = (TextView) rootView.findViewById(R.id.txt_messge);
        txt_requestwith = (TextView) rootView.findViewById(R.id.txt_requestwith);
        img_close = (ImageView) rootView.findViewById(R.id.img_close);
        btn_rquestMetting = (Button) rootView.findViewById(R.id.btn_rquestMetting);
        btn_yes = (Button) rootView.findViewById(R.id.btn_yes);
        btn_no = (Button) rootView.findViewById(R.id.btn_no);
        linear_date = (LinearLayout) rootView.findViewById(R.id.linear_date);
        linear_time = (LinearLayout) rootView.findViewById(R.id.linear_time);
        linear_confirm = (LinearLayout) rootView.findViewById(R.id.linear_confirm);
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        dateArray = new ArrayList<>();
        timeArray = new ArrayList<>();

        txt_requestwith.setText(defaultLang.get2RequestAMeetingWith());
        txt_txtTime.setText(defaultLang.get2Time());
        txt_txtDate.setText(defaultLang.get2Date());

        txt_date.setText(defaultLang.get2SelectDate());
        txt_time.setText(defaultLang.get2SelectTime());
        btn_rquestMetting.setText(defaultLang.get3RequestAMeeting());


        bundle = getArguments();
        if (bundle.containsKey("exhibitorName")) {
            txt_fullName.setText(bundle.getString("exhibitorName"));
            exhibitorId = bundle.getString("exhibitorid");
        }
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeRequestMeetingDateNewUid, Param.getRequestMettingDateTimeNew(sessionManager.getEventId(), exhibitorId, sessionManager.getUserId()), 0, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeRequestMeetingDateNew, Param.getRequestMettingDateTimeNew(sessionManager.getEventId(), exhibitorId, sessionManager.getUserId()), 0, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }


        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_date.setVisibility(View.VISIBLE);
                linear_time.setVisibility(View.VISIBLE);
                linear_confirm.setVisibility(View.GONE);
                btn_rquestMetting.setVisibility(View.VISIBLE);
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    savePerformRequestMetting();
                } else {
                    ToastC.show(getActivity(), "No Internet Connection");
                }

            }
        });


        btn_rquestMetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_date = txt_date.getText().toString();
                str_time = txt_time.getText().toString();

                if (txt_date.getText().toString().equalsIgnoreCase("Select Date")) {
                    txt_date.setError("Please Select Date");
                } else if (txt_time.getText().toString().equalsIgnoreCase("Select Time")) {
                    txt_time.setError("Please Select Time");
                } else {
                    if (GlobalData.isNetworkAvailable(getActivity())) {
                        perfomrRequestMetting();
                    } else {
                        ToastC.show(getActivity(), "No Internet Connection");
                    }

                }
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    if (dateArray.size() != 0) {
                        new MaterialDialog.Builder(getActivity())
                                .title(R.string.rqtMettingdate)
                                .items(dateArray)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        txt_date.setText(dateArray.get(which).toString());
                                        getTimefromDate(dateArray.get(which).toString());
                                    }
                                })
                                .show();
                    } else {
                        ToastC.show(getActivity(), "Please Wait.....");
                    }
                } else {
                    ToastC.show(getActivity(), "No Internet Connection");
                }


            }
        });
        txt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    if (timeArray.size() != 0) {
                        new MaterialDialog.Builder(getActivity())
                                .title(R.string.rqtMettingTime)
                                .items(timeArray)
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                        txt_time.setText(timeArray.get(which).toString());
                                    }
                                })
                                .show();
                    } else {
                        ToastC.show(getActivity(), "Please Wait.....");
                    }
                } else {
                    ToastC.show(getActivity(), "No Internet Connection");
                }
            }
        });
        return rootView;
    }

    private void perfomrRequestMetting() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.exhibitorRequestMeeting, Param.requestMetting(sessionManager.getEventId(), exhibitorId, sessionManager.getUserId(), str_date, str_time), 1, true, this);
    }

    private void savePerformRequestMetting() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.exhibitorSaveMeeting, Param.requestMetting(sessionManager.getEventId(), exhibitorId, sessionManager.getUserId(), str_date, str_time), 2, true, this);
    }


    private void getTimefromDate(String date) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getTimeFromDateNew, Param.getTiemFromDate(sessionManager.getEventId(), exhibitorId, date, "1"), 3, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONArray jArrayDate = jsonObject.getJSONArray("meeting_dates");
                        for (int i = 0; i < jArrayDate.length(); i++) {
                            dateArray.add(jArrayDate.get(i).toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        getDialog().dismiss();
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                    } else {
                        if (jsonObject.getString("flag").equalsIgnoreCase("0")) {
                            linear_date.setVisibility(View.GONE);
                            linear_time.setVisibility(View.GONE);
                            linear_confirm.setVisibility(View.VISIBLE);
                            txt_messge.setText(jsonObject.getString("message"));
                            btn_rquestMetting.setVisibility(View.GONE);
                        } else {
                            ToastC.show(getActivity(), jsonObject.getString("message"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                        dialog.dismiss();
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
                        JSONArray jArrayDate = jsonObject.getJSONArray("meeting_time");
                        for (int i = 0; i < jArrayDate.length(); i++) {
                            timeArray.add(jArrayDate.get(i).toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}
