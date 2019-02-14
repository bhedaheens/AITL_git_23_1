package com.allintheloop.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.allintheloop.Bean.AgendaData.Agenda;
import com.allintheloop.Bean.AgendaData.AgendaCategory;
import com.allintheloop.Bean.AgendaData.AgendaCategoryRelation;
import com.allintheloop.Bean.AgendaData.AgendaData;
import com.allintheloop.Bean.AgendaData.AgendaType;
import com.allintheloop.Bean.Attendee.Attendance;
import com.allintheloop.Bean.CmsGroupData.CmsListData;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorCategoryListing;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorLead_MyLeadData_Offline;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorCountry;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorParentCatGroup;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorType;
import com.allintheloop.Bean.ExhibitorListClass.FavoritedExhibitor;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorListdata;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorParentCategoryData;
import com.allintheloop.Bean.FavoritedSpeaker;
import com.allintheloop.Bean.GroupingData.GroupModuleData;
import com.allintheloop.Bean.GroupingData.GroupRelationModuleData;
import com.allintheloop.Bean.GroupingData.SuperGroupData;
import com.allintheloop.Bean.GroupingData.SuperGroupRelationData;
import com.allintheloop.Bean.Map.MapListData;
import com.allintheloop.Bean.Speaker.SpeakerListClass;
import com.allintheloop.Bean.Speaker.SpeakerType;
import com.allintheloop.Bean.Speaker.SpeakerList;
import com.allintheloop.Bean.SponsorClass.FavoritedSponsor;
import com.allintheloop.Bean.SponsorClass.SponsorListNewData;
import com.allintheloop.Bean.SponsorClass.SponsorType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nteam on 5/10/16.
 */
public class SQLiteDatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AllInTheLoop";
    private static final int DATABASE_VERSION = 14;

    // Table for Tmp
    public static final String TABLE_tmp = "TmpValue";
    public static final String TABLE_EventList = "EventList";
    public static final String TABLE_EventHomeData = "EventHomeData";
    public static final String TABLE_Agenda = "EventAgendaData";
    public static final String TABLE_DetailAgenda = "DetailAgendaData";
    public static final String TABLE_PendingRequest = "PendingRequest";
    public static final String TABLE_PendingActivity_request = "PendingActivityRequest";
    public static final String TABLE_Activity = "ActivityList";
    public static final String Table_Attendeelisting = "AttendeeListing";
    public static final String Table_notesListing = "NotesListing";
    public static final String Table_CMSListing = "CMSListing";
    public static final String TABLE_Presantation = "EventPresantationData";
    public static final String Table_ExhibitorDetail = "ExhibitorDetail";
    public static final String Table_SponsorDetail = "SponsorDetail";
    public static final String Table_AtteendeeSpeakerDetail = "AtteendeeSpeakerDetail";
    public static final String TABLE_Presantation_DetailImage = "Presantation_DetailImage";
    public static final String TABLE_PDetailId = "TABLE_PDetailId";
    public static final String TABLE_LeadExhibitorOffline = "TABLE_LeadExhibitorOffline";
    public static final String TABLE_GROUP = "GroupTable";
    public static final String TABLE_GROUP_RELATION = "GroupRelatoinTable";
    public static final String TABLE_MAP_List = "MapList";
    public static final String TABLE_UPDATE_MODULE = "UpdateModule";
    public static final String TABLE_SPONSOR_LIST_TYPE = "TableSponsorType";
    public static final String TABLE_SPONSOR_LIST = "TableSponsorLIST";
    public static final String TABLE_advertiesment = "TABLE_advertiesment";
    public static final String TABLE_EXHIBITOR_PARENT_CATEGORY = "ParentCategory";
    public static final String TABLE_EXHIBITOR_SUB_CATEGORY = "SubCategory";
    public static final String TABLE_EXHIBITOR_TYPES = "ExhibitorType";
    public static final String TABLE_EXHIBITOR_COUNTRYLIST = "ExhibitorCountryList";
    public static final String TABLE_EXHIBITOR_PRODUCTCAT_LIST = "ExhibitorProductCategory";
    public static final String TABLE_EXHIBITOR_PRODUCTCAT_GROUP_LIST = "ExhibitorProductCategoryGroupList";
    public static final String TABLE_EXHIBITOR_LIST = "ExhibitorList";
    public static final String TABLE_AGENDA_CATEGORYLIST = "AgendaCategoryList";
    public static final String TABLE_AGENDA_CATEGORY_RELATION = "AgendaCategoryRelation";
    public static final String TABLE_AGENDA_TYPELIST = "AgendaTypeList";
    public static final String TABLE_AGENDA_LIST = "AgendaList";
    public static final String TABLE_SPEAKER_LIST = "TableSpeakerLIST";
    public static final String TABLE_SPEAKER_LIST_TYPE = "TableSpeakerType";
    public static final String TABLE_SUPERGROUP_LIST = "SuperGroupList";
    public static final String TABLE_SUPERGROUP_RELATION = "SuperGroupRelation";
    public static final String TABLE_CMSLISTINGOffline = "cmsListingOffline";
    public static final String TABLE_MATCHMAKING_List = "TablematchMakingList";
    public static final String TABLE_MATCHMAKING_MODULENAME_List = "TablematchMakingModuleList";
    public static final String TABLE_ExhiLeadSurveyUpload = "ExhiLeadSurveyUpload";


    // TABLE_MATCHMATCHING_LIST FIELDS
    public static final String MATCHMAKING_EVENTID = "matchmaking_event_id";
    public static final String MATCHMAKING_USERID = "matchmaking_user_id";
    public static final String MATCHMAKING_TITLE = "matchmaking_Title";
    public static final String MATCHMAKING_SUBTITLE = "matchmaking_SubTitle";
    public static final String MATCHMAKING_LOGO = "matchmaking_Logo";
    public static final String MATCHMAKING_ID = "matchmaking_Id";
    public static final String MATCHMAKING_EXHIPAGEID = "matchmaking_ExhiPageId";
    public static final String MATCHMAKING_TYPEID = "matchmaking_TypeId";
    public static final String MATCHMAKING_TYPENAME = "matchmaking_TypeName";


    public static final String MATCHMAKING_MODULE_EVENTID = "matchmaking_module_eventId";
    public static final String MATCHMAKING_MODULE_USERID = "matchmaking_module_userId";
    public static final String MATCHMAKING_MODULE_ID = "matchmaking_moduleId";
    public static final String MATCHMAKING_MODULE_NAME = "matchmaking_moduleName";

    //TABLE_AGENDA_CATEGORYLIST Fields
    public static final String AGENDA_CATEGORY_EVENTID = "event_id";
    public static final String AGENDA_CATEGORY_ID = "categoryId";
    public static final String AGENDA_CATEGORY_NAME = "categoryName";
    public static final String AGENDA_CATEGORY_IMAGE = "categoryImage";
    public static final String AGENDA_CATEGORY_TYPE = "categoryType";
    public static final String AGENDA_CATEGORY_WELCOME_SCREEN = "agendaCategoryWelcomeScreen";
    public static final String AGENDA_CATEGORY_SortOrder = "CategorySortOrder";


    //TABLE_AGENDA_CATEGORY_RELATION Fields
    public static final String AGENDA_CAT_RELATION_EVENTID = "event_id";
    public static final String AGENDA_CAT_RELATION_AGENDAID = "agendaId";
    public static final String AGENDA_CAT_RELATION_CATID = "categoryId";

    //TABLE_AGENDA_TYPELIST Fields
    public static final String AGENDA_TYPELIST_EVENTID = "event_id";
    public static final String AGENDA_TYPELIST_TYPEID = "typeId";
    public static final String AGENDA_TYPELIST_TYPENAME = "typeName";
    public static final String AGENDA_TYPELIST_ORDERNO = "orderNo";

    //TABLE_AGENDA_LIST Fields
    public static final String AGENDA_EVENTID = "event_id";
    public static final String AGENDA_AGENDAID = "agendaId";
    public static final String AGENDA_HEADING = "heading";
    public static final String AGENDA_SPEAKER = "speaker";
    public static final String AGENDA_LOCATION = "location";
    public static final String AGENDA_SESSION_IMAGE = "sessionImage";
    public static final String AGENDA_STARTDATE = "startDate";
    public static final String AGENDA_ENDDATE = "endDate";
    public static final String AGENDA_STARTTIME = "startTime";
    public static final String AGENDA_ENDTIME = "endTime";
    public static final String AGENDA_TYPENAME = "typeName";
    public static final String AGENDA_TYPEID = "typeId";
    public static final String AGENDA_SORT_ORDER = "sort_order";
    public static final String AGENDA_SESSION_TRACK = "session_track";

    //  TABLE_Update Fields
    public static final String Update_MODULE_ID = "moduleId";
    public static final String Update_MODULE_NAME = "ModuleName";
    public static final String Update_DATE = "updateDate";
    public static final String UPDATE_MODULE_EVENTID = "updateModuleEventId";


    // TABLE_GROUP Fields
    public static final String GROUP_ID = "groupId";
    public static final String GROUP_EVENTID = "event_id";
    public static final String GROUP_MENU_ID = "menuId";
    public static final String GROUP_NAME = "groupName";
    public static final String GROUP_ICON = "groupIcon";
    public static final String GROUP_SORT_ORDER_NO = "group_sort_order_no";

    // TABLE_GROUP_RELATION Fields
    public static final String GROUP_RELATION_ID = "groupId";
    public static final String GROUP_RELATION_EVENTID = "event_id";
    public static final String GROUP_RELATION_MENU_ID = "menuId";
    public static final String GROUP_RELATION_MODULEID = "moduleId"; // Module ID is Particular DataID


    // TABLE_MAP_LIST Fields

    public static final String MAP_LIST_MODULEID = "MapId";    // Module ID is MapID
    public static final String MAP_LIST_EventId = "MapEventId";    // Module ID is MapID
    public static final String MAP_LIST_TITLE = "MapTitle";
    public static final String MAP_LIST_LAT_LONG = "MapLatLong";
    public static final String MAP_LIST_ADDRESS = "MapAddress";
    public static final String MAP_LIST_IMAGES = "MapImages";
    public static final String MAP_LIST_INCLUDEMAP = "MapIncludeMap";
    public static final String MAP_LIST_CHECK_DWGFILES = "MapDwgFiles";
    public static final String MAP_LIST_GOOGLE_MAP_ICON = "MapGoogleMapIcon";
    public static final String MAP_LIST_FLOOR_PLAN_ICON = "MapFloorPlanIcon";
    public static final String MAP_LIST_LAT = "MapLat";
    public static final String MAP_LIST_LONG = "MapLong";


    // TABLE_ADVERITESMENT  Fields
    public static final String adaverties_event_id = "adaverties_event_id";
    public static final String adverties_menuID = "adverties_menuID";
    public static final String adverties_Data = "adverties_Data";

    // TABLE_GROUP_RELATION Fields

    //============================================


    //TABLE_PARENT_CATEGORY Fields
    public static final String PARENT_CATEGORY_EVENTID = "event_id";
    public static final String PARENT_CATEGORY_ID = "categoryId";
    public static final String PARENT_CATEGORY_NAME = "categoryName";
    public static final String PARENT_CATEGORY_ICON = "categoryIcon";
    public static final String PARENT_CATEGORY_SORT_ORDERNO = "parentCategory_sortOrderNo";

    //TABLE_SUB_CATEGORY Fields
    public static final String SUB_CATEGORY_EVENTID = "event_id";
    public static final String SUB_CATEGORY_ID = "categoryId";
    public static final String SUB_CATEGORY_NAME = "categoryName";
    public static final String SUB_CATEGORY_ICON = "categoryIcon";
    public static final String SUB_CATEGORY_PARENT = "parentCategoryId";
    public static final String SUB_CATEGORY_SHORT_DESC = "shortDesc";
    public static final String SUB_EXHI_CAT_GROUP_ID = "exhi_cat_group_id";

    //TABLE_EXHIBITOR_TYPES Fields
    public static final String EXHIBITOR_TYPE_EVENTID = "event_id";
    public static final String EXHIBITOR_TYPE_ID = "exhibitorId";
    public static final String EXHIBITOR_TYPE_NAME = "exhibitorType";
    public static final String EXHIBITOR_TYPE_COLOR = "exhibitorTypeColor";


    public static final String EXHIBITOR_PARENT_GROUP_EVENTID = "parentGroup_event_id";
    public static final String EXHIBITOR_PARENT_GROUP_GroupId = "parentGroup_id";
    public static final String EXHIBITOR_PARENT_GROUP_GroupName = "parentGroup_name";
    public static final String EXHIBITOR_PARENT_GROUP_ParentCategoryId = "parentGroup_categoryId";

    //TABLE_EXHIBITOR_COUNTRYLIST Fields
    public static final String EXHIBITOR_COUNTRY_EVENTID = "event_id";
    public static final String EXHIBITOR_COUNTRY_ID = "countryId";
    public static final String EXHIBITOR_COUNTRY_NAME = "countryName";

    //TABLE_EXHIBITOR_PRODUCTCAT_LIST Fields
    public static final String EXHIBITOR_PRODUCTCAT_EVENTID = "event_id";
    public static final String EXHIBITOR_PRODUCTCAT_ID = "productId";
    public static final String EXHIBITOR_PRODUCTCAT_NAME = "productName";

    //TABLE_EXHIBITOR_LIST Fields
    public static final String EXHIBITOR_LIST_EVENTID = "event_id";
    public static final String EXHIBITOR_LIST_ID = "exhibitorId";
    public static final String EXHIBITOR_LIST_PAGEID = "exhibitorPageId";
    public static final String EXHIBITOR_LIST_USERID = "userId";
    public static final String EXHIBITOR_LIST_HEADING = "heading";
    public static final String EXHIBITOR_LIST_SATNDNUMBER = "standNo";
    public static final String EXHIBITOR_LIST_FAVOURITE = "isFavourite";
    public static final String EXHIBITOR_LIST_LOGO = "logo";
    public static final String EXHIBITOR_LIST_SHORTDESC = "shortDesc";
    public static final String EXHIBITOR_LIST_CATEGORYID = "productCategoryId";
    public static final String EXHIBITOR_LIST_COUNTRYID = "countryId";
    public static final String EXHIBITOR_LIST_PARENTID = "parentCategoryId";
    public static final String EXHIBITOR_LIST_TYPEID = "exhibitorTypeId";
    public static final String EXHIBITOR_LIST_SPONSOR_CATEGORY_ID = "exhibitorSponsorCaregoryId";


    // TABLE_SPONSOR_LIST Fields

    public static final String SPONSOR_LIST_EVENTID = "event_id";
    public static final String SPONSOR_LIST_ID = "sponsorId";
    public static final String SPONSOR_LIST_NAME = "sponsorName";
    public static final String SPONSOR_LIST_USERID = "userId";
    public static final String SPONSOR_LIST_COMAPANYNAME = "companyName";
    public static final String SPONSOR_LIST_TYPEID = "typeId";
    public static final String SPONSOR_LIST_FAVOURITE = "isFavourite";
    public static final String SPONSOR_LIST_LOGO = "logo";

    // TABLE_SPONSOR_TYPE_LIST Fields

    public static final String SPONSOR_TYPE_EVENTID = "event_id";
    public static final String SPONSOR_TYPE_ID = "sponsorId";
    public static final String SPONSOR_TYPE_NAME = "sponsorType";
    public static final String SPONSOR_TYPE_POSITION = "sponsorPosition";
    public static final String SPONSOR_TYPE_COLOR = "sponsorTypeColor";

    // TABLE_SPEAKER_TYPE_LIST Fields

    public static final String Speaker_TYPE_EVENTID = "event_id";
    public static final String Speaker_TYPE_ID = "speakerId";
    public static final String Speaker_TYPE_NAME = "speakerType";
    public static final String Speaker_TYPE_POSITION = "speakerPosition";
    public static final String Speaker_TYPE_COLOR = "speakerTypeColor";


    // TABLE_SPEAKER_LIST Fields

    public static final String SPEAKER_LIST_EVENTID = "event_id";
    public static final String SPEAKER_LIST_USERID = "userId";
    public static final String SPEAKER_LIST_ID = "speakerId";
    public static final String SPEAKER_LIST_FIRST_NAME = "speakerFirstName";
    public static final String SPEAKER_LIST_LAST_NAME = "speakerLastName";
    public static final String SPEAKER_LIST_COMAPANYNAME = "companyName";
    public static final String SPEAKER_LIST_TITLE = "title";
    public static final String SPEAKER_LIST_SPEAKER_DESC = "Speaker_desc";
    public static final String SPEAKER_LIST_SPEAKER_EMAIL = "Email";
    public static final String SPEAKER_LIST_SPEAKER_HEADING = "Heading";
    public static final String SPEAKER_LIST_FAVOURITE = "isFavourite";
    public static final String SPEAKER_LIST_LOGO = "logo";
    public static final String SPEAKER_LIST_TYPEID = "typeId";


    public static final String Activity_eventId = "event_id";
    public static final String Activity_userId = "userId";
    public static final String Activity_eventType = "eventType";
    public static final String Activity_activityData = "activityData";


    // Field For EventList
    public static final String Event_Id = "event_id";
    public static final String Event_NAME = "event_name";
    public static final String Fblogin_Enabled = "facebook_login";
    public static final String Event_Type = "event_type";
    public static final String Event_Logo = "logo_images";
    public static final String Fund_Enabled = "fundraising_enbled";
    public static final String EventListmultiLanguage = "multiLanguage";
    public static final String EventListshow_login_screen = "show_login_screen";


    // Field For EventHomeData
    public static final String Home_EventId = "home_EventId";
    public static final String Home_EventData = "home_EventDatas";


    // Field For AgendaData

    public static final String agenda_EventId = "agenda_EventId";
    public static final String agenda_kind = "agenda_kind";
    public static final String agenda_type = "agenda_type";
    public static final String agenda_Data = "agenda_Data";
    public static final String agenda_CategoryIdData = "agenda_CategoryIdData";
    public static final String user_id = "user_id";

    // Field For Presantation

    public static final String presan_EventId = "presan_EventId";
    public static final String presan_type = "presan_type";
    public static final String presan_Data = "presan_Data";
    public static final String presan_userId = "presan_userId";


    // Field For  DetailAgendaData
    public static final String Detailagenda_Data = "detail_agenda";
    public static final String DetailAgenda_user_id = "user_id";
    public static final String DetailAgenda_Event_id = "detailAgenda_eventId";
    public static final String Agenda_id = "detail_agendaId";


    // Field for  AgendaPendingRequest

    public static final String PendingRequestId = "PendingRequestId";
    public static final String PendingRequesturl = "PendingRequestUrl";
    public static final String PendingRequestAgendaId = "PendingRequestAgendaId";
    public static final String PendingRequestEventId = "PendingRequestEventId";
    public static final String PendingRequestToken = "PendingRequestToken";
    public static final String PendingRequestUserId = "PendingRequestUserId";
    public static final String PendingRequestActiveStatus = "PendingRequestActiveStatus";

    // Field For ActivityPendingRequest
    public static final String PendingRequestMessageId = "PendingRequestId";
    public static final String PendingRequestMessageurl = "PendingRequestUrl";
    public static final String PendingRequestMessageImageurl = "PendingRequestImageUrl";
    public static final String PendingRequestMessageEventId = "PendingRequestEventId";
    public static final String PendingRequestMessageToken = "PendingRequestToken";
    public static final String PendingRequestMessageUserId = "PendingRequestUserId";
    public static final String PendingRequestMessage = "PendingRequestMessage";
    public static final String PendingRequestImageArray = "PendingRequestImageArray";


    // Field For Listing Data

    public static final String ListingEventId = "ListingEventId";
    public static final String ListingTag = "ListingTag";
    public static final String ListingUserId = "ListingUserId";
    public static final String ListingData = "ListingData";


    public static final String NotesEventId = "NotesEventId";
    public static final String NotesUserId = "NotesUserId";
    public static final String NotesData = "NotesData";


    public static final String CmsEventId = "CmsEventId";
    public static final String CmsId = "CmsId";
    public static final String CmsData = "CmsData";


    // Field For detail OF ExhibitorDetail
    public static final String ExhibitorDetail_eventId = "Exhibitordetail_event_id";
    public static final String ExhibitorDetail_userId = "Exhibitordetail_userId";
    public static final String ExhibitorDetail_eventType = "Exhibitordetail_eventType";
    public static final String ExhibitorDetail_Data = "Exhibitordetail_Data";
    public static final String ExhibitorDetail_ExId = "Exhibitordetail_ExId";
    public static final String ExhibitorDetail_ExPageId = "Exhibitordetail_PageExId";


    // Field Gor detail SponsorDetail
    public static final String sponsorDetail_eventId = "sponsorDetail_event_id";
    public static final String sponsorDetail_userId = "sponsorDetail_userId";
    public static final String sponsorDetail_eventType = "sponsorDetail_eventType";
    public static final String sponsorDetail_SpId = "sponsorDetail_SpId";
    public static final String sponsorDetail_Data = "sponsorDetail_Data";

    // Field Gor detail AttendeeDetail
    public static final String AtteendeeSpeakerDetail_eventId = "AtteendeeSpeakerDetail_event_id";
    public static final String AtteendeeSpeakerDetail_userId = "AtteendeeSpeakerDetail_userId";
    public static final String AtteendeeSpeakerDetail_eventType = "AtteendeeSpeakerDetail_eventType";
    public static final String AtteendeeSpeakerDetail_Id = "AtteendeeSpeakerDetail_Id";
    public static final String AtteendeeSpeakerDetail_Data = "AtteendeeSpeakerDetail_Data";
    public static final String AtteendeeSpeakerDetail_tag = "AtteendeeSpeakerDetail_tag";


    // Field For Presantation Detail Image

    public static final String PresantationEventId = "PresantationEventId";
    public static final String PresantationUserId = "PresantationUserId";
    public static final String PresantationPresantationId = "PresantationPresantationId";
    public static final String PresantationKind = "PresantationKind";
    public static final String PresantationImageName = "PresantationImageName";
    public static final String PresantationImagePath = "PresantationImagePath";


    // Insert PresantationDetail Id
    public static final String PDetailEventId = "PDetailEventId";
    public static final String PDetailPresantationId = "PDetailPresantationId";


    // Table For TABLE_ExhiLeadSurveyUpload


    // Field For ExhiLeadSurveyUpload

    public static final String ExiLead_EventId = "ExiLead_EventId";
    public static final String ExiLead_userId = "ExiLead_userId";
    public static final String ExiLead_scanId = "ExiLead_scanId";
    public static final String ExiLead_scanData = "ExiLead_scanData";
    public static final String ExiLead_uploadData = "ExiLead_uploadData";
    public static final String ExiLead_LeadSurveyData = "ExiLead_LeadSurveyData";
    public static final String ExiLead_TimeData = "ExiLead_TimeData";


    public static final String MyExiLead_EventId = "MyExiLead_EventId";
    public static final String MyExiLead_userId = "MyExiLead_userId";
    public static final String MyExiLead_leadId = "MyExiLead_leadId";
    public static final String MyExiLead_badgeNumber = "MyExiLead_badgeNumber";
    public static final String MyExiLead_Firstname = "MyExiLead_Firstname";
    public static final String MyExiLead_Lastname = "MyExiLead_Lastname";
    public static final String MyExiLead_Email = "MyExiLead_Email";
    public static final String MyExiLead_Title = "MyExiLead_Title";
    public static final String MyExiLead_Company_name = "MyExiLead_Company_name";
    public static final String MyExiLead_data = "MyExiLead_data";

    // Field for Tmp table

    public static final String tmp_ValueId = "tmp_ValueId";
    public static final String tmp_ValueName = "tmp_ValueName";
    public static final String tmp_Data = "tmp_Data";

    public static final String SUPERGROUP_ID = "super_id";
    public static final String SUPERGROUP_MENUID = "super_menuId";
    public static final String SUPERGROUP_EVENTID = "super_Eventid";
    public static final String SUPERGROUP_GROUP_NAME = "super_groupName";
    public static final String SUPERGROUP_GROUP_IMAGE = "super_groupImage";
    public static final String SUPERGROUP_GROUP_CREATEDATE = "super_createDate";
    public static final String SUPERGROUP_GROUP_UPDATE_DATE = "super_UpdateDate";
    public static final String SUPERGROUP_GROUP_SORT_ORDERNO = "super_group_sort_orderno";


    public static final String SUPERGROUP_RELATION_ID = "super_id";
    public static final String SUPERGROUP_RELATION_MENUID = "super_menuId";
    public static final String SUPERGROUP_RELATION_SUPERGROUP_ID = "relation_superGroup_id";
    public static final String SUPERGROUP_RELATION_CHILDGROUP_ID = "relation_childGroup_id";
    public static final String SUPERGROUP_RELATION_EVENTID = "super_relation_Eventid";


    public static final String CMSLISTING_ID = "cmsId";
    public static final String CMSLISTING_Menu_name = "CmsMenuName";
    public static final String CMSLISTING_cms_icon = "CmsIcon";
    public static final String CMSLISTING_group_id = "cmsGroupId";
    public static final String CMSLISTING_Eventid = "cmsEventId";


    // --------------------------------------------------------------//


    Context context;
    SessionManager sessionManager;

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 16-1-2018 Changes //

        String CREATE_TABLE_SUPERGROUP_TABLE = "CREATE TABLE " + TABLE_SUPERGROUP_LIST
                + "(" + SUPERGROUP_ID + " TEXT NOT NULL DEFAULT '',"
                + SUPERGROUP_MENUID + " TEXT NOT NULL DEFAULT '',"
                + SUPERGROUP_EVENTID + " TEXT NOT NULL DEFAULT '',"
                + SUPERGROUP_GROUP_NAME + " TEXT NOT NULL DEFAULT '',"
                + SUPERGROUP_GROUP_IMAGE + " TEXT NOT NULL DEFAULT '',"
                + SUPERGROUP_GROUP_CREATEDATE + " TEXT NOT NULL DEFAULT '',"
                + SUPERGROUP_GROUP_SORT_ORDERNO + " TEXT NOT NULL DEFAULT '',"
                + SUPERGROUP_GROUP_UPDATE_DATE + " TEXT NOT NULL DEFAULT '' " + ")";


        String CREATE_TABLE_SUPERGROUP_RELATION_TABLE = "CREATE TABLE " + TABLE_SUPERGROUP_RELATION
                + "(" + SUPERGROUP_RELATION_ID + " TEXT NOT NULL DEFAULT '',"
                + SUPERGROUP_RELATION_MENUID + " TEXT NOT NULL DEFAULT '',"
                + SUPERGROUP_RELATION_SUPERGROUP_ID + " TEXT NOT NULL DEFAULT '',"
                + SUPERGROUP_RELATION_EVENTID + " TEXT NOT NULL DEFAULT '',"
                + SUPERGROUP_RELATION_CHILDGROUP_ID + " TEXT NOT NULL DEFAULT '' " + ")";

        String CREATE_TABLE_CMS_LISTING_offline = "CREATE TABLE " + TABLE_CMSLISTINGOffline
                + "(" + CMSLISTING_ID + " TEXT NOT NULL DEFAULT '',"
                + CMSLISTING_Eventid + " TEXT NOT NULL DEFAULT '',"
                + CMSLISTING_group_id + " TEXT NOT NULL DEFAULT '',"
                + CMSLISTING_Menu_name + " TEXT NOT NULL DEFAULT '',"
                + CMSLISTING_cms_icon + " TEXT NOT NULL DEFAULT '' " + ")";

        //----------------------------//////////
        String CREATE_EVENT_TmpValue = "CREATE TABLE " + TABLE_tmp
                + "(" + tmp_ValueId + " TEXT,"
                + tmp_Data + " TEXT,"
                + tmp_ValueName + " TEXT " + ")";

        String CREATE_Presantation_Detail_TABLE = "CREATE TABLE " + TABLE_Presantation_DetailImage
                + "(" + PresantationEventId + " TEXT,"
                + PresantationUserId + " TEXT,"
                + PresantationPresantationId + " TEXT,"
                + PresantationKind + " TEXT,"
                + PresantationImageName + " TEXT,"
                + PresantationImagePath + " TEXT " + ")";


        String CREATE_TABLE_LeadExhibitorOffline = "CREATE TABLE " + TABLE_LeadExhibitorOffline
                + "(" + MyExiLead_EventId + " TEXT NOT NULL DEFAULT '',"
                + MyExiLead_userId + " TEXT NOT NULL DEFAULT '',"
                + MyExiLead_leadId + " TEXT NOT NULL DEFAULT '',"
                + MyExiLead_badgeNumber + " TEXT NOT NULL DEFAULT '',"
                + MyExiLead_Firstname + " TEXT NOT NULL DEFAULT '',"
                + MyExiLead_Lastname + " TEXT NOT NULL DEFAULT '',"
                + MyExiLead_Email + " TEXT NOT NULL DEFAULT '',"
                + MyExiLead_Title + " TEXT NOT NULL DEFAULT '',"
                + MyExiLead_Company_name + " TEXT NOT NULL DEFAULT '',"
                + MyExiLead_data + " TEXT NOT NULL DEFAULT '' " + ")";


        String CREATE_ExhiLeadSurveyUploadTable = "CREATE TABLE " + TABLE_ExhiLeadSurveyUpload
                + "(" + ExiLead_EventId + " TEXT,"
                + ExiLead_userId + " TEXT,"
                + ExiLead_scanId + " TEXT,"
                + ExiLead_scanData + " TEXT,"
                + ExiLead_uploadData + " TEXT,"
                + ExiLead_TimeData + " TEXT,"
                + ExiLead_LeadSurveyData + " TEXT " + ")";


        String CREATE_PDetailId_TABLE = "CREATE TABLE " + TABLE_PDetailId
                + "(" + PDetailEventId + " TEXT,"
                + PDetailPresantationId + " TEXT " + ")";

        String CREATE_EVENT_LIST_TABLE = "CREATE TABLE " + TABLE_EventList
                + "(" + Event_Id + " TEXT,"
                + Event_NAME + " TEXT,"
                + Fblogin_Enabled + " TEXT,"
                + Event_Type + " TEXT,"
                + Event_Logo + " TEXT,"
                + EventListmultiLanguage + " TEXT,"
                + EventListshow_login_screen + " TEXT,"
                + Fund_Enabled + " TEXT " + ")";


        String CREATE_EVENT_HOME_DATA_TABLE = "CREATE TABLE " + TABLE_EventHomeData
                + "(" + Home_EventId + " TEXT,"
                + Home_EventData + " TEXT " + ")";

        String CREATE_EVENT_AGENDA_DATA_TABLE = "CREATE TABLE " + TABLE_Agenda
                + "(" + agenda_EventId + " TEXT,"
                + agenda_kind + " TEXT,"
                + agenda_type + " TEXT,"
                + agenda_Data + " TEXT,"
                + agenda_CategoryIdData + " TEXT,"
                + user_id + " TEXT " + ")";

        String CREATE_EVENT_DETAIL_AGENDA_DATA_TABLE = "CREATE TABLE " + TABLE_DetailAgenda
                + "(" + DetailAgenda_Event_id + " TEXT,"
                + DetailAgenda_user_id + " TEXT,"
                + Agenda_id + " TEXT,"
                + Detailagenda_Data + " TEXT " + ")";


        String CREATE_EVENT_PENDING_REQUEST_DATA_TABLE = "CREATE TABLE " + TABLE_PendingRequest
                + "(" + PendingRequestId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PendingRequesturl + " TEXT,"
                + PendingRequestEventId + " TEXT,"
                + PendingRequestToken + " TEXT,"
                + PendingRequestUserId + " TEXT,"
                + PendingRequestAgendaId + " TEXT,"
                + PendingRequestActiveStatus + " TEXT " + ")";

        String CREATE_EVENT_ACTIVITY_LIST = "CREATE TABLE " + TABLE_Activity
                + "(" + Activity_eventId + " TEXT,"
                + Activity_userId + " TEXT,"
                + Activity_eventType + " TEXT,"
                + Activity_activityData + " TEXT " + ")";


        String CREATE_EVENT_PENDING_MESSGAE_REQUEST = "CREATE TABLE " + TABLE_PendingActivity_request
                + "(" + PendingRequestMessageId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PendingRequestMessageEventId + " TEXT,"
                + PendingRequestMessageUserId + " TEXT,"
                + PendingRequestMessageToken + " TEXT,"
                + PendingRequestMessageurl + " TEXT,"
                + PendingRequestMessageImageurl + " TEXT,"
                + PendingRequestMessage + " TEXT,"
                + PendingRequestImageArray + " TEXT " + ")";


        String CREATE_EVENT_ATTENDEE_LIST = "CREATE TABLE " + Table_Attendeelisting
                + "(" + ListingEventId + " TEXT,"
                + ListingUserId + " TEXT,"
                + ListingData + " TEXT,"
                + ListingTag + " TEXT " + ")";


        String CREATE_EVENT_Notes_LIST = "CREATE TABLE " + Table_notesListing
                + "(" + NotesEventId + " TEXT,"
                + NotesUserId + " TEXT,"
                + NotesData + " TEXT " + ")";


        String CREATE_EVENT_CMS_PAGE = "CREATE TABLE " + Table_CMSListing
                + "(" + CmsEventId + " TEXT,"
                + CmsId + " TEXT,"
                + CmsData + " TEXT " + ")";


        String CREATE_EVENT_PRESANTATION_LIST = "CREATE TABLE " + TABLE_Presantation
                + "(" + presan_EventId + " TEXT,"
                + presan_userId + " TEXT,"
                + presan_Data + " TEXT,"
                + presan_type + " TEXT " + ")";


        String CREATE_Exhibitor_Detail_TABLE = "CREATE TABLE " + Table_ExhibitorDetail
                + "(" + ExhibitorDetail_eventId + " TEXT,"
                + ExhibitorDetail_userId + " TEXT,"
                + ExhibitorDetail_eventType + " TEXT,"
                + ExhibitorDetail_Data + " TEXT,"
                + ExhibitorDetail_ExId + " TEXT,"
                + ExhibitorDetail_ExPageId + " TEXT " + ")";


        String CREATE_Sponsor_Detail_TABLE = "CREATE TABLE " + Table_SponsorDetail
                + "(" + sponsorDetail_eventId + " TEXT,"
                + sponsorDetail_userId + " TEXT,"
                + sponsorDetail_eventType + " TEXT,"
                + sponsorDetail_SpId + " TEXT,"
                + sponsorDetail_Data + " TEXT " + ")";


        String CREATE_AttdeeSpeaker_Detail_TABLE = "CREATE TABLE " + Table_AtteendeeSpeakerDetail
                + "(" + AtteendeeSpeakerDetail_eventId + " TEXT,"
                + AtteendeeSpeakerDetail_userId + " TEXT,"
                + AtteendeeSpeakerDetail_eventType + " TEXT,"
                + AtteendeeSpeakerDetail_Id + " TEXT,"
                + AtteendeeSpeakerDetail_tag + " TEXT,"
                + AtteendeeSpeakerDetail_Data + " TEXT " + ")";


        String CREATE_Advertiesment_TABLE = "CREATE TABLE " + TABLE_advertiesment
                + "(" + adaverties_event_id + " TEXT,"
                + adverties_menuID + " TEXT,"
                + adverties_Data + " TEXT " + ")";


        String CREATE_TABLE_UPDATE_MODULE = "CREATE TABLE " + TABLE_UPDATE_MODULE
                + "(" + Update_MODULE_ID + " TEXT,"
                + Update_MODULE_NAME + " TEXT,"
                + UPDATE_MODULE_EVENTID + " TEXT,"
                + Update_DATE + " TEXT " + ")";

        String CREATE_TABLE_COMMON_GROUP_RELATION = "CREATE TABLE " + TABLE_GROUP_RELATION
                + "(" + GROUP_RELATION_ID + " TEXT,"
                + GROUP_RELATION_EVENTID + " TEXT,"
                + GROUP_RELATION_MENU_ID + " TEXT,"
                + GROUP_RELATION_MODULEID + " TEXT " + ")";


        String CREATE_TABLE_COMMON_GROUP = "CREATE TABLE " + TABLE_GROUP
                + "(" + GROUP_ID + " TEXT,"
                + GROUP_EVENTID + " TEXT,"
                + GROUP_MENU_ID + " TEXT,"
                + GROUP_NAME + " TEXT,"
                + GROUP_SORT_ORDER_NO + " TEXT,"
                + GROUP_ICON + " TEXT " + ")";


        String CREATE_TABLE_MAP_LIST = "CREATE TABLE " + TABLE_MAP_List
                + "(" + MAP_LIST_MODULEID + " TEXT,"
                + MAP_LIST_ADDRESS + " TEXT,"
                + MAP_LIST_EventId + " TEXT,"
                + MAP_LIST_TITLE + " TEXT,"
                + MAP_LIST_CHECK_DWGFILES + " TEXT,"
                + MAP_LIST_FLOOR_PLAN_ICON + " TEXT,"
                + MAP_LIST_GOOGLE_MAP_ICON + " TEXT,"
                + MAP_LIST_IMAGES + " TEXT,"
                + MAP_LIST_INCLUDEMAP + " TEXT,"
                + MAP_LIST_LAT + " TEXT,"
                + MAP_LIST_LAT_LONG + " TEXT,"
                + MAP_LIST_LONG + " TEXT " + ")";


        String CREATE_TABLE_SPEAKER_LIST = "CREATE TABLE " + TABLE_SPEAKER_LIST
                + "(" + SPEAKER_LIST_EVENTID + " TEXT,"
                + SPEAKER_LIST_COMAPANYNAME + " TEXT,"
                + SPEAKER_LIST_FAVOURITE + " TEXT,"
                + SPEAKER_LIST_FIRST_NAME + " TEXT,"
                + SPEAKER_LIST_ID + " TEXT,"
                + SPEAKER_LIST_LAST_NAME + " TEXT,"
                + SPEAKER_LIST_LOGO + " TEXT,"
                + SPEAKER_LIST_SPEAKER_DESC + " TEXT,"
                + SPEAKER_LIST_SPEAKER_EMAIL + " TEXT,"
                + SPEAKER_LIST_SPEAKER_HEADING + " TEXT,"
                + SPEAKER_LIST_USERID + " TEXT,"
                + SPEAKER_LIST_TYPEID + " TEXT NOT NULL DEFAULT '',"
                + SPEAKER_LIST_TITLE + " TEXT " + ")";


        String CREATE_TABLE_EXHIBITOR_PARENT_CATEGORY = "CREATE TABLE " + TABLE_EXHIBITOR_PARENT_CATEGORY
                + "(" + PARENT_CATEGORY_EVENTID + " TEXT,"
                + PARENT_CATEGORY_ID + " TEXT,"
                + PARENT_CATEGORY_NAME + " TEXT,"
                + PARENT_CATEGORY_SORT_ORDERNO + " TEXT,"
                + PARENT_CATEGORY_ICON + " TEXT " + ")";

        String CREATE_TABLE_EXHIBITOR_SUB_CATEGORY = "CREATE TABLE " + TABLE_EXHIBITOR_SUB_CATEGORY
                + "(" + SUB_CATEGORY_EVENTID + " TEXT,"
                + SUB_CATEGORY_ID + " TEXT,"
                + SUB_CATEGORY_NAME + " TEXT,"
                + SUB_CATEGORY_SHORT_DESC + " TEXT,"
                + SUB_CATEGORY_ICON + " TEXT,"
                + SUB_EXHI_CAT_GROUP_ID + " TEXT,"
                + SUB_CATEGORY_PARENT + " TEXT " + ")";

        String CREATE_TABLE_EXHIBITOR_PRODUCTCAT_LIST = "CREATE TABLE " + TABLE_EXHIBITOR_PRODUCTCAT_LIST
                + "(" + EXHIBITOR_PRODUCTCAT_EVENTID + " TEXT,"
                + EXHIBITOR_PRODUCTCAT_ID + " TEXT,"
                + EXHIBITOR_PRODUCTCAT_NAME + " TEXT " + ")";


        String CREATE_TABLE_EXHIBITOR_PRODUCTCAT_GROUP_LIST = "CREATE TABLE " + TABLE_EXHIBITOR_PRODUCTCAT_GROUP_LIST
                + "(" + EXHIBITOR_PARENT_GROUP_EVENTID + " TEXT,"
                + EXHIBITOR_PARENT_GROUP_GroupId + " TEXT,"
                + EXHIBITOR_PARENT_GROUP_GroupName + " TEXT,"
                + EXHIBITOR_PARENT_GROUP_ParentCategoryId + " TEXT " + ")";


        String CREATE_TABLE_EXHIBITOR_TYPES = "CREATE TABLE " + TABLE_EXHIBITOR_TYPES
                + "(" + EXHIBITOR_TYPE_EVENTID + " TEXT,"
                + EXHIBITOR_TYPE_ID + " TEXT NOT NULL DEFAULT '',"
                + EXHIBITOR_TYPE_NAME + " TEXT,"
                + EXHIBITOR_TYPE_COLOR + " TEXT " + ")";

        String CREATE_TABLE_EXHIBITOR_COUNTRYLIST = "CREATE TABLE " + TABLE_EXHIBITOR_COUNTRYLIST
                + "(" + EXHIBITOR_COUNTRY_EVENTID + " TEXT,"
                + EXHIBITOR_COUNTRY_ID + " TEXT,"
                + EXHIBITOR_COUNTRY_NAME + " TEXT " + ")";

        String CREATE_TABLE_EXHIBITOR_LIST = "CREATE TABLE " + TABLE_EXHIBITOR_LIST
                + "(" + EXHIBITOR_LIST_EVENTID + " TEXT,"
                + EXHIBITOR_LIST_ID + " TEXT,"
                + EXHIBITOR_LIST_HEADING + " TEXT,"
                + EXHIBITOR_LIST_SATNDNUMBER + " TEXT,"
                + EXHIBITOR_LIST_FAVOURITE + " TEXT,"
                + EXHIBITOR_LIST_LOGO + " TEXT,"
                + EXHIBITOR_LIST_USERID + " TEXT,"
                + EXHIBITOR_LIST_CATEGORYID + " TEXT,"
                + EXHIBITOR_LIST_COUNTRYID + " TEXT,"
                + EXHIBITOR_LIST_PARENTID + " TEXT,"
                + EXHIBITOR_LIST_PAGEID + " TEXT,"
                + EXHIBITOR_LIST_SPONSOR_CATEGORY_ID + " TEXT NOT NULL DEFAULT '',"
                + EXHIBITOR_LIST_TYPEID + " TEXT NOT NULL DEFAULT '',"
                + EXHIBITOR_LIST_SHORTDESC + " TEXT " + ")";

        // Table For Sponsor


        String CREATE_TABLE_SPONSOR_TYPES = "CREATE TABLE " + TABLE_SPONSOR_LIST_TYPE
                + "(" + SPONSOR_TYPE_EVENTID + " TEXT,"
                + SPONSOR_TYPE_ID + " TEXT NOT NULL DEFAULT '',"
                + SPONSOR_TYPE_NAME + " TEXT,"
                + SPONSOR_TYPE_POSITION + " TEXT,"
                + SPONSOR_TYPE_COLOR + " TEXT " + ")";

        String CREATE_TABLE_SPEAKER_TYPES = "CREATE TABLE " + TABLE_SPEAKER_LIST_TYPE
                + "(" + Speaker_TYPE_EVENTID + " TEXT,"
                + Speaker_TYPE_ID + " TEXT NOT NULL DEFAULT '',"
                + Speaker_TYPE_NAME + " TEXT,"
                + Speaker_TYPE_POSITION + " TEXT,"
                + Speaker_TYPE_COLOR + " TEXT " + ")";


        String CREATE_TABLE_SPONSOR_LIST = "CREATE TABLE " + TABLE_SPONSOR_LIST
                + "(" + SPONSOR_LIST_EVENTID + " TEXT,"
                + SPONSOR_LIST_USERID + " TEXT,"
                + SPONSOR_LIST_COMAPANYNAME + " TEXT,"
                + SPONSOR_LIST_FAVOURITE + " TEXT,"
                + SPONSOR_LIST_LOGO + " TEXT,"
                + SPONSOR_LIST_ID + " TEXT,"
                + SPONSOR_LIST_TYPEID + " TEXT NOT NULL DEFAULT '',"
                + SPONSOR_LIST_NAME + " TEXT " + ")";


        String CREATE_TABLE_MATHCMAKING_LIST = "CREATE TABLE " + TABLE_MATCHMAKING_List
                + "(" + MATCHMAKING_EVENTID + " TEXT,"
                + MATCHMAKING_USERID + " TEXT,"
                + MATCHMAKING_TITLE + " TEXT,"
                + MATCHMAKING_SUBTITLE + " TEXT,"
                + MATCHMAKING_LOGO + " TEXT,"
                + MATCHMAKING_ID + " TEXT,"
                + MATCHMAKING_EXHIPAGEID + " TEXT,"
                + MATCHMAKING_TYPEID + " TEXT,"
                + MATCHMAKING_TYPENAME + " TEXT " + ")";

        String CREATE_TABLE_MATHCMAKING_MODULE_LIST = "CREATE TABLE " + TABLE_MATCHMAKING_MODULENAME_List
                + "(" + MATCHMAKING_MODULE_USERID + " TEXT,"
                + MATCHMAKING_MODULE_EVENTID + " TEXT,"
                + MATCHMAKING_MODULE_ID + " TEXT,"
                + MATCHMAKING_MODULE_NAME + " TEXT " + ")";


        String CREATE_TABLE_AGENDA_CATEGORYLIST = "CREATE TABLE " + TABLE_AGENDA_CATEGORYLIST
                + "(" + AGENDA_CATEGORY_ID + " TEXT,"
                + AGENDA_CATEGORY_EVENTID + " TEXT,"
                + AGENDA_CATEGORY_IMAGE + " TEXT,"
                + AGENDA_CATEGORY_NAME + " TEXT,"
                + AGENDA_CATEGORY_SortOrder + " TEXT,"
                + AGENDA_CATEGORY_WELCOME_SCREEN + " TEXT,"
                + AGENDA_CATEGORY_TYPE + " TEXT " + ")";


        String CREATE_TABLE_AGENDA_CATEGORY_RELATION = "CREATE TABLE " + TABLE_AGENDA_CATEGORY_RELATION
                + "(" + AGENDA_CAT_RELATION_EVENTID + " TEXT,"
                + AGENDA_CAT_RELATION_AGENDAID + " TEXT,"
                + AGENDA_CAT_RELATION_CATID + " TEXT " + ")";

        String CREATE_TABLE_AGENDA_TYPELIST = "CREATE TABLE " + TABLE_AGENDA_TYPELIST
                + "(" + AGENDA_TYPELIST_EVENTID + " TEXT,"
                + AGENDA_TYPELIST_TYPEID + " TEXT,"
                + AGENDA_TYPELIST_TYPENAME + " TEXT,"
                + AGENDA_TYPELIST_ORDERNO + " TEXT " + ")";

        String CREATE_TABLE_AGENDA_LIST = "CREATE TABLE " + TABLE_AGENDA_LIST
                + "(" + AGENDA_EVENTID + " TEXT,"
                + AGENDA_AGENDAID + " TEXT,"
                + AGENDA_HEADING + " TEXT,"
                + AGENDA_SPEAKER + " TEXT,"
                + AGENDA_LOCATION + " TEXT,"
                + AGENDA_SESSION_IMAGE + " TEXT,"
                + AGENDA_STARTDATE + " TEXT,"
                + AGENDA_ENDDATE + " TEXT,"
                + AGENDA_STARTTIME + " TEXT,"
                + AGENDA_ENDTIME + " TEXT,"
                + AGENDA_TYPENAME + " TEXT,"
                + AGENDA_SORT_ORDER + " TEXT,"
                + AGENDA_SESSION_TRACK + " TEXT,"
                + AGENDA_TYPEID + " TEXT"
                + ")";

        //============================================


        db.execSQL(CREATE_EVENT_PRESANTATION_LIST);
        db.execSQL(CREATE_EVENT_CMS_PAGE);
        db.execSQL(CREATE_EVENT_Notes_LIST);
        db.execSQL(CREATE_EVENT_ATTENDEE_LIST);
        db.execSQL(CREATE_EVENT_LIST_TABLE);
        db.execSQL(CREATE_EVENT_HOME_DATA_TABLE);
        db.execSQL(CREATE_EVENT_AGENDA_DATA_TABLE);
        db.execSQL(CREATE_EVENT_DETAIL_AGENDA_DATA_TABLE);
        db.execSQL(CREATE_EVENT_PENDING_REQUEST_DATA_TABLE);
        db.execSQL(CREATE_EVENT_ACTIVITY_LIST);
        db.execSQL(CREATE_EVENT_PENDING_MESSGAE_REQUEST);
        db.execSQL(CREATE_Exhibitor_Detail_TABLE);
        db.execSQL(CREATE_Sponsor_Detail_TABLE);
        db.execSQL(CREATE_AttdeeSpeaker_Detail_TABLE);
        db.execSQL(CREATE_Presantation_Detail_TABLE);
        db.execSQL(CREATE_PDetailId_TABLE);
        db.execSQL(CREATE_ExhiLeadSurveyUploadTable);
        db.execSQL(CREATE_EVENT_TmpValue);
        //============================================
        db.execSQL(CREATE_TABLE_EXHIBITOR_PARENT_CATEGORY);
        db.execSQL(CREATE_TABLE_EXHIBITOR_SUB_CATEGORY);
        db.execSQL(CREATE_TABLE_EXHIBITOR_PRODUCTCAT_LIST);
        db.execSQL(CREATE_TABLE_EXHIBITOR_PRODUCTCAT_GROUP_LIST);
        db.execSQL(CREATE_TABLE_EXHIBITOR_COUNTRYLIST);
        db.execSQL(CREATE_TABLE_EXHIBITOR_TYPES);
        db.execSQL(CREATE_TABLE_EXHIBITOR_LIST);
        db.execSQL(CREATE_Advertiesment_TABLE);
        db.execSQL(CREATE_TABLE_COMMON_GROUP);
        db.execSQL(CREATE_TABLE_COMMON_GROUP_RELATION);
        db.execSQL(CREATE_TABLE_MAP_LIST);
        db.execSQL(CREATE_TABLE_UPDATE_MODULE);
        db.execSQL(CREATE_TABLE_SPONSOR_TYPES);
        db.execSQL(CREATE_TABLE_SPONSOR_LIST);
        db.execSQL(CREATE_TABLE_MATHCMAKING_LIST);
        db.execSQL(CREATE_TABLE_MATHCMAKING_MODULE_LIST);
        db.execSQL(CREATE_TABLE_AGENDA_CATEGORYLIST);
        db.execSQL(CREATE_TABLE_AGENDA_CATEGORY_RELATION);
        db.execSQL(CREATE_TABLE_AGENDA_TYPELIST);
        db.execSQL(CREATE_TABLE_AGENDA_LIST);
        db.execSQL(CREATE_TABLE_SPEAKER_LIST);
        db.execSQL(CREATE_TABLE_SUPERGROUP_RELATION_TABLE);
        db.execSQL(CREATE_TABLE_SUPERGROUP_TABLE);
        db.execSQL(CREATE_TABLE_CMS_LISTING_offline);
        db.execSQL(CREATE_TABLE_LeadExhibitorOffline);

        db.execSQL(CREATE_TABLE_SPEAKER_TYPES);


        //============================================
    }

//************************************************************8


    public boolean insertMyExiLeadData(ExhibitorLead_MyLeadData_Offline listdata, String event_Id, String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MyExiLead_EventId, event_Id);
        contentValues.put(MyExiLead_userId, user_id);
        contentValues.put(MyExiLead_leadId, listdata.getId());
        contentValues.put(MyExiLead_badgeNumber, listdata.getBadgeNumber());
        contentValues.put(MyExiLead_Firstname, listdata.getFirstname());
        contentValues.put(MyExiLead_Lastname, listdata.getLastname());
        contentValues.put(MyExiLead_Email, listdata.getEmail());
        contentValues.put(MyExiLead_Title, listdata.getTitle());
        contentValues.put(MyExiLead_Company_name, listdata.getCompanyName());
        contentValues.put(MyExiLead_data, listdata.getData());

        db.insert(TABLE_LeadExhibitorOffline, null, contentValues);

        return true;
    }


    public boolean updateMyExiLeadDataSurveyData(String eventId, String userId, String badgeNumber, String data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MyExiLead_data, data);

//        db.insert(TABLE_LeadExhibitorOffline, null, contentValues);
        int row = db.update(TABLE_LeadExhibitorOffline, contentValues, MyExiLead_EventId + " = ? AND "
                + MyExiLead_userId + " = ? AND "
                + MyExiLead_badgeNumber + " LIKE ?", new String[]{eventId, userId, badgeNumber});
        return true;
    }

    public boolean isMyExiLeadData(String Event_id, String User_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_LeadExhibitorOffline + " WHERE " + MyExiLead_EventId + " = '" + Event_id + "'"
                + " AND " + MyExiLead_userId + " = '" + User_id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public void deleteMyExiLeadData(String Event_id, String User_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "delete from " + TABLE_LeadExhibitorOffline + " WHERE " + MyExiLead_EventId + " = '" + Event_id + "'"
                + " AND " + MyExiLead_userId + " = '" + User_id + "'";
//
        db.execSQL(qry);

    }

    public void deleteScanLeadData(String Event_id, String User_id, String badgeNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "delete from " + TABLE_LeadExhibitorOffline + " WHERE " + MyExiLead_EventId + " = '" + Event_id + "'"
                + " AND " + MyExiLead_userId + " = '" + User_id + "'"
                + " AND " + MyExiLead_badgeNumber + " LIKE '" + badgeNumber + "'";
//
        db.execSQL(qry);

    }


    public boolean updateMyExiLeadData(ExhibitorLead_MyLeadData_Offline listdata, String event_Id, String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MyExiLead_EventId, event_Id);
        contentValues.put(MyExiLead_userId, user_id);
        contentValues.put(MyExiLead_leadId, listdata.getId());
        contentValues.put(MyExiLead_badgeNumber, listdata.getBadgeNumber());
        contentValues.put(MyExiLead_Firstname, listdata.getFirstname());
        contentValues.put(MyExiLead_Lastname, listdata.getLastname());
        contentValues.put(MyExiLead_Email, listdata.getEmail());
        contentValues.put(MyExiLead_Title, listdata.getTitle());
        contentValues.put(MyExiLead_Company_name, listdata.getCompanyName());
        contentValues.put(MyExiLead_data, listdata.getData());

//        db.insert(TABLE_LeadExhibitorOffline, null, contentValues);
        int row = db.update(TABLE_LeadExhibitorOffline, contentValues, MyExiLead_EventId + " = ? AND "
                + MyExiLead_badgeNumber + " LIKE ? AND "
                + MyExiLead_userId + " = ?", new String[]{event_Id, listdata.getBadgeNumber(), user_id});
        return true;
    }


    // Insert Tmp data

    public boolean insertTmpValueData(String Data, String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(tmp_ValueId, Data);
        contentValues.put(tmp_ValueName, name);
        contentValues.put(tmp_Data, name);
        int rows = db.update(TABLE_tmp, contentValues, tmp_ValueId + " = " + Data, null);
        if (rows == 0) {
            db.insert(TABLE_tmp, null, contentValues);
        }
        return true;
    }

    //  Insert ExhibitorSurveyuploadData

    public boolean insertExhiSurveyUploadData(String Event_id, String user_id, String scanid, String scanData, String uploadData, String surveyData, String date) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExiLead_EventId, Event_id);
        contentValues.put(ExiLead_userId, user_id);
        contentValues.put(ExiLead_scanId, scanid);
        contentValues.put(ExiLead_scanData, scanData);
        contentValues.put(ExiLead_uploadData, uploadData);
        contentValues.put(ExiLead_LeadSurveyData, surveyData);
        contentValues.put(ExiLead_TimeData, date);
        db.insert(TABLE_ExhiLeadSurveyUpload, null, contentValues);
        return true;
    }

    public boolean isExhiSurveyUploadDataExist(String Event_id, String user_id, String scanData) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_ExhiLeadSurveyUpload + " WHERE " + ExiLead_EventId + " = '" + Event_id
                + "' AND " + ExiLead_userId + " = '" + user_id
                + "' AND " + ExiLead_scanData + " LIKE '" + scanData + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public void deleteExhiSurveyUploadData(String Event_id, String user_id, String scanData) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "delete from " + TABLE_ExhiLeadSurveyUpload + " WHERE " + ExiLead_EventId + " = '" + Event_id
                + "' AND " + ExiLead_userId + " = '" + user_id
                + "' AND " + ExiLead_scanData + " LIKE '" + scanData + "'";


        db.execSQL(qry);

    }

    public void deleteAllExhiSurveyUploadData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "delete from " + TABLE_ExhiLeadSurveyUpload;
        String qry1 = "delete from " + TABLE_LeadExhibitorOffline;


        db.execSQL(qry);
        db.execSQL(qry1);

    }


    public boolean UpdateExhiSurveyUploadData(String Event_id, String userId, String surveyData, String scanData) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExiLead_LeadSurveyData, surveyData);
        db.update(TABLE_ExhiLeadSurveyUpload, contentValues,
                ExiLead_EventId + " = ? AND "
                        + ExiLead_userId + " = ? AND "
                        + ExiLead_scanData + " LIKE ?", new String[]{Event_id, userId, scanData});

        return true;
    }

    public Cursor getExhiSurveyUploadData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_ExhiLeadSurveyUpload, null);
        return cursor;
    }


    // Insert Attendee And Speaker Module


    public boolean insertAttdeeSpeaker_Detail(String Event_id, String user_id, String event_Type, String data, String id, String tag) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AtteendeeSpeakerDetail_eventId, Event_id);
        contentValues.put(AtteendeeSpeakerDetail_userId, user_id);
        contentValues.put(AtteendeeSpeakerDetail_eventType, event_Type);
        contentValues.put(AtteendeeSpeakerDetail_Data, data);
        contentValues.put(AtteendeeSpeakerDetail_Id, id);
        contentValues.put(AtteendeeSpeakerDetail_tag, tag);
        db.insert(Table_AtteendeeSpeakerDetail, null, contentValues);
        return true;
    }


    public void deleteAttdeeSpeaker_DetailData(String Event_id, String user_id, String event_Type, String id, String tag) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "delete from " + Table_AtteendeeSpeakerDetail + " WHERE " + AtteendeeSpeakerDetail_eventId + " = '" + Event_id
                + "' AND " + AtteendeeSpeakerDetail_userId + " = '" + user_id
                + "' AND " + AtteendeeSpeakerDetail_eventType + " = '" + event_Type
                + "' AND " + AtteendeeSpeakerDetail_Id + " = '" + id
                + "' AND " + AtteendeeSpeakerDetail_tag + " = '" + tag + "'";


        db.execSQL(qry);

    }

    public boolean isAttdeeSpeaker_DetailExist(String Event_id, String user_id, String event_Type, String id, String tag) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + Table_AtteendeeSpeakerDetail + " WHERE " + AtteendeeSpeakerDetail_eventId + " = '" + Event_id
                + "' AND " + AtteendeeSpeakerDetail_eventType + " = '" + event_Type
                + "' AND " + AtteendeeSpeakerDetail_Id + " = '" + id
                + "' AND " + AtteendeeSpeakerDetail_tag + " = '" + tag
                + "' AND " + AtteendeeSpeakerDetail_userId + " = '" + user_id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getAttdeeSpeaker_Detail(String Event_id, String user_id, String event_Type, String id, String tag) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Table_AtteendeeSpeakerDetail + " WHERE " + AtteendeeSpeakerDetail_eventId + " = '" + Event_id
                + "' AND " + AtteendeeSpeakerDetail_eventType + " = '" + event_Type
                + "' AND " + AtteendeeSpeakerDetail_Id + " = '" + id
                + "' AND " + AtteendeeSpeakerDetail_tag + " = '" + tag
                + "' AND " + AtteendeeSpeakerDetail_userId + " = '" + user_id + "'", null);
        return cursor;
    }


// Insert ExhibitorDetail

    public boolean insertExhibitorDetail(String Event_id, String user_id, String event_Type, String data, String exid, String expageId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExhibitorDetail_eventId, Event_id);
        contentValues.put(ExhibitorDetail_userId, user_id);
        contentValues.put(ExhibitorDetail_eventType, event_Type);
        contentValues.put(ExhibitorDetail_Data, data);
        contentValues.put(ExhibitorDetail_ExId, exid);
        contentValues.put(ExhibitorDetail_ExPageId, expageId);
        db.insert(Table_ExhibitorDetail, null, contentValues);
        return true;
    }


    public void deleteExhibitorDetailData(String Event_id, String user_id, String event_Type, String exid, String expageId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "delete from " + Table_ExhibitorDetail + " WHERE " + ExhibitorDetail_eventId + " = '" + Event_id
                + "' AND " + ExhibitorDetail_userId + " = '" + user_id
                + "' AND " + ExhibitorDetail_eventType + " = '" + event_Type
                + "' AND " + ExhibitorDetail_ExId + " = '" + exid
                + "' AND " + ExhibitorDetail_ExPageId + " = '" + expageId + "'";

        db.execSQL(qry);

    }

    public boolean UpdateExhibitorDetailUserId(String Event_id, String user_Id, String exid, String expageId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExhibitorDetail_userId, user_Id);
        db.update(Table_ExhibitorDetail, contentValues,
                ExhibitorDetail_ExId + " = ? AND "
                        + ExhibitorDetail_eventId + " = ? AND "
                        + ExhibitorDetail_ExPageId + " = ?", new String[]{exid, Event_id, expageId});

        return true;
    }


    public boolean isExhibitorExist(String Event_id, String exid, String expageId, String event_Type, String user_Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + Table_ExhibitorDetail + " WHERE " + ExhibitorDetail_eventId + " = '" + Event_id
                + "' AND " + ExhibitorDetail_eventType + " = '" + event_Type
                + "' AND " + ExhibitorDetail_ExId + " = '" + exid
                + "' AND " + ExhibitorDetail_ExPageId + " = '" + expageId
                + "' AND " + ExhibitorDetail_userId + " = '" + user_Id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getExhibitorDetail(String Event_id, String exid, String expageId, String event_Type, String user_Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Table_ExhibitorDetail + " WHERE " + ExhibitorDetail_eventId + " = '" + Event_id
                + "' AND " + ExhibitorDetail_eventType + " = '" + event_Type
                + "' AND " + ExhibitorDetail_ExId + " = '" + exid
                + "' AND " + ExhibitorDetail_ExPageId + " = '" + expageId
                + "' AND " + ExhibitorDetail_userId + " = '" + user_Id + "'", null);
        return cursor;
    }


    public boolean insertPresantationDetailId(String presantationId, String Event_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PDetailPresantationId, presantationId);
        contentValues.put(PDetailEventId, Event_id);
        db.insert(TABLE_PDetailId, null, contentValues);
        return true;
    }


    public boolean isPresantationDetailIdExist(String Event_id, String presantationId) {


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_PDetailId + " WHERE " + PDetailEventId + " = '" + Event_id
                + "' AND " + PDetailPresantationId + " = '" + presantationId + "'", null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public Cursor getPresantationDetailId(String Event_id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_PDetailId + " WHERE " + PDetailEventId + " = '" + Event_id + "'", null);
        return cursor;
    }


    public boolean insertPresantationDetailImage(String presantationId, String presantation_kind, String Event_id, String user_id, String imagePath, String imageName) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PresantationPresantationId, presantationId);
        contentValues.put(PresantationKind, presantation_kind);
        contentValues.put(PresantationEventId, Event_id);
        contentValues.put(PresantationUserId, user_id);
        contentValues.put(PresantationImagePath, imagePath);
        contentValues.put(PresantationImageName, imageName);
        db.insert(TABLE_Presantation_DetailImage, null, contentValues);
        return true;
    }


    public Cursor getPresantationDetailImage(String presantationId, String presantation_kind, String Event_id, String user_id, String imageName) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_Presantation_DetailImage + " WHERE " + PresantationPresantationId + " = '" + presantationId
                + "' AND " + PresantationKind + " = '" + presantation_kind
                + "' AND " + PresantationEventId + " = '" + Event_id
                + "' AND " + PresantationUserId + " = '" + user_id
                + "' AND " + PresantationImageName + " = '" + imageName + "'", null);
        return cursor;
    }

    public Cursor getPresantationDetail(String presantationId) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_Presantation_DetailImage + " WHERE " + PresantationPresantationId + " = '" + presantationId + "'", null);
        return cursor;
    }


    // Insert Sponsor Detail

    public boolean insertSponsorDetail(String Event_id, String user_id, String event_Type, String data, String sp_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(sponsorDetail_eventId, Event_id);
        contentValues.put(sponsorDetail_userId, user_id);
        contentValues.put(sponsorDetail_eventType, event_Type);
        contentValues.put(sponsorDetail_SpId, sp_id);
        contentValues.put(sponsorDetail_Data, data);
        db.insert(Table_SponsorDetail, null, contentValues);
        return true;
    }

    public void deleteSponsorDetailData(String Event_id, String user_id, String event_Type, String sp_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "delete from " + Table_SponsorDetail + " WHERE " + sponsorDetail_eventId + " = '" + Event_id
                + "' AND " + sponsorDetail_userId + " = '" + user_id
                + "' AND " + sponsorDetail_SpId + " = '" + sp_id
                + "' AND " + sponsorDetail_eventType + " = '" + event_Type + "'";

        db.execSQL(qry);

    }

    public boolean isSponsorExist(String Event_id, String event_Type, String user_Id, String sp_id) {


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Table_SponsorDetail + " WHERE " + sponsorDetail_eventId + " = '" + Event_id
                + "' AND " + sponsorDetail_eventType + " = '" + event_Type
                + "' AND " + sponsorDetail_SpId + " = '" + sp_id
                + "' AND " + sponsorDetail_userId + " = '" + user_Id + "'", null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getSponsorDetail(String Event_id, String event_Type, String user_Id, String sp_id) {


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Table_SponsorDetail + " WHERE " + sponsorDetail_eventId + " = '" + Event_id
                + "' AND " + sponsorDetail_eventType + " = '" + event_Type
                + "' AND " + sponsorDetail_SpId + " = '" + sp_id
                + "' AND " + sponsorDetail_userId + " = '" + user_Id + "'", null);
        return cursor;
    }
// Insert Presantation Data


    public boolean insertPresantationList(String Event_id, String presanation_Type, String presanation_data, String user_Id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(presan_EventId, Event_id);
        contentValues.put(presan_type, presanation_Type);
        contentValues.put(presan_Data, presanation_data);
        contentValues.put(presan_userId, user_Id);
        db.insert(TABLE_Presantation, null, contentValues);
        return true;
    }


    public boolean UpdatePresantationList(String Event_id, String presanation_Type, String presanation_data, String user_Id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(presan_Data, presanation_data);
        db.update(TABLE_Presantation, contentValues,
                presan_type + " = ? AND "
                        + presan_userId + " = ? AND "
                        + presan_EventId + " = ?", new String[]{presanation_Type, user_Id, Event_id});

        return true;
    }

    public boolean isPresantationList(String Event_id, String presanation_Type, String user_Id) {


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_Presantation + " WHERE " + presan_EventId + " = '" + Event_id
                + "' AND " + presan_type + " = '" + presanation_Type
                + "' AND " + presan_userId + " = '" + user_Id + "'", null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public Cursor getPresantationList(String Event_id, String presanation_Type, String user_Id) {


        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_Presantation + " WHERE " + presan_EventId + " = '" + Event_id
                + "' AND " + presan_type + " = '" + presanation_Type
                + "' AND " + presan_userId + " = '" + user_Id + "'";
        Cursor res = db.rawQuery(qry, null);


        return res;
    }


// Insert CMS PAGE DATA


    public boolean insertCmsPage(String Event_id, String cms_id, String data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CmsEventId, Event_id);
        contentValues.put(CmsId, cms_id);
        contentValues.put(CmsData, data);

        db.insert(Table_CMSListing, null, contentValues);
        return true;
    }


    public void deleteCmsPageData(String eventId, String cms_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "delete from " + Table_CMSListing + " WHERE " + CmsEventId + " = '" + eventId
                + "' AND " + CmsId + " = '" + cms_id + "'";

        db.execSQL(qry);

    }


    public boolean isCmsPageExist(String event_id, String cms_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + Table_CMSListing + " WHERE " + CmsEventId + " = '" + event_id
                + "' AND " + CmsId + " = '" + cms_id + "'";

        Cursor cursor = db.rawQuery(qry, null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getCmsPageData(String event_id, String cms_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + Table_CMSListing + " WHERE " + CmsEventId + " = '" + event_id
                + "' AND " + CmsId + " = '" + cms_id + "'";


        Cursor cursor = db.rawQuery(qry, null);

        return cursor;
    }

    // Insert NotesListingData

    public boolean insertNotesListing(String Event_id, String user_id, String data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesEventId, Event_id);
        contentValues.put(NotesUserId, user_id);
        contentValues.put(NotesData, data);

        db.insert(Table_notesListing, null, contentValues);
        return true;
    }


    public void deleteNotesListingData(String eventId, String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "delete from " + Table_notesListing + " WHERE " + NotesEventId + " = '" + eventId
                + "' AND " + NotesUserId + " = '" + user_id + "'";


        db.execSQL(qry);

    }

    public boolean isNotesListExist(String event_id, String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + Table_notesListing + " WHERE " + NotesEventId + " = '" + event_id
                + "' AND " + NotesUserId + " = '" + user_id + "'";

        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getNotesListingData(String event_id, String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + Table_notesListing + " WHERE " + NotesEventId + " = '" + event_id
                + "' AND " + NotesUserId + " = '" + user_id + "'";


        Cursor cursor = db.rawQuery(qry, null);

        return cursor;
    }


    //**********************************//
//////////////////////////////////////////////////

    // Insert Advertiesment

    public boolean insertAdvertiesmentData(String Event_id, String menuId, String advertiesData) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(adaverties_event_id, Event_id);
        contentValues.put(adverties_menuID, menuId);
        contentValues.put(adverties_Data, advertiesData);
        db.insert(TABLE_advertiesment, null, contentValues);
        return true;
    }


    public void deleteAdvertiesMentData(String eventId, String menuId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "delete from " + TABLE_advertiesment + " WHERE " + adaverties_event_id + " = '" + eventId
                + "' AND " + adverties_menuID + " = '" + menuId + "'";


        db.execSQL(qry);

    }

    public Cursor getAdvertiesMentData(String event_id, String menuId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_advertiesment + " WHERE " + adaverties_event_id + " = '" + event_id
                + "' AND " + adverties_menuID + " = '" + menuId + "'";


        Cursor cursor = db.rawQuery(qry, null);

        return cursor;
    }


    public boolean isAdvertiesMentExist(String event_id, String menuID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_advertiesment + " WHERE " + adaverties_event_id + " = '" + event_id
                + "' AND " + adverties_menuID + " = '" + menuID + "'";

        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


// Insert ATTENDEE LIST


    public boolean insertAttendeeListing(String Event_id, String user_id, String data, String tag) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ListingEventId, Event_id);
        contentValues.put(ListingUserId, user_id);
        contentValues.put(ListingData, data);
        contentValues.put(ListingTag, tag);
        db.insert(Table_Attendeelisting, null, contentValues);
        return true;
    }


    public void deleteListingData(String eventId, String tag, String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "delete from " + Table_Attendeelisting + " WHERE " + ListingEventId + " = '" + eventId
                + "' AND " + ListingUserId + " = '" + user_id
                + "' AND " + ListingTag + " = '" + tag + "'";


        db.execSQL(qry);

    }


    public void updateListingData(String eventId, String tag, String user_id, String attendeeData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ListingData, attendeeData);
        db.update(Table_Attendeelisting, contentValues,
                ListingUserId + " = ? AND "
                        + ListingTag + " = ? AND "
                        + ListingEventId + " = ?", new String[]{user_id, tag, eventId});
    }

    public boolean isAttendeeListExist(String event_id, String user_id, String tag) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + Table_Attendeelisting + " WHERE " + ListingEventId + " = '" + event_id
                + "' AND " + ListingUserId + " = '" + user_id
                + "' AND " + ListingTag + " = '" + tag + "'";

        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getAttendeeListingData(String event_id, String user_id, String tag) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + Table_Attendeelisting + " WHERE " + ListingEventId + " = '" + event_id
                + "' AND " + ListingUserId + " = '" + user_id
                + "' AND " + ListingTag + " = '" + tag + "'";


        Cursor cursor = db.rawQuery(qry, null);


        return cursor;
    }


//*************************************************//
    // InsertPendingMessageReuqest

    public boolean insertPendingMessageRequest(String event_id, String user_id, String event_token, String message_url, String messageImg_url, String str_message, String img_array) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PendingRequestMessageEventId, event_id);
        contentValues.put(PendingRequestMessageUserId, user_id);
        contentValues.put(PendingRequestMessageToken, event_token);
        contentValues.put(PendingRequestMessageurl, message_url);
        contentValues.put(PendingRequestMessageImageurl, messageImg_url);
        contentValues.put(PendingRequestMessage, str_message);
        contentValues.put(PendingRequestImageArray, img_array);
        db.insert(TABLE_PendingActivity_request, null, contentValues);

        return true;
    }


    // getPendingMessageReuqest


    public Cursor getPendingMessageRequest(String event_id, String user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_PendingActivity_request + " WHERE " + PendingRequestMessageEventId + " = '" + event_id
                + "' AND " + PendingRequestMessageUserId + " = '" + user_id + "'";


        Cursor cursor = db.rawQuery(qry, null);


        return cursor;
    }

    //  Delete PendingMessageReuqest
    public boolean DeletePendigMessageRequest(String pendingRequestId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PendingActivity_request, PendingRequestMessageId + " = ? ", new String[]{pendingRequestId});
        return true;
    }


    //****************************************88
    // Insert ActivityList Data
    public boolean insertEventActivityList(String event_id, String user_id, String eventType, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Activity_eventId, event_id);
        contentValues.put(Activity_userId, user_id);
        contentValues.put(Activity_eventType, eventType);
        contentValues.put(Activity_activityData, data);
        db.insert(TABLE_Activity, null, contentValues);

        return true;
    }


    public boolean isActivityListExist(String event_id, String user_id, String eventType) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_Activity + " WHERE " + Activity_eventId + " = '" + event_id
                + "' AND " + Activity_userId + " = '" + user_id
                + "' AND " + Activity_eventType + " = '" + eventType + "'";


        Cursor cursor = db.rawQuery(qry, null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public Cursor getActivityList(String event_id, String user_id, String eventType) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_Activity + " WHERE " + Activity_eventId + " = '" + event_id
                + "' AND " + Activity_userId + " = '" + user_id
                + "' AND " + Activity_eventType + " = '" + eventType + "'";


        Cursor cursor = db.rawQuery(qry, null);

        return cursor;
    }

    public boolean UpdateActivityListing(String event_id, String user_id, String eventType, String data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Activity_activityData, data);
        db.update(TABLE_Activity, contentValues,
                Activity_eventId + " = ? AND "
                        + Activity_userId + " = ? AND "
                        + Activity_eventType + " = ?", new String[]{event_id, user_id, eventType});

        return true;
    }


//***********************************************************8
// Insert PendingRequest Data

    public boolean insertPendingRequestData(String pending_url, String pending_eventId, String pending_token, String pending_userId, String PendingAgendaId, String pendingIsSave) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PendingRequesturl, pending_url);
        contentValues.put(PendingRequestEventId, pending_eventId);
        contentValues.put(PendingRequestToken, pending_token);
        contentValues.put(PendingRequestUserId, pending_userId);
        contentValues.put(PendingRequestAgendaId, PendingAgendaId);
        contentValues.put(PendingRequestActiveStatus, pendingIsSave);
        db.insert(TABLE_PendingRequest, null, contentValues);


        return true;
    }

    public boolean DeletePendigRequest(String agenda_id, String eventId, String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PendingRequest,
                PendingRequestAgendaId + " = ? AND "
                        + PendingRequestEventId + " = ? AND "
                        + PendingRequestUserId + " = ? ", new String[]{agenda_id, eventId, user_id});
        return true;
    }

    public Cursor getPendingAgendaRequestListing(String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "SELECT * from " + TABLE_PendingRequest + " WHERE " + PendingRequestActiveStatus + " = '" + status + "'";

        Cursor cursor = db.rawQuery(qry, null);

        return cursor;
    }

    public boolean IsPendingAgendaRequestExist(String status, String agenda_id, String event_id, String user_Id, String url) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_PendingRequest + " WHERE " + PendingRequestActiveStatus + " = '" + status
                + "' AND " + PendingRequestAgendaId + " = '" + agenda_id
                + "' AND " + PendingRequestEventId + " = '" + event_id
                + "' AND " + PendingRequesturl + " = '" + url
                + "' AND " + PendingRequestUserId + " = '" + user_Id + "'";


        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    //*********************************//
// Insert AgenData
    public boolean insertAgendaListing(String Event_id, String agenda_Kind, String agenda_Type, String agenda_data, String user_Id, String agenda_CAtegoryIdData) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(agenda_EventId, Event_id);
        contentValues.put(agenda_kind, agenda_Kind);
        contentValues.put(agenda_type, agenda_Type);
        contentValues.put(agenda_Data, agenda_data);
        contentValues.put(agenda_CategoryIdData, agenda_CAtegoryIdData);
        contentValues.put(user_id, user_Id);
        db.insert(TABLE_Agenda, null, contentValues);
        return true;
    }


    public boolean UpdateAgendaListing(String AgendaEvent_id, String agenda_Kind, String agenda_Type, String agenda_data, String user_Id, String agenda_CAtegoryIdData) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(agenda_Data, agenda_data);
        db.update(TABLE_Agenda, contentValues,
                agenda_kind + " = ? AND "
                        + agenda_type + " = ? AND "
                        + user_id + " = ? AND "
                        + agenda_CategoryIdData + " = ? AND "
                        + agenda_EventId + " = ?", new String[]{agenda_Kind, agenda_Type, user_Id, agenda_CAtegoryIdData, AgendaEvent_id});

        return true;
    }

    public boolean isAgendaListExist(String event_id, String agenda_Kind, String agenda_Type, String user_Id, String agenda_CAtegoryIdData) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_Agenda + " WHERE " + agenda_EventId + " = " + event_id
                + " AND " + agenda_kind + " = '" + agenda_Kind
                + "' AND " + agenda_type + " = '" + agenda_Type
                + "' AND " + agenda_CategoryIdData + " = '" + agenda_CAtegoryIdData
                + "' AND " + user_id + " = '" + user_Id + "'", null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public Cursor getAgendaListing(String event_id, String agenda_Kind, String agenda_Type, String user_Id, String agenda_CAtegoryIdData) {


        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_Agenda + " WHERE " + agenda_EventId + " = " + event_id
                + " AND " + agenda_kind + " = '" + agenda_Kind
                + "' AND " + agenda_type + " = '" + agenda_Type
                + "' AND " + agenda_CategoryIdData + " = '" + agenda_CAtegoryIdData
                + "' AND " + user_id + " = '" + user_Id + "'";
//
        Cursor res = db.rawQuery(qry, null);


        return res;
    }
//**************************************************//
    // Insert AgendaDetail Data

    public boolean insertAgendaDetail(String Event_id, String agenda_id, String Detail_agendaData, String user_Id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DetailAgenda_Event_id, Event_id);
        contentValues.put(Agenda_id, agenda_id);
        contentValues.put(Detailagenda_Data, Detail_agendaData);
        contentValues.put(DetailAgenda_user_id, user_Id);
        db.insert(TABLE_DetailAgenda, null, contentValues);
        return true;
    }

    public boolean UpdateAgendaDetail(String Event_id, String agenda_id, String Detail_agendaData, String user_Id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Detailagenda_Data, Detail_agendaData);
        db.update(TABLE_DetailAgenda, contentValues,
                DetailAgenda_Event_id + " = ? AND "
                        + Agenda_id + " = ? AND "
                        + DetailAgenda_user_id + " = ?", new String[]{Event_id, agenda_id, user_Id});

        return true;
    }

    public boolean isAgendaDetailExist(String Event_id, String agenda_id, String user_Id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_DetailAgenda + " WHERE " + DetailAgenda_Event_id + " = " + Event_id
                + " AND " + Agenda_id + " = '" + agenda_id
                + "' AND " + DetailAgenda_user_id + " = '" + user_Id + "'", null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getAgendaDetail(String Event_id, String agenda_id, String user_Id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_DetailAgenda + " WHERE " + DetailAgenda_Event_id + " = " + Event_id
                + " AND " + Agenda_id + " = '" + agenda_id
                + "' AND " + DetailAgenda_user_id + " = '" + user_Id + "'", null);

        return cursor;
    }


    //******************************************//
// Insert EventListing
    public boolean insertEventListing(String Evevnt_id, String Evevnt_name, String fb_loginStatus, String Evevnt_type, String Evevnt_logo, String fund_status, String multiLanguage, String show_loginScreen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Event_Id, Evevnt_id);
        contentValues.put(Event_NAME, Evevnt_name);
        contentValues.put(Fblogin_Enabled, fb_loginStatus);
        contentValues.put(Event_Type, Evevnt_type);
        contentValues.put(Event_Logo, Evevnt_logo);
        contentValues.put(Fund_Enabled, fund_status);
        contentValues.put(EventListmultiLanguage, multiLanguage);
        contentValues.put(EventListshow_login_screen, show_loginScreen);
        db.insert(TABLE_EventList, null, contentValues);

        return true;
    }

    public boolean UpdateEventListing(String Evevnt_id, String Evevnt_name, String fb_loginStatus, String Evevnt_type, String Evevnt_logo, String fund_status, String multiLanguage, String show_loginScreen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Event_NAME, Evevnt_name);
        contentValues.put(Fblogin_Enabled, fb_loginStatus);
        contentValues.put(Event_Type, Evevnt_type);
        contentValues.put(Event_Logo, Evevnt_logo);
        contentValues.put(Fund_Enabled, fund_status);
        contentValues.put(EventListmultiLanguage, multiLanguage);
        contentValues.put(EventListshow_login_screen, show_loginScreen);
        db.update(TABLE_EventList, contentValues, Event_Id + " = " + Evevnt_id, null);

        return true;
    }


    public void deleteEventListData() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from " + TABLE_EventList);

    }

    public Cursor getEventListData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from " +TABLE_EventList+ " where "+Event_Id+"="+id,null);
        Cursor res = db.rawQuery("select * from " + TABLE_EventList, null);
        return res;
    }


    public boolean isEventExist(String event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_EventList + " WHERE " + Event_Id + " = "
                + event_id, null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //****************************************//
    // Insert EventHomeData
    public boolean insertEventHomeData(String event_id, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Home_EventId, event_id);
            contentValues.put(Home_EventData, data);
            db.insert(TABLE_EventHomeData, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


        return true;
    }

    public boolean UpdateEventHomeData(String event_id, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Home_EventData, data);
            db.update(TABLE_EventHomeData, contentValues, Home_EventId + " = " + event_id, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }

    public Cursor getEventHomeData(String eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from " +TABLE_EventList+ " where "+Event_Id+"="+id,null);
        Cursor res = db.rawQuery("select * from " + TABLE_EventHomeData + " WHERE " + Home_EventId + " = '" + eventId + "'", null);
        return res;
    }

    public boolean isEventDataExist(String event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_EventHomeData + " WHERE " + Home_EventId + " = '" + event_id + "'", null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getEventListHomeData() {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from " +TABLE_EventList+ " where "+Event_Id+"="+id,null);
        Cursor res = db.rawQuery("select * from " + TABLE_EventHomeData, null);
        return res;
    }

    public void deleteHomeData() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from " + TABLE_EventHomeData);

    }

    // New Changes
    //********************************************************************************


    public boolean insertUpdateAllParentCategory(ArrayList<ExhibitorParentCategoryData> list, String event_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {

            for (int i = 0; i < list.size(); i++) {
                ExhibitorParentCategoryData parentCategoryData = list.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(PARENT_CATEGORY_EVENTID, event_Id);
                contentValues.put(PARENT_CATEGORY_ID, parentCategoryData.getC_id());
                contentValues.put(PARENT_CATEGORY_NAME, parentCategoryData.getCategory());
                contentValues.put(PARENT_CATEGORY_ICON, parentCategoryData.getCategorie_icon());
                contentValues.put(PARENT_CATEGORY_SORT_ORDERNO, parentCategoryData.getSort_order());
                db.insert(TABLE_EXHIBITOR_PARENT_CATEGORY, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return true;
    }

    public boolean isExhibitorDataExist(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_EXHIBITOR_LIST + " WHERE " + EXHIBITOR_LIST_EVENTID + " = '" + Event_id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public void deleteExhibitorListData(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        try {
            String qry = "delete from " + TABLE_EXHIBITOR_LIST + " WHERE " + EXHIBITOR_LIST_EVENTID + " = '" + Event_id + "'";
            String qrytype = "delete from " + TABLE_EXHIBITOR_COUNTRYLIST + " WHERE " + EXHIBITOR_COUNTRY_EVENTID + " = '" + Event_id + "'";
            String qryproduct = "delete from " + TABLE_EXHIBITOR_PRODUCTCAT_LIST + " WHERE " + EXHIBITOR_PRODUCTCAT_EVENTID + " = '" + Event_id + "'";
            String qrytsubCate = "delete from " + TABLE_EXHIBITOR_SUB_CATEGORY + " WHERE " + SUB_CATEGORY_EVENTID + " = '" + Event_id + "'";
            String qrytParCat = "delete from " + TABLE_EXHIBITOR_PARENT_CATEGORY + " WHERE " + PARENT_CATEGORY_EVENTID + " = '" + Event_id + "'";
            String qryExhibitor = "delete from " + TABLE_EXHIBITOR_TYPES + " WHERE " + EXHIBITOR_TYPE_EVENTID + " = '" + Event_id + "'";
            String qryExhibitorParentGroup = "delete from " + TABLE_EXHIBITOR_PRODUCTCAT_GROUP_LIST + " WHERE " + EXHIBITOR_PARENT_GROUP_EVENTID + " = '" + Event_id + "'";
            String qryExhibitorDetail = "delete from " + Table_ExhibitorDetail + " WHERE " + ExhibitorDetail_eventId + " = '" + Event_id + "'";
//
            db.execSQL(qry);
            db.execSQL(qrytype);
            db.execSQL(qryproduct);
            db.execSQL(qrytsubCate);
            db.execSQL(qrytParCat);
            db.execSQL(qryExhibitor);
            db.execSQL(qryExhibitorDetail);
            db.execSQL(qryExhibitorParentGroup);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }


    public boolean isGroupDataExist(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_GROUP + " WHERE " + GROUP_EVENTID + " = '" + Event_id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSuperGroupDataExist(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_SUPERGROUP_LIST + " WHERE " + SUPERGROUP_EVENTID + " = '" + Event_id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSuperGroupRelationDataExist(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_SUPERGROUP_RELATION + " WHERE " + SUPERGROUP_RELATION_EVENTID + " = '" + Event_id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isGroupRelationDataExist(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_GROUP_RELATION + " WHERE " + GROUP_EVENTID + " = '" + Event_id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public void deleteGroupRelationExistData(String event_Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry = "delete from " + TABLE_GROUP_RELATION + " WHERE " + GROUP_RELATION_EVENTID + " = '" + event_Id + "'";

            db.execSQL(qry);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public void deleteGroupExistData(String event_Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry1 = "delete from " + TABLE_GROUP + " WHERE " + GROUP_EVENTID + " = '" + event_Id + "'";
            db.execSQL(qry1);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public void deleteSuperGroupExistData(String event_Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry1 = "delete from " + TABLE_SUPERGROUP_LIST + " WHERE " + SUPERGROUP_EVENTID + " = '" + event_Id + "'";
            db.execSQL(qry1);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public void deleteSuperRelationGroupExistData(String event_Id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry1 = "delete from " + TABLE_SUPERGROUP_RELATION + " WHERE " + SUPERGROUP_RELATION_EVENTID + " = '" + event_Id + "'";
            db.execSQL(qry1);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }


    public boolean insertUpdateAllGroupModuleData(ArrayList<GroupModuleData> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                GroupModuleData groupModuleData = list.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(GROUP_EVENTID, groupModuleData.getEventId());
                contentValues.put(GROUP_ID, groupModuleData.getModuleGroupId());
                contentValues.put(GROUP_ICON, groupModuleData.getGroupImage());
                contentValues.put(GROUP_NAME, groupModuleData.getGroupName());
                contentValues.put(GROUP_SORT_ORDER_NO, groupModuleData.getSort_order());
                contentValues.put(GROUP_MENU_ID, groupModuleData.getMenuId());
                db.insert(TABLE_GROUP, null, contentValues);
//            int rows = db.update(TABLE_GROUP, contentValues, GROUP_EVENTID + " = ? AND " + GROUP_ID + " = ? ", new String[]{groupModuleData.getEventId(), groupModuleData.getModuleGroupId()});
//            if (rows == 0) {
//
//            }

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }

    public boolean insertSuperGroupModuleData(ArrayList<SuperGroupData> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                SuperGroupData groupModuleData = list.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(SUPERGROUP_EVENTID, groupModuleData.getEventId());
                contentValues.put(SUPERGROUP_MENUID, groupModuleData.getMenuId());
                contentValues.put(SUPERGROUP_GROUP_IMAGE, groupModuleData.getGroupImage());
                contentValues.put(SUPERGROUP_GROUP_NAME, groupModuleData.getGroupName());
                contentValues.put(SUPERGROUP_ID, groupModuleData.getId());
                contentValues.put(SUPERGROUP_GROUP_CREATEDATE, groupModuleData.getCreatedDate());
                contentValues.put(SUPERGROUP_GROUP_UPDATE_DATE, groupModuleData.getUpdatedDate());
                contentValues.put(SUPERGROUP_GROUP_SORT_ORDERNO, groupModuleData.getSort_order());
                db.insert(TABLE_SUPERGROUP_LIST, null, contentValues);
//            int rows = db.update(TABLE_GROUP, contentValues, GROUP_EVENTID + " = ? AND " + GROUP_ID + " = ? ", new String[]{groupModuleData.getEventId(), groupModuleData.getModuleGroupId()});
//            if (rows == 0) {
//
//            }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }


    public boolean isScanLeadDataExist(String Event_id, String User_id, String badgeNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_LeadExhibitorOffline + " WHERE " + MyExiLead_EventId + " = '" + Event_id + "'"
                + " AND " + MyExiLead_userId + " = '" + User_id + "'"
                + " AND " + MyExiLead_badgeNumber + " LIKE '" + badgeNumber + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public ArrayList<ExhibitorLead_MyLeadData_Offline> getMyLeadMyExiLeadData(String Event_id, String User_id) {
        SQLiteDatabase db = this.getReadableDatabase();


        ArrayList<ExhibitorLead_MyLeadData_Offline> exhibitorLeadMyLeadDataOfflines = new ArrayList<ExhibitorLead_MyLeadData_Offline>();

        String qry = "select * from " + TABLE_LeadExhibitorOffline + " WHERE " + MyExiLead_EventId + " = '" + Event_id + "'"
                + " AND " + MyExiLead_userId + " = '" + User_id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        while (cursor.moveToNext()) {
            ExhibitorLead_MyLeadData_Offline object = new ExhibitorLead_MyLeadData_Offline();
            object.setBadgeNumber(cursor.getString(cursor.getColumnIndex(MyExiLead_badgeNumber)));
            object.setData(cursor.getString(cursor.getColumnIndex(MyExiLead_data)));
            object.setTitle(cursor.getString(cursor.getColumnIndex(MyExiLead_Title)));
            object.setCompanyName(cursor.getString(cursor.getColumnIndex(MyExiLead_Company_name)));
            object.setEmail(cursor.getString(cursor.getColumnIndex(MyExiLead_Email)));
            object.setFirstname(cursor.getString(cursor.getColumnIndex(MyExiLead_Firstname)));
            object.setLastname(cursor.getString(cursor.getColumnIndex(MyExiLead_Lastname)));
            object.setOrganisorId("");
            object.setRoleId("");
            object.setId(cursor.getString(cursor.getColumnIndex(MyExiLead_leadId)));
            exhibitorLeadMyLeadDataOfflines.add(object);
        }
        return exhibitorLeadMyLeadDataOfflines;
    }


    public ArrayList<ExhibitorLead_MyLeadData_Offline> getMyLeadMyExiLeadDataBadgeNumberwise(String Event_id, String User_id, String badgeNumber) {
        SQLiteDatabase db = this.getReadableDatabase();


        ArrayList<ExhibitorLead_MyLeadData_Offline> exhibitorLeadMyLeadDataOfflines = new ArrayList<ExhibitorLead_MyLeadData_Offline>();

        String qry = "select * from " + TABLE_LeadExhibitorOffline + " WHERE " + MyExiLead_EventId + " = '" + Event_id + "'"
                + " AND " + MyExiLead_userId + " = '" + User_id + "'"
                + " AND " + MyExiLead_badgeNumber + " LIKE '" + badgeNumber + "'";
        Cursor cursor = db.rawQuery(qry, null);

        while (cursor.moveToNext()) {
            ExhibitorLead_MyLeadData_Offline object = new ExhibitorLead_MyLeadData_Offline();
            object.setBadgeNumber(cursor.getString(cursor.getColumnIndex(MyExiLead_badgeNumber)));
            object.setData(cursor.getString(cursor.getColumnIndex(MyExiLead_data)));
            object.setTitle(cursor.getString(cursor.getColumnIndex(MyExiLead_Title)));
            object.setCompanyName(cursor.getString(cursor.getColumnIndex(MyExiLead_Company_name)));
            object.setEmail(cursor.getString(cursor.getColumnIndex(MyExiLead_Email)));
            object.setFirstname(cursor.getString(cursor.getColumnIndex(MyExiLead_Firstname)));
            object.setLastname(cursor.getString(cursor.getColumnIndex(MyExiLead_Lastname)));
            object.setOrganisorId("");
            object.setRoleId("");
            object.setId(cursor.getString(cursor.getColumnIndex(MyExiLead_leadId)));
            exhibitorLeadMyLeadDataOfflines.add(object);
        }
        return exhibitorLeadMyLeadDataOfflines;
    }


    public boolean insertSuperGroupRelationModuleData(ArrayList<SuperGroupRelationData> list, String eventid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                SuperGroupRelationData groupModuleData = list.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(SUPERGROUP_RELATION_ID, groupModuleData.getId());
                contentValues.put(SUPERGROUP_RELATION_MENUID, groupModuleData.getMenuId());
                contentValues.put(SUPERGROUP_RELATION_SUPERGROUP_ID, groupModuleData.getSuperGroupId());
                contentValues.put(SUPERGROUP_RELATION_CHILDGROUP_ID, groupModuleData.getChildGroupId());
                contentValues.put(SUPERGROUP_RELATION_EVENTID, eventid);
                db.insert(TABLE_SUPERGROUP_RELATION, null, contentValues);
//            int rows = db.update(TABLE_GROUP, contentValues, GROUP_EVENTID + " = ? AND " + GROUP_ID + " = ? ", new String[]{groupModuleData.getEventId(), groupModuleData.getModuleGroupId()});
//            if (rows == 0) {
//
//            }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }


    public boolean isMapListExist(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_MAP_List + " WHERE " + MAP_LIST_EventId + " = '" + Event_id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSpeakerListExist(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_SPEAKER_LIST + " WHERE " + SPEAKER_LIST_EVENTID + " = '" + Event_id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public boolean insertUpdateSpeakerdata(ArrayList<SpeakerListClass> listdata, String event_Id, String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < listdata.size(); i++) {
            SpeakerListClass speakerListClass = listdata.get(i);
            insertUpdateSpeakerType(speakerListClass.getTypeId(), speakerListClass.getType(), speakerListClass.getBgColor(), event_Id, speakerListClass.getTypePosition());
            insertUpdateAllSpeakerListData(speakerListClass.getData(), event_Id, user_id, speakerListClass.getTypeId());
        }
        return true;
    }

    public boolean insertUpdateAllSpeakerListData(List<SpeakerListClass.SpeakerListNew> list, String eventId, String user_id, String type_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                SpeakerListClass.SpeakerListNew speakerListNew = list.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(SPEAKER_LIST_EVENTID, eventId);
                contentValues.put(SPEAKER_LIST_ID, speakerListNew.getId());
                contentValues.put(SPEAKER_LIST_COMAPANYNAME, speakerListNew.getCompanyName());
                contentValues.put(SPEAKER_LIST_FAVOURITE, speakerListNew.getIsFavorites());
                contentValues.put(SPEAKER_LIST_FIRST_NAME, speakerListNew.getFirstname());
                contentValues.put(SPEAKER_LIST_LAST_NAME, speakerListNew.getLastname());
                contentValues.put(SPEAKER_LIST_LOGO, speakerListNew.getLogo());
                contentValues.put(SPEAKER_LIST_SPEAKER_DESC, speakerListNew.getSpeakerDesc());
                contentValues.put(SPEAKER_LIST_SPEAKER_EMAIL, speakerListNew.getEmail());
                contentValues.put(SPEAKER_LIST_SPEAKER_HEADING, speakerListNew.getHeading());
                contentValues.put(SPEAKER_LIST_TITLE, speakerListNew.getTitle());
                contentValues.put(SPEAKER_LIST_USERID, "");
                if (type_id == null)
                    contentValues.put(SPEAKER_LIST_TYPEID, "");
                else
                    contentValues.put(SPEAKER_LIST_TYPEID, type_id);
                db.insert(TABLE_SPEAKER_LIST, null, contentValues);

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }

    public void deleteSpeakerListExistData(String Event_id) {

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry = "delete from " + TABLE_SPEAKER_LIST + " WHERE " + SPEAKER_LIST_EVENTID + " = '" + Event_id + "'";
            String qry1 = "delete from " + TABLE_SPEAKER_LIST_TYPE + " WHERE " + Speaker_TYPE_EVENTID + " = '" + Event_id + "'";

            db.execSQL(qry);
            db.execSQL(qry1);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public void deleteMapListExistData(String Event_id) {

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry = "delete from " + TABLE_MAP_List + " WHERE " + MAP_LIST_EventId + " = '" + Event_id + "'";


            db.execSQL(qry);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public boolean insertUpdateAllMapListData(ArrayList<MapListData.MapNewData> list, String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                MapListData.MapNewData groupModuleData = list.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(MAP_LIST_EventId, eventId);
                contentValues.put(MAP_LIST_ADDRESS, groupModuleData.getAddress());
                contentValues.put(MAP_LIST_CHECK_DWGFILES, groupModuleData.getCheckDwgFiles());
                contentValues.put(MAP_LIST_FLOOR_PLAN_ICON, groupModuleData.getFloorPlanIcon());
                contentValues.put(MAP_LIST_IMAGES, groupModuleData.getImages());
                contentValues.put(MAP_LIST_GOOGLE_MAP_ICON, groupModuleData.getGoogleMapIcon());
                contentValues.put(MAP_LIST_INCLUDEMAP, groupModuleData.getIncludeMap());
                contentValues.put(MAP_LIST_LAT, groupModuleData.getLat());
                contentValues.put(MAP_LIST_LONG, groupModuleData.getLong());
                contentValues.put(MAP_LIST_LAT_LONG, groupModuleData.getLatLong());
                contentValues.put(MAP_LIST_MODULEID, groupModuleData.getId());
                contentValues.put(MAP_LIST_TITLE, groupModuleData.getMapTitle());
                db.insert(TABLE_MAP_List, null, contentValues);

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }


    public boolean insertUpdateModuleData(String menuId, String menuName, String date, String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(UPDATE_MODULE_EVENTID, eventId);
            contentValues.put(Update_DATE, date);
            contentValues.put(Update_MODULE_NAME, menuName);
            contentValues.put(Update_MODULE_ID, menuId);

            db.insert(TABLE_UPDATE_MODULE, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }

    public boolean isUpdateDataExist(String Event_id, String menuId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_UPDATE_MODULE + " WHERE " + UPDATE_MODULE_EVENTID + " = '" + Event_id
                + "' AND " + Update_MODULE_ID + " = '" + menuId + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getUpdateModuleData(String Event_id, String MenuId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_UPDATE_MODULE + " WHERE " + UPDATE_MODULE_EVENTID + " = '" + Event_id
                + "' AND " + Update_MODULE_ID + " = '" + MenuId + "'", null);
        return cursor;
    }

    public void deleteUpdateModuleData(String eventId, String menuId) {

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry = "delete from " + TABLE_UPDATE_MODULE + " WHERE " + UPDATE_MODULE_EVENTID + " = '" + eventId
                    + "' AND " + Update_MODULE_ID + " = '" + menuId + "'";


            db.execSQL(qry);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public ArrayList<GroupModuleData> getMainGroupData(String EventId, String menuId) {
        ArrayList<GroupModuleData> groupModuleDataArrayList = new ArrayList<GroupModuleData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_GROUP + " WHERE " + GROUP_MENU_ID + " = '" + menuId + "' AND " + GROUP_EVENTID + " = '" + EventId + "'";
//        String qry = "select * from " + TABLE_GROUP;
        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            GroupModuleData groupModuleData = new GroupModuleData();
            groupModuleData.setEventId(cursor.getString(cursor.getColumnIndex(GROUP_EVENTID)));
            groupModuleData.setGroupImage(cursor.getString(cursor.getColumnIndex(GROUP_ICON)));
            groupModuleData.setGroupName(cursor.getString(cursor.getColumnIndex(GROUP_NAME)));
            groupModuleData.setModuleGroupId(cursor.getString(cursor.getColumnIndex(GROUP_ID)));
            groupModuleData.setMenuId(cursor.getString(cursor.getColumnIndex(GROUP_MENU_ID)));
            groupModuleData.setSort_order(cursor.getString(cursor.getColumnIndex(GROUP_SORT_ORDER_NO)));
            groupModuleDataArrayList.add(groupModuleData);
        }
        return groupModuleDataArrayList;
    }


    public ArrayList<SuperGroupData> getSuperGroupData(String EventId, String menuId) {
        ArrayList<SuperGroupData> groupModuleDataArrayList = new ArrayList<SuperGroupData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_SUPERGROUP_LIST + " WHERE " + SUPERGROUP_MENUID + " = '" + menuId + "' AND " + SUPERGROUP_EVENTID + " = '" + EventId + "'"
                + " order by " + SUPERGROUP_GROUP_SORT_ORDERNO + "=0, CAST (" + SUPERGROUP_GROUP_SORT_ORDERNO + " AS INTEGER)" + " ASC ," + SUPERGROUP_GROUP_NAME;
//        String qry = "select * from " + TABLE_GROUP;
        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            SuperGroupData groupModuleData = new SuperGroupData();
            groupModuleData.setEventId(cursor.getString(cursor.getColumnIndex(SUPERGROUP_EVENTID)));
            groupModuleData.setGroupImage(cursor.getString(cursor.getColumnIndex(SUPERGROUP_GROUP_IMAGE)));
            groupModuleData.setGroupName(cursor.getString(cursor.getColumnIndex(SUPERGROUP_GROUP_NAME)));
            groupModuleData.setId(cursor.getString(cursor.getColumnIndex(SUPERGROUP_ID)));
            groupModuleData.setCreatedDate(cursor.getString(cursor.getColumnIndex(SUPERGROUP_GROUP_CREATEDATE)));
            groupModuleData.setUpdatedDate(cursor.getString(cursor.getColumnIndex(SUPERGROUP_GROUP_UPDATE_DATE)));
            groupModuleData.setMenuId(cursor.getString(cursor.getColumnIndex(SUPERGROUP_MENUID)));
            groupModuleData.setSort_order(cursor.getString(cursor.getColumnIndex(SUPERGROUP_GROUP_SORT_ORDERNO)));
            groupModuleDataArrayList.add(groupModuleData);
        }
        return groupModuleDataArrayList;
    }


    public ArrayList<GroupModuleData> getChildGroupData(String EventId, String superGroupId, String menuId) {
        ArrayList<GroupModuleData> groupModuleDataArrayList = new ArrayList<GroupModuleData>();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder qry = new StringBuilder();

        if (superGroupId.isEmpty()) {
            qry.append("select *  FROM " + TABLE_GROUP + " Where " + GROUP_EVENTID + " = '" + EventId + "'"
                    + " AND " + GROUP_MENU_ID + " = '" + menuId + "'"
                    + " order by " + GROUP_SORT_ORDER_NO + "=0, CAST (" + GROUP_SORT_ORDER_NO + " AS INTEGER)" + " ASC ," + GROUP_NAME
            );
        } else {
            qry.append("select *  FROM " + TABLE_GROUP + " m join " + TABLE_SUPERGROUP_RELATION + " sgr ON sgr." + SUPERGROUP_RELATION_CHILDGROUP_ID + " = m." + GROUP_ID + " Where sgr."
                    + SUPERGROUP_RELATION_SUPERGROUP_ID + " = '" + superGroupId + "'"
                    + " AND m." + GROUP_EVENTID + " = '" + EventId + "'"
                    + " AND m." + GROUP_MENU_ID + " = '" + menuId + "'"
                    + " order by m." + GROUP_SORT_ORDER_NO + "=0, CAST (m." + GROUP_SORT_ORDER_NO + " AS INTEGER)" + " ASC , m." + GROUP_NAME
            );
        }
        Cursor cursor = db.rawQuery(qry.toString(), null);
        while (cursor.moveToNext()) {
            GroupModuleData groupModuleData = new GroupModuleData();
            groupModuleData.setEventId(cursor.getString(cursor.getColumnIndex(GROUP_EVENTID)));
            groupModuleData.setGroupImage(cursor.getString(cursor.getColumnIndex(GROUP_ICON)));
            groupModuleData.setGroupName(cursor.getString(cursor.getColumnIndex(GROUP_NAME)));
            groupModuleData.setModuleGroupId(cursor.getString(cursor.getColumnIndex(GROUP_ID)));
            groupModuleData.setMenuId(cursor.getString(cursor.getColumnIndex(GROUP_MENU_ID)));
            groupModuleData.setSort_order(cursor.getString(cursor.getColumnIndex(GROUP_SORT_ORDER_NO)));
            groupModuleDataArrayList.add(groupModuleData);
        }
        return groupModuleDataArrayList;
    }


    public boolean isPrimaryCategoryAvailble(String EventId, String parentId) {

        SQLiteDatabase db = this.getReadableDatabase();
        String getCatType = "select *  FROM " + TABLE_AGENDA_CATEGORYLIST + " m join " + TABLE_GROUP_RELATION + " gr ON gr." + GROUP_RELATION_MODULEID + " = m." + AGENDA_CATEGORY_ID + " Where gr." + GROUP_RELATION_ID + " = " + parentId
                + " AND gr." + GROUP_RELATION_EVENTID + " = " + EventId
                + " AND m." + AGENDA_CATEGORY_TYPE + " = 1";
        Cursor cursor = db.rawQuery(getCatType, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public ArrayList<AgendaCategory> getAgendaCatListData(String EventId, String parentId, boolean isLogin, String allow_show_all_agenda, ArrayList<String> attendeAgenda) {
        ArrayList<AgendaCategory> mapNewDataArrayList = new ArrayList<AgendaCategory>();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder query = new StringBuilder();
        String qry = "";

        if (parentId.equalsIgnoreCase("")) {
            parentId = "''";
        }
        boolean isFoundCategoryType = isPrimaryCategoryAvailble(EventId, parentId);
        if (isLogin) {
            if (parentId.equalsIgnoreCase("")) {
                query.append("select *  FROM " + TABLE_AGENDA_CATEGORYLIST + " m join " + TABLE_GROUP_RELATION + " gr ON gr." + GROUP_RELATION_MODULEID + " = m." + AGENDA_CATEGORY_ID + " Where gr." + GROUP_RELATION_EVENTID + " = " + EventId);
            } else {
                query.append("select *  FROM " + TABLE_AGENDA_CATEGORYLIST + " m join " + TABLE_GROUP_RELATION + " gr ON gr." + GROUP_RELATION_MODULEID + " = m." + AGENDA_CATEGORY_ID + " Where gr." + GROUP_RELATION_ID + " = " + parentId
                        + " AND gr." + GROUP_RELATION_EVENTID + " = " + EventId);
            }

            if (allow_show_all_agenda.equalsIgnoreCase("0")) {
                if (attendeAgenda.size() > 0) {
                    StringBuilder sb = new StringBuilder();

                    sb.append("(");
                    for (int i = 0; i < attendeAgenda.size(); i++) {
                        String keyword = attendeAgenda.get(i);
                        sb.append("m." + AGENDA_CATEGORY_ID + " = '" + keyword + "'");
                        if (i != attendeAgenda.size() - 1)
                            sb.append(" OR ");
                    }
                    sb.append(")");
                    query.append(" AND " + sb.toString());

                } else {
                    if (isFoundCategoryType) {
                        query.append(" AND m." + AGENDA_CATEGORY_TYPE + " = 1");
                    } else {

                    }
                }
            } else {

            }
            query.append(" order by m." + AGENDA_CATEGORY_SortOrder + "=0, CAST (" + "m." + AGENDA_CATEGORY_SortOrder + " AS INTEGER)" + " ASC ");

        } else {

            if (isFoundCategoryType) {
                if (parentId.equalsIgnoreCase("")) {

                    query.append("select *  FROM " + TABLE_AGENDA_CATEGORYLIST + " m join " + TABLE_GROUP_RELATION + " gr ON gr." + GROUP_RELATION_MODULEID + " = m." + AGENDA_CATEGORY_ID + " Where gr." + GROUP_RELATION_EVENTID + " = " + EventId
                            + " AND m." + AGENDA_CATEGORY_TYPE + " = 1"
                            + " order by m." + AGENDA_CATEGORY_SortOrder + "=0, CAST (" + "m." + AGENDA_CATEGORY_SortOrder + " AS INTEGER)" + " ASC ");
                } else {
                    query.append("select *  FROM " + TABLE_AGENDA_CATEGORYLIST + " m join " + TABLE_GROUP_RELATION + " gr ON gr." + GROUP_RELATION_MODULEID + " = m." + AGENDA_CATEGORY_ID + " Where gr." + GROUP_RELATION_ID + " = " + parentId
                            + " AND gr." + GROUP_RELATION_EVENTID + " = " + EventId
                            + " AND m." + AGENDA_CATEGORY_TYPE + " = 1"
                            + " order by m." + AGENDA_CATEGORY_SortOrder + "=0, CAST (" + "m." + AGENDA_CATEGORY_SortOrder + " AS INTEGER)" + " ASC ");
                }
            } else {
                if (parentId.equalsIgnoreCase("")) {

                    query.append("select *  FROM " + TABLE_AGENDA_CATEGORYLIST + " m join " + TABLE_GROUP_RELATION + " gr ON gr." + GROUP_RELATION_MODULEID + " = m." + AGENDA_CATEGORY_ID + " Where gr." + GROUP_RELATION_EVENTID + " = " + EventId
                            + " order by m." + AGENDA_CATEGORY_SortOrder + "=0, CAST (" + "m." + AGENDA_CATEGORY_SortOrder + " AS INTEGER)" + " ASC ");
                } else {
                    query.append("select *  FROM " + TABLE_AGENDA_CATEGORYLIST + " m join " + TABLE_GROUP_RELATION + " gr ON gr." + GROUP_RELATION_MODULEID + " = m." + AGENDA_CATEGORY_ID + " Where gr." + GROUP_RELATION_ID + " = " + parentId + " AND gr." + GROUP_RELATION_EVENTID + " = " + EventId
                            + " order by m." + AGENDA_CATEGORY_SortOrder + "=0, CAST (" + "m." + AGENDA_CATEGORY_SortOrder + " AS INTEGER)" + " ASC ");
                }
            }

        }

//        Log.e("AgendaCatListData", "quer: " + query.toString());

        Cursor cursor = db.rawQuery(query.toString(), null);
        while (cursor.moveToNext()) {
            AgendaCategory mapNewData = new AgendaCategory();
            mapNewData.setId(cursor.getString(cursor.getColumnIndex(AGENDA_CATEGORY_ID)));
            mapNewData.setEventId(cursor.getString(cursor.getColumnIndex(AGENDA_CATEGORY_EVENTID)));
            mapNewData.setCategoryName(cursor.getString(cursor.getColumnIndex(AGENDA_CATEGORY_NAME)));
            mapNewData.setCategorieType(cursor.getString(cursor.getColumnIndex(AGENDA_CATEGORY_TYPE)));
            mapNewData.setCategoryImage(cursor.getString(cursor.getColumnIndex(AGENDA_CATEGORY_IMAGE)));
            mapNewData.setSort_order(cursor.getString(cursor.getColumnIndex(AGENDA_CATEGORY_SortOrder)));
            mapNewData.setWelcome_screen(cursor.getString(cursor.getColumnIndex(AGENDA_CATEGORY_WELCOME_SCREEN)));

            mapNewDataArrayList.add(mapNewData);
        }


        return mapNewDataArrayList;
    }

    public ArrayList<Agenda> getAgendaListDataByType(String EventId, String typeId, String categoryId) {
        ArrayList<Agenda> mapNewDataArrayList = new ArrayList<Agenda>();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder query = new StringBuilder();
        String qry = "";
        if (!categoryId.isEmpty()) {

            query.append("SELECT * FROM " + TABLE_AGENDA_LIST + " m join " + TABLE_AGENDA_CATEGORY_RELATION
                    + " gr ON gr." + AGENDA_CAT_RELATION_AGENDAID + " = m." + AGENDA_AGENDAID
                    + " Where gr." + AGENDA_CAT_RELATION_CATID + " = " + categoryId
                    + " AND gr." + AGENDA_EVENTID + " = " + EventId
                    + " AND (','||m." + AGENDA_SESSION_TRACK + "||',') LIKE '%," + typeId + ",%'"
                    + " order by " + AGENDA_STARTDATE + " ASC, " + AGENDA_STARTTIME
                    + " ASC, " + AGENDA_ENDTIME + " ASC," + AGENDA_HEADING + " ASC"
            );
        } else {
            query.append("SELECT * FROM " + TABLE_AGENDA_LIST + " m join " + TABLE_AGENDA_CATEGORY_RELATION
                    + " gr ON gr." + AGENDA_CAT_RELATION_AGENDAID + " = m." + AGENDA_AGENDAID
                    + " Where gr." + AGENDA_EVENTID + " = " + EventId
                    + " AND (','||m." + AGENDA_SESSION_TRACK + "||',') LIKE '%," + typeId + ",%'"
                    + " order by " + AGENDA_STARTDATE + " ASC, " + AGENDA_STARTTIME
                    + " ASC, " + AGENDA_ENDTIME + " ASC," + AGENDA_HEADING + " ASC"
            );
        }

//        Log.e("AgendaCatListData", "quer: " + query.toString());

        Cursor cursor = db.rawQuery(query.toString(), null);
        while (cursor.moveToNext()) {
            Agenda mapNewData = new Agenda();
            mapNewData.setAgendaId(cursor.getString(cursor.getColumnIndex(AGENDA_AGENDAID)));
            mapNewData.setHeading(cursor.getString(cursor.getColumnIndex(AGENDA_HEADING)));
            mapNewData.setSpeaker(cursor.getString(cursor.getColumnIndex(AGENDA_SPEAKER)));
            mapNewData.setLocation(cursor.getString(cursor.getColumnIndex(AGENDA_LOCATION)));
            mapNewData.setSessionImage(cursor.getString(cursor.getColumnIndex(AGENDA_SESSION_IMAGE)));
            mapNewData.setStartDate(cursor.getString(cursor.getColumnIndex(AGENDA_STARTDATE)));
            mapNewData.setEndDate(cursor.getString(cursor.getColumnIndex(AGENDA_ENDDATE)));
            mapNewData.setStartTime(cursor.getString(cursor.getColumnIndex(AGENDA_STARTTIME)));
            mapNewData.setEndTime(cursor.getString(cursor.getColumnIndex(AGENDA_ENDTIME)));
            mapNewData.setTypes(cursor.getString(cursor.getColumnIndex(AGENDA_TYPENAME)));
            mapNewData.setTypeId(cursor.getString(cursor.getColumnIndex(AGENDA_TYPEID)));
            mapNewData.setSort_order(cursor.getString(cursor.getColumnIndex(AGENDA_SORT_ORDER)));
            mapNewData.setSession_tracks(cursor.getString(cursor.getColumnIndex(AGENDA_SESSION_TRACK)));

            mapNewDataArrayList.add(mapNewData);
        }
        return mapNewDataArrayList;
    }

    public ArrayList<Agenda> getAgendaListDataByTimeOrderByTime(String EventId, String startDate, String categoryId) {
        ArrayList<Agenda> mapNewDataArrayList = new ArrayList<Agenda>();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder query = new StringBuilder();
        String qry = "";

        ArrayList<String> agendaIdArray = new ArrayList<>();
        if (categoryId.isEmpty()) {
            query.append("select * from " + TABLE_AGENDA_LIST + " WHERE "
                    + AGENDA_STARTDATE
                    + " LIKE '%" + startDate + "%'"
                    + " AND " + AGENDA_EVENTID + " = '" + EventId + "'"
                    + " order by " + AGENDA_STARTTIME + " ASC, " + AGENDA_ENDTIME + " ASC ," + AGENDA_HEADING + " ASC"

            );
        } else {

            query.append("SELECT * FROM " + TABLE_AGENDA_LIST + " a join " + TABLE_AGENDA_CATEGORY_RELATION
                    + " acr ON acr." + AGENDA_CAT_RELATION_AGENDAID + " = a." + AGENDA_AGENDAID
                    + " join " + TABLE_AGENDA_CATEGORYLIST + " ac ON ac." + AGENDA_CATEGORY_ID + " = acr." + AGENDA_CAT_RELATION_CATID
                    + " Where ac." + AGENDA_CATEGORY_ID + " = '" + categoryId + "'"
                    + " AND a." + AGENDA_EVENTID + " = '" + EventId + "'"
                    + " AND " + AGENDA_STARTDATE + " = '" + startDate + "'"
                    + " order by " + AGENDA_STARTTIME + " ASC, " + AGENDA_ENDTIME + " ASC ," + AGENDA_HEADING + " ASC"
            );
        }

//        Log.e("AgendaCatListData", "quer: " + query.toString());

        Cursor cursor = db.rawQuery(query.toString(), null);
        while (cursor.moveToNext()) {
            Agenda mapNewData = new Agenda();

            mapNewData.setAgendaId(cursor.getString(cursor.getColumnIndex(AGENDA_AGENDAID)));
            mapNewData.setHeading(cursor.getString(cursor.getColumnIndex(AGENDA_HEADING)));
            mapNewData.setSpeaker(cursor.getString(cursor.getColumnIndex(AGENDA_SPEAKER)));
            mapNewData.setLocation(cursor.getString(cursor.getColumnIndex(AGENDA_LOCATION)));
            mapNewData.setSessionImage(cursor.getString(cursor.getColumnIndex(AGENDA_SESSION_IMAGE)));
            mapNewData.setStartDate(cursor.getString(cursor.getColumnIndex(AGENDA_STARTDATE)));
            mapNewData.setEndDate(cursor.getString(cursor.getColumnIndex(AGENDA_ENDDATE)));
            mapNewData.setStartTime(cursor.getString(cursor.getColumnIndex(AGENDA_STARTTIME)));
            mapNewData.setEndTime(cursor.getString(cursor.getColumnIndex(AGENDA_ENDTIME)));
            mapNewData.setTypes(cursor.getString(cursor.getColumnIndex(AGENDA_TYPENAME)));
            mapNewData.setTypeId(cursor.getString(cursor.getColumnIndex(AGENDA_TYPEID)));
            mapNewData.setSort_order(cursor.getString(cursor.getColumnIndex(AGENDA_SORT_ORDER)));
            mapNewData.setSession_tracks(cursor.getString(cursor.getColumnIndex(AGENDA_SESSION_TRACK)));

            mapNewDataArrayList.add(mapNewData);
        }
        return mapNewDataArrayList;
    }


    public ArrayList<Agenda> getAgendaListDataByTime(String EventId, String startDate, String categoryId) {
        ArrayList<Agenda> mapNewDataArrayList = new ArrayList<Agenda>();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder query = new StringBuilder();
        String qry = "";

        ArrayList<String> agendaIdArray = new ArrayList<>();
        if (categoryId.isEmpty()) {
            query.append("select * from " + TABLE_AGENDA_LIST + " WHERE "
                    + AGENDA_STARTDATE
                    + " LIKE '%" + startDate + "%'"
                    + " AND " + AGENDA_EVENTID + " = '" + EventId + "'"
            );
        } else {

            query.append("SELECT * FROM " + TABLE_AGENDA_LIST + " a join " + TABLE_AGENDA_CATEGORY_RELATION
                    + " acr ON acr." + AGENDA_CAT_RELATION_AGENDAID + " = a." + AGENDA_AGENDAID
                    + " join " + TABLE_AGENDA_CATEGORYLIST + " ac ON ac." + AGENDA_CATEGORY_ID + " = acr." + AGENDA_CAT_RELATION_CATID
                    + " Where ac." + AGENDA_CATEGORY_ID + " = '" + categoryId + "'"
                    + " AND a." + AGENDA_EVENTID + " = '" + EventId + "'"
                    + " AND " + AGENDA_STARTDATE + " = '" + startDate + "'"
            );
        }

//        Log.e("AgendaCatListData", "quer: " + query.toString());

        Cursor cursor = db.rawQuery(query.toString(), null);
        while (cursor.moveToNext()) {
            Agenda mapNewData = new Agenda();

            mapNewData.setAgendaId(cursor.getString(cursor.getColumnIndex(AGENDA_AGENDAID)));
            mapNewData.setHeading(cursor.getString(cursor.getColumnIndex(AGENDA_HEADING)));
            mapNewData.setSpeaker(cursor.getString(cursor.getColumnIndex(AGENDA_SPEAKER)));
            mapNewData.setLocation(cursor.getString(cursor.getColumnIndex(AGENDA_LOCATION)));
            mapNewData.setSessionImage(cursor.getString(cursor.getColumnIndex(AGENDA_SESSION_IMAGE)));
            mapNewData.setStartDate(cursor.getString(cursor.getColumnIndex(AGENDA_STARTDATE)));
            mapNewData.setEndDate(cursor.getString(cursor.getColumnIndex(AGENDA_ENDDATE)));
            mapNewData.setStartTime(cursor.getString(cursor.getColumnIndex(AGENDA_STARTTIME)));
            mapNewData.setEndTime(cursor.getString(cursor.getColumnIndex(AGENDA_ENDTIME)));
            mapNewData.setTypes(cursor.getString(cursor.getColumnIndex(AGENDA_TYPENAME)));
            mapNewData.setTypeId(cursor.getString(cursor.getColumnIndex(AGENDA_TYPEID)));
            mapNewData.setSort_order(cursor.getString(cursor.getColumnIndex(AGENDA_SORT_ORDER)));
            mapNewData.setSession_tracks(cursor.getString(cursor.getColumnIndex(AGENDA_SESSION_TRACK)));

            mapNewDataArrayList.add(mapNewData);
        }
        return mapNewDataArrayList;
    }


    public String getAgendaWelcomeScreenData(String eventId, String agendaCategoryId) {
        String welcomeData = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "";

        if (agendaCategoryId.isEmpty()) {
            welcomeData = "";
        } else {
            query = "SELECT *" + " FROM " + TABLE_AGENDA_CATEGORYLIST
                    + " Where " + AGENDA_CATEGORY_ID + " = '" + agendaCategoryId + "'"
                    + " AND " + AGENDA_CATEGORY_EVENTID + " = '" + eventId + "'";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    welcomeData = cursor.getString(cursor.getColumnIndex(AGENDA_CATEGORY_WELCOME_SCREEN));
                }

            } else
                welcomeData = "";
        }
        return welcomeData;
    }


    public ArrayList<String> getAgendaTimeList(String EventId, String categoryId) {
        ArrayList<String> mapNewDataArrayList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder query = new StringBuilder();
        String qry = "";
        if (!categoryId.isEmpty()) {
            query.append("SELECT DISTINCT a." + AGENDA_STARTDATE + " FROM " + TABLE_AGENDA_LIST + " a join " + TABLE_AGENDA_CATEGORY_RELATION
                    + " acr ON acr." + AGENDA_CAT_RELATION_AGENDAID + " = a." + AGENDA_AGENDAID
                    + " join " + TABLE_AGENDA_CATEGORYLIST + " ac ON ac." + AGENDA_CATEGORY_ID + " = acr." + AGENDA_CAT_RELATION_CATID + ""
                    + " Where ac." + AGENDA_CATEGORY_ID + " = " + categoryId
                    + " AND a." + AGENDA_EVENTID + " = " + EventId
                    + " order by a." + AGENDA_STARTDATE + " ASC"
            );
        } else {
            query.append("SELECT DISTINCT a." + AGENDA_STARTDATE + " FROM " + TABLE_AGENDA_LIST + " a join " + TABLE_AGENDA_CATEGORY_RELATION
                    + " acr ON acr." + AGENDA_CAT_RELATION_AGENDAID + " = a." + AGENDA_AGENDAID
                    + " join " + TABLE_AGENDA_CATEGORYLIST + " ac ON ac." + AGENDA_CATEGORY_ID + " = acr." + AGENDA_CAT_RELATION_CATID + ""
                    + " Where a." + AGENDA_EVENTID + " = " + EventId
                    + " order by a." + AGENDA_STARTDATE + " ASC"
            );
        }


//        Log.e("AgendaCatListData", "quer: " + query.toString());

        Cursor cursor = db.rawQuery(query.toString(), null);
        while (cursor.moveToNext()) {
            mapNewDataArrayList.add(cursor.getString(cursor.getColumnIndex(AGENDA_STARTDATE)));

        }
        return mapNewDataArrayList;
    }

    public ArrayList<AgendaType> getAgendaTypeList(String EventId) {
        ArrayList<AgendaType> mapNewDataArrayList = new ArrayList<AgendaType>();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder query = new StringBuilder();
        String qry = "";
        query.append("select * from " + TABLE_AGENDA_TYPELIST + " WHERE "
                + AGENDA_TYPELIST_EVENTID + " = '" + EventId
                + "'"
                + " order by " + AGENDA_TYPELIST_ORDERNO + "=0, CAST (" + AGENDA_TYPELIST_ORDERNO + " AS INTEGER)" + " ASC"
        );
//        Log.e("AgendaCatListData", "quer: " + query.toString());

        Cursor cursor = db.rawQuery(query.toString(), null);
        while (cursor.moveToNext()) {
            AgendaType agendaType = new AgendaType();

            agendaType.setEventId(EventId);
            agendaType.setTypeId(cursor.getString(cursor.getColumnIndex(AGENDA_TYPELIST_TYPEID)));
            agendaType.setTypeName(cursor.getString(cursor.getColumnIndex(AGENDA_TYPELIST_TYPENAME)));
            agendaType.setOrderNo(cursor.getString(cursor.getColumnIndex(AGENDA_TYPELIST_ORDERNO)));
            mapNewDataArrayList.add(agendaType);

        }
        return mapNewDataArrayList;
    }

    public ArrayList<Agenda> getAgendaTimeListNew(String EventId, String categoryId) {
        ArrayList<Agenda> mapNewDataArrayList = new ArrayList<Agenda>();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder query = new StringBuilder();
        String qry = "";
        if (!categoryId.isEmpty()) {
            query.append("SELECT DISTINCT a." + AGENDA_STARTDATE + " FROM " + TABLE_AGENDA_LIST + " a join " + TABLE_AGENDA_CATEGORY_RELATION
                    + " acr ON acr." + AGENDA_CAT_RELATION_AGENDAID + " = a." + AGENDA_AGENDAID
                    + " join " + TABLE_AGENDA_CATEGORYLIST + " ac ON ac." + AGENDA_CATEGORY_ID + " = acr." + AGENDA_CAT_RELATION_CATID + ""
                    + " Where ac." + AGENDA_CATEGORY_ID + " = " + categoryId
                    + " AND a." + AGENDA_EVENTID + " = " + EventId);
        } else {
            query.append("SELECT DISTINCT a." + AGENDA_STARTDATE + " FROM " + TABLE_AGENDA_LIST + " a join " + TABLE_AGENDA_CATEGORY_RELATION
                    + " acr ON acr." + AGENDA_CAT_RELATION_AGENDAID + " = a." + AGENDA_AGENDAID
                    + " join " + TABLE_AGENDA_CATEGORYLIST + " ac ON ac." + AGENDA_CATEGORY_ID + " = acr." + AGENDA_CAT_RELATION_CATID + ""
                    + " Where a." + AGENDA_EVENTID + " = " + EventId);
        }


//        Log.e("AgendaCatListData", "quer: " + query.toString());

        Cursor cursor = db.rawQuery(query.toString(), null);
        while (cursor.moveToNext()) {
            Agenda mapNewData = new Agenda();
            mapNewData.setAgendaId(cursor.getString(cursor.getColumnIndex(AGENDA_AGENDAID)));
            mapNewData.setHeading(cursor.getString(cursor.getColumnIndex(AGENDA_HEADING)));
            mapNewData.setSpeaker(cursor.getString(cursor.getColumnIndex(AGENDA_SPEAKER)));
            mapNewData.setLocation(cursor.getString(cursor.getColumnIndex(AGENDA_LOCATION)));
            mapNewData.setSessionImage(cursor.getString(cursor.getColumnIndex(AGENDA_SESSION_IMAGE)));
            mapNewData.setStartDate(cursor.getString(cursor.getColumnIndex(AGENDA_STARTDATE)));
            mapNewData.setEndDate(cursor.getString(cursor.getColumnIndex(AGENDA_ENDDATE)));
            mapNewData.setStartTime(cursor.getString(cursor.getColumnIndex(AGENDA_STARTTIME)));
            mapNewData.setEndTime(cursor.getString(cursor.getColumnIndex(AGENDA_ENDTIME)));
            mapNewData.setSort_order(cursor.getString(cursor.getColumnIndex(AGENDA_SORT_ORDER)));
            mapNewData.setSession_tracks(cursor.getString(cursor.getColumnIndex(AGENDA_SESSION_TRACK)));

            mapNewDataArrayList.add(mapNewData);
        }
        return mapNewDataArrayList;
    }

    public ArrayList<MapListData.MapNewData> getMapListData(String EventId, String parentId) {
        ArrayList<MapListData.MapNewData> mapNewDataArrayList = new ArrayList<MapListData.MapNewData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "";
        if (parentId.equalsIgnoreCase("")) {
            qry = "select *  FROM " + TABLE_MAP_List + " Where " + MAP_LIST_EventId + " = " + EventId;
        } else {
            qry = "select *  FROM " + TABLE_MAP_List + " m join " + TABLE_GROUP_RELATION + " gr ON gr." + GROUP_RELATION_MODULEID + " = m." + MAP_LIST_MODULEID + " Where gr." + GROUP_RELATION_ID + " = " + parentId
                    + " AND gr." + GROUP_RELATION_EVENTID + " = " + EventId;
        }
        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            MapListData.MapNewData mapNewData = new MapListData().new MapNewData();
            mapNewData.setAddress(cursor.getString(cursor.getColumnIndex(MAP_LIST_ADDRESS)));
            mapNewData.setCheckDwgFiles(cursor.getString(cursor.getColumnIndex(MAP_LIST_CHECK_DWGFILES)));
            mapNewData.setFloorPlanIcon(cursor.getString(cursor.getColumnIndex(MAP_LIST_FLOOR_PLAN_ICON)));
            mapNewData.setGoogleMapIcon(cursor.getString(cursor.getColumnIndex(MAP_LIST_GOOGLE_MAP_ICON)));
            mapNewData.setId(cursor.getString(cursor.getColumnIndex(MAP_LIST_MODULEID)));
            mapNewData.setImages(cursor.getString(cursor.getColumnIndex(MAP_LIST_IMAGES)));
            mapNewData.setIncludeMap(cursor.getString(cursor.getColumnIndex(MAP_LIST_INCLUDEMAP)));
            mapNewData.setLat(cursor.getString(cursor.getColumnIndex(MAP_LIST_LAT)));
            mapNewData.setLong(cursor.getString(cursor.getColumnIndex(MAP_LIST_LONG)));
            mapNewData.setLatLong(cursor.getString(cursor.getColumnIndex(MAP_LIST_LAT_LONG)));
            mapNewData.setMapTitle(cursor.getString(cursor.getColumnIndex(MAP_LIST_TITLE)));
            mapNewDataArrayList.add(mapNewData);
        }
        return mapNewDataArrayList;
    }


    public ArrayList<SpeakerList.SpeakerData> getSpeakerListData(String EventId) {
        ArrayList<SpeakerList.SpeakerData> mapNewDataArrayList = new ArrayList<SpeakerList.SpeakerData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "";
        qry = "select *  FROM " + TABLE_SPEAKER_LIST + " Where " + SPEAKER_LIST_EVENTID + " = '" + EventId + "'";
        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            SpeakerList.SpeakerData speakerData = new SpeakerList().new SpeakerData();
            speakerData.setCompanyName(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_COMAPANYNAME)));
            speakerData.setEvent_id(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_EVENTID)));
            speakerData.setUser_id(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_USERID)));
            speakerData.setEmail(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_SPEAKER_EMAIL)));
            speakerData.setId(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_ID)));
            speakerData.setLogo(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_LOGO)));
            speakerData.setFirstname(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_FIRST_NAME)));
            speakerData.setLastname(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_LAST_NAME)));
            speakerData.setHeading(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_SPEAKER_HEADING)));
            speakerData.setIsFavorites(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_FAVOURITE)));
            speakerData.setSpeakerDesc(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_SPEAKER_DESC)));
            speakerData.setTitle(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_TITLE)));
            mapNewDataArrayList.add(speakerData);
        }
        return mapNewDataArrayList;
    }


    public boolean isSameSpeakerUser(String EventId, String usreId) {
        boolean isSameUser = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_SPEAKER_LIST + " WHERE " + SPEAKER_LIST_EVENTID + " = '" + EventId + "'";
        Cursor cursor = db.rawQuery(qry, null);
        if (cursor.moveToFirst()) {
            String user_id = cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_USERID));
            if (user_id.equalsIgnoreCase(usreId))
                isSameUser = true;
        }
        return isSameUser;
    }


    public void updateSpeakerUserID(String EventId, String usreId) {
//
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SPEAKER_LIST_USERID, usreId);
        int row = db.update(TABLE_SPEAKER_LIST, cv, SPEAKER_LIST_EVENTID + "= ?", new String[]{EventId});
        Log.e("database", "updateExhibitorUserID: " + row);
    }

    public ArrayList<CmsListData> getCMSListData(String EventId, String parentId) {
        ArrayList<CmsListData> cmsListDataArrayList = new ArrayList<CmsListData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "";

        if (parentId.equalsIgnoreCase("")) {
            qry = "select *  FROM " + TABLE_CMSLISTINGOffline + " Where " + CMSLISTING_Eventid + " = '" + EventId + "'";
        } else {
            qry = "select *  FROM " + TABLE_CMSLISTINGOffline + " Where " + CMSLISTING_group_id + " = '" + parentId + "'"
                    + " AND " + CMSLISTING_Eventid + " = '" + EventId + "'";
        }
        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            CmsListData cmsListData = new CmsListData();
            cmsListData.setCms_icon(cursor.getString(cursor.getColumnIndex(CMSLISTING_cms_icon)));
            cmsListData.setGroupId(cursor.getString(cursor.getColumnIndex(CMSLISTING_group_id)));
            cmsListData.setId(cursor.getString(cursor.getColumnIndex(CMSLISTING_ID)));
            cmsListData.setMenuName(cursor.getString(cursor.getColumnIndex(CMSLISTING_Menu_name)));
            cmsListDataArrayList.add(cmsListData);
        }
        return cmsListDataArrayList;
    }


    public boolean insertUpdateAllGroupModuleRelationData(ArrayList<GroupRelationModuleData> list, String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                GroupRelationModuleData groupModuleData = list.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(GROUP_RELATION_EVENTID, eventId);
                contentValues.put(GROUP_RELATION_ID, groupModuleData.getGroupId());
                contentValues.put(GROUP_RELATION_MENU_ID, groupModuleData.getMenuId());
                contentValues.put(GROUP_RELATION_MODULEID, groupModuleData.getModuleId());
//            int rows = db.update(TABLE_GROUP_RELATION, contentValues, GROUP_RELATION_EVENTID + " = ? AND " + GROUP_RELATION_ID + " = ? ", new String[]{eventId, groupModuleData.getGroupId()});
//            if (rows==0)
//            {
//                db.insert(TABLE_GROUP_RELATION, null, contentValues);
//            }
                db.insert(TABLE_GROUP_RELATION, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }

    public ArrayList<ExhibitorParentCategoryData> getParentCategoryList1(String EventId) {
        ArrayList<ExhibitorParentCategoryData> parentCategoryData = new ArrayList<ExhibitorParentCategoryData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_EXHIBITOR_PARENT_CATEGORY + " WHERE "
//                +SUB_CATEGORY_PARENT+" = '"+ parentId
//                +"' AND "+
                + PARENT_CATEGORY_EVENTID + " = '" + EventId + "'"
                + " order by " + PARENT_CATEGORY_SORT_ORDERNO + "=0, CAST (" + PARENT_CATEGORY_SORT_ORDERNO + " AS INTEGER)" + " ASC ," + PARENT_CATEGORY_NAME;
        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            ExhibitorParentCategoryData parentCategoryData1 = new ExhibitorParentCategoryData();
            parentCategoryData1.setC_id(cursor.getString(cursor.getColumnIndex(PARENT_CATEGORY_ID)));
            parentCategoryData1.setCategorie_icon(cursor.getString(cursor.getColumnIndex(PARENT_CATEGORY_ICON)));
            parentCategoryData1.setCategory(cursor.getString(cursor.getColumnIndex(PARENT_CATEGORY_NAME)));
            parentCategoryData1.setSort_order(cursor.getString(cursor.getColumnIndex(PARENT_CATEGORY_SORT_ORDERNO)));

            parentCategoryData.add(parentCategoryData1);
        }
        return parentCategoryData;
    }

    //SUB CATEGORY

    public boolean insertUpdateAllSubCategory(ArrayList<ExhibitorCategoryListing> list, String event_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                ExhibitorCategoryListing parentCategoryData = list.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(SUB_CATEGORY_EVENTID, event_Id);
                contentValues.put(SUB_CATEGORY_ID, parentCategoryData.getId());
                contentValues.put(SUB_CATEGORY_NAME, parentCategoryData.getSector());
                contentValues.put(SUB_CATEGORY_ICON, parentCategoryData.getImg());
                contentValues.put(SUB_CATEGORY_SHORT_DESC, parentCategoryData.getShortDesc());
                contentValues.put(SUB_CATEGORY_PARENT, parentCategoryData.getPcategory_id());
                contentValues.put(SUB_EXHI_CAT_GROUP_ID, parentCategoryData.getExhi_cat_group_id());

                db.insert(TABLE_EXHIBITOR_SUB_CATEGORY, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return true;
    }


    public ArrayList<ExhibitorType> getExhibitorParentCateogryTypes(String EventId) {
        ArrayList<ExhibitorType> parentCategoryData = new ArrayList<ExhibitorType>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_EXHIBITOR_TYPES + " WHERE " + EXHIBITOR_TYPE_EVENTID + " = '" + EventId
//                +"' AND "+EXHIBITOR_LIST_EVENTID+" = '"+EventId
//                +"' AND "+EXHIBITOR_LIST_USERID+" = '"+usreId
                + "'";
        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            ExhibitorType object = new ExhibitorType();
            object.setEvent_id(cursor.getString(cursor.getColumnIndex(EXHIBITOR_TYPE_EVENTID)));
            object.setExhibitorId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_TYPE_ID)));
            object.setExhibitorType("");
            object.setExhibitorTypeColor(cursor.getString(cursor.getColumnIndex(EXHIBITOR_TYPE_COLOR)));
            parentCategoryData.add(object);
        }
        return parentCategoryData;
    }


    public ArrayList<Attendance> getExhibitorFromChildProductFilterWholeFragment(String EventId, String usreId, ArrayList<ExhibitorType> typeId, ArrayList<String> countryId, ArrayList<String> parentKeywords, ArrayList<String> selectedCategoryId, String fromParentcatId) {
        ArrayList<Attendance> parentCategoryData = new ArrayList<Attendance>();
        ArrayList<String> exhibitorcatId = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();


        if (countryId.size() > 0 && parentKeywords.size() == 0) {

        } else if (countryId.size() == 0 && parentKeywords.size() == 0) {

        } else {
            StringBuilder catesb = new StringBuilder();
            StringBuilder query = new StringBuilder();
            catesb.append("(");
            for (int i = 0; i < selectedCategoryId.size(); i++) {
                String categoryId = selectedCategoryId.get(i);

                catesb.append("(");
                catesb.append(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID + " LIKE '%," + categoryId + ",%' OR ");
                catesb.append(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID + " LIKE '%," + categoryId + "' OR ");
                catesb.append(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID + " LIKE '" + categoryId + ",%' OR ");
                catesb.append(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID + " = '" + categoryId + "')");

                if (i != selectedCategoryId.size() - 1)
                    catesb.append(" OR ");
            }
            catesb.append(")");


            StringBuilder sb = new StringBuilder();
            sb.append("(");
            for (int i = 0; i < countryId.size(); i++) {
                String keyword = countryId.get(i);

                sb.append("(");
                sb.append(EXHIBITOR_LIST_COUNTRYID + " LIKE '%," + keyword + ",%' OR ");
                sb.append(EXHIBITOR_LIST_COUNTRYID + " LIKE '%," + keyword + "' OR ");
                sb.append(EXHIBITOR_LIST_COUNTRYID + " LIKE '" + keyword + ",%' OR ");
                sb.append(EXHIBITOR_LIST_COUNTRYID + " = '" + keyword + "')");

                if (i != countryId.size() - 1)
                    sb.append(" OR ");
            }
            sb.append(")");


            query.append("select * from " + TABLE_EXHIBITOR_LIST + " WHERE " + EXHIBITOR_LIST_EVENTID + " = '" + EventId
                            + "' AND " + EXHIBITOR_LIST_USERID + " = '" + usreId
                            + "' "
//                "AND " + EXHIBITOR_LIST_TYPEID + " = '" + typeId + "'"
            );

            if (selectedCategoryId.size() > 0) {
                query.append(" AND " + catesb.toString());
            }

            if (countryId.size() > 0) {
                query.append(" AND " + sb.toString());
            }


            Cursor cursor = db.rawQuery(query.toString(), null);

            while (cursor.moveToNext()) {
                Attendance object = new Attendance();

                exhibitorcatId.add(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_ID)));
                object.setEx_id(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_ID)));
                object.setEx_pageId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_PAGEID)));
                object.setEx_comapnyLogo(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_LOGO)));
                object.setEx_heading(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_HEADING)));

                object.setEx_desc(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_SHORTDESC)));
                object.setSponsored_category(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID)));
                object.setEx_stand_number(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_SATNDNUMBER)));
                object.setTypeId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_TYPEID)));
                object.setIs_fav(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_FAVOURITE)));
                object.setUserId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_USERID)));
                parentCategoryData.add(object);
            }
        }


        for (int j = 0; j < typeId.size(); j++) {

            StringBuilder parent = new StringBuilder();
            StringBuilder parentFilterBuilder = new StringBuilder();
            StringBuilder sb = new StringBuilder();
            StringBuilder queryKeywordData = new StringBuilder();
            if (countryId.size() > 0) {

                sb.append("(");
                for (int i = 0; i < countryId.size(); i++) {
                    String keyword = countryId.get(i);
                    sb.append(EXHIBITOR_LIST_COUNTRYID + " = '" + keyword + "'");
                    if (i != countryId.size() - 1)
                        sb.append(" OR ");
                }
                sb.append(")");
            }
            parent.append("(");
            for (int i = 0; i < parentKeywords.size(); i++) {
                String keyword = parentKeywords.get(i).replace("\'", "\'\'");

                parent.append("(");
                parent.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '%," + keyword + ",%' OR ");
                parent.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '%," + keyword + "' OR ");
                parent.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '" + keyword + ",%' OR ");
                parent.append(EXHIBITOR_LIST_SHORTDESC + " = '" + keyword + "')");

                if (i != parentKeywords.size() - 1)
                    parent.append(" OR ");
            }
            parent.append(")");


            ArrayList<String> fromParentSearch = new ArrayList<>();
            if (!fromParentcatId.isEmpty()) {
                ArrayList<ExhibitorCategoryListing> categoryListings = getSubCategoryList1(fromParentcatId, EventId);

                for (int i = 0; i < categoryListings.size(); i++) {
                    ExhibitorCategoryListing listing = categoryListings.get(i);
                    String[] categoryKeywords = listing.getShortDesc().split(",");
//            new ArrayList<String>(Arrays.asList(strings));
                    fromParentSearch.addAll(new ArrayList<String>(Arrays.asList(categoryKeywords)));
                }
            }


            parentFilterBuilder.append("(");
            for (int i = 0; i < fromParentSearch.size(); i++) {
                String keyword = fromParentSearch.get(i).replace("\'", "\'\'");

                parentFilterBuilder.append("(");
                parentFilterBuilder.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '%," + keyword + ",%' OR ");
                parentFilterBuilder.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '%," + keyword + "' OR ");
                parentFilterBuilder.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '" + keyword + ",%' OR ");
                parentFilterBuilder.append(EXHIBITOR_LIST_SHORTDESC + " = '" + keyword + "')");

                if (i != fromParentSearch.size() - 1)
                    parentFilterBuilder.append(" OR ");
            }
            parentFilterBuilder.append(")");


            queryKeywordData.append("select * from " + TABLE_EXHIBITOR_LIST + " WHERE " + EXHIBITOR_LIST_EVENTID + " = '" + EventId
                    + "' AND " + EXHIBITOR_LIST_USERID + " = '" + usreId
                    + "' AND " + EXHIBITOR_LIST_TYPEID + " = '" + typeId.get(j).getExhibitorId() + "'");
            if (countryId.size() > 0) {
                queryKeywordData.append(" AND " + sb.toString());
            }
            if (parentKeywords.size() > 0) {
                queryKeywordData.append(" AND " + parent.toString());
            }
            if (fromParentSearch.size() > 0) {
                queryKeywordData.append(" AND " + parentFilterBuilder.toString());
            }


            Cursor cursorKeyword = db.rawQuery(queryKeywordData.toString(), null);
            while (cursorKeyword.moveToNext()) {
                Attendance object = new Attendance();

                if (!(exhibitorcatId.contains(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_ID))))) {
                    object.setEx_id(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_ID)));
                    object.setEx_pageId(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_PAGEID)));
                    object.setEx_comapnyLogo(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_LOGO)));
                    object.setEx_heading(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_HEADING)));

                    object.setEx_desc(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_SHORTDESC)));
                    object.setSponsored_category(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID)));
                    object.setEx_stand_number(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_SATNDNUMBER)));
                    object.setTypeId(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_TYPEID)));
                    object.setIs_fav(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_FAVOURITE)));
                    object.setUserId(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_USERID)));

                    parentCategoryData.add(object);
                }
            }
        }
        return parentCategoryData;
    }

    public ArrayList<Attendance> getExhibitorFromChildProductFiltersponsor(String EventId, String usreId, ArrayList<ExhibitorType> typeId, ArrayList<String> countryId, ArrayList<String> parentKeywords, ArrayList<String> selectedCategoryId) {
        ArrayList<Attendance> parentCategoryData = new ArrayList<Attendance>();
        ArrayList<String> exhibitorcatId = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        StringBuilder catesb = new StringBuilder();
        StringBuilder keywordsb = new StringBuilder();
        StringBuilder query = new StringBuilder();
        catesb.append("(");
        for (int i = 0; i < selectedCategoryId.size(); i++) {
            String categoryId = selectedCategoryId.get(i);

            catesb.append("(");
            catesb.append(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID + " LIKE '%," + categoryId + ",%' OR ");
            catesb.append(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID + " LIKE '%," + categoryId + "' OR ");
            catesb.append(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID + " LIKE '" + categoryId + ",%' OR ");
            catesb.append(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID + " = '" + categoryId + "')");

            if (i != selectedCategoryId.size() - 1)
                catesb.append(" OR ");
        }
        catesb.append(")");

        keywordsb.append("(");
        for (int i = 0; i < parentKeywords.size(); i++) {
            String keyword = parentKeywords.get(i).replace("\'", "\'\'");

            keywordsb.append("(");
            keywordsb.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '%," + keyword + ",%' OR ");
            keywordsb.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '%," + keyword + "' OR ");
            keywordsb.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '" + keyword + ",%' OR ");
            keywordsb.append(EXHIBITOR_LIST_SHORTDESC + " = '" + keyword + "')");

            if (i != parentKeywords.size() - 1)
                keywordsb.append(" OR ");
        }
        keywordsb.append(")");

        query.append("select * from " + TABLE_EXHIBITOR_LIST + " WHERE " + EXHIBITOR_LIST_EVENTID + " = '" + EventId
                + "' AND " + EXHIBITOR_LIST_USERID + " = '" + usreId
                + "' "
        );
        if (selectedCategoryId.size() > 0) {
            query.append(" AND " + catesb.toString());
        }
        if (parentKeywords.size() > 0) {
            query.append(" AND " + keywordsb.toString());
        }
        Cursor cursor = db.rawQuery(query.toString(), null);
        while (cursor.moveToNext()) {
            Attendance object = new Attendance();

            exhibitorcatId.add(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_ID)));
            object.setEx_id(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_ID)));
            object.setEx_pageId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_PAGEID)));
            object.setEx_comapnyLogo(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_LOGO)));
            object.setEx_heading(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_HEADING)));

            object.setEx_desc(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_SHORTDESC)));
            object.setSponsored_category(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID)));
            object.setEx_stand_number(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_SATNDNUMBER)));
            object.setTypeId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_TYPEID)));
            object.setIs_fav(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_FAVOURITE)));
            object.setUserId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_USERID)));
            parentCategoryData.add(object);
        }

        for (int j = 0; j < typeId.size(); j++) {
            StringBuilder parent = new StringBuilder();
            StringBuilder sb = new StringBuilder();
            StringBuilder queryKeywordData = new StringBuilder();
            if (countryId.size() > 0) {

                sb.append("(");
                for (int i = 0; i < countryId.size(); i++) {
                    String keyword = countryId.get(i);
                    sb.append(EXHIBITOR_LIST_COUNTRYID + " = '" + keyword + "'");
                    if (i != countryId.size() - 1)
                        sb.append(" OR ");
                }
                sb.append(")");
            }
            parent.append("(");
            for (int i = 0; i < parentKeywords.size(); i++) {
                String keyword = parentKeywords.get(i).replace("\'", "\'\'");

                parent.append("(");
                parent.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '%," + keyword + ",%' OR ");
                parent.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '%," + keyword + "' OR ");
                parent.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '" + keyword + ",%' OR ");
                parent.append(EXHIBITOR_LIST_SHORTDESC + " = '" + keyword + "')");

                if (i != parentKeywords.size() - 1)
                    parent.append(" OR ");
            }
            parent.append(")");


            queryKeywordData.append("select * from " + TABLE_EXHIBITOR_LIST + " WHERE " + EXHIBITOR_LIST_EVENTID + " = '" + EventId
                    + "' AND " + EXHIBITOR_LIST_USERID + " = '" + usreId
                    + "' AND " + EXHIBITOR_LIST_TYPEID + " = '" + typeId.get(j).getExhibitorId() + "'");
            if (countryId.size() > 0) {
                queryKeywordData.append(" AND " + sb.toString());
            }
            if (parentKeywords.size() > 0) {
                queryKeywordData.append(" AND " + parent.toString());
            }


            Cursor cursorKeyword = db.rawQuery(queryKeywordData.toString(), null);
            while (cursorKeyword.moveToNext()) {
                Attendance object = new Attendance();

                if (!(exhibitorcatId.contains(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_ID))))) {
                    object.setEx_id(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_ID)));
                    object.setEx_pageId(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_PAGEID)));
                    object.setEx_comapnyLogo(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_LOGO)));
                    object.setEx_heading(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_HEADING)));

                    object.setEx_desc(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_SHORTDESC)));
                    object.setSponsored_category(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID)));
                    object.setEx_stand_number(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_SATNDNUMBER)));
                    object.setTypeId(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_TYPEID)));
                    object.setIs_fav(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_FAVOURITE)));
                    object.setUserId(cursorKeyword.getString(cursorKeyword.getColumnIndex(EXHIBITOR_LIST_USERID)));

                    parentCategoryData.add(object);
                }
            }
        }

        return parentCategoryData;
    }


    public ArrayList<ExhibitorCategoryListing> getSubCategoryList1(String parentId, String EventId) {
        ArrayList<ExhibitorCategoryListing> parentCategoryData = new ArrayList<ExhibitorCategoryListing>();
        SQLiteDatabase db = this.getReadableDatabase();


        String qry = "select * from " + TABLE_EXHIBITOR_SUB_CATEGORY + " WHERE " + SUB_CATEGORY_PARENT + " = '" + parentId
                + "' AND " + SUB_CATEGORY_EVENTID + " = '" + EventId + "'";
        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            ExhibitorCategoryListing parentCategoryData1 = new ExhibitorCategoryListing();
            parentCategoryData1.setId(cursor.getString(cursor.getColumnIndex(SUB_CATEGORY_ID)));
            parentCategoryData1.setImg(cursor.getString(cursor.getColumnIndex(SUB_CATEGORY_ICON)));
            parentCategoryData1.setSector(cursor.getString(cursor.getColumnIndex(SUB_CATEGORY_NAME)));
            parentCategoryData1.setShortDesc(cursor.getString(cursor.getColumnIndex(SUB_CATEGORY_SHORT_DESC)));
            parentCategoryData.add(parentCategoryData1);
        }
        return parentCategoryData;
    }

    public ArrayList<ExhibitorCategoryListing> getSubCategoryList1GroupWise(String parentId, String EventId, String groupId) {
        ArrayList<ExhibitorCategoryListing> parentCategoryData = new ArrayList<ExhibitorCategoryListing>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "";
        if (!groupId.isEmpty()) {
            qry = "select * from " + TABLE_EXHIBITOR_SUB_CATEGORY + " WHERE " + SUB_CATEGORY_PARENT + " = '" + parentId
                    + "' AND " + SUB_CATEGORY_EVENTID + " = '" + EventId
                    + "' AND " + SUB_EXHI_CAT_GROUP_ID + " = '" + groupId + "'";

        } else {
            qry = "select * from " + TABLE_EXHIBITOR_SUB_CATEGORY + " WHERE " + SUB_CATEGORY_PARENT + " = '" + parentId
                    + "' AND " + SUB_CATEGORY_EVENTID + " = '" + EventId + "'";
        }
        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            ExhibitorCategoryListing parentCategoryData1 = new ExhibitorCategoryListing();
            parentCategoryData1.setId(cursor.getString(cursor.getColumnIndex(SUB_CATEGORY_ID)));
            parentCategoryData1.setImg(cursor.getString(cursor.getColumnIndex(SUB_CATEGORY_ICON)));
            parentCategoryData1.setSector(cursor.getString(cursor.getColumnIndex(SUB_CATEGORY_NAME)));
            parentCategoryData1.setShortDesc(cursor.getString(cursor.getColumnIndex(SUB_CATEGORY_SHORT_DESC)));
            parentCategoryData1.setExhi_cat_group_id(cursor.getString(cursor.getColumnIndex(SUB_EXHI_CAT_GROUP_ID)));
            parentCategoryData.add(parentCategoryData1);
        }
        return parentCategoryData;
    }


    public ArrayList<ExhibitorParentCatGroup> getExhibitorParentGroupData(String parentId, String EventId) {
        ArrayList<ExhibitorParentCatGroup> parentCategoryData = new ArrayList<ExhibitorParentCatGroup>();
        SQLiteDatabase db = this.getReadableDatabase();

        String qry = "";
        if (parentId != null && parentId.isEmpty()) {
            qry = "select * from " + TABLE_EXHIBITOR_PRODUCTCAT_GROUP_LIST + " WHERE " + EXHIBITOR_PARENT_GROUP_EVENTID + " = '" + EventId + "'";
        } else {
            qry = "select * from " + TABLE_EXHIBITOR_PRODUCTCAT_GROUP_LIST + " WHERE " + EXHIBITOR_PARENT_GROUP_ParentCategoryId + " = '" + parentId
                    + "' AND " + EXHIBITOR_PARENT_GROUP_EVENTID + " = '" + EventId + "'";
        }

        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            ExhibitorParentCatGroup parentCategoryData1 = new ExhibitorParentCatGroup();
            parentCategoryData1.setId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_PARENT_GROUP_GroupId)));
            parentCategoryData1.setEventId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_PARENT_GROUP_EVENTID)));
            parentCategoryData1.setName(cursor.getString(cursor.getColumnIndex(EXHIBITOR_PARENT_GROUP_GroupName)));
            parentCategoryData1.setParentCategoryId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_PARENT_GROUP_ParentCategoryId)));
            parentCategoryData.add(parentCategoryData1);
        }

        return parentCategoryData;
    }

    public boolean insertUpdateAllCountries(ArrayList<ExhibitorCountry> list, String event_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                ExhibitorCountry exhibitorCountry = list.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(EXHIBITOR_COUNTRY_EVENTID, event_Id);
                contentValues.put(EXHIBITOR_COUNTRY_ID, exhibitorCountry.getId());
                contentValues.put(EXHIBITOR_COUNTRY_NAME, exhibitorCountry.getCountry_name());
                db.insert(TABLE_EXHIBITOR_COUNTRYLIST, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return true;
    }

    public boolean insertExhibitorParentGroupData(ArrayList<ExhibitorParentCatGroup> list, String event_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                ExhibitorParentCatGroup exhibitorParentCatGroup = list.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(EXHIBITOR_PARENT_GROUP_EVENTID, event_Id);
                contentValues.put(EXHIBITOR_PARENT_GROUP_GroupId, exhibitorParentCatGroup.getId());
                contentValues.put(EXHIBITOR_PARENT_GROUP_GroupName, exhibitorParentCatGroup.getName());
                contentValues.put(EXHIBITOR_PARENT_GROUP_ParentCategoryId, exhibitorParentCatGroup.getParentCategoryId());
                db.insert(TABLE_EXHIBITOR_PRODUCTCAT_GROUP_LIST, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return true;
    }

    public ArrayList<ExhibitorCountry> getExhibitorCountries(String EventId) {
        ArrayList<ExhibitorCountry> parentCategoryData = new ArrayList<ExhibitorCountry>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_EXHIBITOR_COUNTRYLIST + " WHERE " + EXHIBITOR_COUNTRY_EVENTID + " = '" + EventId
//                +"' AND "+SUB_CATEGORY_EVENTID+" = '"+EventId
                + "'";
        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            ExhibitorCountry parentCategoryData1 = new ExhibitorCountry();
            parentCategoryData1.setId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_COUNTRY_ID)));
            parentCategoryData1.setCountry_name(cursor.getString(cursor.getColumnIndex(EXHIBITOR_COUNTRY_NAME)));
            parentCategoryData.add(parentCategoryData1);
        }
        return parentCategoryData;
    }

    public boolean insertUpdateExhibitorsType(String typeId, String typeName, String typeColor, String event_Id) {
        SQLiteDatabase db = this.getWritableDatabase();


        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(EXHIBITOR_TYPE_EVENTID, event_Id);
            if (typeId == null)
                contentValues.put(EXHIBITOR_TYPE_ID, "");
            else
                contentValues.put(EXHIBITOR_TYPE_ID, typeId);
            contentValues.put(EXHIBITOR_TYPE_NAME, typeName);
            contentValues.put(EXHIBITOR_TYPE_COLOR, typeColor);
//        int rows = db.update(TABLE_EXHIBITOR_TYPES, contentValues, EXHIBITOR_TYPE_ID + " = ? AND " + EXHIBITOR_TYPE_EVENTID + " = ? ", new String[]{typeId, event_Id});

//        int rows= db.update(TABLE_EXHIBITOR_TYPES,  contentValues, EXHIBITOR_TYPE_ID + " = " + typeId ,null);
//        if (rows == 0) {
            db.insert(TABLE_EXHIBITOR_TYPES, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

//        }
        return true;
    }

    public ArrayList<ExhibitorType> getExhibitorTypes(String EventId) {
        ArrayList<ExhibitorType> parentCategoryData = new ArrayList<ExhibitorType>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_EXHIBITOR_TYPES + " WHERE " + EXHIBITOR_TYPE_EVENTID + " = '" + EventId
//                +"' AND "+EXHIBITOR_LIST_EVENTID+" = '"+EventId
//                +"' AND "+EXHIBITOR_LIST_USERID+" = '"+usreId
                + "'";
        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            ExhibitorType object = new ExhibitorType();
            object.setEvent_id(cursor.getString(cursor.getColumnIndex(EXHIBITOR_TYPE_EVENTID)));
            object.setExhibitorId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_TYPE_ID)));
            object.setExhibitorType(cursor.getString(cursor.getColumnIndex(EXHIBITOR_TYPE_NAME)));
            object.setExhibitorTypeColor(cursor.getString(cursor.getColumnIndex(EXHIBITOR_TYPE_COLOR)));

            parentCategoryData.add(object);
        }
        return parentCategoryData;
    }


    public ArrayList<SponsorType> getSponsorTypes(String EventId) {
        ArrayList<SponsorType> parentCategoryData = new ArrayList<SponsorType>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_SPONSOR_LIST_TYPE + " WHERE " + SPONSOR_TYPE_EVENTID + " = '" + EventId + "'";
        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            SponsorType object = new SponsorType();
            object.setEvent_id(cursor.getString(cursor.getColumnIndex(SPONSOR_TYPE_EVENTID)));
            object.setSponsorTypeColor(cursor.getString(cursor.getColumnIndex(SPONSOR_TYPE_COLOR)));
            object.setSponsorTypeId(cursor.getString(cursor.getColumnIndex(SPONSOR_TYPE_ID)));
            object.setSponsorType(cursor.getString(cursor.getColumnIndex(SPONSOR_TYPE_NAME)));
            object.setSponsorPosition(cursor.getString(cursor.getColumnIndex(SPONSOR_TYPE_POSITION)));

            parentCategoryData.add(object);
        }
        return parentCategoryData;
    }

    public ArrayList<SpeakerType> getSpeakerTypes(String EventId) {
        ArrayList<SpeakerType> parentCategoryData = new ArrayList<SpeakerType>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_SPEAKER_LIST_TYPE + " WHERE " + Speaker_TYPE_EVENTID + " = '" + EventId + "'";
        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            SpeakerType object = new SpeakerType();
            object.setEvent_id(cursor.getString(cursor.getColumnIndex(Speaker_TYPE_EVENTID)));
            object.setSpeakerTypeColor(cursor.getString(cursor.getColumnIndex(Speaker_TYPE_COLOR)));
            object.setSpeakerTypeId(cursor.getString(cursor.getColumnIndex(Speaker_TYPE_ID)));
            object.setSpeakerType(cursor.getString(cursor.getColumnIndex(Speaker_TYPE_NAME)));
            object.setSpeakerPosition(cursor.getString(cursor.getColumnIndex(Speaker_TYPE_POSITION)));

            parentCategoryData.add(object);
        }
        return parentCategoryData;
    }

    public boolean insertUpdateAllExhibitorsList(ArrayList<Attendance> attendanceList, String event_Id, String parentId, String user_id, String eventType) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < attendanceList.size(); i++) {
                Attendance attendance = attendanceList.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(EXHIBITOR_LIST_CATEGORYID, "");
                contentValues.put(EXHIBITOR_LIST_COUNTRYID, attendance.getCountry_id());
                contentValues.put(EXHIBITOR_LIST_EVENTID, event_Id);
                contentValues.put(EXHIBITOR_LIST_PARENTID, parentId);

                contentValues.put(EXHIBITOR_LIST_ID, attendance.getEx_id());
                contentValues.put(EXHIBITOR_LIST_HEADING, attendance.getEx_heading());
                contentValues.put(EXHIBITOR_LIST_LOGO, attendance.getEx_comapnyLogo());
                contentValues.put(EXHIBITOR_LIST_SATNDNUMBER, attendance.getEx_stand_number());

                contentValues.put(EXHIBITOR_LIST_FAVOURITE, attendance.getIs_fav());
                contentValues.put(EXHIBITOR_LIST_SHORTDESC, attendance.getEx_desc());
                contentValues.put(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID, attendance.getSponsored_category());


                if (attendance.getTypeId() == null)
                    contentValues.put(EXHIBITOR_LIST_TYPEID, "");
                else
                    contentValues.put(EXHIBITOR_LIST_TYPEID, attendance.getTypeId());
                contentValues.put(EXHIBITOR_LIST_PAGEID, attendance.getEx_pageId());
                contentValues.put(EXHIBITOR_LIST_USERID, user_id);
//            int rows = db.update(TABLE_EXHIBITOR_LIST, contentValues, EXHIBITOR_LIST_ID + " = ? AND " + EXHIBITOR_LIST_EVENTID + " = ? ", new String[]{attendance.getEx_id(), event_Id});
                db.insert(TABLE_EXHIBITOR_LIST, null, contentValues);

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return true;
    }


    public boolean insertExhibitorDetailFromList(String Event_id, String user_id, String event_Type, String exhiId, String exPageId, JSONObject data) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {

            ContentValues contentValues = new ContentValues();
            contentValues.put(ExhibitorDetail_eventId, Event_id);
            contentValues.put(ExhibitorDetail_userId, user_id);
            contentValues.put(ExhibitorDetail_eventType, event_Type);
            contentValues.put(ExhibitorDetail_Data, data.toString());
            contentValues.put(ExhibitorDetail_ExId, exhiId);
            contentValues.put(ExhibitorDetail_ExPageId, exPageId);
            db.insert(Table_ExhibitorDetail, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public boolean insertExhibitorDetailFromSplash(String Event_id, String user_id, String event_Type, JSONObject data) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            JSONArray jsonArray = data.getJSONArray("exhibitor_details_data");
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject index = jsonArray.getJSONObject(i);
                    JSONObject exhibitotDetail = index.getJSONObject("exhibitor_details");

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ExhibitorDetail_eventId, Event_id);
                    contentValues.put(ExhibitorDetail_userId, user_id);
                    contentValues.put(ExhibitorDetail_eventType, event_Type);
                    contentValues.put(ExhibitorDetail_Data, index.toString());
                    contentValues.put(ExhibitorDetail_ExId, exhibitotDetail.getString("exhibitor_id"));
                    contentValues.put(ExhibitorDetail_ExPageId, exhibitotDetail.getString("exhibitor_page_id"));
                    db.insert(Table_ExhibitorDetail, null, contentValues);
                }

            }
            db.setTransactionSuccessful();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return true;
    }

    public boolean insertUpdateExhibitorListdata(ArrayList<ExhibitorListdata> listdata, String event_Id, String parentId, String user_id, String eventType) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < listdata.size(); i++) {
            ExhibitorListdata exhibitorListdata = listdata.get(i);
            insertUpdateExhibitorsType(exhibitorListdata.getExhibitorList().get(0).getTypeId(), exhibitorListdata.getType(), exhibitorListdata.getBg_color(), event_Id);
            insertUpdateAllExhibitorsList(exhibitorListdata.getExhibitorList(), event_Id, parentId, user_id, eventType);

        }
        return true;
    }


    public boolean isAgendaDataExist(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_AGENDA_CATEGORYLIST + " WHERE " + AGENDA_CATEGORY_EVENTID + " = '" + Event_id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isCMSLISTDataExist(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_CMSLISTINGOffline + " WHERE " + CMSLISTING_Eventid + " = '" + Event_id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isCmsPageExistFromSplah(String event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + Table_CMSListing + " WHERE " + CmsEventId + " = '" + event_id + "'";

        Cursor cursor = db.rawQuery(qry, null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public void deleteCMSLISTData(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry = "delete from " + TABLE_CMSLISTINGOffline + " WHERE " + CMSLISTING_Eventid + " = '" + Event_id + "'";


            db.execSQL(qry);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public void deleteCmsPageDataFromSplash(String eventId) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransaction();
        try {
            String qry = "delete from " + Table_CMSListing + " WHERE " + CmsEventId + " = '" + eventId + "'";

            db.execSQL(qry);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public boolean insertCmsPageFromSplash(String Event_id, JSONObject jsonObject) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("cms_details");

            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject index = jsonArray.getJSONObject(i);
                    contentValues.put(CmsEventId, Event_id);
                    contentValues.put(CmsId, index.getString("cms_id"));
                    contentValues.put(CmsData, index.toString());
                    db.insert(Table_CMSListing, null, contentValues);
                }
            }

            db.setTransactionSuccessful();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }


    public void deleteAgendaListData(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry = "delete from " + TABLE_AGENDA_LIST + " WHERE " + AGENDA_EVENTID + " = '" + Event_id + "'";

            db.execSQL(qry);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public void deleteAgendaTypeData(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry = "delete from " + TABLE_AGENDA_TYPELIST + " WHERE " + AGENDA_TYPELIST_EVENTID + " = '" + Event_id + "'";


            db.execSQL(qry);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public void deleteAgendaCatRelationData(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry = "delete from " + TABLE_AGENDA_CATEGORY_RELATION + " WHERE " + AGENDA_CAT_RELATION_EVENTID + " = '" + Event_id + "'";


            db.execSQL(qry);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public void deleteAgendaCatData(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry = "delete from " + TABLE_AGENDA_CATEGORYLIST + " WHERE " + AGENDA_CATEGORY_EVENTID + " = '" + Event_id + "'";


            db.execSQL(qry);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public boolean isSponsorDataExist(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_SPONSOR_LIST_TYPE + " WHERE " + SPONSOR_TYPE_EVENTID + " = '" + Event_id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSpeakerDataExist(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "select * from " + TABLE_SPEAKER_LIST_TYPE + " WHERE " + Speaker_TYPE_EVENTID + " = '" + Event_id + "'";
        Cursor cursor = db.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteSponsorTypeData(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry = "delete from " + TABLE_SPONSOR_LIST_TYPE + " WHERE " + SPONSOR_TYPE_EVENTID + " = '" + Event_id + "'";


            db.execSQL(qry);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public void deleteSpeakerTypeData(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry = "delete from " + TABLE_SPEAKER_LIST_TYPE + " WHERE " + Speaker_TYPE_EVENTID + " = '" + Event_id + "'";


            db.execSQL(qry);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public void deleteSponsorListData(String Event_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            String qry = "delete from " + TABLE_SPONSOR_LIST + " WHERE " + SPONSOR_LIST_EVENTID + " = '" + Event_id + "'";

            db.execSQL(qry);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }


    public boolean insertUpdateAgendadata(AgendaData agendaData, String event_Id) {
        SQLiteDatabase db = this.getWritableDatabase();

        insertUpdateAgendaTypeList(agendaData.getTypes(), event_Id);
        insertUpdateAgendaCategoryList(agendaData.getCategory(), event_Id);
        insertAgendaCatRelationList(agendaData.getCategory_relation(), event_Id);
        insertAgendaList(agendaData.getAgenda_list(), event_Id);

        return true;
    }


    public boolean insertCMSLISTData(ArrayList<CmsListData> list, String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                CmsListData groupModuleData = list.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(CMSLISTING_ID, groupModuleData.getId());
                contentValues.put(CMSLISTING_cms_icon, groupModuleData.getCms_icon());
                contentValues.put(CMSLISTING_Menu_name, groupModuleData.getMenuName());
                contentValues.put(CMSLISTING_group_id, groupModuleData.getGroupId());
                contentValues.put(CMSLISTING_Eventid, eventId);
                db.insert(TABLE_CMSLISTINGOffline, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }


    public boolean insertUpdateAgendaTypeList(ArrayList<AgendaType> listdata, String event_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < listdata.size(); i++) {
                AgendaType agendaType = listdata.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(AGENDA_TYPELIST_EVENTID, agendaType.getEventId());
                contentValues.put(AGENDA_TYPELIST_TYPEID, agendaType.getTypeId());
                contentValues.put(AGENDA_TYPELIST_TYPENAME, agendaType.getTypeName());
                contentValues.put(AGENDA_TYPELIST_ORDERNO, agendaType.getOrderNo());

                db.insert(TABLE_AGENDA_TYPELIST, null, contentValues);

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }

    public boolean insertUpdateAgendaCategoryList(ArrayList<AgendaCategory> listdata, String event_Id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            for (int i = 0; i < listdata.size(); i++) {
                AgendaCategory agendaCategory = listdata.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(AGENDA_CATEGORY_EVENTID, agendaCategory.getEventId());
                contentValues.put(AGENDA_CATEGORY_ID, agendaCategory.getId());
                contentValues.put(AGENDA_CATEGORY_NAME, agendaCategory.getCategoryName());
                contentValues.put(AGENDA_CATEGORY_TYPE, agendaCategory.getCategorieType());
                contentValues.put(AGENDA_CATEGORY_IMAGE, agendaCategory.getCategoryImage());
                contentValues.put(AGENDA_CATEGORY_SortOrder, agendaCategory.getSort_order());
                contentValues.put(AGENDA_CATEGORY_WELCOME_SCREEN, agendaCategory.getWelcome_screen());
                db.insert(TABLE_AGENDA_CATEGORYLIST, null, contentValues);

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


        return true;
    }

    public boolean insertAgendaCatRelationList(ArrayList<AgendaCategoryRelation> listdata, String event_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < listdata.size(); i++) {
                AgendaCategoryRelation agendaCategory = listdata.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(AGENDA_CAT_RELATION_EVENTID, event_Id);
                contentValues.put(AGENDA_CAT_RELATION_CATID, agendaCategory.getCategoryId());
                contentValues.put(AGENDA_CAT_RELATION_AGENDAID, agendaCategory.getAgendaId());

                db.insert(TABLE_AGENDA_CATEGORY_RELATION, null, contentValues);

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }

    public boolean insertAgendaList(ArrayList<Agenda> listdata, String event_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < listdata.size(); i++) {
                Agenda agenda = listdata.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(AGENDA_EVENTID, event_Id);
                contentValues.put(AGENDA_AGENDAID, agenda.getAgendaId());
                contentValues.put(AGENDA_HEADING, agenda.getHeading());
                contentValues.put(AGENDA_SPEAKER, agenda.getSpeaker());
                contentValues.put(AGENDA_LOCATION, agenda.getLocation());
                contentValues.put(AGENDA_SESSION_IMAGE, agenda.getSessionImage());
                contentValues.put(AGENDA_STARTDATE, agenda.getStartDate());
                contentValues.put(AGENDA_ENDDATE, agenda.getEndDate());
                contentValues.put(AGENDA_STARTTIME, agenda.getStartTime());
                contentValues.put(AGENDA_ENDTIME, agenda.getEndTime());
                contentValues.put(AGENDA_TYPENAME, agenda.getTypes());
                contentValues.put(AGENDA_SORT_ORDER, agenda.getSort_order());
                contentValues.put(AGENDA_SESSION_TRACK, agenda.getSession_tracks());
                contentValues.put(AGENDA_TYPEID, agenda.getTypeId());
                db.insert(TABLE_AGENDA_LIST, null, contentValues);

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


        return true;
    }

    public boolean insertUpdateSponsordata(ArrayList<SponsorListNewData> listdata, String event_Id, String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();


        for (int i = 0; i < listdata.size(); i++) {
            SponsorListNewData exhibitorListdata = listdata.get(i);
            insertUpdateSponsorType(exhibitorListdata.getTypeId(), exhibitorListdata.getType(), exhibitorListdata.getBg_color(), event_Id, exhibitorListdata.getType_position());
            insertUpdateAllSponsorList(exhibitorListdata.getSponsorNewDataArrayList(), event_Id, user_id, exhibitorListdata.getTypeId());
        }
        return true;
    }


    public boolean insertUpdateAllSponsorList(ArrayList<SponsorListNewData.SponsorNewData> list, String event_Id, String user_id, String typeId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                SponsorListNewData.SponsorNewData attendance = list.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(SPONSOR_LIST_EVENTID, event_Id);
                contentValues.put(SPONSOR_LIST_ID, attendance.getId());
                contentValues.put(SPONSOR_LIST_COMAPANYNAME, attendance.getCompanyName());
                contentValues.put(SPONSOR_LIST_FAVOURITE, attendance.getIsFavorites());
                contentValues.put(SPONSOR_LIST_LOGO, attendance.getCompanyLogo());
                contentValues.put(SPONSOR_LIST_USERID, user_id);
                if (typeId == null)
                    contentValues.put(SPONSOR_LIST_TYPEID, "");
                else
                    contentValues.put(SPONSOR_LIST_TYPEID, typeId);
                db.insert(TABLE_SPONSOR_LIST, null, contentValues);

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }

    public boolean insertUpdateSponsorType(String typeId, String typeName, String typeColor, String event_Id, String pos) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SPONSOR_TYPE_EVENTID, event_Id);
            if (typeId == null)
                contentValues.put(SPONSOR_TYPE_ID, "");
            else
                contentValues.put(SPONSOR_TYPE_ID, typeId);
            contentValues.put(SPONSOR_TYPE_NAME, typeName);
            contentValues.put(SPONSOR_TYPE_POSITION, pos);
            contentValues.put(SPONSOR_TYPE_COLOR, typeColor);
            db.insert(TABLE_SPONSOR_LIST_TYPE, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }


    public boolean insertUpdateSpeakerType(String typeId, String typeName, String typeColor, String event_Id, String pos) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Speaker_TYPE_EVENTID, event_Id);
            if (typeId == null)
                contentValues.put(Speaker_TYPE_ID, "");
            else
                contentValues.put(Speaker_TYPE_ID, typeId);
            contentValues.put(Speaker_TYPE_NAME, typeName);
            contentValues.put(Speaker_TYPE_POSITION, pos);
            contentValues.put(Speaker_TYPE_COLOR, typeColor);
            db.insert(TABLE_SPEAKER_LIST_TYPE, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return true;
    }

    public ArrayList<SponsorListNewData.SponsorNewData> getSponsorListData(String EventId, String parentId, String typeId) {
        ArrayList<SponsorListNewData.SponsorNewData> groupModuleDataArrayList = new ArrayList<SponsorListNewData.SponsorNewData>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "";
        if (parentId.equalsIgnoreCase("")) {
            qry = "select *  FROM " + TABLE_SPONSOR_LIST + " Where " + SPONSOR_LIST_EVENTID + " = " + EventId + " AND " + SPONSOR_LIST_TYPEID + " = " + typeId;
        } else {
//            + " AND m." + SPONSOR_LIST_TYPEID + " = " + typeId
            qry = "select *  FROM " + TABLE_SPONSOR_LIST + " m join " + TABLE_GROUP_RELATION + " gr ON gr." + GROUP_RELATION_MODULEID + " = m." + SPONSOR_LIST_ID + " Where gr." + GROUP_RELATION_ID + " = " + parentId
                    + " AND gr." + GROUP_RELATION_EVENTID + " = " + EventId
                    + " AND m." + SPONSOR_LIST_TYPEID + " = " + typeId;
        }
        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            SponsorListNewData.SponsorNewData groupModuleData = new SponsorListNewData().new SponsorNewData();
            groupModuleData.setId(cursor.getString(cursor.getColumnIndex(SPONSOR_LIST_ID)));
            groupModuleData.setCompanyLogo(cursor.getString(cursor.getColumnIndex(SPONSOR_LIST_LOGO)));
            groupModuleData.setCompanyName(cursor.getString(cursor.getColumnIndex(SPONSOR_LIST_COMAPANYNAME)));
            groupModuleData.setIsFavorites(cursor.getString(cursor.getColumnIndex(SPONSOR_LIST_FAVOURITE)));
            groupModuleData.setSponsorsName(cursor.getString(cursor.getColumnIndex(SPONSOR_LIST_NAME)));
            groupModuleDataArrayList.add(groupModuleData);
        }
        return groupModuleDataArrayList;
    }

    public ArrayList<SpeakerListClass.SpeakerListNew> getSpeakerListDataByType(String EventId, String typeId) {
        ArrayList<SpeakerListClass.SpeakerListNew> groupModuleDataArrayList = new ArrayList<SpeakerListClass.SpeakerListNew>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qry = "";
        qry = "select *  FROM " + TABLE_SPEAKER_LIST + " Where " + SPEAKER_LIST_EVENTID + " = " + EventId + " AND " + SPEAKER_LIST_TYPEID + " = " + typeId;

        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            SpeakerListClass.SpeakerListNew groupModuleData = new SpeakerListClass().new SpeakerListNew();
            groupModuleData.setId(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_ID)));
            groupModuleData.setLogo(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_LOGO)));
            groupModuleData.setCompanyName(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_COMAPANYNAME)));
            groupModuleData.setIsFavorites(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_FAVOURITE)));
            groupModuleData.setLastname(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_LAST_NAME)));
            groupModuleData.setFirstname(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_FIRST_NAME)));
            groupModuleData.setEmail(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_SPEAKER_EMAIL)));
            groupModuleData.setEvent_id(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_EVENTID)));
            groupModuleData.setHeading(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_SPEAKER_HEADING)));
            groupModuleData.setIsFavorites(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_FAVOURITE)));
            groupModuleData.setSpeakerDesc(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_SPEAKER_DESC)));
            groupModuleData.setTitle(cursor.getString(cursor.getColumnIndex(SPEAKER_LIST_TITLE)));

            groupModuleDataArrayList.add(groupModuleData);
        }
        return groupModuleDataArrayList;
    }


    public void updateExhibitorUserID(String EventId, String usreId) {
//
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EXHIBITOR_LIST_USERID, usreId);
        int row = db.update(TABLE_EXHIBITOR_LIST, cv, EXHIBITOR_LIST_EVENTID + "= ?", new String[]{EventId});
        Log.e("database", "updateExhibitorUserID: " + row);
    }


    public void updateExhibitorFav(String EventId, String usreId, ArrayList<FavoritedExhibitor.FavoriteExhibitor> exhibitors) {
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < exhibitors.size(); i++) {
            FavoritedExhibitor.FavoriteExhibitor exhibitor = exhibitors.get(i);
            ContentValues cv = new ContentValues();
            cv.put(EXHIBITOR_LIST_FAVOURITE, 1);
            int row = db.update(TABLE_EXHIBITOR_LIST, cv, EXHIBITOR_LIST_EVENTID + " = ? AND "
                    + EXHIBITOR_LIST_PAGEID + " = ? AND "
                    + EXHIBITOR_LIST_USERID + " = ?", new String[]{EventId, exhibitor.getExhibitor_page_id(), usreId});
        }

    }

    public void updateSponsorFavFragment(String EventId, String usreId, ArrayList<FavoritedSponsor.FavoriteSponsor> exhibitors) {
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < exhibitors.size(); i++) {
            FavoritedSponsor.FavoriteSponsor exhibitor = exhibitors.get(i);
            ContentValues cv = new ContentValues();
            cv.put(SPONSOR_LIST_FAVOURITE, 1);
            int row = db.update(TABLE_SPONSOR_LIST, cv, SPONSOR_LIST_EVENTID + " = ? AND "
                    + SPONSOR_LIST_ID + " = ? AND "
                    + SPONSOR_LIST_USERID + " = ?", new String[]{EventId, exhibitor.getSponserId(), usreId});
        }
    }


    public void updateSpeakerFavFragment(String EventId, String usreId, ArrayList<FavoritedSpeaker.FavoriteSpeaker> exhibitors) {
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < exhibitors.size(); i++) {
            FavoritedSpeaker.FavoriteSpeaker exhibitor = exhibitors.get(i);
            ContentValues cv = new ContentValues();
            cv.put(SPEAKER_LIST_FAVOURITE, 1);
            int row = db.update(TABLE_SPEAKER_LIST, cv, SPEAKER_LIST_EVENTID + " = ? AND "
                    + SPEAKER_LIST_ID + " = ? AND "
                    + SPEAKER_LIST_USERID + " = ?", new String[]{EventId, exhibitor.getSpeakerId(), usreId});
        }
    }

    public void updateSpeakerFavAdapter(String EventId, String usreId, String speaker_id, String fav) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(SPEAKER_LIST_FAVOURITE, fav);
        int row = db.update(TABLE_SPEAKER_LIST, cv, SPEAKER_LIST_EVENTID + " = ? AND "
                + SPEAKER_LIST_ID + " = ? AND "
                + SPEAKER_LIST_USERID + " = ?", new String[]{EventId, speaker_id, usreId});


    }

    public void updateExhibitorFav(String EventId, String usreId, String Exhibitor_page_id, String fav) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EXHIBITOR_LIST_FAVOURITE, fav);
        int row = db.update(TABLE_EXHIBITOR_LIST, cv, EXHIBITOR_LIST_EVENTID + " = ? AND "
                + EXHIBITOR_LIST_PAGEID + " = ? AND "
                + EXHIBITOR_LIST_USERID + " = ?", new String[]{EventId, Exhibitor_page_id, usreId});
    }

    public void updateSponsorFavAdapter(String EventId, String usreId, String Exhibitor_page_id, String fav) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(SPONSOR_LIST_FAVOURITE, fav);
        int row = db.update(TABLE_SPONSOR_LIST, cv, SPONSOR_LIST_EVENTID + " = ? AND "
                + SPONSOR_LIST_ID + " = ? AND "
                + SPONSOR_LIST_USERID + " = ?", new String[]{EventId, Exhibitor_page_id, usreId});


    }


    public ArrayList<Attendance> getExhibitorListBySubCat(String parentId, String EventId, String usreId, String typeId) {
        ArrayList<Attendance> parentCategoryData = new ArrayList<Attendance>();
        SQLiteDatabase db = this.getReadableDatabase();
//        shortDesc = "test";
        ArrayList<String> parentKeywords = new ArrayList<>();
        if (!parentId.isEmpty()) {
            ArrayList<ExhibitorCategoryListing> categoryListings = getSubCategoryList1(parentId, EventId);

            for (int i = 0; i < categoryListings.size(); i++) {
                ExhibitorCategoryListing listing = categoryListings.get(i);
                String[] categoryKeywords = listing.getShortDesc().split(",");
//            new ArrayList<String>(Arrays.asList(strings));
                parentKeywords.addAll(new ArrayList<String>(Arrays.asList(categoryKeywords)));
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < parentKeywords.size(); i++) {
            String keyword = parentKeywords.get(i).replace("\'", "\'\'");
            sb.append("(");
            sb.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '%," + keyword + ",%' OR ");
            sb.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '%," + keyword + "' OR ");
            sb.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '" + keyword + ",%' OR ");
            sb.append(EXHIBITOR_LIST_SHORTDESC + " = '" + keyword + "')");

            if (i != parentKeywords.size() - 1)
                sb.append(" OR ");
        }
        sb.append(")");


        String qry;
        if (parentKeywords.size() > 0) {
            qry = "select * from " + TABLE_EXHIBITOR_LIST + " WHERE " +
                    EXHIBITOR_LIST_EVENTID + " = '" + EventId
                    + "' AND " + EXHIBITOR_LIST_USERID + " = '" + usreId
                    + "' AND " + EXHIBITOR_LIST_TYPEID + " = '" + typeId + "'"
                    + " AND "
                    + sb.toString();
        } else {
            qry = "select * from " + TABLE_EXHIBITOR_LIST + " WHERE " +
                    EXHIBITOR_LIST_EVENTID + " = '" + EventId
                    + "' AND " + EXHIBITOR_LIST_USERID + " = '" + usreId
                    + "' AND " + EXHIBITOR_LIST_TYPEID + " = '" + typeId + "'"
            ;
        }
        Cursor cursor = db.rawQuery(qry, null);
        Log.e("getExhibitorList", "where: " + cursor.getCount());
        while (cursor.moveToNext()) {
            Attendance object = new Attendance();
            object.setEx_id(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_ID)));
            object.setEx_pageId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_PAGEID)));
            object.setEx_comapnyLogo(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_LOGO)));
            object.setEx_heading(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_HEADING)));
            object.setSponsored_category(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID)));
            object.setEx_desc(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_SHORTDESC)));
            object.setEx_stand_number(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_SATNDNUMBER)));
            object.setTypeId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_TYPEID)));
            object.setIs_fav(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_FAVOURITE)));
            parentCategoryData.add(object);
        }
        return parentCategoryData;
    }

    public ArrayList<Attendance> getExhibitorListBySubCatType(String parentId, String EventId, String usreId, String shortDesc, String typeId) {
        ArrayList<Attendance> parentCategoryData = new ArrayList<Attendance>();
        SQLiteDatabase db = this.getReadableDatabase();


        ArrayList<String> parentKeywords = new ArrayList<>();
        String[] categoryKeywords = shortDesc.split(",");
        parentKeywords.addAll(new ArrayList<String>(Arrays.asList(categoryKeywords)));

        StringBuilder parent = new StringBuilder();
        parent.append("(");
        for (int i = 0; i < parentKeywords.size(); i++) {
            String keyword = parentKeywords.get(i).replace("\'", "\'\'");

            parent.append("(");
            parent.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '%," + keyword + ",%' OR ");
            parent.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '%," + keyword + "' OR ");
            parent.append(EXHIBITOR_LIST_SHORTDESC + " LIKE '" + keyword + ",%' OR ");
            parent.append(EXHIBITOR_LIST_SHORTDESC + " = '" + keyword + "')");

            if (i != parentKeywords.size() - 1)
                parent.append(" OR ");
        }
        parent.append(")");
        String qry = null;
        if (parentKeywords.size() > 0) {
            qry = "select * from " + TABLE_EXHIBITOR_LIST + " WHERE "
//                    +EXHIBITOR_LIST_PARENTID+" = '"+ parentId
//                    +"' AND "
                    + EXHIBITOR_LIST_EVENTID + " = '" + EventId
                    + "' AND " + EXHIBITOR_LIST_USERID + " = '" + usreId
                    + "' AND " + EXHIBITOR_LIST_TYPEID + " = '" + typeId + "'"
//                +"AND "+EXHIBITOR_LIST_SHORTDESC+" LIKE ?"+separated

                    + " AND " + parent.toString()
            ;
        } else {
            qry = "select * from " + TABLE_EXHIBITOR_LIST + " WHERE "
                    + EXHIBITOR_LIST_PARENTID + " = '" + parentId
                    + "' AND " + EXHIBITOR_LIST_EVENTID + " = '" + EventId
                    + "' AND " + EXHIBITOR_LIST_USERID + " = '" + usreId
                    + "' AND " + EXHIBITOR_LIST_TYPEID + " = '" + typeId + "'"
            ;
        }
        Log.e("getExhibitor", "quer: " + qry);

        Cursor cursor = db.rawQuery(qry, null);
        while (cursor.moveToNext()) {
            Attendance object = new Attendance();
            object.setEx_id(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_ID)));
            object.setEx_pageId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_PAGEID)));
            object.setEx_comapnyLogo(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_LOGO)));
            object.setEx_heading(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_HEADING)));
            object.setSponsored_category(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_SPONSOR_CATEGORY_ID)));
            object.setEx_desc(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_SHORTDESC)));
            object.setEx_stand_number(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_SATNDNUMBER)));
            object.setTypeId(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_TYPEID)));
            object.setIs_fav(cursor.getString(cursor.getColumnIndex(EXHIBITOR_LIST_FAVOURITE)));
            parentCategoryData.add(object);
        }
        return parentCategoryData;
    }


    public boolean isSameSponsorUser(String EventId, String usreId) {
        Log.e("getExhibitorList", "isSameExhibitorUser: ");
        boolean isSameUser = false;

        SQLiteDatabase db = this.getReadableDatabase();

        String qry = "select * from " + TABLE_SPONSOR_LIST + " WHERE "

                + SPONSOR_LIST_EVENTID + " = '" + EventId
                + "'";
        Cursor cursor = db.rawQuery(qry, null);
        if (cursor.moveToFirst()) {
            String user_id = cursor.getString(cursor.getColumnIndex(SPONSOR_LIST_USERID));
            if (user_id.equalsIgnoreCase(usreId))
                isSameUser = true;
        }
        return isSameUser;
    }

    public void updateSponsorUserID(String EventId, String usreId) {

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SPONSOR_LIST_USERID, usreId);
        int row = db.update(TABLE_SPONSOR_LIST, cv, SPONSOR_LIST_EVENTID + "= ?", new String[]{EventId});
        Log.e("database", "updateExhibitorUserID: " + row);
    }

    //***********************************************************8
//********************************************************//

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Log.i("AITL VSERION OF DATABASE :: ", " :: " + db.getVersion()
//                + " NEW VESRION :: " + newVersion);

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type IS 'table'" +
                " AND name NOT IN ('sqlite_master', 'sqlite_sequence')", null);
        List<String> tables = new ArrayList<>();
        while (c.moveToNext()) {
            tables.add(c.getString(0));
        }

        for (String table : tables) {
            String dropQuery = "DROP TABLE IF EXISTS " + table;
            db.execSQL(dropQuery);
        }

        try {
            sessionManager = new SessionManager(context);
            sessionManager.logout();
            sessionManager.languageClear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        onCreate(db);

    }
}
