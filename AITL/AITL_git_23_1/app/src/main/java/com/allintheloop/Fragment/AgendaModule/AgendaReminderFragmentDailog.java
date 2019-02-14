package com.allintheloop.Fragment.AgendaModule;


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
import android.widget.Toast;

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
public class AgendaReminderFragmentDailog extends DialogFragment implements VolleyInterface {


    ImageView imgclose;
    Dialog dialog;
    Button btn_fiveMin, btn_fifteenMin, btn_thirtyMin, btn_noAlert;
    SessionManager sessionManager;
    Bundle bundle;
    String agendaId, remidnerTime;

    public AgendaReminderFragmentDailog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final RelativeLayout root = new RelativeLayout(getActivity());
//        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        dialog = new Dialog(getActivity());
////        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(root);
////        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transperent)));
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog.getWindow().setWindowAnimations(R.style.dialog_animation);


        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setWindowAnimations(R.style.dialog_animation);
        return dialog;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_agenda_reminder_fragment_dailog, container, false);
        imgclose = (ImageView) rootView.findViewById(R.id.imgclose);
        btn_fiveMin = (Button) rootView.findViewById(R.id.btn_fiveMin);
        btn_fifteenMin = (Button) rootView.findViewById(R.id.btn_fifteenMin);
        btn_thirtyMin = (Button) rootView.findViewById(R.id.btn_thirtyMin);
        btn_noAlert = (Button) rootView.findViewById(R.id.btn_noAlert);
        sessionManager = new SessionManager(getActivity());
        bundle = getArguments();
        if (bundle.containsKey("agendaId")) {
            agendaId = bundle.getString("agendaId");
            Log.d("AITL AgendaId", agendaId);
        }
        if (bundle.containsKey("time")) {
            remidnerTime = bundle.getString("time");
            Log.d("AITL AgendaId", remidnerTime);
        }


        if (!(remidnerTime.equalsIgnoreCase(""))) {
            if (remidnerTime.equalsIgnoreCase("0")) {
                btn_noAlert.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_accpet_layout));
            } else {
                btn_noAlert.setBackground(getActivity().getResources().getDrawable(R.drawable.survey_btn));
            }
            if (remidnerTime.equalsIgnoreCase("5")) {
                btn_fiveMin.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_accpet_layout));
            } else {
                btn_fiveMin.setBackground(getActivity().getResources().getDrawable(R.drawable.survey_btn));
            }
            if (remidnerTime.equalsIgnoreCase("15")) {
                btn_fifteenMin.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_accpet_layout));
            } else {
                btn_fifteenMin.setBackground(getActivity().getResources().getDrawable(R.drawable.survey_btn));
            }
            if (remidnerTime.equalsIgnoreCase("30")) {
                btn_thirtyMin.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_accpet_layout));
            } else {
                btn_thirtyMin.setBackground(getActivity().getResources().getDrawable(R.drawable.survey_btn));
            }

        }

        imgclose.setOnClickListener(view -> dialog.dismiss());


        btn_noAlert.setOnClickListener(view -> setReminder("0"));

        btn_thirtyMin.setOnClickListener(view -> setReminder("30"));

        btn_fifteenMin.setOnClickListener(view -> setReminder("15"));

        btn_fiveMin.setOnClickListener(view -> setReminder("5"));

        return rootView;
    }


    private void setReminder(String time) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.setAgendaRemider, Param.agendaSetreminder(sessionManager.getEventId(), sessionManager.getUserId(), agendaId, time), 0, true, this);
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
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                        ((MainActivity) getActivity()).reloadFragment();

                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
