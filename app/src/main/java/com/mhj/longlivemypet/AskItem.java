package com.mhj.longlivemypet;

public class AskItem {
    String email, nick, title, question, date;

    public AskItem(){}

    public  AskItem(String email, String nick, String title, String question, String date){
        this.email = email;
        this.nick = nick;
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
