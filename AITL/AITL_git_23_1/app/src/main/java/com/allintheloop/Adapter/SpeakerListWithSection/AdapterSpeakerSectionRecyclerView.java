package com.allintheloop.Adapter.SpeakerListWithSection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.ExhibitorListWithSection.SectionViewHolder;
import com.allintheloop.Bean.Speaker.SpeakerListClass;
import com.allintheloop.Bean.Speaker.SpeakerSectionHeader;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.RoundedImageConverter;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.intrusoft.sectionedrecyclerview.SectionRecyclerViewAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nteam on 1/12/17.
 */

public class AdapterSpeakerSectionRecyclerView extends SectionRecyclerViewAdapter<SpeakerSectionHeader, SpeakerListClass.SpeakerListNew, SectionViewHolder, AdapterSpeakerSectionRecyclerView.ViewHolder> implements VolleyInterface {

    Context context;
    SessionManager sessionManager;
    List<SpeakerSectionHeader> sectionItemList;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    SpeakerListClass.SpeakerListNew favAttendance;
    String speakerId = "";
    int tmp_postion;
    UidCommonKeyClass uidCommonKeyClass;

    public AdapterSpeakerSectionRecyclerView(Context context, List<SpeakerSectionHeader> sectionItemList) {
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
    public ViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_attendance, childViewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindSectionViewHolder(SectionViewHolder sectionViewHolder, int sectionPosition, SpeakerSectionHeader exhibitorListdata) {
        if (!(exhibitorListdata.getSectionText().equalsIgnoreCase(""))) {
            if (exhibitorListdata.getChildItems().size() != 0) {
                sectionViewHolder.name.setVisibility(View.VISIBLE);
                sectionViewHolder.name.setText(exhibitorListdata.getSectionText());
                sectionViewHolder.name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
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
    public void onBindChildViewHolder(final ViewHolder holder, int i, int i1, final SpeakerListClass.SpeakerListNew attendance) {

        GradientDrawable drawable = new GradientDrawable();
        this.tmp_postion = i;


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            holder.layout_relative.setBackground(context.getResources().getDrawable(R.drawable.ripple_effect));
        }

        if (sessionManager.isLogin()) {

            holder.img_star.setContentDescription(attendance.getId());
            if (attendance.getId().equalsIgnoreCase(sessionManager.getUserId())) {
                holder.img_star.setVisibility(View.INVISIBLE);
            } else {


                if (GlobalData.checkForUIDVersion()) {
                    uidCommonKeyClass = sessionManager.getUidCommonKey();
                    if (uidCommonKeyClass.getSpeakerShowRequestMeeting().equalsIgnoreCase("1")
                            && sessionManager.getfavIsEnabled().equalsIgnoreCase("1")) {
                        holder.img_star.setVisibility(View.VISIBLE);
                    } else {
                        holder.img_star.setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (sessionManager.getRolId().equalsIgnoreCase("4")//changes applied
                            && sessionManager.getfavIsEnabled().equalsIgnoreCase("1")) {
                        holder.img_star.setVisibility(View.VISIBLE);
                    } else {
                        holder.img_star.setVisibility(View.INVISIBLE);
                    }
                }


            }
        } else {
            holder.img_star.setVisibility(View.INVISIBLE);
        }
        String topColor = "#FFFFFF";
        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            topColor = sessionManager.getFunTopBackColor();
        } else {
            topColor = sessionManager.getTopBackColor();
        }
        if (attendance.getIsFavorites().equalsIgnoreCase("1")) {
            holder.img_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_selected));
        } else {
            holder.img_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_normal));
        }
        holder.img_star.setColorFilter(Color.parseColor(topColor));
        holder.img_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.isSpeakerstarClick = true;
                speakerId = attendance.getId();
                if (attendance.getIsFavorites().equalsIgnoreCase("0")) {
                    attendance.setIsFavorites("1");
                    sessionManager.isSpeakerFav = "1";
                } else if (attendance.getIsFavorites().equalsIgnoreCase("1")) {
                    attendance.setIsFavorites("0");
                    sessionManager.isSpeakerFav = "0";
                }
                speakerAddorRemoveFav(attendance.getId());
            }
        });

        holder.layout_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SessionManager.speaker_id = attendance.getId();
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Speaker_Detail_Fragment;
                        ((MainActivity) context).loadFragment();
                    }
                }, 500);

            }
        });


        holder.imageView.setVisibility(View.VISIBLE);
        holder.user_sqrimage.setVisibility(View.GONE);
        final String companyLogourl = MyUrls.Imgurl + attendance.getLogo();
        holder.userName.setText(attendance.getFirstname() + " " + attendance.getLastname());

        if (!(attendance.getTitle().equalsIgnoreCase(""))) {
            holder.userDesc.setVisibility(View.VISIBLE);
            if (!(attendance.getCompanyName().equalsIgnoreCase(""))) {
                holder.userDesc.setText(attendance.getTitle() + " " + "at" + " " + attendance.getCompanyName());
            } else {
                holder.userDesc.setText(attendance.getTitle());
            }
        } else {
            holder.userDesc.setVisibility(View.GONE);
        }
        if (attendance.getLogo().equalsIgnoreCase("")) {
            holder.imageView.setVisibility(View.GONE);
            holder.txt_profileName.setVisibility(View.VISIBLE);


            if (!(attendance.getFirstname().equalsIgnoreCase(""))) {
                if (!(attendance.getLastname().equalsIgnoreCase(""))) {
                    String name = attendance.getFirstname().charAt(0) + "" + attendance.getLastname().charAt(0);
                    holder.txt_profileName.setText(name);
                } else {
                    holder.txt_profileName.setText("" + attendance.getFirstname().charAt(0));
                }
                if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                    drawable.setShape(GradientDrawable.OVAL);
                    drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                    holder.txt_profileName.setBackgroundDrawable(drawable);
                    holder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                } else {
                    drawable.setShape(GradientDrawable.OVAL);
                    drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                    holder.txt_profileName.setBackgroundDrawable(drawable);
                    holder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                }
            }
        } else {
            try {
                Glide.with(context).load(companyLogourl).asBitmap()
                        .override(90, 90).thumbnail(0.7f).listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.imageView.setVisibility(View.VISIBLE);
                        holder.txt_profileName.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.imageView.setVisibility(View.VISIBLE);
                        holder.txt_profileName.setVisibility(View.GONE);
                        return false;
                    }
                }).into(new BitmapImageViewTarget(holder.imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.imageView.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        if (sessionManager.getNotification_role().equalsIgnoreCase("Speaker") && sessionManager.getNotification_UserId().equalsIgnoreCase(attendance.getId())) {

            Log.d(" Attendee Adapter Img", MyUrls.Imgurl + sessionManager.getUserProfile());
            try {
                Glide.with(context).load(MyUrls.Imgurl + sessionManager.getUserProfile()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.imageView.setVisibility(View.VISIBLE);
                        holder.txt_profileName.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.imageView.setVisibility(View.VISIBLE);
                        holder.txt_profileName.setVisibility(View.GONE);
                        return false;
                    }
                }).into(new BitmapImageViewTarget(holder.imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.imageView.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void speakerAddorRemoveFav(String ex_pageId) {
        if (GlobalData.isNetworkAvailable(context)) {
            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.addRemoveUid,
                        Param.addonRemoveFav(sessionManager.getEventId(), sessionManager.getUserId(),
                                ex_pageId, sessionManager.getMenuid()), 1, true,
                        this);
            } else {
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.addRemove,
                        Param.addonRemoveFav(sessionManager.getEventId(), sessionManager.getUserId(),
                                ex_pageId, sessionManager.getMenuid()), 1, true,
                        this);
            }
        } else {
            ToastC.show(context, context.getString(R.string.noInernet));
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(context, jsonObject.getString("message"));
                        JSONObject jsonData = jsonObject.getJSONObject("data");
//                        attendance_fragment.loaddataFromAdapter(jsonData);
                        sqLiteDatabaseHandler.updateSpeakerFavAdapter(sessionManager.getEventId(), sessionManager.getUserId(), speakerId, sessionManager.isSpeakerFav);
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    public void filter(String text) {
        List<SpeakerSectionHeader> temp = new ArrayList();

        for (SpeakerSectionHeader d : sectionItemList) {
            List<SpeakerListClass.SpeakerListNew> tempChild = new ArrayList();
            for (SpeakerListClass.SpeakerListNew attendance : d.getChildItems()) {
                if (attendance.getFirstname().toLowerCase().contains(text.toLowerCase()) || attendance.getLastname().toLowerCase().contains(text.toLowerCase())) {
                    tempChild.add(attendance);
                }
            }
            if (tempChild.size() > 0) {
                temp.add(new SpeakerSectionHeader(tempChild, d.getSectionText(), d.getBgColor(), d.getTypePostion()));
            }
        }
        //update recyclerview
        notifyDataChanged(temp);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView userName, txt_profileName, txt_view;
        BoldTextView userDesc;
        ImageView user_sqrimage, img_star;
        RelativeLayout layout_relative;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.user_image);
            user_sqrimage = (ImageView) itemView.findViewById(R.id.user_sqrimage);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userDesc = (BoldTextView) itemView.findViewById(R.id.user_desc);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            img_star = (ImageView) itemView.findViewById(R.id.img_star);
            layout_relative = (RelativeLayout) itemView.findViewById(R.id.layout_relative);
//            progressBar1= (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }
}
