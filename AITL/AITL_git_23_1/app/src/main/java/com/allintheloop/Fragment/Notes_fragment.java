package com.allintheloop.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notes_fragment extends Fragment {


    TextView textView;
    //  SharedPreferences preferences;
    SessionManager sessionManager;

    public Notes_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        textView = (TextView) rootView.findViewById(R.id.defult);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        //preferences=getActivity().getSharedPreferences(GlobalData.USER_PREFRENCE, Context.MODE_PRIVATE);
        sessionManager = new SessionManager(getActivity());
        if (sessionManager.isLogin()) {
            textView.setText("Under Development");
        } else {
            textView.setText(" Login or Sign Up to proceed. To sign up or login tap the Sign Up button on the top right of the screen.");
            textView.setTextSize(15);
        }
        return rootView;
    }


}
