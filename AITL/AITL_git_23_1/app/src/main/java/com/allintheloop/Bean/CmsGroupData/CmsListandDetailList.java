package com.allintheloop.Bean.CmsGroupData;

import com.allintheloop.Bean.AgendaData.Agenda;
import com.allintheloop.Bean.AgendaData.AgendaCategory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 17/1/18.
 */

public class CmsListandDetailList {
    @SerializedName("cms_list")
    @Expose
    ArrayList<CmsListData> cmsListDataArrayList;

    public ArrayList<CmsListData> getCmsListDataArrayList() {
        return cmsListDataArrayList;
    }

    public void setCmsListDataArrayList(ArrayList<CmsListData> cmsListDataArrayList) {
        this.cmsListDataArrayList = cmsListDataArrayList;
    }
}
