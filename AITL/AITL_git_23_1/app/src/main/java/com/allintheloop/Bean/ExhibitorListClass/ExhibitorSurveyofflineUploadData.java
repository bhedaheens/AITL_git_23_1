package com.allintheloop.Bean.ExhibitorListClass;

/**
 * Created by nteam on 15/9/17.
 */

public class ExhibitorSurveyofflineUploadData {
    String event_id, user_id, scan_id, scan_data, upload_lead, survey_data, ExiLead_TimeData;

    public ExhibitorSurveyofflineUploadData(String event_id, String user_id, String scan_id, String scan_data, String upload_lead, String survey_data, String ExiLead_TimeData) {
        this.event_id = event_id;
        this.user_id = user_id;
        this.scan_id = scan_id;
        this.scan_data = scan_data;
        this.upload_lead = upload_lead;
        this.survey_data = survey_data;
        this.ExiLead_TimeData = ExiLead_TimeData;
    }

    public String getExiLead_TimeData() {
        return ExiLead_TimeData;
    }

    public void setExiLead_TimeData(String exiLead_TimeData) {
        ExiLead_TimeData = exiLead_TimeData;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getScan_id() {
        return scan_id;
    }

    public void setScan_id(String scan_id) {
        this.scan_id = scan_id;
    }

    public String getScan_data() {
        return scan_data;
    }

    public void setScan_data(String scan_data) {
        this.scan_data = scan_data;
    }

    public String getUpload_lead() {
        return upload_lead;
    }

    public void setUpload_lead(String upload_lead) {
        this.upload_lead = upload_lead;
    }

    public String getSurvey_data() {
        return survey_data;
    }

    public void setSurvey_data(String survey_data) {
        this.survey_data = survey_data;
    }
}
