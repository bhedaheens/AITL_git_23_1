package com.allintheloop.Adapter.EditProfile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.allintheloop.Bean.Attendee.AttendeeFilterList;
import com.allintheloop.Bean.editProfileAbout.EditProfileKeywordUpdateData;
import com.allintheloop.Fragment.EditProfileModule.ProfileEditAboutYou_activity;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ProfileAboutkeywordAdapter extends RecyclerView.Adapter<ProfileAboutkeywordAdapter.ViewHolder> {
    Context context;
    List<AttendeeFilterList.Data> dataList;
    boolean isFromSearch;
    int position;
    ProfileEditAboutYou_activity aboutYou_activity;

    public ProfileAboutkeywordAdapter(Context context, List<AttendeeFilterList.Data> dataList, int position, boolean isFromSearch, ProfileEditAboutYou_activity aboutYou_activity) {
        this.context = context;
        this.dataList = dataList;
        this.position = position;
        this.isFromSearch = isFromSearch;
        this.aboutYou_activity = aboutYou_activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_about_you_keyword_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AttendeeFilterList.Data datalistKeyword = dataList.get(this.position);
        holder.txtName.setText(datalistKeyword.getKeywordData().get(position).getKeyword());

        if (datalistKeyword.getKeywordData().get(position).isCheck()) {
            datalistKeyword.getKeywordData().get(position).setCheck(true);
            holder.ivCheck.setChecked(true);
            EventBus.getDefault().post(new EditProfileKeywordUpdateData(datalistKeyword.getColumnName()
                    , datalistKeyword.getKeywordData().get(position).getKeyword()
                    , datalistKeyword.getId()
                    , datalistKeyword.getK()
                    , true, true));
        } else {
            datalistKeyword.getKeywordData().get(position).setCheck(false);
            holder.ivCheck.setChecked(false);
//            EventBus.getDefault().post(new EditProfileKeywordUpdateData(datalistKeyword.getColumnName()
//                    ,datalistKeyword.getKeywordData().get(position).getKeyword()
//                    ,datalistKeyword.getId()
//                    ,datalistKeyword.getK()
//                    ,false));
        }

        holder.ivCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aboutYou_activity.isCheckedLeft()) {
                    aboutYou_activity.switchCountEditorNot(true);
                    if (datalistKeyword.getKeywordData().get(position).isCheck()) {
                        holder.ivCheck.setChecked(false);
                        datalistKeyword.getKeywordData().get(position).setCheck(false);
                        EventBus.getDefault().post(new EditProfileKeywordUpdateData(datalistKeyword.getColumnName()
                                , datalistKeyword.getKeywordData().get(position).getKeyword()
                                , datalistKeyword.getId()
                                , datalistKeyword.getK()
                                , false, true));
                    } else {
                        holder.ivCheck.setChecked(true);
                        datalistKeyword.getKeywordData().get(position).setCheck(true);
                        EventBus.getDefault().post(new EditProfileKeywordUpdateData(datalistKeyword.getColumnName()
                                , datalistKeyword.getKeywordData().get(position).getKeyword()
                                , datalistKeyword.getId()
                                , datalistKeyword.getK()
                                , true, true));
                    }
                } else {
                    aboutYou_activity.switchCountEditorNot(true);
                    if (datalistKeyword.getKeywordData().get(position).isCheck()) {
                        holder.ivCheck.setChecked(false);
                        datalistKeyword.getKeywordData().get(position).setCheck(false);
                        EventBus.getDefault().post(new EditProfileKeywordUpdateData(datalistKeyword.getColumnName()
                                , datalistKeyword.getKeywordData().get(position).getKeyword()
                                , datalistKeyword.getId()
                                , datalistKeyword.getK()
                                , false, true));
                    } else {
                        holder.ivCheck.setChecked(false);
                        aboutYou_activity.setLimitExceedMessage();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.get(this.position).getKeywordData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        BoldTextView txtName;
        CheckBox ivCheck;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            ivCheck = itemView.findViewById(R.id.ivCheck);
        }
    }
}