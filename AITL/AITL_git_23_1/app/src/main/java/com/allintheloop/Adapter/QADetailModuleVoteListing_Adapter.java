package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Bean.QAList.QandADetail_VoteListing;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.Fragment.QandAModule.QADetailModule_Fragment;
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

/**
 * Created by Aiyaz on 7/6/17.
 */

public class QADetailModuleVoteListing_Adapter extends RecyclerSwipeAdapter<QADetailModuleVoteListing_Adapter.ViewHolder> implements VolleyInterface {

    Context context;
    SessionManager sessionManager;
    private UidCommonKeyClass uidCommonKeyClass;
    private ArrayList<QandADetail_VoteListing> qandADetail_voteListings;
    private ArrayList<ImageView> imageViewArrayList = new ArrayList<>();
    private QADetailModule_Fragment qaDetailModule_fragment;

    public QADetailModuleVoteListing_Adapter(ArrayList<QandADetail_VoteListing> qandADetail_voteListings, Context context, QADetailModule_Fragment qaDetailModule_fragment, SessionManager sessionManager) {
        this.qandADetail_voteListings = qandADetail_voteListings;
        imageViewArrayList = new ArrayList<>();
        this.context = context;
        this.sessionManager = sessionManager;
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();
        this.qaDetailModule_fragment = qaDetailModule_fragment;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_qanda_detail_vote_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final QandADetail_VoteListing voteListingobj = qandADetail_voteListings.get(position);
        holder.txt_name.setText(voteListingobj.getUsername());
        holder.txt_question.setText(voteListingobj.getMessage());
        holder.txt_votecount.setText(voteListingobj.getVotes());
        holder.card_QandAAdapter.setContentDescription(voteListingobj.getSessionId());
        imageViewArrayList.add(holder.img_vote);
        GradientDrawable drawable = new GradientDrawable();
        if (GlobalData.checkForUIDVersion()) {
            if (uidCommonKeyClass.getIsQaModeratorUser().equalsIgnoreCase("1")) {
                holder.linear_vote.setVisibility(View.VISIBLE);
                holder.view_line.setVisibility(View.VISIBLE);
                holder.linear_accpetbtnLayout.setVisibility(View.VISIBLE);
                holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
                holder.swipeLayout.setSwipeEnabled(true);
            } else {
                holder.view_line.setVisibility(View.VISIBLE);
                holder.linear_vote.setVisibility(View.VISIBLE);
                holder.linear_accpetbtnLayout.setVisibility(View.GONE);
                holder.swipeLayout.setSwipeEnabled(false);
            }
        } else {
            if (sessionManager.getRolId().equalsIgnoreCase("7") &&  //changes applied
                    sessionManager.isModerater().equalsIgnoreCase("1")) {
                holder.linear_vote.setVisibility(View.VISIBLE);
                holder.view_line.setVisibility(View.VISIBLE);
                holder.linear_accpetbtnLayout.setVisibility(View.VISIBLE);
                holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
                holder.swipeLayout.setSwipeEnabled(true);
            } else {
                holder.view_line.setVisibility(View.VISIBLE);
                holder.linear_vote.setVisibility(View.VISIBLE);
                holder.linear_accpetbtnLayout.setVisibility(View.GONE);
                holder.swipeLayout.setSwipeEnabled(false);
            }
        }


        if (voteListingobj.getIsAccpeted().equalsIgnoreCase("0")) {
            holder.img_accpet.setImageDrawable(context.getResources().getDrawable(R.drawable.qa_accept));
//            holder.img_accpet.setColorFilter(context.getResources().getColor(R.color.green));
//            holder.btn_accpet.setText(context.getString(R.string.qa_accpet));
//            holder.btn_accpet.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else {
            if (voteListingobj.getShow_on_web().equalsIgnoreCase("1")) {
                holder.img_accpet.setImageDrawable(context.getResources().getDrawable(R.drawable.qa_show_mainscreen));
                holder.img_accpet.setColorFilter(context.getResources().getColor(R.color.GrayColor));
            } else {
                holder.img_accpet.setImageDrawable(context.getResources().getDrawable(R.drawable.qa_show_mainscreen));
            }
        }

        holder.img_accpet.setOnClickListener(view -> {
            if (voteListingobj.getIsAccpeted().equalsIgnoreCase("0")) {
                voteListingobj.setIsAccpeted("1");

                holder.img_accpet.setImageDrawable(context.getResources().getDrawable(R.drawable.qa_show_mainscreen));

                accpetMetting(voteListingobj.getId(), voteListingobj.getSessionId(), true);
            } else {
                if (voteListingobj.getShow_on_web().equalsIgnoreCase("0")) {
                    voteListingobj.setShow_on_web("1");
                    holder.img_accpet.setImageDrawable(context.getResources().getDrawable(R.drawable.qa_show_mainscreen));
                    holder.img_accpet.setColorFilter(context.getResources().getColor(R.color.GrayColor));
                    accpetMetting(voteListingobj.getId(), voteListingobj.getSessionId(), false);
                }
            }
        });

        holder.linear_delete.setOnClickListener(view -> {
            mItemManger.removeShownLayouts(holder.swipeLayout);
            deleteMessage(voteListingobj.getId(), voteListingobj.getSessionId());
            mItemManger.closeAllItems();
        });

        if (voteListingobj.getUsers_vote().equalsIgnoreCase("1")) {
            imageViewArrayList.get(position).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_voteup));
            voteListingobj.setUsers_vote("1");
        } else {

            if (voteListingobj.getIs_highest().equalsIgnoreCase("1")) {
                imageViewArrayList.get(position).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_votedown));
                if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                    imageViewArrayList.get(position).setColorFilter(Color.parseColor(sessionManager.getFunTopTextColor()));
                } else {
                    imageViewArrayList.get(position).setColorFilter(Color.parseColor(sessionManager.getTopTextColor()));
                }
                voteListingobj.setUsers_vote("0");
            } else {
                imageViewArrayList.get(position).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_votedown));
                voteListingobj.setUsers_vote("0");
            }

        }


        if (voteListingobj.getIs_highest().equalsIgnoreCase("1")) {
            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                holder.card_QandAAdapter.setCardBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                holder.txt_name.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                holder.txt_question.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                holder.txt_votecount.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            } else {
                holder.card_QandAAdapter.setCardBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                holder.txt_name.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                holder.txt_question.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                holder.txt_votecount.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            }
        } else {
            holder.card_QandAAdapter.setCardBackgroundColor(Color.parseColor("#EEEEEE"));
            holder.txt_name.setTextColor(Color.parseColor("#BDBBBD"));
            holder.txt_question.setTextColor(Color.parseColor("#000000"));
            holder.txt_votecount.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.txt_name.setOnClickListener(view -> {

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
        });

        holder.img_vote.setOnClickListener(view -> {
            if (GlobalData.checkForUIDVersion()) {
                if (uidCommonKeyClass.getIsQaModeratorUser().equalsIgnoreCase("1")) {
                    qaDetailModule_fragment.openModeratorVoteDialog(voteListingobj);
                } else {
                    if (voteListingobj.getUsers_vote().equalsIgnoreCase("1")) {
                        likeFeed(voteListingobj.getId(), voteListingobj.getSessionId(), voteListingobj.getAndroid_id());
                        int like = Integer.parseInt(voteListingobj.getVotes());
                        holder.txt_votecount.setText("" + (like - 1));
                        voteListingobj.setVotes("" + (like - 1));
                        imageViewArrayList.get(position).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_votedown));
                        voteListingobj.setUsers_vote("0");
                    } else {
                        likeFeed(voteListingobj.getId(), voteListingobj.getSessionId(), voteListingobj.getAndroid_id());
                        int like = Integer.parseInt(voteListingobj.getVotes());
                        Log.d("AITL Like Variable", "" + like);
                        holder.txt_votecount.setText("" + (like + 1));
                        voteListingobj.setVotes("" + (like + 1));
                        imageViewArrayList.get(position).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_voteup));
                        voteListingobj.setUsers_vote("1");
                    }
                }
            } else {
                if (sessionManager.getRolId().equalsIgnoreCase("7")  //changes applied
                        && sessionManager.isModerater().equalsIgnoreCase("1")) {
                    qaDetailModule_fragment.openModeratorVoteDialog(voteListingobj);
                } else {
                    if (voteListingobj.getUsers_vote().equalsIgnoreCase("1")) {
                        likeFeed(voteListingobj.getId(), voteListingobj.getSessionId(), voteListingobj.getAndroid_id());
                        int like = Integer.parseInt(voteListingobj.getVotes());
                        holder.txt_votecount.setText("" + (like - 1));
                        voteListingobj.setVotes("" + (like - 1));
                        imageViewArrayList.get(position).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_votedown));
                        voteListingobj.setUsers_vote("0");
                    } else {
                        likeFeed(voteListingobj.getId(), voteListingobj.getSessionId(), voteListingobj.getAndroid_id());
                        int like = Integer.parseInt(voteListingobj.getVotes());
                        Log.d("AITL Like Variable", "" + like);
                        holder.txt_votecount.setText("" + (like + 1));
                        voteListingobj.setVotes("" + (like + 1));
                        imageViewArrayList.get(position).setImageDrawable(context.getResources().getDrawable(R.drawable.ic_image_voteup));
                        voteListingobj.setUsers_vote("1");
                    }
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
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.hideQaMessage, Param.qAHideMessage(sessionManager.getEventId(), sessionManager.getUserId(), sessionId, messageIdId, "1"), 2, true, this);
    }

    private void accpetMetting(String messageIdId, String sessionId, boolean isAccpeted) {
        if (isAccpeted) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.approvedQaMessageUid, Param.qAdeleteMessage(sessionManager.getEventId(), sessionManager.getUserId(), sessionId, messageIdId), 1, true, this);
            else
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.approvedQaMessage, Param.qAdeleteMessage(sessionManager.getEventId(), sessionManager.getUserId(), sessionId, messageIdId), 1, true, this);
        } else {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.qaShowOnWeb, Param.qAdeleteMessage(sessionManager.getEventId(), sessionManager.getUserId(), sessionId, messageIdId), 1, true, this);
        }
    }

    private void likeFeed(String messageIdId, String sessionId, String device_id) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.voteUpFeed, Param.QAvoteUp(messageIdId, sessionManager.getUserId(), sessionId, sessionManager.getEventId(), device_id), 0, true, this);
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
                        qaDetailModule_fragment.loadData(jsonObject1, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject1 = new JSONObject(volleyResponse.output);
                    if (jsonObject1.getString("success").equalsIgnoreCase("true")) {
                        qaDetailModule_fragment.loadData(jsonObject1, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject1 = new JSONObject(volleyResponse.output);
                    if (jsonObject1.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL MESSAGE DELETE", jsonObject1.toString());

                        qaDetailModule_fragment.loadData(jsonObject1, true);

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
        ImageView img_profile, img_vote, img_accpet;
        TextView txt_name, txt_question, txt_votecount, txt_profileName;
        CardView card_QandAAdapter;
        SwipeLayout swipeLayout;
        TextView buttonDelete;
        LinearLayout linear_delete, linear_vote, linear_accpetbtnLayout;
        Button btn_accpet;
        View view_line;

        public ViewHolder(View itemView) {

            super(itemView);
            img_profile = itemView.findViewById(R.id.img_profile);
            img_vote = itemView.findViewById(R.id.img_vote);
            img_accpet = itemView.findViewById(R.id.img_accpet);
            txt_question = itemView.findViewById(R.id.txt_question);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_votecount = itemView.findViewById(R.id.txt_votecount);
            txt_profileName = itemView.findViewById(R.id.txt_profileName);
            card_QandAAdapter = itemView.findViewById(R.id.card_QandAAdapter);
            btn_accpet = itemView.findViewById(R.id.btn_accpet);
            swipeLayout = itemView.findViewById(R.id.swipeLayout);
            linear_delete = itemView.findViewById(R.id.linear_delete);
            linear_accpetbtnLayout = itemView.findViewById(R.id.linear_accpetbtnLayout);
            linear_vote = itemView.findViewById(R.id.linear_vote);
            view_line = itemView.findViewById(R.id.view_line);
        }
    }
}
