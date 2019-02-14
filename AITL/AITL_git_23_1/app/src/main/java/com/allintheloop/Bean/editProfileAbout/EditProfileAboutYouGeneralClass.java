package com.allintheloop.Bean.editProfileAbout;

import java.io.Serializable;

public class EditProfileAboutYouGeneralClass implements Serializable {
    String keyword, categoryId, categoryName, k;

    public EditProfileAboutYouGeneralClass(String keyword, String categoryId, String categoryName, String k) {
        this.keyword = keyword;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.k = k;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
