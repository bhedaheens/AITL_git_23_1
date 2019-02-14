package com.allintheloop.Bean.ExhibitorListClass;

/**
 * Created by nteam on 8/9/17.
 */

public class ExhibitorLeadExtraColumnData {
    String Que, Ans;

    public ExhibitorLeadExtraColumnData(String que, String ans) {
        this.Que = que;
        this.Ans = ans;
    }

    public String getQue() {
        return Que;
    }

    public void setQue(String que) {
        Que = que;
    }

    public String getAns() {
        return Ans;
    }

    public void setAns(String ans) {
        Ans = ans;
    }
}
