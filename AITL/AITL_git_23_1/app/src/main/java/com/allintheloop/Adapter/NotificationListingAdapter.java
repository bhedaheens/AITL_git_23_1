package com.allintheloop.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Bean.NotificationData;
import com.allintheloop.Bean.NotificationListingData;
import com.allintheloop.Fragment.NotificationDetailDialog;
import com.allintheloop.R;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.allintheloop.R.id.textView;

/**
 * Created by Aiyaz on 27/12/16.
 */

public class NotificationListingAdapter extends RecyclerView.Adapter<NotificationListingAdapter.ViewHolder> implements VolleyInterface {


    ArrayList<NotificationListingData> notificationDataArrayList;
    Context context;
    SessionManager sessionManager;
    Dialog dialog;
    TextView txt_title, txt_contain;
    ImageView dailog_close;

    public NotificationListingAdapter(ArrayList<NotificationListingData> notificationDataArrayList, Context context) {
        this.notificationDataArrayList = notificationDataArrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
        dialog = new Dialog(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notificationlisting_fragment, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final NotificationListingData notificationDataObj = notificationDataArrayList.get(position);
        holder.txt_notiTitle.setText(notificationDataObj.getContent());

        holder.txt_notiTitle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int lineCnt = holder.txt_notiTitle.getLineCount();
//                Log.d("AITL LINE COUNT",""+lineCnt);

                if (holder.txt_notiTitle.getLineCount() > 1) {
                    holder.txt_notiTitle.append("......");
                    holder.txt_notiSeeMore.setVisibility(View.VISIBLE);
                } else {
                    holder.txt_notiTitle.append("");
                    holder.txt_notiSeeMore.setVisibility(View.GONE);
                }
            }
        });

        holder.txt_notiSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                FragmentActivity activity = (FragmentActivity) (context);
                FragmentManager fm = activity.getSupportFragmentManager();
                bundle.putString("title", notificationDataObj.getTitle());
                bundle.putString("content", notificationDataObj.getContent());
                NotificationDetailDialog fragment = new NotificationDetailDialog();
                fragment.setArguments(bundle);
                fragment.show(fm, "Dialog Fragment");

            }
        });

        if (position % 2 == 0) {
            holder.card_container.setBackgroundColor(context.getResources().getColor(R.color.notiback));
        } else {
            holder.card_container.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return notificationDataArrayList.size();
    }


    public void removeItem(int position, String id) {
        notificationDataArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, notificationDataArrayList.size());
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.deleteNotificationListing, Param.deleteNotificationListing(sessionManager.getUserId(), id), 0, false, this);
    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL ID", jsonObject.toString());
                        ToastC.show(context, jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView card_container;
        TextView txt_notiTitle, txt_notiSeeMore, txt_hideTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_notiTitle = (TextView) itemView.findViewById(R.id.txt_notiTitle);
            card_container = (CardView) itemView.findViewById(R.id.card_container);
            txt_notiSeeMore = (TextView) itemView.findViewById(R.id.txt_notiSeeMore);
            txt_hideTitle = (TextView) itemView.findViewById(R.id.txt_hideTitle);


        }
    }
}
