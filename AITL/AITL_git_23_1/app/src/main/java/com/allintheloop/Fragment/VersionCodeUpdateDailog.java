package com.allintheloop.Fragment;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class VersionCodeUpdateDailog extends DialogFragment {

    CheckBox chk_showagain;
    Button btn_cancel, btn_update1;
    SessionManager sessionManager;
    String checked = "";
    Dialog dialog;
    TextView txt_message;

    public VersionCodeUpdateDailog() {
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
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_version_code_update_dailog, container, false);

//        chk_showagain=(CheckBox)rootView.findViewById(R.id.chk_showagain);
//        btn_cancel=(Button) rootView.findViewById(R.id.btn_cancel);
        btn_update1 = (Button) rootView.findViewById(R.id.btn_update);
        txt_message = (TextView) rootView.findViewById(R.id.txt_message);
        String message = "You must install the latest version of " + getResources().getString(R.string.app_name) + " to use the app";
        txt_message.setText(message);
        sessionManager = new SessionManager(getActivity());


//        chk_showagain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//        {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
//            {
//                if (b)
//                {
//                    checked="1";
//                }
//                else
//                {
//                    checked="0";
//                }
//            }
//        });

//        btn_cancel.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                    Log.d("AITL ISCHECKED",checked);
//                    sessionManager.updateDialog(checked);
//                    dialog.dismiss();
//            }
//        });


        btn_update1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                Log.d("AITL PACKAGE NAME", appPackageName);
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                dialog.dismiss();
            }
        });


        return rootView;
    }

}
