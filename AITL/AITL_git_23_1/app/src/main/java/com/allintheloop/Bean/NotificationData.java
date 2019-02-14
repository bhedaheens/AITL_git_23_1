package com.allintheloop.Bean;

/**
 * Created by nteam on 29/9/16.
 */
public class NotificationData {
    String id, Message, Firstname, Lastname, Ispublic, Issent, Logo, Sender_id;

    public NotificationData(String id, String message, String firstname, String lastname, String ispublic, String issent, String logo, String Sender_id) {
        this.id = id;
        Message = message;
        Firstname = firstname;
        Lastname = lastname;
        Ispublic = ispublic;
        Issent = issent;
        Logo = logo;
        this.Sender_id = Sender_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getIspublic() {
        return Ispublic;
    }

    public void setIspublic(String ispublic) {
        Ispublic = ispublic;
    }

    public String getIssent() {
        return Issent;
    }

    public void setIssent(String issent) {
        Issent = issent;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getSender_id() {
        return Sender_id;
    }

    public void setSender_id(String sender_id) {
        Sender_id = sender_id;
    }
}
