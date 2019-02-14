package com.allintheloop.Bean.Fundraising;

/**
 * Created by nteam on 16/8/16.
 */
public class AuctionItemList {
    String name, bid_amt, date, win_status, payment_status;

    public AuctionItemList(String name, String bid_amt, String date, String win_status, String payment_status) {
        this.name = name;
        this.bid_amt = bid_amt;
        this.date = date;
        this.win_status = win_status;
        this.payment_status = payment_status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBid_amt() {
        return bid_amt;
    }

    public void setBid_amt(String bid_amt) {
        this.bid_amt = bid_amt;
    }

    public String getWin_status() {
        return win_status;
    }

    public void setWin_status(String win_status) {
        this.win_status = win_status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }
}
