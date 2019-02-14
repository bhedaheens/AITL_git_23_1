package com.allintheloop.Bean.RequestMeeting;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 15/3/18.
 */

public class RequestMeetingListNewModeratorDataList implements Parcelable {
    @SerializedName("data")
    @Expose
    public ArrayList<RequestMeetingNewModeratorList> requestMeetingNewLists;

    public ArrayList<RequestMeetingNewModeratorList> getRequestMeetingNewLists() {
        return requestMeetingNewLists;
    }

    public void setRequestMeetingNewLists(ArrayList<RequestMeetingNewModeratorList> requestMeetingNewLists) {
        this.requestMeetingNewLists = requestMeetingNewLists;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.requestMeetingNewLists);
    }

    public RequestMeetingListNewModeratorDataList() {
    }

    protected RequestMeetingListNewModeratorDataList(Parcel in) {
        this.requestMeetingNewLists = in.createTypedArrayList(RequestMeetingNewModeratorList.CREATOR);
    }

    public static final Parcelable.Creator<RequestMeetingListNewModeratorDataList> CREATOR = new Parcelable.Creator<RequestMeetingListNewModeratorDataList>() {
        @Override
        public RequestMeetingListNewModeratorDataList createFromParcel(Parcel source) {
            return new RequestMeetingListNewModeratorDataList(source);
        }

        @Override
        public RequestMeetingListNewModeratorDataList[] newArray(int size) {
            return new RequestMeetingListNewModeratorDataList[size];
        }
    };
}
