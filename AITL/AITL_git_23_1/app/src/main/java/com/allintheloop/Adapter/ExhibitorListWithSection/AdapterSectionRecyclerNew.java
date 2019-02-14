package com.allintheloop.Adapter.ExhibitorListWithSection;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allintheloop.Bean.Attendee.Attendance;
import com.allintheloop.Bean.ExhibitorListClass.SectionHeader;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
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
import com.intrusoft.sectionedrecyclerview.SectionRecyclerViewAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nteam on 7/11/17.
 */

public class AdapterSectionRecyclerNew extends SectionRecyclerViewAdapter<SectionHeader, Attendance, SectionViewHolder, ChildViewHolder> implements VolleyInterface {

    Context context;
    SessionManager sessionManager;
    List<SectionHeader> sectionItemList;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Attendance favAttendance;
    boolean isSponsorExhibitor = false;
    UidCommonKeyClass uidCommonKeyClass;

    public AdapterSectionRecyclerNew(Context context, List<SectionHeader> sectionItemList, boolean isSponsorExhibitor) {
        super(context, sectionItemList);
        this.context = context;
        this.sectionItemList = sectionItemList;
        sessionManager = new SessionManager(context);
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(context);
        this.isSponsorExhibitor = isSponsorExhibitor;
    }

    @Override
    public SectionViewHolder onCreateSectionViewHolder(ViewGroup sectionViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.section_item, sectionViewGroup, false);
        return new SectionViewHolder(view);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, childViewGroup, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindSectionViewHolder(SectionViewHolder sectionViewHolder, int sectionPosition, SectionHeader exhibitorListdata) {
        if (!(exhibitorListdata.getSectionText().equalsIgnoreCase(""))) {
            if (exhibitorListdata.getChildItems().size() != 0) {
                sectionViewHolder.name.setVisibility(View.VISIBLE);
                sectionViewHolder.name.setText(exhibitorListdata.getSectionText());
                sessionManager.setIsLastCategoryName(exhibitorListdata.getSectionText());
                sectionViewHolder.name.setTextColor(context.getResources().getColor(R.color.white));
                if (!(exhibitorListdata.getBgColor().equalsIgnoreCase(""))) {
                    sectionViewHolder.name.setBackgroundColor(Color.parseColor(exhibitorListdata.getBgColor()));
                }
            } else {
                sectionViewHolder.name.setVisibility(View.GONE);
            }
        } else {
            sectionViewHolder.name.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindChildViewHolder(final ChildViewHolder itemViewHolder, int sectionPosition, int childPosition, final Attendance attendance) {

        try {
            GradientDrawable drawable = new GradientDrawable();
            itemViewHolder.imageView.setVisibility(View.GONE);
            itemViewHolder.user_sqrimage.setVisibility(View.VISIBLE);
            itemViewHolder.userDesc.setVisibility(View.VISIBLE);
            itemViewHolder.userDesc.setText(attendance.getEx_stand_number());
            itemViewHolder.userName.setText(attendance.getEx_heading());
            final String companyLogourl = MyUrls.Imgurl + attendance.getEx_comapnyLogo();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                itemViewHolder.layout_relative.setBackground(context.getResources().getDrawable(R.drawable.ripple_effect));
            }

            if (isSponsorExhibitor) {
                if (attendance.getSponsored_category() != null) {
                    if (!(attendance.getSponsored_category().equalsIgnoreCase(""))) {
                        itemViewHolder.layout_relative.setBackgroundColor(Color.parseColor("#ffdf93"));
                    }
                }
            }
            if (sessionManager.isLogin()) {
                itemViewHolder.img_star.setContentDescription(attendance.getEx_pageId());
                if (attendance.getEx_id().equalsIgnoreCase(sessionManager.getUserId())) {
                    itemViewHolder.img_star.setVisibility(View.INVISIBLE);
                } else {
                    if (GlobalData.checkForUIDVersion()) {
                        if (uidCommonKeyClass.getExhibitorShowMyfavoriteBtn().equalsIgnoreCase("1") &&
                                sessionManager.getfavIsEnabled().equalsIgnoreCase("1")) {
                            itemViewHolder.img_star.setVisibility(View.VISIBLE);
                        } else {
                            itemViewHolder.img_star.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (sessionManager.getRolId().equalsIgnoreCase("4") && // changes applied
                                sessionManager.getfavIsEnabled().equalsIgnoreCase("1")) {
                            itemViewHolder.img_star.setVisibility(View.VISIBLE);
                        } else {
                            itemViewHolder.img_star.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            } else {
                itemViewHolder.img_star.setVisibility(View.INVISIBLE);
            }


            String topColor = "#FFFFFF";
            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                topColor = sessionManager.getFunTopBackColor();
            } else {
                topColor = sessionManager.getTopBackColor();
            }
            if (attendance.getIs_fav().equalsIgnoreCase("1")) {
                itemViewHolder.img_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_selected));
            } else {
                itemViewHolder.img_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_normal));
            }
            itemViewHolder.img_star.setColorFilter(Color.parseColor(topColor));

            itemViewHolder.img_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sessionManager.isexhibitorstarClick = true;
                    if (attendance.getIs_fav().equalsIgnoreCase("0")) {
                        sessionManager.isExhibitorFav = "1";
                        //itemViewHolder.img_star.setColorFilter(Color.parseColor("#FFD06C"));
                        attendance.setIs_fav("1");
                        //getDataFromDatabase("1", attendance.getEx_pageId());
                        notifyDataSetChanged();
                    } else if (attendance.getIs_fav().equalsIgnoreCase("1")) {
                        sessionManager.isExhibitorFav = "0";
                        //itemViewHolder.img_star.setColorFilter(Color.parseColor("#939393"));
                        attendance.setIs_fav("0");
                        //getDataFromDatabase("0", attendance.getEx_pageId());
                        notifyDataSetChanged();
                    }
                    favAttendance = attendance;
                    addorRemoveFav(attendance.getEx_pageId());
                }
            });


            itemViewHolder.layout_relative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sessionManager.exhibitor_id = attendance.getEx_id();
                            sessionManager.exhi_pageId = attendance.getEx_pageId();
                            GlobalData.temp_Fragment = 0;
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
                            ((MainActivity) context).loadFragment();
                        }
                    }, 300);

                }
            });

            if (attendance.getEx_comapnyLogo().equalsIgnoreCase("")) {
                itemViewHolder.user_sqrimage.setVisibility(View.GONE);
                itemViewHolder.txt_profileName.setVisibility(View.VISIBLE);

                if (!(attendance.getEx_heading().equalsIgnoreCase(""))) {
                    itemViewHolder.txt_profileName.setText("" + attendance.getEx_heading().charAt(0));
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

            if (sessionManager.getNotification_role().equalsIgnoreCase("Exibitor") && sessionManager.getNotification_UserId().equalsIgnoreCase(attendance.getEx_id())) {

                Glide.with(context).load(MyUrls.Imgurl + sessionManager.getUserProfile()).into(itemViewHolder.user_sqrimage);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
//        childViewHolder.name.setText(child.getEx_heading());
    }


    private void addorRemoveFav(String ex_pageId) {
        if (GlobalData.isNetworkAvailable(context)) {

            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.addRemoveUid, Param.addonRemoveFav(sessionManager.getEventId(), sessionManager.getUserId(), ex_pageId, sessionManager.getMenuid()), 0, true, this);
            } else {
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.addRemove, Param.addonRemoveFav(sessionManager.getEventId(), sessionManager.getUserId(), ex_pageId, sessionManager.getMenuid()), 0, true, this);
            }
        } else {

            ToastC.show(context, context.getString(R.string.noInernet));
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
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        sqLiteDatabaseHandler.updateExhibitorFav(sessionManager.getEventId(), sessionManager.getUserId(), favAttendance.getEx_pageId(), sessionManager.isExhibitorFav);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void filter(String text) {
        List<SectionHeader> temp = new ArrayList();

        for (SectionHeader d : sectionItemList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            List<Attendance> tempChild = new ArrayList();
            for (Attendance attendance : d.getChildItems()) {
                if (attendance.getEx_heading().toLowerCase().contains(text.toLowerCase()) || attendance.getEx_desc().toLowerCase().contains(text.toLowerCase())) {
                    tempChild.add(attendance);
                }
            }
            if (tempChild.size() > 0) {
                temp.add(new SectionHeader(tempChild, d.getSectionText(), d.getBgColor()));
            }
        }
        //update recyclerview
        notifyDataChanged(temp);
    }
}
