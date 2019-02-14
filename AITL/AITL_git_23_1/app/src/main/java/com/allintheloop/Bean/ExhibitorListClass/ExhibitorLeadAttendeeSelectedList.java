package com.allintheloop.Bean.ExhibitorListClass;

/**
 * Created by nteam on 11/9/17.
 */

public class ExhibitorLeadAttendeeSelectedList {
    String Id, logo, Firstname, Lastname, Title, Company_name;
    int isSelected;

    public ExhibitorLeadAttendeeSelectedList(String id, String logo, String firstname, String lastname, String title, String company_name, int isSelected) {
        this.Id = id;
        this.logo = logo;
        this.Firstname = firstname;
        this.Lastname = lastname;
        this.Title = title;
        this.Company_name = company_name;
        this.isSelected = isSelected;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCompany_name() {
        return Company_name;
    }

    public void setCompany_name(String company_name) {
        Company_name = company_name;
    }
}
