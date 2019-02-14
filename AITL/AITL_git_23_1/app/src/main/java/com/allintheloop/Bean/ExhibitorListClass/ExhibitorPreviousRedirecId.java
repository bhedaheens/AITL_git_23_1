package com.allintheloop.Bean.ExhibitorListClass;

/**
 * Created by nteam on 15/9/17.
 */

public class ExhibitorPreviousRedirecId {
    String previousRedirectId;
    int pos;

    public ExhibitorPreviousRedirecId(String previousRedirectId, int pos) {
        this.previousRedirectId = previousRedirectId;
        this.pos = pos;
    }

    public String getPreviousRedirectId() {
        return previousRedirectId;
    }

    public void setPreviousRedirectId(String previousRedirectId) {
        this.previousRedirectId = previousRedirectId;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
