package com.allintheloop.Bean.ExhibitorListClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 9/9/17.
 */

public class ExhibitorLinRepresenetativesArrayList {
    @SerializedName("rep_list")
    @Expose
    public ArrayList<ExhibitorLinkReprenetativesData> exhibitorLead_myLeadDataArrayList;

    public ArrayList<ExhibitorLinkReprenetativesData> getExhibitorLead_myLeadDataArrayList() {
        return exhibitorLead_myLeadDataArrayList;
    }

    public void setExhibitorLead_myLeadDataArrayList(ArrayList<ExhibitorLinkReprenetativesData> exhibitorLead_myLeadDataArrayList) {
        this.exhibitorLead_myLeadDataArrayList = exhibitorLead_myLeadDataArrayList;
    }
}
