package com.atom.traningandroid.converter;

import com.atom.traningandroid.model.Statistic;

import org.json.JSONException;
import org.json.JSONObject;

public class StatisticConverter extends BaseListConverter<Statistic>{

    @Override
    public Statistic convertToEntity(JSONObject obj) {
        Statistic s = new Statistic();
        try {
            s.setAuthorityId(obj.has("authorityId")?obj.getString("authorityId"):null);
            s.setAuthorityName(obj.has("authorityName")?obj.getString("authorityName"):null);
            s.setTotalFemale(obj.has("totalFemale")?obj.getString("totalFemale"):null);
            s.setTotalMale(obj.has("totalMale")?obj.getString("totalMale"):null);
            s.setTotalUnknown(obj.has("totalUnknown")?obj.getString("totalUnknown"):null);
            s.setTotalAgeSmaller19(obj.has("totalAgeSmaller19")?obj.getString("totalAgeSmaller19"):null);
            s.setTotalAgeGreater20(obj.has("totalAgeGreater20")?obj.getString("totalAgeGreater20"):null);
            s.setTotalUnknownAge(obj.has("totalUnknownAge")?obj.getString("totalUnknownAge"):null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return s;
    }
}
