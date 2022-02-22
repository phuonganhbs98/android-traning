package com.atom.traningandroid.model;

import com.google.gson.annotations.SerializedName;

public final class Gender {
    @SerializedName("genderId")
    private Integer genderId;

    @SerializedName("genderName")
    private String genderName;

    public Gender() {
    }

    public Gender(Integer genderId, String genderName) {
        this.genderId = genderId;
        this.genderName = genderName;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    @Override
    public String toString() {
        return genderName;
    }
}
