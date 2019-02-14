package com.allintheloop.Bean.Attendee;

/**
 * Created by Aiyaz on 7/12/16.
 */

public class AttendeeDetailShare {
    String attendeeId, emailId, phoneNumber, country, tag;

    public AttendeeDetailShare(String attendeeId, String emailId, String phoneNumber, String country, String tag) {
        this.attendeeId = attendeeId;
        this.emailId = emailId;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.tag = tag;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
