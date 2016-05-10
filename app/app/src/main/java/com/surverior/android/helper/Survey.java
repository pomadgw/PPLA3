package com.surverior.android.helper;

import java.util.ArrayList;

/**
 * Created by Bardan Putra Prananto on 5/1/2016.
 */
public class Survey {
    private String name;
    private String description;
    private String gender;
    private int ageFrom;
    private int ageTo;
    private String job;
    private String province;
    private String city;
    public ArrayList<Question> questions = new ArrayList<>();

    public Survey(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Survey(String name, String description, String gender, int ageFrom, int ageTo, String job, String province, String city) {
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.ageFrom = ageFrom;
        this.ageTo = ageTo;
        this.job = job;
        this.province = province;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(int ageFrom) {
        this.ageFrom = ageFrom;
    }

    public int getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(int ageTo) {
        this.ageTo = ageTo;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void updateAttribute(String name, String description, String gender, int ageFrom, int ageTo, String job, String province, String city){
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.ageFrom = ageFrom;
        this.ageTo = ageTo;
        this.job = job;
        this.province = province;
        this.city = city;
    }
}
