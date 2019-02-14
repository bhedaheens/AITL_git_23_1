package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Bean.checkInProtal;
import com.allintheloop.Fragment.AttandeeFragments.Attendee_edit_FragmentDailog;
import com.allintheloop.Fragment.CheckIn_Portal_Fragment;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Aiyaz on 5/12/16.
 */

public class PortalListingAdapter extends RecyclerView.Adapter<PortalListingAdapter.Viewholder> implements VolleyInterface, Filterable {
    ArrayList<checkInProtal> checkIn_portalArrayList;
    ArrayList<checkInProtal> tmpCheckIn_portalArrayList;
    Context context;
    SessionManager sessionManager;
    GradientDrawable drawable;
    int tmp_position;
    int i = 0;
    CheckIn_Portal_Fragment fragment;

    public PortalListingAdapter(ArrayList<checkInProtal> checkIn_portalArrayList, Context context, CheckIn_Portal_Fragment fragment) {
        this.checkIn_portalArrayList = checkIn_portalArrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
        tmpCheckIn_portalArrayList = new ArrayList<>();
        tmpCheckIn_portalArrayList.addAll(checkIn_portalArrayList);
        this.fragment = fragment;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_checkin_portal, parent, false);
        return new Viewholder(rootView);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {
        final checkInProtal checkObj = tmpCheckIn_portalArrayList.get(position);
        this.tmp_position = position;
        GradientDrawable drawable = new GradientDrawable();
        Random rnd = new Random();
        holder.userName.setText(checkObj.getFirstname() + " " + checkObj.getLastname());


        if (!(checkObj.getTitle().equalsIgnoreCase(""))) {
            if (!(checkObj.getCompany_name().equalsIgnoreCase(""))) {

                holder.userDesc.setText(checkObj.getTitle() + " " + "at" + " " + checkObj.getCompany_name());
            }
        }

        if (checkObj.getIs_checked_in().equalsIgnoreCase("1")) {
            holder.btn_checkin.setText("Check Out");
            holder.btn_checkin.setBackground(context.getResources().getDrawable(R.drawable.share_btn));
        } else {
            holder.btn_checkin.setText("Check In");
            holder.btn_checkin.setBackground(context.getResources().getDrawable(R.drawable.survey_btn));
        }

        holder.layout_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment.openPDFView(checkObj.getId());
//                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
//                GlobalData.CURRENT_FRAG = GlobalData.OpenPdfFragment;
//                sessionManager.checkingAttendeeID = checkObj.getId();
//                ((MainActivity) context).loadFragment();
            }
        });

        holder.btn_moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentActivity activity = (FragmentActivity) (context);
                FragmentManager fm = activity.getSupportFragmentManager();
                sessionManager.portalCheckInAttenDeeId = checkObj.getId();
                Attendee_edit_FragmentDailog alertDialog = new Attendee_edit_FragmentDailog();
                alertDialog.show(fm, "fragment_alert");
            }
        });

        holder.btn_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalData.isNetworkAvailable(context)) {

                    if (checkObj.getIs_checked_in().equalsIgnoreCase("1")) {
                        checkInProtal(checkObj.getId());
                        checkObj.setIs_checked_in("0");
                        holder.btn_checkin.setText("Check In");
                        holder.btn_checkin.setBackground(context.getResources().getDrawable(R.drawable.survey_btn));
                    } else {
                        checkInProtal(checkObj.getId());
                        checkObj.setIs_checked_in("1");
                        holder.btn_checkin.setText("Check Out");
                        holder.btn_checkin.setBackground(context.getResources().getDrawable(R.drawable.share_btn));
                    }
                } else {
                    ToastC.show(context, "No Internet Connection");
                }
            }
        });


        if (checkObj.getLogo().equalsIgnoreCase("")) {
            holder.progressBar1.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
            holder.txt_profileName.setVisibility(View.VISIBLE);
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            Log.d("Bhavdip SPEAKER Color", "" + color);
            if (!(checkObj.getFirstname().equalsIgnoreCase(""))) {
                if (!checkObj.getLastname().equalsIgnoreCase("")) {
                    holder.txt_profileName.setText("" + checkObj.getFirstname().charAt(0) + "" + checkObj.getLastname().charAt(0));
                } else {
                    holder.txt_profileName.setText("" + checkObj.getFirstname().charAt(0));
                }
            } else {
                holder.txt_profileName.setText("");
            }

            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                drawable.setShape(GradientDrawable.OVAL);
                drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                holder.txt_profileName.setBackgroundDrawable(drawable);
                holder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            } else {
                drawable.setShape(GradientDrawable.OVAL);
                drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                holder.txt_profileName.setBackgroundDrawable(drawable);
                holder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            }
        } else {
            Glide.with(context)
                    .load(MyUrls.Imgurl + checkObj.getLogo())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progressBar1.setVisibility(View.GONE);
                            holder.imageView.setVisibility(View.GONE);
                            holder.txt_profileName.setVisibility(View.VISIBLE);
//                                Glide.with(context).load(companyLogourl).centerCrop().fitCenter().placeholder(R.drawable.defult_attende).into(holder.imageView);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar1.setVisibility(View.GONE);
                            holder.imageView.setVisibility(View.VISIBLE);
                            holder.txt_profileName.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.imageView);
        }

    }

    private void checkInProtal(String id) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.checkInPortal, Param.checkInPortal(sessionManager.getEventId(), id), 0, true, this);
    }

    @Override
    public int getItemCount() {
        return tmpCheckIn_portalArrayList.size();
    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsondata = jsonObject.getJSONObject("data");
                        Log.d("Bhavdip CHECKINDATA", jsondata.toString());
                        if (jsondata.getString("is_checked_in").equalsIgnoreCase("1")) {
//                                PortalListingAdapter.Viewholder.btn_checkin.setBackground(context.getResources().getDrawable(R.drawable.share_btn));
                        } else {

//                                PortalListingAdapter.Viewholder.btn_checkin.setBackground(context.getResources().getDrawable(R.drawable.survey_btn));
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public Filter getFilter() {
        return new PortalListingFilter(this, checkIn_portalArrayList, tmp_position);
    }

    private static class PortalListingFilter extends Filter {
        PortalListingAdapter portalListingAdapter;
        ArrayList<checkInProtal> checkIn_portalArrayList;
        ArrayList<checkInProtal> tmpCheckIn_portalArrayList;
        int postiion;

        public PortalListingFilter(PortalListingAdapter portalListingAdapter, ArrayList<checkInProtal> checkIn_portalArrayList, int postiion) {
            this.portalListingAdapter = portalListingAdapter;
            this.checkIn_portalArrayList = checkIn_portalArrayList;
            tmpCheckIn_portalArrayList = new ArrayList<>();
            this.postiion = postiion;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults results = new FilterResults();
            checkInProtal portalObj = checkIn_portalArrayList.get(postiion);
            if (charSequence.length() <= 0) {
                tmpCheckIn_portalArrayList.addAll(checkIn_portalArrayList);
            } else {
                String filterString = charSequence.toString().toLowerCase();
                for (checkInProtal portalObj1 : checkIn_portalArrayList) {
                    String title = portalObj1.getFirstname().toLowerCase() + " " + portalObj1.getLastname().toLowerCase();
                    if (title.contains(filterString)) {
                        tmpCheckIn_portalArrayList.add(portalObj1);
                    }
                }
            }

            results.values = tmpCheckIn_portalArrayList;
            results.count = tmpCheckIn_portalArrayList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            portalListingAdapter.tmpCheckIn_portalArrayList.clear();
            portalListingAdapter.tmpCheckIn_portalArrayList.addAll((ArrayList<checkInProtal>) filterResults.values);
            portalListingAdapter.notifyDataSetChanged();
        }
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView userName, userDesc, txt_profileName;
        //  CircularTextView txt_profileName;
        ProgressBar progressBar1;
        LinearLayout layout_print;
        Button btn_checkin, btn_moreInfo;

        public Viewholder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.user_image);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userDesc = (TextView) itemView.findViewById(R.id.user_desc);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            progressBar1 = (ProgressBar) itemView.findViewById(R.id.progressBar1);
            btn_checkin = (Button) itemView.findViewById(R.id.btn_checkin);
            layout_print = (LinearLayout) itemView.findViewById(R.id.layout_print);
            btn_moreInfo = (Button) itemView.findViewById(R.id.btn_moreInfo);
        }
    }
}