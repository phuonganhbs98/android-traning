package com.atom.traningandroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class RoleList {
    @SerializedName("role")
    private List<Role> roles;

    public RoleList() {
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
