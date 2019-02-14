package com.allintheloop.Bean.ExhibitorListClass;

/**
 * Created by Aiyaz on 28/7/17.
 */

public class ExhibitorDetailAttendeeList {
    String Id, Firstname, Lastname, Logo;

    public ExhibitorDetailAttendeeList(String id, String firstname, String lastname, String logo) {
        Id = id;
        Firstname = firstname;
        Lastname = lastname;
        Logo = logo;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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
}
