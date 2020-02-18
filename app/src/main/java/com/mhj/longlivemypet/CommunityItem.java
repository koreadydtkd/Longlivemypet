package com.mhj.longlivemypet;

import java.util.Map;

public class CommunityItem {
    String classification, title, content, nick, imgURL;
    int commentCount, likeCount;
    long date;
    Map<String, Boolean> likeUser;

    public CommunityItem() {
    }

    public CommunityItem(String classification, String title, String content, String nick, String imgURL, int commentCount, int likeCount, long date, Map<String, Boolean> likeUser) {
        this.classification = classification;
        this.title = title;
        this.content = content;
        this.nick = nick;
        this.imgURL = imgURL;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.date = date;
        this.likeUser = likeUser;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Map<String, Boolean> getLikeUser() {
        return likeUser;
    }

    public void setLikeUser(Map<String, Boolean> likeUser) {
        this.likeUser = likeUser;
    }
}
