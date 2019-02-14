package com.allintheloop.Bean.ExhibitorListClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nteam on 9/9/17.
 */

public class ExhibitorLinkReprenetativesData {
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Firstname")
    @Expose
    private String firstname;
    @SerializedName("Lastname")
    @Expose
    private String lastname;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Company_name")
    @Expose
    private String companyName;
    @SerializedName("Role_id")
    @Expose
    private String roleId;
    @SerializedName("Organisor_id")
    @Expose
    private String organisorId;

    @SerializedName("logo")
    @Expose
    private String logo;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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
}
