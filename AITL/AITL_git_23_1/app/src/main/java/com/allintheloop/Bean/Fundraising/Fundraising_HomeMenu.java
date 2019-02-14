package com.allintheloop.Bean.Fundraising;

/**
 * Created by nteam on 9/6/16.
 */
public class Fundraising_HomeMenu {
    String id, is_feature_product, menuname;

    public Fundraising_HomeMenu(String id, String is_feature_product, String menuname) {
        this.id = id;
        this.is_feature_product = is_feature_product;
        this.menuname = menuname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_feature_product() {
        return is_feature_product;
    }

    public void setIs_feature_product(String is_feature_product) {
        this.is_feature_product = is_feature_product;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }
}
