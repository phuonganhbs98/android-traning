package com.atom.traningandroid.entity;

public class Role {
    private Integer authorityId;
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
