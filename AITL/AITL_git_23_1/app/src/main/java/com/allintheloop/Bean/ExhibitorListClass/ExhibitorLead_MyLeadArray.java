package com.allintheloop.Bean.ExhibitorListClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 8/9/17.
 */

public class ExhibitorLead_MyLeadArray<T> {
    @SerializedName("leads")
    @Expose
    public ArrayList<ExhibitorLead_MyLeadData> exhibitorLead_myLeadDataArrayList;

    public ArrayList<ExhibitorLead_MyLeadData> getExhibitorLead_myLeadDataArrayList() {
        return exhibitorLead_myLeadDataArrayList;
    }

    public void setExhibitorLead_myLeadDataArrayList(ArrayList<ExhibitorLead_MyLeadData> exhibitorLead_myLeadDataArrayList) {
        this.exhibitorLead_myLeadDataArrayList = exhibitorLead_myLeadDataArrayList;
    }
}
