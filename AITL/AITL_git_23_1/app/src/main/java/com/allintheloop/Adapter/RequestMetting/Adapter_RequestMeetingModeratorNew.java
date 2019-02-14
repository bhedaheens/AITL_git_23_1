package com.allintheloop.Adapter.RequestMetting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Bean.RequestMeeting.RequestMeetingNewModeratorList;
import com.allintheloop.Fragment.RequestMeetingModule.RequestMettingList_loadFragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.RoundedImageConverter;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nteam on 15/3/18.
 */

public class Adapter_RequestMeetingModeratorNew extends RecyclerView.Adapter<Adapter_RequestMeetingModeratorNew.ViewHolder> implements Filterable, VolleyInterface {
    ArrayList<RequestMeetingNewModeratorList.Datum> datumArrayList;
    ArrayList<RequestMeetingNewModeratorList.Datum> tmp_datumArrayList;
    Context context;
    String types = "";
    String dateData = "";
    SessionManager sessionManager;
    RequestMettingList_loadFragment mettingList_loadFragment;
    boolean isclick = true;
    Dialog dailog;

    public Adapter_RequestMeetingModeratorNew(ArrayList<RequestMeetingNewModeratorList.Datum> datumArrayList, Context context, String types, String dateData, RequestMettingList_loadFragment mettingList_loadFragment) {
        this.datumArrayList = datumArrayList;
        tmp_datumArrayList = new ArrayList<>();
        tmp_datumArrayList.addAll(datumArrayList);
        this.context = context;
        this.types = types;
        this.dateData = dateData;
        this.mettingList_loadFragment = mettingList_loadFragment;
        sessionManager = new SessionManager(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_requestmettinglistnew_fragment, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            final RequestMeetingNewModeratorList.Datum dataobj = tmp_datumArrayList.get(position);

            holder.txt_designaion.setVisibility(View.GONE);

            holder.txt_invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sessionManager.setRequestMeetingId(dataobj.getRequestId());
                    sessionManager.setRequestDate(dataobj.getDate());
                    sessionManager.setRequestTime(dataobj.getTime());
                    sessionManager.setRequestlocation(dataobj.getLocation());
                    if (dataobj.getSenderId().equalsIgnoreCase("")) {
                        sessionManager.setRequestsenderid("");
                    } else {
                        sessionManager.setRequestsenderid(dataobj.getSenderId());
                    }
                    mettingList_loadFragment.fragmentRedirect(dataobj.getRequestId());
                }
            });
            GradientDrawable drawable = new GradientDrawable();

            holder.txt_addtoCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        writeCalendarEvent(dataobj.getDate(), dataobj.getTime(), dataobj.getSenderName() + " " + dataobj.getReciverName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            if (dataobj.getMapLocation().getMapId() == null || dataobj.getMapLocation().getMapId().length() == 0) {
                holder.txt_viewLocation.setVisibility(View.GONE);
            } else {
                holder.txt_viewLocation.setVisibility(View.VISIBLE);
            }


            holder.txt_viewLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sessionManager.setMapid(dataobj.getMapLocation().getMapId());
                    sessionManager.Map_coords = dataobj.getMapLocation().getCoords();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Map_Detail_Fragment;
                    ((MainActivity) context).loadFragment();
                    Log.d("Bhavdip maplocation", dataobj.getMapLocation().toString());
                }
            });

            holder.img_pending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String designtation = "";
                    openDailog(dataobj.getSenderName(), dataobj.getReciverName(), dataobj.getTime(), dataobj.getLocation(), dataobj.getLogo(), "", dataobj.getRequestId(), holder, dataobj.getSenderId(), dataobj.getReceiverId());
                }
            });

            if (dataobj.getLogo().equalsIgnoreCase("")) {
                holder.img_profile.setVisibility(View.GONE);
                holder.txt_profileName.setVisibility(View.VISIBLE);
                if (dataobj.getSenderName().isEmpty() && dataobj.getReciverName().isEmpty()) {
                    holder.txt_profileName.setVisibility(View.GONE);
                } else {
                    holder.txt_profileName.setVisibility(View.VISIBLE);
                    if (!dataobj.getSenderName().isEmpty()) {
                        if (!dataobj.getReciverName().isEmpty()) {
                            holder.txt_profileName.setText("" + dataobj.getSenderName().charAt(0) + "" + dataobj.getReciverName().charAt(0));
                        } else {
                            holder.txt_profileName.setText("" + dataobj.getSenderName().charAt(0));
                        }
                    }
                }
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setCornerRadius(10);
                if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {

                    drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                    holder.txt_profileName.setBackgroundDrawable(drawable);
                    holder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                } else {
                    drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                    holder.txt_profileName.setBackgroundDrawable(drawable);
                    holder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                }
            } else {
                Glide.with(context).load(MyUrls.Imgurl + dataobj.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.img_profile.setVisibility(View.VISIBLE);
                        holder.txt_profileName.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.img_profile.setVisibility(View.VISIBLE);
                        holder.txt_profileName.setVisibility(View.GONE);
                        return false;
                    }
                }).into(new BitmapImageViewTarget(holder.img_profile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.img_profile.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.TRANSPARENT, 10, 0, context));
                    }
                });
            }

            if (dataobj.getStatus().equalsIgnoreCase("1") || dataobj.getStatus().equalsIgnoreCase("3")) {
                if (dataobj.getShow_invite_more().equalsIgnoreCase("1")) {
                    holder.txt_invite.setVisibility(View.VISIBLE);
                } else {
                    holder.txt_invite.setVisibility(View.GONE);
                }
                holder.txt_addtoCalendar.setVisibility(View.VISIBLE);
            } else {
                holder.txt_invite.setVisibility(View.GONE);
                holder.txt_addtoCalendar.setVisibility(View.GONE);
            }
            holder.txt_time.setText(dataobj.getTime_new());
            if (dataobj.getSenderName().isEmpty() && dataobj.getReciverName().isEmpty()) {
                holder.txt_mettingwith.setVisibility(View.GONE);
            } else {
                holder.txt_mettingwith.setVisibility(View.VISIBLE);
                holder.txt_mettingwith.setText(dataobj.getSenderName() + " Meeting with " + dataobj.getReciverName());
            }

            if (dataobj.getLocation().isEmpty()) {
                holder.txt_location.setVisibility(View.GONE);
            } else {
                holder.txt_location.setVisibility(View.VISIBLE);
                holder.txt_location.setText("" + dataobj.getLocation());
            }

            if (types.equalsIgnoreCase("All")) {
                holder.card_meetingBooking.setVisibility(View.VISIBLE);
                if (dataobj.getStatus().equalsIgnoreCase("1") || dataobj.getStatus().equalsIgnoreCase("3")) {
                    holder.img_pending.setVisibility(View.GONE);
                    holder.img_accpet.setVisibility(View.VISIBLE);
                    holder.img_reject.setVisibility(View.GONE);
                } else if (dataobj.getStatus().equalsIgnoreCase("2") || dataobj.getStatus().equalsIgnoreCase("4")) {
                    holder.img_pending.setVisibility(View.GONE);
                    holder.img_accpet.setVisibility(View.GONE);
                    holder.img_reject.setVisibility(View.VISIBLE);
                } else if (dataobj.getStatus().equalsIgnoreCase("0")) {
                    holder.img_pending.setClickable(true);
                    holder.img_pending.setVisibility(View.VISIBLE);
                    holder.img_accpet.setVisibility(View.GONE);
                    holder.img_reject.setVisibility(View.GONE);
                } else {

                    holder.img_pending.setClickable(true);
                    holder.img_pending.setVisibility(View.VISIBLE);
                    holder.img_accpet.setVisibility(View.GONE);
                    holder.img_reject.setVisibility(View.GONE);
                }

            } else if (types.equalsIgnoreCase("Accepted")) {
                if (dataobj.getStatus().equalsIgnoreCase("1") || dataobj.getStatus().equalsIgnoreCase("3")) {
                    holder.card_meetingBooking.setVisibility(View.VISIBLE);

                    holder.img_pending.setVisibility(View.GONE);
                    holder.img_accpet.setVisibility(View.VISIBLE);
                    holder.img_reject.setVisibility(View.GONE);

                } else {
                    holder.card_meetingBooking.setVisibility(View.GONE);
                }
            } else if (types.equalsIgnoreCase("Pending")) {
                if (dataobj.getStatus().equalsIgnoreCase("0")) {
                    holder.img_pending.setClickable(true);
                    holder.img_pending.setVisibility(View.VISIBLE);
                    holder.img_accpet.setVisibility(View.GONE);
                    holder.img_reject.setVisibility(View.GONE);
                    holder.card_meetingBooking.setVisibility(View.VISIBLE);
                } else if (dataobj.getStatus().equalsIgnoreCase("")) {
                    holder.img_pending.setClickable(true);
                    holder.img_pending.setVisibility(View.VISIBLE);
                    holder.img_accpet.setVisibility(View.GONE);
                    holder.img_reject.setVisibility(View.GONE);
                    holder.card_meetingBooking.setVisibility(View.VISIBLE);
                } else {
                    holder.card_meetingBooking.setVisibility(View.GONE);
                }
            } else if (types.equalsIgnoreCase("Rejected")) {
                if (dataobj.getStatus().equalsIgnoreCase("2") || dataobj.getStatus().equalsIgnoreCase("4")) {
                    holder.img_pending.setVisibility(View.GONE);
                    holder.img_accpet.setVisibility(View.GONE);
                    holder.img_reject.setVisibility(View.VISIBLE);
                    holder.card_meetingBooking.setVisibility(View.VISIBLE);
                } else {
                    holder.card_meetingBooking.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openDailog(String firstname, String lastname, String time, String location, String logo, String designation, final String requestId, final ViewHolder holder, final String senderid, final String recevierId) {
        isclick = true;
        TextView txt_time, txt_meetingwith, txt_designation, txt_location;
        final TextView txt_profileName;
        Button btn_accept, btn_reject;
        Button btn_skipRejected, btn_sendMessage;
        ImageView img_close;
        final EditText message;
        final ImageView img_profile;
        GradientDrawable drawable = new GradientDrawable();
        final LinearLayout linear_rejectLayout;
        dailog = new Dialog(context);
        dailog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dailog.setContentView(R.layout.requestmeeting_acceptreject_dialog);
        dailog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dailog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        txt_time = (TextView) dailog.findViewById(R.id.txt_time);
        message = (EditText) dailog.findViewById(R.id.message);
        txt_meetingwith = (TextView) dailog.findViewById(R.id.txt_meetingwith);
        txt_designation = (TextView) dailog.findViewById(R.id.txt_designation);
        txt_profileName = (TextView) dailog.findViewById(R.id.txt_profileName);
        txt_location = (TextView) dailog.findViewById(R.id.txt_location);
        linear_rejectLayout = (LinearLayout) dailog.findViewById(R.id.linear_rejectLayout);

        btn_accept = (Button) dailog.findViewById(R.id.btn_accept);
        btn_reject = (Button) dailog.findViewById(R.id.btn_reject);
        btn_skipRejected = (Button) dailog.findViewById(R.id.btn_skipRejected);
        btn_sendMessage = (Button) dailog.findViewById(R.id.btn_sendMessage);

        img_close = (ImageView) dailog.findViewById(R.id.img_close);
        img_profile = (ImageView) dailog.findViewById(R.id.img_profile);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dailog.cancel();
            }
        });

        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText().toString().length() == 0) {
                    ToastC.show(context, "Please Enter Comment");
                } else {
//                    holder.img_reject.setVisibility(View.VISIBLE);
//                    holder.img_pending.setVisibility(View.GONE);
                    performAccept(requestId, "4", message.getText().toString(), senderid, recevierId);
                }
            }
        });

        btn_skipRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                performAccept(requestId, "4", "", senderid, recevierId);
//                holder.img_reject.setVisibility(View.VISIBLE);
//                holder.img_pending.setVisibility(View.GONE);
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                performAccept(requestId, "3", "", senderid, recevierId);
//                holder.txt_invite.setVisibility(View.VISIBLE);
//                holder.img_accpet.setVisibility(View.VISIBLE);
//                holder.img_pending.setVisibility(View.GONE);
//                holder.txt_addtoCalendar.setVisibility(View.VISIBLE);
            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (isclick) {
//                    isclick = false;
//                    linear_rejectLayout.setVisibility(View.VISIBLE);
//                } else {
//                    isclick = true;
//                    linear_rejectLayout.setVisibility(View.GONE);
//                }

                performAccept(requestId, "4", "", senderid, recevierId);
            }
        });

        txt_time.setText(time);
        if (firstname.isEmpty() && lastname.isEmpty()) {
            txt_meetingwith.setVisibility(View.GONE);
        } else {
            txt_meetingwith.setVisibility(View.VISIBLE);
            txt_meetingwith.setText(firstname + " Meeting with " + lastname);
        }
        if (designation.isEmpty()) {
            txt_designation.setVisibility(View.GONE);
        } else {
            txt_designation.setVisibility(View.VISIBLE);
            txt_designation.setText(designation);
        }
        if (location.isEmpty()) {
            txt_location.setVisibility(View.GONE);
        } else {
            txt_location.setVisibility(View.VISIBLE);
            txt_location.setText(location);
        }

        if (logo != null && logo.equalsIgnoreCase("")) {
            img_profile.setVisibility(View.GONE);
            txt_profileName.setVisibility(View.VISIBLE);
            if (firstname.isEmpty() && lastname.isEmpty()) {
                txt_profileName.setVisibility(View.GONE);
            } else {
                txt_profileName.setVisibility(View.VISIBLE);
                if (!firstname.isEmpty()) {
                    if (!lastname.isEmpty()) {
                        txt_profileName.setText("" + firstname.charAt(0) + "" + lastname.charAt(0));
                    } else {
                        txt_profileName.setText("" + firstname.charAt(0));
                    }
                }

            }
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(10);
            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {

                drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                txt_profileName.setBackgroundDrawable(drawable);
                txt_profileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            } else {
                drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                txt_profileName.setBackgroundDrawable(drawable);
                txt_profileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            }
        } else {
            Glide.with(context).load(MyUrls.Imgurl + logo).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    img_profile.setVisibility(View.VISIBLE);
                    txt_profileName.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    img_profile.setVisibility(View.VISIBLE);
                    txt_profileName.setVisibility(View.GONE);
                    return false;
                }
            }).into(new BitmapImageViewTarget(img_profile) {
                @Override
                protected void setResource(Bitmap resource) {
                    img_profile.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.TRANSPARENT, 10, 0, context));
                }
            });
        }
        dailog.show();
    }


    private void performAccept(String requestId, String status, String message, String senderId, String recevierid) {
        if (GlobalData.isNetworkAvailable((Activity) context)) {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.moderatorRequestResponse, Param.saveOrrejectModeratorRequestMetting(sessionManager.getEventId(), sessionManager.getUserId(), requestId, recevierid, senderId, status), 0, true, this);

        } else {
            ToastC.show(context, context.getResources().getString(R.string.noInernet));
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("Bhadip", jsonObject.toString());
                        dailog.cancel();
                        mettingList_loadFragment.reloadFragment();
//                        GlobalData.reuestMeetingReloadData(getActivity());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void writeCalendarEvent(String cal_startDate, String start_time, String name) {

//
        long startMillis;
        try {
            String str_date = cal_startDate + " " + start_time;
            DateFormat formatter = null;
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = (Date) formatter.parse(str_date);
            startMillis = date.getTime();
            Calendar calendarEvent = Calendar.getInstance();

            Intent i = new Intent(Intent.ACTION_EDIT);
            i.setType("vnd.android.cursor.item/event");
            i.putExtra("beginTime", startMillis);
            i.putExtra("allDay", false);
            i.putExtra("rule", "FREQ=YEARLY");
            i.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
            i.putExtra("title", "You have a meeting with " + name);
            context.startActivity(i);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return tmp_datumArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Adapter_RequestMettingListNewFilter(this, datumArrayList);
    }


    private static class Adapter_RequestMettingListNewFilter extends Filter {

        private final Adapter_RequestMeetingModeratorNew adapterRequestMettingListNew;
        private final ArrayList<RequestMeetingNewModeratorList.Datum> datumArrayList;
        private final ArrayList<RequestMeetingNewModeratorList.Datum> tmp_datumArrayList;

        public Adapter_RequestMettingListNewFilter(Adapter_RequestMeetingModeratorNew documentAdapter, ArrayList<RequestMeetingNewModeratorList.Datum> datumArrayList) {
            this.adapterRequestMettingListNew = documentAdapter;
            this.datumArrayList = datumArrayList;
            tmp_datumArrayList = new ArrayList<>();

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final FilterResults results = new FilterResults();
            if (constraint.length() <= 0) {
                tmp_datumArrayList.addAll(datumArrayList);
            } else {
                final String filter_string = constraint.toString().trim().toLowerCase();
                for (RequestMeetingNewModeratorList.Datum docObj : datumArrayList) {
                    String firstname = docObj.getSenderName().trim().toString().toLowerCase();
                    String lastname = docObj.getReciverName().trim().toString().toLowerCase();
                    String location = docObj.getLocation().trim().toString().toLowerCase();
                    if (firstname.contains(filter_string)
                            || lastname.contains(filter_string)
                            || location.contains(filter_string)) {
                        tmp_datumArrayList.add(docObj);
                    }
                }
            }
            results.values = tmp_datumArrayList;
            results.count = tmp_datumArrayList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapterRequestMettingListNew.tmp_datumArrayList.clear();
            adapterRequestMettingListNew.tmp_datumArrayList.addAll((ArrayList<RequestMeetingNewModeratorList.Datum>) results.values);
            adapterRequestMettingListNew.notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_invite, txt_time, txt_mettingwith, txt_designaion, txt_location, txt_viewLocation, txt_addtoCalendar, txt_profileName;
        ImageView img_accpet, img_pending, img_reject, img_profile;
        CardView card_meetingBooking;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_invite = (TextView) itemView.findViewById(R.id.txt_invite);
            txt_mettingwith = (TextView) itemView.findViewById(R.id.txt_mettingwith);
            txt_designaion = (TextView) itemView.findViewById(R.id.txt_designaion);
            txt_location = (TextView) itemView.findViewById(R.id.txt_location);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            txt_viewLocation = (TextView) itemView.findViewById(R.id.txt_viewLocation);
            txt_addtoCalendar = (TextView) itemView.findViewById(R.id.txt_addtoCalendar);
            img_accpet = (ImageView) itemView.findViewById(R.id.img_accpet);
            img_profile = (ImageView) itemView.findViewById(R.id.img_profile);
            img_pending = (ImageView) itemView.findViewById(R.id.img_pending);
            img_reject = (ImageView) itemView.findViewById(R.id.img_reject);
            card_meetingBooking = (CardView) itemView.findViewById(R.id.card_meetingBooking);
        }
    }

}
