package com.allintheloop.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.ModeratorRequestListing;
import com.allintheloop.Fragment.RequestMeetingModule.Moderator_SuggestedTime_Fragment;
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
import java.util.Random;


/**
 * Created by Aiyaz on 25/5/17.
 */

public class ModeratorRequestMeetingAdapter extends RecyclerView.Adapter<ModeratorRequestMeetingAdapter.ViewHolder> implements VolleyInterface, Filterable {


    Context context;
    ArrayList<ModeratorRequestListing> requestListingArrayList;
    ArrayList<ModeratorRequestListing> tmprequestListingArrayList;
    GradientDrawable drawable;
    Random rnd;
    //int tmp_position;
    SessionManager sessionManager;

    public ModeratorRequestMeetingAdapter(Context context, ArrayList<ModeratorRequestListing> requestListingArrayList) {

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
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_moderator_request_list, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ModeratorRequestListing requestListingObj = tmprequestListingArrayList.get(position);
        //this.tmp_position = position;

        holder.userName.setText(requestListingObj.getSender_name() + " with " + requestListingObj.getReciver_name());
        holder.userDesc.setText(requestListingObj.getTime() + "-" + requestListingObj.getDate());


        if (requestListingObj.getStatus().equalsIgnoreCase("0")) {
            holder.btn_accept.setClickable(true);
            holder.btn_accept.setVisibility(View.VISIBLE);
            holder.btn_reject.setVisibility(View.VISIBLE);
            holder.btn_accept.setText("Accept");
            holder.btn_newSuggestTime.setVisibility(View.VISIBLE);
            holder.btn_addComment.setVisibility(View.VISIBLE);
            holder.btn_accept.setBackground(context.getResources().getDrawable(R.drawable.btn_accpet_layout));

        } else {
            holder.btn_accept.setClickable(false);
            holder.btn_accept.setVisibility(View.VISIBLE);
            holder.btn_reject.setVisibility(View.GONE);
            holder.btn_newSuggestTime.setVisibility(View.GONE);
            holder.btn_addComment.setVisibility(View.GONE);
            holder.btn_accept.setText("Accepted");
            holder.btn_accept.setBackground(context.getResources().getDrawable(R.drawable.share_btn));
        }

        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalData.isNetworkAvailable(context)) {

                    holder.btn_accept.setClickable(false);
                    performAccept(requestListingObj.getRequest_id(), requestListingObj.getSender_id(), requestListingObj.getReciver_id(), "1");
                    holder.btn_accept.setText("Accepted");
                    requestListingObj.setStatus("1");
                    holder.btn_accept.setBackground(context.getResources().getDrawable(R.drawable.share_btn));
                    holder.btn_reject.setVisibility(View.GONE);
                    holder.btn_addComment.setVisibility(View.GONE);
                    holder.btn_newSuggestTime.setVisibility(View.GONE);
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

                bundle.putString("requestId", requestListingObj.getRequest_id());
                bundle.putString("reciverId", requestListingObj.getReciver_id());
                bundle.putString("senderId", requestListingObj.getSender_id());

                Moderator_SuggestedTime_Fragment fragment = new Moderator_SuggestedTime_Fragment();
                fragment.setArguments(bundle);
                fragment.show(fm, "Dialog Fragment");
            }
        });

        holder.btn_addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_comment);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                final ImageView imageView = (ImageView) dialog.findViewById(R.id.iv_close);
                final EditText editText = (EditText) dialog.findViewById(R.id.edt_comment);
                Button button = (Button) dialog.findViewById(R.id.btn_addComment);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sessionManager.keyboradHidden(editText);
                        dialog.dismiss();
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sessionManager.keyboradHidden(editText);
                        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.moderatorSaveComment,
                                Param.moderatorSaveComment(sessionManager.getEventId(), requestListingObj.getReciver_id(), requestListingObj.getRequest_id(), requestListingObj.getSender_id(), editText.getText().toString()), 1, true, ModeratorRequestMeetingAdapter.this);

                        dialog.dismiss();

                    }
                });

                dialog.show();


            }
        });

        holder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (GlobalData.isNetworkAvailable(context)) {

                    performAccept(requestListingObj.getRequest_id(), requestListingObj.getSender_id(), requestListingObj.getReciver_id(), "2");
                    requestListingArrayList.remove(position);
                    tmprequestListingArrayList.remove(position);
                    //ExhibitorReuqestMettingList.rv_viewMettingList.removeViewAt(tmp_position);
                    notifyItemRemoved(position);
                    //notifyItemRangeChanged(tmp_position, requestListingArrayList.size());
                    notifyDataSetChanged();

                } else {
                    ToastC.show(context, "No Internet Connection");
                }
            }
        });

    }


    private void performAccept(String requestId, String senderId, String receiverId, String status) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.moderatorRequestResponse, Param.saveOrrejectModeratorRequestMetting(sessionManager.getEventId(), sessionManager.getUserId(), requestId, receiverId, senderId, status), 0, true, this);
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
                break;

            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL SAVEORREJECT", jsonObject.toString());

                        ToastC.show(context, jsonObject.getString("message"));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public Filter getFilter() {
        return new PortalListingFilter(this, requestListingArrayList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userDesc;
        Button btn_accept, btn_reject;
        Button btn_newSuggestTime, btn_addComment;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userDesc = (TextView) itemView.findViewById(R.id.user_desc);
            btn_accept = (Button) itemView.findViewById(R.id.btn_accept);
            btn_reject = (Button) itemView.findViewById(R.id.btn_reject);
            btn_newSuggestTime = (Button) itemView.findViewById(R.id.btn_newSuggestTime);
            btn_addComment = (Button) itemView.findViewById(R.id.btn_addComment);
        }
    }


    private static class PortalListingFilter extends Filter {
        ModeratorRequestMeetingAdapter portalListingAdapter;
        ArrayList<ModeratorRequestListing> requestListingArrayList;
        ArrayList<ModeratorRequestListing> tmprequestListingArrayList;


        public PortalListingFilter(ModeratorRequestMeetingAdapter portalListingAdapter, ArrayList<ModeratorRequestListing> requestListingArrayList) {
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
                for (ModeratorRequestListing portalObj1 : requestListingArrayList) {
                    String title = portalObj1.getSender_name().toLowerCase() + " " + portalObj1.getReciver_name().toLowerCase();
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
            portalListingAdapter.tmprequestListingArrayList.addAll((ArrayList<ModeratorRequestListing>) filterResults.values);
            portalListingAdapter.notifyDataSetChanged();
        }
    }

}