package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Bean.ViewActivity;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Bean.twitterFeed;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import java.util.ArrayList;



/**
 * Created by nteam on 2/8/16.
 */
public class TwitterFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    ArrayList<twitterFeed> feedArrayList;
    Context context;
    Bundle bundle;

    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    WrapContentLinearLayoutManager lytManager;
    private OnLoadMoreListener onLoadMoreListener;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    Activity activity;
    private static final int TYPE_ITEM = 1;
    Handler handler;
    public TwitterFeedAdapter(ArrayList<twitterFeed> feedArrayList, RecyclerView recyclerView, final WrapContentLinearLayoutManager layoutManager, Activity activity, Context context, NestedScrollView nestedScrollView) {
        this.feedArrayList = feedArrayList;
        this.context = context;
        bundle=new Bundle();
        this.activity=activity;
        handler=new Handler();
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (scrollView, scrollX, scrollY, oldScrollX, oldScrollY) -> {


            totalItemCount = layoutManager.getItemCount();
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();

            if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMore();
                }

                loading = true;
            }
        });


//        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
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

    public void addFooter() {


        feedArrayList.add(null);
        try
        {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    notifyItemInserted(feedArrayList.size() - 1);
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

                    feedArrayList.remove(feedArrayList.size() - 1);
                    notifyItemRemoved(feedArrayList.size());
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


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == TYPE_ITEM)
        {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_twitterfeed, parent, false);
            return new ViewHolder(itemView);
        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            return new ViewHolder(v);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof ViewHolder && getItem(position) != null)
        {

            final ViewHolder myViewHolder = (ViewHolder) holder;

            final twitterFeed twitterFeedObj = feedArrayList.get(position);

            myViewHolder.feed_Username.setText(twitterFeedObj.getName());

               myViewHolder.feed_Username.setOnClickListener(new View.OnClickListener()
               {
                   @Override
                   public void onClick(View v)
                   {

                       if (GlobalData.isNetworkAvailable(context)) {

                           GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                           GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                           bundle.putString("Social_url", twitterFeedObj.getScreen_name());
                           ((MainActivity) context).loadFragment(bundle);
                       }
                       else
                       {
                           ToastC.show(context,context.getString(R.string.noInernet));
                       }

                   }
               });

            myViewHolder.feed_date.setText(twitterFeedObj.getTime());
            myViewHolder.feed_desc.setText(twitterFeedObj.getFeed_desc());
//        Glide.with(context).load(twitterFeedObj.getProfile_image()).centerCrop().fitCenter().into(holder.feed_Profileimage);
//        Log.d("AITL MediaURl",twitterFeedObj.getMedia_url());

            Glide.with(context)
                    .load(twitterFeedObj.getMedia_url())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {


                            myViewHolder.media_img.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            myViewHolder.media_img.setVisibility(View.VISIBLE);
                            return false;
                        }
                    }).into(myViewHolder.media_img);


            Glide.with(context)
                    .load(twitterFeedObj.getProfile_image())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            myViewHolder.progressBar1.setVisibility(View.GONE);
                            myViewHolder.feed_Profileimage.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            myViewHolder.progressBar1.setVisibility(View.GONE);
                            myViewHolder.feed_Profileimage.setVisibility(View.VISIBLE);
                            return false;
                        }
                    }).into(myViewHolder.feed_Profileimage);

        }
        else if (holder instanceof ProgressViewHolder)
        {

            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount()
    {
        return feedArrayList.size();
    }

    public twitterFeed getItem(int position)
    {
        return feedArrayList.get(position);
    }

    @Override
    public int getItemViewType(int position)
    {

        if (feedArrayList.get(position) == null)
        {
            return VIEW_PROG;
        } else {
            return TYPE_ITEM;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView media_img,feed_Profileimage;
        TextView feed_Username,feed_date,feed_desc;
        ProgressBar progressBar1;
        public ViewHolder(View itemView)
        {
            super(itemView);
            media_img=(ImageView)itemView.findViewById(R.id.media_img);
            feed_Profileimage=(ImageView)itemView.findViewById(R.id.feed_Profileimage);
            feed_Username=(TextView)itemView.findViewById(R.id.feed_Username);
            feed_date=(TextView)itemView.findViewById(R.id.feed_date);
            feed_desc=(TextView)itemView.findViewById(R.id.feed_desc);
            progressBar1=(ProgressBar)itemView.findViewById(R.id.progressBar1);
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
