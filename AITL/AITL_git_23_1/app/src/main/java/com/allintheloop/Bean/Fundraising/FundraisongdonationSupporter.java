package com.allintheloop.Bean.Fundraising;

/**
 * Created by nteam on 15/9/16.
 */
public class FundraisongdonationSupporter {
    String id, name, amount, time;

    public FundraisongdonationSupporter(String id, String name, String amount, String time) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
