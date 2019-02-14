package com.allintheloop.Bean;

/**
 * Created by nteam on 22/8/16.
 */
public class ItemSilentBuyNowList {
    String product_id, user_id, item_name, price, startPrice, reserveBid, approved_status, thumb_img, tag, auction_type;

    public ItemSilentBuyNowList(String product_id, String user_id, String item_name, String startPrice, String reserveBid, String approved_status, String thumb_img, String tag, String auction_type) {
        this.product_id = product_id;
        this.user_id = user_id;
        this.item_name = item_name;
        this.startPrice = startPrice;
        this.reserveBid = reserveBid;
        this.approved_status = approved_status;
        this.thumb_img = thumb_img;
        this.tag = tag;
        this.auction_type = auction_type;
    }

    public ItemSilentBuyNowList(String product_id, String user_id, String item_name, String approved_status, String thumb_img, String startPrice, String tag, String auction_type) {
        this.product_id = product_id;
        this.user_id = user_id;
        this.item_name = item_name;
        this.approved_status = approved_status;
        this.thumb_img = thumb_img;
        this.startPrice = startPrice;
        this.tag = tag;
        this.auction_type = auction_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    public String getReserveBid() {
        return reserveBid;
    }

    public void setReserveBid(String reserveBid) {
        this.reserveBid = reserveBid;
    }

    public String getApproved_status() {
        return approved_status;
    }

    public void setApproved_status(String approved_status) {
        this.approved_status = approved_status;
    }

    public String getThumb_img() {
        return thumb_img;
    }

    public void setThumb_img(String thumb_img) {
        this.thumb_img = thumb_img;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAuction_type() {
        return auction_type;
    }

    public void setAuction_type(String auction_type) {
        this.auction_type = auction_type;
    }
}
