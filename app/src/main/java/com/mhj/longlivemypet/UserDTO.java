package com.mhj.longlivemypet;

public class UserDTO {
    String email, nick, name, birth, phone;
    boolean sex;

    public UserDTO() {
    }

    public UserDTO(String email, String nick, String name, String birth, String phone, boolean sex) {
        this.email = email;
        this.nick = nick;
        this.name = name;
        this.birth = birth;
        this.phone = phone;
        this.sex = sex;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}
