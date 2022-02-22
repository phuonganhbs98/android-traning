package com.atom.traningandroid.converter;

import com.atom.traningandroid.model.Role;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoleConverter {
    public static List<Role> convertToRoleList(JSONArray arr){
        if(arr== null || arr.length() == 0) return null;
        List<Role> roles = new ArrayList<>();
        for(int i=0; i< arr.length(); i++){
            try {
                roles.add(convertToRole(arr.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return roles;
    }

    public static Role convertToRole(JSONObject obj){
        Role r = new Role();
        try {
            r.setAuthorityId(obj.has("authorityId")?obj.getInt("authorityId"):null);
            r.setRoleName(obj.has("authorityName")?obj.getString("authorityName"):null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return r;
    }
}
