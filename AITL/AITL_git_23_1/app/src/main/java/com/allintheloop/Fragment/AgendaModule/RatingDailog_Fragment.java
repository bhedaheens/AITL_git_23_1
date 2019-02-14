package com.allintheloop.Fragment.AgendaModule;


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
import android.widget.RatingBar;
import android.widget.TextView;

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

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class RatingDailog_Fragment extends DialogFragment implements VolleyInterface {


    TextView txt_sessionName, txt_sessionATime, txt_sessionWarning;
    RatingBar rating_dailog;
    ImageView btnclose;
    Bundle bundle;
    String agenda_id, agenda_rating, str_rating;
    SessionManager sessionManager;
    Button btn_checkin;

    public RatingDailog_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rating_dailog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        txt_sessionName = (TextView) rootView.findViewById(R.id.txt_sessionName);
        txt_sessionATime = (TextView) rootView.findViewById(R.id.txt_sessionATime);
        txt_sessionWarning = (TextView) rootView.findViewById(R.id.txt_sessionWarning);
        rating_dailog = (RatingBar) rootView.findViewById(R.id.rating_dailog);
        btnclose = (ImageView) rootView.findViewById(R.id.btnclose);
        btn_checkin = (Button) rootView.findViewById(R.id.btn_checkin);
        sessionManager = new SessionManager(getActivity());
        bundle = getArguments();


        txt_sessionName.setText(bundle.getString("name"));
        txt_sessionATime.setText(bundle.getString("time"));
        txt_sessionWarning.setText(bundle.getString("warning"));
        agenda_id = bundle.getString("agenda_id");
        if (bundle.getString("isShowCheckIn").equalsIgnoreCase("1")) {
            btn_checkin.setVisibility(View.VISIBLE);
            rating_dailog.setVisibility(View.GONE);
        } else {
            btn_checkin.setVisibility(View.GONE);
            rating_dailog.setVisibility(View.VISIBLE);
        }


        btn_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    checkin_agenda();
                } else {
                    ToastC.show(getActivity(), "No Internet Connection");
                }
            }
        });

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        rating_dailog.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
//                    ToastC.show(getContext(), "called =====");
                    agenda_rating = rating + "";
                    if (GlobalData.isNetworkAvailable(getActivity())) {
                        Rate_agenda();
                    } else {
                        ToastC.show(getActivity(), "No Internet Connection");
                    }
                }
            }
        });

        return rootView;
    }


    private void checkin_agenda() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.CheckIn_agenda, Param.Update_Agenda(sessionManager.getEventId(), sessionManager.getToken(), agenda_id, sessionManager.getUserId()), 1, true, this);
    }

    private void Rate_agenda() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.SaveRating_agenda, Param.SaveRating_agenda(sessionManager.getEventId(), sessionManager.getToken(), agenda_id, sessionManager.getUserId(), agenda_rating), 0, true, this);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {


                        str_rating = rating_dailog.getRating() + "";
                        rating_dailog.setRating(Float.parseFloat(str_rating));
                        getDialog().dismiss();
                        GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                        ((MainActivity) getActivity()).reloadFragment();
                    } else {
//                        new AlertDialogWrapper.Builder(getActivity())
//                                .setMessage(jsonObject.getString("msg").toString())
//                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                }).show();
//
//                        rating_dailog.setRating(0.0f);

                    }
                    Log.e("AITL", "rating response " + jsonObject.toString());
                } catch (Exception e) {

                }
                break;
            case 1:
                try {
                    JSONObject jsoncheckIn = new JSONObject(volleyResponse.output);
                    if (jsoncheckIn.getString("success").equalsIgnoreCase("true")) {
                        btn_checkin.setVisibility(View.GONE);
                        rating_dailog.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
