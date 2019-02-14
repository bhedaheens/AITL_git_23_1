package com.allintheloop.Bean.ExhibitorListClass;

/**
 * Created by nteam on 26/1/18.
 */

public class ExhibitorLead_MyLeadData_Offline {

    private String id;
    private String roleId;
    private String organisorId;
    private String firstname;
    private String lastname;
    private String email;
    private String title;
    private String companyName;
    private String data, badgeNumber;

    public String getData() {
        return data;
    }

    public String getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(String badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public void setData(String data) {

        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getOrganisorId() {
        return organisorId;
    }

    public void setOrganisorId(String organisorId) {
        this.organisorId = organisorId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String toString() {
        String s = " id: " + id +
                "    roleId:" + roleId + " \n" +
                "    organisorId:" + organisorId + " \n" +
                "    firstname:" + firstname + " \n" +
                "    lastname:" + lastname + " \n" +
                "    email:" + email + " \n" +
                "    title:" + title + " \n" +
                "    companyName:" + companyName + " \n" +
                "    data:" + data + " \n" +
                "    badgeNumber:" + badgeNumber;
        return s;
    }
}