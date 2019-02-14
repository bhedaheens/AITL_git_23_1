package com.allintheloop.Bean.SponsorClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 30/11/17.
 */

public class SponsorListNewData {

    @SerializedName("type")
    @Expose
    String type;

    @SerializedName("bg_color")
    @Expose
    String bg_color;

    @SerializedName("type_position")
    @Expose
    private String type_position;


    @SerializedName("type_id")
    @Expose
    private String typeId;


    @SerializedName("data")
    @Expose
    ArrayList<SponsorNewData> sponsorNewDataArrayList;


    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getType_position() {
        return type_position;
    }

    public void setType_position(String type_position) {
        this.type_position = type_position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBg_color() {
        return bg_color;
    }

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    public ArrayList<SponsorNewData> getSponsorNewDataArrayList() {
        return sponsorNewDataArrayList;
    }

    public void setSponsorNewDataArrayList(ArrayList<SponsorNewData> sponsorNewDataArrayList) {
        this.sponsorNewDataArrayList = sponsorNewDataArrayList;
    }

    public class SponsorNewData {
        @SerializedName("Id")
        @Expose
        private String id;
        @SerializedName("Sponsors_name")
        @Expose
        private String sponsorsName;
        @SerializedName("company_logo")
        @Expose
        private String companyLogo;
        @SerializedName("Company_name")
        @Expose
        private String companyName;
        @SerializedName("is_favorites")
        @Expose
        private String isFavorites;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSponsorsName() {
            return sponsorsName;
        }

        public void setSponsorsName(String sponsorsName) {
            this.sponsorsName = sponsorsName;
        }

        public String getCompanyLogo() {
            return companyLogo;
        }

        public void setCompanyLogo(String companyLogo) {
            this.companyLogo = companyLogo;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getIsFavorites() {
            return isFavorites;
        }

        public void setIsFavorites(String isFavorites) {
            this.isFavorites = isFavorites;
        }

    }

}
