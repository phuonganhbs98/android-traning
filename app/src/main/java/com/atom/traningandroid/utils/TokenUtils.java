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
        this.token = token==null?null:("Bearer "+token);
    }
//    eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYTEyMyIsImp0aSI6IjlhNTIxODVlLTg4NzYtNGY1Yy1hMmQzLWFjNGFmNDdiZGQ5NiIsImlhdCI6MTY0NTQzMzQ4NiwiaXNzIjoiUEEiLCJleHAiOjE2NDU0MzUyODZ9.HO1dPncEBV8n0Tu57kygD0-xJQN7Lp6RYAXr-_kW49I
}
