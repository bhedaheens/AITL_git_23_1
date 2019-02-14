package com.allintheloop.Bean.Fundraising;

/**
 * Created by nteam on 23/7/16.
 */
public class CartDetail {
    String Id, product_id, name, price, quantity, auctionType, subtotal;
    boolean isChange;

    public CartDetail(String id, String product_id, String name, String price, String quantity, String auctionType, String subtotal) {
        Id = id;
        this.product_id = product_id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.auctionType = auctionType;
        this.subtotal = subtotal;
        this.isChange = false;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAuctionType() {
        return auctionType;
    }

    public void setAuctionType(String auctionType) {
        this.auctionType = auctionType;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setIsChange(boolean isChange) {
        this.isChange = isChange;
    }
}
