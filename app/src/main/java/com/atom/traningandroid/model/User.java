package com.atom.traningandroid.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public final class User implements Serializable {
    @SerializedName("userId")
    private String userId;
    @SerializedName("password")
    private String password;
    @SerializedName("familyName")
    private String familyName;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("genderId")
    private Integer genderId;
    @SerializedName("genderName")
    private String genderName;
    @SerializedName("age")
    private Integer age;
    @SerializedName("authorityId")
    private Integer authorityId;
    @SerializedName("roleName")
    private String roleName;
    @SerializedName("admin")
    private Integer admin;
    @SerializedName("enabled")
    private Integer enabled;

    public User() {
    }

    public User(String name, Integer authorityId) {
        this.familyName = name;
        this.firstName = name;
        this.authorityId = authorityId;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", familyName='" + familyName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", genderId=" + genderId +
                ", genderName='" + genderName + '\'' +
                ", age=" + age +
                ", authorityId=" + authorityId +
                ", roleName='" + roleName + '\'' +
                ", admin=" + admin +
                ", enabled=" + enabled +
                '}';
    }
}
