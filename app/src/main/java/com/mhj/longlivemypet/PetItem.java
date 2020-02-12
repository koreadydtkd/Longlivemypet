package com.mhj.longlivemypet;

public class PetItem {

    String name;
    String sex;
    String breed;
    String date;
    String weight;
    String email;


    public PetItem() {

    }

    public PetItem(String name, String sex, String breed, String date, String weight, String email) {
        this.name = name;
        this.sex = sex;
        this.breed = breed;
        this.date = date;
        this.weight = weight;

        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


