package com.allintheloop.Util;


import com.allintheloop.BuildConfig;

/**
 * Created by nteam on 25/4/16.
 */
public class MyUrls {

    // Live URl

    //    public static String baseUrl="https://www.allintheloop.net/";
    public static String baseUrl = BuildConfig.base_url;

    public static String url = baseUrl + "native_single_fcm_v2/";
    public static String Imgurl = baseUrl + "assets/user_files/";
    public static String loginImgurl = baseUrl + "assets/";
    public static String badgeFiles = baseUrl + "assets/badges_files/";
    public static String fun_logo = baseUrl + "fundraising/assets/user_files/";
    public static String Fund_Imgurl = baseUrl + "fundraising/";
    public static String doc_url = baseUrl + "assets/user_documents/";
    public static String groupIconUrl = baseUrl + "assets/group_icon/";
    public static String thumImgUrl = baseUrl + "assets/user_files/thumbnail/";
    public static String LangImgUrl = baseUrl + "images/countryflags/";

    // DemoURl

//    public static String url = BaseUrl+"/native_single_fcm/";
//    public static String Imgurl = BaseUrl+"/assets/user_files/";
//    public static String badgeFiles = BaseUrl+"/assets/badges_files/";
//    public static String fun_logo = BaseUrl+"/fundraising/assets/user_files/";
//    public static String Fund_Imgurl = BaseUrl+"/fundraising/";
//    public static String doc_url = BaseUrl+"/assets/user_documents/";
//    public static String groupIconUrl = BaseUrl+"/assets/group_icon/";
//    public static String thumImgUrl = BaseUrl+"/assets/user_files/thumbnail/";
//    public static String LangImgUrl = BaseUrl+"/images/countryflags/";

    // rashmiURl

//    public static String url = "https://www.allintheloop.net/aitldemo/native_single_fcm/";
//    public static String Imgurl = "https://www.allintheloop.net/aitldemo/assets/user_files/";
//    public static String badgeFiles = "https://www.allintheloop.net/aitldemo/assets/badges_files/";
//    public static String fun_logo = "https://www.allintheloop.net/aitldemo/fundraising/assets/user_files/";
//    public static String Fund_Imgurl = "https://www.allintheloop/aitldemo.net/fundraising/";
//    public static String doc_url = "https://www.allintheloop.net/aitldemo/assets/user_documents/";
//    public static String groupIconUrl = "https://www.allintheloop.net/aitldemo/assets/group_icon/";
//    public static String thumImgUrl = "https://www.allintheloop.net/aitldemo/assets/user_files/thumbnail/";
//    public static String LangImgUrl = "https://www.allintheloop.net/aitldemo/images/countryflags/";


    // slaveUrl

//    public static String url = "http://198.37.103.23/~allintheloop/native_single_fcm/";
//    public static String Imgurl = "http://198.37.103.23/~allintheloop/assets/user_files/";
//    public static String badgeFiles = "http://198.37.103.23/~allintheloop/assets/badges_files/";
//    public static String fun_logo = "http://198.37.103.23/~allintheloop/fundraising/assets/user_files/";
//    public static String Fund_Imgurl = "http://198.37.103.23/~allintheloop/fundraising/";
//    public static String doc_url = "http://198.37.103.23/~allintheloop/assets/user_documents/";
//    public static String groupIconUrl = "http://198.37.103.23/~allintheloop/assets/group_icon/";
//    public static String thumImgUrl = "http://198.37.103.23/~allintheloop/assets/user_files/thumbnail/";
//    public static String LangImgUrl = "http://198.37.103.23/~allintheloop/images/countryflags/";

    public static String Login = url + "login/check";
    public static String submitFormBuilderData = url + "login/saveFormBuilderData";
    public static String forgotPassword = url + "login/forgot_password";
    public static String linedInLogin = url + "login/linkedInSignup";
    public static String deleteUser = url + "login/delete_user";
    public static String getUpdatedDateByModule = url + "app/getUpdatedByModulesNew";
    public static String getloginScreenLogo = url + "app/get_login_screen_settings";
    public static String Index = url + "app/event_id";
    public static String home_pageIndex = url + "app/event_id_advance_design";
    public static String updateContactDetail = url + "profile/update_contact_details";
    public static String getCms_Page = url + "cms/get_cms_page";
    public static String EVENT_LIST = url + "event/getAllPublicEvents";
    public static String searchEvent = url + "event/searchEvent";
    public static String FbLogin = url + "login/fb_signup";
    public static String authorizedLogin = url + "login/authorizedLogin";
    public static String OpenApp = url + "event/openApp";
    public static String SECURE_EVENT = url + "event/searchEventBySecurekey";
    public static String AGENDA_BY_TIME = url + "agenda/getAgendaByTime";
    public static String Agenda_By_Type = url + "agenda/getAgendaByType";
    public static String GET_FORM_DATA = url + "login/getFormBuillderDataByEvent";
    public static String Register_data = url + "login/registrationByEvent";
    public static String Get_CountryList = url + "login/getAllCountryList";
    public static String Get_StateList = url + "profile/getStateList";
    public static String get_AttendanceList = url + "attendee/attendee_list_native_new";
    public static String getAttendeeListNew = url + "attendee/attendee_list_v2";
    public static String get_AttendanceCategoryList = url + "attendee/getAttendeeCategories";
    public static String getAttendeeFiterData = url + "attendee/getAttendeeFilters";
    public static String updateAttendeeKeywordData = url + "attendee/updateAtteneeKeywords";
    public static String get_attendanceDetail = url + "attendee/attendee_view_unread_count";

    public static String attendee_delete_comment = url + "attendee/delete_comment";
    public static String getAttendeeContactList = url + "attendee/attendee_contact_list_new";

    public static String get_ExhibitorList = url + "exhibitor/exhibitor_list_native_new";
    public static String get_parentCategoryExhibitorList = url + "exhibitor/exhibitor_parent_category";
    public static String getExhibitorCategoryListing = url + "exhibitor/exhibitor_category";
    public static String exhibitor_keyWordSearch = url + "exhibitor/exhibitorSearch";
    public static String get_ExhibitorDetail = url + "exhibitor/exhibitor_view_unread_count";

    public static String exhibitor_deleteComment = url + "exhibitor/delete_comment";
    public static String exhibitor_makeComment = url + "exhibitor/make_comment";
    public static String exhibitor_deleteMessage = url + "exhibitor/delete_message";
    public static String exhibitor_shareContact = url + "exhibitor/shareContactInformation";

    public static String get_SpeakerList = url + "speaker/speaker_list_offline";
    public static String get_SpeakerList_offline = url + "speaker/speaker_list_offline";

    //    public static String get_SpeakerDetail = url + "speaker/speaker_view_unread_count";
    public static String get_SpeakerDetail = url + "speaker/speaker_view_unread_countV2";


    public static String speaker_DeleteComment = url + "speaker/delete_comment";


    public static String get_MapList = url + "maps/map_list_new";
    public static String getCMSofflineData = url + "cms/get_cms_list_details";
    public static String get_MapDetail = url + "maps/map_details";
    public static String get_SocialList = url + "social/social_list";
    public static String Add_Notes = url + "note/add_note_new";
    public static String update_Notes = url + "note/edit_note";
    public static String View_Notes = url + "note/note_list_new";
    public static String Update_profile = url + "profile/update_profile";
    public static String get_agenda_byId = url + "agenda/getAgendaById";
    public static String save_agenda_comment = url + "agenda/saveAgendaComments";
    public static String saveUser_agenda = url + "agenda/saveUserAgenda";
    public static String Delete_agenda = url + "agenda/deleteUserAgenda";
    public static String CheckIn_agenda = url + "agenda/checkInAgenda";
    public static String SaveRating_agenda = url + "agenda/saveRating";
    public static String get_UserAgenda_ByTime = url + "agenda/getUserAgendaByTimeWithMeeting";
    //    public static String get_UserAgenda_ByTime = url + "agenda/getUserAgendaByTime";
    public static String get_UserAgenda_ByType = url + "agenda/getUserAgendaByType";


    public static String get_PendingAgendaByTime = url + "agenda/getPendingUserAgendaByTime";
    public static String get_PendingAgendaByType = url + "agenda/getPendingUserAgendaByType";
    public static String savePendingAgenda = url + "agenda/savePendingUserAgenda";

    public static String deletePendingAgenda = url + "agenda/deleteUserPendingAgenda";


    public static String get_documentList = url + "document/documents_list_new";
    public static String get_SearchdocumentList = url + "document/searchDocuments";
    public static String get_folderView = url + "document/folder_view";


    public static String Fundrising_Home = url + "fundraising/fundraising_home";
    public static String Fundrising_Product = url + "fundraising/fundraising_products";

    public static String get_survey = url + "survey/get_category_wise_survey";
    public static String Save_survey = url + "survey/saveSurvey";

    public static String get_SponsorList = url + "sponsors/sponsors_list";
    public static String get_SponsorList_offline = url + "sponsors/sponsors_list_offline";
    public static String get_SponsorDetail = url + "sponsors/sponsors_view";
    public static String get_SponsorDetailv2 = url + "sponsors/sponsors_view_v2";

    public static String get_FavrotiyeSponsor = url + "sponsors/get_favorited_sponser";
    public static String get_FavoriteSpeaker = url + "speaker/get_favorited_speakers";

    public static String update_gcm = url + "login/updateUserGCMId";

    public static String public_message = url + "message/publicMessageSend";
    public static String public_images_request = url + "message/public_msg_images_request";
    public static String getAllPublicMessage = url + "message/getPublicMessages";
    public static String public_commentRequest = url + "message/make_comment";
    public static String public_commentDelete = url + "message/delete_comment";

    public static String PubPriv_Delete_Message = url + "message/delete_message";
    //https://test.allintheloop.net/native_single_fcm_v2/development/shareContactInformation
    public static String getAllPrivateMessage = url + "message/getPrivateMessages";
    public static String getAllPrivateSpeaker = url + "message/getAllSpeakersAttendee";
    public static String private_messageSend = url + "message/privateMessageSend";
    public static String private_messageSendtext = url + "message/privateMessageSendText";
    public static String privateMessageSendImage = url + "message/privateMessageSendImage";

    public static String getAllPhotoFeed = url + "photo/getPublicFeeds";
    public static String uploadPhotoFeed = url + "photo/UploadPublicFeeds";
    public static String photolikeFeed = url + "photo/like_feed";
    public static String photodislikeFeed = url + "photo/dislike_feed";
    public static String photoDeleteFeed = url + "photo/delete_feed";
    public static String photo_makeComment = url + "photo/make_comment";
    public static String photo_deleteComment = url + "photo/delete_comment";
    public static String noteDetele = url + "note/delete_note";
    public static String Silentauction_productList = url + "product/product_listing";
    public static String Silentauction_detail = url + "product/slient_products_details";
    public static String getProdcuId = url + "product/getAuctionWiseProductId";
    public static String liveAuctionDetail = url + "product/live_products_details";
    public static String OnlineShopDetail = url + "product/online_shop_details";
    public static String DonateDetail = url + "product/donation_details";
    public static String CartDetail = url + "product/cart_details";
    public static String addTocart = url + "product/add_to_cart";
    public static String save_bidDetail = url + "product/save_bid_details";
    public static String checkOutDetail = url + "product/checkout_details";
    public static String deleteCartProduct = url + "product/delete_cart_product";
    public static String update_cartQty = url + "product/update_cart_quantity";

    public static String TwitterFeed = url + "social/getTwitetrFeedsNew";
    public static String getTwitterHashTagList = url + "social/getTwitetrHashtags";
    public static String getPhotoImageList = url + "photo/getImageList";

    public static String getMessageImageList = url + "message/getImageList";


    public static String moderatorGetAllRequestMeeting = url + "attendee/getAllMeetingRequestModerator";
    public static String moderatorGetAllRequestMeetingWithDate = url + "attendee/getAllMeetingRequestWithDateModerator";
    //    public static String moderatorRequestResponse = url + "attendee/respondRequestModerator";
    public static String moderatorRequestResponse = url + "attendee/respondRequestModeratorV2";
    public static String moderatorSugeestTime = url + "attendee/suggestMeetingTimeModerator";
    public static String moderatorSaveComment = url + "attendee/SaveCommentModerator";


    //  public static String getNotification = url+"settings/notification";

    public static String confirm_order = url + "product/confirmOrder";
    public static String save_instant_donation_details = url + "fundraising/save_instant_donation_details";
    public static String save_fundraising_donation_details = url + "fundraising/save_fundraising_donation_details";
    public static String get_fundraising_donation_details = url + "fundraising/get_fundraising_donation_details";
    public static String getorderList = url + "fundraising/orders";
    public static String addproduct_Item = url + "product/add_item";
    public static String getItemList = url + "product/get_item_list";
    public static String delete_item = url + "product/delete_item";
    public static String getItemCategory = url + "product/get_categories";
    public static String saveItemImage = url + "product/save_image";
    public static String getItemDetail = url + "product/get_item_details";
    public static String item_thumblineImg = url + "product/product_thumbnail";

    public static String getActivityFeed = url + "activity/getFeeds";
    //    public static String getAllActivityFeed = url + "activity/get_all";
    public static String getAllActivityFeed = url + "activity/get_all_v2";
    public static String ansActivityFeedSurvey = url + "activity/ansSurvey";


    public static String activityDeletePost = url + "activity/deletePost";
    public static String getActivityInternalFeed = url + "activity/get_internal";
    public static String getActivitySocialFeed = url + "activity/get_social";
    public static String getActivityAlertFeed = url + "activity/get_alert";
    public static String ActivityLikeFeed = url + "activity/feedLikeNew";
    public static String ActivtyCommentMessage = url + "activity/feedCommentNew";
    public static String DeleteCommentActivity = url + "activity/deleteFeedComment";

    public static String saveActivityImages = url + "activity/saveActivityImages";
    public static String savePhotoFilterImage = url + "activity/add_photo_filter_image";
    public static String saveMultiplePhotoFilterImage = url + "activity/add_photo_filter_multi_image";
    public static String getPhotoFilterUrl = url + "activity/get_photofilter_url";

    public static String postUpdate = url + "activity/postUpdate";
    public static String getDetailActivityfeed = url + "activity/getFeedDetails";
    public static String getActivityImageList = url + "activity/getImageList";

    public static String authorized_payment = url + "product/authorize_payment";
    public static String getAdvertising = url + "settings/getAdvertising_new";
    public static String getPhotoFilerImage = url + "Photofilter/getFiltersByEvent";
    public static String getPhotoFilerUserPhotos = url + "Photofilter/getPhotosByUser";
    public static String uploadUserPhoto = url + "Photofilter/upload";
    public static String stripePayment = url + "product/stripePayment";

    public static String getNotificationCount = url + "message/notificationCounter";
    public static String NotificationSeen = url + "message/messageRead";
    public static String getInstagramFeed = url + "social/getInstagramFeeds";


    public static String getFavouritedExhibitors = url + "app/getFavouritedExhibitors";
    public static String checkUpdateData = url + "app/getModuleUpdateDate";
    //    public static String get_ExhibitorData = url + "exhibitor/getExhibitorListCategoryPcategoriestest";
    public static String get_ExhibitorData = url + "exhibitor/getExhibitorListCategoryPcategoriesoffline";
    public static String getGroupModuleData = url + "group/get_group_data";
    public static String getMenuGroupData = url + "group/get_menu_group";
    public static String getCMSSuperGroupData = url + "cms/get_cms_super_group";
    public static String getCMSChildGroupData = url + "cms/get_chid_cms_group";
    public static String getCMSListData = url + "cms/get_child_cms_list";

    public static String get_AgendaList_offline = url + "agenda/get_agenda_offline";


    public static String presantationDetailLockUnlock = url + "presentation1/lockUnlockSlides";
    public static String presantationDetailPushSlide = url + "presentation1/pushSlide";
    public static String presantationDetailSaveSurvey = url + "presentation1/saveSurveyAnswer";
    public static String presantationDetailPushViewResult = url + "presentation1/pushResult";

    public static String presantationDetailViewResult = url + "presentation1/getChartResult";
    public static String presantationDetailRemoveResult = url + "presentation1/removeChartResult";
    public static String presantationDetailRemovePushSlide = url + "presentation1/removePushSlide";
    public static String Presantation_By_Time = url + "presentation1/getPrsentationByTime";
    public static String Presantation_By_Type = url + "presentation1/getPrsentationByType";
    public static String Presantation_Detail = url + "presentation1/viewPresentationByRole";

    public static String getPresantationImageRefresh_Detail = url + "presentation1/getPrsentationImagesByIdRefresh";
    public static String presantationSendMessge = url + "presentation1/sendmessage";

    public static String getAttendeePortalListing = url + "checkin/attendeeList";
    public static String getChekedInList = url + "checkin/get_checkedin_attendee";
    public static String checkInPortal = url + "checkin/attendeeCheckIn";
    public static String ViewAttnedeePortal = url + "checkin/viewAttendeeInfo";
    public static String saveAttendee = url + "checkin/saveAttendeeInfo";
    public static String shareContactInformation = url + "attendee/shareContactInformation";
    public static String approvedshareContactDetail = url + "attendee/approveStatus";
    public static String rejectshareContactDetail = url + "attendee/rejectStatus ";
    public static String getRequestMetttingDateTime = url + "exhibitor/getRequestMeetingDateTime";

    public static String exhibitorRequestMeeting = url + "exhibitor/requestMeeting";
    public static String exhibitorSaveMeeting = url + "exhibitor/saveRequestMeeting";
    public static String exhibitorgetAllRequestMeeting = url + "exhibitor/getAllMeetingRequestNew";
    public static String exhibitorgetAllRequestMeetingwithDateNew = url + "exhibitor/getAllMeetingRequestWithDate";
    public static String exhibitorRequestResponse = url + "exhibitor/respondRequest";
    public static String setAgendaRemider = url + "reminder/setAgendaReminder";
    public static String getnotificationListing = url + "message/getNotificationListing";
    public static String deleteNotificationListing = url + "message/deleteNotification";
    public static String pageUserClick = url + "settings/userClickBoard";
    public static String getSuggestedTimings = url + "exhibitor/getRequestMeetingDateTime";
    public static String suggestedTime = url + "exhibitor/suggestMeetingTime";
    public static String getSuggestTime = url + "exhibitor/getSuggestedTimings";
    public static String bookSuggestedTime = url + "exhibitor/bookSuggestedTime";
    public static String getFacebookFeed = url + "social/getFacebookFeed";
    public static String updateVersionCode = url + "settings/checkVersionCode";


    public static String getAttendeeRequestMetttingDateTime = url + "attendee/getRequestMeetingDateTime";
    public static String getAttendeeRequestMeetingDateNew = url + "attendee/getRequestMeetingDate";
    public static String attendeeRequestMeeting = url + "attendee/requestMeeting";
    public static String attendeeSaveMeeting = url + "attendee/saveRequestMeeting";
    public static String attendeegetAllRequestMeeting = url + "attendee/getAllMeetingRequestNew";
    public static String attendeegetAllRequestMeetingwithDate = url + "attendee/getAllMeetingRequestWithDate";
    public static String attendeeRequestResponse = url + "attendee/respondRequest";
    public static String attendeegetSuggestedTimings = url + "attendee/getRequestMeetingDateTime";
    public static String attendeeSugeestTime = url + "attendee/suggestMeetingTime";
    public static String addRemove = url + "favorites/addOrRemove";
    public static String addRemoveAttendee = url + "favorites/addOrRemoveAttendee";
    public static String savtoMyfavorite = url + "exhibitor/saveToFavoritesV2";
    public static String getFavoriteList = url + "favorites/getFavoritesList";

    public static String getPrivateUnreadMessageList = url + "message/getPrivateUnreadMessagesList_tmp";
    public static String getPrivateUnreadMessageSenderwise = url + "message/getPrivateMessagesConversation";
    public static String getVirtualMarketList = url + "gulfood/getVirtualSuperMarket";
    public static String getVirtualMarketExhibitorDetail = url + "gulfood/getExhibitorProfile";
    public static String sendBeaconId = url + "beacons/save_beacon";
    public static String checkinPortalScan = url + "checkin/addCheckin_new";
    public static String getALlBeaconIdList = url + "beacons/get_all_beacon";
    public static String sendNotification = url + "beacons/get_notification";
    public static String renameBeacon = url + "beacons/rename_beacon";
    public static String deleleBeacon = url + "beacons/delete_beacon";
    public static String getCheckinPdf = url + "checkin/get_pdf";

    public static String getAdditionalData = url + "profile/get_additional_data";
    public static String saveProfileGoals = url + "attendee/updateAttendeeGoal";
    public static String updateProfileAttendee = url + "profile/update_profile_with_additional_data";
    public static String getSurveyCategoryListing = url + "survey/get_survey_category";
    public static String deleteQaMessage = url + "qa/DeleteMessage";
    public static String hideQaMessage = url + "qa/hideMessage";
    public static String approvedQaMessage = url + "qa/Approve_qa";
    public static String qaShowOnWeb = url + "qa/show_on_web";
    public static String getQaListSession = url + "qa/getAllSession";
    public static String getQaDetailSession = url + "qa/getSessionDetail";
    public static String getHiddenQuestion = url + "qa/getHiddenQuestion";
    public static String voteUpFeed = url + "qa/VoteMessage";
    public static String qAsendMessage = url + "qa/sendMessage";
    public static String qAModeratorUpVote = url + "qa/ModeratorVoteMessage";


    public static String getGamificationData = url + "gamification/get_leaderboard";
    public static String getOnBoardScreen = url + "settings/get_o_screen";
    public static String getDataFromScanLead = url + "lead/scan_lead";
    public static String getExhibitorLeads = url + "lead/get_exhi_leads";
    public static String removeExhibitorRep = url + "lead/remove_rep";
    public static String saveSurveyLeadData = url + "lead/save_questions";
    public static String resetLeadData = url + "lead/reset_questions_answer";
    public static String UpdateSurveyLeadData = url + "lead/update_questions_answer";
    public static String getExhibitor_attendeeList = url + "lead/get_attendee_list_for_rep";
    public static String addNewExhibitorRepresantatives = url + "lead/add_new_representative";
    public static String getExhibitorRepresentativesLead = url + "lead/rep_list";
    public static String exportedLead = url + "lead/export_lead";
    public static String saveScanLeadData = url + "lead/save_scan_lead";
    public static String getofllineDataForExhibitorLead = url + "lead/get_offline_data";
    public static String saveSurveyOfflineData = url + "lead/save_scan_upload";
    public static String getLeadDetailFromMyLead = url + "lead/get_lead_details";
    public static String MyleadUpdateData = url + "lead/updateLead";
    public static String getMyLeadOffline = url + "lead/get_exhi_leads_offline";
    public static String downloadNotes = url + "note/download_pdf";

    public static String getinviteMoreData = url + "attendee/getAttendeeForInviting";
    public static String getinvitedAttedeeMoreData = url + "attendee/GetInvitedAttendee";
    public static String inviteAttendee = url + "attendee/InviteAttendee";
    public static String scanBadge = url + "badge_scanner/ScanBadge";
    public static String scanQrForSurveyandAttendee = url + "badge_scanner/scanQr";

    public static String getTimeFromDateNew = url + "attendee/getRequestMeetingTime";
    public static String getRequestMeetingLocation = url + "attendee/getRequestMeetingLocation";

    //MatchMaking API
    public static String getMathcMakingModuleName = url + "matchmaking/getModules";
    public static String getMathcMakingModuleAttendeeData = url + "matchmaking/getAttendees";
    public static String getMathcMakingModuleSpeakerData = url + "matchmaking/getSpeaker";
    public static String getMathcMakingModuleExhibitorData = url + "matchmaking/getExhibitor";
    public static String getMathcMakingModuleSponsorData = url + "matchmaking/getSponsor";

    public static String getDefaultLang = url + "app/get_default_lang";
    public static String routeWayFinding = "http://gvtesting.cloudapp.net/cgi-bin/Loop/getroute.py";
    public static String blockAttendee = url + "attendee/blockAttendee";
    public static String exhibitorMarkAsVisited = url + "matchmaking/markAsVisitedV2";
    public static String hideAttendeeIdentity = url + "attendee/hideMyIdentity";


    public static String getGeoLocationData = url + "Notification/getGeoNoti";
    public static String updateUserNoti = url + "Notification/updateUserNoti";
    public static String NotificationUpdateLogs = url + "Notification/updateLogs";



    //Uid URLs
    public static String LoginUid = url + "development/check";
    public static String authorizedLoginUid = url + "development/authorizedLogin";
    public static String Register_dataUid = url + "development/registrationByEvent";
    public static String FbLoginUid = url + "development/fb_signup";
    public static String linedInLoginUid = url + "development/linkedInSignup";
    public static String updateContactDetailUid = url + "development/update_contact_details";
    public static String updateProfileAttendeeUid = url + "development/update_profile_with_additional_data";
    public static String forgotPasswordUid = url + "development/forgot_password";
    public static String scanQrForSurveyandAttendeeUid = url + "development/scanQr";
    public static String getAttendeeContactListUid = url + "development/attendee_contact_list_new";
    public static String shareContactInformationUid = url + "development/shareContactInformation";
    public static String getAttendeeListNewUid = url + "development/attendee_list_v2";
    public static String home_pageIndexUid = url + "development/event_id_advance_design";
    public static String addRemoveUid = url + "development/addOrRemove";
    public static String attendeeRequestMeetingUid = url + "development/requestMeeting";
    public static String attendeeSaveMeetingUid = url + "development/saveRequestMeeting";
    public static String getGamificationDataUid = url + "development/get_leaderboard";
    public static String getAttendeeRequestMeetingDateNewUid = url + "attendee/getRequestMeetingDate_new";
    public static String private_messageSendtextUid = url + "message/privateMessageSendText_new";

    public static String get_ExhibitorDataUid = url + "exhibitor/getExhibitorListCategoryPcategoriesoffline1";
    public static String get_ExhibitorDetailUid = url + "exhibitor/exhibitor_view_unread_count1";
    public static String savtoMyfavoriteUid  = url + "exhibitor/saveToFavorites1";
    public static String get_SpeakerList_offlineUid = url + "speaker/speaker_list_offline1";
    public static String get_SpeakerDetailUid = url + "speaker/speaker_view_unread_countV2_new";
    public static String get_SponsorList_offlineUid = url + "sponsors/sponsors_list_offline1";
    public static String get_SponsorDetailv2Uid = url + "sponsors/sponsors_view_v2_new";
    public static String get_UserAgenda_ByTimeUid = url + "agenda/getUserAgendaByTimeWithMeeting1";
    public static String getAllActivityFeedUid = url + "activity/get_all_v2_new";
    public static String get_MapDetailUid = url + "maps/map_details1";
    public static String Presantation_DetailUid = url + "presentation1/viewPresentationByRole1";
    public static String getPresantationImageRefresh_DetailUid = url + "presentation1/getPrsentationImagesByIdRefresh1";
    public static String presantationDetailViewResultUid = url + "presentation1/getChartResult1";
    public static String exhibitorMarkAsVisitedUid = url + "matchmaking/markAsVisited";
    public static String getAllPublicMessageUid = url + "message/getPublicMessages1";
    public static String getPrivateUnreadMessageListUid = url + "message/getPrivateUnreadMessagesList_tmp1";
    public static String downloadNotesUid = url + "note/download_pdf1";
    public static String Silentauction_productListUid = url + "product/product_listing1";
    public static String getFavoriteListUid = url + "favorites/getFavoritesList1";
    public static String update_gcmUid = url + "login/updateUserGCMId1";
    public static String approvedQaMessageUid = url + "qa/Approve_qa1";


}

