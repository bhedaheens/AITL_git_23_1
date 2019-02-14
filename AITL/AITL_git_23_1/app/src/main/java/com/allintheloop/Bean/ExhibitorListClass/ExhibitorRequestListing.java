package com.allintheloop.Bean.ExhibitorListClass;

/**
 * Created by Aiyaz on 16/12/16.
 */

public class ExhibitorRequestListing {
    String Firstname, Lastname, date, time, status, request_id, exhibiotor_id, logo, tag, comment, txt_cmpnyName, txt_title;

    public ExhibitorRequestListing(String firstname, String lastname, String date, String time, String status, String request_id, String exhibiotor_id, String logo, String tag, String comment, String txt_cmpnyName, String txt_title) {
        Firstname = firstname;
        Lastname = lastname;
        this.date = date;
        this.time = time;
        this.status = status;
        this.request_id = request_id;
        this.exhibiotor_id = exhibiotor_id;
        this.logo = logo;
        this.tag = tag;
        this.comment = comment;
        this.txt_cmpnyName = txt_cmpnyName;
        this.txt_title = txt_title;
    }

    public String getTxt_title() {
        return txt_title;
    }

    public void setTxt_title(String txt_title) {
        this.txt_title = txt_title;
    }

    public String getTxt_cmpnyName() {
        return txt_cmpnyName;
    }

    public void setTxt_cmpnyName(String txt_cmpnyName) {
        this.txt_cmpnyName = txt_cmpnyName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getExhibiotor_id() {
        return exhibiotor_id;
    }

    public void setExhibiotor_id(String exhibiotor_id) {
        this.exhibiotor_id = exhibiotor_id;
    }


}
