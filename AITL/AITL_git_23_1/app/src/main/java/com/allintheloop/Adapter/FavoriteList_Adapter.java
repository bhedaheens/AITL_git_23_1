package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.allintheloop.Bean.Attendee.Attendance;
import com.allintheloop.Bean.FavoriteList_Data;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
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
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aiyaz on 10/3/17.
 */

public class FavoriteList_Adapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> implements VolleyInterface {

    ArrayList<FavoriteList_Data> favoriteList_dataArrayList;
    ArrayList<Attendance> attendanceArrayList;
    Context context;
    SessionManager sessionManager;
    int tmp_position;

    public FavoriteList_Adapter(ArrayList<FavoriteList_Data> favoriteList_dataArrayList, Context context) {
        this.favoriteList_dataArrayList = favoriteList_dataArrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public int getSectionCount() {
        return favoriteList_dataArrayList.size();
    }

    @Override
    public int getItemCount(int i) {
        return favoriteList_dataArrayList.get(i).getFavoriteList().size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        FavoriteList_Data favoriteListData = favoriteList_dataArrayList.get(i);
        SectionViewHolder sectionViewHolder = (SectionViewHolder) viewHolder;
        if (!(favoriteListData.getType().equalsIgnoreCase(""))) {
            if (favoriteListData.getFavoriteList().size() != 0) {
                sectionViewHolder.sectionHeader.setText(favoriteListData.getType());
                sectionViewHolder.sectionHeader.setTextColor(context.getResources().getColor(R.color.white));
                if (!(favoriteListData.getBg_color().equalsIgnoreCase(""))) {
                    sectionViewHolder.sectionHeader.setBackgroundColor(Color.parseColor(favoriteListData.getBg_color()));
                }
            } else {
                sectionViewHolder.sectionHeader.setVisibility(View.GONE);
            }
        } else {
            sectionViewHolder.sectionHeader.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, final int i1, int i2) {

        FavoriteList_Data exhibitorListdata = favoriteList_dataArrayList.get(i);
        attendanceArrayList = favoriteList_dataArrayList.get(i).getFavoriteList();
        final Attendance attendance = attendanceArrayList.get(i1);
        this.tmp_position = i;
        final ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;

        GradientDrawable drawable = new GradientDrawable();
        Random rnd = new Random();
        itemViewHolder.imageView.setVisibility(View.GONE);
        itemViewHolder.user_sqrimage.setVisibility(View.VISIBLE);
        itemViewHolder.userDesc.setVisibility(View.VISIBLE);
        final String companyLogourl = MyUrls.Imgurl + attendance.getFavrouite_logo();
        itemViewHolder.userName.setText(attendance.getFavrouite_user_name());
        itemViewHolder.layout_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attendance.getFavrouite_module_type().equalsIgnoreCase("2")) {
                    SessionManager.AttenDeeId = attendance.getFavrouite_module_id();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Attendance_Detail_Fragment;
                    ((MainActivity) context).loadFragment();
                } else if (attendance.getFavrouite_module_type().equalsIgnoreCase("3")) {
                    sessionManager.exhibitor_id = attendance.getFavrouite_module_id();
                    sessionManager.exhi_pageId = attendance.getFavrouite_module_id();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
                    ((MainActivity) context).loadFragment();
                } else if (attendance.getFavrouite_module_type().equalsIgnoreCase("7")) {
                    SessionManager.speaker_id = attendance.getFavrouite_module_id();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Speaker_Detail_Fragment;
                    ((MainActivity) context).loadFragment();
                } else if (attendance.getFavrouite_module_type().equalsIgnoreCase("43")) {
                    sessionManager.sponsor_id = attendance.getFavrouite_module_id();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Sponsor_Detail_Fragment;
                    ((MainActivity) context).loadFragment();
                }

            }
        });

//            holder.img_star.setVisibility(View.GONE);
        itemViewHolder.imageView.setVisibility(View.VISIBLE);
        itemViewHolder.user_sqrimage.setVisibility(View.GONE);


        itemViewHolder.userName.setText(attendance.getFavrouite_user_name());
        if (!(attendance.getFavrouite_extra().equalsIgnoreCase(""))) {
            itemViewHolder.userDesc.setVisibility(View.VISIBLE);
            itemViewHolder.userDesc.setText(attendance.getFavrouite_extra());
        } else {
            itemViewHolder.userDesc.setVisibility(View.GONE);
        }
        if (attendance.getFavrouite_logo().equalsIgnoreCase("")) {
            itemViewHolder.imageView.setVisibility(View.GONE);
            itemViewHolder.txt_profileName.setVisibility(View.VISIBLE);
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            if (!(attendance.getFavrouite_user_name().equalsIgnoreCase(""))) {
//                        String name = attendanceObj.getSpe_firstname().charAt(0) + "" + attendanceObj.getSpe_lastname().charAt(0);
                itemViewHolder.txt_profileName.setText("" + attendance.getFavrouite_user_name().charAt(0));

            }
            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                drawable.setShape(GradientDrawable.OVAL);
                drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                itemViewHolder.txt_profileName.setBackgroundDrawable(drawable);
                itemViewHolder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            } else {
                drawable.setShape(GradientDrawable.OVAL);
                drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                itemViewHolder.txt_profileName.setBackgroundDrawable(drawable);
                itemViewHolder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            }
        } else {
            Glide.with(context)
                    .load(companyLogourl)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            itemViewHolder.imageView.setVisibility(View.VISIBLE);
                            itemViewHolder.txt_profileName.setVisibility(View.GONE);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            itemViewHolder.imageView.setVisibility(View.VISIBLE);
                            itemViewHolder.txt_profileName.setVisibility(View.GONE);
                            return false;
                        }
                    }).thumbnail(0.7f)
                    .into(itemViewHolder.imageView);
        }
    }

    private void addorRemoveFav(String ex_pageId) {
        if (GlobalData.isNetworkAvailable(context)) {
            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.addRemoveUid, Param.addonRemoveFav(sessionManager.getEventId(), sessionManager.getUserId(), ex_pageId, sessionManager.getMenuid()), 0, false, this);
            } else {
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.addRemove, Param.addonRemoveFav(sessionManager.getEventId(), sessionManager.getUserId(), ex_pageId, sessionManager.getMenuid()), 0, false, this);
            }
        } else {
            ToastC.show(context, context.getString(R.string.noInernet));
        }
    }


    private void addorRemoveFavList(String ex_pageId, String moduleType) {
        if (GlobalData.isNetworkAvailable(context)) {
            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.addRemoveUid, Param.addonRemoveFav(sessionManager.getEventId(), sessionManager.getUserId(), ex_pageId, moduleType), 0, false, this);
            } else {
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.addRemove, Param.addonRemoveFav(sessionManager.getEventId(), sessionManager.getUserId(), ex_pageId, moduleType), 0, false, this);
            }
        } else {
            ToastC.show(context, context.getString(R.string.noInernet));
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (viewType == -2) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.section_item, parent, false);
            return new SectionViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.favorite_item_layout, parent, false);
            return new ItemViewHolder(v);
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(context, jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
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
}
