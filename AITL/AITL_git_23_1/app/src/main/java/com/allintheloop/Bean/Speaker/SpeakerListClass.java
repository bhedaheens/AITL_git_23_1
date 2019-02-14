package com.allintheloop.Bean.Speaker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpeakerListClass {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("bg_color")
    @Expose
    private String bgColor;
    @SerializedName("type_id")
    @Expose
    private String typeId;
    @SerializedName("type_position")
    @Expose
    private String typePosition;
    @SerializedName("data")
    @Expose
    private List<SpeakerListNew> data = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypePosition() {
        return typePosition;
    }

    public void setTypePosition(String typePosition) {
        this.typePosition = typePosition;
    }

    public List<SpeakerListNew> getData() {
        return data;
    }

    public void setData(List<SpeakerListNew> data) {
        this.data = data;
    }


    public class SpeakerListNew {
        @SerializedName("Id")
        @Expose
        private String id;
        @SerializedName("Firstname")
        @Expose
        private String firstname;
        @SerializedName("Lastname")
        @Expose
        private String lastname;
        @SerializedName("Company_name")
        @Expose
        private String companyName;
        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("Speaker_desc")
        @Expose
        private String speakerDesc;
        @SerializedName("Email")
        @Expose
        private String email;
        @SerializedName("Logo")
        @Expose
        private String logo;
        @SerializedName("Heading")
        @Expose
        private String heading;
        @SerializedName("is_favorites")
        @Expose
        private String isFavorites;

        private String event_id;
        private String user_id;

        public String getEvent_id() {
            return event_id;
        }

        public void setEvent_id(String event_id) {
            this.event_id = event_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getSpeakerDesc() {
            return speakerDesc;
        }

        public void setSpeakerDesc(String speakerDesc) {
            this.speakerDesc = speakerDesc;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getHeading() {
            return heading;
        }

        public void setHeading(String heading) {
            this.heading = heading;
        }

        public String getIsFavorites() {
            return isFavorites;
        }

        public void setIsFavorites(String isFavorites) {
            this.isFavorites = isFavorites;
        }
    }


}
