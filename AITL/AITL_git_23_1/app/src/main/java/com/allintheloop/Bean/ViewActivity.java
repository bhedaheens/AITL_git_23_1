package com.allintheloop.Bean;

/**
 * Created by nteam on 24/8/16.
 */
public class ViewActivity {
    String id, Firstname, Lastname, Logo, Message, Time, rating, title, navigation_title, type;

    public ViewActivity(String id, String firstname, String lastname, String logo, String message, String time, String rating, String title, String navigation_title, String type) {
        this.id = id;
        Firstname = firstname;
        Lastname = lastname;
        Logo = logo;
        Message = message;
        Time = time;
        this.rating = rating;
        this.title = title;
        this.navigation_title = navigation_title;
        this.type = type;
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

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNavigation_title() {
        return navigation_title;
    }

    public void setNavigation_title(String navigation_title) {
        this.navigation_title = navigation_title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
