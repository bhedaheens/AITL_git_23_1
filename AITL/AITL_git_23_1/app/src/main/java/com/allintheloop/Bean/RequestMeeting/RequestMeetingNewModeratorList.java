package com.allintheloop.Bean.RequestMeeting;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 15/3/18.
 */

public class RequestMeetingNewModeratorList implements Parcelable {

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


    public class Datum implements Parcelable {

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
        @SerializedName("sender_id")
        @Expose
        private String senderId;
        @SerializedName("receiver_id")
        @Expose
        private String receiverId;

        @SerializedName("show_invite_more")
        @Expose
        private String show_invite_more;

        @SerializedName("sender_name")
        @Expose
        private String senderName;

        @SerializedName("reciver_name")
        @Expose
        private String reciverName;
        @SerializedName("map_location")
        @Expose
        private MapLocation mapLocation;

        @SerializedName("Logo")
        @Expose
        private String logo;

        @SerializedName("time_new")
        @Expose
        private String time_new;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
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

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public String getSenderName() {
            return senderName;
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

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getReciverName() {
            return reciverName;
        }


        public void setReciverName(String reciverName) {
            this.reciverName = reciverName;
        }

        public MapLocation getMapLocation() {
            return mapLocation;
        }

        public void setMapLocation(MapLocation mapLocation) {
            this.mapLocation = mapLocation;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.date);
            dest.writeString(this.time);
            dest.writeString(this.status);
            dest.writeString(this.requestId);
            dest.writeString(this.location);
            dest.writeString(this.senderId);
            dest.writeString(this.receiverId);
            dest.writeString(this.senderName);
            dest.writeString(this.reciverName);
            dest.writeString(this.show_invite_more);
            dest.writeParcelable(this.mapLocation, flags);
            dest.writeString(this.logo);
        }

        public Datum() {
        }

        protected Datum(Parcel in) {
            this.date = in.readString();
            this.time = in.readString();
            this.status = in.readString();
            this.requestId = in.readString();
            this.location = in.readString();
            this.senderId = in.readString();
            this.receiverId = in.readString();
            this.senderName = in.readString();
            this.show_invite_more = in.readString();
            this.reciverName = in.readString();
            this.mapLocation = in.readParcelable(MapLocation.class.getClassLoader());
            this.logo = in.readString();
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

        public MapLocation() {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeList(this.data);
    }

    public RequestMeetingNewModeratorList() {
    }

    protected RequestMeetingNewModeratorList(Parcel in) {
        this.date = in.readString();
        this.data = new ArrayList<Datum>();
        in.readList(this.data, Datum.class.getClassLoader());
    }

    public static final Parcelable.Creator<RequestMeetingNewModeratorList> CREATOR = new Parcelable.Creator<RequestMeetingNewModeratorList>() {
        @Override
        public RequestMeetingNewModeratorList createFromParcel(Parcel source) {
            return new RequestMeetingNewModeratorList(source);
        }

        @Override
        public RequestMeetingNewModeratorList[] newArray(int size) {
            return new RequestMeetingNewModeratorList[size];
        }
    };
}



