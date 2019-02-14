package com.allintheloop.Bean.ActivityModule;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ActivityCommonClass implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("activity_no")
    @Expose
    private String activityNo;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("sub_title")
    @Expose
    private String subTitle;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("image")
    @Expose
    private List<String> image = null;
    @SerializedName("is_like")
    @Expose
    private String isLike;
    @SerializedName("likes")
    @Expose
    private List<InternalLike> likes = null;
    @SerializedName("like_count")
    @Expose
    private String likeCount;
    @SerializedName("comments")
    @Expose
    private List<ActivityCommentClass> comments = null;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("role_id")
    @Expose
    private String roleId;
    @SerializedName("advert_image")
    @Expose
    private String advertImage;
    @SerializedName("advert_url")
    @Expose
    private String advertUrl;
    @SerializedName("survey_question")
    @Expose
    private String surveyQuestion;
    @SerializedName("survey_options")
    @Expose
    private List<String> surveyOptions = null;
    @SerializedName("survey_result")
    @Expose
    private List<Activity_SurveyResult> surveyResult = null;

    @SerializedName("ans_submitted")
    @Expose
    private String ans_submitted;


    @SerializedName("result_message")
    @Expose
    private String result_message;

    public String getResult_message() {
        return result_message;
    }

    public void setResult_message(String result_message) {
        this.result_message = result_message;
    }

    public String getAns_submitted() {
        return ans_submitted;
    }

    public void setAns_submitted(String ans_submitted) {
        this.ans_submitted = ans_submitted;
    }


    protected ActivityCommonClass(Parcel in) {
        id = in.readString();
        type = in.readString();
        postType = in.readString();
        activityNo = in.readString();
        logo = in.readString();
        name = in.readString();
        subTitle = in.readString();
        time = in.readString();
        message = in.readString();
        image = in.createStringArrayList();
        isLike = in.readString();
        likes = in.createTypedArrayList(InternalLike.CREATOR);
        likeCount = in.readString();
        comments = in.createTypedArrayList(ActivityCommentClass.CREATOR);
        userId = in.readString();
        roleId = in.readString();
        advertImage = in.readString();
        advertUrl = in.readString();
        ans_submitted = in.readString();
        result_message = in.readString();
        surveyQuestion = in.readString();
        surveyOptions = in.createStringArrayList();
    }

    public static final Creator<ActivityCommonClass> CREATOR = new Creator<ActivityCommonClass>() {
        @Override
        public ActivityCommonClass createFromParcel(Parcel in) {
            return new ActivityCommonClass(in);
        }

        @Override
        public ActivityCommonClass[] newArray(int size) {
            return new ActivityCommonClass[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(String activityNo) {
        this.activityNo = activityNo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public List<InternalLike> getLikes() {
        return likes;
    }

    public void setLikes(List<InternalLike> likes) {
        this.likes = likes;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public List<ActivityCommentClass> getComments() {
        return comments;
    }

    public void setComments(List<ActivityCommentClass> comments) {
        this.comments = comments;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getAdvertImage() {
        return advertImage;
    }

    public void setAdvertImage(String advertImage) {
        this.advertImage = advertImage;
    }

    public String getAdvertUrl() {
        return advertUrl;
    }

    public void setAdvertUrl(String advertUrl) {
        this.advertUrl = advertUrl;
    }

    public String getSurveyQuestion() {
        return surveyQuestion;
    }

    public void setSurveyQuestion(String surveyQuestion) {
        this.surveyQuestion = surveyQuestion;
    }

    public List<String> getSurveyOptions() {
        return surveyOptions;
    }

    public void setSurveyOptions(List<String> surveyOptions) {
        this.surveyOptions = surveyOptions;
    }

    public List<Activity_SurveyResult> getSurveyResult() {
        return surveyResult;
    }

    public void setSurveyResult(List<Activity_SurveyResult> surveyResult) {
        this.surveyResult = surveyResult;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
        dest.writeString(postType);
        dest.writeString(activityNo);
        dest.writeString(logo);
        dest.writeString(name);
        dest.writeString(subTitle);
        dest.writeString(time);
        dest.writeString(message);
        dest.writeStringList(image);
        dest.writeString(isLike);
        dest.writeTypedList(likes);
        dest.writeString(likeCount);
        dest.writeTypedList(comments);
        dest.writeString(userId);
        dest.writeString(roleId);
        dest.writeString(advertImage);
        dest.writeString(advertUrl);
        dest.writeString(ans_submitted);
        dest.writeString(result_message);
        dest.writeString(surveyQuestion);
        dest.writeStringList(surveyOptions);
    }
}
