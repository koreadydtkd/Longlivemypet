package com.mhj.longlivemypet;

public class CommunityDetailItem {
    String nick, commnet;
    long date;

    public CommunityDetailItem() {
    }

    public CommunityDetailItem(String nick, String commnet, long date) {
        this.nick = nick;
        this.commnet = commnet;
        this.date = date;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getCommnet() {
        return commnet;
    }

    public void setCommnet(String commnet) {
        this.commnet = commnet;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
