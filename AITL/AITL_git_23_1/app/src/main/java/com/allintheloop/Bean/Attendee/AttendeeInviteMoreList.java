package com.allintheloop.Bean.Attendee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 13/3/18.
 */

public class AttendeeInviteMoreList {

    @SerializedName("attendees")
    @Expose
    public ArrayList<AttendeeinviteMoreData> attendeeinviteMoreDataArrayList;

    public ArrayList<AttendeeinviteMoreData> getAttendeeinviteMoreDataArrayList() {
        return attendeeinviteMoreDataArrayList;
    }

    public void setAttendeeinviteMoreDataArrayList(ArrayList<AttendeeinviteMoreData> attendeeinviteMoreDataArrayList) {
        this.attendeeinviteMoreDataArrayList = attendeeinviteMoreDataArrayList;
    }

    public class AttendeeinviteMoreData {
        @SerializedName("Firstname")
        @Expose
        private String firstname;
        @SerializedName("Lastname")
        @Expose
        private String lastname;
        @SerializedName("Logo")
        @Expose
        private String logo;
        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("Company_name")
        @Expose
        private String companyName;
        @SerializedName("Id")
        @Expose
        private String id;

        public boolean Ischeck() {
            return ischeck;
        }

        public void setIscheck(boolean ischeck) {
            this.ischeck = ischeck;
        }

        boolean ischeck = false;

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }


}
