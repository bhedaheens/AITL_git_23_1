package com.allintheloop.Bean.Speaker;

/**
 * Created by Aiyaz on 5/5/17.
 */

public class SpeakerDocumentListing {
    String id, title, document_file;

    public SpeakerDocumentListing(String id, String title, String document_file) {
        this.id = id;
        this.title = title;
        this.document_file = document_file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocument_file() {
        return document_file;
    }

    public void setDocument_file(String document_file) {
        this.document_file = document_file;
    }
}
