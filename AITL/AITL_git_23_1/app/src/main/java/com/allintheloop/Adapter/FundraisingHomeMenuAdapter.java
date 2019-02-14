package com.allintheloop.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.allintheloop.Bean.Fundraising.Fundraising_HomeMenu;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;


/**
 * Created by nteam on 9/6/16.
 */
public class FundraisingHomeMenuAdapter extends RecyclerView.Adapter<FundraisingHomeMenuAdapter.ViewHolder> {

    ArrayList<Fundraising_HomeMenu> arrayList;
    Context context;
    SessionManager sessionManager;

    public FundraisingHomeMenuAdapter(ArrayList<Fundraising_HomeMenu> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        sessionManager = new SessionManager(this.context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fundraising_homemenu, parent, false);
        return new ViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fundraising_HomeMenu menuObj = arrayList.get(position);
        holder.btn_homemenu.setText(menuObj.getMenuname());


        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);


        drawable.setStroke(5, Color.parseColor(sessionManager.getFunThemeColor()));
        holder.btn_homemenu.setBackgroundDrawable(drawable);
        holder.btn_homemenu.setTextColor(Color.parseColor(sessionManager.getFunThemeColor()));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btn_homemenu;

        public ViewHolder(View itemView) {
            super(itemView);
            btn_homemenu = (Button) itemView.findViewById(R.id.btn_homemenu);
        }
    }
}
