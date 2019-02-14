package com.allintheloop.Fragment.ActivityModule;


import android.app.Activity;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.SessionManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityShareFragment_Dailog extends DialogFragment {


    ImageView img_close, img_fb_share, img_twitter_share, img_linkedin_share;
    Dialog dialog;
    Bundle bundle;
    Activity activity;
    String fbshareUrl, twitterShareurl, LinkedinShareUrl, url;
    SessionManager sessionManager;
    DefaultLanguage.DefaultLang defaultLang;
    TextView txt_sharepost;

    public ActivityShareFragment_Dailog() {
        // Required empty public constructor
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        activity = getActivity();
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_layout);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activity_share_fragment__dailog, container, false);
        img_close = (ImageView) rootView.findViewById(R.id.img_close);
        img_fb_share = (ImageView) rootView.findViewById(R.id.img_fb_share);
        img_twitter_share = (ImageView) rootView.findViewById(R.id.img_twitter_share);
        txt_sharepost = (TextView) rootView.findViewById(R.id.txt_sharepost);
        img_linkedin_share = (ImageView) rootView.findViewById(R.id.img_linkedin_share);
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        txt_sharepost.setText(defaultLang.get45ShareThisPost());
        bundle = getArguments();
        if (bundle.containsKey("FbShareUrl")) {
            fbshareUrl = bundle.getString("FbShareUrl");
        }
        if (bundle.containsKey("twitterShareUrl")) {
            twitterShareurl = bundle.getString("twitterShareUrl");
        }
        if (bundle.containsKey("linkedInUrl")) {
            LinkedinShareUrl = bundle.getString("linkedInUrl");
        }
        if (bundle.containsKey("url")) {
            url = bundle.getString("url");
        }

        if (url.equalsIgnoreCase("")) {
            img_fb_share.setVisibility(View.GONE);
            img_twitter_share.setVisibility(View.GONE);
            img_linkedin_share.setVisibility(View.GONE);
        } else {
            img_fb_share.setVisibility(View.VISIBLE);
            img_twitter_share.setVisibility(View.VISIBLE);
            img_linkedin_share.setVisibility(View.VISIBLE);
        }

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        img_fb_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent insta = new Intent(Intent.ACTION_SEND);
                insta.setType("text/plain");
                insta.setPackage("com.facebook.katana");
                insta.putExtra(Intent.EXTRA_TEXT, url);
                if (isIntentAvailable(getActivity(), insta)) {
                    startActivity(insta);
                } else {
                    showDialog("Facebook", "com.facebook.katana");
                }

            }
        });

        img_twitter_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent insta = new Intent(Intent.ACTION_SEND);
                insta.setPackage("com.twitter.android");
//                insta.setClassName("com.twitter.android", "com.twitter.android.composer.ComposerActivity");
                insta.setType("text/plain");
                insta.putExtra(Intent.EXTRA_TEXT, url);
                if (isIntentAvailable(getActivity(), insta)) {
                    startActivity(insta);
                } else {
                    showDialog("Twitter", "com.twitter.android");
                }

            }
        });

        img_linkedin_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                Uri uri = Uri.parse(LinkedinShareUrl);
                Intent insta = new Intent(Intent.ACTION_SEND);
                insta.setType("text/plain");
                insta.setPackage("com.linkedin.android");
                insta.putExtra(Intent.EXTRA_TEXT, url);
                if (isIntentAvailable(getActivity(), insta)) {
                    startActivity(insta);
                } else {
                    showDialog("LinkedIn", "com.linkedin.android");
                }
            }
        });

        return rootView;
    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void showDialog(String app, final String packageName) {
        String msg = "Please download " + app + " to continue";
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Download App " + app)
                .items(msg)
                .positiveColor(getResources().getColor(R.color.colorAccent))
                .positiveText("Continue")
                .negativeText(getResources().getString(R.string.cancelText))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        openPlaystore(packageName);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .cancelable(false)
                .build();

        dialog.show();
    }

    private void openPlaystore(String packageName) {
        Intent viewIntent =
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + packageName));
        activity.startActivity(viewIntent);
    }
}
