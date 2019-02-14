package com.allintheloop.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Adapter.FooterImagePagerAdpater;
import com.allintheloop.Adapter.HeaderImagePagerAdpater;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.EventList;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nteam on 25/5/16.
 */
public class SessionManager {
    // Shared pref mode
    private static final int PRIVATE_MODE = 0;
    public static String speaker_id;
    public static String slilentAuctionP_id;
    public static String liveAuctionP_id;
    public static String OnlineShop_id;
    public static String pledge_id;
    public static String viewImg_tag;
    public static String noti_tag;
    public static String agenda_status;
    public static String itemProduct_id;
    public static String itemAuctionType;
    public static String activity_type;
    public static String activity_feedId;
    public static String gallryimg = "";
    public static String exhibitor_id = "";
    public static String exhi_pageId = "";
    public static String sponsor_id = "";
    public static String portalCheckInAttenDeeId = "";
    public static String AttenDeeId = "";
    public static int expantGrpPostion;
    public static String expandStatus = "";
    public static String mettingId = "";
    public static String Notificationmenu_id = "";
    public static boolean isLoginNoti = false;
    public static String notiTitle = "";
    public static String Map_coords = "";
    public static String notiMessage = "";
    public static String notiMessageType = "";
    public static boolean isattendeeStarclick = false;
    public static boolean iscontactattendeeStarclick = false;
    public static boolean isexhibitorstarClick = false;
    public static boolean isSpeakerstarClick = false;
    public static boolean isSpeakerClick = false;
    public static boolean isSponsorClick = false;
    public static String isExhibitorFav = "";
    public static String isSponsorFav = "";
    public static String isSpeakerFav = "";
    public static String private_senderId = "";
    public static String twitterHashTagName = "";
    public static String private_senderIcon = "";
    public static String checkingAttendeeID = "";
    public static String checkSurveyFragment = "";
    public static String onBoradData = "";
    public static String showOnce = "";
    public static String strMenuId = "";
    public static String strModuleId = "0";
    public static String isNoteCms = "0";
    public static String skipLogicAns = "";
    public static String notificationId = "";
    public static String exhibitor_standNumber = "";
    private static SharedPreferences pref;
    private static SharedPreferences isFirstTimePref;
    private static SharedPreferences.Editor editor;
    private static SharedPreferences.Editor editorisFirstTimePref;
    private static SessionManager objSessionManager;
    DefaultLanguage.DefaultLang defaultLang;
    private Context context;
    private SQLiteDatabaseHandler databaseHandler;

    // Constructor
    public SessionManager(Context context) {

        this.context = context;
        objSessionManager = this;
//        resources = context.getResources();
        pref = context.getSharedPreferences("ApplicationPreference", PRIVATE_MODE);
        isFirstTimePref = context.getSharedPreferences("isFirstTimeScreen", PRIVATE_MODE);
        editor = pref.edit();
        editorisFirstTimePref = isFirstTimePref.edit();
        databaseHandler = new SQLiteDatabaseHandler(context);
    }

    public static SessionManager getInstance() {
        return objSessionManager;
    }

    public void keyboradHidden(EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void setUserDetails(String loginResponse, boolean isLogin) {

        try {

            JSONObject objUser = new JSONObject(loginResponse);

            editor.putString("user_id", objUser.optString("customerId"));
            editor.putString("firstname", objUser.optString("firstname"));
            editor.putString("middlename", objUser.optString("middlename"));
            editor.putString("lastname", objUser.optString("lastname"));
            editor.putString("email", objUser.optString("email"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Storing login value as TRUE
        editor.putBoolean("is_login", isLogin);
        // commit changes
        editor.commit();
    }


    public void loginResponse(JSONObject loginresponse, boolean isLogin)   // My Method
    {
        JSONObject jsondata = loginresponse.optJSONObject("data");
        Log.d("JsonResponse", jsondata.toString());

        try {
            String res_imgurl = "";

            editor.putString("token", jsondata.optString("token"));
            editor.putString("user_id", jsondata.optString("User_id"));
            editor.putString("Firstname", jsondata.getString("Firstname"));
            editor.putString("Lastname", jsondata.optString("Lastname"));
            editor.putString("Company_name", jsondata.optString("Company_name"));
            editor.putString("Salutation", jsondata.optString("Salutation"));
            editor.putString("Title", jsondata.optString("Title"));
            editor.putString("Email", jsondata.optString("Email"));
            editor.putString("Street", jsondata.optString("Street"));
            editor.putString("Suburb", jsondata.optString("Suburb"));
            editor.putString("Postcode", jsondata.optString("Postcode"));
            editor.putString("Mobile", jsondata.optString("Mobile"));
            editor.putString("State", jsondata.optString("state_name"));
            editor.putString("Country", jsondata.optString("country_name"));
            editor.putString("State_id", jsondata.optString("State"));
            editor.putString("Country_id", jsondata.optString("Country"));
            editor.putString("Phone_business", jsondata.optString("Phone_business"));
            editor.putString("facebook_id", jsondata.optString("facebook_id"));
            editor.putString("user_profile", jsondata.getString("Logo"));
            editor.putString("isModerater", jsondata.getString("is_moderator"));
            editor.putString("Rid", jsondata.getString("Rid"));
            editor.putString("Organisor_id", jsondata.getString("Organisor_id"));
            editor.putString("linkedin_url", jsondata.getString("linkedin_url"));
            editor.putString("facebook_url", jsondata.getString("facebook_url"));
            editor.putString("twitter_url", jsondata.getString("twitter_url"));
            editor.putString("Website_url", jsondata.getString("Website_url"));
            editor.putString("user_event_id", jsondata.optString("user_event_id"));
            editor.putString("profile_goal", jsondata.optString("goal"));


//            if (loginresponse.has("extra_info"))
//            {
//                JSONObject jsonExtraInfo = loginresponse.getJSONObject("extra_info");
//                String multiplechoice;
//                editor.putString("First Name ?", jsonExtraInfo.optString("First Name ?"));
//                editor.putString("pla select any one ???", jsonExtraInfo.optString("pla select any one ???"));
//
//                JSONArray jsonArray = jsonExtraInfo.getJSONArray("selected ?");
//                for (int i = 0; i < jsonArray.length(); i++) {
//
//                    Log.d("AITL Array", jsonArray.get(i).toString());
//                    editor.putString("selected ?", jsonArray.get(i).toString());
//
//                }
//                editor.putString("description e.g:testing ?", jsonExtraInfo.optString("description e.g:testing ?"));
//                editor.putString("no ?", jsonExtraInfo.optString("no ?"));
//                editor.putString("seleted e.g:info ??", jsonExtraInfo.optString("seleted e.g:info ??"));
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Storing login value as TRUE
        editor.putBoolean("is_login", isLogin);
        // commit changes
        editor.commit();
        try {
            if (GlobalData.checkForUIDVersion())
                setUidCommonKey(jsondata.getJSONObject("key_data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public UidCommonKeyClass getUidCommonKey() {
        String data = pref.getString("uidAllKey", "");
        Gson gson = new Gson();
        return gson.fromJson(data, UidCommonKeyClass.class);
    }

    public void setUidCommonKey(JSONObject jsonObjectKeyData) {
        editor.putString("uidAllKey", jsonObjectKeyData.toString());
        editor.commit();
    }

    public void alertDailogLogin(Context context) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(getEventName())
                .content("Login or Sign Up to proceed. To sign up or login tap the Sign Up button on the top right of the screen.")
                .positiveText("Ok")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void setRequestlocation(String location) {
        editor.putString("location", location);
        editor.commit();
    }

    public void setRequestsenderid(String senderId) {
        editor.putString("RequestsenderId", senderId);
        editor.commit();
    }

    public String getAttendeeMainCategoryData() {
        return pref.getString("mainCategoryData", "");
    }

    public void setAttendeeMainCategoryData(String AttendeeMainGroup) {
        editor.putString("mainCategoryData", AttendeeMainGroup);
        editor.commit();
    }

    public String getRequestMeetingId() {
        return pref.getString("meetingId", "");
    }

    public String getProfileGoal() {
        return pref.getString("profile_goal", "");
    }

    public void setProfileGoal(String profile_goal) {
        editor.putString("profile_goal", profile_goal);
        editor.commit();
    }

    public void setProfileAbout(String aboutYou) {
        editor.putString("aboutYou", aboutYou);
        editor.commit();
    }

    public String getProfileAbout() {
        return pref.getString("aboutYou", "");
    }

    public void setRequestMeetingId(String meetingId) {
        editor.putString("meetingId", meetingId);
        editor.commit();
    }

    public String getRequestDate() {
        return pref.getString("date", "");
    }

    public void setRequestDate(String date) {
        editor.putString("date", date);
        editor.commit();
    }

    public String getRequestTime() {
        return pref.getString("time", "");
    }

    public void setRequestTime(String time) {
        editor.putString("time", time);
        editor.commit();
    }


    public String getVersionCodeId() {
        return pref.getString("version_code_id", "1");
    }

    public void setVersionCodeId(String version_code_id) {
        editor.putString("version_code_id", version_code_id);
        editor.commit();
    }

    public String getRequestLocation() {
        return pref.getString("location", "");
    }

    public String getIsLastCategoryName() {
        return pref.getString("LastCategoryName", "");
    }

    public void setIsLastCategoryName(String LastCategoryName) {
        editor.putString("LastCategoryName", LastCategoryName);
        editor.commit();
    }

    public String getFacebookUrl() {
        return pref.getString("facebook_url", "");
    }

    public String getlinkedInUrl() {
        return pref.getString("linkedin_url", "");
    }

    public String getTwitterUrl() {
        return pref.getString("twitter_url", "");
    }


    public void appColor(JSONObject jObjectevent) {
        try {

            editor.putString("topBack", jObjectevent.getString("Top_background_color"));
            editor.putString("topText", jObjectevent.getString("Top_text_color"));
            editor.putString("iconText", jObjectevent.getString("Icon_text_color"));
            editor.putString("menuBack", jObjectevent.getString("menu_background_color"));
            editor.putString("menuText", jObjectevent.getString("menu_text_color"));
            editor.putString("menuHove", jObjectevent.getString("menu_hover_background_color"));
            editor.putString("Footer_background_color", jObjectevent.getString("Footer_background_color"));
            editor.putString("fun_header_status", jObjectevent.getString("fun_header_status"));
            editor.putString("fun_background_color", jObjectevent.getString("fun_background_color"));
            editor.putString("Background_color", jObjectevent.getString("Background_color"));
            editor.putString("fun_top_background_color", jObjectevent.getString("fun_top_background_color"));
            editor.putString("fun_top_text_color", jObjectevent.getString("fun_top_text_color"));
            editor.putString("fun_footer_background_color", jObjectevent.getString("fun_footer_background_color"));
            editor.putString("fun_block_background_color", jObjectevent.getString("fun_block_background_color"));
            editor.putString("fun_block_text_color", jObjectevent.getString("fun_block_text_color"));
            editor.putString("allow_msg_keypeople_to_attendee", jObjectevent.getString("allow_msg_keypeople_to_attendee"));
            editor.putString("hide_request_meeting", jObjectevent.getString("hide_request_meeting"));
            editor.putString("attendee_hide_request_meeting", jObjectevent.getString("attendee_hide_request_meeting"));
            editor.putString("allow_msg_user_to_exhibitor", jObjectevent.getString("allow_msg_user_to_exhibitor"));
            editor.putString("theme_color", jObjectevent.getString("theme_color"));
            editor.putString("fun_logo_image", MyUrls.fun_logo + jObjectevent.getString("fun_logo_images"));
            editor.putString("show_agenda_place_left_column", jObjectevent.getString("show_agenda_place_left_column"));
            editor.putString("show_agenda_speaker_column", jObjectevent.getString("show_agenda_speaker_column"));
            editor.putString("show_agenda_location_column", jObjectevent.getString("show_agenda_location_column"));
            if (jObjectevent.has("is_enabled_favorites")) {
                editor.putString("is_enabled_favorites", jObjectevent.getString("is_enabled_favorites"));
            }
            if (jObjectevent.has("photo_filter_enabled")) {
                editor.putString("Isphoto_filter_enabled", jObjectevent.getString("photo_filter_enabled"));
            }
            if (jObjectevent.has("show_lead_retrival_setting_tab")) {
                editor.putString("show_lead_retrival_setting_tab", jObjectevent.getString("show_lead_retrival_setting_tab"));
            }
            if (jObjectevent.has("show_slider")) {
                editor.putString("show_slider", jObjectevent.getString("show_slider"));
            }

            if (jObjectevent.has("time_format")) {
                editor.putString("time_format", jObjectevent.getString("time_format"));
            }
            if (jObjectevent.has("attendee_agenda")) {
                editor.putString("attendee_agenda", jObjectevent.getJSONArray("attendee_agenda").toString());
            }
            if (jObjectevent.has("on_pending_agenda")) {
                editor.putString("on_pending_agenda", jObjectevent.getString("on_pending_agenda"));
            }
            if (jObjectevent.has("show_meeting_button")) {
                editor.putString("show_meeting_button", jObjectevent.getString("show_meeting_button"));
            }

            if (jObjectevent.has("allow_meeting_attendee_to_speaker")) {
                editor.putString("allow_meeting_attendee_to_speaker", jObjectevent.getString("allow_meeting_attendee_to_speaker"));
            }
            if (jObjectevent.has("lead_retrival_enabled")) {
                editor.putString("lead_retrival_enabled", jObjectevent.getString("lead_retrival_enabled"));
            }

            if (jObjectevent.has("private_message_enabled")) {
                editor.putString("private_message_enabled", jObjectevent.getString("private_message_enabled"));
            }
            if (jObjectevent.has("public_message_enabled")) {
                editor.putString("public_message_enabled", jObjectevent.getString("public_message_enabled"));
            }
            if (jObjectevent.has("agenda_sort")) {
                editor.putString("agenda_sort", jObjectevent.getString("agenda_sort"));
            }
            if (jObjectevent.has("activity_share_icon_setting")) {
                editor.putString("activity_share_icon_setting", jObjectevent.getString("activity_share_icon_setting"));
            }
            if (jObjectevent.has("key_people_sort_by")) {
                editor.putString("key_people_sort_by", jObjectevent.getString("key_people_sort_by"));
            }
            if (jObjectevent.has("date_format")) {
                editor.putString("date_format", jObjectevent.getString("date_format"));
            }
            if (jObjectevent.has("show_attendee_category")) {
                editor.putString("show_attendee_category", jObjectevent.getString("show_attendee_category"));
            }
            editor.putString("enable_user_goal", jObjectevent.optString("enable_user_goal"));
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getIsprivate_message_enabled() {
        return pref.getString("private_message_enabled", "0");
    }

    public String getIsUserGoalEnabled() {
        return pref.getString("enable_user_goal", "0");
    }


    public String getIsAttendeecategoryShow() {
        return pref.getString("show_attendee_category", "0");
    }

    public String getDate_format() {
        return pref.getString("date_format", "0");
    }

    public String getSpeakerIssortBy() {
        return pref.getString("key_people_sort_by", "0");
    }

    public String getActivity_share_icon_setting() {
        return pref.getString("activity_share_icon_setting", "1");
    }

    public String getAgendaSortOrder() {
        return pref.getString("agenda_sort", "1");
    }

    public String getIspublic_message_enabled() {
        return pref.getString("public_message_enabled", "0");
    }

    public String getIsLeadRetrivalEnabled() {
        return pref.getString("lead_retrival_enabled", "0");
    }


    public String getShowSlider() {
        return pref.getString("show_slider", "1");

    }

    public String getAttendee_agenda() {
        return pref.getString("attendee_agenda", "");

    }


    public String getPendingAgendaButton() {
        return pref.getString("on_pending_agenda", "");

    }

    public String getRequestMettingButton() {
        return pref.getString("show_meeting_button", "");

    }

    public ArrayList<String> getAttendee_agendaList() {
        ArrayList<String> arrayList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(getAttendee_agenda());
            for (int b = 0; b < jsonArray.length(); b++) {
                arrayList.add(jsonArray.getString(b).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public String getShowMeetingLocation() {
        return pref.getString("show_meeting_location", "0");
    }

    public void setShowMeetingLocation(String ShowLocation) {
        editor.putString("show_meeting_location", ShowLocation);
        editor.commit();
    }

    public String getIsStripeEnabled() {
        return pref.getString("stripe_enabled", "0");
    }

    public void setIsStripeEnabled(String stripe_enabled) {
        editor.putString("stripe_enabled", stripe_enabled);
        editor.commit();
    }

    public String getHostName() {
        return pref.getString("host_name", "server.allintheloop.net");
    }

    public void setHostName(String hostName) {
        editor.putString("host_name", hostName);
        editor.commit();
    }

    public String getshowLeadRetrivalSettingTab() {
        return pref.getString("show_lead_retrival_setting_tab", "0");
    }

    public String getPhotoFilterEnabled() {
        return pref.getString("Isphoto_filter_enabled", "");
    }

    public String getPresantationId() {
        return pref.getString("presantationId", "");
    }

    public void setPresantationId(String presantationId) {
        editor.putString("presantationId", presantationId);
        editor.commit();
    }

    public String getExhibitorParentCategoryId() {
        return pref.getString("ExhibitorParentCategoryId", "A");
    }

    public void setExhibitorParentCategoryId(String CategoryId) {
        editor.putString("ExhibitorParentCategoryId", CategoryId);
        editor.commit();
    }

    public String getExhibitorSubCategoryDesc() {
        return pref.getString("ExhibitorSubCategoryDesc", "");
    }

    public void setExhibitorSubCategoryDesc(String shortDesc) {
        editor.putString("ExhibitorSubCategoryDesc", shortDesc);
        editor.commit();
    }

    public String getIsPresantor() {
        return pref.getString("isPresantor", "");
    }

    public void setIsPresantor(String isPresantor) {
        editor.putString("isPresantor", isPresantor);
        editor.commit();
    }

    public String getAgendaPlaceLeft() {
        return pref.getString("show_agenda_place_left_column", "");
    }

    public String getHideMyIdentityStatus() {
        return pref.getString("identity_hidden", "1");
    }

    public void setHideMyIdentityStatus(String identity_hidden) {
        editor.putString("identity_hidden", identity_hidden);
        editor.commit();
    }

    public String getIsFirstTimeLoadPresantation() {
        return isFirstTimePref.getString("isFirstTime", "0");
    }

    public void setIsFirstTimeLoadPresantation(String isFirstTime) {
        editorisFirstTimePref.putString("isFirstTime", isFirstTime);
        // commit changes
        editorisFirstTimePref.commit();
    }

    public boolean getIsFirstTimeOnBoard() {
        return pref.getBoolean("isFirstOnBoard", false);
    }

    public void setIsFirstTimeOnBoard(boolean isFirstTime) {
        editor.putBoolean("isFirstOnBoard", isFirstTime);
        // commit changes
        editor.commit();
    }

    public void set_isForceLogin(String key) {
        editor.putString("is_forceLogin", key);
        editor.commit();
    }

    public void set_checkingBadgeLogo(String logo) {
        editor.putString("badgeLogo", logo);
        editor.commit();
    }

    public String getTimeFormat() {
        return pref.getString("time_format", "0");
    }

    public void setBeaconUUID(String key) {
        editor.putString("UUID", key);
        editor.commit();
    }

    public String getAllowShowAllAgenda() {
        return pref.getString("allow_show_all_agenda", "");
    }

    public void setAllowShowAllAgenda(String key) {
        editor.putString("allow_show_all_agenda", key);
        editor.commit();
    }

    public String getIsForceLogin() {
        return pref.getString("is_forceLogin", "0");
    }

    public String getFormStatus() {
        return pref.getString("form_status", "0");
    }

    public void setFormStatus(String status) {
        editor.putString("form_status", status);
        editor.commit();
    }

    public String getAttendee_hide_request_meeting() {
        return pref.getString("attendee_hide_request_meeting", "0");
    }

    public String getfavIsEnabled() {
        return pref.getString("is_enabled_favorites", "");
    }

    public String getofflineSurveyDataExhiLead() {
        return pref.getString("surveyData", "");
    }

    public void setofflineSurveyDataExhiLead(String data) {
        editor.putString("surveyData", data);
        editor.commit();
    }

    public String getofflineCustomeColumnExhiLead() {
        return pref.getString("custom_column", "");
    }

    public void setofflineCustomeColumnExhiLead(String data) {
        editor.putString("custom_column", data);
        editor.commit();
    }

    public String getPhotoFilterImage() {
        return pref.getString("photo_filterImage", "");
    }

    public void setPhotoFilterImage(String photo_filterImage) {
        editor.putString("photo_filterImage", photo_filterImage);
        editor.commit();
    }

    public String getCategoryId() {
        return pref.getString("categoryId", "");
    }

    public void setCategoryId(String categoryId) {
        editor.putString("categoryId", categoryId);
        editor.commit();
    }

    public String getCmsSuperGroupId() {
        return pref.getString("cmsSuperGroupId", "");
    }

    public void setCmsSuperGroupId(String cmsSuperGroupId) {
        editor.putString("cmsSuperGroupId", cmsSuperGroupId);
        editor.commit();
    }

    public String getCmsChildGroupId() {
        return pref.getString("CmsChildGroupId", "");
    }

    public void setCmsChildGroupId(String CmsChildGroupId) {
        editor.putString("CmsChildGroupId", CmsChildGroupId);
        editor.commit();
    }

    public String getAgendalocationLeft() {
        return pref.getString("show_agenda_location_column", "");
    }

    public String getAgendaSpeakerColumn() {
        return pref.getString("show_agenda_speaker_column", "");
    }

    public String getKeySpeakerAllowmessage() {
        return pref.getString("allow_msg_keypeople_to_attendee", "");
    }

    public void setKeySpeakerAllowmessage(String key) {
        editor.putString("allow_msg_keypeople_to_attendee", key);
        editor.commit();
    }

    public String getRequestexhibitorKey() {
        return pref.getString("hide_request_meeting", "");
    }

    public void setRequestexhibitorKey(String key) {
        editor.putString("hide_request_meeting", key);
        editor.commit();
    }

    public String getKeyExhibitorAllowmessage() {
        return pref.getString("allow_msg_user_to_exhibitor", "");
    }

    public void setKeyExhibitorAllowmessage(String key) {
        editor.putString("allow_msg_user_to_exhibitor", key);
        editor.commit();
    }


    public String getQaSessionId() {
        return pref.getString("qaSessionId", "");
    }

    public void setQaSessionId(String key) {
        editor.putString("qaSessionId", key);
        editor.commit();
    }

    public String getRolId() {
        return pref.getString("Rid", "");
    }

    public String getSecretKey() {
        return pref.getString("key", "");
    }

    public void setSecretKey(String secretKey) {
        editor.putString("key", secretKey);
        editor.commit();
    }

    public String getPrivatePublicStatus() {
        return pref.getString("isPublic", "");
    }

    public void setPrivatePublicStatus(String status) {
        editor.putString("isPublic", status);
        editor.commit();
    }

    public String getUserProfile() {
        return pref.getString("user_profil", "");
    }

    public void setUserProfile(String user_profil) {
        editor.putString("user_profil", user_profil);
        editor.commit();
    }

    public void setNotification_Userid(String Notiuser_id) {
        editor.putString("noti_UserId", Notiuser_id);
        editor.commit();
    }

    public String getNotification_UserId() {
        return pref.getString("noti_UserId", "");
    }

    public String isModerater() {
        return pref.getString("isModerater", "");
    }

    public String getMapid() {

        return pref.getString("map_id", "");
    }

    public void setMapid(String map_id) {
        editor.putString("map_id", map_id);
        editor.commit();
    }

    public String getMenuid() {

        return pref.getString("menu_id", "");
    }

    public void setMenuid(String menuid) {
        editor.putString("menu_id", menuid);
        editor.commit();
    }

    public String getAgendaCategoryId() {

        return pref.getString("agendaCategoryId", "");
    }

    public void setAgendaCategoryId(String agendaCategoryId) {
        editor.putString("agendaCategoryId", agendaCategoryId);
        editor.commit();
    }

    public String getAndroidId() {

        return pref.getString("android_id", "");
    }

    public void setAndroidId(String android_id) {
        editor.putString("android_id", android_id);
        editor.commit();
    }

    public String getNotification_role() {
        return pref.getString("noti_role", "");
    }

    public void setNotification_role(String role) {
        editor.putString("noti_role", role);
        editor.commit();
    }

    public String getfun_logo_image() {
        return pref.getString("fun_logo_image", "");
    }

    public String getHeaderStatus() {
        return pref.getString("fundrising_enabled", "");
    }

    public String getFunBackColor() {
        return pref.getString("fun_background_color", "#FFFFFF");
    }

    public String getFunTopBackColor() {
        return pref.getString("fun_top_background_color", "#FFFFFF");
    }

    public String getFunTopTextColor() {
        return pref.getString("fun_top_text_color", "#FFFFFF");
    }

    public String getBackGroundColor() {
        return pref.getString("Background_color", "#FFFFFF");
    }

    public String getFunThemeColor() {
        return pref.getString("theme_color", "#FFFFFF");
    }

    public String getTopBackColor() {
        return pref.getString("topBack", "#FFFFFF");

    }

    public String getTopTextColor() {
        return pref.getString("topText", "#FFFFFF");

    }

    public String getMenuBackColor() {
        return pref.getString("menuBack", "#FFFFFF");

    }

    public String getMenuTextColor() {
        return pref.getString("menuText", "#FFFFFF");

    }

    public String getMenuHoveColor() {
        return pref.getString("menuHove", "#FFFFFF");

    }

    public void updateProfile(JSONObject updateResponse)   // My Method
    {
        JSONObject jsondata = updateResponse.optJSONObject("data");
        Log.d("JsonResponse", jsondata.toString());

        try {


            editor.putString("token", jsondata.optString("token"));
            editor.putString("user_id", jsondata.optString("User_id"));
            editor.putString("Firstname", jsondata.getString("Firstname"));
            editor.putString("Lastname", jsondata.optString("Lastname"));
            editor.putString("Company_name", jsondata.optString("Company_name"));
            editor.putString("Salutation", jsondata.optString("Salutation"));
            editor.putString("Title", jsondata.optString("Title"));
            editor.putString("Email", jsondata.optString("Email"));
            editor.putString("Street", jsondata.optString("Street"));
            editor.putString("Suburb", jsondata.optString("Suburb"));
            editor.putString("Postcode", jsondata.optString("Postcode"));
            editor.putString("Mobile", jsondata.optString("Mobile"));
            editor.putString("State", jsondata.optString("state_name"));
            editor.putString("Country", jsondata.optString("country_name"));
            editor.putString("State_id", jsondata.optString("State"));
            editor.putString("Country_id", jsondata.optString("Country"));
            editor.putString("Phone_business", jsondata.optString("Phone_business"));
            editor.putString("facebook_id", jsondata.optString("facebook_id"));
            editor.putString("Speaker_desc", jsondata.optString("Speaker_desc"));
            editor.putString("user_profile", jsondata.getString("Logo"));

            editor.commit();


            Log.d("AITL", "FirstName" + jsondata.getString("Firstname"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getExhibitorSponsorCategoryId() {
        return pref.getString("ExhibitorSponsorCategoryId", "");
    }

    public void setExhibitorSponsorCategoryId(String ExhibitorSponsorCategoryId) {
        editor.putString("ExhibitorSponsorCategoryId", ExhibitorSponsorCategoryId);
        editor.commit();
    }

    public void eventdata(EventList EvntObj) {
        editor.putString("ListEventID", EvntObj.getiD());
        editor.putString("facebook_status", EvntObj.getFb_Status());
        editor.putString("Event_logo", EvntObj.getImg());
        editor.putString("EventName", EvntObj.geteName());
        editor.putString("event_type", EvntObj.getEvent_type());
        editor.putString("fundrising_enabled", EvntObj.getFundraising_enbled());
        editor.putString("linkedin_login_enabled", EvntObj.getLinkedin_login_enabled());
        editor.putString("show_login_screen", EvntObj.getShow_login_screen());
        editorisFirstTimePref.putString("default_lang", EvntObj.getLang());
        editor.commit();
        editorisFirstTimePref.commit();
        defaultLang = getMultiLangString();
        setLangId(defaultLang.getLangId());
        setLangImgUrl(defaultLang.getLangIcon());
//        if (!(databaseHandler.isEventExist(EvntObj.getiD()))) {
//            databaseHandler.insertEventListing(EvntObj.getiD(), EvntObj.geteName(), EvntObj.getFb_Status(), EvntObj.getEvent_type(), EvntObj.getImg(), EvntObj.getFundraising_enbled(), EvntObj.getLang(), EvntObj.getShow_login_screen());
//        } else {
//            databaseHandler.UpdateEventListing(EvntObj.getiD(), EvntObj.geteName(), EvntObj.getFb_Status(), EvntObj.getEvent_type(), EvntObj.getImg(), EvntObj.getFundraising_enbled(), EvntObj.getLang(), EvntObj.getShow_login_screen());
//        }
//        Cursor cursor=databaseHandler.getEventListData();
//        cursor.moveToFirst();
//        Log.d("AITL DatabaseData",cursor.getString(cursor.getColumnIndex(databaseHandler.Event_NAME)));
    }

    public String getFolder_id() {
        return pref.getString("folder_id", "0");
    }

    /*  My Data*/

    public String getFundrising_status() {
        return pref.getString("fundrising_enabled", "0");
    }

    public void agenda_id(String agenda_id) {
        editor.putString("agenda_id", agenda_id);
        editor.commit();
    }

    public void cms_id(String cms_id) {
        editor.putString("cms_id", cms_id);
        editor.commit();
    }

//    public void setAgendaStatus(String agenda_status)
//    {
//        editor.putString("agenda_status", agenda_status);
//        editor.commit();
//    }
//
//    public String getAgendaStatus()
//    {
//        return pref.getString("agenda_status", "");
//    }

    public String getAllowMettingRequestattendeeToSpeaker() {
        return pref.getString("allow_meeting_attendee_to_speaker", "");
    }

    public void folder_id(String folder_id) {
        editor.putString("folder_id", folder_id);
        editor.commit();
    }

    public String getTermsAndCondition() {
        return pref.getString("termsandCondition", "http://allintheloop.com/terms.html");
    }

    public void setTermsAndCondition(String hostName) {
        editor.putString("termsandCondition", hostName);
        editor.commit();
    }

    public String getDocumentHirarchy() {
        return pref.getString("leval", "Documents / ");
    }

    public void setDocumentHirarchy(String leval) {
        editor.putString("leval", leval);
        editor.commit();
    }

    public String get_cmsId() {
        return pref.getString("cms_id", "");
    }

    public String get_show_login_screen() {
        return pref.getString("show_login_screen", "1");
    }

    public String agenda_id() {
        return pref.getString("agenda_id", "");
    }

    public String getGcm_id() {
        return pref.getString("gcm_id", "");
    }

    public void setGcm_id(String gcm_id) {
        editor.putString("gcm_id", gcm_id);
        editor.commit();
    }

    public String getUserId() {

        return pref.getString("user_id", "0");
    }

    public String getEventId() {

        return pref.getString("ListEventID", "");
    }

    public String getFacebookStatus() {
        return pref.getString("facebook_status", "");
    }

    public String getLinkedInStatus() {
        return pref.getString("linkedin_login_enabled", "");
    }

    public String getToken() {
        return pref.getString("token", "");
    }

    public String getEventType() {
        return pref.getString("event_type", "");
    }

    public String getEmail() {
        return pref.getString("Email", "");
    }

    public String getFirstName() {
        return pref.getString("Firstname", "");
    }

    public String getImagePath() {
        return pref.getString("user_profile", "");
    }

    public void setImagePath(String img) {
        editor.putString("user_profile", img);
        editor.commit();

    }

    public String getOrganizer_id() {
        return pref.getString("Organisor_id", "");
    }

    public String getFacebookId() {
        return pref.getString("facebook_id", "");
    }

    public String getEventName() {
        return pref.getString("EventName", "");
    }

    public String getSalutationName() {
        return pref.getString("Salutation", "");
    }

    public String getUserEventId() {
        return pref.getString("user_event_id", "");
    }

    public String getLastName() {
        return pref.getString("Lastname", "");
    }

    public String getTitle() {
        return pref.getString("Title", "");
    }

    public String getStreet() {
        return pref.getString("Street", "");
    }

    public String getSuburb() {
        return pref.getString("Suburb", "");
    }

    public String getPostcode() {
        return pref.getString("Postcode", "");
    }

    public String getMobile() {
        return pref.getString("Mobile", "");
    }

    public String getPhone_business() {
        return pref.getString("Phone_business", "");
    }

    public String getEvent_Logo() {
        return pref.getString("Event_logo", "");
    }

    public String getCompany_name() {
        return pref.getString("Company_name", "");
    }

    public String getCountryName() {
        return pref.getString("Country", "");
    }

    public String getStateName() {
        return pref.getString("State", "");
    }

    public String getCountryId() {
        return pref.getString("Country_id", "");
    }

    public void logout() {
        if (isLogin())
            AppController.getInstance().stopBeacon();
        editor.clear();
        editor.commit();
    }

    public String getMapPrentGroupID() {
        return pref.getString("mapParentGroupId", "");

    }

    public void setMapPrentGroupID(String parentGroupId) {
        editor.putString("mapParentGroupId", parentGroupId);
        editor.commit();
    }

    public String getSponsorPrentGroupID() {
        return pref.getString("sponsorParentGroupId", "");

    }

    public void setSponsorPrentGroupID(String parentGroupId) {
        editor.putString("sponsorParentGroupId", parentGroupId);
        editor.commit();
    }

    public String getQandAgroupID() {
        return pref.getString("QandAgroupID", "");

    }

    public void setQandAgroupID(String QandAgroupID) {
        editor.putString("QandAgroupID", QandAgroupID);
        editor.commit();
    }

    public String getAgendagroupID() {
        return pref.getString("AgendagroupID", "");

    }

    public void setAgendagroupID(String QandAgroupID) {
        editor.putString("AgendagroupID", QandAgroupID);
        editor.commit();
    }

    public void saveNoteStatus(String show_notes_icon) {
        editor.putString("show_notes_icon", show_notes_icon);
        editor.commit();
    }

    public String getNoteStatus() {
        return pref.getString("show_notes_icon", "");

    }

    public void languageClear() {
        editorisFirstTimePref.clear();
        editorisFirstTimePref.commit();
    }

    public boolean isLogin() {
        return pref.getBoolean("is_login", false);
    }

    public void setLogin(boolean islogin) {
        editor.putBoolean("is_login", islogin);
        // commit changes
        editor.commit();
    }


    /*My data*/

    public DefaultLanguage.DefaultLang getMultiLangString() {
        String default_lang = isFirstTimePref.getString("default_lang", "");
        Gson gson = new Gson();
        DefaultLanguage.DefaultLang defaultLanguage = gson.fromJson(default_lang, DefaultLanguage.DefaultLang.class);
        return defaultLanguage;
    }

//    public DefaultLanguage.DefaultLang getMultiLangString()
//    {
//        String default_lang = isFirstTimePref.getString("default_lang","");
//        Gson gson = new Gson();
//        DefaultLanguage defaultLanguage = gson.fromJson(default_lang, DefaultLanguage.class);
//        return defaultLanguage.getDefaultLang();
//    }

    public void setMultiLangString(String langauges) {
        editorisFirstTimePref.putString("default_lang", langauges);
        editorisFirstTimePref.commit();
    }

    public String getLangId() {
        String default_lang = isFirstTimePref.getString("lang_id", "");
        return default_lang;
    }

    public void setLangId(String langId) {
        editorisFirstTimePref.putString("lang_id", langId);
        editorisFirstTimePref.commit();
    }

    public String getLangImgUrl() {
        String default_lang = isFirstTimePref.getString("lang_icon", "");
        return default_lang;
    }

    public void setLangImgUrl(String url) {
        editorisFirstTimePref.putString("lang_icon", url);
        editorisFirstTimePref.commit();
    }

    public String getisFooterAdvertiesment() {
        return pref.getString("footerType", "1");

    }

    public void setisFooterAdvertiesment(String footerType) {
        editor.putString("footerType", footerType);
        editor.commit();
    }


    public void footerView(Context context, String type, RelativeLayout MainLayout, RelativeLayout relative_static, LinearLayout linear_content, final ArrayList<AdvertiesMentbottomView> advertiesMentbottomViews, Activity activity) {

        if (advertiesMentbottomViews.size() != 0) {

            setisFooterAdvertiesment("1");

            try {
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
                final ViewPager footerViewPager = new ViewPager(context);
                FooterImagePagerAdpater footerImagePagerAdpater = new FooterImagePagerAdpater(activity, advertiesMentbottomViews);
                footerViewPager.setAdapter(footerImagePagerAdpater);


                if (type.equalsIgnoreCase("0")) {

                    RelativeLayout.LayoutParams paramsViewPager = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) px);
                    paramsViewPager.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    footerViewPager.setLayoutParams(paramsViewPager);

                    MainLayout.addView(footerViewPager, 1);

                    RelativeLayout.LayoutParams paramsLinearHome = (RelativeLayout.LayoutParams) relative_static.getLayoutParams();
                    paramsLinearHome.setMargins(0, 0, 0, (int) px);
                    paramsLinearHome.addRule(RelativeLayout.ABOVE, footerViewPager.getId());
                    relative_static.setLayoutParams(paramsLinearHome);

                } else {

                    RelativeLayout.LayoutParams paramsViewPager = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) px);
                    footerViewPager.setLayoutParams(paramsViewPager);
                    linear_content.addView(footerViewPager);
                }

                Timer timer;
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        footerViewPager.post(new Runnable() {

                            @Override
                            public void run() {
                                footerViewPager.setCurrentItem((footerViewPager.getCurrentItem() + 1) % advertiesMentbottomViews.size());
                            }
                        });
                    }
                };
                timer = new Timer();
                timer.schedule(timerTask, 0, 5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setisFooterAdvertiesment("0");
        }

    }

    public void NotefooterView(Context context, String type, RelativeLayout MainLayout, RelativeLayout relative_static, LinearLayout linear_content, final ArrayList<AdvertiesMentbottomView> advertiesMentbottomViews, Activity activity) {

        if (advertiesMentbottomViews.size() != 0) {

            setisFooterAdvertiesment("0");

            try {
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
                final ViewPager footerViewPager = new ViewPager(context);
                FooterImagePagerAdpater footerImagePagerAdpater = new FooterImagePagerAdpater(activity, advertiesMentbottomViews);
                footerViewPager.setAdapter(footerImagePagerAdpater);


                if (type.equalsIgnoreCase("0")) {

                    RelativeLayout.LayoutParams paramsViewPager = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) px);
                    paramsViewPager.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    footerViewPager.setLayoutParams(paramsViewPager);

                    MainLayout.addView(footerViewPager, 1);

                    RelativeLayout.LayoutParams paramsLinearHome = (RelativeLayout.LayoutParams) relative_static.getLayoutParams();
                    paramsLinearHome.setMargins(0, 0, 0, (int) px);
                    paramsLinearHome.addRule(RelativeLayout.ABOVE, footerViewPager.getId());
                    relative_static.setLayoutParams(paramsLinearHome);

                } else {
                    RelativeLayout.LayoutParams paramsViewPager = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) px);
                    footerViewPager.setLayoutParams(paramsViewPager);
                    linear_content.addView(footerViewPager);
                }

                Timer timer;
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        footerViewPager.post(new Runnable() {

                            @Override
                            public void run() {
                                footerViewPager.setCurrentItem((footerViewPager.getCurrentItem() + 1) % advertiesMentbottomViews.size());
                            }
                        });
                    }
                };
                timer = new Timer();
                timer.schedule(timerTask, 0, 5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setisFooterAdvertiesment("0");
        }
    }


    public void HeaderView(Context context, String type, RelativeLayout MainLayout, RelativeLayout relative_static, LinearLayout linear_content, final ArrayList<AdvertiesmentTopView> advertiesmentTopViewArrayList, Activity activity) {
        if (advertiesmentTopViewArrayList.size() != 0) {

            try {
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
                final ViewPager headerViewPager = new ViewPager(context);
                HeaderImagePagerAdpater footerImagePagerAdpater = new HeaderImagePagerAdpater(activity, advertiesmentTopViewArrayList);
                headerViewPager.setAdapter(footerImagePagerAdpater);
                if (type.equalsIgnoreCase("0")) {

                    RelativeLayout.LayoutParams paramsViewPager = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) px);
                    paramsViewPager.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    headerViewPager.setLayoutParams(paramsViewPager);

                    MainLayout.addView(headerViewPager);

                    RelativeLayout.LayoutParams paramsLinearHome = (RelativeLayout.LayoutParams) relative_static.getLayoutParams();
                    if (getisFooterAdvertiesment().equalsIgnoreCase("1")) {
                        paramsLinearHome.setMargins(0, (int) px, 0, (int) px);
                    } else {
                        paramsLinearHome.setMargins(0, (int) px, 0, 0);
                    }

                    paramsLinearHome.addRule(RelativeLayout.BELOW, headerViewPager.getId());
                    relative_static.setLayoutParams(paramsLinearHome);

                } else {

                    RelativeLayout.LayoutParams paramsViewPager = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) px);
                    headerViewPager.setLayoutParams(paramsViewPager);
                    linear_content.addView(headerViewPager, 0);
                }

                Timer timer;
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        headerViewPager.post(new Runnable() {

                            @Override
                            public void run() {
                                headerViewPager.setCurrentItem((headerViewPager.getCurrentItem() + 1) % advertiesmentTopViewArrayList.size());
                            }
                        });
                    }
                };
                timer = new Timer();
                timer.schedule(timerTask, 0, 5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void alertDailogForGeoLocation(Context context, MaterialDialog.SingleButtonCallback singleButtonCallback) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(getEventName())
                .content("Please enable GPS")
                .positiveText("Setting")
                .onPositive(singleButtonCallback)
                .show();
    }
}
