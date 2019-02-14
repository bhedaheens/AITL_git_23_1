package com.allintheloop.Bean.AgendaData;

/**
 * Created by nteam on 26/5/16.
 */
public class Agenda_SpeakerList {
    String user_id, logo, first_name, last_name, speakerCategory;

    public Agenda_SpeakerList(String user_id, String logo, String first_name, String last_name, String speakerCategory) {
        this.user_id = user_id;
        this.logo = logo;
        this.first_name = first_name;
        this.last_name = last_name;
        this.speakerCategory = speakerCategory;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getSpeakerCategory() {
        return speakerCategory;
    }

    public void setSpeakerCategory(String speakerCategory) {
        this.speakerCategory = speakerCategory;
    }
}
