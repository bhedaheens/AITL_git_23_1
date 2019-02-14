package com.allintheloop.Bean.ExhibitorListClass;

/**
 * Created by nteam on 8/9/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExhibitorLead_MyLeadData {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Role_id")
    @Expose
    private String roleId;
    @SerializedName("Organisor_id")
    @Expose
    private String organisorId;
    @SerializedName("Firstname")
    @Expose
    private String firstname;
    @SerializedName("Lastname")
    @Expose
    private String lastname;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Company_name")
    @Expose
    private String companyName;

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

}