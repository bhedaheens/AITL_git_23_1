package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Bean.FacebookFeedData;
import com.allintheloop.Fragment.FacebookModule.FacebookDailog_Fragment;
import com.allintheloop.R;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;


/**
 * Created by Aiyaz on 23/1/17.
 */

public class Adapter_facebookFeedFragment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    SessionManager sessionManager;
    ArrayList<FacebookFeedData> facebookFeedDataArrayList;
    ArrayList<String> img_arrArrayList;
    Context context;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    WrapContentLinearLayoutManager lytManager;
    private OnLoadMoreListener onLoadMoreListener;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    int pos;
    Activity activity;
    boolean isHide = false;
    private final int TYPE_ITEM = 1;
    ArrayList<String> cardViewArrayList = new ArrayList<>();


    public Adapter_facebookFeedFragment(ArrayList<FacebookFeedData> facebookFeedDataArrayList, RecyclerView recyclerView, Context context, final WrapContentLinearLayoutManager layoutManager, Activity activity) {
        this.facebookFeedDataArrayList = facebookFeedDataArrayList;
        this.context = context;
        this.activity = activity;
        sessionManager = new SessionManager(this.context);
//        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//
//            @Override
//            public void onScrollChange(NestedScrollView scrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//
//                totalItemCount = layoutManager.getItemCount();
//                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//
//                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//
//                    if (onLoadMoreListener != null) {
//                        onLoadMoreListener.onLoadMore();
//                    }
//
//                    loading = true;
//                }
//            }
//        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }
            }
        });

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_facebookfeed, parent, false);
            return new MyViewHolder(itemView);
        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            return new MyViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder && getItem(position) != null) {
            try {

                final MyViewHolder myViewHolder = (MyViewHolder) holder;

                final FacebookFeedData dataObj = getItem(position);
                img_arrArrayList = dataObj.getImg_array();
                myViewHolder.txt_caption.setText(dataObj.getCaption());
                myViewHolder.txt_fromname.setText(dataObj.getName());
                myViewHolder.txt_time.setText(dataObj.getTime());
                myViewHolder.txt_message.setText(dataObj.getMessage());
                myViewHolder.txt_name.setText(dataObj.getBottom_name());
                myViewHolder.txt_descripation.setText(dataObj.getDesc());
                myViewHolder.txt_like.setText(dataObj.getTotalLikes());
                myViewHolder.txt_comment.setText(dataObj.getTotalComment() + " " + "Comment");
                Glide.with(context).load(dataObj.getIcon()).into(myViewHolder.fb_icon);
//                Glide.with(context).load(dataObj.getPicture()).into(myViewHolder.fb_detailImage);

                if (dataObj.getPicture().equalsIgnoreCase("")) {
                    myViewHolder.fb_detailImage.setVisibility(View.GONE);
                } else {
                    myViewHolder.fb_detailImage.setVisibility(View.VISIBLE);

                    if (img_arrArrayList.size() > 1) {
                        myViewHolder.txt_count.setVisibility(View.VISIBLE);
                        myViewHolder.txt_count.setText("+" + (img_arrArrayList.size() - 1));

                    } else {
                        myViewHolder.txt_count.setVisibility(View.GONE);
                    }

                    myViewHolder.fb_detailImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            FragmentActivity activity = (FragmentActivity) (context);
                            FragmentManager fm = activity.getSupportFragmentManager();
                            Bundle bundle = new Bundle();

                            Log.d("AITL ImageARRAY", "IN CLICK" + dataObj.getImg_array().size());
                            FacebookDailog_Fragment fragment = new FacebookDailog_Fragment();
                            bundle.putInt("position", position);
                            bundle.putString("isActivity", "0");
                            bundle.putStringArrayList("img_array", dataObj.getImg_array());
                            fragment.setArguments(bundle);
                            fragment.show(fm, "DialogFragment");
                        }
                    });

                    Glide.with(context)
                            .load(img_arrArrayList.get(0).toString())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                    myViewHolder.fb_detailImage.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    myViewHolder.fb_detailImage.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            })
                            .crossFade()
                            .into(myViewHolder.fb_detailImage);
                }


                myViewHolder.layout_caption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataObj.getLink()));
                        context.startActivity(browserIntent);
                    }
                });


                if (dataObj.getCaption().equalsIgnoreCase("")) {
                    myViewHolder.txt_caption.setVisibility(View.GONE);
                } else {
                    myViewHolder.txt_caption.setVisibility(View.VISIBLE);
                }

                if (dataObj.getBottom_name().equalsIgnoreCase("")) {
                    myViewHolder.txt_name.setVisibility(View.GONE);
                } else {
                    myViewHolder.txt_name.setVisibility(View.VISIBLE);
                }

                if (dataObj.getMessage().equalsIgnoreCase("")) {
                    myViewHolder.txt_message.setVisibility(View.GONE);
                } else {
                    myViewHolder.txt_message.setVisibility(View.VISIBLE);
                }

                if (dataObj.getDesc().equalsIgnoreCase("")) {
                    myViewHolder.txt_descripation.setVisibility(View.GONE);
                } else {
                    myViewHolder.txt_descripation.setVisibility(View.VISIBLE);
                }

                if (dataObj.getTotalLikes().equalsIgnoreCase("")) {
                    myViewHolder.txt_like.setVisibility(View.GONE);
                } else {
                    myViewHolder.txt_like.setVisibility(View.VISIBLE);
                }

                if (dataObj.getTotalComment().equalsIgnoreCase("")) {
                    myViewHolder.txt_comment.setVisibility(View.GONE);
                } else {
                    myViewHolder.txt_comment.setVisibility(View.VISIBLE);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (holder instanceof ProgressViewHolder) {

            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (facebookFeedDataArrayList.get(position) == null) {
            return VIEW_PROG;
        } else {
            return TYPE_ITEM;
        }
    }

    public void addFooter() {
        facebookFeedDataArrayList.add(null);


        try {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyItemInserted(facebookFeedDataArrayList.size() - 1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFooter() {

        try {

            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    facebookFeedDataArrayList.remove(facebookFeedDataArrayList.size() - 1);
                    notifyItemRemoved(facebookFeedDataArrayList.size());
                }
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


    public FacebookFeedData getItem(int position) {
        return facebookFeedDataArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return facebookFeedDataArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView fb_icon, fb_detailImage;
        TextView txt_fromname, txt_time, txt_message, txt_name, txt_descripation, txt_caption, txt_like, txt_comment, txt_count;
        CardView bottom_cardView;
        LinearLayout layout_caption;

        public MyViewHolder(View itemView) {
            super(itemView);

            fb_detailImage = (ImageView) itemView.findViewById(R.id.fb_detailImage);
            fb_icon = (ImageView) itemView.findViewById(R.id.fb_icon);
            txt_fromname = (TextView) itemView.findViewById(R.id.txt_fromname);
            layout_caption = (LinearLayout) itemView.findViewById(R.id.layout_caption);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_message = (TextView) itemView.findViewById(R.id.txt_message);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_descripation = (TextView) itemView.findViewById(R.id.txt_descripation);
            txt_caption = (TextView) itemView.findViewById(R.id.txt_caption);
            txt_like = (TextView) itemView.findViewById(R.id.txt_like);
            txt_comment = (TextView) itemView.findViewById(R.id.txt_comment);
            txt_count = (TextView) itemView.findViewById(R.id.txt_count);
            bottom_cardView = (CardView) itemView.findViewById(R.id.bottom_cardView);
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
