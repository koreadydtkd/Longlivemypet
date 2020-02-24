package com.mhj.longlivemypet;

public class AskItem {
    String email, title, question;
    long date;

    public AskItem(){}

    public AskItem(String title, String question, long date) {
        this.title = title;
        this.question = question;
        this.date = date;
    }

    public AskItem(String email, String title, String question, long date) {
        this.email = email;
        this.title = title;
        this.question = question;
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
