package com.allintheloop.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Aiyaz on 1/8/17.
 */


public class DefaultLanguage {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("default_lang")
    @Expose
    private DefaultLang defaultLang;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public DefaultLang getDefaultLang() {
        return defaultLang;
    }

    public void setDefaultLang(DefaultLang defaultLang) {
        this.defaultLang = defaultLang;
    }

    public class DefaultLang {

        @SerializedName("1__sort_by_time")
        @Expose
        private String _1SortByTime;
        @SerializedName("1__sort_by_track")
        @Expose
        private String _1SortByTrack;
        @SerializedName("1__view_my_agenda")
        @Expose
        private String _1ViewMyAgenda;
        @SerializedName("1__start")
        @Expose
        private String _1Start;
        @SerializedName("1__end")
        @Expose
        private String _1End;
        @SerializedName("1__save_session")
        @Expose
        private String _1SaveSession;
        @SerializedName("1__saved")
        @Expose
        private String _1Saved;
        @SerializedName("1__provide_feedback")
        @Expose
        private String _1ProvideFeedback;
        @SerializedName("1__check_in")
        @Expose
        private String _1CheckIn;
        @SerializedName("1__rate_this_session")
        @Expose
        private String _1RateThisSession;
        @SerializedName("1__cancel")
        @Expose
        private String _1Cancel;
        @SerializedName("1__comments")
        @Expose
        private String _1Comments;
        @SerializedName("1__submit")
        @Expose
        private String _1Submit;
        @SerializedName("1__hosted_by")
        @Expose
        private String _1HostedBy;
        @SerializedName("2__full_directory")
        @Expose
        private String _2FullDirectory;
        @SerializedName("2__my_contacts")
        @Expose
        private String _2MyContacts;
        @SerializedName("2__search")
        @Expose
        private String _2Search;
        @SerializedName("2__cancel")
        @Expose
        private String _2Cancel;
        @SerializedName("2__send_message")
        @Expose
        private String _2SendMessage;
        @SerializedName("2__request_meeting")
        @Expose
        private String _2RequestMeeting;
        @SerializedName("2__share_contact_information")
        @Expose
        private String _2ShareContactInformation;
        @SerializedName("2__request_a_meeting_with")
        @Expose
        private String _2RequestAMeetingWith;
        @SerializedName("2__date")
        @Expose
        private String _2Date;
        @SerializedName("2__select_date")
        @Expose
        private String _2SelectDate;
        @SerializedName("2__time")
        @Expose
        private String _2Time;
        @SerializedName("2__select_time")
        @Expose
        private String _2SelectTime;
        @SerializedName("2__location")
        @Expose
        private String _2Location;
        @SerializedName("2__enter_meeting_location")
        @Expose
        private String _2EnterMeetingLocation;
        @SerializedName("3__search")
        @Expose
        private String _3Search;
        @SerializedName("3__request_a_meeting")
        @Expose
        private String _3RequestAMeeting;
        @SerializedName("3__share_contact")
        @Expose
        private String _3ShareContact;
        @SerializedName("3__save_to_my_favorites")
        @Expose
        private String _3SaveToMyFavorites;
        @SerializedName("3__send_message")
        @Expose
        private String _3SendMessage;
        @SerializedName("3__keywords")
        @Expose
        private String _3Keywords;
        @SerializedName("3__stand")
        @Expose
        private String _3Stand;
        @SerializedName("3__view_on_map")
        @Expose
        private String _3ViewOnMap;
        @SerializedName("7__search")
        @Expose
        private String _7Search;
        @SerializedName("7__speaker")
        @Expose
        private String _7Speaker;
        @SerializedName("7__ask_a_question")
        @Expose
        private String _7AskAQuestion;
        @SerializedName("12__your_conversations")
        @Expose
        private String _12YourConversations;
        @SerializedName("12__start_a_new_conversation")
        @Expose
        private String _12StartANewConversation;
        @SerializedName("12__view_profile")
        @Expose
        private String _12ViewProfile;
        @SerializedName("12__type_your_message_here")
        @Expose
        private String _12TypeYourMessageHere;
        @SerializedName("12__add_photo")
        @Expose
        private String _12AddPhoto;
        @SerializedName("12__send")
        @Expose
        private String _12Send;
        @SerializedName("12__choose_from")
        @Expose
        private String _12ChooseFrom;
        @SerializedName("12__camera")
        @Expose
        private String _12Camera;
        @SerializedName("12__gallery")
        @Expose
        private String _12Gallery;
        @SerializedName("12__cancel")
        @Expose
        private String _12Cancel;
        @SerializedName("15__start")
        @Expose
        private String _15Start;
        @SerializedName("15__end")
        @Expose
        private String _15End;
        @SerializedName("49__search")
        @Expose
        private String _49Search;
        @SerializedName("43__search")
        @Expose
        private String _43Search;
        @SerializedName("49__you_have_not_saved_any_favorites_yet")
        @Expose
        private String _49YouHaveNotSavedAnyFavoritesYet;
        @SerializedName("43__website")
        @Expose
        private String _43Website;
        @SerializedName("6__add_notes")
        @Expose
        private String _6AddNotes;
        @SerializedName("6__view_notes")
        @Expose
        private String _6ViewNotes;
        @SerializedName("left_hand_menu__home")
        @Expose
        private String leftHandMenuHome;
        @SerializedName("left_hand_menu__notifications")
        @Expose
        private String leftHandMenuNotifications;
        @SerializedName("my_profile__my_profile")
        @Expose
        private String myProfileMyProfile;
        @SerializedName("my_profile__my_meetings")
        @Expose
        private String myProfileMyMeetings;
        @SerializedName("my_profile__logout")
        @Expose
        private String myProfileLogout;
        @SerializedName("my_profile__check_in_portal")
        @Expose
        private String myProfileCheckInPortal;
        @SerializedName("13__send_message")
        @Expose
        private String _13SendMessage;
        @SerializedName("13__add_photo")
        @Expose
        private String _13AddPhoto;
        @SerializedName("13__type_your_message_here")
        @Expose
        private String _13TypeYourMessageHere;
        @SerializedName("13__send")
        @Expose
        private String _13Send;
        @SerializedName("13__comment")
        @Expose
        private String _13Comment;
        @SerializedName("bell_icon__notification")
        @Expose
        private String bellIconNotification;
        @SerializedName("bell_icon__see_all_private")
        @Expose
        private String bellIconSeeAllPrivate;
        @SerializedName("bell_icon__see_all_public")
        @Expose
        private String bellIconSeeAllPublic;
        @SerializedName("bell_icon__push_notifications")
        @Expose
        private String bellIconPushNotifications;
        @SerializedName("1__pending_agenda")
        @Expose
        private String _1PendingAgenda;
        @SerializedName("2__add_photo")
        @Expose
        private String _2AddPhoto;
        @SerializedName("1__main_agenda")
        @Expose
        private String _1MainAgenda;
        @SerializedName("9__sort_by_time")
        @Expose
        private String _9SortByTime;
        @SerializedName("9__sort_by_type")
        @Expose
        private String _9SortByType;
        @SerializedName("16__search")
        @Expose
        private String _16Search;
        @SerializedName("16__view_all")
        @Expose
        private String _16ViewAll;
        @SerializedName("11__add_photo")
        @Expose
        private String _11AddPhoto;
        @SerializedName("11__take_a_photo_on_your_device_camera")
        @Expose
        private String _11TakeAPhotoOnYourDeviceCamera;
        @SerializedName("11__send_your_photo")
        @Expose
        private String _11SendYourPhoto;
        @SerializedName("11__feed")
        @Expose
        private String _11Feed;
        @SerializedName("45__all")
        @Expose
        private String _45All;
        @SerializedName("45__internal")
        @Expose
        private String _45Internal;
        @SerializedName("45__social")
        @Expose
        private String _45Social;
        @SerializedName("45__alert")
        @Expose
        private String _45Alert;
        @SerializedName("45__type_your_post_here")
        @Expose
        private String _45TypeYourPostHere;
        @SerializedName("45__publish_post")
        @Expose
        private String _45PublishPost;
        @SerializedName("45__like")
        @Expose
        private String _45Like;
        @SerializedName("45__comment")
        @Expose
        private String _45Comment;
        @SerializedName("45__comments")
        @Expose
        private String _45Comments;
        @SerializedName("45__no_comments_have_been_made_yet")
        @Expose
        private String _45NoCommentsHaveBeenMadeYet;
        @SerializedName("45__share_this_post")
        @Expose
        private String _45ShareThisPost;
        @SerializedName("45__oK")
        @Expose
        private String _45OK;
        @SerializedName("45__updated_profile_picture ")
        @Expose
        private String _45UpdatedProfilePicture;
        @SerializedName("50__search_q&a")
        @Expose
        private String _50SearchQA;
        @SerializedName("50__tap_a_session_to_ask_a_question_and_interact")
        @Expose
        private String _50TapASessionToAskAQuestionAndInteract;
        @SerializedName("50__questions")
        @Expose
        private String _50Questions;
        @SerializedName("50__start")
        @Expose
        private String _50Start;
        @SerializedName("50__end")
        @Expose
        private String _50End;
        @SerializedName("50__refresh")
        @Expose
        private String _50Refresh;
        @SerializedName("50__ask_your_own_question_or_vote_other_to_the_top")
        @Expose
        private String _50AskYourOwnQuestionOrVoteOtherToTheTop;
        @SerializedName("50__ask_a_question")
        @Expose
        private String _50AskAQuestion;
        @SerializedName("50__anonymous")
        @Expose
        private String _50Anonymous;
        @SerializedName("52__earn_points_now")
        @Expose
        private String _52EarnPointsNow;
        @SerializedName("52__points")
        @Expose
        private String _52Points;
        @SerializedName("52__send_a_message")
        @Expose
        private String _52SendAMessage;
        @SerializedName("52__request_a_meeting")
        @Expose
        private String _52RequestAMeeting;
        @SerializedName("52__accept_a_meeting")
        @Expose
        private String _52AcceptAMeeting;
        @SerializedName("52__share_contact_information")
        @Expose
        private String _52ShareContactInformation;
        @SerializedName("52__walk_past_a_beacon")
        @Expose
        private String _52WalkPastABeacon;
        @SerializedName("52__answer_a_survey")
        @Expose
        private String _52AnswerASurvey;
        @SerializedName("sign_up_process__create_your_account_to_gain_access_to")
        @Expose
        private String signUpProcessCreateYourAccountToGainAccessTo;
        @SerializedName("sign_up_process__email")
        @Expose
        private String signUpProcessEmail;
        @SerializedName("sign_up_process__password")
        @Expose
        private String signUpProcessPassword;
        @SerializedName("sign_up_process__log_in")
        @Expose
        private String signUpProcessLogIn;
        @SerializedName("sign_up_process__forgot_password")
        @Expose
        private String signUpProcessForgotPassword;
        @SerializedName("sign_up_process__or")
        @Expose
        private String signUpProcessOr;
        @SerializedName("sign_up_process__register_for_the_app")
        @Expose
        private String signUpProcessRegisterForTheApp;
        @SerializedName("sign_up_process__first_name")
        @Expose
        private String signUpProcessFirstName;
        @SerializedName("sign_up_process__last_name")
        @Expose
        private String signUpProcessLastName;
        @SerializedName("sign_up_process__password_again")
        @Expose
        private String signUpProcessPasswordAgain;
        @SerializedName("sign_up_process__company_name")
        @Expose
        private String signUpProcessCompanyName;
        @SerializedName("sign_up_process__title")
        @Expose
        private String signUpProcessTitle;
        @SerializedName("sign_up_process__select_country")
        @Expose
        private String signUpProcessSelectCountry;
        @SerializedName("sign_up_process__i_agree_to_the_terms_&_conditions")
        @Expose
        private String signUpProcessIAgreeToTheTermsConditions;
        @SerializedName("sign_up_process__submit")
        @Expose
        private String signUpProcessSubmit;
        @SerializedName("sign_up_process__go_back_to_login_screen")
        @Expose
        private String signUpProcessGoBackToLoginScreen;
        @SerializedName("time_and_date__monday")
        @Expose
        private String timeAndDateMonday;
        @SerializedName("time_and_date__tuesday")
        @Expose
        private String timeAndDateTuesday;
        @SerializedName("time_and_date__wednesday")
        @Expose
        private String timeAndDateWednesday;
        @SerializedName("time_and_date__thursday")
        @Expose
        private String timeAndDateThursday;
        @SerializedName("time_and_date__friday")
        @Expose
        private String timeAndDateFriday;
        @SerializedName("time_and_date__saturday")
        @Expose
        private String timeAndDateSaturday;
        @SerializedName("time_and_date__sunday")
        @Expose
        private String timeAndDateSunday;
        @SerializedName("time_and_date__seconds")
        @Expose
        private String timeAndDateSeconds;
        @SerializedName("time_and_date__minutes")
        @Expose
        private String timeAndDateMinutes;
        @SerializedName("time_and_date__hours")
        @Expose
        private String timeAndDateHours;
        @SerializedName("time_and_date__days")
        @Expose
        private String timeAndDateDays;
        @SerializedName("time_and_date__weeks")
        @Expose
        private String timeAndDateWeeks;
        @SerializedName("time_and_date__months")
        @Expose
        private String timeAndDateMonths;
        @SerializedName("time_and_date__years")
        @Expose
        private String timeAndDateYears;
        @SerializedName("my_profile__login")
        @Expose
        private String myProfileLogin;
        @SerializedName("left_hand_menu__exit")
        @Expose
        private String leftHandMenuExit;
        @SerializedName("sign_up_process__next")
        @Expose
        private String signUpProcessNext;
        @SerializedName("notifications__moderator_request_meeting_accept")
        @Expose
        private String notificationsModeratorRequestMeetingAccept;
        @SerializedName("notifications__request_meeting_accept_suggested_datetime")
        @Expose
        private String notificationsRequestMeetingAcceptSuggestedDatetime;
        @SerializedName("notifications__request_meeting_suggested_datetime")
        @Expose
        private String notificationsRequestMeetingSuggestedDatetime;
        @SerializedName("notifications__request_meeting_accept")
        @Expose
        private String notificationsRequestMeetingAccept;
        @SerializedName("notifications__request_meeting_reject")
        @Expose
        private String notificationsRequestMeetingReject;
        @SerializedName("notifications__request_meeting_without_modaretor")
        @Expose
        private String notificationsRequestMeetingWithoutModaretor;
        @SerializedName("notifications__request_meeting_with_modaretor")
        @Expose
        private String notificationsRequestMeetingWithModaretor;
        @SerializedName("notifications__share_contact")
        @Expose
        private String notificationsShareContact;
        @SerializedName("notifications__message_received")
        @Expose
        private String notificationsMessageReceived;
        @SerializedName("notifications__no_notifications_found")
        @Expose
        private String notificationsNoNotificationsFound;
        @SerializedName("lang_id")
        @Expose
        private String langId;
        @SerializedName("lang_name")
        @Expose
        private String langName;
        @SerializedName("lang_icon")
        @Expose
        private String langIcon;

        public String get1SortByTime() {
            return _1SortByTime;
        }

        public void set1SortByTime(String _1SortByTime) {
            this._1SortByTime = _1SortByTime;
        }

        public String get1SortByTrack() {
            return _1SortByTrack;
        }

        public void set1SortByTrack(String _1SortByTrack) {
            this._1SortByTrack = _1SortByTrack;
        }

        public String get1ViewMyAgenda() {
            return _1ViewMyAgenda;
        }

        public void set1ViewMyAgenda(String _1ViewMyAgenda) {
            this._1ViewMyAgenda = _1ViewMyAgenda;
        }

        public String get1Start() {
            return _1Start;
        }

        public void set1Start(String _1Start) {
            this._1Start = _1Start;
        }

        public String get1End() {
            return _1End;
        }

        public void set1End(String _1End) {
            this._1End = _1End;
        }

        public String get1SaveSession() {
            return _1SaveSession;
        }

        public void set1SaveSession(String _1SaveSession) {
            this._1SaveSession = _1SaveSession;
        }

        public String get1Saved() {
            return _1Saved;
        }

        public void set1Saved(String _1Saved) {
            this._1Saved = _1Saved;
        }

        public String get1ProvideFeedback() {
            return _1ProvideFeedback;
        }

        public void set1ProvideFeedback(String _1ProvideFeedback) {
            this._1ProvideFeedback = _1ProvideFeedback;
        }

        public String get1CheckIn() {
            return _1CheckIn;
        }

        public void set1CheckIn(String _1CheckIn) {
            this._1CheckIn = _1CheckIn;
        }

        public String get1RateThisSession() {
            return _1RateThisSession;
        }

        public void set1RateThisSession(String _1RateThisSession) {
            this._1RateThisSession = _1RateThisSession;
        }

        public String get1Cancel() {
            return _1Cancel;
        }

        public void set1Cancel(String _1Cancel) {
            this._1Cancel = _1Cancel;
        }

        public String get1Comments() {
            return _1Comments;
        }

        public void set1Comments(String _1Comments) {
            this._1Comments = _1Comments;
        }

        public String get1Submit() {
            return _1Submit;
        }

        public void set1Submit(String _1Submit) {
            this._1Submit = _1Submit;
        }

        public String get1HostedBy() {
            return _1HostedBy;
        }

        public void set1HostedBy(String _1HostedBy) {
            this._1HostedBy = _1HostedBy;
        }

        public String get2FullDirectory() {
            return _2FullDirectory;
        }

        public void set2FullDirectory(String _2FullDirectory) {
            this._2FullDirectory = _2FullDirectory;
        }

        public String get2MyContacts() {
            return _2MyContacts;
        }

        public void set2MyContacts(String _2MyContacts) {
            this._2MyContacts = _2MyContacts;
        }

        public String get2Search() {
            return _2Search;
        }

        public void set2Search(String _2Search) {
            this._2Search = _2Search;
        }

        public String get2Cancel() {
            return _2Cancel;
        }

        public void set2Cancel(String _2Cancel) {
            this._2Cancel = _2Cancel;
        }

        public String get2SendMessage() {
            return _2SendMessage;
        }

        public void set2SendMessage(String _2SendMessage) {
            this._2SendMessage = _2SendMessage;
        }

        public String get2RequestMeeting() {
            return _2RequestMeeting;
        }

        public void set2RequestMeeting(String _2RequestMeeting) {
            this._2RequestMeeting = _2RequestMeeting;
        }

        public String get2ShareContactInformation() {
            return _2ShareContactInformation;
        }

        public void set2ShareContactInformation(String _2ShareContactInformation) {
            this._2ShareContactInformation = _2ShareContactInformation;
        }

        public String get2RequestAMeetingWith() {
            return _2RequestAMeetingWith;
        }

        public void set2RequestAMeetingWith(String _2RequestAMeetingWith) {
            this._2RequestAMeetingWith = _2RequestAMeetingWith;
        }

        public String get2Date() {
            return _2Date;
        }

        public void set2Date(String _2Date) {
            this._2Date = _2Date;
        }

        public String get2SelectDate() {
            return _2SelectDate;
        }

        public void set2SelectDate(String _2SelectDate) {
            this._2SelectDate = _2SelectDate;
        }

        public String get2Time() {
            return _2Time;
        }

        public void set2Time(String _2Time) {
            this._2Time = _2Time;
        }

        public String get2SelectTime() {
            return _2SelectTime;
        }

        public void set2SelectTime(String _2SelectTime) {
            this._2SelectTime = _2SelectTime;
        }

        public String get2Location() {
            return _2Location;
        }

        public void set2Location(String _2Location) {
            this._2Location = _2Location;
        }

        public String get2EnterMeetingLocation() {
            return _2EnterMeetingLocation;
        }

        public void set2EnterMeetingLocation(String _2EnterMeetingLocation) {
            this._2EnterMeetingLocation = _2EnterMeetingLocation;
        }

        public String get3Search() {
            return _3Search;
        }

        public void set3Search(String _3Search) {
            this._3Search = _3Search;
        }

        public String get3RequestAMeeting() {
            return _3RequestAMeeting;
        }

        public void set3RequestAMeeting(String _3RequestAMeeting) {
            this._3RequestAMeeting = _3RequestAMeeting;
        }

        public String get3ShareContact() {
            return _3ShareContact;
        }

        public void set3ShareContact(String _3ShareContact) {
            this._3ShareContact = _3ShareContact;
        }

        public String get3SaveToMyFavorites() {
            return _3SaveToMyFavorites;
        }

        public void set3SaveToMyFavorites(String _3SaveToMyFavorites) {
            this._3SaveToMyFavorites = _3SaveToMyFavorites;
        }

        public String get3SendMessage() {
            return _3SendMessage;
        }

        public void set3SendMessage(String _3SendMessage) {
            this._3SendMessage = _3SendMessage;
        }

        public String get3Keywords() {
            return _3Keywords;
        }

        public void set3Keywords(String _3Keywords) {
            this._3Keywords = _3Keywords;
        }

        public String get3Stand() {
            return _3Stand;
        }

        public void set3Stand(String _3Stand) {
            this._3Stand = _3Stand;
        }

        public String get3ViewOnMap() {
            return _3ViewOnMap;
        }

        public void set3ViewOnMap(String _3ViewOnMap) {
            this._3ViewOnMap = _3ViewOnMap;
        }

        public String get7Search() {
            return _7Search;
        }

        public void set7Search(String _7Search) {
            this._7Search = _7Search;
        }

        public String get7Speaker() {
            return _7Speaker;
        }

        public void set7Speaker(String _7Speaker) {
            this._7Speaker = _7Speaker;
        }

        public String get7AskAQuestion() {
            return _7AskAQuestion;
        }

        public void set7AskAQuestion(String _7AskAQuestion) {
            this._7AskAQuestion = _7AskAQuestion;
        }

        public String get12YourConversations() {
            return _12YourConversations;
        }

        public void set12YourConversations(String _12YourConversations) {
            this._12YourConversations = _12YourConversations;
        }

        public String get12StartANewConversation() {
            return _12StartANewConversation;
        }

        public void set12StartANewConversation(String _12StartANewConversation) {
            this._12StartANewConversation = _12StartANewConversation;
        }

        public String get12ViewProfile() {
            return _12ViewProfile;
        }

        public void set12ViewProfile(String _12ViewProfile) {
            this._12ViewProfile = _12ViewProfile;
        }

        public String get12TypeYourMessageHere() {
            return _12TypeYourMessageHere;
        }

        public void set12TypeYourMessageHere(String _12TypeYourMessageHere) {
            this._12TypeYourMessageHere = _12TypeYourMessageHere;
        }

        public String get12AddPhoto() {
            return _12AddPhoto;
        }

        public void set12AddPhoto(String _12AddPhoto) {
            this._12AddPhoto = _12AddPhoto;
        }

        public String get12Send() {
            return _12Send;
        }

        public void set12Send(String _12Send) {
            this._12Send = _12Send;
        }

        public String get12ChooseFrom() {
            return _12ChooseFrom;
        }

        public void set12ChooseFrom(String _12ChooseFrom) {
            this._12ChooseFrom = _12ChooseFrom;
        }

        public String get12Camera() {
            return _12Camera;
        }

        public void set12Camera(String _12Camera) {
            this._12Camera = _12Camera;
        }

        public String get12Gallery() {
            return _12Gallery;
        }

        public void set12Gallery(String _12Gallery) {
            this._12Gallery = _12Gallery;
        }

        public String get12Cancel() {
            return _12Cancel;
        }

        public void set12Cancel(String _12Cancel) {
            this._12Cancel = _12Cancel;
        }

        public String get15Start() {
            return _15Start;
        }

        public void set15Start(String _15Start) {
            this._15Start = _15Start;
        }

        public String get15End() {
            return _15End;
        }

        public void set15End(String _15End) {
            this._15End = _15End;
        }

        public String get49Search() {
            return _49Search;
        }

        public void set49Search(String _49Search) {
            this._49Search = _49Search;
        }

        public String get43Search() {
            return _43Search;
        }

        public void set43Search(String _43Search) {
            this._43Search = _43Search;
        }

        public String get49YouHaveNotSavedAnyFavoritesYet() {
            return _49YouHaveNotSavedAnyFavoritesYet;
        }

        public void set49YouHaveNotSavedAnyFavoritesYet(String _49YouHaveNotSavedAnyFavoritesYet) {
            this._49YouHaveNotSavedAnyFavoritesYet = _49YouHaveNotSavedAnyFavoritesYet;
        }

        public String get43Website() {
            return _43Website;
        }

        public void set43Website(String _43Website) {
            this._43Website = _43Website;
        }

        public String get6AddNotes() {
            return _6AddNotes;
        }

        public void set6AddNotes(String _6AddNotes) {
            this._6AddNotes = _6AddNotes;
        }

        public String get6ViewNotes() {
            return _6ViewNotes;
        }

        public void set6ViewNotes(String _6ViewNotes) {
            this._6ViewNotes = _6ViewNotes;
        }

        public String getLeftHandMenuHome() {
            return leftHandMenuHome;
        }

        public void setLeftHandMenuHome(String leftHandMenuHome) {
            this.leftHandMenuHome = leftHandMenuHome;
        }

        public String getLeftHandMenuNotifications() {
            return leftHandMenuNotifications;
        }

        public void setLeftHandMenuNotifications(String leftHandMenuNotifications) {
            this.leftHandMenuNotifications = leftHandMenuNotifications;
        }

        public String getMyProfileMyProfile() {
            return myProfileMyProfile;
        }

        public void setMyProfileMyProfile(String myProfileMyProfile) {
            this.myProfileMyProfile = myProfileMyProfile;
        }

        public String getMyProfileMyMeetings() {
            return myProfileMyMeetings;
        }

        public void setMyProfileMyMeetings(String myProfileMyMeetings) {
            this.myProfileMyMeetings = myProfileMyMeetings;
        }

        public String getMyProfileLogout() {
            return myProfileLogout;
        }

        public void setMyProfileLogout(String myProfileLogout) {
            this.myProfileLogout = myProfileLogout;
        }

        public String getMyProfileCheckInPortal() {
            return myProfileCheckInPortal;
        }

        public void setMyProfileCheckInPortal(String myProfileCheckInPortal) {
            this.myProfileCheckInPortal = myProfileCheckInPortal;
        }

        public String get13SendMessage() {
            return _13SendMessage;
        }

        public void set13SendMessage(String _13SendMessage) {
            this._13SendMessage = _13SendMessage;
        }

        public String get13AddPhoto() {
            return _13AddPhoto;
        }

        public void set13AddPhoto(String _13AddPhoto) {
            this._13AddPhoto = _13AddPhoto;
        }

        public String get13TypeYourMessageHere() {
            return _13TypeYourMessageHere;
        }

        public void set13TypeYourMessageHere(String _13TypeYourMessageHere) {
            this._13TypeYourMessageHere = _13TypeYourMessageHere;
        }

        public String get13Send() {
            return _13Send;
        }

        public void set13Send(String _13Send) {
            this._13Send = _13Send;
        }

        public String get13Comment() {
            return _13Comment;
        }

        public void set13Comment(String _13Comment) {
            this._13Comment = _13Comment;
        }

        public String getBellIconNotification() {
            return bellIconNotification;
        }

        public void setBellIconNotification(String bellIconNotification) {
            this.bellIconNotification = bellIconNotification;
        }

        public String getBellIconSeeAllPrivate() {
            return bellIconSeeAllPrivate;
        }

        public void setBellIconSeeAllPrivate(String bellIconSeeAllPrivate) {
            this.bellIconSeeAllPrivate = bellIconSeeAllPrivate;
        }

        public String getBellIconSeeAllPublic() {
            return bellIconSeeAllPublic;
        }

        public void setBellIconSeeAllPublic(String bellIconSeeAllPublic) {
            this.bellIconSeeAllPublic = bellIconSeeAllPublic;
        }

        public String getBellIconPushNotifications() {
            return bellIconPushNotifications;
        }

        public void setBellIconPushNotifications(String bellIconPushNotifications) {
            this.bellIconPushNotifications = bellIconPushNotifications;
        }

        public String get1PendingAgenda() {
            return _1PendingAgenda;
        }

        public void set1PendingAgenda(String _1PendingAgenda) {
            this._1PendingAgenda = _1PendingAgenda;
        }

        public String get2AddPhoto() {
            return _2AddPhoto;
        }

        public void set2AddPhoto(String _2AddPhoto) {
            this._2AddPhoto = _2AddPhoto;
        }

        public String get1MainAgenda() {
            return _1MainAgenda;
        }

        public void set1MainAgenda(String _1MainAgenda) {
            this._1MainAgenda = _1MainAgenda;
        }

        public String get9SortByTime() {
            return _9SortByTime;
        }

        public void set9SortByTime(String _9SortByTime) {
            this._9SortByTime = _9SortByTime;
        }

        public String get9SortByType() {
            return _9SortByType;
        }

        public void set9SortByType(String _9SortByType) {
            this._9SortByType = _9SortByType;
        }

        public String get16Search() {
            return _16Search;
        }

        public void set16Search(String _16Search) {
            this._16Search = _16Search;
        }

        public String get16ViewAll() {
            return _16ViewAll;
        }

        public void set16ViewAll(String _16ViewAll) {
            this._16ViewAll = _16ViewAll;
        }

        public String get11AddPhoto() {
            return _11AddPhoto;
        }

        public void set11AddPhoto(String _11AddPhoto) {
            this._11AddPhoto = _11AddPhoto;
        }

        public String get11TakeAPhotoOnYourDeviceCamera() {
            return _11TakeAPhotoOnYourDeviceCamera;
        }

        public void set11TakeAPhotoOnYourDeviceCamera(String _11TakeAPhotoOnYourDeviceCamera) {
            this._11TakeAPhotoOnYourDeviceCamera = _11TakeAPhotoOnYourDeviceCamera;
        }

        public String get11SendYourPhoto() {
            return _11SendYourPhoto;
        }

        public void set11SendYourPhoto(String _11SendYourPhoto) {
            this._11SendYourPhoto = _11SendYourPhoto;
        }

        public String get11Feed() {
            return _11Feed;
        }

        public void set11Feed(String _11Feed) {
            this._11Feed = _11Feed;
        }

        public String get45All() {
            return _45All;
        }

        public void set45All(String _45All) {
            this._45All = _45All;
        }

        public String get45Internal() {
            return _45Internal;
        }

        public void set45Internal(String _45Internal) {
            this._45Internal = _45Internal;
        }

        public String get45Social() {
            return _45Social;
        }

        public void set45Social(String _45Social) {
            this._45Social = _45Social;
        }

        public String get45Alert() {
            return _45Alert;
        }

        public void set45Alert(String _45Alert) {
            this._45Alert = _45Alert;
        }

        public String get45TypeYourPostHere() {
            return _45TypeYourPostHere;
        }

        public void set45TypeYourPostHere(String _45TypeYourPostHere) {
            this._45TypeYourPostHere = _45TypeYourPostHere;
        }

        public String get45PublishPost() {
            return _45PublishPost;
        }

        public void set45PublishPost(String _45PublishPost) {
            this._45PublishPost = _45PublishPost;
        }

        public String get45Like() {
            return _45Like;
        }

        public void set45Like(String _45Like) {
            this._45Like = _45Like;
        }

        public String get45Comment() {
            return _45Comment;
        }

        public void set45Comment(String _45Comment) {
            this._45Comment = _45Comment;
        }

        public String get45Comments() {
            return _45Comments;
        }

        public void set45Comments(String _45Comments) {
            this._45Comments = _45Comments;
        }

        public String get45NoCommentsHaveBeenMadeYet() {
            return _45NoCommentsHaveBeenMadeYet;
        }

        public void set45NoCommentsHaveBeenMadeYet(String _45NoCommentsHaveBeenMadeYet) {
            this._45NoCommentsHaveBeenMadeYet = _45NoCommentsHaveBeenMadeYet;
        }

        public String get45ShareThisPost() {
            return _45ShareThisPost;
        }

        public void set45ShareThisPost(String _45ShareThisPost) {
            this._45ShareThisPost = _45ShareThisPost;
        }

        public String get45OK() {
            return _45OK;
        }

        public void set45OK(String _45OK) {
            this._45OK = _45OK;
        }

        public String get45UpdatedProfilePicture() {
            return _45UpdatedProfilePicture;
        }

        public void set45UpdatedProfilePicture(String _45UpdatedProfilePicture) {
            this._45UpdatedProfilePicture = _45UpdatedProfilePicture;
        }

        public String get50SearchQA() {
            return _50SearchQA;
        }

        public void set50SearchQA(String _50SearchQA) {
            this._50SearchQA = _50SearchQA;
        }

        public String get50TapASessionToAskAQuestionAndInteract() {
            return _50TapASessionToAskAQuestionAndInteract;
        }

        public void set50TapASessionToAskAQuestionAndInteract(String _50TapASessionToAskAQuestionAndInteract) {
            this._50TapASessionToAskAQuestionAndInteract = _50TapASessionToAskAQuestionAndInteract;
        }

        public String get50Questions() {
            return _50Questions;
        }

        public void set50Questions(String _50Questions) {
            this._50Questions = _50Questions;
        }

        public String get50Start() {
            return _50Start;
        }

        public void set50Start(String _50Start) {
            this._50Start = _50Start;
        }

        public String get50End() {
            return _50End;
        }

        public void set50End(String _50End) {
            this._50End = _50End;
        }

        public String get50Refresh() {
            return _50Refresh;
        }

        public void set50Refresh(String _50Refresh) {
            this._50Refresh = _50Refresh;
        }

        public String get50AskYourOwnQuestionOrVoteOtherToTheTop() {
            return _50AskYourOwnQuestionOrVoteOtherToTheTop;
        }

        public void set50AskYourOwnQuestionOrVoteOtherToTheTop(String _50AskYourOwnQuestionOrVoteOtherToTheTop) {
            this._50AskYourOwnQuestionOrVoteOtherToTheTop = _50AskYourOwnQuestionOrVoteOtherToTheTop;
        }

        public String get50AskAQuestion() {
            return _50AskAQuestion;
        }

        public void set50AskAQuestion(String _50AskAQuestion) {
            this._50AskAQuestion = _50AskAQuestion;
        }

        public String get50Anonymous() {
            return _50Anonymous;
        }

        public void set50Anonymous(String _50Anonymous) {
            this._50Anonymous = _50Anonymous;
        }

        public String get52EarnPointsNow() {
            return _52EarnPointsNow;
        }

        public void set52EarnPointsNow(String _52EarnPointsNow) {
            this._52EarnPointsNow = _52EarnPointsNow;
        }

        public String get52Points() {
            return _52Points;
        }

        public void set52Points(String _52Points) {
            this._52Points = _52Points;
        }

        public String get52SendAMessage() {
            return _52SendAMessage;
        }

        public void set52SendAMessage(String _52SendAMessage) {
            this._52SendAMessage = _52SendAMessage;
        }

        public String get52RequestAMeeting() {
            return _52RequestAMeeting;
        }

        public void set52RequestAMeeting(String _52RequestAMeeting) {
            this._52RequestAMeeting = _52RequestAMeeting;
        }

        public String get52AcceptAMeeting() {
            return _52AcceptAMeeting;
        }

        public void set52AcceptAMeeting(String _52AcceptAMeeting) {
            this._52AcceptAMeeting = _52AcceptAMeeting;
        }

        public String get52ShareContactInformation() {
            return _52ShareContactInformation;
        }

        public void set52ShareContactInformation(String _52ShareContactInformation) {
            this._52ShareContactInformation = _52ShareContactInformation;
        }

        public String get52WalkPastABeacon() {
            return _52WalkPastABeacon;
        }

        public void set52WalkPastABeacon(String _52WalkPastABeacon) {
            this._52WalkPastABeacon = _52WalkPastABeacon;
        }

        public String get52AnswerASurvey() {
            return _52AnswerASurvey;
        }

        public void set52AnswerASurvey(String _52AnswerASurvey) {
            this._52AnswerASurvey = _52AnswerASurvey;
        }

        public String getSignUpProcessCreateYourAccountToGainAccessTo() {
            return signUpProcessCreateYourAccountToGainAccessTo;
        }

        public void setSignUpProcessCreateYourAccountToGainAccessTo(String signUpProcessCreateYourAccountToGainAccessTo) {
            this.signUpProcessCreateYourAccountToGainAccessTo = signUpProcessCreateYourAccountToGainAccessTo;
        }

        public String getSignUpProcessEmail() {
            return signUpProcessEmail;
        }

        public void setSignUpProcessEmail(String signUpProcessEmail) {
            this.signUpProcessEmail = signUpProcessEmail;
        }

        public String getSignUpProcessPassword() {
            return signUpProcessPassword;
        }

        public void setSignUpProcessPassword(String signUpProcessPassword) {
            this.signUpProcessPassword = signUpProcessPassword;
        }

        public String getSignUpProcessLogIn() {
            return signUpProcessLogIn;
        }

        public void setSignUpProcessLogIn(String signUpProcessLogIn) {
            this.signUpProcessLogIn = signUpProcessLogIn;
        }

        public String getSignUpProcessForgotPassword() {
            return signUpProcessForgotPassword;
        }

        public void setSignUpProcessForgotPassword(String signUpProcessForgotPassword) {
            this.signUpProcessForgotPassword = signUpProcessForgotPassword;
        }

        public String getSignUpProcessOr() {
            return signUpProcessOr;
        }

        public void setSignUpProcessOr(String signUpProcessOr) {
            this.signUpProcessOr = signUpProcessOr;
        }

        public String getSignUpProcessRegisterForTheApp() {
            return signUpProcessRegisterForTheApp;
        }

        public void setSignUpProcessRegisterForTheApp(String signUpProcessRegisterForTheApp) {
            this.signUpProcessRegisterForTheApp = signUpProcessRegisterForTheApp;
        }

        public String getSignUpProcessFirstName() {
            return signUpProcessFirstName;
        }

        public void setSignUpProcessFirstName(String signUpProcessFirstName) {
            this.signUpProcessFirstName = signUpProcessFirstName;
        }

        public String getSignUpProcessLastName() {
            return signUpProcessLastName;
        }

        public void setSignUpProcessLastName(String signUpProcessLastName) {
            this.signUpProcessLastName = signUpProcessLastName;
        }

        public String getSignUpProcessPasswordAgain() {
            return signUpProcessPasswordAgain;
        }

        public void setSignUpProcessPasswordAgain(String signUpProcessPasswordAgain) {
            this.signUpProcessPasswordAgain = signUpProcessPasswordAgain;
        }

        public String getSignUpProcessCompanyName() {
            return signUpProcessCompanyName;
        }

        public void setSignUpProcessCompanyName(String signUpProcessCompanyName) {
            this.signUpProcessCompanyName = signUpProcessCompanyName;
        }

        public String getSignUpProcessTitle() {
            return signUpProcessTitle;
        }

        public void setSignUpProcessTitle(String signUpProcessTitle) {
            this.signUpProcessTitle = signUpProcessTitle;
        }

        public String getSignUpProcessSelectCountry() {
            return signUpProcessSelectCountry;
        }

        public void setSignUpProcessSelectCountry(String signUpProcessSelectCountry) {
            this.signUpProcessSelectCountry = signUpProcessSelectCountry;
        }

        public String getSignUpProcessIAgreeToTheTermsConditions() {
            return signUpProcessIAgreeToTheTermsConditions;
        }

        public void setSignUpProcessIAgreeToTheTermsConditions(String signUpProcessIAgreeToTheTermsConditions) {
            this.signUpProcessIAgreeToTheTermsConditions = signUpProcessIAgreeToTheTermsConditions;
        }

        public String getSignUpProcessSubmit() {
            return signUpProcessSubmit;
        }

        public void setSignUpProcessSubmit(String signUpProcessSubmit) {
            this.signUpProcessSubmit = signUpProcessSubmit;
        }

        public String getSignUpProcessGoBackToLoginScreen() {
            return signUpProcessGoBackToLoginScreen;
        }

        public void setSignUpProcessGoBackToLoginScreen(String signUpProcessGoBackToLoginScreen) {
            this.signUpProcessGoBackToLoginScreen = signUpProcessGoBackToLoginScreen;
        }

        public String getTimeAndDateMonday() {
            return timeAndDateMonday;
        }

        public void setTimeAndDateMonday(String timeAndDateMonday) {
            this.timeAndDateMonday = timeAndDateMonday;
        }

        public String getTimeAndDateTuesday() {
            return timeAndDateTuesday;
        }

        public void setTimeAndDateTuesday(String timeAndDateTuesday) {
            this.timeAndDateTuesday = timeAndDateTuesday;
        }

        public String getTimeAndDateWednesday() {
            return timeAndDateWednesday;
        }

        public void setTimeAndDateWednesday(String timeAndDateWednesday) {
            this.timeAndDateWednesday = timeAndDateWednesday;
        }

        public String getTimeAndDateThursday() {
            return timeAndDateThursday;
        }

        public void setTimeAndDateThursday(String timeAndDateThursday) {
            this.timeAndDateThursday = timeAndDateThursday;
        }

        public String getTimeAndDateFriday() {
            return timeAndDateFriday;
        }

        public void setTimeAndDateFriday(String timeAndDateFriday) {
            this.timeAndDateFriday = timeAndDateFriday;
        }

        public String getTimeAndDateSaturday() {
            return timeAndDateSaturday;
        }

        public void setTimeAndDateSaturday(String timeAndDateSaturday) {
            this.timeAndDateSaturday = timeAndDateSaturday;
        }

        public String getTimeAndDateSunday() {
            return timeAndDateSunday;
        }

        public void setTimeAndDateSunday(String timeAndDateSunday) {
            this.timeAndDateSunday = timeAndDateSunday;
        }

        public String getTimeAndDateSeconds() {
            return timeAndDateSeconds;
        }

        public void setTimeAndDateSeconds(String timeAndDateSeconds) {
            this.timeAndDateSeconds = timeAndDateSeconds;
        }

        public String getTimeAndDateMinutes() {
            return timeAndDateMinutes;
        }

        public void setTimeAndDateMinutes(String timeAndDateMinutes) {
            this.timeAndDateMinutes = timeAndDateMinutes;
        }

        public String getTimeAndDateHours() {
            return timeAndDateHours;
        }

        public void setTimeAndDateHours(String timeAndDateHours) {
            this.timeAndDateHours = timeAndDateHours;
        }

        public String getTimeAndDateDays() {
            return timeAndDateDays;
        }

        public void setTimeAndDateDays(String timeAndDateDays) {
            this.timeAndDateDays = timeAndDateDays;
        }

        public String getTimeAndDateWeeks() {
            return timeAndDateWeeks;
        }

        public void setTimeAndDateWeeks(String timeAndDateWeeks) {
            this.timeAndDateWeeks = timeAndDateWeeks;
        }

        public String getTimeAndDateMonths() {
            return timeAndDateMonths;
        }

        public void setTimeAndDateMonths(String timeAndDateMonths) {
            this.timeAndDateMonths = timeAndDateMonths;
        }

        public String getTimeAndDateYears() {
            return timeAndDateYears;
        }

        public void setTimeAndDateYears(String timeAndDateYears) {
            this.timeAndDateYears = timeAndDateYears;
        }

        public String getMyProfileLogin() {
            return myProfileLogin;
        }

        public void setMyProfileLogin(String myProfileLogin) {
            this.myProfileLogin = myProfileLogin;
        }

        public String getLeftHandMenuExit() {
            return leftHandMenuExit;
        }

        public void setLeftHandMenuExit(String leftHandMenuExit) {
            this.leftHandMenuExit = leftHandMenuExit;
        }

        public String getSignUpProcessNext() {
            return signUpProcessNext;
        }

        public void setSignUpProcessNext(String signUpProcessNext) {
            this.signUpProcessNext = signUpProcessNext;
        }

        public String getNotificationsModeratorRequestMeetingAccept() {
            return notificationsModeratorRequestMeetingAccept;
        }

        public void setNotificationsModeratorRequestMeetingAccept(String notificationsModeratorRequestMeetingAccept) {
            this.notificationsModeratorRequestMeetingAccept = notificationsModeratorRequestMeetingAccept;
        }

        public String getNotificationsRequestMeetingAcceptSuggestedDatetime() {
            return notificationsRequestMeetingAcceptSuggestedDatetime;
        }

        public void setNotificationsRequestMeetingAcceptSuggestedDatetime(String notificationsRequestMeetingAcceptSuggestedDatetime) {
            this.notificationsRequestMeetingAcceptSuggestedDatetime = notificationsRequestMeetingAcceptSuggestedDatetime;
        }

        public String getNotificationsRequestMeetingSuggestedDatetime() {
            return notificationsRequestMeetingSuggestedDatetime;
        }

        public void setNotificationsRequestMeetingSuggestedDatetime(String notificationsRequestMeetingSuggestedDatetime) {
            this.notificationsRequestMeetingSuggestedDatetime = notificationsRequestMeetingSuggestedDatetime;
        }

        public String getNotificationsRequestMeetingAccept() {
            return notificationsRequestMeetingAccept;
        }

        public void setNotificationsRequestMeetingAccept(String notificationsRequestMeetingAccept) {
            this.notificationsRequestMeetingAccept = notificationsRequestMeetingAccept;
        }

        public String getNotificationsRequestMeetingReject() {
            return notificationsRequestMeetingReject;
        }

        public void setNotificationsRequestMeetingReject(String notificationsRequestMeetingReject) {
            this.notificationsRequestMeetingReject = notificationsRequestMeetingReject;
        }

        public String getNotificationsRequestMeetingWithoutModaretor() {
            return notificationsRequestMeetingWithoutModaretor;
        }

        public void setNotificationsRequestMeetingWithoutModaretor(String notificationsRequestMeetingWithoutModaretor) {
            this.notificationsRequestMeetingWithoutModaretor = notificationsRequestMeetingWithoutModaretor;
        }

        public String getNotificationsRequestMeetingWithModaretor() {
            return notificationsRequestMeetingWithModaretor;
        }

        public void setNotificationsRequestMeetingWithModaretor(String notificationsRequestMeetingWithModaretor) {
            this.notificationsRequestMeetingWithModaretor = notificationsRequestMeetingWithModaretor;
        }

        public String getNotificationsShareContact() {
            return notificationsShareContact;
        }

        public void setNotificationsShareContact(String notificationsShareContact) {
            this.notificationsShareContact = notificationsShareContact;
        }

        public String getNotificationsMessageReceived() {
            return notificationsMessageReceived;
        }

        public void setNotificationsMessageReceived(String notificationsMessageReceived) {
            this.notificationsMessageReceived = notificationsMessageReceived;
        }

        public String getNotificationsNoNotificationsFound() {
            return notificationsNoNotificationsFound;
        }

        public void setNotificationsNoNotificationsFound(String notificationsNoNotificationsFound) {
            this.notificationsNoNotificationsFound = notificationsNoNotificationsFound;
        }

        public String getLangId() {
            return langId;
        }

        public void setLangId(String langId) {
            this.langId = langId;
        }

        public String getLangName() {
            return langName;
        }

        public void setLangName(String langName) {
            this.langName = langName;
        }

        public String getLangIcon() {
            return langIcon;
        }

        public void setLangIcon(String langIcon) {
            this.langIcon = langIcon;
        }
    }

}

