package com.allintheloop.Bean.AgendaData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nteam on 4/12/17.
 */

public class AgendaType {
    @SerializedName("type_id")
    @Expose
    private String typeId;
    @SerializedName("type_name")
    @Expose
    private String typeName;
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("order_no")
    @Expose
    private String orderNo;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}