package com.atom.traningandroid.utils;

public class TokenUtils {
    private static TokenUtils instance=null;
    private String token=null;

    public static TokenUtils getInstance() {
        if (instance == null)
            instance = new TokenUtils();
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = "Bearer "+token;
    }
}
