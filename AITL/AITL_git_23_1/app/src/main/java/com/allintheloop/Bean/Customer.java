package com.allintheloop.Bean;

/**
 * Created by Aiyaz on 25/3/17.
 */

public class Customer {

    private String customerId;
    private String customerMapId;
    private long customerPhoneNo;
    private String customerName;
    private String customerImage;
    private String customerType;
    private boolean checked = false;

    public Customer(String customerId, String customerName, String customerImage) {

        this.customerId = customerId;
        this.customerName = customerName;
        this.customerImage = customerImage;
    }

    public Customer(String customerId, String customerMapId, String customerName, String customerImage) {

        this.customerId = customerId;
        this.customerMapId = customerMapId;
        this.customerName = customerName;
        this.customerImage = customerImage;
    }

    public Customer(String customerId, String customerName, String customerImage, long customerPhoneNo) {

        this.customerId = customerId;
        this.customerImage = customerImage;
        this.customerName = customerName;
        this.customerPhoneNo = customerPhoneNo;
    }

    public String getCustomerMapId() {
        return customerMapId;
    }

    public void setCustomerMapId(String customerMapId) {
        this.customerMapId = customerMapId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public long getCustomerPhoneNo() {
        return customerPhoneNo;
    }

    public void setCustomerPhoneNo(long customerPhoneNo) {
        this.customerPhoneNo = customerPhoneNo;
    }

    public void toggleChecked() {
        checked = !checked;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}