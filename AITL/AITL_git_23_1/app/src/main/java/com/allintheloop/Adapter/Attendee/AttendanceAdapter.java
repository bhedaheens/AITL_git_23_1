package com.allintheloop.Adapter.Attendee;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Bean.Attendee.Attendance;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.Fragment.AttandeeFragments.AttendeeFullDirectory_Fragment;
import com.allintheloop.Fragment.AttandeeFragments.AttendeeMyContact_Fragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.RoundedImageConverter;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by nteam on 23/5/16.
 */
public class AttendanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable, VolleyInterface {

    private static final int TYPE_ITEM = 1;
    private final int VIEW_PROG = 2;
    ArrayList<Attendance> attendanceArrayList;
    ArrayList<Attendance> tmpAttendanceList;
    Context context;
    SessionManager sessionManager;
    int tmp_postion;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    AttendeeFullDirectory_Fragment attendeeFullDirectory_fragment;
    AttendeeMyContact_Fragment attendeeMyContact_fragment;
    UidCommonKeyClass uidCommonKeyClass;
    WrapContentLinearLayoutManager lytManager;
    Activity activity;
    Handler handler;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private int visibleThreshold = 5;


    public AttendanceAdapter(ArrayList<Attendance> attendanceArrayList, Context context, AttendeeMyContact_Fragment attendeeMyContact_fragment, RecyclerView recyclerView, final WrapContentLinearLayoutManager layoutManager, NestedScrollView nestedScrollView, Activity activity) {
        this.attendanceArrayList = attendanceArrayList;
        this.context = context;
        this.attendeeMyContact_fragment = attendeeMyContact_fragment;
        sessionManager = new SessionManager(context);
        tmpAttendanceList = new ArrayList<>();
        tmpAttendanceList.addAll(attendanceArrayList);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(context);
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

    public AttendanceAdapter(ArrayList<Attendance> attendanceArrayList, Context context, AttendeeFullDirectory_Fragment attendeeFullDirectory_fragment, String tag, RecyclerView recyclerView, final WrapContentLinearLayoutManager layoutManager, NestedScrollView nestedScrollView, Activity activity) {
        this.attendanceArrayList = attendanceArrayList;
        this.context = context;
        this.attendeeFullDirectory_fragment = attendeeFullDirectory_fragment;
        sessionManager = new SessionManager(context);
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();
        tmpAttendanceList = new ArrayList<>();
        tmpAttendanceList.addAll(attendanceArrayList);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(context);
        this.activity = activity;
        handler = new Handler();
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                                       @Override
                                                       public void onScrollChange(NestedScrollView scrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {


//                Log.d("Alka", "ScrollX : " + scrollX);
//                Log.d("Alka", "ScrollY : " + scrollY);
//                Log.d("Alka", "Old ScrollX : " + oldScrollX);
//                Log.d("Alka", "Old ScrollY : " + oldScrollY);

                                                           if (scrollY >= oldScrollY) {
                                                               totalItemCount = layoutManager.getItemCount();
                                                               lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                                                               if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                                                                   if (onLoadMoreListener != null) {
                                                                       onLoadMoreListener.onLoadMore();
                                                                   }

                                                                   loading = true;
                                                               }
                                                           }
                                                       }
                                                   }

        );
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_attendance, parent, false);
            return new ViewHolder(itemView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder && getItem(position) != null) {
            final ViewHolder myViewHolder = (ViewHolder) holder;
            this.tmp_postion = position;
            final Attendance attendanceObj = tmpAttendanceList.get(position);
            GradientDrawable drawable = new GradientDrawable();
            Random rnd = new Random();
            if (attendanceObj.getCommon_tag().equalsIgnoreCase("attendance")) {

                if (sessionManager.isLogin()) {
                    if (GlobalData.checkForUIDVersion()) {
                        if (sessionManager.getfavIsEnabled().equalsIgnoreCase("1")
                                && uidCommonKeyClass.getIsOnlyAttendeeUser().equalsIgnoreCase("1")) {
                            myViewHolder.img_star.setVisibility(View.VISIBLE);
                        } else {
                            myViewHolder.img_star.setVisibility(View.GONE);
                        }
                    } else {
                        if (sessionManager.getfavIsEnabled().equalsIgnoreCase("1")
                                && sessionManager.getRolId().equalsIgnoreCase("4")) {//changes applied
                            myViewHolder.img_star.setVisibility(View.VISIBLE);
                        } else {
                            myViewHolder.img_star.setVisibility(View.GONE);
                        }
                    }
                } else {
                    myViewHolder.img_star.setVisibility(View.GONE);
                }

                if (attendanceObj.getIs_fav().equalsIgnoreCase("1")) {
                    myViewHolder.img_star.setColorFilter(Color.parseColor("#FFD06C"));
                } else {
                    myViewHolder.img_star.setColorFilter(Color.parseColor("#939393"));
                }

                myViewHolder.img_star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (attendanceObj.getAttendeeFull().equalsIgnoreCase("1")) {
                            sessionManager.isattendeeStarclick = true;
                            Log.d("AAKASh ADAPTER", "STATUS  : " + sessionManager.isattendeeStarclick);
                            if (attendanceObj.getIs_fav().equalsIgnoreCase("0")) {
                                attendanceObj.setIs_fav("1");
                                notifyDataSetChanged();
                            } else if (attendanceObj.getIs_fav().equalsIgnoreCase("1")) {
                                attendanceObj.setIs_fav("0");
                                notifyDataSetChanged();
                            }

                            fullAttendeeaddorRemoveFav(attendanceObj.getId());
                        } else if (attendanceObj.getAttendeeFull().equalsIgnoreCase("0")) {
                            sessionManager.iscontactattendeeStarclick = true;
                            Log.d("AAKASh ADAPTER", "STATUS  : " + sessionManager.iscontactattendeeStarclick);
                            if (attendanceObj.getIs_fav().equalsIgnoreCase("0")) {
                                attendanceObj.setIs_fav("1");
                                notifyDataSetChanged();
                            } else if (attendanceObj.getIs_fav().equalsIgnoreCase("1")) {
                                attendanceObj.setIs_fav("0");
                                notifyDataSetChanged();
                            }
                            mycontactaddorRemoveFav(attendanceObj.getId());
                        }
//
                    }
                });


                myViewHolder.layout_relative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (attendanceObj.getAttendeeFull().equalsIgnoreCase("1")) {
                            SessionManager.AttenDeeId = attendanceObj.getId();

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Attendance_Detail_Fragment;
                            ((MainActivity) context).loadFragment();
                        } else if (attendanceObj.getAttendeeFull().equalsIgnoreCase("0")) {
                            if (attendanceObj.getType().equalsIgnoreCase("0")) {

                                SessionManager.AttenDeeId = attendanceObj.getId();
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.Attendance_Detail_Fragment;
                                ((MainActivity) context).loadFragment();
                            } else {

                                SessionManager.exhibitor_id = attendanceObj.getExhibitor_id();
                                SessionManager.exhi_pageId = attendanceObj.getExhibitor_page_id();
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
                                ((MainActivity) context).loadFragment();
                            }
                        }

                    }
                });

                myViewHolder.imageView.setVisibility(View.VISIBLE);
                myViewHolder.user_sqrimage.setVisibility(View.GONE);
                final String companyLogourl = MyUrls.Imgurl + attendanceObj.getLogo();
                myViewHolder.userName.setText(attendanceObj.getFull_name());
                if (!(attendanceObj.getTitle().equalsIgnoreCase(""))) {
                    if (!(attendanceObj.getCompany_name().equalsIgnoreCase(""))) {
                        myViewHolder.userDesc.setVisibility(View.VISIBLE);
                        myViewHolder.userDesc.setText(attendanceObj.getTitle() + " " + "at" + " " + attendanceObj.getCompany_name());
                    }
                } else {
                    myViewHolder.userDesc.setVisibility(View.GONE);
                }
                if (attendanceObj.getLogo().equalsIgnoreCase("")) {
                    myViewHolder.imageView.setVisibility(View.GONE);
                    myViewHolder.txt_profileName.setVisibility(View.VISIBLE);

                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                    if (!(attendanceObj.getFirst_name().equalsIgnoreCase(""))) {
                        if (!(attendanceObj.getLast_name().equalsIgnoreCase(""))) {
                            String name = attendanceObj.getFirst_name().charAt(0) + "" + attendanceObj.getLast_name().charAt(0);
                            myViewHolder.txt_profileName.setText(name);
                        } else {
                            myViewHolder.txt_profileName.setText("" + attendanceObj.getFirst_name().charAt(0));
                        }
                        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                            drawable.setShape(GradientDrawable.OVAL);
                            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                            myViewHolder.txt_profileName.setBackgroundDrawable(drawable);
                            myViewHolder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                        } else {
                            drawable.setShape(GradientDrawable.OVAL);
                            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                            myViewHolder.txt_profileName.setBackgroundDrawable(drawable);
                            myViewHolder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                        }
                    }
                } else {
                    try {
                        Glide.with(context).load(companyLogourl).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                myViewHolder.imageView.setVisibility(View.VISIBLE);
                                myViewHolder.txt_profileName.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                myViewHolder.imageView.setVisibility(View.VISIBLE);
                                myViewHolder.txt_profileName.setVisibility(View.GONE);
                                return false;
                            }
                        }).placeholder(context.getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(myViewHolder.imageView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                myViewHolder.imageView.setImageBitmap(RoundedImageConverter.getRoundedCornerBitmap(resource, Color.GRAY, 60, 0, context));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (sessionManager.getNotification_role().equalsIgnoreCase("Attendee") && sessionManager.getNotification_UserId().equalsIgnoreCase(attendanceObj.getId())) {
                    try {
                        Glide.with(context).load(MyUrls.Imgurl + sessionManager.getUserProfile()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                myViewHolder.imageView.setVisibility(View.VISIBLE);
                                myViewHolder.txt_profileName.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                myViewHolder.imageView.setVisibility(View.VISIBLE);
                                myViewHolder.txt_profileName.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(new BitmapImageViewTarget(myViewHolder.imageView) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                myViewHolder.imageView.setImageBitmap(RoundedImageConverter.getRoundedCornerBitmap(resource, Color.GRAY, 60, 0, context));
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (holder instanceof ProgressViewHolder) {

            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        }


    }


    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {

        if (attendanceArrayList.get(position) == null) {
            return VIEW_PROG;
        } else {
            return TYPE_ITEM;
        }
    }


    private void mycontactaddorRemoveFav(String ex_pageId) {
        if (GlobalData.isNetworkAvailable(context)) {
            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.addRemoveUid, Param.addonRemoveFav(sessionManager.getEventId(), sessionManager.getUserId(), ex_pageId, sessionManager.getMenuid()), 2, true, this);
            } else {
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.addRemove, Param.addonRemoveFav(sessionManager.getEventId(), sessionManager.getUserId(), ex_pageId, sessionManager.getMenuid()), 2, true, this);
            }
        } else {
            ToastC.show(context, context.getString(R.string.noInernet));
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
        return tmpAttendanceList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public Attendance getItem(int position) {
        return tmpAttendanceList.get(position);
    }

    @Override
    public Filter getFilter() {
        return new AttendaceFilter(this, attendanceArrayList, tmp_postion);
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
                        attendeeFullDirectory_fragment.loaddataFromAdapter(jsonData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(context, jsonObject.getString("message"));
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        attendeeMyContact_fragment.loaddataFromAdapter(jsonData);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private static class AttendaceFilter extends Filter {
        private final AttendanceAdapter attendanceAdapter;
        private final ArrayList<Attendance> attendanceArrayList;
        private final ArrayList<Attendance> tmpAttendanceList;
        private final int postion;

        public AttendaceFilter(AttendanceAdapter attendanceAdapter, ArrayList<Attendance> attendanceArrayList, int position) {
            this.attendanceAdapter = attendanceAdapter;
            this.attendanceArrayList = attendanceArrayList;
            tmpAttendanceList = new ArrayList<>();
            this.postion = position;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            final FilterResults results = new FilterResults();
            Attendance attendance = attendanceArrayList.get(postion);
            Log.d("AITL", "Filter :- " + attendance.getCommon_tag());
            if (constraint.length() <= 0) {
                tmpAttendanceList.addAll(attendanceArrayList);
            } else {
                final String filterString = constraint.toString().toLowerCase();

                if (attendance.getCommon_tag().equalsIgnoreCase("attendance")) {
                    for (Attendance attenObj : attendanceArrayList) {
                        String title = attenObj.getFull_name().toLowerCase();
                        if (title.contains(filterString)) {
                            tmpAttendanceList.add(attenObj);
                        }
                    }
                } else if (attendance.getCommon_tag().equalsIgnoreCase("Speaker")) {
                    for (Attendance attenObj : attendanceArrayList) {
                        String title = attenObj.getSpe_fullname().toLowerCase();
                        if (title.contains(filterString)) {
                            tmpAttendanceList.add(attenObj);
                        }
                    }
                } else if (attendance.getCommon_tag().equalsIgnoreCase("favoriteList")) {
                    for (Attendance attenObj : attendanceArrayList) {
                        String title = attenObj.getFavrouite_user_name().toLowerCase();
                        if (title.contains(filterString)) {
                            tmpAttendanceList.add(attenObj);
                        }
                    }
                }
            }
            results.values = tmpAttendanceList;
            results.count = tmpAttendanceList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            attendanceAdapter.tmpAttendanceList.clear();
            attendanceAdapter.tmpAttendanceList.addAll((ArrayList<Attendance>) results.values);
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

    class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

}
