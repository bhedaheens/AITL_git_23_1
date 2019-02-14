package com.allintheloop.Bean.Attendee;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nteam on 28/3/18.
 */

public class AttendeeCategoryList implements Serializable {


    @SerializedName("attendee_categories")
    @Expose
    private List<AttendeeCategory> attendeeCategories = null;

    public List<AttendeeCategory> getAttendeeCategories() {
        return attendeeCategories;
    }

    public void setAttendeeCategories(List<AttendeeCategory> attendeeCategories) {
        this.attendeeCategories = attendeeCategories;
    }


    public class AttendeeCategory implements Serializable {


        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("categorie_keywords")
        @Expose
        private String categorieKeywords;
        @SerializedName("categorie_icon")
        @Expose
        private String categorieIcon;
        @SerializedName("event_id")
        @Expose
        private String eventId;
        @SerializedName("menu_id")
        @Expose
        private String menuId;
        @SerializedName("category_type")
        @Expose
        private String categoryType;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
        @SerializedName("updated_date")
        @Expose
        private String updatedDate;


        boolean check;


        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCategorieKeywords() {
            return categorieKeywords;
        }


        public void setCategorieKeywords(String categorieKeywords) {
            this.categorieKeywords = categorieKeywords;
        }

        public String getCategorieIcon() {
            return categorieIcon;
        }

        public void setCategorieIcon(String categorieIcon) {
            this.categorieIcon = categorieIcon;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getMenuId() {
            return menuId;
        }

        public void setMenuId(String menuId) {
            this.menuId = menuId;
        }

        public String getCategoryType() {
            return categoryType;
        }

        public void setCategoryType(String categoryType) {
            this.categoryType = categoryType;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }


    }


}
