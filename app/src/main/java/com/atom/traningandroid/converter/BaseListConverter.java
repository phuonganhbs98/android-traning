package com.atom.traningandroid.converter;

import com.atom.traningandroid.entity.Gender;
import com.atom.traningandroid.entity.Role;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListConverter<T> {
    public List<T> convertToList(JSONArray arr){
        if(arr== null || arr.length() == 0) return null;
        List<T> arrayList = new ArrayList<>();
        for(int i=0; i< arr.length(); i++){
            try {
                arrayList.add(convertToEntity(arr.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public abstract T convertToEntity(JSONObject obj);
}
