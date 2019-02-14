package com.allintheloop.Bean;

/**
 * Created by Aiyaz on 19/7/17.
 */

public class DataModel {
    String stand_number, page_id, exi_id, company_logo, comapany_name;
    Double latitude, longitude;

    public DataModel(String stand_number, String page_id, String exi_id, String company_logo, String comapany_name, Double latitude, Double longitude) {

        this.stand_number = stand_number;
        this.page_id = page_id;
        this.exi_id = exi_id;
        this.company_logo = company_logo;
        this.comapany_name = comapany_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    public String getExi_id() {
        return exi_id;
    }

    public void setExi_id(String exi_id) {
        this.exi_id = exi_id;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getComapany_name() {
        return comapany_name;
    }

    public void setComapany_name(String comapany_name) {
        this.comapany_name = comapany_name;
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStand_number() {
        return stand_number;
    }

    public void setStand_number(String stand_number) {
        this.stand_number = stand_number;
    }
}
