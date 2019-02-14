package com.allintheloop.Bean;

import com.allintheloop.Bean.ExhibitorListClass.ExhibitorCategoryListing;
import com.intrusoft.sectionedrecyclerview.Section;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nteam on 1/5/18.
 */

public class SectionHeaderParentGroup implements Section<ExhibitorCategoryListing>, Serializable {

    ArrayList<ExhibitorCategoryListing> exhibitorCategoryListings;
    String sectionText;
    String groupId;


    public SectionHeaderParentGroup(ArrayList<ExhibitorCategoryListing> exhibitorCategoryListings, String sectionText, String groupId) {
        this.exhibitorCategoryListings = exhibitorCategoryListings;
        this.sectionText = sectionText;
        this.groupId = groupId;
    }


    public ArrayList<ExhibitorCategoryListing> getExhibitorCategoryListings() {
        return exhibitorCategoryListings;
    }

    public void setExhibitorCategoryListings(ArrayList<ExhibitorCategoryListing> exhibitorCategoryListings) {
        this.exhibitorCategoryListings = exhibitorCategoryListings;
    }

    public String getSectionText() {
        return sectionText;
    }

    public void setSectionText(String sectionText) {
        this.sectionText = sectionText;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public ArrayList<ExhibitorCategoryListing> getChildItems() {
        return exhibitorCategoryListings;
    }


}
