package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Bean.ViewActivity;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by nteam on 24/8/16.
 */
public class ViewActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    ArrayList<ViewActivity> activityArrayList;
    Context context;
    SessionManager sessionManager;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    LinearLayoutManager lytManager;
    private OnLoadMoreListener onLoadMoreListener;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    Activity activity;
    private static final int TYPE_ITEM = 1;
    Handler handler;

    public ViewActivityAdapter(ArrayList<ViewActivity> activityArrayList, RecyclerView recyclerView, final LinearLayoutManager layoutManager, Activity activity, Context context, NestedScrollView nestedScrollView) {

        this.activityArrayList = activityArrayList;
        this.activity = activity;
        this.context = context;
        sessionManager = new SessionManager(context);
        handler = new Handler();
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView scrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {


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

        /*if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            this.lytManager = layoutManager;

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = lytManager.getItemCount();
                    lastVisibleItem = lytManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }

                        loading = true;
                    }
                }
            });*/
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_activity_fragment, parent, false);
            return new MyViewHolder(itemView);
        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            return new MyViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder && getItem(position) != null) {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;

            try {
                final ViewActivity activityObj = activityArrayList.get(position);

                Spannable user_sender = new SpannableString(activityObj.getFirstname() + " " + activityObj.getLastname());
                user_sender.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.survey_question)), 0, user_sender.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                myViewHolder.user_name.setText(user_sender);

                Log.d("AITL ActivityImage", MyUrls.Imgurl + activityObj.getLogo());
                //   Glide.with(context).load(MyUrls.Imgurl+activityObj.getLogo()).centerCrop().fitCenter().placeholder(R.drawable.defult_attende).into(myViewHolder.user_image);


                Glide.with(context)
                        .load(MyUrls.Imgurl + activityObj.getLogo())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                myViewHolder.user_image.setVisibility(View.VISIBLE);
                                myViewHolder.progressBar1.setVisibility(View.GONE);
                                Glide.with(context).load(MyUrls.Imgurl + activityObj.getLogo()).centerCrop().fitCenter().placeholder(R.drawable.defult_attende).into(myViewHolder.user_image);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                myViewHolder.user_image.setVisibility(View.VISIBLE);
                                myViewHolder.progressBar1.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(myViewHolder.user_image);


                Spannable titile = new SpannableString(activityObj.getTitle());
                titile.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.black)), 0, titile.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                myViewHolder.user_name.append("  ");
                myViewHolder.user_name.append(titile);

                if (activityObj.getRating().equalsIgnoreCase("")) {
                    myViewHolder.rating.setVisibility(View.GONE);
                } else {
                    myViewHolder.rating.setVisibility(View.VISIBLE);
                    myViewHolder.rating.setRating(Float.parseFloat(activityObj.getRating()));
                }
                myViewHolder.txt_message.setText(activityObj.getMessage());
                myViewHolder.txt_readMore.setText(activityObj.getNavigation_title());

                myViewHolder.txt_readMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (GlobalData.isNetworkAvailable(context)) {
                            if (activityObj.getNavigation_title().equalsIgnoreCase("READ MORE")) {
                                if (activityObj.getType().equalsIgnoreCase("activity")) {
                                    sessionManager.activity_type = activityObj.getType();
                                    sessionManager.activity_feedId = activityObj.getId();
                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.activityDetailFeed;
                                    ((MainActivity) context).loadFragment();
                                } else if (activityObj.getType().equalsIgnoreCase("public")) {
                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                                    ((MainActivity) context).loadFragment();
                                }
                            } else if (activityObj.getNavigation_title().equalsIgnoreCase("VIEW PHOTO")) {
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.Photo_Fragment;
                                ((MainActivity) context).loadFragment();
                            }
                        } else {
                            ToastC.show(context, "No Internet Connection");
                        }


                    }
                });
                myViewHolder.txt_time.setText(activityObj.getTime());
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

        if (activityArrayList.get(position) == null) {
            return VIEW_PROG;
        } else {
            return TYPE_ITEM;
        }
    }

    public void addFooter() {

        activityArrayList.add(null);
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    notifyItemInserted(activityArrayList.size() - 1);
                }
            });
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }


    }

    public void removeFooter() {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    activityArrayList.remove(activityArrayList.size() - 1);
                    notifyItemRemoved(activityArrayList.size());
                }
            });
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    public ViewActivity getItem(int position) {
        return activityArrayList.get(position);
    }


    @Override
    public int getItemCount() {
        return activityArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView user_image;
        TextView user_name, txt_message, txt_readMore, txt_time;
        RatingBar rating;
        ProgressBar progressBar1;

        public MyViewHolder(View itemView) {
            super(itemView);

            user_image = (CircleImageView) itemView.findViewById(R.id.user_image);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            txt_message = (TextView) itemView.findViewById(R.id.txt_message);
            txt_readMore = (TextView) itemView.findViewById(R.id.txt_readMore);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
            progressBar1 = (ProgressBar) itemView.findViewById(R.id.progressBar1);
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
