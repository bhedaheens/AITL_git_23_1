package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Bean.Message;
import com.allintheloop.Fragment.PrivateMessage.ViewPrivateImageDialog_Fragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
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
import java.util.Calendar;

/**
 * Created by Aiyaz on 25/3/17.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements VolleyInterface {

    private String userId;
    private int SELF = 100;
    private static String today;

    private static final int TYPE_ITEM = 1;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 2;
    private int firstVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    WrapContentLinearLayoutManager linearLayoutManager;
    private ArrayList<Message> messageArrayList;
    SessionManager sessionManager;
    private int viewType;
    Context context;

    public ChatRoomAdapter(RecyclerView recyclerView, WrapContentLinearLayoutManager linearLayout, ArrayList<Message> messageArrayList, String userId, boolean isBuyer, Context context) {

        this.messageArrayList = messageArrayList;
        this.userId = userId;
        this.context = context;
        sessionManager = new SessionManager(context);
        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            this.linearLayoutManager = linearLayout;

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    super.onScrolled(recyclerView, dx, dy);

//                    Log.e("AAKASH", "ADAPTER ONLOAD IS CALLED");

                    totalItemCount = linearLayoutManager.getItemCount();
                    firstVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (firstVisibleItem + visibleThreshold)) {

                        Log.e("AAKASH", "ADAPTER ONLOAD IS CALLED");

                        if (onLoadMoreListener != null) {

                            onLoadMoreListener.onLoadMore();
                        }

                        loading = true;
                    }
                }
            });
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        this.viewType = viewType;
        // view type is to identify where to render the chat message left or right
        if (viewType == SELF) {

            // self message
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_self, parent, false);
            return new ViewHolder(itemView);
        } else if (viewType == TYPE_ITEM) {
            // others message
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_other, parent, false);
            return new ViewHolder(itemView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new ViewHolder(v);
        }
    }


    @Override
    public int getItemViewType(int position) {

        Message message = messageArrayList.get(position);

        if (messageArrayList.get(position) == null) {
            return VIEW_PROG;
        } else if (message.getUser().getCustomerId().equals(userId)) {
            return SELF;
        } else {
            return TYPE_ITEM;
        }

    }

    private Message getItem(int position) {

        return messageArrayList.get(position);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder && getItem(position) != null) {

            final ViewHolder viewHolder = (ViewHolder) holder;
            final Message message = messageArrayList.get(position);

            if (message.getMessage().equalsIgnoreCase("")) {
                viewHolder.message.setVisibility(View.GONE);
                viewHolder.img_receview.setVisibility(View.VISIBLE);
                Log.d("AITL IMAGEVIEW", "ADAPTER" + MyUrls.Imgurl + message.getUserImage());
                Glide.with(context)
                        .load(MyUrls.Imgurl + message.getUserImage())
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                viewHolder.img_receview.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                viewHolder.img_receview.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(viewHolder.img_receview);
            } else {
                viewHolder.message.setVisibility(View.VISIBLE);
                viewHolder.message.setText(message.getMessage());
                viewHolder.img_receview.setVisibility(View.GONE);

            }

            if (message.getUser().getCustomerId().equalsIgnoreCase(userId)) {
                viewHolder.timestamp.setVisibility(View.GONE);
                viewHolder.timestamp.setVisibility(View.GONE);
            } else {
                viewHolder.timestamp.setVisibility(View.VISIBLE);
                viewHolder.time_img.setColorFilter(context.getResources().getColor(R.color.white));
                viewHolder.timestamp.setText(message.getCreatedAt());
            }


            viewHolder.img_receview.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                FragmentActivity activity = (FragmentActivity) (context);
                FragmentManager fm = activity.getSupportFragmentManager();
                bundle.putString("imageurl", MyUrls.Imgurl + message.getUserImage());
                ViewPrivateImageDialog_Fragment alertDialog = new ViewPrivateImageDialog_Fragment();
                alertDialog.setArguments(bundle);
                alertDialog.show(fm, "fragment_alert");

            });

            viewHolder.message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("AITL ISCLICKABLE", "ADAPTER" + message.getIsClickable());

                    if (message.getIsClickable().equalsIgnoreCase("0")) {
                        Log.d("AITL IS Messge Click", message.getIsClickable());
                    } else if (message.getIsClickable().equalsIgnoreCase("1")) {
                        approvedStatus(message.getUser().getCustomerId());
                        Log.d("AITL IS Messge Click", message.getIsClickable());
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragmentTag;
                        ((MainActivity) context).loadFragment();
                    } else if (message.getIsClickable().equalsIgnoreCase("2")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                        ((MainActivity) context).loadFragment();
                    } else if (message.getIsClickable().equalsIgnoreCase("3")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                        ((MainActivity) context).loadFragment();
                    } else if (message.getIsClickable().equalsIgnoreCase("4")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.viewSuggestedTimeList;
                        ((MainActivity) context).loadFragment();
                    } else {
                        Log.d("AITL IS Messge Click", message.getIsClickable());
                    }
                }
            });


        } else if (holder instanceof ProgressViewHolder) {

            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        }
    }


    private void deleteMessage(String messageId) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.getPrivateUnreadMessageSenderwise, Param.deletePrivateMessageDetail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getToken(), sessionManager.private_senderId, messageId), 0, true, this);
    }

    private void approvedStatus(String attndeeId) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.approvedshareContactDetail, Param.shareContactInformation(sessionManager.getEventId(), attndeeId, sessionManager.getUserId()), 0, true, this);
    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {

        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL STATUS APRROVED", jsonObject.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public void addFooter() {

        messageArrayList.add(null);
        notifyItemInserted(messageArrayList.size() - 1);
    }

    public void removeFooter() {

        messageArrayList.remove(messageArrayList.size() - 1);
        notifyItemRemoved(messageArrayList.size());
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView message, timestamp;
        ImageView img_receview, time_img;
        LinearLayout linear_message;

        public ViewHolder(View view) {
            super(view);
            message = itemView.findViewById(R.id.message);
            timestamp = itemView.findViewById(R.id.timestamp);
            time_img = itemView.findViewById(R.id.time_img);
            img_receview = itemView.findViewById(R.id.img_receview);
            linear_message = itemView.findViewById(R.id.linear_message);

        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar1);
        }
    }


}
