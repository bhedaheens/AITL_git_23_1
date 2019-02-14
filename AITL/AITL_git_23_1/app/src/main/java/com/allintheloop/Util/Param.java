package com.allintheloop.Util;

import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Param {

    public static final String SUCCESS_CODE = "true";

    /**
     * ------------Common key------------------
     */
    public static final String KEY_MESSAGE = "message";

    /**
     * --------------------------------General --------------------------------------
     */


    public static Map<String, String> getAdditionalData(String token, String event_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("user_id", user_id);
        Log.i("AITL", "Additional Questions" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> updateProfileAttendee(String first_name, String last_name, String event_id, String user_id, String token, String cmpy_name, String title, String salutation, String password, String extra, String user_event_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("first_name", first_name);
        mParam.put("last_name", last_name);
        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("user_id", user_id);
        mParam.put("salutation", salutation);
        mParam.put("title", title);
        mParam.put("cmpy_name", cmpy_name);
        mParam.put("password", password);
        mParam.put("extra", extra);
        mParam.put("user_event_id", user_event_id);

        Log.i("kk", "UpdateProfileAttendee: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> socialUpdateProfile(String user_id, String event_id, String token, String salutation, String first_name, String last_name, String cmpy_name, String title, String logo, String password, String extra, String user_event_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("first_name", first_name);
        mParam.put("last_name", last_name);
        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("user_id", user_id);
        mParam.put("salutation", salutation);
        mParam.put("title", title);
        mParam.put("cmpy_name", cmpy_name);
        mParam.put("logo", logo);
        mParam.put("password", password);
        mParam.put("extra", extra);
        mParam.put("user_event_id", user_event_id);

        Log.i("AITL", "Update Social: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> authorizedLogin(String Email_id, String Event_id, String version_code_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("email", Email_id);
        mParam.put("event_id", Event_id);
        mParam.put("version_code_id", version_code_id);
        Log.i("AITL", "Login: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> Login(String Email_id, String Pass, String Event_id, String version_code_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("email", Email_id);
        mParam.put("password", Pass);
        mParam.put("event_id", Event_id);
        mParam.put("version_code_id", version_code_id);
        Log.i("AITL", "Login: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> FacebookLogin(String FbEmailId, String FbId, String Fb_name, String Eventid, String Fb_img, String device_name, String version_code_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("email", FbEmailId);
        mParam.put("facebook_id", FbId);
        mParam.put("first_name", Fb_name);
        mParam.put("event_id", Eventid);
        mParam.put("img", Fb_img);
        mParam.put("device_name", device_name);
        mParam.put("version_code_id", version_code_id);
        Log.i("AITL", "FBLogin: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getDefultEvent() {

        Map<String, String> mParam = new HashMap<String, String>();
        return mParam;
    }


    public static Map<String, String> routeFinding(String startpoint, String endpoint) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("startpoint", startpoint);
        mParam.put("endpoint", endpoint);
        Log.i("AITL", "routeFinding " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> updateVerioncode(String name) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("device", name);

        Log.i("AITL", "Update Photo: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> presantationSendMessage() {

        Map<String, String> mParam = new HashMap<String, String>();
        Log.i("AITL", "Presantation Send Message " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getExhibitorLeadDataOffline(String event_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        Log.i("Bhavdip", "getExhibitorLeadData " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> getUpdatedDateByModule(String event_id, String moduleId) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("module", moduleId);
        Log.i("Bhavdip", "getExhibitorLeadData " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> AddNotes(String Menu_id, String Module_id, String user_id, String heading, String description, String event_id, String token, String is_cms) {

        Map<String, String> mParam = new HashMap<>();

        mParam.put("Menu_id", Menu_id);
        mParam.put("Module_id", Module_id);
        mParam.put("user_id", user_id);
        mParam.put("heading", heading);
        mParam.put("description", description);
        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("is_cms", is_cms);

        Log.i("AITL", "Add Notes: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> addonRemoveFav(String event_id, String user_id, String module_id, String module_type) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("module_id", module_id);
        mParam.put("module_type", module_type);
        Log.i("AITL", "AddorRemoveFaviourate" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> addonRemoveAttendeeFav(String event_id, String user_id, String attendeeId) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("attendee_id", attendeeId);
        Log.i("AITL", "AddorRemoveAttendeeFaviourate" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> forgotPassword(String eventId, String emailId) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("email", emailId);
        mParam.put("event_id", eventId);
        Log.i("AITL", "ForgotPassword: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> EventList(String deviceId) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("gcm_id", deviceId);
        Log.i("AITL", "EventList: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> HomeFragment(String Token, String EventId, String event_type, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("_token", Token);
        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        mParam.put("user_id", user_id);
        Log.i("AITL", "HomeFragment: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> get_CmsPage(String event_id, String event_type, String cms_id, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", token);
        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("cms_id", cms_id);
        Log.i("AITL", "CMS Page: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> EventDialog(String Token, String EventId, String event_type) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("_token", Token);
        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        Log.i("AITL", "EventDialog: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> FindSecyrEvent(String Key) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("secure_key", Key);

        Log.i("AITL", "SecureKey: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> DeleteNote(String eventId, String token, String user_id, String note_id) {

        Map<String, String> mParam = new HashMap<>();

        mParam.put("event_id", eventId);
        mParam.put("token", token);
        mParam.put("user_id", user_id);
        mParam.put("note_id", note_id);
        Log.i("AITL", "Delete Note" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> saveOrrejectModeratorRequestMetting(String eventId, String user_id, String requestId, String receiverId, String senderId, String status) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("request_id", requestId);
        mParam.put("receiver_id", receiverId);
        mParam.put("sender_id", senderId);
        mParam.put("user_id", user_id);
        mParam.put("status", status);
        mParam.put("event_id", eventId);
        Log.i("AITL", "SaveOrRejectMettingModerator: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> moderatorSuggestedTime(String event_id, String receiver_id, String request_id, String sender_id, String date, String time, String comment) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("date", date);
        mParam.put("time", time);
        mParam.put("request_id", request_id);
        mParam.put("receiver_id", receiver_id);
        mParam.put("sender_id", sender_id);
        mParam.put("event_id", event_id);
        mParam.put("comment", comment);
        Log.i("AITL", "SuggestedTime" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> moderatorSaveComment(String event_id, String receiver_id, String request_id, String sender_id, String comment) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("request_id", request_id);
        mParam.put("reciver_id", receiver_id);
        mParam.put("sender_id", sender_id);
        mParam.put("event_id", event_id);
        mParam.put("comment", comment);
        Log.i("AITL", "SuggestedTime" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> searchEvent(String eventName) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_name", eventName);

        Log.i("AITL", "event_name: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> checkUpdate(String eventId) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", eventId);

        Log.i("AITL", "event_id: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getDefaultLangParam(String event_id, String lang_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("lang_id", lang_id);
        Log.i("AITL", "getDefaultLangParam: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getDrawerList(String Token, String EventId, String Subdomain, String event_type, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("_token", Token);
        mParam.put("event_id", EventId);
        mParam.put("subdomain", Subdomain);
        mParam.put("event_type", event_type);
        mParam.put("user_id", user_id);
        Log.i("AITL", "getDrawerList: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> Logout(String Device_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("device_id", Device_id);

        Log.i("AITL", "LogOut: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getRequestMettingDateTime(String eventId) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", eventId);

        Log.i("AITL", "getRequestDate&Time: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getRequestMettingDateTimeNew(String eventId, String attendeeId, String loggedin_user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", eventId);
        mParam.put("user_id", attendeeId);
        mParam.put("loggedin_user_id", loggedin_user_id);

        Log.i("AITL", "getRequestDate&Time: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getTiemFromDate(String eventId, String userId, String date, String is_exhibitor) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", eventId);
        mParam.put("user_id", userId);
        mParam.put("date", date);
        mParam.put("is_exhibitor", is_exhibitor);

        Log.i("Bhavdip", "Request Metting: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getLocationFromDateTime(String eventId, String userId, String date, String time) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", eventId);
        mParam.put("user_id", userId);
        mParam.put("date", date);
        mParam.put("time", time);

        Log.i("Bhavdip", "Request Metting: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> requestMetting(String eventId, String exhibitor_id, String user_id, String date, String time) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", eventId);
        mParam.put("exhibitor_id", exhibitor_id);
        mParam.put("user_id", user_id);
        mParam.put("date", date);
        mParam.put("time", time);

        Log.i("AITL", "Request Metting: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> attendeerequestMetting(String eventId, String attendee_id, String user_id, String date, String time, String location, String role_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", eventId);
        mParam.put("attendee_id", attendee_id);
        mParam.put("user_id", user_id);
        mParam.put("date", date);
        mParam.put("time", time);
        mParam.put("location", location);
        mParam.put("role_id", role_id);

        Log.i("AITL", "Request Metting: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> attendeerequestMettingUid(String eventId, String attendee_id, String user_id, String date, String time, String location) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", eventId);
        mParam.put("attendee_id", attendee_id);
        mParam.put("user_id", user_id);
        mParam.put("date", date);
        mParam.put("time", time);
        mParam.put("location", location);

        Log.i("AITL", "Request Metting: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getAllRequestMettingList(String eventId, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", eventId);
        mParam.put("user_id", user_id);
        Log.i("AITL", "Request Metting: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> submitFormData(String form_data, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("form_data", form_data);
        mParam.put("user_id", user_id);
        Log.i("AITL", "SUBMIT FORMDATA: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> saveOrrejectRequestMetting(String eventId, String user_id, String requestId, String exhibitorId, String status) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", eventId);
        mParam.put("user_id", user_id);
        mParam.put("request_id", requestId);
        mParam.put("exhibitor_id", exhibitorId);
        mParam.put("status", status);
        Log.i("AITL", "SaveOrRejectMetting: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> saveOrrejectAttendeeRequestMetting(String eventId, String user_id, String requestId, String receiver_id, String status, String rejection_msg) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", eventId);
        mParam.put("user_id", user_id);
        mParam.put("request_id", requestId);
        mParam.put("receiver_id", receiver_id);
        mParam.put("status", status);
        mParam.put("rejection_msg", rejection_msg);
        Log.i("AITL", "AttendeeSaveOrRejectMetting: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getVirtualMarketList(String event_id, String keyword, int page_no) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("keyword", keyword);
        mParam.put("page_no", String.valueOf(page_no));

        Log.i("AITL", "getExhibitorKeyword" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getVirtualMarketDetail(String event_id, String email) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("email", email);

        Log.i("AITL", "getExhibitorKeywordDETAIL" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> GetFormData(String Event_Id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", Event_Id);

        Log.i("AITL", "GetFormData: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getAgendaByTimeType(String user_id, String Token_Id, String Event_Id, String event_type, String category_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("_token", Token_Id);
        mParam.put("user_id", user_id);
        mParam.put("event_id", Event_Id);
        mParam.put("event_type", event_type);
        mParam.put("category_id", category_id);
        Log.i("AITL", "getAgendaByTimeType: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getPresanByTimeType(String Token_Id, String Event_Id, String event_type) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token_Id);
        mParam.put("event_id", Event_Id);
        mParam.put("event_type", event_type);
        Log.i("AITL", "getPresantionByTimeType: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> get_UserWise_Agenda_ByTimeType(String Token_id, String Event_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token_id);
        mParam.put("event_id", Event_id);
        mParam.put("user_id", user_id);
        Log.i("AITL", "getUserWiseAgenda: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> get_UserWise_Agenda_ByTimeType1(String Token_id, String Event_id, String user_id, String catgory_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token_id);
        mParam.put("event_id", Event_id);
        mParam.put("user_id", user_id);
        mParam.put("category_id", catgory_id);
        Log.i("AITL", "getUserWiseAgenda: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> savePendingAgenda(String Token_id, String Event_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token_id);
        mParam.put("event_id", Event_id);
        mParam.put("user_id", user_id);
        Log.i("AITL", "SavePendingAgenda: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getAgenda_byId(String event_id, String token, String event_type, String agenda_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("event_type", event_type);
        mParam.put("agenda_id", agenda_id);
        mParam.put("user_id", user_id);
        Log.i("AITL", "getAgendaById: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> Update_Agenda(String event_id, String token, String agenda_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("agenda_id", agenda_id);
        mParam.put("user_id", user_id);
        Log.i("AITL", "getAgendaById: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> Update_AgendaWithCalshes(String event_id, String token, String agenda_id, String user_id, String confirm_clash) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("agenda_id", agenda_id);
        mParam.put("user_id", user_id);
        mParam.put("confirm_clash", confirm_clash);
        Log.i("Bhavdip", "getAgendaById: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> saveSurveyOfflineData(String event_id, String user_id, String scan_id, String scan_data, String upload_lead, String survey_data, String created_date) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("scan_id", scan_id);
        mParam.put("scan_data", scan_data);
        mParam.put("upload_lead", upload_lead);
        mParam.put("survey_data", survey_data);
        mParam.put("created_date", created_date);
        Log.i("AITL", "LeadRetrival: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> Agenda_comments(String event_id, String agenda_id, String comments, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("comments", comments);
        mParam.put("agenda_id", agenda_id);
        mParam.put("user_id", user_id);
        Log.i("AITL", "SAVE AGENDA COMMENT: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getFavoriteList(String event_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        Log.i("AITL", "GetFavoriteList" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> SaveRating_agenda(String event_id, String token, String agenda_id, String user_id, String rating) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("agenda_id", agenda_id);
        mParam.put("user_id", user_id);
        mParam.put("rating", rating);
        Log.i("AITL", "getAgendaById: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> GetCountryList() {

        Map<String, String> mParam = new HashMap<String, String>();


        Log.i("AITL", "GetCountryList: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> GetStateList(String event_id, String country_id, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("country_id", country_id);
        mParam.put("token", token);

        Log.i("AITL", "GetStateList: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> RegisterForm(String email, String fname, String lname, String pass, String eventId, String country, String title, String cname, String form_data, String status, String device_name) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("email", email);
        mParam.put("first_name", fname);
        mParam.put("last_name", lname);
        mParam.put("password", pass);
        mParam.put("event_id", eventId);
        mParam.put("country", country);
        mParam.put("title", title);
        mParam.put("cmpy_name", cname);
        mParam.put("form_data", form_data);
        mParam.put("formbiluder_status", status);
        mParam.put("device_name", device_name);
        Log.i("AITL", "Registration: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getAttendanceList(String Token, String EventId, String event_type, String user_id, String keyword, int page_no, String filter_keywords) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token);
        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        mParam.put("user_id", user_id);
        mParam.put("keyword", keyword);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("filter_keywords", filter_keywords);
        Log.i("AITL", "AttendanceList: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getAttendeeListNew(String Token, String EventId, String event_type, String user_id, String keyword, int page_no, String filter_keywords, String categoryId, String adavancedFilter) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token);
        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        mParam.put("user_id", user_id);
        mParam.put("keyword", keyword);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("filter_keywords", filter_keywords);
        mParam.put("category_id", categoryId);
        mParam.put("advance_filter", adavancedFilter);
        Log.i("AITL", "NewAttendeeList: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getAttendance_Detail(String Token, String EventId, String event_type, String attendee_id, String used_id, int page_no) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token);
        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        mParam.put("attendee_id", attendee_id);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("user_id", used_id);
        Log.i("AITL", "Detail Attendace: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> attendance_message(String EventId, String message, String user_id, String message_type, String attendee_id, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("message", message);
        mParam.put("user_id", user_id);
        mParam.put("message_type", message_type);
        mParam.put("attendee_id", attendee_id);
        mParam.put("token", token);

        Log.i("AITL", "Message Attendace: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getAllpublic_message(String EventId, String token, String event_type, int page_no) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("token", token);

        Log.i("AITL", "Retrive Public Message: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> public_message(String EventId, String token, String user_id, String message) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("message", message);
        mParam.put("user_id", user_id);
        mParam.put("token", token);

        Log.i("AITL", "Message Public Image: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> public_image_request(String EventId, String token, String user_id, String message_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("user_id", user_id);
        mParam.put("message_id", message_id);
        mParam.put("token", token);

        Log.i("AITL", "Message Public Image: " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> public_CommentMessage(String EventId, String token, String used_id, String res_MessageId, String comment) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("token", token);
        mParam.put("user_id", used_id);
        mParam.put("comment", comment);
        mParam.put("message_id", res_MessageId);

        Log.i("AITL", "CommentMessage Public_Message: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getAllprivateMessage(String event_id, String user_id, int page_no, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("token", token);

        Log.i("AITL", "Message Attendace: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getAllprivateMessageSenderWise(String event_id, String user_id, int page_no, String token, String sender_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("token", token);
        mParam.put("sender_id", sender_id);

        Log.i("AITL", "GetPrivate Message SenderWise: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> deletePrivateMessageDetail(String event_id, String user_id, String token, String receiver_id, String message_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("token", token);
        mParam.put("receiver_id", receiver_id);
        mParam.put("message_id", message_id);

        Log.i("AITL", "GetPrivate Message SenderWise: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getAllprivateSpeaker(String event_id, String token, String event_type) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("token", token);

        Log.i("AITL", "Message Private Speaker: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> private_messageSend(String event_id, String user_id, String token, String message, String receiverId) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("token", token);
        mParam.put("message", message);
        mParam.put("receiver_id", receiverId);

        Log.i("AITL", "Message Private: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> private_image_request(String EventId, String user_id, String token, String receiver_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("user_id", user_id);
        mParam.put("token", token);
        mParam.put("receiver_id", receiver_id);
        Log.i("AITL", "Message Public Image: " + mParam.toString());
        return mParam;
    }

    public static Map<String, File> message_img(File img) {

        Map<String, File> mParam = new HashMap<String, File>();

        mParam.put("image", img);
        Log.i("AITL", "Update Photo: " + mParam.toString());

        return mParam;
    }


    public static Map<String, List<File>> photoFilterinternalPost(List<File> img) {

        Map<String, List<File>> mParam = new HashMap<String, List<File>>();

        mParam.put("image", img);
        Log.i("AITL", "Update Photo: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> savePhotoFilterImage(String event_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);

        Log.i("AITL", "Message Attendace: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> attendance_ImgMessage(String EventId, String message_id, String user_id, String message_type, String attendee_id, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("message_id", message_id);
        mParam.put("user_id", user_id);
        mParam.put("message_type", message_type);
        mParam.put("attendee_id", attendee_id);
        mParam.put("token", token);

        Log.i("AITL", "Message Attendace: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> CommentMessage(String EventId, String token, String used_id, String res_MessageId, String comment) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("token", token);
        mParam.put("user_id", used_id);
        mParam.put("comment", comment);
        mParam.put("message_id", res_MessageId);

        Log.i("AITL", "CommentMessage Attendace: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> attendance_delete_message(String token, String user_id, String message_id, String event_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("message_id", message_id);
        mParam.put("user_id", user_id);
        mParam.put("token", token);

        Log.i("AITL", "Message Attendace: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> attendance_delete_comment(String token, String comment_id, String event_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("comment_id", comment_id);
        mParam.put("token", token);

        Log.i("AITL", "Delete  Comment: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> speaker_ImgMessage(String EventId, String message_id, String user_id, String message_type, String speaker_id, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("message_id", message_id);
        mParam.put("user_id", user_id);
        mParam.put("message_type", message_type);
        mParam.put("speaker_id", speaker_id);
        mParam.put("token", token);

        Log.i("AITL", "Message Attendace: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> speaker_message(String EventId, String message, String user_id, String message_type, String attendee_id, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("message", message);
        mParam.put("user_id", user_id);
        mParam.put("message_type", message_type);
        mParam.put("speaker_id", attendee_id);
        mParam.put("token", token);

        Log.i("AITL", "Message Attendace: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getSpeaker_Detail(String Token, String EventId, String event_type, String speaker_id, String user_id, int page_no, String add_fav) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token);
        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        mParam.put("speaker_id", speaker_id);
        mParam.put("user_id", user_id);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("add_fav", add_fav);
        Log.i("AITL", "Detail Speaker: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getSpeakerList(String Token, String EventId, String event_type) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token);
        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        Log.i("AITL", "SpeakerList: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getExhbitor_Detail(String event_id, String event_type, String exhibitor_id, String ex_pageId, int page_no, String user_id, String token) {

        Map<String, String> mParam = new HashMap<String, String>();


        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("exhibitor_id", exhibitor_id);
        mParam.put("exhibitor_page_id", ex_pageId);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("user_id", user_id);
        mParam.put("token", token);

        Log.i("AITL", "Detail Exhibitor: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> markasVisited(String event_id, String visited_id, String user_id, String menu_id, String exhibitor_page_id, String event_type) {

        Map<String, String> mParam = new HashMap<String, String>();


        mParam.put("event_id", event_id);
        mParam.put("visited_id", visited_id);
        mParam.put("user_id", user_id);
        mParam.put("menu_id", menu_id);
        mParam.put("exhibitor_page_id", exhibitor_page_id);
        mParam.put("event_type", event_type);

        Log.i("AITL", "Detail Exhibitor: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> notificationUpdateLog(String user_id, String event_id, String notification_id, String flag) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("notification_id", notification_id);
        mParam.put("flag", flag);

        Log.i("AITL", "NotificationUpdate Log: " + mParam.toString());

        return mParam;
    }

    public static Map getExhibitorList(String Token, String EventId, String event_type, String keyword, int page_no, String category_id, String parent_c_id, String last_type) {
        Map mParam = new HashMap();
        mParam.put("_token", Token);
        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        mParam.put("keyword", keyword);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("category_id", category_id);
        mParam.put("last_type", last_type);
        Log.i("AITL", "AttendanceList: " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> getGroupingData(String EventId) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);

        Log.i("AITL", "getGroupData: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> geMatchMakingModuleName(String EventId) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);

        Log.i("AITL", "getGroupData: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> DatenidoParam(String token, String user_id, String lang) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", token);
        mParam.put("user_id", user_id);
        mParam.put("lang", lang);
        Log.i("AITL", "DatenidoURl: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> exhibitor_message(String event_id, String token, String user_id, String exhibitor_id, String message) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("user_id", user_id);
        mParam.put("exhibitor_id", exhibitor_id);
        mParam.put("message", message);
        Log.i("AITL", "Message Send : " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> exhibitorShareContact(String event_id, String user_id, String to_user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("to_user_id", to_user_id);
        Log.i("AITL", "ShareContactDetail: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> exhibitor_DeleteComment(String event_id, String token, String comment_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("comment_id", comment_id);
        Log.i("AITL", "Message Send : " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> exhibitor_makeComment(String event_id, String token, String user_id, String message_id, String comment) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("user_id", user_id);
        mParam.put("message_id", message_id);
        mParam.put("comment", comment);
        Log.i("AITL", "Message Send : " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getMapList(String Token, String EventId, String event_type) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token);
        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        Log.i("AITL", "MapList: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getSponsorOfflineList(String EventId) {

        Map<String, String> mParam = new HashMap<String, String>();


        mParam.put("event_id", EventId);
        Log.i("AITL", "MapList: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getMapDetail(String Token, String EventId, String event_type, String Map_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token);
        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        mParam.put("map_id", Map_id);
        Log.i("AITL", "MapDetail: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getSurveyListingFragment(String Token, String EventId, String event_type, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token);
        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        mParam.put("user_id", user_id);
        Log.i("AITL", "GetSurveyListingFragment: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getSocialList(String Token, String EventId, String event_type) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token);
        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        Log.i("AITL", "Social: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getCheckinPdf(String eventId, String userId) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", eventId);
        mParam.put("user_id", userId);
        Log.i("AITL", "PDF: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getDocumentList(String Token, String EventId, String event_type, String keyword, String parent) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token);
        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        mParam.put("keyword", keyword);
        mParam.put("parent", parent);
        Log.i("AITL", "Document: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getExhibitorCategoryListing(String EventId, String event_type) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        Log.i("AITL", "ExhibitorCategoryListing: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getExhibitorChildCategoryListing(String EventId, String event_type, String parent_c_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        mParam.put("parent_c_id", parent_c_id);
        Log.i("AITL", "ExhibitorChildCategoryListing: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getGamificationData(String EventId) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        Log.i("AITL", "Gamification: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getNotification(String event_id, String menu_id, String user_id, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("menu_id", menu_id);
        mParam.put("user_id", user_id);
        mParam.put("token", token);
        Log.i("AITL", "Notification: " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> getNotificationListing(String event_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        Log.i("AITL", "Notification: " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> deleteNotificationListing(String user_id, String notification_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("user_id", user_id);
        mParam.put("notification_id", notification_id);
        Log.i("AITL", "Notification: " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> qAdeleteMessage(String event_id, String user_id, String session_id, String message_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("session_id", session_id);
        mParam.put("message_id", message_id);

        Log.i("AITL", "Notification: " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> qAHideMessage(String event_id, String user_id, String session_id, String message_id, String status) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("session_id", session_id);
        mParam.put("message_id", message_id);
        mParam.put("status", status);

        Log.i("AITL", "HideMessage: " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> saveDeviceId(String event_id, String deviceId) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("gcm_id", deviceId);
        Log.i("AITL", "SaveDeviceId: " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> getAdvertisment(String event_id, String menu_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("menu_id", menu_id);
        Log.i("AITL", "Notification: " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> getFilterListing(String event_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        return mParam;
    }

    public static Map<String, String> getFilterListingUserPhotos(String event_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);


        return mParam;
    }

    public static Map<String, String> uploadActityFeedDataFromPhotoFilter(String event_id, String user_id, String imageData) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("image", imageData);
        return mParam;
    }

    public static Map<String, String> getAdvertismentForCms(String event_id, String menu_id, String is_cms) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("menu_id", menu_id);
        mParam.put("is_cms", is_cms);
        Log.i("AITL", "CMS_Advertiesment: " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> getCMS_superGroupData(String event_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        Log.i("AITL", "getCMS_superGroupData: " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> getAttendeeFilterListData(String event_id, String category_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("category_id", category_id);
        Log.i("AITL", "Filter " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> getSelectedKeywordData(String event_id, String category_id, String userId) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("category_id", category_id);
        mParam.put("user_id", userId);
        Log.i("AITL", "Filter " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> submitAboutYouDat(String event_id, String userId, String filter) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("user_id", userId);
        mParam.put("filter", filter);
        Log.i("AITL", "Filter " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> getCMS_ChildGroupData(String event_id, String parentGroupId) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("super_group_id", parentGroupId);
        Log.i("AITL", "getCMS_superGroupData: " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> getCMSListData(String event_id, String child_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("child_id", child_id);
        Log.i("AITL", "getCMSLISTData: " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> getDataFromScanLead(String event_id, String user_id, String scan_id, String scan_data) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("scan_id", scan_id);
        mParam.put("scan_data", scan_data);
        Log.i("AITL", "ScanLeadFragment: " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> scanBadgeForQRcode(String event_id, String user_id, String code, String role_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("code", code);
        mParam.put("role_id", role_id);
        Log.i("AITL", "ScanLeadFragment: " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> scanBadgeForQRcodeUid(String event_id, String user_id, String code, String atendee_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("code", code);
        mParam.put("is_only_attendee_user", atendee_id);
        Log.i("AITL", "ScanLeadFragment: " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> getInviteMoreData(String event_id, String user_id, String meeting_id, int pageNo, String keyword) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("meeting_id", meeting_id);
        mParam.put("page_no", String.valueOf(pageNo));
        mParam.put("keyword", keyword);
        Log.i("AITL", "ScanLeadFragment: " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> getMatchMakingAllData(String event_id, String user_id, String role_id, int pageNo, String keyword) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("role_id", role_id);
        mParam.put("page_no", String.valueOf(pageNo));
        mParam.put("keyword", keyword);
        Log.i("AITL", "ScanLeadFragment: " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> inviteMoreAttendee(String event_id, String user_id, String meeting_id, String date, String time, String location, String invited_ids) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("meeting_id", meeting_id);
        mParam.put("date", date);
        mParam.put("time", time);
        mParam.put("location", location);
        mParam.put("invited_ids", invited_ids);
        Log.i("AITL", "ScanLeadFragment: " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> getInviteEdData(String event_id, String user_id, String meeting_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("meeting_id", meeting_id);
        Log.i("AITL", "ScanLeadFragment: " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> saveScanLeadData(String event_id, String user_id, String lead_user_id, String firstname, String lastname, String email, String company_name, String title, String custom_column_data, String mobile, String country, String salutation, String badgeNumber, String isFirstTimeScan) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("lead_user_id", lead_user_id);
        mParam.put("firstname", firstname);
        mParam.put("lastname", lastname);
        mParam.put("email", email);
        mParam.put("company_name", company_name);
        mParam.put("title", title);
        mParam.put("custom_column_data", custom_column_data);
        mParam.put("salutation", salutation);
        mParam.put("country", country);
        mParam.put("mobile", mobile);
        mParam.put("badgeNumber", badgeNumber);
        mParam.put("isFirstTimeScan", isFirstTimeScan);
        Log.i("Bhavdip", "ScanSaveLeadData: " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> getDocument_folderView(String Token, String EventId, String event_type, String folder_id, String keyword) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token);
        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        mParam.put("parent", folder_id);
        mParam.put("keyword", keyword);

        Log.i("AITL", "FolderView: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> AddNotes(String Token, String EventId, String user_id, String heading, String desc) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token);
        mParam.put("event_id", EventId);
        mParam.put("user_id", user_id);
        mParam.put("heading", heading);
        mParam.put("description", desc);

        Log.i("AITL", "Add Notes: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> update_note(String Token, String EventId, String user_id, String note_id, String heading, String desc) {

        Map<String, String> mParam = new HashMap<>();

        mParam.put("token", Token);
        mParam.put("event_id", EventId);
        mParam.put("user_id", user_id);
        mParam.put("note_id", note_id);
        mParam.put("heading", heading);
        mParam.put("description", desc);

        Log.i("AITL", "Add Notes: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> View_Note(String Token, String EventId, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token);
        mParam.put("event_id", EventId);
        mParam.put("user_id", user_id);

        Log.i("AITL", "View Notes: " + mParam.toString());

        return mParam;
    }

//    public static Map<String, String> update_profile(String first_name,String last_name,String email,String is_profile,String event_id,String user_id,String token,String cmpy_name,String title,String salutation,String password,String phone_bussines,String mobile_no,String street,String suburb,String zip_code,String country_id,String state_id,String biography)
//    {
//
//        Map<String, String> mParam = new HashMap<String, String>();
//        mParam.put("first_name", first_name);
//        mParam.put("last_name", last_name);
//        mParam.put("email", email);
//        mParam.put("event_id", event_id);
//        mParam.put("user_id", user_id);
//        mParam.put("token", token);
//        mParam.put("cmpy_name", cmpy_name);
//        mParam.put("title",title);
//
//        mParam.put("salutation", salutation);
//        mParam.put("password", password);
//        mParam.put("phone_bussines", phone_bussines);
//        mParam.put("mobile_no", mobile_no);
//        mParam.put("street",street);
//
//        mParam.put("suburb", suburb);
//        mParam.put("zip_code", zip_code);
//        mParam.put("country_id", country_id);
//        mParam.put("state_id", state_id);
//        mParam.put("biography",biography);
//
//
//        Log.i("AITL", "UpdateProfile: " + mParam.toString());
//
//        return mParam;
//    }

    public static Map<String, String> update_Photo(String user_id, String event_id, String token, String salutation, String first_name, String last_name, String cmpy_name, String title) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("user_id", user_id);
        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("salutation", salutation);
        mParam.put("first_name", first_name);
        mParam.put("last_name", last_name);
        mParam.put("cmpy_name", cmpy_name);
        mParam.put("title", title);

        Log.i("AITL", "Update Photo: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> LinkedInupdate_Photo(String user_id, String event_id, String token, String salutation, String first_name, String last_name, String cmpy_name, String title, String logo) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("user_id", user_id);
        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("salutation", salutation);
        mParam.put("first_name", first_name);
        mParam.put("last_name", last_name);
        mParam.put("cmpy_name", cmpy_name);
        mParam.put("title", title);
        mParam.put("logo", logo);
        Log.i("AITL", "Update PhotoLinked: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> updateContactDetail(String user_id, String linkedin_url, String facebook_url, String twitter_url, String mobile_no, String country_id, String password, String user_event_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("user_id", user_id);
        mParam.put("linkedin_url", linkedin_url);
        mParam.put("facebook_url", facebook_url);
        mParam.put("twitter_url", twitter_url);
        mParam.put("mobile_no", mobile_no);
        mParam.put("country_id", country_id);
        mParam.put("password", password);
        mParam.put("user_event_id", user_event_id);
        Log.i("AITL", "UpdateContactDetail " + mParam.toString());
        return mParam;
    }


    public static Map<String, File> update_photo(File logo) {

        Map<String, File> mParam = new HashMap<String, File>();

        mParam.put("logo", logo);

        Log.i("AITL", "Update Photo: " + mParam.toString());

        return mParam;
    }


    // Fundrising Home

    public static Map<String, String> fundrising_home(String Token_Id, String Event_Id, String event_type, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token_Id);
        mParam.put("event_id", Event_Id);
        mParam.put("event_type", event_type);
        mParam.put("user_id", user_id);
        Log.i("AITL", "Fundrising Home: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> fundrising_Product(String Token_Id, String Event_Id, String event_type, int page_no) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("token", Token_Id);
        mParam.put("event_id", Event_Id);
        mParam.put("event_type", event_type);
        mParam.put("page_no", String.valueOf(page_no));
        Log.i("AITL", "Fundrising Product: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getSurvey(String event_id, String event_type, String user_id, String token, String category_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("user_id", user_id);
        mParam.put("token", token);
        mParam.put("category_id", category_id);
        Log.i("AITL", "getSurvey Page: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> saveSurveyLeadData(String event_id, String user_id, String lead_user_id, String survey_data) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("lead_user_id", lead_user_id);
        mParam.put("user_id", user_id);
        mParam.put("survey_data", survey_data);
        Log.i("AITL", "saveSurveyLeadData Page: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> Save_survey(String event_id, String user_id, String token, String surveyData, String category_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("token", token);
        mParam.put("survey_json_data", surveyData);
        mParam.put("category_id", category_id);

        Log.i("AITL", "SaveSurveyPage: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> update_Notification(String event_id, String gcm_id, String user_id, String token, String device) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("gcm_id", gcm_id);
        mParam.put("user_id", user_id);
        mParam.put("token", token);
        mParam.put("device", device);
        Log.i("AITL", "Update GCM: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> get_SponsorList(String event_id, String event_type, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("token", token);
        Log.i("AITL", "getSponsorList: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> get_SponsorDetail(String event_id, String event_type, String sponsor_id, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("sponsor_id", sponsor_id);
        mParam.put("token", token);
        Log.i("AITL", "getSponsorDetail: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> get_SponsorDetail(String user_id, String event_id, String event_type, String sponsor_id, String token, String add_fav) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("user_id", user_id);
        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("sponsor_id", sponsor_id);
        mParam.put("token", token);
        mParam.put("add_fav", add_fav);

        Log.i("AITL", "getSponsorDetail: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> get_Presantation_Detail(String event_id, String user_id, String presantation_id, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("presentation_id", presantation_id);
        mParam.put("token", token);
        Log.i("AITL", "getPresantaion: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> get_Presantation_DetailLockUnlock(String event_id, String image_name, String presantation_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("image_name", image_name);
        mParam.put("presentation_id", presantation_id);
        Log.i("AITL", "getPresantaionLockUnlock: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> get_Presantation_DetailSurvey(String questionId, String user_id, String ans) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("survay_id", questionId);
        mParam.put("user_id", user_id);
        mParam.put("ans", ans);
        Log.i("AITL", "getPresantaionSaveSurvey " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> deleteUser(String user_id, String role_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("user_id", user_id);
        mParam.put("role_id", role_id);
        Log.i("AITL", "deleteIser " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> get_PresantationRefresh_Detail(String event_id, String presantation_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("presentation_id", presantation_id);
        mParam.put("user_id", user_id);
        Log.i("AITL", "getPresantaionRefresh: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getAllphotoFeed(String EventId, String event_type, String token, int page_no, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("event_type", event_type);
        mParam.put("token", token);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("user_id", user_id);


        Log.i("AITL", "Retrive Public Message: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> uploadPhotofeed(String EventId, String token, String feed_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("token", token);
        mParam.put("feed_id", feed_id);
        mParam.put("user_id", user_id);


        Log.i("AITL", "Retrive Public Message: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> photoDeleteFeed(String token, String user_id, String feed_id, String event_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("feed_id", feed_id);
        mParam.put("user_id", user_id);
        mParam.put("token", token);

        Log.i("AITL", "Message Attendace: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> photolikeFeed(String token, String user_id, String feed_id, String event_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("feed_id", feed_id);
        mParam.put("user_id", user_id);
        mParam.put("token", token);

        Log.i("AITL", "Message Attendace: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> QAvoteUp(String message_id, String user_id, String session_id, String event_id, String device_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("message_id", message_id);
        mParam.put("user_id", user_id);
        mParam.put("session_id", session_id);
        mParam.put("event_id", event_id);
        mParam.put("device_id", device_id);


        Log.i("AITL", "VoteUp: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> Commentphoto(String EventId, String token, String used_id, String feed_id, String comment) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("token", token);
        mParam.put("user_id", used_id);
        mParam.put("comment", comment);
        mParam.put("feed_id", feed_id);

        Log.i("AITL", "CommentMessage Attendace: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> photo_DeleteComment(String event_id, String token, String comment_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("comment_id", comment_id);
        Log.i("AITL", "Photo Delete : " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> fundrising_Product(String event_id, String event_type, String token, String user_id, String auction_type, int page_no) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("token", token);
        mParam.put("user_id", user_id);
        mParam.put("auction_type", auction_type);
        mParam.put("page_no", String.valueOf(page_no));
        Log.i("AITL", "SilentAction Product: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> detail_product(String event_id, String event_type, String token, String user_id, String product_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("token", token);
        mParam.put("user_id", user_id);
        mParam.put("product_id", product_id);
        Log.i("AITL", "Detail SilentAction Product: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> detail_productId(String event_id, String event_type, String token, String auction_type) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("token", token);
        mParam.put("auction_type", auction_type);
        Log.i("AITL", "Detail ProductListId: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> cart_detail(String event_id, String event_type, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("user_id", user_id);
        Log.i("AITL", "Detail ProductListId: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> addTocart(String event_id, String event_type, String product_id, String user_id, String quantity) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("product_id", product_id);
        mParam.put("user_id", user_id);
        mParam.put("quantity", quantity);
        Log.i("AITL", "AddTocart: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> pledge_addTocart(String event_id, String event_type, String product_id, String user_id, String Pledge_amount, String Pledge_other_amount, String Comment, String Pledge_visibility) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("product_id", product_id);
        mParam.put("user_id", user_id);
        mParam.put("Pledge_amount", Pledge_amount);
        mParam.put("Pledge_other_amount", Pledge_other_amount);
        mParam.put("Comment", Comment);
        mParam.put("Pledge_visibility", Pledge_visibility);

        Log.i("AITL", "Pledge AddtoCard: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> save_bidDetail(String event_id, String event_type, String product_id, String user_id, String bid_amount, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("product_id", product_id);
        mParam.put("user_id", user_id);
        mParam.put("bid_amt", bid_amount);
        mParam.put("token", token);

        Log.i("AITL", "Save BidDetail " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> checkoutdetail(String event_id, String event_type, String user_id, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("user_id", user_id);
        mParam.put("token", token);

        Log.i("AITL", "ChceckOut Detail " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> deleteCartProduct(String event_id, String event_type, String product_id, String user_id, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("product_id", product_id);
        mParam.put("user_id", user_id);
        mParam.put("token", token);
        Log.i("AITL", "Delete Cart Detail: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> update_cartQty(String event_id, String event_type, String product, String user_id, String token) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("product", product);
        mParam.put("user_id", user_id);
        mParam.put("token", token);
        Log.i("AITL", "Update cart Detail: " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getTwitterFeed(String event_id, String event_type, int page_no, String hashtag) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("hashtag", hashtag);
        Log.i("AITL", "TwitterFeed" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getTwitterHashTagList(String event_id, String event_type) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        Log.i("AITL", "TwitterFeed" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getFacebookFeed(String event_id, int page_no) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("page_no", String.valueOf(page_no));
        Log.i("AITL", "FacebookFeed" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getImageList(String event_id, String event_type, String token, String feed_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
//        mParam.put("token", token);
        mParam.put("feed_id", feed_id);

        Log.i("AITL", "getImageList" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getSpeakerImageList(String event_id, String event_type, String token, String speaker_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
//        mParam.put("token", token);
        mParam.put("speaker_id", speaker_id);


        Log.i("AITL", "getSpeakerImageList" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getExhibitorImageList(String event_id, String event_type, String token, String exhibitor_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
//        mParam.put("token", token);
        mParam.put("exhibitor_id", exhibitor_id);


        Log.i("AITL", "getSpeakerImageList" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getMessageImageList(String event_id, String event_type, String token, String message_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
//        mParam.put("token", token);
        mParam.put("message_id", message_id);


        Log.i("AITL", "getMessageImageList" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> complateOrder(String user_id, String transaction_id, String order_status, String address, String event_id, String is_shipping, String shipping_address, String mode) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("user_id", user_id);
        mParam.put("transaction_id", transaction_id);
        mParam.put("order_status", order_status);
        mParam.put("address", address);
        mParam.put("event_id", event_id);
        mParam.put("is_shipping", is_shipping);
        mParam.put("shipping_address", shipping_address);
        mParam.put("mode", mode);
        Log.i("AITL", "Complete_Order" + mParam.toString());

        return mParam;
    }

//    public static Map<String, String> SaveInstantDonation(String event_id,String email,String name,String address,String city,String postcode,String country,String amount,String comment,)
//    {
//
//        Map<String, String> mParam = new HashMap<String, String>();
//
//        mParam.put("event_id", event_id);
//        mParam.put("transaction_id", transaction_id);
//        mParam.put("order_status", order_status);
//        mParam.put("address", address);
//        mParam.put("event_id", event_id);
//        mParam.put("is_shipping", is_shipping);
//        mParam.put("shipping_address", shipping_address);
//        mParam.put("mode", mode);
//        Log.i("AITL", "Complete_Order" + mParam.toString());
//
//        return mParam;
//    }

    public static Map<String, String> get_fundraising_donation_details(String event_id, String token, String event_type) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("token", token);

        Log.i("AITL", "getFundraingDonation" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> saveFundraisingDonation(String event_id, String email, String name, String address, String city, String postcode, String country, String amount, String comment, String status) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("email", email);
        mParam.put("name", name);
        mParam.put("address", address);
        mParam.put("city", city);
        mParam.put("postcode", postcode);
        mParam.put("country", country);
        mParam.put("amount", amount);
        mParam.put("comment", comment);
        mParam.put("status", status);

        Log.i("AITL", "SaveFundraisingDonation" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getOrder(String event_id, String token, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("token", token);

        Log.i("AITL", "Order" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getItem(String event_id, String token, String event_type, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("event_type", event_type);
        mParam.put("token", token);

        Log.i("AITL", "Item" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> delete_item(String event_id, String token, String event_type, String user_id, String product_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("event_type", event_type);
        mParam.put("token", token);
        mParam.put("product_id", product_id);

        Log.i("AITL", "Delete_Item" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getCategory(String event_id, String token, String event_type) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("token", token);

        Log.i("AITL", "getCategory" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> addItem(String event_id, String token, String event_type, String user_id, String auction_type, String category, String title, String description, String features, String listing_status, String start_price, String reserve_price, String reserve_bid_visible, String auction_start_datetime, String auction_end_datetime, String price, String quantity) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("token", token);

        mParam.put("user_id", user_id);
        mParam.put("auction_type", auction_type);
        mParam.put("category", category);

        mParam.put("title", title);
        mParam.put("description", description);
        mParam.put("features", features);

        mParam.put("listing_status", listing_status);
        mParam.put("start_price", start_price);
        mParam.put("reserve_price", reserve_price);

        mParam.put("reserve_bid_visible", reserve_bid_visible);
        mParam.put("auction_start_datetime", auction_start_datetime);
        mParam.put("auction_end_datetime", auction_end_datetime);

        mParam.put("price", price);
        mParam.put("quantity", quantity);

        Log.i("AITL", "getCategory" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> EditItem(String event_id, String token, String event_type, String user_id, String auction_type, String category, String title, String description, String features, String listing_status, String start_price, String reserve_price, String reserve_bid_visible, String auction_start_datetime, String auction_end_datetime, String price, String quantity, String product_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("event_type", event_type);
        mParam.put("token", token);

        mParam.put("user_id", user_id);
        mParam.put("auction_type", auction_type);
        mParam.put("category", category);

        mParam.put("title", title);
        mParam.put("description", description);
        mParam.put("features", features);

        mParam.put("listing_status", listing_status);
        mParam.put("start_price", start_price);
        mParam.put("reserve_price", reserve_price);

        mParam.put("reserve_bid_visible", reserve_bid_visible);
        mParam.put("auction_start_datetime", auction_start_datetime);
        mParam.put("auction_end_datetime", auction_end_datetime);

        mParam.put("price", price);
        mParam.put("quantity", quantity);
        mParam.put("product_id", product_id);

        Log.i("AITL", "getCategory" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> addItemImageRequest(String user_id, String product_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("product_id", product_id);
        mParam.put("user_id", user_id);

        Log.i("AITL", "AddItem Image: " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> getActivityFeed(String event_id, int page_no, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("user_id", user_id);
        Log.i("AITL", "getActivityFeed " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> getActivityInternalFeed(String event_id, int page_no, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("user_id", user_id);
        Log.i("AITL", "getActivityFeed " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> getExhibitorLeadData(String event_id, String user_id, int page_no, String keyword) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("user_id", user_id);
        mParam.put("keyword", keyword);
        Log.i("AITL", "getExhibitorLeadData " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> addNewAttendeeRepresentatibves(String event_id, String user_id, String rep_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("rep_id", rep_id);
        Log.i("AITL", "addNew " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> deleteExhibitorLeadRep(String event_id, String user_id, String rep_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("rep_id", rep_id);
        Log.i("AITL", "deleteExhibitorLeadRep " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> getLeadDetailFromMyLead(String event_id, String lead_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("lead_id", lead_id);
        mParam.put("user_id", user_id);
        Log.i("AITL", "getLeadDetailFromMyLead " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> resetMyLeadQuestionData(String event_id, String lead_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("lead_user_id", lead_id);
        mParam.put("user_id", user_id);
        Log.i("AITL", "deleteExhibitorLeadRep " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> getExhibitorLeadRepresentativedData(String event_id, String user_id, int page_no) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("user_id", user_id);
        Log.i("AITL", "getExhibitorLeadRepresentativedData " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> exportedLead(String event_id, String user_id, String send_email) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("send_email", send_email);
        Log.i("AITL", "ExportedLead " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> deleteCommentView(String comment_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("comment_id", comment_id);
        Log.i("AITL", "DeleteComment " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> activityLikeFeed(String type, String id, String user_id, String eventId) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("module_type", type);
        mParam.put("id", id);
        mParam.put("user_id", user_id);
        mParam.put("event_id", eventId);
        Log.i("AITL", "ActivityLikeFeed " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> activtyCommentMessage(String type, String id, String user_id, String eventId, String commentMessage) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("module_type", type);
        mParam.put("id", id);
        mParam.put("user_id", user_id);
        mParam.put("event_id", eventId);
        mParam.put("comment", commentMessage);
        Log.i("AITL", "ActivityLikeFeed " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> getActivityAllFeed(String event_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        Log.i("AITL", "getActivityFeed " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> activityDeletePost(String activity_id, String event_id, String user_id, String activity_no) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("activity_id", activity_id);
        mParam.put("activity_no", activity_no);
        Log.i("AITL", "getActivityFeed " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> submitActivitySurvey(String event_id, String user_id, String survey_id, String answer) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("survey_id", survey_id);
        mParam.put("answer", answer);
        Log.i("AITL", "submitActivitySurvey " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> getActivityAllFeedPageWise(String event_id, String user_id, int page_no, String fb_next_url, String tw_next_url, String insta_next_url) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("fb_next_url", fb_next_url);
        mParam.put("tw_next_url", tw_next_url);
        mParam.put("insta_next_url", insta_next_url);
        Log.i("AITL", "getActivityFeed " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> getActivityAllFeedPageWisewithpostcount(String event_id, String user_id, int page_no, String fb_next_url, String tw_next_url, String insta_next_url, int post_count, String survey_id, String advertise_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("page_no", String.valueOf(page_no));
        mParam.put("fb_next_url", fb_next_url);
        mParam.put("tw_next_url", tw_next_url);
        mParam.put("insta_next_url", insta_next_url);
        mParam.put("prev_post_after_ad", String.valueOf(post_count));
        mParam.put("last_adv_id", advertise_id);
        mParam.put("last_survey_id", survey_id);
        Log.i("AITL", "getActivityFeed " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> getActivitySocialFeed(String event_id, String user_id, String fb_next_url, String tw_next_url, String insta_next_url) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("fb_next_url", fb_next_url);
        mParam.put("tw_next_url", tw_next_url);
        mParam.put("insta_next_url", insta_next_url);
        Log.i("AITL", "getActivityFeed " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> getItemDetail(String event_id, String token, String event_type, String user_id, String product_id, String auction_type) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("event_type", event_type);
        mParam.put("user_id", user_id);
        mParam.put("product_id", product_id);
        mParam.put("auction_type", auction_type);

        Log.i("AITL", "EditItem: " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> thumbline_img(String event_id, String token, String event_type, String user_id, String product_id, String img_name) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("event_type", event_type);
        mParam.put("user_id", user_id);
        mParam.put("product_id", product_id);
        mParam.put("image_name", img_name);

        Log.i("AITL", "thumblineImg: " + mParam.toString());
        return mParam;
    }


    public static Map<String, String> saveInstantDonation(String event_id, String email, String name, String address, String city, String postcode, String country, String amount, String comment, String status) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("email", email);
        mParam.put("name", name);
        mParam.put("address", address);
        mParam.put("city", city);
        mParam.put("postcode", postcode);
        mParam.put("country", country);
        mParam.put("amount", amount);
        mParam.put("comment", comment);
        mParam.put("status", status);

        Log.i("AITL", "SaveInstantDonation: " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> addActivityImageRequest(String activity_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("activity_id", activity_id);


        Log.i("AITL", "AddActivityImage: " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> getdetailActivityFeed(String type, String id) {

        Map<String, String> mParam = new HashMap<String, String>();


        mParam.put("type", type);
        mParam.put("id", id);

        Log.i("AITL", "getDetailActivityFeed " + mParam.toString());
        return mParam;
    }

    public static Map<String, String> getActivityImgList(String event_id, String activity_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("activity_id", activity_id);
        Log.i("AITL", "getMessageImageList" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getExhibitorByKeyword(String event_id, String token, String keyword) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("keywords", keyword);
        Log.i("AITL", "getExhibitorKeyword" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> authorizedPayment(String firstName, String lastname, String creditCardNumber, String date, String amount, String organisor_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("firstName", firstName);
        mParam.put("lastname", lastname);
        mParam.put("creditCardNumber", creditCardNumber);
        mParam.put("date", date);
        mParam.put("amount", amount);
        mParam.put("organisor_id", organisor_id);

        Log.i("AITL", "AuthorizedPayment" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> StripePayment(String fund_percantage, String stripe_show, String total, String organisor_id, String stripe_user_id, String stripeToken, String currency, String admin_secret_key) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("fund_percantage", fund_percantage);
        mParam.put("stripe_show", stripe_show);
        mParam.put("total", total);
        mParam.put("organisor_id", organisor_id);
        mParam.put("stripe_user_id", stripe_user_id);
        mParam.put("stripeToken", stripeToken);
        mParam.put("currency", currency);
        mParam.put("admin_secret_key", admin_secret_key);

        Log.i("AITL", "StripedPayment" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getNotificationCount(String event_id, String token, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("token", token);
        mParam.put("user_id", user_id);
        Log.i("AITL", "getCountNotification" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> userLogout(String event_id) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("user_id", event_id);
        Log.i("AITL", "UserLogout" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> NotificationSeen(String user_id, String event_id, String message_type) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("user_id", user_id);
        mParam.put("event_id", event_id);
        mParam.put("message_type", message_type);
        Log.i("AITL", "NotificationSeen" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getInstagramFeed(String event_id, int page_no) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", event_id);
        mParam.put("page_no", String.valueOf(page_no));

        Log.i("AITL", "getInstagramFeed" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> get_Presantation_DetailViewResult(String eventId, String surveyid, String presentation_id, String is_bar_chart) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", eventId);
        mParam.put("survay_id", surveyid);
        mParam.put("presentation_id", presentation_id);
        mParam.put("is_bar_chart", is_bar_chart);
        Log.i("AITL", "getVireResdult and PUSH  RESULT" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getPresantationDetailPushResult(String presentation_id, String poll_slideId, String is_bar_chart) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("presentation_id", presentation_id);
        mParam.put("survay_id", poll_slideId);
        mParam.put("is_bar_chart", is_bar_chart);
        Log.i("AITL", " PUSH  RESULT" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> get_Presantation_DetailRemoveResult(String presentation_id, String is_bar_chart) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("presentation_id", presentation_id);
        mParam.put("is_bar_chart", is_bar_chart);
        Log.i("AITL", "Remove Result" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> presantation_liveMode(String presentation_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("presentation_id", presentation_id);
        Log.i("AITL", "Remove Result" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getPortalListing(String event_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        Log.i("AITL", "getListing" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getLoginLogo(String event_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        Log.i("AITL", "getListing" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getQandAListData(String event_id, String group_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("group_id", group_id);
        mParam.put("user_id", user_id);
        Log.i("AITL", "getQandAListing" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> checkInPortal(String event_id, String attendee_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("attendee_id", attendee_id);
        Log.i("AITL", "CheckInPortal" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getSesionDetail(String event_id, String session_id, String user_id, String device_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("session_id", session_id);
        mParam.put("user_id", user_id);
        mParam.put("device_id", device_id);
        Log.i("AITL", "getSessionDetail" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> qaModeratorUpVote(String event_id, String session_id, String user_id, String messageId, String vote) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("session_id", session_id);
        mParam.put("user_id", user_id);
        mParam.put("message_id", messageId);
        mParam.put("vote", vote);
        Log.i("AITL", "getSessionDetail" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> qASendMessage(String event_id, String session_id, String user_id, String Moderator_speaker_id, String message, String is_closed_qa) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("session_id", session_id);
        mParam.put("user_id", user_id);
        mParam.put("Moderator_speaker_id", Moderator_speaker_id);
        mParam.put("message", message);
        mParam.put("is_closed_qa", is_closed_qa);
        Log.i("AITL", "sendQASessionDetail" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> shareContactInformation(String event_id, String attendee_id, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("attendee_id", attendee_id);
        mParam.put("user_id", user_id);
        Log.i("AITL", "ShareContactInforamation" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> blockAttendee(String event_id, String from_id, String to_id, String status) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("from_id", from_id);
        mParam.put("to_id", to_id);
        mParam.put("status", status);
        Log.i("AITL", "ShareContactInforamation" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getNewSuggestedTime(String event_id, String user_id, String mettingId) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("meeting_id", mettingId);
        Log.i("AITL", "getNewSuggestedTime" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> renameBeacon(String event_id, String id, String beacon_name, String organizer_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("id", id);
        mParam.put("beacon_name", beacon_name);
        mParam.put("organizer_id", organizer_id);
        Log.i("AITL", "REnameBeacon" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> startServer() {

        Map<String, String> mParam = new HashMap<String, String>();
        Log.i("AITL", "REnameBeacon" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> delelteBeacon(String event_id, String id, String organizer_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("id", id);
        mParam.put("organizer_id", organizer_id);
        Log.i("AITL", "DeleteBeacon" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> booksuggestedTime(String event_id, String user_id, String suggetedId, String dateTime) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("suggested_id", suggetedId);
        mParam.put("date_time", dateTime);
        Log.i("AITL", "BookSuggestedTime" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> suggestedTime(String event_id, String user_id, String mettingId, String exhibitor_id, String date, String time) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("request_id", mettingId);
        mParam.put("exhibitor_id", exhibitor_id);
        mParam.put("date", date);
        mParam.put("time", time);
        Log.i("AITL", "SuggestedTime" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> attendeesuggestedTime(String event_id, String user_id, String mettingId, String receiver_id, String date, String time) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("request_id", mettingId);
        mParam.put("receiver_id", receiver_id);
        mParam.put("date", date);
        mParam.put("time", time);
        Log.i("AITL", "AttendeeSuggestedTime" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> saveAttendee(String event_id, String attendee_id, String strPersonal, String strCustome) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("attendee_id", attendee_id);
        mParam.put("personal_info", strPersonal);
        mParam.put("custom_info", strCustome);
        Log.i("AITL", "CheckInPortal" + mParam.toString());

        return mParam;
    }


    public static Map<String, String> agendaSetreminder(String event_id, String user_id, String agenda_id, String reminder_time) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("agenda_id", agenda_id);
        mParam.put("reminder_time", reminder_time);
        Log.i("AITL", "setAgendaReminder" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> loginWithLinkedIn(String event_id, String email, String first_name, String last_name, String img, String device_name, String title, String company_name, String version_code_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("email", email);
        mParam.put("first_name", first_name);
        mParam.put("last_name", last_name);
        mParam.put("img", img);
        mParam.put("device_name", device_name);
        mParam.put("title", title);
        mParam.put("company_name", company_name);
        mParam.put("version_code_id", version_code_id);
        Log.i("AITL", "LinjedIn" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> pagewiseClick(String event_id, String user_id, String exhibitor_page_id, String sponser_id, String advertise_id, String click_type, String menu_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("exhibitor_page_id", exhibitor_page_id);
        mParam.put("sponser_id", sponser_id);
        mParam.put("advertise_id", advertise_id);
        mParam.put("click_type", click_type);
        mParam.put("menu_id", menu_id);
        Log.i("AITL", "PerPageClick" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> sendBeaconID(String organizer_id, String UDID, String event_id, String major, String minor, String nameSpaceId, String InstanceId) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("organizer_id", organizer_id);
        mParam.put("UDID", UDID);
        mParam.put("event_id", event_id);
        mParam.put("major", major);
        mParam.put("minor", minor);
        mParam.put("nameSpaceId", nameSpaceId);
        mParam.put("InstanceId", InstanceId);
        Log.i("AITL", "SendBeacon" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getBeaconListData(String organizer_id, String event_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("organizer_id", organizer_id);
        mParam.put("event_id", event_id);
        Log.i("AITL", "SendBeacon" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> getOfflineDataForExhibtior(String exhi_id, String event_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("exhi_id", exhi_id);
        mParam.put("event_id", event_id);
        Log.i("AITL", "ofllineData" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> sendNotification(String userid, String event_id, String uuid, String major, String minor, String nameSpaceId, String InstanceId) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("user_id", userid);
        mParam.put("event_id", event_id);
        mParam.put("UDID", uuid);
        mParam.put("major", major);
        mParam.put("minor", minor);
        mParam.put("nameSpaceId", nameSpaceId);
        mParam.put("InstanceId", InstanceId);
        Log.i("AITL", "SendNotification" + mParam.toString());
        return mParam;
    }

    public static Map<String, String> OtherPageWiseClick(String event_id, String user_id, String exhibitor_page_id, String sponser_id, String advertise_id, String click_type, String menu_id, String banner_id, String cms_id, String exhi_cat_id) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("exhibitor_page_id", exhibitor_page_id);
        mParam.put("sponser_id", sponser_id);
        mParam.put("advertise_id", advertise_id);
        mParam.put("click_type", click_type);
        mParam.put("menu_id", menu_id);
        mParam.put("banner_id", banner_id);
        mParam.put("cms_id", cms_id);
        mParam.put("exhi_cat_id", exhi_cat_id);
        Log.i("AITL", "PerPageClick" + mParam.toString());

        return mParam;
    }

    public static Map<String, String> hideAttendeeIndentity(String event_id, String user_id, String status) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("event_id", event_id);
        mParam.put("user_id", user_id);
        mParam.put("status", status);
        Log.i("AITL", "HideIdentity " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> saveProfileGoals(String user_id, String event_id, String goal) {

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("user_id", user_id);
        mParam.put("event_id", event_id);
        mParam.put("goal", goal);
        Log.i("AITL", "HideIdentity " + mParam.toString());

        return mParam;
    }

    public static Map<String, String> updateUserNotification(String EventId, String user_id, String noti_id, String gcmId, String device) {

        Map<String, String> mParam = new HashMap<String, String>();

        mParam.put("event_id", EventId);
        mParam.put("user_id", user_id);
        mParam.put("noti_id", noti_id);
        mParam.put("gcm", gcmId);
        mParam.put("device", device);
        Log.i("AITL", "getDrawerList: " + mParam.toString());

        return mParam;
    }


    public static Map<String, String> getGeolocationData(String EventId, String user_id) {

        Map<String, String> mParam = new HashMap<String, String>();


        mParam.put("event_id", EventId);
        mParam.put("user_id", user_id);

        return mParam;
    }

}
