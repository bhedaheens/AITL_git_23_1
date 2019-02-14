package com.allintheloop.Fragment.AttandeeFragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
public class AttendeeRequestMettingDialog extends DialogFragment implements VolleyInterface {


    Dialog dialog;
    TextView txt_fullName, txt_date, txt_time, txt_messge, txt_requestWith;
    Button btn_rquestMetting, btn_yes, btn_no;
    TextView txt_location;
    TextView txt_txtdate, txt_txtTime, txt_txtLocation;
    Bundle bundle;
    String str_fullName, attendeeId;
    ImageView img_close;
    ArrayList<String> dateArray;
    ArrayList<String> timeArray;
    ArrayList<String> locationArray;
    String str_date, str_time, str_location;
    SessionManager sessionManager;
    LinearLayout linear_confirm, linear_main, linear_location;

    DefaultLanguage.DefaultLang defaultLang;
    String date = "";

    public AttendeeRequestMettingDialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_attendee_request_metting_dialog, container, false);
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        txt_fullName = (TextView) rootView.findViewById(R.id.txt_fullName);
        txt_date = (TextView) rootView.findViewById(R.id.txt_date);
        bundle = getArguments();
        txt_time = (TextView) rootView.findViewById(R.id.txt_time);
        txt_messge = (TextView) rootView.findViewById(R.id.txt_messge);
        txt_txtdate = (TextView) rootView.findViewById(R.id.txt_txtdate);
        txt_txtTime = (TextView) rootView.findViewById(R.id.txt_txtTime);
        txt_requestWith = (TextView) rootView.findViewById(R.id.txt_requestWith);
        txt_txtLocation = (TextView) rootView.findViewById(R.id.txt_txtLocation);
        txt_messge = (TextView) rootView.findViewById(R.id.txt_messge);
        linear_main = (LinearLayout) rootView.findViewById(R.id.linear_main);
        linear_confirm = (LinearLayout) rootView.findViewById(R.id.linear_confirm);
        linear_location = (LinearLayout) rootView.findViewById(R.id.linear_location);
        txt_location = (TextView) rootView.findViewById(R.id.txt_location);
        img_close = (ImageView) rootView.findViewById(R.id.img_close);
        dateArray = new ArrayList<>();
        timeArray = new ArrayList<>();
        locationArray = new ArrayList<>();
        btn_rquestMetting = (Button) rootView.findViewById(R.id.btn_rquestMetting);
        btn_yes = rootView.findViewById(R.id.btn_yes);
        btn_no = (Button) rootView.findViewById(R.id.btn_no);

        txt_txtdate.setText(defaultLang.get2Date());
        txt_txtTime.setText(defaultLang.get2Time());
        txt_txtLocation.setText(defaultLang.get2Location());

        txt_date.setText(defaultLang.get2SelectDate());
        txt_time.setText(defaultLang.get2SelectTime());
        txt_location.setText(defaultLang.get2EnterMeetingLocation());
        txt_requestWith.setText(defaultLang.get2RequestAMeetingWith());

        if (sessionManager.getShowMeetingLocation().equalsIgnoreCase("0")) {
            linear_location.setVisibility(View.GONE);
        } else {
            linear_location.setVisibility(View.VISIBLE);
        }

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_main.setVisibility(View.VISIBLE);
                btn_rquestMetting.setVisibility(View.VISIBLE);
                linear_confirm.setVisibility(View.GONE);
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

        if (bundle.containsKey("attendeeName")) {
            str_fullName = bundle.getString("attendeeName");
            txt_fullName.setText(str_fullName);
        }
        if (bundle.containsKey("attendeeId")) {
            attendeeId = bundle.getString("attendeeId");

        }
        if (bundle.containsKey("jsonDateData")) {
            try {
                JSONObject getjsonDateData = new JSONObject(bundle.getString("jsonDateData"));

                JSONArray jArrayDate = getjsonDateData.getJSONArray("meeting_dates");
                for (int i = 0; i < jArrayDate.length(); i++) {
                    dateArray.add(jArrayDate.get(i).toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//
        }
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        btn_rquestMetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_date = txt_date.getText().toString();
                str_time = txt_time.getText().toString();
                str_location = txt_location.getText().toString();
                if (str_location.equalsIgnoreCase(defaultLang.get2EnterMeetingLocation())) {
                    str_location = "";
                }
                if (txt_date.getText().toString().equalsIgnoreCase("Select Date")) {
                    txt_date.setError("Please Select Date");
                } else if (txt_time.getText().toString().equalsIgnoreCase("Select Time")) {
                    txt_time.setError("Please Select Time");
                } else {
                    performRequestMetting();
                }

            }
        });

        txt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (locationArray.size() != 0) {
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.select_locationEnter)
                            .items(locationArray)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    txt_location.setText(locationArray.get(which).toString());
                                }
                            })
                            .show();
                } else {
                    ToastC.show(getActivity(), "Please Select Date and Time First");
                }

            }
        });


        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateArray.size() > 0) {
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.rqtMettingdate)
                            .items(dateArray)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    txt_date.setText(dateArray.get(which).toString());
                                    date = "";
                                    date = dateArray.get(which).toLowerCase();
                                    getTimefromDate(dateArray.get(which).toString());
                                }
                            })
                            .show();
                } else {
                    ToastC.show(getActivity(), "Please Wait...");
                }

            }
        });
        txt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeArray.size() > 0) {
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.rqtMettingTime)
                            .items(timeArray)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    txt_time.setText(timeArray.get(which).toString());
                                    if (sessionManager.getShowMeetingLocation().equalsIgnoreCase("1")) {
                                        getLocationfromDateanaTime(date, timeArray.get(which).toString());
                                    }
                                }
                            })
                            .show();
                } else {
                    ToastC.show(getActivity(), "Please select Date First");
                }
            }
        });
        return rootView;
    }

    private void getTimefromDate(String date) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getTimeFromDateNew, Param.getTiemFromDate(sessionManager.getEventId(), attendeeId, date, ""), 3, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void getLocationfromDateanaTime(String date, String time) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getRequestMeetingLocation, Param.getLocationFromDateTime(sessionManager.getEventId(), sessionManager.getUserId(), date, time), 4, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }


    private void performRequestMetting() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.attendeeRequestMeetingUid, Param.attendeerequestMettingUid(sessionManager.getEventId(), attendeeId, sessionManager.getUserId(), str_date, str_time, str_location), 1, true, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.attendeeRequestMeeting, Param.attendeerequestMetting(sessionManager.getEventId(), attendeeId, sessionManager.getUserId(), str_date, str_time, str_location, sessionManager.getRolId()), 1, true, this); //change applied
            }
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void savePerformRequestMetting() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.attendeeSaveMeetingUid, Param.attendeerequestMettingUid(sessionManager.getEventId(), attendeeId, sessionManager.getUserId(), str_date, str_time, str_location), 2, true, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.attendeeSaveMeeting, Param.attendeerequestMetting(sessionManager.getEventId(), attendeeId, sessionManager.getUserId(), str_date, str_time, str_location, sessionManager.getRolId()), 2, true, this);//changes applied
            }
        } else
            ToastC.show(getActivity(), getString(R.string.noInernet));
    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        getDialog().dismiss();
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                    } else {
                        if (jsonObject.getString("flag").equalsIgnoreCase("0")) {
                            linear_main.setVisibility(View.GONE);
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
                        timeArray.clear();
                        JSONArray jArrayTime = jsonObject.getJSONArray("meeting_time");
                        for (int i = 0; i < jArrayTime.length(); i++) {
                            timeArray.add(jArrayTime.get(i).toString());
                        }
                    } else {
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 4:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        locationArray.clear();
                        JSONArray jArrayTime = jsonObject.getJSONArray("meeting_location");
                        for (int i = 0; i < jArrayTime.length(); i++) {
                            locationArray.add(jArrayTime.get(i).toString());
                        }
                    } else {
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }
}
