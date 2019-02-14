package com.allintheloop.Bean.ExhibitorListClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 20/11/17.
 */

public class ExhibitorOfflineData {

    @SerializedName("exhibitor_list")
    @Expose
    private ArrayList<ExhibitorListdata> exhibitor_list = new ArrayList<>();

    @SerializedName("show_meeting_button")
    @Expose
    private String show_meeting_button;


    @SerializedName("total_pages")
    @Expose
    private String total_pages;

    @SerializedName("categories")
    @Expose
    private ArrayList<ExhibitorCategoryListing> categories = new ArrayList<>();

    @SerializedName("pcategories")
    @Expose
    private ArrayList<ExhibitorParentCategoryData> pcategories = new ArrayList<>();


    @SerializedName("exhi_cat_group")
    @Expose
    private ArrayList<ExhibitorParentCatGroup> exhibitorParentCatGroups = new ArrayList<>();

    @SerializedName("countries")
    @Expose
    private ArrayList<ExhibitorCountry> countries = new ArrayList<>();

    public ArrayList<ExhibitorParentCatGroup> getExhibitorParentCatGroups() {
        return exhibitorParentCatGroups;
    }

    public void setExhibitorParentCatGroups(ArrayList<ExhibitorParentCatGroup> exhibitorParentCatGroups) {
        this.exhibitorParentCatGroups = exhibitorParentCatGroups;
    }

    public ArrayList<ExhibitorListdata> getExhibitor_list() {
        return exhibitor_list;
    }

    public void setExhibitor_list(ArrayList<ExhibitorListdata> exhibitor_list) {
        this.exhibitor_list = exhibitor_list;
    }

    public String getShow_meeting_button() {
        return show_meeting_button;
    }

    public void setShow_meeting_button(String show_meeting_button) {
        this.show_meeting_button = show_meeting_button;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public ArrayList<ExhibitorCategoryListing> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<ExhibitorCategoryListing> categories) {
        this.categories = categories;
    }

    public ArrayList<ExhibitorParentCategoryData> getPcategories() {
        return pcategories;
    }

    public void setPcategories(ArrayList<ExhibitorParentCategoryData> pcategories) {
        this.pcategories = pcategories;
    }

    public ArrayList<ExhibitorCountry> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<ExhibitorCountry> countries) {
        this.countries = countries;
    }


}
