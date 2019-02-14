package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Bean.QAList.QandADetail_VoteListing;
import com.allintheloop.Fragment.QandAModule.QAHideQuestionModule_Fragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.RoundedImageConverter;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class QAHideQuestionModule_Adapter
        extends RecyclerSwipeAdapter<QAHideQuestionModule_Adapter.ViewHolder> implements VolleyInterface {

    Context context;
    SessionManager sessionManager;
    private ArrayList<QandADetail_VoteListing> qandADetail_voteListings;
    private ArrayList<ImageView> imageViewArrayList = new ArrayList<>();
    private QAHideQuestionModule_Fragment qaHideQuestionModuleFragment;

    public QAHideQuestionModule_Adapter(ArrayList<QandADetail_VoteListing> qandADetail_voteListings, Context context, QAHideQuestionModule_Fragment qaHideQuestionModuleFragment) {
        this.qandADetail_voteListings = qandADetail_voteListings;
        imageViewArrayList = new ArrayList<>();
        this.context = context;
        sessionManager = new SessionManager(context);
        this.qaHideQuestionModuleFragment = qaHideQuestionModuleFragment;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_qahidequestion_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final QandADetail_VoteListing voteListingobj = qandADetail_voteListings.get(position);
        holder.txt_name.setText(voteListingobj.getUsername());
        holder.txt_question.setText(voteListingobj.getMessage());
        holder.card_QandAAdapter.setContentDescription(voteListingobj.getSessionId());
        GradientDrawable drawable = new GradientDrawable();

//        mItemManger.bind(holder.itemView, position);


        if (voteListingobj.getIs_highest().equalsIgnoreCase("1")) {
            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                holder.card_QandAAdapter.setCardBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                holder.txt_name.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                holder.txt_question.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            } else {

                holder.card_QandAAdapter.setCardBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                holder.txt_name.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                holder.txt_question.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

            }
        } else {

            holder.card_QandAAdapter.setCardBackgroundColor(Color.parseColor("#EEEEEE"));
            holder.txt_name.setTextColor(Color.parseColor("#BDBBBD"));
            holder.txt_question.setTextColor(Color.parseColor("#000000"));
        }

        holder.img_unhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMessage(voteListingobj.getId(), voteListingobj.getSessionId());
            }
        });


        holder.txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (voteListingobj.getRole_id().equalsIgnoreCase("4")) {
                    SessionManager.AttenDeeId = voteListingobj.getUserId();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Attendance_Detail_Fragment;
                    ((MainActivity) context).loadFragment();
                } else if (voteListingobj.getRole_id().equalsIgnoreCase("7")) {
                    SessionManager.speaker_id = voteListingobj.getUserId();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Speaker_Detail_Fragment;
                    ((MainActivity) context).loadFragment();
                } else if (voteListingobj.getRole_id().equalsIgnoreCase("6")) {
                    sessionManager.exhibitor_id = voteListingobj.getUserId();
                    sessionManager.exhi_pageId = voteListingobj.getUserId();
//                    sessionManager.private_senderId = voteListingobj.getUserId();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
                    ((MainActivity) context).loadFragment();
                }
            }
        });


        if (voteListingobj.getLogo().equalsIgnoreCase("")) {
            holder.img_profile.setVisibility(View.GONE);
            holder.txt_profileName.setVisibility(View.VISIBLE);

            if (!(voteListingobj.getUsername().equalsIgnoreCase(""))) {
                String name = "" + voteListingobj.getUsername().charAt(0);
                holder.txt_profileName.setText(name);
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
            try {
                Glide.with(context).load(MyUrls.Imgurl + voteListingobj.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.img_profile.setVisibility(View.VISIBLE);
                        holder.txt_profileName.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.img_profile.setVisibility(View.VISIBLE);
                        holder.txt_profileName.setVisibility(View.GONE);
                        return false;
                    }
                }).into(new BitmapImageViewTarget(holder.img_profile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.img_profile.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteMessage(String messageIdId, String sessionId) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.hideQaMessage, Param.qAHideMessage(sessionManager.getEventId(), sessionManager.getUserId(), sessionId, messageIdId, "0"), 0, true, this);
    }

    @Override
    public int getItemCount() {
        return qandADetail_voteListings.size();
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject1 = new JSONObject(volleyResponse.output);
                    if (jsonObject1.getString("success").equalsIgnoreCase("true")) {
                        qaHideQuestionModuleFragment.loadData(jsonObject1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeLayout;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_profile;
        TextView txt_name, txt_question, txt_profileName;
        CardView card_QandAAdapter;
        SwipeLayout swipeLayout;
        TextView buttonDelete;
        LinearLayout linear_delete;
        ImageView img_unhide;
        View view_line;

        public ViewHolder(View itemView) {

            super(itemView);
            img_profile = (ImageView) itemView.findViewById(R.id.img_profile);

            txt_question = (TextView) itemView.findViewById(R.id.txt_question);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            card_QandAAdapter = (CardView) itemView.findViewById(R.id.card_QandAAdapter);
            buttonDelete = (TextView) itemView.findViewById(R.id.delete);
            img_unhide = (ImageView) itemView.findViewById(R.id.img_unhide);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipeLayout);
            linear_delete = (LinearLayout) itemView.findViewById(R.id.linear_delete);
            view_line = (View) itemView.findViewById(R.id.view_line);
        }
    }
}
