package com.mhj.longlivemypet;

public class PetCalendarItem {
    String email, title, body, write_date;

    public PetCalendarItem(){
    }

    public PetCalendarItem(String email, String title, String body, String write_date) {
        this.email = email;
        this.title = title;
        this.body = body;
        this.write_date = write_date;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getWrite_date() {
        return write_date;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setWrite_date(String write_date) {
        this.write_date = write_date;
    }
}