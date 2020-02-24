package com.mhj.longlivemypet;

public class AskDTO {
    String title, question;
    long date;

    public AskDTO() {
    }

    public AskDTO(String title, String question, long date) {
        this.title = title;
        this.question = question;
        this.date = date;
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
