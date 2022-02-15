package com.atom.traningandroid.converter;

import com.atom.traningandroid.entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {
    public static List<User> convertToUserList(JSONArray arr){
        List<User> users = new ArrayList<>();
        for(int i=0; i< arr.length(); i++){
            try {
                users.add(convertToUser(arr.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public static User convertToUser(JSONObject obj){
        User u = new User();
        try {
            u.setUserId(obj.getString("userId"));
            u.setPassword(obj.getString("password"));
            u.setFamilyName(obj.getString("familyName"));
            u.setFirstName(obj.getString("firstName"));
            u.setGenderId(obj.has("genderId")?obj.getInt("genderId"):null);
            u.setGenderName(obj.has("genderName")?obj.getString("genderName"):null);
            u.setAge(obj.has("age")?obj.getInt("age"):null);
            u.setAuthorityId(obj.has("authorityId")?obj.getInt("authorityId"):null);
            u.setRoleName(obj.has("roleName")?obj.getString("roleName"):null);
            u.setAdmin(obj.getInt("admin"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }
}
