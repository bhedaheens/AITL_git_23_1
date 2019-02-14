package com.allintheloop.Adapter.MatchMaking;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.MatchMaking.MatchMakingListingData;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MatchMakingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<MatchMakingListingData> dataArrayList;
    SessionManager sessionManager;

    public MatchMakingListAdapter(Context context, ArrayList<MatchMakingListingData> dataArrayList, SessionManager sessionManager) {
        this.context = context;
        this.dataArrayList = dataArrayList;
        this.sessionManager = sessionManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_matchmaking_listingdata, parent, false);
            return new ViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_progress, parent, false);
            return new ProgressViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            final ViewHolder holder = (ViewHolder) viewHolder;
            MatchMakingListingData dataObj = dataArrayList.get(position);
            GradientDrawable drawable = new GradientDrawable();

            holder.card_matchMaking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dataObj.getStrModuleId().equalsIgnoreCase("2")) {
                        SessionManager.AttenDeeId = dataObj.getStrId();
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Attendance_Detail_Fragment;
                        ((MainActivity) context).loadFragment();
                    } else if (dataObj.getStrModuleId().equalsIgnoreCase("3")) {
                        GlobalData.temp_Fragment = 0;
                        SessionManager.exhibitor_id = dataObj.getStrId();
                        SessionManager.exhi_pageId = dataObj.getStrExhiPageId();
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
                        ((MainActivity) context).loadFragment();
                    } else if (dataObj.getStrModuleId().equalsIgnoreCase("7")) {
                        SessionManager.speaker_id = dataObj.getStrId();
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Speaker_Detail_Fragment;
                        ((MainActivity) context).loadFragment();
                    } else if (dataObj.getStrModuleId().equalsIgnoreCase("43")) {
                        sessionManager.sponsor_id = dataObj.getStrId();
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Sponsor_Detail_Fragment;
                        ((MainActivity) context).loadFragment();
                    }
                }
            });


            holder.txt_title.setText(dataObj.getStrTitle());
            holder.txt_subTitle.setText(dataObj.getStrSubTitle());
            if (dataObj.getStrLogo().equalsIgnoreCase("")) {
                holder.img_logo.setVisibility(View.GONE);
                holder.txt_profileName.setVisibility(View.VISIBLE);

                if (!(dataObj.getStrTitle().equalsIgnoreCase(""))) {

                    holder.txt_profileName.setText("" + dataObj.getStrTitle().charAt(0));
                }
                if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                    holder.frame_logo.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                    holder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                } else {
                    holder.frame_logo.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                    holder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                }
            } else {
                try {
                    holder.frame_logo.setBackgroundColor(context.getResources().getColor(R.color.white));
                    holder.img_logo.setVisibility(View.VISIBLE);
                    holder.txt_profileName.setVisibility(View.GONE);
                    Glide.with(context).load(MyUrls.Imgurl + dataObj.getStrLogo()).override(90, 90).into(holder.img_logo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        return dataArrayList.get(position) == null ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_logo;
        TextView txt_title, txt_subTitle, txt_profileName;
        FrameLayout frame_logo;
        CardView card_matchMaking;

        public ViewHolder(View itemView) {
            super(itemView);
            img_logo = (ImageView) itemView.findViewById(R.id.img_logo);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_subTitle = (TextView) itemView.findViewById(R.id.txt_subTitle);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            frame_logo = (FrameLayout) itemView.findViewById(R.id.frame_logo);
            card_matchMaking = (CardView) itemView.findViewById(R.id.card_matchMaking);
        }
    }

    public void updateList(ArrayList<MatchMakingListingData> attendanceArrayList) {
        try {
//            this.attendanceArrayList=attendanceArrayList;
            this.dataArrayList = attendanceArrayList;
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }

}
