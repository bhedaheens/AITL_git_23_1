package com.allintheloop.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Bean.UnreadPrivateMessageList;
import com.allintheloop.R;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aiyaz on 27/3/17.
 */

public class UnreadPrivateListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<UnreadPrivateMessageList> unreadPrivateMessageLists;
    Context context;
    private static final int TYPE_ITEM = 1;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 2;
    private int firstVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    WrapContentLinearLayoutManager linearLayoutManager;
    SessionManager sessionManager;
    Handler handler;

    public UnreadPrivateListingAdapter(RecyclerView recyclerView, WrapContentLinearLayoutManager linearLayout, ArrayList<UnreadPrivateMessageList> unreadPrivateMessageLists, Context context) {

        this.unreadPrivateMessageLists = unreadPrivateMessageLists;
        handler = new Handler();
        this.context = context;
        sessionManager = new SessionManager(context);
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


//    public UnreadPrivateListingAdapter(ArrayList<UnreadPrivateMessageList> unreadPrivateMessageLists, Context context) {
//        this.unreadPrivateMessageLists = unreadPrivateMessageLists;
//        this.context = context;
//    }

//    @Override
//    public UnreadPrivateListingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unread_privatemessagelist, parent, false);
//        return new ViewHolder(view);
//    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_unread_privatemessagelist, parent, false);
            return new ViewHolder(itemView);
        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder && getItem(position) != null) {

            final ViewHolder myViewHolder = (ViewHolder) holder;
            UnreadPrivateMessageList unreadObj = unreadPrivateMessageLists.get(position);
            GradientDrawable drawable = new GradientDrawable();
            Random rnd = new Random();

            if (!(unreadObj.getUnread_count().equalsIgnoreCase(""))) {
                myViewHolder.txt_count.setVisibility(View.VISIBLE);
                myViewHolder.txt_count.setText(unreadObj.getUnread_count() + " " + "Unread Messages");
            } else {
                myViewHolder.txt_count.setVisibility(View.GONE);
            }

            myViewHolder.userName.setText(unreadObj.getSendername());
            if (unreadObj.getSenderlogo().equalsIgnoreCase("")) {
                myViewHolder.imageView.setVisibility(View.GONE);
                myViewHolder.txt_profileName.setVisibility(View.VISIBLE);
                if (!(unreadObj.getSendername().equalsIgnoreCase(""))) {
                    myViewHolder.txt_profileName.setText("" + unreadObj.getSendername().charAt(0));
                }
                if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                    drawable.setShape(GradientDrawable.OVAL);
                    drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                    myViewHolder.txt_profileName.setBackgroundDrawable(drawable);
                    myViewHolder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                } else {
                    drawable.setShape(GradientDrawable.OVAL);
                    drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                    myViewHolder.txt_profileName.setBackgroundDrawable(drawable);
                    myViewHolder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                }

            } else {
                Glide.with(context)
                        .load(MyUrls.Imgurl + unreadObj.getSenderlogo())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                myViewHolder.imageView.setVisibility(View.VISIBLE);
                                myViewHolder.txt_profileName.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                myViewHolder.imageView.setVisibility(View.VISIBLE);
                                myViewHolder.txt_profileName.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .thumbnail(0.7f)
                        .into(myViewHolder.imageView);

            }
        } else if (holder instanceof ProgressViewHolder) {

            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemViewType(int position) {

        if (unreadPrivateMessageLists.get(position) == null) {
            return VIEW_PROG;
        } else {
            return TYPE_ITEM;
        }
    }


    public void addFooter() {


        unreadPrivateMessageLists.add(null);
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    notifyItemInserted(unreadPrivateMessageLists.size() - 1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void removeFooter() {

        try {
            handler.post(() -> {

                unreadPrivateMessageLists.remove(unreadPrivateMessageLists.size() - 1);
                notifyItemRemoved(unreadPrivateMessageLists.size());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    @Override
    public int getItemCount() {
        return unreadPrivateMessageLists.size();
    }

    public UnreadPrivateMessageList getItem(int position) {
        return unreadPrivateMessageLists.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView userName, txt_count, txt_profileName;

        RelativeLayout layout_relative;
        CardView app_back;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.user_image);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            txt_count = (TextView) itemView.findViewById(R.id.txt_count);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            layout_relative = (RelativeLayout) itemView.findViewById(R.id.layout_relative);
            app_back = (CardView) itemView.findViewById(R.id.app_back);

        }
    }


    class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
