package com.allintheloop.Bean.ExhibitorListClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 11/9/17.
 */

public class ExhibitorLead_SurveyListData {
    @SerializedName("survey")
    @Expose
    public ArrayList<ExhibitorLead_SurveyData> exhibitorLead_myLeadDataArrayList;

    public ArrayList<ExhibitorLead_SurveyData> getExhibitorLead_myLeadDataArrayList() {
        return exhibitorLead_myLeadDataArrayList;
    }

    public void setExhibitorLead_myLeadDataArrayList(ArrayList<ExhibitorLead_SurveyData> exhibitorLead_myLeadDataArrayList) {
        this.exhibitorLead_myLeadDataArrayList = exhibitorLead_myLeadDataArrayList;
    }
}
