package com.allintheloop.Adapter.Exhibitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Fragment.ExhibitorFragment.Exhibitor_SuggestedTime_Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorRequestListing;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aiyaz on 16/12/16.
 */

public class ExhibitorRequestMettingAdapter extends RecyclerView.Adapter<ExhibitorRequestMettingAdapter.ViewHolder> implements VolleyInterface, Filterable {


    Context context;
    ArrayList<ExhibitorRequestListing> requestListingArrayList;
    ArrayList<ExhibitorRequestListing> tmprequestListingArrayList;
    GradientDrawable drawable;
    Random rnd;
    //int tmp_position;
    SessionManager sessionManager;

    public ExhibitorRequestMettingAdapter(Context context, ArrayList<ExhibitorRequestListing> requestListingArrayList) {

        this.context = context;
        this.requestListingArrayList = requestListingArrayList;
        drawable = new GradientDrawable();
        rnd = new Random();
        sessionManager = new SessionManager(context);
        tmprequestListingArrayList = new ArrayList<>();
        tmprequestListingArrayList.addAll(requestListingArrayList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exhibitor_request_list, parent, false);
        return new ExhibitorRequestMettingAdapter.ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ExhibitorRequestListing requestListingObj = tmprequestListingArrayList.get(position);
        //this.tmp_position = position;

        holder.userName.setText(requestListingObj.getFirstname() + " " + requestListingObj.getLastname());
        holder.userDesc.setText(requestListingObj.getTime() + "-" + requestListingObj.getDate());


        if (!(requestListingObj.getTxt_title().equalsIgnoreCase(""))) {
            if (!(requestListingObj.getTxt_cmpnyName().equalsIgnoreCase(""))) {
                holder.txt_cmpnyName.setVisibility(View.VISIBLE);
                holder.txt_cmpnyName.setText(requestListingObj.getTxt_title() + " at " + requestListingObj.getTxt_cmpnyName());
            } else {
                holder.txt_cmpnyName.setVisibility(View.VISIBLE);
                holder.txt_cmpnyName.setText(requestListingObj.getTxt_title());
            }
        } else {
            holder.txt_cmpnyName.setVisibility(View.GONE);
        }

        if (requestListingObj.getComment().equalsIgnoreCase("")) {
            holder.user_comment.setVisibility(View.GONE);
        } else {
            holder.user_comment.setVisibility(View.VISIBLE);
            holder.user_comment.setText(requestListingObj.getComment());
        }

        if (requestListingObj.getStatus().equalsIgnoreCase("0")) {

            holder.btn_accept.setEnabled(true);
            holder.btn_pending.setEnabled(false);
            holder.btn_pending.setVisibility(View.VISIBLE);
            holder.btn_accept.setVisibility(View.GONE);
            holder.btn_reject.setVisibility(View.GONE);
            holder.btn_newSuggestTime.setVisibility(View.GONE);
            holder.btn_addToCalendar.setVisibility(View.GONE);
            holder.linear_rejectLayout.setVisibility(View.GONE);

        } else if (requestListingObj.getStatus().equalsIgnoreCase("1")) {
            holder.btn_accept.setEnabled(false);
            holder.btn_accept.setVisibility(View.VISIBLE);
            holder.btn_reject.setVisibility(View.GONE);
            holder.btn_newSuggestTime.setVisibility(View.GONE);
            holder.btn_pending.setVisibility(View.GONE);
            holder.btn_addToCalendar.setVisibility(View.VISIBLE);
            holder.linear_rejectLayout.setVisibility(View.GONE);
            holder.btn_accept.setText("Accepted");
            holder.btn_accept.setBackground(context.getResources().getDrawable(R.drawable.share_btn));
        } else if (requestListingObj.getStatus().equalsIgnoreCase("2")) {
            holder.btn_reject.setEnabled(false);
            holder.btn_accept.setVisibility(View.GONE);
            holder.btn_reject.setVisibility(View.VISIBLE);
            holder.btn_reject.setText("Rejected");
            holder.btn_pending.setVisibility(View.GONE);
            holder.btn_newSuggestTime.setVisibility(View.GONE);
            holder.linear_rejectLayout.setVisibility(View.GONE);
            holder.btn_addToCalendar.setVisibility(View.GONE);
        } else {

            holder.btn_reject.setEnabled(true);
            holder.btn_accept.setEnabled(true);
            holder.btn_pending.setVisibility(View.GONE);
            holder.btn_newSuggestTime.setEnabled(true);
            holder.btn_accept.setVisibility(View.VISIBLE);
            holder.btn_reject.setVisibility(View.VISIBLE);
            holder.btn_newSuggestTime.setVisibility(View.GONE);
            holder.btn_addToCalendar.setVisibility(View.GONE);
        }


        holder.btn_addToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    writeCalendarEvent(requestListingObj.getDate(), requestListingObj.getTime(), requestListingObj.getFirstname() + " " + requestListingObj.getLastname());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalData.isNetworkAvailable(context)) {

                    holder.btn_accept.setEnabled(false);
                    performAccept(requestListingObj.getRequest_id(), requestListingObj.getExhibiotor_id(), "1", requestListingObj.getTag(), holder.message.getText().toString());
                    holder.btn_accept.setText("Accepted");
                    requestListingObj.setStatus("1");
                    holder.btn_accept.setBackground(context.getResources().getDrawable(R.drawable.share_btn));
                    holder.btn_reject.setVisibility(View.GONE);
                    holder.btn_newSuggestTime.setVisibility(View.GONE);
                    holder.btn_addToCalendar.setVisibility(View.VISIBLE);
                } else {
                    ToastC.show(context, "No Internet Connection");
                }
            }
        });

        holder.btn_newSuggestTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                FragmentActivity activity = (FragmentActivity) (context);
                FragmentManager fm = activity.getSupportFragmentManager();
                Log.d("AITL METTIGNID", requestListingObj.getRequest_id());
                Log.d("AITL ExhibitorID", requestListingObj.getExhibiotor_id());
                bundle.putString("mettingId", requestListingObj.getRequest_id());
                bundle.putString("exhibitorId", requestListingObj.getExhibiotor_id());
                bundle.putString("tag", requestListingObj.getTag());
                Exhibitor_SuggestedTime_Fragment fragment = new Exhibitor_SuggestedTime_Fragment();
                fragment.setArguments(bundle);
                fragment.show(fm, "Dialog Fragment");
            }
        });

        holder.btn_skipRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performAccept(requestListingObj.getRequest_id(), requestListingObj.getExhibiotor_id(), "2", requestListingObj.getTag(), "");
                requestListingArrayList.remove(position);
                tmprequestListingArrayList.remove(position);
                //ExhibitorReuqestMettingList.rv_viewMettingList.removeViewAt(tmp_position);
                notifyItemRemoved(position);
                //notifyItemRangeChanged(tmp_position, requestListingArrayList.size());
                notifyDataSetChanged();
            }
        });


        holder.btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performAccept(requestListingObj.getRequest_id(), requestListingObj.getExhibiotor_id(), "2", requestListingObj.getTag(), holder.message.getText().toString());
                requestListingArrayList.remove(position);
                tmprequestListingArrayList.remove(position);
                //ExhibitorReuqestMettingList.rv_viewMettingList.removeViewAt(tmp_position);
                notifyItemRemoved(position);
                //notifyItemRangeChanged(tmp_position, requestListingArrayList.size());
                notifyDataSetChanged();
            }
        });

        holder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (GlobalData.isNetworkAvailable(context)) {

                    holder.btn_reject.setEnabled(false);
                    holder.btn_accept.setVisibility(View.GONE);
                    holder.btn_reject.setVisibility(View.VISIBLE);
                    holder.btn_newSuggestTime.setVisibility(View.GONE);
                    holder.btn_reject.setText("Rejected");
                    holder.btn_addToCalendar.setVisibility(View.GONE);
                    holder.linear_rejectLayout.setVisibility(View.VISIBLE);
                } else {
                    ToastC.show(context, "No Internet Connection");
                }
            }
        });

        if (requestListingObj.getLogo().equalsIgnoreCase("")) {
            holder.progressBar1.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
            holder.txt_profileName.setVisibility(View.VISIBLE);
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            Log.d("AITL SPEAKER Color", "" + color);
            if (!(requestListingObj.getFirstname().equalsIgnoreCase(""))) {
                if (!(requestListingObj.getLastname().equalsIgnoreCase(""))) {
                    String name = requestListingObj.getFirstname().charAt(0) + "" + requestListingObj.getLastname().charAt(0);
                    holder.txt_profileName.setText(name);
                } else {
                    holder.txt_profileName.setText("" + requestListingObj.getFirstname().charAt(0));
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
            }
        } else {
            Glide.with(context)
                    .load(MyUrls.Imgurl + requestListingObj.getLogo())
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

    private void writeCalendarEvent(String cal_startDate, String start_time, String name) {

        int sday = 0, smonth = 0, syear = 0;
        int sHours = 0, sMin = 0;
        long startMillis;
        GregorianCalendar calDate = new GregorianCalendar();
        if (!(cal_startDate.equalsIgnoreCase(""))) {
            String[] startDateSepretor = cal_startDate.split("/");
            sday = Integer.parseInt(startDateSepretor[0]);
            smonth = Integer.parseInt(startDateSepretor[1]);
            syear = Integer.parseInt(startDateSepretor[2]);

        }

        if (!(start_time.equalsIgnoreCase(""))) {
            String[] startTimeSepretor = start_time.split(":");
            sHours = Integer.parseInt(startTimeSepretor[0]);
            sMin = Integer.parseInt(startTimeSepretor[1]);
        }
        calDate.set(syear, smonth - 1, sday, sHours, sMin);
        startMillis = calDate.getTimeInMillis();
        Calendar calendarEvent = Calendar.getInstance();
        Intent i = new Intent(Intent.ACTION_EDIT);
        i.setType("vnd.android.cursor.item/event");
        i.putExtra("beginTime", startMillis);
        i.putExtra("allDay", true);
        i.putExtra("rule", "FREQ=YEARLY");
        i.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
        i.putExtra("title", "You have a meeting with " + name);
        context.startActivity(i);
    }

    private void performAccept(String requestId, String exhibitorId, String status, String tag, String message) {
        if (tag.equalsIgnoreCase("0")) {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.exhibitorRequestResponse, Param.saveOrrejectRequestMetting(sessionManager.getEventId(), sessionManager.getUserId(), requestId, exhibitorId, status), 0, true, this);
        } else if (tag.equalsIgnoreCase("1")) {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.attendeeRequestResponse, Param.saveOrrejectAttendeeRequestMetting(sessionManager.getEventId(), sessionManager.getUserId(), requestId, exhibitorId, status, message), 0, true, this);
        }
    }

    @Override
    public int getItemCount() {
        return tmprequestListingArrayList.size();
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL SAVEORREJECT", jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public Filter getFilter() {
        return new ExhibitorRequestMettingAdapter.PortalListingFilter(this, requestListingArrayList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView userName, userDesc, txt_profileName, user_comment, txt_cmpnyName;
        //  CircularTextView txt_profileName;
        ProgressBar progressBar1;
        Button btn_accept, btn_reject;
        Button btn_newSuggestTime, btn_addToCalendar, btn_pending, btn_skipRejected, btn_sendMessage;
        LinearLayout linear_rejectLayout;
        EditText message;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.user_image);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userDesc = (TextView) itemView.findViewById(R.id.user_desc);
            txt_cmpnyName = (TextView) itemView.findViewById(R.id.txt_cmpnyName);
            user_comment = (TextView) itemView.findViewById(R.id.user_comment);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            progressBar1 = (ProgressBar) itemView.findViewById(R.id.progressBar1);
            btn_accept = (Button) itemView.findViewById(R.id.btn_accept);
            btn_reject = (Button) itemView.findViewById(R.id.btn_reject);
            btn_newSuggestTime = (Button) itemView.findViewById(R.id.btn_newSuggestTime);
            btn_addToCalendar = (Button) itemView.findViewById(R.id.btn_addToCalendar);
            btn_skipRejected = (Button) itemView.findViewById(R.id.btn_skipRejected);
            btn_sendMessage = (Button) itemView.findViewById(R.id.btn_sendMessage);
            btn_pending = (Button) itemView.findViewById(R.id.btn_pending);
            message = (EditText) itemView.findViewById(R.id.message);
            linear_rejectLayout = (LinearLayout) itemView.findViewById(R.id.linear_rejectLayout);

        }
    }


    private static class PortalListingFilter extends Filter {
        ExhibitorRequestMettingAdapter portalListingAdapter;
        ArrayList<ExhibitorRequestListing> requestListingArrayList;
        ArrayList<ExhibitorRequestListing> tmprequestListingArrayList;


        public PortalListingFilter(ExhibitorRequestMettingAdapter portalListingAdapter, ArrayList<ExhibitorRequestListing> requestListingArrayList) {
            this.portalListingAdapter = portalListingAdapter;
            this.requestListingArrayList = requestListingArrayList;
            tmprequestListingArrayList = new ArrayList<>();
            //this.postiion = postiion;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults results = new FilterResults();
            if (charSequence.length() <= 0) {
                tmprequestListingArrayList.addAll(requestListingArrayList);
            } else {
                String filterString = charSequence.toString().toLowerCase();
                for (ExhibitorRequestListing portalObj1 : requestListingArrayList) {
                    String title = portalObj1.getFirstname().toLowerCase() + " " + portalObj1.getLastname().toLowerCase();
                    if (title.contains(filterString)) {
                        tmprequestListingArrayList.add(portalObj1);
                    }
                }
            }

            results.values = tmprequestListingArrayList;
            results.count = tmprequestListingArrayList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            portalListingAdapter.tmprequestListingArrayList.clear();
            portalListingAdapter.tmprequestListingArrayList.addAll((ArrayList<ExhibitorRequestListing>) filterResults.values);
            portalListingAdapter.notifyDataSetChanged();
        }
    }

}
