package com.allintheloop.Fragment.ExhibitorFragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
public class Exhibitor_SuggestedTime_Fragment extends DialogFragment implements VolleyInterface {


    TextView txt_firstdate, txt_firstTime, txt_secondDate, txt_secondTime, txt_thirdDate, txt_thirdTime;
    Button btn_rquestMetting;
    Dialog dialog;
    SessionManager sessionManager;
    Bundle bundle;
    String mettingId = "", exhibitorId = "", str_tag = "";
    ArrayList<String> arrayDateList;
    ArrayList<String> arrayTimeList;
    ImageView img_close;
    JSONArray dateput;
    JSONArray timePut;

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

    public Exhibitor_SuggestedTime_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exhibitor__suggested_time, container, false);
        txt_firstdate = (TextView) view.findViewById(R.id.txt_firstdate);
        txt_firstTime = (TextView) view.findViewById(R.id.txt_firstTime);
        txt_secondDate = (TextView) view.findViewById(R.id.txt_secondDate);
        txt_secondTime = (TextView) view.findViewById(R.id.txt_secondTime);
        txt_thirdDate = (TextView) view.findViewById(R.id.txt_thirdDate);
        txt_thirdTime = (TextView) view.findViewById(R.id.txt_thirdTime);
        img_close = (ImageView) view.findViewById(R.id.img_close);
        btn_rquestMetting = (Button) view.findViewById(R.id.btn_rquestMetting);
        dateput = new JSONArray();
        timePut = new JSONArray();
        arrayDateList = new ArrayList<>();
        arrayTimeList = new ArrayList<>();
        bundle = getArguments();
        if (bundle.containsKey("mettingId")) {
            mettingId = bundle.getString("mettingId");
        }
        if (bundle.containsKey("exhibitorId")) {

            exhibitorId = bundle.getString("exhibitorId");

        }
        if (bundle.containsKey("tag")) {
            str_tag = bundle.getString("tag");
        }

        Log.d("AITL TAG ", "ATTENDEE" + str_tag);
        sessionManager = new SessionManager(getActivity());

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (str_tag.equalsIgnoreCase("0")) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getSuggestedTimings, Param.getNewSuggestedTime(sessionManager.getEventId(), sessionManager.getUserId(), mettingId), 0, false, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.attendeegetSuggestedTimings, Param.getNewSuggestedTime(sessionManager.getEventId(), sessionManager.getUserId(), mettingId), 0, false, this);
            }
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }

        txt_firstdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadDate("first");
            }
        });
        txt_firstTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTime("first");
            }
        });
        txt_secondDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDate("second");
            }
        });
        txt_secondTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTime("second");
            }
        });
        txt_thirdDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDate("third");
            }
        });
        txt_thirdTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTime("third");
            }
        });


        btn_rquestMetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dateput.length() == 0) {
                    ToastC.show(getActivity(), "Please Select Minimum One Date");
                } else if (timePut.length() == 0) {
                    ToastC.show(getActivity(), "Please Select Minimum One Time");
                } else {

                    requestSuggestTime();
                    Log.d("AITL JARRAYDATE", dateput.toString());
                    Log.d("AITL JARRAYTIME", timePut.toString());
                }


            }
        });

        return view;

    }

    private void requestSuggestTime() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (str_tag.equalsIgnoreCase("0")) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.suggestedTime, Param.suggestedTime(sessionManager.getEventId(), sessionManager.getUserId(), mettingId, exhibitorId, dateput.toString(), timePut.toString()), 1, true, this);
            } else if (str_tag.equalsIgnoreCase("1")) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.attendeeSugeestTime, Param.attendeesuggestedTime(sessionManager.getEventId(), sessionManager.getUserId(), mettingId, exhibitorId, dateput.toString(), timePut.toString()), 1, true, this);
            }
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void loadDate(final String date) {
        if (arrayDateList.size() != 0) {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.rqtMettingdate)
                    .items(arrayDateList)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (date.equalsIgnoreCase("first")) {
                                txt_firstdate.setText(arrayDateList.get(which).toString());
                                dateput.put(txt_firstdate.getText().toString());

                            } else if (date.equalsIgnoreCase("second")) {
                                txt_secondDate.setText(arrayDateList.get(which).toString());
                                dateput.put(txt_secondDate.getText().toString());

                            } else if (date.equalsIgnoreCase("third")) {
                                txt_thirdDate.setText(arrayDateList.get(which).toString());
                                dateput.put(txt_thirdDate.getText().toString());
                            }

                        }
                    })
                    .show();
        } else {
            ToastC.show(getActivity(), "Please Wait......");
        }

    }

    private void loadTime(final String time) {
        if (arrayTimeList.size() != 0) {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.rqtMettingdate)
                    .items(arrayTimeList)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (time.equalsIgnoreCase("first")) {
                                txt_firstTime.setText(arrayTimeList.get(which).toString());
                                timePut.put(txt_firstTime.getText().toString());
                            } else if (time.equalsIgnoreCase("second")) {
                                txt_secondTime.setText(arrayTimeList.get(which).toString());
                                timePut.put(txt_secondTime.getText().toString());

                            } else if (time.equalsIgnoreCase("third")) {
                                txt_thirdTime.setText(arrayTimeList.get(which).toString());
                                timePut.put(txt_thirdTime.getText().toString());
                            }
                        }
                    })
                    .show();
        } else {
            ToastC.show(getActivity(), "Please wait.....");
        }

    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL OBJECT", jsonObject.toString());
                        arrayDateList.clear();
                        arrayTimeList.clear();

                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONArray jArrayDate = jsonData.getJSONArray("date");
                        JSONArray jArrayTime = jsonData.getJSONArray("time");

                        for (int d = 0; d < jArrayDate.length(); d++) {
                            arrayDateList.add(new String(jArrayDate.getString(d).toString()));
                        }

                        for (int t = 0; t < jArrayTime.length(); t++) {
                            arrayTimeList.add(new String(jArrayTime.getString(t).toString()));
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
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                        dialog.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
