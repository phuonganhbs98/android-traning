package com.atom.traningandroid.model;

import com.google.gson.annotations.SerializedName;

public final class Role {
    @SerializedName("authorityId")
    private Integer authorityId;

    @SerializedName("authorityName")
    private String roleName;

    public Role() {

    }

    public Role(Integer authorityId, String roleName) {
        this.authorityId = authorityId;
        this.roleName = roleName;
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

    @Override
    public String toString() {
        return roleName;
    }
}
