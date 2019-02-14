package com.allintheloop.Bean.AgendaData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nteam on 4/12/17.
 */

public class Agenda implements Serializable {


    @SerializedName("agenda_id")
    @Expose
    private String agendaId;
    @SerializedName("Start_date")
    @Expose
    private String startDate;
    @SerializedName("Start_time")
    @Expose
    private String startTime;
    @SerializedName("End_date")
    @Expose
    private String endDate;
    @SerializedName("End_time")
    @Expose
    private String endTime;
    @SerializedName("checking_datetime")
    @Expose
    private String checkingDatetime;
    @SerializedName("Maximum_People")
    @Expose
    private String maximumPeople;
    @SerializedName("Heading")
    @Expose
    private String heading;
    @SerializedName("Types")
    @Expose
    private String types;
    @SerializedName("Agenda_status")
    @Expose
    private String agendaStatus;
    @SerializedName("Speaker_id")
    @Expose
    private String speakerId;
    @SerializedName("document_id")
    @Expose
    private String documentId;
    @SerializedName("presentation_id")
    @Expose
    private String presentationId;
    @SerializedName("Address_map")
    @Expose
    private String addressMap;
    @SerializedName("other_types")
    @Expose
    private String otherTypes;
    @SerializedName("short_desc")
    @Expose
    private String shortDesc;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("Event_show_time_zone")
    @Expose
    private String eventShowTimeZone;
    @SerializedName("time_zone")
    @Expose
    private String timeZone;
    @SerializedName("Documents")
    @Expose
    private String documents;
    @SerializedName("session_image")
    @Expose
    private String sessionImage;
    @SerializedName("show_checking_in")
    @Expose
    private String showCheckingIn;
    @SerializedName("speaker")
    @Expose
    private String speaker;
    @SerializedName("survey_id")
    @Expose
    private String surveyId;
    @SerializedName("Address_map_id")
    @Expose
    private String addressMapId;
    @SerializedName("Map_title")
    @Expose
    private String mapTitle;
    @SerializedName("tname")
    @Expose
    private String tname;
    @SerializedName("type_id")
    @Expose
    private String typeId;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("sort_order")
    @Expose
    private String sort_order;

    @SerializedName("session_tracks")
    @Expose
    private String session_tracks;


    public String getSession_tracks() {
        return session_tracks;
    }

    public void setSession_tracks(String session_tracks) {
        this.session_tracks = session_tracks;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(String agendaId) {
        this.agendaId = agendaId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCheckingDatetime() {
        return checkingDatetime;
    }

    public void setCheckingDatetime(String checkingDatetime) {
        this.checkingDatetime = checkingDatetime;
    }

    public String getMaximumPeople() {
        return maximumPeople;
    }

    public void setMaximumPeople(String maximumPeople) {
        this.maximumPeople = maximumPeople;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getAgendaStatus() {
        return agendaStatus;
    }

    public void setAgendaStatus(String agendaStatus) {
        this.agendaStatus = agendaStatus;
    }

    public String getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(String speakerId) {
        this.speakerId = speakerId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getPresentationId() {
        return presentationId;
    }

    public void setPresentationId(String presentationId) {
        this.presentationId = presentationId;
    }

    public String getAddressMap() {
        return addressMap;
    }

    public void setAddressMap(String addressMap) {
        this.addressMap = addressMap;
    }

    public String getOtherTypes() {
        return otherTypes;
    }

    public void setOtherTypes(String otherTypes) {
        this.otherTypes = otherTypes;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventShowTimeZone() {
        return eventShowTimeZone;
    }

    public void setEventShowTimeZone(String eventShowTimeZone) {
        this.eventShowTimeZone = eventShowTimeZone;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public String getSessionImage() {
        return sessionImage;
    }

    public void setSessionImage(String sessionImage) {
        this.sessionImage = sessionImage;
    }

    public String getShowCheckingIn() {
        return showCheckingIn;
    }

    public void setShowCheckingIn(String showCheckingIn) {
        this.showCheckingIn = showCheckingIn;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getAddressMapId() {
        return addressMapId;
    }

    public void setAddressMapId(String addressMapId) {
        this.addressMapId = addressMapId;
    }

    public String getMapTitle() {
        return mapTitle;
    }

    public void setMapTitle(String mapTitle) {
        this.mapTitle = mapTitle;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

