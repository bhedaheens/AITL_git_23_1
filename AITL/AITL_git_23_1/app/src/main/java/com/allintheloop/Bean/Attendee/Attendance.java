package com.allintheloop.Bean.Attendee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nteam on 23/5/16.
 */
public class Attendance {
    @SerializedName("exhibitor_id")
    @Expose
    private String ex_id;

    @SerializedName("exhibitor_page_id")
    @Expose
    private String ex_pageId;

    @SerializedName("Heading")
    @Expose
    private String ex_heading;

    @SerializedName("Short_desc")
    @Expose
    private String ex_desc;

    @SerializedName("sponsored_category")
    @Expose
    private String sponsored_category = "";


    @SerializedName("Images")
    @Expose
    private String ex_images;

    @SerializedName("company_logo")
    @Expose
    private String ex_comapnyLogo;

    @SerializedName("website_url")
    @Expose
    private String ex_websiteUrl;

    @SerializedName("facebook_url")
    @Expose
    private String ex_facebookUrl;

    @SerializedName("linkedin_url")
    @Expose
    private String ex_linkinUrl;

    @SerializedName("twitter_url")
    @Expose
    private String ex_twitterUrl;

    @SerializedName("phone_number1")
    @Expose
    private String ex_phoneNumber;

    @SerializedName("stand_number")
    @Expose
    private String ex_stand_number;

    @SerializedName("email_address")
    @Expose
    private String ex_emailId;

    @SerializedName("type_id")
    @Expose
    private String typeId;

    @SerializedName("is_favorites")
    @Expose
    private String is_fav = "";

    @SerializedName("country_id")
    @Expose
    private String country_id;


    String userId;

    String id, first_name, last_name, company_name, title, email, logo, common_tag, full_name;

    String spe_id, spe_firstname, spe_lastname, spe_company, spe_title, spe_desc, spe_email, spe_logo, spe_fullname;
    String spon_id, spon_name, spon_logo, spon_company;
    String favrouite_id, favrouite_module_id, favrouite_module_type, favrouite_user_name, favrouite_logo, favrouite_extra;
    String type, exhi_id, exhi_page_id, attendeeFull;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Attendance() {

    }


    public Attendance(String favrouite_id, String favrouite_module_id, String favrouite_module_type, String favrouite_user_name, String favrouite_logo, String favrouite_extra, String common_tag) {
        this.favrouite_id = favrouite_id;
        this.favrouite_module_id = favrouite_module_id;
        this.favrouite_module_type = favrouite_module_type;
        this.favrouite_user_name = favrouite_user_name;
        this.favrouite_logo = favrouite_logo;
        this.favrouite_extra = favrouite_extra;
        this.common_tag = common_tag;

    }


    public Attendance(String spon_id, String spon_name, String spon_logo, String spon_company, String common_tag, String is_fav) {
        this.spon_id = spon_id;
        this.spon_name = spon_name;
        this.spon_logo = spon_logo;
        this.spon_company = spon_company;
        this.common_tag = common_tag;
        this.is_fav = is_fav;
    }

    public Attendance(String ex_id, String ex_pageId, String ex_heading, String ex_desc, String ex_images, String ex_comapnyLogo, String ex_websiteUrl, String ex_facebookUrl, String ex_twitterUrl, String ex_linkinUrl, String ex_phoneNumber, String ex_emailId, String common_tag, String ex_stand_number, String is_fav) // Exibitors List
    {
        this.ex_id = ex_id;
        this.ex_pageId = ex_pageId;
        this.ex_heading = ex_heading;
        this.ex_desc = ex_desc;
        this.ex_images = ex_images;
        this.ex_comapnyLogo = ex_comapnyLogo;
        this.ex_websiteUrl = ex_websiteUrl;
        this.ex_facebookUrl = ex_facebookUrl;
        this.ex_twitterUrl = ex_twitterUrl;
        this.ex_linkinUrl = ex_linkinUrl;
        this.ex_phoneNumber = ex_phoneNumber;
        this.ex_emailId = ex_emailId;
        this.common_tag = common_tag;
        this.ex_stand_number = ex_stand_number;
        this.is_fav = is_fav;
    }

    public Attendance(String ex_id, String ex_pageId, String ex_heading, String ex_desc, String ex_images, String ex_comapnyLogo, String ex_websiteUrl, String ex_facebookUrl, String ex_twitterUrl, String ex_linkinUrl, String ex_phoneNumber, String ex_emailId, String common_tag, String ex_stand_number, String is_fav, String typeId) // Exibitors List
    {
        this.ex_id = ex_id;
        this.ex_pageId = ex_pageId;
        this.ex_heading = ex_heading;
        this.ex_desc = ex_desc;
        this.ex_images = ex_images;
        this.ex_comapnyLogo = ex_comapnyLogo;
        this.ex_websiteUrl = ex_websiteUrl;
        this.ex_facebookUrl = ex_facebookUrl;
        this.ex_twitterUrl = ex_twitterUrl;
        this.ex_linkinUrl = ex_linkinUrl;
        this.ex_phoneNumber = ex_phoneNumber;
        this.ex_emailId = ex_emailId;
        this.common_tag = common_tag;
        this.ex_stand_number = ex_stand_number;
        this.is_fav = is_fav;
        this.typeId = typeId;
    }


    public Attendance(String id, String first_name, String last_name, String company_name, String title, String email, String logo, String common_tag, String full_name, String is_fav, String attendeeFull, String type) // Attendance List
    {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.company_name = company_name;
        this.title = title;
        this.email = email;
        this.logo = logo;
        this.common_tag = common_tag;
        this.full_name = full_name;
        this.is_fav = is_fav;
        this.attendeeFull = attendeeFull;
    }


    public Attendance(String id, String first_name, String last_name, String company_name, String title, String email, String logo, String common_tag, String full_name, String is_fav, String type, String exhi_id, String exhi_page_id, String attendeeFull) // Attendance List
    {

        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.company_name = company_name;
        this.title = title;
        this.email = email;
        this.logo = logo;
        this.common_tag = common_tag;
        this.full_name = full_name;
        this.is_fav = is_fav;
        this.type = type;
        this.exhi_id = exhi_id;
        this.exhi_page_id = exhi_page_id;
        this.attendeeFull = attendeeFull;
    }

    public Attendance(String spe_id, String spe_firstname, String spe_lastname, String spe_company, String spe_title, String spe_desc, String spe_email, String spe_logo, String tag, String spe_fullname, String is_fav) // Speaker List
    {
        this.spe_id = spe_id;
        this.spe_firstname = spe_firstname;
        this.spe_lastname = spe_lastname;
        this.spe_company = spe_company;
        this.spe_title = spe_title;
        this.spe_desc = spe_desc;
        this.spe_email = spe_email;
        this.spe_logo = spe_logo;
        this.common_tag = tag;
        this.spe_fullname = spe_fullname;
        this.is_fav = is_fav;
    }

    public String getAttendeeFull() {
        return attendeeFull;
    }

    public void setAttendeeFull(String attendeeFull) {
        this.attendeeFull = attendeeFull;
    }

    public String getSpe_fullname() {
        return spe_fullname;
    }

    public void setSpe_fullname(String spe_fullname) {
        this.spe_fullname = spe_fullname;
    }

    public String getSpe_id() {
        return spe_id;
    }

    public void setSpe_id(String spe_id) {
        this.spe_id = spe_id;
    }

    public String getSpe_firstname() {
        return spe_firstname;
    }

    public void setSpe_firstname(String spe_firstname) {
        this.spe_firstname = spe_firstname;
    }

    public String getSpe_lastname() {
        return spe_lastname;
    }

    public void setSpe_lastname(String spe_lastname) {
        this.spe_lastname = spe_lastname;
    }

    public String getSpe_company() {
        return spe_company;
    }

    public void setSpe_company(String spe_company) {
        this.spe_company = spe_company;
    }

    public String getSpe_title() {
        return spe_title;
    }

    public void setSpe_title(String spe_title) {
        this.spe_title = spe_title;
    }

    public String getSpe_desc() {
        return spe_desc;
    }

    public void setSpe_desc(String spe_desc) {
        this.spe_desc = spe_desc;
    }

    public String getSpe_email() {
        return spe_email;
    }

    public void setSpe_email(String spe_email) {
        this.spe_email = spe_email;
    }

    public String getSpe_logo() {
        return spe_logo;
    }

    public void setSpe_logo(String spe_logo) {
        this.spe_logo = spe_logo;
    }

    public String getEx_stand_number() {
        return ex_stand_number;
    }

    public void setEx_stand_number(String ex_stand_number) {
        this.ex_stand_number = ex_stand_number;
    }

    public String getCommon_tag() {
        return common_tag;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setCommon_tag(String common_tag) {
        this.common_tag = common_tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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


    public String getEx_id() {
        return ex_id;
    }

    public void setEx_id(String ex_id) {
        this.ex_id = ex_id;
    }

    public String getEx_heading() {
        return ex_heading;
    }

    public void setEx_heading(String ex_heading) {
        this.ex_heading = ex_heading;
    }

    public String getEx_desc() {
        return ex_desc;
    }

    public void setEx_desc(String ex_desc) {
        this.ex_desc = ex_desc;
    }

    public String getEx_images() {
        return ex_images;
    }

    public void setEx_images(String ex_images) {
        this.ex_images = ex_images;
    }

    public String getEx_comapnyLogo() {
        return ex_comapnyLogo;
    }

    public void setEx_comapnyLogo(String ex_comapnyLogo) {
        this.ex_comapnyLogo = ex_comapnyLogo;
    }

    public String getEx_websiteUrl() {
        return ex_websiteUrl;
    }

    public void setEx_websiteUrl(String ex_websiteUrl) {
        this.ex_websiteUrl = ex_websiteUrl;
    }

    public String getEx_facebookUrl() {
        return ex_facebookUrl;
    }

    public void setEx_facebookUrl(String ex_facebookUrl) {
        this.ex_facebookUrl = ex_facebookUrl;
    }

    public String getEx_twitterUrl() {
        return ex_twitterUrl;
    }

    public void setEx_twitterUrl(String ex_twitterUrl) {
        this.ex_twitterUrl = ex_twitterUrl;
    }

    public String getEx_linkinUrl() {
        return ex_linkinUrl;
    }

    public void setEx_linkinUrl(String ex_linkinUrl) {
        this.ex_linkinUrl = ex_linkinUrl;
    }

    public String getEx_phoneNumber() {
        return ex_phoneNumber;
    }

    public void setEx_phoneNumber(String ex_phoneNumber) {
        this.ex_phoneNumber = ex_phoneNumber;
    }

    public String getEx_emailId() {
        return ex_emailId;
    }

    public void setEx_emailId(String ex_emailId) {
        this.ex_emailId = ex_emailId;
    }

    public String getSpon_id() {
        return spon_id;
    }

    public void setSpon_id(String spon_id) {
        this.spon_id = spon_id;
    }

    public String getSpon_name() {
        return spon_name;
    }

    public void setSpon_name(String spon_name) {
        this.spon_name = spon_name;
    }

    public String getSpon_logo() {
        return spon_logo;
    }

    public void setSpon_logo(String spon_logo) {
        this.spon_logo = spon_logo;
    }

    public String getSpon_company() {
        return spon_company;
    }

    public void setSpon_company(String spon_company) {
        this.spon_company = spon_company;
    }

    public String getEx_pageId() {
        return ex_pageId;
    }


    public String getIs_fav() {
        return is_fav;
    }

    public void setIs_fav(String is_fav) {
        this.is_fav = is_fav;
    }

    public void setEx_pageId(String ex_pageId) {
        this.ex_pageId = ex_pageId;
    }


    public String getFavrouite_id() {
        return favrouite_id;
    }

    public void setFavrouite_id(String favrouite_id) {
        this.favrouite_id = favrouite_id;
    }

    public String getFavrouite_module_id() {
        return favrouite_module_id;
    }

    public void setFavrouite_module_id(String favrouite_module_id) {
        this.favrouite_module_id = favrouite_module_id;
    }

    public String getFavrouite_module_type() {
        return favrouite_module_type;
    }

    public void setFavrouite_module_type(String favrouite_module_type) {
        this.favrouite_module_type = favrouite_module_type;
    }

    public String getFavrouite_user_name() {
        return favrouite_user_name;
    }

    public void setFavrouite_user_name(String favrouite_user_name) {
        this.favrouite_user_name = favrouite_user_name;
    }

    public String getFavrouite_logo() {
        return favrouite_logo;
    }

    public void setFavrouite_logo(String favrouite_logo) {
        this.favrouite_logo = favrouite_logo;
    }

    public String getFavrouite_extra() {
        return favrouite_extra;
    }

    public void setFavrouite_extra(String favrouite_extra) {
        this.favrouite_extra = favrouite_extra;
    }


    public String getSponsored_category() {
        return sponsored_category;
    }

    public void setSponsored_category(String sponsored_category) {
        this.sponsored_category = sponsored_category;
    }

    public String getType() {
        return type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExhi_id() {
        return exhi_id;
    }

    public void setExhi_id(String exhi_id) {
        this.exhi_id = exhi_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getExhi_page_id() {
        return exhi_page_id;
    }

    public void setExhi_page_id(String exhi_page_id) {
        this.exhi_page_id = exhi_page_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExhibitor_id() {
        return exhi_id;
    }

    public void setExhibitor_id(String exhi_id) {
        this.exhi_id = exhi_id;
    }

    public String getExhibitor_page_id() {
        return exhi_page_id;
    }


    public void setExhibitor_page_id(String exhi_page_id) {
        this.exhi_page_id = exhi_page_id;
    }

}