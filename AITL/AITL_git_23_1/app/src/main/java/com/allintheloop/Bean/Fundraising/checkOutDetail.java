package com.allintheloop.Bean.Fundraising;

/**
 * Created by nteam on 26/7/16.
 */
public class checkOutDetail {
    String id, thumb_img, name, qty, price;

    public checkOutDetail(String id, String thumb_img, String name, String qty, String price) {
        this.id = id;
        this.thumb_img = thumb_img;
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumb_img() {
        return thumb_img;
    }

    public void setThumb_img(String thumb_img) {
        this.thumb_img = thumb_img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
