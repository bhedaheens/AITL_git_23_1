package com.allintheloop.Adapter.SponsorListWithSection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allintheloop.Adapter.ExhibitorListWithSection.ChildViewHolder;
import com.allintheloop.Adapter.ExhibitorListWithSection.SectionViewHolder;
import com.allintheloop.Bean.SponsorClass.SponsorListNewData;
import com.allintheloop.Bean.SponsorClass.SponsorSectionHeader;
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
import java.util.Random;


/**
 * Created by nteam on 1/12/17.
 */

public class AdapterSponsorSectionRecyclerView extends SectionRecyclerViewAdapter<SponsorSectionHeader, SponsorListNewData.SponsorNewData, SectionViewHolder, ChildViewHolder> implements VolleyInterface {

    Context context;
    SessionManager sessionManager;
    List<SponsorSectionHeader> sectionItemList;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    SponsorListNewData.SponsorNewData favAttendance;
    UidCommonKeyClass uidCommonKeyClass;

    public AdapterSponsorSectionRecyclerView(Context context, List<SponsorSectionHeader> sectionItemList) {
        super(context, sectionItemList);
        this.context = context;
        this.sectionItemList = sectionItemList;
        sessionManager = new SessionManager(context);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(context);
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
    public void onBindSectionViewHolder(SectionViewHolder sectionViewHolder, int sectionPosition, SponsorSectionHeader exhibitorListdata) {
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
    public void onBindChildViewHolder(final ChildViewHolder itemViewHolder, int i, int i1, final SponsorListNewData.SponsorNewData attendance) {

        itemViewHolder.imageView.setVisibility(View.GONE);
        itemViewHolder.user_sqrimage.setVisibility(View.VISIBLE);
        final String companyLogourl = MyUrls.Imgurl + attendance.getCompanyLogo();


        itemViewHolder.userName.setVisibility(View.VISIBLE);
        itemViewHolder.userDesc.setVisibility(View.GONE);
        itemViewHolder.userName.setText(attendance.getCompanyName());
        GradientDrawable drawable = new GradientDrawable();
        Random rnd = new Random();


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            itemViewHolder.layout_relative.setBackground(context.getResources().getDrawable(R.drawable.ripple_effect));
        }

        if (sessionManager.isLogin()) {
            if (attendance.getId().equalsIgnoreCase(sessionManager.getUserId())) {
                itemViewHolder.img_star.setVisibility(View.INVISIBLE);
            } else {


                if (GlobalData.checkForUIDVersion()) {
                    uidCommonKeyClass = sessionManager.getUidCommonKey();
                    if (uidCommonKeyClass.getIsOnlyAttendeeUser().equalsIgnoreCase("1")
                            && sessionManager.getfavIsEnabled().equalsIgnoreCase("1")) {
                        itemViewHolder.img_star.setVisibility(View.VISIBLE);
                    } else {
                        itemViewHolder.img_star.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (sessionManager.getRolId().equalsIgnoreCase("4") // change applied
                            && sessionManager.getfavIsEnabled().equalsIgnoreCase("1")) {
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
        if (attendance.getIsFavorites().equalsIgnoreCase("1")) {
            itemViewHolder.img_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_selected));
        } else {
            itemViewHolder.img_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_normal));
        }
        itemViewHolder.img_star.setColorFilter(Color.parseColor(topColor));

        itemViewHolder.img_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.isSponsorClick = true;
                if (attendance.getIsFavorites().equalsIgnoreCase("0")) {
                    sessionManager.isSponsorFav = "1";

                    attendance.setIsFavorites("1");
                } else if (attendance.getIsFavorites().equalsIgnoreCase("1")) {
                    sessionManager.isSponsorFav = "0";
                    attendance.setIsFavorites("0");
                }
                favAttendance = attendance;
                addorRemoveFav(attendance.getId());
                notifyDataSetChanged();
            }
        });

        itemViewHolder.layout_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("AITL SPONSOR ID", attendance.getId());
                        sessionManager.sponsor_id = attendance.getId();
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Sponsor_Detail_Fragment;
                        ((MainActivity) context).loadFragment();
                    }
                }, 500);

            }
        });

        if (attendance.getCompanyLogo().equalsIgnoreCase("")) {
            itemViewHolder.user_sqrimage.setVisibility(View.GONE);
            itemViewHolder.txt_profileName.setVisibility(View.VISIBLE);

//            Log.d("AITL SPONSOR Color", "" + color);
            Log.d("AITL SPONSOR COMPANY", "" + attendance.getCompanyName());
            if (!(attendance.getCompanyName().equalsIgnoreCase(""))) {
                itemViewHolder.txt_profileName.setText("" + attendance.getCompanyName().charAt(0));
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
                        sqLiteDatabaseHandler.updateSponsorFavAdapter(sessionManager.getEventId(), sessionManager.getUserId(), favAttendance.getId(), sessionManager.isSponsorFav);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void filter(String text) {
        List<SponsorSectionHeader> temp = new ArrayList();

        for (SponsorSectionHeader d : sectionItemList) {
            List<SponsorListNewData.SponsorNewData> tempChild = new ArrayList();
            for (SponsorListNewData.SponsorNewData attendance : d.getChildItems()) {
                if (attendance.getCompanyName().toLowerCase().contains(text.toLowerCase())) {
                    tempChild.add(attendance);
                }
            }
            if (tempChild.size() > 0) {
                temp.add(new SponsorSectionHeader(tempChild, d.getSectionText(), d.getBgColor(), d.getTypePostion()));
            }
        }
        //update recyclerview
        notifyDataChanged(temp);
    }
}
