package com.allintheloop.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UidCommonKeyClass {
    @SerializedName("attendee_show_request_meeting")
    @Expose
    private String attendeeShowRequestMeeting;

    @SerializedName("attendee_share_contact")
    @Expose
    private String attendeeShareContact;

    @SerializedName("attendee_message_permission")
    @Expose
    private String attendeeMessagePermission;

    @SerializedName("attendee_show_send_message")
    @Expose
    private String attendeeShowSendMessage;

    @SerializedName("attendee_show_send_request")
    @Expose
    private String attendeeShowSendRequest;

    @SerializedName("exhibitor_show_request_meeting")
    @Expose
    private String exhibitorShowRequestMeeting;

    @SerializedName("exhibitor_show_myfavorite_btn")
    @Expose
    private String exhibitorShowMyfavoriteBtn;

    @SerializedName("exhibitor_show_share_contact")
    @Expose
    private String exhibitorShowShareContact;

    @SerializedName("is_qa_moderator_user")
    @Expose
    private String isQaModeratorUser;

    @SerializedName("is_only_attendee_user")
    @Expose
    private String isOnlyAttendeeUser;

    @SerializedName("speaker_show_request_meeting")
    @Expose
    private String speakerShowRequestMeeting;

    @SerializedName("is_my_meetings_moderator_user")
    @Expose
    private String isMyMeetingsModeratorUser;

    @SerializedName("is_organizer_user")
    @Expose
    private String isOrganizerUser;

    @SerializedName("show_check_in_portal_menu")
    @Expose
    private String showCheckInPortalMenu;

    @SerializedName("show_beacon_menu")
    @Expose
    private String showBeaconMenu;

    @SerializedName("show_my_meetings_menu")
    @Expose
    private String showMyMeetingsMenu;

    @SerializedName("is_only_exhibitor_user")
    @Expose
    private String isOnlyExhibitorUser;

    public String getIsOnlyExhibitorUser() {
        return isOnlyExhibitorUser;
    }

    public void setIsOnlyExhibitorUser(String isOnlyExhibitorUser) {
        this.isOnlyExhibitorUser = isOnlyExhibitorUser;
    }

    public String getAttendeeShowRequestMeeting() {
        return attendeeShowRequestMeeting;
    }

    public void setAttendeShowRequestMeeting(String attendeShowRequestMeeting) {
        this.attendeeShowRequestMeeting = attendeShowRequestMeeting;
    }

    public String getAttendeeShareContact() {
        return attendeeShareContact;
    }

    public void setAttendeeShareContact(String attendeeShareContact) {
        this.attendeeShareContact = attendeeShareContact;
    }

    public String getAttendeeMessagePermission() {
        return attendeeMessagePermission;
    }

    public void setAttendeeMessagePermission(String attendeeMessagePermission) {
        this.attendeeMessagePermission = attendeeMessagePermission;
    }

    public String getAttendeeShowSendMessage() {
        return attendeeShowSendMessage;
    }

    public void setAttendeeShowSendMessage(String attendeeShowSendMessage) {
        this.attendeeShowSendMessage = attendeeShowSendMessage;
    }

    public String getAttendeeShowSendRequest() {
        return attendeeShowSendRequest;
    }

    public void setAttendeeShowSendRequest(String attendeeShowSendRequest) {
        this.attendeeShowSendRequest = attendeeShowSendRequest;
    }

    public String getExhibitorShowRequestMeeting() {
        return exhibitorShowRequestMeeting;
    }

    public void setExhibitorShowRequestMeeting(String exhibitorShowRequestMeeting) {
        this.exhibitorShowRequestMeeting = exhibitorShowRequestMeeting;
    }

    public String getExhibitorShowMyfavoriteBtn() {
        return exhibitorShowMyfavoriteBtn;
    }

    public void setExhibitorShowMyfavoriteBtn(String exhibitorShowMyfavoriteBtn) {
        this.exhibitorShowMyfavoriteBtn = exhibitorShowMyfavoriteBtn;
    }

    public String getExhibitorShowShareContact() {
        return exhibitorShowShareContact;
    }

    public void setExhibitorShowShareContact(String exhibitorShowShareContact) {
        this.exhibitorShowShareContact = exhibitorShowShareContact;
    }

    public String getIsQaModeratorUser() {
        return isQaModeratorUser;
    }

    public void setIsQaModeratorUser(String isQaModeratorUser) {
        this.isQaModeratorUser = isQaModeratorUser;
    }

    public String getIsOnlyAttendeeUser() {
        return isOnlyAttendeeUser;
    }

    public void setIsOnlyAttendeeUser(String isOnlyAttendeeUser) {
        this.isOnlyAttendeeUser = isOnlyAttendeeUser;
    }

    public String getSpeakerShowRequestMeeting() {
        return speakerShowRequestMeeting;
    }

    public void setSpeakerShowRequestMeeting(String speakerShowRequestMeeting) {
        this.speakerShowRequestMeeting = speakerShowRequestMeeting;
    }

    public String getIsMyMeetingsModeratorUser() {
        return isMyMeetingsModeratorUser;
    }

    public void setIsMyMeetingsModeratorUser(String isMyMeetingsModeratorUser) {
        this.isMyMeetingsModeratorUser = isMyMeetingsModeratorUser;
    }

    public String getIsOrganizerUser() {
        return isOrganizerUser;
    }

    public void setIsOrganizerUser(String isOrganizerUser) {
        this.isOrganizerUser = isOrganizerUser;
    }

    public String getShowCheckInPortalMenu() {
        return showCheckInPortalMenu;
    }

    public void setShowCheckInPortalMenu(String showCheckInPortalMenu) {
        this.showCheckInPortalMenu = showCheckInPortalMenu;
    }

    public String getShowBeaconMenu() {
        return showBeaconMenu;
    }

    public void setShowBeaconMenu(String showBeaconMenu) {
        this.showBeaconMenu = showBeaconMenu;
    }

    public String getShowMyMeetingsMenu() {
        return showMyMeetingsMenu;
    }

    public void setShowMyMeetingsMenu(String showMyMeetingsMenu) {
        this.showMyMeetingsMenu = showMyMeetingsMenu;
    }
}
