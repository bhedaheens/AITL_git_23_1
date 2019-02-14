package com.allintheloop.Bean.ExhibitorListClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Aiyaz on 29/6/17.
 */

public class ExhibitorCategoryListing implements Serializable {


    @SerializedName("c_id")
    @Expose
    private String Id;

    @SerializedName("category")
    @Expose
    private String sector;

    @SerializedName("categorie_icon")
    @Expose
    private String img;

    @SerializedName("pcategory_id")
    @Expose
    private String pcategory_id;


    @SerializedName("exhi_cat_group_id")
    @Expose
    private String exhi_cat_group_id;


    @SerializedName("short_desc")
    @Expose
    String shortDesc;

    boolean isSelected;

    boolean isCheck;

    public ExhibitorCategoryListing(String id, String sector, String img, boolean isSelected) {
        this.Id = id;
        this.sector = sector;
        this.img = img;
        this.isSelected = isSelected;
    }

    public ExhibitorCategoryListing() {

    }

    public String getExhi_cat_group_id() {
        return exhi_cat_group_id;
    }

    public void setExhi_cat_group_id(String exhi_cat_group_id) {
        this.exhi_cat_group_id = exhi_cat_group_id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPcategory_id() {
        return pcategory_id;
    }

    public void setPcategory_id(String pcategory_id) {
        this.pcategory_id = pcategory_id;
    }
}
