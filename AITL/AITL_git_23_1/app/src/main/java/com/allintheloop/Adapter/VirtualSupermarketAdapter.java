package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import com.allintheloop.Bean.Attendee.Attendance;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorListdata;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
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
 * Created by Aiyaz on 25/1/17.
 */

public class VirtualSupermarketAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

    ArrayList<ExhibitorListdata> exhibitorArrayList;
    ArrayList<Attendance> attendanceArrayList;
    Context context;
    SessionManager sessionManager;
    int tmp_position;

    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    WrapContentLinearLayoutManager lytManager;
    private OnLoadMoreListener onLoadMoreListener;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    Activity activity;
    private static final int TYPE_ITEM = 1;
    Handler handler;

    public VirtualSupermarketAdapter(ArrayList<ExhibitorListdata> attendanceArrayList, RecyclerView recyclerView, final WrapContentLinearLayoutManager layoutManager, Activity activity, Context context, NestedScrollView nestedScrollView) {
        this.exhibitorArrayList = attendanceArrayList;
        this.context = context;
        sessionManager = new SessionManager(context);

        this.activity = activity;
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


    }

    public VirtualSupermarketAdapter(ArrayList<ExhibitorListdata> exhibitorArrayList, Context context) {
        this.exhibitorArrayList = exhibitorArrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public int getSectionCount() {
        return exhibitorArrayList.size();
    }

    @Override
    public int getItemCount(int i) {
        return exhibitorArrayList.get(i).getExhibitorList().size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ExhibitorListdata exhibitorListdata = exhibitorArrayList.get(i);
        SectionViewHolder sectionViewHolder = (SectionViewHolder) viewHolder;
        if (!(exhibitorListdata.getType().equalsIgnoreCase(""))) {
            if (exhibitorListdata.getExhibitorList().size() != 0) {
                sectionViewHolder.sectionHeader.setVisibility(View.VISIBLE);
                sectionViewHolder.sectionHeader.setText(exhibitorListdata.getType());
                sectionViewHolder.sectionHeader.setTextColor(context.getResources().getColor(R.color.white));
                if (!(exhibitorListdata.getBg_color().equalsIgnoreCase(""))) {
                    sectionViewHolder.sectionHeader.setBackgroundColor(Color.parseColor(exhibitorListdata.getBg_color()));
                }
            } else {
                sectionViewHolder.sectionHeader.setVisibility(View.GONE);
            }
        } else {
            sectionViewHolder.sectionHeader.setVisibility(View.GONE);
        }
    }


    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, int i1, int i2) {


        attendanceArrayList = exhibitorArrayList.get(i).getExhibitorList();
        final Attendance attendance = attendanceArrayList.get(i1);
        this.tmp_position = i;
        final ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;

        GradientDrawable drawable = new GradientDrawable();
        Random rnd = new Random();
        itemViewHolder.imageView.setVisibility(View.GONE);
        itemViewHolder.user_sqrimage.setVisibility(View.VISIBLE);
        itemViewHolder.userDesc.setVisibility(View.VISIBLE);
        itemViewHolder.userDesc.setText(attendance.getEx_stand_number());
//       Log.d("AITL LOGO URL",attendance.getEx_comapnyLogo());
//        final String companyLogourl = attendance.getEx_comapnyLogo();
        itemViewHolder.userName.setText(attendance.getEx_heading());

        itemViewHolder.layout_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sessionManager.exhibitor_id = attendance.getEx_id();
                sessionManager.exhi_pageId = attendance.getEx_pageId();
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
                ((MainActivity) context).loadFragment();
            }
        });

        if (attendance.getEx_comapnyLogo().equalsIgnoreCase("")) {
            itemViewHolder.user_sqrimage.setVisibility(View.GONE);
            itemViewHolder.txt_profileName.setVisibility(View.VISIBLE);

            if (!(attendance.getEx_heading().equalsIgnoreCase(""))) {
                itemViewHolder.txt_profileName.setText("" + attendance.getEx_heading().charAt(0));
            }
            drawable.setShape(GradientDrawable.OVAL);
//            drawable.setColor(context.getResources().getColor(R.color.gulfcoloGray));
            itemViewHolder.txt_profileName.setBackgroundDrawable(drawable);

        } else {
            if (attendance.getEx_comapnyLogo().startsWith("http://") || attendance.getEx_comapnyLogo().startsWith("https://")) {

                Glide.with(context)
                        .load(attendance.getEx_comapnyLogo())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                itemViewHolder.user_sqrimage.setVisibility(View.VISIBLE);
                                itemViewHolder.txt_profileName.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                itemViewHolder.user_sqrimage.setVisibility(View.VISIBLE);
                                itemViewHolder.txt_profileName.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .thumbnail(0.7f)
                        .into(itemViewHolder.user_sqrimage);
            } else {
                Glide.with(context)
                        .load(MyUrls.Imgurl + attendance.getEx_comapnyLogo())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                itemViewHolder.user_sqrimage.setVisibility(View.VISIBLE);
                                itemViewHolder.txt_profileName.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                itemViewHolder.user_sqrimage.setVisibility(View.VISIBLE);
                                itemViewHolder.txt_profileName.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .thumbnail(0.7f)
                        .into(itemViewHolder.user_sqrimage);
            }
        }
        if (sessionManager.getNotification_role().equalsIgnoreCase("Exibitor") && sessionManager.getNotification_UserId().equalsIgnoreCase(attendance.getEx_id())) {

            Glide.with(context).load(MyUrls.Imgurl + sessionManager.getUserProfile()).into(itemViewHolder.user_sqrimage);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == -2) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.section_item, parent, false);
            return new SectionViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(v);
        }
    }


    public static class SectionViewHolder extends RecyclerView.ViewHolder {


        final TextView sectionHeader;
        CardView cardHeader;

        public SectionViewHolder(View itemView) {
            super(itemView);
            sectionHeader = (TextView) itemView.findViewById(R.id.sectionHeader);
            cardHeader = (CardView) itemView.findViewById(R.id.cardHeader);

        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView userName, userDesc, txt_profileName;
        ImageView user_sqrimage;
        RelativeLayout layout_relative;
        CardView app_back;

        public ItemViewHolder(View itemView) {

            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.user_image);
            user_sqrimage = (ImageView) itemView.findViewById(R.id.user_sqrimage);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userDesc = (TextView) itemView.findViewById(R.id.user_desc);
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
