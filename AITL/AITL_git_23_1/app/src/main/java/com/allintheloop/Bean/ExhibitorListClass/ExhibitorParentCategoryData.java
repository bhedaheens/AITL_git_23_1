package com.allintheloop.Bean.ExhibitorListClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nteam on 11/10/17.
 */

public class ExhibitorParentCategoryData implements Serializable {

    @SerializedName("c_id")
    @Expose
    private String c_id;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("categorie_icon")
    @Expose
    private String categorie_icon;

    @SerializedName("sort_order")
    @Expose
    private String sort_order = "";

    boolean isCheck;

    public ExhibitorParentCategoryData(String c_id, String category, String categorie_icon) {
        this.c_id = c_id;
        this.category = category;
        this.categorie_icon = categorie_icon;
    }

    public ExhibitorParentCategoryData() {

    }

    public String getC_id() {
        return c_id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategorie_icon() {
        return categorie_icon;
    }

    public void setCategorie_icon(String categorie_icon) {
        this.categorie_icon = categorie_icon;
    }
}
