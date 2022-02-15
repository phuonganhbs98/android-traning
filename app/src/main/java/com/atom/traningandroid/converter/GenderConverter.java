package com.atom.traningandroid.converter;

import com.atom.traningandroid.entity.Gender;

import org.json.JSONException;
import org.json.JSONObject;

public class GenderConverter extends BaseListConverter<Gender>{

    @Override
    public Gender convertToEntity(JSONObject obj) {
        Gender g = new Gender();
        try {
            g.setGenderId(obj.has("genderId")?obj.getInt("genderId"):null);
            g.setGenderName(obj.has("genderName")?obj.getString("genderName"):null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return g;
    }
}
