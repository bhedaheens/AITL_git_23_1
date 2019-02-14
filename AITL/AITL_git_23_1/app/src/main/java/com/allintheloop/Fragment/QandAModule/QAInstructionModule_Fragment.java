package com.allintheloop.Fragment.QandAModule;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class QAInstructionModule_Fragment extends Fragment implements View.OnClickListener {


    Button btn_goBack;
    SessionManager sessionManager;

    public QAInstructionModule_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qainstruction_module_, container, false);
        initView(rootView);
        onClick();
        return rootView;
    }

    private void onClick() {
        btn_goBack.setOnClickListener(this);
    }

    private void initView(View rootView) {
        btn_goBack = (Button) rootView.findViewById(R.id.btn_goBack);
        sessionManager = new SessionManager(getActivity());
        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            btn_goBack.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            btn_goBack.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        } else {
            btn_goBack.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            btn_goBack.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goBack:
                ((MainActivity) getActivity()).fragmentBackStackMaintain();
                break;

        }
    }
}
