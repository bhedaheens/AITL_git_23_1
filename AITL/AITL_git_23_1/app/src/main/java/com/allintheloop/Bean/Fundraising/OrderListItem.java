package com.allintheloop.Bean.Fundraising;

/**
 * Created by nteam on 16/8/16.
 */
public class OrderListItem {
    String srNo, Mode, OrderStatus, created_date;

    public OrderListItem(String mode, String orderStatus, String created_date) {
        this.srNo = srNo;
        Mode = mode;
        OrderStatus = orderStatus;
        this.created_date = created_date;
    }


    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
