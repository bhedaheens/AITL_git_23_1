package com.allintheloop.Bean;

/**
 * Created by Aiyaz on 5/12/16.
 */

public class checkInProtal {
    String id, firstname, lastname, company_name, title, email, logo, is_checked_in;

    public checkInProtal(String id, String firstname, String lastname, String company_name, String title, String email, String logo, String is_checked_in) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.company_name = company_name;
        this.title = title;
        this.email = email;
        this.logo = logo;
        this.is_checked_in = is_checked_in;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIs_checked_in() {
        return is_checked_in;
    }

    public void setIs_checked_in(String is_checked_in) {
        this.is_checked_in = is_checked_in;
    }
}
