package com.allintheloop.Bean.Fundraising;

/**
 * Created by nteam on 10/6/16.
 */
public class Fundraising_Product {
    String id, name, desc, img, action_type, price, max_bid, flag_price;

    public Fundraising_Product(String id, String name, String desc, String img, String action_type, String price, String max_bid, String flag_price) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.img = img;
        this.action_type = action_type;
        this.price = price;
        this.max_bid = max_bid;
        this.flag_price = flag_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMax_bid() {
        return max_bid;
    }

    public void setMax_bid(String max_bid) {
        this.max_bid = max_bid;
    }

    public String getFlag_price() {
        return flag_price;
    }

    public void setFlag_price(String flag_price) {
        this.flag_price = flag_price;
    }
}
