package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Bean.InstagramFeed;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;

/**
 * Created by nteam on 29/9/16.
 */
public class Adapter_instagram extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    ArrayList<InstagramFeed> instagramFeeds;
    Context context;
    SessionManager sessionManager;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    GridLayoutManager lytManager;
    private OnLoadMoreListener onLoadMoreListener;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    Activity activity;
    private static final int TYPE_ITEM = 1;
    Handler handler;

    public Adapter_instagram(ArrayList<InstagramFeed> instagramFeeds, RecyclerView recyclerView, final GridLayoutManager layoutManager, Activity activity, Context context, NestedScrollView scrollView)
    {
        this.instagramFeeds = instagramFeeds;
        this.activity = activity;
        this.context = context;
        sessionManager=new SessionManager(context);
        handler=new Handler();
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (scrollView1, scrollX, scrollY, oldScrollX, oldScrollY) -> {


            totalItemCount = layoutManager.getItemCount();
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();

            if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMore();
                }

                loading = true;
            }
        });

//        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
//
//            this.lytManager = layoutManager;
//
//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//
//                    super.onScrolled(recyclerView, dx, dy);
//
//                    totalItemCount = lytManager.getItemCount();
//                    lastVisibleItem = lytManager.findLastVisibleItemPosition();
//
//                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//
//                        if (onLoadMoreListener != null)
//                        {
//                            onLoadMoreListener.onLoadMore();
//                        }
//
//                        loading = true;
//                    }
//                }
//            });
//        }

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == TYPE_ITEM)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_instagram_feed, parent, false);
            return new MyViewHolder(itemView);
        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            return new MyViewHolder(v);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        if (holder instanceof MyViewHolder && getItem(position) != null)
        {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;

            InstagramFeed instagramFeedObj = instagramFeeds.get(position);

            myViewHolder.txt_CommentCount.setText(instagramFeedObj.getComment_count());
            myViewHolder.txt_likeCount.setText(instagramFeedObj.getLikes_count());


            Log.d("AITL LikeCount", "Count" + instagramFeedObj.getLikes_count());
            DisplayMetrics displaymetrics = new DisplayMetrics();
            float density = context.getResources().getDisplayMetrics().density;
            ((MainActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            boolean tabletSize = context.getResources().getBoolean(R.bool.isTablet);
            if(tabletSize)
            {
                myViewHolder.img_instagram.setLayoutParams(new FrameLayout.LayoutParams(
                        width / 3,
                        width / 3));

                myViewHolder.frame_transpernt.setLayoutParams(new FrameLayout.LayoutParams(
                        width / 3
                        ,width / 3));
            }
            else
            {
                myViewHolder.img_instagram.setLayoutParams(new FrameLayout.LayoutParams(
                        width ,
                        width));

                myViewHolder.frame_transpernt.setLayoutParams(new FrameLayout.LayoutParams(
                        width
                        ,width));
            }

            Log.d("AITL Height And Width", "Hegiht :" + height + "Width :" + width);
            Log.d("AITL Width DP", "" + (width / density));

            if (instagramFeedObj.getType().equalsIgnoreCase("image")) {
                myViewHolder.img_video.setVisibility(View.GONE);
            } else {
                myViewHolder.img_video.setVisibility(View.VISIBLE);
            }

            Glide.with(context)
                    .load(instagramFeedObj.getLow_image())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            myViewHolder.img_instagram.setVisibility(View.VISIBLE);
                            myViewHolder.progressBar1.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            myViewHolder.img_instagram.setVisibility(View.VISIBLE);
                            myViewHolder.progressBar1.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(myViewHolder.img_instagram);
        }
        else if (holder instanceof ProgressViewHolder) {

            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemViewType(int position)
    {

        if (instagramFeeds.get(position) == null)
        {
            return VIEW_PROG;
        } else {
            return TYPE_ITEM;
        }
    }

    public void addFooter() {

        instagramFeeds.add(null);
        try
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemInserted(instagramFeeds.size() - 1);
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public void removeFooter() {

        try
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    instagramFeeds.remove(instagramFeeds.size() - 1);
                    notifyItemRemoved(instagramFeeds.size());
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    public InstagramFeed getItem(int position)
    {
        return instagramFeeds.get(position);
    }

    @Override
    public int getItemCount()
    {
        return instagramFeeds.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_instagram,img_video;
        TextView txt_likeCount,txt_CommentCount;
        ProgressBar progressBar1;
        FrameLayout frame_transpernt;

        public MyViewHolder(View itemView) {
            super(itemView);

            img_instagram=(ImageView)itemView.findViewById(R.id.img_instagram);
            img_video=(ImageView)itemView.findViewById(R.id.img_video);
            progressBar1=(ProgressBar)itemView.findViewById(R.id.progressBar1);
            txt_likeCount=(TextView)itemView.findViewById(R.id.txt_likeCount);
            txt_CommentCount=(TextView)itemView.findViewById(R.id.txt_CommentCount);
            frame_transpernt=(FrameLayout)itemView.findViewById(R.id.frame_transpernt);

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
