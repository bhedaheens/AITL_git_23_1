package com.allintheloop.Bean;

import java.io.Serializable;

/**
 * Created by Aiyaz on 25/3/17.
 */

public class Message implements Serializable {

    String id, message, createdAt, buyerRead, sellerRead, userImage, isClickable;
    Customer user;

    public Message() {

    }

    public Message(String id, String message, String createdAt, Customer user, String buyerRead, String sellerRead, String userImage, String isClickable) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
        this.buyerRead = buyerRead;
        this.sellerRead = sellerRead;
        this.userImage = userImage;
        this.isClickable = isClickable;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Customer getUser() {
        return user;
    }

    public void setUser(Customer user) {
        this.user = user;
    }

    public String getBuyerRead() {
        return buyerRead;
    }

    public void setBuyerRead(String buyerRead) {
        this.buyerRead = buyerRead;
    }

    public String getSellerRead() {
        return sellerRead;
    }

    public void setSellerRead(String sellerRead) {
        this.sellerRead = sellerRead;
    }


    public String getIsClickable() {
        return isClickable;
    }

    public void setIsClickable(String isClickable) {
        this.isClickable = isClickable;
    }
}

