package com.allintheloop.Bean.RequestMeeting;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 9/3/18.
 */

public class RequestMeetingListNewDataList implements Parcelable {
    @SerializedName("data")
    @Expose
    public ArrayList<RequestMeetingNewList> requestMeetingNewLists;

    public ArrayList<RequestMeetingNewList> getRequestMeetingNewLists() {
        return requestMeetingNewLists;
    }

    public void setRequestMeetingNewLists(ArrayList<RequestMeetingNewList> requestMeetingNewLists) {
        this.requestMeetingNewLists = requestMeetingNewLists;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.requestMeetingNewLists);
    }

    public RequestMeetingListNewDataList() {
    }

    protected RequestMeetingListNewDataList(Parcel in) {
        this.requestMeetingNewLists = new ArrayList<RequestMeetingNewList>();
        in.readList(this.requestMeetingNewLists, RequestMeetingNewList.class.getClassLoader());
    }

    public static final Parcelable.Creator<RequestMeetingListNewDataList> CREATOR = new Parcelable.Creator<RequestMeetingListNewDataList>() {
        @Override
        public RequestMeetingListNewDataList createFromParcel(Parcel source) {
            return new RequestMeetingListNewDataList(source);
        }

        @Override
        public RequestMeetingListNewDataList[] newArray(int size) {
            return new RequestMeetingListNewDataList[size];
        }
    };
}
