package com.allintheloop.Bean;

/**
 * Created by nteam on 30/4/16.
 */
public class EventList {
    String iD, eName, img, Fb_Status, event_type, fundraising_enbled, linkedin_login_enabled, show_login_screen;
    String lang;

    public EventList(String iD, String eName, String img, String Fb_Status, String event_type, String fundraising_enbled, String linkedin_login_enabled) {
        this.iD = iD;
        this.eName = eName;
        this.img = img;
        this.Fb_Status = Fb_Status;
        this.event_type = event_type;
        this.fundraising_enbled = fundraising_enbled;
        this.linkedin_login_enabled = linkedin_login_enabled;
    }

    public EventList(String iD, String eName, String img, String Fb_Status, String event_type, String fundraising_enbled, String linkedin_login_enabled, String lang, String show_login_screen) {
        this.iD = iD;
        this.eName = eName;
        this.img = img;
        this.Fb_Status = Fb_Status;
        this.event_type = event_type;
        this.fundraising_enbled = fundraising_enbled;
        this.linkedin_login_enabled = linkedin_login_enabled;
        this.lang = lang;
        this.show_login_screen = show_login_screen;
    }


    public String getShow_login_screen() {
        return show_login_screen;
    }

    public void setShow_login_screen(String show_login_screen) {
        this.show_login_screen = show_login_screen;
    }

    public String getFundraising_enbled() {
        return fundraising_enbled;
    }

    public void setFundraising_enbled(String fundraising_enbled) {
        this.fundraising_enbled = fundraising_enbled;
    }

    public String getLinkedin_login_enabled() {
        return linkedin_login_enabled;
    }

    public void setLinkedin_login_enabled(String linkedin_login_enabled) {
        this.linkedin_login_enabled = linkedin_login_enabled;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getiD() {
        return iD;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public String geteName() {
        return eName;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String getFb_Status() {
        return Fb_Status;
    }

    public void setFb_Status(String fb_Status) {
        Fb_Status = fb_Status;
    }
}
