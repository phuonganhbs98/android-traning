package com.atom.traningandroid.model;

import com.google.gson.annotations.SerializedName;

public final class Statistic {
    @SerializedName("authorityId")
    private String authorityId;
    @SerializedName("authorityName")
    private String authorityName;
    @SerializedName("genderId")
    private String genderId;
    @SerializedName("genderName")
    private String genderName;
    @SerializedName("totalFemale")
    private String totalFemale;
    @SerializedName("totalMale")
    private String totalMale;
    @SerializedName("totalUnknown")
    private String totalUnknown;
    @SerializedName("totalAgeSmaller19")
    private String totalAgeSmaller19;
    @SerializedName("totalAgeGreater20")
    private String totalAgeGreater20;
    @SerializedName("totalUnknownAge")
    private String totalUnknownAge;

    public Statistic() {
    }

    public Statistic(Integer authorityId, String authorityName, String totalFemale, String totalMale, String totalUnknown) {
        this.authorityName = authorityName;
        this.totalFemale = totalFemale;
        this.totalMale = totalMale;
        this.totalUnknown = totalUnknown;
    }

    public Statistic(String authorityName, String totalAgeSmaller19, String totalAgeGreater20, String totalUnknownAge) {
        this.authorityName = authorityName;
        this.totalAgeSmaller19 = totalAgeSmaller19;
        this.totalAgeGreater20 = totalAgeGreater20;
        this.totalUnknownAge = totalUnknownAge;
    }

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getGenderId() {
        return genderId;
    }

    public void setGenderId(String genderId) {
        this.genderId = genderId;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getTotalFemale() {
        return totalFemale;
    }

    public void setTotalFemale(String totalFemale) {
        this.totalFemale = totalFemale;
    }

    public String getTotalMale() {
        return totalMale;
    }

    public void setTotalMale(String totalMale) {
        this.totalMale = totalMale;
    }

    public String getTotalUnknown() {
        return totalUnknown;
    }

    public void setTotalUnknown(String totalUnknown) {
        this.totalUnknown = totalUnknown;
    }

    public String getTotalAgeSmaller19() {
        return totalAgeSmaller19;
    }

    public void setTotalAgeSmaller19(String totalAgeSmaller19) {
        this.totalAgeSmaller19 = totalAgeSmaller19;
    }

    public String getTotalAgeGreater20() {
        return totalAgeGreater20;
    }

    public void setTotalAgeGreater20(String totalAgeGreater20) {
        this.totalAgeGreater20 = totalAgeGreater20;
    }

    public String getTotalUnknownAge() {
        return totalUnknownAge;
    }

    public void setTotalUnknownAge(String totalUnknownAge) {
        this.totalUnknownAge = totalUnknownAge;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "authorityId=" + authorityId +
                ", authorityName='" + authorityName + '\'' +
                ", genderId=" + genderId +
                ", genderName='" + genderName + '\'' +
                ", totalFemale=" + totalFemale +
                ", totalMale=" + totalMale +
                ", totalUnknown=" + totalUnknown +
                ", totalAgeSmaller19=" + totalAgeSmaller19 +
                ", totalAgeGreater20=" + totalAgeGreater20 +
                ", totalUnknownAge=" + totalUnknownAge +
                '}';
    }
}
