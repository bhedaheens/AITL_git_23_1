package com.allintheloop.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.QAList.QaModuleData;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 15/3/17.
 */

public class QaModuleDataAdapter extends RecyclerView.Adapter<QaModuleDataAdapter.ViewHolder> implements Filterable {

    ArrayList<QaModuleData> qaModuleDataArrayList;
    ArrayList<QaModuleData> tmp_qaModuleDataArrayList;
    Context context;
    int tmp_postion;
    SessionManager sessionManager;
    DefaultLanguage.DefaultLang defaultLang;


    public QaModuleDataAdapter(ArrayList<QaModuleData> qaModuleDataArrayList, Context context, SessionManager sessionManager) {
        this.qaModuleDataArrayList = qaModuleDataArrayList;
        tmp_qaModuleDataArrayList = new ArrayList<>();
        tmp_qaModuleDataArrayList.addAll(qaModuleDataArrayList);
        this.context = context;
        this.sessionManager = sessionManager;
        defaultLang = sessionManager.getMultiLangString();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_q_a, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        this.tmp_postion = position;
        final QaModuleData qaModuleData = tmp_qaModuleDataArrayList.get(position);
        holder.place_name.setText(qaModuleData.getSession_name());
        holder.card_QandAAdapter.setContentDescription(qaModuleData.getId());
        holder.txt_totalQuestions.setText(qaModuleData.getTotal_question());
        holder.txt_ques.setText(defaultLang.get50Questions());
        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
            holder.img_question.setColorFilter(Color.parseColor(sessionManager.getTopBackColor()));
            holder.txt_totalQuestions.setTextColor(Color.parseColor(sessionManager.getTopBackColor()));
        } else {
            holder.img_question.setColorFilter(Color.parseColor(sessionManager.getFunTopBackColor()));
            holder.txt_totalQuestions.setTextColor(Color.parseColor(sessionManager.getFunTopBackColor()));
        }
        if (qaModuleData.getSession_date().equalsIgnoreCase("")) {
            holder.txt_date.setVisibility(View.GONE);
        } else {
            holder.txt_date.setVisibility(View.VISIBLE);
            holder.txt_date.setText(qaModuleData.getSession_date());

        }

        holder.card_QandAAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalData.hideSoftKeyboard((MainActivity) context);
                Bundle bundle = new Bundle();
                bundle.putString("session_id", qaModuleData.getId());
                sessionManager.setQaSessionId(qaModuleData.getId());
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.QAListDetail_Fragment;
                ((MainActivity) context).loadFragment(bundle);
            }
        });

        if (qaModuleData.getFulltime().equalsIgnoreCase("")) {
            holder.txt_time.setVisibility(View.GONE);
        } else {
            holder.txt_time.setVisibility(View.VISIBLE);
            holder.txt_time.setText(qaModuleData.getFulltime());

        }

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(10.0f);
        drawable.setStroke(5, context.getResources().getColor(R.color.lightGray));
        holder.card_QandAAdapter.setBackgroundDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return tmp_qaModuleDataArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public QaModuleData getItem(int position) {
        return tmp_qaModuleDataArrayList.get(position);
    }


    @Override
    public Filter getFilter() {
        return new QandAModuleAdapter(this, qaModuleDataArrayList, tmp_postion);
    }


    private static class QandAModuleAdapter extends Filter {
        private final QaModuleDataAdapter qaModuleDataAdapter;
        private final ArrayList<QaModuleData> qaModuleDataArrayList;
        private final ArrayList<QaModuleData> tmp_qaModuleDataArrayList;
        private final int postion;

        public QandAModuleAdapter(QaModuleDataAdapter attendanceAdapter, ArrayList<QaModuleData> qaModuleDataArrayList, int position) {
            this.qaModuleDataAdapter = attendanceAdapter;
            this.qaModuleDataArrayList = qaModuleDataArrayList;
            tmp_qaModuleDataArrayList = new ArrayList<>();
            this.postion = position;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            final FilterResults results = new FilterResults();
            if (constraint.length() <= 0) {
                tmp_qaModuleDataArrayList.addAll(qaModuleDataArrayList);
            } else {
                String filterString = constraint.toString().trim().toLowerCase();
                for (QaModuleData qaModuleDataObj : qaModuleDataArrayList) {
                    String title = qaModuleDataObj.getSession_name().toString().trim().toLowerCase();
                    if (title.contains(filterString.toLowerCase())) {
                        tmp_qaModuleDataArrayList.add(qaModuleDataObj);
                    }
                }
            }
            results.values = tmp_qaModuleDataArrayList;
            results.count = tmp_qaModuleDataArrayList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            qaModuleDataAdapter.tmp_qaModuleDataArrayList.clear();
            qaModuleDataAdapter.tmp_qaModuleDataArrayList.addAll((ArrayList<QaModuleData>) results.values);
            qaModuleDataAdapter.notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView place_name, txt_date, txt_time, txt_totalQuestions, txt_ques;
        CardView card_QandAAdapter;
        ImageView img_question;


        public ViewHolder(View itemView) {
            super(itemView);
            place_name = (TextView) itemView.findViewById(R.id.place_name);
            card_QandAAdapter = (CardView) itemView.findViewById(R.id.card_QandAAdapter);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_totalQuestions = (TextView) itemView.findViewById(R.id.txt_totalQuestions);
            img_question = (ImageView) itemView.findViewById(R.id.img_question);
            txt_ques = (TextView) itemView.findViewById(R.id.txt_ques);


        }
    }
}
