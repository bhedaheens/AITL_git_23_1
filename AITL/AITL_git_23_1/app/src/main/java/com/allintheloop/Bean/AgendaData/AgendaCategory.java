package com.allintheloop.Bean.AgendaData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nteam on 4/12/17.
 */

public class AgendaCategory {


    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_image")
    @Expose
    private String categoryImage;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("show_check_in_time")
    @Expose
    private String showCheckInTime;
    @SerializedName("categorie_type")
    @Expose
    private String categorieType;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
    @SerializedName("sort_order")
    @Expose
    private String sort_order;

    public String getWelcome_screen() {
        return welcome_screen;
    }

    public void setWelcome_screen(String welcome_screen) {
        this.welcome_screen = welcome_screen;
    }

    @SerializedName("welcome_screen")
    @Expose
    private String welcome_screen;


    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getShowCheckInTime() {
        return showCheckInTime;
    }

    public void setShowCheckInTime(String showCheckInTime) {
        this.showCheckInTime = showCheckInTime;
    }

    public String getCategorieType() {
        return categorieType;
    }

    public void setCategorieType(String categorieType) {
        this.categorieType = categorieType;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}

