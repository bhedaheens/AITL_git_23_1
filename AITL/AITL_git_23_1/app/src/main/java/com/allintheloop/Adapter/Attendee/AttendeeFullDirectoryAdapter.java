package com.allintheloop.Adapter.Attendee;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import com.allintheloop.Bean.Attendee.Attendance;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.Fragment.AttandeeFragments.AttendeeFullDirectory_Fragment;
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

import java.util.List;
import java.util.Random;

public class AttendeeFullDirectoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements VolleyInterface {

    List<Attendance> attendanceArrayList;
    Context context;
    SessionManager sessionManager;
    int tmp_postion;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    AttendeeFullDirectory_Fragment attendeeFullDirectory_fragment;
    private UidCommonKeyClass uidCommonKeyClass;

    public AttendeeFullDirectoryAdapter(List<Attendance> attendanceArrayList, Context context, AttendeeFullDirectory_Fragment attendeeFullDirectory_fragment) {
        this.attendanceArrayList = attendanceArrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(context);
        this.attendeeFullDirectory_fragment = attendeeFullDirectory_fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_attendance, parent, false);
            return new ViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_progress, parent, false);
            return new ProgressViewHolder(itemView);
        }

    }


    @Override
    public int getItemViewType(int position) {
        return attendanceArrayList.get(position) == null ? 1 : 0;
//        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) viewHolder;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                holder.layout_relative.setBackground(context.getResources().getDrawable(R.drawable.ripple_effect));
            }

            this.tmp_postion = position;
            final Attendance attendanceObj = attendanceArrayList.get(position);
            GradientDrawable drawable = new GradientDrawable();
            Random rnd = new Random();
            if (attendanceObj.getCommon_tag().equalsIgnoreCase("attendance")) {
                holder.img_star.setContentDescription(attendanceObj.getId());
                if (sessionManager.isLogin()) {
                    if (GlobalData.checkForUIDVersion()) {
                        if (sessionManager.getfavIsEnabled().equalsIgnoreCase("1") &&
                                uidCommonKeyClass.getIsOnlyAttendeeUser().equalsIgnoreCase("1")) {
                            if (sessionManager.getUserId().equalsIgnoreCase(attendanceObj.getId()))
                                holder.img_star.setVisibility(View.INVISIBLE);
                            else
                                holder.img_star.setVisibility(View.VISIBLE);
                        } else {
                            holder.img_star.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (sessionManager.getfavIsEnabled().equalsIgnoreCase("1") &&
                                sessionManager.getRolId().equalsIgnoreCase("4")) {   //changes applied
                            if (sessionManager.getUserId().equalsIgnoreCase(attendanceObj.getId()))
                                holder.img_star.setVisibility(View.INVISIBLE);
                            else
                                holder.img_star.setVisibility(View.VISIBLE);
                        } else {
                            holder.img_star.setVisibility(View.INVISIBLE);
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
                if (attendanceObj.getIs_fav().equalsIgnoreCase("1")) {
                    holder.img_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_selected));
                } else {
                    holder.img_star.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_normal));
                }
                holder.img_star.setColorFilter(Color.parseColor(topColor));
                holder.img_star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d("AAKASh ADAPTER", "TAG  : " + attendanceObj.getAttendeeFull());
                        if (attendanceObj.getAttendeeFull().equalsIgnoreCase("1")) {

                            if (attendanceObj.getIs_fav().equalsIgnoreCase("0")) {
                                attendanceObj.setIs_fav("1");
                                notifyDataSetChanged();
                            } else if (attendanceObj.getIs_fav().equalsIgnoreCase("1")) {
                                attendanceObj.setIs_fav("0");
                                notifyDataSetChanged();
                            }

                            fullAttendeeaddorRemoveFav(attendanceObj.getId());
                        }
                        if (attendanceObj.getAttendeeFull().equalsIgnoreCase("0")) {
                            if (attendanceObj.getIs_fav().equalsIgnoreCase("0")) {
                                attendanceObj.setIs_fav("1");
                                notifyDataSetChanged();
                            } else if (attendanceObj.getIs_fav().equalsIgnoreCase("1")) {
                                attendanceObj.setIs_fav("0");
                                notifyDataSetChanged();
                            }
                        }
//
                    }
                });


                holder.layout_relative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SessionManager.AttenDeeId = attendanceObj.getId();

                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.Attendance_Detail_Fragment;
                                ((MainActivity) context).loadFragment();
                            }
                        }, 300);

                    }
                });

                holder.imageView.setVisibility(View.VISIBLE);
                holder.user_sqrimage.setVisibility(View.GONE);
                final String companyLogourl = MyUrls.Imgurl + attendanceObj.getLogo();
                holder.userName.setText(attendanceObj.getFull_name());
                if (!(attendanceObj.getTitle().equalsIgnoreCase(""))) {
                    if (!(attendanceObj.getCompany_name().equalsIgnoreCase(""))) {
                        holder.userDesc.setVisibility(View.VISIBLE);
                        holder.userDesc.setText(attendanceObj.getTitle() + "," + attendanceObj.getCompany_name());
                    } else {
                        holder.userDesc.setVisibility(View.GONE);
                    }
                } else {
                    holder.userDesc.setVisibility(View.GONE);
                }
                if (attendanceObj.getLogo().equalsIgnoreCase("")) {
                    holder.imageView.setVisibility(View.GONE);
                    holder.txt_profileName.setVisibility(View.VISIBLE);

                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                    if (!(attendanceObj.getFirst_name().equalsIgnoreCase(""))) {
                        if (!(attendanceObj.getLast_name().equalsIgnoreCase(""))) {
                            String name = attendanceObj.getFirst_name().charAt(0) + "" + attendanceObj.getLast_name().charAt(0);
                            holder.txt_profileName.setText(name);
                        } else {
                            holder.txt_profileName.setText("" + attendanceObj.getFirst_name().charAt(0));
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
                        Glide.with(context).load(companyLogourl).asBitmap().override(90, 90).centerCrop().listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {

                                holder.imageView.setVisibility(View.VISIBLE);
                                holder.txt_profileName.setVisibility(View.VISIBLE);
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
                if (sessionManager.getNotification_role().equalsIgnoreCase("Attendee") && sessionManager.getNotification_UserId().equalsIgnoreCase(attendanceObj.getId())) {
                    try {
                        Glide.with(context).load(MyUrls.Imgurl + sessionManager.getUserProfile())
                                .asBitmap().override(90, 90).centerCrop().listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {

                                holder.imageView.setVisibility(View.VISIBLE);
                                holder.txt_profileName.setVisibility(View.VISIBLE);
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
        }
    }


    private void fullAttendeeaddorRemoveFav(String ex_pageId) {
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
    public int getItemCount() {
        return attendanceArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public Attendance getItem(int position) {
        return attendanceArrayList.get(position);
    }

//    @Override
//    public Filter getFilter() {
//        return new AttendaceFilter(this, attendanceArrayList, tmp_postion);
//    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(context, jsonObject.getString("message"));
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        attendeeFullDirectory_fragment.loaddataFromAdapter(jsonData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void updateList(List<Attendance> attendanceArrayList) {
        try {

            this.attendanceArrayList = attendanceArrayList;
            notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
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


    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

}

