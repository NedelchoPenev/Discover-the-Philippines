package com.silenci0.philippines.domain.models.service;

public class RoleServiceModel extends BaseServiceModel{
    private String authority;

    public RoleServiceModel() {
    }

    public RoleServiceModel(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
