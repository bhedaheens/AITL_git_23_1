package com.allintheloop.Bean.Attendee;

/**
 * Created by Aiyaz on 28/7/17.
 */

public class AttndeeDetailExhibitorList {
    String exhibitor_page_id, Heading, company_logo, stand_number, exhibitor_id;

    public AttndeeDetailExhibitorList(String exhibitor_page_id, String heading, String company_logo, String stand_number, String exhibitor_id) {
        this.exhibitor_page_id = exhibitor_page_id;
        this.Heading = heading;
        this.company_logo = company_logo;
        this.stand_number = stand_number;
        this.exhibitor_id = exhibitor_id;
    }

    public String getExhibitor_page_id() {
        return exhibitor_page_id;
    }

    public void setExhibitor_page_id(String exhibitor_page_id) {
        this.exhibitor_page_id = exhibitor_page_id;
    }

    public String getHeading() {
        return Heading;
    }

    public void setHeading(String heading) {
        Heading = heading;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getStand_number() {
        return stand_number;
    }

    public void setStand_number(String stand_number) {
        this.stand_number = stand_number;
    }

    public String getExhibitor_id() {
        return exhibitor_id;
    }

    public void setExhibitor_id(String exhibitor_id) {
        this.exhibitor_id = exhibitor_id;
    }
}
