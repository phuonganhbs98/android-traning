package com.atom.traningandroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class GenderList {
    @SerializedName("gender")
    private List<Gender> genders;

    public GenderList() {
    }

    public List<Gender> getGenders() {
        return genders;
    }

    public void setGenders(List<Gender> genders) {
        this.genders = genders;
    }
}
