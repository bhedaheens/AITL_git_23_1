package com.allintheloop.Bean.ActivityModule;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InternalLike implements Parcelable {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Company_name")
    @Expose
    private String companyName;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("datetime")
    @Expose
    private String datetime;


    public InternalLike() {
    }

    protected InternalLike(Parcel in) {
        name = in.readString();
        companyName = in.readString();
        title = in.readString();
        logo = in.readString();
        userId = in.readString();
        datetime = in.readString();
    }

    public static final Creator<InternalLike> CREATOR = new Creator<InternalLike>() {
        @Override
        public InternalLike createFromParcel(Parcel in) {
            return new InternalLike(in);
        }

        @Override
        public InternalLike[] newArray(int size) {
            return new InternalLike[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(companyName);
        dest.writeString(title);
        dest.writeString(logo);
        dest.writeString(userId);
        dest.writeString(datetime);
    }
}
