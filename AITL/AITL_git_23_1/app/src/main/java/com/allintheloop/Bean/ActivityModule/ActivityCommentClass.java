package com.allintheloop.Bean.ActivityModule;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aiyaz on 1/8/17.
 */

public class ActivityCommentClass implements Parcelable {
    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("comment_image")
    @Expose
    private List<Object> commentImage = new ArrayList<>();
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("show_delete")
    @Expose
    private String showDelete;

    protected ActivityCommentClass(Parcel in) {
        commentId = in.readString();
        comment = in.readString();
        name = in.readString();
        logo = in.readString();
        datetime = in.readString();
        userId = in.readString();
        showDelete = in.readString();
    }

    public static final Creator<ActivityCommentClass> CREATOR = new Creator<ActivityCommentClass>() {
        @Override
        public ActivityCommentClass createFromParcel(Parcel in) {
            return new ActivityCommentClass(in);
        }

        @Override
        public ActivityCommentClass[] newArray(int size) {
            return new ActivityCommentClass[size];
        }
    };

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public List<Object> getCommentImage() {
        return commentImage;
    }

    public void setCommentImage(List<Object> commentImage) {
        this.commentImage = commentImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShowDelete() {
        return showDelete;
    }

    public void setShowDelete(String showDelete) {
        this.showDelete = showDelete;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commentId);
        dest.writeString(comment);
        dest.writeString(name);
        dest.writeString(logo);
        dest.writeString(datetime);
        dest.writeString(userId);
        dest.writeString(showDelete);
    }
}
