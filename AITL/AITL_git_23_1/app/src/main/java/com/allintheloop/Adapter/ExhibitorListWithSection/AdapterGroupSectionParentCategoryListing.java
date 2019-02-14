package com.allintheloop.Adapter.ExhibitorListWithSection;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allintheloop.Bean.ExhibitorListClass.ExhibitorCategoryListing;
import com.allintheloop.Bean.SectionHeaderParentGroup;
import com.allintheloop.R;
import com.allintheloop.Util.FastScrollRecyclerViewInterface;
import com.allintheloop.Util.SessionManager;

import com.intrusoft.sectionedrecyclerview.SectionRecyclerViewAdapter;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nteam on 1/5/18.
 */

public class AdapterGroupSectionParentCategoryListing extends SectionRecyclerViewAdapter<SectionHeaderParentGroup, ExhibitorCategoryListing, SectionViewHolder, ExhibitorParentCategoryGroupChildViewHolder> implements FastScrollRecyclerViewInterface {

    Context context;
    SessionManager sessionManager;
    ArrayList<SectionHeaderParentGroup> sectionItemList;
    ArrayList<SectionHeaderParentGroup> sectionItemList_orignal;
    private HashMap<String, Integer> mMapIndex;

    public AdapterGroupSectionParentCategoryListing(Context context, ArrayList<SectionHeaderParentGroup> sectionItemList, HashMap<String, Integer> mapIndex) {
        super(context, sectionItemList);
        this.context = context;
        this.sectionItemList = sectionItemList;
        sectionItemList_orignal = new ArrayList<>();
        sectionItemList_orignal.addAll(sectionItemList);
        mMapIndex = mapIndex;
        sessionManager = new SessionManager(context);

    }

    @Override
    public SectionViewHolder onCreateSectionViewHolder(ViewGroup sectionViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.section_item, sectionViewGroup, false);
        return new SectionViewHolder(view);
    }

    @Override
    public ExhibitorParentCategoryGroupChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_countrylist_card, childViewGroup, false);
        return new ExhibitorParentCategoryGroupChildViewHolder(view);
    }

    @Override
    public void onBindSectionViewHolder(SectionViewHolder sectionViewHolder, int sectionPosition, SectionHeaderParentGroup exhibitorListdata) {
        if (!(exhibitorListdata.getSectionText().equalsIgnoreCase(""))) {
            if (exhibitorListdata.getChildItems().size() != 0) {

                sectionViewHolder.name.setVisibility(View.VISIBLE);
                sectionViewHolder.name.setGravity(Gravity.LEFT);
                sectionViewHolder.name.setText(exhibitorListdata.getSectionText());
                sectionViewHolder.name.setTextColor(context.getResources().getColor(R.color.dark_gray));
            } else {
                sectionViewHolder.name.setVisibility(View.GONE);
            }
        } else {
            sectionViewHolder.name.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindChildViewHolder(final ExhibitorParentCategoryGroupChildViewHolder itemViewHolder, int sectionPosition, int childPosition, final ExhibitorCategoryListing attendance) {

        itemViewHolder.mTextView.setText(attendance.getSector());
        if (attendance.isCheck()) {
            itemViewHolder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            itemViewHolder.ivCheck.setVisibility(View.INVISIBLE);
        }

        itemViewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attendance.isCheck()) {
                    attendance.setCheck(false);
                    itemViewHolder.ivCheck.setVisibility(View.INVISIBLE);
                } else {
                    attendance.setCheck(true);
                    itemViewHolder.ivCheck.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void filter(String text) {
        ArrayList<SectionHeaderParentGroup> temp = new ArrayList();

        for (SectionHeaderParentGroup d : sectionItemList_orignal) {
            ArrayList<ExhibitorCategoryListing> tempChild = new ArrayList();
            for (ExhibitorCategoryListing attendance : d.getChildItems()) {
                if (attendance.getSector().toLowerCase().contains(text.toLowerCase())) {
                    tempChild.add(attendance);
                }
            }
            if (tempChild.size() > 0) {
                temp.add(new SectionHeaderParentGroup(tempChild, d.getSectionText(), d.getGroupId()));
            }
        }
        notifyDataChanged(temp);
    }


    @Override
    public HashMap<String, Integer> getMapIndex() {
        return this.mMapIndex;
    }

    public ArrayList<SectionHeaderParentGroup> getSelectedList() {
        return sectionItemList_orignal;
    }

}
