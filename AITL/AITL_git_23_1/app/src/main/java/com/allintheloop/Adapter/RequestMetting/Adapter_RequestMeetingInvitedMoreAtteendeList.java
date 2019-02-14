package com.allintheloop.Adapter.RequestMetting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Bean.Attendee.AttendeeInviteMoreList;
import com.allintheloop.R;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.RoundedImageConverter;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by nteam on 14/3/18.
 */

public class Adapter_RequestMeetingInvitedMoreAtteendeList extends RecyclerView.Adapter<Adapter_RequestMeetingInvitedMoreAtteendeList.ViewHolder> implements Filterable {
    ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData> objectArrayList;
    ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData> tmp_objectArrayList;
    Context context;
    SessionManager sessionManager;

    public Adapter_RequestMeetingInvitedMoreAtteendeList(ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData> objectArrayList, Context context) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        tmp_objectArrayList = new ArrayList<>();
        tmp_objectArrayList.addAll(objectArrayList);
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_requestmeetinginvitemore_fragment, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AttendeeInviteMoreList.AttendeeinviteMoreData moreData = (AttendeeInviteMoreList.AttendeeinviteMoreData) tmp_objectArrayList.get(position);
        holder.userName.setText(moreData.getFirstname() + " " + moreData.getLastname());
        GradientDrawable drawable = new GradientDrawable();
        holder.check.setVisibility(View.GONE);
        if (moreData.getTitle().isEmpty()) {
            holder.userDesc.setVisibility(View.GONE);
        } else {
            holder.userDesc.setVisibility(View.VISIBLE);
            if (moreData.getCompanyName().isEmpty()) {
                holder.userDesc.setText(moreData.getTitle());
            } else {
                holder.userDesc.setText(moreData.getTitle() + " at " + moreData.getCompanyName());
            }
        }


        if (moreData.getLogo().equalsIgnoreCase("")) {
            holder.user_sqrimage.setVisibility(View.GONE);
            holder.txt_profileName.setVisibility(View.VISIBLE);

//            Log.d("AITL SPONSOR Color", "" + color);
//            Log.d("AITL SPONSOR COMPANY", "" + attendance.getCompanyName());
            if (moreData.getFirstname().isEmpty() && moreData.getLastname().isEmpty()) {
                holder.txt_profileName.setVisibility(View.GONE);
            } else {
                holder.txt_profileName.setVisibility(View.VISIBLE);
                if (!moreData.getFirstname().isEmpty()) {
                    if (!moreData.getLastname().isEmpty()) {
                        holder.txt_profileName.setText("" + moreData.getFirstname().charAt(0) + "" + moreData.getLastname().charAt(0));
                    } else {
                        holder.txt_profileName.setText("" + moreData.getFirstname().charAt(0));
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
            Glide.with(context).load(MyUrls.Imgurl + moreData.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    holder.user_sqrimage.setVisibility(View.VISIBLE);
                    holder.txt_profileName.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    holder.user_sqrimage.setVisibility(View.VISIBLE);
                    holder.txt_profileName.setVisibility(View.GONE);
                    return false;
                }
            }).into(new BitmapImageViewTarget(holder.user_sqrimage) {
                @Override
                protected void setResource(Bitmap resource) {
                    holder.user_sqrimage.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.TRANSPARENT, 10, 0, context));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return tmp_objectArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Adapter_InViteMoreFilter(this, objectArrayList);
    }


    private static class Adapter_InViteMoreFilter extends Filter {

        private final Adapter_RequestMeetingInvitedMoreAtteendeList adapterRequestMettingListNew;
        private final ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData> datumArrayList;
        private final ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData> tmp_datumArrayList;

        public Adapter_InViteMoreFilter(Adapter_RequestMeetingInvitedMoreAtteendeList documentAdapter, ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData> datumArrayList) {
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

                for (AttendeeInviteMoreList.AttendeeinviteMoreData docObj : datumArrayList) {
                    String firstname = docObj.getFirstname().trim().toString().toLowerCase();
                    String lastname = docObj.getLastname().trim().toString().toLowerCase();
                    String companyname = docObj.getCompanyName().trim().toString().toLowerCase();
                    String title = docObj.getTitle().trim().toString().toLowerCase();
                    if (firstname.contains(filter_string)
                            || lastname.contains(filter_string)
                            || companyname.contains(filter_string)
                            || title.contains(filter_string)) {
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
            adapterRequestMettingListNew.tmp_objectArrayList.clear();
            adapterRequestMettingListNew.tmp_objectArrayList.addAll((ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData>) results.values);
            adapterRequestMettingListNew.notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userDesc, txt_profileName;
        ImageView user_sqrimage;
        RelativeLayout layout_relative;
        CardView app_back;
        CheckBox check;

        public ViewHolder(View itemView) {
            super(itemView);
            user_sqrimage = (ImageView) itemView.findViewById(R.id.user_sqrimage);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userDesc = (TextView) itemView.findViewById(R.id.user_desc);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            layout_relative = (RelativeLayout) itemView.findViewById(R.id.layout_relative);
            app_back = (CardView) itemView.findViewById(R.id.app_back);
            check = (CheckBox) itemView.findViewById(R.id.check_group);
        }
    }

}