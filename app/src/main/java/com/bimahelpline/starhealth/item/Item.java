package com.bimahelpline.starhealth.item;


public class Item {
    int id;
    String title;
    String body;
    long time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTime(){
        return time;
    }

    public void setTime(long time){
        this.time = time;
    }
}
