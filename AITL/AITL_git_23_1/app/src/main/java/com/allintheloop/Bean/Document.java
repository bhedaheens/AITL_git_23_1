package com.allintheloop.Bean;

/**
 * Created by nteam on 14/6/16.
 */
public class Document {
    String doc_id, doc_type, doc_title, doc_file, docicon, count;

    public Document(String doc_id, String doc_type, String doc_title, String docicon, String count) {
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.doc_title = doc_title;
        this.docicon = docicon;
        this.count = count;
    }

    public Document(String doc_id, String doc_type, String doc_title, String doc_file, String docicon, String count) {
        this.doc_id = doc_id;
        this.doc_type = doc_type;
        this.doc_title = doc_title;
        this.doc_file = doc_file;
        this.docicon = docicon;
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDocicon() {
        return docicon;
    }

    public void setDocicon(String docicon) {
        this.docicon = docicon;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public String getDoc_title() {
        return doc_title;
    }

    public void setDoc_title(String doc_title) {
        this.doc_title = doc_title;
    }

    public String getDoc_file() {
        return doc_file;
    }

    public void setDoc_file(String doc_file) {
        this.doc_file = doc_file;
    }
}
