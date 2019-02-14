package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Bean.Speaker.SpeakerList;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
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

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nteam on 11/5/18.
 */

public class SpeakerListNewAdapter extends RecyclerView.Adapter<SpeakerListNewAdapter.ViewHolder> implements VolleyInterface, Filterable {

    ArrayList<SpeakerList.SpeakerData> attendanceArrayList;
    ArrayList<SpeakerList.SpeakerData> tmpAttendanceList;
    Context context;
    SessionManager sessionManager;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    int tmp_postion;
    SpeakerList.SpeakerData speakerData;
    String speakerId = "";
    UidCommonKeyClass uidCommonKeyClass;

    public SpeakerListNewAdapter(ArrayList<SpeakerList.SpeakerData> attendanceArrayList, Context context, SessionManager sessionManager) {
        this.attendanceArrayList = attendanceArrayList;
        this.context = context;
        this.sessionManager = sessionManager;
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();
        tmpAttendanceList = new ArrayList<>();
        tmpAttendanceList.addAll(attendanceArrayList);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_attendance, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SpeakerList.SpeakerData speakerData = tmpAttendanceList.get(position);
        this.speakerData = speakerData;
        GradientDrawable drawable = new GradientDrawable();
        this.tmp_postion = position;
        if (sessionManager.isLogin()) {

            holder.img_star.setContentDescription(speakerData.getId());
            if (speakerData.getId().equalsIgnoreCase(sessionManager.getUserId())) {
                holder.img_star.setVisibility(View.GONE);
            } else {

                if (GlobalData.checkForUIDVersion()) {
                    if (uidCommonKeyClass.getSpeakerShowRequestMeeting().equalsIgnoreCase("1")
                            && sessionManager.getfavIsEnabled().equalsIgnoreCase("1")) {
                        holder.img_star.setVisibility(View.VISIBLE);
                    } else {
                        holder.img_star.setVisibility(View.GONE);
                    }
                } else {

                    if (sessionManager.getRolId().equalsIgnoreCase("4") ///changes applied
                            && sessionManager.getfavIsEnabled().equalsIgnoreCase("1")) {
                        holder.img_star.setVisibility(View.VISIBLE);
                    } else {
                        holder.img_star.setVisibility(View.GONE);
                    }
                }

            }
        } else {
            holder.img_star.setVisibility(View.GONE);
        }

        if (speakerData.getIsFavorites().equalsIgnoreCase("1")) {
            holder.img_star.setColorFilter(Color.parseColor("#FFD06C"));
        } else {
            holder.img_star.setColorFilter(Color.parseColor("#939393"));
        }

        holder.img_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.isSpeakerstarClick = true;
                speakerId = speakerData.getId();
                if (speakerData.getIsFavorites().equalsIgnoreCase("0")) {

                    speakerData.setIsFavorites("1");
                    sessionManager.isSpeakerFav = "1";
                } else if (speakerData.getIsFavorites().equalsIgnoreCase("1")) {
                    speakerData.setIsFavorites("0");
                    sessionManager.isSpeakerFav = "0";
                }
                speakerAddorRemoveFav(speakerData.getId());

            }
        });

        holder.layout_relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager.speaker_id = speakerData.getId();
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Speaker_Detail_Fragment;
                ((MainActivity) context).loadFragment();
            }
        });


        holder.imageView.setVisibility(View.VISIBLE);
        holder.user_sqrimage.setVisibility(View.GONE);
        final String companyLogourl = MyUrls.Imgurl + speakerData.getLogo();
        holder.userName.setText(speakerData.getFirstname() + " " + speakerData.getLastname());

        if (!(speakerData.getTitle().equalsIgnoreCase(""))) {
            holder.userDesc.setVisibility(View.VISIBLE);
            if (!(speakerData.getCompanyName().equalsIgnoreCase(""))) {
                holder.userDesc.setText(speakerData.getTitle() + " " + "at" + " " + speakerData.getCompanyName());
            } else {
                holder.userDesc.setText(speakerData.getTitle());
            }
        } else {
            holder.userDesc.setVisibility(View.GONE);
        }
        if (speakerData.getLogo().equalsIgnoreCase("")) {
            holder.imageView.setVisibility(View.GONE);
            holder.txt_profileName.setVisibility(View.VISIBLE);


            if (!(speakerData.getFirstname().equalsIgnoreCase(""))) {
                if (!(speakerData.getLastname().equalsIgnoreCase(""))) {
                    String name = speakerData.getFirstname().charAt(0) + "" + speakerData.getLastname().charAt(0);
                    holder.txt_profileName.setText(name);
                } else {
                    holder.txt_profileName.setText("" + speakerData.getFirstname().charAt(0));
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
                Glide.with(context).load(companyLogourl).asBitmap().thumbnail(0.7f).centerCrop().listener(new RequestListener<String, Bitmap>() {
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
        if (sessionManager.getNotification_role().equalsIgnoreCase("Speaker") && sessionManager.getNotification_UserId().equalsIgnoreCase(speakerData.getId())) {

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
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.addRemoveUid, Param.addonRemoveFav(sessionManager.getEventId(), sessionManager.getUserId(), ex_pageId, sessionManager.getMenuid()), 1, true, this);
            } else {
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.addRemove, Param.addonRemoveFav(sessionManager.getEventId(), sessionManager.getUserId(), ex_pageId, sessionManager.getMenuid()), 1, true, this);
            }
        } else {
            ToastC.show(context, context.getString(R.string.noInernet));
        }
    }


    @Override
    public int getItemCount() {
        return tmpAttendanceList.size();
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

                        Log.d("Bhavdip", "Speaker IsFav :" + sessionManager.isSpeakerFav);
                        Log.d("Bhavdip", "Speaker Id :" + speakerId);
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public Filter getFilter() {
        return new AttendaceFilter(this, attendanceArrayList, tmp_postion);
    }

    private static class AttendaceFilter extends Filter {
        private final SpeakerListNewAdapter attendanceAdapter;
        private final ArrayList<SpeakerList.SpeakerData> attendanceArrayList;
        private final ArrayList<SpeakerList.SpeakerData> tmpAttendanceList;
        private final int postion;

        public AttendaceFilter(SpeakerListNewAdapter attendanceAdapter, ArrayList<SpeakerList.SpeakerData> attendanceArrayList, int position) {
            this.attendanceAdapter = attendanceAdapter;
            this.attendanceArrayList = attendanceArrayList;
            tmpAttendanceList = new ArrayList<>();
            this.postion = position;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            final FilterResults results = new FilterResults();
            if (constraint.length() <= 0) {
                tmpAttendanceList.addAll(attendanceArrayList);
            } else {
                final String filterString = constraint.toString().toLowerCase();
                for (SpeakerList.SpeakerData attenObj : attendanceArrayList) {
                    String firstname = attenObj.getFirstname().toLowerCase();
                    String lastname = attenObj.getLastname().toLowerCase();
                    if (firstname.contains(filterString.toLowerCase()) || lastname.contains(filterString.toLowerCase())) {
                        tmpAttendanceList.add(attenObj);
                    }

                }
            }
            results.values = tmpAttendanceList;
            results.count = tmpAttendanceList.size();
            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            attendanceAdapter.tmpAttendanceList.clear();
            attendanceAdapter.tmpAttendanceList.addAll((ArrayList<SpeakerList.SpeakerData>) filterResults.values);
            attendanceAdapter.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView userName, userDesc, txt_profileName, txt_view;
        ImageView user_sqrimage, img_star;
        RelativeLayout layout_relative;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.user_image);
            user_sqrimage = (ImageView) itemView.findViewById(R.id.user_sqrimage);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userDesc = (TextView) itemView.findViewById(R.id.user_desc);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            img_star = (ImageView) itemView.findViewById(R.id.img_star);
            layout_relative = (RelativeLayout) itemView.findViewById(R.id.layout_relative);
//            progressBar1= (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }
}
