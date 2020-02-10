package com.mhj.longlivemypet;

public class CommunityItem {
    String title;
    String content;
    String classification;
    String nick;
    long date;
    int commentCount;

    public CommunityItem() {
    }

    public CommunityItem(String title, String content, String classification, String nick, long date, int commentCount) {
        this.title = title;
        this.content = content;
        this.classification = classification;
        this.nick = nick;
        this.date = date;
        this.commentCount = commentCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    class Comment{

    }
}
