package com.atom.traningandroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public final class UserList {
    @SerializedName("user")
    private List<User> users;

    public UserList() {
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
