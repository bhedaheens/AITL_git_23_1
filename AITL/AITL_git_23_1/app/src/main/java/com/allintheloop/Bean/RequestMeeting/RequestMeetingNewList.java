package com.allintheloop.Bean.RequestMeeting;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 9/3/18.
 */

public class RequestMeetingNewList implements Parcelable {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("data")
    @Expose
    private ArrayList<Datum> data = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }


    public class MapLocation implements Parcelable {

        @SerializedName("map_id")
        @Expose
        private String mapId;
        @SerializedName("coords")
        @Expose
        private String coords;
        @SerializedName("location")
        @Expose
        private String location;

        public String getMapId() {
            return mapId;
        }

        public void setMapId(String mapId) {
            this.mapId = mapId;
        }

        public String getCoords() {
            return coords;
        }

        public void setCoords(String coords) {
            this.coords = coords;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }


        public MapLocation() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mapId);
            dest.writeString(this.coords);
            dest.writeString(this.location);
        }

        protected MapLocation(Parcel in) {
            this.mapId = in.readString();
            this.coords = in.readString();
            this.location = in.readString();
        }

        public final Creator<MapLocation> CREATOR = new Creator<MapLocation>() {
            @Override
            public MapLocation createFromParcel(Parcel source) {
                return new MapLocation(source);
            }

            @Override
            public MapLocation[] newArray(int size) {
                return new MapLocation[size];
            }
        };
    }

    public class Datum implements Parcelable {

        @SerializedName("Firstname")
        @Expose
        private String firstname;
        @SerializedName("Lastname")
        @Expose
        private String lastname;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("time")
        @Expose
        private String time;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("request_id")
        @Expose
        private String requestId;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("exhibiotor_id")
        @Expose
        private String exhibiotorId;
        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("Company_name")
        @Expose
        private String companyName;
        @SerializedName("Logo")
        @Expose
        private String logo;

        @SerializedName("sender_id")
        @Expose
        private String sender_id;

        @SerializedName("receiver_id")
        @Expose
        private String receiver_id;

        @SerializedName("show_invite_more")
        @Expose
        private String show_invite_more;

        @SerializedName("time_new")
        @Expose
        private String time_new;

        @SerializedName("map_location")
        @Expose
        private MapLocation mapLocation;

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getSender_id() {
            return sender_id;
        }

        public void setSender_id(String sender_id) {
            this.sender_id = sender_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReceiver_id() {
            return receiver_id;
        }

        public void setReceiver_id(String receiver_id) {
            this.receiver_id = receiver_id;
        }


        public String getShow_invite_more() {
            return show_invite_more;
        }

        public void setShow_invite_more(String show_invite_more) {
            this.show_invite_more = show_invite_more;
        }

        public String getTime_new() {
            return time_new;
        }

        public void setTime_new(String time_new) {
            this.time_new = time_new;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getExhibiotorId() {
            return exhibiotorId;
        }

        public void setExhibiotorId(String exhibiotorId) {
            this.exhibiotorId = exhibiotorId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public MapLocation getMapLocation() {
            return mapLocation;
        }

        public void setMapLocation(MapLocation mapLocation) {
            this.mapLocation = mapLocation;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.firstname);
            dest.writeString(this.lastname);
            dest.writeString(this.date);
            dest.writeString(this.time);
            dest.writeString(this.status);
            dest.writeString(this.requestId);
            dest.writeString(this.location);
            dest.writeString(this.exhibiotorId);
            dest.writeString(this.title);
            dest.writeString(this.companyName);
            dest.writeString(this.logo);
            dest.writeString(this.sender_id);
            dest.writeString(this.receiver_id);
            dest.writeString(this.show_invite_more);
            dest.writeParcelable(this.mapLocation, flags);
        }

        public Datum() {
        }

        protected Datum(Parcel in) {
            this.firstname = in.readString();
            this.lastname = in.readString();
            this.date = in.readString();
            this.time = in.readString();
            this.status = in.readString();
            this.requestId = in.readString();
            this.location = in.readString();
            this.exhibiotorId = in.readString();
            this.title = in.readString();
            this.companyName = in.readString();
            this.logo = in.readString();
            this.sender_id = in.readString();
            this.receiver_id = in.readString();
            this.show_invite_more = in.readString();
            this.mapLocation = in.readParcelable(MapLocation.class.getClassLoader());
        }

        public final Creator<Datum> CREATOR = new Creator<Datum>() {
            @Override
            public Datum createFromParcel(Parcel source) {
                return new Datum(source);
            }

            @Override
            public Datum[] newArray(int size) {
                return new Datum[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeList(this.data);
    }

    public RequestMeetingNewList() {
    }

    protected RequestMeetingNewList(Parcel in) {
        this.date = in.readString();
        this.data = new ArrayList<Datum>();
        in.readList(this.data, Datum.class.getClassLoader());
    }

    public static final Parcelable.Creator<RequestMeetingNewList> CREATOR = new Parcelable.Creator<RequestMeetingNewList>() {
        @Override
        public RequestMeetingNewList createFromParcel(Parcel source) {
            return new RequestMeetingNewList(source);
        }

        @Override
        public RequestMeetingNewList[] newArray(int size) {
            return new RequestMeetingNewList[size];
        }
    };
}

